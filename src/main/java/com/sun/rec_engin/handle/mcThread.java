package com.sun.rec_engin.handle;

import com.sun.rec_engin.map_mix;
import com.sun.rec_engin.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class mcThread implements Callable {

    private ArrayList<Integer> userLikeList;
    private Map<String, double[]> vec1;
    private Map.Entry<Integer, Map<String, Double>> entry;


    public mcThread(ArrayList<Integer> userLikeList,Map<String, double[]> vec1,Map.Entry<Integer, Map<String, Double>> entry){
        this.userLikeList=userLikeList;
        this.vec1=vec1;
        this.entry=entry;
    }

    public Object call() throws Exception {

        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值
        map_mix mix=new map_mix();
        similarity sim=new similarity();

        System.out.println("这是id="+entry.getKey()+"菜谱");
        long startTime = System.currentTimeMillis();   //获取开始时间

        if (!(userLikeList.contains(entry.getKey()))) {//去掉已经标记过喜欢的菜谱。
            Map<String, double[]> vecSpa = new HashMap<String, double[]>();

            Map<String, double[]> vec2 = mingcheng.getVector(entry.getValue(), 1);//得到菜谱的

            Map<String, double[]> vec1plus = new HashMap<String, double[]>();
            Map<String, double[]> vec2plus = new HashMap<String, double[]>();
            if (vec2.isEmpty()) {
                simList.put(entry.getKey(), 0.0);
            } else {
                vec1plus = mingcheng.addSimWordToVec(vec1, vec2, 0);
                vec2plus = mingcheng.addSimWordToVec(vec2, vec1, 1);


                vecSpa = mix.mapMix(vec1plus, vec2plus);
                double res = sim.countCos(vecSpa);
                if (!Double.isNaN(res)) {
                    simList.put(entry.getKey(), res);
                }
                System.out.println(res);
                vec1 = mix.mapRemove(vec1plus, vec2plus);
            }
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

        return simList;
    }
}
