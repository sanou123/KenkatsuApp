package com.example.a1521315.test02;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class VideoPlay extends Activity implements SurfaceHolder.Callback, Runnable, MediaPlayer.OnCompletionListener, View.OnClickListener {

    Globals globals;
    /*メーター関連の関数*/
    TextView tBPM, tHeartbeat;//心拍の変数
    TextView tTargetBPM, tTargetHeartbeat;//目標心拍数の変数
    TextView tKPH, tSpeed;//時速の変数
    TextView tKM, tMileage;//走行距離の変数
    TextView tCAL, tCal;//走行距離の変数
    TextView tTimer;//タイマーの変数
    TextView tCourse;//コース名

    /*ギア*/
    TextView tGear;//ギア

    /*最高速度*/
    double maxSpeed = 0.0;

    /*最大心拍*/
    int maxHeartbeat = 0;

    /*平均速度を出すのに必要な関数*/
    double totalSpeed = 0.0;
    int totalSpeedCnt = 0;

    /*デバッグ用の関数*/
    TextView tDebug1;
    TextView tDebug2;

    boolean mediaPathCheck = false;//rawファイルかどうかを判断する変数。true=内部ストレージ　false=rawファイル
    String mediaPath = null;//動画データ
    private ImageView imageMe;//自機イメージ用の変数
    private ImageView imageGhost;
    double totalMileage = 0;//総走行距離用,選択されたコースごとに変わる
    double speedCount = 0.0;//速度用
    double cal = 0.0; //カロリー計算用
    double weight = Double.parseDouble(globals.weight);

    double psKilometers = 1, psSeconds = 10;

    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    //動画
    private MediaPlayer mp = null;
    //BGM
    private MediaPlayer mpBGM = null;

    //タイムに関する奴
    private ScheduledExecutorService timerscheduler;
    ScheduledFuture future;
    Runnable myTimerTask = new CntTimerTask();

    //シークバーに関する奴
    private ScheduledExecutorService seekbarscheduler;
    ScheduledFuture seekbarfuture;
    Runnable mySeekBarTask = new SeekBarTask();

    PlaybackParams params = new PlaybackParams();


    //USB通信関連の変数　初期化
    private final static int USBAccessoryWhat = 0;
    public static final int UPDATE_LED_SETTING = 1;
    public static final int HOLE_SENSOR_CHANGE = 3;
    public static final int UPDATE_RESISTANCE = 5;
    public static final int APP_CONNECT = (int) 0xFE;
    public static final int APP_DISCONNECT = (int) 0xFF;
    private boolean deviceAttached = false;
    private int firmwareProtocol = 0;

    //センサー、動画再生関連の変数　初期化
    double speed_Value = 0.0;//速度の値
    double all_speed_Value=0.0;//speedValueを25回加算して平均だす
    double max_speed_Value = 0.0;
    double pedal_Value = 0.0005F;//回すやつの直径
    boolean stop_Flg = false;//止まってから始動のとき用
    int timer_check=0;//ON OFF のホールセンサーの順番
    boolean inertia_Flg = false;//慣性に入った判断
    int cnt_25 = 0;//25回データとったかのカウンター
    long limit_Value = 150;//慣性判断での閾値

    //double dist_Value = 0.0;//ペダルレベルでの距離
    //double old_dist_Value = 0.0;//ペダルレベルでの距離

    public float resist_Level = (float) 1.0;//負荷のレベルによる係数
    NumberFormat format2 = NumberFormat.getInstance();

    public int hole_Value = 0;
    public int old_hole_Value = 0;

    public boolean run_Flg = false;//ON OFF で順番にセンサーの値を受け取るため
    public boolean usb_Flg = false;//pause画面で速度を変化させない
    public boolean chSpd_Flg = false;//speed_valueを更新するか否か
    public boolean Gear1_Flg = false;
    public boolean Gear2_Flg = false;
    public boolean Gear3_Flg = false;
    public boolean Gear4_Flg = false;
    public boolean Gear5_Flg = false;
    public boolean Gear6_Flg = false;

    //止まらずに進んでる時間
    long t_cnt=0;
    long old_time = 0;
    long now_time = 0;

    private USBAccessoryManager accessoryManager;


    //bluetooth*************************************************************************************
    private BluetoothAdapter mAdapter;/* Bluetooth Adapter */
    private BluetoothDevice mDevice;/* Bluetoothデバイス */
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");/* Bluetooth UUID */
    private final String DEVICE_NAME = "RNBT-C46F";/* デバイス名 */
    private BluetoothSocket mSocket; /* Soket */
    private Thread mThread; /* Thread */
    private boolean isRunning; /* Threadの状態を表す */
    //private Button connectButton;/** 接続ボタン. */
    private TextView mStatusTextView;
    /**
     * ステータス.
     */
    private TextView mInputTextView;
    /**
     * Bluetoothから受信した値.
     */
    private static final int VIEW_STATUS = 0;
    /**
     * Action(ステータス表示).
     */
    private static final int VIEW_INPUT = 1;/** Action(取得文字列). */
    /**
     * BluetoothのOutputStream.
     */
    OutputStream mmOutputStream = null;
    private boolean connectFlg = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //**********************************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ディスプレイサイズ取得
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        Log.v("width: ", String.valueOf(p.x));
        Log.v("height: ", String.valueOf(p.y));
        if (p.x == 2048) {
            //nexus 9の幅
            setContentView(R.layout.activity_video_play);
        } else {
            //sそれ以外(nexus7 2013とか)
            setContentView(R.layout.activity_video_play_7);
        }

        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.addCallback(this);

        globals = (Globals) this.getApplication();
        globals.DriveDataInit();//グローバル変数初期化

        format2.setMaximumFractionDigits(2);

        tCal = (TextView) findViewById(R.id.textCal);
        tCal.setText("000.00");
        tCAL = (TextView) findViewById(R.id.textCAL);
        tCAL.setText("Calorie              kcal");
        tMileage = (TextView) findViewById(R.id.textMileage);
        tMileage.setText("000.00");
        tKM = (TextView) findViewById(R.id.textKM);
        tKM.setText("Mileage              km");
        tSpeed = (TextView) findViewById(R.id.textSpeed);
        tSpeed.setText("00.00");
        tKPH = (TextView) findViewById(R.id.textKPH);
        tKPH.setText("Speed              km/h");

        tTargetHeartbeat = (TextView) findViewById(R.id.textTargetHeartbeat);
        tTargetHeartbeat.setText("" + TargetBPM(Integer.parseInt(globals.age)));
        tTargetBPM = (TextView) findViewById(R.id.textTargetBPM);
        tTargetBPM.setText("Target BPM");
        tHeartbeat = (TextView) findViewById(R.id.textHeartbeat);
        tHeartbeat.setText("000");
        tBPM = (TextView) findViewById(R.id.textBPM);
        tBPM.setText("BPM");

        tTimer = (TextView) findViewById(R.id.textTimer);
        tTimer.setText("00:00:00.0");

        tDebug1 = (TextView) findViewById(R.id.textDebug1);
        tDebug1.setText("age:" + globals.age);
        TargetBPM(Integer.parseInt(globals.age));
        tDebug2 = (TextView) findViewById(R.id.textDebug2);
        tDebug2.setText("TargetBPM:" + TargetBPM(Integer.parseInt(globals.age)));

        tGear = (TextView) findViewById(R.id.textGear);
        tGear.setText("0");

        /*グローバル変数にバグあるので前回のデータは取得しない↓ゴーストは1kmを10秒で走る設定で固定
        下のメソッドのコメントアウトを消せば前回のデータでゴーストが動くよ*/
        GetLastTrainingData();//前回のデータを色々やってる
        Change7Seg();//7セグフォントに変換

        /*シークバーに関する奴*/
        imageMe = (ImageView) findViewById(R.id.image_view_me);
        imageMe.setImageResource(R.drawable.me);
        imageGhost = (ImageView) findViewById(R.id.image_view_ghost);
        imageGhost.setImageResource(R.drawable.ghost);
        ImageView imageView1 = (ImageView) findViewById(R.id.image_view_bar);
        imageView1.setImageResource(R.drawable.bar0);

        /*タイム表示*/
        ImageView timeDisplay = (ImageView) findViewById(R.id.image_TimeDisplay);
        timeDisplay.setImageResource(R.drawable.time);
        /*コースネーム*/
        ImageView CoursenameDisplay = (ImageView) findViewById(R.id.image_Coursenamedisplay);
        CoursenameDisplay.setImageResource(R.drawable.coursename);
        /*ギア*/
        ImageView GearDisplay = (ImageView) findViewById(R.id.imageGear);
        GearDisplay.setImageResource(R.drawable.gear);

        //各種ディスプレイ
        ImageView CalDisplay = (ImageView) findViewById(R.id.imageCalDisplay);
        CalDisplay.setImageResource(R.drawable.display);
        CalDisplay.setAlpha(150);
        ImageView MileageDisplay = (ImageView) findViewById(R.id.imageMileageDisplay);
        MileageDisplay.setImageResource(R.drawable.display);
        MileageDisplay.setAlpha(150);
        ImageView SpeedDisplay = (ImageView) findViewById(R.id.imageSpeedDisplay);
        SpeedDisplay.setImageResource(R.drawable.display);
        SpeedDisplay.setAlpha(150);

        ImageView TargetBPMDisplay = (ImageView) findViewById(R.id.imageTargetBPMDisplay);
        TargetBPMDisplay.setImageResource(R.drawable.display);
        TargetBPMDisplay.setAlpha(150);
        ImageView BPMDisplay = (ImageView) findViewById(R.id.imageBPMDisplay);
        BPMDisplay.setImageResource(R.drawable.display);
        BPMDisplay.setAlpha(150);

        //コース番号受け取り
        Intent i = getIntent();
        String CourseNum = i.getStringExtra("course");
        tCourse = (TextView) findViewById(R.id.textCourse);
        tCourse.setText("コース" + CourseNum);
        if (CourseNum.equals("0")) {
            tCourse.setText("ポリテク→大地");
            mediaPath = "/test02.mp4";//実機9のストレージにあるファルを指定
            totalMileage = 10.4;
            mediaPathCheck = false;
        } else if (CourseNum.equals("1")) {
            tCourse.setText("東京→御殿場");
            mediaPath = "/test_x264.mp4";//実機9のストレージにあるファイルを指定
            totalMileage = 83.7;
            mediaPathCheck = false;
        } else if (CourseNum.equals("2")) {
            tCourse.setText("鳴子");
            mediaPath = "/_naruko.mp4";//実機9のストレージにあるファイルを指定
            totalMileage = 1.3;
            mediaPathCheck = false;
        } else if (CourseNum.equals("3")) {
            tCourse.setText("デバッグ用");
            mediaPath = "android.resource://" + getPackageName() + "/" + R.raw.test01;//rawフォルダから指定する場合
            totalMileage = 2.9;
            mediaPathCheck = true;
        }

        //USBAccessoryManager の初期化
        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            Log.d(TAG, "Info:" + info.packageName + "\n" + info.versionCode + "\n" + info.versionName);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            Toast.makeText(this,"エラー",Toast.LENGTH_LONG);
        }
        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);

        //bluetooth*********************************************************************************
        mInputTextView = (TextView) findViewById(R.id.textHeartbeat);
        mStatusTextView = (TextView) findViewById(R.id.textConnectStatus);
        // Bluetoothのデバイス名を取得
        // デバイス名は、RNBT-XXXXになるため、
        // DVICE_NAMEでデバイス名を定義
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mStatusTextView.setText("SearchDevice");
        Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getName().equals(DEVICE_NAME)) {
                mStatusTextView.setText("find: " + device.getName());
                mDevice = device;
            }
        }
        //******************************************************************************************
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }//onCreateここまで

    // 再生完了時の処理
    @Override
    public void onCompletion(MediaPlayer agr0) {
        Log.v("MediaPlayer", "onCompletion");
        timerscheduler.shutdown();//タイマー止める
        seekbarscheduler.shutdown();
        //USB通信の切断(停止がないため)
        accessoryManager.disable(this);
        disconnectAccessory();//#
        //リザルトダイアログを表示
        ResultDialog();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    //Activity の　基本ライフサイクル//
    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.d(TAG, "onStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        accessoryManager.enable(VideoPlay.this, getIntent());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
        switch (firmwareProtocol) {
            case 2:
                byte[] commandPacket = new byte[2];
                commandPacket[0] = (byte) APP_DISCONNECT;
                commandPacket[1] = 0;
                accessoryManager.write(commandPacket);
                break;
        }

        try {
            while (accessoryManager.isClosed() == false) {
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //bluetooth***********
        isRunning = false;
        try {
            mSocket.close();
        } catch (Exception e) {
        }
        //*************************
    }

    // Resets the demo application when a device detaches
    public void disconnectAccessory() {
        Log.d(TAG, "disconnectAccessory()");
        Toast.makeText(this, "通信切断処理中…", Toast.LENGTH_LONG).show();

        if (deviceAttached == false) {
            //Toast.makeText(this, "デバイスが切断されました", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        try {
            //MediaPlayerを生成
            mp = new MediaPlayer();

            if (mediaPathCheck == true) {
                //動画ファイルをMediaPlayerに読み込ませる
                mp.setDataSource(getApplicationContext(), Uri.parse(mediaPath));//rawフォルダから指定する場合
            } else {
                File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                String dir = pathExternalPublicDir.getPath();//dirは　/storage/emulated/0/Movie　を指定してい
                //動画ファイルをMediaPlayerに読み込ませる
                mp.setDataSource(dir + mediaPath);//内部ストレージから指定する場合
            }

            //読み込んだ動画ファイルを画面に表示する
            mp.setDisplay(holder);
            mp.prepare();
            mp.setOnCompletionListener(this);

        } catch (IllegalArgumentException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    //bluetooth*************************************************************************************
    //Runnableインターフェース追加
    @Override
    public void run() {
        InputStream mmInStream = null;
        Message valueMsg = new Message();
        valueMsg.what = VIEW_STATUS;
        valueMsg.obj = "connecting...";
        mHandler.sendMessage(valueMsg);

        try {
            // 取得したデバイス名を使ってBluetoothでSocket接続
            mSocket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            mSocket.connect();
            mmInStream = mSocket.getInputStream();
            mmOutputStream = mSocket.getOutputStream();

            // InputStreamのバッファを格納
            byte[] buffer = new byte[1024];

            // 取得したバッファのサイズを格納
            int bytes;
            valueMsg = new Message();
            valueMsg.what = VIEW_STATUS;
            valueMsg.obj = "connected.";
            mHandler.sendMessage(valueMsg);
            connectFlg = true;

            //コネクトチェック画面を消す
            ConnectCheck connectCheck = new ConnectCheck();
            connectCheck.run();
            while (isRunning) {
                // InputStreamの読み込み
                bytes = mmInStream.read(buffer);
                Log.i(TAG, "bytes=" + bytes);
                // String型に変換
                String readMsg = new String(buffer, 0, bytes);

                // null以外なら表示
                if (readMsg.trim() != null && !readMsg.trim().equals("")) {
                    Log.i(TAG, "value=" + readMsg.trim());

                    valueMsg = new Message();
                    valueMsg.what = VIEW_INPUT;
                    valueMsg.obj = readMsg;
                    mHandler.sendMessage(valueMsg);
                } else {
                    // Log.i(TAG,"value=nodata");
                }
                //Thread.sleep(10);

            }
        } catch (Exception e) {

            valueMsg = new Message();
            valueMsg.what = VIEW_STATUS;
            //valueMsg.obj = "Error1:" + e;
            valueMsg.obj = "not connected.";

            mHandler.sendMessage(valueMsg);

            try {
                mSocket.close();

            } catch (Exception ee) {
            }
            isRunning = false;
            //通信が確立するまで通信しようとする↓
            if (!connectFlg) {
                mThread = new Thread(this);
                // Threadを起動し、Bluetooth接続
                isRunning = true;
                mThread.start();
            }
        }
    }

    /**
     * 描画処理はHandlerでおこなう
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int action = msg.what;
            String msgStr = (String) msg.obj;
            if (action == VIEW_INPUT  && msgStr.length() == 3) {
                mInputTextView.setText(msgStr);
                //最大心拍の判断
                Maxheartbeat maxHeartbeat = new Maxheartbeat(Integer.parseInt(mInputTextView.getText().toString()));
                maxHeartbeat.run();

            } else if (action == VIEW_STATUS) {
                mStatusTextView.setText(msgStr);
            }
        }
    };
    //bluetooth*************************************************************************************

    // USB通信のタスク
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            byte[] commandPacket = new byte[2];
            Log.d(TAG, "handleMessage");
            switch (msg.what) {
                case UPDATE_LED_SETTING:
                    if (accessoryManager.isConnected() == false) {
                        return;
                    }
                    break;

                case USBAccessoryWhat:
                    switch (((USBAccessoryManagerMessage) msg.obj).type) {
                        case READ:
                            if (accessoryManager.isConnected() == false) {
                                return;
                            }

                            while (true) {
                                if (accessoryManager.available() < 2) {
                                    //All of our commands in this example are 2 bytes.  If there are less
                                    //  than 2 bytes left, it is a partial command
                                    break;
                                }

                                accessoryManager.read(commandPacket);

                                switch (commandPacket[0]) {

                                    case HOLE_SENSOR_CHANGE:
                                        //一時停止時
                                        if (usb_Flg == true) {
                                            speed_Value = 0.0;
                                        } else {
                                            //時間の取得(秒に直す)
                                            float time_tmp = (float) t_cnt;
                                            long microSec=0;

                                            //センサー値取得
                                            hole_Value = (int) (commandPacket[1] & 0xFF);

                                            //距離の加算(径分)
                                            if (hole_Value == 1 && old_hole_Value == 0 && run_Flg == false) {

                                                now_time = System.nanoTime();

                                                run_Flg = true;
                                                if(timer_check == 2){
                                                    chSpd_Flg = true;
                                                    microSec = TimeUnit.MICROSECONDS.convert( now_time - old_time, TimeUnit.NANOSECONDS );
                                                    if((microSec/1000) >= limit_Value){
                                                        inertia_Flg = true;
                                                    }
                                                    tDebug1.setText( (microSec/1000) + "microsec passed" );
                                                }
                                                timer_check = 1;
                                            }

                                            //距離の加算(径分)
                                            else if (hole_Value == 0 && old_hole_Value == 1 && run_Flg == true) {
                                                run_Flg = false;
                                                old_time = System.nanoTime();
                                                if(timer_check == 1){
                                                    timer_check = 2;
                                                    old_time = now_time;
                                                }
                                            } else {
                                            }

                                            //モータが1回転した場合に、速度を算出する処理
                                            if (chSpd_Flg == true && inertia_Flg == false) {
                                                //25回分の平均の速度を表示
                                                //なぜなら1:25だから
                                                speed_Value = pedal_Value / (((double)(microSec)/1000)/1000000);//  モータの径　/ cnt*10msec
                                                tDebug2.setText("sp:"+speed_Value);
                                                if(speed_Value >= 0 && speed_Value <= 50){
                                                    cnt_25++;
                                                    all_speed_Value += speed_Value;
                                                }
                                                chSpd_Flg=false;
                                            }

                                        }
//
                                        //通常の速度反映処理
                                        if(inertia_Flg == false){
                                            //メディアプレイヤーの再生速度を設定
                                            if (speed_Value <= 50 && speed_Value >= 0) {
                                                if(cnt_25 >= 25){
                                                    cnt_25 = 0;
                                                    all_speed_Value = all_speed_Value / 25;
                                                    tSpeed.setText(String.format("%.2f",all_speed_Value));
                                                    params.setSpeed((float) (all_speed_Value / 20));//再生速度変更
                                                    mp.setPlaybackParams(params);
                                                    max_speed_Value = all_speed_Value;//ここで代入
                                                    all_speed_Value = 0.0;
                                                }
                                                //漕ぎ出し時に速度を出す処理
                                                else if(stop_Flg == true){
                                                    stop_Flg = false;
                                                    tSpeed.setText(String.format("%.2f",speed_Value));
                                                    params.setSpeed((float) (speed_Value / 20));//再生速度変更
                                                    mp.setPlaybackParams(params);
                                                }
                                                else{

                                                }
                                            }
                                            //速度が0のとき
                                            else {
                                            }
                                        }
                                        //慣性始まったと判断された時の処理
                                        //inertiaFlg == true
                                        else{
                                            all_speed_Value = 0;
                                            speed_Value = 0;
                                            cnt_25 = 0;
                                            stop_Flg = true;
                                            inertia_Flg = false;
                                            ZeroSpeedSet();
                                        }

                                        //過去の値を更新
                                        old_hole_Value = hole_Value;
                                        break;

                                    //抵抗値の受け取り
                                    case UPDATE_RESISTANCE:

                                        //センサー値取得
                                        int resist_Value = (int) (commandPacket[1] & 0xFF);

                                        //抵抗値の割り出し
                                        switch (resist_Value) {
                                            case 1:
                                                limit_Value = 120;
                                                Gear1_Flg = true;
                                                Gear2_Flg = false;
                                                Gear3_Flg = false;
                                                Gear4_Flg = false;
                                                Gear5_Flg = false;
                                                Gear6_Flg = false;
                                                break;
                                            case 2:
                                                limit_Value = 120;
                                                Gear1_Flg = false;
                                                Gear2_Flg = true;
                                                Gear3_Flg = false;
                                                Gear4_Flg = false;
                                                Gear5_Flg = false;
                                                Gear6_Flg = false;
                                                break;
                                            case 3:
                                                limit_Value = 150;
                                                Gear1_Flg = false;
                                                Gear2_Flg = false;
                                                Gear3_Flg = true;
                                                Gear4_Flg = false;
                                                Gear5_Flg = false;
                                                Gear6_Flg = false;
                                                break;
                                            case 4:
                                                limit_Value = 150;
                                                Gear1_Flg = false;
                                                Gear2_Flg = false;
                                                Gear3_Flg = false;
                                                Gear4_Flg = true;
                                                Gear5_Flg = false;
                                                Gear6_Flg = false;
                                                break;
                                            case 5:
                                                limit_Value = 150;
                                                Gear1_Flg = false;
                                                Gear2_Flg = false;
                                                Gear3_Flg = false;
                                                Gear4_Flg = false;
                                                Gear5_Flg = true;
                                                Gear6_Flg = false;
                                                break;
                                            case 6:
                                                limit_Value = 150;
                                                Gear1_Flg = false;
                                                Gear2_Flg = false;
                                                Gear3_Flg = false;
                                                Gear4_Flg = false;
                                                Gear5_Flg = false;
                                                Gear6_Flg = true;
                                                break;
                                            default:
                                                limit_Value = 150;
                                                break;
                                        }

                                        break;
                                }
                            }
                            break;

                        case CONNECTED:
                            break;

                        case READY:
                            String version = ((USBAccessoryManagerMessage) msg.obj).accessory.getVersion();
                            firmwareProtocol = getFirmwareProtocol(version);

                            switch (firmwareProtocol) {
                                case 1:
                                    deviceAttached = true;
                                    break;
                                case 2:
                                    deviceAttached = true;
                                    commandPacket[0] = (byte) APP_CONNECT;
                                    commandPacket[1] = 0;
                                    accessoryManager.write(commandPacket);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case DISCONNECTED:
                            disconnectAccessory();
                            break;
                    }

                    break;
                default:
                    break;
            }    //switch
        } //handleMessage

    }; //handler

    //通信時に必ずやること
    private int getFirmwareProtocol(String version) {

        String major = "0";
        int positionOfDot;

        positionOfDot = version.indexOf('.');
        if (positionOfDot != -1) {
            major = version.substring(0, positionOfDot);
        }

        return new Integer(major).intValue();
    }

    //速度を0に
    public void ZeroSpeedSet() {
        params.setSpeed((float) 0);
        mp.setPlaybackParams(params);
        tSpeed.setText(0.00+"");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("VideoPlay Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    /*ボタンタップや画面タップした時の処理*/

    //Connectボタンを押したときの処理
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonYes:
                if (!connectFlg) {
                    //mStatusTextView.setText("try connect");
                    mThread = new Thread(this);
                    // Threadを起動し、Bluetooth接続
                    isRunning = true;
                    mThread.start();
                }
                break;

            case R.id.buttonNo:
                Log.d("No", "no");
                findViewById(R.id.ConnectCheak).setVisibility(View.INVISIBLE);
                StartDialog();
                tHeartbeat.setText("- ");
                break;
        }
    }

    //動画再生中に画面をタップされたときの処理(Pause)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("", "ACTION_DOWN");
                if (findViewById(R.id.ConnectCheak).getVisibility() == View.INVISIBLE) {
                    //トレーニングが始まっているときのみ画面タップでポーズする
                    PauseDialog();
                }
                break;
        }
        return true;
    }

    //スタート時のやつ
    public void StartDialog() {
        // ポップアップメニュー表示
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
        alertDialog.setTitle("トレーニング開始");
        alertDialog.setMessage("");
        alertDialog.setPositiveButton("スタート", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PlayProcess();
            }
        });
        final AlertDialog myDialog = alertDialog.create();
        myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
        myDialog.show();
    }

    //Playボタンを押したときの処理の中身
    public void PlayProcess() {
        speedCount = 0.00;
        tSpeed.setText(String.format("%.2f", (float) (speedCount * 10)));
        mp.setPlaybackParams(params);
        mp.seekTo(0);

        timerscheduler = Executors.newSingleThreadScheduledExecutor();
        future = timerscheduler.scheduleAtFixedRate(myTimerTask, 0, 100, TimeUnit.MILLISECONDS);
        seekbarscheduler = Executors.newSingleThreadScheduledExecutor();
        seekbarfuture = seekbarscheduler.scheduleAtFixedRate(mySeekBarTask, 0, 1000, TimeUnit.MILLISECONDS);
        Thread StartBGM = new Thread(new StartBGM());
        StartBGM.start();
    }

    //Pauseボタンを押したときの処理の中身
    public void PauseDialog() {
        usb_Flg = true;
        future.cancel(true);//タイマー一時停止
        seekbarfuture.cancel(true);
        speed_Value = 0;
        all_speed_Value = 0;
        speedCount = 0.00;
        params.setSpeed((float) speedCount);
        mp.setPlaybackParams(params);
        tSpeed.setText(String.format("%.2f", (float) (speedCount * 10)));
        // ポップアップメニュー表示
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
        alertDialog.setTitle("ポーズ");
        alertDialog.setMessage("一時停止中です");
        alertDialog.setPositiveButton("トレーニングコースに戻る", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timerscheduler.shutdown();//タイマー終了
                seekbarscheduler.shutdown();//タイマー終了
                if (mp != null) {
                    mp.release();
                    mp = null;
                }
                accessoryManager.disable(getApplication());//#
                disconnectAccessory();//#
                Thread StopBGM = new Thread(new StopBGM());
                StopBGM.start();
                Intent intent = new Intent(getApplication(), VideoSelect.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("走行に戻る", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                future = timerscheduler.scheduleAtFixedRate(myTimerTask, 0, 100, TimeUnit.MILLISECONDS);//タイマーを動かす
                seekbarfuture = seekbarscheduler.scheduleAtFixedRate(mySeekBarTask, 0, 1000, TimeUnit.MILLISECONDS);
                usb_Flg = false;
            }
        });
        alertDialog.setNeutralButton("リザルトに行く", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timerscheduler.shutdown();//タイマー終了
                seekbarscheduler.shutdown();//タイマー終了
                if (mp != null) {
                    mp.release();
                    mp = null;
                }
                accessoryManager.disable(getApplication());//!
                disconnectAccessory();//!
                ResultProcess();
            }
        });
        AlertDialog myDialog = alertDialog.create();
        myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
        myDialog.show();
    }

    public void ResultDialog() {
        Toast.makeText(this, "ResultDialog", Toast.LENGTH_LONG).show();
        // ポップアップメニュー表示
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
        alertDialog.setTitle("トレーニング終了");
        alertDialog.setMessage("結果を確認しましょう。");
        alertDialog.setNeutralButton("リザルトに行く", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResultProcess();
            }
        });
        AlertDialog myDialog = alertDialog.create();
        myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
        myDialog.show();
    }
    //Resultボタンを押したときの処理の中身
    public void ResultProcess() {
        Thread StopBGM = new Thread(new StopBGM());
        StopBGM.start();
        globals.coursename = tCourse.getText().toString();//コース名
        globals.mileage = String.valueOf(totalMileage);//走行距離
        globals.maxheartbeat = String.valueOf(maxHeartbeat);//最大心拍
        globals.avg = String.valueOf(AverageSpeed(totalSpeed, totalSpeedCnt));//平均速度
        globals.max = String.format("%.2f", maxSpeed);//最高速度
        globals.time = tTimer.getText().toString();//運動時間
        globals.cal = Double.parseDouble(String.format("%.2f",cal));
        Intent intent = new Intent(getApplication(), Result.class);
        startActivity(intent);
    }



    /*非同期処理関連*/
    //カウントアップタイマタスク
    public class CntTimerTask implements Runnable {
        //private Handler handler = new Handler();
        private long timerCount = 0;

        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //100msecごとに定期実行される
                    if (timerCount <= 35999900) {//99時間59分59秒
                        timerCount++;
                    }
                    long hh = timerCount * 100 / 1000 / 3600;//時
                    long mm = timerCount * 100 / 1000 / 60 % 60;//分
                    long ss = timerCount * 100 / 1000 % 60;//秒
                    long ms = (timerCount * 100 - ss * 1000 - mm * 1000 * 60 - hh * 1000 * 3600) / 100;//ミリ秒
                    globals.hh = hh;
                    globals.mm = mm;
                    globals.ss = ss;
                    globals.ms = ms;
                    // 桁数を合わせるために02d(2桁)を設定
                    tTimer.setText(String.format("%1$02d:%2$02d:%3$02d.%4$01d", hh, mm, ss, ms));

                    MaxSpeed maxSpeed = new MaxSpeed(max_speed_Value);
                    maxSpeed.run();
                    if (Gear1_Flg == true) {
                        cal += 3.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("1");
                    }
                    if (Gear2_Flg == true) {
                        cal += 4.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("2");
                    }
                    if (Gear3_Flg == true) {
                        cal += 5.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("3");
                    }
                    if (Gear4_Flg == true) {
                        cal += 6.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("4");
                    }
                    if (Gear5_Flg == true) {
                        cal += 7.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("5");
                    }
                    if (Gear6_Flg == true) {
                        cal += 8.8 * weight * ((float) 1 / 36000) * 1.05 * ((float) max_speed_Value / 20);
                        tCal.setText(String.format("%.2f",cal));
                        tGear.setText("6");
                    }
                }
            });
        }
    }

    //シークバータスク
    public class SeekBarTask implements Runnable {
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Thread MoveGhost = new Thread(new MoveGhostTask(PerSecond(psKilometers, psSeconds)));
                    MoveGhost.start();
                    Thread MoveMe = new Thread(new MoveMeTask());
                    MoveMe.start();
                    Thread MileageTask = new Thread(new MileageTask((double)mp.getDuration(), (double)mp.getCurrentPosition(), totalMileage));
                    MileageTask.start();
                }
            });
        }
    }

    //自機の移動
    public class MoveMeTask implements Runnable {
        final int startPoint = 545;//スタート地点の座標
        final int endPoint = 45;//エンド地点の座標
        //meとghostのMarginTopの値を入れてください↑
        final int barDistance = startPoint - endPoint;//545-45=500

        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    float getPlayTime = ((float) mp.getCurrentPosition() / (float) mp.getDuration()) * barDistance;//barのpx数
                    getPlayTime = barDistance - getPlayTime;
                    getPlayTime = getPlayTime + endPoint;//画像レイアウトの高さの都合上MarginTop=0はゴール地点ではないので調整しなくてはいけない　
                    imageMe.setY(getPlayTime);

                }
            });
        }
    }

    /*ゴースト関連*/
    //前回の測定結果を変数に入れたりするメソッド　
    public void GetLastTrainingData() {//ここの処理は何か気持ち悪いからあとで書き直したい
        //前回の走行データを色々するところ↓
        if (globals.total_mileage == null) {
            //前回のデータがないときはこっち
            findViewById(R.id.image_view_ghost).setVisibility(View.INVISIBLE);
            Log.v("globals.total_mileage", "null");
        } else {
            psKilometers = Double.parseDouble(globals.total_mileage);
            findViewById(R.id.image_view_ghost).setVisibility(View.VISIBLE);
            Log.v("globals.total_mileage", globals.total_mileage);
        }
        if (globals.total_time == null) {
            //前回のデータがないときはこっち
            Log.v("globals.total_time", "null");
        } else {
            //秒速を求めるために時分秒を秒に変換
            int hours = Integer.parseInt(globals.total_time.substring(0, 2));
            int minutes = Integer.parseInt(globals.total_time.substring(3, 5));
            double seconds = Double.parseDouble(globals.total_time.substring(6));
            double totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
            psSeconds = totalSeconds;
            Log.v("globals.total_time", String.valueOf(totalSeconds));
        }
    }

    //秒速の計算
    public double PerSecond(double kilometers, double seconds) {
        int meters = 0;
        double perSeconds = 0.0;
        meters = (int) (kilometers * 1000);
        perSeconds = meters / seconds;
        perSeconds = (perSeconds / meters);
        return perSeconds;
    }

    //ゴースト用のタスク
    public class MoveGhostTask implements Runnable {
        final int startPoint = 545;//スタート地点の座標
        final int endPoint = 45;//エンド地点の座標
        double perSeconds;
        double ghostPos = imageGhost.getY();

        MoveGhostTask(double perSeconds) {
            this.perSeconds = perSeconds;
        }

        final int barDistance = startPoint - endPoint;//545-45=500

        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ghostPos > endPoint) {
                        float setGhostPosition = (float) ghostPos - ((float) perSeconds * barDistance);
                        imageGhost.setY(setGhostPosition);
                    }
                }
            });
        }
    }

    //走行距離タスク
    public class MileageTask implements Runnable {
        private double duration = 0.0;
        private double currentPosition = 0.0;
        private double totalMileage = 0.0;
        private double Mileage = 0.0;
        public MileageTask(double getDuration, double getCurrentPosition, double totalMileage){
            this.duration = getDuration;
            this.currentPosition = getCurrentPosition;
            this.totalMileage = totalMileage;
        }
        public void run() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //走行距離表示↓
                    Mileage = totalMileage / (duration / currentPosition);
                    tMileage.setText(String.format("%.2f",Mileage));
                }
            });
        }
    }

    //スピードメータータスク
    public class SpeedMeterTask implements Runnable {
        private float taskSpeedCount = (float) 0.0;

        public SpeedMeterTask(float taskSpeedCount) {
            this.taskSpeedCount = taskSpeedCount;
        }

        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mp != null) {
                    /*動画の再生速度を変えるのに必要なプログラム↓*/
                        params.setSpeed(taskSpeedCount);//再生速度変更
                        mp.setPlaybackParams(params);
                        //mp.start();
                        tSpeed.setText(String.format("%.1f", (float) (taskSpeedCount * 10)));
                        totalSpeed = totalSpeed + (taskSpeedCount * 10);
                        totalSpeedCnt++;
                        //最高速度の判断
                        if ((taskSpeedCount * 10) > maxSpeed) {
                            maxSpeed = (taskSpeedCount * 10);
                        }
                    }
                }
            });
        }
    }

    //BGM
    public class StartBGM implements Runnable {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mpBGM == null) {
                        mpBGM = MediaPlayer.create(getApplicationContext(), R.raw.zangyousenshi);
                        mpBGM.start();
                    }
                }
            });
        }
    }

    public class StopBGM implements Runnable {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("stopBGM", "STOP");
                    mpBGM.stop();
                    mpBGM.reset();
                    mpBGM.release();
                    mpBGM = null;
                }
            });
        }
    }

    //フォントを7セグにする
    public void Change7Seg() {
        /*7セグ表示にする処理*/
        // フォントを取得
        Typeface tf = Typeface.createFromAsset(getAssets(), "dseg7classic-bold.ttf");//7セグフォント
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "digitalword.ttf");//7セグフォント
        /*タイマー*/
        tTimer.setTypeface(tf);
        tTimer.setTextSize(32.0f);
        tTimer.setPadding(0, 0, 0, 15);

         /*走行距離*/
        tCal.setTypeface(tf);
        tCal.setTextSize(45.0f);
        tCal.setPadding(0, 0, 5, 0);
        tCAL.setTypeface(tf2);
        tCAL.setTextSize(25.0f);
        tCAL.setPadding(0, 0, 10, 7);

        /*走行距離*/
        tMileage.setTypeface(tf);
        tMileage.setTextSize(45.0f);
        tMileage.setPadding(0, 0, 5, 0);
        tKM.setTypeface(tf2);
        tKM.setTextSize(25.0f);
        tKM.setPadding(0, 0, 10, 7);

        /*スピード*/
        tSpeed.setTypeface(tf);
        tSpeed.setTextSize(45.0f);
        tSpeed.setPadding(0, 0, 5, 0);
        tKPH.setTypeface(tf2);
        tKPH.setTextSize(25.0f);
        tKPH.setPadding(0, 0, 10, 7);

        /*ターゲット心拍*/
        tTargetHeartbeat.setTypeface(tf);
        tTargetHeartbeat.setTextSize(45.0f);
        tTargetHeartbeat.setPadding(0, 0, 5, 0);
        tTargetBPM.setTypeface(tf2);
        tTargetBPM.setTextSize(25.0f);
        tTargetBPM.setPadding(0, 0, 10, 7);

        /*心拍*/
        tHeartbeat.setTypeface(tf);
        tHeartbeat.setTextSize(45.0f);
        tHeartbeat.setPadding(0, 0, 5, 0);
        tBPM.setTypeface(tf2);
        tBPM.setTextSize(25.0f);
        tBPM.setPadding(0, 0, 10, 7);
    }

    //目標心拍を計算する
    public int TargetBPM(int age) {
        int targetBPM;
        targetBPM = (int) ((220 - age) * 0.6);
        return targetBPM;
    }

    //平均速度を計算する
    public String AverageSpeed2(double totalMileage, String time) {
        Log.v("aaaaaaaaaaaaTIME", time);
        double hours = Double.parseDouble(time.substring(0, 2));
        double minutes = Double.parseDouble(time.substring(3, 5));
        double seconds = Double.parseDouble(time.substring(6));
        double totalHours = hours + (minutes / 60) + (seconds / 3600);
        Log.v("mileage", String.valueOf(totalMileage));
        Log.v("HOURS", String.valueOf(hours));
        Log.v("MINUTES", String.valueOf(minutes));
        Log.v("SECONDS", String.valueOf(seconds));
        Log.v("TOTAL", String.valueOf(totalHours));
        String avg = String.format("%.2f", (totalMileage / totalHours));
        return avg;
    }

    //平均速度を計算する(ボリュームで速度調整するとき用)
    public String AverageSpeed(double totalSpeed, int totalSpeedCnt) {
        Log.v("TotalSpeed", String.valueOf(totalSpeed));
        Log.v("TotalSpeedCnt", String.valueOf(totalSpeedCnt));
        double averageSpeed;
        averageSpeed = totalSpeed / totalSpeedCnt;
        String avg = String.format("%.2f", averageSpeed);
        return avg;
    }

    //画面消すタスク
    public class ConnectCheck implements Runnable {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.ConnectCheak).setVisibility(View.INVISIBLE);
                    StartDialog();
                }
            });
        }
    }
    //最大心拍タスク
    public class Maxheartbeat implements Runnable {
        private int inputHeartbeat = 0;
        public Maxheartbeat(int inputHeartbeat){
            this.inputHeartbeat = inputHeartbeat;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(inputHeartbeat > maxHeartbeat){
                        maxHeartbeat = inputHeartbeat;
                    }
                }
            });
        }
    }
    //最大速度タスク
    public class MaxSpeed implements Runnable {
        private double speed = 0.0;
        public MaxSpeed(double speed) {
            this.speed = speed;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //最高速度の判断
                    if (speed > maxSpeed) {
                        maxSpeed = speed;
                    }
                }
            });
        }
    }

    //ボリュームキーの操作(完成版はここで速度変更はできなくする)//菅原mp!=nullいれた
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                if (mp != null) {
                    if (speedCount < 0.1) {
                        speedCount = speedCount + 0.1;
                    } else if (speedCount < 5) {
                        speedCount = speedCount + 0.01;
                    } else if (speedCount >= 5) {
                        //意味わからないほど早くされるとクラッシュする対策
                        speedCount = 5.00;
                    }
                    Thread SpeedUp = new Thread(new SpeedMeterTask((float) speedCount));
                    SpeedUp.start();
                }
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                if (mp != null) {
                    if (speedCount < 0.1) {
                        speedCount = 0.00;
                    } else if (speedCount <= 0.1) {
                        speedCount = speedCount - 0.1;
                    } else if (speedCount >= 0.1) {
                        speedCount = speedCount - 0.01;
                    }
                    Thread SpeedDown = new Thread(new SpeedMeterTask((float) speedCount));
                    SpeedDown.start();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    //戻るキーを無効にする
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}