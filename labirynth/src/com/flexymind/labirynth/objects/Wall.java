package com.flexymind.labirynth.objects;

import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.storage.Settings;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;

public class Wall extends GameObject{

	// показывать стену 30 кадров
	private final int SHOWTIME = 60;
	
	private PointF 	secPoint	= new PointF(),
					thirdPoint	= new PointF(),
					leftUp		= new PointF();
	
	private float softness = 1;
	
	private boolean show = false;
	
	private boolean animateOn = false;
	private boolean animateOff = false;
	
	private float minalfa = 0.1f;
	private float alpfa = minalfa;
	private int frameWasShow = 0;

	/**
	 * Конструктор для текстур которые можно поворачивать
	 * @param mBackG Текстура
	 * @param first Point 1
	 * @param second Point 2
	 * @param third Point 3
	 * @param shift расстояние от верхнего левого угла до Point1
	 * @param softness мягкость стены
	 */
	public Wall(	GL10 gl,
					Bitmap mBackG,
					PointF first,
					PointF second,
					PointF third,
					PointF shift,
					float softness){
		super(gl, new BitmapDrawable(mBackG));
		
		this.softness = softness;
		
		mPoint		= first;
		thirdPoint	= third;
		secPoint	= second;
		
		mWidth = mBackG.getWidth();
		mHeight = mBackG.getHeight();
		
		leftUp.x = mPoint.x - shift.x;
		leftUp.y = mPoint.y - shift.y;
		leftUp.x *= Settings.getScaleFactorX();
		leftUp.y *= Settings.getScaleFactorY();
		
		mPoint.x *= Settings.getScaleFactorX();
		mPoint.y *= Settings.getScaleFactorY();
		
		mSquare.setLeftTop(leftUp);
        
		mSquare.setOpacity(1.0f);
	}
	
	/**
	 * Возвращает значение 1 точки
	 * @return point 1
	 */
	public PointF getPoint1(){
		return mPoint;
	}
	
	/**
	 * Возвращает значение 2 точки
	 * @return point 2
	 */
	public PointF getPoint2(){
		return secPoint;
	}
	
	/**
	 * Возвращает значение 3 точки
	 * @return point 3
	 */
	public PointF getPoint3(){
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
			mSquare.setOpacity(alpfa);
			alpfa += alpfa / 20;
			if (alpfa > 1){
				alpfa = 1;
				if (show){
					animateOn = false;
				}
				show = true;
			}
		}
		if (animateOff){
			mSquare.setOpacity(alpfa);
			alpfa -= alpfa / 20;
			if (alpfa < minalfa){
				animateOff = false;
				alpfa = minalfa;
				mSquare.setOpacity(0);
			}
		}
		if (show && frameWasShow++ >= SHOWTIME){
			frameWasShow = 0;
			show = false;
			animateOff = true;
			alpfa = 1;
		}
	}
	
	/**
	 * показать стену, начало анимации
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
	
	/**
	 * вызывается перед началом игры, чтобы спрятать стены
	 */
	protected void hideWall(){
		mSquare.setOpacity(0);
	}
	
}
