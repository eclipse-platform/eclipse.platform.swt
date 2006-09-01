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
 * This tab demonstrates the use of paths. It allows the user to see the
 * differences between filling, drawing and closing paths.
 */
public class PathTab extends GraphicsTab {

	Button colorButton, fillButton, drawButton, closeButton;
	GraphicsBackground fillColor;
	Menu menu;

public PathTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Path"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("PathOper"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("PathOperDescription"); //$NON-NLS-1$
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}

public void createControlPanel(Composite parent) {
	
	Composite comp;

	// create draw button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
	drawButton = new Button(comp, SWT.TOGGLE);
	drawButton.setText(GraphicsExample.getResourceString("DrawPath")); //$NON-NLS-1$
	drawButton.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			example.redraw();
		}
	});
	drawButton.setSelection(true);
	
	// create fill button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
	fillButton = new Button(comp, SWT.TOGGLE);
	fillButton.setText(GraphicsExample.getResourceString("FillPath")); //$NON-NLS-1$
	fillButton.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			example.redraw();
		}
	});
	
	// create close button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
	closeButton = new Button(comp, SWT.TOGGLE);
	closeButton.setText(GraphicsExample.getResourceString("ClosePath")); //$NON-NLS-1$
	closeButton.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			example.redraw();
		}
	});
		
	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
	ColorMenu cm = new ColorMenu();
	cm.setPatternItems(example.checkAdvancedGraphics());
	menu = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			fillColor = gb;		
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});

	// initialize the foreground to the 5th item in the menu (green)
	fillColor = (GraphicsBackground)menu.getItem(3).getData();
	
	// color button
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample.getResourceString("FillColor")); //$NON-NLS-1$
	colorButton.setImage(fillColor.getThumbNail());
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

	Pattern pattern = null;
	if (fillColor.getBgColor1() != null) {
		gc.setBackground(fillColor.getBgColor1());
	} else if (fillColor.getBgImage() != null) {
		pattern = new Pattern(device, fillColor.getBgImage());
		gc.setBackgroundPattern(pattern);
	}

	gc.setLineWidth(5);
	gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
	
	// arc
	Path path = new Path(device);
	path.addArc((width-250)/2, (height-400)/2, 500, 400, 90, 180);
	if (closeButton.getSelection()) path.close();
	if (fillButton.getSelection()) gc.fillPath(path);
	if (drawButton.getSelection()) gc.drawPath(path);
	path.dispose();
	
	// shape on left
	Transform transform = new Transform(device);
	transform.translate((width-250)/4, height/2-150);
	gc.setTransform(transform);
	transform.dispose();
	path = new Path(device);
	path.cubicTo(-150, 100, 150, 200, 0, 300);
	if (closeButton.getSelection()) path.close();
	if (fillButton.getSelection()) gc.fillPath(path);
	if (drawButton.getSelection()) gc.drawPath(path);
	path.dispose();
	gc.setTransform(null);
	
	// shape on right
	path = new Path(device);
	path.moveTo(3*(width-250)/4 - 25 + 250, height/2);
	path.lineTo(3*(width-250)/4 + 50 + 250, height/2 - 200);
	path.lineTo(3*(width-250)/4 + 50 + 250, height/2 + 50);
	path.lineTo(3*(width-250)/4 - 25 + 250, height/2 + 150);
	path.lineTo(3*(width-250)/4 + 25 + 250, height/2 + 50);
	if (closeButton.getSelection()) path.close();
	if (fillButton.getSelection()) gc.fillPath(path);
	if (drawButton.getSelection()) gc.drawPath(path);
	path.dispose();

	if (pattern != null) pattern.dispose();
}
}


