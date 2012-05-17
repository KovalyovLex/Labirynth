﻿package com.flexymind.labirynth.objects;

import java.util.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.flexymind.labirynth.screens.settings.ScreenSettings;


/**
 * Класс 
 * @author Kurnikov Sergey + Kovalyov Alexaner
 * отрисовка всех стенок + соударение от стенок и движение шарика, условие прохождения уровня
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
	private FINISH mfinish;

	/**Эталонные значения, относительно которых происходит масшатабирование*/
	private int rectHeight = 460;
	private int rectWidth = 805;
	private int left = 9;
	private int top  = 9; 
	private boolean dostup  = true;
	private Rect mplayField = new Rect(left,top,rectWidth,rectHeight);

    /**Игровое поле */
	//private Rect mplayField = new Rect(65,30,720,415);        // 480x800 optimization
    //private Rect mplayField = new Rect(105,50,1175,705);		//1280x800 optimization

    Vector <Wall> Walls;

    
    private void AutoSize()
    {
        if (ScreenSettings.AutoScale())
        {
        	this.resize(ScreenSettings.ScaleFactorX(), ScreenSettings.ScaleFactorY());
        }
    }
    
    /**
     * Конструктор 
     * @param Vector <Wall> walls Все стены данного уровня
     * @param finish_X, finish_Y  Финишное положение
     * @param Diam диаметр шарика
     * @param Ball шарик
     */
    public GameLevel(	Vector <Wall> walls,
						Ball ball, FINISH finish,
						Drawable mBackGr){
		//инициализируем параметры, переданные с помощью конструктора
		super(mBackGr);
		mball   = ball;
		mfinish = finish;
		Walls   = walls;
	}
    
    public void resize(double ScaleFactorX, double ScaleFactorY)
    {
    	rectHeight      =  (int)(ScaleFactorX * rectHeight);
        rectWidth       =  (int)(ScaleFactorY * rectWidth);
        left            =  (int)(ScaleFactorX * left);
        top             =  (int)(ScaleFactorY * top);
    }

    @Override
    /** Отрисовка объектов на игровом поле */
    public void Draw(Canvas canvas)
    {	
    	if(dostup)
        {
        	rectUpdate();
        	dostup = false;
        }
    	this.mImage.setBounds(canvas.getClipBounds());
    	this.mImage.draw(canvas);
    	mfinish.Draw(canvas);
    	mball.Draw(canvas);
        for(int i=0;i < Walls.size();i++){
        	Walls.elementAt(i).Draw(canvas);
        }

        // debug отрисовка краев рамки
        Paint mPaint = new Paint();
        mPaint.setColor(Color.MAGENTA);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.STROKE);
        canvas.drawRect(mplayField, mPaint);
    }
    
    @Override
    /** Перемещение объекта */
    public void Update()
    {	
        mball.Update();
        mfinish.Update();
        for(int i = 0; i < Walls.size();i++){
        	Walls.elementAt(i).Update(); 
        }
        collisionsCheck();
    	while (collision_With_Field (mball, mplayField));
    	victory();
    }
    
    public void rectUpdate()
    {
    	AutoSize();
        mplayField.set(left, top, rectWidth, rectHeight);
    }
    
    /** Функция, описывающая столкновения объектов шар и станки между собой */
    private boolean collisionsCheck()
    {
    	float[] p1 = {0,0}, 
    			p2 = {0,0}, 
    			p3 = {0,0}, 
    			p4 = {0,0}, // точки положения углов 4 стенок
    			v1 = {0,0}, 
    			v2 = {0,0}, 
    			v3 = {0,0}, 
    			v4 = {0,0}, // вектора положения 4 стенок
    			speed = {0,0}, // скорость
    			vec1 = {0,0}, 
    			vec2 = {0,0}, 
    			vec3 = {0,0}, 
    			vec4 = {0,0}, // вектора пересечения скорости со стенкой
    			vec_v_1 = {0,0}, 
    			vec_v_2 = {0,0}, 
    			vec_v_3 = {0,0}, 
    			vec_v_4 = {0,0}, // vec_v_i вектор от pi до пересечения c вектором скорости
    			intersectP_V1 = {0,0}, 
    			intersectP_V2 = {0,0}, 
    			intersectP_V3 = {0,0}, 
    			intersectP_V4 = {0,0}; // точки пересечения со стенками
    	
    	Wall 	twall;
    	
    	boolean collision = false;
    	
        for(int i = 0; i < Walls.size();i++){
        	
        	twall = Walls.elementAt(i);
        	
        	p1[0] = twall.getPoint1().x;
        	p1[1] = twall.getPoint1().y;
        	
        	p2[0] = twall.getPoint2().x;
        	p2[1] = twall.getPoint2().y;

        	p3[0] = twall.getPoint3().x;
        	p3[1] = twall.getPoint3().y;
        	
        	p4[0] = p1[0] + p3[0] - p2[0];
        	p4[1] = p1[1] + p3[1] - p2[1];
        	
        	v1[0] = p2[0] - p1[0];
        	v1[1] = p2[1] - p1[1];
        	
        	v2[0] = p3[0] - p2[0];
			v2[1] = p3[1] - p2[1];
        	
			v3[0] = p4[0] - p1[0];
			v3[1] = p4[1] - p1[1];
			
			v4[0] = p3[0] - p4[0];
			v4[1] = p3[1] - p4[1];
			
			p1[0] -=	mball.mWidth / 2 * v3[0] /(int)(Math.sqrt(scalMul(v3,v3))) + 
						mball.mHeight / 2 * v1[0] /(int)(Math.sqrt(scalMul(v1,v1)));
        	p1[1] -= 	mball.mWidth / 2 * v3[1] /(int)(Math.sqrt(scalMul(v3,v3))) + 
						mball.mHeight / 2 * v1[1] /(int)(Math.sqrt(scalMul(v1,v1)));
        	
        	p2[0] +=	mball.mHeight / 2 * v1[0] /(int)(Math.sqrt(scalMul(v1,v1))) -
        				mball.mWidth / 2 * v2[0] /(int)(Math.sqrt(scalMul(v2,v2)));
        	p2[1] +=	mball.mHeight / 2 * v1[1] /(int)(Math.sqrt(scalMul(v1,v1))) -
    					mball.mWidth / 2 * v2[1] /(int)(Math.sqrt(scalMul(v2,v2)));
        	
        	p3[0] +=	mball.mWidth / 2 * v2[0] /(int)(Math.sqrt(scalMul(v2,v2))) + 
						mball.mHeight / 2 * v4[0] /(int)(Math.sqrt(scalMul(v4,v4)));
        	p3[1] +=	mball.mWidth / 2 * v2[1] /(int)(Math.sqrt(scalMul(v2,v2))) + 
						mball.mHeight / 2 * v4[1] /(int)(Math.sqrt(scalMul(v4,v4)));
			
        	p4[0] = p1[0] + p3[0] - p2[0];
        	p4[1] = p1[1] + p3[1] - p2[1];
        	
        	// переропределение векторов
        	v1[0] = p2[0] - p1[0];
        	v1[1] = p2[1] - p1[1];
        	
        	v2[0] = p3[0] - p2[0];
			v2[1] = p3[1] - p2[1];
        	
			v3[0] = p4[0] - p1[0];
			v3[1] = p4[1] - p1[1];
			
			v4[0] = p3[0] - p4[0];
			v4[1] = p3[1] - p4[1];
        	
        	intersectP_V1 = getIntersectionPoint(p1, p2, mball.getCenterf(), mball.getNextCenterf());
        	intersectP_V2 = getIntersectionPoint(p2, p3, mball.getCenterf(), mball.getNextCenterf());
        	intersectP_V3 = getIntersectionPoint(p1, p4, mball.getCenterf(), mball.getNextCenterf());
        	intersectP_V4 = getIntersectionPoint(p4, p3, mball.getCenterf(), mball.getNextCenterf());
        	
        	speed[0] = mball.getNextCenterf()[0] - mball.getCenterf()[0];
        	speed[1] = mball.getNextCenterf()[1] - mball.getCenterf()[1];
        	
        	if (intersectP_V1 != null){
        		vec1[0] = intersectP_V1[0] - mball.getCenterf()[0];
        		vec1[1] = intersectP_V1[1] - mball.getCenterf()[1];
        		vec_v_1[0] = intersectP_V1[0] - p1[0];
        		vec_v_1[1] = intersectP_V1[1] - p1[1];
        	}
        	
        	if (intersectP_V2 != null){
        		vec2[0] = intersectP_V2[0] - mball.getCenterf()[0];
				vec2[1] = intersectP_V2[1] - mball.getCenterf()[1];
        		vec_v_2[0] = intersectP_V2[0] - p2[0];
        		vec_v_2[1] = intersectP_V2[1] - p2[1];
        	}
        	
        	if (intersectP_V3 != null){
        		vec3[0] = intersectP_V3[0] - mball.getCenterf()[0];
				vec3[1] = intersectP_V3[1] - mball.getCenterf()[1];
        		vec_v_3[0] = intersectP_V3[0] - p1[0];
        		vec_v_3[1] = intersectP_V3[1] - p1[1];
        	}
        	
        	if (intersectP_V4 != null){
        		vec4[0] = intersectP_V4[0] - mball.getCenterf()[0];
				vec4[1] = intersectP_V4[1] - mball.getCenterf()[1];
        		vec_v_4[0] = intersectP_V4[0] - p4[0];
        		vec_v_4[1] = intersectP_V4[1] - p4[1];
        	}
        	
        	// проверка удара об левую стенку
        	if (intersectP_V1 != null && check_intersect(vec_v_1, vec1, v1, speed)){
        		// проверка двойных ударов
        		if (intersectP_V2 != null && check_intersect(vec_v_2, vec2, v2, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec2,vec2)){
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(), intersectP_V2);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(), intersectP_V1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectP_V3 != null && check_intersect(vec_v_3, vec3, v3, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec3,vec3)){
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V3);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectP_V4 != null && check_intersect(vec_v_4, vec4, v4, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V1);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об нижнюю стенку
        	if (intersectP_V2 != null && check_intersect(vec_v_2, vec2, v2, speed)){
        		// проверка двойных ударов     		
        		if (intersectP_V3 != null && check_intersect(vec_v_3, vec3, v3, speed)){
        			if (scalMul(vec2,vec2) > scalMul(vec3,vec3)){
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V3);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V2);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectP_V4 != null && check_intersect(vec_v_4, vec4, v4, speed)){
        			if (scalMul(vec2,vec2) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V2);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V2);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об верхнюю стенку
        	if (intersectP_V3 != null && check_intersect(vec_v_3, vec3, v3, speed)){
        		// проверка двойных ударов
        		if (intersectP_V4 != null && check_intersect(vec_v_4, vec4, v4, speed)){
        			if (scalMul(vec3,vec3) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V3);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV1(twall,twall.getSoftness(),intersectP_V3);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об правую стенку
        	if (intersectP_V4 != null && check_intersect(vec_v_4, vec4, v4, speed)){
        		// двойных ударов нет
    			mball.reflectWallV2(twall,twall.getSoftness(),intersectP_V4);
    			collision = true;
    			continue;
        	}
        }
    	return collision;
    }

    private float scalMul(float[] p1, float[] p2){
    	return p1[0] * p2[0] + p1[1] * p2[1];
    }

    /**
     * проверка на соударение с левой стеной
     * @param vec_v_1 - вектор пересечения со стенкой
     * @param vec1 - вектор пересечения скорости со стенкой (от предыдущего положения шара до точки пересечения со стенкой)
     * @param v1 - вектор, задаюший стенку
     * @param speed - вектор скорости (перемещения) шара в данный момент времени
     * @return <code>true</code> если соударение произошло, <code>false</code> во всех остальных случаях
     */
    private boolean check_intersect(float[] vec_v_1, float[] vec1, float[] v1, float[] speed){
    	return ( scalMul(vec_v_1,v1) <= scalMul(v1,v1) 
    		  && scalMul(vec_v_1,v1) >= 0
    		  && scalMul(vec1,speed) <= scalMul(speed,speed)
    		  && scalMul(vec1,speed) >= 0 );
    }
    
    /**
     * возвращает точку пересечения двух прямых проходяших через (p1, p2) и 
     * (cord_p1,cord_p2) 
     * @param p1 - первая точка
     * @param p2 - вторая точка
     * @param cord_p1 - точка пред. состояния
     * @param cord_p2 - точка след. состояния
     * @return <code>float[2] Point</code> - x и y координаты точки пересечения или <code>null</code> если нет такой точки 
     */
    private float[] getIntersectionPoint(float[] p1, float[] p2, float[] cord_p1, float[] cord_p2){
    	float x1 = p1[0], x2 = p2[0], y1 = p1[1], y2 = p2[1];
    	float x3 = cord_p1[0], x4 = cord_p2[0], y3 = cord_p1[1], y4 = cord_p2[1];

    	float d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
    	if (d == 0) {
    		return null;
    	}

    	float xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
    	float yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;

    	return new float[]{ xi, yi };
    }
    
    /** Функция, описывающая столкновения шарика с ограничивающими стенками */
    private boolean collision_With_Field (Ball ball, Rect PlayField){
    	
    	if (ball.getNextLeft() <= PlayField.left)
        {
            ball.reflectVertical(new Point(PlayField.left + 1, ball.getPoint().y));
            return true;
        }
        else if (ball.getNextRight() >= PlayField.right)
        {
        	ball.reflectVertical(new Point(PlayField.right - ball.getWidth() - 1, ball.getPoint().y));
        	return true;
        }
    	
    	if (ball.getNextTop() <= PlayField.top)
	    {
	        ball.reflectHorizontal(new Point(ball.getPoint().x, PlayField.top + 1));
	        return true;
	    }
	    else if (ball.getNextBottom() >= PlayField.bottom)
	    {
	    	ball.reflectHorizontal(new Point(ball.getPoint().x, PlayField.bottom - ball.getHeight() - 1));
	    	return true;
	    }
		return false;
    }
    
    /** условие прохождения уровня */
    protected void victory() {

    	if(GameObject.intersects_finish(mball, mfinish))
    	{
    		if (!mball.isSpinning()){
    			mball.startSpin(mfinish.getCenter(), mfinish.finDiam());
    		}
    	}
    }
}
