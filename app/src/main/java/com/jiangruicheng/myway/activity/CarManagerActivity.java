package com.jiangruicheng.myway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangruicheng.myway.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarManagerActivity extends AppCompatActivity {

    @BindView(R.id.back)
    TextView back;

    @OnClick(R.id.back)
    void onback() {
        finish();
    }

    @BindView(R.id.search_device)
    TextView searchDevice;

    @OnClick(R.id.search_device)
    void onsearch() {
        Intent intent = new Intent(this, ConnectBleActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.title)
    RelativeLayout title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        ButterKnife.bind(this);
    }
}
