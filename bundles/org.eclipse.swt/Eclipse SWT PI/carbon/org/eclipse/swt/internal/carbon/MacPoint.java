/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.graphics.Point;

public class MacPoint {

	// 0: vertical
	// 1: horizontal
	private short[] fData= new short[2];
	
	public MacPoint() {
	}
	
	public MacPoint(short x, short y) {
		fData[0]= y;
		fData[1]= x;
	}
	
	public MacPoint(int x, int y) {
		fData[0]= (short) y;
		fData[1]= (short) x;
	}
	
	public MacPoint(Point p) {
		fData[0]= (short) p.y;
		fData[1]= (short) p.x;
	}
	
	public short[] getData() {
		return fData;
	}
	
	public int getX() {
		return fData[1];
	}
	
	public int getY() {
		return fData[0];
	}
	
	public Point toPoint() {
		return new Point(fData[1], fData[0]);
	}
}
