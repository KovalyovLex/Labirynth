package com.flexymind.labirynth.screens;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * Класс 
 * @author Kurnikov Sergey
 *
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    /**
     * Область рисования
     */
    private SurfaceHolder mSurfaceHolder;
    
    /**
     * Поток, рисующий в области
     */
    private GameManager mGameManager;
    
    /**
     * Конструктор
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        // подписываемся на события Surface
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        
        // Создание менеджера игровых объектов 
        mGameManager = new GameManager(mSurfaceHolder, context);
        
        // Разрешаем форме обрабатывать события клавиатуры
        setFocusable(true);
    }

    
    /**
     * Изменение области рисования
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mGameManager.initPositions(height, width);
    }

    
    /**
     * Создание области рисования
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
        mGameManager.setRunning(true);
        mGameManager.start();
    }

    
    /**
     * Уничтожение области рисования
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        mGameManager.setRunning(false);
        while (retry) 
        {
            try 
            {
                // ожидание завершение потока
                mGameManager.join(); 
                retry = false;
            } 
            catch (InterruptedException e) { }
        }
    }
    
    /**
     * @return <code>true</code>, если клавиша была обработана<br/>
     *         <code>false</code> иначе
     * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return mGameManager.doKeyDown(keyCode);
    }

    /**
     * @see android.view.View#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return mGameManager.doKeyUp(keyCode);
    }
}

