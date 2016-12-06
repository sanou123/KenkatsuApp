package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //activity_mainのレイアウトを使用
        setContentView(R.layout.activity_main);
        playBGM();
    }
    //BGM再生
    private void playBGM() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mattarikibun);
        mediaPlayer.start();
    }

    //画面タップでユーザ選択に遷移
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("", "ACTION_DOWN");
                mediaPlayer.stop();
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);
                break;
        }
        return true;
    }
}