package com.windfree.firstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.windfree.myfirstapp.MESSAGE";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		 Inflate the menu items for use in the action bar
	    getMenuInflater().inflate(R.menu.main_activity_actions, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
//	            openSearch();
	            return true;
	        case R.id.action_settings:
//	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	public void saveTest(View view) {
//		Context context = getActivity();
//		SharedPreferences sharedPref = context.getSharedPreferences(
//		        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		int newHighScore = 100;
		editor.putInt(getString(R.string.saved_high_score), newHighScore);
		editor.commit();
		
		int defaultValue = getResources().getInteger(R.integer.saved_high_score_default);
		long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
		
		TextView infoText = (TextView)findViewById(R.id.info_text);
		infoText.setText(Long.toString(highScore));
	}

}
