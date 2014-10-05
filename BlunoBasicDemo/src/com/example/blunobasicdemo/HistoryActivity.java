package com.example.blunobasicdemo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HistoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		ListView listView = (ListView)findViewById(R.id.list);

		Context c = getApplicationContext();

		SubmissionSaver s = new SubmissionSaver();
		JSONArray json = s.getJsonArray(c);

		//json = new JSONArray();

		List<RiverData> models = new ArrayList<RiverData>(json.length());

		for(int i = 0; i < json.length(); i++){
			try {
				models.add(new RiverData(c, json.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		HistoryAdapter adapter = new HistoryAdapter(this, models);

		listView.setAdapter(adapter);

		adapter.notifyDataSetChanged();

		// Sets the back button so that it returns to the previous page
				((Button) findViewById(R.id.backButton))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onBackPressed();

						}
				});

	}
}
