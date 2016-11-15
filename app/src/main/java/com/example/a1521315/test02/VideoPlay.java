package com.example.a1521315.test02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.example.a1521315.test02.R.id.ConnectCheak;
import static com.example.a1521315.test02.R.id.buttonNo;
import static com.example.a1521315.test02.R.id.buttonYes;

public class VideoPlay extends Activity implements SurfaceHolder.Callback, Runnable, MediaPlayer.OnCompletionListener,View.OnClickListener {
    Globals globals;
    TextView tSpeed;//時速の変数
    TextView tMileage;//走行距離の変数
    TextView tHeartbeat;//心拍の変数
    TextView tTest;//再生時間の変数
    TextView tTimer;//タイマーの変数
    TextView tCourse;//コース番号

    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private SurfaceView mPreview;
    private MediaPlayer mp = null;

    private ScheduledExecutorService getplaytimescheduler;
    Runnable mygetplaytimetask = new MyGetPlayTimeTask();
    ScheduledFuture getplaytimefuture;

    private ScheduledExecutorService timerscheduler;
    Runnable mytimertask = new MyTimerTask();
    ScheduledFuture future;

    private Timer movemetimer;
    private MoveMeTask timerTask = null;

    PlaybackParams params = new PlaybackParams();
    double speedcount = 0.0;

    private ImageView imageView;//image_view_me用の変数
    float imageX ;
    float imageY ;

    //#########################################################1
    private final static int USBAccessoryWhat = 0;
    public static final int UPDATE_LED_SETTING = 1;
    public static final int POLE_SENSOR_CHANGE = 3;
    public static final int APP_CONNECT = (int) 0xFE;
    public static final int APP_DISCONNECT = (int) 0xFF;
    public static final int POT_UPPER_LIMIT = 100;
    public static final int POT_LOWER_LIMIT = 0;

    String mediaPath = null;//動画データ
    double TotalMileage=0;//総走行距離
    int raw = 0;//rawファイルかどうかを判断する変数。0=内部ストレージ　1=rawファイル
    public boolean usb_flg = false;

    public int sensor_value = 0;
    private boolean deviceAttached = false;
    private int firmwareProtocol = 0;

    private enum ErrorMessageCode {
        ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING,
        ERROR_FIRMWARE_PROTOCOL
    };

    private USBAccessoryManager accessoryManager;

    //bluetooth**********************************************************************************
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

    //********************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
        holder = mPreview.getHolder();
        holder.addCallback(this);

        globals = (Globals)this.getApplication();

        tSpeed = (TextView) findViewById(R.id.textSpeed);
        tSpeed.setText("0.0");
        tMileage = (TextView) findViewById(R.id.textMileage);
        tMileage.setText("0.0");
        tHeartbeat = (TextView) findViewById(R.id.textHeartbeat);
        tHeartbeat.setText("0");
        tTimer = (TextView) findViewById(R.id.textTimer);
        tTimer.setText("00:00.0");
        tTest = (TextView) findViewById(R.id.textTest);
        tTest.setText("");
        ImageView imageView1 = (ImageView)findViewById(R.id.image_view_bar);
        imageView1.setImageResource(R.drawable.bar);
        imageView = (ImageView)findViewById(R.id.image_view_me);
        imageView.setImageResource(R.drawable.me);

        findViewById(R.id.buttonPlay).setOnClickListener(PlayClickListener);
        findViewById(R.id.buttonPause).setOnClickListener(PauseClickListener);
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

        //#####################################################################################################1
        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            Log.d(TAG, "Info:" + info.packageName + "\n" + info.versionCode + "\n" + info.versionName);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //bluetooth*********************************************************************************
        mInputTextView = (TextView)findViewById(R.id.textHeartbeat);
        mStatusTextView = (TextView)findViewById(R.id.statusValue);
        //connectButton = (Button)findViewById(R.id.connectButton);
        //connectButton.setOnClickListener(this);
        findViewById(buttonYes).setOnClickListener(this);
        findViewById(buttonNo).setOnClickListener(this);

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
        //**********************************************************************************

    }
    // 再生完了時の処理
    @Override
    public void onCompletion(MediaPlayer agr0) {
        Log.v("MediaPlayer", "onCompletion");
        //USB通信の切断(停止がないため)
        accessoryManager.disable(this);
        disconnectAccessory();//################################       あやしいかもよ～
        Button BtnPauseView2 = (Button) findViewById(R.id.buttonPause);
        BtnPauseView2.setVisibility(View.INVISIBLE);
        getplaytimescheduler.shutdown();//サービス終了させる
        timerscheduler.shutdown();//タイマー止める
        movemetimer.cancel();//MoveMeTask止める
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
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();//##
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    //#################################################################################################1
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
        Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
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

    //#################################################################################################2

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
        getplaytimescheduler = Executors.newSingleThreadScheduledExecutor();
        getplaytimefuture = getplaytimescheduler.scheduleAtFixedRate(mygetplaytimetask, 0, 1000, TimeUnit.MILLISECONDS);
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

    //bluetooth**********************************************************************************
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
            valueMsg.obj = "Error1:" + e;
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
    //*****************************************************************************************


    //Playボタンを押したときの処理
    View.OnClickListener PlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findViewById(R.id.buttonPlay).setVisibility(View.INVISIBLE);//PLAYボタンを押したらPLAYボタンを消す
            findViewById(R.id.buttonPause).setVisibility(View.VISIBLE);//PLAYボタンを押したらPAUSEボタンを出す
            speedcount = 0.0;
            tSpeed.setText(String.format("%.1f", (float) (speedcount * 10)));
            mp.setPlaybackParams(params);
            mp.seekTo(0);

            timerscheduler = Executors.newSingleThreadScheduledExecutor();
            future = timerscheduler.scheduleAtFixedRate(mytimertask, 0, 100, TimeUnit.MILLISECONDS);
            if (null != movemetimer) {
                movemetimer.cancel();
                movemetimer = null;
            }
            movemetimer = new Timer();//Timerインスタンスを生成
            timerTask = new MoveMeTask();//TimerTaskインスタンスを生成
            movemetimer.schedule(timerTask, 0, 1000);//スケジュールを設定1000msec
        }
    };

    //Pauseボタンを押したときの処理
    View.OnClickListener PauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usb_flg = true;
            future.cancel(true);//タイマー一時停止
            getplaytimefuture.cancel(true);//タイマー一時停止
            speedcount = 0;
            params.setSpeed((float) speedcount);
            mp.setPlaybackParams(params);
            tSpeed.setText(String.format("%.1f", (float) (speedcount * 10)));

            // ポップアップメニュー表示
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlay.this);
            alertDialog.setTitle("ポーズ");
            alertDialog.setMessage("一時停止中です");
            alertDialog.setPositiveButton("走行をやめてコース選択に戻る", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //VideoSelectに戻る処理
                    getplaytimescheduler.shutdown();//サービス終了させる
                    timerscheduler.shutdown();//タイマー終了
                    movemetimer.cancel();//MoveMeTask止める
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
                    future = timerscheduler.scheduleAtFixedRate(mytimertask, 0, 100, TimeUnit.MILLISECONDS);//タイマーを動かす
                    getplaytimefuture = getplaytimescheduler.scheduleAtFixedRate(mygetplaytimetask, 0, 1000, TimeUnit.MILLISECONDS);
                    usb_flg = false;
                }
            });
            AlertDialog myDialog = alertDialog.create();
            myDialog.setCanceledOnTouchOutside(false);//ダイアログ画面外をタッチされても消えないようにする
            myDialog.show();
        }
    };

    //Resultボタンを押したときの処理
    View.OnClickListener ResultClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            globals.coursename = tCourse.getText().toString();//コース名
            globals.mileage = tMileage.getText().toString();//走行距離
            globals.maxheartbeat = tHeartbeat.getText().toString();//最大心拍(現在は心拍数を代入しているので実際最大心拍を取得する処理を書いてから代入する)
            globals.avg = tSpeed.getText().toString();//平均速度(これも計算する処理が必要)
            globals.max = tSpeed.getText().toString();//最高速度(これも同じ)
            globals.time = tTimer.getText().toString();//運動時間
            globals.cal = "1234";//消費カロリー
            Intent intent = new Intent(getApplication(), Result.class);
            startActivity(intent);
        }
    };

    //Connectボタンを押したときの処理
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.buttonYes:
                //イベント処理を記入
                if (!connectFlg) {
                    mStatusTextView.setText("try connect");
                    mThread = new Thread(this);
                    // Threadを起動し、Bluetooth接続
                    isRunning = true;
                    mThread.start();
                }
                break;

            case buttonNo:
                findViewById(R.id.ConnectCheak).setVisibility(View.INVISIBLE);
                findViewById(R.id.buttonPlay).setVisibility(View.VISIBLE);
                tHeartbeat.setText("Not");
                break;
        }
    }


    //ボリュームキーの操作
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                speedcount = speedcount + 0.1;
                if (speedcount > 5)//意味わからないほど早くされるとクラッシュする対策
                {
                    speedcount = 5;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float) speedcount);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                //tSpeed.setText(""+(float)(cnt*10));
                tSpeed.setText(String.format("%.1f", (float) (speedcount * 10)));
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                speedcount = speedcount - 0.1;
                if (speedcount <= 0.1) {
                    speedcount = 0.0;
                }
                /*動画の再生速度を変えるのに必要なプログラム↓*/
                params.setSpeed((float) speedcount);//再生速度変更
                mp.setPlaybackParams(params);
                //mp.start();
                tSpeed.setText(String.format("%.1f", (float) (speedcount * 10)));
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
                                    case POLE_SENSOR_CHANGE:
                                        float speed_tmp;
                                        if( usb_flg == true ) {
                                            speed_tmp = 0;
                                        }
                                        else
                                        {
                                             sensor_value = (int) commandPacket[1];
                                             speed_tmp = (float) sensor_value / 20;
                                         }
                                        tSpeed.setText(speed_tmp * 10 + "km/h");//debug時はsensor_valueをつかう
                                        params.setSpeed(speed_tmp);//再生速度変更
                                        mp.setPlaybackParams(params);
                                        break;
                                }
                            }
                            break;
                        case CONNECTED:
                            break;
                        case READY:
                            //setTitle("Device connected.");
                            Log.d(TAG, "STAND BY OK");
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
                                    Log.d(TAG, "sending connect message.");
                                    accessoryManager.write(commandPacket);
                                    Log.d(TAG, "connect message sent.");
                                    break;
                                default:
                                    showErrorPage(VideoPlay.ErrorMessageCode.ERROR_FIRMWARE_PROTOCOL);
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

    //カウントアップタイマタスク
    public class MyTimerTask implements Runnable {
        //private Handler timerhandler = new Handler();
        private long timercount = 0;

        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                @Override
                public void run() {
                    timercount++;
                    long mm = timercount * 100 / 1000 / 60;
                    long ss = timercount * 100 / 1000 % 60;
                    long ms = (timercount * 100 - ss * 1000 - mm * 1000 * 60) / 100;
                    // 桁数を合わせるために02d(2桁)を設定
                    tTimer.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                }
            });
        }
    }

    //再生時間取得タスク
    public class MyGetPlayTimeTask implements Runnable {
        private Handler getplaytimehandler = new Handler();

        public void run() {
            getplaytimehandler.post(new Runnable() {
                @Override
                public void run() {
                    double f1=0,f2=0,f3=0;
                    f1 = mp.getDuration();
                    f2 = mp.getCurrentPosition();
                    f3 = TotalMileage / ( f1 / f2);
                    tTest.setText("総再生時間:" + mp.getDuration() + " 再生時間:" + mp.getCurrentPosition());tMileage.setText(String.format("%.2f",f3));
                }
            });
        }
    }
    //自機を動かす用のタスク
    class MoveMeTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    imageX = imageView.getX();
                    imageY = imageView.getY();
                    imageY -= 20;
                    //y方向は20pixづつ、画像の横縦幅はそのまま維持
                    imageView.layout((int)imageX, (int)imageY, (int)imageX + imageView.getWidth(), (int)imageY + imageView.getHeight());
                    Log.d("imageXY", "X:" + imageX + " Y:" + imageY);

                }
            });
        }
    }

    private int getFirmwareProtocol(String version) {

        String major = "0";
        int positionOfDot;

        //Toast.makeText(this, "getFirmware", Toast.LENGTH_LONG).show();

        positionOfDot = version.indexOf('.');
        if (positionOfDot != -1) {
            major = version.substring(0, positionOfDot);
        }

        return new Integer(major).intValue();
    }


    private void showErrorPage(VideoPlay.ErrorMessageCode error) {
        //setContentView(R.layout.error);

        //TextView errorMessage = (TextView) findViewById(R.id.error_message);
        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        switch (error) {
            case ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING:
                //errorMessage.setText(getResources().getText(R.string.error_missing_open_accessory_framework));
                break;
            case ERROR_FIRMWARE_PROTOCOL:
                //errorMessage.setText(getResources().getText(R.string.error_firmware_protocol));
                break;
            default:
                //errorMessage.setText(getResources().getText(R.string.error_default));
                break;
        }
    }
}