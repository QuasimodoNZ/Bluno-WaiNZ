package com.example.blunobasicdemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {
	private Button buttonScan;
	private Button buttonSerialSend;
	private EditText serialSendText;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess(); // onCreate Process by BlunoLibrary

		serialBegin(115200); // set the Uart Baudrate on BLE chip to 115200

		serialReceivedText = (TextView) findViewById(R.id.serialReveicedText); 
		// initial the EditText of the received data
		//serialSendText = (EditText) findViewById(R.id.serialSendText); 
		// initial the EditText of the sending data

		buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend); 
		// initial the button for sending the data
		buttonSerialSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (connectionState != connectionStateEnum.isConnected) {
					// TODO tell user not connected

				}

				JSONObject j = new JSONObject();
				try {
					j.put("cmd", "init");
					j.put("dev", "AD");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				serialSend(j.toString()); // send the data to the BLUNO
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan); 
		// initial the button for scanning the BLE device
		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess(); 
				// Alert Dialog for selecting the BLE device
			}
		});

		for (int i = 0; i < 10; i++) {
			JSONObject j = null;
			try {
				j = new JSONObject("{\"test" + i + "\":\"succss\"}");
				SubmissionSaver.saveSubmission(j, this);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void onResume() {
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess(); // onResume Process by BlunoLibrary
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data); 
		// onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPauseProcess(); 
		// onPause Process by BlunoLibrary
	}

	protected void onStop() {
		super.onStop();
		onStopProcess(); 
		// onStop Process by BlunoLibrary
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDestroyProcess(); 
		// onDestroy Process by BlunoLibrary
	}
	
	@Override
	public void onConnectionStateChange(connectionStateEnum theConnectionState) {
		// Once connection state changes, this function will be called
		switch (theConnectionState) { 
		// Four connection state
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
				// TODO was is a failure or success?
				if (status.equalsIgnoreCase("idle")) {
					wizardState = WizardState.complete;
					// TODO send start message
					sendStartMessage();
				} else if (status.equalsIgnoreCase("fatal")) {
					// TODO show error
				} else if (status.equalsIgnoreCase("bt4le")) {
					// TODO Bluetooth communication error / JSON error
				} else if (status.equalsIgnoreCase("temp")) {
					// TODO Temperature status error
				} else if (status.equalsIgnoreCase("ec")) {
					// TODO Electric conductivity error
				} else if (status.equalsIgnoreCase("ph")) {
					// TODO ph sensor error
				} else if (status.equalsIgnoreCase("water level low")) {
					// TODO sensor is not immersed in water
				} else if (status.equalsIgnoreCase("busy")) {
					// TODO device is busy
				} else {
					// TODO throw exception for unsupported state.
				}

			} else if (WizardState.complete == wizardState) {
				// TODO sava data
				SubmissionSaver.saveSubmission(j, this);
				// we should disregard any other information we receive
				// TODO what do you want to do now
				// Temperature between 0->25 Degrees are green
				// Temperatures between 25->30 degrees are amber
				// Temperatures over 30 degrees are red

				
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

	private void sendStartMessage() {
		JSONObject j = new JSONObject();
		try {
			j.put("cmd", "test");
			j.put("session", "AD"); // TODO what do we put in here
			String gpsData = ""; // TODO get gps data and format it for json
			j.put("gps_data", gpsData);
			j.put("time", ""); // Need to get the current time
		} catch (JSONException e) {
			e.printStackTrace();
		}
		serialSend(j.toString()); // send the data to the BLUNO
	}

	

}