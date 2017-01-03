package com.jiangruicheng.myway.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongqing on 17-1-3.
 */
public class CarStatu {
    public Statue getStatue() {
        return statue;
    }

    public void setStatue(Statue statue) {
        this.statue = statue;
    }

    private Statue          statue     = new Statue();
    private List<CarConned> carConneds = new ArrayList();

    public void addconnected(CarConned carConned) {
        carConneds.add(carConned);
    }

    public List<CarConned> getCarconn() {
        return carConneds;
    }

    private static class GetClass {
        private static final CarStatu STATU = new CarStatu();
    }

    public static CarStatu getDefault() {
        return GetClass.STATU;
    }

    public static class Statue implements Serializable {
        public static final int SOFT_MODE = 0;
        public static final int HARD_MODE = 1;
        private boolean is_carlock;
        private boolean is_slide;

        public int getCarstartmode() {
            return carstartmode;
        }

        public void setCarstartmode(int carstartmode) {
            this.carstartmode = carstartmode;
        }

        public boolean is_carlock() {
            return is_carlock;
        }

        public void setIs_carlock(boolean is_carlock) {
            this.is_carlock = is_carlock;
        }

        public boolean is_slide() {
            return is_slide;
        }

        public void setIs_slide(boolean is_slide) {
            this.is_slide = is_slide;
        }

        public boolean is_befor_light_ON() {
            return is_befor_light_ON;
        }

        public void setIs_befor_light_ON(boolean is_befor_light_ON) {
            this.is_befor_light_ON = is_befor_light_ON;
        }

        public boolean is_after_light_ON() {
            return is_after_light_ON;
        }

        public void setIs_after_light_ON(boolean is_after_light_ON) {
            this.is_after_light_ON = is_after_light_ON;
        }

        public boolean is_power_right() {
            return is_power_right;
        }

        public void setIs_power_right(boolean is_power_right) {
            this.is_power_right = is_power_right;
        }

        public boolean is_tran_right() {
            return is_tran_right;
        }

        public void setIs_tran_right(boolean is_tran_right) {
            this.is_tran_right = is_tran_right;
        }

        public boolean is_esp_right() {
            return is_esp_right;
        }

        public void setIs_esp_right(boolean is_esp_right) {
            this.is_esp_right = is_esp_right;
        }

        public boolean is_drive_right() {
            return is_drive_right;
        }

        public void setIs_drive_right(boolean is_drive_right) {
            this.is_drive_right = is_drive_right;
        }

        private int     carstartmode;
        private boolean is_befor_light_ON;
        private boolean is_after_light_ON;
        private boolean is_power_right;
        private boolean is_tran_right;
        private boolean is_esp_right;
        private boolean is_drive_right;
    }

    public static class CarConned implements Serializable {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String name;
        private String address;
        private String type;
    }
}
