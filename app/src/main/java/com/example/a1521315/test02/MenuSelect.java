package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;



public class MenuSelect extends Activity {
    protected MyListItem myListItem;

    /**
     * Called when the activity is first created.
     */



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                //menu_serectのレイアウトを使用
        setContentView(R.layout.menu_select);

        //trainingボタンを押した時TrainingSerectへ移動
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
        Button btnDisp2 = (Button) findViewById(R.id.update);
        btnDisp2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.UserUpdate");
                startActivity(intent);
            }
        });

        //dictionaryボタンを押した時Dictionaryへ移動
        Button btnDisp3 = (Button) findViewById(R.id.dictionary);
        btnDisp3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.Dictionary");
                startActivity(intent);
            }
        });

        //user_serectボタンを押した時UserSerectへ移動
        Button btnDisp4 = (Button) findViewById(R.id.user_serect);
        btnDisp4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);
            }
        });
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