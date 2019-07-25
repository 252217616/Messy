package com.example.demo01.tongjiGailv;
import java.io.*;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.*;
import weka.core.converters.ArffLoader;

public class Test2 {
public static void main(String[] args) throws Exception 
	{
	//便于测试，用数组保存一些数据，从数据库中取数据是同理的
	//二维数组第一列表示当月的实际数据，第二列是上个月的数据，用于辅助对当月数据的预测的
	//二维数组的数据用于测试集数据，为了展示两种weka载入数据的方法，将训练集数据从arff文件中读取
	double[][] a = {{14.23,14.25,13.79,13.93,2046695610},{14.07,14.13,13.11,13.41,1835580532},
			{13.44,13.6,13.12,13.52,1191380555},{13.13,13.32,12.73,12.98,1668151257},{12.82,12.95,12.59,12.6,929717843}
			,{12.62,12.91,12.52,12.67,770316081},{12.95,13.09,12.29,12.34,851865982}};
	double [] b = {13.41,13.52,12.98};
	
	
	//读入训练集数据
	File inputFile = new File("D:\\student\\机器学习\\完整\\test4.arff");//将路径替换掉
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
	Attribute open = new Attribute("open",1);//创建属性，参数为属性名称和属性号，但属性号并不影响FastVector中属性的顺序
	Attribute high = new Attribute("high",2);
	Attribute low = new Attribute("low",3);
	Attribute close = new Attribute("close",4);
	Attribute jyl = new Attribute("jyl",5);
	Attribute tclose = new Attribute("tclose",6);
	attrs.addElement(open);//向FastVector中添加属性，属性在FastVector中的顺序由添加的先后顺序确定。
	attrs.addElement(high);
	attrs.addElement(low);
	attrs.addElement(close);
	attrs.addElement(jyl);
	attrs.addElement(tclose);
	Instances instancesTest = new Instances("bp",attrs,attrs.size());//创建实例集，即数据集，参数为名称，FastVector类型的属性集，以及属性集的大小（即数据集的列数）
	instancesTest.setClass(tclose);//设置数据集的类属性，即对哪个数据列进行预测
	for(int k=0;k<a.length;k++){
	Instance ins = new DenseInstance(attrs.size());//创建实例，即一条数据
	ins.setDataset(instancesTest);//设置该条数据对应的数据集，和数据集的属性进行对应
	ins.setValue(open, a[k][0]);//设置数据每个属性的值
	ins.setValue(high, a[k][1]);
	ins.setValue(low, a[k][2]);
	ins.setValue(close, a[k][3]);
	ins.setValue(jyl, a[k][4]);
	instancesTest.add(ins);//将该条数据添加到数据集中
	}
	
	
	MultilayerPerceptron m_classifier = new MultilayerPerceptron();//创建算法实例，要使用其他的算法，只用把类换做相应的即可
	try {
 		m_classifier.buildClassifier(instancesTrain); //进行训练
	} catch (Exception e) {
 		e.printStackTrace();
 	}

	for(int i = 0;i<a.length-1;i++){//测试分类结果
	 	//instancesTest.instance(i)获得的是用模型预测的结果值，instancesTest.instance(i).classValue();获得的是测试集类属性的值
		//此处是把预测值和当前值同时输出，进行对比
 	try {
		double v = m_classifier.classifyInstance(instancesTest.instance(i));
		System.out.println(v);
		for(int j = 0;j<a[i+1].length;j++){
			System.out.print(v-a[i+1][j]);
			System.out.print("\t");
		}
		System.out.println();
 	}catch (Exception e) {
 		e.printStackTrace();
 	}
	}
	
	System.out.println("bp success!");
}

}