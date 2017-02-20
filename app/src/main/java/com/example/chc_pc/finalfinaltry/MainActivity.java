package com.example.chc_pc.finalfinaltry;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    String shu;
    Button ipcontrol;
    EditText IPText;
    TextView wendu,shidu;
    Button led1btn,led2btn,led3btn;
    Button diancisuobtn;
    Button beerbtn;
    private Context mContext;
    private boolean isConnecting = false;
    private boolean onflag = false;
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
    static BufferedReader mBufferedReaderServer	= null;
    static PrintWriter mPrintWriterServer = null;
    static InputStream mBufferedReaderClient	= null;
    static PrintWriter mPrintWriterClient = null;
    private  String recvMessageClient = "";
    private  String recvMessageServer = "";
    TextView guangzhao;
    int hum=0;
    int tem=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        IPText = (EditText)findViewById(R.id.main2ip);

        wendu=(TextView)findViewById(R.id.mainwenduxianshi);
        shidu=(TextView)findViewById(R.id.mainshiduxianshi);

        led1btn=(Button)findViewById(R.id.mainled1kaiguan);
        led2btn=(Button)findViewById(R.id.mainled2kaiguan);
        led3btn=(Button)findViewById(R.id.mainled3kaiguan);

        beerbtn=(Button)findViewById(R.id.mainbeerkaiguan);
        diancisuobtn=(Button)findViewById(R.id.maindiancisuokaiguan);

        led1btn.setOnClickListener(new led1());
        led2btn.setOnClickListener(new led2());
        led3btn.setOnClickListener(new led3());

        diancisuobtn.setOnClickListener(new diancisuo());
        beerbtn.setOnClickListener(new beer());

        IPText.setText("192.168.1.212:2112");
        lianjie();
    }

    class led1 implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(isConnecting && mSocketClient != null) {
                String output = "1";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    Toast.makeText(mContext,"还未连接" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient == null){
                Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class led2 implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(isConnecting && mSocketClient != null) {
                String output = "2";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    Toast.makeText(mContext,"还未连接" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient == null){
                Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class led3 implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(isConnecting && mSocketClient != null) {
                String output = "3";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    Toast.makeText(mContext,"还未连接" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient == null){
                Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class diancisuo implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(isConnecting && mSocketClient != null) {
                String output = "4";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    Toast.makeText(mContext,"还未连接" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient == null){
                Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class beer implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(isConnecting && mSocketClient != null) {
                String output = "5";
                try {
                    mPrintWriterClient.println(output);
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    Toast.makeText(mContext,"还未连接" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            if(mSocketClient == null){
                Toast.makeText(mContext, "未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void lianjie() {
        if (isConnecting){
            isConnecting = false;
            try{
                if(mSocketClient != null) {
                    mSocketClient.close();
                    mSocketClient = null;

                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            mThreadClient.interrupt();
            IPText.setEnabled(true);
        } else {
            isConnecting = true;
            IPText.setEnabled(false);
            mThreadClient = new Thread(mRunnable);
            mThreadClient.start();
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            String msgText = IPText.getText().toString();
            if (msgText.length() <= 0) {
                recvMessageClient = "IP can't be empty!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }

            int start = msgText.indexOf(":");
            if( (start == -1) ||(start+1 >= msgText.length()) )
            {
                recvMessageClient = "IP address is error!\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }

            String sIP = msgText.substring(0, start);
            String sPort = msgText.substring(start+1);
            int port = Integer.parseInt(sPort);

            Log.d("gjz", "IP:"+ sIP + ":" + port);

            try{
                mSocketClient = new Socket(sIP,port);//连接服务器
                mBufferedReaderClient = mSocketClient.getInputStream();//获得输入输出流
                mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(),true);

                recvMessageClient = "connected to server!\n";
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

            }catch (Exception e) {
                recvMessageClient = "connecting IP is error:" + e.toString() + e.getMessage() + "\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }

            byte[] buffer = new byte[1024];
            int count = 0;
            while (isConnecting)
            {
                try {
                    mBufferedReaderClient.read(buffer);
                    Message message = new Message(); // 通知界面
                    message.what = 2;
                    message.obj = buffer;
                    mHandler.sendMessage(message);
                } catch (Exception e) { }
            }
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 2:
                    byte buffer[] = (byte[])msg.obj;
                    refreshView(buffer); // 接收到数据后显示
                    break;
            }
        }
        private void refreshView(byte[] buffer) {
            hum =  (buffer[0]&0xff);
            tem =  (buffer[2]&0xff);
            wendu.setText("温度:"+tem+"℃");
            shidu.setText("湿度:"+hum+"%");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnecting)
        {
            isConnecting = false;
            try {
                if(mSocketClient!=null)
                {
                    mSocketClient.close();
                    mSocketClient = null;

                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mThreadClient.interrupt();
        }
    }
}
