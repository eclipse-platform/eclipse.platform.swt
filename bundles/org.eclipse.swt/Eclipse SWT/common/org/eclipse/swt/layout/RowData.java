/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.layout;

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
	 * width specifies the desired width in pixels. This value
	 * is the wHint passed into Control#computeSize(int, int, boolean) 
	 * to determine the preferred size of the control.
	 *
	 * The default value is SWT.DEFAULT.
	 *
	 */
	public int width = SWT.DEFAULT;
	/**
	 * height specifies the preferred height in pixels. This value
	 * is the hHint passed into Control#computeSize(int, int, boolean) 
	 * to determine the preferred size of the control.
	 *
	 * The default value is SWT.DEFAULT.
	 *
	 */
	public int height = SWT.DEFAULT;
	
	/**
	 * exclude informs the layout to ignore this control when sizing
	 * and positioning controls.  If this value is <code>true</code>,
	 * the size and position of the control will not be managed by the
	 * layout.  If this	value is <code>false</code>, the size and 
	 * position of the control will be computed and assigned.
	 * 
	 * The default value is <code>false</code>.
	 * 
	 * @since 3.1
	 */
	public boolean exclude = false;
	
public RowData () {
}

public RowData (int width, int height) {
	this.width = width;
	this.height = height;
}

public RowData (Point point) {
	this (point.x, point.y);
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

public String toString () {
	String string = getName ()+" {";
	if (width != SWT.DEFAULT) string += "width="+width+" ";
	if (height != SWT.DEFAULT) string += "height="+height+" ";
	if (exclude) string += "exclude="+exclude+" ";
	string = string.trim();
	string += "}";
	return string;
}
}
