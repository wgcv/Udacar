package com.pckg.udacar;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by Saul on 10/28/2016.
 */

public class MyTimerTask {
    private int delay = 1000;
    private int period = 60000;

    public MyTimerTask(final TextView textView, final Context context, final ImageView imageView){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                        Calendar cal = Calendar.getInstance();
                        textView.setText(dateFormat.format(cal.getTime()));

                        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
                        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                        if(batLevel == 100){
                            imageView.setImageResource((R.drawable.battery_100));
                        }else if(batLevel>75 && batLevel<100){
                            imageView.setImageResource((R.drawable.battery_75));
                        }else if(batLevel>50 && batLevel<=75){
                            imageView.setImageResource((R.drawable.battery_50));
                        }else if(batLevel>25 && batLevel<=50){
                            imageView.setImageResource((R.drawable.battery_25));
                        }else{
                            imageView.setImageResource((R.drawable.battery_15));
                        }
                    }
                });
            }
        }, delay, period);
    }
}