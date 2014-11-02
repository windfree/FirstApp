package com.windfree.firstapp;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUiListener implements IUiListener {
	protected void doComplete(JSONObject values) {
	}

	@Override
	public void onError(UiError e) {
		
	}

	@Override
	public void onCancel() {
		
	}

	@Override
	public void onComplete(Object arg0) {
		// TODO Auto-generated method stub
		
	}
}
