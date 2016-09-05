package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 肖安华 on java.
 */
public class Client {

    public static void main(String arg[]){
        try {
            Socket socket=new Socket("127.0.0.1",9000);
            System.out.println("连接服务端成功....");
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);


            BufferedReader bufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Read1(bufferedReader1).start();
            new Write1(bufferedReader,printWriter).start();


          //  socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Read1 extends Thread {
    BufferedReader bufferedReader;
    public  Read1(BufferedReader bufferedReader) {
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
class Write1 extends Thread {
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public  Write1(BufferedReader bufferedReader,PrintWriter printWriter) {
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