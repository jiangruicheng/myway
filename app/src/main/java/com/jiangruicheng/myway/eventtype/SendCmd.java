package com.jiangruicheng.myway.eventtype;

/**
 * Created by jiang_ruicheng on 16/12/17.
 */
public class SendCmd {
    public byte[] getCmd() {
        return cmd;
    }

    public SendCmd setCmd(byte[] cmd) {
        this.cmd = cmd;
        return this;
    }

    private byte[] cmd;
}
