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
import java.util.*;

/**
 * Headless implementation of Menu for SWT.
 */
public class Menu extends Widget {
	Decorations parent;
	MenuItem[] items = new MenuItem[0];
	int x, y;
	boolean visible;

public Menu(Control parent) {
	this(parent, SWT.POP_UP);
}

public Menu(Control parent, int style) {
	super(parent, checkStyle(style));
}

public Menu(Decorations parent, int style) {
	super(parent, checkStyle(style));
	this.parent = parent;
}

public Menu(Menu parentMenu) {
	this(parentMenu, SWT.DROP_DOWN);
}

public Menu(Menu parentMenu, int style) {
	super(parentMenu, checkStyle(style));
}

public Menu(MenuItem parentItem) {
	this(parentItem.parent, SWT.DROP_DOWN);
}

static int checkStyle(int style) {
	return checkBits(style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

public int getItemCount() {
	checkWidget();
	return items.length;
}

public MenuItem getItem(int index) {
	checkWidget();
	if (index < 0 || index >= items.length) error(SWT.ERROR_INVALID_RANGE);
	return items[index];
}

public MenuItem[] getItems() {
	checkWidget();
	MenuItem[] result = new MenuItem[items.length];
	System.arraycopy(items, 0, result, 0, items.length);
	return result;
}

public Decorations getParent() {
	checkWidget();
	return parent;
}

public Shell getShell() {
	checkWidget();
	return parent != null ? parent.getShell() : null;
}

public boolean getVisible() {
	checkWidget();
	return visible;
}

public int indexOf(MenuItem item) {
	checkWidget();
	if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}

public boolean isEnabled() {
	checkWidget();
	return (state & DISABLED) == 0;
}

public boolean isVisible() {
	checkWidget();
	return getVisible();
}

public void setEnabled(boolean enabled) {
	checkWidget();
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
}

public void setLocation(int x, int y) {
	checkWidget();
	this.x = x;
	this.y = y;
}

public void setLocation(org.eclipse.swt.graphics.Point location) {
	checkWidget();
	if (location == null) error(SWT.ERROR_NULL_ARGUMENT);
	setLocation(location.x, location.y);
}

public void setVisible(boolean visible) {
	checkWidget();
	this.visible = visible;
}

void addItem(MenuItem item) {
	MenuItem[] newItems = new MenuItem[items.length + 1];
	System.arraycopy(items, 0, newItems, 0, items.length);
	newItems[items.length] = item;
	items = newItems;
}

void removeItem(MenuItem item) {
	List<MenuItem> list = new ArrayList<>(Arrays.asList(items));
	list.remove(item);
	items = list.toArray(new MenuItem[list.size()]);
}

}
