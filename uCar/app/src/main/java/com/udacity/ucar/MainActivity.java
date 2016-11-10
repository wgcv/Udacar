package com.udacity.ucar;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mLvMenu;

    List<String> mMenuItems;
    MenuAdapter mMenuAdapter;
    FragmentManager mFragmentManager;
    ImageView mSelctedMenuImg;
    int mSelectedMenuImgPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if(mSelctedMenuImg != null){
                    Helper.setImage(parent.getContext(), mSelctedMenuImg, mSelectedMenuImgPos, false);
                }
                mSelctedMenuImg = iv;
                mSelectedMenuImgPos = position;
                Helper.setImage(parent.getContext(), iv, position, true);

                changeFragment(position);
            }
        });

    }

    private void changeFragment(int position){
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch(position){
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

    private List<String> getMenuItems(){
        List<String> items = new ArrayList<>();
        items.add("Map");
        items.add("Settings");
        items.add("Basic Functions");
        items.add("Comfort");

        return items;
    }
}
