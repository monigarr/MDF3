/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 */
package com.monigarr.blogsearch;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.widget.ShareActionProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class DetailPage.
 */
public class DetailPage extends SherlockFragmentActivity implements DetailFragment.DetailListener {
	
	String _passedBlogInfo;
	String _passedTitle;
	String _passedExcerpt;
	String _passedUrl;

	TextView tv;
	TextView tv2;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailfrag);

		Intent i = getIntent();
		_passedBlogInfo = i.getStringExtra("detail");
		_passedTitle = i.getStringExtra("title");
		_passedExcerpt = i.getStringExtra("excerpt");
		_passedUrl = i.getStringExtra("url");
		
		Log.i("URLNEW", _passedUrl);
			
		tv = (TextView) findViewById(R.id.textView1);
		tv.setText(_passedTitle);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv2.setText(_passedExcerpt);     
    	 	
    	Button bt1 = (Button) findViewById(R.id.btnImplicit);
        bt1.setOnClickListener(new OnClickListener() {
        	
        	/* Implicit Intent. Click button to load URL. 
        	 * URL passed from main activity */
             public void onClick(View v) {
            	 Intent internetIntent = new Intent(Intent.ACTION_VIEW,
        			 Uri.parse(_passedUrl));
        			 internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
        			 internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			 startActivity(internetIntent);
              	}
             }
         );	
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	*/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your menu.
        getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        //XXX: For now, ShareActionProviders must be displayed on the action bar
        // Set file with share history to the provider and set the share intent.
        //MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
        //ShareActionProvider overflowProvider =
        //    (ShareActionProvider) overflowItem.getActionProvider();
        //overflowProvider.setShareHistoryFileName(
        //    ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        //overflowProvider.setShareIntent(createShareIntent());

        return true;
    }

    /**
     * Creates a sharing {@link Intent}.
     *
     * @return The sharing intent.
     */
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.setType("image/*");
        //Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
        //shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, _passedTitle.toString() + _passedUrl.toString());
        return shareIntent;
    }
    

	@Override
	public void onMainPage() {
		// info page button
		Intent intent = new Intent(DetailPage.this, MainActivity.class);	
		//Set data to pass back
		intent.setData(Uri.parse("test"));
		setResult(RESULT_OK, intent);
		startActivity(intent);
		//Close activity
		finish();		
	}
	
}
