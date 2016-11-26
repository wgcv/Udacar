package com.udacity.uDacar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_LOCATION_AUDIO = 3;

    private ResponseReceiver receiver;
    private ResponseSender sender;
    ListView mLvMenu;
    List<String> mMenuItems;
    MenuAdapter mMenuAdapter;
    FragmentManager mFragmentManager;
    ImageView mSelctedMenuImg;
    int mSelectedMenuImgPos;
    Fragment fragMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.udacity.uDacar.R.layout.activity_main);
      //Reciber Message
        IntentFilter filter = new IntentFilter(SocketServiceReciber.ACTION_RECIBER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        Intent reciberIntent = new Intent(this, SocketServiceReciber.class);
        reciberIntent.setAction(SocketServiceReciber.ACTION_RECIBER);
        startService(reciberIntent);
/*
        //Sender Message
        IntentFilter filter2 = new IntentFilter(SocketServiceReciber.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        sender = new ResponseSender();
        registerReceiver(sender, filter);

        Intent msgIntent2 = new Intent(this, SocketServiceReciber.class);
        msgIntent2.putExtra(SocketServiceReciber.PARAM_IN_MSG, "json");
        startService(msgIntent2);
*/
        //Sender Message
        Intent i = new Intent(MainActivity.this, SocketServiceSender.class);
        MainActivity.this.startService(i);
        // How to send sender
        //SocketServiceSender.send("hola");
        
        mLvMenu = (ListView) findViewById(com.udacity.uDacar.R.id.lv_menu);

        mMenuItems = getMenuItems();
        mMenuAdapter = new MenuAdapter(mMenuItems);

        mLvMenu.setAdapter(mMenuAdapter);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragMap = FragmentMapboxMap.newInstance();
        fragmentTransaction.add(com.udacity.uDacar.R.id.ll_content, fragMap);
        fragmentTransaction.commit();

        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(com.udacity.uDacar.R.id.iv_menu);
                if (mSelctedMenuImg != null) {
                    Helper.setImage(parent.getContext(), mSelctedMenuImg, mSelectedMenuImgPos, false);
                }
                mSelctedMenuImg = iv;
                mSelectedMenuImgPos = position;
                Helper.setImage(parent.getContext(), iv, position, true);

                changeFragment(position);
            }
        });

    }

    private void changeFragment(int position) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (position) {
            case 2:
                Fragment fragFunction = FragmentFunction.newInstance();
                fragmentTransaction.replace(com.udacity.uDacar.R.id.ll_content, fragFunction);
                fragmentTransaction.commit();
                break;
            case 3:
                Fragment fragmentDoors = FragmentDoors.newInstance();
                fragmentTransaction.replace(com.udacity.uDacar.R.id.ll_content, fragmentDoors);
                fragmentTransaction.commit();
                break;
            case 1:
                if(fragMap == null){
                   fragMap = FragmentMapboxMap.newInstance();
                }
                fragmentTransaction.replace(com.udacity.uDacar.R.id.ll_content, fragMap);
                fragmentTransaction.commit();
                break;

            case 4:
                Fragment fragComfort = FragmentComfort.newInstance();
                fragmentTransaction.replace(com.udacity.uDacar.R.id.ll_content, fragComfort);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION_AUDIO) {
            if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                ((FragmentMapboxMap)fragMap).initializeView();
            } else {
                finish();
            }
        }
    }
    private List<String> getMenuItems() {
        List<String> items = new ArrayList<>();
        items.add("Udacar");
        items.add("Route");
        items.add("Basic Function");
        items.add("Doors");
        items.add("Comfort");
        return items;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(SocketServiceReciber.PARAM_OUT_MSG);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public class ResponseSender extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(SocketServiceReciber.PARAM_OUT_MSG);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
