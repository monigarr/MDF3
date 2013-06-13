package com.monigarr.audiogpsnetworkstatus;

/*
 * Developer: Monica Peters
 * Instructor: Donna DeGardinier
 * Date: June 10th 2013
 * Full Sail University
 * MDVBS
 * 
 * REQUIREMENTS:
 * 
 * AUDIO PLAYBACK:
 * 		Play audio in background for 5 seconds when the app runs.
 * 
 * 
 * GPS: 
 * 		Show specific url data feed in webview, based on device location.
 * 		If connection not available show error message in webview.
 * 		http://developer.android.com/training/location/retrieve-current.html
 * 		http://developer.android.com/reference/android/location/LocationManager.html
 * 		http://android-developers.blogspot.ca/2011/06/deep-dive-into-location.html
 * 		http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
 * 
 * NETWORK STATUS: 	
 * 		If network available show data feed.  
 * 		If network not available show toast message.
 * 		If network not available tell user to tap settings to configure network.
 * 		http://developer.android.com/guide/topics/connectivity/index.html
 * 		http://developer.android.com/training/basics/network-ops/managing.html
 */

import com.monigarr.audiogpsnetworkstatus.GPSTracker;
import com.monigarr.audiogpsnetworkstatus.NetworkActivity;
import com.monigarr.audiogpsnetworkstatus.SettingsActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	MediaPlayer logoMusic;
	Button buttonShowLocation;
	Button buttonShowNetworkStatus;
	GPSTracker gps;
	NetworkActivity showNetworkStatus;
	String showTextResults;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//PLAY AUDIO 
        //no loop, only play once
		logoMusic = MediaPlayer.create(MainActivity.this, R.raw.arcadia);
		logoMusic.start();
				
		//SHOW LOCATION
		buttonShowLocation = (Button) findViewById(R.id.buttonShowLocation);		
        buttonShowLocation.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {       
                // create class object
                gps = new GPSTracker(MainActivity.this);
 
                // check if GPS enabled    
                if(gps.canGetLocation()){
                     
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                     
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location:  \nLatitude: " + latitude + "\nLongitude: " + longitude + "\n", Toast.LENGTH_LONG).show();  
                    Log.i("Toast gps","Toast GPS");
                    Log.i("BUTTON CLICKED:", Double.toString(latitude));
                    Log.i("BUTTON CLICKED:", Double.toString(longitude));
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                 
            }
        });

	}
	
	//if interrupted by a phone call...
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		logoMusic.release();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
    }
}
