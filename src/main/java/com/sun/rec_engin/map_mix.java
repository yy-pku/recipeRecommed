package com.sun.rec_engin;

import java.util.Iterator;
import java.util.Map;

/**
 * 合并两个向量空间的，
 */
public class map_mix {
    /*
    将vec1+=vec2;
     */
    public Map<String, double[]> mapMix(Map<String, double[]> vec1,Map<String, double[]> vec2){
        Iterator<Map.Entry<String, double[]>> entries = vec2.entrySet().iterator();
//        double[] itemCountArray = null;
        while(entries.hasNext()){
//            Map.Entry<String, double[]> entry=entries.next();
//            itemCountArray=entry.getValue();
//            if(vec1.containsKey(entry.getKey())){
//                vec1.get(entry.getKey())[0]+=itemCountArray[0];
//                vec1.get(entry.getKey())[1]+=itemCountArray[1];
//            }
//            else{
//                vec1.put(entry.getKey(),itemCountArray);
//            }
            /*
            代码优化
            经Google,发现上面写法浪费很多时间,应该缓存对map的get查询结果
            以及没必要使用containskey()方法(除非key有null)
             */
            Map.Entry<String, double[]> entry=entries.next();//vec2从迭代器里面取得一个map
            double[] vec1value = null;
            double[] vec2value = null;
            String vec2key=entry.getKey();
            vec2value=entry.getValue();
            vec1value=vec1.get(vec2key);
            if(vec1value!=null){
                vec1value[0]+=vec2value[0];
                vec1value[1]+=vec2value[1];
            }else {
                vec1.put(vec2key,vec2value);
            }


        }
        return vec1;
    }
    public Map<String, double[]> mapRemove(Map<String, double[]> vec1,Map<String, double[]> vec2){
        /*
        为什么需要这么一个坑爹的方法，因为我发现相同key值的不管在哪个新建的map里面，key和value总是指向一个空间
        所以会造成一直累加
        只能每次计算完之后就赶紧再减去了
         */
        Iterator<Map.Entry<String, double[]>> entries = vec2.entrySet().iterator();
        double[] itemCountArray = null;
        while(entries.hasNext()){
//            Map.Entry<String, double[]> entry=entries.next();
//            itemCountArray=entry.getValue();
//            if(vec1.containsKey(entry.getKey())){
//                vec1.get(entry.getKey())[0]-=itemCountArray[0];
//                vec1.get(entry.getKey())[1]-=itemCountArray[1];
//                /*
//                如果两个都为0,则remove掉
//                 */
//                if(vec1.get(entry.getKey())[0]==0&&vec1.get(entry.getKey())[1]==0){
//                    vec1.remove(entry.getKey());
//                }
//            }
//            else{
//                vec1.put(entry.getKey(),itemCountArray);
//            }
            Map.Entry<String, double[]> entry=entries.next();//vec2从迭代器里面取得一个map
            double[] vec1value = null;
            double[] vec2value = null;
            String vec2key=entry.getKey();
            vec2value=entry.getValue();
            vec1value=vec1.get(vec2key);
            if(vec1value!=null){
                vec1value[0]-=vec2value[0];
                vec1value[1]-=vec2value[1];
                if(vec1value[0]==0&&vec1value[1]==0){
                    vec1.remove(vec2key);
                }
            }else {
                vec1.put(vec2key,vec2value);
            }
        }
        return vec1;
    }
}
