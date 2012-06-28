package com.flexymind.labirynth.screens.game.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class GLGameView extends GLSurfaceView implements SurfaceHolder.Callback{
	
	private GLRenderer render = null;
	
	public GLGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		 SurfaceHolder mHolder = getHolder();
	     mHolder.addCallback(this);
	     mHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return
		//render.destroyLevel();
    }
	
	public void setGameLevelName(String gameLeveName) {
		render = new GLRenderer(this.getContext(), gameLeveName);
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
	}

	public void setGameLevelID(int gameID) {
		render = new GLRenderer(this.getContext(), gameID);
		this.setRenderer(render);
		this.setRenderMode(RENDERMODE_CONTINUOUSLY);
	}
	
	public void onDestroy(){
		render.destroyLevel();
	}

}
