package org.eclipse.swt.layout;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
	this.width = width;  this.height = height;
}
public RowData (Point point) {
	this (point.x, point.y);
}
}
