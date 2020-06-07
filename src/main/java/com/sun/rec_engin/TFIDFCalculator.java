package com.sun.rec_engin;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来计算tf-idf的值
 */
public class TFIDFCalculator {
    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     * 某个词在文章中出现的次数/该文章的总词数
     */
    public double tf(ArrayList<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     *log(语料库的文档总数目/包含该词d的文档数)
     */
    public double idf(ArrayList<ArrayList<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / (n+1));
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(ArrayList<String> doc, ArrayList<ArrayList<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }
}
