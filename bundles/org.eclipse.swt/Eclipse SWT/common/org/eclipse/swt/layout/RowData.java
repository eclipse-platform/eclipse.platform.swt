package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public final class RowData {
	/**
	 * width specifies the width of the cell in pixels.
	 */
	public int width;
	/**
	 * height specifies the height of the cell in pixels.
	 */
	public int height;
	
public RowData () {
	this (SWT.DEFAULT, SWT.DEFAULT);
}

public RowData (int width, int height) {
	this.width = width;
	this.height = height;
}

public RowData (Point point) {
	this (point.x, point.y);
}
}
