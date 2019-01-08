package com.example.demo01.jsoup.support;

import com.example.demo01.jsoup.bean.HistoricalStock;
import com.example.demo01.jsoup.bean.Stock;
import com.example.demo01.jsoup.common.G;
import com.example.demo01.jsoup.common.ReptileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理每日数据--抓取/解析
 */
public class News {
    /**
     * 查询每日开盘的所有股票
     */
    public  void basicInfo(String url,StringBuilder sb) throws IOException {
        Document doc = ReptileUtil.getDoc(url);
        Element table = doc.getElementsByTag("table").get(0);
        Element tbody = table.getElementsByTag("tbody").get(0);
        Elements trs = tbody.getElementsByTag("tr");
        for (Element e : trs) {
            String text = e.text();
            String[] split = text.split(" ");
            if(split[0].matches("[0-9]+")){
                //0 代码，1 名称，5 成交量（手） 6 成交额 7开盘价 8收盘价 9最低价 10最高价
                sb.append(split[0]);
                sb.append("_");
            }
        }
    }

    /**
     * 下载并保存每天的数据
     *  有历史数据 false 无历史数据 true
     */
    public  boolean  updateToday( String stockCode){
        String url = G.TODAY_URL;
        if (stockCode.startsWith("6") || stockCode.startsWith("7")) {
            url = url+"S/SH" + stockCode;
        } else if (stockCode.startsWith("0") || stockCode.startsWith("3")) {
            url = url+"S/SZ" + stockCode;
        }
        String dir = G.FILE_PATH+stockCode;
        return  ReptileUtil.saveHtml(url, dir, G.TODAY_NAME);
    }

    /**
     * 解析每日数据
     * 雪球
     */
    public void Analysis (File file,String time){
        // 开始解析文件
        StringBuilder sb = new StringBuilder();
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Elements table = doc.select(".quote-info");
        String price = doc.select(".stock-current").text();
        sb.append("价格：");
        sb.append(price.substring(1));
        sb.append(" ");
        Elements trs = table.select("tr");
        for (Element tr : trs) {
            String text = tr.text();
            sb.append(text);
            sb.append(" ");
        }
        String[] split = sb.toString().split(" ");
        Map<String,String> data = new HashMap<>(32);
        for(int i = 0;i<split.length;i++){
            String[] param = split[i].split("：");
            data.put(param[0],param[1]);
        }
        Stock stock = new Stock(data);
        HistoricalStock historicalStock = new HistoricalStock(stock,time);
        //todo 入库
        System.out.println(sb.toString());
        System.out.println(stock);
        System.out.println(historicalStock);
    }

    public static void main(String[] args) {
        News news = new News();
        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
        news.Analysis(new File("D:\\tmp\\market\\488\\112.html"),time);
        news.Analysis(new File("D:\\tmp\\market\\488\\113.html"),time);
    }
}
