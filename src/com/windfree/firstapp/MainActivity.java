package com.windfree.firstapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.windfree.myfirstapp.MESSAGE";
	private final static String LOG_TAG = "FirstApp";

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private ImageView m_photo_icon;

	@Override
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.m_photo_icon = (ImageView)findViewById(R.id.photo_icon);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	protected void onDestroy() {
		Log.e(LOG_TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.e(LOG_TAG, "onPause()");
		super.onPause();
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
		//File externalDir = getAlbumStorageDir("TestPublicAlbum");
		try {  
	        String pathName="/sdcard/test/";  
	        String fileName="file.txt";  
	        File path = new File(pathName);  
	        File file = new File(pathName + fileName);  
	        if( !path.exists()) {  
	            Log.d("TestFile", "Create the path:" + pathName);  
	            path.mkdir();  
	        }  
	        if( !file.exists()) {  
	            Log.d("TestFile", "Create the file:" + fileName);  
	            file.createNewFile();  
	        }  
	        FileOutputStream stream = new FileOutputStream(file);  
	        String s = "this is a test string writing to file.";  
	        byte[] buf = s.getBytes();  
	        stream.write(buf);            
	        stream.close();  
	          
	    } catch(Exception e) {  
	        Log.e(LOG_TAG, "Error on writeFilToSD.");  
	        e.printStackTrace();  
	    }
//		try{
//			outputStream = new FileOutputStream("/sdcard/test-pub-external.txt");
//			outputStream.write(string.getBytes());
//			outputStream.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	///////////////////////////////////////Take Photo start
//	private void dispatchTakePictureIntent() {
//	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//	    }
//	}
	
	public void onConfigurationChanged(Configuration newConfig) {  
        // TODO Auto-generated method stub  
        Log.i("UserInfoActivity", "onConfigurationChanged");  
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {  
            Log.i(LOG_TAG, "∫·∆¡");  
            Configuration o = newConfig;  
            o.orientation = Configuration.ORIENTATION_PORTRAIT;  
            newConfig.setTo(o);  
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {  
            Log.i(LOG_TAG, " ˙∆¡");  
        }  
        super.onConfigurationChanged(newConfig);  
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Bundle extras = data.getExtras();
				Bitmap imageBitmap = (Bitmap) extras.get("data");
				m_photo_icon.setImageBitmap(imageBitmap);
				galleryAddPic();
				setPic();
			} else {
				Log.e(LOG_TAG, "onActiveResult data is null!");
			}
		}
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	public void takePhoto(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        }
	    }
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	private void setPic() {
	    // Get the dimensions of the View
	    int targetW = m_photo_icon.getWidth();
	    int targetH = m_photo_icon.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    m_photo_icon.setImageBitmap(bitmap);
	}
}
