package com.monigarr.hybriddemo;

/*
 * Telephone Tutorial: http://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
 */

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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
	private static final int REQUEST_CODE = 1;
	private ImageView _imageView;
	Context _context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		Button btnPhone = (Button) findViewById(R.id.buttonPhone);
		Button btnEmail = (Button) findViewById(R.id.buttonEmail);
		Button btnDelete = (Button) findViewById(R.id.buttonDelete);
		//Button btnPicture = (Button) findViewById(R.id.buttonPicture);
		_imageView = (ImageView) findViewById(R.id.imageView1);
		
		// get image user picked from gallery or other image app
		// Get the intent that started this activity
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		
		//make sure it's an action and type we can handle
				if (Intent.ACTION_SEND.equals(action) && type != null){
				    //image is shared from another mobile app on device.
					if (type.startsWith("image/")) {
						handleSendImage(intent); // Handle single image being sent
					}
				}
				else if (Intent.ACTION_MAIN.equals(action) && type != null){
				    //app launched directly, not from share list
					Toast.makeText(Details.this,
							"Use Gallery Image on Device: 1. Tap Image. 2. Share Image on top right. 3. Choose RockMyPic app.",
							Toast.LENGTH_LONG).show();
				}
		
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
		
		// PICTURE
		/*
		btnPicture.setOnClickListener(new View.OnClickListener() {           
            @Override
            public void onClick(View view) {
            	//Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            	intent.setType("image/*");
            	startActivityForResult(intent, 0);
            }
        }); */
		
		// CALL 
        // PhoneStateListener
 		PhoneCallListener phoneListener = new PhoneCallListener();
 		TelephonyManager telephonyManager = (TelephonyManager) this
 			.getSystemService(Context.TELEPHONY_SERVICE);
 		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
     		
		btnPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("TAG NUMBER", _client_phone);
				if (_client_phone.length() >= 10) {
					Uri number = Uri.parse(_client_phone);
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + number));
					startActivity(callIntent);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "INVALID PHONE NUMBER", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		
		// EMAIL
		btnEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("TAG EMAIL", _client_email);
				if (_client_email.contains("@")) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] {_client_email});
					intent.putExtra(Intent.EXTRA_SUBJECT, "RE: " + _project_name);
					intent.putExtra(Intent.EXTRA_TEXT, "Check out " +_project_name + " online at " + _project_url);
					startActivity(Intent.createChooser(intent, "Email Client"));
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "INVALID EMAIL", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}); 
		
		// DELETE PROJECT
		btnDelete.setOnClickListener(new OnClickListener() {
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
	
	//monitor phone call activities
	private class PhoneCallListener extends PhoneStateListener {
 
		private boolean isPhoneCalling = false;
		String LOG_TAG = "LOGGING";
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
 
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");
				isPhoneCalling = true;
			}
 
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
				if (isPhoneCalling) {
					Log.i(LOG_TAG, "restart app");
					// restart app
					Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					isPhoneCalling = false;
				}
 
			}
		}
	}

	void handleSendImage(Intent intent) {
		//get uri of the received image
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			//set picture resample image data before showing
		    _imageView.setImageURI(imageUri);
		}
	}

	// user picked image from gallery app
	public void pickImage(View View) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, REQUEST_CODE);
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
