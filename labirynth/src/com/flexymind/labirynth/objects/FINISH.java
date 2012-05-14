package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Класс Финиш, закатывание шарика в лунку
 * @author Kurnikov Sergey
 */
public class FINISH extends GameObject{

	private double finDiam;
	public FINISH(Drawable image, Point point, int finDiam) {
		super(image);
		mPoint = point;
        this.mHeight = this.mWidth = finDiam;
	}
	
	
	public void spiral(Ball ball){
		Point bcoord = ball.getCenter();
        bcoord.x = bcoord.x;
		bcoord.y = (int) (Math.sqrt(Math.pow(finDiam, 2)-Math.pow(bcoord.x, 2)));
	}
}
