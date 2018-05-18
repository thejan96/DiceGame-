package com.example.werewolves.dicegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RollingActivity extends AppCompatActivity implements SensorEventListener{

    private final int rollAnimations = 10;
    private final int delayTime = 200;
    private Resources res;
    private final int[] diceImages = new int[] { R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5, R.drawable.d6 };
    private Drawable dice[] = new Drawable[6];
    private final Random randomGen = new Random();
    @SuppressWarnings("unused")
    private int diceSum;
    private int roll =  6;
    private ImageView die1;
    //private ImageView die2;
    private RelativeLayout diceContainer;
    private SensorManager sensorMgr;
    private Handler animationHandler;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private boolean paused = false;
    private static final int UPDATE_DELAY = 50;
    private static final int SHAKE_THRESHOLD = 800;
    public static int dicevalue = 0;
    public Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        paused = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolling);
        //setTitle(getString(R.string.app_title));
        res = getResources();
        for (int i = 0; i < 6; i++) {
            dice[i] = res.getDrawable(diceImages[i]);
        }
//        diceContainer = (RelativeLayout) findViewById(R.id.activity_rolling);
//        diceContainer.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    rollDice();
//                } catch (Exception e) {};
//            }
//        });
        die1 = (ImageView) findViewById(R.id.die1);
        //die2 = (ImageView) findViewById(R.id.die2);
        animationHandler = new Handler() {
            public void handleMessage(Message msg) {
                die1.setImageDrawable(dice[roll]);
                //die2.setImageDrawable(dice[roll[1]]);
            }
        };
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,
                sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),	SensorManager.SENSOR_DELAY_GAME);
        //if (!accelSupported) sensorMgr.unregisterListener(this); //no accelerometer on the device
        //rollDice();
    }

    private void rollDice() {
        if (paused) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    doRoll();
                }
            }
        }).start();

        MediaPlayer mp = MediaPlayer.create(this, R.raw.roll);
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    private void doRoll() { // only does a single roll
        roll = randomGen.nextInt(6);
//        roll[1] = randomGen.nextInt(6);
        dicevalue = roll + 1;
        Log.d("tag",Integer.toString(roll+1));
        //Log.d("tag",Integer.toString(roll[1]));
        //diceSum = roll[0]  + roll[1] + 2; // 2 is added because the values of the rolls start with 0 not 1
        synchronized (getLayoutInflater()) {
            animationHandler.sendEmptyMessage(0);
        }
        try { // delay to alloy for smooth animation
            Thread.sleep(delayTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        paused = false;
    }

    public void onPause() {
        super.onPause();
        paused = true;
    }

    public void goBack()
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("DICEVALUE",dicevalue);
        setResult(1,intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > UPDATE_DELAY) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) { //the screen was shaked
                    rollDice();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            goBack();
                        }
                    }, 3000);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return; //this method isn't used
    }
}
