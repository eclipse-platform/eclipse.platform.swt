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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Composite for SWT.
 */
public class Composite extends Scrollable {
	Layout layout;
	Control[] tabList;
	int layoutCount, backgroundMode;
	List<Control> children = new ArrayList<>();

public Composite() {
	// No-op
}

public Composite(Composite parent, int style) {
	super(parent, checkStyle(style));
}

static int checkStyle(int style) {
	style &= ~SWT.TRANSPARENT;
	return style;
}

void addChild(Control control) {
	children.add(control);
}

void removeChild(Control control) {
	children.remove(control);
}

public Control[] getChildren() {
	checkWidget();
	return children.toArray(new Control[children.size()]);
}

public int getBackgroundMode() {
	checkWidget();
	return backgroundMode;
}

public Layout getLayout() {
	checkWidget();
	return layout;
}

public boolean getLayoutDeferred() {
	checkWidget();
	return layoutCount > 0;
}

public Control[] getTabList() {
	checkWidget();
	if (tabList != null) return tabList;
	int count = 0;
	Control[] list = getChildren();
	for (Control child : list) {
		if (child.isTabGroup()) count++;
	}
	Control[] result = new Control[count];
	int index = 0;
	for (Control child : list) {
		if (child.isTabGroup()) {
			result[index++] = child;
		}
	}
	return result;
}

public boolean isLayoutDeferred() {
	checkWidget();
	return findDeferredControl() != null;
}

Composite findDeferredControl() {
	return layoutCount > 0 ? this : (parent != null ? parent.findDeferredControl() : null);
}

public void layout() {
	checkWidget();
	layout(true);
}

public void layout(boolean changed) {
	checkWidget();
	layout(changed, false);
}

public void layout(boolean changed, boolean all) {
	checkWidget();
	if (layout == null) return;
	if (layoutCount == 0) {
		layout.layout(this, changed);
		if (all) {
			for (Control child : children) {
				if (child instanceof Composite) {
					((Composite) child).layout(changed, all);
				}
			}
		}
	}
}

public void layout(Control[] changed) {
	checkWidget();
	if (changed == null) error(SWT.ERROR_INVALID_ARGUMENT);
	layout(changed, SWT.NONE);
}

public void layout(Control[] changed, int flags) {
	checkWidget();
	if (changed != null) {
		for (Control child : changed) {
			if (child == null) error(SWT.ERROR_INVALID_ARGUMENT);
			if (child.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			if (child.parent != this) error(SWT.ERROR_INVALID_PARENT);
		}
	}
	if (layout == null) return;
	if ((flags & SWT.DEFER) != 0) {
		setLayoutDeferred(true);
		return;
	}
	layout.layout(this, (flags & SWT.CHANGED) != 0);
}

public void setBackgroundMode(int mode) {
	checkWidget();
	backgroundMode = mode;
}

public void setLayout(Layout layout) {
	checkWidget();
	this.layout = layout;
}

public void setLayoutDeferred(boolean defer) {
	if (!defer) {
		if (--layoutCount == 0) {
			if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
				updateLayout(true);
			}
		}
	} else {
		layoutCount++;
	}
}

void updateLayout(boolean all) {
	Composite parent = findDeferredControl();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		layout(changed, all);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		for (Control child : children) {
			if (child instanceof Composite) {
				((Composite) child).updateLayout(all);
			}
		}
	}
}

public void setTabList(Control[] tabList) {
	checkWidget();
	if (tabList != null) {
		for (Control control : tabList) {
			if (control == null) error(SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			if (control.parent != this) error(SWT.ERROR_INVALID_PARENT);
		}
		Control[] newList = new Control[tabList.length];
		System.arraycopy(tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	}
	this.tabList = tabList;
}

@Override
protected void checkSubclass() {
	// Allow subclassing
}

}
