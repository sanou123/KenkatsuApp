package com.example.a1521315.test02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MenuSelect extends AppCompatActivity {
    Globals globals;
    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //menu_selectのレイアウトを使用
        setContentView(R.layout.menu_select);

        // TextView インスタンス生成
        TextView textView = (TextView)findViewById(R.id.title_user_select);

        String columns = globals.now_user + "さんのメニュー";
        textView.setText(columns);

        TextView textView1 = (TextView)findViewById(R.id.text_bmi);

        //Degree of obesity(肥満度)
        if(globals.bmi < 18.5){
            String Doo = "      低体重です";
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
            textView1.setText(columns1);
        }else if(globals.bmi >= 18.5 || globals.bmi < 25){
            String Doo = new String("普通体重です");
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
            textView1.setText(columns1);
        }else if(globals.bmi >= 25 || globals.bmi < 30){
            String Doo = new String("肥満(1度)です");
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
            textView1.setText(columns1);
        }else if(globals.bmi >= 30 || globals.bmi < 35){
            String Doo = new String("肥満(2度)です");
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
            textView1.setText(columns1);
        }else if(globals.bmi >= 35 || globals.bmi < 40){
            String Doo = new String("肥満(3度)です");
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
            textView1.setText(columns1);
        }else if(globals.bmi >= 40){
            String Doo = new String("肥満(4度)です");
            String columns1 = globals.now_user + "さんのBMIは" + globals.bmi + Doo;
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
                        "com.example.a1521315.test02.SelectSheetTable1");
                startActivity(intent);
            }
        });

        //updateボタンを押した時MainUserへ移動
        Button btnDisp2 = (Button) findViewById(R.id.graph);
        btnDisp2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.Graph");
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
        Button btnDisp4 = (Button) findViewById(R.id.dictionary);
        btnDisp4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.Dictionary");
                startActivity(intent);
            }
        });

        //user_selectボタンを押した時UserSelectへ移動
        Button btnDisp5 = (Button) findViewById(R.id.user_serect);
        btnDisp5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);
            }
        });

        //searchボタンを押した時UserSelectへ移動
        Button btnDisp6 = (Button) findViewById(R.id.search);
        btnDisp6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.Search");
                startActivity(intent);
            }
        });

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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}