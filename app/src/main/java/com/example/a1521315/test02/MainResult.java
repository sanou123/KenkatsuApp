package com.example.a1521315.test02;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class MainResult extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener {

    Globals globals;

    //private EditText mEditText01Name;        // 名前
    private EditText mEditText01Heart_rate;        // 心拍数
    private EditText mEditText01Calorie_consumption;         // 消費カロリー
    private EditText mEditText01Total_time;          // 総走行時間
    private EditText mEditText01Total_distance;          // 総走行距離
    private EditText mEditText01Course_name;         //コース名
    private EditText mEditText01Time;         //タイム
    private EditText mEditText01Avg_speed;         //平均速度
    private EditText mEditText01Max_speed;        //最高速度
    private EditText mEditText01Distance;        //走行距離
    private EditText mEditText01BMI;        //走行距離




    private TextView mText01Kome01;             // 心拍数の※印
    private TextView mText01Kome02;             // 消費カロリーの※印
    private TextView mText01Kome03;             // 総走行時間の※印
    private TextView mText01Kome04;             // 総走行距離の※印
    private TextView mText01Kome05;             // コース名の※印
    private TextView mText01Kome06;             // タイムの※印
    private TextView mText01Kome07;             // 平均速度の※印
    private TextView mText01Kome08;             // 最高速度の※印
    private TextView mText01Kome09;             // 走行距離の※印
    private TextView mText01Kome10;             // BMIの※印



    private Button mButton01Regist;             // 登録ボタン
    private Button mButton01Show;               // 表示ボタン

    private RadioGroup mRadioGroup01Show;       // 選択用ラジオボタングループ

    private Intent intent;                      // インテント

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_result);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定

        // ラジオボタン選択時
        mRadioGroup01Show.setOnCheckedChangeListener(this);

        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.VideoSelect");
                startActivity(intent);

                // キーボードを非表示
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // DBに登録
                saveList();

            }
        });

        // 表示ボタン押下時処理
        mButton01Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null) {
                    startActivity(intent);      // 各画面へ遷移
                } else {
                    Toast.makeText(MainResult.this, "ラジオボタンが選択されていません。", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnDisp = (Button) findViewById(R.id.cancel);
        btnDisp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);
            }
        });
    }


    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        //mEditText01Name = (EditText) findViewById(R.id.editText01Name);   // 名前
        mEditText01Heart_rate = (EditText) findViewById(R.id.editText01Heart_rate);   // 心拍数
        mEditText01Calorie_consumption = (EditText) findViewById(R.id.editText01Calorie_consumption);     // 消費カロリー
        mEditText01Total_time = (EditText) findViewById(R.id.editText01Total_time);       // 総走行時間
        mEditText01Total_distance = (EditText) findViewById(R.id.editText01Total_distance);       // 総走行距離
        mEditText01Course_name = (EditText) findViewById(R.id.editText01Course_name);       // コース名
        mEditText01Time = (EditText) findViewById(R.id.editText01Time);       // タイム
        mEditText01Avg_speed = (EditText) findViewById(R.id.editText01Avg_speed);       // 平均速度
        mEditText01Max_speed = (EditText) findViewById(R.id.editText01Max_speed);       // 最高速度
        mEditText01Distance = (EditText) findViewById(R.id.editText01Distance);       // 走行距離
        mEditText01BMI = (EditText) findViewById(R.id.editText01BMI);       // 走行距離



        mText01Kome01 = (TextView) findViewById(R.id.text01Kome01);             // 品名の※印
        mText01Kome02 = (TextView) findViewById(R.id.text01Kome02);             // 産地※印
        mText01Kome03 = (TextView) findViewById(R.id.text01Kome03);             // 個数の※印
        mText01Kome04 = (TextView) findViewById(R.id.text01Kome04);             // 単価の※印
        mText01Kome05 = (TextView) findViewById(R.id.text01Kome05);             // 単価の※印
        mText01Kome06 = (TextView) findViewById(R.id.text01Kome06);             // 単価の※印
        mText01Kome07 = (TextView) findViewById(R.id.text01Kome07);             // 単価の※印
        mText01Kome08 = (TextView) findViewById(R.id.text01Kome08);             // 単価の※印
        mText01Kome09 = (TextView) findViewById(R.id.text01Kome09);             // 単価の※印
        mText01Kome10 = (TextView) findViewById(R.id.text01Kome10);             // 単価の※印


        mButton01Regist = (Button) findViewById(R.id.button01Regist1);           // 登録ボタン
        mButton01Show = (Button) findViewById(R.id.button01Show);               // 表示ボタン

        mRadioGroup01Show = (RadioGroup) findViewById(R.id.radioGroup011);       // 選択用ラジオボタングループ

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {

        //mEditText01Name.setText("");
        mEditText01Heart_rate.setText(globals.maxheartbeat);
        mEditText01Calorie_consumption.setText(globals.cal);
        mEditText01Total_time.setText("");
        mEditText01Total_distance.setText("");
        mEditText01Course_name.setText(globals.coursename);
        mEditText01Time.setText(globals.time);
        mEditText01Avg_speed.setText(globals.avg);
        mEditText01Max_speed.setText(globals.max);
        mEditText01Distance.setText(globals.mileage);
        mEditText01BMI.setText("");





        mText01Kome01.setText("");
        mText01Kome02.setText("");
        mText01Kome03.setText("");
        mText01Kome04.setText("");
        mText01Kome05.setText("");
        mText01Kome06.setText("");
        mText01Kome07.setText("");
        mText01Kome08.setText("");
        mText01Kome09.setText("");
        mText01Kome10.setText("");


        //mEditText01Name.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    /**
     * ラジオボタン選択処理
     * onCheckedChanged()
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton01Product1:         // 品名一覧(ListView×ArrayAdapter)を選択した場合
                intent = new Intent(MainResult.this, SelectSheetProduct1.class);
                break;
            case R.id.radioButton01ListView1:        // ListView表示を選択した場合
                intent = new Intent(MainResult.this, SelectSheetListView1.class);
                break;
            case R.id.radioButton01TableLayout1:     // TableLayout表示を選択した場合
                intent = new Intent(MainResult.this, SelectSheetTable1.class);
                break;
        }
    }

    /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        //String strName = mEditText01Name.getText().toString();
        String strHeart_rate = mEditText01Heart_rate.getText().toString();
        String strCalorie_consumption = mEditText01Calorie_consumption.getText().toString();
        String strTotal_time = mEditText01Total_time.getText().toString();
        String strTotal_distance = mEditText01Total_distance.getText().toString();
        String strCourse_name = mEditText01Course_name.getText().toString();
        String strTime = mEditText01Time.getText().toString();
        String strAvg_speed = mEditText01Avg_speed.getText().toString();
        String strMax_speed = mEditText01Max_speed.getText().toString();
        String strDistance = mEditText01Distance.getText().toString();
        String strBMI = mEditText01BMI.getText().toString();



        // EditTextが空白の場合
        if (strHeart_rate.equals("") || strCalorie_consumption.equals("") ||
                strTotal_time.equals("") || strTotal_distance.equals("") ||
                strCourse_name.equals("") || strTime.equals("") || strAvg_speed.equals("") ||
                strMax_speed.equals("") || strDistance.equals("") || strBMI.equals("")) {


            if (strHeart_rate.equals("")) {
                mText01Kome01.setText("※");     // 産地が空白の場合、※印を表示
            } else {
                mText01Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strCalorie_consumption.equals("")) {
                mText01Kome02.setText("※");     // 個数が空白の場合、※印を表示
            } else {
                mText01Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strTotal_time.equals("")) {
                mText01Kome03.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome03.setText("");      // 空白でない場合は※印を消す
            }

            if (strTotal_distance.equals("")) {
                mText01Kome04.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome04.setText("");      // 空白でない場合は※印を消す
            }
            if (strCourse_name.equals("")) {
                mText01Kome05.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome05.setText("");      // 空白でない場合は※印を消す
            }

            if (strTime.equals("")) {
                mText01Kome06.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome06.setText("");      // 空白でない場合は※印を消す
            }

            if (strAvg_speed.equals("")) {
                mText01Kome07.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome07.setText("");      // 空白でない場合は※印を消す
            }

            if (strMax_speed.equals("")) {
                mText01Kome08.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome08.setText("");      // 空白でない場合は※印を消す
            }

            if (strDistance.equals("")) {
                mText01Kome09.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome09.setText("");      // 空白でない場合は※印を消す
            }

            if (strBMI.equals("")) {
                mText01Kome10.setText("※");     // 品名が空白の場合、※印を表示
            } else {
                mText01Kome10.setText("");      // 空白でない場合は※印を消す
            }


            Toast.makeText(MainResult.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された値を文字列からint型へ変換
/*          int iHeart_rate = Integer.parseInt(strHeart_rate);
            int iCalorie_consumption = Integer.parseInt(strCalorie_consumption);
            int iTotal_time = Integer.parseInt(strTotal_time);
            int iTotal_distance = Integer.parseInt(strTotal_distance);
            int iTime = Integer.parseInt(strTime);
            int iAvg_speed = Integer.parseInt(strAvg_speed);
            int iMax_speed = Integer.parseInt(strMax_speed);
            int iDistance = Integer.parseInt(strDistance);
            int iBMI = Integer.parseInt(strBMI);
*/

            long currentTimeMillis = System.currentTimeMillis();

            Date date = new Date(currentTimeMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH時mm分ss秒");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            Log.v("時間", simpleDateFormat.format(date));

            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.saveDB_DATA(globals.name_id, globals.now_user, simpleDateFormat.format(date), globals.maxheartbeat,
                    globals.cal, globals.total_time, globals.total_mileage, globals.coursename,
                    globals.time, globals.avg, globals.max, globals.mileage, globals.bmi);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        }

    }

}