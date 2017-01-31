package com.example.a1521315.test02;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;


/**
 * Created by 1521315 on 2017/01/11.
 */

public class GraphTab extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_tab);

        TabHost tabhost = getTabHost();

        // IntentでTabページ追加
        tabhost.addTab(tabhost
                .newTabSpec("走行距離")
                .setIndicator("走行距離")
                .setContent(new Intent(this, GraphMile.class)
                ));

        // IntentでTabページ追加
        tabhost.addTab(tabhost
                .newTabSpec("心拍数")
                .setIndicator("心拍数")
                .setContent(new Intent(this, GraphHeartRate.class)
                ));


    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.TrainingHistorySelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}