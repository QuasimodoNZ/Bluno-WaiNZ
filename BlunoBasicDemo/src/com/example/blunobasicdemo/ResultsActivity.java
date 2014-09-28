package com.example.blunobasicdemo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ResultsActivity extends FragmentActivity {

	//BUTTONS
	private Button submitData;
	private Button discardData;

	//DATA
	private JSONObject jason;

	private Map<String, String> data = new HashMap<String, String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		Intent intent = getIntent();

		try {
			jason = new JSONObject(intent.getStringExtra("RiverData"));
			this.parseJSON(jason);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ImageView imageview = (ImageView) findViewById(R.id.image_results_view);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tomassobek);

		imageview.setImageBitmap(
			    bitmap);

		ProgressBar condPb = (ProgressBar) this.findViewById(R.id.conductivity_progress);
		ProgressBar tempPb = (ProgressBar) this.findViewById(R.id.temperature_progress);

		Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);

		an.setFillAfter(true);
		condPb.startAnimation(an);
		tempPb.startAnimation(an);


		//MAP
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		UserLocationTracker t = new UserLocationTracker(getApplicationContext());
		t.getLocation();

		MapFragment map = new MapFragment();

		map.lat = t.getLat();
		map.lon = t.getLon();
		
		fragmentTransaction.add(R.id.results_map_frame, map);
		fragmentTransaction.commit();

		//SUBMIT BUTTON LISTENER
		submitData = (Button) findViewById(R.id.submit_data_button);
		submitData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//TODO Send data to the website
			}
		});

		//DISCARD BUTTON LISTENER
		discardData = (Button) findViewById(R.id.discard_data_button);
		discardData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO do we need some kind of check ("Are you sure you want to delete this data")

				//Remove the submission
				SubmissionSaver.removeEntry(Integer.parseInt(data.get("entryID")), ResultsActivity.this);

				//Redirect the user back to the history page?
				//TODO or should this be the main page?
				Intent i = new Intent(ResultsActivity.this, HistoryActivity.class);
				startActivity(i);
			}
		});

	}

	/**
	 * Takes the given JSON object and puts it into the map for use in the activity
	 * @param json The object containing the results data
	 * @throws JSONException
	 */
	private void parseJSON(JSONObject json) throws JSONException {
		this.data.put("conductivity", json.getString("conductivity"));
		this.data.put("temperature", json.getString("temperature"));
		this.data.put("deviceID", json.getString("deviceID"));
		this.data.put("entryID", json.getString("entryID"));
		this.data.put("time", json.getString("time"));
		this.data.put("date", json.getString("date"));
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
