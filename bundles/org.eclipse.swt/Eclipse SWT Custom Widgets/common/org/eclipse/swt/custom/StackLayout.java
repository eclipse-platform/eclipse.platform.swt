package org.eclipse.swt.custom;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

/**
 * This Layout stacks all the controls one on top of the other and resizes all controls
 * to have the same size and location.
 * The control specified in topControl is visible and all other controls are not visible.
 * Users must set the topControl value to flip between the visible items and the call 
 * layout() on the composite which has the StackLayout.
 */

public class StackLayout extends Layout {
	
 	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the layout.
	 *
	 * The default value is 0.
	 */
 	public int marginWidth = 0;
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the layout.
	 *
	 * The default value is 0.
	 */
	public int marginHeight = 0;

 	/**
 	 * topControl the Control that is displayed at the top of the stack.
 	 * All other controls that are children of the parent composite will not be visible.
 	 */
 	public Control topControl;

protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	Control children[] = composite.getChildren();
	
	int maxWidth = 0;
	int maxHeight = 0;
	for (int i = 0; i < children.length; i++) {
		Point size = children[i].computeSize(wHint, hHint, flushCache);
		maxWidth = Math.max(size.x, maxWidth);
		maxHeight = Math.max(size.y, maxHeight);
	}
	
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = maxWidth;
	if (hHint == SWT.DEFAULT) height = maxHeight;
	return new Point(width + 2 * marginWidth, height + 2 * marginHeight);
}

protected void layout(Composite composite, boolean flushCache) {
	Control children[] = composite.getChildren();
	Rectangle rect = composite.getClientArea();
	rect.x += marginWidth;
	rect.y += marginHeight;
	rect.width -= 2 * marginWidth;
	rect.height -= 2 * marginHeight;
	for (int i = 0; i < children.length; i++) {
		children[i].setBounds(rect);
		children[i].setVisible(children[i] == topControl);
			
	}
}
}
