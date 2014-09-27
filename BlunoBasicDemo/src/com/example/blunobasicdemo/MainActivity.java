package com.example.blunobasicdemo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {
	private Button buttonScan;
	private Button connectToDevice;
	private Button testWaterQuality;
	private Button viewHistory;
	private EditText connectionUpdates;
	private TextView serialReceivedText;
	private connectionStateEnum connectionState;
	private WizardState wizardState;

	public enum WizardState {
		initial, idle, error, complete
	};

	public enum ResponseState {
		idle, fatal, bt4le, temp, ec, ph, water, busy
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		onCreateProcess();

		serialBegin(115200);

		connectToDevice = (Button) findViewById(R.id.connect_button);

		connectToDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject j = new JSONObject();
				UserLocationTracker t = new UserLocationTracker(getApplicationContext());
				t.getLocation();
				try {
					j.put("cmd", "test");
					j.put("session", "AD");
					String gpsData = String.valueOf(t.getLat())+" "+String.valueOf(t.getLon());
					j.put("gps_data", gpsData);
					String currTime = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
					j.put("time", currTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				serialSend(j.toString());
			}
		});
		
		viewHistory = (Button) findViewById(R.id.history_button);
		
		viewHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(MainActivity.this, HistoryActivity.class);

				startActivity(i);
			}
		});
/*
		testWaterQuality = (Button) findViewById(R.id.testWaterQuality);

		testWaterQuality.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				JSONObject jason = new JSONObject();
				try {
					jason.put("status", "idle");
				} catch (JSONException e){
					e.printStackTrace();
				}
				serialSend(jason.toString());
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan);

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonScanOnClickProcess();
			}
		});

		connectionUpdates = (EditText) findViewById(R.id.connectionUpdates);
*/
		for (int i = 0; i < 10; i++) {
			JSONObject jason = null;
			try {
				jason = new JSONObject("{\"test" + i + "\":\"succss\"}");
				SubmissionSaver.saveSubmission(jason, this);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	protected void onResume() {
		super.onResume();
		onResumeProcess(); // onResume Process by BlunoLibrary
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data); // onActivityResult
																// Process by
																// BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPauseProcess(); // onPause Process by BlunoLibrary
	}

	protected void onStop() {
		super.onStop();
		onStopProcess(); // onStop Process by BlunoLibrary
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDestroyProcess(); // onDestroy Process by BlunoLibrary
	}

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {
		switch (theConnectionState) { // Four connection state
		case isConnected:
			buttonScan.setText("Connected");
			break;
		case isConnecting:
			buttonScan.setText("Connecting");
			break;
		case isToScan:
			buttonScan.setText("Scan");
			break;
		case isScanning:
			buttonScan.setText("Scanning");
			break;
		case isDisconnecting:
			buttonScan.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String data) {
		// Once connection data received, this function will be called

		JSONObject j;
		try {

			j = new JSONObject(data);

			if (WizardState.initial == wizardState) {
				String status = j.getString("status");

				if (status.equalsIgnoreCase("fatal")) {
					wizardState = WizardState.error;
					// TODO show error
				} else if (status.equalsIgnoreCase("idle")) {
					wizardState = WizardState.idle;
				} else {
					// TODO throw exception for unsupported state.
				}

			} else if (WizardState.idle == wizardState) {
				String status = j.getString("status");

				String message;

				if (status.equalsIgnoreCase("idle")) {
					wizardState = WizardState.complete;
					message = "Device is idle, please start the test";
				} else if (status.equalsIgnoreCase("busy")) {
					message = "Devoce is currently busy";
				} else {

					wizardState = WizardState.error;

					if (status.equalsIgnoreCase("fatal")) {
						message = "A fatal error has occured";
					} else if (status.equalsIgnoreCase("bt4le")) {
						message = "There's a Bluetooth connection issue, please check device settings";
					} else if (status.equalsIgnoreCase("temp")) {
						message = "Temperature is to high/low please remove Bluetooth device from water";
					} else if (status.equalsIgnoreCase("ec")) {
						message = "Issue occured while measuring the electrical conductivity, please remove from water";
					} else if (status.equalsIgnoreCase("water level low")) {
						message = "Device not submerged in water deeply, please restart test";
					} else {
						message = "An unknown exception has occurred, please restart the test";
					}
				}
				connectionUpdates.setText(message);
			} else if (WizardState.complete == wizardState) {

				SubmissionSaver.saveSubmission(j, this);

				Intent i = new Intent(this, ResultsActivity.class);

				i.putExtra("data", j.toString());

				startActivity(i);
			} else {
				// TODO throw an exception for unsupported wizard state.
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// go do next action
		serialReceivedText.append(data); // append the text into the EditText
		// The Serial data from the BLUNO may be sub-packaged, so using a buffer
		// to hold the String is a good choice.

	}

}