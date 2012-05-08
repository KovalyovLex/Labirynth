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
    private static final int PI = 180;
    
    /** ����, ������� ���������� ����������� ������ ������ � ���� Ox */
    private int mAngle;
    /**�������� ������ */
    private int mSpeed;
    
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
        double angle = Math.toRadians(mAngle);
        
        mPoint.x += (int)Math.round(mSpeed * Math.cos(angle));
        mPoint.y -= (int)Math.round(mSpeed * Math.sin(angle));
    }
    
	
	
    /**�������, ������������ ���� ������� � �������� ����������*/
    private int getAngle()
    {
		return mAngle;
    }
    
    /**�������, ������������ �������� � �������� ����������
     * � ����������� �� ������� ��������*/
    private int getSpeed()
    {
		return mSpeed;
    }
	
	
    /** ��������� ������ �� ��������� */
    public void reflectVertical()
    {
        if (mAngle > 0 && mAngle < PI)
            mAngle = PI - mAngle;
        else
            mAngle = 3 * PI - mAngle;
    }

    /** ��������� ������ �� ����������� */
    public void reflectHorizontal()
    {
        mAngle = 2 * PI - mAngle;
    }

}