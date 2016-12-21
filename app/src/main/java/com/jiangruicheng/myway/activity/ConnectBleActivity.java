package com.jiangruicheng.myway.activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangruicheng.myway.R;
import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.adapter.BleAdapter;
import com.jiangruicheng.myway.eventtype.BleConn;
import com.jiangruicheng.myway.eventtype.BluetoothSearch;
import com.jiangruicheng.myway.eventtype.ConnSucc;
import com.jiangruicheng.myway.eventtype.ReciveCmd;
import com.jiangruicheng.myway.eventtype.SendCmd;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ConnectBleActivity extends AppCompatActivity {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.device_list)
    ListView deviceList;
    @BindView(R.id.send)
    Button send;

    @OnClick(R.id.send)
    void onsend() {
        RxBus.getDefault().post(new SendCmd().setCmd(edit.getText().toString().getBytes()));
    }

    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.show)
    TextView show;

    @OnClick(R.id.back)
    void onback() {
        finish();
    }

    @BindView(R.id.search_device)
    TextView searchDevice;

    @OnClick(R.id.search_device)
    void onsearche() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        } else {
            RxBus.getDefault().post(new BluetoothSearch());
        }
    }


    @BindView(R.id.title)
    RelativeLayout title;

    private Subscription search;
    private BleAdapter adapter;
    private Subscription connsucc;
    private Subscription getmesg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_ble);
        Intent intent = new Intent(this, BleService.class);
        startService(intent);
        getmesg = RxBus.getDefault().toObservable(ReciveCmd.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ReciveCmd>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ReciveCmd reciveCmd) {
                show.setText(String.valueOf(reciveCmd.getCmd()));
            }
        });
        connsucc = RxBus.getDefault().toObservable(ConnSucc.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ConnSucc>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ConnSucc connSucc) {
                Toast.makeText(ConnectBleActivity.this, "succ", Toast.LENGTH_SHORT).show();

            }
        });
        search = RxBus.getDefault().toObservable(ScanResult.class).subscribe(new Observer<ScanResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onNext(ScanResult scanResult) {
                if (adapter == null) {
                    adapter = new BleAdapter(ConnectBleActivity.this);
                    deviceList.setAdapter(adapter);
                }
                adapter.addDevice(scanResult.getDevice());
            }
        });
        ButterKnife.bind(this);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter != null && !adapter.isEmpty()) {
                    RxBus.getDefault().post(new BleConn().setBluetoothDevice(adapter.getDevices().get(i)));
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if (search.isUnsubscribed()) {
            search.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (search.isUnsubscribed()) {
            search.unsubscribe();
        }
        if (connsucc.isUnsubscribed()) {
            connsucc.unsubscribe();
        }
    }
}
