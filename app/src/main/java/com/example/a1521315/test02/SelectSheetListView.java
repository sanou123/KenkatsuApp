package com.example.a1521315.test02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.a1521315.test02.Age.getBirthCal;

/**
 * ListView表示画面に関連するクラス
 * SelectSheetListView
 */
public class SelectSheetListView extends Activity {

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
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

                if((globals.twitter_user == null) && (globals.twitter_user != globals.now_user)){

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
                    String  User_Year = myListItem.getUser_Year();
                    String  User_Month = myListItem.getUser_Month();
                    String  User_Day = myListItem.getUser_Day();


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
                    //元データをBigDecimal型にする
                    BigDecimal bd_bmi = new BigDecimal(Bmi);
                    //四捨五入する
                    BigDecimal bmi = bd_bmi.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                    globals.bmi = bmi.doubleValue();
                    //元データをBigDecimal型にする
                    BigDecimal bd_ideal_weight = new BigDecimal(Ideal_weight);
                    //四捨五入する
                    BigDecimal ideal_weight = bd_ideal_weight.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                    globals.ideal_weight = ideal_weight.doubleValue();
                    globals.login = Login;
                    globals.user_year = User_Year;
                    globals.user_month = User_Month;
                    globals.user_day = User_Day;
                    Intent intent = new Intent(SelectSheetListView.this, MenuSelect.class);
                    startActivity(intent);

                }else if(globals.twitter_user != null){

                    // アラートダイアログ表示
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView.this);
                    builder.setTitle("twitterログイン確認");
                    builder.setMessage("最後にログインしたのは" + globals.twitter_user+ "さんです!\n"
                            +  globals.twitter_user + "さんですか!?");
                    // OKの時の処理
                    builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            globals.twitter_user = globals.now_user;

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
                            String  User_Year = myListItem.getUser_Year();
                            String  User_Month = myListItem.getUser_Month();
                            String  User_Day = myListItem.getUser_Day();


                            globals.name_id = String.valueOf(NameID);
                            globals.age = Age;
                            globals.sex = Sex;
                            globals.weight = Weight;
                            globals.height = Height;
                            //元データをBigDecimal型にする
                            BigDecimal bd_bmi = new BigDecimal(Bmi);
                            //四捨五入する
                            BigDecimal bmi = bd_bmi.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                            globals.bmi = bmi.doubleValue();
                            //元データをBigDecimal型にする
                            BigDecimal bd_ideal_weight = new BigDecimal(Ideal_weight);
                            //四捨五入する
                            BigDecimal ideal_weight = bd_ideal_weight.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                            globals.ideal_weight = ideal_weight.doubleValue();
                            globals.login = Login;

                            globals.user_year = User_Year;
                            globals.user_month = User_Month;
                            globals.user_day = User_Day;

                            Intent intent = new Intent(SelectSheetListView.this, MenuSelect.class);
                            startActivity(intent);

                            dbLogin();

                        }
                    });

                    builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            globals.twitter_user = null;

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
                            String  User_Year = myListItem.getUser_Year();
                            String  User_Month = myListItem.getUser_Month();
                            String  User_Day = myListItem.getUser_Day();


                            globals.name_id = String.valueOf(NameID);
                            globals.age = Age;
                            globals.sex = Sex;
                            globals.weight = Weight;
                            globals.height = Height;
                            //元データをBigDecimal型にする
                            BigDecimal bd_bmi = new BigDecimal(Bmi);
                            //四捨五入する
                            BigDecimal bmi = bd_bmi.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                            globals.bmi = bmi.doubleValue();
                            //元データをBigDecimal型にする
                            BigDecimal bd_ideal_weight = new BigDecimal(Ideal_weight);
                            //四捨五入する
                            BigDecimal ideal_weight = bd_ideal_weight.setScale(2, BigDecimal.ROUND_HALF_UP);  //小数第２位
                            globals.ideal_weight = ideal_weight.doubleValue();
                            globals.login = Login;

                            globals.user_year = User_Year;
                            globals.user_month = User_Month;
                            globals.user_day = User_Day;

                            Intent intent = new Intent(SelectSheetListView.this, MenuSelect.class);
                            startActivity(intent);

                            dbLogin();
                            Toast.makeText(SelectSheetListView.this, "twitterログアウトしました。", Toast.LENGTH_LONG).show();
                        }
                    });
                    // ダイアログの表示
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

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

                Intent intent = new Intent();
                intent.setClassName("com.example.a1521315.test02",
                        "com.example.a1521315.test02.MainUser");
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
                        c.getString(8),
                        c.getString(9),
                        c.getString(10),
                        c.getString(11)
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

    public void dbLogin(){
        long currentTimeMillis = System.currentTimeMillis();

        Date date = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        Log.v("時間", dateFormat.format(date));
        globals.login = dateFormat.format(date);

        int height = Integer.parseInt(globals.height);
        int weight = Integer.parseInt(globals.weight);
        int iYear = Integer.parseInt(globals.user_year);
        int iMonth = Integer.parseInt(globals.user_month);
        int iDay = Integer.parseInt(globals.user_day);

        long iAge = getOld(globals.user_year+globals.user_month+globals.user_day);

        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();                                         // DBの読み書き
        dbAdapter.updateDB(globals.now_user, iAge, globals.sex, height, weight, globals.bmi ,
                globals.ideal_weight, globals.login,iYear,iMonth,iDay);   // DBに登録
        dbAdapter.closeDB();                                        // DBを閉じる
    }

    /**
     * 年齢を返す
     *
     * @param birth_day yyyyMMdd 形式の誕生日
     * @return 年齢
     * @throws ParseException
     */

    public static long getOld(String birth_day) throws ParseException {

        Calendar birthCal = getBirthCal(birth_day);

        Date now = new Date();

        if (birthCal.getTime().after(now)) {

            return 0; // マイナスは0

        }

        Calendar nowCal = Calendar.getInstance();

        nowCal.setTime(now);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String nowYmd = df.format(now);

        return (Long.parseLong(nowYmd) - Long.parseLong(birth_day)) / 10000L;

    }


    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.MainActivity");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}