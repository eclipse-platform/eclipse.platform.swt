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
* @ since 3.0
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
	
	static final int OFFSCREEN = -200;
	static final int CURVE_WIDTH = 50;
	static final int CURVE_RIGHT = 30;
	static final int CURVE_LEFT = 30;
	static final int BORDER_BOTTOM = 2;
	static final int BORDER_TOP = 2;
	static final int BORDER_LEFT = 2;
	static final int BORDER_RIGHT = 2;
	static final int BORDER_STRIPE = 3;
	
	static final int FOREGROUND = SWT.COLOR_TITLE_BACKGROUND;
	static final int BACKGROUND = SWT.COLOR_TITLE_BACKGROUND_GRADIENT;
	
		
public CBanner(Composite parent, int style) {
	super(parent, checkStyle(style));

	Display display = getDisplay();
	setForeground(display.getSystemColor(FOREGROUND));
	setBackground(display.getSystemColor(BACKGROUND));
	
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
/**
 * Check the style bits to ensure that no invalid styles are applied.
 * @private
 */
static int checkStyle (int style) {
	return SWT.NO_BACKGROUND;
}
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	Point rightSize = new Point(0, 0);
	if (right != null) {
		 rightSize = right.computeSize(SWT.DEFAULT, hHint);
	}
	Point leftSize = new Point(0, 0);
	if (left != null) {
		if (wHint != SWT.DEFAULT) {
			leftSize = left.computeSize(wHint - CURVE_WIDTH - rightSize.x, SWT.DEFAULT);
		} else {
			leftSize = left.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
	}
	Point size = new Point(0, 0);
	size.x = leftSize.x + CURVE_WIDTH + rightSize.x;
	size.y = Math.max(leftSize.y + BORDER_STRIPE, rightSize.y);
	
	if (wHint != SWT.DEFAULT) size.x  = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	
	Rectangle trim = computeTrim(0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int trimX = x - BORDER_LEFT;
	int trimY = y - BORDER_TOP;
	int trimWidth = width + BORDER_LEFT + BORDER_RIGHT;
	int trimHeight = height + BORDER_TOP + BORDER_BOTTOM + BORDER_STRIPE;
	return new Rectangle(trimX, trimY, trimWidth, trimHeight);
}
public Rectangle getClientArea() {
	return new Rectangle(0, 0, 0, 0);
}

public Control getLeft() {
	checkWidget();
	return left;
}

public Control getRight() {
	checkWidget();
	return right;
}

public void layout (boolean changed) {
	checkWidget();
	Point size = getSize();
	
	Point rightSize = (right == null) ? new Point (0, 0) : right.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	int width = size.x - rightSize.x - CURVE_WIDTH - BORDER_LEFT - BORDER_RIGHT;
	Point leftSize = (left == null) ? new Point (0, 0) : left.computeSize(width, SWT.DEFAULT);

	int x = BORDER_LEFT;
	int oldStart = curveStart;
	Rectangle leftRect = null;
	Rectangle rightRect = null;
	if(left != null) {
		int height = size.y - BORDER_TOP - BORDER_BOTTOM - BORDER_STRIPE;
		int y = (leftSize.y > height) ? BORDER_TOP : BORDER_TOP + (height - leftSize.y) / 2;
		leftRect = new Rectangle(x, y, leftSize.x, Math.min(height, leftSize.y));
		x += leftSize.x;
		curveStart = x;
	}
	x += CURVE_WIDTH;
	if (right != null) {
		int height = size.y - BORDER_TOP - BORDER_BOTTOM;
		int y = (rightSize.y > height) ? BORDER_TOP : BORDER_TOP + (height - rightSize.y) / 2;
		rightRect = new Rectangle(x, y, rightSize.x, Math.min(size.y, rightSize.y));
	}
	if (curveStart < oldStart) {
		redraw(curveStart, 0, oldStart + CURVE_WIDTH - curveStart, size.y, false);
	}
	if (curveStart > oldStart) {
		redraw(oldStart, 0, curveStart + CURVE_WIDTH - oldStart, size.y, false);
	}
	update();
	if (leftRect != null) left.setBounds(leftRect);
	if (rightRect != null) right.setBounds(rightRect);
}
private void onDispose() {
	left = null;
	right = null;
}

private void onPaint(GC gc) {
	if (curve == null) updateCurve();
	Point size = getSize();
	int[] shape = new int[curve.length+12];
	int index = 0;
	int x = curveStart;
	int y = 0;
	shape[index++] = 0;
	shape[index++] = size.y - BORDER_STRIPE;
	shape[index++] = x + 1;
	shape[index++] = size.y - BORDER_STRIPE;
	for (int i = 0; i < curve.length/2; i++) {
		shape[index++]=x+curve[2*i];
		shape[index++]=y+curve[2*i+1];
	}
	shape[index++] = x + CURVE_WIDTH;
	shape[index++] = 0;
	shape[index++] = size.x;
	shape[index++] = 0;
	shape[index++] = size.x;
	shape[index++] = size.y;
	shape[index++] = 0;
	shape[index++] = size.y;
	
	gc.setBackground(getForeground());
	gc.fillPolygon(shape);
	
	shape = new int[curve.length+8];
	index = 0;
	shape[index++] = 0;
	shape[index++] = size.y - BORDER_STRIPE;
	shape[index++] = x + 1;
	shape[index++] = size.y - BORDER_STRIPE;
	for (int i = 0; i < curve.length/2; i++) {
		shape[index++]=x+curve[2*i];
		shape[index++]=y+curve[2*i+1];
	}
	shape[index++] = x + CURVE_WIDTH;
	shape[index++] = 0;
	shape[index++] = 0;
	shape[index++] = 0;
	
	gc.setBackground(getBackground());
	gc.fillPolygon(shape);
}

private void onResize() {
	updateCurve();
	layout();
}

public void setBackground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(BACKGROUND);
	super.setBackground(color);
	redraw();
}

public void setForeground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(FOREGROUND);
	super.setForeground(color);
	redraw();
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note : ViewForm does not use a layout class to size and position its children.
 * </p>
 *
 * @param the receiver's new layout or null
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

public void setLeft(Control leftControl) {
	checkWidget();
	if (leftControl != null && leftControl.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (left != null && !left.isDisposed()) {
		left.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	left = leftControl;
	layout();
}
public void setRight(Control rightControl) {
	checkWidget();
	if (rightControl != null && rightControl.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (right != null && !right.isDisposed()) {
		right.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	right = rightControl;
	layout();
}
void updateCurve () {
	Point size = getSize();
	curve = bezier(0, size.y - BORDER_STRIPE + 1,
	               CURVE_LEFT, size.y - BORDER_STRIPE + 1,
			       CURVE_WIDTH-CURVE_RIGHT, 0,
	               CURVE_WIDTH, 0,
	               CURVE_WIDTH);
	// workaround to get rid of blip at end of bezier
	int index = -1;
	for (int i = 0; i < curve.length/2; i++) {
		if (curve[2*i+1] > size.y - BORDER_STRIPE) {
			index = i;
		}
	}
	if (index > 0) {
		int[] newCurve = new int[curve.length - 2*(index-1)];
		System.arraycopy(curve, 2*(index-1), newCurve, 0, newCurve.length);
		curve = newCurve;
	}
}
}
