package com.udacity.ucar;


import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SocketServiceReciber extends IntentService {
    public static final String ACTION_RECIBER =
            "me.wgcv.prueba.MESSAGE_RECIBER";
    public static final String PARAM_OUT_MSG = "";
    private static Socket socket;
    private String mensaje = "";
    private static final String SERVER_IP = "192.168.1.8";
    private static final int SERVERPORT_reciber = 5590;
    private Boolean firstTime = Boolean.TRUE;

    public SocketServiceReciber() {
        super("SocketServiceReciber");
        new Thread(new ClientThread()).start();
    }



        @Override
        protected void onHandleIntent(Intent intent) {
            if (intent != null) {
                String responce = "";
                if( firstTime){
                    SystemClock.sleep(5000);
                    firstTime = Boolean.FALSE;
                }
                final String action = intent.getAction();
                if (ACTION_RECIBER.equals(action)) {
                    try {
                        InputStream input = socket.getInputStream();
                        int lockSeconds = 1 * 1000;
                        long lockThreadCheckpoint = System.currentTimeMillis();
                        while(true){

                            int availableBytes = input.available();
                            while (availableBytes <= 0 && (System.currentTimeMillis() < lockThreadCheckpoint + lockSeconds)) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                }
                                availableBytes = input.available();
                            }

                            byte[] buffer = new byte[availableBytes];

                            input.read(buffer, 0, availableBytes);
                            String[] ultimo = (new String(buffer)).split("\n");
                            responce = ultimo[(ultimo.length)-1];
                            if(!(mensaje.equals(responce)) && !(responce.length()==0) ){


                                // processing done here….
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(SocketServiceReciber.ACTION_RECIBER);
                                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                broadcastIntent.putExtra(PARAM_OUT_MSG, responce);
                                sendBroadcast(broadcastIntent);
                                mensaje = responce;
                            }
                        }
                        //input.close();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        }


        class ClientThread implements Runnable {

            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    socket = new Socket(serverAddr, SERVERPORT_reciber);

                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }
