/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 */
package com.monigarr.blogsearch;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class DetailFragment.
 */
public class DetailFragment extends SherlockFragment {
	
private DetailListener listener;
	
	/**
	 * The listener interface for receiving info events.
	 * The class that is interested in processing a info
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addInfoListener<code> method. When
	 * the info event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see InfoEvent
	 */
	public interface DetailListener{
		
		/**
		 * On main page.
		 */
		public void onMainPage();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.detailpage, container, false);
		
		Button B1 = (Button) view.findViewById(R.id.button1);
    	B1.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			listener.onMainPage();
    		}
    	});
		
		return view;
		
	};
    
	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
    
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (DetailListener) activity;
		}catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " implement Listener");
		}
	}
	

}
