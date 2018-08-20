/*******************************************************************************
 * Copyright (c) 2006, 2013 IBM Corporation and others.
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

import org.eclipse.swt.graphics.*;

/**
 * This tab demonstrates how an image can be scaled.
 */
public class ImageScaleTab extends GraphicsTab {

public ImageScaleTab(GraphicsExample example) {
	super(example);
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Image"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("Scale"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("ScaleDescription"); //$NON-NLS-1$
}

@Override
public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();
	Image image = GraphicsExample.loadImage(device, GraphicsExample.class, "houses.png");

	Rectangle bounds = image.getBounds();
	Rectangle canvasBounds = example.canvas.getBounds();
	gc.drawImage(image, 0, 0, bounds.width, bounds.height, 0, 0, canvasBounds.width, canvasBounds.height);

	image.dispose();
}
}

