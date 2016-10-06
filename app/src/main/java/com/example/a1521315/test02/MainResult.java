package com.example.a1521315.test02;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class MainResult extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener {

    private EditText mEditText01Name;        // 名前
    private EditText mEditText01Heart_rate;        // 心拍数
    private EditText mEditText01Calorie_consumption;         // 消費カロリー
    private EditText mEditText01Weight_fluctuates;         // 体重変化
    private EditText mEditText01Total_time;          // 総走行時間
    private EditText mEditText01Total_distance;          // 総走行距離


    private TextView mText01Kome01;             // 名前の※印
    private TextView mText01Kome02;             // 心拍数の※印
    private TextView mText01Kome03;             // 消費カロリーの※印
    private TextView mText01Kome04;             // 単価の※印
    private TextView mText01Kome05;             // 単価の※印
    private TextView mText01Kome06;             // 単価の※印

    private Button mButton01Regist;             // 登録ボタン
    private Button mButton01Show;               // 表示ボタン

    private RadioGroup mRadioGroup01Show;       // 選択用ラジオボタングループ

    private Intent intent;                      // インテント

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_result);

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
                        "com.example.a1521315.test02.MenuSelect");
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

        mEditText01Name = (EditText) findViewById(R.id.editText01Name);   // 品名
        mEditText01Heart_rate = (EditText) findViewById(R.id.editText01Heart_rate);   // 品名
        mEditText01Calorie_consumption = (EditText) findViewById(R.id.editText01Calorie_consumption);     // 産地
        mEditText01Weight_fluctuates = (EditText) findViewById(R.id.editText01Weight_fluctuates);     // 個数
        mEditText01Total_time = (EditText) findViewById(R.id.editText01Total_time);       // 単価
        mEditText01Total_distance = (EditText) findViewById(R.id.editText01Total_distance);       // 単価

        mText01Kome01 = (TextView) findViewById(R.id.text01Kome01);             // 品名の※印
        mText01Kome02 = (TextView) findViewById(R.id.text01Kome02);             // 産地※印
        mText01Kome03 = (TextView) findViewById(R.id.text01Kome03);             // 個数の※印
        mText01Kome04 = (TextView) findViewById(R.id.text01Kome04);             // 単価の※印
        mText01Kome05 = (TextView) findViewById(R.id.text01Kome05);             // 単価の※印
        mText01Kome06 = (TextView) findViewById(R.id.text01Kome06);             // 単価の※印

        mButton01Regist = (Button) findViewById(R.id.button01Regist1);           // 登録ボタン
        mButton01Show = (Button) findViewById(R.id.button01Show);               // 表示ボタン

        mRadioGroup01Show = (RadioGroup) findViewById(R.id.radioGroup011);       // 選択用ラジオボタングループ

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Name.setText("");
        mEditText01Heart_rate.setText("");
        mEditText01Calorie_consumption.setText("");
        mEditText01Weight_fluctuates.setText("");
        mEditText01Total_time.setText("");
        mEditText01Total_distance.setText("");


        mText01Kome01.setText("");
        mText01Kome02.setText("");
        mText01Kome03.setText("");
        mText01Kome04.setText("");
        mText01Kome05.setText("");
        mText01Kome06.setText("");

        mEditText01Heart_rate.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    /**
     * ラジオボタン選択処理
     * onCheckedChanged()
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton01Product1:         // 品名一覧(ListView×ArrayAdapter)を選択した場合
                intent = new Intent(MainResult.this, SelectSheetProduct.class);
                break;
            case R.id.radioButton01ListView1:        // ListView表示を選択した場合
                intent = new Intent(MainResult.this, SelectSheetListView.class);
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
        String strName = mEditText01Name.getText().toString();
        String strHeart_rate = mEditText01Heart_rate.getText().toString();
        String strCalorie_consumption = mEditText01Calorie_consumption.getText().toString();
        String strWeight_fluctuates = mEditText01Weight_fluctuates.getText().toString();
        String strTotal_time = mEditText01Total_time.getText().toString();
        String strTotal_distance = mEditText01Total_distance.getText().toString();


        // EditTextが空白の場合
        if (strName.equals("") || strHeart_rate.equals("") || strCalorie_consumption.equals("") || strWeight_fluctuates.equals("")
                || strTotal_time.equals("") || strTotal_distance.equals("")) {

            if (strName.equals("")) {
                mText01Kome01.setText("※");     // 品名が空白の場合、※印を表示
            } else {
                mText01Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strHeart_rate.equals("")) {
                mText01Kome02.setText("※");     // 品名が空白の場合、※印を表示
            } else {
                mText01Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strCalorie_consumption.equals("")) {
                mText01Kome03.setText("※");     // 産地が空白の場合、※印を表示
            } else {
                mText01Kome03.setText("");      // 空白でない場合は※印を消す
            }

            if (strWeight_fluctuates.equals("")) {
                mText01Kome04.setText("※");     // 個数が空白の場合、※印を表示
            } else {
                mText01Kome04.setText("");      // 空白でない場合は※印を消す
            }

            if (strTotal_time.equals("")) {
                mText01Kome05.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome05.setText("");      // 空白でない場合は※印を消す
            }

            if (strTotal_time.equals("")) {
                mText01Kome06.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01Kome06.setText("");      // 空白でない場合は※印を消す
            }

            Toast.makeText(MainResult.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int iHeart_rate = Integer.parseInt(strHeart_rate);
            int iCalorie_consumption = Integer.parseInt(strCalorie_consumption);
            int iWeight_fluctuates = Integer.parseInt(strWeight_fluctuates);
            int iTotal_time = Integer.parseInt(strTotal_time);
            int iTotal_distance = Integer.parseInt(strTotal_time);

            // DBへの登録処理
            DBAdapter1 dbAdapter1 = new DBAdapter1(this);
            dbAdapter1.openDB();                                         // DBの読み書き
            dbAdapter1.saveDB(strName, iHeart_rate, iCalorie_consumption, iWeight_fluctuates,
                    iTotal_time, iTotal_distance);   // DBに登録
            dbAdapter1.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        }

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