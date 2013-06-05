package com.monigarr.rockmypic;

/*
 * RockMyPic
 * Developer: MoniGarr@Yahoo.com
 * Date:  June 5th 2013
 * 
 * Image Mobile Apps with Share Option can send image data to this app.
 * Spinner DropDown shows Intents  to send data from this app to
 *      other mobile apps installed / available on mobile device.
 * 
 * Intent Tutorial:  http://www.vogella.com/articles/AndroidIntent/article.html
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Spinner spinner;
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	private ImageView imageView;
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		// pick intents from dropdown
		spinner = (Spinner) findViewById(R.id.spinner);
		@SuppressWarnings("rawtypes")
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.intents, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		textView = (TextView) findViewById(R.id.textView);
		imageView = (ImageView) findViewById(R.id.result);

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
			Toast.makeText(MainActivity.this,
					"Use Gallery Image on Device: 1. Tap Image. 2. Share Image on top right. 3. Choose RockMyPic app.",
					Toast.LENGTH_LONG).show();
		}

	}

	void handleSendImage(Intent intent) {
		//get uri of the received image
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			//set picture resample image data before showing
		    imageView.setImageURI(imageUri);
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

	// button to start intent picked from dropdown
	public void onClick(View view) {
		int position = spinner.getSelectedItemPosition();
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.MoniGarr.com"));
			break;
		case 1:
			intent = new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:(+1)3156003351"));
			break;
		case 2:
			intent = new Intent(Intent.ACTION_DIAL,
					Uri.parse("tel:(+1)3156003351"));
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("geo:77.123,7.1333?z=19"));
			break;
		case 4:
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("geo:0,0?q=query"));
			break;
		case 5:
			intent = new Intent("android.media.action.IMAGE_CAPTURE");
			break;
		case 6:
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("content://contacts/people/"));
			break;
		case 7:
			intent = new Intent(Intent.ACTION_EDIT,
					Uri.parse("content://contacts/people/1"));
			break;

		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		InputStream stream = null;
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
			try {
				//recyle unused bitmaps
				if (bitmap != null) {
					bitmap.recycle();
				}
				stream = getContentResolver().openInputStream(data.getData());
				bitmap = BitmapFactory.decodeStream(stream);

				imageView.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
	}
}