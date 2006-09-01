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
 * This tab demonstrates alpha blending. It draws various shapes and images as
 * alpha values change.
 */
public class AlphaTab extends AnimatedGraphicsTab {
	/**
	 * Value used in setAlpha API call. Goes from 0 to 255 and then starts over.
	 */
	int alphaValue;
	
	/**
	 * Value used in setAlpha API call. Goes from 0 to 255, then from 255 to 0
	 * and then starts over.
	 */
	int alphaValue2;

	boolean reachedMax = false;
	int diameter;
	
	/** random numbers used for positioning "SWT" */
	int randX, randY;
	Image alphaImg1, alphaImg2;

	public AlphaTab(GraphicsExample example) {
		super(example);
	}
	
	public String getCategory() {
		return GraphicsExample.getResourceString("Alpha"); //$NON-NLS-1$
	}

	public String getText() {
		return GraphicsExample.getResourceString("Alpha"); //$NON-NLS-1$
	}
	
	public String getDescription() {
		return GraphicsExample.getResourceString("AlphaDescription"); //$NON-NLS-1$
	}
	
	public int getInitialAnimationTime() {
		return 20;
	}
	
	public void dispose() {
		if (alphaImg1 != null) {
			alphaImg1.dispose();
			alphaImg1 = null;
		}
		if (alphaImg2 != null) {
			alphaImg2.dispose();
			alphaImg2 = null;
		}
	}

	public void next(int width, int height) {
		alphaValue = (alphaValue+5)%255;
		
		alphaValue2 = reachedMax ? alphaValue2 - 5 : alphaValue2 + 5;
			
		if (alphaValue2 == 255) {
			reachedMax = true;
		} else if (alphaValue2 == 0) {
			reachedMax = false;
		}
		
		diameter = (diameter + 10)%(width > height ? width : height);
	}

	/** 
	 * Paint the receiver into the specified GC. 
	 */
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;
		Device device = gc.getDevice();

		if (alphaImg1 == null) {
			alphaImg1 = GraphicsExample.loadImage(device, GraphicsExample.class, "alpha_img1.png");
			alphaImg2 = GraphicsExample.loadImage(device, GraphicsExample.class, "alpha_img2.png");	
		}

		Rectangle rect = alphaImg1.getBounds();
		
		gc.setAlpha(alphaValue);
		gc.drawImage(alphaImg1, rect.x, rect.y, rect.width, rect.height, 
				width/2, height/2, width/4, height/4);
		
		gc.drawImage(alphaImg1, rect.x, rect.y, rect.width, rect.height, 
				0, 0, width/4, height/4);

		gc.setAlpha(255-alphaValue);
		gc.drawImage(alphaImg2, rect.x, rect.y, rect.width, rect.height, 
				width/2, 0, width/4, height/4);
		
		gc.drawImage(alphaImg2, rect.x, rect.y, rect.width, rect.height, 
				0, 3*height/4, width/4, height/4);
		
		// pentagon
		gc.setBackground(device.getSystemColor(SWT.COLOR_DARK_MAGENTA));
		gc.fillPolygon(new int [] {width/10, height/2, 3*width/10, height/2-width/6, 5*width/10, height/2, 
				4*width/10, height/2+width/6, 2*width/10, height/2+width/6});

		gc.setBackground(device.getSystemColor(SWT.COLOR_RED));

		// square
		gc.setAlpha(alphaValue);
		gc.fillRectangle(width/2, height-75, 75, 75);
		
		// triangle
		gc.setAlpha(alphaValue + 15);
		gc.fillPolygon(new int[]{width/2+75, height-(2*75), width/2+75, height-75, width/2+(2*75), height-75});
		
		// triangle
		gc.setAlpha(alphaValue + 30);
		gc.fillPolygon(new int[]{width/2+80, height-(2*75), width/2+(2*75), height-(2*75), width/2+(2*75), height-80});
		
		// triangle
		gc.setAlpha(alphaValue + 45);
		gc.fillPolygon(new int[]{width/2+(2*75), height-(2*75), width/2+(3*75), height-(2*75), width/2+(3*75), height-(3*75)});
		
		// triangle
		gc.setAlpha(alphaValue + 60);
		gc.fillPolygon(new int[]{width/2+(2*75), height-((2*75)+5), width/2+(2*75), height-(3*75), width/2+((3*75)-5), height-(3*75)});
		
		// square
		gc.setAlpha(alphaValue + 75);
		gc.fillRectangle(width/2+(3*75), height-(4*75), 75, 75);
		
		gc.setBackground(device.getSystemColor(SWT.COLOR_GREEN));
		
		// circle in top right corner
		gc.setAlpha(alphaValue2);
		gc.fillOval(width-100, 0, 100, 100);
		
		// triangle
		gc.setAlpha(alphaValue + 90);
		gc.fillPolygon(new int[]{width-300, 10, width-100, 10, width-275, 50});
	
		// triangle
		gc.setAlpha(alphaValue + 105);
		gc.fillPolygon(new int[]{width-10, 100, width-10, 300, width-50, 275});
		
		// quadrilateral shape
		gc.setAlpha(alphaValue + 120);
		gc.fillPolygon(new int[]{width-100, 100, width-200, 150, width-200, 200, width-150, 200});
		
		// blue circles
		gc.setBackground(device.getSystemColor(SWT.COLOR_BLUE));
		int size = 50;
		int alpha = 20;
		for (int i = 0; i < 10; i++) {
			gc.setAlpha(alphaValue + alpha);
			if (i % 2 > 0) {
				gc.fillOval(width-((i+1)*size), height-size, size, size);
			} else {
				gc.fillOval(width-((i+1)*size), height-(3*size/2), size, size);
			}
			alpha = alpha + 20;
		}
		
		// SWT string appearing randomly
		gc.setAlpha(alphaValue2);
		String text = GraphicsExample.getResourceString("SWT");
		Font font = createFont(device, 100, SWT.NONE);
		gc.setFont(font);
		
		Point textSize = gc.stringExtent(text);
		int textWidth = textSize.x;
		int textHeight = textSize.y;
		
		if (alphaValue2 == 0){
			randX = (int)(width*Math.random());
			randY = (int)(height*Math.random());
			randX = (randX > textWidth) ? randX - textWidth : randX;
			randY = (randY > textHeight) ? randY - textHeight : randY;
		}
		
		gc.drawString(text, randX, randY, true);
		font.dispose();
		
		// gray donut
		gc.setAlpha(100);
		Path path = new Path(device);
		path.addArc((width-diameter)/2, (height-diameter)/2, diameter, diameter, 0, 360);
		path.close();
		path.addArc((width-diameter+25)/2, (height-diameter+25)/2, diameter-25, diameter-25, 0, 360);
		path.close();
		gc.setBackground(device.getSystemColor(SWT.COLOR_GRAY));
		gc.fillPath(path);
		gc.drawPath(path);
		path.dispose();
	}
	
	/**
	 * Creates a font using the specified arguments and returns it.
	 * This method takes into account the resident platform.
	 * 
	 * @param face
	 * 			The name of the font
	 * @param points
	 * 			The size of the font in point
	 * @param style
	 * 			The style to be applied to the font
	 */
	static Font createFont(Device device, int points, int style) {		
		if(SWT.getPlatform() == "win32") {
			return new Font(device, "Verdana", points, style);	
		} else if (SWT.getPlatform() == "motif") {
			return new Font(device, "Times", points, style);		
		} else if (SWT.getPlatform() == "gtk") {
			return new Font(device, "Baekmuk Batang", points, style);		
		} else if (SWT.getPlatform() == "carbon") {
			return new Font(device, "Verdana", points, style);
		} else { // photon
			return new Font(device, "Verdana", points, style);
		}
	}
}

