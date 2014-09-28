package com.example.blunobasicdemo;

import java.text.SimpleDateFormat;

import org.json.*;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import java.text.ParseException;

import java.util.Date;


public class RiverData {

	Float lat, lon, temperature, conductivity;

	Date readingDate;

	String ID, session;
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
		String date_i = null;
		String conductivity_i = null;
		String temperature_i = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss a");

		try{
			//Get strings from JSON
			String status = j.getString("status");//assuming test status will be in data packet
			ID = j.getString("id");
			session = j.getString("session");
			date_i = j.getString("time");
		    conductivity_i = j.getString("ec");
			temperature_i = j.getString("temp");


			//Convert the ones that aren't strings
			readingDate = (Date)formatter.parse(date_i);
			conductivity = Float.parseFloat(conductivity_i);
			temperature = Float.parseFloat(temperature_i);

			System.out.printf("Date: %s, Conductivity: %f, Temperature: %f \n", readingDate, conductivity, temperature);

		}
		catch (JSONException e){
			e.printStackTrace();
		}
		catch (ParseException e){
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

	public Date getReadingDate() {
		return readingDate;
	}

	public String getID() {
		return ID;
	}

	public String getSession() {
		return session;
	}

}
