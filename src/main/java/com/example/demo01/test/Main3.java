package com.example.demo01.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main3 {
    public static void main(String[] args) {

    }

    private static boolean constains(String str, String cons) {
        Map<Character, Integer> mapIndex = Maps.newHashMap();
        mapIndex.put('a', 1);
        mapIndex.put('b', 2);
        mapIndex.put('c', 3);
        mapIndex.put('d', 4);
        mapIndex.put('e', 5);
        mapIndex.put('f', 6);
        mapIndex.put('g', 7);
        mapIndex.put('h', 8);
        mapIndex.put('i', 9);
        mapIndex.put('j', 10);
        mapIndex.put('k', 11);
        mapIndex.put('l', 12);
        mapIndex.put('m', 13);
        mapIndex.put('n', 14);
        mapIndex.put('o', 15);
        mapIndex.put('p', 16);
        mapIndex.put('q', 17);
        mapIndex.put('r', 18);
        mapIndex.put('s', 19);
        mapIndex.put('t', 20);
        mapIndex.put('u', 21);
        mapIndex.put('v', 22);
        mapIndex.put('w', 23);
        mapIndex.put('x', 24);
        mapIndex.put('y', 25);
        mapIndex.put('z', 26);
        Map<Integer, List<String>> mapStr = Maps.newHashMap();
        for (String s : str.split(" ")) {
            Integer integer = 0;
            for (char c : s.toCharArray()) {
                integer += mapIndex.get(c);
            }
            if (mapStr.containsKey(s)) {
                mapStr.get(s).add(s);
            } else {
                List<String> list = Lists.newArrayList();
                list.add(s);
                mapStr.put(integer, list);
            }
        }
        Integer sum = 0;
        for (char c : cons.toCharArray()) {
            sum += mapIndex.get(c);
        }
        if (mapStr.containsKey(sum)) {
            List<String> list = mapStr.get(sum);
            for (String result : list) {
                if (result.length() == cons.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void allStr(char[] chars, int start) {
        if (start == chars.length - 1) {
            System.out.println(new String(chars));
            return;
        }
        for (int i = start; i < chars.length; i++) {
            swap(chars,i,start);
            allStr(chars, start + 1);
            swap(chars,i,start);
        }
    }

    /**
     * 字典排序：
     * 第一步：从右至左找第一个左邻小于右邻的数，记下位置i，值list[a]
     * 第二部：从右边往左找第一个右边大于list[a]的第一个值，记下位置j，值list[b]
     * 第三步：交换list[a]和list[b]的值
     * 第四步：将i以后的元素重新按从小到大的顺序排列
     */
    private static void allStr2(char[] chars) {
        while (true){
            System.out.println(new String(chars));
            int a = 0;
            int j = 0;
            //从后往前找到一个降序的数
            for(int i = chars.length-1;i>0;i--){
                if(chars[i-1]<chars[i]){
                    a = i-1;
                    break;
                }
            }
            //找到第一个比a大的
            for(int i = chars.length-1;i>=0;i--){
                if(chars[i]> chars[a]){
                    j = i;
                    break;
                }
            }
            swap(chars,a,j);
            Arrays.sort(chars,a+1,chars.length);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    private static void swap(char[] chars,int i,int j){
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
}
