package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Класс Шарик
 * @author Kurnikov Sergey
 *
 */

public class Ball extends GameObject
{
    private static final Point NULL_SPEED = new Point(2, 5);
	
    /**Скорость шарика */
    private Point mSpeed;
    
    /** Ускорение шарика */
    private float[] macelleration = new float[3];
    
    /** Данные компаса */
    private float[] compassValues = new float[3];
    
    /** Массив для вычисления углов наклона */
    private float[] inR = new float[9];
    
    /** Углы наклона */
    private float[] tiltAngles = new float[3];
    
    /** Объект для прослушки сенсоров */
    private SensorManager sMan;
    
                 
    
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
        this.sMan = sensMan;
        		
        sMan.registerListener(accelerometerListener, sMan.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sMan.registerListener(compassListener, sMan.getDefaultSensor(SensorManager.SENSOR_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        
        mSpeed = NULL_SPEED;
        mPoint = pos;
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
        this.mHeight = this.mWidth = diam;
    }
	
	/** Прослушка акселерометра */
	final SensorEventListener accelerometerListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			macelleration = event.values;
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/** Прослушка компаса */
	final SensorEventListener compassListener = new SensorEventListener() {
		
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
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void unregisterListeners() {			//Надо этого метода в OnPause() главной активити
		sMan.unregisterListener(accelerometerListener);
		sMan.unregisterListener(compassListener);
	}
	
	
	
    
	@Override
    /**
     * Функция, определяющая последующее положение шарика
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    protected void updatePoint()
    {
		//mSpeed.x += 0.1 * macelleration[0];
        //mSpeed.y -= 0.1 * macelleration[1];
        
		mPoint.x += mSpeed.x*0.02 + (macelleration[0]*0.0004)/2;	//S = v0t + (at2)/2. t = 20мс (период между вызовами UpdateObjects())
		mPoint.y += mSpeed.y*0.02 + (macelleration[1]*0.0004)/2;
		
		mSpeed.x += 0.02 * macelleration[0];	//ускорение с сенсора в м/с^2 переводим к ускорению за период 20мс
        mSpeed.y -= 0.02 * macelleration[1];
        
        //mPoint.x += mSpeed.x;
        //mPoint.y += mSpeed.y;
    }
    
    /**функция, возвращающая скорость с датчиков устройства
     * в зависимости от наклона телефона*/
    private Point getSpeed()
    {
		return mSpeed;
    }
	
    /**
	 * отражение от стены в направлении v1 (Point2 - Point1)
	 * @param wall стена
	 */
	public void reflectWallV1(Wall wall){
		Point vec1;
		int project;
		
		vec1 = new Point (	wall.getPoint2().x - wall.getPoint1().x,
							wall.getPoint2().y - wall.getPoint1().y);
		
		float length = (float)Math.sqrt(vec1.x*vec1.x+vec1.y*vec1.y);
		
		vec1.x /= length;
		vec1.y /= length;
		
		project = vec1.x * mSpeed.x + vec1.y * mSpeed.y;
		mSpeed.x -= 2 * project * vec1.x;
		mSpeed.y -= 2 * project * vec1.y;
		
	}
    
	/**
	 * отражение от стены в направлении v2 (Point3 - Point2)
	 * @param wall стена
	 */
	public void reflectWallV2(Wall wall){
		Point vec2;
		int project;
		
		vec2 = new Point (	wall.getPoint3().x - wall.getPoint2().x,
							wall.getPoint3().y - wall.getPoint2().y);
		
		float length = (float)Math.sqrt(vec2.x*vec2.x+vec2.y*vec2.y);
		
		vec2.x /= length;
		vec2.y /= length;
		
		project = vec2.x * mSpeed.x + vec2.y * mSpeed.y;
		mSpeed.x -= 2 * project * vec2.x;
		mSpeed.y -= 2 * project * vec2.y;
		
	}
	
    /** Отражение мячика от вертикали */
    public void reflectVertical()
    {
        mSpeed.x = -mSpeed.x;
    }

    /** Отражение мячика от горизонтали */
    public void reflectHorizontal()
    {
    	mSpeed.y = -mSpeed.y;
    }

}