package com.clarance.cateringserviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class PaymentActivity extends AppCompatActivity {
    private TextView textPaymentStatus;
    private TextView textAccountBalance;
    private Button btQueuing;
    int time;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_info);

        //find view
        textPaymentStatus = findViewById(R.id.payment_status);
        textAccountBalance = findViewById(R.id.account_balance);
        btQueuing = findViewById(R.id.bt_queuing);

        //从intent取出bundle
        Bundle bundle = this.getIntent().getBundleExtra("Message");
        //获取数据
        int accountBalance = bundle.getInt("account balance");
        //显示数据
        if ((accountBalance) >= 0) {
            textPaymentStatus.setText("Successful payment");
            textAccountBalance.setText("$ " + accountBalance);
        } else {
            textPaymentStatus.setText("Unsuccessful payment");
            textAccountBalance.setText("Sorry, but insufficient account balance");
            btQueuing.setVisibility(View.INVISIBLE);
        }
        btQueuing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //waiting in line for food
                time = 5 + (int) (Math.random() * 6);
                showWaitingDialog();
            }
        });
    }

    private void showWaitingDialog() {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        final ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("Waiting in line...");
        waitingDialog.setMessage(time + " s");

        // count down, cancel dialog
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    waitingDialog.setMessage((time -= 1) + " s");
                } else {
                    waitingDialog.setMessage("Please pick up your food.");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage();
                    waitingDialog.cancel();
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    // show notification
    private void sendMessage() {
        int windowNum = 1 + (int) (Math.random() * 5);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, FoodChannelActivity.class);
        intent.putExtra("windowNum", windowNum);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // set channel
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("1", "取餐窗口通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId("1");
        }

        // notification attribute
        mBuilder.setSmallIcon(R.drawable.packet)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Click for details!")
                .setContentText("pick up food at window " + windowNum)
                .setContentIntent(pi)
                .setAutoCancel(true);
        notificationManager.notify(1, mBuilder.build()); // send notification
    }
}
