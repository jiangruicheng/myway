package com.jiangruicheng.myway.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.data.Command;
import com.jiangruicheng.myway.eventtype.SendCmd;
import com.jiangruicheng.myway.util.Quee;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CarStatuFragment extends Fragment {


    @BindView(R.id.back)
    ImageView      back;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.path)
    LinearLayout   path;
    @BindView(R.id.checkout_wrong)
    LinearLayout   checkoutWrong;
    @BindView(R.id.button_layout)
    LinearLayout   buttonLayout;

    @OnClick(R.id.checkout_wrong)
    void oncheckout() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new CarCheckOutFragment()).addToBackStack("").commit();
    }

    @BindView(R.id.seekbar_speed)
    SeekBar      seekbarSpeed;
    @BindView(R.id.text_speed)
    TextView     textSpeed;
    @BindView(R.id.text_handle)
    TextView     textHandle;
    @BindView(R.id.btn_handle)
    SwitchButton btnHandle;
    @BindView(R.id.exp)
    TextView     exp;
    @BindView(R.id.checkout_car)
    TextView     checkoutCar;

    @OnClick(R.id.checkout_car)
    void oncheckoutcar() {
        RxBus.getDefault().post(new SendCmd().setCmd(Command.getCommand(Command.setData(Command.COM_ADJUST, new byte[]{0x01}))));
    }

    @BindView(R.id.checkout_car_layout)
    LinearLayout checkoutCarLayout;
    @BindView(R.id.repassword)
    TextView     repassword;
    @BindView(R.id.updata)
    TextView     updata;
    private String Cartype;

    @SuppressLint("ValidFragment")
    public CarStatuFragment(String cartype) {
        // Required empty public constructor
        this.Cartype = cartype;
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_statu, container, false);
        unbinder = ButterKnife.bind(this, view);
        switch (Cartype) {
            case "SA":
                checkoutCarLayout.setVisibility(View.GONE);
                exp.setVisibility(View.GONE);
                textHandle.setText("滑行启动开关");
                break;
            case "RA":
                exp.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                break;
        }
        btnHandle.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                byte[] data = new byte[1];
                if (isChecked) {
                    data = new byte[]{0x01};
                } else {
                    data = new byte[]{0x00};
                }
                RxBus.getDefault().post(new SendCmd().setCmd(
                        Command.getCommand(Command.setData(
                                Command.COM_SLIDE, data))));
            }
        });
        seekbarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RxBus.getDefault().post(new SendCmd().setCmd(Command.getCommand
                        (Command.setData(Command.COM_LOCKSPEED,
                                new byte[]{(byte) seekBar.getProgress()}))));
            }
        });
        setcallback();
        return view;
    }

    private Quee.callback LOCKSPEED;

    private void setcallback() {
        LOCKSPEED = new Quee.callback() {
            @Override
            public void callback(byte[] b) {
                if (b[5] == 1) {
                    textSpeed.setText(seekbarSpeed.getProgress() + "");
                }
            }
        };
        Quee.getDefault().registcallback(Command.COM_LOCKSPEED, LOCKSPEED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Quee.getDefault().unregistcallback(Command.COM_LOCKSPEED, LOCKSPEED);
        unbinder.unbind();
    }
}
