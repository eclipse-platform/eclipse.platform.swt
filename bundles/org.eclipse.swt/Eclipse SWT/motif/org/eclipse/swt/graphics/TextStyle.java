/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

public class TextStyle {

	Device device;
	Font font;
	Color foreground;
	Color background;
	
public TextStyle (Device device, Font font, Color foreground, Color background) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font != null && font.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (foreground != null && foreground.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (background != null && background.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	this.font = font;
	this.foreground = foreground;
	this.background = background;
	if (device.tracking) device.new_Object(this);
}

public void dispose() {
	if (device == null) return;
	font = null;
	foreground = null;
	background = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean isDisposed() {
	return device == null;
}
}
