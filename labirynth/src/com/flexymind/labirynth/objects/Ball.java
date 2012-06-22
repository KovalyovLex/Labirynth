package com.flexymind.labirynth.objects;

import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.storage.Settings;
import com.flexymind.labirynth.storage.SettingsStorage;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Класс Шарик
 * @author Kovalyov Alexander + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final PointF NULL_SPEED = new PointF(-10 * (float)Settings.getScaleFactorX(),
    													10 * (float)Settings.getScaleFactorY());
	
    /** Скорость шарика */
    private PointF mSpeed;
    
    /**Вращение шарика в лунке */
    private boolean spin = false;
    
    /**Центр лунки */
    private PointF   center;
    
    /**Направление закручавания шарика в лунке, true - увеличивается угол*/
    private boolean rigthSpin = true;
    
    /**шаг по времени с каждым обновлением экрана*/
    private float  dt = 0.1f;
    
    /**Число оборотов шара, перед тем как он попадет в лунку*/
    private float  numberSpin = 2.5f;
    
    /** угол вращения в лунке */
    private float  angle;
    
    /** Диаметр лунки */
    private int     diam;
    
    /** время в формуле вращения */
    private float t = 1;
    
    /** Коэффициент трения об пол */
    private float fricCoef = 0.95f;
    
    private float sensAccel = SettingsStorage.getSensivity();
    
    /** Координаты левого верхнего угла шарика на следующем шаге */
    private PointF mNextPoint;
    
    /** Ускорение шарика */
    private static float[] macelleration = new float[3];
    
    /** Нулевое положение акселерометра */
    private static float[] nullacelleration;
    
    /** Данные компаса */
    private static float[] compassValues = new float[3];
    
    /** Массив для вычисления углов наклона */
    private static float[] inR = new float[9];
    
    /** Углы наклона */
    private static float[] tiltAngles = new float[3];
    
    /** true если приложение запущено на телефоне */
    private static boolean isPhone = true;
    
    
    /** Объект для прослушки сенсоров */
    public static SensorManager sMan = null;
    
    /**
     * Конструктор для инициализации объекта с начальными координатами и диаметром
     * @input pos - позиция центра шара
     * @input diam - диаметр шара
     * @input sensMan - сенсор акселерометра
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
	public Ball(GL10 gl, Drawable image, PointF pos, int diam, SensorManager sensMan)
    {
        super(gl, image);
        
        float min = (Settings.getScaleFactorX() < Settings.getScaleFactorY()) ? (float)Settings.getScaleFactorX() : (float)Settings.getScaleFactorY();
        
        mSquare.setSize(diam * min, diam * min);
        refreshSize();
        mSquare.setLeftTop(mPoint);

        Ball.sMan = sensMan;
        registerListeners();
        
        mSpeed = new PointF (NULL_SPEED.x, NULL_SPEED.y);
        
        mPoint = pos;
        mPoint.x *= Settings.getScaleFactorX();
        mPoint.y *= Settings.getScaleFactorY();
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
                
        mNextPoint = new PointF(mPoint.x, mPoint.y);
        mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
        
        isPhone = Settings.isPhoneDevice();
        
        nullacelleration = SettingsStorage.getAcellPosition();
    }
	
	/** Прослушка акселерометра */
	public static final SensorEventListener accelerometerListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			macelleration = event.values;
			macelleration[0] -= nullacelleration[0];
			macelleration[1] -= nullacelleration[1];
			macelleration[2] -= nullacelleration[2];
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
     */
	protected void updatePoint()
    {	
		if (spin) {
			
			t += dt;
			
			if (rigthSpin) {
				angle += dt;
			} else {
				angle -= dt;
			}
			if (numberSpin * Math.PI * 2 > Math.abs(angle)) {
				mPoint.x = center.x - mWidth / 2 + (int)(diam / 2 / t * Math.cos(angle) );
				mPoint.y = center.y - mHeight / 2 + (int)(diam / 2 / t * Math.sin(angle) );
			} else {
				GameLevel.setIsFinidhed(true);
			}
		}
		else{
			// Вязкое трение об пол
			mSpeed.x *= fricCoef;
			mSpeed.y *= fricCoef;

			//изменение скорости в зависимости от разрешения экрана
			if (isPhone){
				mSpeed.x -= 0.01 * sensAccel * Settings.getScaleFactorX() * macelleration[1];
		        mSpeed.y -= 0.01 * sensAccel * Settings.getScaleFactorY() * macelleration[0];
			}else{
				mSpeed.x += 0.01 * sensAccel * Settings.getScaleFactorX() * macelleration[0];
		        mSpeed.y -= 0.01 * sensAccel * Settings.getScaleFactorY() * macelleration[1];
			}
	        
	        //mSpeed.x = (int) (ScreenSettings.ScaleFactorX * (0.005 * (9.81 * Math.cos(tiltAngles[2]))));	//ускорение с сенсора в м/с^2 переводим к ускорению за период 20мс
	        //mSpeed.y = (int) (ScreenSettings.ScaleFactorY * (0.005 * (9.81 * Math.cos(tiltAngles.y))));
	        
			mPoint.x = mNextPoint.x;
	        mPoint.y = mNextPoint.y;
	        
	        mNextPoint.x += mSpeed.x;
	        mNextPoint.y += mSpeed.y;
		}
    }
	
	/**
	 * Начало вращения в лунке
	 * @param center - центр лунки
	 */
    public void startSpin(PointF center) {
    	spin  = true;
    	
    	this.center = center;
    	PointF r = new PointF (	mPoint.x + mWidth / 2f - center.x,
    							mPoint.y + mHeight / 2f - center.y);
    	float length = (float)Math.sqrt(scalMul(r,r));
    	float lenV = (float)Math.sqrt(scalMul(mSpeed,mSpeed));
    	diam = (int)length;
    	r.x /= length;
    	r.y /= length;
    	
    	angle = (float)Math.acos(r.x);
    	if (r.y < 0){
    		angle = 2 * (float)Math.PI - angle;
    	}
    	
    	rigthSpin = (r.x * mSpeed.y - r.y * mSpeed.x) > 0;
    	dt = lenV / length;
    }
	
    public boolean isSpinning() {
    	return spin;
    }
    
    /**Скалярнорное произведение*/
    private float scalMul(PointF p1, PointF p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }
    
    /** Верхняя граница объекта на следующем шаге */
    public float getNextTop() { return mNextPoint.y; }

    /** Нижняя граница объекта на следующем шаге */
    public float getNextBottom() { return mNextPoint.y + mHeight; }

    /** Левая граница объекта на следующем шаге */
    public float getNextLeft() { return mNextPoint.x; }

    /** Правая граница объекта на следующем шаге */
    public float getNextRight() { return mNextPoint.x + mWidth; }

    /** Центральная точка объекта на следующем шаге */
    public PointF getNextCenter() { return new PointF(mNextPoint.x + mWidth / 2, mNextPoint.y + mHeight / 2); }

    /** Верхняя левая точка объекта на следующем шаге */
    public PointF getNextPoint() { return new PointF(mNextPoint.x, mNextPoint.y); }
	
	public PointF getCenterf(){
		return new PointF(mPoint.x + mWidth / 2f, mPoint.y + mHeight / 2f);
	}
	
    /** Возвращает следующее положение центра шара */
    public PointF getNextCenterf()
    {
    	return new PointF(mNextPoint.x + mWidth / 2f, mNextPoint.y + mHeight / 2f);
    }
    
    public PointF getSpeedCenterf()
    {
    	return new PointF(mSpeed.x,mSpeed.y);
    }
    
    @Override
	protected void setCenterY(float value){
		super.setCenterY(value);
		mPoint.y = value - mHeight / 2;
	}
	
	@Override
	protected void setCenterX(float value){
		super.setCenterX(value);
		mPoint.x = value - mHeight / 2;
	}

    /**
	 * отражение от стены в направлении v1 (Point2 - Point1)
	 * @param wall стена
	 * @param softness мягкость стены (0..1)
	 * @param newpos новая позиция центра шара
	 */
    public void reflectWallV1(Wall wall, float softness, PointF newpos){
    	Log.v("reflect V1","ball");
    	
		float project;
		PointF vec1 = new PointF(	wall.getPoint2().x - wall.getPoint1().x,
									wall.getPoint2().y - wall.getPoint1().y);
		
		float length = (float)Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y);
		
		vec1.x /= length;
		vec1.y /= length;
		
		project = vec1.x * mSpeed.x + vec1.y * mSpeed.y;
		mSpeed.x -= (1 + softness) * project * vec1.x;
		mSpeed.y -= (1 + softness) * project * vec1.y;

		project = vec1.x * (newpos.x - mPoint.x - mWidth / 2f) + vec1.y * (newpos.y - mPoint.y - mHeight / 2f);
		mPoint.x = newpos.x - mWidth / 2f - project * vec1.x;
		mPoint.y = newpos.y - mHeight / 2f - project * vec1.y;

        mSquare.setLeftTop(mPoint);
        
		mNextPoint.x = mPoint.x + mSpeed.x;
		mNextPoint.y = mPoint.y + mSpeed.y;
		
		wall.showWall();
	}
    
	/**
	 * отражение от стены в направлении v2 (Point3 - Point2)
	 * @param wall стена
	 * @param softness мягкость стены (0..1)
	 * @param newpos новая позиция центра шара
	 */
	public void reflectWallV2(Wall wall, float softness, PointF newpos){
		Log.v("reflect V2","ball");
		
		float project;
		PointF vec2 = new PointF(	wall.getPoint3().x - wall.getPoint2().x,
									wall.getPoint3().y - wall.getPoint2().y);
		
		float length = (float)Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y);
		
		vec2.x /= length;
		vec2.y /= length;
		
		project = vec2.x * mSpeed.x + vec2.y * mSpeed.y;
		mSpeed.x -= (1 + softness) * project * vec2.x;
		mSpeed.y -= (1 + softness) * project * vec2.y;

		project = vec2.x * (newpos.x - mPoint.x - mWidth / 2f) + vec2.y * (newpos.y - mPoint.y - mHeight / 2f);
		mPoint.x = newpos.x - mWidth / 2f - project * vec2.x;
		mPoint.y = newpos.y - mHeight / 2f - project * vec2.y;

        mSquare.setLeftTop(mPoint);
		
		mNextPoint.x = mPoint.x + mSpeed.x;
		mNextPoint.y = mPoint.y + mSpeed.y;
		
		wall.showWall();
	}
	
    /** Отражение мячика от вертикали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectVertical(PointF newPoint)
    {
    	Log.v("reflect Vertical","ball");
    	
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.x = -mSpeed.x;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

    /** Отражение мячика от горизонтали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectHorizontal(PointF newPoint)
    {
    	Log.v("reflect Horizontal","ball");
    	
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.y = -mSpeed.y;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

}