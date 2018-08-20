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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Spinner;

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

@Override
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
@Override
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
	petalSpinner.addListener(SWT.Selection, event -> example.redraw());

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

	// initialize the foreground to the 2nd item in the menu
	foreground = (GraphicsBackground)menu.getItem(1).getData();

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
public String getCategory() {
	return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("Spiral"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("SpiralDescription"); //$NON-NLS-1$
}

@Override
public int getInitialAnimationTime() {
	return 150;
}

@Override
public void next(int width, int height) {
	if (angle == 270) {
		angle = -90;
	}
	angle += 10;
}

@Override
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
