package com.sun.rec_engin.handle;

import com.sun.dao.DisDAO;
import com.sun.dao.LikeDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.*;
import org.ansj.recognition.impl.StopRecognition;

import java.util.*;

/**
 * 用来处理"菜谱名称"列
 * 方法是先将名称分词,并通过tf-idf计算权重,如"糖醋排骨",""
 * 结果是{"糖" -> "1.5426173900993556","排骨" -> "1.536144694813655","醋" -> "1.4997449308253243"}
 *
 * 将tf-idf的值作为权重,再计算
 */
public class mingcheng {
    static SimDAO simDao=new SimDAO();
    static DisDAO disDao=new DisDAO();
    static ArrayList<ArrayList<String>> docs=new ArrayList<ArrayList<String>>();
    static Set<String> expectedNature = new HashSet<String>() {{
        add("Ag");add("a");add("ad");add("an");add("Bg");add("b");add("d");add("i");
        add("j");add("n");add("nr");add("ns");add("nt");add("nx");add("nz");add("v");
        add("vd");add("vn");
    }};
    /*
    用来存储,所有的菜谱名字的,分词结果
     */
    static Map<Integer,ArrayList<String>> map=new HashMap<Integer, ArrayList<String>>();
    /*
    用来存储所有菜谱的名字的,分词的,tf-idf值
     */
    public static Map<Integer,Map<String,Double>> tfidfmap=new HashMap<Integer, Map<String, Double>>();
    static {

        StopRecognition filter=null;
        ArrayList<String> res=new ArrayList<String>();
        split sw=new split();

        /*
        获取全部文档的分词
         */
        res=simDao.getColumn("菜谱名称");
        for (int i = 0; i < res.size(); i++) {
            ArrayList<String> document=new ArrayList<String>();
            if(res.get(i)!=null){
                document=sw.getFilterWord(res.get(i),expectedNature);
            }else document.add(null);
            map.put(Integer.parseInt(disDao.idlist.get(i).toString()),document);
            docs.add(document);
        }

    }


    public static Map<String,Double> getTFIDF(ArrayList<String> doc){//传doc进去,计算与全部docs的tf-idf
        TFIDFCalculator tfidfcalculator=new TFIDFCalculator();
        Map<String,Double> tfidfres=new HashMap<String, Double>();
        for (int i = 0; i < doc.size(); i++) {
            double tfidf=tfidfcalculator.tfIdf(doc,docs,doc.get(i));
            tfidfres.put(doc.get(i),tfidf);
        }
        return tfidfres;
    }
    public static ArrayList<String> getUserWord(int id){//得到用户的分词结果
        ArrayList<String> res=new ArrayList<String>();
        split sw=new split();
        LikeDAO likedao=new LikeDAO();
        ArrayList<Integer> userLikeList=likedao.queryLike(id);
        ArrayList<String> doc=new ArrayList<String>();
        for (int i = 0; i < userLikeList.size(); i++) {
            res=sw.getFilterWord(simDao.getColumnWithId("菜谱名称",userLikeList.get(i)),expectedNature);
            for (int j = 0; j < res.size(); j++) {
                doc.add(res.get(j));
            }
        }
        return doc;
    }
    public static Map<String,Double> getUserTFIDF(int id){//得到用户的词的tfidf结果
        ArrayList<String> doc=new ArrayList<String>();
        doc=getUserWord(id);
        return getTFIDF(doc);
    }
    public static void getRecipeTFIDF(){//得到所有菜谱的tf idf数据
        ArrayList<String> doc=new ArrayList<String>();
        for(Map.Entry<Integer,ArrayList<String>> entry: map.entrySet()){
            doc=entry.getValue();
            tfidfmap.put(entry.getKey(),getTFIDF(doc));
        }
    }


    /*
    这个方法是将,向量,添加到向量空间中
    type为0是添加用户的,即第一列
    1是添加菜谱的,即第二列
     */
    public static Map<String, double[]> getVector(Map<String,Double> TFIDF,int type){
        Map<String, double[]> vectorSpace = new HashMap<String, double[]>();//向量空间{"皮蛋":[1.973,1.792];"瘦肉":[....}
        double[] itemCountArray = null;//后面的权重
        /*
        添加用户的数据到向量空间
         */
        if(type==0){
            for(Map.Entry<String,Double> entry:TFIDF.entrySet()){
                double[] value=vectorSpace.get(entry.getKey());
                if(value!=null){
                    value[0]+=entry.getValue();
                }
                else{
                    itemCountArray = new double[2];
                    itemCountArray[1]=0;
                    itemCountArray[0]=entry.getValue();
                    vectorSpace.put(entry.getKey(),itemCountArray);
                }
            }
        }
        /*
        添加菜谱的
         */
        else{
            for (Map.Entry<String,Double> entry:TFIDF.entrySet()) {//entry1的形式{"皮蛋":1.973,"瘦肉":"1.889"....}
                double[] value=vectorSpace.get(entry.getKey());
                if(value!=null){//相当于containsKey()
                    value[1]+=entry.getValue();
                }else{
                    itemCountArray = new double[2];
                    itemCountArray[0]=0;
                    itemCountArray[1]=entry.getValue();
                    vectorSpace.put(entry.getKey(),itemCountArray);
                }
            }
        }

        return vectorSpace;

    }
    /*
    假设"牛肉汤"和"羊肉汤",传入的是{"牛肉":1.0,"汤":0.8}{"羊肉":1.1,"粥":0.7},
    其中相似度[牛肉--羊肉]0.8,[牛肉--粥]0.2,[汤--羊肉]0.2,[汤--粥]0.7
    vec1中添加"羊肉",value=max(羊肉*(羊肉--牛肉),羊肉*(羊肉--汤))=0.88....
    {"牛肉":1.0,"汤":0.8,"羊肉":0.88...}

    value1=max(value2*sim(key2,key1))
    vec1.put(key2,value1)

    Collections.max(Arrays.asList(numbers))//获取数组中最大值
     */
    public static Map<String,Double> addSimWord(Map<String,Double> vec1,Map<String,Double> vec2){
        for(Map.Entry<String,Double> entry2:vec2.entrySet()){
            Double value1=vec1.get(entry2.getKey());
            if(value1!=null){
                vec1.put(entry2.getKey(),(value1+=entry2.getValue()));
            }else{
                ArrayList<Double> list=new ArrayList<Double>();
                Double sim=null;
                for(Map.Entry<String,Double> entry1:vec1.entrySet()){
                    sim=WordSimilarity.getSimilarity(entry1.getKey(),entry2.getKey());
                    list.add((entry2.getValue()*sim));
                }
                Double max=Collections.max(list);
                vec1.put(entry2.getKey(),max);
            }

        }
        return vec1;
    }
    /*
    给用户的vector添加同义词,type==0
    给菜谱的vector添加同义词,type==1
    性能很差,比较一条大概需要一秒的时间,主要开销都在查询
     */
    public static Map<String,double[]> addSimWordToVec(Map<String,double[]> vec1,Map<String,double[]> vec2,int type){
        Map<String,double[]> vecplus=new HashMap<String, double[]>();
        int flag=1;
        if(type==1){
            flag=0;
        }
        vecplus.putAll(vec1);
        for(Map.Entry<String,double[]> entry2:vec2.entrySet()){
            double[] itemCountArray = vec1.get(entry2.getKey());

            if(itemCountArray!=null){
                itemCountArray[type]+=entry2.getValue()[flag];
                itemCountArray[flag]=0;
                vecplus.put(entry2.getKey(),itemCountArray);
            }else{
                ArrayList<Double> list=new ArrayList<Double>();
                double sim=0.0;
                for(Map.Entry<String,double[]> entry1:vec1.entrySet()){
                    sim=WordSimilarity.getSimilarity(entry1.getKey(),entry2.getKey());
                    list.add(((entry2.getValue()[flag])*sim));
                }
                Double max=Collections.max(list);
                double[] value=new double[2];
                if(max!=null){
                    value[type]=max;
                }else value[type]=0;
                value[flag]=0;
                vecplus.put(entry2.getKey(),value);
            }
        }
        return vecplus;
    }


    /*
    存入向量空间计算相似度,这个是要求两个向量空间的key完全一致
     */
    public static Map<Integer,Double> getSimList(int id){
        long startTime = System.currentTimeMillis();   //获取开始时间


        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值
        Map<String,Double> userTFIDF=getUserTFIDF(id);//存放用户的词的,tfidf结果.形式{"皮蛋":1.973,"瘦肉":"1.889"....}
        getRecipeTFIDF();
        LikeDAO likedao=new LikeDAO();
        ArrayList<Integer> userLikeList=likedao.queryLike(id);
        map_mix mix=new map_mix();
        similarity sim=new similarity();


        Map<String, double[]> vec1=getVector(userTFIDF,0);//用户的向量空间

        for (Map.Entry<Integer,Map<String,Double>> entry: tfidfmap.entrySet()) {//entry0的形式为{966,{"皮蛋":1.973,"瘦肉":....}}
            if(!(userLikeList.contains(entry.getKey()))){//去掉已经标记过喜欢的菜谱。
                Map<String, double[]> vecSpa = new HashMap<String, double[]>();
                /*
                大坑，不能直接用=。默认是浅拷贝指向统一引用。

                怎么折腾都指向一个引用
                所以在后面把加入向量空间的再减去了，只能这样了。
                 */
                Map<String, double[]> vec2=getVector(entry.getValue(),1);//得到菜谱的
                if(vec2.isEmpty()){
                    simList.put(entry.getKey(),0.0);
                }else{
                    vecSpa=mix.mapMix(vec1,vec2);
                    double res=sim.countCos(vecSpa);
                    if(!Double.isNaN(res)) {
                        simList.put(entry.getKey(),res);
                    }
                    else simList.put(entry.getKey(),0.0);
                    System.out.println(res);
                    vec1=mix.mapRemove(vec1,vec2);
                }
            }
        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

        return simList;
    }

    /*
    countSim1与上面的区别是,这个添加了同义词的权重
     */
    public static  Map<Integer,Double>  getSimList1(int id){
        long startTime = System.currentTimeMillis();   //获取开始时间
        System.out.println("开始计算菜谱名称相似度");

        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值
        Map<String,Double> userTFIDF=getUserTFIDF(id);//存放用户的词的,tfidf结果.形式{"皮蛋":1.973,"瘦肉":"1.889"....}
        getRecipeTFIDF();
        LikeDAO likedao=new LikeDAO();
        ArrayList<Integer> userLikeList=likedao.queryLike(id);
        map_mix mix=new map_mix();
        similarity sim=new similarity();


        Map<String, double[]> vec1=getVector(userTFIDF,0);//用户的向量空间

//        tfidfmap.clear();
//        Map<String,Double> mapv=new HashMap<String, Double>();
//        tfidfmap.put(1047,mapv);

        for (Map.Entry<Integer,Map<String,Double>> entry: tfidfmap.entrySet()) {//entry0的形式为{966,{"皮蛋":1.973,"瘦肉":....}}
            long startTime1 = System.currentTimeMillis();   //获取开始时间

            if(!(userLikeList.contains(entry.getKey()))){//去掉已经标记过喜欢的菜谱。
                Map<String, double[]> vecSpa = new HashMap<String, double[]>();
                /*
                大坑，不能直接用=。默认是浅拷贝指向统一引用。

                怎么折腾都指向一个引用
                所以在后面把加入向量空间的再减去了，只能这样了。
                 */
                Map<String, double[]> vec2=getVector(entry.getValue(),1);//得到菜谱的

                Map<String,double[]> vec1plus=new HashMap<String, double[]>();
                Map<String,double[]> vec2plus=new HashMap<String, double[]>();
                if(vec2.isEmpty()){
                    simList.put(entry.getKey(),0.0);
                }else{
                    vec1plus=mingcheng.addSimWordToVec(vec1,vec2,0);
                    vec2plus=mingcheng.addSimWordToVec(vec2,vec1,1);


                    vecSpa=mix.mapMix(vec1plus,vec2plus);
                    double res=sim.countCos(vecSpa);
                    if(!Double.isNaN(res)) {
                        simList.put(entry.getKey(),res);
                    }
                    System.out.println(res);
                    vec1=mix.mapRemove(vec1plus,vec2plus);
                }
            }
            long endTime1=System.currentTimeMillis(); //获取结束时间
            System.out.println("计算id="+entry.getKey()+"用了:"+(endTime1-startTime1)+"ms");
        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

        return simList;
    }
    /*
    排序单独写一个函数调用
     */
    public static List<Map.Entry<Integer,Double>> countSim(Map<Integer,Double> simList){
        return sort.sortRes(simList);
    }
}
