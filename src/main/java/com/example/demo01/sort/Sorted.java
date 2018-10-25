package com.example.demo01.sort;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.cnblogs.com/onepixel/articles/7674659.html
 */
public class Sorted {

    public static void main(String[] args) {
        charu(getNums());
    }


    public static int [] getNums(){
        Random random = new Random();
        return IntStream.rangeClosed(0,100).map(x->x+random.nextInt(100)).toArray();
    }

    /**
     * 冒泡排序
     */
    public static void maopao (int[] nums){
        int len = nums.length;
        for(int i =0;i<len-1;i++){
            for(int j =0;j<len-1-i;j++){
                if(nums[j]>nums[j+1]){
                    int temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                }

            }
        }
        Arrays.stream(nums).forEach(x -> System.out.println(x+""));
    }


    /**
     * 选择排序
     */
    public static void xuanze(int [] nums){
        int len = nums.length;
        for(int i=0;i<len-1;i++){
            int maxidex = i;
            for(int j=i+1;j<len;j++){
                if(nums[j]<nums[maxidex]){
                    maxidex = j;
                }
            }
            int temp = nums[i];
            nums[i] = nums[maxidex];
            nums[maxidex] = temp;
        }
        System.out.println();
        Arrays.stream(nums).forEach(x -> System.out.print(x+"  "));
    }

    /**
     * 插入排序
     */
    public static void charu(int[] nums){
        int len = nums.length;
        int preIndex, current;
        for(int i =1;i<len;i++){
            preIndex = i-1;
            current = nums[i];
            while(preIndex >0&&nums[preIndex]>current){
                nums[preIndex+1] = nums[preIndex];
                preIndex--;
            }
            nums[preIndex+1] = current;
        }
        Arrays.stream(nums).forEach(x -> System.out.print(x+"  "));
    }
}
