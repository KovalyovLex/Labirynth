package com.flexymind.labirynth.objects;

import java.util.Vector;


import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Класс 
 * @author Kurnikov Sergey + Kovalyov Alexaner
 * отрисовка всех стенок + соударение от стенок и движение шарика, условие прохождения уровня
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
	private FINISH mfinish;
    /**Игровое поле */
	private Rect mplayField = new Rect(65,30,720,415);
    int Number;
    Vector <Wall> Walls;
    
    /**
     * Конструктор 
     * @param Vector <Wall> walls Все стены данного уровня
     * @param finish_X, finish_Y  Финишное положение
     * @param Diam диаметр шарика
     * @param Ball шарик
     */
    public GameLevel(	Vector <Wall> walls,
						Ball ball, FINISH finish,
						Drawable mBackGr){
		//инициализируем параметры, переданные с помощью конструктора
		super(mBackGr);
		mball   = ball;
		mfinish = finish;
		Walls   = walls;
		Number  = Walls.size();
	}

    @Override
    /** Отрисовка объектов на игровом поле */
    public void Draw(Canvas canvas)
    {	
    	this.mImage.setBounds(canvas.getClipBounds());
    	this.mImage.draw(canvas);
    	mfinish.Draw(canvas);
    	mball.Draw(canvas);
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).Draw(canvas);
        }
    }
    
    @Override
    /** Перемещение объекта */
    public void Update()
    {	
    	collisionsCheck();
    	collision_With_Field (mball, mplayField);
    	victory();
        mball.Update();
        mfinish.Update();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).Update(); 
        }
    }
    
    /** Функция, описывающая столкновения объектов шар и станки между собой */
    private void collisionsCheck()
    {
    	Point p1, p2, p3, v1, v2;
    	Point vec1, vec2, sum;
    	Wall twall;

        for(int i=0;i<Number;i++){
        	twall = Walls.elementAt(i);
        	p1 = new Point (	twall.getPoint1().x - mball.mWidth / 2,
        						twall.getPoint1().y -  mball.mHeight / 2);
        	
        	p2 = new Point (	twall.getPoint2().x - mball.mWidth / 2,
								twall.getPoint2().y +  mball.mHeight / 2);

        	p3 = new Point (	twall.getPoint3().x + mball.mWidth / 2,
								twall.getPoint3().y -  mball.mHeight / 2);
        	
        	v1 = new Point( p2.x - p1.x,
        					p2.y - p1.y);
        	
        	v2 = new Point( p3.x - p2.x,
							p3.y - p2.y);
        	
        	sum = new Point(	mball.getCenter().x - p1.x,
        						mball.getCenter().y - p1.y);
        	// проекция sum на v2
        	vec2 = new Point(	sum.x - scal_mul(sum,v1) * v1.x / scal_mul(v1,v1),
        						sum.y - scal_mul(sum,v1) * v1.y / scal_mul(v1,v1));
        	// проекция sum на v1
        	vec1 = new Point(	sum.x - vec2.x,
        						sum.y - vec2.y);
        	
        	if (	scal_mul(vec1,v1) >= 0 
        		 && scal_mul(vec1,vec1) <= scal_mul(v1,v1)
        		 && scal_mul(vec2,v2) >= 0
        		 && scal_mul(vec2,vec2) <= scal_mul(v2,v2)){
        		// удар
        		float minV1 = ((float)scal_mul(vec1,vec1) / scal_mul(v1,v1) > ((float)(scal_mul(v1,v1) - scal_mul(vec1,vec1)) / scal_mul(v1,v1)) )?
        				((float)(scal_mul(v1,v1) - scal_mul(vec1,vec1)) / scal_mul(v1,v1)) : (float)scal_mul(vec1,vec1) / scal_mul(v1,v1); 
        		float minV2 = ((float)scal_mul(vec2,vec2) / scal_mul(v2,v2) > ((float)(scal_mul(v2,v2) - scal_mul(vec2,vec2)) / scal_mul(v2,v2)) )?
                				((float)(scal_mul(v2,v2) - scal_mul(vec2,vec2)) / scal_mul(v2,v2)) : (float)scal_mul(vec2,vec2) / scal_mul(v2,v2); 
        		if ( minV1 > minV2 ){
        			// удар в направлении v2
            		mball.reflectWallV2(twall);
            		Log.v("reflect","v2");
        		}else{
        			// удар в направлении v1
            		mball.reflectWallV1(twall);
            		Log.v("reflect","v1");
        		}
        	}
        }
    	
    }

    private int scal_mul(Point p1, Point p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }

    /** Функция, описывающая столкновения шарика с ограничивающими стенками */
    private void collision_With_Field (Ball ball, Rect PlayField){
    	
    	if (ball.getLeft() <= PlayField.left)
        {
            //ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
            ball.reflectVertical(new Point(PlayField.left, ball.getPoint().y));
            Log.v("reflect","vertical");
        }
        else if (ball.getRight() >= PlayField.right)
        {
        	//ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
        	ball.reflectVertical(new Point(PlayField.right - ball.getWidth(), ball.getPoint().y));
        	Log.v("reflect","vertical");
        }
    	
    	if (ball.getTop() <= PlayField.top)
	    {
	        //ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
	        ball.reflectHorizontal(new Point(ball.getPoint().x, PlayField.top));
	        Log.v("reflect","horizontal");
	    }
	    else if (ball.getBottom() >= PlayField.bottom)
	    {
	    	//ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
	    	ball.reflectHorizontal(new Point(ball.getPoint().x, PlayField.bottom - ball.getHeight()));
	    	Log.v("reflect","horizontal");
	    }
    }
    
    /** условие прохождения уровня */
    protected void victory() {

    	if(GameObject.intersects_finish(mball, mfinish))
    	{
    		mball.setCenterY(mfinish.getCenter().y);
			mball.setCenterX(mfinish.getCenter().x);
    		
    	}
    	/*if (	 (bCenter.x >= end_x - diam / 2)
    			  && (bCenter.x <= end_x + diam / 2)
    			  && (bCenter.y >= end_y - diam / 2)
    			  && (bCenter.y <= end_y + diam / 2) ){
    			  
    		bCenter.y =end_y + diam / 2+(int) Math.sqrt(Math.abs(Math.pow(diam, 2)-Math.pow((bCenter.x-end_x - diam / 2), 2)));
			mball.setCenterY(bCenter.y);
			mball.setCenterX(bCenter.x);
    	}*/
    }
}
