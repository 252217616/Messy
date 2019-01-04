package com.example.demo01.yujie;

public enum A {
    AA,BB;
    public void exce(String pag){
        System.out.println("开始执行！"+pag);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行结束"+pag);

    }
}
