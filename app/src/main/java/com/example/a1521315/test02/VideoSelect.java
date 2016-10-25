package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by 1521315 on 2016/07/05.
 */
public class VideoSelect extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_serect);

        Button btnDisp0 = (Button)findViewById(R.id.course0);
        btnDisp0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","0");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });
        Button btnDisp1 = (Button)findViewById(R.id.course1);
        btnDisp1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","1");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });

        Button btnBack = (Button)findViewById(R.id.menu_serect);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.TrainingSelect");
                startActivity(intent);
            }
        });

        Button btnTest = (Button)findViewById(R.id.testtest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.MainResult");
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