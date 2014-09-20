package com.example.blunobasicdemo;

import android.content.Context;

//Each method returns a number. 0 for error state, 1 for green, 2 for orange and 3 for red
public class RiverHealthCalculator{
	RiverData river;
	
	public RiverHealthCalculator(Context context){
		river = new RiverData(context);
	}
	
	public int CompareTemperature(){
		float temperature = river.getTemperature();
		if (temperature < 25.0){
			return 1;
		}
		else if (temperature < 30){
			return 2;
		}
		else if (temperature != Float.NaN){
			return 3;
		}
		return 0;
	}
	
	public int CompareConductivity(){
		float conductivity = river.getConductivity();
		if (conductivity != 0){
			return 3;
			}
		else if (conductivity != Float.NaN){
			return 1;
			}
		return 0;
	}
	
	public float CompareHealth(){
		int con = this.CompareConductivity();
		int tem = this.CompareTemperature();
		
		if (tem == 0 || con == 0){
			return 0;
		}
		else if (tem == 3 || con == 3){
			return 3;
		}
		else if (tem == 2){
			return 2;
		}
		else if (tem == 1 && con ==1){
			return 1;
		}
		else{
			return 0;
		}
	}
}
