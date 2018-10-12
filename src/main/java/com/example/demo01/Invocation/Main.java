package com.example.demo01.Invocation;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {

        DemoClientImpl demoClient = new DemoClientImpl();

        DemoClientPoxy demoClientPoxy = new DemoClientPoxy(demoClient);

        ClassLoader classLoader = demoClient.getClass().getClassLoader();

        Class[] cls = new Class[]{DemoClient.class};

        DemoClient proxy = (DemoClient)Proxy.newProxyInstance(classLoader, cls, demoClientPoxy);

        proxy.login();


    }
}
