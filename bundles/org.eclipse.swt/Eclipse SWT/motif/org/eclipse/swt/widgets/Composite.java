/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>, <code>NO_MERGE_PAINTS</code>,
 * and <code>NO_REDRAW_RESIZE</code> styles are intended for use with <code>Canvas</code>.
 * They can be used with <code>Composite</code> if you are drawing your own, but their
 * behavior is undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p><p>
 * Note: The <code>CENTER</code> style, although undefined for composites, has the
 * same value as <code>EMBEDDED</code> which is used to embed widgets from other
 * widget toolkits into SWT.  On some operating systems (GTK, Motif), this may cause
 * the children of this composite to be obscured.
 * </p><p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 * @see <a href="http://www.eclipse.org/swt/snippets/#composite">Composite snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Composite extends Scrollable {
	Layout layout;
	public int embeddedHandle;
	int focusHandle, damagedRegion, clientWindow;
	Control [] tabList;
	int layoutCount, backgroundMode;
	
	static byte [] _XEMBED_INFO = Converter.wcsToMbcs (null, "_XEMBED_INFO", true);
	static byte[] _XEMBED = Converter.wcsToMbcs (null, "_XEMBED", true);

Composite () {
	/* Do nothing */
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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_FOCUS
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#EMBEDDED
 * @see SWT#DOUBLE_BUFFERED
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, style);
}
static int checkStyle (int style) {
	style &= ~SWT.TRANSPARENT;
	return style;
}
Control [] _getChildren () {
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return new Control [0];
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	int length = focusHandle != 0 ? count - 1 : count;
	Control [] children = new Control [length];
	int i = 0, j = 0;
	while (i < count) {
		int handle = handles [i];
		if (handle != 0) {
			Widget widget = display.getWidget (handle);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [j++] = (Control) widget;
				}
			}
		}
		i++;
	}
	if (j == length) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}
Control [] _getTabList () {
	if (tabList == null) return tabList;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) count++;
	}
	if (count == tabList.length) return tabList;
	Control [] newList = new Control [count];
	int index = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) {
			newList [index++] = tabList [i];
		}
	}
	tabList = newList;
	return tabList;
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	display.runSkin();
	Point size;
	if (layout != null) {
		if ((wHint == SWT.DEFAULT) || (hHint == SWT.DEFAULT)) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			size = layout.computeSize (this, wHint, hHint, changed);
			state &= ~LAYOUT_CHANGED;
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize (wHint, hHint, changed);
		if (size.x == 0) size.x = DEFAULT_WIDTH;
		if (size.y == 0) size.y = DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}
Control [] computeTabList () {
	Control result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? _getTabList () : _getChildren ();
	for (int i=0; i<list.length; i++) {
		Control child = list [i];
		Control [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Control [] newResult = new Control [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	return result;
}
/**
 * Clears any data that has been cached by a Layout for all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  If an ancestor does not have a layout, it is skipped.
 * 
 * @param changed an array of controls that changed state and require a recalculation of size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void changed (Control[] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<changed.length; i++) {
		Control control = changed [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		boolean ancestor = false;
		Composite composite = control.parent;
		while (composite != null) {
			ancestor = composite == this;
			if (ancestor) break;
			composite = composite.parent;
		}
		if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
	}
	for (int i=0; i<changed.length; i++) {
		Control child = changed [i];
		Composite composite = child.parent;
		while (child != this) {
			if (composite.layout == null || !composite.layout.flushCache (child)) {
				composite.state |= LAYOUT_CHANGED;
			}
			child = composite;
			composite = child.parent;
		}
	}
}
void checkBuffered () {
	if ((state & CANVAS) == 0) {
		super.checkBuffered ();
	}
}
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}
void createHandle (int index) {
	state |= CANVAS;
	boolean scroll = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	if (!scroll) state |= THEME_BACKGROUND;
	createHandle (index, parent.handle, scroll);
}
void createHandle (int index, int parentHandle, boolean scrolled) {
	if (scrolled) {
		int [] argList = {OS.XmNancestorSensitive, 1};
		scrolledHandle = OS.XmCreateMainWindow (parentHandle, null, argList, argList.length / 2);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		int [] argList1 = {
			OS.XmNmarginWidth, 3,
			OS.XmNmarginHeight, 3, 
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNshadowType, OS.XmSHADOW_IN,
			OS.XmNshadowThickness, hasBorder () ? display.buttonShadowThickness : 0,
		};
		formHandle = OS.XmCreateForm (scrolledHandle, null, argList1, argList1.length / 2);
		if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = {
			OS.XmNtopAttachment, OS.XmATTACH_FORM,
			OS.XmNbottomAttachment, OS.XmATTACH_FORM,
			OS.XmNleftAttachment, OS.XmATTACH_FORM,
			OS.XmNrightAttachment, OS.XmATTACH_FORM,
			OS.XmNresizable, 0,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		handle = OS.XmCreateDrawingArea (formHandle, null, argList2, argList2.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNborderWidth, hasBorder () ? 1 : 0,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		if (scrolledHandle != 0) parentHandle = scrolledHandle;
		handle = OS.XmCreateDrawingArea (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	int [] argList = {OS.XmNtraversalOn, 0};
	focusHandle = OS.XmCreateDrawingArea (handle, null, argList, argList.length / 2);
	if (focusHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int [] argList1 = {OS.XmNforeground, 0, OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	if (formHandle != 0) OS.XtSetValues (formHandle, argList1, argList1.length / 2);
}
int defaultBackground () {
	return display.compositeBackground;
}
int defaultForeground () {
	return display.compositeForeground;
}
void deregister () {
	super.deregister ();
	if (focusHandle != 0) display.removeWidget (focusHandle);
}
void drawBackground (GC gc, int x, int y, int width, int height) {
	drawBackground(gc, x, y, width, height, 0, 0);
}
/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * <p>The <code>offsetX</code> and <code>offsetY</code> are used to map from
 * the <code>gc</code> origin to the origin of the parent image background. This is useful
 * to ensure proper alignment of the image background.</p>
 * 
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param offsetX the image background x offset 
 * @param offsetY the image background y offset
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
 * @since 3.6
 */
public void drawBackground (GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	Control control = findBackgroundControl ();
	if (control != null) {
		GCData data = gc.getGCData ();
		long /*int*/ cairo = data.cairo;
		if (cairo != 0) {
			Cairo.cairo_save (cairo);
			if (control.backgroundImage != null) {
				short [] root_x = new short [1], root_y = new short [1];
				OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
				short [] control_x = new short [1], control_y = new short [1];
				OS.XtTranslateCoords (control.handle, (short) 0, (short) 0, control_x, control_y);
				int tileX = root_x[0] - control_x[0], tileY = root_y[0] - control_y[0];
				Cairo.cairo_translate (cairo, -tileX - offsetX, -tileY - offsetY);
				x += tileX + offsetX;
				y += tileY + offsetY;
				int xDisplay = OS.XtDisplay (handle);
				int xVisual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
				int xDrawable = control.backgroundImage.pixmap;				
				int [] unused = new int [1];  int [] w = new int [1];  int [] h = new int [1];
			 	OS.XGetGeometry (xDisplay, xDrawable, unused, unused, unused, w, h, unused, unused);
				long /*int*/ surface = Cairo.cairo_xlib_surface_create (xDisplay, xDrawable, xVisual, w [0], h [0]);
				if (surface == 0) error (SWT.ERROR_NO_HANDLES);
				long /*int*/ pattern = Cairo.cairo_pattern_create_for_surface (surface);
				if (pattern == 0) error (SWT.ERROR_NO_HANDLES);
				Cairo.cairo_pattern_set_extend (pattern, Cairo.CAIRO_EXTEND_REPEAT);
				Cairo.cairo_set_source (cairo, pattern);
				Cairo.cairo_surface_destroy (surface);
				Cairo.cairo_pattern_destroy (pattern);
			} else {
				XColor color = getXColor (control.getBackgroundPixel ());
				Cairo.cairo_set_source_rgba (cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
			}			
			Cairo.cairo_rectangle (cairo, x, y, width, height);
			Cairo.cairo_fill (cairo);
			Cairo.cairo_restore (cairo);
		} else {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xGC = gc.handle;
			XGCValues values = new XGCValues();
			if (control.backgroundImage != null) {
				OS.XGetGCValues (xDisplay, xGC, OS.GCFillStyle | OS.GCTile | OS.GCTileStipXOrigin | OS.GCTileStipYOrigin, values);
				short [] root_x = new short [1], root_y = new short [1];
				OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
				short [] control_x = new short [1], control_y = new short [1];
				OS.XtTranslateCoords (control.handle, (short) 0, (short) 0, control_x, control_y);
				int tileX = root_x[0] - control_x[0], tileY = root_y[0] - control_y[0];
				OS.XSetFillStyle (xDisplay, xGC, OS.FillTiled);
				OS.XSetTSOrigin (xDisplay, xGC, -tileX - offsetX, -tileY - offsetY);
				OS.XSetTile (xDisplay, xGC, control.backgroundImage.pixmap);
				OS.XFillRectangle (data.display, data.drawable, xGC, x, y, width, height);
				OS.XSetFillStyle (xDisplay, xGC, values.fill_style);
				OS.XSetTSOrigin (xDisplay, xGC, values.ts_x_origin, values.ts_y_origin);
			} else {
				OS.XGetGCValues (xDisplay, xGC, OS.GCForeground, values);
				OS.XSetForeground (xDisplay, xGC, control.getBackgroundPixel ());
				OS.XFillRectangle (data.display, data.drawable, xGC, x, y, width, height);
				OS.XSetForeground (xDisplay, xGC, values.foreground);
			}
		}
	} else {
		gc.fillRectangle (x, y, width, height);
	}
}
Composite findDeferredControl () {
	return layoutCount > 0 ? this : parent.findDeferredControl ();
}
void fixTabList (Control control) {
	if (tabList == null) return;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (tabList [i] == control) count++;
	}
	if (count == 0) return;
	Control [] newList = null;
	int length = tabList.length - count;
	if (length != 0) {
		newList = new Control [length];
		int index = 0;
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] != control) {
				newList [index++] = tabList [i];
			}
		}
	}
	tabList = newList;
}
int focusHandle () {
	if (focusHandle == 0) return super.focusHandle ();
	return focusHandle;
}
int focusProc (int w, int client_data, int call_data, int continue_to_dispatch) {
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, call_data, XFocusChangeEvent.sizeof);
	int handle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	Shell shell = getShell ();
	if (handle != shell.shellHandle) {
		return super.XFocusChange (w, client_data, call_data, continue_to_dispatch);
	}
	if (xEvent.mode != OS.NotifyNormal) return 0;
	switch (xEvent.detail) {
		case OS.NotifyNonlinear:
		case OS.NotifyNonlinearVirtual: {
			switch (xEvent.type) {
				case OS.FocusIn: 
					sendClientEvent (OS.CurrentTime, OS.XEMBED_WINDOW_ACTIVATE, 0, 0, 0);	
					break;
				case OS.FocusOut:
					sendClientEvent (OS.CurrentTime, OS.XEMBED_WINDOW_DEACTIVATE, 0, 0, 0);	
					break;
			}
		}
	}
	return 0;
}
boolean fowardKeyEvent (int event) {
	if (clientWindow == 0) return false;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, event, XKeyEvent.sizeof);
	xEvent.window = clientWindow;
	int newEvent = OS.XtMalloc (XEvent.sizeof);
	OS.memmove (newEvent, xEvent, XKeyEvent.sizeof);
	int xDisplay = OS.XtDisplay (handle);
	OS.XSendEvent (xDisplay, clientWindow, false, 0, newEvent);
	OS.XSync (xDisplay, false);
	OS.XtFree (newEvent);
	display.setWarnings (warnings);
	return true;
}
/**
 * Returns the receiver's background drawing mode. This
 * will be one of the following constants defined in class
 * <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERTIT_FORCE</code>.
 *
 * @return the background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 * 
 * @since 3.2
 */
public int getBackgroundMode () {
	checkWidget ();
	return backgroundMode;
}
/**
 * Returns a (possibly empty) array containing the receiver's children.
 * Children are returned in the order that they are drawn.  The topmost
 * control appears at the beginning of the array.  Subsequent controls
 * draw beneath this control and appear later in the array.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
 * 
 * @see Control#moveAbove
 * @see Control#moveBelow
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget();
	return _getChildren ();
}
public Rectangle getClientArea () {
	checkWidget();
	return _getClientArea();
}

Rectangle _getClientArea() {
	/*
	* Bug in Motif. For some reason, if a form has not been realized,
	* calling XtResizeWidget () on the form does not lay out properly.
	* The fix is to force the widget to be realized by forcing the shell
	* to be realized. 
	*/
	if (formHandle != 0) {
		if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();
	}
	return super.getClientArea ();
}
int getChildrenCount () {
	/*
	* NOTE:  The current implementation will count
	* non-registered children.
	*/
	int [] argList = {OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (focusHandle != 0) return Math.max (0, argList [1] - 1);
	return argList [1];
}
/**
 * Returns layout which is associated with the receiver, or
 * null if one has not been set.
 *
 * @return the receiver's layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Layout getLayout () {
	checkWidget();
	return layout;
}
/**
 * Returns <code>true</code> if the receiver has deferred
 * the performing of layout, and <code>false</code> otherwise.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #isLayoutDeferred()
 *
 * @since 3.1
 */
public boolean getLayoutDeferred () {
	checkWidget ();
	return layoutCount > 0 ;
}
/**
 * Gets the (possibly empty) tabbing order for the control.
 *
 * @return tabList the ordered list of controls representing the tab order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setTabList
 */
public Control [] getTabList () {
	checkWidget ();
	Control [] tabList = _getTabList ();
	if (tabList == null) {
		int count = 0;
		Control [] list =_getChildren ();
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) count++;
		}
		tabList = new Control [count];
		int index = 0;
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) {
				tabList [index++] = list [i];
			}
		}
	}
	return tabList;
}
boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}
void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		OS.XtInsertEventHandler (handle, 0, true, display.windowProc, NON_MASKABLE, OS.XtListTail);
	}
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		int focusProc = display.focusProc;
		int windowProc = display.windowProc;
		OS.XtInsertEventHandler (handle, OS.StructureNotifyMask | OS.SubstructureNotifyMask, false, windowProc, STRUCTURE_NOTIFY, OS.XtListTail);
		OS.XtInsertEventHandler (handle, OS.PropertyChangeMask, false, windowProc, PROPERTY_CHANGE, OS.XtListTail);
		OS.XtInsertEventHandler (handle, 0, true, windowProc, NON_MASKABLE, OS.XtListTail);
		Shell shell = getShell ();
		OS.XtInsertEventHandler (shell.shellHandle, OS.FocusChangeMask, false, focusProc, handle, OS.XtListTail);
	}
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}
/**
 * Returns <code>true</code> if the receiver or any ancestor 
 * up to and including the receiver's nearest ancestor shell
 * has deferred the performing of layouts.  Otherwise, <code>false</code>
 * is returned.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #getLayoutDeferred()
 * 
 * @since 3.1
 */
public boolean isLayoutDeferred () {
	checkWidget ();
	return findDeferredControl () != null;
}
boolean isTabGroup () {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup ();
}
/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the receiver does not have a layout, do nothing.
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	checkWidget ();
	layout (true);
}
/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the argument is <code>true</code> the layout must not rely
 * on any information it has cached about the immediate children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * If a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  The layout
 * will cascade down through all child widgets in the receiver's widget 
 * tree until a child is encountered that does not resize.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget ();
	if (layout == null) return;
	layout (changed, false);
}
/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the changed argument is <code>true</code> the layout must not rely
 * on any information it has cached about its children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the all argument is <code>true</code> the layout will cascade down
 * through all child widgets in the receiver's widget tree, regardless of
 * whether the child has changed size.  The changed argument is applied to 
 * all layouts.  If the all argument is <code>false</code>, the layout will
 * <em>not</em> cascade down through all child widgets in the receiver's widget 
 * tree.  However, if a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (boolean changed, boolean all) {
	checkWidget ();
	if (layout == null && !all) return;
	markLayout (changed, all);
	updateLayout (all);
}
/**
 * Forces a lay out (that is, sets the size and location) of all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  The layouts in the hierarchy must not rely on any information 
 * cached about the changed control or any of its ancestors.  The layout may 
 * (potentially) optimize the work it is doing by assuming that none of the 
 * peers of the changed control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @param changed a control that has had a state change which requires a recalculation of its size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (Control [] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	layout (changed, SWT.NONE);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver. 
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be laid out</dd>
 * <dt><b>SWT.CHANGED</b></dt>
 * <dd>the layout must flush its caches</dd>
 * <dt><b>SWT.DEFER</b></dt>
 * <dd>layout will be deferred</dd>
 * </dl>
 * </p>
 * <p>
 * When the <code>changed</code> array is specified, the flags <code>SWT.ALL</code>
 * and <code>SWT.CHANGED</code> have no effect. In this case, the layouts in the 
 * hierarchy must not rely on any information cached about the changed control or
 * any of its ancestors.  The layout may (potentially) optimize the
 * work it is doing by assuming that none of the peers of the changed
 * control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * </p>
 * <p>
 * When the <code>changed</code> array is not specified, the flag <code>SWT.ALL</code>
 * indicates that the whole widget tree should be laid out. And the flag
 * <code>SWT.CHANGED</code> indicates that the layouts should flush any cached
 * information for all controls that are laid out. 
 * </p>
 * <p>
 * The <code>SWT.DEFER</code> flag always causes the layout to be deferred by
 * calling <code>Composite.setLayoutDeferred(true)</code> and scheduling a call
 * to <code>Composite.setLayoutDeferred(false)</code>, which will happen when
 * appropriate (usually before the next event is handled). When this flag is set,
 * the application should not call <code>Composite.setLayoutDeferred(boolean)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @param changed a control that has had a state change which requires a recalculation of its size
 * @param flags the flags specifying how the layout should happen
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the controls in changed is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public void layout (Control [] changed, int flags) {
	checkWidget ();
	if (changed != null) {
		for (int i=0; i<changed.length; i++) {
			Control control = changed [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			boolean ancestor = false;
			Composite composite = control.parent;
			while (composite != null) {
				ancestor = composite == this;
				if (ancestor) break;
				composite = composite.parent;
			}
			if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
		}
		int updateCount = 0;
		Composite [] update = new Composite [16];
		for (int i=0; i<changed.length; i++) {
			Control child = changed [i];
			Composite composite = child.parent;
			while (child != this) {
				if (composite.layout != null) {
					composite.state |= LAYOUT_NEEDED;
					if (!composite.layout.flushCache (child)) {
						composite.state |= LAYOUT_CHANGED;
					}
				}
				if (updateCount == update.length) {
					Composite [] newUpdate = new Composite [update.length + 16];
					System.arraycopy (update, 0, newUpdate, 0, update.length);
					update = newUpdate;
				}
				child = update [updateCount++] = composite;
				composite = child.parent;
			}
		}
		if ((flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		for (int i=updateCount-1; i>=0; i--) {
			update [i].updateLayout (false);
		}
	} else {
		if (layout == null && (flags & SWT.ALL) == 0) return;
		markLayout ((flags & SWT.CHANGED) != 0, (flags & SWT.ALL) != 0);
		if ((flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		updateLayout ((flags & SWT.ALL) != 0);
	}
}
void manageChildren () {
	if (focusHandle != 0) {	
		OS.XtSetMappedWhenManaged (focusHandle, false);
		OS.XtManageChild (focusHandle);
	}
	super.manageChildren ();
	if (focusHandle != 0) {
		OS.XtConfigureWidget(focusHandle, 0, 0, 1, 1, 0);
		OS.XtSetMappedWhenManaged (focusHandle, true);
	}
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		Shell shell = getShell ();
		shell.createFocusProxy ();
		if (!OS.XtIsRealized (handle)) shell.realizeWidget ();
		int xDisplay = OS.XtDisplay (handle);
		OS.XSync (xDisplay, false);
		embeddedHandle = OS.XtWindow (handle);
	}
}
void markLayout (boolean changed, boolean all) {
	if (layout != null) {
		state |= LAYOUT_NEEDED;
		if (changed) state |= LAYOUT_CHANGED;
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].markLayout (changed, all);
		}
	}
}
Point minimumSize (int wHint, int hHint, boolean changed) {
	Control [] children = _getChildren ();
	Rectangle clientArea = getClientArea ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x - clientArea.x + rect.width);
		height = Math.max (height, rect.y - clientArea.y + rect.height);
	}
	return new Point (width, height);
}
void moveAbove (int handle1, int handle2) {
	if (handle1 == handle2) return;
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return;
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	if (handle2 == 0) handle2 = handles [0];
	int i = 0, index1 = -1, index2 = -1;
	while (i < count) {
		int handle = handles [i];
		if (handle == handle1) index1 = i;
		if (handle == handle2) index2 = i;
		if (index1 != -1 && index2 != -1) break;
		i++;
	}
	if (index1 == -1 || index2 == -1) return;
	if (index1 == index2) return;
	if (index1 < index2) {
		System.arraycopy (handles, index1 + 1, handles, index1, index2 - index1 - 1);
		handles [index2 - 1] = handle1;
	} else {
		System.arraycopy (handles, index2, handles, index2 + 1, index1 - index2);
		handles [index2] = handle1;
	}
	OS.memmove (ptr, handles, count * 4);
}
void moveBelow (int handle1, int handle2) {
	if (handle1 == handle2) return;
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return;
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	if (handle2 == 0) handle2 = handles [count - 1];
	int i = 0, index1 = -1, index2 = -1;
	while (i < count) {
		int handle = handles [i];
		if (handle == handle1) index1 = i;
		if (handle == handle2) index2 = i;
		if (index1 != -1 && index2 != -1) break;
		i++;
	}
	if (index1 == -1 || index2 == -1) return;
	if (index1 == index2) return;
	if (index1 < index2) {
		System.arraycopy (handles, index1 + 1, handles, index1, index2 - index1);
		handles [index2] = handle1;
	} else {
		System.arraycopy (handles, index2 + 1, handles, index2 + 2, index1 - index2 - 1);
		handles [index2 + 1] = handle1;
	}
	OS.memmove (ptr, handles, count * 4);
}
void propagateChildren (boolean enabled) {
	super.propagateChildren (enabled);
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		if (child.getEnabled ()) {
			child.propagateChildren (enabled);
		}
	}
}
void realizeChildren () {
	super.realizeChildren ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		children [i].realizeChildren ();
	}
	/*
	* Feature in Motif.  XmProcessTraversal() will not give focus to
	* a widget that is obscured so the focus handle must be inside the
	* client area of the parent.  This means that it is visible as a
	* single pixel widget in the parent.  The fix is to unmap the
	* focus handle so that it will be traversed by XmProcessTraversal()
	* and will accept focus but will not be visible in the parent.
	*/
	if (focusHandle != 0) OS.XtUnmapWidget (focusHandle);
	if ((state & CANVAS) != 0) {
		if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND)) != 0 || (style & SWT.NO_REDRAW_RESIZE) == 0) {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			int flags = 0;
			XSetWindowAttributes attributes = new XSetWindowAttributes ();
			if ((style & (SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED)) != 0) {
				flags |= OS.CWBackPixmap;
				attributes.background_pixmap = OS.None;
			}
			if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
				flags |= OS.CWBitGravity;
				attributes.bit_gravity = OS.ForgetGravity;
			}
			if (flags != 0) {
				OS.XChangeWindowAttributes (xDisplay, xWindow, flags, attributes);
			}
		}
	}
}
void redrawChildren () {
	super.redrawChildren ();
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		if ((child.state & PARENT_BACKGROUND) != 0) {
			child.redrawWidget (0, 0, 0, 0, true, false, true);
			child.redrawChildren ();
		}
	}
}
void register () {
	super.register ();
	if (focusHandle != 0) display.addWidget (focusHandle, this);
}
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean allChildren, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, allChildren, trim);
	if (!allChildren) return;
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		Point location = child.getClientLocation ();
		child.redrawWidget (x - location.x, y - location.y, width, height, redrawAll, allChildren, true);
	}
}
void release (boolean destroy) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		Shell shell = getShell ();
		int focusProc = display.focusProc;
		OS.XtRemoveEventHandler (shell.shellHandle, OS.FocusChangeMask, false, focusProc, handle);
		if (clientWindow != 0) {
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			int xDisplay = OS.XtDisplay (handle);
			OS.XUnmapWindow (xDisplay, clientWindow);
			OS.XReparentWindow (xDisplay, clientWindow, OS.XDefaultRootWindow (xDisplay), 0, 0);
			OS.XSync (xDisplay, false);
			display.setWarnings (warnings);
		}
		setClientWindow (0);
	}
	super.release (destroy);
}
void releaseChildren (boolean destroy) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) {
			child.release (false);
		}
	}
	super.releaseChildren (destroy);
}
void releaseHandle () {
	super.releaseHandle ();
	focusHandle = embeddedHandle = 0;
}
void releaseWidget () {
	super.releaseWidget ();
	layout = null;
	tabList = null;
	if (damagedRegion != 0) OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
}
void removeControl (Control control) {
	fixTabList (control);
}
void reskinChildren (int flags) {
	super.reskinChildren (flags);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null) child.reskin (flags);
	}
}
void resizeClientWindow	() {
	if (clientWindow == 0) return;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (handle);
	OS.XMoveResizeWindow (xDisplay, clientWindow, 0, 0, Math.max(1, argList [1]), Math.max(1, argList [3]));
	display.setWarnings (warnings);
}
void sendClientEvent (int time, int message, int detail, int data1, int data2) {
	if (clientWindow == 0) return;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int xDisplay = OS.XtDisplay (handle);
	XClientMessageEvent xEvent = new XClientMessageEvent ();
	xEvent.type = OS.ClientMessage;
	xEvent.window = clientWindow;
	xEvent.message_type = OS.XInternAtom (xDisplay, _XEMBED, false);
	xEvent.format = 32;
	xEvent.data [0] = time != 0 ? time : OS.XtLastTimestampProcessed (xDisplay);
	xEvent.data [1] = message;
	xEvent.data [2] = detail;
	xEvent.data [3] = data1;
	xEvent.data [4] = data2;
	int event = OS.XtMalloc (XEvent.sizeof);
	OS.memmove (event, xEvent, XClientMessageEvent.sizeof);
	OS.XSendEvent (xDisplay, clientWindow, false, 0, event);
	OS.XSync (xDisplay, false);
	OS.XtFree (event);
	display.setWarnings (warnings);
}
/**
 * Sets the background drawing mode to the argument which should
 * be one of the following constants defined in class <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERIT_FORCE</code>.
 *
 * @param mode the new background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 * 
 * @since 3.2
 */
public void setBackgroundMode (int mode) {
	checkWidget ();
	backgroundMode = mode;
	Control[] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();
	}
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if ((state & CANVAS) != 0) {
		if ((style & (SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED)) != 0) {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			XSetWindowAttributes attributes = new XSetWindowAttributes ();
			attributes.background_pixmap = OS.None;
			OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWBackPixmap, attributes);
		}
	}
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) {
		if (focusHandle != 0) {
			int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
			OS.XtGetValues (handle, argList, argList.length / 2);
			OS.XtConfigureWidget (focusHandle, 0, 0, argList [1], argList [3], 0);
		}
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false);
		}
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			resizeClientWindow ();
		}
	}
	return changed;
}
void setClientWindow (int window) {
	if (focusHandle != 0) {
		if (window == OS.XtWindow (focusHandle)) return;
	}
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int xDisplay = OS.XtDisplay (handle);
	if (window != 0) {
		if (clientWindow == 0) {
			clientWindow = window;
			sendClientEvent (0, OS.XEMBED_EMBEDDED_NOTIFY, 0, 0, 0);
			OS.XtRegisterDrawable (xDisplay, clientWindow, handle);
			OS.XSelectInput (xDisplay, clientWindow, OS.PropertyChangeMask);
			updateMapped ();		
			resizeClientWindow ();
			Shell shell = getShell ();
			if (shell == display.getActiveShell ()) {
				shell.bringToTop (true);
				sendClientEvent (0, OS.XEMBED_WINDOW_ACTIVATE, 0, 0, 0);
				if (this == display.getFocusControl ()) {
					sendClientEvent (0, OS.XEMBED_FOCUS_IN, OS.XEMBED_FOCUS_CURRENT, 0, 0);
				}
			}
		}
	} else {
		if (clientWindow != 0) OS.XtUnregisterDrawable (xDisplay, clientWindow);
		clientWindow = 0;
	}
	display.setWarnings (warnings);
}
public boolean setFocus () {
	checkWidget ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setFocus ()) return true;
	}
	return super.setFocus ();
}
public void setFont (Font font) {
	checkWidget();
	super.setFont (font);
	if ((state & CANVAS) != 0) {
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		int xWindow = OS.XtWindow (handle);
		if (xWindow == 0) return;
		OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
	}
}
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	if ((state & CANVAS) != 0) {
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		int xWindow = OS.XtWindow (handle);
		if (xWindow == 0) return;
		OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
	}
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
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
	this.layout = layout;
}
/**
 * If the argument is <code>true</code>, causes subsequent layout
 * operations in the receiver or any of its children to be ignored.
 * No layout of any kind can occur in the receiver or any of its
 * children until the flag is set to false.
 * Layout operations that occurred while the flag was
 * <code>true</code> are remembered and when the flag is set to 
 * <code>false</code>, the layout operations are performed in an
 * optimized manner.  Nested calls to this method are stacked.
 *
 * @param defer the new defer state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #layout(boolean)
 * @see #layout(Control[])
 *
 * @since 3.1
 */
public void setLayoutDeferred (boolean defer) {
	checkWidget();
	if (!defer) {
		if (--layoutCount == 0) {
			if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
				updateLayout (true);
			}
		}
	} else {
		layoutCount++;
	}
}
/**
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabList (Control [] tabList) {
	checkWidget ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			Control control = tabList [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
		}
		Control [] newList = new Control [tabList.length];
		System.arraycopy (tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	} 
	this.tabList = tabList;
}
void setParentBackground () {
	super.setParentBackground ();	
	if (scrolledHandle != 0) setParentBackground (scrolledHandle);
	if (formHandle != 0) setParentBackground (formHandle);
}
boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	boolean changed = super.setScrollBarVisible (bar, visible);
	if (changed && layout != null) {
		markLayout (false, false);
		updateLayout (false);
	}
	return changed;
}
boolean setTabGroupFocus (boolean next) {
	if (isTabItem ()) return setTabItemFocus (next);
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) {
		takeFocus = hooksKeys ();
		if ((style & SWT.EMBEDDED) != 0) takeFocus = true;
	}
	if (takeFocus && setTabItemFocus (next)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus (next)) return true;
	}
	return false;
}
boolean setTabItemFocus (boolean next) {
	if (!super.setTabItemFocus (next)) return false;
	if (handle != 0) {
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			int detail = next ? OS.XEMBED_FOCUS_FIRST : OS.XEMBED_FOCUS_LAST;
			sendClientEvent (0, OS.XEMBED_FOCUS_IN, detail, 0, 0);
		}
	}
	return true;
}
int traversalCode (int key, XKeyEvent xEvent) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, xEvent);
}
boolean translateMnemonic (Event event, Control control) {
	if (super.translateMnemonic (event, control)) return true;
	if (control != null) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			if (child.translateMnemonic (event, control)) return true;
		}
	}
	return false;
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) return false;
	return super.translateTraversal (key, xEvent);
}
void updateBackgroundMode () {
	super.updateBackgroundMode ();
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();
	}
}
void updateLayout (boolean all) {
	Composite parent = findDeferredControl ();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin();
		layout.layout (this, changed);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].updateLayout (all);
		}
	}
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;

	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		XButtonEvent xEvent = new XButtonEvent ();
		OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			if (xEvent.button == 1) {
				if (getChildrenCount () == 0) setFocus ();
			}
		}
		/*
		* Bug in Motif. On Solaris 8 only, stopping the other event
		* handlers from being invoked after a menu has been displayed
		* causes a segment fault.  The fix is to not stop the event for
		* button 3.
		*/
		if (xEvent.button != 3) {
			OS.memmove (continue_to_dispatch, new int [1], 4);
		}
		return 1;
	}
	return result;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	if ((state & CANVAS) == 0) {
		return super.XExposure (w, client_data, call_data, continue_to_dispatch);
	}
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	if (damagedRegion == 0) damagedRegion = OS.XCreateRegion ();
	OS.XtAddExposureToRegion (call_data, damagedRegion);
	if ((style & SWT.NO_MERGE_PAINTS) == 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
		int exposeCount = xEvent.count;
		if (exposeCount == 0) {
			if (OS.XEventsQueued (xEvent.display, OS.QueuedAfterReading) != 0) {
				int xEvent1 = OS.XtMalloc (XEvent.sizeof);
				display.exposeCount = display.lastExpose = 0;
				int checkExposeProc = display.checkExposeProc;
				OS.XCheckIfEvent (xEvent.display, xEvent1, checkExposeProc, xEvent.window);
				exposeCount = display.exposeCount;
				int lastExpose = display.lastExpose;
				if (exposeCount != 0 && lastExpose != 0) {
					XExposeEvent xExposeEvent = display.xExposeEvent;
					OS.memmove (xExposeEvent, lastExpose, XExposeEvent.sizeof);
					xExposeEvent.count = 0;
					OS.memmove (lastExpose, xExposeEvent, XExposeEvent.sizeof);
				}
				OS.XtFree (xEvent1);
			}
		}
		if (exposeCount != 0) return 0;
	}
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int damageRgn = damagedRegion;
	damagedRegion = 0;
	GCData data = new GCData ();
	data.damageRgn = damageRgn;
	GC gc = GC.motif_new (this, data);
	OS.XSetRegion (xDisplay, gc.handle, damageRgn);
	XRectangle rect = new XRectangle ();
	OS.XClipBox (damageRgn, rect);
	GC paintGC = null;
	Image image = null;
	if ((style & SWT.DOUBLE_BUFFERED) != 0) {
		Rectangle client = _getClientArea ();
		int width = Math.max (1, Math.min (client.width, rect.x + rect.width));
		int height = Math.max (1, Math.min (client.height, rect.y + rect.height));
		image = new Image (display, width, height);
		paintGC = gc;
		GCData imageGCData = new GCData ();
		imageGCData.damageRgn = damageRgn; 
		gc = GC.motif_new (image, imageGCData);
		gc.setForeground (getForeground ());
		gc.setBackground (getBackground ());
		gc.setFont (getFont ());
		if ((style & SWT.NO_BACKGROUND) != 0) {
			/* This code is intentionaly commented because it is too slow to copy bits from the screen */
//			paintGC.copyArea(image, 0, 0);
		} else {
			drawBackground (gc, 0, 0, width, height, 0, 0);
		}
	}
	Event event = new Event ();
	event.x = rect.x;
	event.y = rect.y;
	event.width = rect.width;
	event.height = rect.height;
	event.gc = gc;
	sendEvent (SWT.Paint, event);
	event.gc = null;
	if ((style & SWT.DOUBLE_BUFFERED) != 0) {
		gc.dispose ();
		if (!isDisposed ()) {
			paintGC.drawImage (image, 0, 0);
		}
		image.dispose ();
		gc = paintGC;
	}	
	gc.dispose ();
	OS.XDestroyRegion (damageRgn);
	return 0;
}
int xFocusIn (XFocusChangeEvent xEvent) {
	int result = super.xFocusIn (xEvent);
	if (handle != 0) {
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			sendClientEvent (0, OS.XEMBED_FOCUS_IN, OS.XEMBED_FOCUS_CURRENT, 0, 0);
		}
	}
	return result;
}
int xFocusOut (XFocusChangeEvent xEvent) {
	int result = super.xFocusOut (xEvent);
	if (handle != 0) {
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			sendClientEvent (0, OS.XEMBED_FOCUS_OUT, 0, 0, 0);
		}
	}
	return result;
}
int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XKeyPress (w, client_data, call_data, continue_to_dispatch);
	if (result == 0) {
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			if (fowardKeyEvent (call_data)) return 0;
		}
	}
	return result;
}
int XKeyRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XKeyRelease (w, client_data, call_data, continue_to_dispatch);
	if (result == 0) {
		if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
			if (fowardKeyEvent (call_data)) return 0;
		}
	}
	return result;
}
int XNonMaskable (int w, int client_data, int call_data, int continue_to_dispatch) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		XEvent xEvent = new XEvent ();
		OS.memmove (xEvent, call_data, XEvent.sizeof);
		if (xEvent.type == OS.ClientMessage) {
			XClientMessageEvent xClientEvent = new XClientMessageEvent ();
			OS.memmove (xClientEvent, call_data, XClientMessageEvent.sizeof);
			int xDisplay = OS.XtDisplay (handle);
			if (xClientEvent.message_type == OS.XInternAtom (xDisplay, _XEMBED, false)) {
				int type = xClientEvent.data [1];
				switch (type) {
					case OS.XEMBED_REQUEST_FOCUS: {
						setFocus ();
						break;
					}
					case OS.XEMBED_FOCUS_PREV: {
						traverse (SWT.TRAVERSE_TAB_PREVIOUS);
						break;
					}
					case OS.XEMBED_FOCUS_NEXT: {
						traverse (SWT.TRAVERSE_TAB_NEXT);
						break;
					}
				}
			}
			return 0;
		}
	}
	if ((state & CANVAS) != 0) {
		XEvent xEvent = new XEvent ();
		OS.memmove (xEvent, call_data, XEvent.sizeof);
		if (xEvent.type == OS.GraphicsExpose) {
			return XExposure (w, client_data, call_data, continue_to_dispatch);
		}
	}
	return 0;
}
int XPropertyChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XPropertyChange (w, client_data, call_data, continue_to_dispatch);
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		XPropertyEvent xPropertyEvent = new XPropertyEvent ();
		OS.memmove(xPropertyEvent, call_data, XPropertyEvent.sizeof);
		if (xPropertyEvent.window == clientWindow) {
			int atom = xPropertyEvent.atom;
			int xDisplay = xPropertyEvent.display;
			if (atom == OS.XInternAtom (xDisplay, _XEMBED_INFO, false)) {
				updateMapped ();
			}
		}
	}
	return result;
}
int XStructureNotify (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XStructureNotify (w, client_data, call_data, continue_to_dispatch);
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		XEvent xEvent = new XEvent ();
		OS.memmove (xEvent, call_data, XEvent.sizeof);
		switch (xEvent.type) {
			case OS.ReparentNotify: {
				XReparentEvent xReparentEvent = new XReparentEvent ();
				OS.memmove (xReparentEvent, call_data, XReparentEvent.sizeof);
				if (clientWindow == 0) setClientWindow (xReparentEvent.window);
				break;
			}
			case OS.CreateNotify: {
				XCreateWindowEvent xCreateEvent = new XCreateWindowEvent ();
				OS.memmove (xCreateEvent, call_data, XCreateWindowEvent.sizeof);
				if (clientWindow == 0) setClientWindow (xCreateEvent.window);
				break;
			}
			case OS.DestroyNotify: {
				XDestroyWindowEvent xDestroyEvent = new XDestroyWindowEvent ();
				OS.memmove (xDestroyEvent, call_data, XDestroyWindowEvent.sizeof);
				if (xDestroyEvent.window == clientWindow) setClientWindow (0);
				break;
			}
			case OS.ConfigureNotify: {
				XConfigureEvent xConfigureEvent = new XConfigureEvent ();
				OS.memmove (xConfigureEvent, call_data, XConfigureEvent.sizeof);
				if (xConfigureEvent.window == clientWindow) resizeClientWindow ();
				break;
			}
		}
	}
	return result;
}
void updateMapped () {
	if (clientWindow == 0) return;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int xDisplay = OS.XtDisplay (handle);
	int prop = OS.XInternAtom (xDisplay, _XEMBED_INFO, false);
	int [] type = new int [1], format = new int [1];
	int [] nitems = new int [1], bytes_after = new int [1], data = new int [1];
	if (OS.XGetWindowProperty (xDisplay, clientWindow, prop, 0, 2, false, prop, type, format, nitems, bytes_after, data) == 0) {
		if (type [0] == prop) {
			if (nitems [0] >= 2) {
				int [] buffer = new int [2];
				OS.memmove (buffer, data [0], buffer.length * 4);
				int flags = buffer [1];
				if ((flags & OS.XEMBED_MAPPED) != 0) {
					OS.XMapWindow (xDisplay, clientWindow);
				} else {
					OS.XUnmapWindow (xDisplay, clientWindow);
				}
			}
		}
	}
	if (data [0] != 0) OS.XFree (data [0]);
	display.setWarnings (warnings);
}
}
