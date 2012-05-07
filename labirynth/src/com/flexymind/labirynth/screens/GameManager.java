package com.flexymind.labirynth.screens;


import com.android.pingpong.R;
import com.flexymind.labirynth.objects.Ball;
import com.flexymind.labirynth.objects.GameObject;
import com.flexymind.labirynth.objects.Wall;

import android.content.Context;
import android.content.res.Resources;
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
    private Rect mField,Pole;
    
    /** Мячик */
    private Ball mBall;    
    
    /** Фоновый рисунок и стенки */
    private Wall stenka2,stenka,phon;
    

    /** Фон */
    private Bitmap mBackground;
    
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

        Resources res = context.getResources();

        mField = new Rect();
        Pole = new  Rect(100,65,720,360);
        mBall = new Ball(res.getDrawable(R.drawable.ball2));
        //stenka2 = new Racquet(res.getDrawable(R.drawable.stenka2));
        //stenka = new Racquet(res.getDrawable(R.drawable.stenka));
        //phon = new Racquet (res.getDrawable(R.drawable.flexy3));
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
        while (mRunning)
        {
            Canvas canvas = null;
            try
            {
                // подготовка Canvas-а
                canvas = mSurfaceHolder.lockCanvas(); 
                synchronized (mSurfaceHolder)
                {
                    //updateObjects(); // обновляем объекты
                    refreshCanvas(canvas); // обновляем экран
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
    
    /** Обновление объектов на экране */
    private void refreshCanvas(Canvas canvas)
    {
        // вывод фонового изображения
    	canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawRect(mField, mPaint);
       
        // рисуем игровые объекты
       //phon.draw(canvas);
       //stenka2.draw(canvas);
       //mBall.draw(canvas);
       //stenka.draw(canvas);
       // рисуем игровое поле
       canvas.drawRect(Pole, null);

    }
    
    /*
    //функция, описывающая столкновения объектов
    private void collision(Ball ball, Racquet Wall)
    {

    	if (GameObject.intersects(ball, Wall))
        {
        	//проверка столкновения с верхней и нижней границей стенки
    	    if(ball.getTop() <=  Wall.getTop())
        	{
        		ball.setBottom( Wall.getBottom() - Math.abs( Wall.getBottom() - ball.getBottom()));
        		ball.reflectHorizontal();
        	}
    	    else if(ball.getBottom() >=  Wall.getBottom())
            {
            	ball.setTop(Wall.getTop() + Math.abs( Wall.getTop() - ball.getTop()));
            	ball.reflectHorizontal();  
            }

          	//проверка столкновения с правой и левой границей стенки
    	    else if(ball.getRight() <=  Wall.getRight())
            {
            	ball.setBottom(Wall.getBottom() - Math.abs(Wall.getBottom() - ball.getBottom()));
            	ball.reflectVertical();
            }
    	    else if(ball.getLeft() >=  Wall.getLeft())
            {
            	ball.setBottom( Wall.getBottom() - Math.abs( Wall.getBottom() - ball.getBottom()));
            	ball.reflectVertical();  
            }
       	
        }
    	
    }
    */
    
    
    /** Обновление состояния игровых объектов */
    /*private void updateObjects()
    {
        //phon.update();
    	//mBall.update();
    	//stenka.update();
    	//stenka2.update();

        
        // проверка столкновения мячика с вертикальными стенами
        if (mBall.getLeft() <= Pole.left)
        {
            mBall.setLeft(Pole.left + Math.abs(Pole.left - mBall.getLeft()));
            mBall.reflectVertical();
        }
        else if (mBall.getRight() >= Pole.right)
        {
            mBall.setRight(Pole.right - Math.abs(Pole.right - mBall.getRight()));
            mBall.reflectVertical();
        }
        
     // проверка столкновения мячика с горизонтальными стенами
        if (mBall.getTop() <= Pole.top)
        {
         mBall.setTop(Pole.top + Math.abs(Pole.top - mBall.getTop()));
         mBall.reflectHorizontal();
        }
        else if (mBall.getBottom() >= Pole.bottom)
        {
         mBall.setBottom(Pole.bottom - Math.abs(Pole.bottom - mBall.getBottom()));
         mBall.reflectHorizontal();
        }

        //collision(mBall, stenka);
        //collision(mBall, stenka2);
        
    }
    */


   
    /**
     * Инициализация положения объектов, в соответствии с размерами экрана
     * @param screenHeight Высота экрана
     * @param screenWidth Ширина экрана
     */
    public void initPositions(int screenHeight, int screenWidth)
    {
        int left = (screenWidth - FIELD_WIDTH) / 2;
        int top = (screenHeight - FIELD_HEIGHT) / 2;
        
        mField.set(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
        
        mBackground = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
        
        // мячик ставится в центр поля
        //mBall.setCenterX(Pole.centerX());
        //mBall.setCenterY(Pole.centerY());
        
        //phon.setLeft(mField.left);
        //phon.setBottom(mField.bottom);
        
        
        //stenka2.setCenterX(Pole.centerX());
        //stenka2.setBottom(Pole.bottom);
    
        // ракетка компьютера - сверху по центру
        //stenka.setCenterX(Pole.centerX()+100);
        //stenka.setBottom(Pole.bottom-60);
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
