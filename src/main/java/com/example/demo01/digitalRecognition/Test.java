package com.example.demo01.digitalRecognition;

import java.io.File;


public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        OCR ocr = new OCR();
        try {
            String maybe2 = new OCR().recognizeText(new File("D:\\test\\验证码\\11.png"), "png");
            System.out.println("正在生成中，请稍等");
            System.out.println(maybe2);
            System.out.println("**********");
            //MyString str=new MyString();
            //System.out.println(str.getString(maybe2));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		SoundServer s=new SoundServer();
//		s.playSound("E:\\111\\HOOK1.wav");
    }

}
