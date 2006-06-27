/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class BlackHoleTab extends AnimatedGraphicsTab {
	
	int size = 1;
	
public BlackHoleTab(GraphicsExample example) {
	super(example);
}

public String getText() {
	return GraphicsExample.getResourceString("BlackHole"); //$NON-NLS-1$
}

public void next(int width, int height) {
	if (size > width * 3 / 2) size = 0;
	else size += 10;
}

public void paint(GC gc, int width, int height) {
	Display display = Display.getCurrent();
	gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
	gc.fillOval((width - size) / 2, (height - size) / 2, size, size);
}
}
