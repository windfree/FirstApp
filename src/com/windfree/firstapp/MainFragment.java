package com.windfree.firstapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.VideoView;

public class MainFragment extends ListFragment {
	public final static String EXTRA_MESSAGE = "com.windfree.myfirstapp.MESSAGE";
	private final static String LOG_TAG = "FirstApp";
	private final static String TTS_FUNCTION = "TTS";
	private final static int TTS_ACTION = 0;

	private TextView m_info_text;
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private ImageView m_photo_icon;
	private Bitmap m_photo_bitmap;
	
	static final int REQUEST_VIDEO_CAPTURE = 2;
	private VideoView m_video_view;
	private Uri m_video_uri;
	
	private ListView m_function_list_view;
	private List<Map<String, Object>> m_function_list_data;
	
	private onButtonClickListener m_button_click_callback;	
	public interface onButtonClickListener{
		public void onButtonClick();
		public void sendMessage(View view);
		public void saveTest(View view);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			this.m_button_click_callback = (onButtonClickListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implement onButtonClickListener");
		}
	}

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		// ∑¢ÀÕ◊÷∑˚¥Æ∏¯¡Ì“ª∏ˆactivityœ‘ æ
		Button sendMessageBtn = (Button) rootView.findViewById(R.id.send_message_btn);
		sendMessageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				m_button_click_callback.sendMessage(view);
			}
		});
		// ¥Ê¥¢≤‚ ‘
		Button saveTestBtn = (Button) rootView.findViewById(R.id.save_test_btn);
		saveTestBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				m_button_click_callback.saveTest(view);
			}
		});
		// ¬º÷∆≤¢≤•∑≈ ”∆µ≤‚ ‘
		Button takeVideoBtn = (Button) rootView.findViewById(R.id.take_video_btn);
		takeVideoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dispatchTakeVideoIntent();
			}
		});
		// ≈ƒ’’≤¢œ‘ æÕº∆¨≤‚ ‘
		Button takePhotoBtn = (Button) rootView.findViewById(R.id.take_photo_btn);
		takePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dispatchTakePictureIntent();
			}
		});
		this.m_info_text = (TextView) rootView.findViewById(R.id.info_text);
		this.m_photo_icon = (ImageView) rootView.findViewById(R.id.photo_icon);
		this.m_video_view = (VideoView) rootView.findViewById(R.id.video_view);
		this.m_function_list_view = (ListView) rootView.findViewById(android.R.id.list);
		this.m_function_list_data = getFunctionData();
		FunctionListAdapter adapter = new FunctionListAdapter(getActivity()); //, getFunctionData(), R.layout.function_btn_item, new String[]{"title"}, new int[]{R.id.function_btn});
		setListAdapter(adapter);
		
		this.m_photo_bitmap = null;
		this.m_video_uri = null;
		return rootView;
	}

	public final class ViewHolder{
		public Button btn;
	}
	
	public class FunctionListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public FunctionListAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_function_list_data.size();
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.function_btn_item, null);
				holder.btn = (Button)convertView.findViewById(R.id.function_btn);
				convertView.setTag(holder);
				
			}else {				
				holder = (ViewHolder)convertView.getTag();
			}			
			
			holder.btn.setText((String)m_function_list_data.get(position).get("title"));
			holder.btn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					do_function((int)m_function_list_data.get(position).get("action"));
				}
			});
			
			return convertView;
		}
	}
	private List<Map<String, Object>> getFunctionData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", this.TTS_FUNCTION);
		map.put("action", this.TTS_ACTION);
		list.add(map);
		
		return list;
	}

	private void do_function(int action) {
		switch (action) {
		case TTS_ACTION:
			Log.i(this.LOG_TAG, String.format("do %s test", this.TTS_FUNCTION));
			break;
		}
	}

	public void setInfoText(String info) {
		this.m_info_text.setText(info);
	}
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, this.REQUEST_IMAGE_CAPTURE);
	}
	
	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(takeVideoIntent, this.REQUEST_VIDEO_CAPTURE);
	}
	
	private void handleSmallCameraPhoto(Intent intent) {
		Bundle extras = intent.getExtras();
		this.m_photo_bitmap = (Bitmap) extras.get("data");
		this.m_photo_icon.setImageBitmap(this.m_photo_bitmap);
		this.m_photo_icon.setVisibility(View.VISIBLE);
	}
	
	private void handleCameraVideo(Intent intent) {
		this.m_video_uri = intent.getData();
		Log.i(this.LOG_TAG, "get video data in " + this.m_video_uri.toString());
		this.m_video_view.setVideoURI(this.m_video_uri);
		this.m_video_view.setVisibility(View.VISIBLE);
		this.m_video_view.start();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		Log.i(LOG_TAG, "onActivityResult start1");
		try {
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
				Log.i(LOG_TAG, "onActivityResult start2");
				handleSmallCameraPhoto(data);
			}
			if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == -1) {
				Log.i(this.LOG_TAG, "Let's handle video Now");
				handleCameraVideo(data);
		    }
		} catch (Exception e) {

		}
	}


	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
//		this.sendBroadcast(mediaScanIntent);
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
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		m_photo_icon.setImageBitmap(bitmap);
	}
	
	public void onButtonClick() {
		Log.i(this.LOG_TAG, "fragment onButtonClick");
//		dispatchTakeVideoIntent();
	}
}
