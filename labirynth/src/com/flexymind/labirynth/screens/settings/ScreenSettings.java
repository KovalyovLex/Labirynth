﻿package com.flexymind.labirynth.screens.settings;


public class ScreenSettings {
	
	private static boolean autoScale   = false;
	private static boolean fullScreen  = false;
    private static int defaultXRes 	  = 800;
    private static int defaultYRes 	  = 480;
    private static int currentXRes;
    private static int currentYRes;
    private static double scaleFactorX = 1;
    private static double scaleFactorY = 1;

    
    public static void GenerateSettings(int w, int h)
    {
    	currentXRes = w;
        currentYRes = h;
        scaleFactorX = currentXRes / (double)defaultXRes;
        scaleFactorY = currentYRes / (double)defaultYRes;
        if (scaleFactorX != 1 || scaleFactorY != 1)
        {
        	autoScale = true;
        }
    }
    
    public static void setDefaultRes(int x, int y)
    {
        defaultXRes = x;
        defaultYRes = y;
    }

    public static boolean getAutoScale()
    {
        return autoScale;
    }
    
    public static int getDefaultXRes()
    {
        return defaultXRes;
    }
    
    public static int getDefaultYRes()
    {
        return defaultYRes;
    }
    
    public static int getCurrentXRes()
    {
        return currentXRes;
    }
    
    public static int getCurrentYRes()
    {
        return currentYRes;
    }
    
    public static double getScaleFactorX()
    {
        return scaleFactorX;
    }
    
    public static double getScaleFactorY()
    {
        return scaleFactorY;
    }
    
    public static boolean isFullScreen()
    {
        return fullScreen;
    }
}
