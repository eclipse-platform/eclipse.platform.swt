package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 */
public class Composite extends Scrollable {
	Layout layout;
	int damagedRegion;
	
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, style);
}
Control [] _getChildren () {
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return new Control [0];
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	Control [] children = new Control [count];
	int i = 0, j = 0;
	while (i < count) {
		int handle = handles [i];
		if (handle != 0) {
			Widget widget = WidgetTable.get (handle);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [j++] = (Control) widget;
				}
			}
		}
		i++;
	}
	if (i == j) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}
public void addListener (int eventType, Listener handler) {
	checkWidget();
	super.addListener (eventType, handler);
	if ((state & CANVAS) != 0) {
		if (eventType == SWT.KeyDown || eventType == SWT.KeyUp) {
			enableTraversal (true);
		}
	}
}
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	Point size;
	if (layout != null) {
		if ((wHint == SWT.DEFAULT) || (hHint == SWT.DEFAULT)) {
			size = layout.computeSize (this, wHint, hHint, changed);
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		int border = (style & SWT.BORDER) != 0 ? 1 : 0;
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNborderWidth, border,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, 0,
		};
		int parentHandle = parent.handle;
		handle = OS.XmCreateDrawingArea (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		Display display = getDisplay ();
		OS.XtOverrideTranslations (handle, display.tabTranslations);
		OS.XtOverrideTranslations (handle, display.arrowTranslations);
	} else {
		createScrolledHandle (parent.handle);
	}
}
void createScrolledHandle (int topHandle) {
	int [] argList = {OS.XmNancestorSensitive, 1};
	scrolledHandle = OS.XmCreateMainWindow (topHandle, null, argList, argList.length / 2);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	Display display = getDisplay ();
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		int thickness = display.buttonShadowThickness;
		int [] argList1 = {
			OS.XmNmarginWidth, 3,
			OS.XmNmarginHeight, 3, 
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNshadowType, OS.XmSHADOW_IN,
			OS.XmNshadowThickness, thickness,
		};
		formHandle = OS.XmCreateForm (scrolledHandle, null, argList1, argList1.length / 2);
		if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = {
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtopAttachment, OS.XmATTACH_FORM,
			OS.XmNbottomAttachment, OS.XmATTACH_FORM,
			OS.XmNleftAttachment, OS.XmATTACH_FORM,
			OS.XmNrightAttachment, OS.XmATTACH_FORM,
			OS.XmNresizable, 0,
			OS.XmNtraversalOn, 0,
		};
		handle = OS.XmCreateDrawingArea (formHandle, null, argList2, argList2.length / 2);
	} else {
		int [] argList3 = {
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, 0,
		};
		handle = OS.XmCreateDrawingArea (scrolledHandle, null, argList3, argList3.length / 2);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.XtOverrideTranslations (handle, display.tabTranslations);
	OS.XtOverrideTranslations (handle, display.arrowTranslations);
}
int defaultBackground () {
	return getDisplay ().compositeBackground;
}
int defaultForeground () {
	return getDisplay ().compositeForeground;
}
void enableTraversal (boolean enable) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return;
		int [] argList = {OS.XmNtraversalOn, enable ? 1 : 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
}
public boolean forceFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	int [] traversals = new int [children.length];
	int [] argList = new int [] {OS.XmNtraversalOn, 0};
	for (int i=0; i<children.length; i++) {
		OS.XtGetValues (children [i].handle, argList, argList.length / 2);
		traversals [i] = argList [1];
		argList [1] = 0;
		OS.XtSetValues (children [i].handle, argList, argList.length / 2);
	}
	boolean result = super.forceFocus ();
	for (int i=0; i<children.length; i++) {
		argList [1] = traversals [i];
		OS.XtSetValues (children [i].handle, argList, argList.length / 2);
	}
	return result;
}
/**
 * Returns an array containing the receiver's children.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
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
int getChildrenCount () {
	/*
	* NOTE:  The current implementation will count
	* non-registered children.
	* */
	int [] argList = {OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
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

public Control [] getTabList () {
	checkWidget ();
	int count = 0;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		int [] argList = new int [] {OS.XmNnavigationType, 0};
		OS.XtGetValues (control.handle, argList, argList.length / 2);
		if (argList [1] == OS.XmEXCLUSIVE_TAB_GROUP) count++;
	}
	int index = 0;
	Control [] tabList = new Control [count];
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		int [] argList = new int [] {OS.XmNnavigationType, 0};
		OS.XtGetValues (control.handle, argList, argList.length / 2);
		if (argList [1] == OS.XmEXCLUSIVE_TAB_GROUP) {
			tabList [index++] = control;
		}
	}
	return tabList;
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		int windowProc = getDisplay ().windowProc;
		OS.XtInsertEventHandler (handle, 0, true, windowProc, -1, OS.XtListTail);
	}
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the receiver does not have a layout, do nothing.
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	checkWidget();
	layout (true);
}
/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the the argument is <code>true</code> the layout must not rely
 * on any cached information it is keeping about the children. If it
 * is <code>false</code> the layout may (potentially) simplify the
 * work it is doing by assuming that the state of the none of the
 * receiver's children has changed since the last layout.
 * If the receiver does not have a layout, do nothing.
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget();
	if (layout == null) return;
	int count = getChildrenCount ();
	if (count == 0) return;
	layout.layout (this, changed);
}
Point minimumSize () {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x + rect.width);
		height = Math.max (height, rect.y + rect.height);
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

int processNonMaskable (int callData) {
	if ((state & CANVAS) != 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, callData, XExposeEvent.sizeof);
		if (xEvent.type == OS.GraphicsExpose) processPaint (callData);
	}
	return 0;
}
int processPaint (int callData) {
	if ((state & CANVAS) == 0) {
		return super.processPaint (callData);
	}
	if (!hooks (SWT.Paint)) return 0;
	if ((style & SWT.NO_MERGE_PAINTS) != 0) {
		return super.processPaint (callData);
	}
	XExposeEvent xEvent = new XExposeEvent ();
	OS.memmove (xEvent, callData, XExposeEvent.sizeof);
	int exposeCount = xEvent.count;
	if (damagedRegion == 0 && exposeCount == 0) {
		XAnyEvent event = new XAnyEvent ();
		Display display = getDisplay ();
		display.exposeCount = 0;
		int checkExposeProc = display.checkExposeProc;
		OS.XCheckIfEvent (xEvent.display, event, checkExposeProc, xEvent.window);
		if (display.exposeCount == 0) return super.processPaint (callData);
		exposeCount = display.exposeCount;
	}
	if (damagedRegion == 0) damagedRegion = OS.XCreateRegion ();
	OS.XtAddExposureToRegion (callData, damagedRegion);
	if (exposeCount != 0) return 0;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	Event event = new Event ();
	GC gc = event.gc = new GC (this);
	OS.XSetRegion (xDisplay, gc.handle, damagedRegion);
	XRectangle rect = new XRectangle ();
	OS.XClipBox (damagedRegion, rect);
	OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
	event.time = OS.XtLastTimestampProcessed (xDisplay);
	event.x = rect.x;  event.y = rect.y;
	event.width = rect.width;  event.height = rect.height;
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 0;
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
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) == 0 && (style & SWT.NO_REDRAW_RESIZE) != 0) return;
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		int xWindow = OS.XtWindow (handle);
		if (xWindow == 0) return;
		int flags = 0;
		XSetWindowAttributes attributes = new XSetWindowAttributes ();
		if ((style & SWT.NO_BACKGROUND) != 0) {
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
void redrawWidget (int x, int y, int width, int height, boolean all) {
	super.redrawWidget (x, y, width, height, all);
	if (!all) return;
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		Point location = child.getClientLocation ();
		child.redrawWidget (x - location.x, y - location.y, width, height, all);
	}
}
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	super.removeListener (eventType, handler);
	if ((state & CANVAS) != 0) {
		if (eventType == SWT.KeyDown || eventType == SWT.KeyUp) {
			enableTraversal (!(hooks (SWT.KeyDown) || hooks (SWT.KeyUp)));
		}
	}
}
void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (!child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
}
void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
	if (damagedRegion != 0) OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) != 0) {
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
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	if (layout != null) layout (false);
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
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
	this.layout = layout;
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	if (layout != null) layout (false);
}
public void setTabList (Control [] tabList) {
	checkWidget ();
	if (tabList == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<tabList.length; i++) {
		Control control = tabList [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		Shell shell = control.getShell ();
		while (control != shell && control != this) {
			control = control.parent;
		}
		if (control != this) error (SWT.ERROR_INVALID_PARENT);
	}
	int [] argList1 = new int [] {OS.XmNnavigationType, OS.XmTAB_GROUP};
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		OS.XtSetValues (control.handle, argList1, argList1.length / 2);
	}
	int [] argList2 = new int [] {OS.XmNnavigationType, OS.XmEXCLUSIVE_TAB_GROUP};
	for (int i=0; i<tabList.length; i++) {
		/*
		* Set the XmNnavigationType twice, once to clear the
		* old value and once to set the new.  If the old value
		* is not cleared, Motif detects that the values are the
		* same and does not change the tab order.
		*/
		Control control = tabList [i];
		OS.XtSetValues (control.handle, argList1, argList1.length / 2);
		OS.XtSetValues (control.handle, argList2, argList2.length / 2);
	}
}
int traversalCode () {
	if ((state & CANVAS) != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) return 0;
	}
	return super.traversalCode ();
}
boolean translateMnemonic (char key, XKeyEvent xEvent) {
	if (super.translateMnemonic (key, xEvent)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.translateMnemonic (key, xEvent)) return true;
	}
	return false;
}
}
