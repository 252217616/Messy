package com.example.demo01.duoxianchen;


import com.example.demo01.bean.Msg;

public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();
        for(int i = 1;i<1000;i++){
            for(int j = 1;j<1000;j++){
                Msg msg = new Msg();
                msg.setI(i);
                msg.setJ(j);
                msg.setStr("(("+i+"+"+j+")*"+i+")/2");
                Plus.bq.add(msg);
            }
        }
        long end = System.currentTimeMillis();
        long time = end-start;
        start = System.currentTimeMillis();
        for(int i = 1;i<1000;i++){
            for(int j = 1;j<1000;j++){
                double result = ((i+j)*i)/2;
                System.out.println(result);
            }
        }
        end = System.currentTimeMillis();
        System.out.println("耗时1："+time+"，耗时2："+(end-start));

    }

}
