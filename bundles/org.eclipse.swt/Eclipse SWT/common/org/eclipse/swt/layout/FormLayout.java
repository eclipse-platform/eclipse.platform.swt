package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class control the position and size of the 
 * children of a composite control by using <code>FormAttachments</code>
 * that have been set on the sides of the controls.
 * <p>
 * The following example code creates a <code>FormLayout</code> and then sets
 * it into a <code>Shell</code>:
 * <pre>
 * 		Display display = new Display ();
 *		Shell shell = new Shell(display);
 *		FormLayout layout= new FormLayout();
 *		layout.marginWidth = 3;
 *		layout.marginHeight = 3;
 *		shell.setLayout(layout);
 * </pre>
 * </p>
 * <p>
 * To use a <code>FormLayout</code>, define a <code>FormData</code>
 * for each child control within the parent <code>Composite</code>. 
 * Each side of a child control can be attached to a position in the parent 
 * composite, or to other controls within the layout, by creating instances
 * of <code>FormAttachment</code> and setting them into the top, bottom,
 * left, and right fields of the child control's <code>FormData</code>.
 * If a side is not given an attachment, it is defined as not being attached
 * to anything.
 * </p>
 * 
 * @see FormData
 * @see FormAttachment
 */
public final class FormLayout extends Layout {
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
	
public FormLayout () {
}
	
protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Point size = layout (composite, false, 0, 0, 0, 0, flushCache);
	size.x += marginWidth * 2;
	size.y += marginHeight * 2;
	return size;
}

Point getSize (Control control, boolean flushCache) {
	int wHint = SWT.DEFAULT, hHint = SWT.DEFAULT;
	FormData data = (FormData) control.getLayoutData ();
	if (data != null) {
		wHint = data.width;
		hHint = data.height;
	}
	return control.computeSize (wHint, hHint, flushCache);
}

protected void layout (Composite composite, boolean flushCache) {
	Rectangle rect = composite.getClientArea ();
	int x = rect.x + marginWidth;
	int y = rect.y + marginHeight;
	int width = Math.max (0, rect.width - 2 * marginWidth);
	int height = Math.max (0, rect.height - 2 * marginHeight);
	layout (composite, true, x, y, width, height, flushCache);
}

Point layout (Composite composite, boolean move, int x, int y, int width, int height, boolean flushCache) {
	Control [] children = composite.getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		Point pt = getSize (child, false);
		FormData data = (FormData) child.getLayoutData ();
		if (data == null) {
			child.setLayoutData (data = new FormData ());
		}
		data.cacheWidth = pt.x;
		data.cacheHeight = pt.y;
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		FormData data = (FormData) child.getLayoutData ();
		if (move) {
			int x1 = data.getLeftAttachment ().solveX (width);
			int y1 = data.getTopAttachment ().solveX (height);
			int x2 = data.getRightAttachment ().solveX (width);
			int y2 = data.getBottomAttachment ().solveX (height);
			child.setBounds (x + x1, y + y1, x2 - x1, y2 - y1);
		} else {
			width = Math.max (computeWidth (data), width);
			height = Math.max (computeHeight (data), height);
		}
	}
	return move ? null : new Point (width, height);
}

int computeHeight (FormData data) {
	FormAttachment top = data.getTopAttachment ();
	FormAttachment bottom = data.getBottomAttachment ();
	FormAttachment height = bottom.minus (top);
	if (height.numerator == 0) {
		if (bottom.numerator == 0) return bottom.offset;
		if (top.numerator == top.denominator) {
			return Math.abs (top.offset);
		}
		int divider = bottom.denominator - bottom.numerator; 
		return bottom.denominator * bottom.offset / divider;
	}
	return height.solveY (data.cacheHeight);
}

int computeWidth (FormData data) {
	FormAttachment left = data.getLeftAttachment ();
	FormAttachment right = data.getRightAttachment ();
	FormAttachment width = right.minus (left);
	if (width.numerator == 0) {
		if (right.numerator == 0) return right.offset;
		if (left.numerator == left.denominator) {
			return Math.abs (left.offset);
		}
		int divider = right.denominator - right.numerator; 
		return right.denominator * right.offset / divider;
	}
	return width.solveY (data.cacheWidth);
}
	
}
