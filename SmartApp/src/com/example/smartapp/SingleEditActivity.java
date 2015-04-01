package com.example.smartapp;
 

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SingleEditActivity extends Activity {

	EditText appName, start, end, run, watt;
	String c;
	RadioGroup radio;
	RadioButton hard;
	RadioButton soft;
	String objID;
	Button ok;
	String ip;
	ParseObject appliance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_edit);
		appName = (EditText) findViewById(R.id.editText5);
		start = (EditText) findViewById(R.id.editText2);
		end = (EditText) findViewById(R.id.editText3);
		run = (EditText) findViewById(R.id.editText4);
		watt = (EditText) findViewById(R.id.editText1);
		objID = getIntent().getExtras().getString("ObjID");
		ok = (Button) findViewById(R.id.button2);
		radio = (RadioGroup) findViewById(R.id.radio);
		hard = (RadioButton) findViewById(R.id.hard);
		soft = (RadioButton) findViewById(R.id.soft);

		String n = getIntent().getExtras().getString("Name");
		String r = getIntent().getExtras().getString("Run");
		String st = getIntent().getExtras().getString("Start");
		String e = getIntent().getExtras().getString("End");
		String w = getIntent().getExtras().getString("Watt");
		ip = getIntent().getExtras().getString("IP");

		appName.setText(n);
		start.setText(st);
		end.setText(e);
		run.setText(r);
		watt.setText(w);

		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Appliance");
		query.whereEqualTo("User", user);
		query.getInBackground(objID, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					object.deleteInBackground();
				} else {
					Toast.makeText(SingleEditActivity.this,
							"Could not locate the object!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (appName.getEditableText().toString().isEmpty()
						|| start.getEditableText().toString().isEmpty()
						|| end.getEditableText().toString().isEmpty()
						|| run.getEditableText().toString().isEmpty()
						|| watt.getEditableText().toString().isEmpty()) {
					Toast.makeText(SingleEditActivity.this,
							"Enter all fields!", Toast.LENGTH_SHORT).show();
				} else {
					new MyAsyncTask().execute();
				}
			}
		});

	}

	private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			postData();
			return null;
		}

		public void postData() {
			appliance = new ParseObject("Appliance");
			appliance.put("Name", appName.getEditableText().toString());
			appliance.put("Watt", watt.getEditableText().toString());
			appliance.put("Start", start.getEditableText().toString());
			appliance.put("End", end.getEditableText().toString());
			appliance.put("Run", run.getEditableText().toString());
			ParseUser user = ParseUser.getCurrentUser();
			appliance.put("User", user);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip + "/edit.php");

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
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
				nameValuePairs.add(new BasicNameValuePair("Watt", watt
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("StartTime", start
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("StopTime", end
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("RunTime", run
						.getEditableText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			finish();
		}

	}

}
