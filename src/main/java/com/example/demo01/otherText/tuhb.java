package com.example.demo01.otherText;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class tuhb {
    private static List<String> list  = new ArrayList<>();

    public static void main(String[] args) {

        BufferedImage bi_0 = null;

        //读取文件
            String imageaddr1 = "D:\\test\\image\\zhu.jpg";
            String imageaddr2 = "D:\\test\\image\\jian.jpg";
            String imageaddr3 = "D:\\test\\image\\zhu.jpg";
            String imageaddr4 = "D:\\test\\image\\jian.jpg";
            String imageaddr5 = "D:\\test\\image\\zhu.jpg";
            String imageaddr6 = "D:\\test\\image\\jian.jpg";
            String imageaddr7 = "D:\\test\\image\\zhu.jpg";
            String imageaddr8 = "D:\\test\\image\\jian.jpg";
            list.add(imageaddr1);
            list.add(imageaddr2);
            list.add(imageaddr3);
            list.add(imageaddr4);
            list.add(imageaddr5);
            list.add(imageaddr6);
            list.add(imageaddr7);
            list.add(imageaddr8);

        //假设图片0 和图片1 宽度相同，上下合成
        //new 一个新的图像
        int num = list.size();
        int w = 1200;
        int h = 1200*((num/5)+1);
        if(num >= 5){
            w *= 5;
        }else{
            w *= num;
        }


        //解决图片变红问题及白色背景
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.OPAQUE);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.drawImage(bi,0,0,null);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, w, h);
        g2.drawImage(bi,0,0,null);



        int a = 0;
        int b  =0;
        //像素一个一个复制过来
        try {
            for (int i = 0; i < list.size();i++){
                BufferedImage target = ImageIO.read(new File(list.get(i)));
                System.out.println("W"+target.getWidth());
                System.out.println("H"+target.getHeight());
                System.out.println("WW"+bi.getWidth());
                System.out.println("HH"+bi.getHeight());
                for (int y = 0 ; y < target.getHeight(); y++) {
                    for (int x = 0; x < target.getWidth(); x++) {
                        a=(i%5)*1200+x;
                        b=((i/5)*1200)+y;
                        bi.setRGB(a, b, target.getRGB(x, y));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("a:"+a+" b:"+b);
        }

        //输出文件
        try {
            ImageIO.write(bi, "JPEG", new File("D:\\test\\image\\0+1.jpg"));
        } catch (IOException ex) {
        }
    }

    /**
     * 解决图片变红问题
     * @param copy2
     * @return
     */
    public static BufferedImage Remove_alpha_channel(BufferedImage copy2) {
        BufferedImage image = copy2;
        BufferedImage imageRGB = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.OPAQUE);
        Graphics2D graphics = imageRGB.createGraphics();
        graphics.drawImage(image,0,0,null);

        return imageRGB;
    }
}


