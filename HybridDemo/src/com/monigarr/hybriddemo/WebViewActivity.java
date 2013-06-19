package com.monigarr.hybriddemo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {
	 
		private WebView webView;
		
		public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.webview);
	 
			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			
			Intent editTextIntent = getIntent();
			String inputURL = editTextIntent.getStringExtra("inputURL");			
			webView.loadUrl(inputURL);
			
		   //String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
		   //webView.loadData(customHtml, "text/html", "UTF-8");
	 
		}	 
	}