package com.example.demo01.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class QRCode implements Serializable {

    @NotNull(message = "宽度不能为空")
    private Integer width = 350;
    @NotNull(message = "宽度不能为空")
    private Integer higth = 350;
    @NotBlank(message = "内容不能为空")
    private String msg;

    public QRCode(){
        this.msg="数据为空";
    }

    public QRCode(String msg) {
        this.msg = msg;
    }

    public QRCode(Integer width, Integer higth, String msg) {
        this.width = width;
        this.higth = higth;
        this.msg = msg;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHigth() {
        return higth;
    }

    public void setHigth(Integer higth) {
        this.higth = higth;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null? null : msg.trim();
    }
}
