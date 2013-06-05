package com.parse.monigarr;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import com.parse.monigarr.R;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PushService.setDefaultPushCallback(this, ParseStarterProjectActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(getIntent());
	}
}
