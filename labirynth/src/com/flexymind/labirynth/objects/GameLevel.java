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
    	//collision_With_Field (mball, mplayField);
        mball.Update();
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
        	
        	vec2 = new Point(	sum.x - scal_mul(sum,v1) * v1.x / scal_mul(v1,v1),
        						sum.y - scal_mul(sum,v1) * v1.y / scal_mul(v1,v1));
        	
        	vec1 = new Point(	sum.x - vec2.x,
        						sum.y - vec2.y);
        	
        	//Log.v("Hit",Integer.toString(scal_mul(vec1,vec1)) + " " +Integer.toString(scal_mul(v1,v1)));
        	
        	if (	scal_mul(vec1,v1) >= 0 
        		 && scal_mul(vec1,vec1) <= scal_mul(v1,v1)
        		 && scal_mul(vec2,v2) >= 0
        		 && scal_mul(vec2,vec2) <= scal_mul(v2,v2)){
        		// удар
        		if (((float)scal_mul(vec1,vec1) / scal_mul(v1,v1)) > ((float)scal_mul(vec2,vec2) / scal_mul(v2,v2)) ){
        			// удар в направлении vec2
            		Log.v("Hit","V2" + Integer.toString(i));
            		mball.reflectWallVec2(twall);	
        		}else{
        			// удар в направлении vec1
            		Log.v("Hit","V1" + Integer.toString(i));
            		mball.reflectWallVec1(twall);
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
    private void collision_With_Field (Ball ball, Rect PlayField){
    	
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
