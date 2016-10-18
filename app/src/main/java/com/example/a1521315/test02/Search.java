package com.example.a1521315.test02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/09/29.
 */

public class Search extends Activity {

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search);

            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText("ツイッター検索準備中");

            Intent intent = new Intent( this, MenuSelect.class );//kaede_jishoブランチでSelectに変更したらエラー起きたから戻した
            startActivity( intent );

            Uri uri = Uri.parse("https://twitter.com/search?q=＃けんかつAPP");
            Intent i = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(i);

        }

}
