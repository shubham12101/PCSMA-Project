package com.pcsma.ifhtt.locationService;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pcsma.ifhtt.R;

public class ListViewActivity extends ListActivity {

	String[] columns;
	
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		
		ArrayList<String> lab_arr = bundle.getStringArrayList("lab_array");
		
		 if(lab_arr.isEmpty()) {
				String arr = "No history found";
				ArrayList<String> log = new ArrayList<String>() ;
				log.add(arr);
				
				this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView1, log));
				
				listView = getListView();
			} 
			
			else {
	
		
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView1, lab_arr));
		
		listView = getListView(); }
		
	}

}
