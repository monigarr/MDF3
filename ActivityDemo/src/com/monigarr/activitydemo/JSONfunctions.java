package com.monigarr.activitydemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class JSONfunctions {
	Context _context;
	//static when we want to access from outside this class
	final Boolean _conn = false;
	final String _connectionType = "Unavailable";
	
	public static boolean haveNetworkConnection(final Context context) {
        boolean connectedWifi = false;
        boolean connectedMobile = false;

        final ConnectivityManager cm = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
               final NetworkInfo[] netInfo = cm.getAllNetworkInfo();
               for (final NetworkInfo netInfoCheck : netInfo) {
                     if (netInfoCheck.getTypeName().equalsIgnoreCase("WIFI")) {
                            if (netInfoCheck.isConnected()) {
                                   connectedWifi = true;
                            }
                     }
                     if (netInfoCheck.getTypeName().equalsIgnoreCase("MOBILE")) {
                            if (netInfoCheck.isConnected()) {
                                   connectedMobile = true;
                            }
                     }
               }
        }

        return connectedWifi || connectedMobile;
	 }

	//GET RESPONSE FROM URL
	//YAHOO IQL LIBRARY RETURNS JSON STRING
	//MAKE SURE YOU KNOW WHAT YOUR LIBRARY WILL RETURN
	//I'M USING HTTPCLIENT FOR MY WPAPI JSON LIBRARY
	//SEE getJSONfromURL METHOD BELOW
	public static String getURLStringResponse(URL url){
		
		String response = "";
		
		//make sure we are online
		//not a universal way netInfo(_context);
		//universal way
		try{
			URLConnection conn = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1){
				response = new String(contentBytes, 0, bytesRead);
				responseBuffer.append(response);
			}
		return responseBuffer.toString();
	} catch (Exception e){
		//Log.e("URL RESPONSE ERROR", e.toString);
		Log.e("URL RESPONSE ERROR", "getURLStringResponse");
	}
	
	return response;
}
	
	public static JSONArray getJsonFromFile(Context context, String location) {
		
		String showfilecontents = "";

		try {
			ContextWrapper cw = new ContextWrapper(context);
			File directory = cw.getFilesDir();
			
			FileReader file = new FileReader(directory + "/" + location);
			BufferedReader buffer = new BufferedReader(file);
			String line;
			while ((line = buffer.readLine()) != null) {
				showfilecontents += line;
			}
			file.close();
		} catch (IOException e) {
			Log.d("GETJSONFROMFILE", "Failed getting File Contents");
			e.printStackTrace();
        }

		if (!showfilecontents.equals("")) {
			Log.d("GETJSONFROMFILE", "Success! " + showfilecontents);
			try {
				return new JSONArray(showfilecontents);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("GETJSONFROMFILE", "Failed to create json array");
			} 			
		}

		return new JSONArray();
	}

	public static JSONObject getJSONfromURL(String url){
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;
		
		//HTTP
	    try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
	    }catch(Exception e){
	            Log.e("log_tag", "Error in http connection "+e.toString());
	    }
				
	    //CONVERT RESPONSE TO STRING
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    
	    //PARSE JSON
	    try{
            jArray = new JSONObject(result);            
	    }catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    }
    
	    return jArray;
		}
	
	@SuppressWarnings("unused")
	private class getJSON extends AsyncTask<String, Void, String> {            
        protected String doInBackground(String... urls) {
            HttpClient client = new DefaultHttpClient();
            String json = "";
            try {
                String line = "";
                HttpGet request = new HttpGet(urls[0]);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = rd.readLine()) != null) {
                    json += line + System.getProperty("line.separator");
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return json;
        }

        protected void onProgressUpdate(Void... progress) {

        }
	}//end getJSON
}