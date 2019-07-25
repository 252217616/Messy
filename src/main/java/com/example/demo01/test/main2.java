package com.example.demo01.test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main2 {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        String url = "http://172.28.38.34/1sdsdfwer3";
//        System.out.println(url.substring(url.indexOf("/",10)+1));
        List<A> aList = new ArrayList<>();
        A a = new A();
        a.bigDecimal = BigDecimal.ZERO;
        a.integer = 0;
        aList.add(a);
        a = new A();
        a.bigDecimal = BigDecimal.TEN;
        a.integer = 10;
        aList.add(a);
        System.out.println();
    }
    @Data
    static class A {
        private BigDecimal bigDecimal;
        private Integer integer;
    }
}


