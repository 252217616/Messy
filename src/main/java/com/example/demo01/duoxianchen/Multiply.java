package com.example.demo01.duoxianchen;

import com.example.demo01.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Multiply implements Runnable {
    public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<Msg>();

    @Override
    public void run() {
        while (true){
            try {
                Msg take = bq.take();
                take.setI(take.getI()*take.getJ());
                Div.bq.add(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
