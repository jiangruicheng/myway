package com.jiangruicheng.myway.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.view.ArcCheckOut;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCheckOutFragment extends Fragment {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            arcprog.setProg(msg.what);
        }
    };
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.arcprog)
    ArcCheckOut    arcprog;

    @OnClick(R.id.arcprog)
    void onarcprog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 101; i++) {
                    handler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @BindView(R.id.path)
    LinearLayout path;
    @BindView(R.id.mesg)
    TextView     mesg;
    @BindView(R.id.checkout_wrong)
    LinearLayout checkoutWrong;
    @BindView(R.id.screen)
    TextView     screen;
    @BindView(R.id.qudong)
    TextView     qudong;
    @BindView(R.id.power)
    TextView     power;
    @BindView(R.id.dianji)
    TextView     dianji;
    @BindView(R.id.msocket)
    TextView     msocket;
    @BindView(R.id.monmistake)
    TextView     monmistake;

    public CarCheckOutFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_check_out, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
