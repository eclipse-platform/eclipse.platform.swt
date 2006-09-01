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
 * Miscellaneous tab that demonstrates emerging colors from layering other
 * colors.
 */
public class RGBTab extends AnimatedGraphicsTab {
	
	int translateX, translateY;
	float diagTranslateX1, diagTranslateX2, diagTranslateY1, diagTranslateY2;

/**
 * Constructor
 * @param example A GraphicsExample
 */
public RGBTab(GraphicsExample example) {
	super(example);
	translateX = translateY = 0;
	diagTranslateX1 = diagTranslateX2 = diagTranslateY1 = diagTranslateY2 = 0;
}

public String getCategory() {
	return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("rgb"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("rgbDescription"); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.AnimatedGraphicsTab#next(int, int)
 */
public void next(int width, int height) {
	
	float h = height;
	float w = width;
	
	translateX = (translateX+3)%width;
	translateY = (translateY+5)%height;

	diagTranslateX1 = (diagTranslateX1+6)%width;
	diagTranslateY1 = diagTranslateX1*(h/w);
	
	diagTranslateX2 = (diagTranslateX2+8)%width;
	diagTranslateY2 = -diagTranslateX2*(h/w) + h;
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	// horizontal rectangle
	Transform transform = new Transform(device);
	transform.translate(0, translateY);
	gc.setTransform(transform);
	transform.dispose();
	
	Path path = new Path(device);
	path.addRectangle(0, 0, width, 50);
	Pattern pattern = new Pattern(device, 0, 0, width, 50, 
				device.getSystemColor(SWT.COLOR_BLUE), 0x7f,
				device.getSystemColor(SWT.COLOR_RED), 0x7f);
	gc.setBackgroundPattern(pattern);
	gc.fillPath(path);
	gc.drawPath(path);
	path.dispose();

	// vertical rectangle
	transform = new Transform(device);
	transform.translate(translateX, 0);
	gc.setTransform(transform);
	transform.dispose();
	
	path = new Path(device);
	path.addRectangle(0, 0, 50, height);
	pattern.dispose();
	pattern = new Pattern(device, 0, 0, 50, height, 
				device.getSystemColor(SWT.COLOR_DARK_CYAN), 0x7f,
				device.getSystemColor(SWT.COLOR_WHITE), 0x7f);
	gc.setBackgroundPattern(pattern);
	gc.fillPath(path);
	gc.drawPath(path);
	path.dispose();

	// diagonal rectangle from bottom right corner
	Rectangle rect = new Rectangle(0, 0, 50, height);
	transform = new Transform(device);
	transform.translate(width-diagTranslateX1, (height/2)-diagTranslateY1);
	
	// rotate on center of rectangle
	transform.translate(rect.width/2, rect.height/2);
	transform.rotate(45);
	transform.translate(-rect.width/2, -rect.height/2);
	gc.setTransform(transform);
	transform.dispose();
	
	path = new Path(device);
	path.addRectangle(rect.x, rect.y, rect.width, rect.height);
	pattern.dispose();
	pattern = new Pattern(device, rect.x, rect.y, rect.width, rect.height, 
			device.getSystemColor(SWT.COLOR_DARK_GREEN), 0x7f,
			device.getSystemColor(SWT.COLOR_DARK_MAGENTA), 0x7f);
	gc.setBackgroundPattern(pattern);
	gc.fillPath(path);
	gc.drawPath(path);
	path.dispose();
	
	// diagonal rectangle from top right corner
	transform = new Transform(device);
	transform.translate(width-diagTranslateX2, (height/2)-diagTranslateY2);
	
	// rotate on center of rectangle
	transform.translate(rect.width/2, rect.height/2);
	transform.rotate(-45);
	transform.translate(-rect.width/2, -rect.height/2);
	gc.setTransform(transform);
	transform.dispose();
	
	path = new Path(device);
	path.addRectangle(rect.x, rect.y, rect.width, rect.height);
	pattern.dispose();
	pattern = new Pattern(device, rect.x, rect.y, rect.width, rect.height, 
			device.getSystemColor(SWT.COLOR_DARK_RED), 0x7f,
			device.getSystemColor(SWT.COLOR_YELLOW), 0x7f);
	gc.setBackgroundPattern(pattern);
	gc.fillPath(path);
	gc.drawPath(path);
	pattern.dispose();
	path.dispose();
}

}
