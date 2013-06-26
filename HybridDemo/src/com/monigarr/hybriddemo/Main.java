package com.monigarr.hybriddemo;

/*
 * Developer: Monica Peters  monigarr@yahoo.com
 * Instructor: Donna Gardinier
 * Full Sail University
 * Mobile Development Bach. of Science
 * MDF3 June 2013
 * 
 * Project Requirements
 * WebView Requirements

    Well designed
    	My WebView calls to my self hosted web form at http://www.monicalamb.com/add_project_mdf3_project4.html
    	I used jquery, jquery mobile, javascript, css3, html5 to create webform.
    	Color Theme inspired by ColourLovers: http://www.colourlovers.com/palette/1473/Ocean_Five
    At least 1 data collection control and 1 user interaction control (button, etc.)
    	Submit Form and Camera Button on html form.
    At least 1 JavaScript method for enhanced UI
    	jquery, main.js customizes jquery mobile body ui
    	http://www.monicalamb.com/javascripts/mdf3.js
    	http://www.monicalamb.com/javascripts/main.js
    At least 1 JavaScript method for native integration
    	http://www.monicalamb.com/javascripts/camera.js

Native Requirements

    At least 1 meaningful Activity including a WebView
    	Main, AddProject, Details
    Properly defined JavaScript interface
    	AddProject.java
    At least 1 method to accept and meaningfully handle data from WebView
    	AddProject.java
    At least 1 intent initiated from a method called from WebView
    	Main, AddProject, Details
 *
 * 
 * Tutorials
 * http://Parse.com
 * http://www.NewRelic.com
 * http://developer.android.com/guide/webapps/best-practices.html
 * http://developer.android.com/guide/webapps/webview.html
 * http://docs.xamarin.com/guides/android/user_interface/web_view
 * http://java.dzone.com/articles/learning-android-webview
 * http://examples.javacodegeeks.com/android/core/ui/webview/android-webview-example/
 * http://www.mkyong.com/android/android-webview-example/
 * http://stackoverflow.com/questions/9634409/run-custom-javascript-code-after-loading-any-website
 * http://gurushya.com/customizing-android-webview/
 * http://javatechig.com/android/android-webview-example/
 * http://stackoverflow.com/questions/11152391/android-send-string-between-activities
 */

import java.io.File;
import java.util.HashMap;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import com.newrelic.agent.android.NewRelic;

@SuppressLint("SetJavaScriptEnabled")
public class Main extends Activity {

	HashMap<String, String> _recents;
	int TAKE_PICTURE = 1;
	Uri imageUri;
	Bitmap mImageBitmap;
	ImageView mImageView;
	Context context;
	
	//testing
	String project_name = "Project Name";
	String project_url = "http://www.Test.com";
	String project_notes = "This is a test";
	String client_phone = "315-600-3351";
	String client_email = "monigarr@yahoo.com";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Analytics processed by Parse.com
		ParseAnalytics.trackAppOpened(getIntent());
		
		//Analytics processed by NewRelic.com
		NewRelic.withApplicationToken( "AA503aec9d69a0acb423ef1404f9d2aed1abd7d150" ).start(this.getApplication());

		ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connection != null && (connection.getNetworkInfo(1).isAvailable() == true)
				|| (connection.getNetworkInfo(0).isAvailable() == true)) {
			this.setTitle("Add New Project");

			Parse.initialize(this, "LGG1MbCVOrLbi6Hr6lXxiWTMoqEwjGQX4RAjjMCN", "XINCv5XuXUffmWtVGO41TXRf1P0SFowjSfay9L7C"); 

			WebView myWebView = (WebView) findViewById(R.id.webView1);
			WebSettings webSettings = myWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDomStorageEnabled(true);
			
			Application application = getApplication();
			myWebView.addJavascriptInterface(application, "HybridDemo");
			myWebView.requestFocusFromTouch();
			myWebView.setWebViewClient(new WebViewClient());
			myWebView.setWebChromeClient(new WebChromeClient());
			myWebView.loadUrl("file:///android_asset/addproject.html");

		} else {
			Toast toast = Toast.makeText(this, "NO CONNECTION",Toast.LENGTH_SHORT);
			toast.show();
		}
		
		//test
		/*
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("project_name", project_name);
		testObject.put("project_url", project_url);
		testObject.put("project_notes", project_notes);
		testObject.put("client_phone", client_phone);
		testObject.put("client_emaikl", client_email);
		testObject.saveInBackground();
		
		//test
		ParseObject projectObject = new ParseObject("projectObject");
		projectObject.put("project_name", project_name);
		projectObject.put("project_url", project_url);
		projectObject.put("project_notes", project_notes);
		projectObject.put("client_phone", client_phone);
		projectObject.put("client_emaikl", client_email);
		projectObject.saveInBackground();
		*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.refreshButton:
			Log.i("TAG", "REFRESH");
			return super.onOptionsItemSelected(item);
			
		case R.id.addButton:
			Log.i("TAG", "ADD");
			ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connection != null
					&& (connection.getNetworkInfo(1).isAvailable() == true)
					|| (connection.getNetworkInfo(0).isAvailable() == true)) {
				Intent i = new Intent(getApplicationContext(), Main.class);
				startActivity(i);
			} else {
				Toast toast = Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT);
				toast.show();
			}
			break;

		case R.id.homeButton:
			Log.i("TAG", "HOME");
			Intent ii = new Intent(getApplicationContext(), AddProject.class);
			startActivity(ii);
			break;
			
		}
		return true;
	}

	public class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		//Show toast from web page
		// @JavascriptInterface
		public void showData(String[] data) {
			String project_name = data[0];
			String project_url = data[1];
			String project_notes = data[2];
			String client_phone = data[3];
			String client_email = data[4];

			//Validate
			if (project_name.equals("") || project_name.length() == 0) {
				Toast toast = Toast.makeText(getApplicationContext(), "PROJECT NAME", Toast.LENGTH_LONG);
				toast.show();
			} else if (project_url.equals("") || project_url.length() == 0) {
				Toast toast = Toast.makeText(getApplicationContext(), "PROJECT URL", Toast.LENGTH_LONG);
				toast.show();
			} else if (project_notes.equals("") || project_notes.length() == 0) {
				Toast toast = Toast.makeText(getApplicationContext(), "PROJECT NOTES", Toast.LENGTH_LONG);
				toast.show();
			} else if (client_phone.equals("") || client_phone.length() == 0
					|| client_phone.length() < 12) {
				Toast toast = Toast.makeText(getApplicationContext(), "CLIENT PHONE NUMBER", Toast.LENGTH_LONG);
				toast.show();
			} else if (client_email.equals("") || client_email.length() == 0
					|| !(client_email.contains("@"))) {
				Toast toast = Toast.makeText(getApplicationContext(), "CLIENT EMAIL", Toast.LENGTH_LONG);
				toast.show();
			} else {
				ParseObject projectObject = new ParseObject("ProjectObject");
				projectObject.put("project_name", project_name);
				projectObject.put("project_url", project_url);
				projectObject.put("project_notes", project_notes);
				projectObject.put("client_phone", client_phone);
				projectObject.put("client_email", client_email);
				//projectObject.saveInBackground();
				//If No Network Available. Store on Device until Network is Available.
				projectObject.saveEventually();

				Intent i = new Intent(mContext, AddProject.class);
				startActivity(i);
			}
		}

		// @JavascriptInterface
		public void showCamera(String c) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File photo = new File(Environment.getExternalStorageDirectory(), "ProjectImage.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			imageUri = Uri.fromFile(photo);
			startActivityForResult(intent, TAKE_PICTURE);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			// ImageView imageView = (ImageView) findViewById(R.id.ImageView);
			ContentResolver cr = getContentResolver();
			@SuppressWarnings("unused")
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
						selectedImage);

				// imageView.setImageBitmap(bitmap);
				Toast.makeText(this, selectedImage.toString(),
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
						.show();
				Log.e("Camera", e.toString());
			}

		}
	}
}
