package com.example.demo01.jsoup.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class News {

    public static void main(String[] args) throws IOException, InterruptedException {
        String hsUrl = "http://app.finance.ifeng.com/list/stock.php?t=ha&f=chg_pct&o=desc&p=";
        String szUrl = "http://app.finance.ifeng.com/list/stock.php?t=sa&f=chg_pct&o=desc&p=";
        StringBuilder sb = new StringBuilder();
        for(int i = 1;i<44;i++){
            basicInfo(szUrl+i,sb);
            Thread.sleep(2000);
        }
        System.out.println(sb.toString());
    }
    /**
     * 查询公司基本信息
     *
     * @param stockCode
     * @return
     * @throws IOException
     */
    public static void basicInfo(String url,StringBuilder sb) throws IOException {
        Map<String, String> map = new HashMap<String, String>();



        Document doc = Jsoup
                .connect(url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(1000 * 30)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .get();
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
                sb.append(split[1]);
                sb.append("|");
            }


        }
    }
}
