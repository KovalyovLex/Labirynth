package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.objects.Ball;
import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.screens.settings.ScreenSettings;
import com.flexymind.labirynth.storage.LevelStorage;

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
    
    private boolean surfWasCreated = false;
    
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
        
        // загрузка уровня
        LevelStorage storage = new LevelStorage(context);
        GameLevel lvl = storage.loadGameLevelbyName("Test level");
        
        // Создание менеджера игровых объектов
        mGameManager = new GameManager(mSurfaceHolder,  lvl);
        
        // Разрешаем форме обрабатывать события клавиатуры
        setFocusable(true);
    }

    
    /**
     * Изменение области рисования
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mGameManager.initPositions(height, width);
        ScreenSettings.GenerateSettings(width, height);
    }

    
    /**
     * Создание области рисования
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
		surfWasCreated = true;
		if (Thread.State.NEW.equals(mGameManager.getState())){
			mGameManager.setRunning(true);
			mGameManager.start();
		}else{
			onResume();
		}
    }

    
    /**
     * Уничтожение области рисования
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	surfWasCreated = false;
        mGameManager.setRunning(false);
        while (true) 
        {
            try 
            {
                // ожидание завершение потока
                mGameManager.join();
                break;
            } 
            catch (InterruptedException e) { }
        }
        
    }
    
    public void onPause(){
    	Ball.unregisterListeners();
    }
    
    public void onResume(){
    	if (surfWasCreated){
    		Ball.registerListeners();
    		if (Thread.State.TERMINATED.equals(mGameManager.getState())){
    			mGameManager = new GameManager(mSurfaceHolder,  mGameManager.getGameLevel());
    			mGameManager.initPositions(ScreenSettings.CurrentYRes(), ScreenSettings.CurrentXRes());
    			mGameManager.setRunning(true);
    			mGameManager.start();
    		}
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

