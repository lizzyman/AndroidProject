package com.ktds.noodletimer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_time_min;
    private TextView tv_time_sec;
    private Button btn_start;
    private Button btn_stop;

    private int time;

    private IMyCounterInterface binder;
    private boolean isBinding;

    private Handler handler;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            binder = IMyCounterInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        handler = new Handler();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time_min = (TextView) findViewById(R.id.tv_time_min);
        tv_time_sec = (TextView) findViewById(R.id.tv_time_sec);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        Intent tempIntent = getIntent();
        time = tempIntent.getIntExtra("time", 0);

        Log.d("TIME", time + "");
        tv_title.setText(time + " sec");

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBinding) { // Binding 되어 있다면
                    Intent intent =  new Intent(TimerActivity.this, MyCounterService.class);
                    intent.putExtra("TIME", 100);
                    bindService(intent, connection, BIND_AUTO_CREATE);

                    Thread thread = new Thread(new GetCountThread());
                    thread.start();
                }

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBinding) { // Binding이 되어 있지 않다면
                    isBinding = false;
                    unbindService(connection);
                }
            }
        });
    }

    class GetCountThread implements Runnable {
        int min = 0;
        int sec = 0;

        @Override
        public void run() {

            isBinding = true;

            while (isBinding) {

                if (binder == null) {
                    continue;
                }

                try {
                    min = binder.getCount();
                    sec = binder.getCount2();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_time_min.setText(min + "");
                        tv_time_sec.setText(sec + "");
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (isBinding) {
                TimerActivity.this.unbindService(connection);
            }

            isBinding = false;

        }
    }
}
