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
import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.data.CarStatu;
import com.jiangruicheng.myway.data.Command;
import com.jiangruicheng.myway.eventtype.SendCmd;
import com.jiangruicheng.myway.util.ByteUtil;
import com.jiangruicheng.myway.util.Quee;
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
            if (isrun) {
                arcprog.setProg(msg.what);
            }
        }
    };
    private boolean isrun = true;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.arcprog)
    ArcCheckOut    arcprog;

    @OnClick(R.id.arcprog)
    void onarcprog() {
        RxBus.getDefault().post(new SendCmd().setCmd(Command.getCommand(Command.setData(Command.COM_TEST, new byte[]{0x01}))));
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
        setcallback();
        return view;
    }

    private Quee.callback CHECKEOUT;

    private void setcallback() {
        CHECKEOUT = new Quee.callback() {
            @Override
            public void callback(byte[] b) {
                byte[] statu = new byte[4];
                System.arraycopy(b, b.length - 7, statu, 0, 4);
                byte[] carstatues_1 = ByteUtil.getBitArray(statu[0]);
                byte[] carstatues_2 = ByteUtil.getBitArray(statu[1]);
                CarStatu.getDefault().getStatue().setIs_carlock(Is(carstatues_1[0]));
                CarStatu.getDefault().getStatue().setIs_slide(Is(carstatues_1[1]));
                if (Is(carstatues_1[2])) {
                    CarStatu.getDefault().getStatue().setCarstartmode(CarStatu.Statue.SOFT_MODE);
                }
                if (Is(carstatues_1[3])) {
                    CarStatu.getDefault().getStatue().setCarstartmode(CarStatu.Statue.HARD_MODE);
                }
                CarStatu.getDefault().getStatue().setIs_esp_right(Is(carstatues_1[4]));
                CarStatu.getDefault().getStatue().setIs_power_right(Is(carstatues_1[5]));
                CarStatu.getDefault().getStatue().setIs_drive_right(Is(carstatues_1[6]));
                CarStatu.getDefault().getStatue().setIs_tran_right(Is(carstatues_1[7]));
                CarStatu.getDefault().getStatue().setIs_befor_light_ON(Is(carstatues_2[0]));
                CarStatu.getDefault().getStatue().setIs_after_light_ON(Is(carstatues_2[1]));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isrun) {
                            for (int i = 0; i < 101; i++) {
                                handler.sendEmptyMessage(i);
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
            }
        };
        Quee.getDefault().registcallback((byte) 0xff, CHECKEOUT);
    }

    private boolean Is(byte b) {
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void onStop() {
        super.onStop();
        isrun = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Quee.getDefault().unregistcallback((byte) 0xff, CHECKEOUT);
    }
}
