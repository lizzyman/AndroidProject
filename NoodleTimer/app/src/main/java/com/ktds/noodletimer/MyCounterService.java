package com.ktds.noodletimer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Admin on 2017-07-12.
 */

public class MyCounterService extends Service {

    private Handler handler;
    private TextToSpeech tts;
    private int i;
    private int min;
    private int sec;

    private IMyCounterInterface.Stub binder = new IMyCounterInterface.Stub() {

        @Override
        public int getCount() throws RemoteException {
            return min;
        }

        @Override
        public int getCount2() throws RemoteException {
            return sec;
        }
    };

    public MyCounterService() {}

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();

        Thread counter = new Thread(new CountThread());
        counter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        i = intent.getIntExtra("TIME", 0);
        Log.d("A", i+"");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

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

                min = i / 60;
                sec = i % 60;

                Log.d("TIME", ""+min+"**"+sec);

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
                            tts.setLanguage(Locale.ENGLISH);
                            tts.setSpeechRate(1.0f);

                            tts.speak("GOOD LUCK!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });

                }
            });

            i = -1;
        }
    }
}
