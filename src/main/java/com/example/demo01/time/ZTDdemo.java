package com.example.demo01.time;

import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZTDdemo {
    public static void main(String[] args) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime zonedDateTime1 = zonedDateTime.minus(Period.of(1,1,1));

        System.out.println("计算后时间"+zonedDateTime1);
        String format = zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm:ss"));
        System.out.println("现在时间："+format);
        System.out.println(ZonedDateTime.now());
    }
}
