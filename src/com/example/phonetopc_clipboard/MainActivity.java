package com.example.phonetopc_clipboard;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/* TO-DO LIST
Use the IP and port field

*/

public class MainActivity extends Activity {

	Context context = this;
	
	Intent intent_takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	private static final int REQUEST_CAPTURE_IMAGE = 133;
	String temp_picture_filename = "temp.jpg";
	File temp_picture_path = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	File temp_picture_dirname;

	Boolean connectionStatus = false;
	String ipAddr;
	int portNumber;

	EditText editTextIP;
	EditText editTextPort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button mainButton = (Button) findViewById(R.id.mainButton);
		editTextIP = (EditText) findViewById(R.id.editTextIP);
		editTextPort = (EditText) findViewById(R.id.editTextPort);

		mainButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				temp_picture_dirname = new File(temp_picture_path,
						temp_picture_filename);
				intent_takepic.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(temp_picture_dirname));
				startActivityForResult(intent_takepic, REQUEST_CAPTURE_IMAGE);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAPTURE_IMAGE) {
			sendPictToServer();
		} else if (resultCode == RESULT_CANCELED) {
			// User cancelled the image capture
			Toast.makeText(this, "User Cancelled", Toast.LENGTH_LONG).show();
		} else {
			// Image capture failed, advise user
			Toast.makeText(this, "Error in Camera", Toast.LENGTH_LONG).show();
		}
	}

	private void sendPictToServer() {
		
		ipAddr = editTextIP.getText().toString();
		portNumber = Integer.parseInt(editTextPort.getText().toString());
		
		ClientThread connection = new ClientThread(context, portNumber, ipAddr,
				temp_picture_path.getPath(), temp_picture_filename);
		connection.start();
		try {
			connection.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
