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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This tab demonstrates the re-usage of paths. It allows the user to test with
 * long living Path instances.
 */
public class PathReuseTab extends GraphicsTab {
	Path path;

public PathReuseTab(GraphicsExample example) {
	super(example);
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Path"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("PathReuseOper"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("PathReuseOperDescription"); //$NON-NLS-1$
}

private void resetPath(Device device) {
	if (this.path != null) {
		this.path.dispose();
	}
	this.path = new Path(device);
	example.redraw();
}

@Override
public void createControlPanel(final Composite parent) {
	resetPath(parent.getDisplay());
	// create draw button
	Composite comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(9, false));

	final Button drawArcButton = new Button(comp, SWT.PUSH);
	drawArcButton.setText(GraphicsExample.getResourceString("DrawArc")); //$NON-NLS-1$
	drawArcButton.addListener(SWT.Selection, event -> {
		path.moveTo(100, 100);
		this.path.addArc(100, 100, 250, 250, 90, 180);
		example.redraw();
	});

	final Button drawLineButton = new Button(comp, SWT.PUSH);
	drawLineButton.setText(GraphicsExample.getResourceString("DrawLine")); //$NON-NLS-1$
	drawLineButton.addListener(SWT.Selection, event -> {
		path.moveTo(400, 100);
		path.lineTo(500, 100);
		path.lineTo(400, 200);
		path.lineTo(500, 300);
		path.lineTo(400, 300);
		example.redraw();
	});

	final Button drawRectangleButton = new Button(comp, SWT.PUSH);
	drawRectangleButton.setText(GraphicsExample.getResourceString("DrawRectangle")); //$NON-NLS-1$
	drawRectangleButton.addListener(SWT.Selection, event -> {
		path.addRectangle(600, 100, 100, 200);
		example.redraw();
	});

	final Button drawStringButton = new Button(comp, SWT.PUSH);
	drawStringButton.setText(GraphicsExample.getResourceString("DrawText")); //$NON-NLS-1$
	FontData fd = comp.getFont().getFontData()[0];
	final Font font = new Font(comp.getDisplay(), fd.getName(), 60, SWT.BOLD);
	drawStringButton.addListener(SWT.Selection, event -> {
		path.addString("Text", 800, 100, font);
		example.redraw();
	});

	final Button drawCubicButton = new Button(comp, SWT.PUSH);
	drawCubicButton.setText(GraphicsExample.getResourceString("DrawCubic")); //$NON-NLS-1$
	drawCubicButton.addListener(SWT.Selection, event -> {
		path.moveTo(100, 400);
		path.cubicTo(100, 400, 150, 600, 400, 500);
		example.redraw();
	});

	final Button drawQuadButton = new Button(comp, SWT.PUSH);
	drawQuadButton.setText(GraphicsExample.getResourceString("DrawQuad")); //$NON-NLS-1$
	drawQuadButton.addListener(SWT.Selection, event -> {
		path.moveTo(500, 400);
		path.quadTo(500, 400, 500, 600);
		path.quadTo(600, 500, 700, 600);
		example.redraw();
	});

	final Button addPathButton = new Button(comp, SWT.PUSH);
	addPathButton.setText(GraphicsExample.getResourceString("AddPath")); //$NON-NLS-1$
	addPathButton.addListener(SWT.Selection, event -> {
		Path newPath = new Path(comp.getDisplay());
		newPath.addArc(800, 400, 250, 250, 90, -180);
		path.addPath(newPath);
		example.redraw();
	});

	final Button flattenButton = new Button(comp, SWT.PUSH);
	flattenButton.setText(GraphicsExample.getResourceString("FlattenPath")); //$NON-NLS-1$
	flattenButton.addListener(SWT.Selection, event -> {
		path = new Path(comp.getDisplay(), path, 5);
		example.redraw();
	});

	final Button closeButton = new Button(comp, SWT.PUSH);
	closeButton.setText(GraphicsExample.getResourceString("ClosePath")); //$NON-NLS-1$
	closeButton.addListener(SWT.Selection, event -> {
		path.close();
		example.redraw();
	});

	final Button resetButton = new Button(comp, SWT.PUSH);
	resetButton.setText(GraphicsExample.getResourceString("Reset")); //$NON-NLS-1$
	resetButton.addListener(SWT.Selection, event -> resetPath(parent.getDisplay()));
}

@Override
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	gc.setLineWidth(5);
	gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
	gc.drawPath(path);
}
}


