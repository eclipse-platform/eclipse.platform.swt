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
 * <pre>
 *		FormData data2 = new FormData();
 *		data2.left = new FormAttachment(button1);
 *		data2.right = new FormAttachment(100, 0);
 *		button2.setLayoutData(data2);
 * </pre>
 * </p>
 * <p>
 * IMPORTANT: Do not define circular attachments.  For example, do not attach
 * the right edge of <code>button1</code> to the left edge of <code>button2</code>
 * and then attach the left edge of <code>button2</code> to the right edge of
 * <code>button1</code>.  This will over constrain the layout, causing undefined
 * behavior.  The algorithm will terminate, but the results are undefined.
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
 	
	/**
	 * spacing specifies the number of pixels between the edge of one control
	 * and the edge of its neighbouring control.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
	public int spacing = 0;
	
/**
 * Constructs a new instance of this class.
 */
public FormLayout () {
}

/**
 * Computes the preferred height of the form with
 * respect to the preferred height of the control.
 * 
 * Given that the equations for top (T) and bottom (B)
 * of the control in terms of the height of the form (X)
 * are:
 *		T = AX + B
 *		B = CX + D
 * 
 * The equation for the height of the control (H)
 * is bottom (B) minus top (T) or (H = B - T) or:
 * 
 *		H = (CX + D) - (AX + B)
 * 
 * Solving for (X), the height of the form, we get:
 * 
 *		X = (H + B - D) / (C - A)
 * 
 * When (A = C), (C - A = 0) and the equation has no
 * solution for X.  This is a special case meaning that
 * the control does not constrain the height of the
 * form.  In this case, we need to arbitrarily define
 * the height of the form (X):
 * 
 * Case 1: A = C, A = 0, C = 0
 *
 * 		Let X = D, the distance from the top of the form
 * 		to the bottom edge of the control.  In this case,
 * 		the control was attatched to the top of the form
 * 		and the form needs to be large enough to show the
 * 		bottom edge of the control.
 * 
 * Case 2: A = C, A = 1, C = 1
 * 
 * 		Let X = -B, the distance from the bottom of the
 *		form to the top edge of the control.  In this case,
 * 		the control was attached to the bottom of the form
 * 		and the only way that the control would be visible
 * 		is if the offset is negative.  If the offset is
 * 		positive, there is no possible height for the form
 * 		that will show the control as it will always be
 * 		below the bottom edge of the form.
 * 
 * Case 3: A = C, A != 0, C != 0 and A != 1, C != 0
 * 
 * 		Let X = D / (1 - C), the distance from the top of the 
 * 		form to the bottom edge of the control.  In this case, 
 * 		since C is not 0 or 1, it must be a fraction, U / V.  
 * 		The offset D is the distance from CX to the bottom edge 
 * 		of the control.  This represents a fraction of the form 
 * 		(1 - C)X. Since the height of a fraction of the form is 
 * 		known, the height of the entire form can be found by setting
 * 		(1 - C)X = D.  We solve this equation for X in terms of U 
 * 		and V, giving us X = (U * D) / (U - V). Similarily, if the 
 * 		offset D is	negative, the control is positioned above CX.
 * 		The offset -B is the distance from the top edge of the control
 * 		to CX. We can find the height of the entire form by setting 
 * 		CX = -B. Solving in terms of U and V gives us X = (-B * V) / U.
 */
int computeHeight (Control control, FormData data, boolean flushCache) {
	FormAttachment top = data.getTopAttachment (control, spacing, flushCache);
	FormAttachment bottom = data.getBottomAttachment (control, spacing, flushCache);
	FormAttachment height = bottom.minus (top);
	if (height.numerator == 0) {
		if (bottom.numerator == 0) return bottom.offset;
		if (bottom.numerator == bottom.denominator) return -top.offset;
		if (bottom.offset <= 0) {
			return -top.offset * top.denominator / bottom.numerator;
		}
		int divider = bottom.denominator - bottom.numerator; 
		return bottom.denominator * bottom.offset / divider;
	}
	return height.solveY (data.getHeight (control, flushCache));
}

protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Point size = layout (composite, false, 0, 0, 0, 0, flushCache);
	size.x += marginWidth * 2;
	size.y += marginHeight * 2;
	return size;
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

/**
 * Computes the preferred height of the form with
 * respect to the preferred height of the control.
 */
int computeWidth (Control control, FormData data, boolean flushCache) {
	FormAttachment left = data.getLeftAttachment (control, spacing, flushCache);
	FormAttachment right = data.getRightAttachment (control, spacing, flushCache);
	FormAttachment width = right.minus (left);
	if (width.numerator == 0) {
		if (right.numerator == 0) return right.offset;
		if (right.numerator == right.denominator) return -left.offset;
		if (right.offset <= 0) {
			return -left.offset * left.denominator / left.numerator;
		}
		int divider = right.denominator - right.numerator; 
		return right.denominator * right.offset / divider;
	}
	return width.solveY (data.getWidth (control, flushCache));
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
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		FormData data = (FormData) child.getLayoutData ();
		if (data == null) child.setLayoutData (data = new FormData ());
		data.flushCache ();
	}
	Rectangle [] bounds = null;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		FormData data = (FormData) child.getLayoutData ();
		if (move) {
			FormAttachment left = data.getLeftAttachment (child, spacing, flushCache);
			FormAttachment right = data.getRightAttachment (child, spacing, flushCache);
			int x1 = left.solveX (width);
			int x2 = right.solveX (width);
//			if ((child.getStyle () & SWT.WRAP) != 0) {
//				if (data.width == SWT.DEFAULT && data.cacheWidth == -1) {
//					//width favoured over height (calling getLeftAttachment() does this)
//					//this means that when y1 and y2 are computed, the cached height values
//					//reflect the desired height wrt "x2 - x1" rather than "width".  This
//					//means that the cache is wrong wrt "width" and should be cleared
//					//bug (x2 - x1) needs to get rid of trim
//					int border = child.getBorderWidth ();
//					data.computeCache (child, x2 - x1 - border * 2, data.height, flushCache);
//				}
//			}
			int y1 = data.getTopAttachment (child, spacing, flushCache).solveX (height);
			int y2 = data.getBottomAttachment (child, spacing, flushCache).solveX (height);
			if (bounds == null) bounds = new Rectangle [children.length];
			bounds [i] = new Rectangle (x + x1, y + y1, x2 - x1, y2 - y1);
		} else {
			width = Math.max (computeWidth (child, data, flushCache), width);
			height = Math.max (computeHeight (child, data, flushCache), height);
		}
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		FormData data = (FormData) child.getLayoutData ();
		data.flushCache ();
	}
	if (move) {
		for (int i=0; i<children.length; i++) {
			children [i].setBounds (bounds [i]);		
		}
	}
	return move ? null : new Point (width, height);
}

public String toString () {
 	String string =  getName ()+" {";
 	if (marginWidth != 0) string += "marginWidth="+marginWidth+" ";
 	if (marginHeight != 0) string += "marginHeight="+marginHeight+" ";
 	if (spacing != 0) string += "spacing="+spacing+" ";
 	string = string.trim();
 	string += "}";
 	return string;
}	
}
