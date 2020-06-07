package test.com.sun.rec_engin.handle; 

import com.sun.rec_engin.handle.zuofa;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* zuofa Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 27, 2017</pre> 
* @version 1.0 
*/ 
public class zuofaTest { 

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
} 

/** 
* 
* Method: addSimWordToVec(Map<String,double[]> vec1, Map<String,double[]> vec2, int type) 
* 
*/ 
@Test
public void testAddSimWordToVec() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: countSim(int id) 
* 
*/ 
@Test
public void testCountSim() throws Exception { 
//TODO: Test goes here...
    zuofa zf=new zuofa();
    zuofa.countSim(zf.getSimList(1));
}

    @Test
    public void testCountSim1() throws Exception {
//TODO: Test goes here...
        zuofa zf=new zuofa();
        zuofa.countSim(zf.getSimList(1));
    }


} 
