package no.srib;

import java.io.File;

import no.srib.fragment.PlaceholderFragment;
import no.srib.fragment.PodcastList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity implements OnClickListener, SensorEventListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		//StrictMode.setThreadPolicy(policy);
		
		super.onCreate(savedInstanceState);
	
		
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//Vibrator manager = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//manager.vibrate(3000);
		SensorManager manager1 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor sen = manager1.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		manager1.registerListener(this, sen, SensorManager.SENSOR_DELAY_FASTEST);
	
		
		Notification not = new Notification();
		not.vibrate = new long[] {0,100,150,100};
		not.flags |= Notification.FLAG_INSISTENT;
		
	    not.defaults |=  Notification.DEFAULT_VIBRATE;          

		not.ledARGB = 0xff0dff;
	
		not.ledOnMS = 1000;
		not.ledOffMS = 1000;
		
		not.flags |= Notification.FLAG_SHOW_LIGHTS;
		NotificationManager nman = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
		nman.notify(1, not);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Fragment fragment = null;
		if(v.getId() == R.id.knapp){
			fragment = new PodcastList();
		}else if(v.getId() == R.id.tilbakeknapp){
			fragment = new PlaceholderFragment();
		}
		
		
		  FragmentManager fm = getSupportFragmentManager();
		  FragmentTransaction transaction = fm.beginTransaction();
		    transaction.addToBackStack(null);
		  transaction.replace(R.id.container, fragment); //Container -> R.id.contentFragment
		  transaction.commit();
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float value = event.values[0];
		
		Log.i("deub",value + "");
	}
	
	
	
	
	
}
