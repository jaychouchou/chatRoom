package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by 肖安华 on java.
 * 也需要两个线程：
 * 1,用来接收客户端发来的数据并且做相应处理
 * 2,服务端能保存所有的socket对象。并且提供通过ip地址获取socket的方式，
 */
public class ChartServer {


    private static HashMap<String, Socket> user = new HashMap<>();

    public static HashMap<String, Socket> getMap() {

        return user;
    }

    public static Socket getClient(String s) {
        return user.get(s);
    }

    public static void main(String arg[]) {

        new ChartServer().getSocket();
        //new  AcceptClient(socket);

    }

    public void getSocket() {

        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("the server was started...");
            while (true) {
                Socket socket = serverSocket.accept();
                //通过客户端请求的socket获得该客户端的地址，并且加上UUID来唯一标识连接过来的socket地址
                String addre = socket.getInetAddress().getHostAddress() + UUID.randomUUID().toString();
                System.out.println("accept the " + addre + " access");
                //String address = socket.getInetAddress().getHostAddress();
                //将该地址和socket放入HashMap中。
                user.put(addre, socket);
                new AcceptClient(socket, addre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class AcceptClient extends Thread {
    Socket socket;//接收的socket
    BufferedReader bufferedReader;
    PrintStream cli; //私聊的内容
    PrintStream allCli;//群聊的内容
    PrintStream ps;//当前的客户端写
    String addre;

    public AcceptClient(Socket socket, String addre) {
        this.socket = socket;
        try {
            ps = new PrintStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.addre = addre;
        this.start();
    }

    public void setAllCli(String s) {
        for (String ss : ChartServer.getMap().keySet()) {
            Socket socket = ChartServer.getClient(ss);
            try {
                allCli = new PrintStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            allCli.println(s);
        }
    }


    @Override
    public void run() {
      //  boolean cont=this.socket.isConnected();
        while (true) {
            try {
                String s = bufferedReader.readLine();
                if ("".equals(s) || s == null) continue;
                if ("exit".equals(s)) {
                    System.out.println(this.addre + " out of the chat room !");
                    String send=this.addre+";"+s;
                    setAllCli(send);
                    ChartServer.getMap().remove(this.addre);

                    Thread.sleep(10);
                    Thread.currentThread().interrupt();
                } else {
                    int index = s.indexOf("/");
                    if (index == -1) {//说明是群聊
                        setAllCli(s);
                    } else {//是私聊
                        //获取接收人的地址
                        String addrs = s.split("/")[0];
                        String con = s.split("/")[1];
                        //服务端要发送的内容，包括发送人的地址和内容
                        String send = this.addre + "/" + con;
                        //通过地址去HashMap中获得相应的socket
                        Socket socket = ChartServer.getClient(addrs);
                        if (socket == null) {
                            ps.println("You send the client does not exist...");
                        } else {
                            cli = new PrintStream(socket.getOutputStream());
                            cli.println(send);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
