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
    private static final Point NULL_SPEED = new Point(0,0);
	
    /**�������� ������ */
    private Point mSpeed = NULL_SPEED;
    
    /**
     * ����������� ��� ������������� ������� � ���������� ������������ � ���������
     * @input pos - ������� ������ ����
     * @input diam - ������� ����
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    public Ball(Drawable image, Point pos, int diam)
    {
        super(image);
        this.mHeight = this.mWidth = diam;
        pos = mPoint;
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