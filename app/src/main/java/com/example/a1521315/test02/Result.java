package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/09/29.
 */

public class Result extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        TextView tv = (TextView)findViewById(R.id.restx);

        MovementMethod mMethod = LinkMovementMethod.getInstance();
        tv.setMovementMethod(mMethod);
        String url = "https://twitter.com/intent/tweet?hashtags=けんかつAPP &text=ぼくはけんこうです！やったー！";
        // urlをhtmlを使ってリンクテキストに埋め込む
        CharSequence link = Html.fromHtml("<a href=\"" + url + "\">結果をつぶやく</a>");
        tv.setText(link);
        /*TextView textView = (TextView)findViewById(R.id.restx);
        textView.setText("結果をつぶやく");*/
    }

}
