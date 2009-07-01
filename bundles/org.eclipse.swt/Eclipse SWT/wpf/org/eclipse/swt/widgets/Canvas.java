/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 * @see <a href="http://www.eclipse.org/swt/snippets/#canvas">Canvas snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public class Canvas extends Composite {
	Caret caret;
	IME ime;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Canvas () {
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void clearArea (int x, int y, int width, int height) {
	checkWidget ();
//	if (OS.IsWindowVisible (handle)) {
//		RECT rect = new RECT ();
//		OS.SetRect (rect, x, y, x + width, y + height);
//		int hDC = OS.GetDCEx (handle, 0, OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS);
//		drawBackground (hDC, rect);
//		OS.ReleaseDC (handle, hDC);
//	}
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret for the receiver, may be null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget ();
	return caret;
}

int getCaretHandle () {
	if (caret != null && !caret.isDisposed()) {
		return caret.handle;
	}
	return 0;
}

/**
 * Returns the IME.
 *
 * @return the IME
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public IME getIME () {
	checkWidget ();
	return ime;
}

void releaseChildren (boolean destroy) {
	super.releaseChildren (destroy);
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	if (ime != null) {
		ime.release (false);
		ime = null;
	}
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void drawBackground (GC gc, int x, int y, int width, int height) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	Control control = findBackgroundControl ();
	if (control != null) {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		Point pt = display.map (this, control, 0, 0);
		int transform = OS.gcnew_TranslateTransform (-pt.x, -pt.y);
		OS.DrawingContext_PushTransform (gc.handle, transform);
		x += pt.x;
		y += pt.y;
		int rect = OS.gcnew_Rect (x, y, width, height);
		int backgroundHandle = control.backgroundHandle ();
		int property = backgroundProperty ();
		int brush = OS.DependencyObject_GetValue (backgroundHandle, property);
		OS.GCHandle_Free (property);
		OS.DrawingContext_DrawRectangle (gc.handle, brush, 0, rect);
		OS.DrawingContext_Pop (gc.handle);
		OS.GCHandle_Free (transform);
		OS.GCHandle_Free (rect);
		OS.GCHandle_Free (brush);
	} else {
		gc.fillRectangle (x, y, width, height);
	}

}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget ();
//	boolean isFocus = caret != null && caret.isFocusCaret ();
//	if (isFocus) caret.killFocus ();
	//TODO 
	redraw (false);
//	if (isFocus) caret.setFocus ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget ();
	if (caret != null && caret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int children = OS.Panel_Children (handle);
	if (this.caret != null) {
		OS.UIElementCollection_Remove (children, this.caret.handle);
	}
	this.caret = caret;
	if (caret != null) {
		OS.UIElementCollection_Insert (children, 0, caret.handle);
		if (hasFocus ()) caret.show (); 
	}
	OS.GCHandle_Free (children);
}

public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

/**
 * Sets the receiver's IME.
 * 
 * @param ime the new IME for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the IME has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setIME (IME ime) {
	checkWidget ();
	if (ime != null && ime.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.ime = ime;
}

void HandlePreviewGotKeyboardFocus (int sender, int e) {
	if (!checkEvent (e)) return;
	super.HandlePreviewGotKeyboardFocus (sender, e);
	if (caret != null) caret.show ();
}

void HandleLostKeyboardFocus (int sender, int e) {
	if (!checkEvent (e)) return;
	super.HandleLostKeyboardFocus (sender, e);
	if (caret != null) caret.hide ();
}

}
