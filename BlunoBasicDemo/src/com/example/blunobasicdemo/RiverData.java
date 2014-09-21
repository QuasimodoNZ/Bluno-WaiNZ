package com.example.blunobasicdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.json.*;
import android.content.Context;
import android.location.Location;
import java.text.ParseException;
import java.util.Locale;


public class RiverData {
	
	double lat,lon, alt;
	Date readingDate;
	float temperature = Float.NaN;
	float conductivity = Float.NaN;
	String ID, session, status;
	Context context;
	UserLocationTracker track;
	boolean sent;
	
	public RiverData(Context c){
		//TODO Initial state with no JSON
		this.context = c;
	}

	public RiverData(Context c, JSONObject j){
		this.context = c;
		this.RiverDataJSON(c,j);
	}
	
	public RiverData(Context c, JSONObject j, UserLocationTracker t){
		this.track = t;
		this.context = c;
		this.RiverDataJSON(c,j);
		lat = t.getLat();
		lon = t.getLon();
	}
	
	public void RiverDataJSON(Context context, JSONObject j){
		String date_i = null;
		String conductivity_i = null;
		String temperature_i = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss a", Locale.getDefault());
		try{
			//Get strings from JSON
			status = j.getString("status");//assuming test status will be in data packet
			ID = j.getString("id");
			session = j.getString("session");
			date_i = j.getString("time");
		    conductivity_i = j.getString("ec");
			temperature_i = j.getString("temp");
			sent = j.getBoolean("sent");
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
	
	public void setupGPS(){
		//TODO make sure this shit works
		track = new UserLocationTracker(context);
		Location l = track.getLocation();
		lat = l.getLatitude();
		lon = l.getLongitude();
		alt = l.getAltitude();
	}
	
	public boolean getSent(){
		return sent;
	}

	public float getTemperature() {
		return temperature;
	}
	
	public float getConductivity() {
		return conductivity;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public double getAlt(){
		return alt;
	}
	
	public UserLocationTracker getLocation(){
		return track;
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
