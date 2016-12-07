package com.example.a1521315.test02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by 1521315 on 2016/07/05.
 */
public class VideoSelect extends AppCompatActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_serect);

        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Button btnDisp0 = (Button)findViewById(R.id.course0);
        btnDisp0.setAllCaps(false);//小文を字使用可能にする
        btnDisp0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","0");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });
        Button btnDisp1 = (Button)findViewById(R.id.course1);
        btnDisp1.setAllCaps(false);
        btnDisp1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","1");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });
        Button btnDisp2 = (Button)findViewById(R.id.course2);
        btnDisp2.setAllCaps(false);
        btnDisp2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","2");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });
        Button btnDisp3 = (Button)findViewById(R.id.course3);
        btnDisp3.setAllCaps(false);
        btnDisp3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","3");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });

        Button btnBack = (Button)findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.TrainingSelect");
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