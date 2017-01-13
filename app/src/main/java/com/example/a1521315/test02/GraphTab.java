package com.example.a1521315.test02;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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

        // IntentでTabページ追加
        tabhost.addTab(tabhost
                .newTabSpec("走行時間")
                .setIndicator("走行時間")
                .setContent(new Intent(this, GraphTime.class)
                ));
    }
}