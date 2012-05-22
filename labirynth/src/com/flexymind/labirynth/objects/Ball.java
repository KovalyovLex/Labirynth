package com.flexymind.labirynth.objects;

import com.flexymind.labirynth.screens.settings.ScreenSettings;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Класс Шарик
 * @author Kovalyov Alexander + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final PointF NULL_SPEED = new PointF(0, 0);
	
    /** Скорость шарика */
    private PointF mSpeed;
    
    /**Вращение шарика в лунке */
    private boolean spin = false;
    
    /**Центр лунки */
    private Point   center;
    
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
    
    /** Координаты левого верхнего угла шарика (int очень груб) */
    private PointF mPosition;
    
    /** Координаты левого верхнего угла шарика на следующем шаге */
    private PointF mNextPoint;
    
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
        
        mSpeed = new PointF (NULL_SPEED.x, NULL_SPEED.y);
        
        mPoint = pos;
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
        
        mPosition = new PointF();
        mPosition.x = mPoint.x;
        mPosition.y = mPoint.y;
        
        mNextPoint = new PointF(mPosition.x, mPosition.y);
        mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
        
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
    
	/** изменение размеров объекта */
    public void resize(double ScaleFactorX, double ScaleFactorY)
    {
    	int newX;
    	int newY;
    	
    	mPoint.x = (int)(mPoint.x * ScaleFactorX);
    	mPoint.y = (int)(mPoint.y * ScaleFactorY);
    	
    	mPosition.x = (float)(mPosition.x * ScaleFactorX);
    	mPosition.y = (float)(mPosition.y * ScaleFactorY);
    	
    	mNextPoint.x = (float)(mNextPoint.x * ScaleFactorX);
    	mNextPoint.y = (float)(mNextPoint.y * ScaleFactorY);
    	
    	newX = (int)(ScaleFactorX * mWidth);
    	newY = (int)(ScaleFactorY * mHeight);
    	
    	if (newX > newY){
    		newY = newX;
    	}
    	
    	mWidth = mHeight = newY;
    	
    	Bitmap bmp = ((BitmapDrawable)mImage).getBitmap();
    	Bitmap tmp = Bitmap.createScaledBitmap(bmp, newY, newY, true);
        bmp = tmp;
        mImage = new BitmapDrawable(bmp);
    }
	
	@Override
    /**
     * Функция, определяющая последующее положение шарика
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
	protected void updatePoint()
    {	
		if(spin){
			t += dt;
			if(rigthSpin){
				angle += dt;
			}else{
				angle -= dt;
			}
			if (numberSpin * Math.PI * 2 > Math.abs(angle)){
				mPoint.x = center.x - mWidth / 2 + (int)(diam / 2 / t * Math.cos(angle) );
				mPoint.y = center.y - mHeight / 2 + (int)(diam / 2 / t * Math.sin(angle) );
			}else{
				// шарик в лунке!
			}
		}
		else{
			// Вязкое трение об пол
			mSpeed.x = fricCoef * mSpeed.x;
			mSpeed.y = fricCoef * mSpeed.y;

			//изменение скорости в зависимости от разрешения экрана
			// for asus prime o_0
			mSpeed.x += 0.045 * ScreenSettings.getScaleFactorX() * macelleration[0];
	        mSpeed.y -= 0.045 * ScreenSettings.getScaleFactorY() * macelleration[1];
	        
	        // for other normal devices
	        //mSpeed.x -= 0.045 * ScreenSettings.getScaleFactorX() * macelleration[1];
	        //mSpeed.y -= 0.045 * ScreenSettings.getScaleFactorY() * macelleration[0];
	        
	        //mSpeed.x = (int) (ScreenSettings.ScaleFactorX * (0.005 * (9.81 * Math.cos(tiltAngles[2]))));	//ускорение с сенсора в м/с^2 переводим к ускорению за период 20мс
	        //mSpeed.y = (int) (ScreenSettings.ScaleFactorY * (0.005 * (9.81 * Math.cos(tiltAngles.y))));
	        
	        mPosition.x = mNextPoint.x;
	        mPosition.y = mNextPoint.y;
	        
	        mPoint.x = (int)mPosition.x;
	        mPoint.y = (int)mPosition.y;
	        
	        mNextPoint.x += mSpeed.x;
	        mNextPoint.y += mSpeed.y;
		}
    }
	
	/**
	 * Начало вращения в лунке
	 * @param center - центр лунки
	 */
    public void startSpin(Point center){ 
    	spin  = true;
    	this.center = center;
    	PointF r = new PointF (	mPosition.x + mWidth / 2f - center.x,
    							mPosition.y + mHeight / 2f - center.y);
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
	
    public boolean isSpinning(){
    	return spin;
    }
    
    /**Скалярнорное произведение*/
    private float scalMul(PointF p1, PointF p2){
    	return p1.x * p2.x + p1.y * p2.y;
    }
    
    /** Верхняя граница объекта на следующем шаге */
    public int getNextTop() { return (int)mNextPoint.y; }

    /** Нижняя граница объекта на следующем шаге */
    public int getNextBottom() { return (int)mNextPoint.y + mHeight; }

    /** Левая граница объекта на следующем шаге */
    public int getNextLeft() { return (int)mNextPoint.x; }

    /** Правая граница объекта на следующем шаге */
    public int getNextRight() { return (int)mNextPoint.x + mWidth; }

    /** Центральная точка объекта на следующем шаге */
    public Point getNextCenter() { return new Point((int)mNextPoint.x + mWidth / 2, (int)mNextPoint.y + mHeight / 2); }

    /** Верхняя левая точка объекта на следующем шаге */
    public Point getNextPoint() { return new Point((int)mNextPoint.x, (int)mNextPoint.y); }
	
	public PointF getCenterf(){
		return new PointF(mPosition.x + mWidth / 2f, mPosition.y + mHeight / 2f);
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
	protected void setCenterY(int value){
		super.setCenterY(value);
		mPosition.y = value - mHeight / 2;
	}
	
	@Override
	protected void setCenterX(int value){
		super.setCenterX(value);
		mPosition.x = value - mHeight / 2;
	}

    /**
	 * отражение от стены в направлении v1 (Point2 - Point1)
	 * @param wall стена
	 * @param softness мягкость стены (0..1)
	 * @param newpos новая позиция центра шара
	 */
    public void reflectWallV1(Wall wall, float softness, PointF newpos){
		float project;
		PointF vec1 = new PointF(	wall.getPoint2().x - wall.getPoint1().x,
									wall.getPoint2().y - wall.getPoint1().y);
		
		float length = (float)Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y);
		
		vec1.x /= length;
		vec1.y /= length;
		
		project = vec1.x * mSpeed.x + vec1.y * mSpeed.y;
		mSpeed.x -= (1 + softness) * project * vec1.x;
		mSpeed.y -= (1 + softness) * project * vec1.y;

		project = vec1.x * (newpos.x - mPosition.x - mWidth / 2f) + vec1.y * (newpos.y - mPosition.y - mHeight / 2f);
		mPosition.x = newpos.x - mWidth / 2f - project * vec1.x;
		mPosition.y = newpos.y - mHeight / 2f - project * vec1.y;
		mPoint.x = (int)mPosition.x;
        mPoint.y = (int)mPosition.y;

        mImage.setBounds(mPoint.x, mPoint.y, mPoint.x + mWidth, mPoint.y + mHeight);
        
		mNextPoint.x = mPosition.x + mSpeed.x;
		mNextPoint.y = mPosition.y + mSpeed.y;
	}
    
	/**
	 * отражение от стены в направлении v2 (Point3 - Point2)
	 * @param wall стена
	 * @param softness мягкость стены (0..1)
	 * @param newpos новая позиция центра шара
	 */
	public void reflectWallV2(Wall wall, float softness, PointF newpos){
		float project;
		PointF vec2 = new PointF(	wall.getPoint3().x - wall.getPoint2().x,
									wall.getPoint3().y - wall.getPoint2().y);
		
		float length = (float)Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y);
		
		vec2.x /= length;
		vec2.y /= length;
		
		project = vec2.x * mSpeed.x + vec2.y * mSpeed.y;
		mSpeed.x -= (1 + softness) * project * vec2.x;
		mSpeed.y -= (1 + softness) * project * vec2.y;

		project = vec2.x * (newpos.x - mPosition.x - mWidth / 2f) + vec2.y * (newpos.y - mPosition.y - mHeight / 2f);
		mPosition.x = newpos.x - mWidth / 2f - project * vec2.x;
		mPosition.y = newpos.y - mHeight / 2f - project * vec2.y;
		mPoint.x = (int)mPosition.x;
        mPoint.y = (int)mPosition.y;

        mImage.setBounds(mPoint.x, mPoint.y, mPoint.x + mWidth, mPoint.y + mHeight);
		
		mNextPoint.x = mPosition.x + mSpeed.x;
		mNextPoint.y = mPosition.y + mSpeed.y;
	}
	
    /** Отражение мячика от вертикали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectVertical(Point newPoint)
    {
    	mPoint = newPoint;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.x = -mSpeed.x;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

    /** Отражение мячика от горизонтали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectHorizontal(Point newPoint)
    {
    	mPoint = newPoint;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.y = -mSpeed.y;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

}