package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    MediaPlayer mediaPlayer = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //activity_mainのレイアウトを使用
        setContentView(R.layout.activity_main);
        ImageView title = (ImageView)findViewById(R.id.imageView);
        title.setImageResource(R.drawable.kenkatsuapp_title);
        //playBGM();
    }
    //画面タップでユーザ選択に遷移
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Top", "ACTION_DOWN");
                //stopBGM();
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetListView");
                startActivity(intent);
                break;
        }
        return true;
    }

    //BGM再生
    private void playBGM() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mattarikibun);
            mediaPlayer.start();
        }
    }

    //BGM停止
    private void stopBGM(){
        Log.d("stopBGM", "STOP");
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}