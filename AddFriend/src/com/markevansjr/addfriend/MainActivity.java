package com.markevansjr.addfriend;

import java.io.File;
import java.util.HashMap;

import com.parse.Parse;
import com.parse.ParseObject;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	
	HashMap<String, String> _recents;
	int TAKE_PICTURE = 1;
	Uri imageUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
				(connec.getNetworkInfo(0).isAvailable() == true)){
			this.setTitle("New Friend..");
		
			Parse.initialize(this, "PV07xPdLpxzJKBKUS2UP6iJ0W9GbEHvmfPMMQovz", "OPxrcJKt4Pe26WHvCAeuI86hnGzXjOlO1NmyfJyP"); 
		
			WebView myWebView = (WebView) findViewById(R.id.webView1);
			myWebView.loadUrl("http://www.markevansjr.com/AndroidApp/index.html");
		
			WebSettings webSettings = myWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
		
			myWebView.setWebViewClient(new WebViewClient());
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
	    	ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
					(connec.getNetworkInfo(0).isAvailable() == true)){
				Intent i = new Intent(getApplicationContext(), AddFriend.class); 
				startActivity(i);
			} else {
				Toast toast = Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT);
	    		toast.show();
			}
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
	
	public class WebAppInterface {
	    Context mContext;

	    /** Instantiate the interface and set the context */
	    WebAppInterface(Context c) {
	        mContext = c;
	    }

	    /** Show a toast from the web page */
	    //@JavascriptInterface
	    public void showData(String[] data) {	        
	        String firstName = data[0];
	        String lastName = data[1];
	        String phoneNumber = data[2];
	        String emailAddress =data[3];
	        
	        // A LITTLE VAILDATION
	        if (firstName.equals("") || firstName.length() == 0 ) {
	        	Toast toast = Toast.makeText(getApplicationContext(), "CHECK FIRST NAME", Toast.LENGTH_LONG);
				toast.show();
	        } else if (lastName.equals("") || lastName.length() == 0 ) {
	        	Toast toast = Toast.makeText(getApplicationContext(), "CHECK LAST NAME", Toast.LENGTH_LONG);
				toast.show();
	        } else if (phoneNumber.equals("") || phoneNumber.length() == 0 || phoneNumber.length() < 12)  {
	        	Toast toast = Toast.makeText(getApplicationContext(), "CHECK YOUR PHONE NUMBER", Toast.LENGTH_LONG);
				toast.show();
	        } else if (emailAddress.equals("") || emailAddress.length() == 0 || !(emailAddress.contains("@"))) {
	        	Toast toast = Toast.makeText(getApplicationContext(), "CHECK EMAIL", Toast.LENGTH_LONG);
				toast.show();
	        } else {
	        	ParseObject contactObject = new ParseObject("ContactObject");
	        	contactObject.put("fname", firstName);
	        	contactObject.put("lname", lastName);
	        	contactObject.put("phone", phoneNumber);
	        	contactObject.put("email", emailAddress);
	        	contactObject.saveInBackground();
	        
	        	Intent i = new Intent(mContext, AddFriend.class); 
				startActivity(i);
	        }
	    }
	    
	  //@JavascriptInterface
	    public void showCamera(String c) {
	    	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
	        imageUri = Uri.fromFile(photo);
	        startActivityForResult(intent, TAKE_PICTURE);
	    }
	    
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	        if (resultCode == Activity.RESULT_OK) {
	            Uri selectedImage = imageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	            //ImageView imageView = (ImageView) findViewById(R.id.ImageView);
	            ContentResolver cr = getContentResolver();
	            @SuppressWarnings("unused")
				Bitmap bitmap;
	            try {
	                 bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

	                //imageView.setImageBitmap(bitmap);
	                Toast.makeText(this, selectedImage.toString(),
	                        Toast.LENGTH_LONG).show();
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	            }
	      
	        }
	}
}
