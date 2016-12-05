package com.example.a1521315.test02;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/11/30.
 */

public class Resulttweet extends Activity{
    Globals globals;
    String coursename;
    String mileage;
    String maxheartbeat;
    String avg;
    String max;
    String time;
    double cal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        globals = (Globals)this.getApplication();
        coursename = globals.coursename;
        mileage = globals.mileage;
        maxheartbeat = globals.maxheartbeat;
        avg = globals.avg;
        max = globals.max;
        time = globals.time;
        cal = globals.cal;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        TextView tv = (TextView)findViewById(R.id.tv);
        tv.setText("トレーニング結果のツイート");

        WebView myWebView = (WebView) findViewById(R.id.web1);
        myWebView.setWebViewClient(new WebViewClient());
        //myWebView.loadUrl("http://www.google.com");
        myWebView.loadUrl("https://twitter.com/intent/tweet?hashtags=けんかつAPP &text="+"コース名："+coursename+"%0a"+"走行距離："+mileage+"%0a"+"最大心拍："+maxheartbeat+"%0a"+"平均速度："+avg+"%0a"+"最高速度："+max+"%0a"+"運動時間："+time+"%0a"+"消費カロリー："+cal+"%0a");
    }
}
