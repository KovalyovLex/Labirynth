package com.flexymind.labirynth.objects;


import com.flexymind.labirynth.storage.Settings;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class GameObject {

    public int index = 0; //номер стенки, с которой происходит соударение
 
    /** Координаты опорной точки */
    protected Point mPoint;
 
    /** Высота изображения */
    protected int mHeight;
 
    /** Ширина изображения */
    protected int mWidth;
 
    /** Изображение */
    protected Drawable mImage;
 
    /** Пременная для autoScale */
    protected boolean needResize = true;

    /**
     * Конструктор
     * @param image Изображение, которое будет обозначать данный объект
     */
    public GameObject(Drawable image)
    {
        mImage = image;
        mPoint = new Point(0, 0);
        refreshSize();
    }
    
    /** Перемещение опорной точки */
    protected void updatePoint() { }
 
    /** изменение размеров объекта */
    public void resize(double ScaleFactorX, double ScaleFactorY)
    {
    	int newX;
    	int newY;
    	
    	mPoint.x = (int)(mPoint.x * ScaleFactorX);
    	mPoint.y = (int)(mPoint.y * ScaleFactorY);
    	
    	refreshSize();
    	newX = (int)(ScaleFactorX * mWidth);
    	newY = (int)(ScaleFactorY * mHeight);
    	Bitmap bmp = ((BitmapDrawable)mImage).getBitmap();
    	Bitmap tmp = Bitmap.createScaledBitmap(bmp, newX, newY, true);
        bmp = tmp;
        mImage = new BitmapDrawable(bmp);
        refreshSize();
    }
    
    public void refreshSize()
    {
    	mWidth = mImage.getBounds().width();
        mHeight = mImage.getBounds().height();
    }
    
    protected void autoSize()
    {
        if (Settings.getAutoScale())
        {
        	this.resize(Settings.getScaleFactorX(), Settings.getScaleFactorY());
        }
    }
    
    /** Перемещение объекта */
    public void onUpdate()
    {
        updatePoint();
        mImage.setBounds(mPoint.x, mPoint.y, mPoint.x + mWidth, mPoint.y + mHeight);
    }
    
    /** Отрисовка объекта */
    public void onDraw(Canvas canvas)
    {
    	if(needResize)
        {
    		autoSize();
        	needResize = false;
        }
        mImage.draw(canvas);
    }
    
    
    /** Задает левую границу объекта */
    protected void setLeft(int value) { mPoint.x = value; }
 
    /** Задает правую границу объекта */
    protected void setRight(int value) { mPoint.x = value - mWidth; }
 
    /** Задает верхнюю границу объекта */
    protected void setTop(int value) { mPoint.y = value; }
 
    /** Задает нижнюю границу объекта */
    protected void setBottom(int value) { mPoint.y = value - mHeight; }
 
    /** Задает абсциссу центра объекта */
    protected void setCenterX(int value) { mPoint.x = value - mHeight / 2; }
 
    /** Задает левую ординату центра объекта */
    protected void setCenterY(int value) { mPoint.y = value - mWidth / 2; }
    
    
    
    /** Верхняя граница объекта */
    public int getTop() { return mPoint.y; }

    /** Нижняя граница объекта */
    public int getBottom() { return mPoint.y + mHeight; }

    /** Левая граница объекта */
    public int getLeft() { return mPoint.x; }

    /** Правая граница объекта */
    public int getRight() { return mPoint.x + mWidth; }

    /** Центральная точка объекта */
    public Point getCenter() { return new Point(mPoint.x + mWidth / 2, mPoint.y + mHeight / 2); }

    /** Верхняя левая точка объекта */
    public Point getPoint() { return mPoint; }
    
    /** Высота объекта */
    public int getHeight() { return mHeight; }

    /** Ширина объекта */
    public int getWidth() { return mWidth; }

   
    
    /** @return Прямоугольник, ограничивающий объект */
    public Rect getRect() { return mImage.getBounds(); }
    
    public static boolean intersectsFinish(GameObject obj1, GameObject obj2)
    {
        return Rect.intersects(obj1.getRect(), obj2.getRect());
    }

}
