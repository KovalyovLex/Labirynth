package com.flexymind.labirynth.screens;


import java.util.Vector;

import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.storage.LevelStorage;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class GameManager extends Thread
{

	public static final int FPS = 60;
	
	private long updMillisec = 1000 / FPS;
	
    /** Область, на которой будем рисовать */
    private SurfaceHolder mSurfaceHolder;
    
    /** Состояние потока  */
    private boolean mRunning;
    
    private GameLevel mlevel = null;
    
    private long lastDrawTime = 0;
    
    /**
     * Конструктор
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     * @param level Игровой уровень
     */
    public GameManager(SurfaceHolder surfaceHolder, GameLevel level)
    {
    	mlevel = level;
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        lastDrawTime = SystemClock.elapsedRealtime();
    }
    
    /**
     * Конструктор по умолчанию запускающий первый уровень
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        
        // загрузка первого уровня из файла
        LevelStorage storage = new LevelStorage(context);
        Vector<String> names = storage.get_level_names();
        mlevel = storage.loadGameLevelbyName(names.elementAt(0));
    }
    
	/**
     * Задание состояния потока
     * @param running
     */
    public void setRunning(boolean running)
    {
        mRunning = running;
    }
    
    @Override
    /** Действия, выполняемые в потоке */
    public void run()
    {
    	Canvas canvas = null;
    	lastDrawTime = SystemClock.elapsedRealtime();
    	
        while (mRunning)
        {
        	lastDrawTime = SystemClock.elapsedRealtime() - lastDrawTime;
        	if (lastDrawTime < updMillisec){
        		SystemClock.sleep(updMillisec - lastDrawTime);
        	}
        	lastDrawTime = SystemClock.elapsedRealtime();
        	
        	canvas = null;
            updateObjects();     // обновляем объекты
        	try
            {
                synchronized (mSurfaceHolder)
                {
                    // подготовка Canvas-а
                    canvas = mSurfaceHolder.lockCanvas();
                    
                	Log.v("CanvasAcell",new Boolean(canvas.isHardwareAccelerated()).toString());
                	
                    refreshCanvas(canvas); // обновляем экран
                }
            }
        	catch(Exception ex){ }
            finally
            {
                if (canvas != null)
                {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    
    /** Обновление объектов на экране */
    private void refreshCanvas(Canvas canvas)
    {
    	// рисуем уровень
    	mlevel.onDraw(canvas);
    }

        
    /** Обновление состояния игровых объектов */
    private void updateObjects()
    {
        mlevel.onUpdate();
    }
   
    /**
     * Инициализация положения объектов, в соответствии с размерами экрана
     * @param screenHeight Высота экрана
     * @param screenWidth Ширина экрана
     */
    public void initPositions(int screenHeight, int screenWidth){ }
    
    /**
     * Возвращает игровой уровень
     * @return уровень
     */
    public GameLevel getGameLevel(){
    	return mlevel;
    }
    
    /**
     * Обработка нажатия кнопки
     * @param keyCode Код нажатой кнопки
     * @return Было ли обработано нажатие
     */
    public boolean doKeyDown(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_A:
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_D:
                return true;
            default:
                return false;
        }   
    }
    
    /**
     * Обработка отпускания кнопки
     * @param keyCode Код кнопки
     * @return Было ли действие обработано
     */
    public boolean doKeyUp(int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
            keyCode == KeyEvent.KEYCODE_A ||
            keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
            keyCode == KeyEvent.KEYCODE_D
            )
        {
            return true;
        }
        return false;
    }


}
