package com.example.esomount;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity {

// DECLARATIONS START
	Locale preferredLocale = Locale.getDefault();
	static ProgressBar mProgressBar;
	TextView timeDisplay, text1, text2, text3, text4, lastFed, feedAt;
	CountDownTimer mountCountDownTimer;
	int i = 0;
	long placeholder;        // placeholder used in onFeedButtonClick()
	long feedClickTimeStamp; // timeStamp created in onFeedButtonClick()
	int previousProgress;
	String time;
	String timestamp;
	String difference;
	String LAST_FED;
	String FEED_AT;
// DECLARATIONS END
	
	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		text1=(TextView)findViewById(R.id.timeStampTest1);
		text2=(TextView)findViewById(R.id.timeStampTest2);
		text3=(TextView)findViewById(R.id.timeStampTest3);
		text4=(TextView)findViewById(R.id.timeStampTest4);
		lastFed=(TextView)findViewById(R.id.lastFedTextView);
		feedAt=(TextView)findViewById(R.id.feedAtTextView);
		
		
		// get timeStamp and boolean from onFeedButtonClick() START
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		feedClickTimeStamp = prefs.getLong("savedOnFeedClick", placeholder);
		previousProgress = prefs.getInt("previous progress", 1);
		timestamp = Long.toString(feedClickTimeStamp);
		LAST_FED = prefs.getString("lastFed", LAST_FED);
		FEED_AT = prefs.getString("feedAt", FEED_AT);
		feedAt.setText("Feed at: " + FEED_AT);
		lastFed.setText("Last fed at: " + LAST_FED);
		// get timeStamp and boolean from onFeedButtonClick() END
		

		DateTime date = new DateTime();
		long currentTime = date.getMillis();
		
		long diffInMillis;
			if(feedClickTimeStamp==0){ // if there is no saved timeStamp then don't perform normal operation or it will give a negative countdown time.
				diffInMillis = currentTime;
        		feedAt.setText("");
        		lastFed.setText("");
			}else {
				diffInMillis = currentTime - feedClickTimeStamp;
			}
		i += diffInMillis;
		
		time = Long.toString(currentTime);
		difference = Long.toString(diffInMillis);


		
		text2.setText("Time Stamp: " + timestamp);
		text3.setText("millis since last feed button click: \n" + difference);
//		text4.setText(tester);
		
	
		Thread t = new Thread() {
			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(1000);
			        runOnUiThread(new Runnable() {
			          @Override
			          public void run() {
			        	 refreshUI();
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

			t.start();
		
		
	
		// PROGRESS BAR START
		mProgressBar = (ProgressBar)findViewById(R.id.mountProgressBar);
		mProgressBar.setProgress(i);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			mProgressBar.setRotation(180);
		} else{
		    // FIND A WAY TO ROTATE PROGRESSBAR BEFORE API 11 (3.0)
		}
		timeDisplay=(TextView)findViewById(R.id.mount_countdown);
		mountCountDownTimer = new CountDownTimer(72000000,1000) {
			
			@Override
		        public void onTick(long millisUntilFinished) {
		            Log.v("Log_tag", "Tick of Progress "+ i + " " + millisUntilFinished);
		            i++;
		            mProgressBar.setProgress(i);
		            
		            	timeDisplay.setText(convertMillis(millisUntilFinished-i));
		            
		        }
		
		        @Override
		        public void onFinish() {
		        	
		        	placeholder = 0;
	        		SharedPreferences.Editor editor = prefs.edit();
	        		editor.putLong("savedOnFeedClick", 0).commit();
	        		editor.remove("previous progress").commit();
	        		String progressBarTitle = "mount countdown";
	        		timeDisplay = (TextView)findViewById(R.id.mount_countdown);
	        		timeDisplay.setText(progressBarTitle.toUpperCase(preferredLocale));
	        		feedAt = (TextView)findViewById(R.id.feedAtTextView);
	        		lastFed = (TextView)findViewById(R.id.lastFedTextView);
	        		feedAt.setText("");
	        		lastFed.setText("");
		        	mProgressBar.setProgress(72000000);
		        	mountCountDownTimer.cancel();
		        //Do what you want 
		            i=0;
		            Context context = getApplicationContext();
		            Toast countdownFinishToast = Toast.makeText(context, "Your mount can now be fed again.", Toast.LENGTH_SHORT);
		            countdownFinishToast.show();
		        }
		};
		// PROGRESS BAR END
		
		if (savedInstanceState != null) {
			
	    } 
		else {
			
	    }
	
	}// onCreate END 
	
	public void refreshUI() {
		DateTime testDate = new DateTime();
		text1.setText(Long.toString(testDate.getMillis()));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		mountCountDownTimer.cancel();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(feedClickTimeStamp>0){
			mountCountDownTimer.start();
		}
		
		
	}
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	public void onFeedButtonClick(View view) {
		DateTime dt = new DateTime();	
			DateTimeFormatter fmt = DateTimeFormat.forPattern("hh:mm a (E, MM/dd)");
			final String LAST_FED = fmt.print(dt);
			final long placeholder = dt.getMillis();
			long feedAtLong = placeholder + 72000000;
			DateTime feedDate =  new DateTime(feedAtLong);
			final String FEED_AT = fmt.print(feedDate);
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		feedClickTimeStamp = prefs.getLong("savedOnFeedClick", placeholder);
		previousProgress = prefs.getInt("previous progress", 1);
		
		// toast
		Context context = getApplicationContext();
		CharSequence toastTextOnFeed = "Feed mount timer started.";
		CharSequence toastTextOnNewFeed = "New feed mount timer started.";
		int toastOnFeedDuration = Toast.LENGTH_SHORT;
		final Toast toastOnFeed = Toast.makeText(context, toastTextOnFeed, toastOnFeedDuration);
		final Toast toastOnNewFeed = Toast.makeText(context, toastTextOnNewFeed, toastOnFeedDuration);
		// toast
		
		if(feedClickTimeStamp==0){
			
			i=0;
			mProgressBar.setProgress(0);
			SharedPreferences.Editor editor = prefs.edit();
    		editor.putLong("savedOnFeedClick", placeholder).commit();
			editor.putString("lastFed", LAST_FED).commit();
			editor.putString("feedAt", FEED_AT).commit();
			feedAt.setText("Feed at: " + FEED_AT);
    		lastFed.setText("Last fed at: " + LAST_FED);
			mountCountDownTimer.start();
			toastOnNewFeed.show();
			
			
		}
		else if(feedClickTimeStamp!=0){
			new AlertDialog.Builder(this)
		    .setTitle("New Feed Timer \n(erases current)")
		    .setMessage("Are you sure you want to start a new timer? \n(Current timer will be erased.)")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	i=0;
		        	mProgressBar.setProgress(0);
		        	mountCountDownTimer.cancel();
		    		SharedPreferences.Editor editor = prefs.edit();
		    		editor.putLong("savedOnFeedClick", placeholder).commit(); //saves timestamp in millis
		    		editor.putString("lastFed", LAST_FED).commit();
		    		editor.putString("feedAt", FEED_AT).commit();
		    		feedAt.setText("Feed at: " + FEED_AT);
		    		lastFed.setText("Last fed at: " + LAST_FED);
		    		mountCountDownTimer.start();
		    		toastOnFeed.show();
		        	}
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            dialog.dismiss();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();
		}
		
	}
	
	public void onMountSettingsButtonClick(View view) {
		Intent myIntent = new Intent(HomeActivity.this, MountSettingsActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}
	
	@SuppressLint("DefaultLocale") 
	public void onResetButtonClick(View view) {
		
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		feedClickTimeStamp = prefs.getLong("savedOnFeedClick", placeholder);
			new AlertDialog.Builder(this)
		    .setTitle("Reset Timer?")
		    .setMessage("Reset mount timer? \n(Current timer will be erased.)")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 

		        		placeholder = 0;
		        		SharedPreferences.Editor editor = prefs.edit();
		        		editor.putLong("savedOnFeedClick", 0).commit();
		        		editor.remove("previous progress").commit();
		        		String progressBarTitle = "mount countdown";
		        		timeDisplay = (TextView)findViewById(R.id.mount_countdown);
		        		timeDisplay.setText(progressBarTitle.toUpperCase(preferredLocale));
		        		feedAt = (TextView)findViewById(R.id.feedAtTextView);
		        		lastFed = (TextView)findViewById(R.id.lastFedTextView);
		        		feedAt.setText("");
		        		lastFed.setText("");
			        	mProgressBar.setProgress(72000000);
			        	mountCountDownTimer.cancel();

		        }})
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            dialog.dismiss();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();
			
	}
	
	@SuppressLint("DefaultLocale") 
	public static String convertMillis(long milliseconds){
		long seconds, minutes, hours;
		seconds = milliseconds / 1000;
		minutes = seconds / 60;
		seconds = seconds % 60;
		hours = minutes / 60;
		minutes = minutes % 60;
		
		Locale.getDefault();
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return(time);
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
	
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.home,
					container, false);
			return rootView;
		}
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();  // Always call the superclass

	    mProgressBar.destroyDrawingCache();
	    mountCountDownTimer.cancel();
	    android.os.Debug.stopMethodTracing(); // Stop method tracing that the activity started during onCreate()
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
}

	