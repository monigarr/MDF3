package com.monigarr.audiogpsnetworkstatus;

/*
 * Developer: Monica Peters
 * Full Sail University
 * Mobile Dev Bachelor of Science
 * MDF3 Class
 * Instructor: Donna DeGardinier
 * 
 * Project Due: Thursday June 12, 2013
 * 
 * audio: http://developer.android.com/guide/topics/media/audio-capture.html
 * file storage / network management
 * 		save audio recording files (unique name for each) 
 * 		if internet available save audio recording to parse.  
 * 		if internet not available save audio recording to local device to be uploaded to parse
 * 			later when internet is available.
 * 
 */

import java.io.IOException;

import android.R;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

  
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MediaPlayer logoMusic = MediaPlayer.create(MainActivity.this, R.raw.arcadia);
		logoMusic.start();
		
	}
    

}
