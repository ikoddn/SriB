package no.srib;



import com.viewpagerindicator.UnderlinePageIndicator;

import no.srib.adapter.TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.annotation.SuppressLint;

import android.app.Notification;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;


@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements 
		SensorEventListener {

	private ViewPager viewPager;
	private TabsPagerAdapter adapter;
	private ActionBar actionbar;
	public Fragment frag = new Fragment();

	//


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);
		UnderlinePageIndicator titleIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
		titleIndicator.setFades(false);
	
		
		titleIndicator.setViewPager(viewPager);
		
		
		actionbar = getSupportActionBar();
		adapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		actionbar.hide();
		
		//actionbar.hide();
		
		/*
		
		
		 viewPager
	        .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	            actionbar.setSelectedNavigationItem(position);
	            }
	        });
		
		*/
		/*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.pager, new PlaceholderFragment()).commit();
		}
		*/
		// Vibrator manager = (Vibrator)
		// getSystemService(Context.VIBRATOR_SERVICE);
		// manager.vibrate(3000);
		SensorManager manager1 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor sen = manager1.getDefaultSensor(Sensor.TYPE_LIGHT);

		manager1.registerListener(this, sen, SensorManager.SENSOR_DELAY_FASTEST);

		Notification not = new Notification();
		not.vibrate = new long[] { 0, 100, 150, 100 };
		not.flags |= Notification.FLAG_INSISTENT;

		not.defaults |= Notification.DEFAULT_VIBRATE;

		not.ledARGB = 0xff0dff;

		not.ledOnMS = 1000;
		not.ledOffMS = 1000;

		not.flags |= Notification.FLAG_SHOW_LIGHTS;
		//NotificationManager nman = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
		// nman.notify(1, not);
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//float value = event.values[0];

		// Log.i("deub",value + "");
	}

	



}
