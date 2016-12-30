package com.jiangruicheng.myway.util;

import android.util.Log;

import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.data.Command;
import com.jiangruicheng.myway.eventtype.ReciveCmd;
import com.jiangruicheng.myway.eventtype.SendCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kongqing on 16-12-29.
 */
public class Quee {
    interface callback {
        void callback(byte[] b);
    }

    private QueeThread   queeThread;
    private Subscription recive;

    private Map<Integer, List<callback>> eventcallbackmap;

    private static class GetQuee {
        private static final Quee QUEE = new Quee();
    }

    public static final Quee getDefault() {
        return GetQuee.QUEE;
    }

    private Quee() {
        if (eventcallbackmap == null) {
            eventcallbackmap = new HashMap<>();
        }
    }

    public void onstart() {

        if (queeThread == null) {
            queeThread = new QueeThread();
            new Thread(queeThread).start();
        }

        if (recive == null) {
            recive = RxBus.getDefault().
                    toObservable(ReciveCmd.class).
                /*observeOn(AndroidSchedulers.mainThread()).*/
                        subscribe(new Observer<ReciveCmd>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ReciveCmd reciveCmd) {
                        try {
                            if (reciveCmd.getCmd()[0] == Command.HEAD_LOW && reciveCmd.getCmd()[1] == Command.HEAD_HEIGHT && Command.chechsum(reciveCmd.getCmd())) {
                                byte eventtype = 0;
                                if (reciveCmd.getCmd() != null && reciveCmd.getCmd()[2] != 0x01 && reciveCmd.getCmd()[2] != 0x02) {
                                    eventtype = geteventtype(reciveCmd.getCmd());
                                }

                                if (eventtype == Command.EVENT_ACK) {
                                    pop();
                                    onnext();
                                }
                                if (eventcallbackmap != null && eventcallbackmap.containsKey(eventtype)) {
                                    for (callback c : eventcallbackmap.get(eventtype)) {
                                        c.callback(reciveCmd.getCmd());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.d("error", "onNext: " + e.getMessage());
                        }
                    }
                });
        }
    }

    public void registcallback(int type, callback callback) {
        if (eventcallbackmap != null) {
            if (!eventcallbackmap.containsKey(type)) {
                List<callback> callbacks = new ArrayList<>();
                callbacks.add(callback);
                eventcallbackmap.put(type, callbacks);
            } else {
                eventcallbackmap.get(type).add(callback);
            }
        }
    }

    public void unregistcallback(int type, callback callback) {
        if (eventcallbackmap != null) {
            if (!eventcallbackmap.containsKey(type)) {
                return;
            } else {
                eventcallbackmap.get(type).remove(callback);
            }
        }
    }

    private byte geteventtype(byte[] b) {
        if (b[3] > 20) {
            return (byte) 0xff;
        }
        if (b[2] == 0x05) {
            return b[4];
        } else {
            return b[2];
        }
    }

    private void getcallback(int type) {
        if (eventcallbackmap != null && eventcallbackmap.containsKey(type)) {
            for (callback c : eventcallbackmap.get(type)) {
                c.callback(new byte[]{});
            }
        }
    }

    public void sendcomm(byte[] b) {
        if (queeThread != null) {
            queeThread.addcomm(b);

        }
    }

    public void removecomm(byte[] b) {
        if (queeThread != null) {
            queeThread.removecomm(b);
        }
    }

    public void pop() {
        if (queeThread != null) {
            queeThread.pop();
        }
    }

    public void onstop() {
        if (queeThread != null) {
            queeThread.onstop();
            queeThread = null;
        }
        if (eventcallbackmap != null) {
            eventcallbackmap = null;
        }
        if (recive.isUnsubscribed()) {
            recive.unsubscribe();
            recive = null;
        }
    }

    public void onnext() {
        if (queeThread != null) {
            queeThread.next();
        }
    }

    private class QueeThread extends Thread {
        private boolean      isrun;
        private boolean      stop;
        private List<byte[]> comm;

        public QueeThread() {
            comm = new ArrayList<>();
            isrun = true;
            stop = false;
        }

        public void addcomm(byte[] b) {
            comm.add(b);
            Log.i("send", "addcomm: " + isrun);
        }

        public void removecomm(byte[] b) {
            comm.remove(b);
        }

        public void pop() {
            comm.remove(comm.size() - 1);
        }

        public void onstop() {
            stop = true;
        }

        public void next() {
            isrun = true;
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                if (stop) {
                    break;
                }
                if (!comm.isEmpty() && isrun) {
                    RxBus.getDefault().post(new SendCmd().setCmd(comm.get(comm.size() - 1)));
                    isrun = false;
                }
            }
        }
    }
}
