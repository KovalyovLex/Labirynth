package com.flexymind.labirynth.objects;

import java.util.Vector;

import com.android.pingpong.R;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * ����� 
 * @author Kurnikov Sergey
 * ��������� ���� ������ + ���������� �� ������ � �������� ������, ������� ����������� ������
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
    int end_x, 
        end_y, 
        diam ,
        Number;
    Vector <Wall> Walls;
    /**
     * ����������� 
     * @param Vector <Wall> walls ��� ����� ������� ������
     * @param finish_X, finish_Y  �������� ���������
     * @param Diam ������� ������
     * @param Ball �����
     */
    public GameLevel(	Vector <Wall> walls, //��� ���������������� ���� ������?
						Ball ball,
						int finish_X,
						int finish_Y,
						int finish_Diam,
						Drawable mBackGr){
		//�������������� ���������, ���������� � ������� ������������
		super(mBackGr);
        end_x = finish_X;
		end_y = finish_Y;
		diam  = finish_Diam;
		mball = ball;
		Walls = walls;}

    
    /** ��������� �������� �� ������� ���� */
    private void Draw(Canvas canvas)
    {
        mball.draw(canvas);
        Number = Walls.size();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).draw(canvas);
        }
    }
    
    /** ����������� ������ � ���������� �������� �� ������ */
    private void updateAllObjects()
    {
        mball.update();
        Number = Walls.size();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).update(); 
        }
    } 
    

    /** �������, ����������� ������������ �������� ����� ����� */
    private void collision(Ball ball, Vector <Wall> walls)
    {

    	if (GameObject.intersects(ball, walls, index))
        {
        	//�������� ������������ � ������� � ������ �������� ������
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

          	//�������� ������������ � ������ � ����� �������� ������
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


    /** �������, ����������� ������������ ������ � ������������� �������� */
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
    
    /** �������, ����������� ������������ ������ � ��������������� �������� */
    
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
    
    /** ������� ����������� ������ */
    protected boolean victory(int end_x, int end_y) {
    	boolean ween = false;
    		if ((mPoint.x == end_x)&(mPoint.y == end_y)){
    			ween=true;
    		}
    			
    	return ween;
    }

        
}
