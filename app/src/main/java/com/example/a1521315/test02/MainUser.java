package com.example.a1521315.test02;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.a1521315.test02.Age.getBirthCal;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class MainUser extends AppCompatActivity {

    Globals globals;

    private EditText mEditText01Name;        // 名前
    //private EditText mEditText01Age;         // 年齢
    private RadioGroup mRadioGroup01Sex;         // 性別
    private EditText mEditText01Height;         // 身長
    private EditText mEditText01Weight;          // 体重
    private EditText mEditText01Year;          // 年
    private EditText mEditText01Month;          // 月
    private EditText mEditText01Day;          // 日


    private TextView mText01Kome01;             // 名前の※印
    private TextView mText01Kome02;             // 年齢の※印
    private TextView mText01Kome03;             // 年の※印
    private TextView mText01Kome04;             // 月の※印


    private Button mButton01Regist;             // 登録ボタン


    private Intent intent;                      // インテント

    private DatePickerDialog.OnDateSetListener varDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.user_registration);



        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定

        AgeSet();

        ((Button)findViewById(R.id.date_set))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog dateDialog = new DatePickerDialog(
                                MainUser.this,
                                varDateSetListener,
                                calendar.get(YEAR),
                                calendar.get(MONTH),
                                calendar.get(DAY_OF_MONTH)
                        );

                        dateDialog.show();
                    }
                });


        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        mEditText01Name = (EditText) findViewById(R.id.editText01Name);   // 名前
        //mEditText01Age = (EditText) findViewById(R.id.editText01Age);     // 年齢
        //mEditText01Year = (EditText) findViewById(R.id.editText01Year);     // 年
        //mEditText01Month = (EditText) findViewById(R.id.editText01Month);     // 月
        //mEditText01Day = (EditText) findViewById(R.id.editText01Day);     // 日
        mRadioGroup01Sex = (RadioGroup) findViewById(R.id.radioGroup01Sex);     // 年齢
        mEditText01Height = (EditText) findViewById(R.id.editText01Height);     // 身長
        mEditText01Weight = (EditText) findViewById(R.id.editText01Weight);       // 体重

        mText01Kome01 = (TextView) findViewById(R.id.text01Kome01);             // 名前の※印
        mText01Kome02 = (TextView) findViewById(R.id.text01Kome02);             // 年齢※印
        mText01Kome03 = (TextView) findViewById(R.id.text01Kome03);             // 年齢※印
        mText01Kome04 = (TextView) findViewById(R.id.text01Kome04);             // 年齢※印


        mButton01Regist = (Button) findViewById(R.id.button01Regist);           // 登録ボタン

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Name.setText("");
        //mEditText01Age.setText("");
        //mEditText01Year.setText(globals.year);
        //mEditText01Month.setText(globals.month);
        //mEditText01Day.setText(globals.day);
        mEditText01Height.setText("");
        mEditText01Weight.setText("");

        mText01Kome01.setText("");
        mText01Kome02.setText("");
        mText01Kome03.setText("");
        mText01Kome04.setText("");



        mEditText01Name.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    private void AgeSet(){

        mEditText01Year = (EditText) findViewById(R.id.editText01Year);     // 年
        mEditText01Month = (EditText) findViewById(R.id.editText01Month);     // 月
        mEditText01Day = (EditText) findViewById(R.id.editText01Day);     // 日


        varDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(android.widget.DatePicker view , int year , int monthOfYear , int dayOfMonth){
                globals.user_year = String.valueOf(year);

                if(monthOfYear + 1 >= 1 && monthOfYear + 1 <= 9) {
                    globals.user_month = '0' + String.valueOf((monthOfYear + 1));
                }else {
                    globals.user_month = String.valueOf((monthOfYear + 1));
                }
                if(dayOfMonth >= 1 && dayOfMonth <= 9) {
                    globals.user_day = '0' + String.valueOf(dayOfMonth) ;
                }else {
                    globals.user_day = String.valueOf(dayOfMonth) ;
                }
                mEditText01Year.setText(globals.user_year);
                mEditText01Month.setText(globals.user_month);
                mEditText01Day.setText(globals.user_day);

            }
        };

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
        //String strAge = mEditText01Age.getText().toString();
        String strYear = mEditText01Year.getText().toString();
        String strMonth = mEditText01Month.getText().toString();
        String strDay = mEditText01Day.getText().toString();
        String strSex = mRadioGroup01Sex.getText().toString();
        String strHeight = mEditText01Height.getText().toString();
        String strWeight = mEditText01Weight.getText().toString();

        // EditTextが空白の場合
        if (strName.equals("") || strYear.equals("") || strHeight.equals("") || strWeight.equals("")) {

            if (strName.equals("")) {
                mText01Kome01.setText("※");     // 名前が空白の場合、※印を表示
            } else {
                mText01Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strYear.equals("")) {
                mText01Kome02.setText("※");     // 年齢が空白の場合、※印を表示
            } else {
                mText01Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strHeight.equals("")) {
                mText01Kome03.setText("※");     // 性別が空白の場合、※印を表示
            } else {
                mText01Kome03.setText("");      // 空白でない場合は※印を消す
            }


            if (strWeight.equals("")) {
                mText01Kome04.setText("※");     // 身長が空白の場合、※印を表示
            } else {
                mText01Kome04.setText("");      // 空白でない場合は※印を消す
            }


            Toast.makeText(MainUser.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された年齢と身長、体重は文字列からint型へ変換
            int iHeight = Integer.parseInt(strHeight);
            int iWeight = Integer.parseInt(strWeight);
            int iYear = Integer.parseInt(globals.user_year);
            int iMonth = Integer.parseInt(globals.user_month);
            int iDay = Integer.parseInt(globals.user_day);

            long iAge = getOld(strYear+strMonth+strDay);



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
            dbAdapter.saveDB(strName, iAge,  strSex, iHeight, iWeight, bmi.doubleValue(),
                    ideal_weight.doubleValue(), "ログインしていません", iYear, iMonth, iDay);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定


            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.SelectSheetListView");
            startActivity(intent);

        }
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
                    "com.example.a1521315.test02.SelectSheetListView");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}