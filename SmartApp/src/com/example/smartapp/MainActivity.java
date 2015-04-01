package com.example.smartapp;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button signup;
	Button login;
	EditText username;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		signup = (Button) findViewById(R.id.button2);
		login = (Button) findViewById(R.id.button1);
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		ParseUser user = ParseUser.getCurrentUser();
		if (user != null) {
			Intent intent = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {

			signup.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,
							SignUpActivity.class);
					startActivity(intent);
					finish();
				}
			});

			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (username.getEditableText().toString().isEmpty()
							|| password.getEditableText().toString().isEmpty()) {
						Toast.makeText(MainActivity.this,
								"Check login details!", Toast.LENGTH_SHORT)
								.show();
					} else {
						ParseUser.logInInBackground(username.getEditableText()
								.toString(), password.getEditableText()
								.toString(), new LogInCallback() {

							@Override
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									Intent intent = new Intent(
											MainActivity.this,
											HomeActivity.class);
									startActivity(intent);
									finish();
								} else {
									Toast.makeText(MainActivity.this,
											"Login Unsuccessful!",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				}
			});
		}
	}
}
