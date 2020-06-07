package com.sun.rec_engin;

/**
 * 这个类只用来计算相似度.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class similarity {
    /*
    * 计算两个字符串(英文字符)的相似度，简单的余弦计算，未添权重
    */
    public double getSimilarDegree(ArrayList<String> str1, ArrayList<String> str2)
    {
        //创建向量空间模型，使用map实现，主键为词项，值为长度为2的数组，存放着对应词项在字符串中的出现次数
        Map<String, double[]> vectorSpace = new HashMap<String, double[]>();
        double[] itemCountArray = null;

        //动态数组转为数组
        double weight=10;//设计一个权重值，原料中出现的第一项为10，然后逐渐-1（这个值需要凭感觉调优）
        double wei_reduce=0.5;//权重下降的值，默认为1
        double wei_value=weight;//这是循环中实际用到的值

        int size1=str1.size();
        String[] strArray1 = (String[])str1.toArray(new String[size1]);

        for(int i=0; i<strArray1.length; ++i)
        {
            if (wei_value<=1){//小于1的话都设为1
                wei_value=1;
            }
            else wei_value=weight-i*wei_reduce;

            if(vectorSpace.containsKey(strArray1[i]))
                vectorSpace.get(strArray1[i])[0]+=wei_value;
            else
            {
                itemCountArray = new double[2];
                itemCountArray[0] = wei_value;
                itemCountArray[1] = 0;
                vectorSpace.put(strArray1[i], itemCountArray);
            }
        }
        int size2=str2.size();
        String[] strArray2 = (String[])str2.toArray(new String[size2]);
        for(int i=0; i<strArray2.length; ++i)
        {
            if (wei_value<=1){
                wei_value=1;
            }
            else wei_value=weight-i*wei_reduce;

            if(vectorSpace.containsKey(strArray2[i]))
                vectorSpace.get(strArray2[i])[1]+=wei_value;
            else
            {
                itemCountArray = new double[2];
                itemCountArray[0] = 0;
                itemCountArray[1] = wei_value;

                vectorSpace.put(strArray2[i], itemCountArray);
            }
        }

        //计算相似度，改为调用函数。
        //返回相似度
        return (this.countCos(vectorSpace));
    }

    public double countCos(Map<String, double[]> vectorSpace){//提供一个向量空间。计算相似度
        double[] itemCountArray = null;//为了避免频繁产生局部变量，所以将itemCountArray声明在此

        double vector1Modulo = 0.00;//向量1的模
        double vector2Modulo = 0.00;//向量2的模
        double vectorProduct = 0.00; //向量积
        Iterator iter = vectorSpace.entrySet().iterator();

        while(iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            itemCountArray = (double[])entry.getValue();

            vector1Modulo += itemCountArray[0]*itemCountArray[0];
            vector2Modulo += itemCountArray[1]*itemCountArray[1];

            vectorProduct += itemCountArray[0]*itemCountArray[1];
        }

        vector1Modulo = Math.sqrt(vector1Modulo);
        vector2Modulo = Math.sqrt(vector2Modulo);
        return (vectorProduct/(vector1Modulo*vector2Modulo));
    }

    public Map<String, double[]> getVector(ArrayList<String> str,int type){
        /*
        type参数是为了区分是用户向量空间还是菜谱向量空间
        0代表是用户向量空间，1是菜谱向量空间。
        区别是存到double[]的0还是1
         */
        int flag=1;
        if(type==1){
            flag=0;
        }
        Map<String, double[]> vectorSpace = new HashMap<String, double[]>();
        double[] itemCountArray = null;

        //动态数组转为数组
        double weight=10;//设计一个权重值，原料中出现的第一项为10，然后逐渐-1（这个值需要凭感觉调优）
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
//        if(type==0){
//            for(int i=0; i<strArray.length; ++i)
//            {
//                if (wei_value<=1){//小于1的话都设为1
//                    wei_value=1;
//                }
//                else wei_value=weight-i*wei_reduce;
//
//                if(vectorSpace.containsKey(strArray[i]))
//                    vectorSpace.get(strArray[i])[0]+=wei_value;
//                else
//                {
//                    itemCountArray = new double[2];
//                    itemCountArray[0] = wei_value;
//                    itemCountArray[1] = 0;
//                    vectorSpace.put(strArray[i], itemCountArray);
//                }
//            }
//        }
//        else{
//            for(int i=0; i<strArray.length; ++i)
//            {
//                if (wei_value<=1){
//                    wei_value=1;
//                }
//                else wei_value=weight-i*wei_reduce;
//
//                if(vectorSpace.containsKey(strArray[i]))
//                    vectorSpace.get(strArray[i])[1]+=wei_value;
//                else
//                {
//                    itemCountArray = new double[2];
//                    itemCountArray[0] = 0;
//                    itemCountArray[1] = wei_value;
//
//                    vectorSpace.put(strArray[i], itemCountArray);
//                }
//            }
//        }

        return vectorSpace;
    }

}
