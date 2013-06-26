package com.markevansjr.addfriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AddFriend extends Activity {

	List<Map<String, String>> _data;
	List<Map<String, String>> _data2;
	SimpleAdapter _adapter;
	@SuppressWarnings("rawtypes")
	ArrayAdapter _adapter2;
	ListView _lv;
	Context _context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriend);
		
		ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
				(connec.getNetworkInfo(0).isAvailable() == true)){
		Parse.initialize(this, "PV07xPdLpxzJKBKUS2UP6iJ0W9GbEHvmfPMMQovz", "OPxrcJKt4Pe26WHvCAeuI86hnGzXjOlO1NmyfJyP"); 
        
		// Create our installation query
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("Recipe", true);
		
		// List View setup for data from PARSE
        _lv = (ListView) findViewById(R.id.listView1);
		
		ParseQuery query = new ParseQuery("ContactObject");
		query.findInBackground(new FindCallback() {
			@SuppressWarnings("unchecked")
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
    
					if (objects.toArray().length > 0){
						_data = new ArrayList<Map<String, String>>();
    
						for(int ii=0;ii<objects.toArray().length;ii++){							
							ParseObject s = objects.get(ii);
							Map<String, String> map = new HashMap<String, String>(2);
							map.put("fname", s.getString("fname"));
							map.put("lname", s.getString("lname"));
							map.put("phone", s.getString("phone"));
							map.put("email", s.getString("email"));
							map.put("id", s.getObjectId());
							map.put("fullname", s.getString("fname")+" "+s.getString("lname"));
							_data.add(map);
						}
				        
						// List adapter is set
						_adapter = new SimpleAdapter(getApplicationContext(), _data, android.R.layout.simple_list_item_2,
								new String[] {"fullname", "phone", "fname", "lname", "phone", "email"},
								new int[] {android.R.id.text1,
								android.R.id.text2});
							_lv.setAdapter(_adapter);
      
							_lv.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
								
								HashMap<String, String> o = (HashMap<String, String>) _lv.getItemAtPosition(position);
								
								Intent intent = new Intent(getApplicationContext(), Details.class);
								intent.putExtra("AllData", o.toString());
								intent.putExtra("fname", o.get("fname"));
								intent.putExtra("lname", o.get("lname"));
								intent.putExtra("phone", o.get("phone"));
								intent.putExtra("email", o.get("email"));
								intent.putExtra("fullname", o.get("fullname"));
								intent.putExtra("id", o.get("id"));
								startActivity(intent);
								
							}
						});
   
					} else {
						Log.i("FROM PARSE::", "NO DATA STORED");
						String[] thedata = {"You have no friends."};
						_adapter2 = new ArrayAdapter<Object>(getApplicationContext(), android.R.layout.simple_list_item_1, thedata);
						_lv.setAdapter(_adapter2);
					}
      
				} else {
					Log.e("ERROR::", "ERROR");
				}
			}
		});
		} else {
			Toast toast = Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT);
    		toast.show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()) {
	    case R.id.add:
	        //click on about item
	    	Log.i("TAG", "ADD");
	    	Intent i = new Intent(getApplicationContext(), AddFriend.class); 
			startActivity(i);
	        break;
	        
	    case R.id.home:
	        //click on about item
	    	Log.i("TAG", "HOME");
	    	ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
					(connec.getNetworkInfo(0).isAvailable() == true)){
				Intent ii = new Intent(getApplicationContext(), MainActivity.class); 
				startActivity(ii);
			} else {
				Toast toast = Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT);
	    		toast.show();
			}
	        break;
    	}
	    return true;
	}
}