package com.example.blunobasicdemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

public class ResultsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		Intent intent = getIntent();

		try {
			JSONObject jason = new JSONObject(intent.getStringExtra("HEY"));
		} catch (JSONException e) {
			e.printStackTrace();
		}


		ProgressBar condPb = (ProgressBar) this.findViewById(R.id.conductivity_progress);
		ProgressBar tempPb = (ProgressBar) this.findViewById(R.id.temperature_progress);

		Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);

		an.setFillAfter(true);
		condPb.startAnimation(an);
		tempPb.startAnimation(an);



		FragmentManager fragmentManager = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		UserLocationTracker t = new UserLocationTracker(getApplicationContext());

		t.getLocation();

		MapFragment map = new MapFragment();

		map.lat = t.getLat();

		map.lon = t.getLon();

		fragmentTransaction.add(R.id.results_map_frame, map);

		fragmentTransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
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
}
