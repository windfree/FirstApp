package com.windfree.firstapp;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.windfree.myfirstapp.MESSAGE";
	private final static String LOG_TAG = "FirstApp";


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
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory. 
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Public Directory not created");
	    }
	    return file;
	}
	
	public File getAlbumStorageDir(Context context, String albumName) {
	    // Get the directory for the app's private pictures directory. 
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Private Directory not created");
	    }
	    return file;
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
		
		// save file
		String filename = "myFirstAppFile";
		String string = "Hello world!";
		FileOutputStream outputStream;

		try {
			// Context.MODE_PRIVATE so nobody can access file
			outputStream = openFileOutput(filename, Context.MODE_APPEND);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!isExternalStorageWritable()){
			Log.e(LOG_TAG, "ExternalStorage is not Writable!");
			return;
		}
		File externalDir = getAlbumStorageDir("TestPublicAlbum");
		try{
			outputStream = new FileOutputStream("TestPublicAlbum/test-pub-external.txt");
			outputStream.write(string.getBytes());
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
