package com.monigarr.activitydemo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainFragment extends Fragment implements OnClickListener{

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
	
	
	public interface onSearchButtonClicked {
		void startResultActivity(String title, String excerpt, String url);
		
	}
	
	private onSearchButtonClicked parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if (activity instanceof onSearchButtonClicked){
			parentActivity = (onSearchButtonClicked) activity;
		}
		else{
			throw new ClassCastException(activity.toString() + "must implement onSearchButtonClicked");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.main_fragment, container);
		
		//setContentView(R.layout.main_fragment);
		
		TextView message = (TextView) view.findViewById(R.id.message);
		Button myButton = (Button) view.findViewById(R.id.go_btn);
		myButton.setOnClickListener(this);
		@SuppressWarnings("unused")
		ImageView itemImage = (ImageView) view.findViewById(R.id.item_image);
		
		@SuppressWarnings("unused")
		TextView comment = (TextView) view.findViewById(R.id.comment);
        message.setText("Enter ONE word. Try Searching for \"Mobile\" or \"IOS\" ");

        //IMPLICIT INTENT Get ImageView & Load Image we Selected from Gallery.
  		/*Bundle extras = getIntent().getExtras();
      		  		
        //EXPLICIT INTENT Get data from Main Activity
        Intent intent = getIntent();
	  	_passedDetails = intent.getStringExtra("Details");
	  	_passedTitle = intent.getStringExtra("title");
	  	_passedExcerpt = intent.getStringExtra("excerpt");
	  	_passedLink = intent.getStringExtra("url");
	  	_passedSearch = intent.getStringExtra("search");
	  	_passedComment = intent.getStringExtra("comment");
	  	//_passedImage = intent.getImageURI("image");
		
	  	//message.setText("You Searched for: " + _passedSearch);
	  	//itemImage.setText(_passedImage);
	  	//itemImage.setImageBitmap(_passedImage);
		itemTitle.setText(_passedTitle);
		//itemSubTitle.setText(_passedExcerpt);
		//itemLink.setText(_passedLink);
		comment.setText(_passedComment);
		*/
        
		return view;
	}

	
	public void onClick(View v) {

		parentActivity.startResultActivity(title, excerpt, url);
	}

}
