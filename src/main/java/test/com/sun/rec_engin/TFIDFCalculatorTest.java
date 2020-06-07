package test.com.sun.rec_engin; 

import com.sun.dao.DisDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.TFIDFCalculator;
import com.sun.rec_engin.split;
import org.ansj.recognition.impl.StopRecognition;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.*;

/** 
* TFIDFCalculator Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 25, 2017</pre> 
* @version 1.0 
*/ 
public class TFIDFCalculatorTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: tf(ArrayList<String> doc, String term) 
* 
*/ 
@Test
public void testTf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: idf(ArrayList<ArrayList<String>> docs, String term) 
* 
*/ 
@Test
public void testIdf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: tfIdf(ArrayList<String> doc, ArrayList<ArrayList<String>> docs, String term) 
* 
*/ 
@Test
public void testTfIdf() throws Exception {
//TODO: Test goes here...
    /*
    定义一大堆变量
     */
    ArrayList<String> doc=new ArrayList<String>();
    ArrayList<ArrayList<String>> docs=new ArrayList<ArrayList<String>>();
    TFIDFCalculator tfidfcalculator=new TFIDFCalculator();
    DisDAO disDAO=new DisDAO();
    SimDAO simDAO=new SimDAO();
    Set<String> expectedNature = new HashSet<String>() {{
        add("Ag");add("a");add("ad");add("an");add("Bg");add("b");add("d");add("i");
        add("j");add("n");add("nr");add("ns");add("nt");add("nx");add("nz");add("v");
        add("vd");add("vn");
    }};
    StopRecognition filter=null;
    ArrayList<String> res=new ArrayList<String>();
    split sw=new split();

    /*
    获取全部文档的分词
     */
    res=simDAO.getColumn("菜谱名称");
    for (int i = 0; i < res.size(); i++) {
        ArrayList<String> document=new ArrayList<String>();
        document=sw.getFilterWord(res.get(i),expectedNature);
        docs.add(document);
    }
    /*
    获取某一个菜谱的分词
    */
    res=sw.getFilterWord(simDAO.getColumn("菜谱名称",966),expectedNature);
    for (int i = 0; i < res.size(); i++) {
        doc.add(res.get(i));
    }



    Map<String,Double> tfidfres=new HashMap<String, Double>();
    for (int i = 0; i < doc.size(); i++) {
        double tfidf=tfidfcalculator.tfIdf(doc,docs,doc.get(i));
        tfidfres.put(doc.get(i),tfidf);
    }
    List<Map.Entry<String,Double>> infoIds0 = new ArrayList<Map.Entry<String,Double>>(tfidfres.entrySet());
    Collections.sort(infoIds0, new Comparator<Map.Entry<String, Double>>() {
        public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
            if ((o2.getValue() - o1.getValue()) > 0)
                return 1;
            else if ((o2.getValue() - o1.getValue()) == 0)
                return 0;
            else
                return -1;
        }
    });
    for (Map.Entry<String,Double> map:infoIds0) {
        System.out.println(map.getKey()+":"+map.getValue());
    }
} 


} 
