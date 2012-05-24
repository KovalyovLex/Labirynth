package com.flexymind.labirynth.objects;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Wall extends GameObject{

	// показывать стену 30 кадров
	private final int SHOWTIME = 30;
	
	private Point 	secPoint	= new Point(),
					thirdPoint	= new Point(),
					leftUp		= new Point();
	
	private float softness = 1;
	
	private boolean show = false;
	
	private boolean animateOn = false;
	private boolean animateOff = false;
	
	private int alpfa = 2;
	private int frameWasShow = 0;
	
	/**
	 * Конструктор по умолчанию
	 * @param mBackG Текстура
	 * @param first Point 1
	 * @param second Point 2
	 * @param third Point 3
	 * @param softness мягкость стены
	 */
	public Wall(	Drawable mBackG,
					Point first,
					Point second,
					Point third,
					float softness){
		super(mBackG);
		
		this.softness = softness;
		
		mPoint = first;
		thirdPoint = third;
		secPoint = second;
		
		mImage.setBounds(mPoint.x, mPoint.y, thirdPoint.x, thirdPoint.y);
		
		mWidth = mImage.getBounds().width();
        mHeight = mImage.getBounds().height();
        
        mImage.setAlpha(0);
	}

	/**
	 * Конструктор для текстур которые можно поворачивать
	 * @param mBackG Текстура
	 * @param first Point 1
	 * @param second Point 2
	 * @param third Point 3
	 * @param shift расстояние от верхнего левого угла до Point1
	 * @param softness мягкость стены
	 */
	public Wall(	Bitmap mBackG,
					Point first,
					Point second,
					Point third,
					Point shift,
					float softness){
		super(new BitmapDrawable(mBackG));
		
		this.softness = softness;
		
		mPoint		= first;
		thirdPoint	= third;
		secPoint	= second;
		
		mWidth = mBackG.getWidth();
		mHeight = mBackG.getHeight();
		
		leftUp.x = mPoint.x - shift.x;
		leftUp.y = mPoint.y - shift.y;
		
		mImage.setBounds(leftUp.x, leftUp.y, leftUp.x + mWidth, leftUp.y + mHeight);
		
		mImage.setAlpha(0);
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
	
	/**
	 * возвращает коэффициент мягкости стены
	 * @return мягкость стены
	 */
	public float getSoftness(){
		return softness;
	}
	
	@Override
    /** Перемещение объекта */
    public void onUpdate(){
		if (animateOn){
			mImage.setAlpha(alpfa);
			alpfa += alpfa / 2;
			if (alpfa > 255){
				alpfa = 255;
				if (show){
					animateOn = false;
				}
				show = true;
			}
		}
		if (animateOff){
			mImage.setAlpha(alpfa);
			alpfa -= alpfa / 2;
			if (alpfa < 2){
				animateOff = false;
				alpfa = 2;
				mImage.setAlpha(0);
			}
		}
		if (show && frameWasShow++ >= SHOWTIME){
			frameWasShow = 0;
			show = false;
			animateOff = true;
			alpfa = 211;
		}
	}
	
	/**
	 * показать стену на 30 frame + 2 * 14 (анимация)
	 */
	protected void showWall(){
		if (animateOn){
			animateOn = true;
		}else{
			if (show){
				frameWasShow = 0;
			}else{
				if (animateOff){
					animateOff = false;
					animateOn = true;
				}else{
					animateOn = true;
				}
			}
		}
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
		leftUp.x = (int)(leftUp.x * ScaleFactorX);
		leftUp.y = (int)(leftUp.y * ScaleFactorY);

		mWidth	= (int)(mWidth * ScaleFactorX);
        mHeight	= (int)(mHeight * ScaleFactorY);

		mImage.setBounds(leftUp.x, leftUp.y, leftUp.x + mWidth, leftUp.y + mHeight);
	}
	
}
