package com.example.a1521315.test02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrainingSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.training_select);

        //buttonBackを押した時MenuSerectへ移動
        Button btnNml = (Button) findViewById(R.id.buttonNormal);
        btnNml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.VideoSelect");
                startActivity(intent);
            }
        });

        //buttonBackを押した時MenuSerectへ移動
        Button btndebug = (Button) findViewById(R.id.buttonFitness);
        btndebug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.usb_debug");
                startActivity(intent);
            }
        });

        //buttonBackを押した時MenuSerectへ移動
        Button btnBack = (Button) findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.MenuSelect");
                startActivity(intent);
            }
        });

    }
}
