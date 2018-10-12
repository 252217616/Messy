package com.example.demo01.xianchengchi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String[] args) {

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(16, 128, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200000));
        //往线程池中循环提交线程
        for (int i = 0; i < 200000; i++) {
            //创建线程类对象
            MyTask myTask = new MyTask(i);
            //开启线程
            tpe.execute(myTask);
            //获取线程池中线程的相应参数
            System.out.println("线程池中线程数目：" +tpe.getPoolSize() + "，队列中等待执行的任务数目："+tpe.getQueue().size() + "，已执行完的任务数目："+tpe.getCompletedTaskCount());
            Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
            System.out.println(allStackTraces.size());
        }
        //待线程池以及缓存队列中所有的线程任务完成后关闭线程池。
        System.out.println("关闭线程池");
        tpe.shutdown();

        while(tpe.isTerminated()){
            System.out.println("线程池中线程数目：" +tpe.getPoolSize() + "，队列中等待执行的任务数目："+tpe.getQueue().size() + "，已执行完的任务数目："+tpe.getCompletedTaskCount());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     *线程类
     */
    static class MyTask implements Runnable {
        private int num;

        public MyTask(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task " + num);
//            int i = 5/0;
//            System.out.println(i);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + num + "执行完毕");
        }

        /**
         * 获取（未来时间戳-当前时间戳）的差值，
         * 也即是：（每个线程的睡醒时间戳-每个线程的入睡时间戳）
         * 作用：用于实现多线程高并发
         *
         * @return
         */
        public long getDelta() throws ParseException {
            //获取当前时间戳
            long t1 = new Date().getTime();
            //获取未来某个时间戳（自定义，可写入配置文件）
            String str = "2016-11-11 15:15:15";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long t2 = simpleDateFormat.parse(str).getTime();
            return t2 - t1;
        }
    }
}
