package com.example.demo01.excel.bean;

import lombok.Data;

@Data
public class Person {
    private String name;
    private String result;

    @Override
    public String toString(){
        return name+"\t"+result;
    }
}
