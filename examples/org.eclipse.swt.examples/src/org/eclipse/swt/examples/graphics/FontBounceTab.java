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
 * This tab is an animated graphic. It presents the word SWT in different fonts
 * as it bounces around the screen.
 */
public class FontBounceTab extends AnimatedGraphicsTab {
	
	float x, y;
	float incX = 10.0f;				// units by which to move the word along X axis
	float incY = 5.0f;				// units by which to move the word along Y axis 
	int textWidth, textHeight;		// width and height of the word SWT
	String text = GraphicsExample.getResourceString("SWT");
	int fontSize = 100;
	int fontFace = 0; 
	int foreGrdColor, fillColor;	// font colors
	int fontStyle;				// represents various style attributes applicable to a Font
	
public FontBounceTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Font"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Bounce"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("FontBounceDescription"); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.AnimatedGraphicsTab#next(int, int)
 */
public void next(int width, int height) {
	x += incX;
    y += incY;
	float random = (float)Math.random();
	
	// collision with right side of screen 
    if (x + textWidth > width) {
        x = width - textWidth;
        incX = random * -width / 16 - 1;
        fontFace = 0;
        fontSize = 125;
        fillColor = SWT.COLOR_DARK_BLUE;  
        foreGrdColor = SWT.COLOR_YELLOW;
        fontStyle = SWT.ITALIC;
    }
	// collision with left side of screen
    if (x < 0) {
        x = 0;
        incX = random * width / 16 + 1;
        fontFace = 1;
        fontSize = 80;
        fillColor = SWT.COLOR_DARK_MAGENTA;
        foreGrdColor = SWT.COLOR_CYAN;
        fontStyle = SWT.NONE;
    }
	// collision with bottom side of screen
    if (y + textHeight > height) {
        y = (height - textHeight)- 2;
        incY = random * -height / 16 - 1;
        fontFace = 2;
        fontSize = 100;
        fillColor = SWT.COLOR_YELLOW;
        foreGrdColor = SWT.COLOR_BLACK;
        fontStyle = SWT.BOLD;
    }
	// collision with top side of screen
    if (y < 0) {
        y = 0;
        incY = random * height / 16 + 1;
        fontFace = 3;
        fontSize = 120;
        fillColor = SWT.COLOR_GREEN;
        foreGrdColor = SWT.COLOR_GRAY;
        fontStyle = SWT.NONE;
    }
}


/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	Font font = new Font(device, getPlatformFontFace(fontFace), fontSize, fontStyle);
	gc.setFont(font);
	
	Point size = gc.stringExtent(text);
	textWidth = size.x;
	textHeight = size.y;
	
	Path path = new Path(device);
	path.addString(text, x, y, font);

	gc.setForeground(device.getSystemColor(foreGrdColor));
	gc.setBackground(device.getSystemColor(fillColor));

	gc.fillPath(path);
	gc.drawPath(path);
	font.dispose();
	path.dispose();
}

/**
 * Returns the name of the font using the specified index.
 * This method takes into account the resident platform.
 * 
 * @param index
 * 			The index of the font to be used
 */
static String getPlatformFontFace(int index) {	
	if(SWT.getPlatform() == "win32") {
		return new String [] {"Arial", "Impact", "Times", "Verdana"} [index];	
	} else if (SWT.getPlatform() == "motif") {
		return new String [] {"URW Chancery L", "URW Gothic L", "Times", "qub"} [index];		
	} else if (SWT.getPlatform() == "gtk") {
		return new String [] {"URW Chancery L", "Baekmuk Batang", "Baekmuk Headline", "KacsTitleL"} [index];
	} else if (SWT.getPlatform() == "carbon") {
		return new String [] {"Arial", "Impact", "Times", "Verdana"} [index];
	} else { // photon, etc ...
		return new String [] {"Arial", "Impact", "Times", "Verdana"} [index];
	}
}
}
