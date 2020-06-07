package com.sun.rec_engin.handle;

import java.util.*;

public class sort {
    public static List<Map.Entry<Integer,Double>> sortRes(Map<Integer,Double> simList){
        /*
        对map进行排序取前十
        */
        List<Map.Entry<Integer,Double>> infoIds = new ArrayList<Map.Entry<Integer,Double>>(simList.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                if ((o2.getValue() - o1.getValue()) > 0)
                    return 1;
                else if ((o2.getValue() - o1.getValue()) == 0)
                    return 0;
                else
                    return -1;
            }
        });
        return infoIds;
    }
}
