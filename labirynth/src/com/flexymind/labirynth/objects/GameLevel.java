package com.flexymind.labirynth.objects;

import java.util.Vector;
import android.graphics.drawable.Drawable;

/**
 * Класс 
 * @author Alexander Kovalyov
 *
 */
public class GameLevel extends GameObject{
	
	
	public GameLevel(	Vector<Wall> walls,
						Ball ball,
						int finish_X,
						int finish_Y,
						int finish_Diam,
						Drawable mBackGr){
		super(mBackGr);
		
	}

	@Override
	protected void updatePoint() {
		// TODO Auto-generated method stub
		
	}
	
}
