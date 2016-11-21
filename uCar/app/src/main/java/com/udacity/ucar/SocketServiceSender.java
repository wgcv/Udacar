package com.udacity.ucar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketServiceSender extends Service {
    public static final String PARAM_IN_MSG = "";
    private static Socket socket;
    private static final String SERVER_IP = "192.168.1.8";
    private static final int SERVERPORT_sender = 5589;
    private Boolean firstTime = Boolean.TRUE;
    public SocketServiceSender() {
        new Thread(new SocketServiceSender.ClientThread2()).start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void send( String message){
        try {
            final String str = message;
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(str);
            out.flush();
            //out.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ClientThread2 implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT_sender);


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
