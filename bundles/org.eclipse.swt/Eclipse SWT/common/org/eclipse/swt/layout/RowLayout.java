package org.eclipse.swt.layout;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class determine the size and position of the 
 * children of a <code>Composite</code> by placing them either in 
 * horizontal rows or vertical columns within the parent <code>Composite</code>. 
 * <p>
 * <code>RowLayout</code> aligns all controls in one row if the
 * <code>type</code> is set to horizontal, and one column if it is
 * set to vertical. It has the ability to wrap, and provides configurable 
 * margins and spacing. <code>RowLayout</code> has a number of configuration 
 * fields. In addition, the height and width of each control in a 
 * <code>RowLayout</code> can be specified by setting a <code>RowData</code>
 * object into the control using <code>setLayoutData ()</code>.
 * </p>
 * <p>
 * The following example code creates a <code>RowLayout</code>, sets all 
 * of its fields to non-default values, and then sets it into a 
 * <code>Shell</code>. 
 * <pre>
 * 		RowLayout rowLayout = new RowLayout();
 * 		rowLayout.wrap = false;
 * 		rowLayout.pack = false;
 * 		rowLayout.justify = true;
 * 		rowLayout.type = SWT.VERTICAL;
 * 		rowLayout.marginLeft = 5;
 * 		rowLayout.marginTop = 5;
 * 		rowLayout.marginRight = 5;
 * 		rowLayout.marginBottom = 5;
 * 		rowLayout.spacing = 0;
 * 		shell.setLayout(rowLayout);
 * </pre>
 * If you are using the default field values, you only need one line of code:
 * <pre>
 * 		shell.setLayout(new RowLayout());
 * </pre>
 * </p>
 * 
 * @see RowData
 */
public final class RowLayout extends Layout {
	/**
	 * type specifies whether the layout places controls in rows or 
	 * columns.
	 * 
	 * The default value is HORIZONTAL.
	 * 
	 * Possible values are:
	 * 
	 * HORIZONTAL: Position the controls horizontally from left to right
	 * VERTICAL: Position the controls vertically from top to bottom
	 * 
	 * @since 2.0
	 */
	public int type = SWT.HORIZONTAL;	
	/**
	 * wrap specifies whether a control will be wrapped to the next
	 * row if there is insufficient space on the current row.
	 *
	 * The default value is true.
	 */
	public boolean wrap = true;
	/**
	 * pack specifies whether all controls in the layout take
	 * their preferred size.  If pack is false, all controls will 
	 * have the same size which is the size required to accommodate the 
	 * largest preferred height and the largest preferred width of all 
	 * the controls in the layout.
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

/**
 * Constructs a new instance of this class.
 */
public RowLayout () {
}

/**
 * Constructs a new instance of this class given the type.
 *
 * @param type the type of row layout
 * 
 * @since 2.0
 */
public RowLayout (int type) {
	this.type = type;
}

protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Point extent;
	if (type == SWT.HORIZONTAL) {
		extent = layoutHorizontal (composite, false, (wHint != SWT.DEFAULT) && wrap, wHint, flushCache);
	} else {
		extent = layoutVertical (composite, false, (hHint != SWT.DEFAULT) && wrap, wHint, flushCache);
	}
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
	if (type == SWT.HORIZONTAL) {
		layoutHorizontal (composite, true, wrap, clientArea.width, flushCache);
	} else {
		layoutVertical (composite, true, wrap, clientArea.height, flushCache);
	}
}

Point layoutHorizontal (Composite composite, boolean move, boolean wrap, int width, boolean flushCache) {
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

Point layoutVertical (Composite composite, boolean move, boolean wrap, int height, boolean flushCache) {
	Control [] children = composite.getChildren ();
	int count = children.length;
	int childWidth = 0, childHeight = 0, maxWidth = 0;
	if (!pack) {
		for (int i=0; i<count; i++) {
			Control child = children [i];
			Point pt = getSize (child, flushCache);
			childWidth = Math.max (childWidth, pt.x);
			childHeight = Math.max (childHeight, pt.y);
		}
		maxWidth = childWidth;
	}
	int clientX = 0, clientY = 0;
	if (move) {
		Rectangle rect = composite.getClientArea ();
		clientX = rect.x;  clientY = rect.y;
	}
	boolean wrapped = false;
	Rectangle [] bounds = null;
	if (move && justify) bounds = new Rectangle [count];
	int maxY = 0, x = marginLeft, y = marginTop;
	for (int i=0; i<count; i++) {
		Control child = children [i];
		if (pack) {
			Point pt = getSize (child, flushCache);
			childWidth = pt.x;  childHeight = pt.y;
		}
		if (wrap && (i != 0) && (y + childHeight > height)) {
			wrapped = true;
			x += spacing + maxWidth;  y = marginTop;
		}
		if (pack) {
			maxWidth = Math.max (maxWidth, childWidth);
		}
		if (move) {
			int childX = x + clientX, childY = y + clientY;
			if (justify) {
				bounds [i] = new Rectangle (childX, childY, childWidth, childHeight);
			} else {
				child.setBounds (childX, childY, childWidth, childHeight);
			}
		}
		y += spacing + childHeight;
		maxY = Math.max (maxY, y);
	}
	if (!wrap) maxY = y + marginBottom;
	if (move && justify) {
		int space = 0, margin = 0;
		if (!wrapped) {
			space = Math.max (0, (height - maxY) / (count + 1));
			margin = Math.max (0, ((height - maxY) % (count + 1)) / 2);
		}
		for (int i=0; i<count; i++) {
			Control child = children [i];
			bounds [i].y += (space * (i + 1)) + margin;
			child.setBounds (bounds [i]);
		}
	}
	return new Point (x + maxWidth + marginRight, maxY);
}
}
