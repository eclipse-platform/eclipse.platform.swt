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
 * to optionally configure the left, top, right and bottom edge of
 * each child.
 * <p>
 * The following example code creates a <code>FormLayout</code> and then sets
 * it into a <code>Shell</code>:
 * <pre>
 * 		Display display = new Display ();
 *		Shell shell = new Shell(display);
 *		FormLayout layout = new FormLayout();
 *		layout.marginWidth = 3;
 *		layout.marginHeight = 3;
 *		shell.setLayout(layout);
 * </pre>
 * </p>
 * <p>
 * To use a <code>FormLayout</code>, create a <code>FormData</code> with
 * <code>FormAttachment</code> for each child of <code>Composite</code>.
 * The following example code attaches <code>button1</code> to the top
 * and left edge of the composite and <code>button2</code> to the right
 * edge of <code>button1</code> and the top and right edges of the
 * composite:
 * <pre>
 *		FormData data1 = new FormData();
 *		data1.left = new FormAttachment(0, 0);
 *		data1.top = new FormAttachment(0, 0);
 *		button1.setLayoutData(data1);
 *		FormData data2 = new FormData();
 *		data2.left = new FormAttachment(button1);
 *		data2.top = new FormAttachment(0, 0);
 *		data2.right = new FormAttachment(100, 0);
 *		button2.setLayoutData(data2);
 * </pre>
 * </p>
 * <p>
 * Each side of a child control can be attached to a position in the parent 
 * composite, or to other controls within the <code>Composite</code> by
 * creating instances of <code>FormAttachment</code> and setting them into
 * the top, bottom, left, and right fields of the child's <code>FormData</code>.
 * </p>
 * <p>
 * If a side is not given an attachment, it is defined as not being attached
 * to anything, causing the child to remain at it's preferred size.  If a child
 * is given no attachments on either the left or the right or top or bottom, it is
 * automatically attached to the left and top of the composite respectively.
 * The following code positions <code>button1</code> and <code>button2</code>
 * but relies on default attachments:
 * 
 * <pre>
 *		FormData data2 = new FormData();
 *		data2.left = new FormAttachment(button1);
 *		data2.right = new FormAttachment(100, 0);
 *		button2.setLayoutData(data2);
 * </pre>
 * </p>
 * 
 * @see FormData
 * @see FormAttachment
 * 
 * @since 2.0
 * 
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
