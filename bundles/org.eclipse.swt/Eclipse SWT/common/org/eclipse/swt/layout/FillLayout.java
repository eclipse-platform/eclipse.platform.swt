package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public final class FillLayout extends Layout {
	/**
	 * type specifies how controls will be positioned 
	 * within the layout.
	 *
	 * The default value is HORIZONTAL.
	 *
	 * Possible values are:
	 *
	 * HORIZONTAL: Position the controls horizontally from left to right
	 * VERTICAL: Position the controls vertically from top to bottom
	 */
	public int type = SWT.HORIZONTAL;
	
public FillLayout () {
}

protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Control [] children = composite.getChildren ();
	int count = children.length;
	int maxWidth = 0, maxHeight = 0;
	for (int i=0; i<count; i++) {
		Control child = children [i];
		Point pt = child.computeSize (SWT.DEFAULT, SWT.DEFAULT, flushCache);
		maxWidth = Math.max (maxWidth, pt.x);
		maxHeight = Math.max (maxHeight, pt.y);
	}	
	if (type == SWT.HORIZONTAL) {
		return new Point (count * maxWidth, maxHeight);
	}
	return new Point (maxWidth, count * maxHeight);
}

protected void layout (Composite composite, boolean flushCache) {
	Rectangle rect = composite.getClientArea ();
	Control [] children = composite.getChildren ();
	int count = children.length;
	if (count == 0) return;
	if (type == SWT.HORIZONTAL) {
		int x = rect.x + ((rect.width % count) / 2);
		int width = rect.width / count;
		int y = rect.y, height = rect.height;
		for (int i=0; i<count; i++) {
			Control child = children [i];
			child.setBounds (x, y, width, height);
			x += width;
		}
		return;
	}
	int x = rect.x, width = rect.width;
	int y = rect.y + ((rect.height % count) / 2);
	int height = rect.height / count;
	for (int i=0; i<count; i++) {
		Control child = children [i];
		child.setBounds (x, y, width, height);
		y += height;
	}
}
}
