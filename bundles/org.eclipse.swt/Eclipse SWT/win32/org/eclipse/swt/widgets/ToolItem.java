/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	ToolBar parent;
	Control control;
	String toolTipText;
	Image disabledImage, hotImage;
	Image disabledImage2;
	int id;
	short cx;
	int foreground = -1, background = -1;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void click (boolean dropDown) {
	long hwnd = parent.handle;
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) return;
	int index = (int)OS.SendMessage (hwnd, OS.TB_COMMANDTOINDEX, id, 0);
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.TB_GETITEMRECT, index, rect);
	int hotIndex = (int)OS.SendMessage (hwnd, OS.TB_GETHOTITEM, 0, 0);

	/*
	* In order to emulate all the processing that
	* happens when a mnemonic key is pressed, fake
	* a mouse press and release.  This will ensure
	* that radio and pull down items are handled
	* properly.
	*/
	int y = rect.top + (rect.bottom - rect.top) / 2;
	long lParam = OS.MAKELPARAM (dropDown ? rect.right - 1 : rect.left, y);
	parent.ignoreMouse = true;
	OS.SendMessage (hwnd, OS.WM_LBUTTONDOWN, 0, lParam);
	OS.SendMessage (hwnd, OS.WM_LBUTTONUP, 0, lParam);
	parent.ignoreMouse = false;

	if (hotIndex != -1) {
		OS.SendMessage (hwnd, OS.TB_SETHOTITEM, hotIndex, 0);
	}
}

Widget [] computeTabList () {
	if (isTabGroup ()) {
		if (getEnabled ()) {
			if ((style & SWT.SEPARATOR) != 0) {
				if (control != null) return control.computeTabList();
			} else {
				return new Widget [] {this};
			}
		}
	}
	return new Widget [0];
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	return DPIUtil.autoScaleDown(getBoundsInPixels());
}

Rectangle getBoundsInPixels () {
	long hwnd = parent.handle;
	int index = (int)OS.SendMessage (hwnd, OS.TB_COMMANDTOINDEX, id, 0);
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.TB_GETITEMRECT, index, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

/**
 * Returns the receiver's background color.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on some versions of Windows the background of a TabFolder,
 * is a gradient rather than a solid color.
 * </p>
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public Color getBackground () {
	checkWidget ();
	return Color.win32_new (display, parent.getBackgroundPixel (this));
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) {
		return (state & DISABLED) == 0;
	}
	long hwnd = parent.handle;
	long fsState = OS.SendMessage (hwnd, OS.TB_GETSTATE, id, 0);
	return (fsState & OS.TBSTATE_ENABLED) != 0;
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public Color getForeground () {
	checkWidget ();
	return Color.win32_new (display, parent.getForegroundPixel (this));
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's enabled image if it has one, or null
 * if it does not.
 *
 * @return the receiver's enabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public Image getImage () {
	return super.getImage();
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	long hwnd = parent.handle;
	long fsState = OS.SendMessage (hwnd, OS.TB_GETSTATE, id, 0);
	return (fsState & OS.TBSTATE_CHECKED) != 0;
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	return DPIUtil.autoScaleDown(getWidthInPixels());
}

int getWidthInPixels () {
	long hwnd = parent.handle;
	int index = (int)OS.SendMessage (hwnd, OS.TB_COMMANDTOINDEX, id, 0);
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.TB_GETITEMRECT, index, rect);
	return rect.right - rect.left;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

boolean isTabGroup () {
	ToolItem [] tabList = parent._getTabItemList ();
	if (tabList != null) {
		for (ToolItem item : tabList) {
			if (item == this) return true;
		}
	}
	if ((style & SWT.SEPARATOR) != 0) return true;
	int index = parent.indexOf (this);
	if (index == 0) return true;
	ToolItem previous = parent.getItem (index - 1);
	return (previous.getStyle () & SWT.SEPARATOR) != 0;
}

void redraw () {
	RECT rect = new RECT ();
	if (OS.SendMessage (parent.handle, OS.TB_GETRECT, id, rect) != 0) {
		OS.InvalidateRect (parent.handle, rect, true);
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	releaseImages ();
	control = null;
	toolTipText = null;
	disabledImage = hotImage = null;
	if (disabledImage2 != null) disabledImage2.dispose ();
	disabledImage2 = null;
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	id = -1;
}

void releaseImages () {
	TBBUTTONINFO info = new TBBUTTONINFO ();
	info.cbSize = TBBUTTONINFO.sizeof;
	info.dwMask = OS.TBIF_IMAGE | OS.TBIF_STYLE;
	long hwnd = parent.handle;
	OS.SendMessage (hwnd, OS.TB_GETBUTTONINFO, id, info);
	/*
	* Feature in Windows.  For some reason, a tool item that has
	* the style BTNS_SEP does not return I_IMAGENONE when queried
	* for an image index, despite the fact that no attempt has been
	* made to assign an image to the item.  As a result, operations
	* on an image list that use the wrong index cause random results.
	* The fix is to ensure that the tool item is not a separator
	* before using the image index.  Since separators cannot have
	* an image and one is never assigned, this is not a problem.
	*/
	if ((info.fsStyle & OS.BTNS_SEP) == 0 && info.iImage != OS.I_IMAGENONE) {
		ImageList imageList = parent.getImageList ();
		ImageList hotImageList = parent.getHotImageList ();
		ImageList disabledImageList = parent.getDisabledImageList();
		if (imageList != null) imageList.put (info.iImage, null);
		if (hotImageList != null) hotImageList.put (info.iImage, null);
		if (disabledImageList != null) disabledImageList.put (info.iImage, null);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

void resizeControl () {
	if (control != null && !control.isDisposed ()) {
		/*
		* Set the size and location of the control
		* separately to minimize flashing in the
		* case where the control does not resize
		* to the size that was requested.  This
		* case can occur when the control is a
		* combo box.
		*/
		Rectangle itemRect = getBounds ();
		control.setSize (itemRect.width, itemRect.height);
		Rectangle rect = control.getBounds ();
		rect.x = itemRect.x + (itemRect.width - rect.width) / 2;
		rect.y = itemRect.y + (itemRect.height - rect.height) / 2;
		control.setLocation (rect.x, rect.y);
	}
}

void selectRadio () {
	int index = 0;
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	parent.state |= CUSTOM_DRAW_ITEM;
	int pixel = (color != null) ? color.handle : -1;
	if (pixel == background) return;
	background = pixel;
	redraw ();
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	parent.layout(true);
	this.control = control;
	/*
	* Feature in Windows.  When a tool bar wraps, tool items
	* with the style BTNS_SEP are used as wrap points.  This
	* means that controls that are placed on top of separator
	* items are not positioned properly.  Also, vertical tool
	* bars are implemented using TB_SETROWS to set the number
	* of rows.  When a control is placed on top of a separator,
	* the height of the separator does not grow.  The fix in
	* both cases is to change the tool item style from BTNS_SEP
	* to BTNS_BUTTON, causing the item to wrap like a tool item
	* button.  The new tool item button is disabled to avoid key
	* traversal and the image is set to I_IMAGENONE to avoid
	* getting the first image from the image list.
	*/
	if ((parent.style & (SWT.WRAP | SWT.VERTICAL)) != 0) {
		boolean changed = false;
		long hwnd = parent.handle;
		TBBUTTONINFO info = new TBBUTTONINFO ();
		info.cbSize = TBBUTTONINFO.sizeof;
		info.dwMask = OS.TBIF_STYLE | OS.TBIF_STATE;
		OS.SendMessage (hwnd, OS.TB_GETBUTTONINFO, id, info);
		if (control == null) {
			if ((info.fsStyle & OS.BTNS_SEP) == 0) {
				changed = true;
				info.fsStyle &= ~(OS.BTNS_BUTTON | OS.BTNS_SHOWTEXT);
				info.fsStyle |= OS.BTNS_SEP;
				if ((state & DISABLED) != 0) {
					info.fsState &= ~OS.TBSTATE_ENABLED;
				} else {
					info.fsState |= OS.TBSTATE_ENABLED;
				}
			}
		} else {
			if ((info.fsStyle & OS.BTNS_SEP) != 0) {
				changed = true;
				info.fsStyle &= ~OS.BTNS_SEP;
				info.fsStyle |= OS.BTNS_BUTTON | OS.BTNS_SHOWTEXT;
				info.fsState &= ~OS.TBSTATE_ENABLED;
				info.dwMask |= OS.TBIF_IMAGE;
				info.iImage = OS.I_IMAGENONE;
			}
		}
		if (changed) {
			OS.SendMessage (hwnd, OS.TB_SETBUTTONINFO, id, info);
			/*
			* Bug in Windows.  When TB_SETBUTTONINFO changes the
			* style of a tool item from BTNS_SEP to BTNS_BUTTON
			* and the tool bar is wrapped, the tool bar does not
			* redraw properly.  Windows uses separator items as
			* wrap points and sometimes draws etching above or
			* below and entire row.  The fix is to redraw the
			* tool bar.
			*/
			if (OS.SendMessage (hwnd, OS.TB_GETROWS, 0, 0) > 1) {
				OS.InvalidateRect (hwnd, null, true);
			}
		}
	}
	resizeControl ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	long hwnd = parent.handle;
	int fsState = (int)OS.SendMessage (hwnd, OS.TB_GETSTATE, id, 0);
	/*
	* Feature in Windows.  When TB_SETSTATE is used to set the
	* state of a tool item, the item redraws even when the state
	* has not changed.  The fix is to detect this case and avoid
	* setting the state.
	*/
	if (((fsState & OS.TBSTATE_ENABLED) != 0) == enabled) return;
	if (enabled) {
		fsState |= OS.TBSTATE_ENABLED;
		state &= ~DISABLED;
	} else {
		fsState &= ~OS.TBSTATE_ENABLED;
		state |= DISABLED;
	}
	OS.SendMessage (hwnd, OS.TB_SETSTATE, id, fsState);
	if ((style & SWT.SEPARATOR) == 0) {
		if (image != null) updateImages (enabled && parent.getEnabled ());
	}
	if (!enabled && parent.lastFocusId == id) {
		parent.lastFocusId = -1;
	}
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (this.disabledImage == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	parent.layout(isImageSizeChanged(disabledImage, image));
	disabledImage = image;
	updateImages (getEnabled () && parent.getEnabled ());
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	parent.state |= CUSTOM_DRAW_ITEM;
	int pixel = (color != null) ? color.handle : -1;
	if (pixel == foreground) return;
	foreground = pixel;
	redraw ();
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (this.hotImage == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	parent.layout(isImageSizeChanged(hotImage, image));
	hotImage = image;
	updateImages (getEnabled () && parent.getEnabled ());
}

@Override
public void setImage (Image image) {
	checkWidget();
	if (this.image == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	parent.layout(isImageSizeChanged(super.image, image));
	super.setImage (image);
	updateImages (getEnabled () && parent.getEnabled ());
}

boolean isImageSizeChanged(Image oldImage, Image image) {
	boolean changed = true;
	// check if image size really changed for old and new images
	if (oldImage != null && !oldImage.isDisposed() && image != null && !image.isDisposed()) {
		changed = !oldImage.getBounds().equals(image.getBounds());
	}
	return changed;
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	long hwnd = parent.handle;
	int fsState = (int)OS.SendMessage (hwnd, OS.TB_GETSTATE, id, 0);
	/*
	* Feature in Windows.  When TB_SETSTATE is used to set the
	* state of a tool item, the item redraws even when the state
	* has not changed.  The fix is to detect this case and avoid
	* setting the state.
	*/
	if (((fsState & OS.TBSTATE_CHECKED) != 0) == selected) return;
	if (selected) {
		fsState |= OS.TBSTATE_CHECKED;
	} else {
		fsState &= ~OS.TBSTATE_CHECKED;
	}
	OS.SendMessage (hwnd, OS.TB_SETSTATE, id, fsState);

	/*
	* Bug in Windows.  When a tool item with the style
	* BTNS_CHECK or BTNS_CHECKGROUP is selected and then
	* disabled, the item does not draw using the disabled
	* image.  The fix is to use the disabled image in all
	* image lists for the item.
	*
	* NOTE: This means that the image list must be updated
	* when the selection changes in a disabled tool item.
	*/
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		if (!getEnabled () || !parent.getEnabled ()) {
			updateImages (false);
		}
	}
}

@Override
boolean setTabItemFocus () {
	if (parent.setTabItemFocus ()) {
		long hwnd = parent.handle;
		int index = (int)OS.SendMessage (hwnd, OS.TB_COMMANDTOINDEX, id, 0);
		OS.SendMessage (hwnd, OS.TB_SETHOTITEM, index, 0);
		return true;
	}
	return false;
}

void _setText (String string) {
	long hwnd = parent.handle;
	TBBUTTONINFO info = new TBBUTTONINFO ();
	info.cbSize = TBBUTTONINFO.sizeof;
	info.dwMask = OS.TBIF_TEXT | OS.TBIF_STYLE;
	info.fsStyle = (byte) (widgetStyle () | OS.BTNS_AUTOSIZE);
	long hHeap = OS.GetProcessHeap (), pszText = 0;
	if (string.length () != 0) {
		info.fsStyle |= OS.BTNS_SHOWTEXT;
		TCHAR buffer;
		if ((style & SWT.FLIP_TEXT_DIRECTION) != 0) {
			int bits  = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
			if ((bits & OS.WS_EX_LAYOUTRTL) != 0) {
				buffer = new TCHAR (parent.getCodePage (), LRE + string, true);
			} else {
				buffer = new TCHAR (parent.getCodePage (), RLE + string, true);
			}
		} else {
			buffer = new TCHAR(parent.getCodePage (), string, true);
		}
		int byteCount = buffer.length () * TCHAR.sizeof;
		pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (pszText, buffer, byteCount);
		info.pszText = pszText;
	}
	OS.SendMessage (hwnd, OS.TB_SETBUTTONINFO, id, info);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	if (string.equals (text)) return;
	parent.layout(true);
	super.setText (string);
	if ((state & HAS_AUTO_DIRECTION) == 0 || !updateTextDirection (AUTO_TEXT_DIRECTION)) {
		_setText (string);
	}
	/*
	* Bug in Windows.  For some reason, when the font is set
	* before any tool item has text, the tool items resize to
	* a very small size.  Also, a tool item will only show text
	* when text has already been set on one item and then a new
	* item is created.  The fix is to use WM_SETFONT to force
	* the tool bar to redraw and layout.
	*/
	parent.setDropDownItems (false);
	long hwnd = parent.handle;
	long hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
	OS.SendMessage (hwnd, OS.WM_SETFONT, hFont, 0);
	parent.setDropDownItems (true);
	parent.layoutItems ();
}

@Override
boolean updateTextDirection(int textDirection) {
	/* AUTO is handled by super */
	if (super.updateTextDirection(textDirection) && text.length() != 0) {
		_setText (text);
		return true;
	}
	return false;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width. If the new value is <code>SWT.DEFAULT</code>,
 * the width is a fixed-width area whose amount is determined by the platform.
 * If the new value is 0 a vertical or horizontal line will be drawn, depending
 * on the setting of the corresponding style bit (<code>SWT.VERTICAL</code> or
 * <code>SWT.HORIZONTAL</code>). If the new value is <code>SWT.SEPARATOR_FILL</code>
 * a variable-width space is inserted that acts as a spring between the two adjoining
 * items which will push them out to the extent of the containing ToolBar.
 *
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	setWidthInPixels(DPIUtil.autoScaleUp(width));
}

void setWidthInPixels (int width) {
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	long hwnd = parent.handle;
	TBBUTTONINFO info = new TBBUTTONINFO ();
	info.cbSize = TBBUTTONINFO.sizeof;
	info.dwMask = OS.TBIF_SIZE;
	info.cx = cx = (short) width;
	OS.SendMessage (hwnd, OS.TB_SETBUTTONINFO, id, info);
	parent.layoutItems ();
}

void updateImages (boolean enabled) {
	if ((style & SWT.SEPARATOR) != 0) return;
	long hwnd = parent.handle;
	TBBUTTONINFO info = new TBBUTTONINFO ();
	info.cbSize = TBBUTTONINFO.sizeof;
	info.dwMask = OS.TBIF_IMAGE;
	OS.SendMessage (hwnd, OS.TB_GETBUTTONINFO, id, info);
	if (info.iImage == OS.I_IMAGENONE && image == null) return;
	ImageList imageList = parent.getImageList ();
	ImageList hotImageList = parent.getHotImageList ();
	ImageList disabledImageList = parent.getDisabledImageList();
	if (info.iImage == OS.I_IMAGENONE) {
		Rectangle bounds = image.getBoundsInPixels ();
		int listStyle = parent.style & SWT.RIGHT_TO_LEFT;
		if (imageList == null) {
			imageList = display.getImageListToolBar (listStyle, bounds.width, bounds.height);
		}
		if (disabledImageList == null) {
			disabledImageList = display.getImageListToolBarDisabled (listStyle, bounds.width, bounds.height);
		}
		if (hotImageList == null) {
			hotImageList = display.getImageListToolBarHot (listStyle, bounds.width, bounds.height);
		}
		Image disabled = disabledImage;
		if (disabledImage == null) {
			if (disabledImage2 != null) disabledImage2.dispose ();
			disabledImage2 = null;
			disabled = image;
			if (!enabled) {
				disabled = disabledImage2 = new Image (display, image, SWT.IMAGE_DISABLE);
			}
		}
		/*
		* Bug in Windows.  When a tool item with the style
		* BTNS_CHECK or BTNS_CHECKGROUP is selected and then
		* disabled, the item does not draw using the disabled
		* image.  The fix is to assign the disabled image in
		* all image lists.
		*/
		Image image2 = image, hot = hotImage;
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			if (!enabled) image2 = hot = disabled;
		}
		info.iImage = imageList.add (image2);
		disabledImageList.add (disabled);
		hotImageList.add (hot != null ? hot : image2);
		parent.setImageList (imageList);
		parent.setDisabledImageList (disabledImageList);
		parent.setHotImageList (hotImageList);
	} else {
		Image disabled = null;
		if (disabledImageList != null) {
			if (image != null) {
				if (disabledImage2 != null) disabledImage2.dispose ();
				disabledImage2 = null;
				disabled = disabledImage;
				if (disabledImage == null) {
					disabled = image;
					if (!enabled) {
						disabled = disabledImage2 = new Image (display, image, SWT.IMAGE_DISABLE);
					}
				}
			}
			disabledImageList.put (info.iImage, disabled);
		}
		/*
		* Bug in Windows.  When a tool item with the style
		* BTNS_CHECK or BTNS_CHECKGROUP is selected and then
		* disabled, the item does not draw using the disabled
		* image.  The fix is to use the disabled image in all
		* image lists.
		*/
		Image image2 = image, hot = hotImage;
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			if (!enabled) image2 = hot = disabled;
		}
		if (imageList != null) imageList.put (info.iImage, image2);
		if (hotImageList != null) {
			hotImageList.put (info.iImage, hot != null ? hot : image2);
		}
		if (image == null) info.iImage = OS.I_IMAGENONE;
	}

	/*
	* Bug in Windows.  If the width of an item has already been
	* calculated, the tool bar control will not recalculate it to
	* include the space for the image.  The fix is to set the width
	* to zero, forcing the control recalculate the width for the item.
	*/
	info.dwMask |= OS.TBIF_SIZE;
	info.cx = 0;
	OS.SendMessage (hwnd, OS.TB_SETBUTTONINFO, id, info);
	long hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
	OS.SendMessage (hwnd, OS.WM_SETFONT, hFont, 0);
	parent.layoutItems ();
}

int widgetStyle () {
	if ((style & SWT.DROP_DOWN) != 0) return OS.BTNS_DROPDOWN;
	if ((style & SWT.PUSH) != 0) return OS.BTNS_BUTTON;
	if ((style & SWT.CHECK) != 0) return OS.BTNS_CHECK;
	/*
	* This code is intentionally commented.  In order to
	* consistently support radio tool items across platforms,
	* the platform radio behavior is not used.
	*/
//	if ((style & SWT.RADIO) != 0) return OS.BTNS_CHECKGROUP;
	if ((style & SWT.RADIO) != 0) return OS.BTNS_CHECK;
	if ((style & SWT.SEPARATOR) != 0) return OS.BTNS_SEP;
	return OS.BTNS_BUTTON;
}

LRESULT wmCommandChild (long wParam, long lParam) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	sendSelectionEvent (SWT.Selection);
	return null;
}

}
