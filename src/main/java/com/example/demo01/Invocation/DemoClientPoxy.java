package com.example.demo01.Invocation;

import org.springframework.http.HttpMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DemoClientPoxy implements InvocationHandler {
    private Object obj;

    DemoClientPoxy(Object obj){
        this.obj = obj;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;
        HttpMethod httpMethod = HttpMethod.GET;
        if(method.isAnnotationPresent(GET.class)){

        }


        // http client
        result = method.invoke(this.obj,args);

        return result;
    }
}
