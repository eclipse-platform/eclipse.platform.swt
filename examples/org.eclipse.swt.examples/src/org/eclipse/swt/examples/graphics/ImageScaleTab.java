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

import org.eclipse.swt.graphics.*;

/**
 * This tab demonstrates how an image can be scaled. 
 */
public class ImageScaleTab extends GraphicsTab {

public ImageScaleTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Image"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Scale"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("ScaleDescription"); //$NON-NLS-1$
}

public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();
	Image image = GraphicsExample.loadImage(device, GraphicsExample.class, "houses.png");
	
	Rectangle bounds = image.getBounds();
	Rectangle canvasBounds = example.canvas.getBounds();
	gc.drawImage(image, 0, 0, bounds.width, bounds.height, 0, 0, canvasBounds.width, canvasBounds.height);
	
	image.dispose();
}
}

