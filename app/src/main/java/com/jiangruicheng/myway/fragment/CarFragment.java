package com.jiangruicheng.myway.fragment;


import android.content.Intent;
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
import android.widget.Toast;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.activity.CarManagerActivity;
import com.jiangruicheng.myway.activity.CarStatuActivity;
import com.jiangruicheng.myway.data.Command;
import com.jiangruicheng.myway.eventtype.SendCmd;
import com.jiangruicheng.myway.util.ByteUtil;
import com.jiangruicheng.myway.util.Quee;
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
    ArcProgBar     licheng;
    @BindView(R.id.dianliang)
    ArcProgBar     dianliang;
    @BindView(R.id.biaopan)
    RelativeLayout biaopan;
    @BindView(R.id.search_ble)
    TextView       searchBle;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.sudu)
    ArcProgBar     sudu;
    @BindView(R.id.conn)
    LinearLayout   conn;
    @BindView(R.id.statue)
    LinearLayout   statue;
    @BindView(R.id.conn_text)
    TextView       connText;
    @BindView(R.id.lock_text)
    TextView       lockText;
    @BindView(R.id.mode_text)
    TextView       modeText;

    @OnClick(R.id.statue)
    void onstatue() {
        Intent intent = new Intent(getActivity(), CarStatuActivity.class);
        getActivity().startActivity(intent);
    }

    @BindView(R.id.sock)
    LinearLayout sock;
    private boolean isLock = false;

    @OnClick(R.id.sock)
    void onsock() {
        if (isLock) {
            RxBus.getDefault().post(new SendCmd().
                    setCmd(Command.getCommand
                            (Command.setData(Command.COM_LOCK,
                                    new byte[]{0x00}))));
            isLock = false;
        } else {
            RxBus.getDefault().post(new SendCmd().
                    setCmd(Command.getCommand
                            (Command.setData(Command.COM_LOCK,
                                    new byte[]{0x01}))));
            isLock = true;
        }
    }

    @BindView(R.id.mode)
    LinearLayout mode;
    private byte bmode = 1;

    @OnClick(R.id.mode)
    void onmode() {
        if (bmode == 1) {
            RxBus.getDefault().post(new SendCmd().
                    setCmd(Command.getCommand
                            (Command.setData(Command.COM_MODE,
                                    new byte[]{0x00}))));
            bmode = 0;
        } else {
            RxBus.getDefault().post(new SendCmd().
                    setCmd(Command.getCommand
                            (Command.setData(Command.COM_MODE,
                                    new byte[]{0x01}))));
            bmode = 1;
        }
    }

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
        setcallback();
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

    private Quee.callback powerandspeed;
    private Quee.callback MILEAGE;
    private Quee.callback LOCK;

    private void setcallback() {
        powerandspeed = new Quee.callback() {
            @Override
            public void callback(byte[] b) {
                byte[] b_speed = new byte[2];
                System.arraycopy(b, 4, b_speed, 0, 2);
                float speed = ByteUtil.getFloat(b_speed);
                sudu.setValue((int) (speed / 100) + "");
                sudu.setGuide_rate((speed / 100) * ((float) 270 / (float) 40) + 45);
                byte[] b_power = new byte[2];
                System.arraycopy(b, 6, b_power, 0, 2);
                float power = ByteUtil.getFloat(b_power);
                dianliang.setValue((int) (power) + "");
                dianliang.setGuide_rate((power) * ((float) 270 / (float) 100) + 45);
                sudu.invalidate();
                dianliang.invalidate();
            }
        };
        MILEAGE = new Quee.callback() {
            @Override
            public void callback(byte[] b) {
                byte[] b_now = new byte[2];
                System.arraycopy(b, 4, b_now, 0, 2);
                float now = ByteUtil.getFloat(b_now);

                byte[] b_total = new byte[2];
                System.arraycopy(b, 6, b_total, 0, 2);
                float total = ByteUtil.getFloat(b_total);
                licheng.setValue((int) (total / 10) + "");
                dianliang.setGuide_rate((now / 10) * ((float) 270 / (float) 100) + 45);
                licheng.invalidate();
            }
        };
        LOCK = new Quee.callback() {
            @Override
            public void callback(byte[] b) {
                if (b[5] == 1) {
                    if (isLock) {
                        lockText.setText("解锁");
                    } else {
                        lockText.setText("锁车");
                    }
                }
                Toast.makeText(getActivity(), "" + b[5], Toast.LENGTH_SHORT).show();
            }
        };
        Quee.getDefault().registcallback(Command.EVENT_POWERANDSPEED, powerandspeed);
        Quee.getDefault().registcallback(Command.EVENT_MILEAGE, MILEAGE);
        Quee.getDefault().registcallback(Command.COM_LOCK, LOCK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Quee.getDefault().unregistcallback(Command.EVENT_POWERANDSPEED, powerandspeed);
        Quee.getDefault().unregistcallback(Command.EVENT_MILEAGE, MILEAGE);
        Quee.getDefault().unregistcallback(Command.COM_LOCK, LOCK);
        unbinder.unbind();
    }
}
