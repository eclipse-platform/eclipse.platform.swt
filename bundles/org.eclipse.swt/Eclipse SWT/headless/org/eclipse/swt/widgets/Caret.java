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
 * Headless implementation of Caret for SWT.
 */
public class Caret extends Widget {
	Canvas parent;
	Image image;
	Font font;
	int x, y, width, height;
	boolean visible;

public Caret(Canvas parent, int style) {
	super(parent, style);
	this.parent = parent;
	width = 1;
	height = 10;
}

public Rectangle getBounds() {
	checkWidget();
	return new Rectangle(x, y, width, height);
}

public Font getFont() {
	checkWidget();
	return font;
}

public Image getImage() {
	checkWidget();
	return image;
}

public Point getLocation() {
	checkWidget();
	return new Point(x, y);
}

public Canvas getParent() {
	checkWidget();
	return parent;
}

public Point getSize() {
	checkWidget();
	return new Point(width, height);
}

public boolean getVisible() {
	checkWidget();
	return visible;
}

public boolean isVisible() {
	checkWidget();
	return visible && parent.isVisible();
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

public void setFont(Font font) {
	checkWidget();
	this.font = font;
}

public void setImage(Image image) {
	checkWidget();
	this.image = image;
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

public void setVisible(boolean visible) {
	checkWidget();
	this.visible = visible;
}

}
