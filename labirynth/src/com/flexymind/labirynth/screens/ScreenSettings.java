package com.flexymind.labirynth.screens;

<<<<<<< HEAD
import android.view.Display;

=======
>>>>>>> 1a0496cf887058b13588f9079f632d26f2b8a560

public class ScreenSettings {
	
	public static boolean AutoScale   = false;
	public static boolean FullScreen  = false;
    public static int DefaultXRes 	  = 800;
    public static int DefaultYRes 	  = 480;
    public static int CurrentXRes;
    public static int CurrentYRes;
    public static double ScaleFactorX =1;
    public static double ScaleFactorY =1;
<<<<<<< HEAD
    public Display display;
=======
>>>>>>> 1a0496cf887058b13588f9079f632d26f2b8a560
    
 /*   public static void GenerateSettings(int w, int h)
    {
        ScreenSettings.CurrentXRes = w;
        ScreenSettings.CurrentYRes = h;
        ScreenSettings.ScaleFactorX = ScreenSettings.CurrentXRes/(double)ScreenSettings.DefaultXRes;
        ScreenSettings.ScaleFactorY = ScreenSettings.CurrentYRes/(double)ScreenSettings.DefaultYRes;
        if (ScreenSettings.ScaleFactorX!=1||ScreenSettings.ScaleFactorY!=1)
        {
            ScreenSettings.AutoScale=true;
        }
    } */
    
    public static void GenerateSettings(int w, int h)
    {
<<<<<<< HEAD
    	ScreenSettings.CurrentXRes =w;
=======
        ScreenSettings.CurrentXRes =w;
>>>>>>> 1a0496cf887058b13588f9079f632d26f2b8a560
        ScreenSettings.CurrentYRes = h;
        ScreenSettings.ScaleFactorX = ScreenSettings.CurrentXRes/(double)ScreenSettings.DefaultXRes;
        ScreenSettings.ScaleFactorY = ScreenSettings.CurrentYRes/(double)ScreenSettings.DefaultYRes;
        if (ScreenSettings.ScaleFactorX!=1||ScreenSettings.ScaleFactorY!=1)
        {
        	ScreenSettings.AutoScale=true;
        }
    }
    
    public static void setDefaultRes(int x, int y)
    {
        ScreenSettings.DefaultXRes = x;
        ScreenSettings.DefaultYRes = y;
    }

}
