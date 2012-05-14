package com.flexymind.labirynth.objects;

import com.flexymind.labirynth.screens.ScreenSettings;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Класс Шарик
 * @author Kurnikov Sergey + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final float[] NULL_SPEED = new float[]{0, 0};
	
    /** Скорость шарика */
    private float[] mSpeed;
    
    /** Коэффициент трения об пол */
    private float fric_coef = 0.95f;
    
    /** Координаты левого верхнего угла шарика (int очень груб) */
    private float[] mPosition;
    
    /** Координаты левого верхнего угла шарика на предыдущем шаге */
    private float[] mPrevPoint;
    
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
        
        mPrevPoint = new float[]{mPosition[0], mPosition[1]};
        
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
        
        mPrevPoint[0] = mPosition[0];
        mPrevPoint[1] = mPosition[1];
        
        mPosition[0] += mSpeed[0];
        mPosition[1] += mSpeed[1];
        
        mPoint.x = (int)mPosition[0];
        mPoint.y = (int)mPosition[1];
    }
	

	public float[] getCenterf(){
		return new float[]{mPosition[0] + mWidth / 2, mPosition[1] + mHeight / 2};
	}
	
    /** Возвращает предыдущее положение центра шара */
    public float[] getPrevCenterf()
    {
    	return new float[]{mPrevPoint[0] + mWidth / 2, mPrevPoint[1] + mHeight / 2};
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
		
		mPosition[0] = newpnt[0] - mWidth / 2;
		mPosition[1] = newpnt[1] - mHeight / 2;
		
		mPoint.x = (int)mPosition[0];
        mPoint.y = (int)mPosition[1];
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
		
		mPosition[0] = newpnt[0] - mWidth / 2;
		mPosition[1] = newpnt[1] - mHeight / 2;
		
		mPoint.x = (int)mPosition[0];
        mPoint.y = (int)mPosition[1];
	}
	
    /** Отражение мячика от вертикали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectVertical(Point newPoint)
    {
    	mPoint = newPoint;
    	mPosition[0] = mPoint.x;
    	mPosition[1] = mPoint.y;
    	
    	mSpeed[0] = -mSpeed[0];
    }

    /** Отражение мячика от горизонтали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectHorizontal(Point newPoint)
    {
    	mPoint = newPoint;
    	mPosition[0] = mPoint.x;
    	mPosition[1] = mPoint.y;
    	
    	mSpeed[1] = -mSpeed[1];
    }

}