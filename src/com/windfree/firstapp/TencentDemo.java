package com.windfree.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.tauth.Tencent;

public class TencentDemo extends Activity implements OnClickListener{
	private Tencent m_tencent;
	private BaseUiListener m_ui_listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tencent_demo);
		this.m_tencent = Tencent.createInstance(Consts.TENCENT_ID, this.getApplicationContext());
		this.m_ui_listener = new BaseUiListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tencent_demo, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.m_tencent.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qq_login_btn:
			if (!this.m_tencent.isSessionValid()) {
			this.m_tencent.login(this, "all", this.m_ui_listener);
			}
			break;
		default:
			break;
		}
	}
}
