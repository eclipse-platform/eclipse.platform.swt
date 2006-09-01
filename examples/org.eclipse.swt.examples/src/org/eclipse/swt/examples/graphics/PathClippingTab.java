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
 * This tab demonstrates how to apply a path clipping and the effects of
 * applying one.
 */
public class PathClippingTab extends GraphicsTab {
	
	private Combo clippingCb;
	private Button colorButton;
	private GraphicsBackground background;
	private Menu menu;

public PathClippingTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Clipping"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("PathClipping"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("PathClippingDesc"); //$NON-NLS-1$
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
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
	clippingCb.add(GraphicsExample.getResourceString("Circles")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Rectangle")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Oval")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Word")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Star")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Triangles")); //$NON-NLS-1$
	clippingCb.add(GraphicsExample.getResourceString("Default")); //$NON-NLS-1$
	clippingCb.select(0);
	clippingCb.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
				example.redraw();
		}
	});
	
	// color menu
	ColorMenu cm = new ColorMenu();
	cm.setPatternItems(example.checkAdvancedGraphics());
	menu = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			background = gb;		
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});

	// initialize the background to the 5th item in the menu (blue)
	background =(GraphicsBackground)menu.getItem(4).getData();
	
	// color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample
			.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(background.getThumbNail());
	colorButton.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			final Button button = (Button) event.widget;
			final Composite parent = button.getParent(); 
			Rectangle bounds = button.getBounds();
			Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
			menu.setLocation(point.x, point.y + bounds.height);
			menu.setVisible(true);
		}
	});
}
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();

	int clipping = clippingCb.getSelectionIndex();
	switch (clipping) {
	
	case 0:		// circles
		Path path = new Path(device);
		path.addArc((width-width/5)/2, (height-height/5)/2, width/5, height/5, 0, 360);
		path.addArc(5*(width-width/8)/12, 4*(height-height/8)/12, width/8, height/8, 0, 360);
		path.addArc(7*(width-width/8)/12, 8*(height-height/8)/12, width/8, height/8, 0, 360);
		path.addArc(6*(width-width/12)/12, 3*(height-height/12)/12, width/12, height/12, 0, 360);
		path.addArc(6*(width-width/12)/12, 9*(height-height/12)/12, width/12, height/12, 0, 360);
		path.addArc(11.5f*(width-width/18)/20, 5*(height-height/18)/18, width/18, height/18, 0, 360);
		path.addArc(8.5f*(width-width/18)/20, 13*(height-height/18)/18, width/18, height/18, 0, 360);
		gc.setClipping(path);
		path.dispose();
		break;
	case 1:		// rectangle
		path = new Path(device);
		path.addRectangle(100, 100, width-200, height-200);
		path.addRectangle(120, 120, width-240, height-240);
		path.addRectangle(140, 140, width-280, height-280);
		gc.setClipping(path);
		path.dispose();
		break;
	case 2:		// circle
		path = new Path(device);
		path.addArc(100, 100, width-200, height-200, 0, 360);
		path.addArc((width-width/5)/2, (height-height/5)/2, width/5, height/5, 0, 360);
		path.addArc(5*(width-width/8)/12, 4*(height-height/8)/12, width/8, height/8, 0, 360);
		path.addArc(7*(width-width/8)/12, 8*(height-height/8)/12, width/8, height/8, 0, 360);
		path.addArc(6*(width-width/12)/12, 3*(height-height/12)/12, width/12, height/12, 0, 360);
		path.addArc(6*(width-width/12)/12, 9*(height-height/12)/12, width/12, height/12, 0, 360);
		path.addArc(11.5f*(width-width/18)/20, 5*(height-height/18)/18, width/18, height/18, 0, 360);
		path.addArc(8.5f*(width-width/18)/20, 13*(height-height/18)/18, width/18, height/18, 0, 360);
		gc.setClipping(path);
		path.dispose();
		break;
	case 3:		// word
		path = new Path(device);
		String text = GraphicsExample.getResourceString("SWT"); //$NON-NLS-1$
		Font font = new Font(device, "Times", 200, SWT.NORMAL);
		gc.setFont(font);
		Point size = gc.stringExtent(text);
		path.addString(text, (width-size.x)/2, (height-size.y)/2, font);
		font.dispose();
		gc.setClipping(path);
		path.dispose();
		break;
	case 4:		// star
		path = new Path(device);
		path.lineTo(width/2, 0);
		path.lineTo(5*width/8, height/3);
		path.lineTo(width, height/3);
		path.lineTo(3*width/4, 10*height/16);
		path.lineTo(7*width/8, height);
		path.lineTo(width/2, 3*height/4);
		path.lineTo(width/8, height);
		path.lineTo(width/4, 10*height/16);
		path.lineTo(0, height/3);
		path.lineTo(3*width/8, height/3);
		path.lineTo(width/2, 0);
		
		Path ovalPath = new Path(device);
		ovalPath.addArc(90, 90, width-180, height-180, 0, 360);
		
		path.addPath(ovalPath);
		gc.setClipping(path);
		ovalPath.dispose();
		path.dispose();
		break;
	case 5:		// triangles
		path = new Path(device);
		path.addRectangle(0, 0, width, height);
		path.lineTo(width/4, 0);
		path.lineTo(width/4, height/2);
		path.lineTo(0, height/2);
		path.lineTo(width/4, 0);
		
		Path path2 = new Path(device);
		path2.lineTo(width/2, 0);
		path2.lineTo(width/4, height/2);
		path2.lineTo(3*width/4, height/2);
		path2.lineTo(width/2, 0);
		
		Path path3 = new Path(device);
		path3.lineTo(3*width/4, 0);
		path3.lineTo(3*width/4, height/2);
		path3.lineTo(width, height/2);
		path3.lineTo(3*width/4, 0);
		
		Path path4 = new Path(device);
		path4.lineTo(0, height);
		path4.lineTo(width/4, height/2);
		path4.lineTo(width/2, height);
		path4.lineTo(0, height);
		
		Path path5 = new Path(device);
		path5.lineTo(width/2, height);
		path5.lineTo(3*width/4, height/2);
		path5.lineTo(width, height);
		path5.lineTo(width/2, height);
		
		path.addPath(path2);
		path.addPath(path3);
		path.addPath(path4);
		path.addPath(path5);
		gc.setClipping(path);
		
		path5.dispose();
		path4.dispose();
		path3.dispose();
		path2.dispose();
		path.dispose();
		break;
	case 6:		// default
		gc.setClipping(0, 0, width, height);
		break;
	}
	
	Pattern pattern = null;
	if (background.getBgColor1() != null) {
		gc.setBackground(background.getBgColor1());
	} else if (background.getBgImage() != null) {
		pattern = new Pattern(device, background.getBgImage());
		gc.setBackgroundPattern(pattern);
	}
    gc.fillRectangle(0, 0, width, height);
    if (pattern != null) pattern.dispose();
}
}
