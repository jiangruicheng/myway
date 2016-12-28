package com.jiangruicheng.myway.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.fragment.CarFragment;
import com.jiangruicheng.myway.fragment.CarStatuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frame)
    FrameLayout         frame;
    @BindView(R.id.bottom_navigation)
    BottomNavigationBar bottomNavigation;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initnavigation();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new CarFragment()).commit();
    }

    private void initnavigation() {
        bottomNavigation.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigation.setActiveColor(android.R.color.holo_blue_light).setBarBackgroundColor(android.R.color.background_light);
        bottomNavigation.addItem(new BottomNavigationItem(R.drawable.dashuju, "动态"))
                .addItem(new BottomNavigationItem(R.drawable.dashuju, "发现"))
                .addItem(new BottomNavigationItem(R.drawable.dashuju, "车辆"))
                .addItem(new BottomNavigationItem(R.drawable.dashuju, "路线"))
                .addItem(new BottomNavigationItem(R.drawable.dashuju, "我的"))
                .setFirstSelectedPosition(2).initialise();
        bottomNavigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new CarStatuFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new CarFragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
