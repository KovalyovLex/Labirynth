package com.flexymind.labirynth.objects;


import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Класс Финиш, закатывание шарика в лунку
 * @author Kurnikov Sergey
 */
public class FINISH extends GameObject{

	public FINISH(Drawable image, Point point, int finDiam) {
		super(image);
		mPoint = point;
        this.mHeight = this.mWidth = finDiam;
	}
	
    public int finDiam(){
		return this.mHeight;
    }
    
}



















