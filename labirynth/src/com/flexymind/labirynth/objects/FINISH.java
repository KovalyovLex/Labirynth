package com.flexymind.labirynth.objects;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
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
	
	/** изменение размеров объекта */
    public void resize(double ScaleFactorX, double ScaleFactorY)
    {
    	int newX;
    	
    	mPoint.x = (int)(mPoint.x * ScaleFactorX);
    	mPoint.y = (int)(mPoint.y * ScaleFactorY);
    	
    	newX = (int)(ScaleFactorX * mWidth);
    	
    	mWidth = mHeight = newX;
    	
    	Bitmap bmp = ((BitmapDrawable)mImage).getBitmap();
    	Bitmap tmp = Bitmap.createScaledBitmap(bmp, newX, newX, true);
        bmp = tmp;
        mImage = new BitmapDrawable(bmp);
        onUpdate();
    }
	
    public int finDiam(){
		return this.mHeight;
    }
    
}



















