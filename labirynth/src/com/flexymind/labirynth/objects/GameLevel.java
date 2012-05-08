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
    int end_x, 
        end_y, 
        diam ,
        Number;
    Vector <Wall> Walls;
    
    /**
     * Конструктор 
     * @param Vector <Wall> walls Все стены данного уровня
     * @param finish_X, finish_Y  Финишное положение
     * @param Diam диаметр шарика
     * @param Ball шарик
     */
    public GameLevel(	Vector <Wall> walls, //как инициализировать этот вектор?
						Ball ball,
						int finish_X,
						int finish_Y,
						int finish_Diam,
						Drawable mBackGr){
		//инициализируем параметры, переданные с помощью конструктора
		super(mBackGr);
        end_x = finish_X;
		end_y = finish_Y;
		diam  = finish_Diam;
		mball = ball;
		Walls = walls;
		Number = Walls.size();
	}

    @Override
    /** Отрисовка объектов на игровом поле */
    public void Draw(Canvas canvas)
    {	
    	this.mImage.setBounds(canvas.getClipBounds());
    	this.mImage.draw(canvas);
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
        mball.Update();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).Update(); 
        }
    }

    /** Функция, описывающая столкновения объектов шар и станки между собой */
    private void collisionsCheck()
    {
    	Point vec1, vec2, ballvec;
    	Wall twall;
    	int vec1Len, vec2Len;

        for(int i=0;i<Number;i++){
        	twall = Walls.elementAt(i);
        	vec1 = new Point (	twall.getPoint2().x - twall.getPoint1().x,
        						twall.getPoint2().y - twall.getPoint1().y);
        	vec1Len = (int)Math.sqrt(scal_mul(vec1,vec1));
        	vec1.x += (int)(vec1.x * (float)(mball.mWidth / vec1Len));
        	vec1.y += (int)(vec1.x * (float)(mball.mHeight / vec1Len));
        	vec1Len = (int)Math.sqrt(scal_mul(vec1,vec1));
        	
        	vec1.x /= vec1Len;
        	vec1.y /= vec1Len;
        	
        	vec2 = new Point (	twall.getPoint3().x - twall.getPoint2().x,
								twall.getPoint3().y - twall.getPoint2().y);
        	vec2Len = (int)Math.sqrt(scal_mul(vec2,vec2));
        	vec2.x += (int)(vec1.x * (float)(mball.mWidth / vec2Len));
        	vec2.y += (int)(vec1.x * (float)(mball.mHeight / vec2Len));
        	vec2Len = (int)Math.sqrt(scal_mul(vec2,vec2));
        	
        	ballvec = new Point(mball.getCenter().x - twall.getPoint1().x,
        						mball.getCenter().y - twall.getPoint1().y);
        	
        	vec2.x /= vec2Len;
        	vec2.y /= vec2Len;
        	
    		//Log.v("HIT",Integer.toString(scal_mul(ballvec,vec1)) + " " + Integer.toString(scal_mul(ballvec,vec2)));
    		//Log.v("LEN",Integer.toString(vec1Len) + " " + Integer.toString(vec2Len));
        	
        	if (	scal_mul(ballvec,vec1) >= 0 
        		 && scal_mul(ballvec,vec1) <= vec1Len 
        		 && scal_mul(ballvec,vec2) >= 0
        		 && scal_mul(ballvec,vec2) <= vec2Len){
        		
        		Log.v("HIT", Integer.toString(mball.getCenter().x) + " " + Integer.toString(mball.getCenter().y));
        		
        		// удар об стенку
        		if ((float)scal_mul(ballvec,vec1) / vec1Len > (float)scal_mul(ballvec,vec2) / vec2Len){
        			// отражение от стенки в напрвлении vec2
        			mball.reflectWallVec1(twall);
        		}else{
        			// отражение от стенки в напрвлении vec1
        			mball.reflectWallVec2(twall);
        		}
        	}
        }
    	
    	/*if (GameObject.intersects(ball, walls, index))
        {
        	//проверка столкновения с верхней и нижней границей стенки
    	    if(ball.getTop() <=  walls.elementAt(index).getTop())
        	{
        		ball.setBottom( walls.elementAt(index).getBottom() - Math.abs( walls.elementAt(index).getBottom() - ball.getBottom()));
        		ball.reflectHorizontal();
        	}
    	    else if(ball.getBottom() >=  walls.elementAt(index).getBottom())
            {
            	ball.setTop(walls.elementAt(index).getTop() + Math.abs( walls.elementAt(index).getTop() - ball.getTop()));
            	ball.reflectHorizontal();  
            }

          	//проверка столкновения с правой и левой границей стенки
    	    else if(ball.getRight() <=  walls.elementAt(index).getRight())
            {
            	ball.setBottom(walls.elementAt(index).getBottom() - Math.abs(walls.elementAt(index).getBottom() - ball.getBottom()));
            	ball.reflectVertical();
            }
    	    else if(ball.getLeft() >=  walls.elementAt(index).getLeft())
            {
            	ball.setBottom( walls.elementAt(index).getBottom() - Math.abs( walls.elementAt(index).getBottom() - ball.getBottom()));
            	ball.reflectVertical();
            }
       	
        }*/
    	
    }

    private int scal_mul(Point p1, Point p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }

    /** Функция, описывающая столкновения шарика с ограничивающими стенками */
    private void collision_Vith_Field (Ball ball, Rect PlayField){
    	
    	if (ball.getLeft() <= PlayField.left)
        {
            //ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
            ball.reflectVertical();
        }
        else if (ball.getRight() >= PlayField.right)
        {
        	//ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
        	ball.reflectVertical();
        }
    	
    	if (ball.getTop() <= PlayField.top)
	    {
	        //ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
	        ball.reflectHorizontal();
	    }
	    else if (ball.getBottom() >= PlayField.bottom)
	    {
	    	//ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
	    	ball.reflectHorizontal();
	    }
    }
    
    /** условие прохождения уровня */
    protected boolean victory(int end_x, int end_y) {
    	boolean ween = false;
    	Point bCenter = mball.getCenter();
    		if (	 (bCenter.x >= end_x - diam / 2)
    			  && (bCenter.x <= end_x + diam / 2)
    			  && (bCenter.y >= end_y - diam / 2)
    			  && (bCenter.y <= end_y + diam / 2) ){
    			ween=true;
    		}
    	
    	return ween;
    }     
}
