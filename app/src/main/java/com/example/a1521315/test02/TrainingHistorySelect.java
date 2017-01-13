package com.example.a1521315.test02;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by 1521315 on 2016/12/16.
 */

public class TrainingHistorySelect extends AppCompatActivity {

    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.training_select);

/*        // TextView インスタンス生成
        TextView textView = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        String listName = intent.getStringExtra("SELECTED_DATA");//設定したSELECTED_DATAで取り出す
        String columns = listName + "さんのメニュー";
        textView.setText(columns);
        */

        //buttonBackを押した時MenuSelectへ移動
        Button btnNml = (Button) findViewById(R.id.buttonNormal);
        btnNml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetTableNormal");
                startActivity(intent);
            }
        });

        //buttonFitnessを押したときの挙動
        Button btnFit = (Button) findViewById(R.id.buttonFitness);
        btnFit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetTableTimeAttack");
                startActivity(intent);
            }
        });

        //buttonEnduranceを押したときの挙動
        Button btnEndR = (Button) findViewById(R.id.buttonEndurance);
        btnEndR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sub 画面を起動
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.SelectSheetTableEndlessrun");
                startActivity(intent);
            }
        });

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
