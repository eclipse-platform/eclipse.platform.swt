/*******************************************************************************
 * Copyright (c) 2006, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

/**
 * This tab demonstrates various line caps applicable to a line.
 */
public class LineCapTab extends GraphicsTab {

	Button colorButton;
	GraphicsBackground foreground;
	Menu menu;

public LineCapTab(GraphicsExample example) {
	super(example);
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Lines"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("LineCap"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("LineCapDescription"); //$NON-NLS-1$
}

@Override
public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}

@Override
public void createControlPanel(Composite parent) {

	Composite comp;

	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());

	ColorMenu cm = new ColorMenu();
	cm.setPatternItems(example.checkAdvancedGraphics());
	menu = cm.createMenu(parent.getParent(), gb -> {
		foreground = gb;
		colorButton.setImage(gb.getThumbNail());
		example.redraw();
	});

	// initialize the foreground to the 3rd item in the menu (red)
	foreground = (GraphicsBackground)menu.getItem(2).getData();

	// color button
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample
			.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(foreground.getThumbNail());
	colorButton.addListener(SWT.Selection, event -> {
		final Button button = (Button) event.widget;
		final Composite parent1 = button.getParent();
		Rectangle bounds = button.getBounds();
		Point point = parent1.toDisplay(new Point(bounds.x, bounds.y));
		menu.setLocation(point.x, point.y + bounds.height);
		menu.setVisible(true);
	});
}

@Override
public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();

	// draw side lines
	gc.setLineWidth(1);
	gc.setLineStyle(SWT.LINE_DOT);
	gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
	gc.drawLine(3*width/16, height/6, 3*width/16, 5*height/6);
	gc.drawLine(13*width/16, height/6, 13*width/16, 5*height/6);
	gc.setLineStyle(SWT.LINE_SOLID);

	// draw labels
	Font font = new Font(device, getPlatformFont(), 20, SWT.NORMAL);
	gc.setFont(font);

	String text = GraphicsExample.getResourceString("Flat"); //$NON-NLS-1$
	Point size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 3*height/12, true);
	text = GraphicsExample.getResourceString("Square"); //$NON-NLS-1$
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 5*height/12, true);
	text = GraphicsExample.getResourceString("Round"); //$NON-NLS-1$
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 7*height/12, true);
	font.dispose();

	Pattern pattern = null;
	if (foreground.getBgColor1() != null) {
		gc.setForeground(foreground.getBgColor1());
	} else if (foreground.getBgImage() != null) {
		pattern = new Pattern(device, foreground.getBgImage());
		gc.setForegroundPattern(pattern);
	}

	// draw lines with caps
	gc.setLineWidth(20);
	gc.setLineCap(SWT.CAP_FLAT);
	gc.drawLine(3*width/16, 2*height/6, 13*width/16, 2*height/6);
	gc.setLineCap(SWT.CAP_SQUARE);
	gc.drawLine(3*width/16, 3*height/6, 13*width/16, 3*height/6);
	gc.setLineCap(SWT.CAP_ROUND);
	gc.drawLine(3*width/16, 4*height/6, 13*width/16, 4*height/6);

	if (pattern != null) pattern.dispose();
}

/**
 * Returns the name of a valid font for the resident platform.
 */
static String getPlatformFont() {
	if(SWT.getPlatform() == "win32") {
		return "Arial";
	} else if (SWT.getPlatform() == "gtk") {
		return "Baekmuk Batang";
	} else {
		return "Verdana";
	}
}
}
