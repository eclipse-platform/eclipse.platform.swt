package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * <code>FillLayout</code> is the simplest layout class. It lays out 
 * widgets in a single row or column, forcing them to be the same size. 
 * <p>
 * Initially, the widgets will all be as tall as the tallest widget, 
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
