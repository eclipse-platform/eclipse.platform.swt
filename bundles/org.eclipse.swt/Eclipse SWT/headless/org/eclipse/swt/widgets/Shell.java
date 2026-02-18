/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Shell for SWT.
 */
public class Shell extends Decorations {
	boolean opened;
	String text = "";
	Image image;
	Image[] images = new Image[0];
	int alpha = 255;
	Region region;
	int minWidth = 0, minHeight = 0;
	int maxWidth = Integer.MAX_VALUE, maxHeight = Integer.MAX_VALUE;
	boolean modified;
	boolean fullScreen;

public Shell() {
	this((Display) null, SWT.SHELL_TRIM);
}

public Shell(int style) {
	this((Display) null, style);
}

public Shell(Display display) {
	this(display, SWT.SHELL_TRIM);
}

public Shell(Display display, int style) {
	super(display, checkStyle(display, style));
	if (display == null) display = Display.getCurrent();
	if (display == null) display = Display.getDefault();
	this.display = display;
	createWidget();
}

public Shell(Shell parent) {
	this(parent, SWT.DIALOG_TRIM);
}

public Shell(Shell parent, int style) {
	super(parent, checkStyle(parent, style));
	if (parent != null) {
		this.display = parent.display;
		if (this.parent == null) this.parent = parent;
	}
	createWidget();
}

void createWidget() {
	display.addShell(this);
	width = 200;
	height = 200;
}

static int checkStyle(Display display, int style) {
	style = checkBits(style, SWT.ON_TOP, SWT.TOOL, SWT.NO_TRIM, 0, 0, 0);
	if ((style & (SWT.ON_TOP | SWT.TOOL)) != 0) {
		if ((style & SWT.CLOSE) != 0) style |= SWT.TITLE;
	}
	if ((style & SWT.NO_TRIM) != 0) {
		return style & ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
	}
	if ((style & (SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER)) == 0) {
		style |= SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER;
	}
	return style;
}

static int checkStyle(Shell parent, int style) {
	if ((style & SWT.ON_TOP) != 0) style &= ~SWT.SHELL_TRIM;
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	if ((style & SWT.SHEET) != 0) {
		style &= ~SWT.SHEET;
		style |= (parent != null) ? SWT.DIALOG_TRIM : SWT.NONE;
		if ((style & mask) == 0) {
			style |= (parent != null) ? SWT.APPLICATION_MODAL : SWT.NONE;
		}
	}
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

public void addShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Close, typedListener);
	addListener(SWT.Iconify, typedListener);
	addListener(SWT.Deiconify, typedListener);
	addListener(SWT.Activate, typedListener);
	addListener(SWT.Deactivate, typedListener);
}

public void close() {
	checkWidget();
	Event event = new Event();
	sendEvent(SWT.Close, event);
	if (event.doit && !isDisposed()) dispose();
}

public void dispose() {
	if (isDisposed()) return;
	display.removeShell(this);
	super.dispose();
}

public int getAlpha() {
	checkWidget();
	return alpha;
}

public Rectangle getBounds() {
	checkWidget();
	return new Rectangle(x, y, width, height);
}

public boolean getFullScreen() {
	checkWidget();
	return fullScreen;
}

public Image getImage() {
	checkWidget();
	return image;
}

public Image[] getImages() {
	checkWidget();
	if (images == null) return new Image[0];
	Image[] result = new Image[images.length];
	System.arraycopy(images, 0, result, 0, images.length);
	return result;
}

public boolean getMaximized() {
	checkWidget();
	return false;
}

public Point getMaximumSize() {
	checkWidget();
	return new Point(maxWidth, maxHeight);
}

public boolean getMinimized() {
	checkWidget();
	return false;
}

public Point getMinimumSize() {
	checkWidget();
	return new Point(minWidth, minHeight);
}

public boolean getModified() {
	checkWidget();
	return modified;
}

public Region getRegion() {
	checkWidget();
	return region;
}

@Override
public Shell getShell() {
	checkWidget();
	return this;
}

public Shell[] getShells() {
	checkWidget();
	int count = 0;
	Shell[] allShells = display.getShells();
	for (Shell shell : allShells) {
		Control control = shell;
		while (control != null && control != this) {
			control = control.parent;
		}
		if (control == this) count++;
	}
	int index = 0;
	Shell[] result = new Shell[count];
	for (Shell shell : allShells) {
		Control control = shell;
		while (control != null && control != this) {
			control = control.parent;
		}
		if (control == this) {
			result[index++] = shell;
		}
	}
	return result;
}

public String getText() {
	checkWidget();
	return text;
}

public void open() {
	checkWidget();
	setVisible(true);
	opened = true;
}

public void removeShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Close, listener);
	eventTable.unhook(SWT.Iconify, listener);
	eventTable.unhook(SWT.Deiconify, listener);
	eventTable.unhook(SWT.Activate, listener);
	eventTable.unhook(SWT.Deactivate, listener);
}

public void setActive() {
	checkWidget();
	display.activeShell = this;
}

public void setAlpha(int alpha) {
	checkWidget();
	this.alpha = alpha;
}

public void setEnabled(boolean enabled) {
	checkWidget();
	this.enabled = enabled;
}

public void setFullScreen(boolean fullScreen) {
	checkWidget();
	this.fullScreen = fullScreen;
}

public void setImage(Image image) {
	checkWidget();
	this.image = image;
}

public void setImages(Image[] images) {
	checkWidget();
	if (images != null) {
		this.images = new Image[images.length];
		System.arraycopy(images, 0, this.images, 0, images.length);
	} else {
		this.images = new Image[0];
	}
}

public void setMaximized(boolean maximized) {
	checkWidget();
	// No-op in headless mode
}

public void setMaximumSize(int width, int height) {
	checkWidget();
	maxWidth = width;
	maxHeight = height;
}

public void setMaximumSize(Point size) {
	checkWidget();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setMaximumSize(size.x, size.y);
}

public void setMinimized(boolean minimized) {
	checkWidget();
	// No-op in headless mode
}

public void setMinimumSize(int width, int height) {
	checkWidget();
	minWidth = width;
	minHeight = height;
}

public void setMinimumSize(Point size) {
	checkWidget();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setMinimumSize(size.x, size.y);
}

public void setModified(boolean modified) {
	checkWidget();
	this.modified = modified;
}

public void setRegion(Region region) {
	checkWidget();
	this.region = region;
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

@Override
protected void checkSubclass() {
	// Allow subclassing
}

}
