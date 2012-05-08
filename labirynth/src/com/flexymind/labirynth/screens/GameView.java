package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.storage.LevelStorage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * ����� 
 * @author Kurnikov Sergey
 *
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    /**
     * ������� ���������
     */
    private SurfaceHolder mSurfaceHolder;
    
    /**
     * �����, �������� � �������
     */
    private GameManager mGameManager;
    
    /**
     * �����������
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        // ������������� �� ������� Surface
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        
        // �������� ������
        LevelStorage storage = new LevelStorage(context);
        
        // �������� ��������� ������� ��������
        mGameManager = new GameManager(mSurfaceHolder, storage.loadGameLevelbyName("First level") );
        
        // ��������� ����� ������������ ������� ����������
        setFocusable(true);
    }

    
    /**
     * ��������� ������� ���������
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mGameManager.initPositions(height, width);
    }

    
    /**
     * �������� ������� ���������
     */
    public void surfaceCreated(SurfaceHolder holder)
    {	
        mGameManager.setRunning(true);
        mGameManager.start();
    }

    
    /**
     * ����������� ������� ���������
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mGameManager.setRunning(false);
        while (true) 
        {
            try 
            {
                // �������� ���������� ������
                mGameManager.join();
                break;
            } 
            catch (InterruptedException e) { }
        }
        
    }
    
    /**
     * @return <code>true</code>, ���� ������� ���� ����������<br/>
     *         <code>false</code> �����
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

