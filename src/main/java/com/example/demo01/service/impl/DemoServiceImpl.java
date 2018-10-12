package com.example.demo01.service.impl;

import com.example.demo01.service.DemoService;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {

    private ThreadLocal<Integer> mun = new ThreadLocal<Integer>();

    public Integer add (){
        return null;

    }

    @Override
    public void add(Integer in) {
        Integer a = in;

        System.out.println(in+"===:==="+mun);
        mun.remove();
    }

    public Integer getMun() {
        return null;
    }

    public void setMun(Integer in) {
        mun.set(in);
    }
}
