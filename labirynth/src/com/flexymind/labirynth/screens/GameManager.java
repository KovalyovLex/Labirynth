package com.flexymind.labirynth.screens;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.screens.start.StartScreen;


public class GameManager extends AsyncTask<Void, Void, Boolean>
{

	public static final int FPS = 60;
	
	private Context context;
	
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
    public GameManager(SurfaceHolder surfaceHolder, GameLevel level, Context context)
    {
    	mlevel = level;
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        lastDrawTime = SystemClock.elapsedRealtime();
        this.context = context;
    }
    
    /**
     * Конструктор по умолчанию запускающий первый уровень
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    /*public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        
        // загрузка первого уровня из файла
        LevelStorage storage = new LevelStorage(context);
        Vector<String> names = storage.getLevelNames();
        mlevel = storage.loadGameLevelbyName(names.elementAt(0));
    }*/
    
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
	protected Boolean doInBackground(Void... arg0) {
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
        	
        	//если достигли финиша - то финишное меню, иначе обновляем уровень
        	if(mlevel.getIsFinished()) {        		
        		return true;
        	} else {
        		mlevel.onUpdate();
        	}
        	
        	try
            {
                synchronized (mSurfaceHolder)
                {
                    // подготовка Canvas-а
                    canvas = mSurfaceHolder.lockCanvas();
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
		return false;
	}
    
	@Override
	/**
	 * if result == true start dialog with finish!
	 */
    protected void onPostExecute(Boolean result) {
        if (result == true){
        	final Dialog dialog = new Dialog(context);
    		
			dialog.setContentView(R.layout.finishmenu);
			dialog.setTitle("You WIN!");
			
			TextView text = (TextView) dialog.findViewById(R.id.score);
			text.setText(String.format("Your score -  %d", (int)mlevel.getScore()));
			
			Button dialogButtonRestart = (Button) dialog.findViewById(R.id.dialogButtonRestart);
			// if button is clicked, close the custom dialog
			dialogButtonRestart.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			Button dialogButtonLevels = (Button) dialog.findViewById(R.id.dialogButtonLevels);
			// if button is clicked, close the custom dialog
			dialogButtonLevels.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (StartScreen.startActivity != null){
						StartScreen.startActivity.finishActivity(StartScreen.ID_GAMESCREEN);
					}
				}
			});
			
			Button dialogButtonQuit = (Button) dialog.findViewById(R.id.dialogButtonQuit);
			// if button is clicked, close the custom dialog
			dialogButtonQuit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (StartScreen.startActivity != null){
						StartScreen.startActivity.finishActivity(StartScreen.ID_GAMESCREEN);
						StartScreen.startActivity.finishActivity(StartScreen.ID_CHOISELEVELSCREEN);
					}
				}
			});
			dialog.show();
        }
    }
	
    /** Обновление объектов на экране */
    private void refreshCanvas(Canvas canvas)
    {
    	// рисуем уровень
    	mlevel.onDraw(canvas);
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
