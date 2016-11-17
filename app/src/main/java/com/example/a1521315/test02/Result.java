package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Administrator on 2016/09/29.
 */

public class Result extends Activity {
    Globals globals;
    String coursename;
    String mileage;
    String maxheartbeat;
    String avg;
    String max;
    String time;
    String cal;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
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

        TextView textcoursename = (TextView)findViewById(R.id.coursename);
        textcoursename.setText(coursename);
        TextView textmileage = (TextView)findViewById(R.id.mileage);
        textmileage.setText(mileage);
        TextView textmaxheartbeat = (TextView)findViewById(R.id.maxheartbeat);
        textmaxheartbeat.setText(maxheartbeat);
        TextView textavg = (TextView)findViewById(R.id.avg);
        textavg.setText(avg);
        TextView textmax = (TextView)findViewById(R.id.max);
        textmax.setText(max);
        TextView texttime = (TextView)findViewById(R.id.time);
        texttime.setText(time);
        TextView textcal = (TextView)findViewById(R.id.cal);
        textcal.setText(cal);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), VideoSelect.class);
                startActivity( intent );
            }
        });

        btn_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/intent/tweet?hashtags=けんかつAPP &text="+"コース名："+coursename+"%0a"+"走行距離："+mileage+"%0a"+"最大心拍："+maxheartbeat+"%0a"+"平均速度："+avg+"%0a"+"最高速度："+max+"%0a"+"運動時間："+time+"%0a"+"消費カロリー："+cal+"%0a"));
                startActivity(i);
            }
        });

        // DBに登録
        saveList();


    }
    private void saveList() {


        long currentTimeMillis = System.currentTimeMillis();

        Date date = new Date(currentTimeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH時mm分ss秒");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", simpleDateFormat.format(date));


        globals.bmi = (Integer.parseInt(globals.weight) / (Integer.parseInt(globals.height) * Integer.parseInt(globals.height)));
        //globals.cal = (1.05 * 9 * Integer.parseInt(globals.time) * Integer.parseInt(globals.weight));

        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.saveDB_DATA(globals.name_id, globals.now_user, simpleDateFormat.format(date), globals.maxheartbeat,
                globals.cal, globals.total_time, globals.total_mileage, globals.coursename,
                globals.time, globals.avg, globals.max, globals.mileage, String.valueOf(globals.bmi));   // DBに登録
        dbAdapter.closeDB();                                        // DBを閉じる

    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
