package com.flexymind.labirynth.storage;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.android.pingpong.R;
import com.flexymind.labirynth.objects.Ball;
import com.flexymind.labirynth.objects.GameLevel;
import com.flexymind.labirynth.objects.Wall;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Point;
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
		Wall twall = null;
		Ball tball = null;
		int X1 = 0, X2 = 0, Y1 = 0, Y2 = 0, D = 0, deep = 0, X3 = 0, Y3 = 0;
		int finX = 0, finY = 0, finDiam = 0;
		
		try {
			while (xml.next() != XmlPullParser.END_DOCUMENT){
				if (BALL.equals(xml.getName())){
					deep = xml.getDepth();
					xml.next();
					while(xml.getDepth() > deep){
						if (PROP_X.equals(xml.getName())){
							X1 = new Integer(xml.nextText());
						}
						if (PROP_Y.equals(xml.getName())){
							Y1 = new Integer(xml.nextText());
						}
						if (PROP_DIAM.equals(xml.getName())){
							D = new Integer(xml.nextText());
						}
						xml.next();
					}
					// загрузка шара с текстурой ball
					tball = new Ball(	context.getResources().getDrawable(R.drawable.ball2),
										new Point(X1, Y1), 
										D,
										(SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
				}else if (WALL.equals(xml.getName())){
					deep = xml.getDepth();
					xml.next();
					while(xml.getDepth() > deep){
						if (PROP_X1.equals(xml.getName())){
							X1 = new Integer(xml.nextText());
						}
						if (PROP_Y1.equals(xml.getName())){
							Y1 = new Integer(xml.nextText());
						}
						if (PROP_X2.equals(xml.getName())){
							X2 = new Integer(xml.nextText());
						}
						if (PROP_Y2.equals(xml.getName())){
							Y2 = new Integer(xml.nextText());
						}
						if (PROP_X3.equals(xml.getName())){
							X3 = new Integer(xml.nextText());
						}
						if (PROP_Y3.equals(xml.getName())){
							Y3 = new Integer(xml.nextText());
						}
						xml.next();
					}
					// загрузка стены с текстурой stenka
					twall = new Wall(	context.getResources().getDrawable(R.drawable.stenka2),
										new Point(X1, Y1), 
										new Point(X2, Y2), 
										new Point(X3, Y3));
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
		}
		
		game = new GameLevel(	walls,
								tball,
								finX,
								finY,
								finDiam,
								context.getResources().getDrawable(R.drawable.flexy3));
		
		return game;
	}
	
}
