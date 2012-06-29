package com.flexymind.labirynth.objects;

import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.FXs.SoundEngine;
import com.flexymind.labirynth.screens.start.StartScreen;
import com.flexymind.labirynth.storage.Settings;
import com.flexymind.labirynth.storage.SettingsStorage;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

/**
 * Класс Шарик
 * @author Kovalyov Alexander + Soloviev Vyacheslav
 *
 */

public class Ball extends GameObject
{
    private static final PointF NULL_SPEED = new PointF(10 * (float)Settings.getScaleFactorX(),
    													10 * (float)Settings.getScaleFactorY());
	// максимальное значение скорости, для которого громкость 1.
    private static final PointF MAX_SPEED = new PointF(	7 * (float)Settings.getScaleFactorX(),
														7 * (float)Settings.getScaleFactorY());
    
    /** Скорость шарика */
    private PointF	mSpeed;
    
    /**Вращение шарика в лунке */
    private boolean	spin = false;
    
    /**Центр лунки */
    private PointF	center;
    
    /**Направление закручавания шарика в лунке, true - увеличивается угол*/
    private boolean	rigthSpin = true;
    
    /**шаг по времени с каждым обновлением экрана*/
    private float	dt = 0.1f;
    
    /**Число оборотов шара, перед тем как он попадет в лунку*/
    private float	numberSpin = 2.5f;
    
    /** угол вращения в лунке */
    private float	angle = 0;
    
    /** Диаметр лунки */
    private int     diam = 0;
    
    /** время в формуле вращения */
    private float	t = 1;
    
    /** Коэффициент трения об пол */
    private float	fricCoef = 0.95f;
    
    private float	sensAccel = SettingsStorage.getSensivity();
    
    /** Координаты левого верхнего угла шарика на следующем шаге */
    private PointF	mNextPoint;
    
    /** устройство для воспроизведения звука качения */
	private SoundEngine mSoundEngine = null;
	
	/** значение громкости качения шара */
	private float	soundVolume = 0;
	
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
        
        mSoundEngine = new SoundEngine(StartScreen.startActivity);
        mSoundEngine.addSound(1, R.raw.roll_stone_wood);
        mSoundEngine.playLoopedSound(1);
        soundVolume = 0;
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
				mPoint.x = center.x - mWidth / 2 + (int)(diam / 2 / t * FloatMath.cos(angle) );
				mPoint.y = center.y - mHeight / 2 + (int)(diam / 2 / t * FloatMath.sin(angle) );
			} else {
				GameLevel.setIsFinidhed(true);
				soundVolume = 0;
			}
		}
		else{
			// Вязкое трение об пол
			mSpeed.x *= fricCoef;
			mSpeed.y *= fricCoef;

			//изменение скорости в зависимости от разрешения экрана
			if (isPhone){
				// запущено на телефоне
				mSpeed.x -= 0.01 * sensAccel * Settings.getScaleFactorX() * macelleration[1];
		        mSpeed.y -= 0.01 * sensAccel * Settings.getScaleFactorY() * macelleration[0];
			}else{
				// запущено на планшете
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
		
		soundVolume = (float)(Math.log(5f * scalMul(mSpeed,mSpeed) / scalMul(MAX_SPEED,MAX_SPEED) + 1) / Math.log(6f));
		
		if (soundVolume > 1f){
			soundVolume = 1f;
		}
    }
	
	public void onDraw(GL10 gl){
		super.onDraw(gl);
		mSoundEngine.setPlaySoundVolume(1,soundVolume);
	}
	
	public void mutePlaySound(){
		soundVolume = 0;
		mSoundEngine.setPlaySoundVolume(1,soundVolume);
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
    	float length = FloatMath.sqrt(scalMul(r,r));
    	float lenV = FloatMath.sqrt(scalMul(mSpeed,mSpeed));
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
	
	public PointF getCenter(){
		return new PointF(mPoint.x + mWidth / 2f, mPoint.y + mHeight / 2f);
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
	
    /** Отражение мячика от вертикали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectVertical(PointF newPoint)
    {   	
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.x = -mSpeed.x;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

    /** Отражение мячика от вертикали 
     * @param newPoint - точка шара после соударения
     * @param wall - стена об которую произошел удар
     * */
    public void reflectVertical(Wall wall, PointF newPoint)
    {
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.x = -mSpeed.x * wall.getSoftness();
    	mSpeed.y *= wall.getSoftness();
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
        
        wall.showWall();
    }
    
    /** Отражение мячика от горизонтали 
     * @param newPoint точка шара после соударения
     * */
    public void reflectHorizontal(PointF newPoint)
    {
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.y = -mSpeed.y;
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
    }

    /** Отражение мячика от горизонтали 
     * @param newPoint - точка шара после соударения
     * @param wall - стена об которую произошел удар
     * */
    public void reflectHorizontal(Wall wall, PointF newPoint)
    {
    	mPoint.x = newPoint.x;
    	mPoint.y = newPoint.y;
    	mNextPoint.x = newPoint.x;
    	mNextPoint.y = newPoint.y;
    	
    	mSpeed.y = -mSpeed.y * wall.getSoftness();
    	mSpeed.y *= wall.getSoftness();
    	
    	mNextPoint.x += mSpeed.x;
        mNextPoint.y += mSpeed.y;
        
        wall.showWall();
    }
    
    protected void onDestroy()
    {
    	super.onDestroy();
    	mSoundEngine.releaseSounds();
    }
    
}