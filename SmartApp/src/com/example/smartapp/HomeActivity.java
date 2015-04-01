package com.example.smartapp;
/*
 * Gives the user access to various options to navigate within the app.
 */
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	TextView username;
	Button viewApp;
	Button manageApp;
	Button schedule;
	EditText ip;
	Button delete;
	Button edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		username = (TextView) findViewById(R.id.textView1);
		username.setText("Hello, "
				+ ParseUser.getCurrentUser().getString("Name"));
		viewApp = (Button) findViewById(R.id.button1);
		manageApp = (Button) findViewById(R.id.button2);
		ip = (EditText) findViewById(R.id.editText1);
		schedule = (Button) findViewById(R.id.button3);
		edit = (Button) findViewById(R.id.button4);
		delete = (Button) findViewById(R.id.button5);

		schedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ip.getEditableText().toString().isEmpty()) {
					Intent intent = new Intent(HomeActivity.this,
							ScheduleActivity.class);
					intent.putExtra("IP", ip.getEditableText().toString());
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "Enter IP Address!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		manageApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ip.getEditableText().toString().isEmpty()) {
					Intent intent = new Intent(HomeActivity.this,
							ManageActivity.class);
					intent.putExtra("IP", ip.getEditableText().toString());
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "Enter IP Address!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		viewApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						ViewActivity.class);
				startActivity(intent);
			}
		});

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ip.getEditableText().toString().isEmpty()) {
					Intent intent = new Intent(HomeActivity.this,
							EditActivity.class);
					intent.putExtra("IP", ip.getEditableText().toString());
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "Enter IP Address!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!ip.getEditableText().toString().isEmpty()) {
					Intent intent = new Intent(HomeActivity.this,
							DeleteActivity.class);
					intent.putExtra("IP", ip.getEditableText().toString());
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "Enter IP Address!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mune_logout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_logout){
			ParseUser.logOut();
			Intent intent = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return true;
	}
	
}
