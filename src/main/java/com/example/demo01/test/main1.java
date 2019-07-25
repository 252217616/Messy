package com.example.demo01.test;

import java.math.BigDecimal;
import java.util.Random;

public class main1 {

    public static void main(String[] args) {
        double num = 154;
        System.out.println(num);
        num = fourToReduction(num);
        System.out.println(num);
        num = fourToReduction(num);
        System.out.println(num);
        num = fourToReduction(num);
        System.out.println(num);
        num = fourToReduction(num);
        System.out.println(num);
    }

    public static double twoToReduction (double num){
//        return BigDecimal.ONE.divide(num,BigDecimal.ROUND_HALF_UP,2);
        return 1/num;
    }
    public static double threeToReduction (double num){
//        return BigDecimal.ONE.divide( BigDecimal.ONE.subtract(num),BigDecimal.ROUND_HALF_UP,2);
        return 1/(1-num);
    }
    public static double fourToReduction (double num){
//        return BigDecimal.ONE.add(num).divide(BigDecimal.ONE.subtract(num),BigDecimal.ROUND_HALF_UP,2);
        return (1+num)/(1-num);
    }
}
