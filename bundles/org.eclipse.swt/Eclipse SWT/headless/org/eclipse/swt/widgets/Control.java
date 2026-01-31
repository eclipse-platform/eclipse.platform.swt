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
 * Headless implementation of Control for SWT.
 */
public abstract class Control extends Widget implements Drawable {
	Composite parent;
	String toolTipText;
	Object layoutData;
	Menu menu;
	Cursor cursor;
	Font font;
	Color foreground, background;
	Image backgroundImage;
	boolean enabled = true;
	boolean visible = true;
	int x, y, width, height;

public Control() {
	// No-op
}

public Control(Composite parent, int style) {
	super(parent, style);
	this.parent = parent;
	if (parent != null) {
		parent.addChild(this);
	}
	width = DEFAULT_WIDTH;
	height = DEFAULT_HEIGHT;
}

public Point computeSize(int wHint, int hHint) {
	return computeSize(wHint, hHint, true);
}

public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point(width, height);
}

public boolean forceFocus() {
	checkWidget();
	return false;
}

public Color getBackground() {
	checkWidget();
	if (background != null) return background;
	return display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
}

public Image getBackgroundImage() {
	checkWidget();
	return backgroundImage;
}

public Rectangle getBounds() {
	checkWidget();
	return new Rectangle(x, y, width, height);
}

public Cursor getCursor() {
	checkWidget();
	return cursor;
}

public boolean getEnabled() {
	checkWidget();
	return enabled;
}

public Font getFont() {
	checkWidget();
	if (font != null) return font;
	return parent != null ? parent.getFont() : display.getSystemFont();
}

public Color getForeground() {
	checkWidget();
	if (foreground != null) return foreground;
	return display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
}

public Object getLayoutData() {
	checkWidget();
	return layoutData;
}

public Point getLocation() {
	checkWidget();
	return new Point(x, y);
}

public Menu getMenu() {
	checkWidget();
	return menu;
}

public Composite getParent() {
	checkWidget();
	return parent;
}

public Shell getShell() {
	checkWidget();
	return parent != null ? parent.getShell() : null;
}

public Point getSize() {
	checkWidget();
	return new Point(width, height);
}

public String getToolTipText() {
	checkWidget();
	return toolTipText;
}

public boolean getVisible() {
	checkWidget();
	return visible;
}

@Override
public boolean isEnabled() {
	checkWidget();
	return getEnabled() && (parent != null ? parent.isEnabled() : true);
}

public boolean isFocusControl() {
	checkWidget();
	return display.focusControl == this;
}

public boolean isVisible() {
	checkWidget();
	return getVisible() && (parent != null ? parent.isVisible() : true);
}

public boolean isReparentable() {
	checkWidget();
	return true;
}

boolean isTabGroup() {
	Control[] tabList = parent.tabList;
	if (tabList != null) {
		for (Control control : tabList) {
			if (control == this) return true;
		}
	}
	int bits = SWT.NONE;
	return (bits & style) != 0;
}

public void moveAbove(Control control) {
	checkWidget();
	// No-op in headless mode
}

public void moveBelow(Control control) {
	checkWidget();
	// No-op in headless mode
}

public void pack() {
	pack(true);
}

public void pack(boolean changed) {
	checkWidget();
	setSize(computeSize(SWT.DEFAULT, SWT.DEFAULT, changed));
}

public void redraw() {
	checkWidget();
	// No-op in headless mode
}

public void redraw(int x, int y, int width, int height, boolean all) {
	checkWidget();
	// No-op in headless mode
}

@Override
void releaseParent() {
	if (parent != null) parent.removeChild(this);
}

public boolean setFocus() {
	checkWidget();
	if (!isEnabled() || !isVisible()) return false;
	display.focusControl = this;
	return true;
}

public void setBackground(Color color) {
	checkWidget();
	background = color;
}

public void setBackgroundImage(Image image) {
	checkWidget();
	backgroundImage = image;
}

public void setBounds(int x, int y, int width, int height) {
	checkWidget();
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
}

public void setBounds(Rectangle rect) {
	checkWidget();
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
	setBounds(rect.x, rect.y, rect.width, rect.height);
}

public void setCursor(Cursor cursor) {
	checkWidget();
	this.cursor = cursor;
}

public void setEnabled(boolean enabled) {
	checkWidget();
	this.enabled = enabled;
}

public void setFont(Font font) {
	checkWidget();
	this.font = font;
}

public void setForeground(Color color) {
	checkWidget();
	foreground = color;
}

public void setLayoutData(Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

public void setLocation(int x, int y) {
	checkWidget();
	this.x = x;
	this.y = y;
}

public void setLocation(Point location) {
	checkWidget();
	if (location == null) error(SWT.ERROR_NULL_ARGUMENT);
	setLocation(location.x, location.y);
}

public void setMenu(Menu menu) {
	checkWidget();
	this.menu = menu;
}

public boolean setParent(Composite parent) {
	checkWidget();
	if (parent == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (this.parent != null) this.parent.removeChild(this);
	this.parent = parent;
	parent.addChild(this);
	return true;
}

public void setSize(int width, int height) {
	checkWidget();
	this.width = width;
	this.height = height;
}

public void setSize(Point size) {
	checkWidget();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setSize(size.x, size.y);
}

public void setToolTipText(String string) {
	checkWidget();
	toolTipText = string;
}

public void setVisible(boolean visible) {
	checkWidget();
	this.visible = visible;
}

public Point toControl(int x, int y) {
	checkWidget();
	return new Point(x - this.x, y - this.y);
}

public Point toControl(Point point) {
	checkWidget();
	if (point == null) error(SWT.ERROR_NULL_ARGUMENT);
	return toControl(point.x, point.y);
}

public Point toDisplay(int x, int y) {
	checkWidget();
	return new Point(x + this.x, y + this.y);
}

public Point toDisplay(Point point) {
	checkWidget();
	if (point == null) error(SWT.ERROR_NULL_ARGUMENT);
	return toDisplay(point.x, point.y);
}

public void update() {
	checkWidget();
	// No-op in headless mode
}

@Override
public boolean isAutoScalable() {
	return true;
}

}
