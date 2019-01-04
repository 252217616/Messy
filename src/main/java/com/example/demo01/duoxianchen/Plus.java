package com.example.demo01.duoxianchen;

import com.example.demo01.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Plus implements Runnable {
    public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<Msg>();

    @Override
    public void run() {
        while (true){
            try {
                Msg take = bq.take();
                take.setJ(take.getI()+take.getJ());
                Multiply.bq.add(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
