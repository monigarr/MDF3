package com.markevansjr.addfriend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
 
    Activity activity;
    private List<Map<String, String>> data;
    LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public CustomAdapter(Activity a, List<Map<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

	public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
 
        TextView name = (TextView)vi.findViewById(R.id.list_name); // name
        TextView details = (TextView)vi.findViewById(R.id.list_detail); // details
        
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
 
        
        Map<String, String> contact = new HashMap<String, String>();
        contact = data.get(position);
 
        // Setting all values in listview
        name.setText(contact.get("fullname"));
        details.setText(contact.get("phone"));
        imageLoader.DisplayImage(contact.get(""), thumb_image);
        return vi;
    }
}