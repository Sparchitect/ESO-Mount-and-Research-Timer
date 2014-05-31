package com.example.esomount;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;



public class MountSettingsActivity extends ActionBarActivity {

	long feedClickTimeStamp;
	long placeholder;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mount_settings);
		
		// get timeStamp from onFeedButtonClick() and checkbox boolean START
			SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
			feedClickTimeStamp = prefs.getLong("savedOnFeedClick", placeholder);
		// get timeStamp from onFeedButtonClick() and checkbox boolean END
		
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mount, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	
	public void howToOpenDialogFragment (View view) {
		// dialog fragment code is in HowToOpenDialogFragment.java
		
		FragmentManager manager = getFragmentManager();
		HowToOpenDialogFragment timeDialog = new HowToOpenDialogFragment();
		timeDialog.show(manager, "My dialog");
		
	}
	public void openAdjustTimer (View view) {
		FragmentManager fmanager = getFragmentManager();
		AdjustTimerFragment newFrag = new AdjustTimerFragment();
		newFrag.show(fmanager, "Adjust Timer Fragment");
	}
	
	
	
	
	
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mount,
					container, false);
			return rootView;
		}
	}

    public void scheduleAlarm(View V) {
    	
    	// create and save mountReminder Boolean
    			boolean isMountReminderChecked = true;
    			SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
    			SharedPreferences.Editor editor = prefs.edit();
    			editor.putBoolean("mountReminderIsChecked", isMountReminderChecked).commit();
		// create and save mountReminder Boolean
      
          Long time = feedClickTimeStamp + 72000000;
          Intent intentAlarm = new Intent(this, AlarmReciever.class);
          AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
          alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
          Toast.makeText(this, "You will now be reminded to feed your mount.", Toast.LENGTH_LONG).show();
      
  }
	
}
