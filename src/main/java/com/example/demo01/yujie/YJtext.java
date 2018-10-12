package com.example.demo01.yujie;

import java.math.BigDecimal;
import java.util.Scanner;

public class YJtext {

    //一个会员拥有的最多数量
    public final static BigDecimal LIMIT = new BigDecimal(2000);
    public static void main(String[] args){
        //会员当前拥有的产品数量
        BigDecimal cur = BigDecimal.valueOf(-11);
        System.out.println(cur.toString());
//        Scanner input = new Scanner(System.in);
//        System.out.println("请输入需要预订的数量：");
//        while(input.hasNext()){
//            String o = input.nextLine();
//            if(!yuejie(o)){
//                System.out.println("数值过大");
//                continue;
//            }
//
//            BigDecimal order = new BigDecimal(Double.parseDouble(o));
//            //当前拥有的与准备订购的产品的数量之和
//            if(order.compareTo(new BigDecimal(0))== 1 && LIMIT.compareTo(cur.add(order)) == 1){
//
//                System.out.println("你已经成功预订的" + order + "个产品");
//            }else{
//                System.out.println(order.compareTo(new BigDecimal(0)));
//                System.out.println(LIMIT.compareTo(cur.add(order)));
//                System.out.println("超限额，预订失败！");
//                System.out.println(order);
//                System.out.println(cur.add(order));
//            }
//        }
    }



    public static boolean yuejie (String data) {
        if(data.length()<10 && data.matches("-?\\d*\\.?\\d*")) {
            return true;
        }
        return false;
    }
}
