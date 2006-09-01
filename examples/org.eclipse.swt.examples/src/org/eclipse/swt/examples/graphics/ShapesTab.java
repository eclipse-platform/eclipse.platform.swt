/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This tab draws 3D shapes (in 2D) using various line styles.
 */
public class ShapesTab extends AnimatedGraphicsTab {
	
	int upDownValue;
	int inc = 1;
	
public ShapesTab(GraphicsExample example) {
	super(example);
	upDownValue = 0;
}

public String getCategory() {
	return GraphicsExample.getResourceString("Lines"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Shapes"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("ShapesDescription"); //$NON-NLS-1$
}

public void next(int width, int height) {
	upDownValue += inc;

	if (upDownValue > 5) inc = -1;
	if (upDownValue < -5) inc = 1;
}

public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();

	int size = 100;
	
	gc.setLineWidth(2);
	
	// ----- cube -----
	
	Transform transform = new Transform(device);
	transform.translate(width/4 - size, height/4 + -upDownValue);
	gc.setTransform(transform);
	
	gc.setLineStyle(SWT.LINE_DOT);

	// fill in left face
	gc.setBackground(device.getSystemColor(SWT.COLOR_RED));
	gc.fillPolygon(new int [] {0, 0, size/3, -size/2, size/3, size/2, 0, size});
	
	gc.setLineStyle(SWT.LINE_SOLID);
	
	// square
	gc.drawRectangle(0, 0, size, size);
	
	// next 3 solid lines
	gc.drawLine(0, 0, size/3, -size/2);			// left
	gc.drawLine(size, 0, 4*size/3, -size/2);	// middle
	gc.drawLine(size, size, 4*size/3, size/2);	// right
	
	// 2 furthest solid lines
	gc.drawLine(size/3, -size/2, 4*size/3, -size/2);  	// horizontal
	gc.drawLine(4*size/3, size/2, 4*size/3, -size/2);	// vertical
	
	// 3 dotted lines
	gc.setLineStyle(SWT.LINE_DOT);
	gc.drawLine(0, size, size/3, size/2);
	gc.drawLine(size/3, -size/2, size/3, size/2);
	gc.drawLine(4*size/3, size/2, size/3, size/2);
	
	// fill right side of cube
	gc.setBackground(device.getSystemColor(SWT.COLOR_GRAY));
	gc.fillPolygon(new int [] {size, 0, 4*size/3, -size/2, 4*size/3, size/2, size, size});
	
	transform.dispose();
	
	// ----- pyramid -----
	
	transform = new Transform(device);
	transform.translate(width/2 + size/2, height/4 + size + upDownValue);
	gc.setTransform(transform);
	
	// fill back of pyramid
	gc.fillPolygon(new int [] {size/3, -size/2, 6*size/10, -5*size/4, 4*size/3, -size/2});
	
	// fill left side of pyramid
	gc.setBackground(device.getSystemColor(SWT.COLOR_GREEN));
	gc.fillPolygon(new int [] {0, 0, 6*size/10, -5*size/4, size/3, -size/2});
	
	// select solid line style
	gc.setLineStyle(SWT.LINE_SOLID);
	
	// 2 solid lines of base
	gc.drawLine(0, 0, size, 0);
	gc.drawLine(size, 0, 4*size/3, -size/2);
	
	// 3 solid lines of pyramid
	gc.drawLine(0, 0, 6*size/10, -5*size/4);
	gc.drawLine(size, 0, 6*size/10, -5*size/4);
	gc.drawLine(4*size/3, -size/2, 6*size/10, -5*size/4);
	
	// select dot line style
	gc.setLineStyle(SWT.LINE_DOT);
	
	// 3 dotted lines
	gc.drawLine(0, 0, size/3, -size/2);					// left 
	gc.drawLine(size/3, -size/2, 6*size/10, -5*size/4); // to top of pyramid
	gc.drawLine(4*size/3, -size/2, size/3, -size/2);	// right

	transform.dispose();
	
	// ----- rectangular prism -----
	
	transform = new Transform(device);
	transform.translate(width/2 + upDownValue, height/2 + size);
	gc.setTransform(transform);
	
	// fill bottom
	gc.setBackground(device.getSystemColor(SWT.COLOR_BLUE));
	gc.fillPolygon(new int [] {0, size, size/3, size/2, 7*size/3, size/2, 2*size, size});
	
	// select solid line style
	gc.setLineStyle(SWT.LINE_SOLID);
	
	gc.drawRectangle(0, 0, 2*size, size);
	
	// next 3 solid lines
	gc.drawLine(0, 0, size/3, -size/2);			// left
	gc.drawLine(2*size, 0, 7*size/3, -size/2);	// middle
	gc.drawLine(2*size, size, 7*size/3, size/2);	// right
	
	// 2 furthest solid lines
	gc.drawLine(size/3, -size/2, 7*size/3, -size/2);  	// horizontal
	gc.drawLine(7*size/3, size/2, 7*size/3, -size/2);	// vertical
	
	// 3 dotted lines
	gc.setLineStyle(SWT.LINE_DASHDOTDOT);
	gc.drawLine(0, size, size/3, size/2);
	gc.drawLine(size/3, -size/2, size/3, size/2);
	gc.drawLine(7*size/3, size/2, size/3, size/2);
	
	// fill top
	gc.setBackground(device.getSystemColor(SWT.COLOR_GRAY));
	gc.fillPolygon(new int [] {0, 0, size/3, -size/2, 7*size/3, -size/2, 2*size, 0});

	transform.dispose();
	
	// ----- triangular shape -----
	transform = new Transform(device);
	transform.translate(width/4 - size - upDownValue, height/2 + size + upDownValue);
	gc.setTransform(transform);
	
	// fill back of shape (top left)
	gc.setBackground(device.getSystemColor(SWT.COLOR_YELLOW));
	gc.fillPolygon(new int [] {0, 0, size/2, -size, size/2, -size/3});

	// fill back of shape (bottom right)
	gc.fillPolygon(new int [] {size, 0, size/2, size, size/2, -size/3});
	
	// select solid line style
	gc.setLineStyle(SWT.LINE_SOLID);
	
	// solid lines of bottom triangle
	gc.drawLine(0, 0, size/2, size);
	gc.drawLine(size, 0, size/2, size);
	
	// solid lines of top triangle
	gc.drawLine(0, 0, size/2, -size);
	gc.drawLine(size, 0, size/2, -size);
	
	// solid lines on top
	gc.drawLine(0, 0, size/2, -size/3);
	gc.drawLine(size, 0, size/2, -size/3);
	gc.drawLine(size/2, -size/3, size/2, size);
	gc.drawLine(size/2, -size/3, size/2, -size);
	
	transform.dispose();
}

}

