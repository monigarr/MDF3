/*
 * file			ActivityDemo.java
 * 
 * project		Java2Week1
 * 
 * package		com.monigarr.activitydemo
 * 
 * @author		Monica Peters
 * @email		admin@SeawayWebSite.com
 * @website		MonicaLamb.com
 * @youtube		YouTube.com/monigarr
 * @google		plus.google.com/u/0/102299600034023229066/
 * @facebook	FaceBook.com/monigarr
 * @linkedin	ca.linkedin.com/in/monigarr/
 * 
 * date			May 8, 2013
 * 
 */
package com.monigarr.activitydemo;

/*
 * 
 * MAIN ACTIVITY
 * 	Enter Search Word to search MonicaLamb.com Blog (self hosted WordPress Blog).
 * 	wpapi plugin provides json feed of Blog content
 * 	Open Search Results in Second Activity
 * 	Button to Pick an Image from Device Gallery
 * 	Show Image that was picked on Main Activity
 * 	IF end user in their device gallery and taps SHARE on an image
 * 		they can choose to run this app & show the image they want to share.
 * SECOND ACTIVITY
 *  Show Blog Title, Excerpt, Link on Second Activity
 * 	Close button on Second Activity shows Main Activity
 * 
 * Colour Palette: 	"Caribean Coast" by GreenMyEyes http://www.colourlovers.com/palette/118273/Caribbean_Coast
 * Video Tutorials:  YouTube.com/profgustin
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends Activity implements OnClickListener, MainFragment.onSearchButtonClicked{
	
	public final static String BUNDLE_TITLE_KEY = "title";
	public final static String BUNDLE_EXCERPT_KEY = "excerpt";
	public final static String BUNDLE_URL_KEY = "url";
	
	
	Context _context;
	EditText editText;
	TextView message;
	Button myButton;
	JSONArray posts;
	JSONObject post;
	ArrayList<HashMap<String, String>> mylist;
	String baseURL = "http://www.monicalamb.com/blog3/?wpapi=search&get_posts&dev=0&type=post&count=1&keyword=";
	String title;
	String excerpt;
	String url;
	String _passedDetails;
	String _passedTitle;
	String _passedExcerpt;
	String _passedLink;
	String _passedSearch;
	String _passedComment;
	ImageView _passedImage;
	ImageView item_image;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}//end onCreate


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		//empty. not sure why we keep this on Main when it's empty.
		//if I remove this, my app crashes.
	}
	
	public void startResultActivity(String title, String excerpt, String url){
		
		EditText editText = (EditText) findViewById(R.id.searchField);
		TextView message = (TextView) findViewById(R.id.message);
		Button myButton = (Button) findViewById(R.id.go_btn);
		myButton.setOnClickListener(this);
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
      	@SuppressWarnings("unused")
		ImageView itemImage = (ImageView) findViewById(R.id.item_image);
		@SuppressWarnings("unused")
		TextView itemTitle = (TextView) findViewById(R.id.item_title);
		@SuppressWarnings("unused")
		TextView itemSubTitle = (TextView) findViewById(R.id.item_subtitle);
		@SuppressWarnings("unused")
		TextView itemLink = (TextView) findViewById(R.id.item_link);
		@SuppressWarnings("unused")
		TextView comment = (TextView) findViewById(R.id.comment);
        message.setText("Enter ONE word. Try Searching for \"Mobile\" or \"IOS\" ");
		
		Log.i("TEXT ENTERED:", editText.getText().toString());	
		Log.i("JSON URL: ", baseURL + editText.getText().toString());
		
		//only return one result because we only show one result on second page.
        JSONObject json = JSONfunctions.getJSONfromURL("http://www.monicalamb.com/blog3/?wpapi=search&get_posts&dev=0&type=post&count=1&keyword=" + editText.getText().toString());   
        //JSONObject json = JSONfunctions.getURLStringResponse("http://www.monicalamb.com/blog3/?wpapi=search&get_posts&dev=1&type=post&count=1&keyword=" + editText.getText().toString());   
		//new getJSON().execute(baseURL + editText.getText().toString());
		
		Log.i("json:", json.toString());
		try{
        	JSONArray posts = json.getJSONArray("posts");
        	for(int i = 0; i < posts.length(); i++){
					HashMap<String, String> map = new HashMap<String, String>();	
					JSONObject e = posts.getJSONObject(i);
					map.put("id",  String.valueOf(i));
		        	map.put("title", e.getString("title"));
		        	map.put("excerpt", e.getString("excerpt"));
		        	map.put("link", "Link: " +  e.getString("url"));
					mylist.add(map);

					//EXPLICIT INTENT
					//Details passed to Second Activity
        			//Intent intent = new Intent(getApplicationContext(), Second.class);
					Intent intent = new Intent(_context, Second.class);
					/*Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_TITLE_KEY, title);
					bundle.putString(BUNDLE_EXCERPT_KEY, excerpt);
					bundle.putString(BUNDLE_URL_KEY, url);
					intent.putExtras(bundle);
					*/// for future ref & studying
					
        			intent.putExtra("title", e.getString("title"));
        			intent.putExtra("excerpt", e.getString("excerpt"));
        			intent.putExtra("url",  e.getString("url"));
        			intent.putExtra("search", editText.getText().toString());
					//startActivity(intent);
					
					startActivityForResult(intent, 0);
				}
        }catch(JSONException e){
        	 Log.e("log_tag", "JSON Parsing Error "+e.toString());
        	 message.setText("Nothing Found for your search word. Try searching for iOS");
        }
	}

}
	
