package com.windfree.firstapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

class Triangle {
	private FloatBuffer m_vertex_buffer;
	private int m_program;
	private int m_position_handle;
	private int m_color_handle;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float triangleCoords[] = { // in counterclockwise order:
	0.0f, 0.622008459f, 0.0f, // top
			-0.5f, -0.311004243f, 0.0f, // bottom left
			0.5f, -0.311004243f, 0.0f // bottom right
	};
	private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	public final String vertexShaderCode = "attribute vec4 vPosition;"
			+ "void main() {" + "  gl_Position = vPosition;" + "}";

	public final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public Triangle() {
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		this.m_program = GLES20.glCreateProgram(); // create empty OpenGL ES Program
		GLES20.glAttachShader(this.m_program, vertexShader); // add the vertex shader to program
		GLES20.glAttachShader(this.m_program, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(this.m_program); // creates OpenGL ES program executables

		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
				triangleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		this.m_vertex_buffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		this.m_vertex_buffer.put(triangleCoords);
		// set the buffer to read the first coordinate
		this.m_vertex_buffer.position(0);
	}
	
	public void draw() {
	    // Add program to OpenGL ES environment
	    GLES20.glUseProgram(this.m_program);

	    // get handle to vertex shader's vPosition member
	    this.m_position_handle = GLES20.glGetAttribLocation(this.m_program, "vPosition");

	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(this.m_position_handle);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(this.m_position_handle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 vertexStride, this.m_vertex_buffer);

	    // get handle to fragment shader's vColor member
	    this.m_color_handle = GLES20.glGetUniformLocation(this.m_program, "vColor");

	    // Set color for drawing the triangle
	    GLES20.glUniform4fv(this.m_color_handle, 1, color, 0);

	    // Draw the triangle
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(this.m_position_handle);
	}
}

class Square {

	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { -0.5f, 0.5f, 0.0f, // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f, // bottom right
			0.5f, 0.5f, 0.0f }; // top right

	private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

	public Square() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 4 bytes per float)
				squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);
	}
}

class MyRenderer implements GLSurfaceView.Renderer {
	private Triangle m_triangle;
	private Square m_square;

	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		this.m_triangle.draw();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0,
			javax.microedition.khronos.egl.EGLConfig arg1) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		// initialize a triangle
		this.m_triangle = new Triangle();
		// initialize a square
		this.m_square = new Square();
	}
}

class MyGLSurfaceView extends GLSurfaceView {

	public MyGLSurfaceView(Context context){
    	super(context);

    	setEGLContextClientVersion(2);
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
