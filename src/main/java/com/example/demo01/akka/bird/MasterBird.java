package com.example.demo01.akka.bird;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.demo01.akka.bean.GBestMsg;
import com.example.demo01.akka.bean.PBestMsg;
import com.example.demo01.akka.bean.PsoValue;

public class MasterBird extends UntypedActor {
    //日志打印
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);
    //全局最优解
    private PsoValue gBest = null;


    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof PBestMsg){
            PsoValue pBest = ((PBestMsg) msg).getValue();
            if(gBest == null || gBest.getValue()<pBest.getValue()){
                //更新全局最优解，通知所有粒子
                System.out.println(msg);
                gBest = pBest;
                ActorSelection selection = getContext().actorSelection("/user/bird_*");
                selection.tell(new GBestMsg(gBest),getSelf());
            }
        }else {
            unhandled(msg);
        }
    }
}
