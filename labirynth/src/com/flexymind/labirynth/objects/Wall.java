package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Wall extends GameObject{

	private Point secPoint, thirdPoint;
	
	/**
	 * Конструктор по умолчанию
	 * @param mBackG Текстура
	 * @param first Point 1
	 * @param second Point 2
	 * @param third Point 3
	 */
	public Wall(	Drawable mBackG,
					Point first,
					Point second,
					Point third){
		super(mBackG);
		
		mPoint = first;
		thirdPoint = third;
		secPoint = second;
		
		mImage.setBounds(mPoint.x, mPoint.y, thirdPoint.x, thirdPoint.y);
		mWidth = mImage.getBounds().width();
        mHeight = mImage.getBounds().height();
	}

	/**
	 * Возвращает значение 1 точки
	 * @return point 1
	 */
	public Point getPoint1(){
		return mPoint;
	}
	
	/**
	 * Возвращает значение 2 точки
	 * @return point 2
	 */
	public Point getPoint2(){
		return secPoint;
	}
	
	/**
	 * Возвращает значение 3 точки
	 * @return point 3
	 */
	public Point getPoint3(){
		return thirdPoint;
	}
	
	@Override
	/**
	 * Изменяем положение точек на нашем дисплее и делаем scale текстуры
	 * @param ScaleFactorX - множитель размера экрана по оси X
	 * @param ScaleFactorY - множитель размера экрана по оси Y
	 */
	public void resize(double ScaleFactorX, double ScaleFactorY){
		mPoint.x = (int)(mPoint.x * ScaleFactorX);
		mPoint.y = (int)(mPoint.y * ScaleFactorY);
		secPoint.x = (int)(secPoint.x * ScaleFactorX);
		secPoint.y = (int)(secPoint.y * ScaleFactorY);
		thirdPoint.x = (int)(thirdPoint.x * ScaleFactorX);
		thirdPoint.y = (int)(thirdPoint.y * ScaleFactorY);
		
		mImage.setBounds(mPoint.x, mPoint.y, thirdPoint.x, thirdPoint.y);
		mWidth = mImage.getBounds().width();
        mHeight = mImage.getBounds().height();
	}
	
}
