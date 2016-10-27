package com.example.a1521315.test02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class VideoPlay extends Activity implements SurfaceHolder.Callback, View.OnClickListener, MediaPlayer.OnCompletionListener {
    TextView tSpeed;//時速の変数
    TextView tMileage;//走行距離の変数
    TextView tHeartbeat;//心拍の変数
    TextView tTest;//再生時間の変数
    TextView tTimer;//タイマーの変数
    TextView tCourse;//コース番号

    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    private MediaPlayer mp = null;

    private ScheduledExecutorService getplaytimescheduler;
    Runnable mygetplaytimetask = new MyGetPlayTimeTask();
    ScheduledFuture getplaytimefuture;

    private ScheduledExecutorService timerscheduler;
    Runnable mytimertask = new MyTimerTask();
    ScheduledFuture future;

    PlaybackParams params = new PlaybackParams();
    double speedcount = 0.0;


    String mediaPath = null;//動画データ
    double TotalMileage=0;//総走行距離
    int raw = 0;//rawファイルかどうかを判断する変数。0=内部ストレージ　1=rawファイル




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        getWindow().setFormat(PixelFormat.TRANSPARENT);

        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.addCallback(this);

        tSpeed = (TextView)findViewById(R.id.textSpeed);
        tSpeed.setText("0.0");
        tMileage = (TextView)findViewById(R.id.textMileage);
        tMileage.setText("0.0");
        tHeartbeat = (TextView)findViewById(R.id.textHeartbeat);
        tHeartbeat.setText("0");
        tTimer = (TextView)findViewById(R.id.textTimer);
        tTimer.setText("00:00.0");
        tTest = (TextView) findViewById(R.id.textTest);
        tTest.setText("");
        ImageView imageView1 = (ImageView)findViewById(R.id.image_view_bar);
        imageView1.setImageResource(R.drawable.bar);
        ImageView imageView2 = (ImageView)findViewById(R.id.image_view_me);
        imageView2.setImageResource(R.drawable.me);

        findViewById(R.id.buttonPlay).setOnClickListener(this);
        findViewById(R.id.buttonResult).setOnClickListener(this);
        findViewById(R.id.buttonPause).setOnClickListener(this);

        //コース番号受け取り
        Intent i = getIntent();
        String CourseNum = i.getStringExtra("course");
        tCourse = (TextView)findViewById(R.id.textCourse);
        tCourse.setText("コース"+CourseNum);

        if(CourseNum.equals("0")) {
            mediaPath = "/test02.mp4";//実機9のストレージにあるファルを指定
            TotalMileage = 10.4;
            raw = 0;
        }else if(CourseNum.equals("1")) {
            mediaPath = "/test_x264.mp4";//実機9のストレージにあるファイルを指定
            TotalMileage = 83.7;
            raw = 0;
        }else if(CourseNum.equals("2")) {
            mediaPath = "/_naruko.mp4";//実機9のストレージにあるファイルを指定
            TotalMileage = 1.3;
            raw = 0;
        }else if(CourseNum.equals("3")) {
            mediaPath = "android.resource://" + getPackageName() + "/" + R.raw.test01;//rawフォルダから指定する場合
            TotalMileage = 20.0;
            raw = 1;
        }


    }
    // 再生完了時の処理
    @Override
    public void onCompletion(MediaPlayer agr0) {
        Log.v("MediaPlayer", "onCompletion");
        getplaytimescheduler.shutdown();//サービス終了させる
        timerscheduler.shutdown();//タイマー止める
        //リザルトボタンを表示
        Button BtnResultView = (Button) findViewById(R.id.buttonResult);
        BtnResultView.setVisibility(View.VISIBLE);
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

    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        try {
            //MediaPlayerを生成
            mp = new MediaPlayer();

            if(raw==1){
                //動画ファイルをMediaPlayerに読み込ませる
                mp.setDataSource(getApplicationContext(), Uri.parse(mediaPath));//rawフォルダから指定する場合
            }else{
                File pathExternalPublicDir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                String dir = pathExternalPublicDir.getPath();//dirは　/storage/emulated/0/Movie　を指定してい
                //動画ファイルをMediaPlayerに読み込ませる
                mp.setDataSource(dir + mediaPath);//内部ストレージから指定する場合
            }

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
        getplaytimescheduler = Executors.newSingleThreadScheduledExecutor();
        getplaytimefuture = getplaytimescheduler.scheduleAtFixedRate(mygetplaytimetask, 0, 1000, TimeUnit.MILLISECONDS);
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



    //ボタンを押された時の処理
    public void onClick(View v){
        int id = v.getId();
        switch(id) {
            case R.id.buttonPlay://Playボタン押したとき
                Button BtnPlayView = (Button) findViewById(R.id.buttonPlay);
                Button BtnPauseView = (Button) findViewById(R.id.buttonPause);
                BtnPlayView.setVisibility(View.INVISIBLE);//PLAYボタンを押したらPLAYボタンを消す
                BtnPauseView.setVisibility(View.VISIBLE);//PLAYボタンを押したらPAUSEボタンを出す
                speedcount = 0.0;
                tSpeed.setText(String.format("%.1f",(float)(speedcount*10)));
                mp.setPlaybackParams(params);
                mp.seekTo(0);

                timerscheduler = Executors.newSingleThreadScheduledExecutor();
                future = timerscheduler.scheduleAtFixedRate(mytimertask, 0, 100, TimeUnit.MILLISECONDS);

                break;

            case R.id.buttonResult://Resultボタン押したとき
                Intent intent = new Intent(getApplication(), Result.class);
                startActivity(intent);
                break;

            case R.id.buttonPause://Pauseボタンを押したとき
                future.cancel(true);//タイマー一時停止
                getplaytimefuture.cancel(true);//タイマー一時停止
                speedcount = 0;
                params.setSpeed((float)speedcount);
                mp.setPlaybackParams(params);
                tSpeed.setText(String.format("%.1f", (float)(speedcount*10)));
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
                alertDialog.setTitle("aaaaa");
                alertDialog.setMessage("一時停止中です");
                alertDialog.setPositiveButton("走行をやめてコース選択に戻る", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //VideoSelectに戻る処理
                        getplaytimescheduler.shutdown();//サービス終了させる
                        timerscheduler.shutdown();//タイマー終了
                        if(mp != null){
                            mp.release();
                            mp = null;
                        }
                        Intent intent = new Intent(getApplication(),VideoSelect.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("走行に戻る", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        future = timerscheduler.scheduleAtFixedRate(mytimertask, 0, 100, TimeUnit.MILLISECONDS);//タイマーを動かす
                        getplaytimefuture = getplaytimescheduler.scheduleAtFixedRate(mygetplaytimetask, 0, 100, TimeUnit.MILLISECONDS);
                    }
                });
                AlertDialog myDialog =alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                speedcount = speedcount + 0.1;
                if(speedcount > 5)//意味わからないほど早くされるとクラッシュする対策
                {
                    speedcount = 5;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float)speedcount);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                //tSpeed.setText(""+(float)(cnt*10));
                tSpeed.setText(String.format("%.1f", (float)(speedcount*10)));
                return true;
            }
        }
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                speedcount = speedcount - 0.1;
                if(speedcount <= 0.1)
                {
                    speedcount = 0.0;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float)speedcount);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                tSpeed.setText(String.format("%.1f", (float)(speedcount*10)));
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //カウントアップタイマタスク
    public class MyTimerTask implements Runnable{
        private Handler timerhandler = new Handler();
        private long timercount = 0;
        public void run(){
            // handlerを使って処理をキューイングする
            timerhandler.post(new Runnable() {
                public void run() {
                    timercount++;
                    long mm = timercount*100 / 1000 / 60;
                    long ss = timercount*100 / 1000 % 60;
                    long ms = (timercount*100 - ss * 1000 - mm * 1000 * 60)/100;
                    // 桁数を合わせるために02d(2桁)を設定
                    tTimer.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                }
            });
        }
    }
    //再生時間取得タスク
    public class MyGetPlayTimeTask implements Runnable{
        private Handler getplaytimehandler = new Handler();
        public void run(){
            getplaytimehandler.post(new Runnable() {
                @Override
                public void run() {
                    double f1=0,f2=0,f3=0;
                    f1 = mp.getDuration();
                    f2 = mp.getCurrentPosition();
                    f3 = TotalMileage / ( f1 / f2);
                    tTest.setText("総再生時間:" + mp.getDuration() + " 再生時間:" + mp.getCurrentPosition());tMileage.setText(String.format("%.2f",f3));

                }
            });
        }
    }
}
