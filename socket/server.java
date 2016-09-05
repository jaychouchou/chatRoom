package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 肖安华 on java.
 */
public class server {
    public static void main(String arg[]) throws IOException {

            ServerSocket serverSocket = new ServerSocket(9000);
            //去接收客服端的连接，返回一个socket对象，并且是阻塞状态；
            System.out.println("服务端启动成功，等待连接....");
            Socket socket = serverSocket.accept();
            System.out.println("连接成功....");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);


            //开启一个读线程
            new Read(bufferedReader).start();
            //开启一个写线程
            new  Write(bufferedReader1,printWriter).start();

            //socket.close();


    }
}
class Read extends Thread {
    BufferedReader bufferedReader;
    public  Read(BufferedReader bufferedReader) {
        this.bufferedReader=bufferedReader;
    }
    @Override
    public void run(){
        while (true) {
            String s = null;
            try {
                s = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(s);

        }
    }

}
class Write extends Thread {
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public  Write(BufferedReader bufferedReader,PrintWriter printWriter) {
        this.bufferedReader=bufferedReader;
        this.printWriter=printWriter;
    }

    @Override
    public void run(){
        while (true) {
            try {
                String s1 = bufferedReader.readLine();
                printWriter.println(s1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

