package com.example.demo01.otherText;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class tuhb {
    private static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        File f = new File("C:\\Users\\卢囧囧\\Pictures\\Camera Roll\\eJ5HNPzdWapP8k4zYyZEETRoHtKalW.png");
        addWaterMark(f, "测试水印内容");
//
//        BufferedImage bi_0 = null;
//
//        //读取文件
//            String imageaddr1 = "D:\\test\\image\\zhu.jpg";
//            String imageaddr2 = "D:\\test\\image\\jian.jpg";
//            String imageaddr3 = "D:\\test\\image\\zhu.jpg";
//            String imageaddr4 = "D:\\test\\image\\jian.jpg";
//            String imageaddr5 = "D:\\test\\image\\zhu.jpg";
//            String imageaddr6 = "D:\\test\\image\\jian.jpg";
//            String imageaddr7 = "D:\\test\\image\\zhu.jpg";
//            String imageaddr8 = "D:\\test\\image\\jian.jpg";
//            list.add(imageaddr1);
//            list.add(imageaddr2);
//            list.add(imageaddr3);
//            list.add(imageaddr4);
//            list.add(imageaddr5);
//            list.add(imageaddr6);
//            list.add(imageaddr7);
//            list.add(imageaddr8);
//
//        //假设图片0 和图片1 宽度相同，上下合成
//        //new 一个新的图像
//        int num = list.size();
//        int w = 1200;
//        int h = 1200*((num/5)+1);
//        if(num >= 5){
//            w *= 5;
//        }else{
//            w *= num;
//        }
//
//
//        //解决图片变红问题及白色背景
//        BufferedImage bi = new BufferedImage(w, h, BufferedImage.OPAQUE);
//        Graphics2D g2 = (Graphics2D)bi.getGraphics();
//        g2.drawImage(bi,0,0,null);
//        g2.setBackground(Color.WHITE);
//        g2.clearRect(0, 0, w, h);
//        g2.drawImage(bi,0,0,null);
//
//
//
//        int a = 0;
//        int b  =0;
//        //像素一个一个复制过来
//        try {
//            for (int i = 0; i < list.size();i++){
//                BufferedImage target = ImageIO.read(new File(list.get(i)));
//                System.out.println("W"+target.getWidth());
//                System.out.println("H"+target.getHeight());
//                System.out.println("WW"+bi.getWidth());
//                System.out.println("HH"+bi.getHeight());
//                for (int y = 0 ; y < target.getHeight(); y++) {
//                    for (int x = 0; x < target.getWidth(); x++) {
//                        a=(i%5)*1200+x;
//                        b=((i/5)*1200)+y;
//                        bi.setRGB(a, b, target.getRGB(x, y));
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("a:"+a+" b:"+b);
//        }
//
//        //输出文件
//        try {
//            ImageIO.write(bi, "JPEG", new File("D:\\test\\image\\0+1.jpg"));
//        } catch (IOException ex) {
//        }
    }

    /**
     * 解决图片变红问题
     *
     * @param copy2
     * @return
     */
    public static BufferedImage Remove_alpha_channel(BufferedImage copy2) {
        BufferedImage image = copy2;
        BufferedImage imageRGB = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = imageRGB.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        return imageRGB;
    }

    /**
     * 图片增加水印
     */
    public static void addWaterMark(File srcImgFile, String content) {
        try {
                    // 读取原图片信息
            Image srcImg = ImageIO.read(srcImgFile);// 文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);// 获取图片的高
            int min = Math.min(srcImgWidth,srcImgHeight);
            int fontSize = min/(2*content.length())>50?50:min/(2*content.length());
            Font font = new Font("微软雅黑", Font.PLAIN, fontSize<20?20:fontSize);

            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(new Color(0, 0, 0, 50)); // 根据图片的背景设置水印颜色
            g.setFont(font); // 设置字体
            // 设置水印的坐标
            int max = Math.max(srcImgHeight,srcImgWidth);
            g.rotate(Math.toRadians(330), (double)max/2 ,  (double)max/2 );
            int gap =font.getSize() * content.length()+4*fontSize ;
            int first ;
            for (int i = -4*fontSize; i < max+4*fontSize; i += 2*fontSize) {
                if(i%(4*fontSize) == 0){
                    first = 4*fontSize;
                }else {
                    first = 0;
                }
                for (int j = -4*fontSize; j <  max; j += gap) {
                    g.drawString(content, 0.0f + j+first, i); // 画出水印
                }
            }
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream("F:\\test.jpg");
            ImageIO.write(bufImg, "jpg", outImgStream);
            System.out.println("添加水印完成");
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
}


