package com.example.a1521315.test02;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class VideoPlay extends Activity implements SurfaceHolder.Callback{
    TextView tv1;
    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    private MediaPlayer mp = null;

    double cnt = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);

        tv1 = (TextView)findViewById(R.id.TextView03);
        tv1.setText("時速:0.0km/h");

        ImageView imageView1 = (ImageView)findViewById(R.id.image_view_1);
        imageView1.setImageResource(R.drawable.bar);



    }

    public boolean onDestroy(MediaPlayer mp, int what, int extra) {
        if(mp != null){
            mp.release();
            mp = null;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        //String filePath = "/data/test/test_x264.mp4";
        String mediaPath = Environment.getExternalStorageDirectory().getPath()+"/Movies/test_x264.mp4";


        try {
            mp = new MediaPlayer();
            mp.setDataSource(mediaPath);
            mp.setDisplay(paramSurfaceHolder);
            mp.prepare();

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



        /*動画の再生速度を変えるのに必要なプログラム↓*/
        PlaybackParams params = new PlaybackParams();
        params.setSpeed((float) 0.9);//再生速度変更
        //params.setSpeed((float) cnt);//再生速度変更
        //params.setSpeed(1.f);//再生速度変更
        mp.setPlaybackParams(params);

        mp.seekTo(15000);


    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                cnt = cnt + 0.01;
                if(cnt > 5)//意味わからないほど早くされるとクラッシュする対策
                {
                    cnt = 5;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                PlaybackParams params = new PlaybackParams();
                //params.setSpeed((float) 0.9);//再生速度変更
                params.setSpeed((float)cnt);//再生速度変更
                //params.setSpeed(1.f);//再生速度変更
                mp.setPlaybackParams(params);
                mp.start();
                tv1.setText("時速："+(float)(cnt*10)+"km/h");
                return true;
            }
        }
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                cnt = cnt - 0.01;
                if(cnt < 0.01)
                {
                    cnt = 0.01;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                PlaybackParams params = new PlaybackParams();
                //params.setSpeed((float) 0.9);//再生速度変更
                params.setSpeed((float)cnt);//再生速度変更
                //params.setSpeed(1.f);//再生速度変更
                mp.setPlaybackParams(params);
                mp.start();
                tv1.setText("時速："+(float)(cnt*10)+"km/h");
                return true;
            }
            mp.start();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
                               int paramInt2, int paramInt3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        if(mp != null){
            mp.release();
            mp = null;
        }
    }
}