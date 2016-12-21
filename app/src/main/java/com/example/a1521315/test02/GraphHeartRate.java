package com.example.a1521315.test02;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class GraphHeartRate extends Activity {

    private DBAdapter dbAdapter;                // DBAdapter
    Globals globals;
    private DatePickerDialog.OnDateSetListener varDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_linechart);

        final EditText editText = (EditText)findViewById(R.id.editText01);

        varDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                globals.year = year +"年";
                globals.month = (monthOfYear + 1) + "月";
                globals.day = dayOfMonth + "日";
            }
        };


        ((Button)findViewById(R.id.button1))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog dateDialog = new DatePickerDialog(
                                GraphHeartRate.this,
                                varDateSetListener,
                                calendar.get(YEAR),
                                calendar.get(MONTH),
                                calendar.get(DAY_OF_MONTH)
                        );

                        dateDialog.show();
                    }
                });

        ((Button)findViewById(R.id.button2))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        createLineChartYear();
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("走行距離");


        toolbar.inflateMenu(R.menu.toolbar_graph_select);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.times) {
                    Toast.makeText(GraphHeartRate.this,"走行時間を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphHeartRate.this, GraphTime.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.distance) {
                    Toast.makeText(GraphHeartRate.this,"走行距離を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphHeartRate.this, GraphMile.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.heart_rate) {
                    Toast.makeText(GraphHeartRate.this,"心拍数を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphHeartRate.this, GraphHeartRate.class);
                    startActivity(intent);
                    return true;
                }

                return true;
            }
        });


    }

    private void createLineChartYear() {
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();

        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        String column = "name_id";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c = dbAdapter.searchDB(null, column, name_id);

        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                entries.add(new Entry(c.getFloat(7), Num));
                Log.d("取得したCursor:", c.getString(7));
                Num = Num + 1;
            } while (c.moveToNext());
        }

        LineDataSet dataset = new LineDataSet(entries, "心拍数");

        // ArrayListを生成
        ArrayList<String> labels = new ArrayList<String>();

        if (c.moveToFirst()) {
            do {
                labels.add(new String(c.getString(3) + c.getString(4) + c.getString(5)
                        + c.getString(6)));
                Log.d("取得したCursor:", c.getString(3) + c.getString(4) + c.getString(5)
                        + c.getString(6));
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる


        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);

    }
}
