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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.example.a1521315.test02.R.id.ConnectCheak;
import static com.example.a1521315.test02.R.id.buttonPlay;


public class VideoPlay extends Activity implements SurfaceHolder.Callback, Runnable, MediaPlayer.OnCompletionListener,View.OnClickListener {

    Globals globals;
    /*メーター関連の関数*/
    double speedMeterAngle      =-158;  //速度メーター0の位置
    double heartbeatMeterAngle  =-158;  //心拍メーター0の位置

    TextView tSpeedDec;//時速の変数　少数
    TextView tSpeedInt;//時速の変数　整数
    TextView tMileageDec;//走行距離の変数　小数
    TextView tMileageInt;//走行距離の変数　整数
    TextView tHeartbeat;//心拍の変数
    TextView tTimer;//タイマーの変数
    TextView tCourse;//コース名

    /*デバッグ用の関数*/
    TextView tSpeed;//時速の変数
    TextView tMileage;//走行距離の変数
    TextView tDebug1;
    TextView tDebug2;

    int raw = 0;//rawファイルかどうかを判断する変数。0=内部ストレージ　1=rawファイル
    String mediaPath = null;//動画データ
    private ImageView imageMe;//自機イメージ用の変数
    double TotalMileage = 0;//総走行距離用,選択されたコースごとに変わる
    double speedcount = 0.0;//速度用

    Bitmap bitmapSpeedMeterNeedle;//bitmap形式にして針を回すので必要
    ImageView imageSpeedMeterNeedle;//針用のimageView
    Bitmap bitmapHeartbeatMeterNeedle;//bitmap形式にして針を回すので必要
    ImageView imageHeartbeatMeterNeedle;//針用のimageView

    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    private MediaPlayer mp = null;

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
    public static final int RESISTANCE_LEVEL = 4;
    public static final int ALERT_MORTER = 5;
    public static final int APP_CONNECT = (int) 0xFE;
    public static final int APP_DISCONNECT = (int) 0xFF;
    private boolean deviceAttached = false;
    private int firmwareProtocol = 0;

    //センサー、動画再生関連の変数　初期化
    double speed_Value = 0.0;
    double dist_Value = 0.0;
    double my_dist_Value = 0.0;
    public float plus_dist_Value = 0.0005F;//0.0015F

    double old_dist_Value = 0.0;
    public float resist_Level = (float)1.0;//負荷のレベルによる係数
    NumberFormat format1 = NumberFormat.getInstance();
    NumberFormat format2 = NumberFormat.getInstance();

    public int hole_Value = 0;
    public int old_hole_Value = 0;

    public boolean run_Flg = false;
    public boolean usb_Flg = false;
    public boolean chSpd_Flg = false;
    public boolean clear_Flg = false;
    public int null_Cnt = 0;

    long my_mm = 0;
    long my_ss = 0;

    public Timer watchMeTimer;
    public WatchMeTask watchMeTask;

    public int sensor_value = 0;
    private enum ErrorMessageCode {
        ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING,
        ERROR_FIRMWARE_PROTOCOL
    };
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
    private TextView mStatusTextView;/** ステータス. */
    private TextView mInputTextView;/** Bluetoothから受信した値. */
    private static final int VIEW_STATUS = 0; /** Action(ステータス表示). */
    private static final int VIEW_INPUT = 1;/** Action(取得文字列). */
    /** BluetoothのOutputStream. */
    OutputStream mmOutputStream = null;
    private boolean connectFlg = false;
    //**********************************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.addCallback(this);

        globals = (Globals)this.getApplication();

        tSpeedDec = (TextView) findViewById(R.id.textSpeedDec);
        tSpeedDec.setText(".0");
        tSpeedInt = (TextView) findViewById(R.id.textSpeedInt);
        tSpeedInt.setText("0");
        tMileageDec = (TextView) findViewById(R.id.textMileageDec);
        tMileageDec.setText(".00");
        tMileageInt = (TextView) findViewById(R.id.textMileageInt);
        tMileageInt.setText("0");
        tHeartbeat = (TextView) findViewById(R.id.textHeartbeat);
        tHeartbeat.setText("000");
        tTimer = (TextView) findViewById(R.id.textTimer);
        tTimer.setText("00:00:00.0");

        /*デバッグ用のやつ*/
        tMileage = (TextView) findViewById(R.id.textMileage);
        tMileage.setText("0.00");
        tSpeed = (TextView) findViewById(R.id.textSpeed);
        tSpeed.setText("0.0");
        tDebug1 = (TextView) findViewById(R.id.textDebug1);
        tDebug1.setText("デバッグ用テキスト1");
        tDebug2 = (TextView) findViewById(R.id.textDebug2);
        tDebug2.setText("デバッグ用テキスト2");

        /*シークバーに関する奴*/
        imageMe = (ImageView)findViewById(R.id.image_view_me);
        imageMe.setImageResource(R.drawable.me);
        ImageView imageView1 = (ImageView)findViewById(R.id.image_view_bar);
        imageView1.setImageResource(R.drawable.bar0);

        /*メーター*/
        /*速度メーター*/
        ImageView imageSpeedMeter = (ImageView)findViewById(R.id.image_SpeedMeter);
        imageSpeedMeter.setImageResource(R.drawable.meter0);
        imageSpeedMeterNeedle = (ImageView)findViewById(R.id.image_Hari1);
        bitmapSpeedMeterNeedle = BitmapFactory.decodeResource(getResources(), R.drawable.hari4_45);
        imageSpeedMeterNeedle.setImageBitmap(bitmapSpeedMeterNeedle);
        /*心拍メーター*/
        ImageView imageHeartBeatMeter = (ImageView)findViewById(R.id.image_HeartBeatMeter);
        imageHeartBeatMeter.setImageResource(R.drawable.heartbeatmeter2);
        imageHeartbeatMeterNeedle = (ImageView)findViewById(R.id.image_Hari2);
        bitmapHeartbeatMeterNeedle = BitmapFactory.decodeResource(getResources(), R.drawable.hari4_45);
        imageHeartbeatMeterNeedle.setImageBitmap(bitmapHeartbeatMeterNeedle);

        ImageView timeDisplay = (ImageView)findViewById(R.id.image_TimeDisplay);
        timeDisplay.setImageResource(R.drawable.time);

        Thread SetBothNeedlesToZero = new  Thread(new BothNeedlesToZero(-158));//2つの針を0に戻す
        SetBothNeedlesToZero.start();

        //ボタン押したときメソッドの宣言
        findViewById(buttonPlay).setOnClickListener(PlayClickListener);
        findViewById(R.id.buttonResult).setOnClickListener(ResultClickListener);

        //コース番号受け取り
        Intent i = getIntent();
        String CourseNum = i.getStringExtra("course");
        tCourse = (TextView)findViewById(R.id.textCourse);
        tCourse.setText("コース"+CourseNum);
        if(CourseNum.equals("0")) {
            tCourse.setText("ポリテク→大地");
            mediaPath = "/test02.mp4";//実機9のストレージにあるファルを指定
            TotalMileage = 10.4;
            raw = 0;
        }else if(CourseNum.equals("1")) {
            tCourse.setText("東京→御殿場");
            mediaPath = "/test_x264.mp4";//実機9のストレージにあるファイルを指定
            TotalMileage = 83.7;
            raw = 0;
        }else if(CourseNum.equals("2")) {
            tCourse.setText("鳴子");
            mediaPath = "/_naruko.mp4";//実機9のストレージにあるファイルを指定
            TotalMileage = 1.3;
            raw = 0;
        }else if(CourseNum.equals("3")) {
            tCourse.setText("デバッグ用");
            mediaPath = "android.resource://" + getPackageName() + "/" + R.raw.test01;//rawフォルダから指定する場合
            TotalMileage = 10.0;
            raw = 1;
        }

        //USBAccessoryManager の初期化
        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            Log.d(TAG, "Info:" + info.packageName + "\n" + info.versionCode + "\n" + info.versionName);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);

        //bluetooth*********************************************************************************
        mInputTextView = (TextView)findViewById(R.id.textHeartbeat);
        mStatusTextView = (TextView)findViewById(R.id.textConnectStatus);
        // Bluetoothのデバイス名を取得
        // デバイス名は、RNBT-XXXXになるため、
        // DVICE_NAMEでデバイス名を定義
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mStatusTextView.setText("SearchDevice");
        Set< BluetoothDevice > devices = mAdapter.getBondedDevices();
        for ( BluetoothDevice device : devices){
            if(device.getName().equals(DEVICE_NAME)){
                mStatusTextView.setText("find: " + device.getName());
                mDevice = device;
            }
        }
        //******************************************************************************************
    }//onCreateここまで



    // 再生完了時の処理
    @Override
    public void onCompletion(MediaPlayer agr0) {
        Log.v("MediaPlayer", "onCompletion");
        //USB通信の切断(停止がないため)
        accessoryManager.disable(this);
        //disconnectAccessory();//################################       あやしいかもよ～
        ////Button BtnPauseView2 = (Button) findViewById(R.id.buttonPause);
        ////BtnPauseView2.setVisibility(View.INVISIBLE);
        timerscheduler.shutdown();//タイマー止める
        seekbarscheduler.shutdown();
        //リザルトボタンを表示
        Button BtnResultView = (Button) findViewById(R.id.buttonResult);
        BtnResultView.setVisibility(View.VISIBLE);
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

    //Activity の　基本ライフサイクル
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        accessoryManager.enable(VideoPlay.this, getIntent());
    }

    @Override
    public void onStop() {
        super.onStop();
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
        accessoryManager.disable(this);
        disconnectAccessory();

        //bluetooth***********
        isRunning = false;
        try{
            mSocket.close();
        }
        catch(Exception e){}
        //*************************
    }

    // Resets the demo application when a device detaches
    public void disconnectAccessory() {
        Log.d(TAG, "disconnectAccessory()");
        Toast.makeText(this, "DISCONNECT SUCCESS", Toast.LENGTH_LONG).show();

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

            if(raw==1){
                //動画ファイルをMediaPlayerに読み込ませる
                mp.setDataSource(getApplicationContext(), Uri.parse(mediaPath));//rawフォルダから指定する場合
            }else{
                File pathExternalPublicDir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
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

        try{

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

            while(isRunning){

                // InputStreamの読み込み
                bytes = mmInStream.read(buffer);
                Log.i(TAG,"bytes="+bytes);
                // String型に変換
                String readMsg = new String(buffer, 0, bytes);

                // null以外なら表示
                if(readMsg.trim() != null && !readMsg.trim().equals("")){
                    Log.i(TAG,"value="+readMsg.trim());

                    valueMsg = new Message();
                    valueMsg.what = VIEW_INPUT;
                    valueMsg.obj = readMsg;
                    mHandler.sendMessage(valueMsg);
                }
                else{
                    // Log.i(TAG,"value=nodata");
                }
                //Thread.sleep(10);
            }
        }catch(Exception e){

            valueMsg = new Message();
            valueMsg.what = VIEW_STATUS;
            //valueMsg.obj = "Error1:" + e;
            valueMsg.obj = "not connected.";
            mHandler.sendMessage(valueMsg);

            try{
                mSocket.close();
            }catch(Exception ee){}
            isRunning = false;
        }
    }

    /**
     * 描画処理はHandlerでおこなう
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int action = msg.what;
            String msgStr = (String)msg.obj;
            if(action == VIEW_INPUT){
                mInputTextView.setText(msgStr);
            }
            else if(action == VIEW_STATUS){
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

                    /* LED初期化 */
                    /*commandPacket[0] = UPDATE_LED_SETTING;
                    commandPacket[1] = 0;
                    accessoryManager.write(commandPacket);
                    */
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
                                        if( usb_Flg == true ) {
                                            speed_Value = 0.0;
                                        }

                                        else
                                        {
                                            //時間の取得
                                            float time_tmp = (float)((my_mm*60) + my_ss);

                                            //センサー値取得
                                            hole_Value = (int) (commandPacket[1] & 0xFF);

                                            //距離の加算(径分)
                                            if(hole_Value == 1 && old_hole_Value == 0 && run_Flg == false) {
                                                dist_Value += plus_dist_Value;
                                                my_dist_Value += plus_dist_Value;
                                                run_Flg = true;
                                                chSpd_Flg = true;
                                                clear_Flg = false;//
                                                null_Cnt = 0;
                                            }

                                            //距離の加算(径分)
                                            else if(hole_Value == 0 && old_hole_Value == 1 && run_Flg == true){
                                                dist_Value += plus_dist_Value;
                                                my_dist_Value += plus_dist_Value;
                                                run_Flg = false;
                                                chSpd_Flg = true;
                                                clear_Flg = false;//
                                                null_Cnt = 0;
                                            }
                                            //一定値以上の場合、停止したとみなす①
                                            else{
                                                null_Cnt++;
                                            }

                                            //一定値以上の場合、停止したとみなす②(cnt上限は適当)
                                            if(null_Cnt >= 1400){
                                                null_Cnt = 1500;
                                                my_dist_Value = 0.0;
                                                chSpd_Flg = false;
                                                clear_Flg = true;
                                            }

                                            //時間の取得
                                            //float time_tmp = (float)((mm*60) + ss);

                                            //距離が進んだ場合
                                            if(chSpd_Flg == true){

                                                //0秒時の処理
                                                if(time_tmp <= 0){
                                                    speed_Value = 1.8;//plus_dist_Value / (1 / 3600);//my_distを使いたい
                                                }
                                                else{
                                                    //加算された距離とタイマーの時間で、時速割り出し(現在の時速)
                                                    speed_Value = my_dist_Value / (time_tmp / 3600);//my_distを使いたい
                                                }
                                            }
                                            else{
                                                //speed_Value = 0.0;
                                            }

                                            if(my_dist_Value == 0){
                                                speed_Value = 0.0;
                                            }

                                        }

                                        //初回時のInfinity回避
                                             /*   if(Double.isNaN(speed_Value) || Double.isInfinite(speed_Value)){
                                                    speed_Value = 0.0;
                                                    dist_Value = 0.0;
                                                    my_dist_Value = 0.0;
                                                }
                                                */

                                        //各テキストに値を反映
                                        tHeartbeat.setText("debug_value:"+String.format("%.4f",my_dist_Value));//my_dist_Value
                                        //tSpeed.setText(format2.format(speed_Value) + "km/h");
                                        //tMileage.setText(String.format("%.4f",dist_Value)+ "km");
                                        tDebug1.setText(format2.format(speed_Value) + "km/h");
                                        tDebug2.setText(String.format("%.4f",dist_Value)+ "km");

                                        //メディアプレイヤーの再生速度を設定
                                        if(speed_Value <= 50 && speed_Value >= 1){
                                            params.setSpeed( (float) ( speed_Value / 10 ) );//再生速度変更
                                            mp.setPlaybackParams(params);
                                        }
                                        //0速度
                                        else{
                                            params.setSpeed((float)0);
                                            mp.setPlaybackParams(params);
                                        }

                                        //過去の値を更新
                                        old_hole_Value = hole_Value;
                                        old_dist_Value = dist_Value;
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

    //プレイヤーが止まらずに進んだ時間(止まるたびにリセット)
    public class WatchMeTask extends TimerTask {

        long t_cnt = 0;

        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    t_cnt++;
                    my_mm = t_cnt * 100 / 1000 / 60;
                    my_ss = t_cnt * 100 / 1000 % 60;
                    if(clear_Flg == true){
                        t_cnt = 0;
                    }
                }
            });
        }
    }

    //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    /*ボタンタップや画面タップした時の処理*/
    //Playボタンを押したときの処理
    View.OnClickListener PlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayProcess();
        }
    };

    //Resultボタンを押したときの処理
    View.OnClickListener ResultClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ResultProcess();
        }
    };

    //Pauseボタンを押したときの処理
    View.OnClickListener PauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PauseProcess();
        }
    };

    //Connectボタンを押したときの処理
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
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
                Log.d("No","no");
                findViewById(R.id.ConnectCheak).setVisibility(View.INVISIBLE);
                findViewById(buttonPlay).setVisibility(View.VISIBLE);
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
                if( findViewById(buttonPlay).getVisibility() == View.INVISIBLE &&  findViewById(ConnectCheak).getVisibility() == View.INVISIBLE) {
                    //トレーニングが始まっているときのみ画面タップでポーズする
                    PauseProcess();
                }
                break;
        }
        return true;
    }

    /*処理の中身*/
    //Playボタンを押したときの処理の中身
    public void PlayProcess(){
        speedMeterAngle = -158;
        heartbeatMeterAngle = -158;
        Thread SetBothNeedlesToZero = new  Thread(new BothNeedlesToZero(-158));//2つの針を0に戻す
        SetBothNeedlesToZero.start();
        findViewById(buttonPlay).setVisibility(View.INVISIBLE);//PLAYボタンを押したらPLAYボタンを消す
        //findViewById(R.id.buttonPause).setVisibility(View.VISIBLE);//PLAYボタンを押したらPAUSEボタンを出す
        speedcount = 0.0;
        tSpeed.setText(String.format("%.1f", (float) (speedcount*10)));
        tSpeedInt.setText("0");
        tSpeedDec.setText(".0");
        mp.setPlaybackParams(params);
        mp.seekTo(0);

        if (null != watchMeTimer) {
            watchMeTimer.cancel();
            watchMeTimer = null;
        }

        watchMeTimer = new Timer();//Timerインスタンスを生成
        watchMeTask = new WatchMeTask();//TimerTaskインスタンスを生成
        watchMeTimer.schedule(watchMeTask,0,100);

        timerscheduler = Executors.newSingleThreadScheduledExecutor();
        future = timerscheduler.scheduleAtFixedRate(myTimerTask, 0, 100, TimeUnit.MILLISECONDS);
        seekbarscheduler = Executors.newSingleThreadScheduledExecutor();
        seekbarfuture = seekbarscheduler.scheduleAtFixedRate(mySeekBarTask, 0, 1000, TimeUnit.MILLISECONDS);
    }

    //Pauseボタンを押したときの処理の中身
    public void PauseProcess(){
        speedMeterAngle = -158;
        heartbeatMeterAngle = -158;
        Thread SetNeedletoZero = new Thread(new SpeedMeterNeedle(speedMeterAngle));
        SetNeedletoZero.start();
        Thread setHeartbeatToZero = new Thread(new HeartbeatMeterNeedle(heartbeatMeterAngle));
        setHeartbeatToZero.start();

        usb_Flg = true;
        future.cancel(true);//タイマー一時停止
        seekbarfuture.cancel(true);
        speedcount = 0;
        params.setSpeed((float) speedcount);
        mp.setPlaybackParams(params);
        tSpeed.setText(String.format("%.1f", (float) (speedcount*10)));
        tSpeedInt.setText("0");
        tSpeedDec.setText(".0");

        // ポップアップメニュー表示
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
        alertDialog.setTitle("ポーズ");
        alertDialog.setMessage("一時停止中です");
        alertDialog.setPositiveButton("走行をやめてコース選択に戻る", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //VideoSelectに戻る処理
                timerscheduler.shutdown();//タイマー終了
                seekbarscheduler.shutdown();//タイマー終了
                if (mp != null) {
                    mp.release();
                    mp = null;
                }
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
                ResultProcess();
            }
        });
        AlertDialog myDialog = alertDialog.create();
        myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
        myDialog.show();
    }

    //Resultボタンを押したときの処理の中身
    public void ResultProcess(){
        globals.coursename = tCourse.getText().toString();//コース名
        globals.mileage = tMileage.getText().toString();//走行距離
        globals.maxheartbeat = tHeartbeat.getText().toString();//最大心拍(現在は心拍数を代入しているので実際最大心拍を取得する処理を書いてから代入する)
        globals.avg = tSpeed.getText().toString();//平均速度(これも計算する処理が必要)
        globals.max = tSpeed.getText().toString();//最高速度(これも同じ)
        globals.time = tTimer.getText().toString();//運動時間
        //int iWeight = Integer.parseInt(globals.weight);
        //globals.cal = (8.4 * Double.valueOf(globals.time) * iWeight);//カロリー計算
        globals.cal = 123.32;
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
                    if(timerCount <= 35999900) {//99時間59分59秒
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

                    //Thread MoveMe = new Thread(new MoveMeTask());
                    //MoveMe.start();
                    //Thread TestMileageTask = new Thread(new MileageTask());
                    //TestMileageTask.start();


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
                    Thread MoveMe = new Thread(new MoveMeTask());
                    MoveMe.start();
                    Thread TestMileageTask = new Thread(new MileageTask());
                    TestMileageTask.start();
                }
            });
        }
    }

    //自機の移動
    public class MoveMeTask implements Runnable {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //再生終了時に死ぬ原因がここにあります
                    //おいおい直す
                    float getPlayTime = ((float)mp.getCurrentPosition() / (float)mp.getDuration()) * 480;//barのpx数
                    getPlayTime = 450 - getPlayTime;
                    getPlayTime = getPlayTime + 45;
                    imageMe.setY(getPlayTime);

                }
            });
        }
    }

    //走行距離タスク
    public class MileageTask implements Runnable {
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //走行距離表示↓
                    double f3 = TotalMileage / ( (double)mp.getDuration() / (double)mp.getCurrentPosition());
                    tMileage.setText(String.format("%.2f",f3));
                    if(f3 < 10.00) {
                        //0.00~9.99までの処理
                        tMileageInt.setText(String.format("%.2f",f3).substring(0,1));
                        tMileageDec.setText(String.format("%.2f",f3).substring(1,4));
                    }else if(f3 < 100.00){
                        //10.00~99.99までの処理
                        tMileageInt.setText(String.format("%.1f", (float) (speedcount * 10)).substring(0, 2));
                        tMileageDec.setText(String.format("%.1f", (float) (speedcount * 10)).substring(2, 5));
                    }else{
                        //110.00~999.99までの処理
                        tMileageInt.setText(String.format("%.1f", (float) (speedcount * 10)).substring(0,3));
                        tMileageDec.setText(String.format("%.1f", (float) (speedcount * 10)).substring(3, 6));
                    }
                }
            });
        }
    }

    //スピードメータータスク
    public class SpeedMeterTask implements Runnable {
        private float pSpeedCount = (float) 0.0;
        public SpeedMeterTask(float pSpeedCount){
            this.pSpeedCount = pSpeedCount;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /*動画の再生速度を変えるのに必要なプログラム↓*/
                    params.setSpeed((float)pSpeedCount);//再生速度変更
                    mp.setPlaybackParams(params);
                    //mp.start();
                    tSpeed.setText(String.format("%.1f", (float) (pSpeedCount*10)));
                    if((pSpeedCount * 10) < 10.0) {
                        tSpeedInt.setText(String.format("%.1f", (float) (pSpeedCount * 10)).substring(0, 1));
                        tSpeedDec.setText(String.format("%.1f", (float) (pSpeedCount * 10)).substring(1, 3));
                    }else{
                        tSpeedInt.setText(String.format("%.1f", (float) (pSpeedCount * 10)).substring(0, 2));
                        tSpeedDec.setText(String.format("%.1f", (float) (pSpeedCount * 10)).substring(2, 4));
                    }
                }
            });
        }
    }

    //針を0にするタスク
    public class BothNeedlesToZero implements Runnable {
        private double angle = 0.0;
        public BothNeedlesToZero(double angle){
            this.angle = angle;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //画像の横、縦サイズを取得
                    int imageWidth = bitmapSpeedMeterNeedle.getWidth();
                    int imageHeight = bitmapSpeedMeterNeedle.getHeight();
                    //Matrixインスタンス生成
                    Matrix matrix = new Matrix();
                    //画像中心を起点に90度回転
                    matrix.setRotate((float)angle, imageWidth/2, imageHeight/2);
                    //90度回転したBitmap画像を生成
                    Bitmap bitmapSet = bitmapSpeedMeterNeedle.createBitmap(bitmapSpeedMeterNeedle,0,0, imageWidth, imageHeight, matrix, true);
                    imageSpeedMeterNeedle.setImageBitmap(bitmapSet);
                    imageHeartbeatMeterNeedle.setImageBitmap(bitmapSet);
                }
            });
        }
    }

    //速度の針のタスク
    public class SpeedMeterNeedle implements Runnable {
        private double angle = 0.0;
        public SpeedMeterNeedle(double angle){
            this.angle = angle;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //画像の横、縦サイズを取得
                    int imageWidth = bitmapSpeedMeterNeedle.getWidth();
                    int imageHeight = bitmapSpeedMeterNeedle.getHeight();
                    //Matrixインスタンス生成
                    Matrix matrix = new Matrix();
                    //画像中心を起点に90度回転
                    matrix.setRotate((float)angle, imageWidth/2, imageHeight/2);
                    //90度回転したBitmap画像を生成
                    Bitmap bitmapSet = bitmapSpeedMeterNeedle.createBitmap(bitmapSpeedMeterNeedle,0,0, imageWidth, imageHeight, matrix, true);
                    imageSpeedMeterNeedle.setImageBitmap(bitmapSet);
                }
            });
        }
    }

    //心拍の針のタスク
    public class HeartbeatMeterNeedle implements Runnable {
        private double angle = 0.0;
        public HeartbeatMeterNeedle(double angle){
            this.angle = angle;
        }
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //画像の横、縦サイズを取得
                    int imageWidth = bitmapHeartbeatMeterNeedle.getWidth();
                    int imageHeight = bitmapHeartbeatMeterNeedle.getHeight();
                    //Matrixインスタンス生成
                    Matrix matrix = new Matrix();
                    //画像中心を起点に90度回転
                    matrix.setRotate((float)angle, imageWidth/2, imageHeight/2);
                    //90度回転したBitmap画像を生成
                    Bitmap bitmapSet = bitmapHeartbeatMeterNeedle.createBitmap(bitmapHeartbeatMeterNeedle,0,0, imageWidth, imageHeight, matrix, true);
                    imageHeartbeatMeterNeedle.setImageBitmap(bitmapSet);
                }
            });
        }
    }

    //ボリュームキーの操作(完成版はここで速度変更はできなくする)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                if(speedcount < 0.1){
                    speedcount = speedcount + 0.1;
                    speedMeterAngle = speedMeterAngle + 4.52;
                }else {
                    speedcount = speedcount + 0.01;
                    speedMeterAngle = speedMeterAngle + 0.452;
                }
                if (speedcount > 5){//意味わからないほど早くされるとクラッシュする対策
                    speedcount = 5;
                }
                Thread SetNeedleUp = new Thread(new SpeedMeterNeedle(speedMeterAngle));
                SetNeedleUp.start();

                Thread SpeedUp = new Thread(new SpeedMeterTask((float)speedcount));
                SpeedUp.start();
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                if(speedcount <= 0.1){
                    speedcount = speedcount - 0.1;
                    speedMeterAngle = speedMeterAngle - 4.52;
                }else {
                    speedcount = speedcount - 0.01;
                    speedMeterAngle = speedMeterAngle - 0.452;
                }
                if (speedcount <= 0.01) {
                    speedcount = 0.00;
                }
                Thread SetNeedleDown = new Thread(new SpeedMeterNeedle(speedMeterAngle));
                SetNeedleDown.start();
                Thread SpeedDown = new Thread(new SpeedMeterTask((float)speedcount));
                SpeedDown.start();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}