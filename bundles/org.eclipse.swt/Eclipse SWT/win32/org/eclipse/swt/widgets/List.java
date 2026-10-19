/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Hitesh Soliwal (Synexia) - viewport mediation hooks (ListViewport)
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a selectable user interface
 * object that displays a list of strings and issues notification
 * when a string is selected.  A list may be single or multi select.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, NO_SEARCH, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData</dd>
 * </dl>
 * <p>
 * Note: Only one of SINGLE and MULTI may be specified.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>List</code> whose
 * native control keeps only the item count: item text is held in one
 * <code>String</code> reference per item and is drawn only for the rows on
 * screen. Items added with <code>add</code> or <code>setItems</code> behave as
 * in a plain list; items created by <code>setItemCount</code> or emptied by
 * <code>clear</code> are requested with an <code>SWT.SetData</code> event
 * (<code>event.index</code> is the row) when they are first drawn or read, and
 * the listener answers with <code>setItem(event.index, text)</code>.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="https://eclipse.dev/eclipse/swt/snippets/#list">List snippets</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/examples.html">SWT Example: ControlExample</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class List extends Scrollable {
	static final int INSET = 3;
	static final long ListProc;
	static final TCHAR ListClass = new TCHAR ("LISTBOX", true);
	boolean addedUCC = false; // indicates whether Bidi UCC were added; 'state & HAS_AUTO_DIRECTION' isn't a sufficient indicator
	final ViewportRuntime viewportRuntime = new ViewportRuntime ();
	/*
	 * SWT.VIRTUAL (owner-data viewport): the native list box is created with
	 * LBS_NODATA | LBS_OWNERDRAWFIXED and keeps only the item count and the
	 * selection; the text lives in itemStore and is painted in WM_DRAWITEM for
	 * the rows on screen. itemStore is null for a plain list.
	 */
	VirtualListModel itemStore;
	int storeBits, setDataIndex = -1, setDataCount;
	long measureDC;
	char [] textBuffer;
	DRAWITEMSTRUCT drawStruct;
	RECT drawRect, measureRect, scrollRect;
	SCROLLINFO scrollInfo;
	static final int STORE_EMPTY_EXTENT_INSET = 1 << 0, STORE_IN_SET_DATA = 1 << 1, STORE_ADDED_UCC = 1 << 2;
	static final int STORE_PREFETCH_PENDING = 1 << 3, STORE_SCROLLED_UP = 1 << 4;
	/*
	 * Content freeze: while set, WM_DRAWITEM paints nothing and requests no
	 * SetData, but the list box and its scroll bar keep running (no
	 * WM_SETREDRAW, which would also freeze the scroll bar). Set around bulk
	 * count changes and while the thumb is dragged faster than a page per
	 * message; the thaw invalidates the client area once.
	 */
	static final int STORE_CONTENT_FROZEN = 1 << 5, STORE_THUMB_FROZEN = 1 << 6;
	/*
	 * SetData negotiation: the visible rows are requested by the native paint;
	 * after the debounce delay the overscan rows below and above, then one
	 * prefetch page in the scroll direction, STORE_SETDATA_BATCH rows per step.
	 * A new paint or scroll restarts the delay (debounce); every step re-reads
	 * the top row, so work for a stale position is dropped (cancel); nothing is
	 * requested while the list is not visible.
	 */
	static final int STORE_SETDATA_BATCH = 32, STORE_PREFETCH_DELAY = 40, STORE_PREFETCH_PAGES = 1;
	Runnable storePrefetch;
	int storeLastTop;
	/* text-direction changes re-add rows natively up to this count, like the plain list */
	static final int STORE_READD_LIMIT = 4096;
	/* x offset of the item text inside the item rectangle, as the plain list box draws it */
	static final int STORE_TEXT_X = 2;
	/* the documented LB_SETANCHORINDEX; OS.LB_SETANCHORINDEX is 0xf19c */
	static final int STORE_LB_SETANCHORINDEX = 0x19c;
	/*
	 * MULTI count changes use row messages (exact native semantics) while
	 * rows * count stays under STORE_ROW_MESSAGE_WORK, each row message moving the
	 * selection state of every row behind it; larger changes rebuild with one
	 * LB_SETCOUNT. removeRebuildThreshold is the smallest rebuilt change (0 forces
	 * the rebuild, for the differential tests).
	 */
	static int removeRebuildThreshold = 64;
	static final int STORE_ROW_MESSAGE_WORK = 1 << 26;
	/* rows up to which the horizontal extent of a VIRTUAL list is kept exact on every change */
	static final int STORE_EXACT_EXTENT_LIMIT = 4096;
	/* measurements of more rows than this show the wait cursor */
	static final int STORE_BUSY_ROWS = 100000;
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ListClass, lpWndClass);
		ListProc = lpWndClass.lpfnWndProc;
		VirtualListModel.growth = List::growCapacity;
	}

	/* Viewport mediation of a list without SWT.VIRTUAL (see ListViewport); null in the original mode. */
	ListViewport viewport;

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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public List (Composite parent, int style) {
	super (parent, checkStyle (style));
	viewportRuntime.initializeOrigin (0, 0);
	if ((this.style & SWT.VIRTUAL) != 0) {
		itemStore = new VirtualListModel (this::measureStoreText, true);
		updateStoreItemHeight (OS.SendMessage (handle, OS.WM_GETFONT, 0, 0));
	}
	if (ListViewport.mediates (this.style)) {
		viewport = new ListViewport (this);
		viewport.install ();
	}
}
/**
 * Adds the argument to the end of the receiver's list.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	addItem (string);
	M3Emptiness.changed (this);
}

void addItem (String string) {
	checkWidget ();
    if (string == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	if (viewport != null) {
		viewport.add (string, viewport.getItemCount ());
		return;
	}
	if (itemStore != null) {
		storeInsert (string, itemStore.getItemCount ());
		return;
	}
	TCHAR buffer = new TCHAR (string, true);
	int result = (int)OS.SendMessage (handle, OS.LB_ADDSTRING, 0, buffer);
    if (result == OS.LB_ERR) {
        error(SWT.ERROR_ITEM_NOT_ADDED);
    }
    if (result == OS.LB_ERRSPACE) {
        error(SWT.ERROR_ITEM_NOT_ADDED);
    }
    if ((style & SWT.H_SCROLL) != 0) {
        setScrollWidth(buffer.chars, true);
    }
}
/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	insertItem (string, index);
	M3Emptiness.changed (this);
}

void insertItem (String string, int index) {
	checkWidget ();
    if (string == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (index == -1) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (viewport != null) {
		viewport.add (string, index);
		return;
	}
	if (itemStore != null) {
		if (!(0 <= index && index <= itemStore.getItemCount ())) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		storeInsert (string, index);
		return;
	}
	TCHAR buffer = new TCHAR (string, true);
	int result = (int)OS.SendMessage (handle, OS.LB_INSERTSTRING, index, buffer);
    if (result == OS.LB_ERRSPACE) {
        error(SWT.ERROR_ITEM_NOT_ADDED);
    }
	if (result == OS.LB_ERR) {
		int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		if (0 <= index && index <= count) {
			error (SWT.ERROR_ITEM_NOT_ADDED);
		} else {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
    if ((style & SWT.H_SCROLL) != 0) {
        setScrollWidth(buffer.chars, true);
    }
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the selection changes.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
    if (handle == 0) {
        return 0;
    }
	if (viewport != null && msg == OS.WM_VSCROLL) return viewport.onVScroll (wParam);
	if (viewport != null && msg == OS.WM_MOUSEWHEEL) return viewport.onWheel (hwnd, wParam, lParam);
	boolean redraw = false;
	switch (msg) {
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL: {
			redraw = findImageControl () != null && getDrawing() && OS.IsWindowVisible (handle);
            if (redraw) {
                OS.DefWindowProc(handle, OS.WM_SETREDRAW, 0, 0);
            }
			break;
		}
	}
	if (viewport != null && viewport.beforeNative (msg, wParam, lParam)) return 0;
	long code;
	int scrollCode = OS.LOWORD (wParam);
	int topBefore = itemStore != null && msg == OS.WM_VSCROLL ? (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0) : 0;
	if (itemStore != null && msg == OS.WM_VSCROLL && (scrollCode == OS.SB_THUMBTRACK || scrollCode == OS.SB_THUMBPOSITION)) {
		/*
		* WM_VSCROLL carries a 16-bit thumb position (N16); above 65535 rows
		* scroll to the 32-bit track position of the scroll bar instead.
		*/
		if (scrollInfo == null) {
			scrollInfo = new SCROLLINFO ();
			scrollInfo.cbSize = SCROLLINFO.sizeof;
		}
		SCROLLINFO info = scrollInfo;
		info.fMask = OS.SIF_TRACKPOS;
		if (OS.GetScrollInfo (handle, OS.SB_VERT, info) && info.nTrackPos > 0xFFFF && (info.nTrackPos & 0xFFFF) == OS.HIWORD (wParam)) {
			code = OS.SendMessage (handle, OS.LB_SETTOPINDEX, info.nTrackPos, 0) == OS.LB_ERR ? 1 : 0;
		} else {
			code = OS.CallWindowProc (ListProc, hwnd, msg, wParam, lParam);
		}
	} else {
		code = OS.CallWindowProc (ListProc, hwnd, msg, wParam, lParam);
	}
	switch (msg) {
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL: {
			if (msg == OS.WM_VSCROLL) {
				int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
                if (top != OS.LB_ERR) {
                    viewportRuntime.scrollTo(0, Math.max(0, top));
                }
				if (itemStore != null) {
					storeThumbFreeze (scrollCode, topBefore, top);
					storeRequestPrefetch ();
				}
			}
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
			break;
		}
	}
	if (viewport != null) viewport.afterNative (msg, wParam);
	return code;
}

static int checkStyle (int style) {
	return WidgetStylePolicy.LIST_SELECTION.applyAsInt(style);
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text of the item is set to the empty string. If the receiver is
 * <code>VIRTUAL</code>, the item text is forgotten and requested again
 * with an <code>SWT.SetData</code> event the next time the item is drawn
 * or read.
 *
 * @param index the index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.137
 */
public void clear (int index) {
	checkWidget ();
	if (!(0 <= index && index < getItemCount ())) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (itemStore == null) {
		setItem (index, "");
		return;
	}
	itemStore.clear (index);
	storeRedrawItem (index);
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
}

/**
 * Clears all the items in the receiver. The text of each item is set to the
 * empty string. If the receiver is <code>VIRTUAL</code>, the item texts are
 * forgotten and requested again with <code>SWT.SetData</code> events the next
 * time each item is drawn or read.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.137
 */
public void clearAll () {
	checkWidget ();
	if (itemStore == null) {
		int count = getItemCount ();
		for (int i = 0; i < count; i++) {
			setItem (i, "");
		}
		return;
	}
	itemStore.clearAll ();
	OS.InvalidateRect (handle, null, true);
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
}

@Override
Point computeSizeInPixels (Point hintInPoints, int zoom, boolean changed) {
	checkWidget ();
	if (viewport != null) return viewport.computeSize (hintInPoints, zoom);
	Point hintInPixels = Win32DPIUtils.pointToPixelAsSufficientlyLargeSize(hintInPoints, zoom);
	int width = 0, height = 0;
	if (hintInPoints.x == SWT.DEFAULT && itemStore != null) {
		width = (style & SWT.H_SCROLL) != 0 ? storeExtent (true) - INSET : storeExactWidth ();
	} else if (hintInPoints.x == SWT.DEFAULT) {
		if ((style & SWT.H_SCROLL) != 0) {
			width = (int)OS.SendMessage (handle, OS.LB_GETHORIZONTALEXTENT, 0, 0);
			width -= INSET;
		} else {
			int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
			long newFont, oldFont = 0;
			long hDC = OS.GetDC (handle);
			newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
            if (newFont != 0) {
                oldFont = OS.SelectObject(hDC, newFont);
            }
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
			char [] buffer = new char [64 + 1];
			for (int i=0; i<count; i++) {
				int length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, i, 0);
				if (length != OS.LB_ERR) {
					if (length + 1 > buffer.length) {
						buffer = new char [length + 1];
					}
					int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, i, buffer);
					if (result != OS.LB_ERR) {
						OS.DrawText (hDC, buffer, length, rect, flags);
						width = Math.max (width, rect.right - rect.left);
					}
				}
			}
            if (newFont != 0) {
                OS.SelectObject(hDC, oldFont);
            }
			OS.ReleaseDC (handle, hDC);
		}
	}
	if (hintInPoints.y == SWT.DEFAULT) {
		int count = itemStore != null ? itemStore.getItemCount () : (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		int itemHeight = (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
		height = count * itemHeight;
	}
    if (width == 0) {
        width = DEFAULT_WIDTH;
    }
    if (height == 0) {
        height = DEFAULT_HEIGHT;
    }
    if (hintInPoints.x != SWT.DEFAULT) {
        width = hintInPixels.x;
    }
    if (hintInPoints.y != SWT.DEFAULT) {
        height = hintInPixels.y;
    }
	int border = getBorderWidthInPixels ();
	width += border * 2 + INSET;
	height += border * 2;
	if ((style & SWT.V_SCROLL) != 0) {
		width += getSystemMetrics (OS.SM_CXVSCROLL);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		height += getSystemMetrics (OS.SM_CYHSCROLL);
	}
	return new Point (width, height);
}

@Override
int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (indices.length == 0) {
        return;
    }
	if (viewport != null) {
		viewport.deselect (indices);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
        if (oldIndex == OS.LB_ERR) {
            return;
        }
		for (int index : indices) {
			if (oldIndex == index) {
				OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
				return;
			}
		}
		return;
	}
	for (int index : indices) {
		if (index != -1) {
			OS.SendMessage (handle, OS.LB_SETSEL, 0, index);
		}
	}
}

/**
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget ();
    if (index == -1) {
        return;
    }
	if (viewport != null) {
		viewport.deselect (index);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
        if (oldIndex == OS.LB_ERR) {
            return;
        }
        if (oldIndex == index) {
            OS.SendMessage(handle, OS.LB_SETCURSEL, -1, 0);
        }
		return;
	}
	OS.SendMessage (handle, OS.LB_SETSEL, 0, index);
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget ();
    if (start > end) {
        return;
    }
	if (viewport != null) {
		viewport.deselect (start, end);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
        if (oldIndex == OS.LB_ERR) {
            return;
        }
		if (start <= oldIndex && oldIndex <= end) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
		}
		return;
	}
	/*
	* Ensure that at least one item is contained in
	* the range from start to end.  Note that when
	* start = end, LB_SELITEMRANGEEX deselects the
	* item.
	*/
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (start < 0 && end < 0) {
        return;
    }
    if (start >= count && end >= count) {
        return;
    }
	start = Math.min (count - 1, Math.max (0, start));
	end = Math.min (count - 1, Math.max (0, end));
	OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, end, start);
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll () {
	checkWidget ();
	if (viewport != null) {
		viewport.deselectAll ();
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
	} else {
		OS.SendMessage (handle, OS.LB_SETSEL, 0, -1);
	}
}

/**
 * Returns the zero-relative index of the item which currently
 * has the focus in the receiver, or -1 if no item has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	checkWidget ();
	if (viewport != null) return viewport.getFocusIndex ();
	int result = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	if (result == 0) {
		int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
        if (count == 0) {
            return -1;
        }
	}
	return result;
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
public String getItem (int index) {
	checkWidget ();
	if (viewport != null) return viewport.getItem (index);
	if (itemStore != null) {
		if (!(0 <= index && index < itemStore.getItemCount ())) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		return storeText (index);
	}
	int length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
	if (length != OS.LB_ERR) {
		char [] buffer = new char [length + 1];
		int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
        if (result != OS.LB_ERR) {
            return ((state & HAS_AUTO_DIRECTION) != 0) ? new String(buffer, 1, length - 1) : new String(buffer, 0, length);
        }
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (0 <= index && index < count) {
        error(SWT.ERROR_CANNOT_GET_ITEM);
    }
	error (SWT.ERROR_INVALID_RANGE);
	return "";
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
	if (viewport != null) return viewport.getItemCount ();
	if (itemStore != null) {
		return itemStore.getItemCount ();
	}
	int result = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (result == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_COUNT);
    }
	return result;
}

/* M3Residency seam: the native row window with the viewport; null (every row) without one */
int [] m3ResidentRange () {
	return viewport != null ? viewport.residentRange () : null;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return DPIUtil.pixelToPoint(getItemHeightInPixels(), getAutoscalingZoom());
}

int getItemHeightInPixels () {
	int result = (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
    if (result == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
    }
	return result;
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which
 * are the items in the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget ();
	if (viewport != null) return viewport.getItems ();
	int count = getItemCount ();
	String [] result = new String [count];
	if (itemStore != null) {
		for (int i = 0; i < count; i++) {
			result [i] = storeText (i);
		}
		return result;
	}
    for (int i = 0; i < count; i++) {
        result [i] = getItem(i);
    }
	return result;
}

/**
 * Returns an array of <code>String</code>s that are currently
 * selected in the receiver.  The order of the items is unspecified.
 * An empty array indicates that no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String [] getSelection () {
	checkWidget ();
	int [] indices = getSelectionIndices ();
	String [] result = new String [indices.length];
	for (int i=0; i<indices.length; i++) {
		result [i] = getItem (indices [i]);
	}
	return result;
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	if (viewport != null) return viewport.getSelectionCount ();
	if ((style & SWT.SINGLE) != 0) {
		int result = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
        if (result == OS.LB_ERR) {
            return 0;
        }
		return 1;
	}
	int result = (int)OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
    if (result == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_COUNT);
    }
	return result;
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item or -1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget ();
	if (viewport != null) return viewport.getSelectionIndex ();
	if ((style & SWT.SINGLE) != 0) {
		return (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
    if (count == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_SELECTION);
    }
    if (count == 0) {
        return -1;
    }
	int index = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	int result = (int)OS.SendMessage (handle, OS.LB_GETSEL, index, 0);
    if (result == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_SELECTION);
    }
    if (result != 0) {
        return index;
    }
	int [] buffer = new int[1];
	result = (int)OS.SendMessage (handle, OS.LB_GETSELITEMS, 1, buffer);
    if (result != 1) {
        error(SWT.ERROR_CANNOT_GET_SELECTION);
    }
	return buffer [0];
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver.  The order of the indices is unspecified.
 * The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget ();
	if (viewport != null) return viewport.getSelectionIndices ();
	if ((style & SWT.SINGLE) != 0) {
		int result = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
        if (result == OS.LB_ERR) {
            return new int [0];
        }
		return new int [] {result};
	}
	int length = (int)OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
    if (length == OS.LB_ERR) {
        error(SWT.ERROR_CANNOT_GET_SELECTION);
    }
	int [] indices = new int [length];
	int result = (int)OS.SendMessage (handle, OS.LB_GETSELITEMS, length, indices);
    if (result != length) {
        error(SWT.ERROR_CANNOT_GET_SELECTION);
    }
	return indices;
}

/**
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	if (viewport != null) return viewport.getTopIndex ();
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	viewportRuntime.scrollTo (0, top);
	return top;
}

/**
 * Gets the index of an item.
 * <p>
 * The list is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	return indexOf (string, 0);
}

/**
 * Searches the receiver's list starting at the given,
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to start the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget ();
    if (string == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	if (itemStore != null && string.length () != 0) {
		int count = itemStore.getItemCount ();
		if (!(0 <= start && start < count)) {
			return -1;
		}
		if ((state & HAS_AUTO_DIRECTION) != 0) {
			storeBits |= STORE_ADDED_UCC;
		}
		for (int i = start; i < count; i++) {
			if (string.equals (storeText (i))) {
				return i;
			}
		}
		return -1;
	}

	/*
	* Bug in Windows.  For some reason, LB_FINDSTRINGEXACT
	* will not find empty strings even though it is legal
	* to insert an empty string into a list.  The fix is
	* to search the list, an item at a time.
	*/
	if (string.length () == 0) {
		int count = getItemCount ();
		for (int i=start; i<count; i++) {
            if (string.equals(getItem(i))) {
                return i;
            }
		}
		return -1;
	}
	if (viewport != null) return viewport.indexOf (string, start);

	/* Use LB_FINDSTRINGEXACT to search for the item */
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (!(0 <= start && start < count)) {
        return -1;
    }
	int index = start - 1, last;
	TCHAR buffer = new TCHAR (string, true);
	do {
		index = (int)OS.SendMessage (handle, OS.LB_FINDSTRINGEXACT, last = index, buffer);
        if (index == OS.LB_ERR || index <= last) {
            return -1;
        }
	} while (!string.equals (getItem (index)));
	return index;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the selection state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget ();
	if (viewport != null) return viewport.isSelected (index);
	int result = (int)OS.SendMessage (handle, OS.LB_GETSEL, index, 0);
	return (result != 0) && (result != OS.LB_ERR);
}

@Override
boolean isUseWsBorder () {
	return super.isUseWsBorder () || ((display != null) && display.useWsBorderList);
}

/**
 * Removes the items from the receiver at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int [] indices) {
	removeIndices (indices);
	M3Emptiness.changed (this);
}

void removeIndices (int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (indices.length == 0) {
        return;
    }
	if (viewport != null) {
		viewport.remove (indices);
		return;
	}
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	if (itemStore != null) {
		storeRemove (newIndices, topIndex);
		return;
	}
	RECT rect = null;
	long hDC = 0, oldFont = 0, newFont = 0;
	int newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
        if (newFont != 0) {
            oldFont = OS.SelectObject(hDC, newFont);
        }
	}
	int i = 0, topCount = 0, last = -1;
	while (i < newIndices.length) {
		int index = newIndices [i];
		if (index != last) {
			char [] buffer = null;
			int length = 0;
			if ((style & SWT.H_SCROLL) != 0) {
				length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
                if (length == OS.LB_ERR) {
                    break;
                }
				buffer = new char [length + 1];
				int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
                if (result == OS.LB_ERR) {
                    break;
                }
			}
			int result = (int)OS.SendMessage (handle, OS.LB_DELETESTRING, index, 0);
            if (result == OS.LB_ERR) {
                break;
            }
			if ((style & SWT.H_SCROLL) != 0) {
				int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
				OS.DrawText (hDC, buffer, length, rect, flags);
				newWidth = Math.max (newWidth, rect.right - rect.left);
			}
            if (index < topIndex) {
                topCount++;
            }
			last = index;
		}
		i++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
        if (newFont != 0) {
            OS.SelectObject(hDC, oldFont);
        }
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth, false);
	}
	if (topCount > 0) {
		topIndex -= topCount;
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
    if (i < newIndices.length) {
        error(SWT.ERROR_ITEM_NOT_REMOVED);
    }
}

/**
 * Removes the item from the receiver at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	removeIndex (index);
	M3Emptiness.changed (this);
}

void removeIndex (int index) {
	checkWidget ();
	if (viewport != null) {
		viewport.remove (index);
		return;
	}
	if (itemStore != null) {
		if (!(0 <= index && index < itemStore.getItemCount ())) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
		if (!storeDeleteRange (index, index)) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		if (index < topIndex) {
			topIndex -= 1;
		}
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
		return;
	}
	char [] buffer = null;
	if ((style & SWT.H_SCROLL) != 0) {
		int length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
		if (length == OS.LB_ERR) {
			int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
            if (0 <= index && index < count) {
                error(SWT.ERROR_ITEM_NOT_REMOVED);
            }
			error (SWT.ERROR_INVALID_RANGE);
		}
		buffer = new char [length + 1];
		int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
		if (result == OS.LB_ERR) {
			int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
            if (0 <= index && index < count) {
                error(SWT.ERROR_ITEM_NOT_REMOVED);
            }
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	int result = (int)OS.SendMessage (handle, OS.LB_DELETESTRING, index, 0);
	if (result == OS.LB_ERR) {
		int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
        if (0 <= index && index < count) {
            error(SWT.ERROR_ITEM_NOT_REMOVED);
        }
		error (SWT.ERROR_INVALID_RANGE);
	}
    if ((style & SWT.H_SCROLL) != 0) {
        setScrollWidth(buffer, false);
    }
	if (index < topIndex) {
		topIndex -= 1;
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
}

/**
 * Removes the items from the receiver which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	removeSpan (start, end);
	M3Emptiness.changed (this);
}

void removeSpan (int start, int end) {
	checkWidget ();
    if (start > end) {
        return;
    }
	if (viewport != null) {
		viewport.remove (start, end);
		return;
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == count - 1) {
		removeAll ();
		return;
	}
	int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	if (itemStore != null) {
		if (!storeDeleteRange (start, end)) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		if (end < topIndex) {
			topIndex -= end - start + 1;
		}
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
		return;
	}
	RECT rect = null;
	long hDC = 0, oldFont = 0, newFont = 0;
	int newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
        if (newFont != 0) {
            oldFont = OS.SelectObject(hDC, newFont);
        }
	}
	int index = start;
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	while (index <= end) {
		char [] buffer = null;
		int length = 0;
		if ((style & SWT.H_SCROLL) != 0) {
			length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, start, 0);
            if (length == OS.LB_ERR) {
                break;
            }
			buffer = new char [length + 1];
			int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, start, buffer);
            if (result == OS.LB_ERR) {
                break;
            }
		}
		int result = (int)OS.SendMessage (handle, OS.LB_DELETESTRING, start, 0);
        if (result == OS.LB_ERR) {
            break;
        }
		if ((style & SWT.H_SCROLL) != 0) {
			OS.DrawText (hDC, buffer, length, rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
		index++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
        if (newFont != 0) {
            OS.SelectObject(hDC, oldFont);
        }
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth, false);
	}
	if (end < topIndex) {
		topIndex -= end - start + 1;
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
    if (index <= end) {
        error(SWT.ERROR_ITEM_NOT_REMOVED);
    }
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument,
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget ();
    if (string == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	int index = indexOf (string, 0);
    if (index == -1) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	remove (index);
}

/**
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	removeAllItems ();
	M3Emptiness.changed (this);
}

void removeAllItems () {
	checkWidget ();
	if (viewport != null) {
		viewport.removeAll ();
		return;
	}
	OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	if (itemStore != null) {
		itemStore.removeAll ();
		storeBits &= ~STORE_EMPTY_EXTENT_INSET;
	}
	if ((style & SWT.H_SCROLL) != 0) {
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, 0, 0);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
	checkWidget ();
    if (listener == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (eventTable == null) {
        return;
    }
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If the item at a given index is not selected, it is selected.
 * If the item at a given index was already selected, it remains selected.
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	int length = indices.length;
    if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
        return;
    }
	select (indices, false);
}

void select (int [] indices, boolean scroll) {
	int i = 0;
	while (i < indices.length) {
		int index = indices [i];
		if (index != -1) {
			select (index, false);
		}
		i++;
	}
    if (scroll) {
        showSelection();
    }
}

/**
 * Selects the item at the given zero-relative index in the receiver's
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget ();
	select (index, false);
}

void select (int index, boolean scroll) {
	if (viewport != null) {
		viewport.select (index, scroll);
		return;
	}
    if (index < 0) {
        return;
    }
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (index >= count) {
        return;
    }
	if (scroll) {
		if ((style & SWT.SINGLE) != 0) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, index, 0);
		} else {
			OS.SendMessage (handle, OS.LB_SETSEL, 1, index);
		}
		return;
	}
	int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	RECT itemRect = new RECT (), selectedRect = null;
	OS.SendMessage (handle, OS.LB_GETITEMRECT, index, itemRect);
	boolean redraw = getDrawing () && OS.IsWindowVisible (handle);
	if (redraw) {
		OS.UpdateWindow (handle);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	int focusIndex = -1;
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (oldIndex != -1) {
			selectedRect = new RECT ();
			OS.SendMessage (handle, OS.LB_GETITEMRECT, oldIndex, selectedRect);
		}
		OS.SendMessage (handle, OS.LB_SETCURSEL, index, 0);
	} else {
		focusIndex = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
		OS.SendMessage (handle, OS.LB_SETSEL, 1, index);
	}
	if ((style & SWT.MULTI) != 0) {
		if (focusIndex != -1) {
			OS.SendMessage (handle, OS.LB_SETCARETINDEX, focusIndex, 0);
		}
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.ValidateRect (handle, null);
		OS.InvalidateRect (handle, itemRect, true);
		if (selectedRect != null) {
			OS.InvalidateRect (handle, selectedRect, true);
		}
	}
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If an item in the given range is not selected, it is selected.
 * If an item in the given range was already selected, it remains selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
    if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) {
        return;
    }
	if (viewport != null) {
		viewport.selectRange (start, end);
		return;
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (count == 0 || start >= count) {
        return;
    }
	start = Math.max (0, start);
	end = Math.min (end, count - 1);
	if ((style & SWT.SINGLE) != 0) {
		select (start, false);
	} else {
		select (start, end, false);
	}
}

void select (int start, int end, boolean scroll) {
	if (viewport != null) {
		viewport.select (start, end, scroll);
		return;
	}
	/*
	* Note that when start = end, LB_SELITEMRANGEEX
	* deselects the item.
	*/
	if (start == end) {
		select (start, scroll);
		return;
	}
	OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, start, end);
    if (scroll) {
        showSelection();
    }
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if (viewport != null) {
		viewport.selectAll ();
		return;
	}
    if ((style & SWT.SINGLE) != 0) {
        return;
    }
	OS.SendMessage (handle, OS.LB_SETSEL, 1, -1);
}

void setFocusIndex (int index) {
	if (viewport != null) {
		viewport.setFocusIndex (index);
		return;
	}
//	checkWidget ();
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (!(0 <= index && index < count)) {
        return;
    }
	OS.SendMessage (handle, OS.LB_SETCARETINDEX, index, 0);
}

@Override
public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
    if ((style & SWT.H_SCROLL) != 0) {
        setScrollWidth();
    }
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	/* remove and add: the transient count is not an emptiness change */
	M3Emptiness.hold (this);
	try {
		setItemText (index, string);
	} finally {
		M3Emptiness.release (this);
	}
}

void setItemText (int index, String string) {
	checkWidget ();
    if (string == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	if (itemStore != null && (storeBits & STORE_IN_SET_DATA) != 0 && index == setDataIndex) {
		/* SetData answer: the row is being materialized, nothing native changes */
		itemStore.setItem (index, string);
		return;
	}
	int topIndex = getTopIndex ();
	boolean isSelected = isSelected (index);
	remove (index);
	add (string, index);
    if (isSelected) {
        select(index, false);
    }
	setTopIndex (topIndex);
}

/**
 * Sets the number of items contained in the receiver. Items are removed from
 * or added at the end. Added items of a <code>VIRTUAL</code> receiver are
 * requested with an <code>SWT.SetData</code> event when they are first drawn
 * or read; added items of any other receiver are empty strings.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.137
 */
public void setItemCount (int count) {
	/* the native path adds and removes through the public methods: one announcement, at the end */
	M3Emptiness.hold (this);
	try {
		setCount (count);
	} finally {
		M3Emptiness.release (this);
	}
}

void setCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	int itemCount = getItemCount ();
	if (count == itemCount) {
		return;
	}
	if (itemStore != null) {
		storeSetCount (count, itemCount);
		return;
	}
	if (viewport != null) {
		viewport.setItemCount (count);
		return;
	}
	if (count < itemCount) {
		remove (count, itemCount - 1);
		return;
	}
	for (int i = itemCount; i < count; i++) {
		add ("");
	}
}

/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String... items) {
	setAllItems (items);
	M3Emptiness.changed (this);
}

void setAllItems (String... items) {
	checkWidget ();
    if (items == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	for (String item : items) {
        if (item == null) {
            error(SWT.ERROR_INVALID_ARGUMENT);
        }
	}
	if (viewport != null) {
		viewport.setItems (items);
		return;
	}
	if (itemStore != null) {
		storeSetItems (items);
		return;
	}
	long oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, ListProc);
	boolean redraw = getDrawing () && OS.IsWindowVisible (handle);
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	RECT rect = null;
	long hDC = 0, oldFont = 0, newFont = 0;
	int newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
        if (newFont != 0) {
            oldFont = OS.SelectObject(hDC, newFont);
        }
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, 0, 0);
	}
	int length = items.length;
	OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	OS.SendMessage (handle, OS.LB_INITSTORAGE, length, length * 32);
	int index = 0;
	while (index < length) {
		String string = items [index];
		TCHAR buffer = new TCHAR (string, true);
		int result = (int)OS.SendMessage (handle, OS.LB_ADDSTRING, 0, buffer);
        if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
            break;
        }
		if ((style & SWT.H_SCROLL) != 0) {
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
			OS.DrawText (hDC, buffer, -1, rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
		index++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
        if (newFont != 0) {
            OS.SelectObject(hDC, oldFont);
        }
		OS.ReleaseDC (handle, hDC);
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth + INSET, 0);
	}
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		/*
		* This code is intentionally commented.  The window proc
		* for the list box implements WM_SETREDRAW to invalidate
		* and erase the widget.  This is undocumented behavior.
		* The commented code below shows what is actually happening
		* and reminds us that we are relying on this undocumented
		* behavior.
		*/
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//		OS.RedrawWindow (handle, null, 0, flags);
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
    if (index < items.length) {
        error(SWT.ERROR_ITEM_NOT_ADDED);
    }
}

/**
 * Calculates the scroll width depending on the item with the highest width
 */
void setScrollWidth () {
	if (viewport != null) {
		viewport.recomputeExtent ();
		return;
	}
	if (itemStore != null) {
		storeBits |= STORE_EMPTY_EXTENT_INSET;
		storeExtent (false);
		return;
	}
	int newWidth = 0;
	RECT rect = new RECT ();
	long newFont, oldFont = 0;
	long hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
    if (newFont != 0) {
        oldFont = OS.SelectObject(hDC, newFont);
    }
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	for (int i=0; i<count; i++) {
		int length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, i, 0);
		if (length != OS.LB_ERR) {
			char [] buffer = new char [length + 1];
			int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, i, buffer);
			if (result != OS.LB_ERR) {
				OS.DrawText (hDC, buffer, length, rect, flags);
				newWidth = Math.max (newWidth, rect.right - rect.left);
			}
		}
	}
    if (newFont != 0) {
        OS.SelectObject(hDC, oldFont);
    }
	OS.ReleaseDC (handle, hDC);
	OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth + INSET, 0);
}

void setScrollWidth (char[] buffer, boolean grow) {
	RECT rect = new RECT ();
	long newFont, oldFont = 0;
	long hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
    if (newFont != 0) {
        oldFont = OS.SelectObject(hDC, newFont);
    }
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	OS.DrawText (hDC, buffer, -1, rect, flags);
    if (newFont != 0) {
        OS.SelectObject(hDC, oldFont);
    }
	OS.ReleaseDC (handle, hDC);
	setScrollWidth (rect.right - rect.left, grow);
}

void setScrollWidth (int newWidth, boolean grow) {
	newWidth += INSET;
	int width = (int)OS.SendMessage (handle, OS.LB_GETHORIZONTALEXTENT, 0, 0);
	if (grow) {
        if (newWidth <= width) {
            return;
        }
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth, 0);
	} else {
        if (newWidth < width) {
            return;
        }
		setScrollWidth ();
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection(int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	deselectAll ();
	int length = indices.length;
    if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
        return;
    }
	select (indices, true);
	if ((style & SWT.MULTI) != 0) {
		int focusIndex = indices [0];
        if (focusIndex >= 0) {
            setFocusIndex(focusIndex);
        }
	}
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 * @see List#setSelection(int[])
 */
public void setSelection (String [] items) {
	checkWidget ();
    if (items == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	deselectAll ();
	int length = items.length;
    if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
        return;
    }
	int focusIndex = -1;
	for (int i=length-1; i>=0; --i) {
		String string = items [i];
		int index = 0;
		if (string != null) {
			int localFocus = -1;
			while ((index = indexOf (string, index)) != -1) {
                if (localFocus == -1) {
                    localFocus = index;
                }
				select (index, false);
				if ((style & SWT.SINGLE) != 0 && isSelected (index)) {
					showSelection ();
					return;
				}
				index++;
			}
            if (localFocus != -1) {
                focusIndex = localFocus;
            }
		}
	}
	if ((style & SWT.MULTI) != 0) {
        if (focusIndex >= 0) {
            setFocusIndex(focusIndex);
        }
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	deselectAll ();
	select (index, true);
	if ((style & SWT.MULTI) != 0) {
        if (index >= 0) {
            setFocusIndex(index);
        }
	}
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	deselectAll ();
    if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) {
        return;
    }
	if (viewport != null) {
		viewport.setSelectionRange (start, end);
		return;
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (count == 0 || start >= count) {
        return;
    }
	start = Math.max (0, start);
	end = Math.min (end, count - 1);
	if ((style & SWT.SINGLE) != 0) {
		select (start, true);
	} else {
		select (start, end, true);
		setFocusIndex (start);
	}
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget ();
	if (viewport != null) {
		viewport.setTopIndex (index);
		return;
	}
	if (itemStore != null) {
		storeRequestPrefetch ();
	}
	int result = (int)OS.SendMessage (handle, OS.LB_SETTOPINDEX, index, 0);
	if (result == OS.LB_ERR) {
		int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		index = Math.min (count - 1, Math.max (0, index));
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, index, 0);
	}
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
    if (top != OS.LB_ERR) {
        viewportRuntime.scrollTo(0, Math.max(0, top));
    }
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	if (viewport != null) {
		viewport.showSelection ();
		return;
	}
	int index;
	if ((style & SWT.SINGLE) != 0) {
		index = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	} else {
		int [] indices = new int [1];
		int result = (int)OS.SendMessage (handle, OS.LB_GETSELITEMS, 1, indices);
		index = indices [0];
        if (result != 1) {
            index = -1;
        }
	}
    if (index == -1) {
        return;
    }
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (count == 0) {
        return;
    }
	int height = (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
	forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	int visibleCount = Math.max (rect.bottom / height, 1);
	int bottomIndex = Math.min (topIndex + visibleCount, count) - 1;
    if (topIndex <= index && index <= bottomIndex) {
        return;
    }
	int newTop = Math.min (Math.max (index - (visibleCount / 2), 0), count - 1);
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, newTop, 0);
	viewportRuntime.scrollTo (0, newTop);
}

/*
 * SWT.VIRTUAL owner-data viewport. The native list box (LBS_NODATA) owns the
 * count, the selection, the caret and the scroll position; itemStore owns the
 * text and the width lane. Nothing below touches more rows than the caller's
 * request names, except where the request itself is O(n) (setItems, getItems,
 * indexOf, an exact computeSize).
 */

int storeChars (String string, boolean bidiMark) {
	int length = string.length ();
	int offset = bidiMark && (state & HAS_AUTO_DIRECTION) != 0 ? 1 : 0;
	int needed = length + offset + 1;
	if (textBuffer == null || textBuffer.length < needed) {
		int grown = textBuffer == null ? 64 : textBuffer.length + Math.min (textBuffer.length, 1 << 16);
		textBuffer = new char [Math.max (needed, grown)];
	}
	if (offset == 1) {
		int direction = BidiUtil.resolveTextDirection (string);
		if (direction == SWT.NONE) {
			direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
		}
		textBuffer [0] = direction == SWT.RIGHT_TO_LEFT ? RLE : LRE;
	}
	string.getChars (0, length, textBuffer, offset);
	textBuffer [offset + length] = 0;
	return offset + length;
}

boolean storeDeleteRange (int start, int end) {
	return storeDeleteRange (start, end, false);
}

/* Deletes start..end natively and from the store; see storeNativeCount for the native order. */
boolean storeDeleteRange (int start, int end, boolean descending) {
	int removed = end - start + 1;
	if (!storeNativeCount (start, -removed, descending)) {
		return false;
	}
	itemStore.remove (start, end);
	if (itemStore.getItemCount () == 0) {
		storeBits |= STORE_EMPTY_EXTENT_INSET;
	}
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
	return true;
}

int storeExactWidth () {
	if (itemStore.getPendingWidthCount () == 0) {
		return itemStore.getWidestWidth ();
	}
	long hDC = OS.GetDC (handle);
	long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0), oldFont = 0;
	if (newFont != 0) {
		oldFont = OS.SelectObject (hDC, newFont);
	}
	/* a long measurement shows busy (the wait cursor) for its duration */
	long oldCursor = itemStore.getPendingWidthCount () > STORE_BUSY_ROWS ? OS.SetCursor (Cursor.win32_getHandle (display.getSystemCursor (SWT.CURSOR_WAIT), DPIUtil.getZoomForAutoscaleProperty (getShellZoom ()))) : 0;
	measureDC = hDC;
	try {
		return itemStore.getWidestWidth ();
	} finally {
		measureDC = 0;
		if (oldCursor != 0) {
			OS.SetCursor (oldCursor);
		}
		if (newFont != 0) {
			OS.SelectObject (hDC, oldFont);
		}
		OS.ReleaseDC (handle, hDC);
	}
}

/*
 * Horizontal extent of a plain list: max item width + INSET, or 0/INSET for an
 * empty list depending on how it became empty. In SWT.VIRTUAL the native extent
 * covers the measured rows (painted rows and exact queries), as Table does.
 */
/*
 * The grow method of the item and width lanes: the shared heap-aware policy
 * (PackedHeapGrowth, 1.5x geometric growth, the exact length under heap
 * pressure; 8 bytes per slot).
 */
static int growCapacity (int length, int needed) {
	return PackedHeapGrowth.capacity (length, needed, 8);
}

int storeExtent (boolean exact) {
	int count = itemStore.getItemCount ();
	exact |= count <= STORE_EXACT_EXTENT_LIMIT;
	int extent;
	if (count == 0) {
		extent = (storeBits & STORE_EMPTY_EXTENT_INSET) != 0 ? INSET : 0;
	} else {
		extent = (exact ? storeExactWidth () : itemStore.getMeasuredWidestWidth ()) + INSET;
	}
	if ((style & SWT.H_SCROLL) != 0 && (int)OS.SendMessage (handle, OS.LB_GETHORIZONTALEXTENT, 0, 0) != extent) {
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, extent, 0);
	}
	return extent;
}

void storeInsert (String string, int index) {
	if (!storeNativeCount (index, 1, false)) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		storeBits |= STORE_ADDED_UCC;
	}
	itemStore.add (string, index);
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
}

/*
 * Changes the native count by delta rows at start (delta < 0 removes
 * [start, start - delta)) so that selection, caret, anchor and top index end
 * where the plain list box's row messages leave them (calibrated on this host,
 * NATIVE_LIMITS N13-N15: the caret keeps its row number unless its own row is
 * deleted, the anchor is dropped when a row at or above it is deleted, the
 * selection follows its items).
 * - SINGLE removes with row messages (no per-row native storage, exact).
 * - SINGLE inserts with row messages, unless the count came from LB_SETCOUNT:
 *   then the list box loses its count (N7) and one LB_SETCOUNT plus a
 *   selection restore is used; the caret then follows the selection (D1).
 * - MULTI uses row messages up to removeRebuildThreshold rows, above that one
 *   LB_SETCOUNT and a restore (each row message moves the per-row selection
 *   state of every row behind it, N5).
 */
boolean storeNativeCount (int start, int delta, boolean descending) {
	int magnitude = delta < 0 ? -delta : delta;
	boolean frozen = magnitude > 1 && storeFreeze ();
	try {
		return storeNativeCountFrozen (start, delta, descending, magnitude);
	} finally {
		storeThaw (frozen);
	}
}

boolean storeNativeCountFrozen (int start, int delta, boolean descending, int magnitude) {
	int count = itemStore.getItemCount ();
	if (count == 0 && delta > 0) {
		return storeSetEmptyCount (delta);
	}
	if ((style & SWT.SINGLE) != 0 && delta > 0) {
		return storeSingleInsert (start, delta, count);
	}
	if ((style & SWT.SINGLE) != 0 || magnitude <= removeRebuildThreshold
		|| (removeRebuildThreshold != 0 && magnitude <= STORE_ROW_MESSAGE_WORK / count)) {
		return storeRowMessages (start, delta, descending, count);
	}
	return storeRebuild (count + delta, start, delta, descending);
}

/*
 * Sets the count of an empty list box. A single-selection LBS_NODATA list box
 * whose count came from LB_SETCOUNT alone refuses later LB_INSERTSTRING (N7);
 * one LB_INSERTSTRING first keeps row inserts working after LB_SETCOUNT.
 */
boolean storeSetEmptyCount (int count) {
	if ((style & SWT.SINGLE) != 0) {
		OS.SendMessage (handle, OS.LB_INSERTSTRING, 0, 0);
	}
	int result = (int)OS.SendMessage (handle, OS.LB_SETCOUNT, count, 0);
	return result != OS.LB_ERR && result != OS.LB_ERRSPACE;
}

boolean storeRowMessages (int start, int delta, boolean descending, int count) {
	int magnitude = delta < 0 ? -delta : delta;
	for (int i = 0; i < magnitude; i++) {
		int result;
		if (delta > 0) {
			result = (int)OS.SendMessage (handle, OS.LB_INSERTSTRING, start + i, 0);
		} else {
			result = (int)OS.SendMessage (handle, OS.LB_DELETESTRING, descending ? start + magnitude - 1 - i : start, 0);
		}
		if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
			OS.SendMessage (handle, OS.LB_SETCOUNT, count, 0);
			return false;
		}
	}
	return true;
}

boolean storeSingleInsert (int start, int delta, int count) {
	int selection = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	int caret = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	int anchor = (int)OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	if (delta <= removeRebuildThreshold) {
		boolean inserted = true;
		for (int i = 0; i < delta && inserted; i++) {
			int result = (int)OS.SendMessage (handle, OS.LB_INSERTSTRING, start + i, 0);
			inserted = result != OS.LB_ERR && result != OS.LB_ERRSPACE;
		}
		if (inserted && (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0) == count + delta) {
			return true;
		}
	}
	int result = (int)OS.SendMessage (handle, OS.LB_SETCOUNT, count + delta, 0);
	if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
		return false;
	}
	if (selection >= 0) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, selection >= start ? selection + delta : selection, 0);
	} else if (caret > 0) {
		/* a single-selection list box has no caret message: select the caret row, then clear */
		OS.SendMessage (handle, OS.LB_SETCURSEL, caret, 0);
		OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
	}
	if (anchor >= 0) {
		OS.SendMessage (handle, STORE_LB_SETANCHORINDEX, anchor, 0);
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, top, 0);
	return true;
}

int measureStoreText (String string) {
	int nul = string.indexOf ('\0');
	if (nul != -1) {
		string = string.substring (0, nul);
	}
	long hDC = measureDC, oldFont = 0, newFont = 0;
	if (hDC == 0) {
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) {
			oldFont = OS.SelectObject (hDC, newFont);
		}
	}
	int length = storeChars (string, false);
	if (measureRect == null) {
		measureRect = new RECT ();
	}
	RECT rect = measureRect;
	OS.SetRect (rect, 0, 0, 0, 0);
	int width = 0;
	if (length != 0) {
		OS.DrawText (hDC, textBuffer, length, rect, OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX);
		width = rect.right - rect.left;
	}
	if (measureDC == 0) {
		if (newFont != 0) {
			OS.SelectObject (hDC, oldFont);
		}
		OS.ReleaseDC (handle, hDC);
	}
	return width;
}

/*
 * MULTI: replaces the native count with one LB_SETCOUNT, which clears the
 * selection, caret, anchor and top index, and restores them as the row
 * messages would have left them.
 */
boolean storeRebuild (int newCount, int start, int delta, boolean descending) {
	int count = itemStore.getItemCount ();
	int caret = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	int anchor = (int)OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	int [] selected = null;
	int length = (int)OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
	if (length > 0) {
		selected = new int [length];
		OS.SendMessage (handle, OS.LB_GETSELITEMS, length, selected);
	}
	int nativeAnchor = storeNativeAnchor (anchor, start, delta);
	if (nativeAnchor < 0 && anchor >= 0 && count > 0) {
		/* LB_SETCOUNT keeps the anchor and LB_SETANCHORINDEX refuses -1; deleting row 0 drops it (N14) */
		OS.SendMessage (handle, OS.LB_DELETESTRING, 0, 0);
	}
	int result = (int)OS.SendMessage (handle, OS.LB_SETCOUNT, newCount, 0);
	if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
		return false;
	}
	if (newCount == 0) {
		return true;
	}
	if (selected != null) {
		storeSelectShifted (selected, start, delta);
	}
	anchor = nativeAnchor;
	if (anchor >= 0) {
		OS.SendMessage (handle, STORE_LB_SETANCHORINDEX, anchor, 0);
	}
	caret = storeNativeCaret (caret, count, start, delta, descending);
	if (caret >= 0 && (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0) != caret) {
		OS.SendMessage (handle, OS.LB_SETCARETINDEX, caret, 1);
	}
	/* a row delete resets the plain list box's top row to 0 (N17); inserts keep it */
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, delta < 0 ? 0 : Math.min (top, newCount - 1), 0);
	return true;
}

/* New index of a row after a count change, -1 when the row was removed. */
static int storeShift (int index, int start, int delta) {
	if (index < start) {
		return index;
	}
	if (delta >= 0) {
		return index + delta;
	}
	return index < start - delta ? -1 : index + delta;
}

/* Caret of the plain list box after the row messages (N13): inserts keep it; a deleted caret row moves it up one; it stays on a row. */
static int storeNativeCaret (int caret, int count, int start, int delta, boolean descending) {
	if (caret < 0 || delta >= 0) {
		return caret;
	}
	int removed = -delta;
	for (int i = 0; i < removed; i++) {
		int row = descending ? start + removed - 1 - i : start;
		if (caret == row && caret > 0) {
			caret--;
		}
		count--;
		if (caret > count - 1) {
			caret = count - 1;
		}
	}
	return caret;
}

/* Anchor of the plain list box after the row messages (N14): inserts keep it; deleting a row at or above it drops it. */
static int storeNativeAnchor (int anchor, int start, int delta) {
	return delta < 0 && anchor >= start ? -1 : anchor;
}

void storeSelectShifted (int [] selected, int start, int delta) {
	int runStart = -1, runEnd = -2;
	for (int i = 0; i <= selected.length; i++) {
		int index = -1;
		if (i < selected.length) {
			index = storeShift (selected [i], start, delta);
			if (index == -1) {
				continue;
			}
			if (index == runEnd + 1) {
				runEnd = index;
				continue;
			}
		}
		if (runStart >= 0) {
			storeSelectRun (runStart, runEnd);
		}
		runStart = runEnd = index;
	}
}

/*
 * Selects first..last without moving the caret or the anchor (N15):
 * LB_SELITEMRANGEEX selects first < last and deselects first == last, so a
 * single row is selected together with a neighbour that is then deselected
 * (runs are maximal, so the neighbour is not selected).
 */
void storeSelectRun (int first, int last) {
	if (first != last) {
		OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, first, last);
		return;
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (first + 1 < count) {
		OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, first, first + 1);
		OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, first + 1, first + 1);
	} else if (first > 0) {
		OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, first - 1, first);
		OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, first - 1, first - 1);
	} else {
		OS.SendMessage (handle, OS.LB_SETSEL, 1, first);
	}
}

boolean storeRedrawItem (int index) {
	RECT rect = new RECT ();
	if (OS.SendMessage (handle, OS.LB_GETITEMRECT, index, rect) == OS.LB_ERR) {
		return false;
	}
	OS.InvalidateRect (handle, rect, true);
	return true;
}

void storeRemove (int [] descending, int topIndex) {
	int topCount = 0, i = 0;
	while (i < descending.length) {
		int high = descending [i], low = high;
		i++;
		while (i < descending.length && (descending [i] == low || descending [i] == low - 1)) {
			low = descending [i];
			i++;
		}
		if (!storeDeleteRange (low, high, true)) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		if (low < topIndex) {
			topCount += Math.min (high, topIndex - 1) - low + 1;
		}
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex - topCount, 0);
}

/*
 * Moves to a row found by the keyboard search the way the plain list box does
 * (measured, NATIVE_LIMITS N8): single and extended selection select the row,
 * multiple selection moves only the caret; the anchor follows. The plain list
 * box notifies only while it has the focus.
 */
/*
 * Fast thumb drag: when one SB_THUMBTRACK moves the top row by more than a
 * page, the content stays frozen until SB_THUMBPOSITION or SB_ENDSCROLL; the
 * scroll bar keeps tracking.
 */
void storeThumbFreeze (int scrollCode, int topBefore, int topAfter) {
	if (scrollCode == OS.SB_THUMBTRACK) {
		int itemHeight = Math.max (1, (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0));
		if (scrollRect == null) {
			scrollRect = new RECT ();
		}
		RECT rect = scrollRect;
		OS.GetClientRect (handle, rect);
		int page = Math.max (1, rect.bottom / itemHeight);
		int moved = topAfter > topBefore ? topAfter - topBefore : topBefore - topAfter;
		if (moved > page) {
			storeBits |= STORE_THUMB_FROZEN;
		}
	} else if ((storeBits & STORE_THUMB_FROZEN) != 0 && (scrollCode == OS.SB_THUMBPOSITION || scrollCode == OS.SB_ENDSCROLL)) {
		storeBits &= ~STORE_THUMB_FROZEN;
		OS.InvalidateRect (handle, null, true);
	}
}

/* Debounced request for the overscan and prefetch rows (see STORE_SETDATA_BATCH). */
void storeRequestPrefetch () {
	if (itemStore == null || !hooks (SWT.SetData) || display == null || display.isDisposed ()) {
		return;
	}
	if (storePrefetch == null) {
		storePrefetch = this::storePrefetchStep;
	}
	if ((storeBits & STORE_PREFETCH_PENDING) != 0) {
		display.timerExec (-1, storePrefetch);
	}
	storeBits |= STORE_PREFETCH_PENDING;
	display.timerExec (STORE_PREFETCH_DELAY, storePrefetch);
}

void storePrefetchStep () {
	storeBits &= ~STORE_PREFETCH_PENDING;
	if (isDisposed () || itemStore == null || !hooks (SWT.SetData)
			|| !OS.IsWindowVisible (handle) || (storeBits & STORE_IN_SET_DATA) != 0) return;
	if ((storeBits & (STORE_CONTENT_FROZEN | STORE_THUMB_FROZEN)) != 0) {
		storeRequestPrefetch ();
		return;
	}
	int count = itemStore.getItemCount ();
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	if (count == 0 || top < 0 || top >= count) return;
	if (top != storeLastTop) {
		storeBits = top < storeLastTop ? storeBits | STORE_SCROLLED_UP : storeBits & ~STORE_SCROLLED_UP;
		storeLastTop = top;
	}
	int itemHeight = Math.max (1, (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0));
	if (scrollRect == null) scrollRect = new RECT ();
	RECT rect = scrollRect;
	OS.GetClientRect (handle, rect);
	long bodyHeight = Math.max (0L, (long)rect.bottom - rect.top);
	int visible = (int)Math.min ((long)count - top, (bodyHeight + itemHeight - 1L) / itemHeight);
	if (visible == 0) return;
	ViewportPrefetch.Bands plan = ViewportPrefetch.forRuntime (count, top, visible,
			VirtualViewportPlanner.DEFAULT_OVERSCAN_ROWS, STORE_PREFETCH_PAGES,
			(storeBits & STORE_SCROLLED_UP) != 0);
	if (plan.empty ()) return;
	long epoch = itemStore.prefetchEpoch ();
	int budget = STORE_SETDATA_BATCH;
	budget = storeMaterialize (plan.firstFrom (), plan.firstTo (), budget, top, count, epoch);
	if (storePrefetchObsolete (top, count, epoch)) return;
	budget = storeMaterialize (plan.secondFrom (), plan.secondTo (), budget, top, count, epoch);
	if (storePrefetchObsolete (top, count, epoch)) return;
	budget = storeMaterialize (plan.pageFrom (), plan.pageTo (), budget, top, count, epoch);
	if (budget == 0 && !storePrefetchObsolete (top, count, epoch)) storeRequestPrefetch ();
}

/*
 * SetData may replace, clear or shrink the owning model. The parent epoch is
 * O(1); no row object/flag array or new event dispatcher is introduced.
 * Ordinary SetData.setItem is not a structural mutation and does not cancel
 * prefetch. A structural mutation stops optional work without retry spinning.
 */
boolean storePrefetchObsolete (int expectedTop, int expectedCount, long expectedEpoch) {
	if (isDisposed () || itemStore == null || !hooks (SWT.SetData)
			|| !OS.IsWindowVisible (handle)) return true;
	if ((storeBits & (STORE_PREFETCH_PENDING | STORE_CONTENT_FROZEN | STORE_THUMB_FROZEN)) != 0)
		return true;
	if (itemStore.prefetchEpoch () != expectedEpoch || itemStore.getItemCount () != expectedCount)
		return true;
	if ((int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0) != expectedTop) {
		storeRequestPrefetch ();
		return true;
	}
	return false;
}

/* Exactly one native List and its existing synchronous SWT.SetData event path. */
int storeMaterialize (int from, int to, int budget, int top, int count, long epoch) {
	for (int index = from; index < to && budget > 0 && !isDisposed ()
			&& (storeBits & STORE_PREFETCH_PENDING) == 0; index++) {
		if (itemStore == null || itemStore.prefetchEpoch () != epoch
				|| itemStore.getItemCount () != count) return budget;
		if (index < count && !itemStore.isMaterialized (index)) {
			if (storePrefetchObsolete (top, count, epoch)) return budget;
			storeSetData (index);
			budget--;
			if (storePrefetchObsolete (top, count, epoch)) return budget;
		}
	}
	return budget;
}

void storeSelectFromKeyboard (int index) {
	boolean changed = true;
	if ((style & SWT.SINGLE) != 0) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, index, 0);
	} else if ((OS.GetWindowLong (handle, OS.GWL_STYLE) & OS.LBS_EXTENDEDSEL) != 0) {
		OS.SendMessage (handle, OS.LB_SETSEL, 0, -1);
		OS.SendMessage (handle, OS.LB_SETSEL, 1, index);
		OS.SendMessage (handle, OS.LB_SETCARETINDEX, index, 0);
	} else {
		OS.SendMessage (handle, OS.LB_SETCARETINDEX, index, 0);
		changed = false;
	}
	if (changed) {
		OS.SendMessage (handle, STORE_LB_SETANCHORINDEX, index, 0);
	}
	if (changed && OS.GetFocus () == handle) {
		sendSelectionEvent (SWT.Selection);
	}
}

void storeSetCount (int count, int itemCount) {
	if (count < itemCount) {
		if (!storeDeleteRange (count, itemCount - 1)) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		return;
	}
	if (!storeNativeCount (itemCount, count - itemCount, false)) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	itemStore.setItemCount (count);
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
}

/* Materializes a row: SWT.SetData is sent once per row, never re-entrantly. */
String storeSetData (int index) {
	if ((storeBits & STORE_IN_SET_DATA) != 0) {
		String string = itemStore.getItem (index);
		return string == null ? "" : string;
	}
	Event event = new Event ();
	event.index = index;
	storeBits |= STORE_IN_SET_DATA;
	setDataIndex = index;
	setDataCount++;
	try {
		sendEvent (SWT.SetData, event);
	} finally {
		storeBits &= ~STORE_IN_SET_DATA;
		setDataIndex = -1;
	}
	if (isDisposed () || index >= itemStore.getItemCount ()) {
		return "";
	}
	String string = itemStore.getItem (index);
	if (string == null) {
		string = "";
		itemStore.setItem (index, string);
	}
	return string;
}

void storeSetItems (String [] items) {
	boolean frozen = storeFreeze ();
	OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	itemStore.setItems (items);
	int result = items.length == 0 ? 0 : storeSetEmptyCount (items.length) ? 0 : OS.LB_ERR;
	storeBits |= STORE_EMPTY_EXTENT_INSET;
	if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
		itemStore.removeAll ();
		OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		storeExtent (false);
	}
	storeThaw (frozen);
	if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
}

/* Freezes the content area for a bulk change; returns whether this call froze it. */
boolean storeFreeze () {
	if ((storeBits & STORE_CONTENT_FROZEN) != 0) {
		return false;
	}
	storeBits |= STORE_CONTENT_FROZEN;
	return true;
}

/* Ends a freeze started by this caller and repaints the content once. */
void storeThaw (boolean frozen) {
	if (!frozen) {
		return;
	}
	storeBits &= ~STORE_CONTENT_FROZEN;
	if (getDrawing () && OS.IsWindowVisible (handle)) {
		OS.InvalidateRect (handle, null, true);
	}
}

int storeSingleSelection () {
	return (style & SWT.SINGLE) != 0 ? (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0) : -1;
}

/* Item text with the plain list's NUL truncation; unmaterialized rows are requested with SWT.SetData. */
String storeText (int index) {
	String string = itemStore.getItem (index);
	if (string == null) {
		string = storeSetData (index);
	}
	int nul = string.indexOf ('\0');
	return nul == -1 ? string : string.substring (0, nul);
}

/*
 * Text direction of the plain list: switching to AUTO, or away from AUTO once
 * a row was inserted under it, deletes and re-inserts every non-empty row from
 * the last one up, which clears their selection and moves caret, anchor and
 * top index; the row messages do the same here (exact up to
 * STORE_READD_LIMIT rows; above it the selection of non-empty rows is cleared
 * and the caret and top index go to the first row).
 */
boolean storeTextDirection (int textDirection) {
	if (textDirection == AUTO_TEXT_DIRECTION) {
		if ((state & HAS_AUTO_DIRECTION) != 0) {
			return false;
		}
		state |= HAS_AUTO_DIRECTION;
		storeReAddRows (true);
		return true;
	}
	state &= ~HAS_AUTO_DIRECTION;
	if ((storeBits & STORE_ADDED_UCC) == 0) {
		return super.updateTextDirection (textDirection);
	}
	storeReAddRows (false);
	return super.updateTextDirection (textDirection);
}

void storeReAddRows (boolean auto) {
	boolean frozen = storeFreeze ();
	try {
		storeReAddRowsFrozen (auto);
	} finally {
		storeThaw (frozen);
	}
}

void storeReAddRowsFrozen (boolean auto) {
	int count = itemStore.getItemCount ();
	int selection = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	storeBits &= ~STORE_ADDED_UCC;
	if (count <= STORE_READD_LIMIT) {
		for (int i = count - 1; i >= 0; i--) {
			String string = itemStore.getItem (i);
			if (string == null || string.isEmpty ()) {
				continue;
			}
			OS.SendMessage (handle, OS.LB_DELETESTRING, i, 0);
			OS.SendMessage (handle, OS.LB_INSERTSTRING, i, 0);
			if (auto) {
				storeBits |= STORE_ADDED_UCC;
			}
		}
	} else {
		for (int i = 0; i < count; i++) {
			String string = itemStore.getItem (i);
			if (string != null && !string.isEmpty ()) {
				if ((style & SWT.MULTI) != 0) {
					OS.SendMessage (handle, OS.LB_SETSEL, 0, i);
				}
				if (auto) {
					storeBits |= STORE_ADDED_UCC;
				}
			}
		}
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, 0, 0);
	}
	if (selection != OS.LB_ERR) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, selection, 0);
	}
	OS.InvalidateRect (handle, null, true);
}

/*
 * Keyboard search of the plain list box (measured on this host): a typed
 * character moves to the next item, after the caret and wrapping, whose first
 * character matches ignoring case. Only materialized rows are searched; like
 * Table answering LVN_ODFINDITEM, a key never triggers SWT.SetData.
 */
boolean storeTypeAhead (int key) {
	if (key < 0x20) {
		return false;
	}
	int count = itemStore.getItemCount ();
	if (count == 0) {
		return false;
	}
	int caret = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	if (caret < -1 || caret >= count) {
		caret = -1;
	}
	char lower = Character.toLowerCase ((char)key), upper = Character.toUpperCase ((char)key);
	for (int n = 1; n <= count; n++) {
		int index = n <= count - 1 - caret ? caret + n : n - (count - caret);
		String string = itemStore.getItem (index);
		if (string == null || string.isEmpty ()) {
			continue;
		}
		char first = string.charAt (0);
		if (first != 0 && (Character.toLowerCase (first) == lower || Character.toUpperCase (first) == upper)) {
			storeSelectFromKeyboard (index);
			return true;
		}
	}
	return false;
}

void updateStoreItemHeight (long font) {
	long hDC = OS.GetDC (handle);
	long oldFont = font != 0 ? OS.SelectObject (hDC, font) : 0;
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	if (font != 0) {
		OS.SelectObject (hDC, oldFont);
	}
	OS.ReleaseDC (handle, hDC);
	OS.SendMessage (handle, OS.LB_SETITEMHEIGHT, 0, tm.tmHeight);
}

@Override
void updateMenuLocation (Event event) {
	if (viewport != null) {
		viewport.updateMenuLocation (event);
		return;
	}
	Rectangle clientArea = getClientAreaInPixels ();
	int x = clientArea.x, y = clientArea.y;
	int focusIndex = getFocusIndex();
	if (focusIndex != -1) {
		RECT rect = new RECT ();
		long newFont, oldFont = 0;
		long hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
        if (newFont != 0) {
            oldFont = OS.SelectObject(hDC, newFont);
        }
		int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
		char [] buffer = new char [64 + 1];
		int length = itemStore != null ? OS.LB_ERR : (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, focusIndex, 0);
		if (itemStore != null && focusIndex < itemStore.getItemCount ()) {
			int textLength = storeChars (storeText (focusIndex), true);
			OS.DrawText (hDC, textBuffer, textLength, rect, flags);
		}
		if (length != OS.LB_ERR) {
			if (length + 1 > buffer.length) {
				buffer = new char [length + 1];
			}
			int result = (int)OS.SendMessage (handle, OS.LB_GETTEXT, focusIndex, buffer);
			if (result != OS.LB_ERR) {
				OS.DrawText (hDC, buffer, length, rect, flags);
			}
		}
        if (newFont != 0) {
            OS.SelectObject(hDC, oldFont);
        }
		OS.ReleaseDC (handle, hDC);
		x = Math.max (x, rect.right / 2);
		x = Math.min (x, clientArea.x + clientArea.width);

		OS.SendMessage (handle, OS.LB_GETITEMRECT, focusIndex, rect);
		y = Math.max (y, rect.bottom);
		y = Math.min (y, clientArea.y + clientArea.height);
	}
	Point pt = toDisplayInPixels (x, y);
	int zoom = getAutoscalingZoom();
	event.setLocation(DPIUtil.pixelToPoint(pt.x, zoom), DPIUtil.pixelToPoint(pt.y, zoom));
}

@Override
boolean updateTextDirection (int textDirection) {
	if (itemStore != null) {
		return storeTextDirection (textDirection);
	}
	if (textDirection == AUTO_TEXT_DIRECTION) {
        /* If auto is already in effect, there's nothing to do. */
        if ((state & HAS_AUTO_DIRECTION) != 0) {
            return false;
        }
		state |= HAS_AUTO_DIRECTION;
	} else {
		state &= ~HAS_AUTO_DIRECTION;
		if (!addedUCC /*(state & HAS_AUTO_DIRECTION) == 0*/) {
			return super.updateTextDirection (textDirection);
		}
	}
	if (viewport != null) {
		addedUCC = false;
		viewport.reloadResidentRows ();
		return textDirection == AUTO_TEXT_DIRECTION || super.updateTextDirection (textDirection);
	}
	int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
    if (count == OS.LB_ERR) {
        return false;
    }
	int selection = (int)OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	addedUCC = false;
	while (count-- > 0) {
		int length = (int)OS.SendMessage (handle, OS.LB_GETTEXTLEN, count, 0);
        if (length == OS.LB_ERR) {
            break;
        }
        if (length == 0) {
            continue;
        }
		char [] buffer = new char [length + 1];
        if (OS.SendMessage(handle, OS.LB_GETTEXT, count, buffer) == OS.LB_ERR) {
            break;
        }
        if (OS.SendMessage(handle, OS.LB_DELETESTRING, count, 0) == OS.LB_ERR) {
            break;
        }
		if ((state & HAS_AUTO_DIRECTION) == 0) {
			/* Should remove UCC */
			System.arraycopy(buffer, 1, buffer, 0, length);
		}
        /* Adding UCC is handled in OS.LB_INSERTSTRING */
        if (OS.SendMessage(handle, OS.LB_INSERTSTRING, count, buffer) == OS.LB_ERR) {
            break;
        }
	}
	if (selection != OS.LB_ERR) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, selection, 0);
	}
	return textDirection == AUTO_TEXT_DIRECTION || super.updateTextDirection (textDirection);
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle () | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT;
	if ((style & SWT.VIRTUAL) != 0) {
		bits |= OS.LBS_NODATA | OS.LBS_OWNERDRAWFIXED;
	}
	if (ListViewport.mediates (style)) bits &= ~OS.WS_VSCROLL;
    if ((style & SWT.SINGLE) != 0) {
        return bits;
    }
	if ((style & SWT.MULTI) != 0) {
        if ((style & SWT.SIMPLE) != 0) {
            return bits | OS.LBS_MULTIPLESEL;
        }
		return bits | OS.LBS_EXTENDEDSEL;
	}
	return bits;
}

@Override
void releaseWidget () {
	if (storePrefetch != null && display != null && !display.isDisposed ()) {
		display.timerExec (-1, storePrefetch);
	}
	storePrefetch = null;
	super.releaseWidget ();
}

@Override
TCHAR windowClass () {
	return ListClass;
}

@Override
long windowProc () {
	return ListProc;
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
	/* Below code is to support auto text direction. */
	if (handle != 0 && lParam != 0 && (state & HAS_AUTO_DIRECTION) != 0) {
		switch (msg) {
			case OS.LB_ADDSTRING:
			case OS.LB_INSERTSTRING:
			case OS.LB_FINDSTRINGEXACT:
				int length = OS.wcslen (lParam); // we are always Unicode here
				TCHAR buffer = new TCHAR (length);
				OS.MoveMemory (buffer, lParam, buffer.length () * TCHAR.sizeof);
				String string = buffer.toString (0, length);
				int direction = BidiUtil.resolveTextDirection (string);
				if (direction == SWT.NONE) {
					/*
					 * Force adding a UCC even when no strong characters are found.
					 * Otherwise, the List items would retain the old direction,
					 * which might be inappropriate for the new text.
					 */
					direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
				}
				string = (direction == SWT.RIGHT_TO_LEFT ? RLE : LRE) + string;
				buffer = new TCHAR (string, true);
				long hHeap = OS.GetProcessHeap ();
				length = buffer.length() * TCHAR.sizeof;
				long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, length);
				OS.MoveMemory (pszText, buffer, length);
				long code = super.windowProc (hwnd, msg, wParam, pszText);
				OS.HeapFree (hHeap, 0, pszText);
				addedUCC = true;
				return code;
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

@Override
LRESULT WM_CHAR (long wParam, long lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
    if (result != null) {
        return result;
    }
	if (viewport != null) {
		result = viewport.onChar (wParam);
		if (result != null) return result;
	}
	/*
	* Feature in Windows.  The Windows list box does not implement
	* the control key interface for multi-select list boxes, making
	* it inaccessible from the keyboard.  The fix is to implement
	* the key processing.
	*/
	if (OS.GetKeyState (OS.VK_CONTROL) < 0 && OS.GetKeyState (OS.VK_SHIFT) >= 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.LBS_EXTENDEDSEL) != 0) {
			switch ((int)wParam) {
				case OS.VK_SPACE: {
					int index = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
					int code = (int)OS.SendMessage (handle, OS.LB_GETSEL, index, 0);
                    if (code == OS.LB_ERR) {
                        break;
                    }
					OS.SendMessage (handle, OS.LB_SETSEL, code != 0 ? 0 : 1, index);
					OS.SendMessage (handle, OS.LB_SETANCHORINDEX, index, 0);
					sendSelectionEvent (SWT.Selection);
					return LRESULT.ZERO;
				}
			}
		}
	}
	if (itemStore != null && storeTypeAhead ((int)wParam)) {
		return LRESULT.ZERO;
	}
	return result;
}

@Override
LRESULT WM_GETOBJECT (long wParam, long lParam) {
	/*
	* An LBS_NODATA list box has no item text for the standard accessible
	* proxy; name the rows from the item store.
	*/
	if (itemStore != null && accessible == null && (int)lParam == OS.OBJID_CLIENT) {
		getAccessible ().addAccessibleListener (new AccessibleAdapter () {
			@Override
			public void getName (AccessibleEvent e) {
				if (itemStore != null && e.childID >= 0 && e.childID < itemStore.getItemCount ()) {
					e.result = storeText (e.childID);
				}
			}
		});
	}
	if (viewport != null && (int) lParam == OS.OBJID_CLIENT) viewport.installAccessibility ();
	return super.WM_GETOBJECT (wParam, lParam);
}

@Override
LRESULT WM_KEYDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
    if (result != null) {
        return result;
    }
	if (viewport != null) {
		result = viewport.onKeyDown (wParam);
		if (result != null) return result;
	}
	/*
	* Feature in Windows.  The Windows list box does not implement
	* the control key interface for multi-select list boxes, making
	* it inaccessible from the keyboard.  The fix is to implement
	* the key processing.
	*/
	if (OS.GetKeyState (OS.VK_CONTROL) < 0 && OS.GetKeyState (OS.VK_SHIFT) >= 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.LBS_EXTENDEDSEL) != 0) {
			int newIndex = -1;
			switch ((int)wParam) {
				case OS.VK_SPACE: {
					/*
					* Ensure that the window proc does not process VK_SPACE
					* so that it can be handled in WM_CHAR.  This allows the
					* application to cancel an operation that is normally
					* performed in WM_KEYDOWN from WM_CHAR.
					*/
					return LRESULT.ZERO;
				}
				case OS.VK_UP:
				case OS.VK_DOWN: {
					int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
					newIndex = Math.max (0, oldIndex + (((int)wParam) == OS.VK_UP ? -1 : 1));
					break;
				}
				case OS.VK_PRIOR: {
					int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
					int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
					if (oldIndex != topIndex) {
						newIndex = topIndex;
					} else {
						forceResize ();
						RECT rect = new RECT ();
						OS.GetClientRect (handle, rect);
						int itemHeight = (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
						int pageSize = Math.max (2, (rect.bottom / itemHeight));
						newIndex = Math.max (0, topIndex - (pageSize - 1));
					}
					break;
				}
				case OS.VK_NEXT: {
					int topIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
					int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
					forceResize ();
					RECT rect = new RECT ();
					OS.GetClientRect (handle, rect);
					int itemHeight = (int)OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
					int pageSize = Math.max (2, (rect.bottom / itemHeight));
					int bottomIndex = topIndex + pageSize - 1;
					if (oldIndex != bottomIndex) {
						newIndex = bottomIndex;
					} else {
						newIndex = bottomIndex + pageSize - 1;
					}
					int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
                    if (count != OS.LB_ERR) {
                        newIndex = Math.min(count - 1, newIndex);
                    }
					break;
				}
				case OS.VK_HOME: {
					newIndex = 0;
					break;
				}
				case OS.VK_END: {
					int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
                    if (count == OS.LB_ERR) {
                        break;
                    }
					newIndex = count - 1;
					break;
				}
			}
			if (newIndex != -1) {
				/*
				* Feature in Windows.  When the user changes focus using
				* the keyboard, the focus indicator does not draw.  The
				* fix is to update the UI state for the control whenever
				* the focus indicator changes as a result of something
				* the user types.
				*/
				int uiState = (int)OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
				if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
					OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
					/*
					* Bug in Windows.  When the WM_CHANGEUISTATE is used
					* to update the UI state for a list that has been
					* selected using Shift+Arrow, the focus indicator
					* has pixel corruption.  The fix is to redraw the
					* control.
					*/
					RECT itemRect = new RECT ();
					int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
					OS.SendMessage (handle, OS.LB_GETITEMRECT, oldIndex, itemRect);
					OS.InvalidateRect (handle, itemRect, true);
				}
				OS.SendMessage (handle, OS.LB_SETCARETINDEX, newIndex, 0);
				return LRESULT.ZERO;
			}
		}
	}
	return result;
}

@Override
LRESULT WM_SETFONT (long wParam, long lParam) {
	LRESULT result = super.WM_SETFONT (wParam, lParam);
	if (itemStore == null || result != null) {
		return result;
	}
	/* An owner-draw fixed list box keeps its first item height; follow the font like a plain list box. */
	int top = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	long code = callWindowProc (handle, OS.WM_SETFONT, wParam, lParam);
	updateStoreItemHeight (wParam);
	if ((int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0) != top) {
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, top, 0);
	}
	itemStore.invalidateWidths ();
	return new LRESULT (code);
}

@Override
LRESULT WM_SETREDRAW (long wParam, long lParam) {
	LRESULT result = super.WM_SETREDRAW (wParam, lParam);
    if (result != null) {
        return result;
    }
	/*
	* Bug in Windows.  When WM_SETREDRAW is used to turn off
	* redraw for a list, table or tree, the background of the
	* control is drawn.  The fix is to call DefWindowProc(),
	* which stops all graphics output to the control.
	*/
	OS.DefWindowProc (handle, OS.WM_SETREDRAW, wParam, lParam);
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	/*
	* Bug in Windows.  If the top index is changed while the
	* list is being resized, Windows does not redraw properly
	* when their is white space at the bottom of the control.
	* The fix is to detect when the top index has changed and
	* redraw the control.
	*
	* Bug in Windows.  If the receiver is scrolled horizontally
	* and is resized, the list does not redraw properly.  The fix
	* is to redraw the control when the horizontal scroll bar is
	* not at the beginning.
	*/
	int oldIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (!isDisposed ()) {
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_POS;
		if (OS.GetScrollInfo (handle, OS.SB_HORZ, info)) {
            if (info.nPos != 0) {
                OS.InvalidateRect(handle, null, true);
            }
		}
		int newIndex = (int)OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
        if (newIndex != OS.LB_ERR) {
            viewportRuntime.scrollTo(0, Math.max(0, newIndex));
        }
        if (oldIndex != newIndex) {
            OS.InvalidateRect(handle, null, true);
        }
	}
	return result;
}

@Override
LRESULT wmCommandChild (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
	switch (code) {
		case OS.LBN_SELCHANGE:
			sendSelectionEvent (SWT.Selection);
			break;
		case OS.LBN_DBLCLK:
			sendSelectionEvent (SWT.DefaultSelection);
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

@Override
LRESULT wmDrawChild (long wParam, long lParam) {
	if (itemStore == null) {
		return super.wmDrawChild (wParam, lParam);
	}
	if ((storeBits & (STORE_CONTENT_FROZEN | STORE_THUMB_FROZEN)) != 0) {
		return LRESULT.ONE;
	}
	if (drawStruct == null) {
		drawStruct = new DRAWITEMSTRUCT ();
		drawRect = new RECT ();
	}
	DRAWITEMSTRUCT struct = drawStruct;
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	RECT rect = drawRect;
	OS.SetRect (rect, struct.left, struct.top, struct.right, struct.bottom);
	long hDC = struct.hDC;
	int index = struct.itemID;
	boolean focus = (struct.itemState & OS.ODS_FOCUS) != 0 && (struct.itemState & OS.ODS_NOFOCUSRECT) == 0;
	if (index < 0 || index >= itemStore.getItemCount ()) {
		if (focus) {
			OS.DrawFocusRect (hDC, rect);
		}
		return LRESULT.ONE;
	}
	int requested = setDataCount;
	String string = storeText (index);
	if (isDisposed () || index >= itemStore.getItemCount ()) {
		return LRESULT.ONE;
	}
	if (setDataCount != requested) {
		storeRequestPrefetch ();
	}
	long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0), oldFont = 0;
	if (newFont != 0) {
		oldFont = OS.SelectObject (hDC, newFont);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		int widest = itemStore.getMeasuredWidestWidth ();
		measureDC = hDC;
		itemStore.measureItem (index);
		measureDC = 0;
		if (itemStore.getMeasuredWidestWidth () != widest) {
			storeExtent (false);
		}
	}
	boolean selected = (struct.itemState & OS.ODS_SELECTED) != 0;
	int oldText = -1, oldBack = -1;
	if (selected) {
		oldBack = OS.SetBkColor (hDC, OS.GetSysColor (OS.COLOR_HIGHLIGHT));
		oldText = OS.SetTextColor (hDC, OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT));
	} else if ((struct.itemState & OS.ODS_DISABLED) != 0) {
		oldText = OS.SetTextColor (hDC, OS.GetSysColor (OS.COLOR_GRAYTEXT));
	}
	int length = storeChars (string, true);
	int flags = OS.ETO_OPAQUE | OS.ETO_CLIPPED;
	if ((OS.GetWindowLong (handle, OS.GWL_EXSTYLE) & OS.WS_EX_RTLREADING) != 0) {
		flags |= OS.ETO_RTLREADING;
	}
	OS.ExtTextOut (hDC, rect.left + STORE_TEXT_X, rect.top, flags, rect, textBuffer, length, null);
	if (oldText != -1) {
		OS.SetTextColor (hDC, oldText);
	}
	if (oldBack != -1) {
		OS.SetBkColor (hDC, oldBack);
	}
	if (newFont != 0) {
		OS.SelectObject (hDC, oldFont);
	}
	if (focus) {
		OS.DrawFocusRect (hDC, rect);
	}
	return LRESULT.ONE;
}

@Override
void handleDPIChange(Event event, float scalingFactor) {
	super.handleDPIChange(event, scalingFactor);
	if((style & SWT.H_SCROLL) != 0) {
		// Recalculate the Scroll width, as length of items has changed
		setScrollWidth();
	}
}
}
