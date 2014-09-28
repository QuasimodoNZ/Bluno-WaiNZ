package com.example.blunobasicdemo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
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

		json = new JSONArray();

		List<RiverData> models = new ArrayList<RiverData>(json.length());


		Random r = new Random();
		for(int j = 0; j < 30; j++){
			JSONObject js = new JSONObject();
			try {

				js.put("id", r.nextInt(100));
				js.put("session", r.nextLong());
				js.put("time", "2014-09-24 11:11:11.111");
				js.put("ec", r.nextInt(1));
				js.put("temp", r.nextInt(25));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			json.put(js);

		}

		for(int i = 0; i < json.length(); i++){
			try {
				System.out.println(json.getJSONObject(i).toString());
				models.add(new RiverData(c, json.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		HistoryAdapter adapter = new HistoryAdapter(this, models);

		listView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
	}
}
