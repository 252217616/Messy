package com.example.demo01.tongjiGailv;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

public class BaseCalculation {

    public static void main(String[] args) {
        List<BigDecimal> list  = Lists.newArrayList();
        list.add(new BigDecimal(1));
        list.add(new BigDecimal(2));
        list.add(new BigDecimal(3));
        list.add(new BigDecimal(4));
        System.out.println(getVal(list));
        System.out.println(getVariance(list));
        System.out.println(getStandardDeviation(list));
    }

    /**
     * 均值
     */
    public static BigDecimal getVal(List<BigDecimal> nums){
        return nums.stream().reduce(BigDecimal::add).get().divide(new BigDecimal(nums.size()),2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 方差
     */
    public static BigDecimal getVariance(List<BigDecimal> nums){
        BigDecimal val = getVal(nums);
        BigDecimal variance = BigDecimal.ZERO;
        for (BigDecimal b:nums){
            variance = variance.add(b.subtract(val).pow(2));
        }
        return variance.divide(new BigDecimal(nums.size()),2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * bigdecimal开根号
     * @param num
     * @return
     */
    public static BigDecimal sqrt(BigDecimal num) {
        if(num.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal x = num.divide(new BigDecimal("2"), MathContext.DECIMAL128);
        while(x.subtract(x = sqrtIteration(x, num)).abs().compareTo(new BigDecimal("0.0000000000000000000001")) > 0);
        return x;
    }
    private static BigDecimal sqrtIteration(BigDecimal x, BigDecimal n) {
        return x.add(n.divide(x, MathContext.DECIMAL128)).divide(new BigDecimal("2"), MathContext.DECIMAL128);
    }

    /**
     * 标准差
     */
    public static BigDecimal getStandardDeviation (List<BigDecimal> nums) {
        BigDecimal standard = getVariance(nums);
        return sqrt(standard).setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 频数
     */
    public static Map<Object,Integer> getFrequencyMap (List<Object> list){
        Map<Object,Integer>  map = Maps.newHashMap();
        for(Object o : list){
            if(map.containsKey(o)){
                map.put(o,map.get(o)+1);
            }else {
                map.put(o,1);
            }
        }
        return map;
    }

}
