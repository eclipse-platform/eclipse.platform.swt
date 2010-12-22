/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
	Control [] tabList;
	int layoutCount, backgroundMode;

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

static int checkStyle (int style) {
	style &= ~SWT.TRANSPARENT;
	return style;
}

int getCaretHandle () {
	return 0;
}

Control [] _getChildren () {
	int parentHandle = parentingHandle ();
	int elements = OS.Panel_Children (parentHandle);
	int count = OS.UIElementCollection_Count (elements);
	int caretHandle = getCaretHandle ();
	if (caretHandle != 0) count--;
	Control [] children = new Control [count];
	int index = count - 1;
	if (count != 0) {
		int enumerator = OS.UIElementCollection_GetEnumerator (elements);
		while (OS.IEnumerator_MoveNext (enumerator)) {
			int current = OS.UIElementCollection_Current (enumerator);
			if (caretHandle != 0 && OS.Object_Equals(caretHandle, current)) {
				OS.GCHandle_Free (current);
				continue;
			}
			Widget widget = display.getWidget (current);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [index--] = (Control)widget;
				}
			}
			OS.GCHandle_Free (current);
		}
		OS.GCHandle_Free (enumerator);
	}
	OS.GCHandle_Free (elements);
	if (index == -1) return children;
	Control [] newChildren = new Control [count - index - 1];
	System.arraycopy (children, index + 1, newChildren, 0, newChildren.length);
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

void addChild (Control control) {
	int parentHandle = parentingHandle ();
	int children = OS.Panel_Children (parentHandle);
	int index = 0;
	if (parentHandle != handle) {
		if (OS.UIElementCollection_Contains (children, handle)) {
			index = 1;
		}
	}
	int topHandle = control.topHandle ();
	OS.UIElementCollection_Insert (children, index, topHandle);
	OS.GCHandle_Free (children);
	OS.FrameworkElement_Width (topHandle, 0);
	OS.FrameworkElement_Height (topHandle, 0);
}

int backgoundProperty () {
	return OS.Panel_BackgroundProperty();
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

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	display.runSkin ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			state &= ~LAYOUT_CHANGED;
			size = layout.computeSize (this, wHint, hHint, changed);
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

void createHandle () {
	state |= CANVAS;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		state |= THEME_BACKGROUND;
	}
	boolean scrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	createHandle (scrolled, false);
}

void createHandle (boolean scrolled, boolean menubar) {
	handle = OS.gcnew_SWTCanvas (jniRef);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_FocusVisualStyle (handle, 0);
	if ((style & SWT.NO_FOCUS) == 0) { 
		OS.UIElement_Focusable (handle, true);
	}
	if (scrolled || menubar) {
		scrolledHandle = OS.gcnew_Grid ();
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);

		/* Create grid definition */
		int gridLength = OS.gcnew_GridLength (1, OS.GridUnitType_Auto);
		if (gridLength == 0) error (SWT.ERROR_NO_HANDLES);
		int columnDefinitions = OS.Grid_ColumnDefinitions (scrolledHandle);
		int column0 = OS.gcnew_ColumnDefinition ();
		if (column0 == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ColumnDefinitionCollection_Add (columnDefinitions, column0);
		int column1 = OS.gcnew_ColumnDefinition ();
		if (column1 == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ColumnDefinition_Width (column1, gridLength);
		OS.ColumnDefinitionCollection_Add (columnDefinitions, column1);
		int rowDefinitions = OS.Grid_RowDefinitions (scrolledHandle);
		int row0 = OS.gcnew_RowDefinition ();
		if (row0  == 0) error (SWT.ERROR_NO_HANDLES);
		OS.RowDefinition_Height (row0, gridLength);
		OS.RowDefinitionCollection_Add (rowDefinitions, row0);
		int row1 = OS.gcnew_RowDefinition ();
		if (row1  == 0) error (SWT.ERROR_NO_HANDLES);
		OS.RowDefinitionCollection_Add (rowDefinitions, row1);
		int row2 = OS.gcnew_RowDefinition ();
		if (row2  == 0) error (SWT.ERROR_NO_HANDLES);
		OS.RowDefinition_Height (row2, gridLength);
		OS.RowDefinitionCollection_Add (rowDefinitions, row2);
		OS.GCHandle_Free (column0);
		OS.GCHandle_Free (column1);
		OS.GCHandle_Free (row0);
		OS.GCHandle_Free (row1);
		OS.GCHandle_Free (row2);
		OS.GCHandle_Free (gridLength);
		OS.GCHandle_Free (columnDefinitions);
		OS.GCHandle_Free (rowDefinitions);

		/* create children */
		int children = OS.Panel_Children (scrolledHandle);
		OS.UIElementCollection_Add (children, handle);		
		OS.Grid_SetRow (handle, 1);
		OS.Grid_SetColumn (handle, 0);
		if ((style & SWT.V_SCROLL) != 0) {
			int vScroll = OS.gcnew_ScrollBar ();
			if (vScroll == 0) error (SWT.ERROR_NO_HANDLES);
			OS.ScrollBar_Orientation (vScroll, OS.Orientation_Vertical);
			OS.RangeBase_Maximum(vScroll, 90);
			OS.RangeBase_LargeChange (vScroll, 10);
			OS.ScrollBar_ViewportSize (vScroll, 10);
			OS.Grid_SetRow (vScroll, 1);
			OS.Grid_SetColumn (vScroll, 1);
			OS.UIElementCollection_Add (children, vScroll);			
			OS.GCHandle_Free (vScroll);
		}
		if ((style & SWT.H_SCROLL) != 0) {
			int hScroll = OS.gcnew_ScrollBar ();	
			if (hScroll == 0) error (SWT.ERROR_NO_HANDLES);
			OS.ScrollBar_Orientation (hScroll, OS.Orientation_Horizontal);
			OS.RangeBase_Maximum (hScroll, 90);
			OS.RangeBase_LargeChange (hScroll, 10);
			OS.ScrollBar_ViewportSize (hScroll, 10);
			OS.Grid_SetRow (hScroll, 2);
			OS.Grid_SetColumn (hScroll, 0);
			OS.UIElementCollection_Add (children, hScroll);
			OS.GCHandle_Free (hScroll);
		}
		OS.GCHandle_Free (children);		
	}
}

int defaultBackground () {
	if ((state & CANVAS) != 0) {
		return OS.SystemColors_ControlColor;
	}
	return 0;
}

void enableWidget (boolean enabled) {
	if ((state & CANVAS) != 0) {
		OS.UIElement_IsHitTestVisible (topHandle (), enabled);
	} else {
		OS.UIElement_IsEnabled (handle, enabled);
		OS.UIElement_IsHitTestVisible (parentingHandle (), enabled);
	}
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : parent.findDeferredControl ();
}

Menu [] findMenus (Control control) {
	if (control == this) return new Menu [0];
	Menu result [] = super.findMenus (control);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
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

void fixScrollbarVisibility () {
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
	checkWidget ();
	return _getChildren ();
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

Point getLocation (Control child) {
	int topHandle = child.topHandle ();
	int x = (int) OS.Canvas_GetLeft (topHandle);
	int y = (int) OS.Canvas_GetTop (topHandle);
	return new Point (x, y);
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

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
	/*
	* In WPF arrows key move the focus around, this
	* behavior is not expected in SWT.  
	*/
	if ((state & CANVAS) != 0) {
		int key = OS.KeyEventArgs_Key (e);
		switch (key) {
			case OS.Key_Up:
			case OS.Key_Left: 
			case OS.Key_Down:
			case OS.Key_Right: {
				OS.RoutedEventArgs_Handled (e, true);
				break;
			}
		}
	}
}

void HandlePreviewMouseDown(int sender, int e) {
	if (!checkEvent (e)) return;
	super.HandlePreviewMouseDown (sender, e);
	
	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			int children = OS.Panel_Children (handle);
			int count = OS.UIElementCollection_Count (children);
			OS.GCHandle_Free (children);
			int caretHandle = getCaretHandle ();
			if (caretHandle != 0) count--;
			if (count == 0) {
				OS.UIElement_Focus (handle);
			}
		}
	}
}

void HandlePreviewTextInput(int sender, int e) {
	super.HandlePreviewTextInput (sender, e);
	if (!checkEvent (e)) return;
	if ((state & CANVAS) != 0) {
		OS.RoutedEventArgs_Handled (e, true);
	}
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

boolean isTabGroup() {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup();
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

void OnRender(int drawingContext) {
	if (isDisposed ()) return;
	OS.SWTCanvas_Visual (handle, 0);
	if (display.ignoreRender) {
		display.addInvalidate (this);
		return;
	}
	if (!hooks (SWT.Paint)) return;
	int width = (int)OS.FrameworkElement_ActualWidth (handle);
	int height = (int)OS.FrameworkElement_ActualHeight (handle);
	if (width != 0 && height != 0) {
		GCData data = new GCData ();
		data.device = display;
		data.drawingContext = drawingContext;		 
		GC gc = GC.wpf_new (this, data);
		Event event = new Event ();
		event.gc = gc;
		event.width = width;
		event.height = height;
		sendEvent (SWT.Paint, event);
		event.gc = null;
		gc.dispose ();
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
	Rectangle clientArea = getClientArea();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x - clientArea.x + rect.width);
		height = Math.max (height, rect.y - clientArea.y + rect.height);
	}
	return new Point (width, height);
}

int parentingHandle () {
	return handle;
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

void releaseWidget () {
	super.releaseWidget ();
//	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
//		int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
//		if (hwndChild != 0) {
//			int threadId = OS.GetWindowThreadProcessId (hwndChild, null);
//			if (threadId != OS.GetCurrentThreadId ()) {
//				OS.ShowWindow (hwndChild, OS.SW_HIDE);
//				OS.SetParent (hwndChild, 0);
//			}
//		}
//	}
	layout = null;
	tabList = null;
}

void removeChild (Control control) {
	int topHandle = control.topHandle ();
	int parentHandle = parentingHandle ();
	int children = OS.Panel_Children (parentHandle);
	OS.UIElementCollection_Remove (children, topHandle);
	OS.GCHandle_Free (children);
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

void resized () {
	super.resized();
	if (isDisposed ()) return;
	if (layout != null) {
		markLayout (false, false);
		updateLayout (false, false);
	}
}

/**
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public void setAlpha(int alpha) {
	checkWidget ();
	int backgroundHandle = backgroundHandle ();
	int property = backgroundProperty ();
	int brush = OS.DependencyObject_GetValue (backgroundHandle, property);
	if (brush != 0) {
		int newBrush = OS.Freezable_Clone(brush);
		OS.Brush_Opacity (newBrush, (alpha & 0xFF) / (double)0xFF);
		OS.DependencyObject_SetValue (backgroundHandle, property, newBrush);
		OS.GCHandle_Free (brush);
		OS.GCHandle_Free (newBrush);
	}
	OS.GCHandle_Free (property);
//	OS.UIElement_Opacity (handle, (alpha & 0xFF) / (double)0xFF);
}

void setBackgroundBrush (int brush) {
	if (brush != 0) {
		OS.Panel_Background (handle, brush);
	} else {
		int property = OS.Panel_BackgroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();		
	}
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0 && layout != null) {
		markLayout (false, false);
		updateLayout (false, false);
	}
	return result;
}

void setClipping () {
	OS.UIElement_ClipToBounds (topHandle (), true);
}

public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

void setFont (int font, double size) {
}

void setForegroundBrush (int brush) {
	OS.UIElement_InvalidateVisual (handle);
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

boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) {
		takeFocus = hooksKeys ();
		if ((style & SWT.EMBEDDED) != 0) takeFocus = true;
	}
	if (takeFocus && setTabItemFocus ()) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
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

int traversalCode(int key, int event) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, event);
}

boolean translateTraversal (int msg) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) return false;
	return super.translateTraversal (msg);
}

void updateBackgroundColor () {
	super.updateBackgroundColor ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		if ((children [i].state & PARENT_BACKGROUND) != 0) {
			children [i].updateBackgroundColor ();
		}
	}
}

void updateBackgroundImage () {
	super.updateBackgroundImage ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		if ((children [i].state & PARENT_BACKGROUND) != 0) {
			children [i].updateBackgroundImage ();
		}
	}
}

void updateBackgroundMode () {
	super.updateBackgroundMode ();
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();		
	}
}

void updateFont (Font oldFont, Font newFont) {
	super.updateFont (oldFont, newFont);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		if (!control.isDisposed ()) {
			control.updateFont (oldFont, newFont);
		}
	}
}

void updateLayout (boolean all) {
	updateLayout (true, all);
}

void updateLayout (boolean resize, boolean all) {
	Composite parent = findDeferredControl ();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin ();
		layout.layout (this, changed);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].updateLayout (resize, all);
		}
	}
}

}
