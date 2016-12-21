package com.example.a1521315.test02;

/**
 * Created by 1521315 on 2016/11/17.
 */


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class GraphTime extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DBAdapter dbAdapter;                // DBAdapter
    private ArrayList<String> items;            // ArrayList
    Globals globals;
    private DatePickerDialog.OnDateSetListener varDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_barchart);

        // ArrayListを生成
        items = new ArrayList<>();

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
                                GraphTime.this,
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
                        createBarChartYear();
                    }
                });
        ((Button)findViewById(R.id.button3))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        createBarChartMonth();
                    }
                });
        ((Button)findViewById(R.id.button4))
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        createBarChartDay();
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
                    Toast.makeText(GraphTime.this,"走行時間を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphTime.this, GraphTime.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.distance) {
                    Toast.makeText(GraphTime.this,"走行距離を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphTime.this, GraphMile.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.heart_rate) {
                    Toast.makeText(GraphTime.this,"心拍数を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphTime.this, GraphHeartRate.class);
                    startActivity(intent);
                    return true;
                }

                return true;
            }
        });


    }

    private void createBarChartYear() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);
        barChart.setDescription("");

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setHighlightEnabled(true);
        barChart.setDrawHighlightArrow(true);
        barChart.setHighlightEnabled(true);

        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        barChart.setData(createBarChartDataYear());

        barChart.invalidate();
        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    // BarChartの設定
    private BarData createBarChartDataYear() {
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        // X軸
        /*ArrayList<String> xValues = new ArrayList<>();
        xValues.add(globals.date);*/

        //////////////////////////////////////////////////////////////////////////////
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        //string.xmlのyear_planets_arrayを取得
        int[] year_label = getResources().getIntArray(R.array.year_planets_array);
        //TypedArray year_label = getResources().obtainTypedArray(R.array.year_planets_array);

        // ArrayListを生成
        //items = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        String column_name_id = "name_id";          //検索対象のカラム名
        String column_year = "year";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字
        globals.year = "2016";
        String[] year = {globals.year};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c_name_id = dbAdapter.searchDB(null, column_name_id, name_id);
        Cursor c_year = dbAdapter.searchDB(null, column_year, year);

        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c_name_id.moveToFirst() && c_year.moveToFirst()) {
            do {
                String crlf = System.getProperty("line.separator");
                xValues.add(c_name_id.getString(3)+c_name_id.getString(4)+c_name_id.getString(5)+ crlf
                        +c_name_id.getString(6));
                Log.d("取得したCursor:", c_name_id.getString(3)+c_name_id.getString(4)+c_name_id.getString(5)+ crlf
                        +c_name_id.getString(6));
            } while (c_name_id.moveToNext() && c_year.moveToNext());
        }

        //////////////////////////////////////////////////////////////////////////////


        // valueA
        /*ArrayList<BarEntry> valuesA = new ArrayList<>();
        valuesA.add(new BarEntry(100, 0));*/

        //////////////////////////////////////////////////////////////////////////////


        // ArrayListを生成
        ArrayList<BarEntry> valuesA = new ArrayList<>();

        // DBのデータを取得
        /*String[] yColumns = {DBAdapter.COL_DISTANCE};     // DBのカラム：品名
        Cursor yc = dbAdapter.getDB1(yColumns);

        String[] yColumns_Num = {DBAdapter.COL_ID_DATA};     // DBのカラム：品名
        Cursor yc_num = dbAdapter.getDB1(yColumns_Num);*/

        if (c_name_id.moveToFirst() && c_year.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c_name_id.getString(12)), Num));
                Log.d("取得したCursor:", c_name_id.getString(12));
                Num = Num +1;
            } while (c_name_id.moveToNext() && c_year.moveToNext());
        }
        c_name_id.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行時間");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        barDataSets.add(valuesADataSet);



        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }

    private void createBarChartMonth() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);
        barChart.setDescription("");

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setHighlightEnabled(true);
        barChart.setDrawHighlightArrow(true);
        barChart.setHighlightEnabled(true);

        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        barChart.setData(createBarChartDataMonth());

        barChart.invalidate();
        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    // BarChartの設定
    private BarData createBarChartDataMonth() {
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        // X軸
        /*ArrayList<String> xValues = new ArrayList<>();
        xValues.add(globals.date);*/

        //////////////////////////////////////////////////////////////////////////////
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        //items = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        String column_name_id = "name_id";          //検索対象のカラム名
        String column_year = "year";          //検索対象のカラム名
        String column_month = "month";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字
        globals.year = "2016";
        String[] year = {globals.year};            //検索対象の文字
        globals.month = "12";
        String[] month = {globals.month};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c_name_id = dbAdapter.searchDB(null, column_name_id, name_id);
        Cursor c_year = dbAdapter.searchDB(null, column_year, year);
        Cursor c_month = dbAdapter.searchDB(null, column_month, month);


        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c_name_id.moveToFirst() && c_year.moveToFirst() && c_month.moveToFirst()) {
            do {
                xValues.add(c_name_id.getString(3)+c_name_id.getString(4)+c_name_id.getString(5)+
                        '\n' +c_name_id.getString(6));
                Log.d("取得したCursor:", c_name_id.getString(3)+c_name_id.getString(4)+
                        c_name_id.getString(5)+'\n' +c_name_id.getString(6));
            } while (c_name_id.moveToNext() && c_year.moveToNext() && c_month.moveToNext());
        }

        //////////////////////////////////////////////////////////////////////////////


        // valueA
        /*ArrayList<BarEntry> valuesA = new ArrayList<>();
        valuesA.add(new BarEntry(100, 0));*/

        //////////////////////////////////////////////////////////////////////////////


        // ArrayListを生成
        ArrayList<BarEntry> valuesA = new ArrayList<>();

        // DBのデータを取得
        /*String[] yColumns = {DBAdapter.COL_DISTANCE};     // DBのカラム：品名
        Cursor yc = dbAdapter.getDB1(yColumns);

        String[] yColumns_Num = {DBAdapter.COL_ID_DATA};     // DBのカラム：品名
        Cursor yc_num = dbAdapter.getDB1(yColumns_Num);*/

        if (c_name_id.moveToFirst() && c_year.moveToFirst() && c_month.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c_name_id.getString(12)), Num));
                Log.d("取得したCursor:", c_name_id.getString(12));
                Num = Num +1;
            } while (c_name_id.moveToNext() && c_year.moveToNext() && c_month.moveToNext());
        }
        c_name_id.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行時間");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        barDataSets.add(valuesADataSet);



        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }

    private void createBarChartDay() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);
        barChart.setDescription("");

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setHighlightEnabled(true);
        barChart.setDrawHighlightArrow(true);
        barChart.setHighlightEnabled(true);

        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        barChart.setData(createBarChartDataDay());

        barChart.invalidate();
        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    // BarChartの設定
    private BarData createBarChartDataDay() {
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        // X軸
        /*ArrayList<String> xValues = new ArrayList<>();
        xValues.add(globals.date);*/

        //////////////////////////////////////////////////////////////////////////////
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        //items = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        String column_name_id = "name_id";          //検索対象のカラム名
        String column_year = "year";          //検索対象のカラム名
        String column_month = "month";          //検索対象のカラム名
        String column_day = "day";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字
        String[] year = {globals.year};            //検索対象の文字
        String[] month = {globals.month};            //検索対象の文字
        String[] day = {globals.day};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c_name_id = dbAdapter.searchDB(null, column_name_id, name_id);
        Cursor c_year = dbAdapter.searchDB(null, column_year, year);
        Cursor c_month = dbAdapter.searchDB(null, column_month, month);
        Cursor c_day = dbAdapter.searchDB(null, column_day, day);

        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c_name_id.moveToFirst() && c_year.moveToFirst() && c_month.moveToFirst()
                && c_day.moveToFirst()) {
            do {
                xValues.add(c_name_id.getString(3)+c_name_id.getString(4)+c_name_id.getString(5)+
                        '\n' +c_name_id.getString(6));
                Log.d("取得したCursor:", c_name_id.getString(3)+c_name_id.getString(4)+
                        c_name_id.getString(5)+'\n' +c_name_id.getString(6));
            } while (c_name_id.moveToNext() && c_year.moveToNext() && c_month.moveToNext()
                    && c_day.moveToNext());
        }

        //////////////////////////////////////////////////////////////////////////////


        // valueA
        /*ArrayList<BarEntry> valuesA = new ArrayList<>();
        valuesA.add(new BarEntry(100, 0));*/

        //////////////////////////////////////////////////////////////////////////////


        // ArrayListを生成
        ArrayList<BarEntry> valuesA = new ArrayList<>();

        // DBのデータを取得
        /*String[] yColumns = {DBAdapter.COL_DISTANCE};     // DBのカラム：品名
        Cursor yc = dbAdapter.getDB1(yColumns);

        String[] yColumns_Num = {DBAdapter.COL_ID_DATA};     // DBのカラム：品名
        Cursor yc_num = dbAdapter.getDB1(yColumns_Num);*/

        if (c_name_id.moveToFirst() && c_year.moveToFirst() && c_month.moveToFirst()
                && c_day.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c_name_id.getString(12)), Num));
                Log.d("取得したCursor:", c_name_id.getString(12));
                Num = Num +1;
            } while (c_name_id.moveToNext() && c_year.moveToNext() && c_month.moveToNext()
                    && c_day.moveToNext());
        }
        c_name_id.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行時間");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        barDataSets.add(valuesADataSet);



        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_graph_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}