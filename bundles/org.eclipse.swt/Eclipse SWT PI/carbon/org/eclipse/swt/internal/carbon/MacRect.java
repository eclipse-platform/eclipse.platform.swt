/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Point;

public class MacRect {

	// 0: top
	// 1: left
	// 2: bottom
	// 3: right
	private short[] fData= new short[4];
	
	public MacRect() {
	}
	
	public MacRect(int x, int y, int w, int h) {
		fData[0]= (short) y;
		fData[1]= (short) x;
		fData[2]= (short) (y+h);
		fData[3]= (short) (x+w);
	}
	
	public MacRect(Rectangle r) {
		fData[0]= (short) (r.y);
		fData[1]= (short) (r.x);
		fData[2]= (short) (r.y+r.height);
		fData[3]= (short) (r.x+r.width);
	}
	
	public void set(int x, int y, int w, int h) {
		fData[0]= (short) y;
		fData[1]= (short) x;
		fData[2]= (short) (y+h);
		fData[3]= (short) (x+w);
	}
	
	public short[] getData() {
		return fData;
	}
	
	public Rectangle toRectangle() {
		return new Rectangle(fData[1], fData[0], fData[3]-fData[1], fData[2]-fData[0]);
	}
	
	public Point getSize() {
		return new Point(fData[3]-fData[1], fData[2]-fData[0]);
	}
	
	public Point getLocation() {
		return new Point(fData[1], fData[0]);
	}
	
	public void setSize(int width, int height) {
		fData[2]= (short)(fData[0]+ height);
		fData[3]= (short)(fData[1]+ width);
	}
	
	public int getX() {
		return fData[1];
	}
	
	public int getY() {
		return fData[0];
	}

	public int getWidth() {
		return fData[3]-fData[1];
	}
	
	public int getHeight() {
		return fData[2]-fData[0];
	}

	public boolean isEmpty() {
		return (fData[0] >= fData[2]) || (fData[1] >= fData[3]);
	}
	
	public boolean contains(Point p) {
		return p.x >= fData[1] && p.x < fData[3] && p.y >= fData[0] && p.y < fData[2];
	}
}
