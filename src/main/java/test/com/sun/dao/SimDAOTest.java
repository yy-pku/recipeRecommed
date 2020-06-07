package test.com.sun.dao; 

import com.sun.dao.SimDAO;
import org.ansj.recognition.impl.StopRecognition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** 
* SimDAO Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 25, 2017</pre> 
* @version 1.0 
*/ 
public class SimDAOTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getUserVector(int id, String queue, Set<String> expectedNature, StopRecognition filter) 
* 
*/ 
@Test
public void testGetUserVector() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getRecipeVector(int i, String queue, Set<String> expectedNature, StopRecognition filter) 
* 
*/ 
@Test
public void testGetRecipeVector() throws Exception { 
//TODO: Test goes here...
    SimDAO simDAO=new SimDAO();
    Set<String> expectedNature = new HashSet<String>() {{
        add("n");add("nr");add("ns");add("nz");add("nl");add("ng");add("nw");
    }};
    StopRecognition filter=null;
    Map<String, double[]> res=simDAO.getRecipeVector(1097,"做法",expectedNature,filter);
    Iterator<Map.Entry<String, double[]>> entries = res.entrySet().iterator();
    while (entries.hasNext()){
        Map.Entry<String, double[]> entry=entries.next();//vec2从迭代器里面取得一个map
        System.out.println(entry.getKey()+":"+entry.getValue()[0]+" "+entry.getValue()[1]);
    }
}

} 
