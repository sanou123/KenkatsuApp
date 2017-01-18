package com.example.a1521315.test02;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * メイン画面に関連するクラス
 *  MainActivity
 */
public class UserUpdate extends AppCompatActivity {

    Globals globals;

    private EditText mEditText01Height;         // 身長
    private EditText mEditText01Weight;          // 体重

    private TextView mText01Kome04;             // 身長の※印
    private TextView mText01Kome05;             // 体重の※印


    private Button mButton01Regist;             // 登録ボタン


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.user_update);

        // TextView インスタンス生成
        TextView textView = (TextView)findViewById(R.id.title01);

        String columns = globals.now_user + "さんの登録情報を更新します。";
        textView.setText(columns);


        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定


        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);

                // キーボードを非表示
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // DBに登録
                saveList();

            }
        });

        Button btnDisp = (Button) findViewById(R.id.cancel);
        btnDisp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.MenuSelect");
                startActivity(intent);
            }
        });
    }


    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        mEditText01Height = (EditText) findViewById(R.id.editText01Height);     // 身長
        mEditText01Weight = (EditText) findViewById(R.id.editText01Weight);       // 体重

        mText01Kome04 = (TextView) findViewById(R.id.text01Kome04);             // 身長の※印
        mText01Kome05 = (TextView) findViewById(R.id.text01Kome05);             // 体重の※印

        mButton01Regist = (Button) findViewById(R.id.button01Regist);           // 登録ボタン

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Height.setText("");
        mEditText01Weight.setText("");

        mText01Kome04.setText("");
        mText01Kome05.setText("");
    }

        /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        String strHeight = mEditText01Height.getText().toString();
        String strWeight = mEditText01Weight.getText().toString();


        // EditTextが空白の場合
        if (strHeight.equals("") || strWeight.equals("")) {

            if (strHeight.equals("")) {
                mText01Kome04.setText("※");     // 身長が空白の場合、※印を表示
            } else {
                mText01Kome04.setText("");      // 空白でない場合は※印を消す
            }

            if (strWeight.equals("")) {
                mText01Kome05.setText("※");     // 体重が空白の場合、※印を表示
            } else {
                mText01Kome05.setText("");      // 空白でない場合は※印を消す
            }

            Toast.makeText(UserUpdate.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合


            // 入力された年齢と身長、体重は文字列からint型へ変換
            int iAge = Integer.parseInt(globals.age);
            int iHeight = Integer.parseInt(strHeight);
            int iWeight = Integer.parseInt(strWeight);

            //BMIの算出
            double dbmi = iWeight / ((iHeight * 0.01) * (iHeight * 0.01));
            //理想体重の算出
            double dideal_weight = ((iHeight * 0.01) * (iHeight * 0.01)) * 22;

            //元データをBigDecimal型にする
            BigDecimal bd_bmi = new BigDecimal(dbmi);
            //四捨五入する
            BigDecimal bmi = bd_bmi.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位

            //元データをBigDecimal型にする
            BigDecimal bd_i_w = new BigDecimal(dideal_weight);
            //四捨五入する
            BigDecimal ideal_weight = bd_i_w.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位

            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.updateDB(globals.now_user, iAge, globals.sex, iHeight, iWeight,
                    bmi.doubleValue(), ideal_weight.doubleValue(), globals.login);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        }
    }

    //戻るキーを無効にする
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.MenuSelect");
            startActivity(intent);        }
        return super.onKeyDown(keyCode, event);
    }
}