package com.example.demo01.jsoup.support;

import com.example.demo01.jsoup.bean.HistoricalStock;
import com.example.demo01.jsoup.bean.Stock;
import com.example.demo01.jsoup.common.G;
import com.example.demo01.jsoup.common.ReptileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HistoricalData {

    /**
     * 获取历史数据
     */
    public  void get(String code,String time){
        boolean flag = true;
        StringBuilder sb = new StringBuilder();
        String [] times = time.split("-");
        int year = Integer.parseInt(times[0]);
        int mouth = Integer.parseInt(times[1]);
        int jidu = mouth<=3?1:mouth<=6?2:mouth<=9?3:mouth<=12?4:0;
        for (int i = year; i < 2020; i++) {
            for (int j = 1; j < 5; j++) {
                if(flag){
                    j = jidu;
                    flag = false;
                }
                String url = sb.append(G.H_URL).append(code).append(G.H_CODE).append(i).append(G.H_JD).append(j).toString();
                sb.setLength(0);
                String file = sb.append(i).append("-").append(j).append(".html").toString();
                sb.setLength(0);
                String dir = sb.append(G.FILE_PATH).append(code).toString();
                sb.setLength(0);
                ReptileUtil.saveHtml(url,dir, file);
                try {
                    Thread.sleep(1000+G.RANDOM.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 解析历史数据
     */
    public void Analysis (File file){
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 获取所有内容 获取新闻内容
        Elements table = doc.select("#FundHoldSharesTable");
        Elements trs = table.select("tr");
        for(int i = 2 ;i<trs.size();i++){
            String text = trs.get(i).text();
            String[] split = text.split(" ");
            HistoricalStock historicalStock = new HistoricalStock(split);
            System.out.println(historicalStock);

        }
    }

    public static void main(String[] args) {
       //解析所有历史数据
        HistoricalData h = new HistoricalData();
        File file = new File("D:\\tmp");
        File[] files = file.listFiles();
        Assert.notNull(files,"没有文件");
        Arrays.asList(files).parallelStream().forEach(f->{
            File[] subList = f.listFiles();
            Assert.notNull(subList,"没有文件");
            Arrays.stream(subList).forEach(h::Analysis);
        });

    }

}
