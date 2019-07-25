package com.example.demo01.jsoup.support;

import com.example.demo01.jsoup.bean.IP;
import com.example.demo01.jsoup.common.G;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    private static final News NEWS = new News();
    private static final HistoricalData HISTORICAL_DATA = new HistoricalData();


    /**
     * 获取每天开盘的所有股票
     * param 总页数
     * return String [] 股票代码
     */
    public  String [] getCodeList(int hs, int sz, List<IP> list){
        StringBuilder sb = new StringBuilder();
        try {
            //获取沪市所有开盘的代码
            for(int i = 1;i<hs+1;i++){
                NEWS.basicInfo(G.HS_URL+i,sb);
                System.out.println("沪市共计"+hs+"页，已完成："+i);
                Thread.sleep(1000+G.RANDOM.nextInt(1000));
            }
            //获取深市所有开盘的代码
            for(int i = 1;i<sz+1;i++){
                NEWS.basicInfo(G.SZ_URL+i,sb);
                System.out.println("深市共计"+sz+"页，已完成："+i);
                Thread.sleep(1000+G.RANDOM.nextInt(1000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().split("_");
    }

    /**
     * 查询公司基本信息
     *
     * @param stockCode
     * @return
     * @throws IOException
     */
    private Map<String, String> basicInfo(String stockCode) throws IOException {
        Map<String, String> map = new HashMap<String, String>();

        String url = "";

        if (stockCode.startsWith("6") || stockCode.startsWith("7")) {
            url = "http://quote.eastmoney.com/sh" + stockCode + ".html";
        } else if (stockCode.startsWith("0") || stockCode.startsWith("3")) {
            url = "http://quote.eastmoney.com/sz" + stockCode + ".html";
        }

        Document doc = Jsoup
                .connect(url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(1000 * 30)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("accept", "test/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .get();
        Element table = doc.getElementsByClass("pad5").get(0);
        Element tbody = table.getElementsByTag("tbody").get(0);
        Elements trs = tbody.getElementsByTag("tr");

        for (Element e : trs) {
            Element td = e.getElementsByTag("tr").get(0).getElementsByTag("td").get(0);
            if (td.html().contains("hxsjccsyl")) {// 净资产收益率
                map.put("净资产收益率", td.html().split("：")[1]);
            } else if (td.html().contains("href")) {
                map.put(td.getElementsByTag("a").get(0).html(), td.html().split("：")[1]);
            } else {
                String[] temp = td.html().split("：");
                map.put(temp[0], temp[1]);
            }

        }
        return map;
    }

    /**
     * 获取公司上市时间
     */
    public    String getCreateTime (String code){
        Map<String, String> map = null;
        String time = "";
        try {
            map = basicInfo(code);
            time = map.get("上市时间");
        } catch (IOException e) {
            System.out.println("代码："+code+",获取上市日期失败");
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 下载并保存每天的数据
     *  有历史数据 false 无历史数据 true
     */
    public  boolean  updateToday( String stockCode,String time){
        return  NEWS.updateToday(stockCode,time);
    }

    /**
     * 文件夹改名
     */
    public  void reName(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        Assert.notNull(files,"该文件夹下没有子文件夹，请确认文件路径是否正确。");
        Arrays.stream(files).filter(File::isDirectory).forEach(f -> {
            String name = f.getName().substring(0,f.getName().indexOf("_")+1);
            boolean b = f.renameTo(new File(path + "\\" + name));
            System.out.println("文件："+name+"，改名："+b);
        });
    }

    /**
     * 获取历史数据
     * @param code
     * @param createTime
     */
    public void getHistoricalData(String code, String createTime) {
        HISTORICAL_DATA.get(code,createTime);
    }
}
