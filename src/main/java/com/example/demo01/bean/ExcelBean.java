package com.example.demo01.bean;


import com.example.demo01.excel.ExcelHead;

import java.util.Date;

public class ExcelBean {

    @ExcelHead(value = "姓名")
    private String name;
    @ExcelHead(value = "电话号码")
    private String phoneNum;
    @ExcelHead(value = "年龄")
    private String age;
    @ExcelHead(value = "交易摘要")
    private String summary;//交易摘要
    @ExcelHead(value = "备注")
    private String remarks;//备注
    @ExcelHead(value = "入职时间")
    private Date date;//入职时间

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "ExcelBean{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", age='" + age + '\'' +
                ", summary='" + summary + '\'' +
                ", remarks='" + remarks + '\'' +
                ", date=" + date +
                '}';
    }
}
