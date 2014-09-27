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

	/**
	 * Saves the given JSON object
	 * @param j The JSON object containing the data for the particular submission
	 * @param c The context for the app
	 */
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

	/**
	 * Gets all of the results stored
	 * @param c The context for the app
	 * @return A JSON array populated with JSON objects which describe all of the results stored on the device. 
	 */
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
	
	/**
	 * Gets and returns the entry specified by the given entry ID
	 * @param entryID The ID number of the entry to be returned
	 * @param c The context for the app
	 * @return The JSON object that contains the results for the entry of the given ID
	 */
	public static JSONObject getEntry(int entryID, Context c) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);
		JSONArray saveData = null;
		int i = 0;
		JSONObject j;
		try {
			saveData = new JSONArray(prefs.getString(PREFERENCES_KEY, "Fail"));
			while((j = saveData.getJSONObject(i)) != null){
				if(j.getInt("entryID") == entryID){
					return j;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * Removes the entry of the results specified by the provided entry ID
	 * @param entryID The ID number of the entry to be removed
	 * @param c The context for the app
	 * @return true if the entry was successfully removed, otherwise false
	 */
	public static boolean removeEntry(int entryID, Context c){
		//Get the preferences (where the data is stored)
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		JSONArray saveData = null;
		JSONObject entry = null;
		int i = 0;
		
		try {
			//Get the array of data entries
			saveData = new JSONArray(prefs.getString(PREFERENCES_KEY, "Fail"));
			
			//look through the save data for the entry with the correct ID
			while((entry = saveData.getJSONObject(i)) != null){
				if(entry.getInt("entryID") == entryID){
					//if an object is returned, it has been removed and true is returned
					return saveData.remove(i) != null;
				}
			}
			i++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
