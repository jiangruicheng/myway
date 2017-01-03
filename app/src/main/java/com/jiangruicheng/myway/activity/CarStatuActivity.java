package com.jiangruicheng.myway.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.fragment.CarStatuFragment;

public class CarStatuActivity extends AppCompatActivity {
    private CarStatuFragment carStatuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_statu);
        carStatuFragment = new CarStatuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, carStatuFragment).commit();
    }
}
