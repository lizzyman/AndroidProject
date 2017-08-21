package com.example.lizzyman.timer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isBinding; // 기본값은 false

    private TextView tv_counter;
    private Button btn_play;
    private Button btn_stop;

    private Handler handler;

    private IMyCounterInterface binder;

    private ServiceConnection connection = new ServiceConnection() { // 현재 binder가 요청되고 있는 것이 여기로 들어오게 된다.
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
        setContentView(R.layout.activity_main);

        handler = new Handler();

        tv_counter = (TextView) findViewById(R.id.tv_counter);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        final Intent intent = new Intent(MainActivity.this, MyCounterService.class);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBinding) {
                    Intent intent = new Intent(MainActivity.this, MyCounterService.class);
                    intent.putExtra("TIME", 180);
                    bindService(intent, connection, BIND_AUTO_CREATE);

                    Thread thread = new Thread(new GetCountThread());
                    thread.start();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBinding) {
                    isBinding = false;
                    unbindService(connection);
                }
            }
        });
    }

    class GetCountThread implements Runnable {
        int count = 0;

        @Override
        public void run() {

            isBinding = true;

            while (isBinding) {

                if (binder == null) {
                    continue;
                }

                try {
                    count = binder.getCount();
                    if (count == -1) {
                        break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_counter.setText(count + "");
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (isBinding) {
                MainActivity.this.unbindService(connection);
            }

            isBinding = false;

        }
    }

}
