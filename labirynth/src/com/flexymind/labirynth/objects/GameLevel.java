package com.flexymind.labirynth.objects;

import java.util.Vector;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.flexymind.labirynth.storage.Settings;


/**
 * Класс 
 * @author Kurnikov Sergey + Kovalyov Alexaner
 * отрисовка всех стенок + соударение от стенок и движение шарика, условие прохождения уровня
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
	private FINISH mfinish;

	/**Эталонные значения, относительно которых происходит масшатабирование*/
	private int left = 9;
	private int top  = 9;
	private Rect mplayField = null;

	private final int preShowFrame = 60; // вначле игры уровень показывается 60 кадров
	private boolean hidewall = true;
	private int frameShowed = 0;
	
    /**Игровое поле */
	//private Rect mplayField = new Rect(65,30,720,415);        // 480x800 optimization
    //private Rect mplayField = new Rect(105,50,1175,705);		//1280x800 optimization

    Vector <Wall> Walls;
    
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
		mplayField = new Rect(left, top, Settings.getCurrentXRes()-left, Settings.getCurrentYRes()-top);
		
		mball.onUpdate();
		mfinish.onUpdate();
	}
    
    @Override
    public void resize(double ScaleFactorX, double ScaleFactorY)
    {
    	super.resize(ScaleFactorX, ScaleFactorY);
        left            =  (int)(ScaleFactorX * left);
        top             =  (int)(ScaleFactorY * top);
        mplayField = new Rect(left, top, Settings.getCurrentXRes()-left, Settings.getCurrentYRes()-top);
    }

    @Override
    /** Отрисовка объектов на игровом поле */
    public void onDraw(Canvas canvas)
    {
    	if(needResize)
        {
    		mImage.setBounds(canvas.getClipBounds());
    		autoSize();
    		mImage.setBounds(canvas.getClipBounds());
        	needResize = false;
        }
    	mImage.draw(canvas);
    	mfinish.onDraw(canvas);
    	mball.onDraw(canvas);
        for(int i=0;i < Walls.size();i++){
        	Walls.elementAt(i).onDraw(canvas);
        }

        // debug отрисовка краев рамки
        //Paint mPaint = new Paint();
        //mPaint.setColor(Color.MAGENTA);
        //mPaint.setStrokeWidth(2);
        //mPaint.setStyle(Style.STROKE);
        //canvas.drawRect(mplayField, mPaint);
    }
    
    @Override
    /** Перемещение объекта */
    public void onUpdate()
    {
    	if (frameShowed > preShowFrame){
    		mball.onUpdate();
    		mfinish.onUpdate();
    		if (hidewall){
    			hidewall = false;
    			for(int i = 0; i < Walls.size();i++){
        			Walls.elementAt(i).hideWall();
        		}
    		}
    		for(int i = 0; i < Walls.size();i++){
    			Walls.elementAt(i).onUpdate();
    		}
    		int i = 0;
    		while (collisionWithField(mball, mplayField) | collisionsCheck() & i < 5){
    			i++;
    		}
    		victory();
    	}else{
    		frameShowed++;
    	}
    }
    
    /** Функция, описывающая столкновения объектов шар и станки между собой */
    private boolean collisionsCheck()
    {
    	PointF  p1 = new PointF(),
    			p2 = new PointF(),
    			p3 = new PointF(),
    			p4 = new PointF(), // точки положения углов 4 стенок
    			v1 = new PointF(),
    			v2 = new PointF(),
    			v3 = new PointF(),
    			v4 = new PointF(), // вектора положения 4 стенок
    			speed = new PointF(), // скорость
    			vec1 = new PointF(),
    			vec2 = new PointF(),
    			vec3 = new PointF(),
    			vec4 = new PointF(), // вектора пересечения скорости со стенкой
    			vecV1 = new PointF(),
    			vecV2 = new PointF(),
    			vecV3 = new PointF(),
    			vecV4 = new PointF(), // vec_v_i вектор от pi до пересечения c вектором скорости
    			intersectPV1 = new PointF(), 
    			intersectPV2 = new PointF(),
    			intersectPV3 = new PointF(),
    			intersectPV4 = new PointF(); // точки пересечения со стенками
    	
    	Wall 	twall;
    	
    	boolean collision = false;
    	
        for(int i = 0; i < Walls.size();i++){
        	
        	twall = Walls.elementAt(i);
        	
        	p1.x = twall.getPoint1().x;
        	p1.y = twall.getPoint1().y;
        	
        	p2.x = twall.getPoint2().x;
        	p2.y = twall.getPoint2().y;

        	p3.x = twall.getPoint3().x;
        	p3.y = twall.getPoint3().y;
        	
        	p4.x = p1.x + p3.x - p2.x;
        	p4.y = p1.y + p3.y - p2.y;
        	
        	v1.x = p2.x - p1.x;
        	v1.y = p2.y - p1.y;
        	
        	v2.x = p3.x - p2.x;
			v2.y = p3.y - p2.y;
        	
			v3.x = p4.x - p1.x;
			v3.y = p4.y - p1.y;
			
			v4.x = p3.x - p4.x;
			v4.y = p3.y - p4.y;
			
			p1.x -=	mball.mWidth / 2f * v3.x /(int)(Math.sqrt(scalMul(v3,v3))) + 
						mball.mHeight / 2f * v1.x /(int)(Math.sqrt(scalMul(v1,v1)));
        	p1.y -= 	mball.mWidth / 2f * v3.y /(int)(Math.sqrt(scalMul(v3,v3))) + 
						mball.mHeight / 2f * v1.y /(int)(Math.sqrt(scalMul(v1,v1)));
        	
        	p2.x +=	mball.mHeight / 2f * v1.x /(int)(Math.sqrt(scalMul(v1,v1))) -
        				mball.mWidth / 2f * v2.x /(int)(Math.sqrt(scalMul(v2,v2)));
        	p2.y +=	mball.mHeight / 2f * v1.y /(int)(Math.sqrt(scalMul(v1,v1))) -
    					mball.mWidth / 2f * v2.y /(int)(Math.sqrt(scalMul(v2,v2)));
        	
        	p3.x +=	mball.mWidth / 2f * v2.x /(int)(Math.sqrt(scalMul(v2,v2))) + 
						mball.mHeight / 2f * v4.x /(int)(Math.sqrt(scalMul(v4,v4)));
        	p3.y +=	mball.mWidth / 2f * v2.y /(int)(Math.sqrt(scalMul(v2,v2))) + 
						mball.mHeight / 2f * v4.y /(int)(Math.sqrt(scalMul(v4,v4)));
			
        	p4.x = p1.x + p3.x - p2.x;
        	p4.y = p1.y + p3.y - p2.y;
        	
        	// переропределение векторов
        	v1.x = p2.x - p1.x;
        	v1.y = p2.y - p1.y;
        	
        	v2.x = p3.x - p2.x;
			v2.y = p3.y - p2.y;
        	
			v3.x = p4.x - p1.x;
			v3.y = p4.y - p1.y;
			
			v4.x = p3.x - p4.x;
			v4.y = p3.y - p4.y;
        	
        	intersectPV1 = getIntersectionPoint(p1, p2, mball.getCenterf(), mball.getNextCenterf());
        	intersectPV2 = getIntersectionPoint(p2, p3, mball.getCenterf(), mball.getNextCenterf());
        	intersectPV3 = getIntersectionPoint(p1, p4, mball.getCenterf(), mball.getNextCenterf());
        	intersectPV4 = getIntersectionPoint(p4, p3, mball.getCenterf(), mball.getNextCenterf());
        	
        	speed.x = mball.getNextCenterf().x - mball.getCenterf().x;
        	speed.y = mball.getNextCenterf().y - mball.getCenterf().y;
        	
        	if (intersectPV1 != null){
        		vec1.x = intersectPV1.x - mball.getCenterf().x;
        		vec1.y = intersectPV1.y - mball.getCenterf().y;
        		vecV1.x = intersectPV1.x - p1.x;
        		vecV1.y = intersectPV1.y - p1.y;
        	}
        	
        	if (intersectPV2 != null){
        		vec2.x = intersectPV2.x - mball.getCenterf().x;
				vec2.y = intersectPV2.y - mball.getCenterf().y;
        		vecV2.x = intersectPV2.x - p2.x;
        		vecV2.y = intersectPV2.y - p2.y;
        	}
        	
        	if (intersectPV3 != null){
        		vec3.x = intersectPV3.x - mball.getCenterf().x;
				vec3.y = intersectPV3.y - mball.getCenterf().y;
        		vecV3.x = intersectPV3.x - p1.x;
        		vecV3.y = intersectPV3.y - p1.y;
        	}
        	
        	if (intersectPV4 != null){
        		vec4.x = intersectPV4.x - mball.getCenterf().x;
				vec4.y = intersectPV4.y - mball.getCenterf().y;
        		vecV4.x = intersectPV4.x - p4.x;
        		vecV4.y = intersectPV4.y - p4.y;
        	}
        	
        	// проверка удара об левую стенку
        	if (intersectPV1 != null && checkIntersect(vecV1, vec1, v1, speed)){
        		// проверка двойных ударов
        		if (intersectPV2 != null && checkIntersect(vecV2, vec2, v2, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec2,vec2)){
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(), intersectPV2);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(), intersectPV1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectPV3 != null && checkIntersect(vecV3, vec3, v3, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec3,vec3)){
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV3);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectPV4 != null && checkIntersect(vecV4, vec4, v4, speed)){
        			if (scalMul(vec1,vec1) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v1
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV1);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV1);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об нижнюю стенку
        	if (intersectPV2 != null && checkIntersect(vecV2, vec2, v2, speed)){
        		// проверка двойных ударов     		
        		if (intersectPV3 != null && checkIntersect(vecV3, vec3, v3, speed)){
        			if (scalMul(vec2,vec2) > scalMul(vec3,vec3)){
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV3);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV2);
            			collision = true;
            			continue;
        			}
        		}
        		
        		if (intersectPV4 != null && checkIntersect(vecV4, vec4, v4, speed)){
        			if (scalMul(vec2,vec2) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v2
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV2);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV2);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об верхнюю стенку
        	if (intersectPV3 != null && checkIntersect(vecV3, vec3, v3, speed)){
        		// проверка двойных ударов
        		if (intersectPV4 != null && checkIntersect(vecV4, vec4, v4, speed)){
        			if (scalMul(vec3,vec3) > scalMul(vec4,vec4)){
        				// удар об стенку v4
            			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV4);
            			collision = true;
            			continue;
        			}else{
        				// удар об стенку v3
            			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV3);
            			collision = true;
            			continue;
        			}
        		}
        		
        		// двойных ударов нет
    			mball.reflectWallV1(twall,twall.getSoftness(),intersectPV3);
    			collision = true;
    			continue;
        	}
        	
        	// проверка удара об правую стенку
        	if (intersectPV4 != null && checkIntersect(vecV4, vec4, v4, speed)){
        		// двойных ударов нет
    			mball.reflectWallV2(twall,twall.getSoftness(),intersectPV4);
    			collision = true;
    			continue;
        	}
        }
    	return collision;
    }

    private float scalMul(PointF p1, PointF p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }
    
    private float scalMul(Point p1, Point p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }

    /**
     * проверка на соударение с левой стеной
     * @param vec_v_1 - вектор пересечения со стенкой
     * @param vec1 - вектор пересечения скорости со стенкой (от предыдущего положения шара до точки пересечения со стенкой)
     * @param v1 - вектор, задаюший стенку
     * @param speed - вектор скорости (перемещения) шара в данный момент времени
     * @return <code>true</code> если соударение произошло, <code>false</code> во всех остальных случаях
     */
    private boolean checkIntersect(PointF vec_v_1, PointF vec1, PointF v1, PointF speed){
    	return ( scalMul(vec_v_1,v1) < scalMul(v1,v1) 
    		  && scalMul(vec_v_1,v1) > 0
    		  && scalMul(vec1,speed) < scalMul(speed,speed)
    		  && scalMul(vec1,speed) > 0 );
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
    private PointF getIntersectionPoint(PointF p1, PointF p2, PointF cord_p1, PointF cord_p2){
    	float	x1 = p1.x,
    			x2 = p2.x,
    			y1 = p1.y,
    			y2 = p2.y,
    			x3 = cord_p1.x,
    			x4 = cord_p2.x,
    			y3 = cord_p1.y,
    			y4 = cord_p2.y;
    	
    	float d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
    	
    	if (d == 0) {
    		return null;
    	}

    	float xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
    	float yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;

    	return new PointF( xi, yi );
    }
    
    /** Функция, описывающая столкновения шарика с ограничивающими стенками */
    private boolean collisionWithField (Ball ball, Rect PlayField){
    	
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
    	Point r = new Point();
    	r.x = mfinish.getCenter().x - mball.getCenter().x;
    	r.y = mfinish.getCenter().y - mball.getCenter().y;
    	
    	if(scalMul(r,r) <= Math.pow(mfinish.finDiam() / 2f + mball.getHeight() / 2f, 2))
    	{
    		if (!mball.isSpinning()){
    			mball.startSpin(mfinish.getCenter());
    		}
    	}
    }
}
