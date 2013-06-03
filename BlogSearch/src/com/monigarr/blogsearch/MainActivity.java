/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 * 
 * Color inspired by http://www.colourlovers.com/palette/2386738/Cherry_Diamond
 * Search Blog on first screen.
 * Pick past searches at bottom of first screen.
 * View search results and tap item to view details.
 * Close button on detail page to return to first screen.
 * 
 */
package com.monigarr.blogsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.widget.ShareActionProvider;

import com.monigarr.blogsearch.FileFunctions;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends SherlockActivity {
	
	EditText _searchField;
	Button _searchGo;
	ListView _listview;

	String _history;
	JSONArray _results;
	Context _context;
	String _passedData;
	List<Map<String, String>> _blogData;
	SimpleAdapter _adapter;
	ArrayList<String> _titleArray = new ArrayList<String>();
	String _fav;
	String _passed;
	GetBlog getBlog;
	HashMap<String, String> _recent = new HashMap<String, String>();
	Spinner _recentsList;
	ArrayList<String> _recentTitle = new ArrayList<String>();
	boolean _check = false;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//string array help ids from values/helparray.xml
        String[] idArray = {"share","search","fave","search2"};
        
        // initialize HelpDisplayer with idArrays and display the tips
        HelpDisplayer mTD = HelpDisplayer.getInstanceOf(this);
        mTD.setIdArrays(idArray);
        mTD.showHelpDialog(this);
		
        // Receive and Update Favorites
        getAndUpdate();
        
		//Search field and button
		_searchField = (EditText) findViewById(R.id.editText_1);
        _searchGo = (Button) findViewById(R.id.button_1);
        
        // ListView Adapter setup
        _listview = (ListView) findViewById(R.id.listView1);
        
        // Find spinner set listAdapter
        _recentsList = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, _recentTitle);
		listAdapter.setDropDownViewResource(R.layout.spinner_layout);
		_recentsList.setAdapter(listAdapter);
		_recentsList.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
        	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
        		if(pos > 0){
        			_fav = parent.getItemAtPosition(pos).toString();	
        			if (_fav.equals("Previous Search") || _fav == "Previous Search"){
        			} else {
        				_searchField.setText(_fav);
        			}
        		}
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
        });
        
        _searchGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(_searchField.getWindowToken(), 0);
					if (_searchField.getText().toString().equals("") || _searchField.getText().toString().equals(" ") || 
							_searchField.getText().toString() == "" || _searchField.getText().toString() == " ")
					{ 
						Toast toast = Toast.makeText(getApplicationContext(), "Search for Android or Test", Toast.LENGTH_SHORT);
						toast.show();
					}
					else
					{
						getBlogs(_searchField.getText().toString());
					}
            	}
			
        });
        
	}
	
	/**
	 * Update recents.
	 */
	private void updateRecents() {
    	for(String key : _recent.keySet()) {
    		_recentTitle.add(key);
    	}
    }
    
    /**
     * Gets the recents.
     *
     * @return the recents
     */
    @SuppressWarnings("unchecked")
	private HashMap<String, String> getRecents() {
    	Object stored = FileFunctions.readObjectFile(getApplicationContext(), "recent", false);
    	
    	HashMap<String, String> recents;
    	if (stored == null) {
			Log.i("RECENTS", "NO RECENTS FOUND");
			recents = new HashMap<String, String>();
		} else {
			recents = (HashMap<String, String>) stored;
		}
    	return recents;
    }
    
    /**
     * Gets the and update.
     *
     * @return the and update
     */
    private void getAndUpdate() {
    	if (!_recentTitle.isEmpty()) {
    		_recentTitle.clear();
		}
    	_recentTitle.add("Previous Search");
    	_recent = getRecents();
    	updateRecents();
    }
	
	private Handler theHandler = new Handler(){
		public void handleMessage(Message message){
			Object path = message.obj;
			if (message.arg1 == RESULT_OK && path != null){
				String bloglist = (String) message.obj.toString();
				try{
					JSONObject json = new JSONObject(bloglist);	
	    			JSONArray _results = json.getJSONArray("posts");
	    			if(_results == null){
	    				Toast toast = Toast.makeText(getApplicationContext(), "No blog posts found.", Toast.LENGTH_SHORT);
						toast.show();
	    			}else{
	    				Log.i("THE RESULTS", _results.toString());
	    			
				        Uri initProvider = Uri.parse("content://com.monigarr.blogsearch.provider/" +_searchField.getText().toString());
						getContentResolver().update(initProvider, null, _results.toString(), null);
						getAndUpdate();
				        
						_blogData = new ArrayList<Map<String, String>>();
						
					    for(int i=0;i<_results.length();i++){							
							JSONObject s = _results.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>(2);
							map.put("detail", s.getString("id"));
							map.put("title", s.getString("title"));
							map.put("excerpt", s.getString("excerpt"));
						    map.put("url", s.getString("url"));
						    _blogData.add(map);
					        
						    // List adapter is set
					        _adapter = new SimpleAdapter(getApplicationContext(), _blogData, R.layout.list_item2,
					                new String[] {"title","excerpt", "url"},
					                new int[] {R.id.text1,
					                           R.id.text2});
					        _listview.setAdapter(_adapter);
					        
					        //clickable list items
					        _listview.setOnItemClickListener(new OnItemClickListener() {
					        	@Override
					        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
					        		@SuppressWarnings("unchecked")
									HashMap<String, String> o = (HashMap<String, String>) _listview.getItemAtPosition(position);

				        			// EXPLICIT intent details view
				        			//Intent intent = new Intent(MainActivity.this, DetailPage.class);
				        			Intent intent = new Intent(getApplicationContext(), DetailPage.class);
				        			intent.putExtra("detail", o.toString());
				        			intent.putExtra("title", o.get("title"));
				        			intent.putExtra("excerpt", o.get("excerpt"));
				        			intent.putExtra("url", o.get("url"));
				        			startActivity(intent);
								}
							});
					        
					        
					    }
				    }
			        
				} catch (JSONException e){
					Log.e("JSON", "JSON OBJECT EXCEPTION");
				}
			}
		}
	};
	
	/**
	 * Gets the blog posts.
	 *
	 * @param item the item
	 * @return the blog posts
	 */
	private void getBlogs(String item){
		if (_searchField.getText().toString().length() > 0){
			item = _searchField.getText().toString();
		} else {
			item = _fav;
		}
		_passed = item;
		Log.i("CHECK ITEM", item);
		
		// Searched Item and Messenger passed to GetBlog
		Messenger messenger = new Messenger(theHandler);
		Intent i = new Intent(getApplicationContext(), PassSearch.class);
		i.putExtra("item", item);
		i.putExtra("messenger", messenger);
		startService(i);
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your menu.
        getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        //XXX: For now, ShareActionProviders must be displayed on the action bar
        // Set file with share history to the provider and set the share intent.
        //MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
        //ShareActionProvider overflowProvider =
        //    (ShareActionProvider) overflowItem.getActionProvider();
        //overflowProvider.setShareHistoryFileName(
        //    ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        //overflowProvider.setShareIntent(createShareIntent());

        return true;
    }

    /**
     * Creates a sharing {@link Intent}.
     *
     * @return The sharing intent.
     */
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.setType("image/*");
        shareIntent.setType("text/plain");
        //Uri uri = Uri.fromFile(getFileStreamPath("ic_launcher.png"));
        //shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Monigarr.com Mobile App Test");
        return shareIntent;
    }

}
