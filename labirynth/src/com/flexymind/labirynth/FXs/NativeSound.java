package com.flexymind.labirynth.FXs;

public class NativeSound {
	
		static {
	       System.loadLibrary("com.flexymind.labirynth.FXs.NativeSound");
	    }
	    
	    public NativeSound(){
	    	initialize();
	    }
	    
	    /**
	     * Finish the native sound, free resouces
	     */
	    public native void finish();
	    
	    /**
	     * initialize the object
	     */
	    private native void initialize();
	    
}
