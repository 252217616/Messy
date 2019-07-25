package com.example.demo01.ciyun;

import com.google.common.collect.Maps;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import scala.util.parsing.combinator.token.StdTokens;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String,Integer> map = Maps.newHashMap();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        File file = new File("D:\\GitProject\\words.txt");
        InputStream is = new FileInputStream(file);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            List<SegToken> process = segmenter.process(line.trim(), JiebaSegmenter.SegMode.INDEX);
            for(SegToken segToken:process){
                String word = segToken.word;
                if(word.length()==1) continue;
                if(map.containsKey(word)){
                    map.put(word,map.get(word)+1);
                }else {
                    map.put(word,1);
                }
            }
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
       for(Map.Entry<String,Integer> entry:map.entrySet()){
           System.out.println(entry.getKey()+"\t"+entry.getValue());
       }
    }
}
