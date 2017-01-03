package com.jiangruicheng.myway.fragment;


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
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarStatuFragment extends Fragment {


    @BindView(R.id.back)
    ImageView      back;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.path)
    LinearLayout   path;
    @BindView(R.id.checkout_wrong)
    LinearLayout   checkoutWrong;
    @OnClick(R.id.checkout_wrong)
    void oncheckout(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,new CarCheckOutFragment()).addToBackStack("").commit();
    }
    @BindView(R.id.seekbar_speed)
    SeekBar        seekbarSpeed;
    @BindView(R.id.text_speed)
    TextView       textSpeed;
    @BindView(R.id.text_handle)
    TextView       textHandle;
    @BindView(R.id.btn_handle)
    SwitchButton   btnHandle;
    @BindView(R.id.exp)
    TextView       exp;
    @BindView(R.id.checkout_car)
    TextView       checkoutCar;
    @BindView(R.id.checkout_car_layout)
    LinearLayout   checkoutCarLayout;
    @BindView(R.id.repassword)
    TextView       repassword;
    @BindView(R.id.updata)
    TextView       updata;

    public CarStatuFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_statu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
