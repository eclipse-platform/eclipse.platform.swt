package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Scrollable extends Control {
 	int scrolledHandle /* formHandle */;
	int hScrollBar, vScrollBar;
	ScrollBar horizontalBar, verticalBar;
Scrollable () {
	/* Do nothing */
}

public Scrollable (Composite parent, int style) {
	super (parent, style);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	return new Rectangle (0, 0, 0, 0);
}

ScrollBar createScrollBar (int type) {
    return new ScrollBar (this, type);
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) WidgetTable.remove (scrolledHandle);
}

public Rectangle getClientArea () {
	checkWidget();
	return new Rectangle (0, 0, 0, 0);
}

public ScrollBar getHorizontalBar () {
	checkWidget();
	return horizontalBar;
}

public ScrollBar getVerticalBar () {
	checkWidget();
	return verticalBar;
}

void register () {
	super.register ();
	if (scrolledHandle != 0) WidgetTable.put (scrolledHandle, this);
}
void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void releaseWidget () {
	if (horizontalBar != null) {
		horizontalBar.releaseWidget ();
		horizontalBar.releaseHandle ();
	}
	if (verticalBar != null) {
		verticalBar.releaseWidget ();
		verticalBar.releaseHandle ();
	}
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}
}
