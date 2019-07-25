package com.example.demo01.leetcode;

import java.util.*;

public class Simple2 {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public static void main(String[] args) {
        merge(new int[]{1,2, 3,0,0,0}, 3, new int[]{2,5,6}, 3);

    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // maintain an unchanging reference to node ahead of the return node.
        ListNode prehead = new ListNode(-1);

        ListNode prev = prehead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        // exactly one of l1 and l2 can be non-null at this point, so connect
        // the non-null list to the end of the merged list.
//        prev.next = l1 == null ? l2 : l1;

        return prehead.next;
    }

    public static int removeDuplicates(int[] nums) {
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    public static int removeElement(int[] nums, int val) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    /**
     * 输入: haystack = "hello", needle = "ll"
     * 输出: 2
     */
    public static int strStr(String haystack, String needle) {
        haystack = "hello";
        needle = "ll";
        return haystack.indexOf(needle);
//        return 0;
    }

    /**
     * ① 首先确定整个查找区间的中间位置 mid = （ left + right ）/ 2
     * ② 用待查关键字值与中间位置的关键字值进行比较；
     * 若相等，则查找成功
     * 若大于，则在后（右）半个区域继续进行折半查找
     * 若小于，则在前（左）半个区域继续进行折半查找
     * ③ 对确定的缩小区域再按折半公式，重复上述步骤。
     */
    public static int searchInsert(int[] nums, int target) {
        //最头最尾
        if (nums[0] >= target) return 0;
        if (nums[nums.length - 1] < target) {
            return nums.length;
        } else if (nums[nums.length - 1] == target) {
            return nums.length - 1;
        }
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }

        }
        return right;
    }

    public static int maxSubArray(int[] nums) {
        if (nums.length == 0) return 0;
        int result = nums[0];
        int sub = 0;
        for (int i = 0; i < nums.length; i++) {
            if (sub > 0) {
                sub = sub + nums[i];
            } else {
                sub = nums[i];
            }
            result = Math.max(result, sub);
        }
        return result;
    }


    public static int fmnq(int num, Map<Integer, Integer> map) {
        if (num == 0 || num == 1) {
            return 1;
        }
        if (map.containsKey(num)) {
            return map.get(num);
        } else {
            int result = fmnq(num - 1, map) + fmnq(num - 2, map);
            map.put(num, result);
            return result;
        }


    }

    public static int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] %= 10;
            if (digits[i] != 0) {
                return digits;
            }
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;

    }

    public static String addBinary(String a, String b) {
        int len = Math.max(a.length(), b.length());
        char[] result = new char[Math.max(a.length(), b.length()) + 1];
        int temp = 0;
        for (int i = 0; i < len; i++) {
            int sum = temp;
            if (i < a.length()) {
                int val = a.charAt(a.length() - 1 - i) - '0';
                sum += val;
            }
            if (i < b.length()) {
                int val = b.charAt(b.length() - 1 - i) - '0';
                sum += val;
            }
            int v = sum % 2;
            temp = sum / 2;
            result[len - i] = (char) (v + '0');
        }
        result[0] = (char) (temp + '0');
        return temp == 0 ? new String(result, 1, result.length - 1) : new String(result);
    }

    public static ListNode deleteDuplicates(ListNode head) {
        ListNode current = head;
        while (current != null && current.next != null) {
            if (current.next.val == current.val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
    }

    //123   256
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] nums1_copy = new int[m];
        System.arraycopy(nums1, 0, nums1_copy, 0, m);
        int mi = 0;
        int ni = 0;
        for (int i = 0; i < nums1.length; i++) {
            int temp_1 = mi<m? nums1_copy[mi]:nums2[n-1];
            int temp_2 = ni<n? nums2[ni]: nums1_copy[m-1];
            if (temp_1 >= temp_2 ) {
                nums1[i] = temp_2;
                ni++;
            } else {
                nums1[i] = temp_1;
                mi++;
            }
        }
    }
}
