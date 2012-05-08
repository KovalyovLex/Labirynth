package com.flexymind.labirynth.screens;


import java.util.Vector;

import com.flexymind.labirynth.objects.GameLevel;
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

    /** �������, �� ������� ����� �������� */
    private SurfaceHolder mSurfaceHolder;
    
    /** ��������� ������  */
    private boolean mRunning;
    
    /** ����� ��������� */
    private Paint mPaint;
    
    /** ������������� �������� ���� */
    private Rect mField;
    
    private GameLevel mlevel = null;

    /** ��� */
    private Bitmap mBackground;
    
    /**
     * �����������
     * @param surfaceHolder ������� ���������
     * @param context �������� ����������
     * @param level ������� �������
     */
    public GameManager(SurfaceHolder surfaceHolder, GameLevel level)
    {
    	mlevel = level;
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        // ������������� ������ ���������
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.STROKE);

        mField = new Rect();
    }
    
    /**
     * �����������
     * @param surfaceHolder ������� ���������
     * @param context �������� ����������
     */
    public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        // ������������� ������ ���������
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.STROKE);
        
        // �������� ������� ������ �� �����
        LevelStorage storage = new LevelStorage(context);
        Vector<String> names = storage.get_level_names();
        mlevel = storage.loadGameLevelbyName(names.elementAt(0));
        
        mField = new Rect();
    }

	/**
     * ������� ��������� ������
     * @param running
     */
    public void setRunning(boolean running)
    {
        mRunning = running;
    }
    
    @Override
    /** ��������, ����������� � ������ */
    public void run()
    {
    	Canvas canvas = null;
        while (mRunning)
        {	
        	canvas = null;
            try
            {
            	//Log.v("Gman","run");
                // ���������� Canvas-�
                canvas = mSurfaceHolder.lockCanvas(); 
                synchronized (mSurfaceHolder)
                {
                    updateObjects();     // ��������� �������
                    refreshCanvas(canvas); // ��������� �����
                    sleep(20);
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
    
    /** ���������� �������� �� ������ */
    private void refreshCanvas(Canvas canvas)
    {
    	
    	// ����� �������� �����������
    	canvas.drawBitmap(mBackground, 0, 0, null);
    	canvas.drawRect(mField, mPaint);
    	
    	// ������ �������
    	mlevel.Draw(canvas);
    }

        
    /** ���������� ��������� ������� �������� */
    private void updateObjects()
    {
        mlevel.Update();
    }
   
    /**
     * ������������� ��������� ��������, � ������������ � ��������� ������
     * @param screenHeight ������ ������
     * @param screenWidth ������ ������
     */
    public void initPositions(int screenHeight, int screenWidth)
    {
        int left = (screenWidth - FIELD_WIDTH) / 2;
        int top = (screenHeight - FIELD_HEIGHT) / 2;
        
        mField.set(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
        
        mBackground = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
    }
    
    /**
     * ��������� ������� ������
     * @param keyCode ��� ������� ������
     * @return ���� �� ���������� �������
     */
    public boolean doKeyDown(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_A:
                //stenka2.setDirection(GameObject.DIR_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_D:
                //stenka2.setDirection(GameObject.DIR_RIGHT);
                return true;
            default:
                return false;
        }   
    }
    
    /**
     * ��������� ���������� ������
     * @param keyCode ��� ������
     * @return ���� �� �������� ����������
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
