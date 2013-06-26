package com.monigarr.hybriddemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Details extends Activity {
	
	String _allData;
	String _project_name;
	String _project_url;
	String _project_notes;
	String _client_phone;
	String _client_email;
	String _theId;
	ImageView _imageView;
	Context _context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		Button btn = (Button) findViewById(R.id.button1);
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.button3);
		Button btn4 = (Button) findViewById(R.id.button4);
		_imageView = (ImageView) findViewById(R.id.imageView1);
		
		Intent i = getIntent();
		_allData = i.getStringExtra("AllData");
		_project_name = i.getStringExtra("project_name");
		_project_url = i.getStringExtra("project_url");
		_project_notes = i.getStringExtra("project_notes");
		_client_email = i.getStringExtra("client_email");
		_client_phone = i.getStringExtra("client_phone");
		_theId = i.getStringExtra("id");
		
		this.setTitle(_project_name);
		
		tv.setText(_project_name+"\n"+_project_url+"\n"+_project_notes+"\n"+_client_email+"\n"+_client_phone);
		
		
        btn4.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            	intent.setType("image/*");
            	startActivityForResult(intent, 0);
            }
        });
		
		// CALL 
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("TAG NUMBER", _client_phone);
				if (_client_phone.length() > 10) {
				       Uri number = Uri.parse("Phone:" + _client_phone);
				       Intent dial = new Intent(Intent.ACTION_CALL, number);
				       startActivity(dial);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "INVAILED NUMBER", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		
		// EMAIL
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("TAG EMAIL", _client_email);
				if (_client_email.contains("@")) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] {_client_email});
					intent.putExtra(Intent.EXTRA_SUBJECT, "RE: " + _project_name);
					intent.putExtra(Intent.EXTRA_TEXT, "Status update about " +_project_name);

					startActivity(Intent.createChooser(intent, "Email Client"));
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "INVAILED NUMBER", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}); 
		
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery query = new ParseQuery("ProjectObject");
				query.getInBackground(_theId, new GetCallback() {

					@Override
					public void done(ParseObject object, ParseException arg1) {
						object.deleteInBackground();
						Toast toast = Toast.makeText(getApplicationContext(), "DELETED PROJECT", Toast.LENGTH_SHORT);
						toast.show();
						
					}
				});
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
	     super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

	     switch(requestCode) {
	     case 0:
	         if(resultCode == RESULT_OK){ 
	             Uri selectedImage = imageReturnedIntent.getData();
	             String[] filePathColumn = {MediaStore.Images.Media.DATA};

	             Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	             cursor.moveToFirst();

	             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	             // file path of image
	             String filePath = cursor.getString(columnIndex);
	             cursor.close();
	             
	             Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	             _imageView.setImageBitmap(yourSelectedImage);
	         }
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
