package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

/**
 * ����� �����
 * @author Kurnikov Sergey
 *
 */

public class Ball extends GameObject
{
    private static final Point NULL_SPEED = new Point(2, 5);
	
    /**�������� ������ */
    private Point mSpeed;
    
    /** ��������� ������ */
    private float[] macelleration = new float[3];
    
    /** ������ ������� */
    private float[] compassValues = new float[3];
    
    /** ������ ��� ���������� ����� ������� */
    private float[] inR = new float[9];
    
    /** ���� ������� */
    private float[] tiltAngles = new float[3];
    
    /** ������ ��� ������������� */
    //private SensorManager sMan;
    public SensorManagerSimulator sMan;
    
               
    
    /**
     * ����������� ��� ������������� ������� � ���������� ������������ � ���������
     * @input pos - ������� ������ ����
     * @input diam - ������� ����
     * @input sensMan - ������ �������������
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
	public Ball(Drawable image, Point pos, int diam, SensorManagerSimulator sensMan)
    {
        super(image);
        this.sMan = sensMan;
        sMan.connectSimulator();
        		
        sMan.registerListener(new SensorEventListener(){

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}

			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				macelleration = event.values;
			}
			}, sMan.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        
        sMan.registerListener(new SensorEventListener(){

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}

			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
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
			}, sMan.getDefaultSensor(SensorManager.SENSOR_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        
        mSpeed = NULL_SPEED;
        mPoint = pos;
        mPoint.x -= diam / 2;
        mPoint.y -= diam / 2;
        this.mHeight = this.mWidth = diam;
    }
    
	@Override
    /**
     * �������, ������������ ����������� ��������� ������
     * @see com.android.pingpong.objects.GameObject#GameObject(Drawable)
     */
    protected void updatePoint()
    {
		mSpeed.x += 0.1 * macelleration[0];
        mSpeed.y -= 0.1 * macelleration[1];
        
        mPoint.x += mSpeed.x;
        mPoint.y += mSpeed.y;
    }
    
    /**�������, ������������ �������� � �������� ����������
     * � ����������� �� ������� ��������*/
    private Point getSpeed()
    {
		return mSpeed;
    }
	
    /**
	 * ��������� �� ����� � ����������� v1 (Point2 - Point1)
	 * @param wall �����
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
	 * ��������� �� ����� � ����������� v2 (Point3 - Point2)
	 * @param wall �����
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
	
    /** ��������� ������ �� ��������� */
    public void reflectVertical()
    {
        mSpeed.x = -mSpeed.x;
    }

    /** ��������� ������ �� ����������� */
    public void reflectHorizontal()
    {
    	mSpeed.y = -mSpeed.y;
    }

}