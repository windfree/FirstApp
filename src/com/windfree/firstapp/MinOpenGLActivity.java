package com.windfree.firstapp;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

class MyRenderer implements GLSurfaceView.Renderer {	
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}
	
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0,
			javax.microedition.khronos.egl.EGLConfig arg1) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}
}

class MyGLSurfaceView extends GLSurfaceView {

	public MyGLSurfaceView(Context context){
    	super(context);

    	// Set the Renderer for drawing on the GLSurfaceView
    	setRenderer(new MyRenderer());
    	// Render the view only when there is a change in the drawing data
    	setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
}

public class MinOpenGLActivity extends Activity {
	private GLSurfaceView m_gl_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_min_open_gl);
		this.m_gl_view = new MyGLSurfaceView(this);
		setContentView(this.m_gl_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.min_open_gl, menu);
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
