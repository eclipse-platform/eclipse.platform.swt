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
 * This tab demonstrates antialiasing for graphics. Antialiasing is used for
 * smoothing jagged edges in graphics. This tab allows the user to see the
 * effects of different antialiasing values.
 */
public class GraphicAntialiasTab extends GraphicsTab {

	Combo aliasCombo;
	int[] aliasValues = { SWT.OFF, SWT.DEFAULT, SWT.ON };
	
	Button colorButton;
	Menu menu;
	GraphicsBackground ovalColorGB;

	
public GraphicAntialiasTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Antialiasing"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Graphics"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("AntialiasingGraphicsDesc"); //$NON-NLS-1$
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}

public void createControlPanel(Composite parent) {

	Composite comp;
	
	// create drop down combo for antialiasing
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	new Label(comp, SWT.CENTER).setText(GraphicsExample
			.getResourceString("Antialiasing")); //$NON-NLS-1$
	aliasCombo = new Combo(comp, SWT.DROP_DOWN);
	aliasCombo.add("OFF");
	aliasCombo.add("DEFAULT");
	aliasCombo.add("ON");
	aliasCombo.select(0);
	aliasCombo.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
				example.redraw();
		}
	});

	ColorMenu cm = new ColorMenu();
	cm.setColorItems(true);
	menu = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			ovalColorGB = gb;
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	
	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
    // initialize the background to the 5th item in the menu (blue)
	ovalColorGB = (GraphicsBackground)menu.getItem(4).getData();
	
	// color button
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(ovalColorGB.getThumbNail());
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

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	if (ovalColorGB != null && ovalColorGB.getBgColor1() != null)
		gc.setBackground(ovalColorGB.getBgColor1());
	
	gc.setAntialias(aliasValues[aliasCombo.getSelectionIndex()]);

	Path path = new Path(device);
	float offsetX = 2*width/3f, offsetY = height/3f;
	for(int i=0; i < 25; i++) {
		path.addArc(offsetX-(50*i), offsetY-(25*i), 50+(100*i), 25+(50*i), 0, 360);
	}
	gc.fillPath(path);
	path.dispose();
}
}

