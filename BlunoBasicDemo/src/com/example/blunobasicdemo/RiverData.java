package com.example.blunobasicdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.json.*;
import android.content.Context;
import android.location.Location;
import java.text.ParseException;


public class RiverData {
	
	float lat,lon;
	Date readingDate;
	float temperature;
	float conductivity;
	String ID, session;
	Context context;
	
	public RiverData(Context context){
		this.context = context;
	}

	public void RiverDataErr(){
		//TODO error state - empty one
	}
	
	public void RiverDataJSON(JSONObject j){
		String date_i = null;
		String conductivity_i = null;
		String temperature_i = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss a");
		try{
			//Get strings from JSON
			ID = j.getString("id");
			session = j.getString("session");
			date_i = j.getString("time");
		    conductivity_i = j.getString("ec");
			temperature_i = j.getString("temp");
		}
		catch (JSONException e){
			//TODO log error
			e.printStackTrace();
		}
		try{
			//Convert the ones that aren't strings
			readingDate = (Date)formatter.parse(date_i);
			conductivity = Float.parseFloat(conductivity_i);
			temperature = Float.parseFloat(temperature_i);
		}
		catch (ParseException e){
			//TODO log error
			e.printStackTrace();
		}
		catch (NumberFormatException e){
			//TODO log error
			e.printStackTrace();
		}
	}
	
	public void RiverDataComplete(JSONObject j, UserLocationTracker t){
		this.RiverDataJSON(j);
		lat = t.getLat();
		lon = t.getLon();
	}
	
	public void setupGPS(){
		//TODO make sure this shit works
		UserLocationTracker t = new UserLocationTracker(context);
		Location l = t.getLocation();
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
