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
 * Headless implementation of MenuItem for SWT.
 */
public class MenuItem extends Item {
	Menu parent, menu;
	boolean selected;
	int accelerator;
	boolean enabled = true;

public MenuItem(Menu parent, int style) {
	super(parent, checkStyle(style));
	this.parent = parent;
	parent.addItem(this);
}

public MenuItem(Menu parent, int style, int index) {
	super(parent, checkStyle(style));
	this.parent = parent;
	parent.addItem(this);
}

static int checkStyle(int style) {
	return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

public int getAccelerator() {
	checkWidget();
	return accelerator;
}

public boolean getEnabled() {
	checkWidget();
	return enabled;
}

public Menu getMenu() {
	checkWidget();
	return menu;
}

public Menu getParent() {
	checkWidget();
	return parent;
}

public boolean getSelection() {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return selected;
}

public void setAccelerator(int accelerator) {
	checkWidget();
	this.accelerator = accelerator;
}

public void setEnabled(boolean enabled) {
	checkWidget();
	this.enabled = enabled;
}

public void setImage(Image image) {
	checkWidget();
	super.setImage(image);
}

public void setMenu(Menu menu) {
	checkWidget();
	if ((style & SWT.CASCADE) == 0) return;
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.DROP_DOWN) == 0) {
			error(SWT.ERROR_MENU_NOT_DROP_DOWN);
		}
	}
	this.menu = menu;
}

public void setSelection(boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	this.selected = selected;
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	super.setText(string);
}

@Override
void releaseParent() {
	super.releaseParent();
	if (parent != null) parent.removeItem(this);
}

}
