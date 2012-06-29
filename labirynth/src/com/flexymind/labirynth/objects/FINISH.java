package com.flexymind.labirynth.objects;


import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.storage.Settings;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;

/**
 * Класс Финиш, закатывание шарика в лунку
 * @author Kurnikov Sergey
 */
public class FINISH extends GameObject{

	public FINISH(	GL10 gl,
					Drawable image,
					PointF point,
					int finDiam) {
		super(gl, image);
		mPoint = point;
		mPoint.x *= Settings.getScaleFactorX();
        mPoint.y *= Settings.getScaleFactorY();
        
		float min = (Settings.getScaleFactorX() < Settings.getScaleFactorY()) ? (float)Settings.getScaleFactorX() : (float)Settings.getScaleFactorY();
        
		mSquare.setSize(finDiam * min, finDiam * min);
        refreshSize();
		mSquare.setLeftTop(mPoint);
	}
	
    /** Перемещение объекта */
    public void onUpdate() { }
	
    public float finDiam(){
		return this.mHeight;
    }
    
}



















