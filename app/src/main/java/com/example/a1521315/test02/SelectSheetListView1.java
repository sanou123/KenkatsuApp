package com.example.a1521315.test02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView表示画面に関連するクラス
 * SelectSheetListView
 */
public class SelectSheetListView1 extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<MyListItem1> items;
    private ListView mListView04;
    protected MyListItem1 myListItem1;

    // 参照するDBのカラム：名前,日時,最大心拍数,消費カロリー,総走行時間,総走行距離の全部なのでnullを指定
    private String[] columns = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sheet_listview1);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // DBAdapterのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);

        // itemsのArrayList生成
        items = new ArrayList<>();

        // MyBaseAdapterのコンストラクタ呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new MyBaseAdapter(this, items);

        // ListViewの結び付け
        mListView04 = (ListView) findViewById(R.id.listView03);

        loadMyList();   // DBを読み込む＆更新する処理

        // 行を長押しした時の処理
        mListView04.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long name) {

                // アラートダイアログ表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView1.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                // OKの時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // IDを取得する
                        myListItem1 = items.get(position);
                        String listName = myListItem1.getName();

                        dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                        dbAdapter.selectDelete(String.valueOf(listName));     // DBから取得したIDが入っているデータを削除する
                        Log.d("Long click : ", String.valueOf(listName));
                        dbAdapter.closeDB();    // DBを閉じる
                        loadMyList();
                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                //setOnItemClickListenerとsetOnItemLongClickListenerの両立
                return true;
            }
        });

    }

    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */
    private void loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // DBのデータを取得
        Cursor c = dbAdapter.getDB1(columns);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem1 = new MyListItem1(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8),
                        c.getString(9),
                        c.getString(10),
                        c.getString(11),
                        c.getString(12),
                        c.getString(13));

                Log.d("取得したCursor(DATA_ID):", String.valueOf(c.getString(0)));
                Log.d("取得したCursor(名前に対するID):", String.valueOf(c.getString(1)));
                Log.d("取得したCursor(名前):", String.valueOf(c.getString(2)));
                Log.d("取得したCursor(日時):", c.getString(3));
                Log.d("取得したCursor(心拍数):", c.getString(4));
                Log.d("取得したCursor(消費カロリー):", c.getString(5));
                Log.d("取得したCursor(総走行時間):", c.getString(6));
                Log.d("取得したCursor(総走行距離):", c.getString(7));
                Log.d("取得したCursor(コース名):", c.getString(8));
                Log.d("取得したCursor(タイム):", c.getString(9));
                Log.d("取得したCursor(平均速度):", c.getString(10));
                Log.d("取得したCursor(最高速度):", c.getString(11));
                Log.d("取得したCursor(走行距離):", c.getString(12));



                items.add(myListItem1);          // 取得した要素をitemsに追加

            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();                    // DBを閉じる
        mListView04.setAdapter(myBaseAdapter);  // ListViewにmyBaseAdapterをセット
        myBaseAdapter.notifyDataSetChanged();   // Viewの更新

    }

    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */
    public class MyBaseAdapter extends BaseAdapter {

        private Context context;
        private List<MyListItem1> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {
            TextView text05Name;
            TextView text05Date;
            TextView text05Heart_rate;
            TextView text05Calorie_consumption;
            TextView text05Total_time;
            TextView text05Total_distance;
            TextView text05Course_name;
            TextView text05Time;
            TextView text05Avg_speed;
            TextView text05Max_speed;
            TextView text05Distance;
            TextView text05Training_name;

        }

        // コンストラクタの生成
        public MyBaseAdapter(Context context, List<MyListItem1> items) {
            this.context = context;
            this.items = items;
        }

        // Listの要素数を返す
        @Override
        public int getCount() {
            return items.size();
        }

        // indexやオブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        // IDを他のindexに返す
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 新しいデータが表示されるタイミングで呼び出される
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;

            // データを取得
            myListItem1 = items.get(position);


            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_sheet_listview1, parent, false);

                TextView text05Name = (TextView) view.findViewById(R.id.text05Name);      // 品名のTextView
                TextView text05Date = (TextView) view.findViewById(R.id.text05Date);
                TextView text05Heart_rate = (TextView) view.findViewById(R.id.text05Heart_rate);      // 品名のTextView
                TextView text05Calorie_consumption = (TextView) view.findViewById(R.id.text05Calorie_consumption);        // 産地のTextView
                TextView text05Total_time = (TextView) view.findViewById(R.id.text05Total_time);          // 単価のTextView
                TextView text05Total_distance = (TextView) view.findViewById(R.id.text05Total_distance);          // 単価のTextView
                TextView text05Course_name = (TextView) view.findViewById(R.id.text05Course_name);      // 品名のTextView
                TextView text05Time = (TextView) view.findViewById(R.id.text05Time);      // 品名のTextView
                TextView text05Avg_speed = (TextView) view.findViewById(R.id.text05Avg_speed);      // 品名のTextView
                TextView text05Max_speed = (TextView) view.findViewById(R.id.text05Max_speed);      // 品名のTextView
                TextView text05Distance = (TextView) view.findViewById(R.id.text05Distance);      // 品名のTextView
                TextView text05Training_name = (TextView) view.findViewById(R.id.text05Training_name);      // 品名のTextView



                // holderにviewを持たせておく
                holder = new ViewHolder();
                holder.text05Name = text05Name;
                holder.text05Date = text05Date;
                holder.text05Heart_rate = text05Heart_rate;
                holder.text05Calorie_consumption = text05Calorie_consumption;
                holder.text05Total_time = text05Total_time;
                holder.text05Total_distance = text05Total_distance;
                holder.text05Course_name = text05Course_name;
                holder.text05Time = text05Time;
                holder.text05Avg_speed = text05Avg_speed;
                holder.text05Max_speed = text05Max_speed;
                holder.text05Distance = text05Distance;
                holder.text05Training_name = text05Training_name;

                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            holder.text05Name.setText(myListItem1.getName());
            holder.text05Date.setText(myListItem1.getDate());
            holder.text05Heart_rate.setText(myListItem1.getHeart_rate());
            holder.text05Calorie_consumption.setText(myListItem1.getCalorie_consumption());
            holder.text05Total_time.setText(myListItem1.getTotal_time());
            holder.text05Total_distance.setText(myListItem1.getTotal_distance());
            holder.text05Course_name.setText(myListItem1.getCourse_name());
            holder.text05Time.setText(myListItem1.getTime());
            holder.text05Avg_speed.setText(myListItem1.getAvg_speed());
            holder.text05Max_speed.setText(myListItem1.getMax_speed());
            holder.text05Distance.setText(myListItem1.getDistance());
            holder.text05Training_name.setText(myListItem1.getTraining_name());



            return view;

        }
    }

}