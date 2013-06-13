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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.monigarr.audiogpsnetworkstatus.GPSTracker;
import com.monigarr.audiogpsnetworkstatus.NetworkActivity;
import com.monigarr.audiogpsnetworkstatus.SettingsActivity;
import com.monigarr.audiogpsnetworkstatus.StackOverflowXmlParser.Entry;

import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	MediaPlayer logoMusic;
	Button buttonShowLocation;
	Button buttonShowNetworkStatus;
	GPSTracker gps;
	NetworkActivity showNetworkStatus;
	String showTextResults;
	
	 public static final String WIFI = "Wi-Fi";
	    public static final String ANY = "Any";
	    private static final String URL =
	            "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

	    // Is Wi-Fi connection available.
	    private static boolean wifiConnected = false;
	    // Is mobile connection available.
	    private static boolean mobileConnected = false;
	    // Should display be refreshed.
	    public static boolean refreshDisplay = true;

	    // User's current network preference setting.
	    public static String sPref = null;

	    // BroadcastReceiver tracks network connectivity changes.
	    private NetworkReceiver receiver = new NetworkReceiver();
	    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
		
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
	

    // Refreshes display if network connection and pref settings allow.
    @Override
    public void onStart() {
        super.onStart();

        // Gets user's network pref settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves string value for preferences. Second parameter
        // is default value to use if preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        // Only loads page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if user set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through user using app,
        // you don't want to refresh display--this would force display of
        // an error page instead of stackoverflow.com content.
        if (refreshDisplay) {
            loadPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    // Check network connection and set wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    // Uses AsyncTask subclass to download XML feed from stackoverflow.com.
    // This avoids UI lock up. To prevent network operations from
    // causing a delay that results in poor user experience, always perform
    // network operations on a separate thread from the UI.
    private void loadPage() {
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            // AsyncTask subclass
            new DownloadXmlTask().execute(URL);
        } else {
            showErrorPage();
        }
    }

    // Displays error if app is unable to load content.
    private void showErrorPage() {
        setContentView(R.layout.activity_main);

        // Specified network connection is not available. Displays error message.
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadData(getResources().getString(R.string.connection_error),
                "text/html", null);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
    }
	
	// Handles user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
                Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
        case R.id.refresh:
                loadPage();
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            setContentView(R.layout.activity_main);
            // Displays HTML string in the UI via a WebView
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData(result, "text/html", null);
        }
    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<Entry> entries = null;
        @SuppressWarnings("unused")
		String title = null;
        @SuppressWarnings("unused")
		String url = null;
        @SuppressWarnings("unused")
		String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks if user set preference to include summary text
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = stackOverflowXmlParser.parse(stream);
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (Entry entry : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
            if (pref) {
                htmlString.append(entry.summary);
            }
        }
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /**
     *
     * This BroadcastReceiver intercepts android.net.ConnectivityManager.CONNECTIVITY_ACTION,
     * which indicates a connection change. It checks whether type is TYPE_WIFI.
     * If it is, it checks if Wi-Fi is connected and sets wifiConnected flag in the
     * main activity accordingly.
     *
     */
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // Checks user prefs and network connection. Based on result, decides
            // to refresh display or keep current display.
            // If userpref is Wi-Fi only, checks to see if device has a Wi-Fi connection.
            if (WIFI.equals(sPref) && networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // If device has its Wi-Fi connection, sets refreshDisplay
                // to true. This causes display to be refreshed when user
                // returns to app.
                refreshDisplay = true;
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

                // If setting is ANY network and there is a network connection
                // (which by process of elimination would be mobile), sets refreshDisplay to true.
            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;

                // Otherwise, app can't download content--either because there is no network
                // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
                // is no Wi-Fi connection.
                // Sets refreshDisplay to false - so we can view last loaded content.
            } else {
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
