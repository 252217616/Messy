package com.example.demo01.sort;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * https://www.cnblogs.com/onepixel/articles/7674659.html
 */
public class Sorted {

    public static void main(String[] args) {
        //快速排序
//        int[] nums = getNums();
//        int start = 0;
//        int end = nums.length - 1;
//        kuaipai(nums, start, end);
//        Arrays.stream(nums).forEach(System.out::println);

        //二分查找
        int a [] = new int[] {0,1,2,3,4,5,6,7,8,9};
        System.out.println(erfen(a,2));
    }


    public static int[] getNums() {
        Random random = new Random();
        return IntStream.rangeClosed(0, 10).map(x -> x + random.nextInt(100)).toArray();
    }

    /**
     * 冒泡排序
     */
    public static void maopao(int[] nums) {
        int len = nums.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }

            }
        }
        Arrays.stream(nums).forEach(x -> System.out.println(x + ""));
    }


    /**
     * 选择排序
     */
    public static void xuanze(int[] nums) {
        int len = nums.length;
        for (int i = 0; i < len - 1; i++) {
            int maxidex = i;
            for (int j = i + 1; j < len; j++) {
                if (nums[j] < nums[maxidex]) {
                    maxidex = j;
                }
            }
            int temp = nums[i];
            nums[i] = nums[maxidex];
            nums[maxidex] = temp;
        }
        System.out.println();
        Arrays.stream(nums).forEach(x -> System.out.print(x + "  "));
    }

    /**
     * 插入排序
     */
    public static void charu(int[] nums) {
        int len = nums.length;
        int preIndex, current;
        for (int i = 1; i < len; i++) {
            preIndex = i - 1;
            current = nums[i];
            while (preIndex > 0 && nums[preIndex] > current) {
                nums[preIndex + 1] = nums[preIndex];
                preIndex--;
            }
            nums[preIndex + 1] = current;
        }
        Arrays.stream(nums).forEach(x -> System.out.print(x + "  "));
    }

    /**
     * 快速排序
     */
    public static void kuaipai(int[] a, int low, int high) {
        int start = low;
        int end = high;
        int key = a[low];
        while (end > start) {
            //从后往前比较
            while (end > start && a[end] >= key)
                //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if (a[end] <= key) {
                int temp = a[end];
                a[end] = a[start];
                a[start] = temp;
            }
            //从前往后比较
            while (end > start && a[start] <= key)
                //如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if (a[start] >= key) {
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if (start > low) kuaipai(a, low, start - 1);//左边序列。第一个索引位置到关键值索引-1
        if (end < high) kuaipai(a, end + 1, high);//右边序列。从关键值索引+1到最后一个
    }


    /**
     * 二分查找
     */
    public static int erfen(int[] a,int key) {
        int start = 0;
        int end = a.length-1;

        if(a[start]>key || a[end]<key){
            return -1;
        }
        while(start<end){
            int mid = (start+end)/2;
            if(a[mid]>key){
                end = mid-1;
            }else if (a[mid]<key){
                start = mid+1;
            }else {
                return mid;
            }
        }
        return -1;
    }

/**
 * 链表合并
 */

/**
 * 链表反转
 */
}
