package com.example.blunobasicdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SubmissionSaver {
	private final static String PREFERENCES_KEY = "submissionData";

	public static String saveSubmission(JSONObject j, Context c) {
		// TODO Auto-generated method stub
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		String jasonArray = prefs.getString(PREFERENCES_KEY, "[]");

		try {
			JSONArray save = new JSONArray(jasonArray);

			save.put(j);

			prefs.edit().putString(PREFERENCES_KEY, save.toString());

			if (!prefs.edit().commit()) {
				return "Failed to add to the shared preferences";
				// TODO report error
			}
			return "Succesfully  added data";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Ultimate failure";
	}

	public static String getJson(Context c) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);
		return prefs.getString(PREFERENCES_KEY, "Fail");
		// TODO Auto-generated method stub

	}

}
