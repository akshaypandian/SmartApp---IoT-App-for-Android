package com.example.smartapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ViewActivity extends Activity {
	ListView list;
	List<ApplianceList> appList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		list = (ListView) findViewById(R.id.listView1);
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
					}
					ApplianceAdapter adapter = new ApplianceAdapter(
							ViewActivity.this, R.layout.row_layout, appList);
					list.setAdapter(adapter);
				} else {
					e.printStackTrace();
				}
			}
		});
	}
}
