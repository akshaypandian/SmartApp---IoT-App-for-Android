package com.example.smartapp;
/*
 * Configuration required to enable use of Parse.com
 */
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "yuC0Nzrq82KcdQoBj0fcxQ4hkVbtNX1Ce9WLXOeW",
				"ba9FsvSOJa0CoVSMdQXyGQlS20E2wTDYiGnnUVfF");

		//ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(false);
		// ParseObject testObject = new ParseObject("TestObject");
		// testObject.put("foo", "bar");
		// testObject.saveInBackground();
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
