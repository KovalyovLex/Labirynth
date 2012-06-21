package com.flexymind.labirynth.screens.game.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLGameView extends GLSurfaceView{
	
	private GLRenderer render = null;
	
	public GLGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onResume(){
		super.onResume();
	}
	
	public void onDestroy(){
		render.destroyLevel();
	}
	
	public void setGameLevelName(String gameLeveName) {
		render = new GLRenderer(this.getContext(), gameLeveName);
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
	}

}
