package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Ball extends GameObject
{
    private static final Point DEFAULT_SPEED = new Point(0,0);
    private Point mSpeed;
 
    /**
     *  онструктор дл€ инициализации объекта с начальными координатами и диаметром
     * @input pos - позици€ центра шара
     * @input diam - диаметр шара
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    public Ball(Drawable image, Point pos, int diam)
    {
        super(image);
 
        mPoint = pos;
        this.mHeight = this.mWidth = diam;
        
        mSpeed = DEFAULT_SPEED;    // задали скорость по умолчанию
    }
    
    /**
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    public Ball(Drawable image)
    {
        super(image);
 
        mSpeed = DEFAULT_SPEED;    // задали скорость по умолчанию
    }
 
    /**
     * @see com.android.pingpong.objects.GameObject#updatePoint()
     */
    @Override
    protected void updatePoint()
    {
        mPoint.x += mSpeed.x;
        mPoint.y += mSpeed.y;
    }
    
    /** ќтражение м€чика от вертикали */
    public void reflectVertical()
    {
        mSpeed.y = -mSpeed.y;
    }

    /** ќтражение м€чика от горизонтали */
    public void reflectHorizontal()
    {
    	mSpeed.y = -mSpeed.y;
    }
}