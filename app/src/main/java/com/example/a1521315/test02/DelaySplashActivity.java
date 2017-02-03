package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 1521315 on 2017/01/27.
 */

public class DelaySplashActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // タイトルバーを隠す
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // ステータスバーを隠す
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //横画面に固定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // splash.xmlをViewに指定します。
            setContentView(R.layout.splash);
            Handler hdl = new Handler();
            // 500ms遅延させてsplashHandlerを実行します。
            hdl.postDelayed(new splashHandler(), 1500);
        }
        class splashHandler implements Runnable {
            public void run() {
                // スプラッシュ完了後に実行するActivityを指定します。
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                // SplashActivityを終了させます。
                DelaySplashActivity.this.finish();
            }
        }

    /**
     * バックキー無効。
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}