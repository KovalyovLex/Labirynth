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
import android.util.Log;

/**
 * Класс сохранения состояния уровня, и загрузки уровней из хранилища
 * @author Alexander Kovalyov
 */
public class LevelStorage {

	private static final String LEVEL = "level";
	private static final String BALL = "ball";
	private static final String WALL = "wall";
	private static final String FINISH = "finish";
	private static final String PROP_WIDTH = "width";
	private static final String PROP_DIAM = "D";
	private static final String PROP_X1 = "X1";
	private static final String PROP_X2 = "X2";
	private static final String PROP_Y1 = "Y1";
	private static final String PROP_Y2 = "Y2";
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
		Wall twall = null;
		Ball tball = null;
		int X1, X2, Y1, Y2, D;
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
	
}
