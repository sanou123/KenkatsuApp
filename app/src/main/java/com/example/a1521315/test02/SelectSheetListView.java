package com.example.a1521315.test02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView表示画面に関連するクラス
 * SelectSheetListView
 */
public class SelectSheetListView extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<MyListItem> items;
    private ListView mListView03;
    protected MyListItem myListItem;

    Globals globals;

    // 参照するDBのカラム：ID,名前,年齢,身長,体重の全部なのでnullを指定
    private String[] columns = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.select_sheet_listview);

        // DBAdapterのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);

        // itemsのArrayList生成
        items = new ArrayList<>();

        // MyBaseAdapterのコンストラクタ呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new MyBaseAdapter(this, items);

        // ListViewの結び付け
        mListView03 = (ListView) findViewById(R.id.listView03);

        loadMyList();   // DBを読み込む＆更新する処理

        //行を押した時の処理
        mListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long name) {
                // IDを取得する
                myListItem = items.get(position);
                String listName = myListItem.getName();

                globals.now_user = listName;


                // アラートダイアログ表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView.this);
                builder.setTitle("ログイン確認");
                builder.setMessage(listName + "さんですか？");
                // OKの時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // IDを取得する
                        //myListItem = items.get(position);
                        //String listName = myListItem.getName();
                        int NameID = myListItem.getUser_id();
                        String Age = myListItem.getAge();
                        String Sex = myListItem.getSex();
                        String Weight = myListItem.getWeight();
                        String Height = myListItem.getHeight();
                        double Bmi = myListItem.getBmi();
                        double Ideal_weight = myListItem.getIdeal_weight();
                        String  Login = myListItem.getLogin();


                        //int listId =  myListItem.getUser_id();//################
                        //String columns = listName + "さんが選択されました";
                        //Toast.makeText(getApplicationContext(), columns, Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(SelectSheetListView.this, VideoPlay.class);
                        //intent.putExtra("SELECTED_DATA",listName);
                        //globals.now_user = listName;
                        globals.name_id = String.valueOf(NameID);
                        globals.age = Age;
                        globals.sex = Sex;
                        globals.weight = Weight;
                        globals.height = Height;
                        globals.bmi = Bmi;
                        globals.ideal_weight = Ideal_weight;
                        globals.login = Login;
                        Intent intent = new Intent(SelectSheetListView.this, MenuSelect.class);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SelectSheetListView.this, Login.class);
                        startActivity(intent);
                    }
                });
                // ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        // 行を長押しした時の処理
        mListView03.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long user_id) {

                // アラートダイアログ表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                // OKの時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // IDを取得する
                        myListItem = items.get(position);
                        int listId = myListItem.getUser_id();

                        dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                        dbAdapter.selectDelete(String.valueOf(listId));     // DBから取得したIDが入っているデータを削除する
                        // dbAdapter.delete("MyList", "_id=1", null);
                        dbAdapter.allDelete();
                        Log.d("Long click : ", String.valueOf(listId));
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



        Button btnDisp = (Button)findViewById(R.id.insert);
        btnDisp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

         /*      dbAdapter.openDB();     // DBの読み込み(読み書きの方)

                // DBのデータを取得
                String[] columns = {DBAdapter.COL_ID_USER};     // DBのカラム：ID
                Cursor c = dbAdapter.getDB(columns);
                c.moveToFirst();
                if ( c.getInt(0)  < 6) {    */
                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.MainUser");
                startActivity(intent);

         /*           c.close();
                    dbAdapter.closeDB();    // DBを閉じる
                }else{
                    do {
                        String over = "登録出来ません。ユーザーを削除してください。";
                        Toast.makeText(getApplicationContext(), over, Toast.LENGTH_LONG).show();

                        c.close();
                        dbAdapter.closeDB();    // DBを閉じる
                    } while (c.moveToNext());
                }   */
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
        Cursor c = dbAdapter.getDB(columns);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem = new MyListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getDouble(6),
                        c.getDouble(7),
                        c.getString(8)
                );

                Log.d("取得したCursor(USER_ID):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(名前):", c.getString(1));
                Log.d("取得したCursor(年齢):", c.getString(2));
                Log.d("取得したCursor(性別):", c.getString(3));
                Log.d("取得したCursor(身長):", c.getString(4));
                Log.d("取得したCursor(体重):", c.getString(5));
                Log.d("取得したCursor(BMI):", c.getString(6));
                Log.d("取得したCursor(理想体重):", c.getString(7));
                Log.d("取得したCursor(最終ログイン):", c.getString(8));


                items.add(myListItem);          // 取得した要素をitemsに追加

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
        private List<MyListItem> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {
            TextView text05Name;
            TextView text05Age;
            TextView text05Sex;
            TextView text05Login;

        }

        // コンストラクタの生成
        public MyBaseAdapter(Context context, List<MyListItem> items) {
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
            myListItem = items.get(position);


            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_sheet_listview, parent, false);

                TextView text05Name = (TextView) view.findViewById(R.id.text05Name);      // 名前のTextView
                TextView text05Age = (TextView) view.findViewById(R.id.text05Age);        // 年齢のTextView
                TextView text05Sex = (TextView) view.findViewById(R.id.text05Sex);        // 性別のTextView
                TextView text05Login = (TextView) view.findViewById(R.id.text05Login);        // 性別のTextView


                // holderにviewを持たせておく
                holder = new ViewHolder();
                holder.text05Name = text05Name;
                holder.text05Age = text05Age;
                holder.text05Sex = text05Sex;
                holder.text05Login = text05Login;

                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            holder.text05Name.setText(myListItem.getName());
            holder.text05Age.setText(myListItem.getAge());
            holder.text05Sex.setText(myListItem.getSex());
            holder.text05Login.setText(myListItem.getLogin());


            return view;

        }
    }
}