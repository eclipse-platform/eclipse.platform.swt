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
import org.eclipse.swt.events.*;

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
	int[] curve;
	int curveStart;
	int rightWidth = SWT.DEFAULT;
	Rectangle curveRect = new Rectangle(0, 0, 0, 0);
	Cursor resizeCursor;
	boolean dragging = false;
	int rightDragDisplacement = 0;
	
	static final int OFFSCREEN = -200;
	static final int CURVE_WIDTH = 50;
	static final int CURVE_RIGHT = 30;
	static final int CURVE_LEFT = 30;
	static final int CURVE_TAIL = 200;
	static final int BORDER_BOTTOM = 2;
	static final int BORDER_TOP = 3;
	static final int BORDER_LEFT = 2;
	static final int BORDER_RIGHT = 2;
	static final int BORDER_STRIPE = 2;
	static final int INDENT_LEFT = 10;
	static final int INDENT_RIGHT = 10;
	
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
	
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent event) {
			onPaint(event.gc);
		}
	});
	addControlListener(new ControlAdapter(){
		public void controlResized(ControlEvent e) {
			onResize();
		}
	});
	addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event e) {
			onDispose();
		}
	});
	addListener(SWT.MouseMove, new Listener() {
		public void handleEvent(Event event) {
			onMouseMove(event.x, event.y);
		}
	});
	addListener(SWT.MouseExit, new Listener() {
		public void handleEvent(Event event) {
			onMouseExit();
		}
	});
	addListener(SWT.MouseDown, new Listener() {
		public void handleEvent(Event event) {
			onMouseDown (event.x, event.y);
		}
	});
	addListener(SWT.MouseUp, new Listener() {
		public void handleEvent(Event event) {
			onMouseUp ();
		}
	});
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
	Point rightSize = (right == null) ? new Point(0, 0) : right.computeSize(rightWidth, hHint);
	int width = (wHint == SWT.DEFAULT) ? SWT.DEFAULT : wHint - rightSize.x - CURVE_WIDTH + INDENT_LEFT + INDENT_RIGHT;
	Point leftSize = (left == null) ? new Point(0, 0) : left.computeSize(width, hHint);
	Point size = new Point(0, 0);
	size.x = leftSize.x + CURVE_WIDTH - INDENT_LEFT - INDENT_RIGHT + rightSize.x;
	size.y = Math.max (leftSize.y, rightSize.y);
	
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	
	Rectangle trim = computeTrim(0, 0, size.x, size.y);
	return new Point(trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	boolean drawCurve = left != null && right != null;
	int borderTop = drawCurve ? BORDER_TOP : 0;
	int borderBottom = drawCurve ? BORDER_BOTTOM : 0;
	int trimX = x - BORDER_LEFT;
	int trimY = y - borderTop;
	int trimWidth = width + BORDER_LEFT + BORDER_RIGHT;
	int trimHeight = height + borderTop + borderBottom + 2*BORDER_STRIPE;
	return new Rectangle(trimX, trimY, trimWidth, trimHeight);
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
public int getRightWidth() {
	checkWidget();
	if (right == null) return 0;
	if (rightWidth == SWT.DEFAULT) return right.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
	return rightWidth;
}
public void layout (boolean changed) {
	checkWidget();
	Point size = getSize();
	Point rightSize;
	boolean drawCurve = left != null && right != null;
	int curveWidth = drawCurve ? CURVE_WIDTH : 0;
	int indentLeft = drawCurve ? INDENT_LEFT : 0;
	int indentRight = drawCurve ? INDENT_RIGHT : 0;
	int borderTop = drawCurve ? BORDER_TOP : 0;
	int borderBottom = drawCurve ? BORDER_BOTTOM : 0;
	if (right == null) {
		int width = rightWidth == SWT.DEFAULT ? 0 : rightWidth;
		rightSize = new Point(width, 0);
	} else {
		rightSize = right.computeSize(rightWidth, SWT.DEFAULT);
	}
	int width = size.x - curveWidth + indentLeft + indentRight - BORDER_LEFT - BORDER_RIGHT; 
	rightSize.x = Math.min(width, rightSize.x);
	width -= rightSize.x;
	Point leftSize = (left == null) ? new Point (0, 0) : left.computeSize(width, SWT.DEFAULT);

	int x = BORDER_LEFT;
	int oldStart = curveStart;
	Rectangle leftRect = null;
	Rectangle rightRect = null;
	if(left != null) {
		int height = Math.min(size.y - borderTop - borderBottom - 2*BORDER_STRIPE, leftSize.y);
		int y = borderTop + BORDER_STRIPE;
		leftRect = new Rectangle(x, y, leftSize.x, height);
		x += leftSize.x;
	}
	curveStart = x - indentLeft;
	x += curveWidth - indentLeft - indentRight;
	if (right != null) {
		int height = Math.min(size.y - borderTop - borderBottom - 2*BORDER_STRIPE, rightSize.y);
		int y = borderTop + BORDER_STRIPE;
		rightRect = new Rectangle(x, y, rightSize.x, height);
	}
	if (curveStart < oldStart) {
		redraw(curveStart - CURVE_TAIL, 0, oldStart + CURVE_WIDTH - curveStart + CURVE_TAIL, size.y, false);
	}
	if (curveStart > oldStart) {
		redraw(oldStart - CURVE_TAIL, 0, curveStart + CURVE_WIDTH - oldStart + CURVE_TAIL, size.y, false);
	}
	curveRect = new Rectangle(curveStart, 0, curveWidth, size.y);
	update();
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
		rightDragDisplacement = curveStart - x + BORDER_LEFT + CURVE_WIDTH - INDENT_RIGHT;
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
		rightWidth = Math.max(0, rightWidth);
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
	if (left == null || right == null) return;
	if (curve == null) updateCurve();
	Point size = getSize();
	int[] line1 = new int[curve.length+6];
	int index = 0;
	int x = curveStart;
	int y = 0;
	line1[index++] = x +1;
	line1[index++] = size.y - BORDER_STRIPE;
	for (int i = 0; i < curve.length/2; i++) {
		line1[index++]=x+curve[2*i];
		line1[index++]=y+curve[2*i+1];
	}
	line1[index++] = x + CURVE_WIDTH;
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
	updateCurve();
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
		left.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
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
		right.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	right = control;
	layout();
}
public void setRightWidth(int width) {
	checkWidget();
	if (width < SWT.DEFAULT) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	rightWidth = width;
	layout(true);
}
void updateCurve () {
	Point size = getSize();
	curve = bezier(0, size.y - BORDER_STRIPE + 1,
	               CURVE_LEFT, size.y - BORDER_STRIPE + 1,
			       CURVE_WIDTH-CURVE_RIGHT, 0,
	               CURVE_WIDTH, 0,
	               CURVE_WIDTH * 2);
	// workaround to get rid of blip at end of bezier
	int index = -1;
	for (int i = 0; i < curve.length/2; i++) {
		if (curve[2*i+1] > size.y - BORDER_STRIPE) {
			index = i;
		} else {
			break;
		}
	}
	if (index > -1) {
		int[] newCurve = new int[curve.length - 2*(index+1)];
		System.arraycopy(curve, 2*(index+1), newCurve, 0, newCurve.length);
		curve = newCurve;
	}
}
}
