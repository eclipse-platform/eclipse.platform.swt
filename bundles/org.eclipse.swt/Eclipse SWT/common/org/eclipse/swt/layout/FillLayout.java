/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.layout;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * <code>FillLayout</code> is the simplest layout class. It lays out 
 * controls in a single row or column, forcing them to be the same size. 
 * <p>
 * Initially, the controls will all be as tall as the tallest control, 
 * and as wide as the widest. <code>FillLayout</code> does not wrap, 
 * and you cannot specify margins or spacing. You might use it to 
 * lay out buttons in a task bar or tool bar, or to stack checkboxes 
 * in a <code>Group</code>. <code>FillLayout</code> can also be used 
 * when a <code>Composite</code> only has one child. For example, 
 * if a <code>Shell</code> has a single <code>Group</code> child, 
 * <code>FillLayout</code> will cause the <code>Group</code> to 
 * completely fill the <code>Shell</code>.
 * </p>
 * <p>
 * Example code: first a <code>FillLayout</code> is created and
 * its type field is set, and then the layout is set into the 
 * <code>Composite</code>. Note that in a <code>FillLayout</code>,
 * children are always the same size, and they fill all available space.
 * <pre>
 * 		FillLayout fillLayout = new FillLayout();
 * 		fillLayout.type = SWT.VERTICAL;
 * 		shell.setLayout(fillLayout);
 * </pre>
 * </p>
 */
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
	
	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the layout.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
 	public int marginWidth = 0;
 	
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the layout.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
 	public int marginHeight = 0;
 	
 	/**
	 * spacing specifies the number of pixels between the edge of one cell
	 * and the edge of its neighbouring cell.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
	public int spacing = 0;
	
/**
 * Constructs a new instance of this class.
 */
public FillLayout () {
}

/**
 * Constructs a new instance of this class given the type.
 *
 * @param type the type of fill layout
 * 
 * @since 2.0
 */
public FillLayout (int type) {
	this.type = type;
}

protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Control [] children = composite.getChildren ();
	int count = children.length;
	int maxWidth = 0, maxHeight = 0;
	for (int i=0; i<count; i++) {
		Control child = children [i];
		Point size = child.computeSize (SWT.DEFAULT, SWT.DEFAULT, flushCache);
		maxWidth = Math.max (maxWidth, size.x);
		maxHeight = Math.max (maxHeight, size.y);
	}
	int width = 0, height = 0;
	if (type == SWT.HORIZONTAL) {
		width = count * maxWidth;
		if (count != 0) width += (count - 1) * spacing;
		height = maxHeight;
	} else {
		width = maxWidth;
		height = count * maxHeight;
		if (count != 0) height += (count - 1) * spacing;
	}
	width += marginWidth * 2;
	height += marginHeight * 2;
	return new Point (width, height);
}

protected void layout (Composite composite, boolean flushCache) {
	Rectangle rect = composite.getClientArea ();
	Control [] children = composite.getChildren ();
	int count = children.length;
	if (count == 0) return;
	int width = rect.width - marginWidth * 2;
	int height = rect.height - marginHeight * 2;
	if (type == SWT.HORIZONTAL) {
		width -= (count - 1) * spacing;
		int x = rect.x + marginWidth, extra = width % count;
		int y = rect.y + marginHeight, cellWidth = width / count;
		for (int i=0; i<count; i++) {
			Control child = children [i];
			int childWidth = cellWidth;
			if (i == 0) {
				childWidth += extra / 2;
			} else {
				if (i == count - 1) childWidth += (extra + 1) / 2;
			}
			child.setBounds (x, y, childWidth, height);
			x += childWidth + spacing;
		}
	} else {
		height -= (count - 1) * spacing;
		int x = rect.x + marginWidth, cellHeight = height / count;
		int y = rect.y + marginHeight, extra = height % count;
		for (int i=0; i<count; i++) {
			Control child = children [i];
			int childHeight = cellHeight;
			if (i == 0) {
				childHeight += extra / 2;
			} else {
				if (i == count - 1) childHeight += (extra + 1) / 2;
			}
			child.setBounds (x, y, width, childHeight);
			y += childHeight + spacing;
		}
	}
}

}
