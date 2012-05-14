package com.flexymind.labirynth.objects;

import com.flexymind.labirynth.screens.ScreenSettings;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * ����� �����
 * @author Kurnikov Sergey + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final float[] NULL_SPEED = new float[]{5, 8};
	
    /** �������� ������ */
    private float[] mSpeed;
    
    /** ����������� ������ �� ��� */
    private float fric_coef = 0.95f;
    
    /** ���������� ������ �������� ���� ������ (int ����� ����) */
    private float[] mPosition;
    
    /** ���������� ������ �������� ���� ������ �� ���������� ���� */
    private float[] mPrevPoint;
    
    /** ��������� ������ */
    private static float[] macelleration = new float[3];
    
    /** ������ ������� */
    private static float[] compassValues = new float[3];
    
    /** ������ ��� ���������� ����� ������� */
    private static float[] inR = new float[9];
    
    /** ���� ������� */
    private static float[] tiltAngles = new float[3];
    
    /** ������ ��� ��������� �������� */
    public static SensorManager sMan;
    
    /**
     * ����������� ��� ������������� ������� � ���������� ������������ � ���������
     * @input pos - ������� ������ ����
     * @input diam - ������� ����
     * @input sensMan - ������ �������������
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
	
	/** ��������� ������������� */
	public static final SensorEventListener accelerometerListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			macelleration = event.values;
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	};
	
	/** ��������� ������� */
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
	
	public static void registerListeners() {			//���� ����� ������ � OnResume() ������� ��������

        sMan.registerListener(accelerometerListener, sMan.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sMan.registerListener(compassListener, sMan.getDefaultSensor(SensorManager.SENSOR_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        
	}
	
	public static void unregisterListeners() {			//���� ����� ������ � OnPause() ������� ��������
		sMan.unregisterListener(accelerometerListener);
		sMan.unregisterListener(compassListener);
	}
    
	@Override
    /**
     * �������, ������������ ����������� ��������� ������
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    protected void updatePoint()
    {
		// ������ ������ �� ���
		mSpeed[0] = fric_coef * mSpeed[0];
		mSpeed[1] = fric_coef * mSpeed[1];
		
		//mSpeed[0] += 0.05 * ScreenSettings.ScaleFactorX * macelleration[1];	//��������� �������� � ����������� �� ���������� ������
        //mSpeed[1] += 0.05 * ScreenSettings.ScaleFactorY * macelleration[0];
		
		// for asus prime o_0
		mSpeed[0] += 0.045 * ScreenSettings.ScaleFactorX * macelleration[0];
        mSpeed[1] -= 0.045 * ScreenSettings.ScaleFactorX * macelleration[1];
        
        // for other normal devices
        //mSpeed[0] -= 0.045 * ScreenSettings.ScaleFactorX * macelleration[1];
        //mSpeed[1] -= 0.045 * ScreenSettings.ScaleFactorX * macelleration[0];
        
        //mSpeed.x = (int) (ScreenSettings.ScaleFactorX * (0.005 * (9.81 * Math.cos(tiltAngles[2]))));	//��������� � ������� � �/�^2 ��������� � ��������� �� ������ 20��
        //mSpeed.y = (int) (ScreenSettings.ScaleFactorY * (0.005 * (9.81 * Math.cos(tiltAngles[1]))));
        
        mPrevPoint[0] = mPosition[0];
        mPrevPoint[1] = mPosition[1];
        
        mPosition[0] += mSpeed[0];
        mPosition[1] += mSpeed[1];
        
        mPoint.x = (int)mPosition[0];
        mPoint.y = (int)mPosition[1];
    }
	
    /** ���������� ���������� ��������� ������ ����*/
    public float[] getPrevCenter()
    {
    	return new float[]{mPrevPoint[0] + mWidth / 2, mPrevPoint[1] + mHeight / 2};
    }
    
    /**
	 * ��������� �� ����� � ����������� v1 (Point2 - Point1)
	 * @param wall �����
	 * @param new_pos ����� ���������� �� ��� V1
	 */
	public void reflectWallV1(Wall wall){
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
	}
    
	/**
	 * ��������� �� ����� � ����������� v2 (Point3 - Point2)
	 * @param wall �����
	 * @param new_pos ����� ���������� �� ��� V2
	 */
	public void reflectWallV2(Wall wall){
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
	}
	
    /** ��������� ������ �� ��������� 
     * @param newPoint ����� ���� ����� ����������
     * */
    public void reflectVertical(Point newPoint)
    {
    	mPoint = newPoint;
    	mPosition[0] = mPoint.x;
    	mPosition[1] = mPoint.y;
    	
    	mSpeed[0] = -mSpeed[0];
    }

    /** ��������� ������ �� ����������� 
     * @param newPoint ����� ���� ����� ����������
     * */
    public void reflectHorizontal(Point newPoint)
    {
    	mPoint = newPoint;
    	mPosition[0] = mPoint.x;
    	mPosition[1] = mPoint.y;
    	
    	mSpeed[1] = -mSpeed[1];
    }

}