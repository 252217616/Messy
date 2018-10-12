package com.example.demo01.digitalRecognition;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;

/*
 依赖包
 <!--图片识别-->
        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>jna</artifactId>
            <version>4.1.0</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.tess4j</groupId>
            <artifactId>tess4j</artifactId>
            <version>2.0.1</version>
            <exclusions>
                <exclusion>
                    <groupId>com.sun.jna</groupId>
                    <artifactId>jna</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
 */
public class VerificationCodeDistinguish {
    public static void main(String[] args) throws Exception {
//        downloadJPG(HttpClient.post("验证码url", "time=" + System.currentTimeMillis()),"1.jpg");
        String code = getImgContent("D:\\test\\验证码\\5.jpg");
        System.out.println("验证码 = " + code);
    }
//    protected static void downloadJPG(HttpResponse httpResponse, String fileName) throws IOException {
//        InputStream input = httpResponse.getEntity().getContent();
//        OutputStream output = new FileOutputStream(new File(fileName));
//        IOUtils.copy(input, output);
//        if (output != null) {
//            output.close();
//        }
//        output.flush();
//    }

    protected static String getImgContent(String imgUrl) {
        String content = "";
        File imageFile = new File(imgUrl);
        //读取图片数字
        ITesseract instance = new Tesseract();


        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确
        instance.setDatapath(tessDataFolder.getAbsolutePath());


        try {
            content = instance.doOCR(imageFile).replace("\n", "");
            System.out.println(content);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return content;
    }
}

