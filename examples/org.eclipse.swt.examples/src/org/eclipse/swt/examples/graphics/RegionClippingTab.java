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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab demonstrates how to apply a region clipping and the effects of
 * applying one.
 */
public class RegionClippingTab extends GraphicsTab {
	
	private Combo clippingCb;
	private Button colorButton1, colorButton2;
	private Menu menu1, menu2;
	private GraphicsBackground colorGB1, colorGB2;

public RegionClippingTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Clipping"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("RegionClipping"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("RegionClippingDescription"); //$NON-NLS-1$
}

public void dispose() {
	if (menu1 != null) {
		menu1.dispose();
		menu1 = null;
	}
	if (menu2 != null) {
		menu2.dispose();
		menu2 = null;
	}
}

/** 
 * Creates the widgets used to control the drawing.
 */
public void createControlPanel(Composite parent) {
	// create drop down combo for choosing clipping
	Composite comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.CENTER).setText(GraphicsExample
			.getResourceString("Clipping")); //$NON-NLS-1$
	clippingCb = new Combo(comp, SWT.DROP_DOWN);
	clippingCb.add(GraphicsExample.getResourceString("Region1")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Region2")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Add")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Sub")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Inter")); //$NON-NLS-1$
	clippingCb.select(0);
	clippingCb.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
				example.redraw();
		}
	});

	// color menu
	ColorMenu cm = new ColorMenu();
	menu1 = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			colorGB1 = gb;		
			colorButton1.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	menu2 = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			colorGB2 = gb;		
			colorButton2.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	
	// initialize the color to blue
	colorGB1 = (GraphicsBackground)menu1.getItem(4).getData();
	// initialize the color to red
	colorGB2 = (GraphicsBackground)menu2.getItem(2).getData();
	
	// color button 1
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	colorButton1 = new Button(comp, SWT.PUSH);
	colorButton1.setText(GraphicsExample
			.getResourceString("Color1")); //$NON-NLS-1$
	colorButton1.setImage(colorGB1.getThumbNail());
	colorButton1.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			final Button button = (Button) event.widget;
			final Composite parent = button.getParent(); 
			Rectangle bounds = button.getBounds();
			Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
			menu1.setLocation(point.x, point.y + bounds.height);
			menu1.setVisible(true);
		}
	});

	// color button 2
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	colorButton2 = new Button(comp, SWT.PUSH);
	colorButton2.setText(GraphicsExample
			.getResourceString("Color2")); //$NON-NLS-1$
	colorButton2.setImage(colorGB2.getThumbNail());
	colorButton2.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			final Button button = (Button) event.widget;
			final Composite parent = button.getParent(); 
			Rectangle bounds = button.getBounds();
			Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
			menu2.setLocation(point.x, point.y + bounds.height);
			menu2.setVisible(true);
		}
	});
}

public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	// array of coordinate points of polygon 1 (region 1)
	int [] polygon1 = new int [] {10, height/2, 9*width/16, 10, 9*width/16, height-10};
	Region region1 = new Region(device);
	region1.add(polygon1);

	// array of coordinate points of polygon 2 (region 2)
	int [] polygon2 = new int [] {
			9*width/16, 10,
			9*width/16, height/8,
			7*width/16, 2*height/8,
			9*width/16, 3*height/8,
			7*width/16, 4*height/8,
			9*width/16, 5*height/8,
			7*width/16, 6*height/8,
			9*width/16, 7*height/8,
			9*width/16, height-10,
			width-10, height/2
	};
	Region region2 = new Region(device);
	region2.add(polygon2);
	
	gc.setAlpha(127);
	
	int clippingIndex = clippingCb.getSelectionIndex();
	
	switch (clippingIndex) {
	case 0:
		// region 1
		gc.setClipping(region1);
		gc.setBackground(colorGB1.getBgColor1());
		gc.fillPolygon(polygon1);
		break;
	case 1:
		// region 2
		gc.setClipping(region2);
		gc.setBackground(colorGB2.getBgColor1());
		gc.fillPolygon(polygon2);
		break;
	case 2:
		// add		
		region1.add(region2);
		break;
	case 3:
		// sub
		region1.subtract(region2);
		break;
	case 4:
		// intersect
		region1.intersect(region2);
		break;		
	}
	
	if (clippingIndex > 1) {
		gc.setClipping(region1);
		
		gc.setBackground(colorGB1.getBgColor1());
		gc.fillPolygon(polygon1);
		
		gc.setBackground(colorGB2.getBgColor1());
	    gc.fillPolygon(polygon2);
	}
	
	region1.dispose();
	region2.dispose();
    }
}
