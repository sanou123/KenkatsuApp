package com.example.a1521315.test02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by 1521315 on 2016/07/05.
 */
public class VideoSelect extends Activity {
    /** Called when the activity is first created. */

    Globals globals;
    private DBAdapter dbAdapter;                // DBAdapter

    /*コース名*/
    final String COURSE0 = "瀬峰";
    final String COURSE1 = "伊豆沼";
    final String COURSE2 = "出羽海道";
    final String COURSE3 = "鳴子";
    final String COURSE6 = "瀬峰ショート2";
    final String COURSE7 = "瀬峰ショート1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_serect);

        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        dbAdapter = new DBAdapter(this);

        Button btnDisp0 = (Button)findViewById(R.id.course0);
        btnDisp0.setAllCaps(false);//小文を字使用可能にする
        btnDisp0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("瀬峰コース(約10.4km)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE0;
                        ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","0");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
            }
        });
        Button btnDisp1 = (Button)findViewById(R.id.course1);
        btnDisp1.setAllCaps(false);
        btnDisp1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("伊豆沼コース(約4.4km)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE1;
                        ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","1");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
            }
        });
        Button btnDisp2 = (Button)findViewById(R.id.course2);
        btnDisp2.setAllCaps(false);
        btnDisp2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("出羽街道コース(約5.4km)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE2;
                        ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","2");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
            }
        });
        Button btnDisp3 = (Button)findViewById(R.id.course3);
        btnDisp3.setAllCaps(false);
        btnDisp3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("鳴子コース(約1.3km)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE3;
                        ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","3");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();

            }
        });
        Button btnDisp6 = (Button)findViewById(R.id.course6);
        btnDisp6.setAllCaps(false);
        btnDisp6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("瀬峰ショートコース1(約200m)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE6;
                        //ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","6");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
            }
        });
        Button btnDisp7 = (Button)findViewById(R.id.course7);
        btnDisp7.setAllCaps(false);
        btnDisp7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ポップアップメニュー表示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoSelect.this);
                alertDialog.setTitle("コースセレクト");
                alertDialog.setMessage("瀬峰ショートコース2(約1km)");
                alertDialog.setPositiveButton("始める", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.coursename = COURSE7;
                        ghost();
                        Intent intent = new Intent(getApplication(),VideoPlay.class);
                        intent.putExtra("course","7");//VideoPlayにコース番号を渡す
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog myDialog = alertDialog.create();
                myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
                myDialog.show();
            }
        });

    }

    private void ghost(){
/*
        dbAdapter.readDB();                         // DBの読み込み(読み込みの方)
        String column = "course_name";
        String column1 = "ghost_time";
        String columns2 = "name";

        String columns = globals.coursename;
        String columns1 = globals.now_user;

        // DBの検索データを取得 入力した文字列を参照して検索
        Cursor c = dbAdapter.sortDB(null, column, column1, columns2, columns, columns1);


        if (c.moveToFirst()) {
            do {
                switch(globals.coursename) {
                    case COURSE0:
                        globals.bestrecord_time0 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    case COURSE1:
                        globals.bestrecord_time1 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    case COURSE2:
                        globals.bestrecord_time2 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    case COURSE3:
                        globals.bestrecord_time3 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    case COURSE6:
                        globals.bestrecord_time6 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    case COURSE7:
                        globals.bestrecord_time7 = changeSeconds(c.getDouble(3));     // TextViewのカスタマイズ処理
                        break;
                    default:
                        break;
                }
            } while (c.moveToNext());
        } else if(c.moveToFirst()){
            do {
                switch (globals.coursename) {
                    case COURSE0:
                        globals.bestrecord_time0 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    case COURSE1:
                        globals.bestrecord_time1 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    case COURSE2:
                        globals.bestrecord_time2 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    case COURSE3:
                        globals.bestrecord_time3 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    case COURSE6:
                        globals.bestrecord_time6 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    case COURSE7:
                        globals.bestrecord_time7 = "99:99:99.9";     // TextViewのカスタマイズ処理
                        break;
                    default:
                        break;
                }
            } while (c.moveToNext()) ;
        }
        c.close();
        dbAdapter.closeDB();        // DBを閉じる
*/
    }

    /*時分秒を秒に変換*/
    private String  changeSeconds(double time){
        int hours = (int)time / 3600;
        int minutes = (int)time - (3600 * hours) / 60;
        double seconds = time - (3600 * hours) - (60 * minutes);

        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.TrainingSelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}