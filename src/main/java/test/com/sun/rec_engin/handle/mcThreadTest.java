package test.com.sun.rec_engin.handle; 

import com.sun.dao.LikeDAO;
import com.sun.rec_engin.handle.mcThread;
import com.sun.rec_engin.handle.mingcheng;
import com.sun.rec_engin.map_mix;
import com.sun.rec_engin.similarity;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/** 
* mcThread Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 27, 2017</pre> 
* @version 1.0 
*/ 
public class mcThreadTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: call() 
* 
*/ 
@Test
public void testCall() throws Exception { 
//TODO: Test goes here...
    //创建线程池,我的cpu应该就4线程最多了
    ExecutorService pool = Executors.newFixedThreadPool(3);


    System.out.println("开始计算菜谱名称相似度");

    Map<Integer,Double> simList = new HashMap<Integer,Double>();//存放计算的相似值
    Map<String,Double> userTFIDF=mingcheng.getUserTFIDF(4);//存放用户的词的,tfidf结果.形式{"皮蛋":1.973,"瘦肉":"1.889"....}
    mingcheng.getRecipeTFIDF();
    LikeDAO likedao=new LikeDAO();
    ArrayList<Integer> userLikeList=likedao.queryLike(1);
    map_mix mix=new map_mix();
    similarity sim=new similarity();


    Map<String, double[]> vec1=mingcheng.getVector(userTFIDF,0);//用户的向量空间

//    List<Future<String>> results = new ArrayList<Future<String>>();

    //这个存放Future,得到线程的返回结果
    ArrayList<Future<Map<Integer,Double>>> res=new ArrayList<Future<Map<Integer, Double>>>();

    //这里新建一个map,从tfidfmap里面读取100条做测试
    Map<Integer,Map<String,Double>> map=new HashMap<Integer, Map<String, Double>>();
    int i=0;
    for (Map.Entry<Integer,Map<String,Double>> entry: mingcheng.tfidfmap.entrySet()){
        map.put(entry.getKey(),entry.getValue());
        i++;
        if(i>100) break;
    }

    long startTime = System.currentTimeMillis();   //获取开始时间

    for (Map.Entry<Integer,Map<String,Double>> entry: map.entrySet()) {//entry0的形式为{966,{"皮蛋":1.973,"瘦肉":....}}{
        //这里新建一个线程
        mcThread c1=new mcThread(userLikeList,vec1,entry);
        //把线程放入到一个封装好的FutureTask里面
        FutureTask<Map<Integer,Double>> task = new FutureTask(c1);
        //里面暂存
        res.add(task);
        pool.execute(task);
    }

    //Future的get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回；
    for(Future<Map<Integer,Double>> f:res){
        simList.putAll(f.get());
    }
    long endTime=System.currentTimeMillis(); //获取结束时间
    System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

    System.out.println();
    pool.shutdown();

    }
} 
