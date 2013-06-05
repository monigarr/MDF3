package com.monigarr.callme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		/*
		 * Add Parse initialization code here
		 */
		Parse.initialize(this,"VdrYzRiT0qP1paeWKFXfdmXcumtcfry7YYoLSEo9", "yEuTn5lc1cjD4W4hk9uamMxDBBTv2Z8P1BL5d8V4");

		//remove because we want to add our own users ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
