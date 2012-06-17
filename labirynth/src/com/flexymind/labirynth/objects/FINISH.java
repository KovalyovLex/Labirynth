package com.flexymind.labirynth.objects;


import javax.microedition.khronos.opengles.GL10;

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
        this.mHeight = this.mWidth = finDiam;
        mSquare.setSize(finDiam, finDiam);
        mSquare.setLeftTop(mPoint);
	}
	
    /** Перемещение объекта */
    public void onUpdate() { }
	
    public int finDiam(){
		return this.mHeight;
    }
    
}



















