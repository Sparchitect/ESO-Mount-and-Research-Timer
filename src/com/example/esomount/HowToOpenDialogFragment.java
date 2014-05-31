package com.example.esomount;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HowToOpenDialogFragment extends DialogFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.adjust_timer_fragment, null);
	}
}

//Command to open the dialog fragment is in MountSettingsActivity.java