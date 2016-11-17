package com.example.a1521315.test02;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Scanner;

/**
 * Created by 1521312 on 2016/11/14.
 */

public class usb_debug extends Activity {

    private final static int USBAccessoryWhat = 0;
    public static final int UPDATE_LED_SETTING = 1;
    public static final int POLE_SENSOR_CHANGE = 3;
    public static final int APP_CONNECT = (int) 0xFE;
    public static final int APP_DISCONNECT = (int) 0xFF;
    public static final int POT_UPPER_LIMIT = 100;
    public static final int POT_LOWER_LIMIT = 0;
    private static final String TAG = "usb_debug";

    String mediaPath = null;//動画データ
    double TotalMileage=0;//総走行距離
    int raw = 0;//rawファイルかどうかを判断する変数。0=内部ストレージ　1=rawファイル
    public boolean usb_flg = false;

    public int sensor_value = 0;
    private boolean deviceAttached = false;
    private int firmwareProtocol = 0;
    public int sent_cnt= 0;

    private enum ErrorMessageCode {
        ERROR_OPEN_ACCESSORY_FRAMEWORK_MISSING,
        ERROR_FIRMWARE_PROTOCOL
    };

    private USBAccessoryManager accessoryManager;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_debug);

        accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        accessoryManager.enable(usb_debug.this, getIntent());
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

    }

    // Resets the demo application when a device detaches
    public void disconnectAccessory() {
        Toast.makeText(this, "DISCONNECT SUCCESS", Toast.LENGTH_LONG).show();

        if (deviceAttached == false) {
            //Toast.makeText(this, "デバイスが切断されました", Toast.LENGTH_LONG).show();
            return;
        }
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
                                        sent_cnt++;
                                        TextView tSpeed = (TextView) findViewById(R.id.textView5);
                                        sensor_value = (int) (commandPacket[1] & 0xFF);
                                            tSpeed.setText(sensor_value+"changed→"+sent_cnt);
                                            if(sent_cnt > 30000){
                                                sent_cnt = 0;
                                            }
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


}
