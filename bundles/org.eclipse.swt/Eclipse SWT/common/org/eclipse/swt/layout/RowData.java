package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Each control controlled by a <code>RowLayout</code> can have its initial 
 * width and height specified by setting a <code>RowData</code> object 
 * into the control.
 * <p>
 * The following code uses a <code>RowData</code> object to change the initial
 * size of a <code>Button</code> in a <code>Shell</code>:
 * <pre>
 * 		Display display = new Display();
 * 		Shell shell = new Shell(display);
 * 		shell.setLayout(new RowLayout());
 * 		Button button1 = new Button(shell, SWT.PUSH);
 * 		button1.setText("Button 1");
 * 		button1.setLayoutData(new RowData(50, 40));
 * </pre>
 * </p>
 * 
 * @see RowLayout
 */
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
