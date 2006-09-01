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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab draws an image consisting of gradients of two colors.
 * */
public class GradientTab extends GraphicsTab {
	
	ToolBar toolBar;
	ToolItem colorItem1, colorItem2;
	Menu menu1, menu2;
	GraphicsBackground colorGB1, colorGB2;
	ArrayList resources;		// resources to be disposed when the dipose() method is invoked
	

public GradientTab(GraphicsExample example) {
	super(example);
	resources = new ArrayList();
}

/**
 * Dispose resources created by this tab.
 * */
public void dispose() {
	for (int i = 0; i < resources.size(); i++) {
		Object obj = resources.get(i);
		if (obj != null && obj instanceof Resource) 
			((Resource) obj).dispose();
	}
	resources = new ArrayList();
	
	if (menu1 != null) {
		menu1.dispose();
		menu1 = null;
	}
	if (menu2 != null) {
		menu2.dispose();
		menu2 = null;
	}
}

public String getCategory() {
	return GraphicsExample.getResourceString("Gradient"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("GradImage"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("GradientImageDescription"); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#createControlPanel(org.eclipse.swt.widgets.Composite)
 */
public void createControlPanel(final Composite parent) {
	final Display display = parent.getDisplay();
	
	toolBar = new ToolBar(parent, SWT.FLAT);
	
	ColorMenu colorMenu = new ColorMenu();

	// menu for colorItem1
	menu1 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			colorGB1 = gb;
			colorItem1.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	
	// initialize the background to the 5th item in the menu (blue)
	colorGB1 = (GraphicsBackground)menu1.getItem(4).getData();
	
	// toolbar item for color1
	colorItem1 = new ToolItem(toolBar, SWT.PUSH);
	colorItem1.setText(GraphicsExample.getResourceString("GradientTabItem1"));
	colorItem1.setImage(colorGB1.getThumbNail());
	colorItem1.addListener(SWT.Selection, new Listener(){
		public void handleEvent(Event event) {
			final ToolItem toolItem = (ToolItem) event.widget;
			final ToolBar  toolBar = toolItem.getParent();
			Rectangle toolItemBounds = toolItem.getBounds();
			Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
			menu1.setLocation(point.x, point.y + toolItemBounds.height);
			menu1.setVisible(true);
		}
	});
	
	// menu for colorItem2
	menu2 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			colorGB2 = gb;
			colorItem2.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	
	// initialize the background to the 3rd item in the menu (red)
	colorGB2 = (GraphicsBackground)menu2.getItem(2).getData();
	
	// toolbar item for color2
	colorItem2 = new ToolItem(toolBar, SWT.PUSH);
	colorItem2.setText(GraphicsExample.getResourceString("GradientTabItem2"));
	colorItem2.setImage(colorGB2.getThumbNail());
	colorItem2.addListener(SWT.Selection, new Listener(){
		public void handleEvent(Event event) {
			final ToolItem toolItem = (ToolItem) event.widget;
			final ToolBar  toolBar = toolItem.getParent();
			Rectangle toolItemBounds = toolItem.getBounds();
			Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
			menu2.setLocation(point.x, point.y + toolItemBounds.height);
			menu2.setVisible(true);
		}
	});
	
	// toolbar item for swapping colors
	ToolItem swapItem = new ToolItem(toolBar, SWT.PUSH);
	swapItem.setText(GraphicsExample.getResourceString("SwapColors")); //$NON-NLS-1$
	swapItem.setImage(example.loadImage(display, "swap.gif"));
	swapItem.addListener(SWT.Selection, new Listener(){
		public void handleEvent(Event event) {
			GraphicsBackground tmp = colorGB1;
			colorGB1 = colorGB2;
			colorGB2 = tmp;
			colorItem1.setImage(colorGB1.getThumbNail());
			colorItem2.setImage(colorGB2.getThumbNail());
			example.redraw();
		}
	});
}

/**
 * This method draws the gradient patterns that make up the image. The image
 * consists of 4 rows, each consisting of 4 gradient patterns (total of 16).
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	Image image = createImage(device, colorGB1.getBgColor1(), colorGB2.getBgColor1(), width, height);
	Pattern p = new Pattern(device, image);
	gc.setBackgroundPattern(p);
	gc.fillRectangle(0, 0, width, height);

	p.dispose();
	image.dispose();
}


/**
 * Creates and returns an image made up of gradient patterns. The image takes up
 * a quarter of the area of the total drawing surface.
 * 
 * @param device
 *            A Device
 * @param color1
 *            A Color
 * @param color2
 *            A Color
 * @param width
 *            Width of the drawing surface
 * @param height
 *            Height of the drawing surface
 */
Image createImage(Device device, Color color1, Color color2, int width, int height) {
	Image image = new Image(device, width/2, height/2);
	GC gc = new GC(image);
	Rectangle rect = image.getBounds();
	
	Pattern pattern1 = new Pattern(device, rect.x, rect.y, rect.width/2f, rect.height/2f, color1, color2);
	gc.setBackgroundPattern(pattern1);	
	Path path = new Path(device);
	path.addRectangle(0, 0, width/4f, height/4f);
	path.addRectangle(width/4f, height/4f, width/4f, height/4f);
	gc.fillPath(path);
	path.dispose();
	
	Pattern pattern2 = new Pattern(device, rect.width, 0, rect.width/2f, rect.height/2f, color1, color2);
	gc.setBackgroundPattern(pattern2);
	path = new Path(device);
	path.addRectangle(width/4f, 0, width/4f, height/4f);
	path.addRectangle(0, height/4f, width/4f, height/4f);
	gc.fillPath(path);
	path.dispose();	

	gc.dispose();
	pattern1.dispose();
	pattern2.dispose();
	return image;
}

}
