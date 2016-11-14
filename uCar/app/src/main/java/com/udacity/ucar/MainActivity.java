package com.udacity.ucar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mLvMenu;

    List<String> mMenuItems;
    MenuAdapter mMenuAdapter;
    FragmentManager mFragmentManager;
    ImageView mSelctedMenuImg;
    int mSelectedMenuImgPos;
    private ResponseReceiver receiver;
    private ResponseSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Reciber Message
        IntentFilter filter = new IntentFilter(SocketService.ACTION_RECIBER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        Intent msgIntent = new Intent(this, SocketService.class);
        startService(msgIntent);

        //Sender Message
        IntentFilter filter2 = new IntentFilter(SocketService.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        sender = new ResponseSender();
        registerReceiver(sender, filter);

        Intent msgIntent2 = new Intent(this, SocketService.class);
        msgIntent.putExtra(SocketService.PARAM_IN_MSG, "json");
        startService(msgIntent);

        mLvMenu = (ListView) findViewById(R.id.lv_menu);

        mMenuItems = getMenuItems();
        mMenuAdapter = new MenuAdapter(mMenuItems);

        mLvMenu.setAdapter(mMenuAdapter);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment fragMap = FragmentMap.newInstance();
        fragmentTransaction.add(R.id.ll_content, fragMap);
        fragmentTransaction.commit();

        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.iv_menu);
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
            case 0:
                Fragment fragMap = FragmentMap.newInstance();
                fragmentTransaction.replace(R.id.ll_content, fragMap);
                fragmentTransaction.commit();
                break;
            case 1:
                Fragment fragSettings = FragmentSettings.newInstance();
                fragmentTransaction.replace(R.id.ll_content, fragSettings);
                fragmentTransaction.commit();
                break;
            case 2:
                Fragment fragFunction = FragmentFunction.newInstance();
                fragmentTransaction.replace(R.id.ll_content, fragFunction);
                fragmentTransaction.commit();
                break;
            case 3:
                Fragment fragComfort = FragmentComfort.newInstance();
                fragmentTransaction.replace(R.id.ll_content, fragComfort);
                fragmentTransaction.commit();
                break;
        }
    }

    private List<String> getMenuItems() {
        List<String> items = new ArrayList<>();
        items.add("Map");
        items.add("Settings");
        items.add("Basic Functions");
        items.add("Comfort");

        return items;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(SocketService.PARAM_OUT_MSG);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public class ResponseSender extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(SocketService.PARAM_OUT_MSG);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
