package com.example.demo01.stream;

import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class demo {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for(int i = 1; i<10000;i++){
            list.add(""+i);
        }
        long start = System.currentTimeMillis();
        int i = 1;
        for(String s:list){
            i++;
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        start = System.currentTimeMillis();
        end = System.currentTimeMillis();
        System.out.println(end-start);
        Stream<String> stream = list.parallelStream();

    }
}
