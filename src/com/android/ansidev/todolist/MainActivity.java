package com.android.ansidev.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
//import android.widget.Adapter;
import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

	ListView mListView;
	ArrayList<String> toDoListItems;
	ArrayAdapter<String> arrAdptr, autoArrAdptr;
	int selectedPosition = -1;

	AutoCompleteTextView mSingleComplete;
	private final static String[] autoCompleteText = new String[] {
		"Appointment",
		"Birthday",
		"Course",
		"Deadline",
		"Examination",
		"Homework",
		"Meeting",
		"Seminar",
		"Picnic",
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get references to UI widget
		mListView = (ListView) findViewById(R.id.myListView);
		mSingleComplete = (AutoCompleteTextView)findViewById(R.id.myAutoCompleteTextView);
		
		//Create ArrayList of to do list items
		toDoListItems = new ArrayList<String>();
		
		//Create ArrayAdapter to bind the array to the ListView
		arrAdptr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,toDoListItems);
		
		//Bind the array to the ListView
		mListView.setAdapter(arrAdptr);
		mSingleComplete.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN) {
					if((keyCode == KeyEvent.KEYCODE_ENTER)) {
						if(!mSingleComplete.getText().toString().matches("")) {
							toDoListItems.add(mSingleComplete.getText().toString());
							arrAdptr.notifyDataSetChanged();
							mSingleComplete.setText("");
						}
						return true;
					}
				}
				return false;
			}
		});
		
        // Setting the item click listener for the listview
        mListView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		selectedPosition = position;
        		//Register Context Menu for ListView
        		registerForContextMenu(view);
        		view.setLongClickable(false);
        		openContextMenu(view);
            }
		});
        
		autoArrAdptr = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteText);
		mSingleComplete.setAdapter(autoArrAdptr);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("UserSettings", 0);
		Editor editor = sharedPref.edit();
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
//	    AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
//	    ListView lv = (ListView) menuInfo.targetView.getParent(); 
	    switch (item.getItemId()) {
	        case R.id.itemEdit:
//	        	final EditText mEditText = (EditText) findViewById(R.id.myEditText);
	            AlertDialog.Builder medBuilder = new AlertDialog.Builder(this);
	            medBuilder.setTitle("Edit your task"); 
	            medBuilder.setMessage("Enter your text");
	 
	            // Set an EditText view to get user input 
	            final EditText mEditDialog = new EditText(this);
	            medBuilder.setView(mEditDialog);
	 
	            medBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	toDoListItems.set(selectedPosition,mEditDialog.getEditableText().toString());
		            	arrAdptr.notifyDataSetChanged();
//		            	mEditText.setText(mEditDialog.getEditableText().toString());
//		            	mEditText.setSelection(mEditText.getText().length());
		            }
	            });
	            medBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					    dialog.cancel();
					}
	            });
	            AlertDialog alertDialog = medBuilder.create();
	            alertDialog.show();	 
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	            return true;
	        case R.id.itemMoveTop:
	        	Functions.moveTop(toDoListItems, selectedPosition);
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	        	return true;
	        case R.id.itemMoveUp:
	        	Functions.moveUp(toDoListItems, selectedPosition);
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	        	return true;
	        case R.id.itemMoveDown:
	        	Functions.moveDown(toDoListItems, selectedPosition);
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	        	return true;
	        case R.id.itemMoveBottom:
	        	Functions.moveBottom(toDoListItems, selectedPosition);
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	        	return true;
	        case R.id.itemDelete:
	        	toDoListItems.remove(selectedPosition);
	        	arrAdptr.notifyDataSetChanged();
	        	selectedPosition = -1;
	            return true;
	        default:
	        	selectedPosition = -1;
	            return super.onContextItemSelected(item);
	    }
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()) {
		case R.id.itemDeleteAll:
			for(int i = 0; i < toDoListItems.size(); i += 0) {
        		toDoListItems.remove(i);
        	}
        	arrAdptr.notifyDataSetChanged();
        	return true;
		case R.id.itemSettings:
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivityForResult(intent, 100);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK)) {
			//Alert Dialog when Back is clicked
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to do this action?")
			.setCancelable(false)
			.setPositiveButton("Yes", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton("No", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}