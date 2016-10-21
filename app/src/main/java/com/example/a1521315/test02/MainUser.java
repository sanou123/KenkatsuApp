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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class MainUser extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener {

    private EditText mEditText01Name;        // 名前
    private EditText mEditText01Age;         // 年齢
    private RadioGroup mRadioGroup01Sex;         // 性別
    private EditText mEditText01Height;         // 身長
    private EditText mEditText01Weight;          // 体重

    private TextView mText01Kome01;             // 名前の※印
    private TextView mText01Kome02;             // 年齢の※印
    private TextView mText01Kome03;             // 性別の※印
    private TextView mText01Kome04;             // 身長の※印
    private TextView mText01Kome05;             // 体重の※印


    private Button mButton01Regist;             // 登録ボタン
    private Button mButton01Show;               // 表示ボタン

    private RadioGroup mRadioGroup01Show;       // 選択用ラジオボタングループ

    private Intent intent;                      // インテント

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.user_registration);

        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定

        // ラジオボタン選択時
        mRadioGroup01Show.setOnCheckedChangeListener(this);

        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.myapplication",
                        "com.example.a1521315.myapplication.SelectSheetListView");
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
                    Toast.makeText(MainUser.this, "ラジオボタンが選択されていません。",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnDisp = (Button) findViewById(R.id.cancel);
        btnDisp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.myapplication",
                        "com.example.a1521315.myapplication.SelectSheetListView");
                startActivity(intent);
            }
        });
    }


    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        mEditText01Name = (EditText) findViewById(R.id.editText01Name);   // 名前
        mEditText01Age = (EditText) findViewById(R.id.editText01Age);     // 年齢
        mRadioGroup01Sex = (RadioGroup) findViewById(R.id.radioGroup01Sex);     // 年齢
        mEditText01Height = (EditText) findViewById(R.id.editText01Height);     // 身長
        mEditText01Weight = (EditText) findViewById(R.id.editText01Weight);       // 体重

        mText01Kome01 = (TextView) findViewById(R.id.text01Kome01);             // 名前の※印
        mText01Kome02 = (TextView) findViewById(R.id.text01Kome02);             // 年齢※印
        mText01Kome03 = (TextView) findViewById(R.id.text01Kome03);             // 性別の※印
        mText01Kome04 = (TextView) findViewById(R.id.text01Kome04);             // 身長の※印
        mText01Kome05 = (TextView) findViewById(R.id.text01Kome05);             // 体重の※印

        mButton01Regist = (Button) findViewById(R.id.button01Regist);           // 登録ボタン
        mButton01Show = (Button) findViewById(R.id.button01Show);               // 表示ボタン

        mRadioGroup01Show = (RadioGroup) findViewById(R.id.radioGroup01);       // 選択用ラジオボタングループ

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Name.setText("");
        mEditText01Age.setText("");
        mEditText01Height.setText("");
        mEditText01Weight.setText("");

        mText01Kome01.setText("");
        mText01Kome02.setText("");
        mText01Kome03.setText("");
        mText01Kome04.setText("");
        mText01Kome05.setText("");

        mEditText01Name.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    /**
     * ラジオボタン選択処理
     * onCheckedChanged()
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton01Product:         // 名前一覧(ListView×ArrayAdapter)を選択した場合
                intent = new Intent(MainUser.this, SelectSheetProduct.class);
                break;
        }
    }

    /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        int checkedId = mRadioGroup01Sex.getCheckedRadioButtonId();

        RadioButton mRadioGroup01Sex = (RadioButton) findViewById(checkedId);// (Fragmentの場合は「getActivity().findViewById」にする)

        // 各EditTextで入力されたテキストを取得
        String strName = mEditText01Name.getText().toString();
        String strAge = mEditText01Age.getText().toString();
        String strSex = mRadioGroup01Sex.getText().toString();
        String strHeight = mEditText01Height.getText().toString();
        String strWeight = mEditText01Weight.getText().toString();

        // EditTextが空白の場合
        if (strName.equals("") || strAge.equals("") || strSex.equals("")
                || strHeight.equals("") || strWeight.equals("")) {

            if (strName.equals("")) {
                mText01Kome01.setText("※");     // 名前が空白の場合、※印を表示
            } else {
                mText01Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strAge.equals("")) {
                mText01Kome02.setText("※");     // 年齢が空白の場合、※印を表示
            } else {
                mText01Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strAge.equals("")) {
                mText01Kome03.setText("※");     // 性別が空白の場合、※印を表示
            } else {
                mText01Kome03.setText("");      // 空白でない場合は※印を消す
            }


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

            Toast.makeText(MainUser.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された年齢と身長、体重は文字列からint型へ変換
            int iAge = Integer.parseInt(strAge);
            int iHeight = Integer.parseInt(strHeight);
            int iWeight = Integer.parseInt(strWeight);



            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.saveDB(strName, strAge, strSex, iHeight, iWeight
            );   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

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