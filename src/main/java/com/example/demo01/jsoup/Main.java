package com.example.demo01.jsoup;

import com.example.demo01.jsoup.support.Handler;
import com.example.demo01.jsoup.support.HistoricalData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Handler handler = new Handler();
        //获取每天开盘的所有股票代码
        String[] codeList = handler.getCodeList(0,0);
        //查询当日信息
        for (String code : codeList){
            //下载并检查是否有历史数据
            boolean isHistorical = handler.updateToday(code);
            if(isHistorical){ //无历史数据
                //查询该股票上市日期
                String createTime = handler.getCreateTime(code);
                //下载历史数据
                handler.getHistoricalData(code,createTime);
                //todo 解析并保存历史数据
            }
            //todo 解析并保存今日数据
        }
    }



}
