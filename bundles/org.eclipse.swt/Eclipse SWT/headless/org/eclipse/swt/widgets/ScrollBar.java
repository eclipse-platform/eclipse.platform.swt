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
 * Headless implementation of ScrollBar for SWT.
 */
public class ScrollBar extends Widget {
	Scrollable parent;
	int selection, minimum, maximum, thumb, increment, pageIncrement;
	boolean enabled = true, visible = true;

public ScrollBar(Scrollable parent, int style) {
	super(parent, checkStyle(style));
	this.parent = parent;
	maximum = 100;
	thumb = 10;
	increment = 1;
	pageIncrement = 10;
}

static int checkStyle(int style) {
	return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public boolean getEnabled() {
	checkWidget();
	return enabled;
}

public int getIncrement() {
	checkWidget();
	return increment;
}

public int getMaximum() {
	checkWidget();
	return maximum;
}

public int getMinimum() {
	checkWidget();
	return minimum;
}

public int getPageIncrement() {
	checkWidget();
	return pageIncrement;
}

public Scrollable getParent() {
	checkWidget();
	return parent;
}

public int getSelection() {
	checkWidget();
	return selection;
}

public Point getSize() {
	checkWidget();
	return new Point(10, 10);
}

public int getThumb() {
	checkWidget();
	return thumb;
}

public boolean getVisible() {
	checkWidget();
	return visible;
}

public boolean isEnabled() {
	checkWidget();
	return getEnabled() && parent.isEnabled();
}

public boolean isVisible() {
	checkWidget();
	return getVisible() && parent.isVisible();
}

public void setEnabled(boolean enabled) {
	checkWidget();
	this.enabled = enabled;
}

public void setIncrement(int value) {
	checkWidget();
	if (value < 1) return;
	increment = value;
}

public void setMaximum(int value) {
	checkWidget();
	if (value < 0) return;
	maximum = value;
}

public void setMinimum(int value) {
	checkWidget();
	if (value < 0) return;
	minimum = value;
}

public void setPageIncrement(int value) {
	checkWidget();
	if (value < 1) return;
	pageIncrement = value;
}

public void setSelection(int value) {
	checkWidget();
	if (value < minimum) value = minimum;
	if (value > maximum - thumb) value = maximum - thumb;
	selection = value;
}

public void setThumb(int value) {
	checkWidget();
	if (value < 1) return;
	thumb = value;
}

public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
	checkWidget();
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	this.minimum = minimum;
	this.maximum = maximum;
	this.thumb = thumb;
	this.increment = increment;
	this.pageIncrement = pageIncrement;
	if (selection < minimum) selection = minimum;
	if (selection > maximum - thumb) selection = maximum - thumb;
	this.selection = selection;
}

public void setVisible(boolean visible) {
	checkWidget();
	this.visible = visible;
}

}
