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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;

/**
 * This tab demonstrates various text fonts. It allows the user to specify font
 * parameters such as face, style and size.
 */
public class CustomFontTab extends GraphicsTab {

	String text = GraphicsExample.getResourceString("SWT");
	GraphicsBackground fontForeground;
	Combo fontFaceCb, fontStyleCb;
	Spinner fontPointSpinner;
	Button colorButton;
	List<String> fontNames;
	int [] styleValues;
	String [] fontStyles;
	Menu menu;

public CustomFontTab(GraphicsExample example) {
	super(example);

	// create list of fonts for this platform
	FontData [] fontData = Display.getCurrent().getFontList(null, true);
	fontNames = new ArrayList<>();
	for (FontData element : fontData) {
		// remove duplicates and sort
		String nextName = element.getName();
		if (!fontNames.contains(nextName)) {
			int j = 0;
			while(j < fontNames.size() && nextName.compareTo(fontNames.get(j)) > 0) {
				j++;
			}
			fontNames.add(j, nextName);
		}
	}
	fontStyles = new String [] {
			GraphicsExample.getResourceString("Regular"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Italic"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Bold"), //$NON-NLS-1$
			GraphicsExample.getResourceString("BoldItalic") //$NON-NLS-1$
	};
	styleValues = new int [] {SWT.NORMAL, SWT.ITALIC, SWT.BOLD, SWT.BOLD | SWT.ITALIC};
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Font"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("CustomFont"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("CustomFontDescription"); //$NON-NLS-1$
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

	Composite mainComp = new Composite(parent, SWT.NONE);
	mainComp.setLayout(new RowLayout());

	// create combo for font face
	Composite comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));

	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontFace")); //$NON-NLS-1$
	fontFaceCb = new Combo(comp, SWT.DROP_DOWN);
	for (String name : fontNames) {
		fontFaceCb.add(name);
	}
	fontFaceCb.select(0);
	fontFaceCb.addListener(SWT.Selection, event -> example.redraw());

	// create combo for font style
	comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));

	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontStyle")); //$NON-NLS-1$
	fontStyleCb = new Combo(comp, SWT.DROP_DOWN);
	for (String fontStyle : fontStyles) {
		fontStyleCb.add(fontStyle);
	}
	fontStyleCb.select(0);
	fontStyleCb.addListener(SWT.Selection, event -> example.redraw());

	// create spinner for font size (points)
	comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));

	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontSize")); //$NON-NLS-1$
	fontPointSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	fontPointSpinner.setMinimum(1);
	fontPointSpinner.setMaximum(1000);
	fontPointSpinner.setSelection(200);
	fontPointSpinner.addListener(SWT.Selection, event -> example.redraw());

	ColorMenu cm = new ColorMenu();
	cm.setColorItems(true);
	cm.setPatternItems(example.checkAdvancedGraphics());
	menu = cm.createMenu(parent.getParent(), gb -> {
		fontForeground = gb;
		colorButton.setImage(gb.getThumbNail());
		example.redraw();
	});

	// initialize the background to the 2nd item in the menu (black)
	fontForeground = (GraphicsBackground)menu.getItem(1).getData();

	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());

	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(fontForeground.getThumbNail());
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

	String fontFace = fontNames.get(fontFaceCb.getSelectionIndex());
	int points = fontPointSpinner.getSelection();
	int style = styleValues[fontStyleCb.getSelectionIndex()];

	Font font = new Font(device, fontFace, points, style);
	gc.setFont(font);
	gc.setTextAntialias(SWT.ON);

	Point size = gc.stringExtent(text);
	int textWidth = size.x;
	int textHeight = size.y;

	Pattern pattern = null;
	if (fontForeground.getBgColor1() != null) {
		gc.setForeground(fontForeground.getBgColor1());
	} else if (fontForeground.getBgImage() != null) {
		pattern = new Pattern(device, fontForeground.getBgImage());
		gc.setForegroundPattern(pattern);
	}

	gc.drawString(text, (width-textWidth)/2, (height-textHeight)/2, true);

	font.dispose();
	if (pattern != null) pattern.dispose();
}

}

