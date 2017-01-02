package com.jiangruicheng.myway.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.activity.CarManagerActivity;
import com.jiangruicheng.myway.view.ArcProgBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    @BindView(R.id.car_id)
    TextView       carId;
    @BindView(R.id.licheng)
    ArcProgBar licheng;
    @BindView(R.id.dianliang)
    ArcProgBar dianliang;
    @BindView(R.id.biaopan)
    RelativeLayout biaopan;
    @BindView(R.id.search_ble)
    TextView       searchBle;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.sudu)
    ArcProgBar sudu;

    @OnClick(R.id.search_ble)
    void onsearch_ble() {
        Intent intent = new Intent(getActivity(), CarManagerActivity.class);
        getActivity().startActivity(intent);
    }


    public CarFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sudu.setGuide_rate(msg.what);
            licheng.setGuide_rate(msg.what);
            dianliang.setGuide_rate(msg.what);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        unbinder = ButterKnife.bind(this, view);
        dianliang.setScreen_rate(30);
        licheng.setScreen_rate(330);
        dianliang.setScale(1);
        licheng.setScale(2);
       /* new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 0;
                while (true) {
                    handler.sendEmptyMessage(45 + i * 45);
                    i++;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
