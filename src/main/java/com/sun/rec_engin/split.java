package com.sun.rec_engin;

/**
 * Created by sunyang on 2016/12/8.
 * 这个类用来完成分词功能.
 */

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类负责分词
 */
public class split {
    similarity sim=new similarity();
    //体现出分词效果
    public void splitWord(String s) throws IOException {
        String text = s;
        Analyzer analyzer = new IKAnalyzer(true);// 构造函数当为 true时，分词器采用智能切分
        StringReader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream("", reader);
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
        while(ts.incrementToken()){
            if(!delUseless(term.toString())){
                System.out.print(term.toString() + "|");
            }
        }
        System.out.println();
        analyzer.close();
        reader.close();
    }
    //将分词结果存到数组中去,在这里面处理掉无用词
    public ArrayList<String> splitWordtoArrDelUseless(String s) throws IOException {
        String text = s;
        ArrayList List = new ArrayList();
        Analyzer analyzer = new IKAnalyzer(true);// 构造函数当为 true时,分词器采用智能切分
        StringReader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream("", reader);
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
        while(ts.incrementToken()){
            if(!delUseless(term.toString())){
                List.add(term.toString());
            }
        }
        analyzer.close();
        reader.close();
        return List;
    }
    public boolean delUseless(String value){//正则表达式去除xx克

        Pattern pattern = Pattern.compile("^\\pN");

        Matcher isNum = pattern.matcher(value);
        if(isNum.find()){
            return true;
        }else
            return false;
    }
//    public ArrayList<String> getNoun(String str){//分词,只得到名词
//        ArrayList List = new ArrayList();
//        Set<String> expectedNature = new HashSet<String>() {{
//            add("n");add("nr");add("ns");add("nz");add("nl");add("ng");add("nw");
//        }};
//
//        StopRecognition fitler = new StopRecognition();//实用化过滤器,添加过滤词
//        fitler.insertStopWords("主");
//        fitler.insertStopWords("料");
//        fitler.insertStopWords("配料");
//
//
////        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
//        Result result = ToAnalysis.parse(str).recognition(fitler); //分词结果的一个封装，主要是一个List<Term>的terms
//
//        java.util.List<Term> terms = result.getTerms(); //拿到terms
//
//        for(int i=0; i<terms.size(); i++) {
//            String word = terms.get(i).getName(); //拿到词
//            String natureStr = terms.get(i).getNatureStr(); //拿到词性
//            if(expectedNature.contains(natureStr)) {
//                System.out.print(word+"|");
//                List.add(word);
//            }
//        }
//        System.out.println();
//        return List;
//    }

    public ArrayList<String> getWord(String str){//分词,无过滤器,无词性
        ArrayList List = new ArrayList();
//        Set<String> expectedNature = new HashSet<String>() {{
//            add("n");add("nr");add("ns");add("nz");add("nl");add("ng");add("nw");add("a");
//            add("an");
//        }};

//        StopRecognition fitler = new StopRecognition();//实用化过滤器,添加过滤词
//        fitler.insertStopWords("主");
//        fitler.insertStopWords("料");
//        fitler.insertStopWords("配料");


//        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
//        Result result = ToAnalysis.parse(str).recognition(fitler); //分词结果的一个封装，主要是一个List<Term>的terms
        Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms

        java.util.List<Term> terms = result.getTerms(); //拿到terms

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            System.out.print(word+"|");
            List.add(word);
        }
        System.out.println();
        return List;
    }
    public ArrayList<String> getFilterWord(String str,Set<String> expectedNature){//分词,自定义需要的词性
        ArrayList List = new ArrayList();

        Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms

        java.util.List<Term> terms = result.getTerms(); //拿到terms

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                System.out.print(word+"|");
                List.add(word);
            }
        }
        System.out.println();
        return List;
    }

    public ArrayList<String> getFilterWord(String str,Set<String> expectedNature,StopRecognition fitler){
        /*
        重载上面那个方法.分词,自定义需要的词性,并过滤停止词
         */
        ArrayList List = new ArrayList();

        Result result = ToAnalysis.parse(str).recognition(fitler); //分词结果的一个封装，主要是一个List<Term>的terms

        java.util.List<Term> terms = result.getTerms(); //拿到terms

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                System.out.print(word+"|");
                List.add(word);
            }
        }
        System.out.println();
        return List;
    }

}
