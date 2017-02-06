package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 1521315 on 2016/07/05.
 */
public class VideoSelect extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        Button btnDisp6 = (Button)findViewById(R.id.course6);
        btnDisp6.setAllCaps(false);
        btnDisp6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","6");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });
        Button btnDisp7 = (Button)findViewById(R.id.course7);
        btnDisp7.setAllCaps(false);
        btnDisp7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),VideoPlay.class);
                intent.putExtra("course","7");//VideoPlayにコース番号を渡す
                startActivity(intent);
            }
        });

    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.TrainingSelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}