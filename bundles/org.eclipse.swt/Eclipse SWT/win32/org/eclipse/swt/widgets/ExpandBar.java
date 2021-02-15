/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 * Instances of this class support the layout of selectable
 * expand bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ExpandItem</code>.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>V_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Expand, Collapse</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see ExpandItem
 * @see ExpandEvent
 * @see ExpandListener
 * @see ExpandAdapter
 * @see <a href="http://www.eclipse.org/swt/snippets/#expandbar">ExpandBar snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandBar extends Composite {
	ExpandItem[] items;
	int itemCount;
	ExpandItem focusItem;
	int spacing = 4;
	int yCurrentScroll;
	long hFont;


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
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>ExpandListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ExpandListener
 * @see #removeExpandListener
 */
public void addExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	style &= ~SWT.H_SCROLL;
	return style | SWT.NO_BACKGROUND;
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	int height = 0, width = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if (itemCount > 0) {
			long hDC = OS.GetDC (handle);
			long hTheme = 0;
			if (isAppThemed ()) {
				hTheme = display.hExplorerBarTheme ();
			}
			long hCurrentFont = 0, oldFont = 0;
			if (hTheme == 0) {
				if (hFont != 0) {
					hCurrentFont = hFont;
				} else {
					NONCLIENTMETRICS info = new NONCLIENTMETRICS ();
					info.cbSize = NONCLIENTMETRICS.sizeof;
					if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
						LOGFONT logFont = info.lfCaptionFont;
						hCurrentFont = OS.CreateFontIndirect (logFont);
					}
				}
				if (hCurrentFont != 0) {
					oldFont = OS.SelectObject (hDC, hCurrentFont);
				}
			}
			height += spacing;
			for (int i = 0; i < itemCount; i++) {
				ExpandItem item = items [i];
				height += item.getHeaderHeightInPixels ();
				if (item.expanded) height += item.height;
				height += spacing;
				width = Math.max (width, item.getPreferredWidth (hTheme, hDC));
			}
			if (hCurrentFont != 0) {
				OS.SelectObject (hDC, oldFont);
				if (hCurrentFont != hFont) OS.DeleteObject (hCurrentFont);
			}
			OS.ReleaseDC (handle, hDC);
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrimInPixels (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

@Override
void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
	state |= TRACK_MOUSE;
}

void createItem (ExpandItem item, int style, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ExpandItem [] newItems = new ExpandItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount - index);
	items [index] = item;
	itemCount++;
	if (focusItem == null) focusItem = item;

	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	item.width = Math.max (0, rect.right - rect.left - spacing * 2);
	layoutItems (index, true);
}

@Override
void createWidget () {
	super.createWidget ();
	items = new ExpandItem [4];
	if (!isAppThemed ()) {
		backgroundMode = SWT.INHERIT_DEFAULT;
	}
}

@Override
int defaultBackground() {
	if (!isAppThemed ()) {
		return OS.GetSysColor (OS.COLOR_WINDOW);
	}
	return super.defaultBackground();
}

void destroyItem (ExpandItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	if (item == focusItem) {
		int focusIndex = index > 0 ? index - 1 : 1;
		if (focusIndex < itemCount) {
			focusItem = items [focusIndex];
			focusItem.redraw (true);
		} else {
			focusItem = null;
		}
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.redraw (true);
	layoutItems (index, true);
}

@Override
void drawThemeBackground (long hDC, long hwnd, RECT rect) {
	RECT rect2 = new RECT ();
	OS.GetClientRect (handle, rect2);
	OS.MapWindowPoints (handle, hwnd, rect2, 2);
	OS.DrawThemeBackground (display.hExplorerBarTheme (), hDC, OS.EBP_NORMALGROUPBACKGROUND, 0, rect2, null);
}

void drawWidget (GC gc, RECT clipRect) {
	long hTheme = 0;
	if (isAppThemed ()) {
		hTheme = display.hExplorerBarTheme ();
	}
	if (hTheme != 0) {
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		OS.DrawThemeBackground (hTheme, gc.handle, OS.EBP_HEADERBACKGROUND, 0, rect, clipRect);
	} else {
		drawBackground (gc.handle);
	}
	boolean drawFocus = false;
	if (handle == OS.GetFocus ()) {
		int uiState = (int)OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
		drawFocus = (uiState & OS.UISF_HIDEFOCUS) == 0;
	}
	long hCurrentFont = 0, oldFont = 0;
	if (hTheme == 0) {
		if (hFont != 0) {
			hCurrentFont = hFont;
		} else {
			NONCLIENTMETRICS info = new NONCLIENTMETRICS ();
			info.cbSize = NONCLIENTMETRICS.sizeof;
			if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
				LOGFONT logFont = info.lfCaptionFont;
				hCurrentFont = OS.CreateFontIndirect (logFont);
			}
		}
		if (hCurrentFont != 0) {
			oldFont = OS.SelectObject (gc.handle, hCurrentFont);
		}
		if (foreground != -1) {
			OS.SetTextColor (gc.handle, foreground);
		}
	}
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		item.drawItem (gc, hTheme, clipRect, item == focusItem && drawFocus);
	}
	if (hCurrentFont != 0) {
		OS.SelectObject (gc.handle, oldFont);
		if (hCurrentFont != hFont) OS.DeleteObject (hCurrentFont);
	}
}

@Override
Control findBackgroundControl () {
	Control control = super.findBackgroundControl ();
	if (!isAppThemed ()) {
		if (control == null) control = this;
	}
	return control;
}

@Override
Control findThemeControl () {
	return isAppThemed () ? this : super.findThemeControl ();
}

int getBandHeight () {
	long hDC = OS.GetDC (handle);
	long oldHFont = OS.SelectObject (hDC, hFont == 0 ? defaultFont () : hFont);
	TEXTMETRIC lptm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, lptm);
	OS.SelectObject (hDC, oldHFont);
	OS.ReleaseDC (handle, hDC);
	return Math.max (ExpandItem.CHEVRON_SIZE, lptm.tmHeight + 4);
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ExpandItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return itemCount;
}

/**
 * Returns an array of <code>ExpandItem</code>s which are the items
 * in the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ExpandItem [] getItems () {
	checkWidget ();
	ExpandItem [] result = new ExpandItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

/**
 * Returns the receiver's spacing.
 *
 * @return the spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSpacing () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getSpacingInPixels ());
}

int getSpacingInPixels () {
	return spacing;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (ExpandItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i = 0; i < itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

boolean isAppThemed () {
	if (background != -1) return false;
	if (foreground != -1) return false;
	if (hFont != 0) return false;
	return OS.IsAppThemed ();
}

void layoutItems (int index, boolean setScrollbar) {
	if (index < itemCount) {
		int y = spacing - yCurrentScroll;
		for (int i = 0; i < index; i++) {
			ExpandItem item = items [i];
			if (item.expanded) y += item.height;
			y += item.getHeaderHeightInPixels () + spacing;
		}
		for (int i = index; i < itemCount; i++) {
			ExpandItem item = items [i];
			item.setBoundsInPixels (spacing, y, 0, 0, true, false);
			if (item.expanded) y += item.height;
			y += item.getHeaderHeightInPixels () + spacing;
		}
	}
	if (setScrollbar) setScrollbar ();
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (ExpandItem item : items) {
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	focusItem = null;
	super.releaseChildren (destroy);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see ExpandListener
 * @see #addExpandListener
 */
public void removeExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

@Override
void reskinChildren (int flags) {
	if (items != null) {
		for (ExpandItem item : items) {
			if (item != null ) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

@Override
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
	OS.RedrawWindow (handle, null, 0, flags);
}

@Override
public void setFont (Font font) {
	super.setFont (font);
	hFont = font != null ? font.handle : 0;
	layoutItems (0, true);
}

@Override
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
	OS.RedrawWindow (handle, null, 0, flags);
}

void setScrollbar () {
	if (itemCount == 0) return;
	if ((style & SWT.V_SCROLL) == 0) return;
	RECT rect = new RECT();
	OS.GetClientRect (handle, rect);
	int height = rect.bottom - rect.top;
	ExpandItem item = items [itemCount - 1];
	int maxHeight = item.y + getBandHeight () + spacing;
	if (item.expanded) maxHeight += item.height;

	//claim bottom free space
	if (yCurrentScroll > 0 && height > maxHeight) {
		yCurrentScroll = Math.max (0, yCurrentScroll + maxHeight - height);
		layoutItems (0, false);
	}
	maxHeight += yCurrentScroll;

	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE | OS.SIF_POS;
	info.nMin = 0;
	info.nMax = maxHeight;
	info.nPage = height;
	info.nPos = Math.min (yCurrentScroll, info.nMax);
	if (info.nPage != 0) info.nPage++;
	OS.SetScrollInfo (handle, OS.SB_VERT, info, true);
}

/**
 * Sets the receiver's spacing. Spacing specifies the number of points allocated around
 * each item.
 *
 * @param spacing the spacing around each item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSpacing (int spacing) {
	checkWidget ();
	setSpacingInPixels(DPIUtil.autoScaleUp(spacing));
}

void setSpacingInPixels (int spacing) {
	if (spacing < 0) return;
	if (spacing == this.spacing) return;
	this.spacing = spacing;
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int width = Math.max (0, (rect.right - rect.left) - spacing * 2);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		if (item.width != width) item.setBoundsInPixels (0, 0, width, item.height, false, true);
	}
	layoutItems (0, true);
	OS.InvalidateRect (handle, null, true);
}

@Override
boolean updateTextDirection(int textDirection) {
	if (super.updateTextDirection(textDirection)) {
		for (ExpandItem item : items) {
			if (item != null) {
				item.updateTextDirection(textDirection == AUTO_TEXT_DIRECTION ? AUTO_TEXT_DIRECTION : style & SWT.FLIP_TEXT_DIRECTION);
			}
		}
		return true;
	}
	return false;
}

void showItem (ExpandItem item) {
	Control control = item.control;
	if (control != null && !control.isDisposed ()) {
		control.setVisible (item.expanded);
	}
	item.redraw (true);
	int index = indexOf (item);
	layoutItems (index + 1, true);
}

void showFocus (boolean up) {
	RECT rect = new RECT();
	OS.GetClientRect (handle, rect);
	int height = rect.bottom - rect.top;
	int updateY = 0;
	if (up) {
		if (focusItem.y < 0) {
			updateY = Math.min (yCurrentScroll, -focusItem.y);
		}
	} else {
		int itemHeight = focusItem.y + getBandHeight ();
		if (focusItem.expanded) {
			if (height >= getBandHeight () + focusItem.height) {
				itemHeight += focusItem.height;
			}
		}
		if (itemHeight > height) {
			updateY = height - itemHeight;
		}
	}
	if (updateY != 0) {
		yCurrentScroll = Math.max (0, yCurrentScroll - updateY);
		if ((style & SWT.V_SCROLL) != 0) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_POS;
			info.nPos = yCurrentScroll;
			OS.SetScrollInfo (handle, OS.SB_VERT, info, true);
		}
		OS.ScrollWindowEx (handle, 0, updateY, null, null, 0, null, OS.SW_SCROLLCHILDREN | OS.SW_INVALIDATE);
		for (int i = 0; i < itemCount; i++) {
			items [i].y += updateY;
		}
	}
}

@Override
TCHAR windowClass () {
	return display.windowClass;
}

@Override
long windowProc () {
	return display.windowProc;
}

@Override
LRESULT WM_KEYDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	if (focusItem == null) return result;
	switch ((int)wParam) {
		case OS.VK_SPACE:
		case OS.VK_RETURN:
			Event event = new Event ();
			event.item = focusItem;
			sendEvent (focusItem.expanded ? SWT.Collapse : SWT.Expand, event);
			focusItem.expanded = !focusItem.expanded;
			showItem (focusItem);
			return LRESULT.ZERO;
		case OS.VK_UP: {
			int focusIndex = indexOf (focusItem);
			if (focusIndex > 0) {
				focusItem.redraw (true);
				focusItem = items [focusIndex - 1];
				focusItem.redraw (true);
				showFocus (true);
				return LRESULT.ZERO;
			}
			break;
		}
		case OS.VK_DOWN: {
			int focusIndex = indexOf (focusItem);
			if (focusIndex < itemCount - 1) {
				focusItem.redraw (true);
				focusItem = items [focusIndex + 1];
				focusItem.redraw (true);
				showFocus (false);
				return LRESULT.ZERO;
			}
			break;
		}
	}
	return result;
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	if (focusItem != null) focusItem.redraw (true);
	return result;
}

@Override
LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	int x = OS.GET_X_LPARAM (lParam);
	int y = OS.GET_Y_LPARAM (lParam);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		boolean hover = item.isHover (x, y);
		if (hover && focusItem != item) {
			focusItem.redraw (true);
			focusItem = item;
			focusItem.redraw (true);
			forceFocus ();
			break;
		}
	}
	return result;
}

@Override
LRESULT WM_LBUTTONUP (long wParam, long lParam) {
	LRESULT result = super.WM_LBUTTONUP (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	if (focusItem == null) return result;
	int x = OS.GET_X_LPARAM (lParam);
	int y = OS.GET_Y_LPARAM (lParam);
	boolean hover = focusItem.isHover (x, y);
	if (hover) {
		Event event = new Event ();
		event.item = focusItem;
		sendEvent (focusItem.expanded ? SWT.Collapse : SWT.Expand, event);
		focusItem.expanded = !focusItem.expanded;
		showItem (focusItem);
	}
	return result;
}

@Override
LRESULT WM_MOUSELEAVE (long wParam, long lParam) {
	LRESULT result = super.WM_MOUSELEAVE (wParam, lParam);
	if (result != null) return result;
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items [i];
		if (item.hover) {
			item.hover = false;
			item.redraw (false);
			break;
		}
	}
	return result;
}

@Override
LRESULT WM_MOUSEMOVE (long wParam, long lParam) {
	LRESULT result = super.WM_MOUSEMOVE (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	int x = OS.GET_X_LPARAM (lParam);
	int y = OS.GET_Y_LPARAM (lParam);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items [i];
		boolean hover = item.isHover (x, y);
		if (item.hover != hover) {
			item.hover = hover;
			item.redraw (false);
		}
	}
	return result;
}

@Override
LRESULT WM_MOUSEWHEEL (long wParam, long lParam) {
	return wmScrollWheel (true, wParam, lParam, false);
}

@Override
LRESULT WM_PAINT (long wParam, long lParam) {
	if ((state & DISPOSE_SENT) != 0) return LRESULT.ZERO;

	PAINTSTRUCT ps = new PAINTSTRUCT ();
	GCData data = new GCData ();
	data.ps = ps;
	data.hwnd = handle;
	GC gc = new_GC (data);
	if (gc != null) {
		int width = ps.right - ps.left;
		int height = ps.bottom - ps.top;
		if (width != 0 && height != 0) {
			RECT rect = new RECT ();
			OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
			drawWidget (gc, rect);
			if (hooks (SWT.Paint) || filters (SWT.Paint)) {
				Event event = new Event ();
				event.gc = gc;
				event.setBoundsInPixels(new Rectangle(rect.left, rect.top, width, height));
				sendEvent (SWT.Paint, event);
				event.gc = null;
			}
		}
		gc.dispose ();
	}
	return LRESULT.ZERO;
}

@Override
LRESULT WM_PRINTCLIENT (long wParam, long lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	GCData data = new GCData ();
	data.device = display;
	data.foreground = getForegroundPixel ();
	GC gc = GC.win32_new (wParam, data);
	drawWidget (gc, rect);
	gc.dispose ();
	return result;
}

@Override
LRESULT WM_SETCURSOR (long wParam, long lParam) {
	LRESULT result = super.WM_SETCURSOR (wParam, lParam);
	if (result != null) return result;
	int hitTest = (short) OS.LOWORD (lParam);
	if (hitTest == OS.HTCLIENT) {
		for (int i = 0; i < itemCount; i++) {
			ExpandItem item = items [i];
			if (item.hover) {
				long hCursor = OS.LoadCursor (0, OS.IDC_HAND);
				OS.SetCursor (hCursor);
				return LRESULT.ONE;
			}
		}
	}
	return result;
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	if (focusItem != null) focusItem.redraw (true);
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int width = Math.max (0, (rect.right - rect.left) - spacing * 2);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		if (item.width != width) item.setBoundsInPixels (0, 0, width, item.height, false, true);
	}
	setScrollbar ();
	OS.InvalidateRect (handle, null, true);
	return result;
}

@Override
LRESULT wmScroll (ScrollBar bar, boolean update, long hwnd, int msg, long wParam, long lParam) {
	LRESULT result = super.wmScroll (bar, true, hwnd, msg, wParam, lParam);
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_POS;
	OS.GetScrollInfo (handle, OS.SB_VERT, info);
	int updateY = yCurrentScroll - info.nPos;
	OS.ScrollWindowEx (handle, 0, updateY, null, null, 0, null, OS.SW_SCROLLCHILDREN | OS.SW_INVALIDATE);
	yCurrentScroll = info.nPos;
	if (updateY != 0) {
		for (int i = 0; i < itemCount; i++) {
			items [i].y += updateY;
		}
	}
	return result;
}
}
