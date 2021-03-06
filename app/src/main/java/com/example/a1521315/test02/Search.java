package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/09/29.
 */

public class Search extends Activity {

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // タイトルバーを隠す
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // ステータスバーを隠す
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //横画面に固定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.search);

            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText("ツイート検索結果");

            WebView myWebView = (WebView) findViewById(R.id.web1);
            myWebView.setWebViewClient(new WebViewClient());
            myWebView.loadUrl("https://twitter.com/search?q=＃けんかつAPP");

            /*TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText("ツイッター検索準備中");

            Intent intent = new Intent( this, MenuSelect.class );//kaede_jishoブランチでSelectに変更したらエラー起きたから戻した
            startActivity( intent );

            Uri uri = Uri.parse("https://twitter.com/search?q=＃けんかつAPP");
            Intent i = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(i);*/

        }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.MenuSelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
