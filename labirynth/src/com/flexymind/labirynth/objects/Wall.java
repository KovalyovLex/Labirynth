package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Wall extends GameObject{

	private Point secPoint;
	private int width;
	
	public Wall(	Drawable mBackG,
					Point first,
					Point second,
					int width){
		super(mBackG);
		this.width = width;
		this.mPoint = first;
		secPoint = second;
	}

	@Override
	protected void updatePoint() {
		// TODO Auto-generated method stub
		
	}
}
