package com.example.smartapp;

/*
 * Activity to edit an already added appliance.
 */
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class EditActivity extends Activity {

	ListView list;
	List<ApplianceList> appList;
	String ip;
	String objId;
	List<String> objectList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		/*
		 * objectList = new ArrayList<String>(); list = (ListView)
		 * findViewById(R.id.listView1); ip =
		 * getIntent().getExtras().getString("IP"); appList = new
		 * ArrayList<ApplianceList>(); ParseUser user =
		 * ParseUser.getCurrentUser(); ParseQuery<ParseObject> query =
		 * ParseQuery.getQuery("Appliance"); query.whereEqualTo("User", user);
		 * 
		 * query.findInBackground(new FindCallback<ParseObject>() {
		 * 
		 * @Override public void done(List<ParseObject> objects, ParseException
		 * e) { if (e == null) { for (int i = 0; i < objects.size(); i++) {
		 * appList.add(new ApplianceList(objects.get(i).getString( "Name"),
		 * objects.get(i).getString("Start"), objects.get(i).getString("End"),
		 * objects.get(i) .getString("Run"), objects.get(i)
		 * .getString("Constraints"), objects.get( i).getString("Watt")));
		 * objectList.add(objects.get(i).getObjectId()); } ApplianceAdapter
		 * adapter = new ApplianceAdapter( EditActivity.this,
		 * R.layout.row_layout, appList); list.setAdapter(adapter);
		 * list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { Intent intent = new
		 * Intent(EditActivity.this, SingleEditActivity.class);
		 * intent.putExtra("Name", appList.get(position) .getApplianceName());
		 * intent.putExtra("Start", appList.get(position) .getStartTime());
		 * intent.putExtra("End", appList.get(position) .getEndTime());
		 * intent.putExtra("Run", appList.get(position) .getRunTime());
		 * intent.putExtra("Constraints", appList
		 * .get(position).getConstraints()); intent.putExtra("Watt",
		 * appList.get(position) .getWattage()); intent.putExtra("IP", ip);
		 * intent.putExtra("ObjID", objectList.get(position));
		 * startActivity(intent);
		 * 
		 * } }); } else { e.printStackTrace(); } } });
		 */
	}

	@Override
	protected void onResume() {
		super.onResume();
		objectList = new ArrayList<String>();
		list = (ListView) findViewById(R.id.listView1);
		ip = getIntent().getExtras().getString("IP");
		appList = new ArrayList<ApplianceList>();
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
						objectList.add(objects.get(i).getObjectId());
					}
					ApplianceAdapter adapter = new ApplianceAdapter(
							EditActivity.this, R.layout.row_layout, appList);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(EditActivity.this,
									SingleEditActivity.class);
							intent.putExtra("Name", appList.get(position)
									.getApplianceName());
							intent.putExtra("Start", appList.get(position)
									.getStartTime());
							intent.putExtra("End", appList.get(position)
									.getEndTime());
							intent.putExtra("Run", appList.get(position)
									.getRunTime());
							intent.putExtra("Constraints", appList
									.get(position).getConstraints());
							intent.putExtra("Watt", appList.get(position)
									.getWattage());
							intent.putExtra("IP", ip);
							intent.putExtra("ObjID", objectList.get(position));
							startActivity(intent);

						}
					});
				} else {
					e.printStackTrace();
				}
			}
		});
	}

}
