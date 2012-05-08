package com.flexymind.labirynth.objects;


import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class GameObject {
	
	// ��������� ��� �����������
    public final static int DIR_LEFT = -1;
    public final static int DIR_RIGHT = 1;
    public final static int DIR_NONE = 0;
    public int index = 0; //����� ������, � ������� ���������� ����������
 
    /** ���������� ������� ����� */
    protected Point mPoint;
 
    /** ������ ����������� */
    protected int mHeight;
 
    /** ������ ����������� */
    protected int mWidth;
 
    /** ����������� */
    protected Drawable mImage;
 
 
    /**
     * �����������
     * @param image �����������, ������� ����� ���������� ������ ������
     */
    public GameObject(Drawable image)
    {
        mImage = image;
        mPoint = new Point(0, 0);
        mWidth = image.getIntrinsicWidth();
        mHeight = image.getIntrinsicHeight();
    }
    /** ����������� ������� ����� */
    protected void updatePoint() {
	}
 
    /** ����������� ������� */
    public void Update()
    {
        updatePoint();
        mImage.setBounds(mPoint.x, mPoint.y, mPoint.x + mWidth, mPoint.y + mHeight);
    }
    
    /** ��������� ������� */
    public void Draw(Canvas canvas)
    {
        mImage.draw(canvas);
    }
    
    
    /** ������ ����� ������� ������� */
    protected void setLeft(int value) { mPoint.x = value; }
 
    /** ������ ������ ������� ������� */
    protected void setRight(int value) { mPoint.x = value - mWidth; }
 
    /** ������ ������� ������� ������� */
    protected void setTop(int value) { mPoint.y = value; }
 
    /** ������ ������ ������� ������� */
    protected void setBottom(int value) { mPoint.y = value - mHeight; }
 
    /** ������ �������� ������ ������� */
    protected void setCenterX(int value) { mPoint.x = value - mHeight / 2; }
 
    /** ������ ����� �������� ������ ������� */
    protected void setCenterY(int value) { mPoint.y = value - mWidth / 2; }
    
    
    
    /** ������� ������� ������� */
    public int getTop() { return mPoint.y; }

    /** ������ ������� ������� */
    public int getBottom() { return mPoint.y + mHeight; }

    /** ����� ������� ������� */
    public int getLeft() { return mPoint.x; }

    /** ������ ������� ������� */
    public int getRight() { return mPoint.x + mWidth; }

    /** ����������� ����� ������� */
    public Point getCenter() { return new Point(mPoint.x + mWidth / 2, mPoint.y + mHeight / 2); }

    /** ������� ����� ����� ������� */
    public Point getPoint() { return mPoint; }
    
    /** ������ ������� */
    public int getHeight() { return mHeight; }

    /** ������ ������� */
    public int getWidth() { return mWidth; }

   
    
    /** @return �������������, �������������� ������ */
    public Rect getRect() { return mImage.getBounds(); }

    /** ���������, ������������ �� ��� ������� ������� */
    public static boolean intersects(GameObject obj1, Vector <Wall> Walls, int index)
    {
    	int Number = Walls.size();
    	boolean strike = false;
    	for (int i = 0;i<Number;i++){
    		strike = Rect.intersects(obj1.getRect(), Walls.elementAt(i).getRect());
    		if(strike){
    			index = i;
    		}
    	}
		return strike; // ����� ��� ������ ����������� ����� �������� (i)
    }

}
