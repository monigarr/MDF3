/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 * 
 */

/*
 * mrtips library is being used to make it easier to show help
 * info throughout this app.
 * https://github.com/lethargicpanda/mrtips
 * 
 */
package com.monigarr.blogsearch;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class HelpDisplayer.
 */
public class HelpDisplayer implements DialogInterface.OnClickListener {
	private static final String PREFERENCE_HELP = "help";
	
	private String helpId = "";
	
	public static Boolean clicked = false;
	
	private CheckBox checkBox;
	private static ArrayList<String> idList;
	
	// Elements to populate dialog
	private String titleDialogRes;
	private String imgDialog;
	private String textDialogRes;
	private String checkBoxDialogRes;
	private static HelpDisplayer instance;
	private Context context;
	
	
	/**
	 * Instantiates a new tips displayer.
	 */
	private HelpDisplayer() {
		super();
		
	}
	
	/**
	 * Gets the instance of.
	 *
	 * @param mContext the m context
	 * @return the instance of
	 */
	public static HelpDisplayer getInstanceOf(Context mContext){
		if(instance==null){
			instance = new HelpDisplayer();
			idList = new ArrayList<String>();
		}
		
		return instance;
	}
	
	
	/**
	 * Sets the id arrays.
	 *
	 * @param idArray the new id arrays
	 */
	public void setIdArrays(String[] idArray) {
		for (int i = 0; i < idArray.length; i++) {
			 idList.add(idArray[i]);
		}
	}
	
	
	/**
	 * Show help dialog.
	 *
	 * @param mContext the m context
	 */
	public void showHelpDialog(Context mContext){
		this.context = mContext; 
		Log.d("HelpDisplayer.showHelpDialog()", "clicked : " +clicked);
		if(!clicked){
			Iterator<String> idIterator = idList.iterator();
			while (idIterator.hasNext()){
				String currentId = idIterator.next();
				if(!isHidden(currentId)){
					helpId = currentId;
					break;
				}
			}
		
			Log.d("HelpDisplayer.showHelpDialog()", "currentID = " + helpId);
			
			if(helpId!=""){
				String[] myHelpItems = context.getResources().getStringArray(context.getResources().getIdentifier(helpId, "array", context.getPackageName()));

				titleDialogRes = myHelpItems[0];
				imgDialog = myHelpItems[1];
				textDialogRes = myHelpItems[2];
				checkBoxDialogRes = myHelpItems[3];		
				
				LinearLayout layout = new LinearLayout(context);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
				ScrollView mScrollView = new ScrollView(context);
				
				ImageView image = (ImageView) new ImageView(context);
				image.setImageResource(context.getResources().getIdentifier("drawable/"+imgDialog, null, context.getPackageName()));
				if(titleDialogRes!=""){
					image.setPadding(0, 0, 0, 0);
				} else {
					image.setPadding(0, 0, 0, 0);
				}
				
				layout.addView(image);
				
				TextView text = (TextView) new TextView(context);
				String textDialogString = context.getString(context.getResources().getIdentifier("string/"+textDialogRes, null, context.getPackageName()));
				text.setText(textDialogString);
				text.setPadding(10, 10, 10, 10);
				layout.addView(text);
				
				String checkBoxDialogString = context.getString(context.getResources().getIdentifier("string/"+checkBoxDialogRes, null, context.getPackageName()));
				checkBox = new CheckBox(context);
				checkBox.setText(checkBoxDialogString);
				
				layout.addView(checkBox);
				mScrollView.addView(layout);
		        
				Builder helpDialogBuilder = new AlertDialog.Builder(context);
		        
				if(titleDialogRes!=""){
					String titleDialogString = context.getString(context.getResources().getIdentifier("string/"+titleDialogRes, null, context.getPackageName()));
					helpDialogBuilder.setTitle(titleDialogString);
				}
		        
		        helpDialogBuilder.setView(mScrollView);
				helpDialogBuilder.setCancelable(false);
				helpDialogBuilder.setPositiveButton("OK", this);
				helpDialogBuilder.create().show();
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which){
			case Dialog.BUTTON_POSITIVE :
				if(checkBox!=null && checkBox.isChecked()){
					Log.i("HelpDisplayer.onClick()", "checked");
					setHiddenId(helpId);
				}
				
				clicked = true;
				dialog.dismiss();
			break;
		}
	}

	/**
	 * Checks if is hidden.
	 *
	 * @param id the id
	 * @return the boolean
	 */
	public Boolean isHidden(String id){
		SharedPreferences mPreferences = context.getSharedPreferences(PREFERENCE_HELP, Context.MODE_PRIVATE);
		return mPreferences.getBoolean(id, false);
	}
	
	/**
	 * Sets the hidden id.
	 *
	 * @param id the new hidden id
	 */
	public void setHiddenId(String id){
		SharedPreferences mPreferences = context.getSharedPreferences(PREFERENCE_HELP, Context.MODE_PRIVATE);
		Editor editor = mPreferences.edit();
		editor.putBoolean(id, true);
		editor.commit();
	}
	
	
}
