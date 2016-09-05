package com.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 肖安华 on java.
 */
public class InetAddressDemo {
    public static void main(String arg[]){

        try {
            InetAddress address=InetAddress.getByName("www.163.com");
            System.out.println(address.getHostName());
            //返回本地主机
            InetAddress address1=InetAddress.getLocalHost();
            System.out.println(address1.getHostAddress());
            System.out.println(address1.getHostName());
            //本机的默认ip是127.0.0.1
            InetAddress address2=InetAddress.getByName(null);
            System.out.println(address2.getHostAddress());
            System.out.println(address2.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}
