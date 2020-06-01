package com.example.demo01.pdf;

import com.baidu.aip.ocr.AipOcr;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DemoApplication {
    //设置APPID/AK/SK
    public static final String APP_ID = "18292924";
    public static final String API_KEY = "UIfqNomNnXvfvgneOkmiZHsL";
    public static final String SECRET_KEY = "Hv8QwUTGxTl0IUzBaXvAmGYaqsoqz8Dw";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 解析pdf文档信息
     *
     * @param pdfPath pdf文档路径
     * @throws Exception
     */
    public static void pdfParse(String pdfPath) throws Exception {
        InputStream input = null;
        File pdfFile = new File(pdfPath);
        PDDocument document = null;
        try {
            input = new FileInputStream(pdfFile);
        //加载 pdf 文档
        document = PDDocument.load(input);

        /** 文档属性信息 **/
        PDDocumentInformation info = document.getDocumentInformation();
        System.out.println("标题:" + info.getTitle());
        System.out.println("主题:" + info.getSubject());
        System.out.println("作者:" + info.getAuthor());
        System.out.println("关键字:" + info.getKeywords());

        System.out.println("应用程序:" + info.getCreator());
        System.out.println("pdf 制作程序:" + info.getProducer());

        System.out.println("作者:" + info.getTrapped());

        System.out.println("创建时间:" + dateFormat(info.getCreationDate()));
        System.out.println("修改时间:" + dateFormat(info.getModificationDate()));


        //获取内容信息
        PDFTextStripper pts = new PDFTextStripper();
        String content = pts.getText(document);
        System.out.println("内容:" + content);


        /** 文档页面信息 **/
        PDDocumentCatalog cata = document.getDocumentCatalog();
        PDPageTree pages = cata.getPages();
        System.out.println(pages.getCount());
        int count = 1;

        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        //todo
//            XWPFDocument doc;
//            doc = new XWPFDocument();
//            Map<String,String> param = Maps.newHashMap();
//            param.put("grant_type","client_credentials");
//            param.put("client_id",API_KEY);
//            param.put("client_secret",SECRET_KEY);
//            String post = HttpClientUtil.post("https://aip.baidubce.com/oauth/2.0/token", param);
//            JSONObject token =  new JSONObject(post);
//            System.out.println(token.toString(2));
        StringBuilder sb = new StringBuilder();
        outsize:
        for (int i = 0; i < pages.getCount(); i++) {
            PDPage page = (PDPage) pages.get(i);
            if (null != page) {
                PDResources res = page.getResources();
                Iterable xobjects = res.getXObjectNames();
                if(xobjects != null){
                    Iterator imageIter = xobjects.iterator();
                    while(imageIter.hasNext()){
                        COSName key = (COSName) imageIter.next();
                        if (res.isImageXObject(key)) {
                            try {
                                PDImageXObject image = (PDImageXObject) res.getXObject(key);
                                BufferedImage bimage = image.getImage();
                                // 将BufferImage转换成字节数组
                                ByteArrayOutputStream out =new ByteArrayOutputStream();
                                ImageIO.write(bimage,"png",out);//png 为要保存的图片格式
                                byte[] barray = out.toByteArray();
                                out.close();
                                // 发送图片识别请求
                                JSONObject json = client.basicGeneral(barray, new HashMap<String, String>());
                                System.out.println(json.toString(2));
//                                    if(json.has("words_result")){
//                                        JSONArray words_result = json.getJSONArray("words_result");
//                                        for(Object j :words_result) {
//                                            JSONObject jsonObject = (JSONObject)j;
//                                            if(jsonObject.has("words")){
//                                                Object words = jsonObject.get("words");
//                                                String s = words.toString();
//                                                sb.append(s);
//                                                if(s.contains("。")){
//                                                    sb.append(s, 0, s.indexOf("。")+1);
//                                                    System.out.println(sb.toString());
//                                                    sb.setLength(0);
//                                                    sb.append(s, s.indexOf("。")+1, s.length());
//                                                }else {
//                                                    if(s.length()<10){
//                                                        System.out.println(sb.toString());
//                                                        sb.setLength(0);
//                                                    }else {
//                                                        sb.append(s);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        XWPFParagraph p = doc.createParagraph();
//                                        XWPFRun run = p.createRun();
//                                        run.setText(sb.toString());
//                                        run.addBreak(BreakType.PAGE);
//                                    }
                                count++;
                                System.out.println(count);
                                if(count>=5) break outsize;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
//            FileOutputStream out = new FileOutputStream("F:\\Temp\\无从选择.docx");
//            doc.write(out);
//            out.close();
//            System.out.println("Document converted successfully");
    } catch (Exception e) {
        throw e;
    } finally {
        if (null != input)
            input.close();
        if (null != document)
            document.close();
    }
    }

    /**
     * 获取格式化后的时间信息
     *
     * @return
     * @throws Exception
     */
    public static String dateFormat(Calendar calendar) throws Exception {
        if (null == calendar)
            return null;
        String date = null;
        try {
            String pattern = DATE_FORMAT;
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.format(calendar.getTime());
        } catch (Exception e) {
            throw e;
        }
        return date == null ? "" : date;
    }

    public static void main(String[] args) throws Exception {

        // 读取pdf文件
//        String path = "F:\\Temp\\无从选择.pdf";
        String path = "F:\\Temp\\2015年审计报告扫描版.pdf";
        pdfParse(path);

    }

}