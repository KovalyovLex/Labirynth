package com.flexymind.labirynth.objects;

import java.util.Vector;

import com.android.pingpong.R;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Класс 
 * @author Kurnikov Sergey
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
		Walls = walls;}

    
    /** Отрисовка объектов на игровом поле */
    private void Draw(Canvas canvas)
    {
        mball.draw(canvas);
        Number = Walls.size();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).draw(canvas);
        }
    }
    
    /** Перемещение шарика и обновление объектов на экране */
    private void updateAllObjects()
    {
        mball.update();
        Number = Walls.size();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).update(); 
        }
    } 
    

    /** Функция, описывающая столкновения объектов между собой */
    private void collision(Ball ball, Vector <Wall> walls)
    {

    	if (GameObject.intersects(ball, walls, index))
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
       	
        }
    	
    }


    /** Функция, описывающая столкновения шарика с вертикальными стенками */
    private void collisionVertical (Ball ball, Rect PlayField){
    	
    	if (ball.getLeft() <= PlayField.left)
        {
            ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
            ball.reflectVertical();
        }
        else if (ball.getRight() >= PlayField.right)
        {
        	ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
        	ball.reflectVertical();
        }
    }
    
    /** Функция, описывающая столкновения шарика с горизонтальными стенками */
    
    private void collisionHorizontal(Ball ball, Rect PlayField){
 
	    if (ball.getLeft() <= PlayField.left)
	    {
	        ball.setLeft(PlayField.left + Math.abs(PlayField.left - ball.getLeft()));
	        ball.reflectVertical();
	    }
	    else if (ball.getRight() >= PlayField.right)
	    {
	    	ball.setRight(PlayField.right - Math.abs(PlayField.right - ball.getRight()));
	    	ball.reflectVertical();
	    }
    }
    
    /** условие прохождения уровня */
    protected boolean victory(int end_x, int end_y) {
    	boolean ween = false;
    		if ((mPoint.x == end_x)&(mPoint.y == end_y)){
    			ween=true;
    		}
    			
    	return ween;
    }

        
}
