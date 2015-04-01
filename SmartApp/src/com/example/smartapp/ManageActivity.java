package com.example.smartapp;
/*
 * Activity to add an appliance and various parameters to
 * enable the type of scheduling the user wants like start time
 * of the appliance, how long it should run, end time of the appliance,
 * wattage requirements, whether the scheduling algorithm can deviate
 * from the time specified or not
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ManageActivity extends Activity {

	EditText wattage, startTime, endTime, runTime, appName;
	Button ok, home;
	List<Appliance> list;
	String ip;
	RadioGroup radio;
	RadioButton hard;
	RadioButton soft;
	ParseObject appliance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		ip = getIntent().getExtras().getString("IP");
		// user = (TextView) findViewById(R.id.textView1);
		radio = (RadioGroup) findViewById(R.id.radio);
		appName = (EditText) findViewById(R.id.editText5);
		hard = (RadioButton) findViewById(R.id.hard);
		soft = (RadioButton) findViewById(R.id.soft);
		// user.setText("Hello, " +
		// ParseUser.getCurrentUser().getString("Name"));
		wattage = (EditText) findViewById(R.id.editText1);
		startTime = (EditText) findViewById(R.id.editText2);
		endTime = (EditText) findViewById(R.id.editText3);
		runTime = (EditText) findViewById(R.id.editText4);

		ok = (Button) findViewById(R.id.button2);
		home = (Button) findViewById(R.id.button3);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isConnectedOnline()) {
					if (appName.getEditableText().toString().isEmpty()
							|| wattage.getEditableText().toString().isEmpty()
							|| startTime.getEditableText().toString().isEmpty()
							|| endTime.getEditableText().toString().isEmpty()
							|| runTime.getEditableText().toString().isEmpty()) {
						Toast.makeText(ManageActivity.this,
								"Enter all fields!", Toast.LENGTH_SHORT).show();

					} else {
						new MyAsyncTask().execute();
					}

				}

			}
		});

		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManageActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private boolean isConnectedOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	/*
	 * Async task to add appliance and corresponding data on cloud, as well as send the details
	 * to server which will run the scheduling as per user requirements and send that data
	 * to the Utility server
	 */
	private class MyAsyncTask extends AsyncTask<Void, Void, Double> {
		String Hadoopout;

		public void postData() {
			// Create a new HttpClient and Post Header
			appliance = new ParseObject("Appliance");
			appliance.put("Name", appName.getEditableText().toString());
			appliance.put("Watt", wattage.getEditableText().toString());
			appliance.put("Start", startTime.getEditableText().toString());
			appliance.put("End", endTime.getEditableText().toString());
			appliance.put("Run", runTime.getEditableText().toString());
			ParseUser user = ParseUser.getCurrentUser();
			appliance.put("User", user);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip + "/comm.php");

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Username", user
						.getObjectId()));
				nameValuePairs.add(new BasicNameValuePair("ApplianceName",
						appName.getEditableText().toString()));
				switch (radio.getCheckedRadioButtonId()) {
				case R.id.hard:
					nameValuePairs.add(new BasicNameValuePair("Constraints",
							"HC"));
					appliance.put("Constraints", "HC");
					break;
				case R.id.soft:
					nameValuePairs.add(new BasicNameValuePair("Constraints",
							"SC"));
					appliance.put("Constraints", "SC");
					break;
				}
				appliance.saveInBackground();
				nameValuePairs.add(new BasicNameValuePair("Watt", wattage
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("StartTime",
						startTime.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("StopTime", endTime
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("RunTime", runTime
						.getEditableText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}

		@Override
		protected Double doInBackground(Void... params) {
			postData();
			return null;
		}

		@Override
		protected void onPostExecute(Double result) {
			appName.setText("");
			wattage.setText("");
			startTime.setText("");
			endTime.setText("");
			runTime.setText("");
			radio.check(-1);
		}

	}
}
