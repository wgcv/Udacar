package com.pckg.udacar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView hourText;
    private ImageView batteryLevel;
    private ViewPager viewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.vp_vertical_ntb);
        viewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(viewPager);
        final String[] colors = getResources().getStringArray(R.array.vertical_ntb);
        final com.gigamole.navigationtabbar.ntb.NavigationTabBar navigationTabBar =
                (com.gigamole.navigationtabbar.ntb.NavigationTabBar) findViewById(R.id.ntb_vertical);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BasicFunctionsFragment(), "Basic");
        adapter.addFragment(new RouteFragment(), "Route");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
