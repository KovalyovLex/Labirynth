package com.flexymind.labirynth.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.flexymind.labirynth.FXs.Vibration;
import com.flexymind.labirynth.screens.start.StartScreen;
import com.flexymind.labirynth.storage.Settings;


/**
 * Класс 
 * @author Kurnikov Sergey + Kovalyov Alexaner
 * отрисовка всех стенок + соударение от стенок и движение шарика, условие прохождения уровня
 */
public class GameLevel extends GameObject{
 
	private Ball mball;
	private FINISH mfinish;
	
	private static boolean isFinished;
	private boolean pause;
	
	/**Эталонные значения, относительно которых происходит масшатабирование*/
	private int left = 17;
	private int top  = 17;
	private Rect mplayField = null;

	private final long preShowFrame = 60; // вначле игры уровень показывается 60 кадров (1 секунда)
	private boolean hidewall = true;
	private long frameShowed = 0; // переменная для отсчёта кадров
	private long startTime = 0; // время начала раунда в секундах
	
	private float scorePF = 1.5f; // понижение очков за кадр (90 очков в секунду)
	private float score = 10000; // Стартовое значение очков
	private float scorePWall = 100; // понижение очков за удар об стену
	private float scoreFor1Star = score * 0.5f;
	private float scoreFor2Star = score * 0.7f;
	private float scoreFor3Star = score * 0.85f;
	
	private Paint mPaintScore = new Paint(); // Для отрисовки очков
	private Vector <Wall> walls;
    private Vibration mVibration = null;
	
    /**
     * Конструктор 
     * @param Vector <Wall> walls Все стены данного уровня
     * @param finish_X, finish_Y  Финишное положение
     * @param Diam диаметр шарика
     * @param Ball шарик
     */
    public GameLevel(	Vector <Wall> walls,
						Ball ball, FINISH finish,
						GL10 gl,
						Drawable mBackGr){
		//инициализируем параметры, переданные с помощью конструктора
		super(gl, mBackGr);
		
		pause = false;
		
		mSquare.setSize(Settings.getCurrentYRes(), Settings.getCurrentXRes());
		refreshSize();
		
		isFinished = false;
		
		top *= Settings.getScaleFactorY();
		left *= Settings.getScaleFactorX();
		
        mplayField = new Rect(left, top, Settings.getCurrentXRes() - left, Settings.getCurrentYRes() - top);
        
		mball   = ball;
		mfinish = finish;
		this.walls   = walls;
		
		mball.onUpdate();
		mfinish.onUpdate();
		
		mball.mutePlaySound();
		
		mPaintScore = new Paint();
        mPaintScore.setAntiAlias(true);
        mPaintScore.setTextSize((int)(20 * Settings.getScaleFactorY()));
        mPaintScore.setColor(Color.WHITE);
        mPaintScore.setTextAlign(Paint.Align.CENTER);
        
        mVibration = new Vibration(StartScreen.startActivity);
	}
    
    /**
     * Задаёт значения для очков уровня
     * @param firstScore - значение очков в начальный момент времени
     * @param score1S - количество очков для 1 звезды
     * @param score2S - количество очков для 2 звезд
     * @param score3S - количество очков для 3 звезд
     */
    public void setScoreSystem(float firstScore, float score1S, float score2S, float score3S){
    	score = firstScore;
    	scoreFor1Star = score1S;
    	scoreFor2Star = score2S;
    	scoreFor3Star = score3S;
    }
    
    public int getNumOfStars(){
    	int i = 0;
    	float[] scores = {scoreFor1Star, scoreFor2Star, scoreFor3Star};
    	for (; i < 3; i++){
    		if (score < scores[i]){
    			break;
    		}
    	}
    	return i;
    }
    
    public boolean getIsFinished() {
    	return isFinished;
    }
    
    protected static void setIsFinidhed(boolean isFin) {
    	isFinished = isFin;
    }
    
    /** Set & unset pause */
    public void onPause() {
    	pause = !pause;
    }
    
    public float getScore() {
    	return score;
    }

    @Override
    /** Отрисовка объектов на игровом поле */
    public void onDraw(GL10 gl)
    {
    	// Point to our buffers
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
    	mSquare.draw(gl);
    	mfinish.onDraw(gl);
    	mball.onDraw(gl);
        for(int i=0;i < walls.size();i++){
        	walls.elementAt(i).onDraw(gl);
        }

		// Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        
        /*
        canvas.drawText(Integer.toString((int)score),
        				(float)(Settings.getCurrentXRes()) / 2,
        				(float) (30 * Settings.getScaleFactorY()),
        				mPaintScore);
        */
    }
    
    @Override
    /** Перемещение объекта */
    public void onUpdate()
    {
    	if (!pause && frameShowed > preShowFrame){
    		if(!mball.isSpinning()) {
    			score -= scorePF;
    		}
    		mball.onUpdate();
    		mfinish.onUpdate();
    		if (hidewall){
    			hidewall = false;
    			for(int i = 0; i < walls.size();i++){
        			walls.elementAt(i).hideWall();
        		}
    			Calendar calendar = Calendar.getInstance();
    			startTime = calendar.getTimeInMillis() / 1000;
    		}
    		for(int i = 0; i < walls.size(); i++){
    			walls.elementAt(i).onUpdate();
    		}
    		
    		if (collisionsCheck() | collisionWithField(mball, mplayField)){
    			// вычитаем очки за удар об стену или ограничивающие стенки
    			score -= scorePWall;
    			// вибро сигнал
    			mVibration.singleVibration();
    		}
    		victory();
    	}else{
    		frameShowed++;
    	}
    	if (score < 0){
    		score = 0;
    	}
    }
    
    /**
     * возвращает время в секундах с момента старта игры
     * @return time
     */
    protected long getPlayTime(){
    	Calendar calendar = Calendar.getInstance();
    	return calendar.getTimeInMillis() / 1000 - startTime;
    }
    
    /** Функция, описывающая столкновения объектов шар и стенки между собой */
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
    			vecV4 = new PointF(), // vec_v_i вектор от pi стены до точки пересечения вектора стены Vi c вектором скорости
    			intersectPV1 = new PointF(), 
    			intersectPV2 = new PointF(),
    			intersectPV3 = new PointF(),
    			intersectPV4 = new PointF(); // точки пересечения со стенками
    	
    	Wall 	twall;
    	
    	ArrayList<IntersectVectorContainer> vectcont = new ArrayList<IntersectVectorContainer>();
    	
    	boolean collision = false;
    	
        for(int i = 0; i < walls.size();i++){
        	
        	vectcont.clear();
        	
        	twall = walls.elementAt(i);
        	
        	p1.x = twall.getPoint1().x - mball.mWidth / 2f;
        	p1.y = twall.getPoint1().y - mball.mHeight / 2f;
        	
        	p2.x = p1.x;
        	p2.y = twall.getPoint2().y + mball.mHeight / 2f;

        	p3.x = twall.getPoint2().x + mball.mWidth / 2f;
        	p3.y = p2.y;
        	
        	p4.x = p3.x;
        	p4.y = p1.y;

        	v1.x = p2.x - p1.x;
        	v1.y = p2.y - p1.y;
        	
        	v2.x = p3.x - p2.x;
			v2.y = p3.y - p2.y;
        	
			v3.x = p4.x - p1.x;
			v3.y = p4.y - p1.y;

			v4.x = p3.x - p4.x;
			v4.y = p3.y - p4.y;
        	
        	intersectPV1 = getIntersectionPoint(p1, p2, mball.getCenter(), mball.getNextCenter());
        	intersectPV2 = getIntersectionPoint(p2, p3, mball.getCenter(), mball.getNextCenter());
        	intersectPV3 = getIntersectionPoint(p1, p4, mball.getCenter(), mball.getNextCenter());
        	intersectPV4 = getIntersectionPoint(p4, p3, mball.getCenter(), mball.getNextCenter());
        	
        	speed.x = mball.getNextCenter().x - mball.getCenter().x;
        	speed.y = mball.getNextCenter().y - mball.getCenter().y;
        	
        	if (intersectPV1 != null){
        		vec1.x = intersectPV1.x - mball.getCenter().x;
        		vec1.y = intersectPV1.y - mball.getCenter().y;
        		vecV1.x = intersectPV1.x - p1.x;
        		vecV1.y = intersectPV1.y - p1.y;
        		vectcont.add(new IntersectVectorContainer(	vec1, 
        				new PointF(intersectPV1.x - mball.mWidth / 2f, intersectPV1.y - mball.mHeight / 2f),
        				vecV1,
        				v1,
        				false));
        	}
        	
        	if (intersectPV2 != null){
        		vec2.x = intersectPV2.x - mball.getCenter().x;
				vec2.y = intersectPV2.y - mball.getCenter().y;
        		vecV2.x = intersectPV2.x - p2.x;
        		vecV2.y = intersectPV2.y - p2.y;
        		vectcont.add(new IntersectVectorContainer(	vec2, 
        				new PointF(intersectPV2.x - mball.mWidth / 2f, intersectPV2.y - mball.mHeight / 2f) ,
        				vecV2,
        				v2,
        				true));
        	}
        	
        	if (intersectPV3 != null){
        		vec3.x = intersectPV3.x - mball.getCenter().x;
				vec3.y = intersectPV3.y - mball.getCenter().y;
        		vecV3.x = intersectPV3.x - p1.x;
        		vecV3.y = intersectPV3.y - p1.y;
        		vectcont.add(new IntersectVectorContainer(	vec3, 
        				new PointF(intersectPV3.x - mball.mWidth / 2f, intersectPV3.y - mball.mHeight / 2f) ,
        				vecV3,
        				v3,
        				true));
        	}
        	
        	if (intersectPV4 != null){
        		vec4.x = intersectPV4.x - mball.getCenter().x;
				vec4.y = intersectPV4.y - mball.getCenter().y;
        		vecV4.x = intersectPV4.x - p4.x;
        		vecV4.y = intersectPV4.y - p4.y;
        		vectcont.add(new IntersectVectorContainer(	vec4, 
        				new PointF(intersectPV4.x - mball.mWidth / 2f, intersectPV4.y - mball.mHeight / 2f) ,
        				vecV4,
        				v4,
        				false));
        	}
        	
        	// узнаём где находится следующая точка
        	if (mball.getNextCenter().y >= p1.y &&
        		mball.getNextCenter().y <= p3.y &&
        		mball.getNextCenter().x <= p3.x &&
        		mball.getNextCenter().x >= p1.x){
        		// Next Point inside the wall, hit from wall
        		
        		if (vectcont.size() > 0){
        			IntersectVectorContainer minLen;
        		
        			minLen = vectcont.get(0);
        			// находим вектор минимальной длинны
        			for (int n = 1; n < vectcont.size(); n++){
        				if (minLen.getPowerLength() > vectcont.get(n).getPowerLength()){
        					minLen = vectcont.get(n);
        				}
        			}
        			
        			if (minLen.isHorizontReflection()){
        				mball.reflectHorizontal(twall, minLen.getNextPosVector());
        			}else{
        				mball.reflectVertical(twall, minLen.getNextPosVector());
        			}
        			
        			collision = true;
        		}
        	}else{
        		// Next Point not inside the wall, check for fast overflight
        		
        		if (vectcont.size() > 0){
        			IntersectVectorContainer minLen;
        		
        			minLen = vectcont.get(0);
        			// находим вектор минимальной длинны
        			for (int n = 1; n < vectcont.size(); n++){
        				if (minLen.getPowerLength() > vectcont.get(n).getPowerLength()){
        					minLen = vectcont.get(n);
        				}
        			}

        			// проверка что точка пересечения находится на отрезке соединяющем настоящую и следующую точки траектории
        			if (checkIntersect(minLen.getVectV(), minLen.getVector(), minLen.getV(), speed)){
        				
        				if (minLen.isHorizontReflection()){
        					mball.reflectHorizontal(twall, minLen.getNextPosVector());
        				}else{
        					mball.reflectVertical(twall, minLen.getNextPosVector());
        				}
        				collision = true;
        			}
        		}
        	}
        }
    	return collision;
    }

    private class IntersectVectorContainer{
    	private boolean horiz;
    	private PointF vect;
    	private PointF vecV;
    	private PointF nextvect;
    	private PointF v;
    	
    	/**
    	 * Конструктор контейнера
    	 * @param vec - вектор от положения шара до положения стены.
    	 * @param vecV - вектор от точки p стены до точки пересечения вектора стены c вектором скорости.
    	 * @param nextPosVec - вектор от положения шара до положения стены.
    	 * @param v - вектор направления стены.
    	 * @param horizont - <code>true</code> в случае горизонтальной стены.
    	 */
    	public IntersectVectorContainer(PointF vec,
    									PointF nextPosVec,
    									PointF vecV,
    									PointF v,
    									boolean horizont){
    		vect = vec;
    		this.vecV = vecV;
    		horiz = horizont;
    		nextvect = nextPosVec;
    		this.v = v;
    	}
    	
    	/** горизонтальная стена */
    	public boolean isHorizontReflection(){
    		return horiz;
    	}
    	
    	public float getPowerLength(){
    		return vect.x * vect.x + vect.y * vect.y;
    	}
    	
    	/** вектор от положения шара до положения стены */
    	public PointF getVector(){
    		return vect;
    	}
    	
    	/** вектор от положения шара до положения стены */
    	public PointF getNextPosVector(){
    		return nextvect;
    	}
    	
    	/** вектор от точки p стены до точки пересечения вектора стены c вектором скорости */
    	public PointF getVectV(){
    		return vecV;
    	}
    	
    	/** вектор направления стены */
    	public PointF getV(){
    		return v;
    	}
    }
    
    /**
     * проверка на соударение со стеной
     * @param vec_v_1 - вектор пересечения со стенкой
     * @param vec1 - вектор пересечения скорости со стенкой (от предыдущего положения шара до точки пересечения со стенкой)
     * @param v1 - вектор, задаюший стенку
     * @param speed - вектор скорости (перемещения) шара в данный момент времени
     * @return <code>true</code> если соударение произошло, <code>false</code> во всех остальных случаях
     */
    private boolean checkIntersect(PointF vec_v_1, PointF vec1, PointF v1, PointF speed){
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
     * @return <code>PointF Point</code> - x и y координаты точки пересечения или <code>null</code> если нет такой точки 
     */
    private PointF getIntersectionPoint(PointF p1, PointF p2, PointF cord_p1, PointF cord_p2){
    	double	x1 = p1.x,
    			x2 = p2.x,
    			y1 = p1.y,
    			y2 = p2.y,
    			x3 = cord_p1.x,
    			x4 = cord_p2.x,
    			y3 = cord_p1.y,
    			y4 = cord_p2.y;
    	
    	double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
    	
    	if (d == 0) {
    		return null;
    	}

    	double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
    	double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;

    	return new PointF( (float)xi, (float)yi );
    }
    
    private float scalMul(PointF p1, PointF p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }
    
    /** Функция, описывающая столкновения шарика с ограничивающими стенками */
    private boolean collisionWithField (Ball ball, Rect PlayField){
    	
    	if (ball.getNextLeft() <= PlayField.left)
        {
            ball.reflectVertical(new PointF(PlayField.left + 1, ball.getPoint().y));
            return true;
        }
        else if (ball.getNextRight() >= PlayField.right)
        {
        	ball.reflectVertical(new PointF(PlayField.right - ball.getWidth() - 1, ball.getPoint().y));
        	return true;
        }
    	
    	if (ball.getNextTop() <= PlayField.top)
	    {
	        ball.reflectHorizontal(new PointF(ball.getPoint().x, PlayField.top + 1));
	        return true;
	    }
	    else if (ball.getNextBottom() >= PlayField.bottom)
	    {
	    	ball.reflectHorizontal(new PointF(ball.getPoint().x, PlayField.bottom - ball.getHeight() - 1));
	    	return true;
	    }
		return false;
    }
    
    /** условие прохождения уровня */
    protected void victory() {
    	PointF r = new PointF();
    	r.x = mfinish.getCenter().x - mball.getCenter().x;
    	r.y = mfinish.getCenter().y - mball.getCenter().y;
    	
    	if(scalMul(r,r) <= Math.pow(mfinish.finDiam() / 2f + mball.getHeight() / 2f, 2))
    	{
    		if (!mball.isSpinning()){
    			mball.startSpin(mfinish.getCenter());
    		}
    	}
    }
    
    protected void onDestroy()
    {
    	super.onDestroy();
    	mfinish.onDestroy();
    	mball.onDestroy();
        for(int i=0;i < walls.size();i++){
        	walls.elementAt(i).onDestroy();
        } 
    }
    
    public void destoyLevel(){
    	//Log.v("GameLevel","destroyed");
    	//Thread.dumpStack();
    	onDestroy();
    	Square.resetCount();
    	isFinished = false;
    }
}
