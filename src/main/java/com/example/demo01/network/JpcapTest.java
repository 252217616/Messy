package com.example.demo01.network;
import java.io.IOException;
import java.util.Scanner;

import jpcap.*;
import jpcap.packet.*;


/**
 * Jpcat实现抓包
 *
 * @date 2018年2月7日
 */
public class JpcapTest implements PacketReceiver {

    @Override
    public void receivePacket(Packet arg0) {
        System.out.println(arg0);
    }

    public static void main(String[] args) {

        //获得网卡设备列表
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        if (devices.length == 0) {

            System.out.println("无网卡信息！");
            return;
        }
        //输出网卡信息
        for (int i = 0; i < devices.length; i++) {

            System.out.println("网卡" + i + "信息:" + devices[i].name);
            for (NetworkInterfaceAddress address : devices[i].addresses) {

                System.out.print(address.address + " ");
            }
            System.out.println("\n");
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("请选择您要监听的网卡序号：");
        int index = scan.nextInt();

        //监听选中的网卡
        try {
            JpcapCaptor jpcapCaptor = JpcapCaptor.openDevice(devices[index], 2000, false, 20);
            JpcapSender jpcapSender = JpcapSender.openDevice(devices[index]);
            jpcapCaptor.loopPacket(-1, new NetFetcher(jpcapSender));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}