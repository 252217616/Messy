package com.example.demo01.leetcode;


import java.util.*;

public class Simple {

    public static void main(String[] args) {

    }

    //两数之和
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int sub = target - nums[i];
            if (map.containsKey(sub)) {
                if (map.get(sub) == i) {
                    continue;
                }
                return new int[]{map.get(sub), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    public static int reverse(int x) {
        String nums = x + "";
        char[] chars = nums.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        int limit;
        if (x < 0) {
            limit = 1;
            sb.append("-");
        } else {
            limit = 0;
        }
        for (int i = chars.length - 1; i >= limit; i--) {
            if ('0' == chars[i] && flag) {
                continue;
            } else {
                flag = false;
                sb.append(chars[i]);
            }
        }
        int num = 0;
        try {
            num = Integer.parseInt(sb.toString());
        } catch (Exception e) {

        } finally {
            return num;
        }

    }

    public static boolean isPalindrome(int x) {
        String reversedStr = (new StringBuilder(x + "")).reverse().toString();
        return (x + "").equals(reversedStr);
    }

    public static String longestCommonPrefix(String[] strs) {
        StrNode nodes = new StrNode();
        int limit = 0;
        for (int i = 0; i < strs.length; i++) {
            if(strs[i] == null || "".equals(strs[i]) ){
                return "";
            }
            if(limit == 0 ){
                limit = strs[i].length();
            }
            if(limit>strs[i].length()){
                limit = strs[i].length();
            }
            nodes.add(strs[i]);
        }

        StringBuilder sb = new StringBuilder();
        do{
            sb.append(nodes.getData() == null?"":nodes.getData());
            if (nodes.getSons().size() != 1) break;
            nodes = nodes.getSons().entrySet().iterator().next().getValue();

        }while (true);
        String result = sb.toString();
        if(result.length()>limit){
            result = result.substring(0,limit);
        }
        return result;

    }

    static class StrNode {
        private Map<Character, StrNode> sons = new HashMap<>();
        private Character data;


        public void add(String string) {
            char[] chars = string.toCharArray();
            StrNode son = this;
            for (int i = 0; i < string.length(); i++) {
                char car = chars[i];
                if (!son.getSons().containsKey(car)) {
                    son.getSons().put(car, new StrNode(car));
                    son = son.getSons().get(car);
                } else {
                    son = son.getSons().get(car);
                    continue;
                }
            }


        }

        public StrNode() {
        }

        ;

        public StrNode(char car) {
            this.data = car;
        }

        ;

        public Character getData() {
            return data;
        }

        public void setData(Character data) {
            this.data = data;
        }

        public Map<Character, StrNode> getSons() {
            return sons;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StrNode strNode = (StrNode) o;
            return Objects.equals(data, strNode.data);
        }

        @Override
        public int hashCode() {

            return Objects.hash(data);
        }
    }

    public static boolean isValid(String s) {
            if("".equals(s)){
                return true;
            }
            Map<Character,Character> map = new HashMap<>();
            map.put(')','(');
            map.put(']','[');
            map.put('}','{');
            char[] chars = s.toCharArray();
            Stack<Character> stack = new Stack<>();

            for(int i = 0;i<chars.length;i++){
                char tar = chars[i];
                //如果是结束符号
                if(map.containsKey(tar)){
                    Character c = stack.empty()?'#':stack.pop();
                    if(c!=map.get(tar)){
                        return  false;
                    }
                }else {
                    stack.push(chars[i]);
                }
            }
            return stack.empty();
    }

}

