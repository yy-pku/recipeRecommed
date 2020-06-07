package test.com.sun.rec_engin.handle; 

import com.sun.rec_engin.handle.yuanliao;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* yuanliao Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 25, 2017</pre> 
* @version 1.0 
*/ 
public class yuanliaoTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: countSim(int id) 
* 
*/ 
@Test
public void testCountSim() throws Exception {
//TODO: Test goes here...
    yuanliao yl=new yuanliao();
    yl.countSim(yl.getSimList(1));
} 


} 
