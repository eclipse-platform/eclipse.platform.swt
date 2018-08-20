/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class BlackHoleTab extends AnimatedGraphicsTab {

	int size = 1;

public BlackHoleTab(GraphicsExample example) {
	super(example);
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("BlackHole"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("BlackHoleDescription"); //$NON-NLS-1$
}

@Override
public void next(int width, int height) {
	if (size > width * 3 / 2) size = 0;
	else size += 10;
}

@Override
public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();
	gc.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
	gc.fillOval((width - size) / 2, (height - size) / 2, size, size);
}
}
