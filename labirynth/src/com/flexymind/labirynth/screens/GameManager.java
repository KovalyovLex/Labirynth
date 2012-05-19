package com.flexymind.labirynth.screens;


import java.util.Vector;

import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.screens.settings.ScreenSettings;
import com.flexymind.labirynth.storage.LevelStorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class GameManager extends Thread
{
    private static final int FIELD_WIDTH  = 800;
    private static final int FIELD_HEIGHT = 410;

    /** Область, на которой будем рисовать */
    private SurfaceHolder mSurfaceHolder;
    
    /** Состояние потока  */
    private boolean mRunning;
    
    /** Стили рисования */
    private Paint mPaint;
    
    /** Прямоугольник игрового поля */
    private Rect mField;
    
    private GameLevel mlevel = null;

    /** Фон */
    private Bitmap mBackground;
    
        
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
        // инициализация стилей рисования
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.STROKE);

        mField = new Rect();
    }
    
    /**
     * Конструктор
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        // инициализация стилей рисования
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.STROKE);
        
        // загрузка первого уровня из файла
        LevelStorage storage = new LevelStorage(context);
        Vector<String> names = storage.get_level_names();
        mlevel = storage.loadGameLevelbyName(names.elementAt(0));
        
        mField = new Rect();
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
        while (mRunning)
        {	
        	canvas = null;
            updateObjects();     // обновляем объекты
        	try
            {
                synchronized (mSurfaceHolder)
                {
                    // подготовка Canvas-а
                    canvas = mSurfaceHolder.lockCanvas(); 
                    refreshCanvas(canvas); // обновляем экран
                    sleep(2);
                }
            }
            catch (Exception e) { }
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
    	
    	// вывод фонового изображения
    	canvas.drawBitmap(mBackground, 0, 0, null);
    	
    	// debug отрисовка краёв рамки
    	canvas.drawRect(mField, mPaint);
    	
    	// рисуем уровень
    	mlevel.Draw(canvas);
    }

        
    /** Обновление состояния игровых объектов */
    private void updateObjects()
    {
        mlevel.Update();
    }
   
    /**
     * Инициализация положения объектов, в соответствии с размерами экрана
     * @param screenHeight Высота экрана
     * @param screenWidth Ширина экрана
     */
    public void initPositions(int screenHeight, int screenWidth)
    {
    	
        int left = (int) ((screenWidth - ScreenSettings.getScaleFactorX() * FIELD_WIDTH) / 2);
        int top = (int) ((screenHeight - ScreenSettings.getScaleFactorY() * FIELD_HEIGHT) / 2);
        
        mField.set(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
        
        mBackground = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
        
    }
    
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
            //stenka2.setDirection(GameObject.DIR_NONE);
            return true;
        }
        return false;
    }


}
