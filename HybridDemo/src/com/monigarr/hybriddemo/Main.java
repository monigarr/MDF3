package com.monigarr.hybriddemo;

/*
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

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity {
	 
		private Button buttonAT;
		private EditText editTextURL;
		private String inputURL = null;
		private TextView textView1;
	 
		public void onCreate(Bundle savedInstanceState) {
			final Context context = this;
	 
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
	 
			editTextURL = (EditText) findViewById(R.id.editTextURL);
			buttonAT = (Button) findViewById(R.id.buttonAT);
			
			textView1 = (TextView) findViewById(R.id.textView1);
	 
			buttonAT.setOnClickListener(new OnClickListener() { 
			  @Override
			  public void onClick(View arg0) {
				  Intent editTextIntent = new Intent(context, WebViewActivity.class);
				  String inputURL = editTextURL.getText().toString();
				  //&& inputURL == "^((http)://[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$")
				  if (inputURL == null){
					  textView1.setText("Enter Full WebSite URL as http://YourWebSite.com, then tap the Go Button");
				  }else if (inputURL != null){
					  editTextIntent.putExtra("inputURL", inputURL);
					  startActivity(editTextIntent);
				  }
			  }	 
			});
		}
	}