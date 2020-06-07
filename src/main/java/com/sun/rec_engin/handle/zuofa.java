package com.sun.rec_engin.handle;

import com.sun.dao.DisDAO;
import com.sun.dao.LikeDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.*;
import org.ansj.recognition.impl.StopRecognition;

import java.util.*;

public class zuofa {
    static SimDAO simDao=new SimDAO();
    static DisDAO disDao=new DisDAO();
    static ArrayList<ArrayList<String>> docs=new ArrayList<ArrayList<String>>();
    static Set<String> expectedNature = new HashSet<String>() {{
        add("Ag");add("a");add("ad");add("an");add("Bg");add("b");add("d");add("i");
        add("j");add("n");add("nr");add("ns");add("nt");add("nx");add("nz");add("v");
        add("vd");add("vn");
    }};
    /*
    用来存储,所有的菜谱做法的,分词结果
     */
    static Map<Integer,ArrayList<String>> map=new HashMap<Integer, ArrayList<String>>();
    /*
    用来存储所有菜谱的做法的,分词的,tf-idf值
     */
    public static Map<Integer,Map<String,Double>> tfidfmap=new HashMap<Integer, Map<String, Double>>();
    static {

        StopRecognition filter=null;
        ArrayList<String> res=new ArrayList<String>();
        split sw=new split();

        /*
        获取全部文档的分词
         */
        res=simDao.getColumn("做法");
        for (int i = 0; i < res.size(); i++) {
            ArrayList<String> document=new ArrayList<String>();
            /*
            如果得到的为空,就给存入一个{null},也就是仅包含null的动态数组
             */
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
            res=sw.getFilterWord(simDao.getColumnWithId("做法",userLikeList.get(i)),expectedNature);
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
    权重全部为1
     */
    public static Map<String,double[]> getVector1(ArrayList<String> str,int type){
        int flag=1;
        if(type==1){
            flag=0;
        }
        Map<String, double[]> vectorSpace = new HashMap<String, double[]>();
        double[] itemCountArray = null;

        //动态数组转为数组
        double weight=1;//设计一个权重值，原料中出现的第一项为10，然后逐渐-1（这个值需要凭感觉调优）
        double wei_reduce=1;//权重下降的值，默认为1
        double wei_value=weight;//这是循环中实际用到的值

        int size=str.size();
        String[] strArray = (String[])str.toArray(new String[size]);
        for(int i=0; i<strArray.length; ++i)
        {
            if (wei_value<=1){//小于1的话都设为1
                wei_value=1;
            }
            else wei_value=weight-i*wei_reduce;

            if(vectorSpace.containsKey(strArray[i]))
                vectorSpace.get(strArray[i])[type]+=wei_value;
            else
            {
                itemCountArray = new double[2];
                itemCountArray[type] = wei_value;
                itemCountArray[flag] = 0;
                vectorSpace.put(strArray[i], itemCountArray);
            }
        }

        return vectorSpace;

    }



    /*
    存入向量空间计算相似度,这个是要求两个向量空间的key完全一致
    依靠tf-idf给权重,效率低的可怕直接忽略
    但是可以离线推荐用,只计算菜谱间的相似度,即只执行一次就缓存下来数据供以后查询.
    菜谱间相似度用来,评价推荐结果多样性.
     */
    public static Map<Integer,Double> getSimList1(int id){
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
                    System.out.println(res);
                    vec1=mix.mapRemove(vec1,vec2);
                }
            }
        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

        return simList;
    }
    //纯靠词频(即权重为1)计算相似度,不依靠tf-idf值
    public static Map<Integer,Double> getSimList(int id){
        long startTime = System.currentTimeMillis();   //获取开始时间


        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值

        LikeDAO likedao=new LikeDAO();
        ArrayList<Integer> userLikeList=likedao.queryLike(id);
        map_mix mix=new map_mix();
        similarity sim=new similarity();

        ArrayList<String> doc=getUserWord(id);
        Map<String, double[]> vec1=getVector1(doc,0);//用户的向量空间

        for (Map.Entry<Integer,ArrayList<String>> entry: map.entrySet()) {//entry0的形式为{966,{"皮蛋":1.973,"瘦肉":....}}

//            if(entry.getKey()!=2894)continue;
            if(!(userLikeList.contains(entry.getKey()))){//去掉已经标记过喜欢的菜谱。
                Map<String, double[]> vecSpa = new HashMap<String, double[]>();
                /*
                如果含有null,说明这一行为空,不处理
                 */
                ArrayList<String> value=entry.getValue();
                if(value.contains(null))
                {
                    simList.put(entry.getKey(),0.0);
                    continue;
                }

                Map<String, double[]> vec2=getVector1(value,1);//得到菜谱的
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
    public static List<Map.Entry<Integer,Double>> countSim(Map<Integer,Double> simList){
        return sort.sortRes(simList);
    }
}
