package com.example.a1521315.test02;

import android.app.Application;

/**
 * Created by Administrator on 2016/10/31.
 */

public class Globals extends Application {
    //リザルトで使う変数
    static String coursename;
    static String mileage;
    static String maxheartbeat;
    static String avg;
    static String max;
    static String time;
    static double cal = 12.3;
    static String now_user;
    static String total_time ;
    static String total_mileage;
    static String name_id;
    static double bmi;
    static String weight;
    static String height;
    static String sex;
    static double ideal_weight;
    static String year;
    static String month;
    static String day;
    static String times_of_day;
    static String age;
    static String deal_weight;

    static String mode_flg;
    int timflg1;
    int time_disp_cnt;

    public void DriveDataInit(){
        coursename = "Init";
        mileage = "0";
        maxheartbeat = "0";
        avg = "0";
        max = "0";
        time = "0";
        cal = 0.0;
        mode_flg = "";
        timflg1 = 0;
        time_disp_cnt = 0;
    }

}
