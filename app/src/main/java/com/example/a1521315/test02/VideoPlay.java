package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class VideoPlay extends Activity implements SurfaceHolder.Callback, View.OnClickListener, MediaPlayer.OnCompletionListener {
    Handler handler = new Handler();
    TextView Speed;//時速
    TextView Mileage;//走行距離
    TextView Heartbeat;//心拍
    TextView Test;//aaaaaaaa
    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    private MediaPlayer mp = null;
    private ScheduledExecutorService srv;
    PlaybackParams params = new PlaybackParams();
    double cnt = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.addCallback(this);

        Speed = (TextView)findViewById(R.id.textSpeed);
        Mileage = (TextView)findViewById(R.id.textMileage);
        Heartbeat = (TextView)findViewById(R.id.textHeartbeat);
        Test = (TextView) findViewById(R.id.textTest);
        ImageView imageView1 = (ImageView)findViewById(R.id.image_view_1);
        imageView1.setImageResource(R.drawable.bar);
        ImageView imageView2 = (ImageView)findViewById(R.id.image_view_2);
        imageView2.setImageResource(R.drawable.me);

        findViewById(R.id.buttonPlay).setOnClickListener(this);
        findViewById(R.id.buttonResult).setOnClickListener(this);
    }



    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        //String mediaPath = "/data/TestFolder/test_x264.mp4";//エミュレータ6P用
        //String mediaPath = "/data/TestFolder/TEST01.mp4";//エミュレータ9用
        //String mediaPath = "/sdcard/test_x264.mp4";//実機9用
        //String mediaPath = "/storage/emulated/0/test_x264.mp4";//実機9用
        //String mediaPath = "/sdcard/DCIM/Camera/VID_20160929_170002.3gp";//実機9用
        String mediaPath = "android.resource://" + getPackageName() + "/" + R.raw.test01;

        try {
            //MediaPlayerを生成
            mp = new MediaPlayer();

            //動画ファイルをMediaPlayerに読み込ませる
            mp.setDataSource(getApplicationContext(),Uri.parse(mediaPath));

            //読み込んだ動画ファイルを画面に表示する
            mp.setDisplay(holder);
            mp.prepare();
            mp.setOnCompletionListener(this);

        } catch (IllegalArgumentException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        srv = Executors.newSingleThreadScheduledExecutor();
        srv.scheduleAtFixedRate(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Test.setText("総再生時間:" + mp.getDuration() + " 再生時間:" + mp.getCurrentPosition());
                        //Mileage.setText("走行距離:" + String.format("f", (float) 83.7 / (mp.getDuration() / mp.getCurrentPosition()) ) + "km");
                    }
                });
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
    @Override
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {


    }
    @Override
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        if(mp != null){
            mp.release();
            mp = null;
        }
    }



    //再生時間表示に関するやつ↓
    public void onClick(View v){
        int id = v.getId();
        switch(id) {
            case R.id.buttonPlay://Playボタン押したとき
                Button BtnView = (Button) findViewById(R.id.buttonPlay);
                cnt = 0;
                Speed.setText("時速:"+(float)(cnt*10)+"km/h");
                mp.setPlaybackParams(params);
                mp.seekTo(0);
                BtnView.setVisibility(View.INVISIBLE);//PLAYボタンを押したらPLAYボタンを消す
                break;
            case R.id.buttonResult://Resultボタン押したとき
                //インテントの作成
                Intent intent = new Intent(getApplication(), Result.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                cnt = cnt + 0.1;
                if(cnt > 5)//意味わからないほど早くされるとクラッシュする対策
                {
                    cnt = 5;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float)cnt);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                Speed.setText("時速:"+(float)(cnt*10)+"km/h");
                return true;
            }
        }
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                cnt = cnt - 0.1;
                if(cnt <= 0.1)
                {
                    cnt = 0.00;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float)cnt);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                Speed.setText("時速:"+(float)(cnt*10)+"km/h");
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    // 再生完了時の処理
    @Override
    public void onCompletion(MediaPlayer agr0) {
        Log.v("MediaPlayer", "onCompletion");
        srv.shutdown();//サービス終了させる
        //ボタン表示↓
        Button BtnView2 = (Button) findViewById(R.id.buttonResult);
        BtnView2.setVisibility(View.VISIBLE);
        if(mp != null){
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp != null){
            mp.release();
            mp = null;
        }
    }
}
