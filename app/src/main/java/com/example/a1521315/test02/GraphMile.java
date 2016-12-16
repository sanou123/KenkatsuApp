package com.example.a1521315.test02;

/**
 * Created by 1521315 on 2016/11/17.
 */


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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


public class GraphMile extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DBAdapter dbAdapter;                // DBAdapter
    private ArrayList<String> items;            // ArrayList
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_barchart);

        // ArrayListを生成
        items = new ArrayList<>();

        /*
         * 表示
         */
        final Spinner yearspinner = (Spinner) findViewById(R.id.year_spinner);
        // ArrayAdapter を、string-array とデフォルトのレイアウトを引数にして生成
        //int planets_array = Integer.parseInt("2016");
        ArrayAdapter<CharSequence> yearadapter = ArrayAdapter.createFromResource(this, R.array.year_planets_array, android.R.layout.simple_spinner_item);
        //ArrayAdapter<CharSequence> yearadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        // 選択肢が表示された時に使用するレイアウトを指定
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // スピナーにアダプターを設定
        yearspinner.setAdapter(yearadapter);

        final Spinner monthspinner = (Spinner) findViewById(R.id.month_spinner);
        // ArrayAdapter を、string-array とデフォルトのレイアウトを引数にして生成
        ArrayAdapter<CharSequence> monthadapter = ArrayAdapter.createFromResource(this, R.array.month_planets_array, android.R.layout.simple_spinner_item);
        // 選択肢が表示された時に使用するレイアウトを指定
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // スピナーにアダプターを設定
        monthspinner.setAdapter(monthadapter);

        final Spinner dayspinner = (Spinner) findViewById(R.id.day_spinner);
        // ArrayAdapter を、string-array とデフォルトのレイアウトを引数にして生成
        ArrayAdapter<CharSequence> dayadapter = ArrayAdapter.createFromResource(this, R.array.day_planets_array, android.R.layout.simple_spinner_item);
        // 選択肢が表示された時に使用するレイアウトを指定
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // スピナーにアダプターを設定
        dayspinner.setAdapter(dayadapter);

        /*
         * イベントリスナー
         */
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // スピナー要素の文字列を取得
                String selectedItemString = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    Toast.makeText(GraphMile.this,"走行時間を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphMile.this, GraphTime.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.distance) {
                    Toast.makeText(GraphMile.this,"走行距離を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphMile.this, GraphMile.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.heart_rate) {
                    Toast.makeText(GraphMile.this,"心拍数を表示します!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GraphMile.this, GraphHeartRate.class);
                    startActivity(intent);
                    return true;
                }

                return true;
            }
        });

        //graphボタンを押した時UserSelectへ移動
        Button btnDisp = (Button) findViewById(R.id.graph_button);
        btnDisp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dayspinner == null) {
                    createBarChartDay();
                }else if(monthspinner == null) {
                    createBarChartDataMonth();
                }else if(yearspinner != null) {
                    createBarChartYear();
                }
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

        // ArrayListを生成
        //items = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

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
            do {
                xValues.add(c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
                Log.d("取得したCursor:", c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
            } while (c.moveToNext());
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

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c.getString(15)), Num));
                Log.d("取得したCursor:", c.getString(15));
                Num = Num +1;
            } while (c.moveToNext()/* && yc_num.moveToNext()*/);
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行距離");
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

        String column = "name_id";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字
        String column1 = "year";          //検索対象のカラム名
        String[] year = {"2015"};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c = dbAdapter.searchDB(null, column, name_id);
        Cursor c1 = dbAdapter.searchDB(null, column1, year);

        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c.moveToFirst() && c1.moveToFirst()) {
            do {
                xValues.add(c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
                Log.d("取得したCursor:", c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
            } while (c.moveToNext());
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

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c.getString(15)), Num));
                Log.d("取得したCursor:", c.getString(15));
                Num = Num +1;
            } while (c.moveToNext() && c1.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行距離");
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
            do {
                xValues.add(c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
                Log.d("取得したCursor:", c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
            } while (c.moveToNext());
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

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c.getString(15)), Num));
                Log.d("取得したCursor:", c.getString(15));
                Num = Num +1;
            } while (c.moveToNext()/* && yc_num.moveToNext()*/);
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行距離");
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