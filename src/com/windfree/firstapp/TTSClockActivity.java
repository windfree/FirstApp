package com.windfree.firstapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TTSClockActivity extends Activity implements OnInitListener,
		OnClickListener {
	private static final int TEXT_TO_VOICE_INTENT = 0;
	private static final int VOICE_TO_TEXT_INTENT = 1;
	private static final int COUNT_DOWN_EVENT = 0;
	private TextToSpeech tts;

	private Button m_text_to_vocice_btn;
	private EditText m_input_tts_txt;
	private SpeechRecognizer m_speech_recognizer;
	private Button m_voice_to_text_btn;
	private ListView m_voice_to_text_list;

	private Button m_timer_clock_btn;
	private Button m_pause_clock_btn;
	private EditText m_input_total_time_txt;
	private TextView m_remain_time_txt;
	private Timer m_clock_timer;
	private boolean m_is_clock_start = false;
	private boolean m_is_clock_pause = false;
	private int m_total_time = 0;

	private Handler m_timer_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COUNT_DOWN_EVENT:
				refresh_remain_time();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onInit(int status) {
		Log.i(Consts.LOG_TAG, "init tts engine");
		if (status == TextToSpeech.SUCCESS) {
			Log.i(Consts.LOG_TAG, "init engine ok");
			Toast.makeText(TTSClockActivity.this,
					"Text-To-Speech engine is initialized", Toast.LENGTH_LONG)
					.show();
		} else if (status == TextToSpeech.ERROR) {
			Log.i(Consts.LOG_TAG, "init engine fail");
			Toast.makeText(TTSClockActivity.this,
					"Error occurred while initializing Text-To-Speech engine",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ttsclock);

		this.m_text_to_vocice_btn = (Button) findViewById(R.id.text_to_voice_btn);
		this.m_text_to_vocice_btn.setOnClickListener(this);
		this.m_input_tts_txt = (EditText) findViewById(R.id.input_tts_text);
		this.m_voice_to_text_btn = (Button) findViewById(R.id.voice_to_text_btn);
		this.m_voice_to_text_btn.setOnClickListener(this);
		this.m_voice_to_text_list = (ListView) findViewById(R.id.voice_to_text_list);
		this.init_recognizer();
		
		this.m_timer_clock_btn = (Button) findViewById(R.id.timer_clock_btn);
		this.m_timer_clock_btn.setOnClickListener(this);
		this.m_pause_clock_btn = (Button) findViewById(R.id.pause_clock_btn);
		this.m_pause_clock_btn.setOnClickListener(this);
		this.m_input_total_time_txt = (EditText) findViewById(R.id.input_total_time_txt);
		this.m_remain_time_txt = (TextView) findViewById(R.id.remain_time_txt);

		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			// speakButton.setOnClickListener(this);
		} else {
			// speakButton.setEnabled(false);
			// speakButton.setText("Recognizer not present");
		}
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TEXT_TO_VOICE_INTENT);
	}

	private void init_recognizer() {
		this.m_speech_recognizer = SpeechRecognizer.createSpeechRecognizer(this);
		if(SpeechRecognizer.isRecognitionAvailable(this) == false) {
			Toast.makeText(TTSClockActivity.this, "识别服务不可用",
					Toast.LENGTH_LONG).show();
			return;
		}
		this.m_speech_recognizer.setRecognitionListener(new RecognitionListener() {
			@Override
			public void onBeginningOfSpeech () {
				
			}
			@Override
			public void onError (int error) {
				
			}
			@Override
			public void onRmsChanged (float rmsdB) {
				
			}
			@Override
			public void onEvent (int eventType, Bundle params) {
				
			}
			@Override
			public void onReadyForSpeech (Bundle params) {
				
			}
			@Override
			public void onPartialResults (Bundle partialResults) {
				
			}
			@Override
			public void onBufferReceived (byte[] buffer) {
				
			}
			@Override
			public void onEndOfSpeech() {
				
			}
			@Override
			public void onResults (Bundle results) {
				
			}
		});
	}
	
	private void onTextToVoiceBtnClick(View v) {
		String text = this.m_input_tts_txt.getText().toString();
		if (text != null && text.length() > 0) {
			Toast.makeText(TTSClockActivity.this, "Saying: " + text,
					Toast.LENGTH_LONG).show();
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
	}

	private void onVoiceToTextBtnClick(View v) {
//		startVoiceRecognitionActivity();		
	}

	private void onTimerClockBtnClick(View v) {
		if (this.m_is_clock_start == true) {
			if (this.m_clock_timer != null) {
				this.m_clock_timer.cancel();
				this.m_clock_timer = null;
			}
			this.m_timer_clock_btn
					.setText(R.string.timer_clock_btn_label_start);
			this.m_pause_clock_btn
					.setText(R.string.pause_clock_btn_label_pause);
			this.m_remain_time_txt.setText("");
			this.m_is_clock_pause = false;
		} else {
			this.m_clock_timer = new Timer("ClockTimer", false);
			String total_time_str = this.m_input_total_time_txt.getText()
					.toString();
			try {
				this.m_total_time = Integer.parseInt(total_time_str);
				if (this.m_total_time <= 0) {
					Toast.makeText(TTSClockActivity.this, "请输入正整数",
							Toast.LENGTH_LONG).show();
				}
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						Message message = new Message();
						message.what = COUNT_DOWN_EVENT;
						m_timer_handler.sendMessage(message);
					}
				};
				this.m_clock_timer.schedule(task, 0, 1000);
				this.m_timer_clock_btn
						.setText(R.string.timer_clock_btn_label_stop);
			} catch (NumberFormatException e) {
				Toast.makeText(TTSClockActivity.this, "请输入正整数",
						Toast.LENGTH_LONG).show();
			}
		}
		this.m_is_clock_start = !this.m_is_clock_start;
	}

	private void onPauseClockBtnClick(View v) {
		if (this.m_is_clock_start) {
			if (this.m_is_clock_pause) {
				// 重新启动
				this.m_clock_timer = new Timer("ClockTimer", false);
				this.m_pause_clock_btn
						.setText(R.string.pause_clock_btn_label_pause);
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						Message message = new Message();
						message.what = COUNT_DOWN_EVENT;
						m_timer_handler.sendMessage(message);
					}
				};
				this.m_clock_timer.schedule(task, 1000, 1000);
			} else {
				// 暂停
				this.m_pause_clock_btn
						.setText(R.string.pause_clock_btn_label_restart);
				this.m_clock_timer.cancel();
				this.m_clock_timer = null;
			}
			this.m_is_clock_pause = !this.m_is_clock_pause;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_to_voice_btn:
			onTextToVoiceBtnClick(v);
			break;
		case R.id.timer_clock_btn:
			onTimerClockBtnClick(v);
			break;
		case R.id.pause_clock_btn:
			onPauseClockBtnClick(v);
			break;
		case R.id.voice_to_text_btn:
			onVoiceToTextBtnClick(v);
			break;
		}
	}

	private void refresh_remain_time() {
		m_remain_time_txt.setText(String.format("%d", m_total_time));
		m_total_time -= 1;
		if (m_total_time < 0) {
			m_clock_timer.cancel();
			m_clock_timer = null;
			m_timer_clock_btn.setText(R.string.timer_clock_btn_label_start);
			m_is_clock_start = false;
			// tts.speak("计时完成", TextToSpeech.QUEUE_ADD, null);
		}
	}

	// void test() {
	// try {
	// InputStream audio = new MicrophoneInputStream(11025, 11025 * 5); //设置输入参数
	// String cdir = SpeechRecognizer.getConsfigDir(null); // 获取语音识别配置目录
	// Recognizer recognizer = new Recognizer(cdir + "/baseline11k.par");
	// Recognizer.Grammar grammar = recognizer.new Grammar(cdir
	// + "/grammars/VoiceDialer.g2g");
	// grammar.setupRecognizer();
	// grammar.resetAllSlots();
	// grammar.compile();
	// recognizer.start(); // 开始识别
	// while (true) { // 循环等待识别结果
	// switch (recognizer.advance()) {
	// case Recognizer.EVENT_INCOMPLETE:
	// case Recognizer.EVENT_STARTED:
	// case Recognizer.EVENT_START_OF_VOICING:
	// case Recognizer.EVENT_END_OF_VOICING:
	// continue; // 未完成，继续等待识别结果
	// case Recognizer.EVENT_RECOGNITION_RESULT:
	// for (int i = 0; i < recognizer.getResultCount(); i++) {
	// String result = recognizer.getResult(i,
	// Recognizer.KEY_LITERAL);
	// Log.d(TAG, "result " + result);
	// mText.setText(result);
	// } // 识别到字串，显示并退出循环
	// break;
	// case Recognizer.EVENT_NEED_MORE_AUDIO:
	// recognizer.putAudio(audio) // 需要更多音频数据;
	// continue;
	// default:
	// break;
	// }
	// break;
	// }
	// recognizer.stop();
	// recognizer.destroy();
	// audio.close(); // 回收资源
	// } catch (IOException e) {
	// Log.d(Consts.LOG_TAG, "error", e);
	// }
	// }

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"Speech recognition demo");
			startActivityForResult(intent, VOICE_TO_TEXT_INTENT);
		} catch (ActivityNotFoundException e) {
//			Intent browserIntent = new Intent(
//					Intent.ACTION_VIEW,
//					Uri.parse("https://market.android.com/details?id=com.google.android.voicesearch"));
//			startActivity(browserIntent);
			Toast t = Toast.makeText(getApplicationContext(),
                    "Ops! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();

		}
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_TO_TEXT_INTENT && resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			this.m_voice_to_text_list.setAdapter(new ArrayAdapter(this,
					R.layout.voice_to_text_item, matches));
		}
		if (requestCode == TEXT_TO_VOICE_INTENT) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// Close the Text to Speech Library
		if (this.tts != null) {
			this.tts.stop();
			this.tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ttsclock, menu);
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
}
