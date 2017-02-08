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
    static double cal;

    static String now_user;
    static String total_time ;
    static String bestrecord_time0 = "00:00:00.0";
    static String bestrecord_time1 = "00:00:00.0";
    static String bestrecord_time2 = "00:00:00.0";
    static String bestrecord_time3 = "00:00:00.0";
    static String bestrecord_time6 = "00:00:00.0";
    static String bestrecord_time7 = "00:00:00.0";
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
    static String user_year;
    static String user_month;
    static String user_day;
    static String times_of_day;
    static String login;
    static String age;
    static long hh;
    static long mm;
    static long ss;
    static long ms;
    static String  twitter_user;
    static String training_name;
    static long graph_time;

    int timflg1;//2
    int timflg2;//4
    int timflg3;//6
    int timflg4;//8
    int time_disp_cnt;
    static String rank;

    public void DriveDataInit(){
        coursename = "Init";
        mileage = "0";
        maxheartbeat = "0";
        avg = "0";
        max = "0";
        time = "0";
        cal = 0.0;
        timflg1 = 0;
        timflg2 = 0;
        timflg3 = 0;
        timflg4 = 0;
        time_disp_cnt = 0;
        rank = "";
        cal = 0;
    }

}
