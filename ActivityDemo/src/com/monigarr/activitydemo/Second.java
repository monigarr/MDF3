package com.monigarr.activitydemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * capture data from Main.java
 */

public class Second extends Activity {

	String _passedDetails;
	String _passedTitle;
	String _passedExcerpt;
	String _passedLink;
	String _passedSearch;
	ImageView _passedImage;
	String _passedComment;
	Button pickImage;
	ImageView imageView1;
	EditText commentField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		//setContentView(R.layout.second_fragment);
		
		@SuppressWarnings("unused")
		final Button pickImage = (Button) findViewById(R.id.pickImage);
		
		//IMPLICIT INTENT Get ImageView & Load Image we Selected from Gallery.
		
		 Bundle extras = getIntent().getExtras();
		 
		  		if(extras != null){
		  			//Get data from Implicit Intent
		  			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		  			imageView.setImageURI((Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM));
		  		}
		
		//EXPLICIT INTENT Get data from Main Activity
		Intent intent = getIntent();
		_passedDetails = intent.getStringExtra("Details");
		_passedTitle = intent.getStringExtra("title");
		_passedExcerpt = intent.getStringExtra("excerpt");
		_passedLink = intent.getStringExtra("url");
		_passedSearch = intent.getStringExtra("search");
		//_passedImage = intent.getStringExtra("imageView1");
		
		
		final TextView message = (TextView) findViewById(R.id.message);
		//final TextView searchView = (TextView) findViewById(R.id.searchview);
		final TextView itemTitle = (TextView) findViewById(R.id.item_title);
		final TextView itemSubTitle = (TextView) findViewById(R.id.item_subtitle);
		final TextView itemLink = (TextView) findViewById(R.id.item_link);
		final EditText commentField = (EditText) findViewById(R.id.commentField);
		
		//searchView.setText(_passedDetails);
		//message.setText("You Searched for: " + getIntent().getExtras().getString("search"));
		message.setText("You Searched for: " + _passedSearch);
		itemTitle.setText(_passedTitle);
		itemSubTitle.setText(_passedExcerpt);
		itemLink.setText(_passedLink);
		
		
		Button myButton = (Button) findViewById(R.id.go_btn);
		myButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(Second.this, Main.class));	
				
				//EXPLICIT INTENT
				//Details passed to Main Activity
    			//Intent intent = new Intent(getApplicationContext(), Second.class);
				Intent intent = new Intent(Second.this, Main.class);
    			intent.putExtra("title", _passedTitle);
    			intent.putExtra("excerpt", _passedExcerpt);
    			intent.putExtra("url",  _passedLink);
    			intent.putExtra("search", _passedSearch);
    			
    			_passedComment = commentField.getText().toString();
    			intent.putExtra("comment", commentField.getText().toString());
    			
    			//_passedImage = imageView1.get
    			//intent.putExtra("image", _passedImage);
    			startActivity(intent);
    			
			}
		});
	}//end onCreate
	
	/**
	 * Get image the end user picked from their device gallery.
	 * We will put this at top of listview of blog posts
	 *
	 * @param v the v
	 * @return the image
	 */
	public void getImage(View v){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Pick Image"),1);
	}
	
	/* When Image is picked from gallery
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				ImageView imgView = (ImageView) findViewById(R.id.imageView1);
						imgView.setImageURI((Uri)data.getData());
			}
		}
	}
}
