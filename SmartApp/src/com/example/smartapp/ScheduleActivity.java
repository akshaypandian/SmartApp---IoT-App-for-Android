package com.example.smartapp;
/*
 * Sends command to the home server to start scheduling for the user.
 * Scheduled data is sent back onto the mobile device from where the mobile sends this
 * information to the Utility server. Utility server checks if the schedule is okay 
 * with the global schedule of all users. If there are any conflicts, the Utility 
 * server will reschedule and send that data to the mobile app.
 * The user can either stick to his schedule or accept the schedule sent by Utility
 * server.
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.transition.Scene;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	String ip;
	TextView resultText;
	String[] line;
	String[] lineModified;
	String[] resultToUtility;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		ip = getIntent().getExtras().getString("IP");
		resultText = (TextView) findViewById(R.id.textView1);
		new GetScheduledData().execute();
	}

	private class GetScheduledData extends AsyncTask<Void, Void, Void> {
		String resultFromServer;

		private void getData() throws ClientProtocolException, IOException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip + "/homeserv.php");
			ParseUser user = ParseUser.getCurrentUser();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("Username", user
					.getObjectId()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			resultFromServer = EntityUtils.toString(entity);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				getData();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		/*
		 * Retrieve scheduled data from home server
		 */
		@Override
		protected void onPostExecute(Void result) {
			if (resultFromServer.isEmpty())
				Toast.makeText(ScheduleActivity.this, "Return failed",
						Toast.LENGTH_SHORT).show();
			else {
				line = resultFromServer.split(Pattern.quote("\n"));
				resultToUtility = line[0].split(",");

				resultText.setText(line[1] + "\n" + line[2] + "\n" + line[3]
						+ "\n" + line[4] + "\n" + line[5] + "\n" + line[6]
						+ "\n" + line[7] + "\n" + line[8] + "\n" + line[9]
						+ "\n" + line[10] + "\n" + line[11] + "\n" + line[12]
						+ "\n" + line[13] + "\n" + line[14] + "\n" + line[15]
						+ "\n" + line[16] + "\n" + line[17] + "\n" + line[18]
						+ "\n" + line[19] + "\n" + line[20] + "\n" + line[21]
						+ "\n" + line[22] + "\n" + line[23] + "\n" + line[24]);
				new SendDataToUtility().execute();
			}
		}

	}
	/*
	 * Send received schedule from home server to utility server
	 */
	private class SendDataToUtility extends AsyncTask<Void, Void, Void> {
		String resultFromServer;
		String[] resultFinal;

		private void sendData() throws ClientProtocolException, IOException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip + "/utility.php");

			ParseUser user = ParseUser.getCurrentUser();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("Username", user
					.getObjectId()));
			for (int i = 0; i < 24; i++) {
				nameValuePairs.add(new BasicNameValuePair(i + "",
						resultToUtility[i]));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			resultFromServer = EntityUtils.toString(entity);
			resultFinal = resultFromServer.split(":");
			Log.d("demo", resultFromServer);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				sendData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ScheduleActivity.this);
			pd.setMessage("Getting data from server...");
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}
		/*
		 * Receive data from Utility server
		 */
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			if (resultFinal[0].equals("exceeds")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ScheduleActivity.this);
				double minus = Double.parseDouble(resultFinal[1]) / 1000;
				builder.setTitle("Message From Utility Server : ")
						.setMessage(
								"Your price is exceeded by " + minus)
						.setCancelable(false)
						.setPositiveButton("Commit",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(ScheduleActivity.this,
												"Commit Successful!",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("Reschedule",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new UtilityReschedule().execute();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				Toast.makeText(ScheduleActivity.this,
						"Scheduling Successfull!", Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class UtilityReschedule extends AsyncTask<Void, Void, Void> {
		String resultFromUtility;
		String[] resultBack;

		void sendDatatoUtilityAgain() throws ClientProtocolException,
				IOException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip
					+ "/utilityserv.php");

			ParseUser user = ParseUser.getCurrentUser();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("Username", user
					.getObjectId()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			resultFromUtility = EntityUtils.toString(entity);
			Log.d("ResultFrom", resultFromUtility);
			resultBack = resultFromUtility.split("\n");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				sendDatatoUtilityAgain();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			resultText.setText(resultBack[1] + "\n" + resultBack[2] + "\n"
					+ resultBack[3] + "\n" + resultBack[4] + "\n"
					+ resultBack[5] + "\n" + resultBack[6] + "\n"
					+ resultBack[7] + "\n" + resultBack[8] + "\n"
					+ resultBack[9] + "\n" + resultBack[10] + "\n"
					+ resultBack[11] + "\n" + resultBack[12] + "\n"
					+ resultBack[13] + "\n" + resultBack[14] + "\n"
					+ resultBack[15] + "\n" + resultBack[16] + "\n"
					+ resultBack[17] + "\n" + resultBack[18] + "\n"
					+ resultBack[19] + "\n" + resultBack[20] + "\n"
					+ resultBack[21] + "\n" + resultBack[22] + "\n"
					+ resultBack[23] + "\n" + resultBack[24]);
		}

	}
}
