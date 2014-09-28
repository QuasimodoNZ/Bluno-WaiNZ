package com.example.blunobasicdemo;

import org.json.*;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class RiverData {

	Float lat, lon, temperature, conductivity;

	String readingDate;

	String id, session;

	Context context;

	public RiverData(Context c, JSONObject j){
		this.context = c;
		this.PopulateModel(j);
	}


	 OnClickListener listener = new OnClickListener(){ // the book's action
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
	};

	public void PopulateModel(JSONObject j){

		try{
			//Get strings from JSON
			//String status = j.getString("status");//assuming test status will be in data packet
			id = j.getString("id");
			session = j.getString("session");
			readingDate = j.getString("time");
		    conductivity = Float.parseFloat(j.getString("ec"));
			temperature = Float.parseFloat(j.getString("temp"));

			System.out.printf("Date: %s, Conductivity: %f, Temperature: %f \n", readingDate, conductivity, temperature);

		}
		catch (JSONException e){
			e.printStackTrace();
		}
	}

	public float getTemperature() {
		return temperature;
	}

	public float getConductivity() {
		return conductivity;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public String getReadingDate() {
		return readingDate;
	}

	public String getId() {
		return id;
	}

	public String getSession() {
		return session;
	}

	public JSONObject toJson(){
		JSONObject jason = new JSONObject();
		try {
			jason.put("id", id);
			jason.put("session", session);
			jason.put("time", readingDate);
			jason.put("ec", conductivity);
			jason.put("temp", temperature);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jason;
	}
}
