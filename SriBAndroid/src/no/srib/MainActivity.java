package no.srib;



import no.srib.adapter.TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;

import android.app.Notification;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;


@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements 
		SensorEventListener,  android.support.v7.app.ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter adapter;
	private ActionBar actionbar;
	public Fragment frag = new Fragment();

	//
	private String[] tabs = { "Radio", "Podcast list" };
	private int[] icon = {R.drawable.ic_tab_radio_unselected,R.drawable.ic_tab_example_unselected};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionbar = getSupportActionBar();
		adapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		
		actionbar.setDisplayShowCustomEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		
		//actionbar.hide();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Adding tabs
		for(int i = 0; i < tabs.length; i++){
			actionbar.addTab(actionbar.newTab().setText(tabs[i]).setTabListener(this).setIcon(icon[i]));
			
		}
		
		 viewPager
	        .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	            actionbar.setSelectedNavigationItem(position);
	            }
	        });
		
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

	

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


}
