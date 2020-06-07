package com.sun.rec_engin.handle;

import com.sun.dao.DisDAO;
import com.sun.dao.LikeDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.map_mix;
import com.sun.rec_engin.similarity;
import org.ansj.recognition.impl.StopRecognition;

import java.util.*;

public class yuanliao {
    /*
    仅做对原料列的处理
     */
    public Map<Integer,Double> getSimList(int id){
        long startTime = System.currentTimeMillis();   //获取开始时间

        DisDAO disDao=new DisDAO();
        SimDAO simdao=new SimDAO();
        similarity sim=new similarity();
        LikeDAO likedao=new LikeDAO();
        map_mix mix=new map_mix();
        String queue="原料";//需要的列的名称
        /*
        需要的词性
         */
//        Set<String> expectedNature = new HashSet<String>() {{
//            add("n");add("v");add("nw");
//        }};
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("nr");add("ns");add("nz");add("nl");add("ng");add("nw");
        }};
        /*
        原料列需要过滤器的话就加
         */
        StopRecognition filter = new StopRecognition();//实用化过滤器,添加过滤词
        filter.insertStopWords("主");
        filter.insertStopWords("料");
        filter.insertStopWords("配料");


        ArrayList<Integer> userLikeList=likedao.queryLike(id);

        Map<String, double[]> vec1=simdao.getUserVector(id,queue,expectedNature,filter);
        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值

        for (int i = 0; i < DisDAO.idlist.size(); i++) {//在这里修改总共对比的条数
            if(!(userLikeList.contains(disDao.idlist.get(i)))){//去掉已经标记过喜欢的菜谱。
                Map<String, double[]> vecSpa = new HashMap<String, double[]>();
                /*
                大坑，不能直接用=。默认是浅拷贝指向统一引用。

                怎么折腾都指向一个引用
                所以在后面把加入向量空间的再减去了，只能这样了。
                 */
                Map<String, double[]> vec2=simdao.getRecipeVector(i,queue,expectedNature,filter);
                vecSpa=mix.mapMix(vec1,vec2);
                double res=sim.countCos(vecSpa);
                if(!Double.isNaN(res)) {
                    simList.put((Integer) disDao.idlist.get(i),res);
                }
                else simList.put((Integer) disDao.idlist.get(i),0.0);
                System.out.println(res);
                vec1=mix.mapRemove(vec1,vec2);
            }
        }
//        /*
//        对map进行排序取前十
//        */
//        List<Map.Entry<Integer,Double>> infoIds = new ArrayList<Map.Entry<Integer,Double>>(simList.entrySet());
//        //对value进行排序
////        Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
////            @Override
////            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
////                return (o2.getValue()).toString().compareTo(o1.getValue().toString());
////            }
////
////        });
//        Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
//            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
//                if ((o2.getValue() - o1.getValue()) > 0)
//                    return 1;
//                else if ((o2.getValue() - o1.getValue()) == 0)
//                    return 0;
//                else
//                    return -1;
//            }
//        });

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
