package com.example.lab4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Sensor mag, acc;
    SensorManager m;
    float xy_angle, xz_angle, zy_angle;
    ImageView compass;
    Matrix matrix;
    float currentDegree = 0f;


    float[] mLastAccelerometer = new float[3];
    float[] mLastMagnetometer = new float[3];
    boolean mLastAccelerometerSet = false;
    boolean mLastMagnetometerSet = false;
    float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = m.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        compass = (ImageView) findViewById(R.id.imageView2);
        Matrix matrix = new Matrix();
        compass.setScaleType(ImageView.ScaleType.MATRIX);
        onStart();
        }



    @Override
    public void onStart(){
        super.onStart();
        m.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        m.registerListener(this,mag, SensorManager.SENSOR_DELAY_NORMAL);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor == acc) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else
            if(event.sensor == mag){
                System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.length);
                mLastMagnetometerSet = true;
            }

         if(mLastAccelerometerSet && mLastMagnetometerSet){
             m.getRotationMatrix(mR,null,mLastAccelerometer,mLastMagnetometer);
             m.getOrientation(mR, mOrientation);
             int degree = (int) (Math.toDegrees(mOrientation[0])+360)%360;
             //degree = Math.round(degree);
             //compass.setRotation(-degree);
             compass.animate().rotation(-degree);
             /*RotateAnimation ra = new RotateAnimation(
                     currentDegree,
                     -degree,
                     Animation.RELATIVE_TO_SELF, 0.5f,
                     Animation.RELATIVE_TO_SELF,
                     0.5f);

             ra.setDuration(250);

             ra.setFillAfter(true);

             compass.startAnimation(ra);
             currentDegree = -degree;*/
         }





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
