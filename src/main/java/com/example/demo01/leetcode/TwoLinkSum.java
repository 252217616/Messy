package com.example.demo01.leetcode;

import java.math.BigDecimal;

public class TwoLinkSum {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(9);
        l1.next = new ListNode(8);
        ListNode l2 = new ListNode(1);
        ListNode listNode = addTwoNumbers(l1, l2);
        System.out.println("123");
    }
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        BigDecimal v1 = new BigDecimal(getValue(l1));
        BigDecimal v2 = new BigDecimal(getValue(l2));
        char[] chars = v1.add(v2).toString().toCharArray();
        char[] charsCopy = new char[chars.length];
        for(int i=0;i<chars.length;i++){
            charsCopy[i] = chars[chars.length-1-i];
        }
        ListNode listNode = new ListNode(0);
        ListNode temp = listNode;

        for (int i = 0; i < chars.length; i++) {
            ListNode node = new ListNode(Integer.parseInt(charsCopy[i]+""));
            temp.next = node;
            temp = temp.next;

        }
        return listNode.next;
    }

    public static String getValue(ListNode l) {
        if (l.next == null) {
            return l.val+"";
        }
        return ""+getValue(l.next)+l.val;
    }

}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}
