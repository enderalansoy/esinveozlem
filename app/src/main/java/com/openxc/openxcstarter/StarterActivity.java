package com.openxc.openxcstarter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.FuelConsumed;
import com.openxc.measurements.Odometer;
import com.openxc.measurements.VehicleSpeed;
import com.openxcplatform.openxcstarter.R;
import com.openxc.VehicleManager;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.EngineSpeed;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifTextView;

public class StarterActivity extends Activity {
    private static final String TAG = "StarterActivity";

    private VehicleManager mVehicleManager;
    private TextView mEngineSpeedView;
    private TextView mfuel_counsumed;
    private TextView five_km;
    private TextView efficient;
    private GifTextView gif;

double speed_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        // grab a reference to the engine speed text object in the UI, so we can
        // manipulate its value later from Java code
        mEngineSpeedView = (TextView) findViewById(R.id.engine_speed);
        mfuel_counsumed = (TextView) findViewById(R.id.textView_fuel_consumed_value);
        five_km = (TextView) findViewById(R.id.textView_five_km);
        efficient=(TextView)findViewById(R.id.textView_verimlilik);
        gif=(GifTextView)findViewById(R.id.imageView);



        //Declare the timer
        Timer t = new Timer();
//Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {


                                      final MediaPlayer ses = MediaPlayer.create(StarterActivity.this, R.raw.verimhesap);
                                      ses.start();
                                      SystemClock.sleep(5000);

                                      //efficient.setText("Veriminiz: %"+(float) (predicted_fuel/fuel_consumed)*100);
                                      //Toast.makeText(StarterActivity.this,"Veriminiz: %"+(float) (predicted_fuel/fuel_consumed)*100, Toast.LENGTH_LONG).show();


                                      if(speed_v>60 && km<=5)
                                      {

                                          final MediaPlayer ses2 = MediaPlayer.create(StarterActivity.this, R.raw.aniyukselis);
                                          ses2.start();

                                          //Toast.makeText(getApplicationContext(),"Ani kalkış yapılması yakıt tüketimini arttırabilir", Toast.LENGTH_LONG).show();
                                          SystemClock.sleep(5000);

                                      }

                                  }



                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                5000);





    }

    @Override
    public void onPause() {
        super.onPause();
        // When the activity goes into the background or exits, we want to make
        // sure to unbind from the service to avoid leaking memory
        if(mVehicleManager != null) {
            Log.i(TAG, "Unbinding from Vehicle Manager");
            // Remember to remove your listeners, in typical Android
            // fashion.
            mVehicleManager.removeListener(EngineSpeed.class,
                    mSpeedListener);
            mVehicleManager.removeListener(VehicleSpeed.class,
                    mSpeedListener);
            mVehicleManager.removeListener(AcceleratorPedalPosition.class,
                    mSpeedListener);
            mVehicleManager.removeListener(FuelConsumed.class,
                    mSpeedListener);
            unbindService(mConnection);
            mVehicleManager = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.
        if(mVehicleManager == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /* This is an OpenXC measurement listener object - the type is recognized
     * by the VehicleManager as something that can receive measurement updates.
     * Later in the file, we'll ask the VehicleManager to call the receive()
     * function here whenever a new EngineSpeed value arrives.
     */



    VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final VehicleSpeed speed = (VehicleSpeed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            StarterActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                    //mEngineSpeedView.setText(" speed : "
                      //      + speed.getValue().doubleValue());


                }
            });
        }
    };



    double fuel_consumed;
    FuelConsumed.Listener mFuelConsumedListener = new FuelConsumed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final FuelConsumed speed = (FuelConsumed) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            // important concept in Android.
            // important concept in Android.
            // important concept in Android.
            StarterActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                   // mfuel_counsumed.setText(" fuel consumed : "
                     //       + speed.getValue().doubleValue());
                    fuel_consumed=speed.getValue().doubleValue();

                    efficient.setText("Veriminiz: %"+(float) (predicted_fuel/fuel_consumed)*100);

                }
            });
        }
    };


    double km ;
    double predicted_fuel;
    Odometer.Listener mOdometerListener = new Odometer.Listener() {
        @Override
        public void receive(Measurement measurement) {
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final Odometer speed = (Odometer) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            StarterActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                    km=speed.getValue().doubleValue();

                    predicted_fuel=(km*6)/100;


                    //five_km.setText(" Odometer : " + speed.getValue().doubleValue());
                }
            });
        }
    };

    AcceleratorPedalPosition.Listener  mAcceleratorPedalPositionListener2 = new AcceleratorPedalPosition.Listener() {
        @Override
        public void receive(Measurement measurement) {
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final AcceleratorPedalPosition speed = (AcceleratorPedalPosition) measurement;
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            StarterActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value

                    speed_v=speed.getValue().doubleValue();

                /*    if(speed.getValue().doubleValue()>=70 && km<=5)
                    {

                        final MediaPlayer ses = MediaPlayer.create(StarterActivity.this, R.raw.aniyukselis);
                        ses.start();

                        //Toast.makeText(getApplicationContext(),"Ani kalkış yapılması yakıt tüketimini arttırabilir", Toast.LENGTH_LONG).show();


                    }*/


                }
            });
        }
    };









    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the VehicleManager service is
        // established, i.e. bound.
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");
            // When the VehicleManager starts up, we store a reference to it
            // here in "mVehicleManager" so we can call functions on it
            // elsewhere in our code.
            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();

            // We want to receive updates whenever the EngineSpeed changes. We
            // have an EngineSpeed.Listener (see above, mSpeedListener) and here
            // we request that the VehicleManager call its receive() method
            // whenever the EngineSpeed changes
            mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
            mVehicleManager.addListener(FuelConsumed.class, mFuelConsumedListener);
            mVehicleManager.addListener(Odometer.class, mOdometerListener);
            mVehicleManager.addListener(AcceleratorPedalPosition.class, mAcceleratorPedalPositionListener2 );


        }



        // Called when cmd
        // cthe connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.starter, menu);
        return true;
    }
}
