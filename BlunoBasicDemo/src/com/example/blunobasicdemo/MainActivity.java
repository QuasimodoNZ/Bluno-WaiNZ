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

public class MainActivity  extends BlunoLibrary {
	private Button buttonScan;
	private Button buttonSerialSend;
	private EditText serialSendText;
	private TextView serialReceivedText;
	private connectionStateEnum connectionState;
	private WizardState wizardState;
	
	public enum WizardState{initial, idle, complete};
	public enum ResponseState{idle, fatal, bt4le, temp, ec, ph, water, busy};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        onCreateProcess();														//onCreate Process by BlunoLibrary
        
        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200
		
        serialReceivedText=(TextView) findViewById(R.id.serialReveicedText);	//initial the EditText of the received data
        serialSendText=(EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data
        
        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);		//initial the button for sending the data
        buttonSerialSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(connectionState != connectionStateEnum.isConnected){
					//TODO tell user not connected
				}
				
				JSONObject j = new JSONObject();
				try {
					j.put("cmd", "init");
					j.put("dev", "AD");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				serialSend(j.toString());				//send the data to the BLUNO
			}
		});
        
        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
			}
		});
	}

	protected void onResume(){
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }
	
	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
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
	public void onSerialReceived(String data) {							//Once connection data received, this function will be called
		// TODO Auto-generated method stub
		JSONObject j;
		try {
			
		j = new JSONObject(data);

		if(WizardState.initial == wizardState){
			//TODO was is a failure or success?
			String status = j.getString("status");
			
			if (status.equalsIgnoreCase("fatal")){
				//TODO show error
			} else if (status.equalsIgnoreCase("idle")){
				wizardState = WizardState.idle;
			} else {
				//TODO throw exception for unsupported state.
			}
		} else if(WizardState.idle == wizardState){
			
			//TODO was is a failure or success?
		} else if(WizardState.complete == wizardState){
			//TODO what do you want to do now
		} else {
			//TODO throw an exception for unsupported wizard state.
		}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//go do next action
		serialReceivedText.append(data);							//append the text into the EditText
		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
					
	}

}