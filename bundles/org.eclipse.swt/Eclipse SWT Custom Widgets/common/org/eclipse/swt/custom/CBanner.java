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
package org.eclipse.swt.custom;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

/**
* DO NOT USE - UNDER CONSTRUCTION
*
* @since 3.0
*/

/**
 * Instances of this class implement a Composite that ...
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(None)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class CBanner extends Composite {	

	Control left;
	Control right;
	Control bottom;
	
	boolean simple = true;
	
	int[] curve;
	int curveStart = 0;
	Rectangle curveRect = new Rectangle(0, 0, 0, 0);
	int curve_width = 5;
	int curve_indent = -2;
	
	int rightWidth = SWT.DEFAULT;
	Cursor resizeCursor;
	boolean dragging = false;
	int rightDragDisplacement = 0;
	
	static final int OFFSCREEN = -200;
	static final int BORDER_BOTTOM = 2;
	static final int BORDER_TOP = 3;
	static final int BORDER_STRIPE = 1;
	static final int CURVE_TAIL = 200;
	static final int BEZIER_RIGHT = 30;
	static final int BEZIER_LEFT = 30;
	static final int MIN_LEFT = 10;
	static final int MIN_RIGHT = 10;
	
	static RGB BORDER1 = null;
	
		
/**
 * DO NOT USE - UNDER CONSTRUCTION
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 * 
 * @since 3.0
 */
public CBanner(Composite parent, int style) {
	super(parent, checkStyle(style));
	if (BORDER1 == null) BORDER1 = getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW).getRGB();
	resizeCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEWE);
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
				case SWT.Dispose:
					onDispose(); break;
				case SWT.MouseDown:
					onMouseDown (e.x, e.y); break;
				case SWT.MouseExit:
					onMouseExit(); break;
				case SWT.MouseMove:
					onMouseMove(e.x, e.y); break;
				case SWT.MouseUp:
					onMouseUp(); break;
				case SWT.Paint:
					onPaint(e.gc); break;
				case SWT.Resize:
					onResize(); break;
			}
		}
	};
	int[] events = new int[] {SWT.Dispose, SWT.MouseDown, SWT.MouseExit, SWT.MouseMove, SWT.MouseUp, SWT.Paint, SWT.Resize};
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener);
	}
}
static int[] bezier(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int count) {
	// The parametric equations for a Bezier curve for x[t] and y[t] where  0 <= t <=1 are:
	// x[t] = x0+3(x1-x0)t+3(x0+x2-2x1)t^3+(x3-x0+3x1-3x2)t^3
	// y[t] = y0+3(y1-y0)t+3(y0+y2-2y1)t^2+(y3-y0+3y1-3y2)t^3
	double a0 = x0;
	double a1 = 3*(x1 - x0);
	double a2 = 3*(x0 + x2 - 2*x1);
	double a3 = x3 - x0 + 3*x1 - 3*x2;
	double b0 = y0;
	double b1 = 3*(y1 - y0);
	double b2 = 3*(y0 + y2 - 2*y1);
	double b3 = y3 - y0 + 3*y1 - 3*y2;

	int[] polygon = new int[2*count + 2];
	for (int i = 0; i <= count; i++) {
		double t = (double)i / (double)count;
		polygon[2*i] = (int)(a0 + a1*t + a2*t*t + a3*t*t*t);
		polygon[2*i + 1] = (int)(b0 + b1*t + b2*t*t + b3*t*t*t);
	}
	return polygon;
}
static int checkStyle (int style) {
	return SWT.NONE;
}
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	boolean showCurve = left != null && right != null;
	int height = hHint;
	int width = wHint;
	
	Point bottomSize = new Point(0, 0);
	if (bottom != null) {
		Point trim = bottom.computeSize(width, SWT.DEFAULT);
		trim.x = trim.x - width;
		bottomSize = bottom.computeSize(width == SWT.DEFAULT ? SWT.DEFAULT : width - trim.x, SWT.DEFAULT);
		if (height != SWT.DEFAULT) {
			bottomSize.y = Math.min(bottomSize.y, height);
			height -= bottomSize.y + BORDER_TOP + BORDER_STRIPE + BORDER_BOTTOM;
		}
	}
	if (showCurve && height != SWT.DEFAULT ) height -= BORDER_TOP + BORDER_BOTTOM + 2*BORDER_STRIPE;
	Point rightSize = new Point(0, 0);
	if (right != null) {
		Point trim = right.computeSize(rightWidth, height);
		trim.x = trim.x - rightWidth;
		rightSize = right.computeSize(rightWidth == SWT.DEFAULT ? SWT.DEFAULT : rightWidth - trim.x, rightWidth == SWT.DEFAULT ? SWT.DEFAULT : height);
		if (width != SWT.DEFAULT) {
			rightSize.x = Math.min(rightSize.x, width);
			width -= rightSize.x + curve_width - 2* curve_indent;
			width = Math.max(width, MIN_LEFT);
		}
	}
	Point leftSize = new Point(0, 0);
	if (left != null) {
		Point trim = left.computeSize(width, SWT.DEFAULT);
		trim.x = trim.x - width;
		leftSize = left.computeSize(width == SWT.DEFAULT ? SWT.DEFAULT : width - trim.x, SWT.DEFAULT);
	}
	int w = 0, h = 0;
	h += bottomSize.y;
	if (bottom != null && (left != null || right != null)) h += BORDER_TOP + BORDER_BOTTOM + BORDER_STRIPE;
	w += leftSize.x + rightSize.x;
	if (showCurve) {
		w += curve_width - 2*curve_indent;
		h +=  BORDER_TOP + BORDER_BOTTOM + 2*BORDER_STRIPE;
	}
	h += left != null ? leftSize.y : rightSize.y; 
	
	if (wHint != SWT.DEFAULT) w = wHint;
	if (hHint != SWT.DEFAULT) h = hHint;
	
	return new Point(w, h);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	return new Rectangle(x, y, width, height);
}
/**
* Returns the Control that appears on the bottom side of the banner.
* 
* @return the control that appears on the bottom side of the banner or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
* 
* @since 3.0
*/
public Control getBottom() {
	checkWidget();
	return bottom;
}
public Rectangle getClientArea() {
	return new Rectangle(0, 0, 0, 0);
}

/**
* Returns the Control that appears on the left side of the banner.
* 
* @return the control that appears on the left side of the banner or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
* 
* @since 3.0
*/
public Control getLeft() {
	checkWidget();
	return left;
}

/**
* Returns the Control that appears on the right side of the banner.
* 
* @return the control that appears on the right side of the banner or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
* 
* @since 3.0
*/
public Control getRight() {
	checkWidget();
	return right;
}
/**
 * Returns the width of the control that appears on the right of the banner.
 * 
 * @return the width of the control that appears on the right of the banner
 * 
 * @since 3.0
 */
public int getRightWidth() {
	checkWidget();
	if (right == null) return 0;
	if (rightWidth == SWT.DEFAULT) return right.computeSize(SWT.DEFAULT, getSize().y).x;
	return rightWidth;
}
/**
 * UNDER CONSTRUCTION
 * @since 3.0
 */
public boolean getSimple() {
	checkWidget();
	return simple;
}
public void layout (boolean changed) {
	checkWidget();
	Point size = getSize();
	boolean showCurve = left != null && right != null;
	int width = size.x;
	int height = size.y;
	
	Point bottomSize = new Point(0, 0);
	if (bottom != null) {
		Point trim = bottom.computeSize(width, SWT.DEFAULT);
		trim.x = trim.x - width;
		bottomSize = bottom.computeSize(width - trim.x, SWT.DEFAULT);
		bottomSize.y = Math.min(bottomSize.y, height);
		height -= bottomSize.y + BORDER_TOP + BORDER_BOTTOM + BORDER_STRIPE;
	}
	
	if (showCurve) height -=  BORDER_TOP + BORDER_BOTTOM + 2*BORDER_STRIPE;
	height = Math.max(0, height);
	Point rightSize = new Point(0,0);
	if (right != null) {
		Point trim = right.computeSize(rightWidth, height);
		trim.x = trim.x - rightWidth;
		rightSize = right.computeSize(rightWidth == SWT.DEFAULT ? SWT.DEFAULT : rightWidth - trim.x, rightWidth == SWT.DEFAULT ? SWT.DEFAULT : height);
		rightSize.x = Math.min(rightSize.x, width);
		width -= rightSize.x + curve_width - 2*curve_indent;
		width = Math.max(width, MIN_LEFT); 
	}

	Point leftSize = new Point(0, 0);
	if (left != null) {
		Point trim = left.computeSize(width, SWT.DEFAULT);
		trim.x = trim.x - width;
		leftSize = left.computeSize(width - trim.x, SWT.DEFAULT);
	}

	int x = 0;
	int y = 0;
	int oldStart = curveStart;
	Rectangle leftRect = null;
	Rectangle rightRect = null;
	Rectangle bottomRect = null;
	if (bottom != null) {
		bottomRect = new Rectangle(x, y+size.y-bottomSize.y, bottomSize.x, bottomSize.y);
	}
	if (showCurve) y += BORDER_TOP + BORDER_STRIPE;
	if(left != null) {
		leftRect = new Rectangle(x, y, leftSize.x, leftSize.y);
		curveStart = x + leftSize.x - curve_indent;
		x += leftSize.x + curve_width - 2*curve_indent;
	}
	if (right != null) {
		rightRect = new Rectangle(x, y, rightSize.x, rightSize.y);
	}
	if (curveStart < oldStart) {
		redraw(curveStart - CURVE_TAIL, 0, oldStart + curve_width - curveStart + CURVE_TAIL + 5, size.y, false);
	}
	if (curveStart > oldStart) {
		redraw(oldStart - CURVE_TAIL, 0, curveStart + curve_width - oldStart + CURVE_TAIL + 5, size.y, false);
	}
	curveRect = new Rectangle(curveStart, 0, curve_width, size.y);
	update();
	if (bottomRect != null) bottom.setBounds(bottomRect);
	if (rightRect != null) right.setBounds(rightRect);
	if (leftRect != null) left.setBounds(leftRect);
}
void onDispose() {
	if (resizeCursor != null) resizeCursor.dispose();
	resizeCursor = null;
	left = null;
	right = null;
}
void onMouseDown (int x, int y) {
	if (curveRect.contains(x, y)) {
		dragging = true;
		rightDragDisplacement = curveStart - x + curve_width - curve_indent;
	}
}
void onMouseExit() {
	if (!dragging) setCursor(null);
}
void onMouseMove(int x, int y) {
	if (dragging) {
		Point size = getSize();
		if (!(0 < x && x < size.x)) return;
		rightWidth = size.x - x - rightDragDisplacement;
		rightWidth = Math.max(MIN_RIGHT, rightWidth);
		layout();
		return;
	}
	if (curveRect.contains(x, y)) {
		setCursor(resizeCursor); 
	} else {
		setCursor(null);
	}
}
void onMouseUp () {
	dragging = false;
}
void onPaint(GC gc) {
//	 Useful for debugging paint problems
//	{
//	Point size = getSize();	
//	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
//	gc.fillRectangle(-10, -10, size.x+20, size.y+20);
//	}
	Point size = getSize();
	
	if (bottom != null && (left != null || right != null)) {
		Color border1 = new Color(getDisplay(), BORDER1);
		gc.setForeground(border1);
		int y = bottom.getBounds().y - BORDER_BOTTOM - BORDER_STRIPE;
		gc.drawLine(0, y, size.x, y);
		border1.dispose();
	}
	
	if (left == null || right == null) return;
	int[] line1 = new int[curve.length+6];
	int index = 0;
	int x = curveStart;
	int y = 0;
	line1[index++] = x + 1;
	line1[index++] = size.y - BORDER_STRIPE;
	for (int i = 0; i < curve.length/2; i++) {
		line1[index++]=x+curve[2*i];
		line1[index++]=y+curve[2*i+1];
	}
	line1[index++] = x + curve_width;
	line1[index++] = 0;
	line1[index++] = size.x;
	line1[index++] = 0;
	
	Color background = getBackground();
		
	// Anti- aliasing
	int[] line2 = new int[line1.length];
	index = 0;
	for (int i = 0; i < line1.length/2; i++) { 
		line2[index] = line1[index++]  - 1;
		line2[index] = line1[index++];
	}
	int[] line3 = new int[line1.length];
	index = 0;
	for (int i = 0; i < line1.length/2; i++) {
		line3[index] = line1[index++] + 1;
		line3[index] = line1[index++];
	}
	RGB from = BORDER1;
	RGB to = background.getRGB();
	int red = from.red + 3*(to.red - from.red)/4;
	int green = from.green + 3*(to.green - from.green)/4;
	int blue = from.blue + 3*(to.blue - from.blue)/4;
	Color color = new Color(getDisplay(), red, green, blue);
	gc.setForeground(color);
	gc.drawPolyline(line2);
	gc.drawPolyline(line3);
	color.dispose();
	
	int x1 = Math.max(0, curveStart - CURVE_TAIL);
	Color border1 = new Color(getDisplay(), BORDER1);
	gc.setForeground(background);
	gc.setBackground(border1);
	gc.fillGradientRectangle(x1, size.y - BORDER_STRIPE, curveStart-x1+1, 1, false);
	gc.setForeground(border1);
	gc.drawPolyline(line1);
	border1.dispose();
}

void onResize() {
	updateCurve(getSize().y);
	layout();
}
/**
* Set the control that appears on the bottom side of the banner.
* The bottom control is optional.  Setting the bottom control to null will remove it from 
* the banner - however, the creator of the control must dispose of the control.
* 
* @param control the control to be displayed on the bottom or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the bottom control was not created as a child of the receiver</li>
* </ul>
* 
* @since 3.0
*/
public void setBottom(Control control) {
	checkWidget();
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (bottom != null && !bottom.isDisposed()) {
		Point size = bottom.getSize();
		bottom.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
	}
	bottom = control;
	layout();
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note : CBanner does not use a layout class to size and position its children.
 * </p>
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	return;
}

/**
* Set the control that appears on the left side of the banner.
* The left control is optional.  Setting the left control to null will remove it from 
* the banner - however, the creator of the control must dispose of the control.
* 
* @param control the control to be displayed on the left or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the left control was not created as a child of the receiver</li>
* </ul>
* 
* @since 3.0
*/
public void setLeft(Control control) {
	checkWidget();
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (left != null && !left.isDisposed()) {
		Point size = left.getSize();
		left.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
	}
	left = control;
	layout();
}
/**
* Set the control that appears on the right side of the banner.
* The right control is optional.  Setting the right control to null will remove it from 
* the banner - however, the creator of the control must dispose of the control.
* 
* @param control the control to be displayed on the right or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the right control was not created as a child of the receiver</li>
* </ul>
* 
* @since 3.0
*/
public void setRight(Control control) {
	checkWidget();
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (right != null && !right.isDisposed()) {
		Point size = right.getSize();
		right.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
	}
	right = control;
	layout();
}
/**
 * 
 * @param width the width of the control on the right
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setRightWidth(int width) {
	checkWidget();
	if (width < SWT.DEFAULT) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	rightWidth = width;
	layout(true);
}
/**
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * UNDER CONSTRUCTION
 * @since 3.0
 */
public void setSimple(boolean simple) {
	checkWidget();
	if (this.simple != simple) {
		this.simple = simple;
		if (simple) {
			curve_width = 5;
			curve_indent = -2;
		} else {
			curve_width = 50;
			curve_indent = 5;
		}
		updateCurve(getSize().y);
		layout();
		redraw();
	}
}
void updateCurve(int height) {
	int h = height - BORDER_STRIPE;
	if (simple) {
		curve = new int[] {0,h, 1,h, 2,h-1, 3,h-2,
			                       3,2, 4,1, 5,0,};
	} else {
		curve = bezier(0, h+1, BEZIER_LEFT, h+1,
				             curve_width-BEZIER_RIGHT, 0, curve_width, 0,
		                     curve_width);
	}
}
}
