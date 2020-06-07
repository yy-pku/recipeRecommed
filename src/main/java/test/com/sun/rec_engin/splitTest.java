package test.com.sun.rec_engin; 

import com.sun.rec_engin.split;
import org.ansj.recognition.impl.StopRecognition;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashSet;
import java.util.Set;

/** 
* split Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 25, 2017</pre> 
* @version 1.0 
*/ 
public class splitTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: splitWord(String s) 
* 
*/ 
@Test
public void testSplitWord() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: splitWordtoArrDelUseless(String s) 
* 
*/ 
@Test
public void testSplitWordtoArrDelUseless() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: delUseless(String value) 
* 
*/ 
@Test
public void testDelUseless() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getWord(String str) 
* 
*/ 
@Test
public void testGetWord() throws Exception { 
//TODO: Test goes here...
    split sw=new split();
    String content="1.将牛肉筋膜剥除掉，切成0.1-0.2厘米厚的薄片，横着肉纹切成5-6厘米长的细丝。把芹菜的根、筋、叶择去，洗干净后切成2-3厘米长的段（过粗的劈开）。将豆瓣辣椒酱剁成细泥。 2.用旺火烧热炒勺，倒入植物油烧到6-7成热，放入牛肉丝快速煽炒几下，加入盐，再炒至酥脆，肉变成枣红地，再加入豆瓣辣椒酱泥和辣椒粉，再颠炒几下。 3.后顺次加入白糖、料酒、酱油、味精，翻炒均匀，再加入芹菜、青蒜、姜丝、拌炒几下后，喷点醋颠翻几下盛出，在上面撒上花椒即可。";
    sw.getWord(content);
} 

/** 
* 
* Method: getFilterWord(String str, Set<String> expectedNature) 
* 
*/ 
@Test
public void testGetFilterWordForStrExpectedNature() throws Exception { 
//TODO: Test goes here...
    split sw=new split();
    Set<String> expectedNature = new HashSet<String>() {{
        add("Ag");add("a");add("ad");add("an");add("Bg");add("b");add("d");add("i");
        add("j");add("n");add("nr");add("ns");add("nt");add("nx");add("nz");add("v");
        add("vd");add("vn");
    }};
    String s="蛏溜奇";
    sw.getFilterWord(s,expectedNature);
    System.out.println(sw.getFilterWord(s,expectedNature));
} 

/** 
* 
* Method: getFilterWord(String str, Set<String> expectedNature, StopRecognition fitler) 
* 
*/ 
@Test
public void testGetFilterWordForStrExpectedNatureFitler() throws Exception { 
//TODO: Test goes here...
    split sw=new split();
            /*
        需要的词性
         */
//        Set<String> expectedNature = new HashSet<String>() {{
//            add("n");add("v");add("nw");
//        }};
    Set<String> expectedNature = new HashSet<String>() {{
        add("n");add("nr");add("ns");add("nz");add("nl");add("ng");add("nw");
    }};
        /*
        原料列需要过滤器的话就加
         */
    StopRecognition filter = new StopRecognition();//实用化过滤器,添加过滤词
    filter.insertStopWords("主");
    filter.insertStopWords("料");
    filter.insertStopWords("配料");
    String content="1.将牛肉筋膜剥除掉，切成0.1-0.2厘米厚的薄片，横着肉纹切成5-6厘米长的细丝。把芹菜的根、筋、叶择去，洗干净后切成2-3厘米长的段（过粗的劈开）。将豆瓣辣椒酱剁成细泥。 2.用旺火烧热炒勺，倒入植物油烧到6-7成热，放入牛肉丝快速煽炒几下，加入盐，再炒至酥脆，肉变成枣红地，再加入豆瓣辣椒酱泥和辣椒粉，再颠炒几下。 3.后顺次加入白糖、料酒、酱油、味精，翻炒均匀，再加入芹菜、青蒜、姜丝、拌炒几下后，喷点醋颠翻几下盛出，在上面撒上花椒即可。";
    sw.getFilterWord(content,expectedNature,filter);
} 


} 
