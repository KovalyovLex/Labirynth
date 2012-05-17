package com.flexymind.labirynth.storage;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.objects.Ball;
import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.objects.Wall;
import com.flexymind.labirynth.objects.FINISH;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;

/**
 * Класс сохранения состояния уровня, и загрузки уровней из хранилища
 * @author Alexander Kovalyov
 */
public class LevelStorage {

	private static final String LEVEL = "level";
	private static final String BALL = "ball";
	private static final String WALL = "wall";
	private static final String FINISH = "finish";
	private static final String PROP_DIAM = "D";
	private static final String PROP_X1 = "X1";
	private static final String PROP_X2 = "X2";
	private static final String PROP_X3 = "X3";
	private static final String PROP_Y1 = "Y1";
	private static final String PROP_Y2 = "Y2";
	private static final String PROP_Y3 = "Y3";
	private static final String PROP_X = "X";
	private static final String PROP_Y = "Y";
	private static final String ATTR_NAME = "name";
	
	private Context context;
	
	/** 
	 * Конструктор класса
	 */
	public LevelStorage(Context cont){
		context = cont;
	}
	
	/**
	 * Возвращает список всех имен уровней из хранилища
	 * @return <code>Vector<String></code> - список имен уровней
	 */
	public Vector<String> get_level_names(){
		Vector<String> strs = new Vector<String>();
		XmlResourceParser xml = context.getResources().getXml(R.xml.levels);
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if ( LEVEL.equals(xml.getName()) ){
					for (int i = 0; i < xml.getAttributeCount(); i++){
						if ( ATTR_NAME.equals(xml.getAttributeName(i)) ){
							strs.addElement(xml.getAttributeValue(i));
						}
					}
				}
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			xml.close();
		}
		
		return strs;
	}

	/**
	 * Загружает из xml файла обьект GameLevel
	 * @param <code>String name<code> - имя уровня
	 * @return <code>GameLevel</code>, загруженный из базы
	 */
	public GameLevel loadGameLevelbyName(String name){
		GameLevel game = null;
		XmlResourceParser xml = context.getResources().getXml(R.xml.levels);
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if ( LEVEL.equals(xml.getName()) ){
					for (int i = 0; i < xml.getAttributeCount(); i++){
						if ( ATTR_NAME.equals(xml.getAttributeName(i)) ){
							if (xml.getAttributeValue(i).equals(name) ){
								game = loadGameLevelfromxml(xml);
							}
						}
					}
				}
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			xml.close();
		}
		
		return game;
	}
	
	/**
	 * Загружает из xml файла обьект GameLevel
	 * @param <code>XmlResourceParser xml<code> - xml файл из базы, указывающий на level
	 * @return <code>GameLevel</code>, загруженный из базы
	 */
	private GameLevel loadGameLevelfromxml(XmlResourceParser xml){
		GameLevel game = null;
		Vector<Wall> walls = new Vector<Wall>();
		Wall twall     = null;
		Ball tball     = null;
		FINISH tfinish = null;
		int x1		= 0,
			x2		= 0,
			y1		= 0,
			y2		= 0,
			d		= 0,
			deep	= 0,
			x3		= 0,
			y3		= 0,
			finX	= 0,
			finY	= 0,
			finDiam = 0;
		
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if (BALL.equals(xml.getName())){
					deep = xml.getDepth();
					xml.next();
					while(xml.getDepth() > deep){
						if (PROP_X.equals(xml.getName())){
							x1 = new Integer(xml.nextText());
						}
						if (PROP_Y.equals(xml.getName())){
							y1 = new Integer(xml.nextText());
						}
						if (PROP_DIAM.equals(xml.getName())){
							d = new Integer(xml.nextText());
						}
						xml.next();
					}
					// загрузка шара с текстурой ball
					tball = new Ball(	context.getResources().getDrawable(R.drawable.ball2),
										new Point(x1, y1), 
										d,
										(SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
				}else if (WALL.equals(xml.getName())){
					deep = xml.getDepth();
					xml.next();
					while(xml.getDepth() > deep){
						if (PROP_X1.equals(xml.getName())){
							x1 = new Integer(xml.nextText());
						}
						if (PROP_Y1.equals(xml.getName())){
							y1 = new Integer(xml.nextText());
						}
						if (PROP_X2.equals(xml.getName())){
							x2 = new Integer(xml.nextText());
						}
						if (PROP_Y2.equals(xml.getName())){
							y2 = new Integer(xml.nextText());
						}
						if (PROP_X3.equals(xml.getName())){
							x3 = new Integer(xml.nextText());
						}
						if (PROP_Y3.equals(xml.getName())){
							y3 = new Integer(xml.nextText());
						}
						xml.next();
					}
					Drawable texture = context.getResources().getDrawable(R.drawable.stenka2);
					
					Bitmap bmp = ((BitmapDrawable)texture).getBitmap();
					bmp = Bitmap.createScaledBitmap(bmp, (int)(Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))), (int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)), true);
					
					int max = bmp.getHeight() > bmp.getWidth() ? bmp.getHeight() : bmp.getWidth();
					
					Bitmap textbmp = Bitmap.createBitmap(max, max, Bitmap.Config.ARGB_8888);
					Canvas canv = new Canvas(textbmp);
					
					Paint p = new Paint();
					
					// угол поворота текстуры
					float angle, centang;
					int x = - y1 + y2;
					int y = x1 - x2;
					
					Point	shift	= new Point((max - bmp.getWidth() ) / 2, (max - bmp.getHeight()) / 2);
					float shiftleng;
					Point cent = new Point(max / 2, max / 2);
					
					shift.x -= cent.x;
					shift.y -= cent.y;
					shiftleng = (float)Math.sqrt((shift.x)*(shift.x)+(shift.y)*(shift.y));
					
					// угол поворота вектора от центра до point1
					centang = (float)Math.acos( shift.x / shiftleng);
					if (shift.y < 0){
						centang = 2 * (float)Math.PI - centang;
					}
					// угол поворота текстуры
					angle = (float)Math.acos( x / Math.sqrt(x * x + y * y));
					if (y < 0){
						angle = 2 * (float)Math.PI - angle;
					}
					centang += angle;
					angle *= 360f / 2 / (float)Math.PI;
					
					shift.x = (int)(shiftleng * Math.cos(centang));
					shift.y = (int)(shiftleng * Math.sin(centang));
					
					// расстояние до точки p1
					shift.x += cent.x;
					shift.y += cent.y;
					
					Matrix 	matrix	= new Matrix();

					matrix.setTranslate( (max - bmp.getWidth()) / 2, (max - bmp.getHeight()) / 2);
					matrix.postRotate(angle, max / 2, max / 2);

					p = new Paint();
					p.setFilterBitmap(true);
					
					canv.drawBitmap(bmp, matrix, p);

					bmp.recycle();
					
					// загрузка стены с текстурой stenka
					twall = new Wall(	textbmp,
										new Point(x1, y1), 
										new Point(x2, y2), 
										new Point(x3, y3),
										shift,
										0.70f);
					walls.add(twall);
				}else if (FINISH.equals(xml.getName())){
					deep = xml.getDepth();
					xml.next();
					while(xml.getDepth() > deep){
						if (PROP_X.equals(xml.getName())){
							finX = new Integer(xml.nextText());
						}
						if (PROP_Y.equals(xml.getName())){
							finY = new Integer(xml.nextText());
						}
						if (PROP_DIAM.equals(xml.getName())){
							finDiam = new Integer(xml.nextText());
						}
						xml.next();
					}
				}
				// загрузка  Объекта финиш, с текстурой
				tfinish = new FINISH (	context.getResources().getDrawable(R.drawable.finish),
									new Point(finX, finY), 
									finDiam);
				if ( LEVEL.equals(xml.getName()) ){
					break;
				}
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			xml.close();
		}
		
		game = new GameLevel(	walls,
								tball,
								tfinish,
								context.getResources().getDrawable(R.drawable.flexy3));
		
		return game;
	}
	
}
