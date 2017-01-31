package com.example.a1521315.test02;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by 1521315 on 2016/12/16.
 */

public class TrainingHistorySelect extends FragmentActivity
        implements DatePickerDialog.OnDateSetListener {

    Globals globals;

    private DatePickerDialog.OnDateSetListener varDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.training_history_select);

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


/*
        DateSet();

        ((Button)findViewById(R.id.buttonGraph))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog dateDialog = new DatePickerDialog(
                                TrainingHistorySelect.this,
                                varDateSetListener,
                                calendar.get(calendar.YEAR),
                                calendar.get(calendar.MONTH),
                                calendar.get(calendar.DAY_OF_MONTH)
                        );



                        dateDialog.setButton(
                                DialogInterface.BUTTON_POSITIVE,
                                "検索",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Sub 画面を起動
                                        Intent intent = new Intent();
                                        intent.setClassName("com.example.a1521315.test02",
                                                "com.example.a1521315.test02.GraphSearch");
                                        startActivity(intent);
                                    }
                                }
                        );
/*
                        dateDialog.setButton(
                                DialogInterface.BUTTON_POSITIVE,
                                "月で比較",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Sub 画面を起動
                                        Intent intent = new Intent();
                                        intent.setClassName("com.example.a1521315.test02",
                                                "com.example.a1521315.test02.GraphTab");
                                        startActivity(intent);
                                    }
                                }
                        );
                        dateDialog.setButton(
                                DialogInterface.BUTTON_POSITIVE,
                                "年で比較",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Sub 画面を起動
                                        Intent intent = new Intent();
                                        intent.setClassName("com.example.a1521315.test02",
                                                "com.example.a1521315.test02.GraphTab");
                                        startActivity(intent);
                                    }
                                }
                        );

                        dateDialog.setButton(
                                DialogInterface.BUTTON_NEGATIVE,
                                "キャンセル",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        );
                        dateDialog.show();
                    }
                });*/
    }

    @Override
    public void onDateSet(DatePicker view,
                          int year, int monthOfYear, int dayOfMonth) {

        globals.year = String.valueOf(year) + "年";

        if(monthOfYear + 1 >= 1 && monthOfYear + 1 <= 9) {
            globals.month = '0' + String.valueOf(monthOfYear + 1) + "月";
        }else {
            globals.month = String.valueOf(monthOfYear + 1) + "月";
        }
        if(dayOfMonth >= 1 && dayOfMonth <= 9) {
            globals.day = '0' + String.valueOf(dayOfMonth) + "日";
        }else {
            globals.day = String.valueOf(dayOfMonth) + "日" ;
        }
        Intent intent = new Intent();
        intent.setClassName("com.example.a1521315.test02",
                "com.example.a1521315.test02.GraphTab");
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");

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
