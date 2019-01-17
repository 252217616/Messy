package com.example.demo01.akka;


import java.util.List;

public class Fitness {
    public static double fitness(List<Double> list){
        double sum = 0;
        for(int i=1;i<list.size();i++){
            sum += Math.sqrt(list.get(i)) ;
        }
        return sum;
    }
}
