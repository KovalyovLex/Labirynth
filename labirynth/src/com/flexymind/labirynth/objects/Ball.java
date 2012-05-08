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
    private static final Point NULL_SPEED = new Point(0,0);
	
    /**—корость шарика */
    private Point mSpeed = NULL_SPEED;
    
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
        mPoint.x += mSpeed.x;
        mPoint.y -= mSpeed.y;
    }
    
    /**функци€, возвращающа€ скорость с датчиков устройства
     * в зависимости от наклона телефона*/
    private Point getSpeed()
    {
		return mSpeed;
    }
	
	
    /** ќтражение м€чика от вертикали */
    public void reflectVertical()
    {
        mSpeed.x = -mSpeed.x;
    }

    /** ќтражение м€чика от горизонтали */
    public void reflectHorizontal()
    {
    	mSpeed.y = -mSpeed.y;
    }

}