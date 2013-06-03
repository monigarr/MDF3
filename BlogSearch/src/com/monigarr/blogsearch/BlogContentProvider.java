/*
 * project	BlogSearch
 * package	com.monigarr.blogsearch
 * @author	Monica Peters
 * date		May 23, 2013 
 */

package com.monigarr.blogsearch;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.*;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class BlogContentProvider.
 */
public class BlogContentProvider extends ContentProvider {
	
	public static String PROVIDER_NAME = "com.monigarr.blogsearch.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://" +PROVIDER_NAME+ "/item");
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.monigarr.blogsearch.provider", "", 1);
		uriMatcher.addURI("com.monigarr.blogsearch.provider", "*", 2);
	}
	
	HashMap<String, String> _recents;

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
		case 1:
			return "com.monigarr.blogsearch.provider.recents";
		case 2:
			return "com.monigarr.blogsearch.search";
			default:
				throw new IllegalArgumentException("Unsupported URI: "+ uri);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCreate() {
		Context context = getContext();
		Object stored = FileFunctions.readObjectFile(context, "recent", false);
		if (stored == null){
			_recents = new HashMap<String, String>();
			return false;
		} else {
			_recents = (HashMap<String, String>) stored;
			return true;
		}
	}


	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MatrixCursor result = new MatrixCursor(new String[] {"_id", "title", "excerpt", "url"});
		if(uriMatcher.match(uri)==2){
			String item = uri.getPathSegments().get(0).toString();
			String value = _recents.get(item);
			if(value != null){
				try{
					JSONObject data = new JSONObject(value);
					result.addRow(new Object[] {0, data.get("title"), data.get("excerpt"), data.get("url") });
				} catch (JSONException e) {
					Log.e("JSON EXCEPTION", e.toString());
				}
			}
		} else {
			int id = 0;
			for (Map.Entry<String, String> entry : _recents.entrySet()){
				String value = entry.getValue();
				try{
					JSONObject data = new JSONObject(value);
					result.addRow(new Object[] {id, data.get("title"), data.get("excerpt"), data.get("url") });
				} catch (JSONException e){
					Log.e("JSON EXCEPTION", e.toString());
				}
			}
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri)==2){
			String item = uri.getPathSegments().get(0).toString();
			_recents.put(item, selection);
			FileFunctions.storeObjectFile(getContext(), "recent", _recents, false);
			return 1;
		} else {
			return 0;
		}
	}

}
