package com.example.demo01.akka.bird;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.demo01.akka.Fitness;
import com.example.demo01.akka.bean.GBestMsg;
import com.example.demo01.akka.bean.PBestMsg;
import com.example.demo01.akka.bean.PsoValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bird extends UntypedActor {
    //日志打印
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);
    //这个粒子最优解
    private PsoValue pBest = null;
    //全局最优解
    private PsoValue gBest = null;
    //表示各个维度上的速度
    private List<Double> velocity = new ArrayList<Double>();
    //投资方案
    private List<Double> result = new ArrayList<Double>();
    private Random r = new Random();

    /**
     * 粒子初始化--确定当前投资方案
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        //全部塞满负无穷
        for(int i = 0;i<5;i++){
            velocity.add(Double.NEGATIVE_INFINITY);
            result.add(Double.NEGATIVE_INFINITY);
        }
        //第一年
        result.set(1,(double)r.nextInt(401));
        //第二年
        double max = 440-1.1*result.get(1);
        if (max<0) max=0;//如第一年全存银行
        result.set(2,r.nextDouble()*max);
        //第三年
        max = 484-1.21*result.get(1)-1.1*result.get(2);
        if (max<0) max=0;
        result.set(3,r.nextDouble()*max);
        //第四年
        max = 532-1.331*result.get(1)-1.21*result.get(2)-1.1*result.get(3);
        if (max<0) max=0;
        result.set(4,r.nextDouble()*max);

        //每个值开方后累加
        double newFit = Fitness.fitness(result);
        pBest = new PsoValue(newFit,result);
        PBestMsg pBestMsg = new PBestMsg(pBest);
        ActorSelection selection = getContext().actorSelection("/user/masterbird");
        //将自身结果发送给管理员
        selection.tell(pBestMsg,getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof GBestMsg){
            gBest = ((GBestMsg) msg).getValue();
            //将最优解更新至自身
            for(int i=1;i<velocity.size();i++){
                updateVeloctiy(i);
            }
            for(int i=1;i<result.size();i++){
                updateResult(i);
            }
            //校验结果
            validareResult();
            double newFit = Fitness.fitness(result);
            //如果优于最优解
            if(newFit>pBest.getValue()){
                pBest = new PsoValue(newFit,result);
                PBestMsg pBestMsg = new PBestMsg(pBest);
                getSender().tell(pBestMsg,getSelf());
            }
        }else {
            unhandled(msg);
        }

    }

    /**
     * 位置更新根据标准粒子群实现
     * @param i
     */
    private double updateVeloctiy(int i) {
        double v = Math.random()*velocity.get(i)
                +2*Math.random()*(pBest.getResult().get(i)-result.get(i))
                +2*Math.random()*(gBest.getResult().get(i)-result.get(i));
        v = v>0?Math.max(v,5):Math.max(v,-5);
        velocity.set(i,v);
        return v;
    }
    /**
     * 更新投资
     * @param i
     */
    private double updateResult(int i){
        double newResult = result.get(i)+velocity.get(i);
        result.set(i,newResult);
        return newResult;
    }

    /**
     * 校验结果避免出界
     */
    private void validareResult(){
        //第一年
        if(result.get(1)>400){
            result.set(1,(double)r.nextInt(401));
        }
        //第二年
        double max = 440-1.1*result.get(1);
        if(result.get(2)>max || result.get(2)<0){
            result.set(2,r.nextDouble()*max);
        }
        //第三年
        max = 484-1.21*result.get(1)-1.1*result.get(2);
        if(result.get(3)>max || result.get(3)<0){
            result.set(3,r.nextDouble()*max);
        }
        //第四年
        max = 532-1.331*result.get(1)-1.21*result.get(2)-1.1*result.get(3);
        if(result.get(4)>max || result.get(4)<0){
            result.set(4,r.nextDouble()*max);
        }
    }
}
