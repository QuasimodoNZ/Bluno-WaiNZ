package com.example.blunobasicdemo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {
	public static final String JSON_MESSAGE = "INTENT_MESSAGE_COLLECTED_DATA";
	private Button buttonScan;
	private Button historyButton;
	private Button connectToDevice;
	private Button testWaterQuality;
	private TextView connectionUpdates;
	private TextView serialReceivedText;
	private connectionStateEnum connectionState;
	private WizardState wizardState;
	private RiverData river;
	private UserLocationTracker track;

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

		String currTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault()).format(Calendar.getInstance().getTime());

		serialBegin(115200);

		TextView connectionUpdate = (TextView) findViewById(R.id.connection_updates);

		connectionUpdate.setAlpha(0.5f);

		wizardState = WizardState.initial;

		connectToDevice = (Button) findViewById(R.id.connect_button);

		connectToDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject jason = new JSONObject();
				try {
					jason.put("cmd", "init");
					jason.put("dev", "ad");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				serialSend(jason.toString());

			}
		});

		testWaterQuality = (Button) findViewById(R.id.start_button);

		testWaterQuality.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				JSONObject j = new JSONObject();
				UserLocationTracker t = new UserLocationTracker(
						getApplicationContext());
				t.getLocation();

				try {
					j.put("cmd", "test");
					j.put("session", "ad");
					String gpsData = String.valueOf(t.getLat()) + " - "
							+ String.valueOf(t.getLon());
					j.put("gps", gpsData);
					String currTime = new SimpleDateFormat("yyyyMMdd_HHmmss",
							Locale.getDefault()).format(Calendar.getInstance()
							.getTime());

					j.put("time", currTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				serialSend(j.toString());
				// connectionUpdates.setText("Initialising device");
			}
		});

		buttonScan = (Button) findViewById(R.id.scan_button);

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonScanOnClickProcess();
			}
		});

		historyButton = (Button) findViewById(R.id.history_button);

		historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(MainActivity.this, HistoryActivity.class);

				startActivity(i);

			}

		});

		for (int i = 0; i < 10; i++) {
			JSONObject jason = null;
			try {
				jason = new JSONObject("{\"test" + i + "\":\"succss\"}");
				SubmissionSaver.saveSubmission(jason, this);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// Sets the back button so that it returns to the previous page
		((Button) findViewById(R.id.backButton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();

					}
				});
	}

	protected void onResume() {
		super.onResume();
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

	String message = "";

	@Override
	public void onSerialReceived(String data) {
		// Once connection data received, this function will be called
		if (!data.contains("stop")) {
			message += data;
		} else {

			message += data;

			message = message.split("&")[0];

			JSONObject j;

			try {
				j = new JSONObject(message);

				if (WizardState.initial == wizardState) {
					String status = j.getString("status");

					if (status.equalsIgnoreCase("fatal")) {
						wizardState = WizardState.error;
						// connectionUpdates.setText("Error in initialising device");
					} else if (status.equalsIgnoreCase("idle")) {
						wizardState = WizardState.idle;
						// connectionUpdates.setText("Device is idle, please start the test");
					} else {
						// TODO throw exception for unsupported state.
					}

				} else if (WizardState.idle == wizardState) {
					String status = j.getString("status");

					String message = "";

					if (status.equalsIgnoreCase("complete")) {

						wizardState = WizardState.complete;

						SubmissionSaver.saveSubmission(j, this);

						Intent i = new Intent(this, ResultsActivity.class);

						i.putExtra("RiverData", j.toString());

						startActivity(i);

					} else if (status.equalsIgnoreCase("busy")) {
						message = "Device is currently busy";
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
							message = "Device not submerged in water deeply enough, please restart test";
						} else {
							message = "An unknown exception has occurred, please restart the test";
						}
					}

					// connectionUpdates.setText(message);

				} else {
					// TODO do we want the user to see this or remove after
					// debugging?
					String message = "Unrecognised data received from device";
					connectionUpdates.setText(message);
					// Reset the device when it sends us unknown data
					JSONObject n = new JSONObject();
					n.put("cmd", "reset");
					serialSend(n.toString());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			message = "";

		}

	}

	@Override
	public void onConectionStateChange(
			connectionStateEnum theconnectionStateEnum) {
		switch (theconnectionStateEnum) { // Four connection state

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
}