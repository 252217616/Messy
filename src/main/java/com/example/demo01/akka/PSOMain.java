package com.example.demo01.akka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.demo01.akka.bird.Bird;
import com.example.demo01.akka.bird.MasterBird;
import com.typesafe.config.ConfigFactory;

public class PSOMain {
    public static final int BIRD_COUNT = 100000;

    public static void main(String[] args) {

        ActorSystem psoSystem = ActorSystem.create("psoSystem", ConfigFactory.load("samplehello.conf"));
        psoSystem.actorOf(Props.create(MasterBird.class),"masterbird");
        for(int i =0;i<BIRD_COUNT;i++){
            psoSystem.actorOf(Props.create(Bird.class),"bird"+i);
        }

    }
}
