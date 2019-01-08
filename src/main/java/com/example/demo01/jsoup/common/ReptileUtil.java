package com.example.demo01.jsoup.common;

import com.example.demo01.jsoup.bean.IP;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReptileUtil {
    /**
     * @param url
     * @return void 返回类型
     * @throws
     * @Title: saveHtml
     * @Description: 将抓取过来的数据保存到本地或者json文件
     * @author liangchu
     * @date 2017-12-28 下午12:23:05
     */
    public static boolean saveHtml(String url, String path, String fileName) {
        boolean flag = false;
        try {
            // 这是将首页的信息存入到一个html文件中 为了后面分析html文件里面的信息做铺垫
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                flag = true;
            }
            File dest = new File(path + "\\" + fileName);
            // 接收字节输入流
            InputStream is;
            // 字节输出流
            FileOutputStream fos = new FileOutputStream(dest);
            //设置代理
            System.setProperty("http.maxRedirects", "50");
            System.getProperties().setProperty("proxySet", "true");
            // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
            String ip = "11.184.192.49";
            System.getProperties().setProperty("http.proxyHost", ip);
            System.getProperties().setProperty("http.proxyPort", "80");
            URL temp = new URL(url);
            // 这个地方需要加入头部 避免大部分网站拒绝访问
            // 这个地方是容易忽略的地方所以要注意
            URLConnection uc = temp.openConnection();
            // 因为现在很大一部分网站都加入了反爬虫机制 这里加入这个头信息
            uc.addRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 "
                            + "(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) "
                            + "AppleWebKit/533.17.9"
                            + " (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
            is = temp.openStream();
            // 为字节输入流加入缓冲
            BufferedInputStream bis = new BufferedInputStream(is);
            // 为字节输出流加入缓冲
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int length;
            byte[] bytes = new byte[1024 * 20];
            while ((length = bis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
            }
            bos.close();
            fos.close();
            bis.close();
            is.close();
            Thread.sleep(100+G.RANDOM.nextInt(100));
        } catch (IOException e) {
            e.printStackTrace();
            return flag;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static Document getDoc(String url) throws IOException {
        return Jsoup
                .connect(url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(1000 * 30)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .get();
    }

    public static Document postDoc(String url, Map<String, String> data) throws IOException {
        Connection accept = Jsoup.connect(url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(1000 * 30)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        for(Map.Entry<String,String> e : data.entrySet()){
            accept.data(e.getKey(),e.getValue());
        }
        return accept.post();
    }

    /**
     * 获得代理池
     */
    public static List<IP> getIpMap () throws IOException {
        String url = "https://www.kuaidaili.com/free/intr/";
        List<IP> ipList = new ArrayList<>();
        for(int i = 1;i<20;i++){
            url = url+i+"/";
            Document doc = getDoc(url);
            Elements tbody = doc.select("#list").select("tr");
            for(int j=1;j<tbody.size();j++){
                String[] split = tbody.get(i).text().split(" ");
                ipList.add(new IP(split[0],split[1]));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ipList;
    }

    public static void main(String[] args) throws IOException {
        getIpMap();
    }

}
