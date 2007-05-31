/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class LineTab extends GraphicsTab {

public LineTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Lines"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Line"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("LineDescription"); //$NON-NLS-1$
}

public void paint(GC gc, int width, int height) {
	gc.drawLine(0, 0, width, height);
	gc.drawLine(width, 0, 0, height);
}
}
