package com.sun.rec_engin.handle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 这一个方法是混合推荐,使用[菜谱名称:0.1,做法:0.3,原料:0.6]
 * 的权重加权,如果为空,那么相似度定义为0.1
 */
public class mix_rec {
    public static List<Map.Entry<Integer,Double>> countSim(int id){
        mingcheng mc=new mingcheng();
        zuofa zf=new zuofa();
        yuanliao yl=new yuanliao();

        Map<Integer,Double> simList1=mc.getSimList(id);
        Map<Integer,Double> simList2=zf.getSimList(id);
        Map<Integer,Double> simList3=yl.getSimList(id);

        Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值
        Iterator<Map.Entry<Integer, Double>> entries = simList1.entrySet().iterator();
        while (entries.hasNext()){
            Double res=0.0;
            Map.Entry<Integer, Double> entry = entries.next();
            int i=entry.getKey();
            res=0.1*entry.getValue()+0.3*simList2.get(i)+0.6*simList3.get(i);
            simList.put(i,res);
        }
        return sort.sortRes(simList);
    }
}
