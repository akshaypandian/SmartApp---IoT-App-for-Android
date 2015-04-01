package com.example.smartapp;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

	EditText name;
	EditText email;
	EditText password;
	EditText conPassword;
	Button signUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		name = (EditText) findViewById(R.id.editTextFirstName);
		email = (EditText) findViewById(R.id.editTextLastName);
		password = (EditText) findViewById(R.id.editTextPassword);
		conPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
		signUp = (Button) findViewById(R.id.buttonSignup);

		signUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (name.getEditableText().toString().isEmpty()
						|| email.getEditableText().toString().isEmpty()
						|| password.getEditableText().toString().isEmpty()
						|| conPassword.getEditableText().toString().isEmpty()) {
					if (name.getEditableText().toString().isEmpty()) {
						Toast.makeText(SignUpActivity.this, "Enter name!",
								Toast.LENGTH_SHORT).show();
					}
					if (email.getEditableText().toString().isEmpty()) {
						Toast.makeText(SignUpActivity.this, "Enter email!",
								Toast.LENGTH_SHORT).show();
					}
					if (password.getEditableText().toString().isEmpty()) {
						Toast.makeText(SignUpActivity.this, "Enter password!",
								Toast.LENGTH_SHORT).show();
					}
					if (conPassword.getEditableText().toString().isEmpty()) {
						Toast.makeText(SignUpActivity.this, "Enter password!",
								Toast.LENGTH_SHORT).show();
					}
					if (password.getEditableText().toString()
							.equals(conPassword.getEditableText().toString())) {
						Toast.makeText(SignUpActivity.this,
								"Check both passwords!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					ParseUser user = new ParseUser();
					user.setUsername(email.getEditableText().toString());
					user.setPassword(password.getEditableText().toString());
					user.put("Name", name.getEditableText().toString());
					user.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast.makeText(SignUpActivity.this,
										"Signup Successfull!",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(SignUpActivity.this,
										HomeActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(SignUpActivity.this,
										"Signup unsuccessfull!",
										Toast.LENGTH_SHORT).show();
							}

						}
					});
				}
			}
		});
	}
}
