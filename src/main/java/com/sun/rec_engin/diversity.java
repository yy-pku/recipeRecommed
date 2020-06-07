package com.sun.rec_engin;

import com.sun.rec_engin.handle.mix_rec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用来计算多样性
 */
public class diversity {
    public static void getDiv(int id){
        List<Map.Entry<Integer,Double>> infoId = new ArrayList<Map.Entry<Integer,Double>>();
        infoId= mix_rec.countSim(id);
        double div=0.0;
        int x=0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j <=i; j++) {
                div+=infoId.get(i).getValue()-infoId.get(j).getValue();
                x++;
            }
        }
        System.out.println(div);
    }
}
