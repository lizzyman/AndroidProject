package com.example.lizzyman.timer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class MyCounterService extends Service {

    private boolean isBinding;

    private Handler handler;
    private TextToSpeech tts;
    private int i;

    private IMyCounterInterface.Stub binder = new IMyCounterInterface.Stub() {
        @Override
        public int getCount() throws RemoteException {
            return i;
        }
    }; // Rebuild project를 해야 Stub이 생성된다.

    public MyCounterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isBinding = true;

        handler = new Handler();

        Thread counter = new Thread(new CountThread());
        counter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        isBinding = true;
        i = intent.getIntExtra("TIME", 0);

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBinding = false;

        while(i != -1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        i = 0;
        return true;
    }

    class CountThread implements  Runnable {

        @Override
        public void run() {

            for(; i >= 0; i--) {

                if (!isBinding) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            tts.setLanguage(Locale.KOREAN);
                            tts.setSpeechRate(1.0f);

                            tts.speak("카운팅 끝", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });

                }
            });

            i = -1;
        }
    }

}
