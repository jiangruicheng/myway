package com.jiangruicheng.myway.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.activity.CarManagerActivity;
import com.jiangruicheng.myway.view.ArcProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    @BindView(R.id.car_id)
    TextView carId;
    @BindView(R.id.licheng)
    ArcProgressBar licheng;
    @BindView(R.id.dianliang)
    ArcProgressBar dianliang;
    @BindView(R.id.biaopan)
    RelativeLayout biaopan;
    @BindView(R.id.search_ble)
    TextView searchBle;

    @OnClick(R.id.search_ble)
    void onsearch_ble() {
        Intent intent = new Intent(getActivity(), CarManagerActivity.class);
        getActivity().startActivity(intent);
    }

    public CarFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        unbinder = ButterKnife.bind(this, view);
        dianliang.setAllRota(45);
        licheng.setAllRota(315);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
