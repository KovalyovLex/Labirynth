package com.flexymind.labirynth.screens.OpenGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;

public class GLGameView extends GLSurfaceView implements GLSurfaceView.Renderer{

	private GL10 glSurfase = null;
	private Context context = null;
	private Square square;
	
	public GLGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void onPause(){
		super.onPause();
	}
	
	public void onResume(){
		super.onResume();
	}
	
	public void onDrawFrame(GL10 gl) {
		
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

		// Drawing
		gl.glTranslatef(0.0f, 0.0f, -5.0f); // move 5 units INTO the screen
											// is the same as moving the camera
											// 5 units away
											// gl.glScalef(0.5f, 0.5f, 0.5f); //
											// scale the square to 50%
		// otherwise it will be too large
		square.draw(gl); // Draw the triangle
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity(); // Reset The Modelview Matrix
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glSurfase = gl;
		this.square = new Square();
		square.loadGLTexture(gl, this.context);
		
		square.moveTo(new PointF(-2.9f,1.05f));
		
		gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
		gl.glClearColor(0.3f, 0.5f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do
		gl.glEnable(GL10.GL_DEPTH_TEST);
	    gl.glEnable(GL10.GL_BLEND); 				//Enable alpha blending
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); //Set the blend function
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

}
