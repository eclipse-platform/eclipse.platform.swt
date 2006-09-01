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
 * This tab presents a spiral consisting of the number of petals specified.
 * */
public class SpiralTab extends AnimatedGraphicsTab {
	
	int angle;					// angle by which to rotate the petals
	Spinner petalSpinner;		// spinner to control number of petals
	Button colorButton;
	GraphicsBackground foreground;
	Menu menu;
	
public SpiralTab(GraphicsExample example) {
	super(example);
	angle = -90;
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}
/**
 * This method creates a spinner for specifying the number of petals. The call to the
 * createControlPanel method in the super class create the controls that are
 * defined in the super class.
 * 
 * @param parent The parent composite 
 */
public void createControlPanel(Composite parent) {
	super.createControlPanel(parent);
		
	// create spinner number of petals
	Composite comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.CENTER).setText(GraphicsExample
			.getResourceString("Petals")); //$NON-NLS-1$
	petalSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	petalSpinner.setSelection(8);
	petalSpinner.setMinimum(3);
	petalSpinner.setMaximum(20);
	petalSpinner.addListener(SWT.Selection, new Listener() {
		public void handleEvent (Event event) {
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
			foreground = gb;		
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});

	// initialize the foreground to the 2nd item in the menu
	foreground = (GraphicsBackground)menu.getItem(1).getData();
	
	// color button
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample
			.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(foreground.getThumbNail());
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

public String getCategory() {
	return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Spiral"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("SpiralDescription"); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.AnimatedGraphicsTab#getAnimationTime()
 */
public int getInitialAnimationTime() {
	return 150;	
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.AnimatedGraphicsTab#next(int, int)
 */
public void next(int width, int height) {
	if (angle == 270) {
		angle = -90;
	}
	angle += 10;
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	// set line attributes
	gc.setLineWidth(20);
	gc.setLineCap(SWT.CAP_ROUND);	// round line ends
	gc.setAntialias(SWT.ON);	// smooth jagged edges
	
	Pattern pattern = null;
	if (foreground.getBgColor1() != null) {
		gc.setForeground(foreground.getBgColor1());
	} else if (foreground.getBgImage() != null) {
		pattern = new Pattern(device, foreground.getBgImage());
		gc.setForegroundPattern(pattern);
	}

	// draw petals for the spiral
	Transform transform;
	int n = petalSpinner.getSelection();
	for (int i=0; i < n; i++) {
		transform = new Transform(device);
		transform.translate(width/2, height/2);
		transform.rotate(-(angle + 360/n * i));
		gc.setTransform(transform);
		gc.drawArc(0, 0, width/3, height/6, 0, 180);
		transform.dispose();
	}
	
	if (pattern != null) pattern.dispose();
}
}
