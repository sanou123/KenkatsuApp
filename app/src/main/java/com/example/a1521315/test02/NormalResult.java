package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Administrator on 2016/09/29.
 */

public class NormalResult extends Activity {
    Globals globals;
    String coursename;
    String mileage;
    String maxheartbeat;
    String avg;
    String max;
    String time;
    String kuriarank;
    double cal;

    /*コース名*/
    final String course0 = "瀬峰";
    final String course1 = "伊豆沼";
    final String course2 = "出羽海道";
    final String course3 = "鳴子";
    final String course6 = "瀬峰ショート2";
    final String course7 = "瀬峰ショート1";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_normal_result);
        Button btn_tweet = (Button)findViewById(R.id.tweet);
        Button btn_back = (Button)findViewById(R.id.back);

        globals = (Globals)this.getApplication();

        coursename = globals.coursename;
        mileage = globals.mileage;
        maxheartbeat = globals.maxheartbeat;
        avg = globals.avg;
        max = globals.max;
        time = globals.time;
        cal = globals.cal;
        kuriarank = globals.rank;


        TextView textcoursename = (TextView)findViewById(R.id.coursename);
        textcoursename.setText(coursename);
        TextView textmileage = (TextView)findViewById(R.id.mileage);
        textmileage.setText(mileage+"km");
        TextView textmaxheartbeat = (TextView)findViewById(R.id.maxheartbeat);
        textmaxheartbeat.setText(maxheartbeat+"bpm");
        TextView textavg = (TextView)findViewById(R.id.avg);
        textavg.setText(avg+"km/h");
        TextView textmax = (TextView)findViewById(R.id.max);
        textmax.setText(max+"km/h");
        TextView texttime = (TextView)findViewById(R.id.time);
        texttime.setText(time);
        TextView textcal = (TextView)findViewById(R.id.cal);
        String stringal = new Double(cal).toString();
        textcal.setText(stringal+"kcal");
        TextView rank = (TextView)findViewById(R.id.rank);
        rank.setText(kuriarank);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TrainingSelect.class);
                startActivity( intent );
            }
        });

        btn_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), NormalResultTweet.class);
                startActivity(i);
            }
        });

        saveList();

    }

    private void saveList() {


        long currentTimeMillis = System.currentTimeMillis();

        //////////年
        Date year = new Date(currentTimeMillis);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy年");
        yearFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", yearFormat.format(year));
        globals.year = yearFormat.format(year);
        //////////月
        Date month = new Date(currentTimeMillis);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM月");
        monthFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", monthFormat.format(month));
        globals.month = monthFormat.format(month);
        //////////日
        Date day = new Date(currentTimeMillis);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd日");
        dayFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", dayFormat.format(day));
        globals.day = dayFormat.format(day);
        //////////時間
        Date times_of_day = new Date(currentTimeMillis);
        SimpleDateFormat times_of_dayFormat = new SimpleDateFormat("HH時mm分ss秒");
        times_of_dayFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", times_of_dayFormat.format(times_of_day));
        globals.times_of_day = times_of_dayFormat.format(times_of_day);

        int name_id = Integer.parseInt(globals.name_id);

        //globals.cal = (8.4 * Double.valueOf(globals.time) * iWeight);

        globals.graph_time = (globals.hh*60) + globals.mm + (globals.ss/60) + (globals.ss%60);


        globals.total_mileage = globals.mileage;
        globals.total_time = globals.time;


        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.saveDB_DATA(name_id, globals.now_user, globals.year, globals.month, globals.day,
                            globals.times_of_day, globals.maxheartbeat, globals.cal, globals.total_time,
                            globals.total_mileage, globals.coursename, globals.time, globals.avg,
                            globals.max, globals.mileage,globals.training_name, globals.graph_time );   // DBに登録
        switch(globals.coursename) {
            case course0:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
            case course1:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
            case course2:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
            case course3:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
            case course6:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
            case course7:
                dbAdapter.saveDB_GHOST(globals.now_user, globals.coursename, globals.totalSeconds);
                break;
        }
        dbAdapter.closeDB();                                        // DBを閉じる

    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(getApplication(), TrainingSelect.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
