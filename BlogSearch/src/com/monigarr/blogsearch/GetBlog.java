/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 */
package com.monigarr.blogsearch;

import java.net.MalformedURLException;
import java.net.URL;

import com.actionbarsherlock.app.SherlockActivity;
import com.monigarr.blogsearch.WebFunctions;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class GetBlog extends IntentService {

	private int _result;
	String _response = null;
	
	/**
	 * Instantiates a new gets the blog.
	 *
	 * @param search the search
	 */
	public GetBlog(String search) {
		super(search);
		//return up to 5 results this week
		String apiURL = "http://www.monicalamb.com/blog3/?wpapi=search&get_posts&dev=1&type=post&count=5&keyword=";
		URL finalURL = null;
		
		try{
			finalURL = new URL(apiURL);
			_response = WebFunctions.getURLStringResponse(finalURL);
			if(_response.length() > 0) _result = SherlockActivity.RESULT_OK;
		} catch (MalformedURLException e){
			Log.e("BAD URL", "MALFORMED URL");
			finalURL = null;
		}
	}
	  

 	@Override
	    protected void onHandleIntent(Intent intent) {
		  
		Bundle extras = intent.getExtras();
	    if (extras != null) {
	    	Messenger messenger = (Messenger) extras.get("MESSENGER");
	    	Message msg = Message.obtain();
	    	msg.arg1 = _result;
	    	msg.obj = _response;
	    	try {
	    		messenger.send(msg);
	    	} catch (android.os.RemoteException e1) {
	    		Log.w(getClass().getName(), "Exception sending message", e1);
	    	}
	    }
	}

}
