package com.flexymind.labirynth.objects;

import java.util.Vector;


import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * ����� 
 * @author Kurnikov Sergey + Kovalyov Alexaner
 * ��������� ���� ������ + ���������� �� ������ � �������� ������, ������� ����������� ������
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
	private FINISH mfinish;
    /**������� ���� */
	private Rect mplayField = new Rect(95,70,700, 425);
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
    public GameLevel(	Vector <Wall> walls,
						Ball ball, FINISH finish,
						int finish_X,
						int finish_Y,
						int finish_Diam,
						Drawable mBackGr){
		//�������������� ���������, ���������� � ������� ������������
		super(mBackGr);
        end_x   = finish_X;
		end_y   = finish_Y;
		diam    = finish_Diam;
		mball   = ball;
		mfinish = finish;
		Walls   = walls;
		Number  = Walls.size();
	}

    @Override
    /** ��������� �������� �� ������� ���� */
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
    /** ����������� ������� */
    public void Update()
    {	
    	collisionsCheck();
    	collision_With_Field (mball, mplayField);
    	victory(end_x,end_y);
        mball.Update();
        mfinish.Update();
        for(int i=0;i<Number;i++){
        	Walls.elementAt(i).Update(); 
        }
    }
    
    /** �������, ����������� ������������ �������� ��� � ������ ����� ����� */
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
        	// �������� sum �� v2
        	vec2 = new Point(	sum.x - scal_mul(sum,v1) * v1.x / scal_mul(v1,v1),
        						sum.y - scal_mul(sum,v1) * v1.y / scal_mul(v1,v1));
        	// �������� sum �� v1
        	vec1 = new Point(	sum.x - vec2.x,
        						sum.y - vec2.y);
        	
        	if (	scal_mul(vec1,v1) >= 0 
        		 && scal_mul(vec1,vec1) <= scal_mul(v1,v1)
        		 && scal_mul(vec2,v2) >= 0
        		 && scal_mul(vec2,vec2) <= scal_mul(v2,v2)){
        		// ����
        		if (((float)scal_mul(vec1,vec1) / scal_mul(v1,v1)) > ((float)scal_mul(vec2,vec2) / scal_mul(v2,v2)) ){
        			// ���� � ����������� v2
            		mball.reflectWallV2(twall);	
        		}else{
        			// ���� � ����������� v1
            		mball.reflectWallV1(twall);
        		}
        	}
        }
    	
    }

    private int scal_mul(Point p1, Point p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }

    /** �������, ����������� ������������ ������ � ��������������� �������� */
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
    
      	
    
    
    /** ������� ����������� ������ */
    protected void victory(int end_x, int end_y) {
    	Point bCenter = mball.getCenter();
    		
    	if(GameObject.intersects_finish(mball, mfinish))
    	{

			bCenter.y = end_y + diam / 2+(int) Math.sqrt(Math.abs(Math.pow(diam, 2)-Math.pow((bCenter.x-end_x - diam / 2), 2)));
			mball.setCenterY(bCenter.y);
			mball.setCenterX(bCenter.x);
    		
    	}
    	/*if (	 (bCenter.x >= end_x - diam / 2)
    			  && (bCenter.x <= end_x + diam / 2)
    			  && (bCenter.y >= end_y - diam / 2)
    			  && (bCenter.y <= end_y + diam / 2) ){
    			  bCenter.y =end_y + diam / 2+(int) Math.sqrt(Math.abs(Math.pow(diam, 2)-Math.pow((bCenter.x-end_x - diam / 2), 2)));
			      mball.setCenterY(bCenter.y);
    			  }*/
    }
}
