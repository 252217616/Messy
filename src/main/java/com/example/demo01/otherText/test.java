package com.example.demo01.otherText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class test<T> {

    private static final Map<String,String> map = new HashMap<>();
    public static void main(String[] args) throws Exception{
       for(int i = 0;i<100;i++){
           map.put(""+i,""+i);
       }
       map.clear();
       for(int i = 0;i<100;i++){
           map.put(""+i,""+i);
       }
    }
     static class AA{
        private String message;
        private String succeed;
        private List<Result> result;



    }
    static class  Result{
        private String com;
        private String no;
    }
}
