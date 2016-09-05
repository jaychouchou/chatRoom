package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 肖安华 on java.
 * 完成聊天室客户端的一些工作
 * <p>
 * 一共有两个线程
 * 1，随时接收从服务端发送过来的数据
 * 2，开启一个client，随时从控制台读取数据，并写入socket
 */
public class ChartClient {
    public static void main(String arg[]) {

        new ChartClient().startClient();
    }

    public void startClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 9000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            //开启一个接收服务端数据的线程；
            new Service(socket);

            while (true) {
                String s = bufferedReader.readLine();
               /* if ("exit".equals(s)) break;*/
                printWriter.println(s);
                if ("exit".equals(s)) break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Service extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;

    public Service(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();
    }

    @Override
    public void run() {

        try {
            while (true) {
                String s = bufferedReader.readLine();
                if ("".equals(s) || s == null) continue;
                int sindex= s.indexOf(";");
                if (sindex!=-1){
                    System.out.println(s.split(";")[0]+" out of the chat room ...");
                } else {
                    int index = s.indexOf("/");
                    if (index == -1) {//说明是群聊的内容
                        System.out.println("The content of the group chat is :" + s);
                    } else {//私聊的内容
                        String[] strings = s.split("/");
                        System.out.println(strings[0] + " send the content is :" + strings[1]);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
