package com.example.demo01.leetcode;

import java.util.HashSet;
import java.util.Set;

public class 无重复字符的最长子串 {
    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("jbpnbwwd"));
    }

    public static int lengthOfLongestSubstring(String s) {

        char[] chars = s.toCharArray();
        if(chars.length == 1){
            return 1;
        }
        int result = 0;
        int count = 0;
        Set<Character> set = new HashSet<>();
        for(int i = 0;i<chars.length;i++){
            for(int j = i;j<chars.length;j++){
                set.add(chars[j]);
                count++;
                if(count>set.size()){
                    if(result<count){
                        result = set.size();
                    }
                    count =0;
                    set.clear();
                    break;
                }
            }
        }
        return result;
    }
}
