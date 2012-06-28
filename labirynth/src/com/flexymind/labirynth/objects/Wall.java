package com.flexymind.labirynth.objects;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Wall extends GameObject{

	// показывать стену 60 кадров
	private final int SHOWTIME = 60;
	
	private PointF 	secPoint	= new PointF();
	
	private float softness = 1;
	
	private boolean show = false;
	
	private boolean animateOn = false;
	private boolean animateOff = false;
	
	private float minalfa = 0.1f;
	private float alpfa = minalfa;
	private int frameWasShow = 0;
	
	/** count for loaded texture (1 texture for all walls) */
	private static int countText = -1;

	/**
	 * Конструктор для стен прямоугольных стен (только горизонтальные или вертикальные)
	 * @param mBackG Текстура
	 * @param first Point 1
	 * @param second Point 2
	 * @param softness мягкость стены
	 */
	public Wall(	GL10 gl,
					Drawable mBackG,
					PointF first,
					PointF second,
					float softness){
		super(first, second, gl, mBackG);
		
		if (countText == -1){
			mSquare.loadGLTexture(gl, mBackG);
			countText = mSquare.getCount();
		}else{
			mSquare.setThisCount(countText);
		}
		
		this.softness = softness;
		
		mPoint		= first;
		secPoint	= second;
		
		mWidth = ((BitmapDrawable)mBackG).getBitmap().getWidth();
		mHeight = ((BitmapDrawable)mBackG).getBitmap().getHeight();
		
		mSquare.setLeftTop(mPoint);
        
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
	
	protected void onDestroy()
    {
		super.onDestroy();
    	countText = -1;
    }
	
}
