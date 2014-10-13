package com.windfree.firstapp;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements MainFragment.onButtonClickListener {
	public final static String EXTRA_MESSAGE = "com.windfree.myfirstapp.MESSAGE";
	private final static String LOG_TAG = "FirstApp";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onResume()");
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(findViewById(R.id.container) != null) {
			if (savedInstanceState == null) {
				MainFragment mainFg = new MainFragment();
				mainFg.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, mainFg).commit();
			}
		}
	}

	@Override
	protected void onDestroy() {
		Log.e(LOG_TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(LOG_TAG, "onPause()");
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		// return super.onOptionsItemSelected(item);
	}

	public void onButtonClick(){
		Log.i(this.LOG_TAG, "activity onButtonClick");
		MainFragment mainFg = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_fg);
		if(mainFg != null){
			mainFg.onButtonClick();
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
//		 Context context = getActivity();
//		 SharedPreferences sharedPref = context.getSharedPreferences(
//		 getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences sharedPref = this
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		int newHighScore = 100;
		editor.putInt(getString(R.string.saved_high_score), newHighScore);
		editor.commit();

				
		int defaultValue = getResources().getInteger(
				R.integer.saved_high_score_default);
		long highScore = sharedPref.getInt(
				getString(R.string.saved_high_score), defaultValue);

		MainFragment mainFg = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_fg);
		if(mainFg != null){
			mainFg.setInfoText(Long.toString(highScore));
		}
//		TextView infoText = (TextView) findViewById(R.id.info_text);
//		this.m_info_text.setText(Long.toString(highScore));

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

		if (!FileUtil.isExternalStorageWritable()) {
			Log.e(LOG_TAG, "ExternalStorage is not Writable!");
			return;
		}
		// File externalDir = getAlbumStorageDir("TestPublicAlbum");
		try {
			String pathName = "/sdcard/test/";
			String fileName = "file.txt";
			File path = new File(pathName);
			File file = new File(pathName + fileName);
			if (!path.exists()) {
				Log.d("TestFile", "Create the path:" + pathName);
				path.mkdir();
			}
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + fileName);
				file.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(file);
			String s = "this is a test string writing to file.";
			byte[] buf = s.getBytes();
			stream.write(buf);
			stream.close();

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error on writeFilToSD.");
			e.printStackTrace();
		}
		// try{
		// outputStream = new FileOutputStream("/sdcard/test-pub-external.txt");
		// outputStream.write(string.getBytes());
		// outputStream.close();
		// }catch(Exception e){
		// e.printStackTrace();
		// }
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		try {
			super.onConfigurationChanged(newConfig);
			Log.i(LOG_TAG, "onConfigurationChanged");
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.i(LOG_TAG, "∫·∆¡");
				Configuration o = newConfig;
				o.orientation = Configuration.ORIENTATION_PORTRAIT;
				newConfig.setTo(o);
			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.i(LOG_TAG, " ˙∆¡");
			}
		} catch (Exception e) {

		}
	}
}
