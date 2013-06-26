package com.monigarr.hybriddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseAnalytics;

public class AddProject extends Activity {

	List<Map<String, String>> _data;
	List<Map<String, String>> _data2;
	SimpleAdapter _adapter;
	@SuppressWarnings("rawtypes")
	ArrayAdapter _adapter2;
	ListView _lv;
	Context _context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ParseAnalytics.trackAppOpened(getIntent());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addproject);

		ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connection != null
				&& (connection.getNetworkInfo(1).isAvailable() == true)
				|| (connection.getNetworkInfo(0).isAvailable() == true)) {
			
			
			Parse.initialize(this, "LGG1MbCVOrLbi6Hr6lXxiWTMoqEwjGQX4RAjjMCN", "XINCv5XuXUffmWtVGO41TXRf1P0SFowjSfay9L7C"); 
			
			// Create install query
			ParseQuery pushQuery = ParseInstallation.getQuery();
			pushQuery.whereEqualTo("ProjectObject", true);

			// List View for data from PARSE
			_lv = (ListView) findViewById(R.id.listView1);

			ParseQuery query = new ParseQuery("ProjectObject");
			query.findInBackground(new FindCallback() {
				@SuppressWarnings("unchecked")
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {

						if (objects.toArray().length > 0) {
							_data = new ArrayList<Map<String, String>>();

							for (int ii = 0; ii < objects.toArray().length; ii++) {
								ParseObject s = objects.get(ii);
								Map<String, String> map = new HashMap<String, String>(2);
								map.put("project_name", s.getString("project_name"));
								map.put("project_url", s.getString("project_url"));
								map.put("project_notes", s.getString("project_notes"));
								map.put("client_email", s.getString("client_email"));
								map.put("client_phone", s.getString("client_phone"));
								map.put("id", s.getObjectId());
								_data.add(map);
							}

							// set list adapter
							_adapter = new SimpleAdapter(
									getApplicationContext(), _data,
									android.R.layout.simple_list_item_2,
									new String[] { 
										"project_name",
										"project_url",
										"project_notes",
										"client_email",
										"client_phone" },
									new int[] { android.R.id.text1, android.R.id.text2 });
							_lv.setAdapter(_adapter);

							_lv.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

									HashMap<String, String> o = (HashMap<String, String>) _lv.getItemAtPosition(position);

									Intent intent = new Intent(getApplicationContext(), Details.class);
									intent.putExtra("AllData", o.toString());
									intent.putExtra("project_name", o.get("project_name"));
									intent.putExtra("project_url", o.get("project_url"));
									intent.putExtra("project_notes", o.get("project_notes"));
									intent.putExtra("client_email", o.get("client_email"));
									intent.putExtra("client_phone", o.get("client_phone"));
									intent.putExtra("id", o.get("id"));
									startActivity(intent);
								}
							});

						} else {
							Log.i("FROM PARSE:", "NO DATA STORED");
							String[] thedata = { "No Projects to Show. Tap Plus Button at Top Right." };
							_adapter2 = new ArrayAdapter<Object>(getApplicationContext(),android.R.layout.simple_list_item_1,thedata);
							_lv.setAdapter(_adapter2);
						}

					} else {
						Log.e("ERROR::", "ERROR");
					}
				}
			});

		}else{
			Toast toast = Toast.makeText(this, "NO CONNECTION",Toast.LENGTH_SHORT);
			toast.show();
		}
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

}