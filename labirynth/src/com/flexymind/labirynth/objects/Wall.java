package com.flexymind.labirynth.objects;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Wall extends GameObject{

	private Point secPoint, thirdPoint;
	
	/**
	 * ����������� �� ���������
	 * @param mBackG ��������
	 * @param first Point 1
	 * @param second Point 2
	 * @param third Point 3
	 */
	public Wall(	Drawable mBackG,
					Point first,
					Point second,
					Point third){
		super(mBackG);
		mImage.setBounds(first.x, first.y, third.x, third.y);
		mWidth = mImage.getIntrinsicWidth();
        mHeight = mImage.getIntrinsicHeight();
		
		this.mPoint = first;
		thirdPoint = third;
		secPoint = second;
	}

	/**
	 * ���������� �������� 1 �����
	 * @return point 1
	 */
	public Point getPoint1(){
		return mPoint;
	}
	
	/**
	 * ���������� �������� 2 �����
	 * @return point 2
	 */
	public Point getPoint2(){
		return secPoint;
	}
	
	/**
	 * ���������� �������� 3 �����
	 * @return point 3
	 */
	public Point getPoint3(){
		return thirdPoint;
	}
	
	@Override
	protected void updatePoint() {
		// TODO Auto-generated method stub
		
	}
}
