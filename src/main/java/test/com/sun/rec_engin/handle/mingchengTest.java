package test.com.sun.rec_engin.handle; 

import com.sun.rec_engin.handle.mingcheng;
import com.sun.rec_engin.map_mix;
import com.sun.rec_engin.similarity;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashMap;
import java.util.Map;

/** 
* mingcheng Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 26, 2017</pre> 
* @version 1.0 
*/ 
public class mingchengTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getTFIDF(ArrayList<String> doc) 
* 
*/ 
@Test
public void testGetTFIDF() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getUserWord(int id) 
* 
*/ 
@Test
public void testGetUserWord() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getUserTFIDF(int id) 
* 
*/ 
@Test
public void testGetUserTFIDF() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getRecipeTFIDF() 
* 
*/ 
@Test
public void testGetRecipeTFIDF() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getVector(Map<String,Double> TFIDF, int type) 
* 
*/ 
@Test
public void testGetVector() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: addSimWord(Map<String,Double> vec1, Map<String,Double> vec2) 
* 
*/ 
@Test
public void testAddSimWord() throws Exception { 
//TODO: Test goes here...
    Map<String,Double> vec1=new HashMap<String, Double>();
    Map<String,Double> vec2=new HashMap<String, Double>();
    vec1.put("皮蛋",1.973);vec1.put("瘦肉",1.889);vec1.put("粥",1.093);
    vec2.put("羊肉",2.448);vec2.put("汤",1.426);
    mingcheng.addSimWord(vec1,vec2);
    System.out.println();
} 

/** 
* 
* Method: addSimWordToVec(Map<String,double[]> vec1, Map<String,double[]> vec2, int type) 
* 
*/ 
@Test
public void testAddSimWordToVec() throws Exception { 
//TODO: Test goes here...
    Map<String,double[]> vec1=new HashMap<String, double[]>();
    Map<String,double[]> vec2=new HashMap<String, double[]>();
    double[][] value={{1.973,0},{1.889,0},{1.093,0},{0,2.448},{0,1.426}};

    vec1.put("皮蛋",value[0]);vec1.put("瘦肉",value[1]);vec1.put("粥",value[2]);
    vec2.put("羊肉",value[3]);vec2.put("汤",value[4]);

    Map<String,double[]> vec1plus=new HashMap<String, double[]>();
    Map<String,double[]> vec2plus=new HashMap<String, double[]>();

    vec1plus=mingcheng.addSimWordToVec(vec1,vec2,0);
    vec2plus=mingcheng.addSimWordToVec(vec2,vec1,1);



    similarity sim=new similarity();
    map_mix mix=new map_mix();


    Map<String, double[]> vecSpa = new HashMap<String, double[]>();


    vecSpa=mix.mapMix(vec1plus,vec2plus);
    double res=sim.countCos(vecSpa);

    System.out.println(res);
    vec1plus=mix.mapRemove(vec1plus,vec2plus);

    System.out.println();
} 

/** 
* 
* Method: countSim(int id) 
* 
*/ 
@Test
public void testCountSim() throws Exception { 
//TODO: Test goes here...
    mingcheng.countSim(mingcheng.getSimList1(1));

} 

/** 
* 
* Method: countSim1(int id) 
* 
*/ 
@Test
public void testCountSim1() throws Exception {
//TODO: Test goes here...
    mingcheng.countSim(mingcheng.getSimList1(1));
} 


} 
