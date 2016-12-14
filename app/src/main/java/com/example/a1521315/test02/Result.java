package com.example.a1521315.test02;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    double cal;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Button btn_tweet = (Button)findViewById(R.id.tweet);
        Button btn_back = (Button)findViewById(R.id.back);

        globals = (Globals)this.getApplication();

        saveList();

        coursename = globals.coursename;
        mileage = globals.mileage;
        //maxheartbeat = globals.maxheartbeat;
        maxheartbeat = globals.mode_flg;
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
        String stringal = new Double(cal).toString();
        textcal.setText(stringal);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globals.mode_flg == "fit") {
                            Intent intent = new Intent(getApplication(), TrainingSelect.class);
                            startActivity( intent );
                }else{
                    Intent intent = new Intent(getApplication(), VideoSelect.class);
                    startActivity( intent );
                }

            }
        });

        btn_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // IDを取得する
                /*String listName = myListItem.getName();

                globals.now_user = listName;
*/

                    // アラートダイアログ表示
                    AlertDialog.Builder builder = new AlertDialog.Builder(Result.this);
                    builder.setTitle("ログイン確認");
                    builder.setMessage(globals.now_user + "さんですか？");
                    // OKの時の処理
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dbLogin();

                            Intent intent = new Intent(Result.this, Resulttweet.class);
                            startActivity(intent);

                        }
                    });

                    builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Result.this, MenuSelect.class);
                            startActivity(intent);
                        }
                    });
                    // ダイアログの表示
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
        });

    }
    private void dbLogin() {

        long currentTimeMillis = System.currentTimeMillis();

        Date date = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", dateFormat.format(date));
        globals.login = dateFormat.format(date);

        int age = Integer.parseInt(globals.age);
        int height = Integer.parseInt(globals.height);
        int weight = Integer.parseInt(globals.weight);


        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.updateDB(globals.now_user, age, globals.sex, height, weight, globals.bmi , globals.ideal_weight, globals.login);   // DBに登録
        dbAdapter.closeDB();                                        // DBを閉じる
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


        ///////////////////////////////////////////////////////////////////
        if(globals.total_time != null){
            globals.total_time = globals.total_time + globals.time;
        }else {
            globals.total_time = globals.time;
        }
        globals.total_mileage = globals.mileage;
        /////////////////////////////////////////////////////////////////////////

        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.saveDB_DATA(globals.name_id, globals.now_user, globals.year, globals.month,
                globals.day,globals.times_of_day, globals.maxheartbeat,
                globals.cal, globals.total_time, globals.total_mileage, globals.coursename,
                globals.time, globals.avg, globals.max, globals.mileage);   // DBに登録
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
