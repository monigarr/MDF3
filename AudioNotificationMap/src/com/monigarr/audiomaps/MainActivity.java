package com.monigarr.audiomaps;

/*
 * Developer: Monica Peters
 * Full Sail University
 * Mobile Dev Bachelor of Science
 * MDF3 Class
 * Instructor: Donna DeGardinier
 * 
 * Project Due: Thursday June 12, 2013
 * 
 * audio: http://developer.android.com/guide/topics/media/audio-capture.html
 * file storage / network management
 * 		save audio recording files (unique name for each) 
 * 		if internet available save audio recording to parse.  
 * 		if internet not available save audio recording to local device to be uploaded to parse
 * 			later when internet is available.
 * 
 */
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.monigarr.audiomaps.MainActivity;
import com.monigarr.audiomaps.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.monigarr.audiomaps.AddAudioActivity;
import com.monigarr.audiomaps.LoginOrSignupActivity;
import com.monigarr.audiomaps.MainActivity;
import com.monigarr.audiomaps.SelectUsersActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	public static final String TAG = MainActivity.class.getSimpleName();
	protected ProgressBar mProgressBar;

	//private static final String LOG_TAG = "AudioRecordTest";
	String mFileName = null;
	SimpleDateFormat sdf;
	String currentDateandTime;
	TextView showFileName = null;
	EditText fileName = null;

	RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;
	PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;
	SaveButton mSaveButton = null;

	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	class RecordButton extends Button {
		boolean mStartRecording = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording) {
					setText("Stop Recording");
				} else {
					setText("Record");
				}
				mStartRecording = !mStartRecording;
			}
		};

		public RecordButton(Context ctx) {
			super(ctx);
			setText("Start recording");
			setOnClickListener(clicker);
		}
	}

	class PlayButton extends Button {
		boolean mStartPlaying = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying) {
					setText("Stop Playing");
				} else {
					setText("Start Playing");
				}
				mStartPlaying = !mStartPlaying;
			}
		};

		public PlayButton(Context ctx) {
			super(ctx);
			setText("Start playing");
			setOnClickListener(clicker);
		}
	}

	private void onSave(boolean start) {
		if (start) {
			startSaving();
		} else {
			// message audio was not saved
		}
	}

	private void startSaving() {
		// save file locally and to parse cloud
		// mFileName
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(TAG, "prepare() failed");
		}
	}

	class SaveButton extends Button {
		// save file locally and to parse cloud
		boolean mStartSaving = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onSave(mStartSaving);
				if (mStartSaving) {
					setText("Saving Audio");
				} else {
					setText("Audio Not Saved");
				}
				mStartSaving = !mStartSaving;
			}
			// clear last file name so we save new file name next time we hit
			// record button
			// String mFileName = null;
		};

		public SaveButton(Context ctx) {
			super(ctx);
			setText("Saving Audio");
			setOnClickListener(clicker);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		// reviewing old skool layout setup from first Java classes.
		/*LinearLayout ll = new LinearLayout(this);

		EditText fileName = new EditText(this);
		fileName.setId(1);
		ll.addView(fileName, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));

		mRecordButton = new RecordButton(this);
		mRecordButton.setId(2);
		ll.addView(mRecordButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));

		mPlayButton = new PlayButton(this);
		mPlayButton.setId(3);
		ll.addView(mPlayButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));

		mSaveButton = new SaveButton(this);
		mSaveButton.setId(4);
		ll.addView(mSaveButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));

		TextView showTextView = new TextView(this);
		showTextView.setId(5);
		ll.addView(showTextView, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 0));

		fileName.setText(fileName.getText().toString());
		// fileName.getText().toString();
		showTextView.setText(mFileName);

		setContentView(ll);
		*/
	}

	public MainActivity() {
		//String unique = fileName.getText().toString();
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		//mFileName += "/audiorecordtest" + unique + ".3gp";
		mFileName += "/audiorecordtest" + ".3gp";
		// analytics
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLatestPosts();
	}

	protected void getLatestPosts() {
		mProgressBar.setVisibility(View.VISIBLE);

		/*
		 * Use ParseQuery to get latest posts
		 */
		ParseQuery query = new ParseQuery(AddAudioActivity.POSTS);
		query.setLimit(50);
		query.orderByDescending("createAt");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> results, ParseException e) {
				mProgressBar.setVisibility(View.INVISIBLE);

				if (e == null) {
					ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
					for (ParseObject result : results) {
						HashMap<String, String> article = new HashMap<String, String>();
						article.put(AddAudioActivity.KEY_NOTES,
								result.getString(AddAudioActivity.KEY_NOTES));
						article.put(AddAudioActivity.KEY_URL,
								result.getString(AddAudioActivity.KEY_URL));
						articles.add(article);
					}
					SimpleAdapter adapter = new SimpleAdapter(
							MainActivity.this, articles,
							android.R.layout.simple_list_item_2, new String[] {
									AddAudioActivity.KEY_NOTES,
									AddAudioActivity.KEY_URL }, new int[] {
									android.R.id.text1, android.R.id.text2 });
					setListAdapter(adapter);
				} else {
					Log.e(TAG, "Exception caught!", e);
					// Something went wrong.
					Toast.makeText(MainActivity.this, "Check Your Internet Connection, it may be slow or not connected.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TextView urlLabel = (TextView) v.findViewById(android.R.id.text2);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(urlLabel.getText().toString()));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addButton:
			startActivity(new Intent(this, AddAudioActivity.class));
			return true;
		case R.id.followButton:
			startActivity(new Intent(this, SelectUsersActivity.class));
			return true;
		case R.id.logoutButton:
			/*
			 * Log current user out using ParseUser.logOut()
			 */
			ParseUser.logOut();
			Intent intent = new Intent(this, LoginOrSignupActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
