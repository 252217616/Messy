package com.example.demo01.stock.akka.bird;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.demo01.stock.akka.bean.GBestMsg;
import com.example.demo01.stock.akka.bean.PBestMsg;

public class MasterBird extends UntypedActor {
    //日志打印
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);
    //全局最优解
    private GBestMsg gBest = null;


    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof PBestMsg){
            PBestMsg pBest = (PBestMsg) msg;
            if(gBest == null || pBest.getCgl().compareTo(gBest.getCgl())>0){
                //更新全局最优解，通知所有粒子
                System.out.println("最优解："+msg);
                GBestMsg gBestMsg = new GBestMsg();
                gBestMsg.setCgl(pBest.getCgl());
                gBestMsg.setRate(pBest.getRate());
                gBestMsg.setSeed(pBest.getSeed());
                gBestMsg.setWucha(pBest.getWucha());
                gBestMsg.setZhunqueCount(pBest.getZhunqueCount());
                gBest =gBestMsg;
//                ActorSelection selection = getContext().actorSelection("/user/bird_*");
//                selection.tell(gBestMsg,getSelf());
            }
        }else {
            unhandled(msg);
        }
    }
}
