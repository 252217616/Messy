package com.example.demo01.test;


import com.example.demo01.test.entity.ListNode;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.awt.*;
import java.util.*;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
public class TestBean {

    public static void main(String[] args) {
        TestBean test = new TestBean();
//        String i = test.longestPalindrome("aa");
//        int i = test.maxArea(new int[]{1,2,3,4});
//        System.out.println(i);
        ListNode init = test.init("321");
        test.removeNthFromEnd(init, 3);
        System.out.println(init);

    }


    public ListNode init(String num) {
        ListNode temp = null;
        for (char i : num.toCharArray()) {
            ListNode node = new ListNode(Integer.parseInt(i + ""));
            if (Objects.isNull(temp)) {
                temp = node;
            } else {
                node.next = (temp);
                temp = node;
            }


        }
        return temp;
    }


    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
//         保存起始位置，测试了用数组似乎能比全局变量稍快一点
        int[] range = new int[2];
        char[] str = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
//             把回文看成中间的部分全是同一字符，左右部分相对称
//             找到下一个与当前字符不同的字符
            i = findLongest(str, i, range);
        }
        return s.substring(range[0], range[1] + 1);
    }

    public static int findLongest(char[] str, int low, int[] range) {
//         查找中间部分
        int high = low;
        while (high < str.length - 1 && str[high + 1] == str[low]) {
            high++;
        }
//         定位中间部分的最后一个字符
        int ans = high;
//         从中间向左右扩散
        while (low > 0 && high < str.length - 1 && str[low - 1] == str[high + 1]) {
            low--;
            high++;
        }
//         记录最大长度
        if (high - low > range[1] - range[0]) {
            range[0] = low;
            range[1] = high;
        }
        return ans;
    }


    public int maxArea(int[] height) {
        int area = 0;
        int left = 0;
        for (int i = 1; i < height.length; i++) {
            int min = height[left] > height[i] ? height[i] : height[left];
            int mul = min * (i - left);
            if (area < mul) {
                area = mul;
                if (height[i] > height[left] && height[i] - height[left] > i - left) {
                    left = i;
                }
            }

        }
        return area;
    }


    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode result = head;
        ListNode first = head;
        if(head.next == null) return null;
        int count = 0;
        while (head != null) {

            if (count >=  n && head.next != null) {
                first = first.next;
            }

            if (head.next == null ) {
                //删除
                if(first == result){
                    //没移动
                    //是头？是尾？
                    if(n ==1){ //尾巴
                        result.next = null;
                    }else if(count == n){
                        result.next = result.next.next;
                    }else {
                        result = result.next;
                    }
                }else {
                    //移动
                    if(n == 1){
                        first.next = null;
                    }else {
                        first.next = first.next.next;
                    }
                }
            }
            head = head.next;
            count++;
        }
        return result;
    }

    private static Map<String,char[]> map = new HashMap<>();
    static {
        map.put("2",new char[]{'a','b','c'});
        map.put("3",new char[]{'d','e','f'});
        map.put("4",new char[]{'g','h','i'});
        map.put("5",new char[]{'j','k','l'});
        map.put("6",new char[]{'m','n','o'});
        map.put("7",new char[]{'p','q','r','s'});
        map.put("8",new char[]{'t','u','v'});
        map.put("9",new char[]{'w','x','y','z'});
    }
    public List<String> letterCombinations(String digits) {

        return null;
    }


}
