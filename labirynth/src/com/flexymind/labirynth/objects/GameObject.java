package com.flexymind.labirynth.objects;


import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;

public abstract class GameObject {

    public int index = 0; //номер стенки, с которой происходит соударение
 
    /** Координаты опорной точки */
    protected PointF mPoint;
 
    /** Высота изображения */
    protected float mHeight;
 
    /** Ширина изображения */
    protected float mWidth;
 
    /** Изображение */
    protected Square mSquare;

    /**
     * Конструктор
     * @param image Изображение, которое будет обозначать данный объект
     * @param gl OpenGL обьект для рисования
     */
    public GameObject(GL10 gl, Drawable image)
    {
    	mSquare = new Square();
    	mSquare.loadGLTexture(gl, image);
        mPoint = new PointF(0, 0);
        refreshSize();
    }
    
    /**
     * Конструктор построение квадрата по 3 точкам 
	 * @param p1 - left up point
	 * @param p2 - left bottom point
	 * @param p3 - right bottom point
     * @param image Изображение, которое будет обозначать данный объект
     * @param gl OpenGL обьект для рисования
     */
    public GameObject(PointF p1, PointF p2, PointF p3, GL10 gl, Drawable image)
    {
    	mSquare = new Square(p1, p2, p3);
    	mSquare.loadGLTexture(gl, image);
        mPoint = new PointF(0, 0);
        refreshSize();
    }
    
    /** Перемещение опорной точки */
    protected void updatePoint() { }
    
    protected void refreshSize()
    {
    	mWidth = mSquare.getWidth();
        mHeight = mSquare.getHeight();
    }
    
    /** Перемещение объекта */
    public void onUpdate()
    {
        updatePoint();
        mSquare.setLeftTop(mPoint);
    }
    
    /** Отрисовка объекта */
    public void onDraw(GL10 gl)
    {
    	mSquare.draw(gl);
    }
    
    /** Задает левую границу объекта */
    protected void setLeft(float value) { mPoint.x = value; }
 
    /** Задает правую границу объекта */
    protected void setRight(float value) { mPoint.x = value - mWidth; }
 
    /** Задает верхнюю границу объекта */
    protected void setTop(float value) { mPoint.y = value; }
 
    /** Задает нижнюю границу объекта */
    protected void setBottom(float value) { mPoint.y = value - mHeight; }
 
    /** Задает абсциссу центра объекта */
    protected void setCenterX(float value) { mPoint.x = value - mHeight / 2; }
 
    /** Задает левую ординату центра объекта */
    protected void setCenterY(float value) { mPoint.y = value - mWidth / 2; }
    
    
    /** Верхняя граница объекта */
    public float getTop() { return mPoint.y; }

    /** Нижняя граница объекта */
    public float getBottom() { return mPoint.y + mHeight; }

    /** Левая граница объекта */
    public float getLeft() { return mPoint.x; }

    /** Правая граница объекта */
    public float getRight() { return mPoint.x + mWidth; }

    /** Центральная точка объекта */
    public PointF getCenter() { return new PointF(mPoint.x + mWidth / 2, mPoint.y + mHeight / 2); }

    /** Верхняя левая точка объекта */
    public PointF getPoint() { return mPoint; }
    
    /** Высота объекта */
    public float getHeight() { return mHeight; }

    /** Ширина объекта */
    public float getWidth() { return mWidth; }
    
    public Square getSquare(){
    	return mSquare;
    }
    
    protected void onDestroy()
    {
    	mSquare.onDestroy();
    }
    
}
