package com.example.demo01.stock.akka.bird;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.demo01.stock.akka.Fitness;
import com.example.demo01.stock.akka.PSOMain;
import com.example.demo01.stock.akka.bean.GBestMsg;
import com.example.demo01.stock.akka.bean.PBestMsg;
import com.google.common.collect.Lists;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.*;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bird extends UntypedActor {
    //日志打印
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);
    //这个粒子最优解
    private PBestMsg pBest = null;
    //全局最优解
    private GBestMsg gBest = null;
    private Random r = new Random();

    /**
     * 粒子初始化--确定当前投资方案
     * 自定义
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {

        while (true){
            PBestMsg bestSeedAndRate = getBestSeedAndRate();
            ActorSelection selection = getContext().actorSelection("/user/masterbird");
            //将自身结果发送给管理员
            selection.tell(bestSeedAndRate,getSelf());
        }

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        System.out.println("接到消息");
        if(msg instanceof GBestMsg){
            gBest = (GBestMsg) msg;
            PBestMsg best = getBestSeedAndRate();
            //如果优于最优解
            if(best.getCgl().compareTo(gBest.getCgl())>0 && best.getWucha().compareTo(gBest.getWucha())<0){
                getSender().tell(best,getSelf());
            }
        }else {
            unhandled(msg);
        }

    }

  private PBestMsg getBestSeedAndRate() throws IOException {
      File [] files = PSOMain.resource.getArffFiles();
      String[] split = PSOMain.resource.getData();
      int len = split.length;
      List<BigDecimal> val = Lists.newArrayList();
      int randomSeed = r.nextInt(100);
      int count = 0;
      int cglcount = 0;
      int sum = 0;
      double randomRate = r.nextDouble();
      for (File file : files) {
          sum++;
          int num = Integer.parseInt(file.getName().substring(file.getName().indexOf("_") + 1, file.getName().indexOf(".")));
          String[] yuceDate = split[len - num].split("_");
          double[] param = {Double.parseDouble(yuceDate[1]), Double.parseDouble(yuceDate[2]), Double.parseDouble(yuceDate[3]), Double.parseDouble(yuceDate[4]), Double.parseDouble(yuceDate[5])};
          BigDecimal result = new BigDecimal(split[len - num+1].split("_")[4]);
          BigDecimal qiantian = new BigDecimal(split[len - num - 1].split("_")[4]);
          double v = execTest(param, file, randomSeed, randomRate);
          BigDecimal yuce = new BigDecimal(v + "");
          BigDecimal wucha = yuce.subtract(result).abs().divide(result, 4, BigDecimal.ROUND_HALF_UP);
          val.add(wucha);
          if (wucha.compareTo(new BigDecimal("0.01")) < 0) {
              count++;
          }
          if ((result.subtract(qiantian)).multiply(yuce.subtract(qiantian)).compareTo(BigDecimal.ZERO) > 0) {
              cglcount++;
          }
      }
      BigDecimal cgl = new BigDecimal(cglcount).divide(new BigDecimal(sum), 4, BigDecimal.ROUND_HALF_UP);
      BigDecimal pjwc = Fitness.getVal(val);
      PBestMsg pBestMsg = new PBestMsg();
      pBestMsg.setRate(randomRate);
      pBestMsg.setSeed(randomSeed);
      pBestMsg.setWucha(pjwc);
      pBestMsg.setCgl(cgl);
      pBestMsg.setZhunqueCount(count);

      return pBestMsg;
  }

    public static double execTest(double[] param, File inputFile, int seed, double rate) throws IOException {

        //读入训练集数据
        ArffLoader atf = new ArffLoader();
        try {
            atf.setFile(inputFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Instances instancesTrain = atf.getDataSet();
        instancesTrain.setClassIndex(0);//设置训练数据集的类属性，即对哪个数据列进行预测（属性的下标从0开始）


        //读入测试集数据
        FastVector attrs = new FastVector();
        Attribute open = new Attribute("open", 1);//创建属性，参数为属性名称和属性号，但属性号并不影响FastVector中属性的顺序
        Attribute high = new Attribute("high", 2);
        Attribute low = new Attribute("low", 3);
        Attribute close = new Attribute("close", 4);
        Attribute jyl = new Attribute("jyl", 5);
        Attribute tclose = new Attribute("tclose", 6);
        attrs.addElement(open);//向FastVector中添加属性，属性在FastVector中的顺序由添加的先后顺序确定。
        attrs.addElement(high);
        attrs.addElement(low);
        attrs.addElement(close);
        attrs.addElement(jyl);
        attrs.addElement(tclose);
        Instances instancesTest = new Instances("bp", attrs, attrs.size());//创建实例集，即数据集，参数为名称，FastVector类型的属性集，以及属性集的大小（即数据集的列数）
        instancesTest.setClass(tclose);//设置数据集的类属性，即对哪个数据列进行预测
        for (int k = 0; k < 1; k++) {
            Instance ins = new DenseInstance(attrs.size());//创建实例，即一条数据
            ins.setDataset(instancesTest);//设置该条数据对应的数据集，和数据集的属性进行对应
            ins.setValue(open, param[0]);//设置数据每个属性的值
            ins.setValue(high, param[1]);
            ins.setValue(low, param[2]);
            ins.setValue(close, param[3]);
            ins.setValue(jyl, param[4]);
            instancesTest.add(ins);//将该条数据添加到数据集中
        }


        MultilayerPerceptron m_classifier = new MultilayerPerceptron();//创建算法实例，要使用其他的算法，只用把类换做相应的即可
        try {
            m_classifier.setSeed(seed);
            m_classifier.setLearningRate(rate);
            m_classifier.buildClassifier(instancesTrain); //进行训练

        } catch (Exception e) {
            e.printStackTrace();
        }


        //instancesTest.instance(i)获得的是用模型预测的结果值，instancesTest.instance(i).classValue();获得的是测试集类属性的值
        //此处是把预测值和当前值同时输出，进行对比
        try {
            double yuce = m_classifier.classifyInstance(instancesTest.instance(0));
//            System.out.println(
//                    "\t seed:" + m_classifier.getSeed()
//                    + "\t Rate:" + m_classifier.getLearningRate()
//                    + "\t yuce:" +yuce
//
//            );
            return yuce;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
