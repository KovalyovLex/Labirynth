package com.flexymind.labirynth.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.objects.Ball;
import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.objects.Wall;
import com.flexymind.labirynth.objects.FINISH;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;

/**
 * Класс сохранения состояния уровня, и загрузки уровней из хранилища
 * @author Alexander Kovalyov
 */
public class LevelStorage {

	private static final String LEVEL		= "level";
	private static final String SCORE		= "score";
	private static final String SCORE1STAR	= "sc1star";
	private static final String SCORE2STAR	= "sc2star";
	private static final String SCORE3STAR	= "sc3star";
	private static final String BALL		= "ball";
	private static final String WALL		= "wall";
	private static final String FINISH		= "finish";
	private static final String DRAWABLE	= "drawable";
	private static final String PROP_DIAM 	= "D";
	private static final String PROP_X1		= "X1";
	private static final String PROP_X2		= "X2";
	private static final String PROP_Y1		= "Y1";
	private static final String PROP_Y2		= "Y2";
	private static final String PROP_X		= "X";
	private static final String PROP_Y		= "Y";
	private static final String PROP_SOFT	= "Y";
	private static final String ATTR_NAME	= "name";
	
	private Map<String,String> drawablenames = new HashMap<String,String>();
	private Vector<String> names = new Vector<String>();
	private static int numNames;
	private Context context;
	
	/** 
	 * Конструктор класса
	 */
	public LevelStorage(Context cont){
		numNames = 0;
		context = cont;
		parseXML();
	}
	
	private void parseXML(){
		names = new Vector<String>();
		int deep = 0;
		String name = null;
		
		XmlResourceParser xml = context.getResources().getXml(R.xml.levels);
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if ( LEVEL.equals(xml.getName()) ){
					deep = xml.getDepth();
					for (int i = 0; i < xml.getAttributeCount(); i++){
						if ( ATTR_NAME.equals(xml.getAttributeName(i)) ){
							name = xml.getAttributeValue(i);
							names.addElement(xml.getAttributeValue(i));
						}
					}
					xml.next();
					while(xml.getDepth() > deep){
						if (DRAWABLE.equals(xml.getName())){
							drawablenames.put(name, xml.nextText());
						}
						xml.next();
					}
				}
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			xml.close();
		}
		numNames = names.size();
	}
	
	/**
	 * @return количество уровней в базе данных
	 */
	public static int getNumOfLevels(){
		return numNames;
	}
	
	/**
	 * Возвращает список всех имен уровней из хранилища
	 * @return <code>Vector<String></code> - список имен уровней
	 */
	public Vector<String> getLevelNames(){
		return names;
	}

	/**
	 * возвращает картинку превью уровня
	 * @param name - название уровня
	 * @return
	 */
	public Drawable getPrevPictireByName(String name){
		Drawable pic = null;
		String uri = "@drawable/";

		if (drawablenames.containsKey(name)){
			uri = uri.concat(drawablenames.get(name));		
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
		    pic = context.getResources().getDrawable(imageResource);
		}

		return pic;
	}
	
	/**
	 * Загружает из xml файла обьект GameLevel
	 * @param <code>String name<code> - имя уровня
	 * @param <code>GL10 gl<code> - OpenGL объект для рисования
	 * @return <code>GameLevel</code>, загруженный из базы
	 */
	public GameLevel loadGameLevelbyName(GL10 gl, String name){
		GameLevel game = null;
		XmlResourceParser xml = context.getResources().getXml(R.xml.levels);
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if ( LEVEL.equals(xml.getName()) ){
					for (int i = 0; i < xml.getAttributeCount(); i++){
						if ( ATTR_NAME.equals(xml.getAttributeName(i)) ){
							if (xml.getAttributeValue(i).equals(name) ){
								game = loadGameLevelfromxml(gl, xml);
							}
						}
					}
				}
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	private GameLevel loadGameLevelfromxml(GL10 gl, XmlResourceParser xml){
		GameLevel game = null;
		Vector<Wall> walls = new Vector<Wall>();
		Wall twall     = null;
		Ball tball     = null;
		FINISH tfinish = null;
		float softness = 0.7f;
		int score	= 0,
			sc1Star	= 0,
			sc2Star	= 0,
			sc3Star	= 0;
		int x1		= 0,
			x2		= 0,
			y1		= 0,
			y2		= 0,
			d		= 0,
			deep	= 0,
			finX	= 0,
			finY	= 0,
			finDiam = 0;
		
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if (SCORE.equals(xml.getName())){
					score = new Integer(xml.nextText());
				}
				if (SCORE1STAR.equals(xml.getName())){
					sc1Star = new Integer(xml.nextText());
				}
				if (SCORE2STAR.equals(xml.getName())){
					sc2Star = new Integer(xml.nextText());
				}
				if (SCORE3STAR.equals(xml.getName())){
					sc3Star = new Integer(xml.nextText());
				}
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
					tball = new Ball(	gl,
										context.getResources().getDrawable(R.drawable.ball2),
										new PointF(x1, y1), 
										d,
										(SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
				}
				if (FINISH.equals(xml.getName())){
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
					// загрузка  Объекта финиш, с текстурой
					tfinish = new FINISH (	gl,
											context.getResources().getDrawable(R.drawable.finish),
											new PointF(finX, finY), 
											finDiam);
				}
				if (WALL.equals(xml.getName())){
					softness = 0.7f;
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
						if (PROP_SOFT.equals(xml.getName())){
							softness = new Float(xml.nextText());
						}
						xml.next();
					}
					Drawable texture = context.getResources().getDrawable(R.drawable.wall);

					// загрузка стены с текстурой stenka
					twall = new Wall(	gl,
										texture,
										new PointF(x1 * (float)Settings.getScaleFactorX(), y1 * (float)Settings.getScaleFactorY()), 
										new PointF(x2 * (float)Settings.getScaleFactorX(), y2 * (float)Settings.getScaleFactorY()),
										softness);
					walls.add(twall);
					
				}
				if ( LEVEL.equals(xml.getName()) ){
					break;
				}
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			xml.close();
		}
		
		game = new GameLevel(	walls,
								tball,
								tfinish,
								gl,
								context.getResources().getDrawable(R.drawable.flexy3));
		
		if (score 	!= 0 &&
			sc1Star	!= 0 &&
			sc2Star	!= 0 &&
			sc3Star	!= 0){
			game.setScoreSystem(score, sc1Star, sc2Star, sc3Star);
		}
		
		return game;
	}
	
}
