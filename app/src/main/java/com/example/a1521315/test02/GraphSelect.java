package com.example.a1521315.test02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GraphSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.graph_select);

/*        // TextView インスタンス生成
        TextView textView = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        String listName = intent.getStringExtra("SELECTED_DATA");//設定したSELECTED_DATAで取り出す
        String columns = listName + "さんのメニュー";
        textView.setText(columns);
        */

        //buttonBackを押した時MenuSelectへ移動
        Button btnNml = (Button) findViewById(R.id.graph_time);
        btnNml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.GraphTime");
                startActivity(intent);
            }
        });

        //buttonBackを押した時MenuSelectへ移動
        Button btnBack = (Button) findViewById(R.id.graph_mile);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.GraphMile");
                startActivity(intent);
            }
        });

    }
}
