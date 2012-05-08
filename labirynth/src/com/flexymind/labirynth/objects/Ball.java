package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * ����� �����
 * @author Kurnikov Sergey
 *
 */

public class Ball extends GameObject
{
    private static final Point NULL_SPEED = new Point(2,-5);
	
    /**�������� ������ */
    private Point mSpeed;
    
    /**
     * ����������� ��� ������������� ������� � ���������� ������������ � ���������
     * @input pos - ������� ������ ����
     * @input diam - ������� ����
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    public Ball(Drawable image, Point pos, int diam)
    {
        super(image);
        mSpeed = NULL_SPEED;
        mPoint = pos;
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
        this.mHeight = this.mWidth = diam;
    }
    
	@Override
    /**
     * �������, ������������ ����������� ��������� ������
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    protected void updatePoint()
    {	
        mPoint.x += mSpeed.x;
        mPoint.y -= mSpeed.y;
    }
    
    /**�������, ������������ �������� � �������� ����������
     * � ����������� �� ������� ��������*/
    private Point getSpeed()
    {
		return mSpeed;
    }
	
    /**
	 * ��������� �� ����� � ����������� vec1 (Point2 - Point1)
	 * @param wall �����
	 */
	public void reflectWallVec1(Wall wall){
		Point vec1;
		int project;
		
		vec1 = new Point (	wall.getPoint2().x - wall.getPoint1().x,
							wall.getPoint2().y - wall.getPoint1().y);
		
		float length = (float)Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y);
		
		vec1.x /= length;
		vec1.y /= length;
		
		project = vec1.x * mSpeed.x + vec1.y * mSpeed.y;
		mSpeed.x -= 2 * project * vec1.x;
		mSpeed.y -= 2 * project * vec1.y;
		
	}
    
	/**
	 * ��������� �� ����� � ����������� vec2 (Point3 - Point2)
	 * @param wall �����
	 */
	public void reflectWallVec2(Wall wall){
		Point vec2;
		int project;
		
		vec2 = new Point (	wall.getPoint3().x - wall.getPoint2().x,
							wall.getPoint3().y - wall.getPoint2().y);
		
		float length = (float)Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y);
		
		vec2.x /= length;
		vec2.y /= length;
		
		project = vec2.x * mSpeed.x + vec2.y * mSpeed.y;
		mSpeed.x -= 2 * project * vec2.x;
		mSpeed.y -= 2 * project * vec2.y;
		
	}
	
    /** ��������� ������ �� ��������� */
    public void reflectVertical()
    {
        mSpeed.x = -mSpeed.x;
    }

    /** ��������� ������ �� ����������� */
    public void reflectHorizontal()
    {
    	mSpeed.y = -mSpeed.y;
    }

}