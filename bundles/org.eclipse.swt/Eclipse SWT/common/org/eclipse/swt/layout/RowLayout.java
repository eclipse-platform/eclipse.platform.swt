package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public final class RowLayout extends Layout {
	/**
	 * wrap specifies whether a control will be wrapped to the next
	 * row if there is insufficient space on the current row.
	 *
	 * The default value is true.
	 */
	public boolean wrap = true;
	/**
	 * pack specifies whether all controls in the layout are forced
	 * to be the same size.
	 *
	 * The default value is true.
	 */
	public boolean pack = true;
	/**
	 * justify specifies whether the controls in a row should be
	 * fully justified, with any extra space placed between the controls.
	 *
	 * The default value is false.
	 */
	public boolean justify = false;
	/**
	 * spacing specifies the number of pixels between the edge of one cell
	 * and the edge of its neighbouring cell.
	 *
	 * The default value is 3.
	 */
	public int spacing = 3;
	/**
	 * marginLeft specifies the number of pixels of horizontal margin
	 * that will be placed along the left edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginLeft = 3;
	/**
	 * marginTop specifies the number of pixels of vertical margin
	 * that will be placed along the top edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginTop = 3;
	/**
	 * marginRight specifies the number of pixels of horizontal margin
	 * that will be placed along the right edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginRight = 3;
	/**
	 * marginBottom specifies the number of pixels of vertical margin
	 * that will be placed along the bottom edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginBottom = 3;
public RowLayout () {
}
protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Point extent = layout (composite, false, (wHint != SWT.DEFAULT) && wrap, wHint, flushCache);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	return extent;
}
Point getSize (Control control, boolean flushCache) {
	int wHint = SWT.DEFAULT, hHint = SWT.DEFAULT;
	RowData data = (RowData) control.getLayoutData ();
	if (data != null) {
		wHint = data.width;
		hHint = data.height;
	}
	return control.computeSize (wHint, hHint, flushCache);
}
protected void layout (Composite composite, boolean flushCache) {
	Rectangle clientArea = composite.getClientArea ();
	layout (composite, true, wrap, clientArea.width, flushCache);
}
Point layout (Composite composite, boolean move, boolean wrap, int width, boolean flushCache) {
	Control [] children = composite.getChildren ();
	int count = children.length;
	int childWidth = 0, childHeight = 0, maxHeight = 0;
	if (!pack) {
		for (int i=0; i<count; i++) {
			Control child = children [i];
			Point pt = getSize (child, flushCache);
			childWidth = Math.max (childWidth, pt.x);
			childHeight = Math.max (childHeight, pt.y);
		}
		maxHeight = childHeight;
	}
	int clientX = 0, clientY = 0;
	if (move) {
		Rectangle rect = composite.getClientArea ();
		clientX = rect.x;  clientY = rect.y;
	}
	boolean wrapped = false;
	Rectangle [] bounds = null;
	if (move && justify) bounds = new Rectangle [count];
	int maxX = 0, x = marginLeft, y = marginTop;
	for (int i=0; i<count; i++) {
		Control child = children [i];
		if (pack) {
			Point pt = getSize (child, flushCache);
			childWidth = pt.x;  childHeight = pt.y;
		}
		if (wrap && (i != 0) && (x + childWidth > width)) {
			wrapped = true;
			x = marginLeft;  y += spacing + maxHeight;
		}
		if (pack) {
			maxHeight = Math.max (maxHeight, childHeight);
		}
		if (move) {
			int childX = x + clientX, childY = y + clientY;
			if (justify) {
				bounds [i] = new Rectangle (childX, childY, childWidth, childHeight);
			} else {
				child.setBounds (childX, childY, childWidth, childHeight);
			}
		}
		x += spacing + childWidth;
		maxX = Math.max (maxX, x);
	}
	if (!wrap) maxX = x + marginRight;
	if (move && justify) {
		int space = 0, margin = 0;
		if (!wrapped) {
			space = Math.max (0, (width - maxX) / (count + 1));
			margin = Math.max (0, ((width - maxX) % (count + 1)) / 2);
		}
		for (int i=0; i<count; i++) {
			Control child = children [i];
			bounds [i].x += (space * (i + 1)) + margin;
			child.setBounds (bounds [i]);
		}
	}
	return new Point (maxX, y + maxHeight + marginBottom);
}
}
