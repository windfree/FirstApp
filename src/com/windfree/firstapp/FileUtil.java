package com.windfree.firstapp;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private final static String LOG_TAG = "FileUtil";
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	


	public static File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e(LOG_TAG, "Public Directory not created");
		}
		return file;
	}

	public static File getAlbumStorageDir(Context context, String albumName) {
		// Get the directory for the app's private pictures directory.
		File file = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e(LOG_TAG, "Private Directory not created");
		}
		return file;
	}
}
