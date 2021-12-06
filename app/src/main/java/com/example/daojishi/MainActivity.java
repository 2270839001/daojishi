package com.example.daojishi;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout countDown;
    // 倒计时
    private TextView mDays_Tv, mHours_Tv, mMinutes_Tv, mSeconds_Tv;
    private EditText tian,shi,fen,miao;
    private long mDay = 0;// 天
    private long mHour = 0;//小时,
    private long mMin = 0;//分钟,
    private long mSecond = 0;//秒

    private Timer mTimer;

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                mDays_Tv.setText(mDay+"");//天数不用补位
                mHours_Tv.setText(getTv(mHour));
                mMinutes_Tv.setText(getTv(mMin));
                mSeconds_Tv.setText(getTv(mSecond));
                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    mTimer.cancel();
                }
            }
        }
    };

    private String getTv(long l){
        if(l>=10){
            return l+"";
        }else{
            return "0"+l;//小于10,,前面补位一个"0"
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = new Timer();
        countDown = (RelativeLayout) findViewById(R.id.countdown_layout);
        mDays_Tv = (TextView) findViewById(R.id.days_tv);
        mHours_Tv = (TextView) findViewById(R.id.hours_tv);
        mMinutes_Tv = (TextView) findViewById(R.id.minutes_tv);
        mSeconds_Tv = (TextView) findViewById(R.id.seconds_tv);
        tian = findViewById(R.id.tian);
        shi = findViewById(R.id.shi);
        fen = findViewById(R.id.fen);
        miao = findViewById(R.id.miao);
    }
    public void start(View v) {
        mDay =Long.parseLong(tian.getText().toString());
        mHour =Long.parseLong(shi.getText().toString());
        mMin =Long.parseLong(fen.getText().toString());
        mSecond =Long.parseLong(miao.getText().toString());
        startRun();
    }

    /**
     * 开启倒计时
     *  //time为Date类型：在指定时间执行一次。
     *        timer.schedule(task, time);
     *  //firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
     *       timer.schedule(task, firstTime,period);
     *  //delay 为long类型：从现在起过delay毫秒执行一次。
     *       timer.schedule(task, delay);
     *  //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
     *       timer.schedule(task, delay,period);
     */
    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                    if(mDay < 0){
                        // 倒计时结束
                        mDay = 0;
                        mHour= 0;
                        mMin = 0;
                        mSecond = 0;
                    }
                }
            }
        }
        if(mMin==0&&mHour==0&&mDay==0&&mSecond==0){
            Toast.makeText(MainActivity.this, "时间到", Toast.LENGTH_SHORT).show();
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MainActivity.this, notification);
            r.play();
        }
    }
}