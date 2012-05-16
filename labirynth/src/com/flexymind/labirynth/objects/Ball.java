package com.flexymind.labirynth.objects;

import com.flexymind.labirynth.screens.settings.ScreenSettings;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Класс Шарик
 * @author Kurnikov Sergey + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final float[] NULL_SPEED = new float[]{16, 5};
	
    /** Скорость шарика */
    private float[] mSpeed;
    
    /**Вращение шарика в лунке */
    private boolean spin = false;
    
    /**Центр лунки */
    private Point   center;
    /**Направление закручавания шарика в лунке, true - увеличивается угол*/
    private boolean directionSpin = true;
    private double  dt = 0.1;
    private double  numberSpin = 4;
    private double  angle;
    private int     diam;
    
    /** Коэффициент трения об пол */
    private float fric_coef = 0.95f;
    
    /** Координаты левого верхнего угла шарика (int очень груб) */
    private float[] mPosition;
    
    /** Координаты левого верхнего угла шарика на следующем шаге */
    private float[] mNextPoint;
    
    /** Ускорение шарика */
    private static float[] macelleration = new float[3];
    
    /** Данные компаса */
    private static float[] compassValues = new float[3];
    
    /** Массив для вычисления углов наклона */
    private static float[] inR = new float[9];
    
    /** Углы наклона */
    private static float[] tiltAngles = new float[3];
    
    /** Объект для прослушки сенсоров */
    public static SensorManager sMan = null;
    
    /**
     * Конструктор для инициализации объекта с начальными координатами и диаметром
     * @input pos - позиция центра шара
     * @input diam - диаметр шара
     * @input sensMan - сенсор акселерометра
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
	public Ball(Drawable image, Point pos, int diam, SensorManager sensMan)
    {
        super(image);
        Ball.sMan = sensMan;
        
        registerListeners();
        
        mSpeed = new float[] {NULL_SPEED[0], NULL_SPEED[1]};
        
        mPoint = pos;
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
        
        mPosition = new float[2];
        mPosition[0] = mPoint.x;
        mPosition[1] = mPoint.y;
        
        mNextPoint = new float[]{mPosition[0], mPosition[1]};
        mNextPoint[0] += mSpeed[0];
        mNextPoint[1] += mSpeed[1];
        
        this.mHeight = this.mWidth = diam;
    }
	
	/** Прослушка акселерометра */
	public static final SensorEventListener accelerometerListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			macelleration = event.values;
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	};
	
	/** Прослушка компаса */
	public static final SensorEventListener compassListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			compassValues = event.values;
			if (SensorManager.getRotationMatrix(inR, null, macelleration, compassValues)) {
				SensorManager.getOrientation(inR, tiltAngles);
			}
			
			for (int i=0; i<3; i++) {
				tiltAngles[i] = (float) Math.toDegrees(tiltAngles[i]);
				if(tiltAngles[i] < 0) {
					tiltAngles[i] += 360.0f;
				}
			}
			
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	};
	
	public static void registerListeners() {			//Надо этого метода в OnResume() главной активити
		if (sMan != null){
			sMan.registerListener(accelerometerListener, sMan.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
			//sMan.registerListener(compassListener, sMan.getDefaultSensor(SensorManager.SENSOR_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	public static void unregisterListeners() {			//Надо этого метода в OnPause() главной активити
		if (sMan != null){
			sMan.unregisterListener(accelerometerListener);
			//sMan.unregisterListener(compassListener);
		}
	}
    
	@Override
    /**
     * Функция, определяющая последующее положение шарика
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
	protected void updatePoint()
    {	
		if(spin){
			if(directionSpin){
				angle += dt;
			}else{
				angle -= dt;
			}
			if (numberSpin * Math.PI * 2 > Math.abs(angle)){
				//Log.v("Angle","OK");
				mPoint.x = center.x - mWidth / 2 + (int)(diam / 2 / angle * Math.cos(angle) );
				mPoint.y = center.y - mHeight / 2 + (int)(diam / 2 / angle * Math.sin(angle) );
			}else{
				// шарик в лунке!
			}
		}
		else{
			// Вязкое трение об пол
			mSpeed[0] = fric_coef * mSpeed[0];
			mSpeed[1] = fric_coef * mSpeed[1];

			//изменение скорости в зависимости от разрешения экрана
			// for asus prime o_0
			mSpeed[0] += 0.045 * ScreenSettings.ScaleFactorX() * macelleration[0];
	        mSpeed[1] -= 0.045 * ScreenSettings.ScaleFactorY() * macelleration[1];
	        
	        // for other normal devices
	        //mSpeed[0] -= 0.045 * ScreenSettings.ScaleFactorX * macelleration[1];
	        //mSpeed[1] -= 0.045 * ScreenSettings.ScaleFactorX * macelleration[0];
	        
	        //mSpeed.x = (int) (ScreenSettings.ScaleFactorX * (0.005 * (9.81 * Math.cos(tiltAngles[2]))));	//ускорение с сенсора в м/с^2 переводим к ускорению за период 20мс
	        //mSpeed.y = (int) (ScreenSettings.ScaleFactorY * (0.005 * (9.81 * Math.cos(tiltAngles[1]))));
	        
	        mPosition[0] = mNextPoint[0];
	        mPosition[1] = mNextPoint[1];
	        
	        mPoint.x = (int)mPosition[0];
	        mPoint.y = (int)mPosition[1];
	        
	        mNextPoint[0] += mSpeed[0];
	        mNextPoint[1] += mSpeed[1];
		}
    }
	
	/**
	 * Начало вращения в лунке
	 * @param center - центр лунки
	 * @param diam - диаметр лунки
	 */
    public void startSpin(Point center, int diam){ 
    	spin  = true;
    	this.diam = diam;
    	Point vecBalToCent = new Point(	center.x - this.getCenter().x,
    									center.y - this.getCenter().y);
    	//if (vecBalToCent)
    	angle = 1;
    	this.center = center;
    }
	
    public boolean isSpinning(){
    	return spin;
    }
    
    /**Скалярнорное произведение*/
    private float scalMul(Point p1, Point p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }
    
    /** Верхняя граница объекта на следующем шаге */
    public int getNextTop() { return (int)mNextPoint[1]; }

    /** Нижняя граница объекта на следующем шаге */
    public int getNextBottom() { return (int)mNextPoint[1] + mHeight; }

    /** Левая граница объекта на следующем шаге */
    public int getNextLeft() { return (int)mNextPoint[0]; }

    /** Правая граница объекта на следующем шаге */
    public int getNextRight() { return (int)mNextPoint[0] + mWidth; }

    /** Центральная точка объекта на следующем шаге */
    public Point getNextCenter() { return new Point((int)mNextPoint[0] + mWidth / 2, (int)mNextPoint[1] + mHeight / 2); }

    /** Верхняя левая точка объекта на следующем шаге */
    public Point getNextPoint() { return new Point((int)mNextPoint[0], (int)mNextPoint[1]); }
	
	public float[] getCenterf(){
		return new float[]{mPosition[0] + mWidth / 2f, mPosition[1] + mHeight / 2f};
	}
	
    /** Возвращает следующее положение центра шара */
    public float[] getNextCenterf()
    {
    	return new float[]{mNextPoint[0] + mWidth / 2f, mNextPoint[1] + mHeight / 2f};
    }
    
    public float[] getSpeedCenterf()
    {
    	return new float[]{mSpeed[0],mSpeed[1]};
    }
    
    @Override
	protected void setCenterY(int value){
		super.setCenterY(value);
		mPosition[1] = value - mHeight / 2;
	}
	
	@Override
	protected void setCenterX(int value){
		super.setCenterX(value);
		mPosition[0] = value - mHeight / 2;
	}

    /**
	 * отражение от стены в направлении v1 (Point2 - Point1)
	 * @param wall стена
	 * @param new_pos новая координата по оси V1
	 */
    public void reflectWallV1(Wall wall, float[] newpnt){
		Point vec1;
		float project;
		
		vec1 = new Point (	wall.getPoint2().x - wall.getPoint1().x,
							wall.getPoint2().y - wall.getPoint1().y);
		
		float length = (float)Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y);
		
		vec1.x /= length;
		vec1.y /= length;
		
		project = vec1.x * mSpeed[0] + vec1.y * mSpeed[1];
		mSpeed[0] -= 2 * project * vec1.x;
		mSpeed[1] -= 2 * project * vec1.y;
		
		mNextPoint[0] = newpnt[0] - mWidth / 2f;
		mNextPoint[1] = newpnt[1] - mHeight / 2f;
	}
    
	/**
	 * отражение от стены в направлении v2 (Point3 - Point2)
	 * @param wall стена
	 * @param new_pos новая координата по оси V2
	 */
	public void reflectWallV2(Wall wall, float[] newpnt){
		Point vec2;
		float project;
		
		vec2 = new Point (	wall.getPoint3().x - wall.getPoint2().x,
							wall.getPoint3().y - wall.getPoint2().y);
		
		float length = (float)Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y);
		
		vec2.x /= length;
		vec2.y /= length;
		
		project = vec2.x * mSpeed[0] + vec2.y * mSpeed[1];
		mSpeed[0] -= 2 * project * vec2.x;
		mSpeed[1] -= 2 * project * vec2.y;
		
		mNextPoint[0] = newpnt[0] - mWidth / 2f;
		mNextPoint[1] = newpnt[1] - mHeight / 2f;
	}
	
    /** Отражение мячика от вертикали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectVertical(Point newPoint)
    {
    	mPoint = newPoint;
    	mNextPoint[0] = newPoint.x;
    	mNextPoint[1] = newPoint.y;
    	
    	mSpeed[0] = -mSpeed[0];
    	
    	mNextPoint[0] += mSpeed[0];
        mNextPoint[1] += mSpeed[1];
    }

    /** Отражение мячика от горизонтали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectHorizontal(Point newPoint)
    {
    	mPoint = newPoint;
    	mNextPoint[0] = newPoint.x;
    	mNextPoint[1] = newPoint.y;
    	
    	mSpeed[1] = -mSpeed[1];
    	
    	mNextPoint[0] += mSpeed[0];
        mNextPoint[1] += mSpeed[1];
    }

}