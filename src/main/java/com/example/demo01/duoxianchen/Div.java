package com.example.demo01.duoxianchen;

import com.example.demo01.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Div implements Runnable {
    public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<Msg>();

    @Override
    public void run() {
        while (true){
            try {
                Msg take = bq.take();
                take.setI(take.getI()/2);
//                System.out.println(take.getStr()+ "="+take.getI());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
