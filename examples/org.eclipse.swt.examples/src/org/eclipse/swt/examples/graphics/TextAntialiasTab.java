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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;

/**
 * This tab demonstrates antialiasing for text. Antialiasing is used for
 * smoothing jagged edges in graphics. This tab allows the user to see the
 * effects of different antialiasing values.
 */
public class TextAntialiasTab extends GraphicsTab {

	Combo aliasCombo;
	static int[] aliasValues = { SWT.OFF, SWT.DEFAULT, SWT.ON };

	Button colorButton;
	Menu menu;
	GraphicsBackground textColor;
	String text = GraphicsExample.getResourceString("SWT");


public TextAntialiasTab(GraphicsExample example) {
	super(example);
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Antialiasing"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("Text"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("AntialiasingTextDesc"); //$NON-NLS-1$
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
	aliasCombo.addListener(SWT.Selection, event -> example.redraw());

	ColorMenu cm = new ColorMenu();
	cm.setColorItems(true);
	menu = cm.createMenu(parent.getParent(), gb -> {
		textColor = gb;
		colorButton.setImage(gb.getThumbNail());
		example.redraw();
	});

	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());

	// initialize the color to black
	textColor = (GraphicsBackground)menu.getItem(1).getData();

	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(textColor.getThumbNail());

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
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();

	if (textColor != null && textColor.getBgColor1() != null)
		gc.setForeground(textColor.getBgColor1());

	gc.setTextAntialias(aliasValues[aliasCombo.getSelectionIndex()]);

	// column 1, row 1
	Font font = new Font(device, getPlatformFontFace(0), 100, SWT.NORMAL);
	gc.setFont(font);
	Point size = gc.stringExtent(text);
	gc.drawString(text, width/4 - size.x/2, height/4 - size.y/2, true);
	font.dispose();

	// column 1, row 2
	font = new Font(device, getPlatformFontFace(1), 100, SWT.NORMAL);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, width/4 - size.x/2, 3*height/4 - size.y/2, true);
	font.dispose();

	// column 2, row 1
	font = new Font(device, getPlatformFontFace(2), 50, SWT.NORMAL);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 0, true);
	font.dispose();

	// column 2, row 2
	font = new Font(device, getPlatformFontFace(3), 100, SWT.ITALIC);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, (height-size.y)/2, true);
	font.dispose();

	// column 2, row 3
	font = new Font(device, getPlatformFontFace(4), 50, SWT.NORMAL);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, height-size.y, true);
	font.dispose();

	// column 3, row 1
	font = new Font(device, getPlatformFontFace(5), 100, SWT.NORMAL);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, 3*width/4 - size.x/2, height/4 - size.y/2, true);
	font.dispose();

	// column 3, row 2
	font = new Font(device, getPlatformFontFace(6), 100, SWT.NORMAL);
	gc.setFont(font);
	size = gc.stringExtent(text);
	gc.drawString(text, 3*width/4 - size.x/2, 3*height/4 - size.y/2, true);
	font.dispose();
}

/**
 * Returns the name of a valid font for the host platform.
 *
 * @param index
 *            index is used to determine the appropriate font face
 */
static String getPlatformFontFace(int index) {
	if(SWT.getPlatform() == "win32") {
		return new String [] {"Bookman Old Style", "Century Gothic", "Comic Sans MS", "Impact", "Garamond", "Lucida Console", "Monotype Corsiva"} [index];
	} else if (SWT.getPlatform() == "gtk") {
		return new String [] {"Luxi Mono", "KacstTitleL", "Baekmuk Batang", "Baekmuk Headline", "KacstFarsi", "Baekmuk Gulim", "URW Chancery L"} [index];
	} else {
		return new String [] {"Courier", "Verdana", "Verdana", "Verdana", "Verdana", "Verdana", "Verdana"} [index];
	}
}
}
