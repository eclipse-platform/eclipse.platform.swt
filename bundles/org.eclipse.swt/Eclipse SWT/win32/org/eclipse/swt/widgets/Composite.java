/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
 * widget toolkits into SWT.  On some operating systems (GTK), this may cause
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
	WINDOWPOS [] lpwp;
	Control [] tabList;
	int layoutCount, backgroundMode;

	static final int TOOLTIP_LIMIT = 4096;

	static {
		DPIZoomChangeRegistry.registerHandler(Composite::handleDPIChange, Composite.class);
	}

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Composite () {
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

Control [] _getChildren () {
	int count = 0;
	long hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	if (hwndChild == 0) return new Control [0];
	while (hwndChild != 0) {
		count++;
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	Control [] children = new Control [count];
	int index = 0;
	hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		Control control = display.getControl (hwndChild);
		if (control != null && control != this) {
			children [index++] = control;
		}
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	if (count == index) return children;
	Control [] newChildren = new Control [index];
	System.arraycopy (children, 0, newChildren, 0, index);
	return newChildren;
}

Control [] _getTabList () {
	if (tabList == null) return tabList;
	int count = 0;
	for (Control element : tabList) {
		if (!element.isDisposed ()) count++;
	}
	if (count == tabList.length) return tabList;
	Control [] newList = new Control [count];
	int index = 0;
	for (Control element : tabList) {
		if (!element.isDisposed ()) {
			newList [index++] = element;
		}
	}
	tabList = newList;
	return tabList;
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
 * @deprecated use {@link Composite#layout(Control[], int)} instead
 * @since 3.1
 */
@Deprecated
public void changed (Control[] changed) {
	layout(changed, SWT.DEFER);
}

@Override
void checkBuffered () {
	if ((state & CANVAS) == 0) {
		super.checkBuffered ();
	}
}

@Override
void checkComposited () {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.TRANSPARENT) != 0) {
			long hwndParent = parent.handle;
			int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
			bits |= OS.WS_EX_COMPOSITED;
			OS.SetWindowLong (hwndParent, OS.GWL_EXSTYLE, bits);
		}
	}
}

@Override
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

@Override
Widget [] computeTabList () {
	Widget result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? _getTabList () : _getChildren ();
	for (Control child : list) {
		Widget  [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Widget [] newResult = new Widget [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	return result;
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	display.runSkin ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			state &= ~LAYOUT_CHANGED;
			size = DPIUtil.autoScaleUp(layout.computeSize (this, DPIUtil.autoScaleDown(wHint), DPIUtil.autoScaleDown(hHint), changed));
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
	/*
	 * Since computeTrim can be overridden by subclasses, we cannot
	 * call computeTrimInPixels directly.
	 */
	Rectangle trim = DPIUtil.autoScaleUp(computeTrim (0, 0, DPIUtil.autoScaleDown(size.x), DPIUtil.autoScaleDown(size.y)));
	return new Point (trim.width, trim.height);
}

/**
 * Copies a rectangular area of the receiver at the specified
 * position using the gc.
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
 */
/*public*/ void copyArea (GC gc, int x, int y, int width, int height) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);

	//XP only, no GDI+
	//#define PW_CLIENTONLY 0x00000001
	//DCOrg() wrong
	//topHandle wrong for Tree?
	long hDC = gc.handle;
	int nSavedDC = OS.SaveDC (hDC);
	OS.IntersectClipRect (hDC, 0, 0, width, height);

	//WRONG PARENT
	POINT lpPoint = new POINT ();
	long hwndParent = OS.GetParent (handle);
	OS.MapWindowPoints (handle, hwndParent, lpPoint, 1);
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	POINT lpPoint1 = new POINT (), lpPoint2 = new POINT ();
	x = x + (lpPoint.x - rect.left);
	y = y + (lpPoint.y - rect.top);
	OS.SetWindowOrgEx (hDC, x, y, lpPoint1);
	OS.SetBrushOrgEx (hDC, x, y, lpPoint2);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_VISIBLE) == 0) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
	}
	//NECESSARY?
	OS.RedrawWindow (handle, null, 0, OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN);
	OS.PrintWindow (handle, hDC, 0);//0x00000001);
	if ((bits & OS.WS_VISIBLE) == 0) {
		OS.DefWindowProc(handle, OS.WM_SETREDRAW, 0, 0);
	}
	OS.RestoreDC (hDC, nSavedDC);
}

@Override
void createHandle () {
	super.createHandle ();
	state |= CANVAS;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0 || findThemeControl () == parent) {
		state |= THEME_BACKGROUND;
	}
	if ((style & SWT.TRANSPARENT) != 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
		bits |= OS.WS_EX_TRANSPARENT;
		OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	}
}

@Override
int applyThemeBackground () {
	/*
	 * Composite with scrollbars would not inherit the theme because it was
	 * probably being used to implement a control similar to a Text, List,
	 * Table, or Tree, and those controls do not inherit the background theme.
	 * We assume that a Composite that did not have scrollbars was probably just
	 * being used to group some other controls, therefore it should inherit.
	 *
	 * But when Composite background is set to COLOR_TRANSPARENT (i.e.
	 * backgroundAlpha as '0') which means parent theme should be inherited, so
	 * enable the THEME_BACKGROUND in 'state' to support background transparent.
	 * Refer bug 463127 & related bug 234649.
	 */
	return (backgroundAlpha == 0 || (style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0 || findThemeControl () == parent) ? 1 : 0;
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
	x = DPIUtil.autoScaleUp(x);
	y = DPIUtil.autoScaleUp(y);
	width = DPIUtil.autoScaleUp(width);
	height = DPIUtil.autoScaleUp(height);
	offsetX = DPIUtil.autoScaleUp(offsetX);
	offsetY = DPIUtil.autoScaleUp(offsetY);
	drawBackgroundInPixels(gc, x, y, width, height, offsetX, offsetY);
}

void drawBackgroundInPixels(GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	long hDC = gc.handle;
	int pixel = background == -1 ? gc.getBackground ().handle : -1;
	drawBackground (hDC, rect, pixel, offsetX, offsetY);
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : parent.findDeferredControl ();
}

@Override
Menu [] findMenus (Control control) {
	if (control == this) return new Menu [0];
	Menu result [] = super.findMenus (control);
	for (Control child : _getChildren ()) {
		Menu [] menuList = child.findMenus (control);
		if (menuList.length != 0) {
			Menu [] newResult = new Menu [result.length + menuList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (menuList, 0, newResult, result.length, menuList.length);
			result = newResult;
		}
	}
	return result;
}

@Override
void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	super.fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	for (Control child : _getChildren ()) {
		child.fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
}

void fixTabList (Control control) {
	if (tabList == null) return;
	int count = 0;
	for (Control element : tabList) {
		if (element == control) count++;
	}
	if (count == 0) return;
	Control [] newList = null;
	int length = tabList.length - count;
	if (length != 0) {
		newList = new Control [length];
		int index = 0;
		for (Control element : tabList) {
			if (element != control) {
				newList [index++] = element;
			}
		}
	}
	tabList = newList;
}

/**
 * Returns the receiver's background drawing mode. This
 * will be one of the following constants defined in class
 * <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERIT_FORCE</code>.
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
	checkWidget ();
	return _getChildren ();
}

int getChildrenCount () {
	/*
	* NOTE: The current implementation will count
	* non-registered children.
	*/
	int count = 0;
	long hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		count++;
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	return count;
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
	checkWidget ();
	return layout;
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
		for (Control element : list) {
			if (element.isTabGroup ()) count++;
		}
		tabList = new Control [count];
		int index = 0;
		for (Control element : list) {
			if (element.isTabGroup ()) {
				tabList [index++] = element;
			}
		}
	}
	return tabList;
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
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

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * Use of this method is discouraged since it is the least-efficient
 * way to trigger a layout. The use of <code>layout(true)</code>
 * discards all cached layout information, even from controls which
 * have not changed. It is much more efficient to invoke
 * {@link Control#requestLayout()} on every control which has changed
 * in the layout than it is to invoke this method on the layout itself.
 * </p>
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
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
 * </p>
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
 * <p>
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
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
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
 * </p>
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
 * </p>
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be laid out</dd>
 * <dt><b>SWT.CHANGED</b></dt>
 * <dd>the layout must flush its caches</dd>
 * <dt><b>SWT.DEFER</b></dt>
 * <dd>layout will be deferred</dd>
 * </dl>
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
		for (Control control : changed) {
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
		for (Control element : changed) {
			Control child = element;
			Composite composite = child.parent;
			// Update layout when the list of children has changed.
			// See bug 497812.
			child.markLayout(false, false);
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
		if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		for (int i=updateCount-1; i>=0; i--) {
			update [i].updateLayout (false);
		}
	} else {
		if (layout == null && (flags & SWT.ALL) == 0) return;
		markLayout ((flags & SWT.CHANGED) != 0, (flags & SWT.ALL) != 0);
		if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		updateLayout ((flags & SWT.ALL) != 0);
	}
}

@Override
void markLayout (boolean changed, boolean all) {
	if (layout != null) {
		state |= LAYOUT_NEEDED;
		if (changed) state |= LAYOUT_CHANGED;
	}
	if (all) {
		for (Control element : _getChildren ()) {
			element.markLayout (changed, all);
		}
	}
}

Point minimumSize (int wHint, int hHint, boolean changed) {
	/*
	 * Since getClientArea can be overridden by subclasses, we cannot
	 * call getClientAreaInPixels directly.
	 */
	Rectangle clientArea = DPIUtil.autoScaleUp(getClientArea ());
	int width = 0, height = 0;
	for (Control element : _getChildren ()) {
		Rectangle rect = DPIUtil.autoScaleUp(element.getBounds ());
		width = Math.max (width, rect.x - clientArea.x + rect.width);
		height = Math.max (height, rect.y - clientArea.y + rect.height);
	}
	return new Point (width, height);
}

@Override
boolean redrawChildren () {
	if (!super.redrawChildren ()) return false;
	for (Control element : _getChildren ()) {
		element.redrawChildren ();
	}
	return true;
}

@Override
void releaseParent () {
	super.releaseParent ();
	if ((state & CANVAS) != 0) {
		if ((style & SWT.TRANSPARENT) != 0) {
			long hwndParent = parent.handle;
			long hwndChild = OS.GetWindow (hwndParent, OS.GW_CHILD);
			while (hwndChild != 0) {
				if (hwndChild != handle) {
					int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
					if ((bits & OS.WS_EX_TRANSPARENT) != 0) return;
				}
				hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
			}
			int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
			bits &= ~OS.WS_EX_COMPOSITED;
			OS.SetWindowLong (hwndParent, OS.GWL_EXSTYLE, bits);
		}
	}
}

@Override
void releaseChildren (boolean destroy) {
	try (ExceptionStash exceptions = new ExceptionStash ()) {
		for (Control child : _getChildren ()) {
			if (child == null || child.isDisposed ())
				continue;

			try {
				child.release (false);
			} catch (Error | RuntimeException ex) {
				exceptions.stash (ex);
			}
		}
		super.releaseChildren (destroy);
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		long hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
		if (hwndChild != 0) {
			int threadId = OS.GetWindowThreadProcessId (hwndChild, null);
			if (threadId != OS.GetCurrentThreadId ()) {
				OS.ShowWindow (hwndChild, OS.SW_HIDE);
				OS.SetParent (hwndChild, 0);
			}
		}
	}
	layout = null;
	tabList = null;
	lpwp = null;
}

void removeControl (Control control) {
	fixTabList (control);
	resizeChildren ();
}

@Override
void reskinChildren (int flags) {
	super.reskinChildren (flags);
	for (Control child : _getChildren ()) {
		if (child != null) child.reskin (flags);
	}
}

void resizeChildren () {
	if (lpwp == null) return;
	do {
		WINDOWPOS [] currentLpwp = lpwp;
		lpwp = null;
		if (!resizeChildren (true, currentLpwp)) {
			resizeChildren (false, currentLpwp);
		}
	} while (lpwp != null);
}

boolean resizeChildren (boolean defer, WINDOWPOS [] pwp) {
	if (pwp == null) return true;
	long hdwp = 0;
	if (defer) {
		hdwp = OS.BeginDeferWindowPos (pwp.length);
		if (hdwp == 0) return false;
	}
	for (WINDOWPOS wp : pwp) {
		if (wp != null) {
			if (defer) {
				hdwp = OS.DeferWindowPos (hdwp, wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
				if (hdwp == 0) return false;
			} else {
				OS.SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
			}
		}
	}
	if (defer) return OS.EndDeferWindowPos (hdwp);
	return true;
}

void resizeEmbeddedHandle(long embeddedHandle, int width, int height) {
	if (embeddedHandle == 0) return;
	int [] processID = new int [1];
	int threadId = OS.GetWindowThreadProcessId (embeddedHandle, processID);
	if (threadId != OS.GetCurrentThreadId ()) {
		if (processID [0] == OS.GetCurrentProcessId ()) {
			if (display.msgHook == 0) {
				display.getMsgCallback = new Callback (display, "getMsgProc", 3);
				display.getMsgProc = display.getMsgCallback.getAddress ();
				display.msgHook = OS.SetWindowsHookEx (OS.WH_GETMESSAGE, display.getMsgProc, OS.GetLibraryHandle(), threadId);
				OS.PostThreadMessage (threadId, OS.WM_NULL, 0, 0);
			}
		}
		int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE | OS.SWP_ASYNCWINDOWPOS;
		OS.SetWindowPos (embeddedHandle, 0, 0, 0, width, height, flags);
	}
}

@Override
void sendResize () {
	setResizeChildren (false);
	super.sendResize ();
	if (isDisposed ()) return;
	if (layout != null) {
		markLayout (false, false);
		updateLayout (false, false);
	}
	setResizeChildren (true);
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
	for (Control element : _getChildren ()) {
		element.updateBackgroundMode ();
	}
}

@Override
void setBoundsInPixels (int x, int y, int width, int height, int flags, boolean defer) {
	if (display.resizeCount > Display.RESIZE_LIMIT) {
		defer = false;
	}
	if (!defer && (state & CANVAS) != 0) {
		state &= ~(RESIZE_OCCURRED | MOVE_OCCURRED);
		state |= RESIZE_DEFERRED | MOVE_DEFERRED;
	}
	super.setBoundsInPixels (x, y, width, height, flags, defer);
	if (!defer && (state & CANVAS) != 0) {
		boolean wasMoved = (state & MOVE_OCCURRED) != 0;
		boolean wasResized = (state & RESIZE_OCCURRED) != 0;
		state &= ~(RESIZE_DEFERRED | MOVE_DEFERRED);
		if (wasMoved && !isDisposed ()) sendMove ();
		if (wasResized && !isDisposed ()) sendResize ();
	}
}

@Override
public boolean setFocus () {
	checkWidget ();
	Control [] children = _getChildren ();
	for (Control child : children) {
		if (child.getVisible() && child.setRadioFocus (false)) return true;
	}
	for (Control child : children) {
		if (child.getVisible() && child.setFocus ()) return true;
	}
	return super.setFocus ();
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
	checkWidget ();
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
		for (Control control : tabList) {
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

void setResizeChildren (boolean resize) {
	if (resize) {
		resizeChildren ();
	} else {
		if (display.resizeCount > Display.RESIZE_LIMIT) {
			return;
		}
		int count = getChildrenCount ();
		if (count > 1 && lpwp == null) {
			lpwp = new WINDOWPOS [count];
		}
	}
}

@Override
boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) {
		takeFocus = hooksKeys ();
		if ((style & SWT.EMBEDDED) != 0) takeFocus = true;
	}
	if (takeFocus && setTabItemFocus ()) return true;
	Control [] children = _getChildren ();
	for (Control child : children) {
		/*
		 * It is unlikely but possible that a child is disposed at this point, for more
		 * details refer bug 381668.
		 */
		if (!child.isDisposed() && child.isTabItem() && child.setRadioFocus (true)) return true;
	}
	for (Control child : children) {
		if (!child.isDisposed() && child.isTabItem () && !child.isTabGroup () && child.setTabItemFocus ()) {
			return true;
		}
	}
	return false;
}

@Override
boolean updateTextDirection(int textDirection) {
	super.updateTextDirection (textDirection);
	/*
	 * Always continue, communicating the direction to the children since
	 * OS.WS_EX_RTLREADING doesn't propagate to them natively, and since
	 * the direction might need to be handled by each child individually.
	 */
	Control[] children = _getChildren ();
	int i = children.length;
	while (i-- > 0) {
		if (children[i] != null && !children[i].isDisposed ()) {
			children[i].updateTextDirection(textDirection);
		}
	}
	/*
	 * Return value indicates whether or not to update derivatives, so in case
	 * of AUTO always return true regardless of the actual update.
	 */
	return true;
}

String toolTipText (NMTTDISPINFO hdr) {
	Shell shell = getShell ();
	if ((hdr.uFlags & OS.TTF_IDISHWND) == 0) {
		String string = null;
		ToolTip toolTip = shell.findToolTip ((int)hdr.idFrom);
		if (toolTip != null) {
			string = toolTip.message;
			if (string == null || string.length () == 0) string = " ";
			/*
			* Bug in Windows.  On Windows 7, tool tips hang when displaying large
			* unwrapped strings. The fix is to wrap the string ourselves.
			*/
			if (string.length () > TOOLTIP_LIMIT / 4) {
				string = display.wrapText (string, handle, toolTip.getWidth ());
			}
		}
		return string;
	}
	shell.setToolTipTitle (hdr.hwndFrom, null, 0);
	OS.SendMessage (hdr.hwndFrom, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
	Control control = display.getControl (hdr.idFrom);
	return control != null ? control.toolTipText : null;
}

@Override
boolean translateMnemonic (Event event, Control control) {
	if (super.translateMnemonic (event, control)) return true;
	if (control != null) {
		for (Control child : _getChildren ()) {
			if (child.translateMnemonic (event, control)) return true;
		}
	}
	return false;
}

@Override
boolean translateTraversal (MSG msg) {
	if ((state & CANVAS) != 0 ) {
		if ((style & SWT.EMBEDDED) != 0) return false;
		switch ((int)msg.wParam) {
			case OS.VK_UP:
			case OS.VK_LEFT:
			case OS.VK_DOWN:
			case OS.VK_RIGHT:
			case OS.VK_PRIOR:
			case OS.VK_NEXT:
				int uiState = (int)OS.SendMessage (msg.hwnd, OS.WM_QUERYUISTATE, 0, 0);
				if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
					OS.SendMessage (msg.hwnd, OS.WM_UPDATEUISTATE, OS.MAKEWPARAM (OS.UIS_CLEAR, OS.UISF_HIDEFOCUS), 0);
				}
				break;
		}
	}
	return super.translateTraversal (msg);
}

@Override
void updateBackgroundColor () {
	super.updateBackgroundColor ();
	for (Control element : _getChildren ()) {
		if ((element.state & PARENT_BACKGROUND) != 0) {
			element.updateBackgroundColor ();
		}
	}
}

@Override
void updateBackgroundImage () {
	super.updateBackgroundImage ();
	for (Control element : _getChildren ()) {
		if ((element.state & PARENT_BACKGROUND) != 0) {
			element.updateBackgroundImage ();
		}
	}
}

@Override
void updateBackgroundMode () {
	super.updateBackgroundMode ();
	for (Control element : _getChildren ()) {
		element.updateBackgroundMode ();
	}
}

@Override
void updateFont (Font oldFont, Font newFont) {
	super.updateFont (oldFont, newFont);
	for (Control control : _getChildren ()) {
		if (!control.isDisposed ()) {
			control.updateFont (oldFont, newFont);
		}
	}
}

void updateLayout (boolean all) {
	updateLayout (true, all);
}

@Override
void updateLayout (boolean resize, boolean all) {
	Composite parent = findDeferredControl ();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin();
		if (resize) setResizeChildren (false);
		layout.layout (this, changed);
		if (resize) setResizeChildren (true);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		for (Control element : _getChildren ()) {
			element.updateLayout (resize, all);
		}
	}
}

@Override
void updateOrientation () {
	Control [] controls = _getChildren ();
	RECT [] rects = new RECT [controls.length];
	for (int i=0; i<controls.length; i++) {
		Control control = controls [i];
		RECT rect = rects [i] = new RECT();
		control.forceResize ();
		OS.GetWindowRect (control.topHandle (), rect);
		OS.MapWindowPoints (0, handle, rect, 2);
	}
	int orientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	super.updateOrientation ();
	for (int i=0; i<controls.length; i++) {
		Control control = controls [i];
		RECT rect = rects [i];
		control.setOrientation (orientation);
		int flags = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE;
		OS.SetWindowPos (control.topHandle (), 0, rect.left, rect.top, 0, 0, flags);
	}
}

void updateUIState () {
	long hwndShell = getShell ().handle;
	int uiState = (int)OS.SendMessage (hwndShell, OS.WM_QUERYUISTATE, 0, 0);
	if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
		OS.SendMessage (hwndShell, OS.WM_CHANGEUISTATE, OS.MAKEWPARAM (OS.UIS_CLEAR, OS.UISF_HIDEFOCUS), 0);
	}
}

@Override
int widgetStyle () {
	/* Force clipping of children by setting WS_CLIPCHILDREN */
	return super.widgetStyle () | OS.WS_CLIPCHILDREN;
}

@Override
LRESULT WM_ERASEBKGND (long wParam, long lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		/* Return zero to indicate that the background was not erased */
		if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) != 0) {
			return LRESULT.ZERO;
		}
	}
	return result;
}

@Override
LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		int flags = 0;
		if (hooksKeys ()) {
			flags |= OS.DLGC_WANTALLKEYS | OS.DLGC_WANTARROWS | OS.DLGC_WANTTAB;
		}
		if ((style & SWT.NO_FOCUS) != 0) flags |= OS.DLGC_STATIC;
		if (OS.GetWindow (handle, OS.GW_CHILD) != 0) flags |= OS.DLGC_STATIC;
		if (flags != 0) return new LRESULT (flags);
	}
	return result;
}

@Override
LRESULT WM_GETFONT (long wParam, long lParam) {
	LRESULT result = super.WM_GETFONT (wParam, lParam);
	if (result != null) return result;
	long code = callWindowProc (handle, OS.WM_GETFONT, wParam, lParam);
	if (code != 0) return new LRESULT (code);
	return new LRESULT (font != null ? font.handle : defaultFont ());
}

@Override
LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if (result == LRESULT.ZERO) return result;

	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			if (OS.GetWindow (handle, OS.GW_CHILD) == 0) setFocus ();
		}
	}
	return result;
}

@Override
LRESULT WM_NCHITTEST (long wParam, long lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  For some reason, under circumstances
	* that are not understood, when one scrolled window is
	* embedded in another and the outer window scrolls the
	* inner horizontally by moving the location of the inner
	* one, the vertical scroll bars of the inner window no
	* longer function.  Specifically, WM_NCHITTEST returns
	* HTCLIENT instead of HTVSCROLL.  The fix is to detect
	* the case where the result of WM_NCHITTEST is HTCLIENT
	* and the point is not in the client area, and redraw
	* the trim, which somehow fixes the next WM_NCHITTEST.
	*/
	if (OS.IsAppThemed ()) {
		if ((state & CANVAS)!= 0) {
			long code = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
			if (code == OS.HTCLIENT) {
				RECT rect = new RECT ();
				OS.GetClientRect (handle, rect);
				POINT pt = new POINT ();
				pt.x = OS.GET_X_LPARAM (lParam);
				pt.y = OS.GET_Y_LPARAM (lParam);
				OS.MapWindowPoints (0, handle, pt, 1);
				if (!OS.PtInRect (rect, pt)) {
					int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE;
					OS.RedrawWindow (handle, null, 0, flags);
				}
			}
			return new LRESULT (code);
		}
	}
	return result;
}

@Override
LRESULT WM_PARENTNOTIFY (long wParam, long lParam) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		if (OS.LOWORD (wParam) == OS.WM_CREATE) {
			RECT rect = new RECT ();
			OS.GetClientRect (handle, rect);
			resizeEmbeddedHandle (lParam, rect.right - rect.left, rect.bottom - rect.top);
		}
	}
	return super.WM_PARENTNOTIFY (wParam, lParam);
}

@Override
LRESULT WM_PAINT (long wParam, long lParam) {
	if ((state & DISPOSE_SENT) != 0) return LRESULT.ZERO;
	if ((state & CANVAS) == 0 || (state & FOREIGN_HANDLE) != 0) {
		return super.WM_PAINT (wParam, lParam);
	}

	/* Set the clipping bits */
	int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int newBits = oldBits | OS.WS_CLIPSIBLINGS | OS.WS_CLIPCHILDREN;
	if (newBits != oldBits) OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);

	/* Paint the control and the background */
	PAINTSTRUCT ps = new PAINTSTRUCT ();
	if (hooks (SWT.Paint) || filters (SWT.Paint)) {

		/* Use the buffered paint when possible */
		boolean bufferedPaint = false;
		if ((style & SWT.DOUBLE_BUFFERED) != 0) {
			if ((style & (SWT.NO_MERGE_PAINTS | SWT.RIGHT_TO_LEFT | SWT.TRANSPARENT)) == 0) {
				bufferedPaint = true;
			}
		}
		if (bufferedPaint) {
			long hDC = OS.BeginPaint (handle, ps);
			int width = ps.right - ps.left;
			int height = ps.bottom - ps.top;
			if (width != 0 && height != 0) {
				long [] phdc = new long [1];
				int flags = OS.BPBF_COMPATIBLEBITMAP;
				RECT prcTarget = new RECT ();
				OS.SetRect (prcTarget, ps.left, ps.top, ps.right, ps.bottom);
				long hBufferedPaint = OS.BeginBufferedPaint (hDC, prcTarget, flags, null, phdc);
				GCData data = new GCData ();
				data.device = display;
				data.foreground = getForegroundPixel ();
				Control control = findBackgroundControl ();
				if (control == null) control = this;
				data.background = control.getBackgroundPixel ();
				data.font = Font.win32_new(display, OS.SendMessage (handle, OS.WM_GETFONT, 0, 0));
				data.uiState = (int)OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
				if ((style & SWT.NO_BACKGROUND) != 0) {
					/* This code is intentionally commented because it may be slow to copy bits from the screen */
					//paintGC.copyArea (image, ps.left, ps.top);
				} else {
					RECT rect = new RECT ();
					OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
					drawBackground (phdc [0], rect);
				}
				GC gc = GC.win32_new (phdc [0], data);
				Event event = new Event ();
				event.gc = gc;
				event.setBoundsInPixels(new Rectangle(ps.left, ps.top, width, height));
				sendEvent (SWT.Paint, event);
				if (data.focusDrawn && !isDisposed ()) updateUIState ();
				gc.dispose ();
				OS.EndBufferedPaint (hBufferedPaint, true);
			}
			OS.EndPaint (handle, ps);
		} else {

			/* Create the paint GC */
			GCData data = new GCData ();
			data.ps = ps;
			data.hwnd = handle;
			GC gc = GC.win32_new (this, data);

			/* Get the system region for the paint HDC */
			long sysRgn = 0;
			if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0 || (style & SWT.NO_MERGE_PAINTS) != 0) {
				sysRgn = OS.CreateRectRgn (0, 0, 0, 0);
				if (OS.GetRandomRgn (gc.handle, sysRgn, OS.SYSRGN) == 1) {
					if ((OS.GetLayout (gc.handle) & OS.LAYOUT_RTL) != 0) {
						int nBytes = OS.GetRegionData (sysRgn, 0, null);
						int [] lpRgnData = new int [nBytes / 4];
						OS.GetRegionData (sysRgn, nBytes, lpRgnData);
						long newSysRgn = OS.ExtCreateRegion (new float [] {-1, 0, 0, 1, 0, 0}, nBytes, lpRgnData);
						OS.DeleteObject (sysRgn);
						sysRgn = newSysRgn;
					}
					POINT pt = new POINT();
					OS.MapWindowPoints (0, handle, pt, 1);
					OS.OffsetRgn (sysRgn, pt.x, pt.y);
				}
			}

			/* Send the paint event */
			int width = ps.right - ps.left;
			int height = ps.bottom - ps.top;
			if (width != 0 && height != 0) {
				GC paintGC = null;
				Image image = null;
				if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0) {
					image = new Image (display, width, height);
					paintGC = gc;
					gc = new GC (image, paintGC.getStyle() & SWT.RIGHT_TO_LEFT);
					GCData gcData = gc.getGCData ();
					gcData.uiState = data.uiState;
					gc.setForeground (getForeground ());
					gc.setBackground (getBackground ());
					gc.setFont (getFont ());
					if ((style & SWT.TRANSPARENT) != 0) {
						OS.BitBlt (gc.handle, 0, 0, width, height, paintGC.handle, ps.left, ps.top, OS.SRCCOPY);
					}
					OS.OffsetRgn (sysRgn, -ps.left, -ps.top);
					OS.SelectClipRgn (gc.handle, sysRgn);
					OS.OffsetRgn (sysRgn, ps.left, ps.top);
					OS.SetMetaRgn (gc.handle);
					OS.SetWindowOrgEx (gc.handle, ps.left, ps.top, null);
					OS.SetBrushOrgEx (gc.handle, ps.left, ps.top, null);
					if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) != 0) {
						/* This code is intentionally commented because it may be slow to copy bits from the screen */
						//paintGC.copyArea (image, ps.left, ps.top);
					} else {
						RECT rect = new RECT ();
						OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
						drawBackground (gc.handle, rect);
					}
				}
				Event event = new Event ();
				event.gc = gc;
				RECT rect = null;
				if ((style & SWT.NO_MERGE_PAINTS) != 0 && OS.GetRgnBox (sysRgn, rect = new RECT ()) == OS.COMPLEXREGION) {
					int nBytes = OS.GetRegionData (sysRgn, 0, null);
					int [] lpRgnData = new int [nBytes / 4];
					OS.GetRegionData (sysRgn, nBytes, lpRgnData);
					int count = lpRgnData [2];
					for (int i=0; i<count; i++) {
						int offset = 8 + (i << 2);
						OS.SetRect (rect, lpRgnData [offset], lpRgnData [offset + 1], lpRgnData [offset + 2], lpRgnData [offset + 3]);
						if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
							drawBackground (gc.handle, rect);
						}
						event.setBoundsInPixels(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
						event.count = count - 1 - i;
						sendEvent (SWT.Paint, event);
					}
				} else {
					if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
						if (rect == null) rect = new RECT ();
						OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
						drawBackground (gc.handle, rect);
					}
					event.setBoundsInPixels(new Rectangle(ps.left, ps.top, width, height));
					sendEvent (SWT.Paint, event);
				}
				// widget could be disposed at this point
				event.gc = null;
				if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0) {
					if (!gc.isDisposed ()) {
						GCData gcData = gc.getGCData ();
						if (gcData.focusDrawn && !isDisposed ()) updateUIState ();
					}
					gc.dispose();
					if (!isDisposed ()) paintGC.drawImage (image, DPIUtil.autoScaleDown(ps.left), DPIUtil.autoScaleDown(ps.top));
					image.dispose ();
					gc = paintGC;
				}
			}
			if (sysRgn != 0) OS.DeleteObject (sysRgn);
			if (data.focusDrawn && !isDisposed ()) updateUIState ();

			/* Dispose the paint GC */
			gc.dispose ();
		}
	} else {
		long hDC = OS.BeginPaint (handle, ps);
		if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
			RECT rect = new RECT ();
			OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
			drawBackground (hDC, rect);
		}
		OS.EndPaint (handle, ps);
	}

	/* Restore the clipping bits */
	if (!isDisposed ()) {
		if (newBits != oldBits) {
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the paint
			* event.  If this happens, don't attempt to restore
			* the style.
			*/
			if (!isDisposed ()) {
				OS.SetWindowLong (handle, OS.GWL_STYLE, oldBits);
			}
		}
	}
	return LRESULT.ZERO;
}

@Override
LRESULT WM_PRINTCLIENT (long wParam, long lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		forceResize ();
		int nSavedDC = OS.SaveDC (wParam);
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
			drawBackground (wParam, rect);
		}
		if (hooks (SWT.Paint) || filters (SWT.Paint)) {
			GCData data = new GCData ();
			data.device = display;
			data.foreground = getForegroundPixel ();
			Control control = findBackgroundControl ();
			if (control == null) control = this;
			data.background = control.getBackgroundPixel ();
			data.font = Font.win32_new(display, OS.SendMessage (handle, OS.WM_GETFONT, 0, 0));
			data.uiState = (int)OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
			GC gc = GC.win32_new (wParam, data);
			Event event = new Event ();
			event.gc = gc;
			event.setBoundsInPixels(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
			sendEvent (SWT.Paint, event);
			event.gc = null;
			gc.dispose ();
		}
		OS.RestoreDC (wParam, nSavedDC);
	}
	return result;
}

@Override
LRESULT WM_SETFONT (long wParam, long lParam) {
	if (lParam != 0) OS.InvalidateRect (handle, null, true);
	return super.WM_SETFONT (wParam, lParam);
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = null;
	if ((state & RESIZE_DEFERRED) != 0) {
		result = super.WM_SIZE (wParam, lParam);
	} else {
		/* Begin deferred window positioning */
		setResizeChildren (false);

		/* Resize and Layout */
		result = super.WM_SIZE (wParam, lParam);
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the resize
		* event.  If this happens, end the processing of the
		* Windows message by returning the result of the
		* WM_SIZE message.
		*/
		if (isDisposed ()) return result;
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false, false);
		}

		/* End deferred window positioning */
		setResizeChildren (true);
	}

	/* Damage the widget to cause a repaint */
	if (OS.IsWindowVisible (handle)) {
		if ((state & CANVAS) != 0) {
			if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
				if (hooks (SWT.Paint)) {
					OS.InvalidateRect (handle, null, true);
				}
			}
		}
		if (OS.IsAppThemed ()) {
			if (findThemeControl () != null) redrawChildren ();
		}
	}

	/* Resize the embedded window */
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		resizeEmbeddedHandle (OS.GetWindow (handle, OS.GW_CHILD), OS.LOWORD (lParam), OS.HIWORD (lParam));
	}
	return result;
}

@Override
LRESULT WM_SYSCOLORCHANGE (long wParam, long lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	long hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		OS.SendMessage (hwndChild, OS.WM_SYSCOLORCHANGE, 0, 0);
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	return result;
}

@Override
LRESULT WM_SYSCOMMAND (long wParam, long lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;

	/*
	* Check to see if the command is a system command or
	* a user menu item that was added to the system menu.
	*
	* NOTE: This is undocumented.
	*/
	if ((wParam & 0xF000) == 0) return result;

	/*
	* Bug in Windows.  When a vertical or horizontal scroll bar is
	* hidden or shown while the opposite scroll bar is being scrolled
	* by the user (with WM_HSCROLL code SB_LINEDOWN), the scroll bar
	* does not redraw properly.  The fix is to detect this case and
	* redraw the non-client area.
	*/
	int cmd = (int)wParam & 0xFFF0;
	switch (cmd) {
		case OS.SC_HSCROLL:
		case OS.SC_VSCROLL:
			boolean showHBar = horizontalBar != null && horizontalBar.getVisible ();
			boolean showVBar = verticalBar != null && verticalBar.getVisible ();
			long code = callWindowProc (handle, OS.WM_SYSCOMMAND, wParam, lParam);
			if ((showHBar != (horizontalBar != null && horizontalBar.getVisible ())) ||
				(showVBar != (verticalBar != null && verticalBar.getVisible ()))) {
					int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_UPDATENOW;
					OS.RedrawWindow (handle, null, 0, flags);
				}
			if (code == 0) return LRESULT.ZERO;
			return new LRESULT (code);
	}

	/* Return the result */
	return result;
}

@Override
LRESULT WM_UPDATEUISTATE (long wParam, long lParam) {
	LRESULT result = super.WM_UPDATEUISTATE (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0 && hooks (SWT.Paint)) {
		OS.InvalidateRect (handle, null, true);
	}
	return result;
}

@Override
LRESULT wmNCPaint (long hwnd, long wParam, long lParam) {
	LRESULT result = super.wmNCPaint (hwnd, wParam, lParam);
	if (result != null) return result;
	long borderHandle = borderHandle ();
	if ((state & CANVAS) != 0 || (hwnd == borderHandle && handle != borderHandle)) {
		if (OS.IsAppThemed ()) {
			int bits1 = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
			if ((bits1 & OS.WS_EX_CLIENTEDGE) != 0) {
				long code = 0;
				int bits2 = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
				if ((bits2 & (OS.WS_HSCROLL | OS.WS_VSCROLL)) != 0) {
					code = callWindowProc (hwnd, OS.WM_NCPAINT, wParam, lParam);
				}
				long hDC = OS.GetWindowDC (hwnd);
				RECT rect = new RECT ();
				OS.GetWindowRect (hwnd, rect);
				rect.right -= rect.left;
				rect.bottom -= rect.top;
				rect.left = rect.top = 0;
				int border = OS.GetSystemMetrics (OS.SM_CXEDGE);
				OS.ExcludeClipRect (hDC, border, border, rect.right - border, rect.bottom - border);
				OS.DrawThemeBackground (display.hEditTheme (), hDC, OS.EP_EDITTEXT, OS.ETS_NORMAL, rect, null);
				OS.ReleaseDC (hwnd, hDC);
				return new LRESULT (code);
			}
		}
	}
	return result;
}

@Override
LRESULT wmNotify (NMHDR hdr, long wParam, long lParam) {
	switch (hdr.code) {
		/*
		* Feature in Windows.  When the tool tip control is
		* created, the parent of the tool tip is the shell.
		* If SetParent () is used to reparent the tool bar
		* into a new shell, the tool tip is not reparented
		* and pops up underneath the new shell.  The fix is
		* to make sure the tool tip is a topmost window.
		*/
		case OS.TTN_SHOW:
		case OS.TTN_POP: {
			/*
			* Bug in Windows 98 and NT.  Setting the tool tip to be the
			* top most window using HWND_TOPMOST can result in a parent
			* dialog shell being moved behind its parent if the dialog
			* has a sibling that is currently on top.  The fix is to
			* lock the z-order of the active window.
			*
			* Feature in Windows.  Using SetWindowPos() with HWND_NOTOPMOST
			* to clear the topmost state of a window whose parent is already
			* topmost clears the topmost state of the parent.  The fix is to
			* check if the parent is already on top and neither set or clear
			* the topmost status of the tool tip.
			*/
			long hwndParent = hdr.hwndFrom;
			do {
				hwndParent = OS.GetParent (hwndParent);
				if (hwndParent == 0) break;
				int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
				if ((bits & OS.WS_EX_TOPMOST) != 0) break;
			} while (true);
			if (hwndParent != 0) break;
			/*
			 * Bug in Windows.  TTN_SHOW is sent for inactive shells.  When
			 * SetWindowPos is called as a reaction, inactive shells can
			 * wrongly end up on top.  The fix is to swallow such requests.
			 *
			 * A visible effect is that spurious tool tips can show up and
			 * disappear in a split second.  This is a mostly harmless
			 * feature that can also be observed in the Windows Explorer.
			 * See bug 491627 for more details.
			 */
			if (display.getActiveShell () == null) return LRESULT.ONE;

			display.lockActiveWindow = true;
			int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOSIZE;
			long hwndInsertAfter = hdr.code == OS.TTN_SHOW ? OS.HWND_TOPMOST : OS.HWND_NOTOPMOST;
			OS.SetWindowPos (hdr.hwndFrom, hwndInsertAfter, 0, 0, 0, 0, flags);
			display.lockActiveWindow = false;
			break;
		}
		case OS.TTN_GETDISPINFO: {
			NMTTDISPINFO lpnmtdi = new NMTTDISPINFO ();
			OS.MoveMemory (lpnmtdi, lParam, NMTTDISPINFO.sizeof);
			String string = toolTipText (lpnmtdi);
			if (string != null) {
				Shell shell = getShell ();
				string = Display.withCrLf (string);
				/*
				* Bug in Windows.  On Windows 7, tool tips hang when displaying large
				* strings. The fix is to limit the tool tip string to 4Kb.
				*/
				if (string.length() > TOOLTIP_LIMIT) {
					string = string.substring(0, TOOLTIP_LIMIT);
				}
				/*
				 * Bug 475858: In Japanese like languages where mnemonics are not taken from the
				 * source label text but appended in parentheses like "(&M)" at end. In order to
				 * allow the reuse of such label text as a tool-tip text as well, "(&M)" like
				 * character sequence has to be removed from the end of CJK-style mnemonics.
				 */
				char [] chars = fixMnemonic (string, false, true);

				/*
				* Ensure that the orientation of the tool tip matches
				* the orientation of the control.
				*/
				Widget widget = null;
				long hwnd = hdr.idFrom;
				if ((lpnmtdi.uFlags & OS.TTF_IDISHWND) != 0) {
					widget = display.getControl (hwnd);
				} else {
					if (hdr.hwndFrom == shell.toolTipHandle || hdr.hwndFrom == shell.balloonTipHandle) {
						widget = shell.findToolTip ((int)hdr.idFrom);
					}
				}
				if (widget != null) {
					int style = widget.getStyle();
					int flags = SWT.RIGHT_TO_LEFT | SWT.FLIP_TEXT_DIRECTION;
					if ((style & flags) != 0 && (style & flags) != flags) {
						lpnmtdi.uFlags |= OS.TTF_RTLREADING;
					} else {
						lpnmtdi.uFlags &= ~OS.TTF_RTLREADING;
					}
				}
				shell.setToolTipText (lpnmtdi, chars);
				OS.MoveMemory (lParam, lpnmtdi, NMTTDISPINFO.sizeof);
				return LRESULT.ZERO;
			}
			break;
		}
	}
	return super.wmNotify (hdr, wParam, lParam);
}

@Override
public String toString() {
	return super.toString() + " [layout=" + layout + "]";
}

private static void handleDPIChange(Widget widget, int newZoom, float scalingFactor) {
	if (!(widget instanceof Composite composite)) {
		return;
	}
	for (Control child : composite.getChildren()) {
		DPIZoomChangeRegistry.applyChange(child, newZoom, scalingFactor);
	}
	composite.redrawInPixels (null, true);
}
}
