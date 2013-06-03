/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 */
package com.monigarr.blogsearch;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import com.monigarr.blogsearch.WebFunctions;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class PassSearch.
 */
public class PassSearch extends IntentService {

	URL finalURL = null;
    int _result;
	String _response = null;
	JSONArray _results;
	String _passedItem = null;
 
    /**
     * Instantiates a new pass search.
     */
    public PassSearch() {
        super("PassSearch");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
    
    	Bundle extras = intent.getExtras();
        if (extras != null) {
        	// Searched Item received
        	_passedItem = (String) extras.get("item");
        	//get up to 5 results this week
        	String responseString = "http://www.monicalamb.com/blog3/?wpapi=search&get_posts&dev=0&type=post&count=5&keyword="+_passedItem;
            Log.i("onHandleIntent::", responseString);
            try{
            	// API string response
    			finalURL = new URL(responseString);
    			_response = WebFunctions.getURLStringResponse(finalURL);
    			if (_response.length() > 0) _result = Activity.RESULT_OK;
    		} catch (MalformedURLException e){
    			Log.e("BAD URL", "MALFORMED URL");
    			finalURL = null;
    		}
            
            // Messenger received. API response sent back via message object
        	Messenger messenger = (Messenger) extras.get("messenger");
        	Message msg = Message.obtain();
        	msg.arg1 = _result;
        	msg.obj = _response;
        	Log.i("msg.obj::", msg.obj.toString());
        	try{
        		messenger.send(msg);
        	} catch (android.os.RemoteException e){
        		Log.e(getClass().getName(), "EXCEPTION sending message", e);
        	}
        } 
    }

}
