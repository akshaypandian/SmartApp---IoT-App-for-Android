package com.example.smartapp;
/*
 * Activity to retrieve all appliances added by the user to database
 * and delete any appliance. Deleting will also reflect in the cloud 
 * database used. Used Parse.com db services.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteActivity extends Activity {

	ListView list;
	List<ApplianceList> appList;
	List<String> objID;
	String ip;
	String appName;
	ApplianceAdapter adapter;
	int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		list = (ListView) findViewById(R.id.listView1);
		objID = new ArrayList<String>();
		appList = new ArrayList<ApplianceList>();
		ip = getIntent().getExtras().getString("IP");
		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Appliance");
		query.whereEqualTo("User", user);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objects.size(); i++) {
						appList.add(new ApplianceList(objects.get(i).getString(
								"Name"), objects.get(i).getString("Start"),
								objects.get(i).getString("End"), objects.get(i)
										.getString("Run"), objects.get(i)
										.getString("Constraints"), objects.get(
										i).getString("Watt")));
						objID.add(objects.get(i).getObjectId());
					}
					adapter = new ApplianceAdapter(DeleteActivity.this,
							R.layout.row_layout, appList);
					list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							pos = position;
							ParseUser user = ParseUser.getCurrentUser();
							ParseQuery<ParseObject> query = ParseQuery
									.getQuery("Appliance");
							query.whereEqualTo("User", user);
							query.getInBackground(objID.get(position),
									new GetCallback<ParseObject>() {

										@Override
										public void done(ParseObject object,
												ParseException e) {
											if (e == null) {
												object.deleteInBackground();
												appName = object
														.getString("Name");
												new MyAsyncTask().execute();
											} else {
												Toast.makeText(
														DeleteActivity.this,
														"Could not locate object!",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
							return true;
						}
					});
				} else {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Async task to delete appliance and corresponding data on cloud, as well as send the details
	 * to server which will delete it from the server copy of added appliances
	 */
	private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			Toast.makeText(DeleteActivity.this, "Deleted!", Toast.LENGTH_SHORT)
					.show();
			appList.remove(pos);
			objID.remove(pos);
		}

		@Override
		protected Void doInBackground(Void... params) {
			postData();
			return null;
		}

		public void postData() {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ip + "/delete.php");
			ParseUser user = ParseUser.getCurrentUser();
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Username", user
						.getObjectId()));
				nameValuePairs.add(new BasicNameValuePair("ApplianceName",
						appName));
				Log.d("delete", appName);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
