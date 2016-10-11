package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class VideoPlay extends AppCompatActivity implements SurfaceHolder.Callback,MediaPlayer.OnCompletionListener {
    double cnt = 0.0;
    TextView Speed;
    TextView Mileage;
    MediaPlayer player = null;
    SurfaceHolder sh;
    PlaybackParams params = new PlaybackParams();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        Speed = (TextView)findViewById(R.id.textSpeed);
        Speed.setText(0.00+"km/h");
        Mileage = (TextView)findViewById(R.id.textMileage);
        Mileage.setText("走行距離"+0.00+"km");

        final Button playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*動画の再生速度を変えるのに必要なプログラム↓*/
                //PlaybackParams params = new PlaybackParams();
                //params.setSpeed((float)0.0);//再生速度変更
                player.setPlaybackParams(params);
                player.seekTo(1500);
                //player.start();
                playBtn.setVisibility(View.INVISIBLE);//PLAYボタンを押したらPLAYボタンを消す

            }
        });
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = cnt + 0.01;
                if(cnt > 5){//意味わからないほど早くされるとクラッシュする対策
                    cnt = 5;
                }
                 /*動画の再生速度を変えるのに必要なプログラム↓*/
                //PlaybackParams params = new PlaybackParams();
                params.setSpeed((float)cnt);//再生速度変更
                player.setPlaybackParams(params);
                //player.start();
                Speed.setText((float)(cnt*10)+"km/h");
            }
        });
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = cnt - 0.01;
                if(cnt < 0.01) {
                    cnt = 0.01;
                }
                 /*動画の再生速度を変えるのに必要なプログラム↓*/
                //PlaybackParams params = new PlaybackParams();
                params.setSpeed((float)cnt);//再生速度変更
                player.setPlaybackParams(params);
                //player.start();
                Speed.setText((float)(cnt*10)+"km/h");
            }
        });
        SurfaceView view = (SurfaceView) findViewById(R.id.videoSurfaceView);
        sh = view.getHolder();
        sh.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        player = new MediaPlayer();
        String movieUri = "android.resource://" + getPackageName() + "/" + R.raw.test01;
        try {
            player.setDataSource(getApplicationContext(), Uri.parse(movieUri));
            player.setDisplay(sh);
            player.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(player != null){
            player.release();
            player = null;

        }
    }
    @Override
    public void onCompletion(MediaPlayer player) {
        //インテントの作成
        //Intent intent = new Intent(getApplication(),ResultActivity.class);//画面遷移
        //startActivity(intent);
    }

}

