package com.example.demo01.bean;

import lombok.Data;

import java.util.Date;

@Data
public class WeChatMsg {
    private Date time;
    private String type;
    private String msg;

}
