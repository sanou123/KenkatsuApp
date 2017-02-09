package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.a1521315.test02.Age.getBirthCal;


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
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.result);
        Button btn_tweet = (Button)findViewById(R.id.tweet);
        Button btn_back = (Button)findViewById(R.id.back);

        globals = (Globals)this.getApplication();

        saveList();

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
                            dbLogin();

                            Intent intent = new Intent(Result.this, Resulttweet.class);
                            startActivity(intent);

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

        int height = Integer.parseInt(globals.height);
        int weight = Integer.parseInt(globals.weight);
        int iYear = Integer.parseInt(globals.user_year);
        int iMonth = Integer.parseInt(globals.user_month);
        int iDay = Integer.parseInt(globals.user_day);

        long iAge = getOld(globals.user_year+globals.user_month+globals.user_day);

        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.updateDB(globals.now_user, iAge, globals.sex, height, weight, globals.bmi ,
                globals.ideal_weight, globals.login, iYear, iMonth, iDay);   // DBに登録
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

        int name_id = Integer.parseInt(globals.name_id);

        globals.graph_time = (globals.hh*60) + globals.mm + (globals.ss/60)
                + (globals.ss%60) + (globals.ms);


        globals.total_mileage = globals.mileage;
        globals.total_time = globals.time;

        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.saveDB_DATA(name_id, globals.now_user, globals.year, globals.month, globals.day,
                globals.times_of_day, globals.maxheartbeat, globals.cal, globals.total_time,
                globals.total_mileage, globals.coursename, globals.time, globals.avg, globals.max,
                globals.mileage,globals.training_name, globals.graph_time);   // DBに登録
        dbAdapter.closeDB();                                        // DBを閉じる

    }

    /**
     * 年齢を返す
     *
     * @param birth_day yyyyMMdd 形式の誕生日
     * @return 年齢
     * @throws ParseException
     */

    public static long getOld(String birth_day) throws ParseException {

        Calendar birthCal = getBirthCal(birth_day);

        Date now = new Date();

        if (birthCal.getTime().after(now)) {

            return 0; // マイナスは0

        }

        Calendar nowCal = Calendar.getInstance();

        nowCal.setTime(now);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String nowYmd = df.format(now);

        return (Long.parseLong(nowYmd) - Long.parseLong(birth_day)) / 10000L;

    }


    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.VideoSelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
