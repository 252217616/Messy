package com.example.demo01.stock.akka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.demo01.stock.akka.bean.GResource;
import com.example.demo01.stock.akka.bird.Bird;
import com.example.demo01.stock.akka.bird.MasterBird;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public class PSOMain {
    public static final int BIRD_COUNT = 10;
    public static final String CODE= "000606";
    public static GResource resource= new GResource();

    public static void main(String[] args) throws IOException {
        File[] files = new File("D:\\data\\qhLearn\\" + CODE + "\\textArff").listFiles();
        resource.setArffFiles(files);
        File data = new File("D:\\data\\learn\\" + CODE + "_.txt");
        StringBuilder sb = new StringBuilder();
        FileUtils.readToBuffer(sb, data);
        String[] split = sb.toString().split("\\|");
        resource.setData(split);
        ActorSystem psoSystem = ActorSystem.create("psoSystem", ConfigFactory.load("samplehello.conf"));
        psoSystem.actorOf(Props.create(MasterBird.class),"masterbird");
        for(int i =0;i<BIRD_COUNT;i++){
            System.out.println("创建小鸟："+i+"号");
            psoSystem.actorOf(Props.create(Bird.class),"bird"+i);
        }

    }
}
