package com.example.a1521315.test02;

import android.content.Context;
import android.content.Intent;
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
 * Created by 1521315 on 2016/12/22.
 */

public class GraphListView extends AppCompatActivity {

        private DBAdapter dbAdapter;
        private com.example.a1521315.test02.GraphListView.MyBaseAdapter myBaseAdapter;
        private List<MyListItem1> items;
        private ListView mListView03;
        protected MyListItem1 myListItem1;

        Globals globals;

        // 参照するDBのカラム：ID,名前,年齢,身長,体重の全部なのでnullを指定
        private String[] columns = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //横画面に固定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.graph_sheet_listview);

            // DBAdapterのコンストラクタ呼び出し
            dbAdapter = new DBAdapter(this);

            // itemsのArrayList生成
            items = new ArrayList<>();

            // MyBaseAdapterのコンストラクタ呼び出し(myBaseAdapterのオブジェクト生成)
            myBaseAdapter = new com.example.a1521315.test02.GraphListView.MyBaseAdapter(this, items);

            // ListViewの結び付け
            mListView03 = (ListView) findViewById(R.id.listView03);

            loadMyList();   // DBを読み込む＆更新する処理

            //行を押した時の処理
            mListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long name) {
                    // IDを取得する
                    myListItem1 = items.get(position);
                    String listName = myListItem1.getName();

                    //globals.now_user = listName;

                            String Year = myListItem1.getDate();
                            String Month = myListItem1.getHeart_rate();
                            String Day = myListItem1.getCalorie_consumption();



                            globals.year = Year;
                            globals.month = Month;
                            globals.day = Day;

                            Log.d("Year",Year);
                            Log.d("Month",Month);
                            Log.d("Day",Day);

                            Intent intent = new Intent(com.example.a1521315.test02.GraphListView.this,
                                    GraphTab.class);
                            startActivity(intent);


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
                            c.getString(13),
                            c.getString(14),
                            c.getString(15),
                            c.getString(16),
                            c.getLong(17)
                            );

                    Log.d("取得したCursor(DATA_ID):", String.valueOf(c.getInt(0)));
                    Log.d("取得したCursor(名前に対するID):", c.getString(1));
                    Log.d("取得したCursor(名前):", c.getString(2));
                    Log.d("取得したCursor(年):", c.getString(3));
                    Log.d("取得したCursor(月):", c.getString(4));
                    Log.d("取得したCursor(日):", c.getString(5));
                    Log.d("取得したCursor(時間):", c.getString(6));
                    Log.d("取得したCursor(心拍数):", c.getString(7));
                    Log.d("取得したCursor(消費カロリー):", c.getString(8));
                    Log.d("取得したCursor(総走行時間):", c.getString(9));
                    Log.d("取得したCursor(総走行距離):", c.getString(10));
                    Log.d("取得したCursor(コース名):", c.getString(11));
                    Log.d("取得したCursor(タイム):", c.getString(12));
                    Log.d("取得したCursor(平均速度):", c.getString(13));
                    Log.d("取得したCursor(最高速度):", c.getString(14));
                    Log.d("取得したCursor(走行距離):", c.getString(15));
                    Log.d("取得したCursor(トレーニング名):", c.getString(16));
                    Log.d("取得したCursor(グラフ用時間(分表示)):", String.valueOf(c.getLong(17)));

                    items.add(myListItem1);          // 取得した要素をitemsに追加

                } while (c.moveToNext());
            }
            c.close();
            dbAdapter.closeDB();                    // DBを閉じる
            mListView03.setAdapter(myBaseAdapter);  // ListViewにmyBaseAdapterをセット
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
                TextView text05Year;
                TextView text05Month;
                TextView text05Day;

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
                com.example.a1521315.test02.GraphListView.MyBaseAdapter.ViewHolder holder;

                // データを取得
                myListItem1 = items.get(position);

                if (view == null) {
                    LayoutInflater inflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.row_graph_listview, parent, false);

                    TextView text05Year = (TextView) view.findViewById(R.id.text05Year);      // 名前のTextView
                    TextView text05Month = (TextView) view.findViewById(R.id.text05Month);        // 年齢のTextView
                    TextView text05Day = (TextView) view.findViewById(R.id.text05Day);        // 性別のTextView


                    // holderにviewを持たせておく
                    holder = new com.example.a1521315.test02.GraphListView.MyBaseAdapter.ViewHolder();
                    holder.text05Year = text05Year;
                    holder.text05Month = text05Month;
                    holder.text05Day = text05Day;

                    view.setTag(holder);

                } else {
                    // 初めて表示されるときにつけておいたtagを元にviewを取得する
                    holder = (com.example.a1521315.test02.GraphListView.MyBaseAdapter.ViewHolder) view.getTag();
                }

                // 取得した各データを各TextViewにセット
                holder.text05Year.setText(myListItem1.getDate());
                holder.text05Month.setText(myListItem1.getHeart_rate());
                holder.text05Day.setText(myListItem1.getCalorie_consumption());


                return view;

            }
        }
    }
