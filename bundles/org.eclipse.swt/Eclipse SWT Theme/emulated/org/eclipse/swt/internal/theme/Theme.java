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
package org.eclipse.swt.internal.theme;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Theme {
	Device device;
	
public Theme(Device device) {
	this.device = device;
}

void checkTheme() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public Rectangle computeTrim(GC gc, DrawData data) {
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return data.computeTrim(this, gc);
}

public void dispose () {
	device = null;
}
	
public void drawBackground(GC gc, Rectangle bounds, DrawData data) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.draw(this, gc, bounds);
}

public void drawFocus(GC gc, Rectangle bounds, DrawData data) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	gc.drawFocus(bounds.x, bounds.y, bounds.width, bounds.height);
}

public void drawImage(GC gc, Rectangle bounds, DrawData data, Image image, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.drawImage(this, image, gc, bounds);
}

public void drawText(GC gc, Rectangle bounds, DrawData data, String text, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.drawText(this, text, flags, gc, bounds);
}

public Rectangle getBounds(int part, Rectangle bounds, DrawData data) {
	checkTheme();
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.getBounds(part, bounds);
}

public int getSelection(Point offset, Rectangle bounds, RangeDrawData data) {
	checkTheme();
	if (offset == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.getSelection(offset, bounds);
}

public int hitBackground(Point position, Rectangle bounds, DrawData data) {
	checkTheme();
	if (position == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.hit(this, position, bounds);
}

public boolean isDisposed() {
	return device == null;
}

public Rectangle measureText(GC gc, Rectangle bounds, DrawData data, String text, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return data.measureText(this, text, flags, gc, bounds);
}
}
