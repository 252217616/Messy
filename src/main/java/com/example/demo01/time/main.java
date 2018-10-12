package com.example.demo01.time;

public class main {

    public static void main(String[] args) throws InterruptedException {

        for(int i = 0;i<10;i++){
            add();
            System.out.println("停止了");
            Thread.sleep(10);
        }

    }

    public static void add(){
        for(int i = 0;i<1000;i++){

            System.out.println(i);
        }
    }
}
