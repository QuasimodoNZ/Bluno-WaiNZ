package com.example.blunobasicdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SubmissionSaver {
	private final static String PREFERENCES_KEY = "submissionData";

	public static void saveSubmission(JSONObject j, Context c) {
		// TODO Auto-generated method stub

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		String jasonArray = prefs.getString(PREFERENCES_KEY, "[]");

		try {
			JSONArray save = new JSONArray(jasonArray);

			save.put(j);

			Editor edit = prefs.edit();

			edit.putString(PREFERENCES_KEY, save.toString());

			if (!edit.commit()) {
				// TODO report error
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JSONArray getJsonArray(Context c) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);
		try {
			return new JSONArray(prefs.getString(PREFERENCES_KEY, "Fail"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
