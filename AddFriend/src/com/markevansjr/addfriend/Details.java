package com.markevansjr.addfriend;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class Details extends Activity {
	
	String _allData;
	String _fname;
	String _lname;
	String _phone;
	String _email;
	String _fullname;
	String _theId;
	ImageView _imageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		Button btn = (Button) findViewById(R.id.button1);
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.button3);
		Button btn4 = (Button) findViewById(R.id.button4);
		_imageView = (ImageView) findViewById(R.id.imageView1);
		
		Intent i = getIntent();
		_allData = i.getStringExtra("AllData");
		_fname = i.getStringExtra("fname");
		_lname = i.getStringExtra("lname");
		_phone = i.getStringExtra("phone");
		_email = i.getStringExtra("email");
		_fullname = i.getStringExtra("fullname");
		_theId = i.getStringExtra("id");
		
		this.setTitle(_fullname);
		
		tv.setText(_fullname+"\n"+_phone+"\n"+_email);
		
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
				Log.i("TAG NUMBER", _phone);
				if (_phone.length() > 10) {
				       Uri number = Uri.parse("tel:" + _phone);
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
				Log.i("TAG EMAIL", _email);
				if (_email.contains("@")) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] {_email});
					intent.putExtra(Intent.EXTRA_SUBJECT, "RE: Hello Friend");
					intent.putExtra(Intent.EXTRA_TEXT, "Say hello..");

					startActivity(Intent.createChooser(intent, "Send Email"));
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "INVAILED NUMBER", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}); 
		
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery query = new ParseQuery("ContactObject");
				query.getInBackground(_theId, new GetCallback() {

					@Override
					public void done(ParseObject object, ParseException arg1) {
						object.deleteInBackground();
						Toast toast = Toast.makeText(getApplicationContext(), "FRIEND DELETED", Toast.LENGTH_SHORT);
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
	             String filePath = cursor.getString(columnIndex); // file path of selected image
	             cursor.close();
	             
	             Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	             _imageView.setImageBitmap(yourSelectedImage);
	         }
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
	    	Intent ii = new Intent(getApplicationContext(), MainActivity.class); 
			startActivity(ii);
	        break;
    	}
	    return true;
	}
}
