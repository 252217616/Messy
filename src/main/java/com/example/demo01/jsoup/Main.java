package com.example.demo01.jsoup;

import com.example.demo01.jsoup.bean.IP;
import com.example.demo01.jsoup.common.ReptileUtil;
import com.example.demo01.jsoup.support.Handler;
import com.example.demo01.jsoup.support.HistoricalData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Handler handler = new Handler();
        List<IP> ipMap = ReptileUtil.getIpMap();
        //获取每天开盘的所有股票代码
        String[] codeList = handler.getCodeList(29,43,ipMap);
        int sum = codeList.length;
        int count = 0;
        //查询当日信息
        for (String code : codeList){
            //下载并检查是否有历史数据
            boolean isHistorical = handler.updateToday(code,time);
            count++;
            System.out.println("共需要下载："+sum+"个，已完成："+count+"个，名称："+code);

//            if(isHistorical){ //无历史数据
//                //查询该股票上市日期
//                String createTime = handler.getCreateTime(code);
//                //下载历史数据
//                handler.getHistoricalData(code,createTime);
//                //todo 解析并保存历史数据
//            }
            //todo 解析并保存今日数据
        }
    }



}
