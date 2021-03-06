package com.monigarr.workshop;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseWorkshopApplication extends Application {

	@Override
	public void onCreate(){
		super.onCreate();
		
		// Add your initialization code here
		Parse.initialize(this,"VdrYzRiT0qP1paeWKFXfdmXcumtcfry7YYoLSEo9", "yEuTn5lc1cjD4W4hk9uamMxDBBTv2Z8P1BL5d8V4");

		//remove following line so we can add our own users
		//ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
