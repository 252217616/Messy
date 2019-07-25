package com.example.demo01.stock.akka;


import java.math.BigDecimal;
import java.util.List;

public class Fitness {
    public static double fitness(List<Double> list){
        double sum = 0;
        for(int i=1;i<list.size();i++){
            sum += Math.sqrt(list.get(i)) ;
        }
        return sum;
    }

    /**
     * 均值
     */
    public static BigDecimal getVal(List<BigDecimal> nums){
        return nums.stream().reduce(BigDecimal::add).get().divide(new BigDecimal(nums.size()),4,BigDecimal.ROUND_HALF_UP);
    }
}
