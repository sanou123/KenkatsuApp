package com.example.a1521315.test02;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MenuSelect extends Activity {
    private DBAdapter dbAdapter;
    private SelectSheetListView.MyBaseAdapter myBaseAdapter;
    private List<MyListItem> items;
    private ListView mListView03;
    protected MyListItem myListItem;
    // 参照するDBのカラム：ID,名前,年齢,身長,体重の全部なのでnullを指定
    private String[] columns = null;

    Globals globals;
    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //menu_selectのレイアウトを使用
        setContentView(R.layout.menu_select);

        ImageView image = (ImageView) findViewById(R.id.imageDisplay);
        image.setImageResource(R.drawable.display);
        // TextView インスタンス生成
        TextView textView = (TextView)findViewById(R.id.title_user_select);

        String columns = globals.now_user + "さん";
        textView.setText(columns);

        TextView textView1 = (TextView)findViewById(R.id.text_bmi);

        //Degree of obesity(肥満度)
        if(globals.bmi < 18.5){

            String Doo = "(低体重)\n";
            String columns1 = "身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);

        }else if(globals.bmi >= 18.5 && globals.bmi < 25){

            String Doo = new String("(普通体重)\n");
            String columns1 = "身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);

        }else if(globals.bmi >= 25 && globals.bmi < 30){

            String Doo = new String("(肥満(1度))\n");
            String columns1 = "身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);

        }else if(globals.bmi >= 30 && globals.bmi < 35){

            String Doo = new String("(肥満(2度))\n");
            String columns1 ="身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);

        }else if(globals.bmi >= 35 && globals.bmi < 40){

            String Doo = new String("(肥満(3度))\n");
            String columns1 ="身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);
        }else if(globals.bmi >= 40){

            String Doo = new String("(肥満(4度))\n");
            String columns1 ="身長"+globals.height+"cm,"+ "体重"+globals.weight + "kg\n"
                    +"BMIは" + globals.bmi + Doo
                    + "理想体重は" + globals.ideal_weight+ "kg";
            textView1.setText(columns1);
        }


        //trainingボタンを押した時TrainingSelectへ移動
        Button btnDisp0 = (Button) findViewById(R.id.training);
        btnDisp0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.TrainingSelect");
                startActivity(intent);
            }
        });

        //training_logボタンを押した時TrainingLogへ移動
        Button btnDisp1 = (Button) findViewById(R.id.training_log);
        btnDisp1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.TrainingHistorySelect");
                startActivity(intent);
            }
        });

        //updateボタンを押した時MainUserへ移動
        Button btnDisp3 = (Button) findViewById(R.id.update);
        btnDisp3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.UserUpdate");
                startActivity(intent);
            }
        });

        //dictionaryボタンを押した時Dictionaryへ移動
        ImageButton btnDisp4 = (ImageButton) findViewById(R.id.imageButtonBook);
        btnDisp4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.Dictionary");
                startActivity(intent);
            }
        });

        /////////////////////////////////
        //searchボタンを押した時UserSelectへ移動
        ImageButton btnDisp6 = (ImageButton) findViewById(R.id.imageButton);
        btnDisp6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(globals.twitter_user == null){

                    globals.twitter_user = globals.now_user;
                    Intent intent = new Intent(MenuSelect.this, twitter_logout.class);
                    startActivity(intent);
                    Toast.makeText(MenuSelect.this, "セッションが切れています", Toast.LENGTH_LONG).show();
                }else if(globals.twitter_user != null){

                    // アラートダイアログ表示
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuSelect.this);

                    builder.setTitle("ログイン確認");
                    builder.setMessage(globals.twitter_user+ "さんでログインします!\n");
                    // OKの時の処理
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            globals.twitter_user = globals.now_user;
                            dbLogin();

                            Intent intent = new Intent(MenuSelect.this, Search.class);
                            startActivity(intent);

                        }
                    });

                    builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            globals.twitter_user = globals.now_user;

                            Intent intent = new Intent(MenuSelect.this, twitter_logout.class);
                            startActivity(intent);
                        }
                    });
                    // ダイアログの表示
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            }
        });
        //////////////////////////////////////////////////////////////////////

  }

    public void dbLogin(){
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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