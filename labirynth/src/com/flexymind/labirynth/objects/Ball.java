package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 *  ласс Ўарик
 * @author Kurnikov Sergey
 *
 */

public class Ball extends GameObject
{
    private static final int PI = 180;
    
    /** ”гол, который составл€ет направление полета шарика с осью Ox */
    private int mAngle;
    /**—корость шарика */
    private int mSpeed;
    
    /**
     *  онструктор дл€ инициализации объекта с начальными координатами и диаметром
     * @input pos - позици€ центра шара
     * @input diam - диаметр шара
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
     * ‘ункци€, определ€юща€ последующее положение шарика
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    protected void updatePoint()
    {
        double angle = Math.toRadians(mAngle);
        
        mPoint.x += (int)Math.round(mSpeed * Math.cos(angle));
        mPoint.y -= (int)Math.round(mSpeed * Math.sin(angle));
    }
    
	
	
    /**функци€, возвращающа€ угол наклона с датчиков устройства*/
    private int getAngle()
    {
		return mAngle;
    }
    
    /**функци€, возвращающа€ скорость с датчиков устройства
     * в зависимости от наклона телефона*/
    private int getSpeed()
    {
		return mSpeed;
    }
	
	
    /** ќтражение м€чика от вертикали */
    public void reflectVertical()
    {
        if (mAngle > 0 && mAngle < PI)
            mAngle = PI - mAngle;
        else
            mAngle = 3 * PI - mAngle;
    }

    /** ќтражение м€чика от горизонтали */
    public void reflectHorizontal()
    {
        mAngle = 2 * PI - mAngle;
    }

}