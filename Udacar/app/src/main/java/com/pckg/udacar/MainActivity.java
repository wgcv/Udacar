package com.pckg.udacar;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.*;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity  {

    private TextView hourText;
    private ImageView batteryLevel;
    private ViewPager viewPager;
    private MyPageAdapter pageAdapter;
    private android.app.FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        hourText = (TextView) findViewById(R.id.hourText);
        batteryLevel = (ImageView) findViewById(R.id.batteryImage);
        new MyTimerTask(hourText, this, batteryLevel);
        viewPager = (ViewPager) findViewById(R.id.vp_vertical_ntb);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        /*viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 7;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp, null, false);

                container.addView(view);
                return view;
            }
        });*/

        final String[] colors = getResources().getStringArray(R.array.vertical_ntb);

        final com.gigamole.navigationtabbar.ntb.NavigationTabBar navigationTabBar = (com.gigamole.navigationtabbar.ntb.NavigationTabBar) findViewById(R.id.ntb_vertical);
        final ArrayList<com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.functions_icon),
                        Color.parseColor(colors[6]))
                        .selectedIcon(getResources().getDrawable(R.drawable.functions_icon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.locksicon),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.locksicon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.statistical_icon),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.drawable.statistical_icon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.route_icon),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.route_icon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.adjustment_icon),
                        Color.parseColor(colors[3]))
                        .selectedIcon(getResources().getDrawable(R.drawable.adjustment_icon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.music_icon),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.music_icon_selected))
                        .build()
        );
        models.add(
                new com.gigamole.navigationtabbar.ntb.NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.luxury_icon),
                        Color.parseColor(colors[5]))
                        .selectedIcon(getResources().getDrawable(R.drawable.luxury_icon_selected))
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 3);
        navigationTabBar.setBgColor(getResources().getColor(R.color.navBarDay));
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return new BasicFunctionsFragment();
                //case 1: return SecondFragment.newInstance("asdasd");
                default : return BasicFunctionsFragment.newInstance("", "");
            }
        }

        @Override
        public void destroyItem(final View container, final int position, final Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final View view = LayoutInflater.from(
                    getBaseContext()).inflate(R.layout.item_vp, null, false);

            container.addView(view);
            return view;
        }
    }
}
