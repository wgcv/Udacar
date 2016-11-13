package com.udacity.ucar;


import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
public class SocketService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SEND =
            "me.wgcv.prueba.MESSAGE_SEND";
    public static final String ACTION_RECIBER =
            "me.wgcv.prueba.MESSAGE_RECIBER";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "me.wgcv.prueba.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "me.wgcv.prueba.extra.PARAM2";
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";

    private static Socket socket;
    private static Socket socket2;

    private static final int SERVERPORT_sender = 5589;
    private static final int SERVERPORT_reciber = 5590;
    private static final String SERVER_IP = "192.168.1.8";
    private Boolean firstTime = Boolean.TRUE;
    public SocketService() {
        super("SocketService");
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
                    int lockSeconds = 10 * 1000;
                    long lockThreadCheckpoint = System.currentTimeMillis();
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
                    responce = new String(buffer);

                    input.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String resultTxt = responce;

                // processing done here….
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(SocketService.ACTION_RECIBER);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_OUT_MSG, resultTxt);
                sendBroadcast(broadcastIntent);
            }
            else if (ACTION_SEND.equals(action)){
                try {
                    final String str = intent.getStringExtra(EXTRA_PARAM1);
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket2.getOutputStream())), true);
                    out.println(str);
                    out.flush();
                    out.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // processing done here….
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(SocketService.ACTION_SEND);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_OUT_MSG, "True");
                sendBroadcast(broadcastIntent);

            }

        }
    }


    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT_reciber);
                socket2 = new Socket(serverAddr, SERVERPORT_sender);


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}