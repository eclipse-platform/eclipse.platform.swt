/*******************************************************************************
 * Copyright (c) 2025 Yatta and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This tab demonstrates the re-usage of transform. It allows the user to see the
 * differences between filling, drawing and closing paths.
 */
public class TransformReuseTab extends GraphicsTab {
	Transform transform;

public TransformReuseTab(GraphicsExample example) {
	super(example);
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Transform"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("TransformReuseOper"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("TransformReuseOperDescription"); //$NON-NLS-1$
}

private void resetTransform(Device device) {
	if (this.transform != null) {
		this.transform.dispose();
	}
	this.transform = new Transform(device);
	example.redraw();
}

@Override
public void createControlPanel(final Composite parent) {
	resetTransform(parent.getDisplay());
	// create draw button
	Composite comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(6, false));

	final Button rotateButton = new Button(comp, SWT.PUSH);
	rotateButton.setText(GraphicsExample.getResourceString("Rotate")); //$NON-NLS-1$
	rotateButton.addListener(SWT.Selection, event -> {
		this.transform.rotate(45);
		example.redraw();
	});

	final Button scaleButton = new Button(comp, SWT.PUSH);
	scaleButton.setText(GraphicsExample.getResourceString("Scale")); //$NON-NLS-1$
	scaleButton.addListener(SWT.Selection, event -> {
		this.transform.scale(2, 2);
		example.redraw();
	});

	final Button shearButton = new Button(comp, SWT.PUSH);
	shearButton.setText(GraphicsExample.getResourceString("Shear")); //$NON-NLS-1$
	shearButton.addListener(SWT.Selection, event -> {
		this.transform.shear(2, 2);
		example.redraw();
	});

	final Button translateButton = new Button(comp, SWT.PUSH);
	translateButton.setText(GraphicsExample.getResourceString("Translate")); //$NON-NLS-1$
	translateButton.addListener(SWT.Selection, event -> {
		this.transform.translate(100, 50);
		example.redraw();
	});

	final Button identityButton = new Button(comp, SWT.PUSH);
	identityButton.setText(GraphicsExample.getResourceString("Identity")); //$NON-NLS-1$
	identityButton.addListener(SWT.Selection, event -> {
		this.transform.identity();
		example.redraw();
	});

	final Button resetButton = new Button(comp, SWT.PUSH);
	resetButton.setText(GraphicsExample.getResourceString("Reset")); //$NON-NLS-1$
	resetButton.addListener(SWT.Selection, event -> resetTransform(parent.getDisplay()));
}

@Override
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();

	Image image = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_club.jpg");
	gc.setTransform(transform);
	gc.drawImage(image, 0, 0);
	image.dispose();
}
}


