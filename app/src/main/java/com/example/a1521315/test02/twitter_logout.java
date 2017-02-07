package com.example.a1521315.test02;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/09/29.
 */

public class twitter_logout extends AppCompatActivity {

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

       /*TextView tv = (TextView)findVwById(R.id.tv);
        tv.setText("ツイッターログアウト");*/

        WebView myWebView = (WebView) findViewById(R.id.web1);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://twitter.com/logout?lang=jp");

            /*TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText("ツイッター検索準備中");

            Intent intent = new Intent( this, MenuSelect.class );//kaede_jishoブランチでSelectに変更したらエラー起きたから戻した
            startActivity( intent );

            Uri uri = Uri.parse("https://twitter.com/search?q=＃けんかつAPP");
            Intent i = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(i);*/

    }

}
