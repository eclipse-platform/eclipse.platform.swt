package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
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
	int font, hdwp;
	Control [] tabList;
	
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
	int count = 0;
	int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	if (hwndChild == 0) return new Control [0];
	while (hwndChild != 0) {
		count++;
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	Control [] children = new Control [count];
	int index = 0;
	hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		Control control = WidgetTable.get (hwndChild);
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
	int index = 0, count = 0;
	while (index < tabList.length) {
		if (!tabList [index].isDisposed ()) count++;
		index++;
	}
	if (index == count) return tabList;
	Control [] newList = new Control [count];
	index = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [index].isDisposed ()) {
			newList [index++] = tabList [i];
		}
	}
	tabList = newList;
	return tabList;
}

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

Control [] computeTabList () {
	Control result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? tabList : _getChildren ();
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
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
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

void createHandle () {
	super.createHandle ();
	state |= CANVAS;
}
void drawBackground (int hdc, RECT rect) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) != 0) return;
	}
	int pixel = getBackgroundPixel ();
	int hBrush = findBrush (pixel);
	OS.FillRect (hdc, rect, hBrush);
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
	checkWidget ();
	return _getChildren ();
}

int getChildrenCount () {
	/*
	* NOTE: The current implementation will count
	* non-registered children.
	*/
	int count = 0;
	int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
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
 * Gets the last specified tabbing order for the control.
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
	checkWidget ();
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
	checkWidget ();
	if (layout == null) return;
	int count = getChildrenCount ();
	if (count == 0) return;
	if (count > 1 && hdwp == 0) {
		hdwp = OS.BeginDeferWindowPos (count);
	}
	layout.layout (this, changed);
	int oldHdwp = hdwp;
	hdwp = 0;
	if (oldHdwp != 0) OS.EndDeferWindowPos (oldHdwp);
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
	tabList = null;
	int oldHdwp = hdwp;
	hdwp = 0;
	if (oldHdwp != 0) OS.EndDeferWindowPos (oldHdwp);
}

public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setRadioFocus ()) return true;
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setFocus ()) return true;
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
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order; must not be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the tabList is null</li>
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
	if (tabList == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<tabList.length; i++) {
		Control control = tabList [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//		Shell shell = control.getShell ();
//		while (control != shell && control != this) {
//			control = control.parent;
//		}
//		if (control != this) error (SWT.ERROR_INVALID_PARENT);
		if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	/*
	* This code is intentionally commented.  It is
	* not yet clear whether setting the tab list 
	* should force the widget to be a tab group
	* instead of a tab item or non-traversable.
	*/
//	Control [] children = _getChildren ();
//	for (int i=0; i<children.length; i++) {
//		Control control = children [i];
//		if (control != null) {
//			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//			int index = 0;
//			while (index < tabList.length) {
//				if (tabList [index] == control) break;
//				index++;
//			}
//			int hwnd = control.handle;
//			int bits = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
//			if (index == tabList.length) {
//				bits &= ~OS.WS_TABSTOP;
//			} else {
//				bits |= OS.WS_TABSTOP;
//			}
//			OS.SetWindowLong (hwnd, OS.GWL_STYLE, bits);
//		}
//	}
	this.tabList = tabList;
}

boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	if ((state & CANVAS) != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			if (setTabItemFocus ()) return true;
		}
	}
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isVisible () && child.setRadioFocus ()) return true;
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
}

boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	if ((state & CANVAS) != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			return forceFocus ();
		}
	}
	return setFocus ();
}

String toolTipText (NMTTDISPINFO hdr) {
	if ((hdr.uFlags & OS.TTF_IDISHWND) == 0) {
		return null;
	}
	int hwnd = hdr.idFrom;
	if (hwnd == 0) return null;
	Control control = WidgetTable.get (hwnd);
	if (control == null) return null;
	return control.toolTipText;
}

boolean translateMnemonic (char key) {
	if (super.translateMnemonic (key)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.translateMnemonic (key)) return true;
	}
	return false;
}

int widgetStyle () {
	/* Temporary code to force SWT.CLIP_SIBLINGS */
	return super.widgetStyle () | OS.WS_CLIPCHILDREN;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	if ((state & CANVAS) != 0) {
		return super.WM_ERASEBKGND (wParam, lParam);
	}
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	drawBackground (wParam, rect);
	return LRESULT.ONE;
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return new LRESULT (OS.DLGC_STATIC);
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			int flags = OS.DLGC_WANTALLKEYS | OS.DLGC_WANTARROWS | OS.DLGC_WANTTAB;
			return new LRESULT (flags);
		}
		int count = getChildrenCount ();
		if (count != 0) return new LRESULT (OS.DLGC_STATIC);
	}
	return result;
}

LRESULT WM_GETFONT (int wParam, int lParam) {
	LRESULT result = super.WM_GETFONT (wParam, lParam);
	if (result != null) return result;
	int code = callWindowProc (OS.WM_GETFONT, wParam, lParam);
	if (code != 0) return new LRESULT (code);
	if (font == 0) font = defaultFont ();
	return new LRESULT (font);
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);

	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return result;
		if (OS.GetWindow (handle, OS.GW_CHILD) == 0) setFocus ();
	}
	return result;
}

LRESULT WM_NOTIFY (int wParam, int lParam) {
	if (!OS.IsWinCE) {
		NMHDR hdr = new NMHDR ();
		OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
		switch (hdr.code) {
			case OS.TTN_GETDISPINFOW:
			case OS.TTN_GETDISPINFOA: {
				NMTTDISPINFO lpnmtdi = new NMTTDISPINFO ();
				OS.MoveMemory (lpnmtdi, lParam, NMTTDISPINFO.sizeof);
				String string = toolTipText (lpnmtdi);
				if (string != null && string.length () != 0) {
					Shell shell = getShell ();
					string = Display.withCrLf (string);
					/*
					* Bug in Windows 98.  For some reason, the tool bar control
					* sends both TTN_GETDISPINFOW and TTN_GETDISPINFOA to get
					* the tool tip text and the tab folder control sends only 
					* TTN_GETDISPINFOW.  The fix is to handle only TTN_GETDISPINFOW,
					* even though it should never be sent on Windows 98.
					*/
					if (hdr.code == OS.TTN_GETDISPINFOW) {
						int length = string.length ();
						char [] buffer = new char [length + 1];
						string.getChars(0, length, buffer, 0);
						shell.setToolTipText (lpnmtdi, buffer);
					} else {
						/* Use the character encoding for the default locale */
						TCHAR buffer = new TCHAR (0, string, true);
						shell.setToolTipText (lpnmtdi, buffer);
					}
					OS.MoveMemory (lParam, lpnmtdi, NMTTDISPINFO.sizeof);
					return LRESULT.ZERO;
				}
				break;
			}
		}
	}
	return super.WM_NOTIFY (wParam, lParam);
}

LRESULT WM_PAINT (int wParam, int lParam) {
	if ((state & CANVAS) == 0) {
		return super.WM_PAINT (wParam, lParam);
	}
	
	/*
	* This code is intentionally commented.  Don't exit
	* early because the background must still be painted,
	* even though no application code will be painting
	* the widget.
	*
	* Do not uncomment this code.
	*/
//	if (!hooks (SWT.Paint)) return null;

	/* Get the damage */
	int [] lpRgnData = null;
	boolean isComplex = false;
	boolean exposeRegion = false;
	if ((style & SWT.NO_MERGE_PAINTS) != 0) {
		int rgn = OS.CreateRectRgn (0, 0, 0, 0);
		isComplex = OS.GetUpdateRgn (handle, rgn, false) == OS.COMPLEXREGION;
		if (isComplex) {
			int nBytes = OS.GetRegionData (rgn, 0, null);
			lpRgnData = new int [nBytes / 4];
			exposeRegion = OS.GetRegionData (rgn, nBytes, lpRgnData) != 0;
		}
		OS.DeleteObject (rgn);
	}

	/* Set the clipping bits */
	int oldBits = 0;
	if (!OS.IsWinCE) {
		oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		int newBits = oldBits | OS.WS_CLIPSIBLINGS | OS.WS_CLIPCHILDREN;	
		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	}

	/* Create the paint GC */
	PAINTSTRUCT ps = new PAINTSTRUCT ();
	GCData data = new GCData ();
	data.ps = ps;
	GC gc = GC.win32_new (this, data);
	int hDC = gc.handle;
	
	/* Send the paint event */
	Event event = new Event ();
	event.gc = gc;
	if (isComplex && exposeRegion) {
		RECT rect = new RECT ();
		int nCount = lpRgnData [2];
		for (int i=0; i<nCount; i++) {
			OS.SetRect (rect,
				lpRgnData [8 + (i << 2)],
				lpRgnData [8 + (i << 2) + 1],
				lpRgnData [8 + (i << 2) + 2],
				lpRgnData [8 + (i << 2) + 3]);
			drawBackground (hDC, rect);	
			event.x = rect.left;
			event.y = rect.top;
			event.width = rect.right - rect.left;
			event.height = rect.bottom - rect.top;
			event.count = nCount - 1 - i;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the paint
			* event.  If this happens, attempt to give back the
			* paint GC anyways because this is a scarce Windows
			* resource.
			*/
			sendEvent (SWT.Paint, event);
			if (isDisposed ()) break;
		}
	} else {
		RECT rect = new RECT ();
		OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
		drawBackground (hDC, rect);
		event.x = ps.left;
		event.y = ps.top;
		event.width = ps.right - ps.left;
		event.height = ps.bottom - ps.top;
		sendEvent (SWT.Paint, event);
	}
	// widget could be disposed at this point

	/* Dispose the paint GC */
	event.gc = null;
	gc.dispose ();

	if (!OS.IsWinCE) { 
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
	return LRESULT.ZERO;
}

LRESULT WM_SETFONT (int wParam, int lParam) {
	return super.WM_SETFONT (font = wParam, lParam);
}

LRESULT WM_SIZE (int wParam, int lParam) {
	/*
	* Begin deferred window positioning
	*/
	int count = getChildrenCount ();
	if (count > 1 && hdwp == 0) {
		hdwp = OS.BeginDeferWindowPos (count);
	}
	
	/* Layout and resize */
	if (layout != null) layout.layout (this, false);
	LRESULT result = super.WM_SIZE (wParam, lParam);
	
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the resize
	* event.  If this happens, end the processing of the
	* Windows message by returning the result of the
	* WM_SIZE message.
	*/
	if (isDisposed ()) return result;
	
	/* End deferred window positioning */
	int oldHdwp = hdwp;
	hdwp = 0;
	if (oldHdwp != 0) OS.EndDeferWindowPos (oldHdwp);
	
	/* Damage the widget to cause a repaint */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
			if (hooks (SWT.Paint)) {
				OS.InvalidateRect (handle, null, true);
			}
		}
	}
	return result;
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		int hwndChild = children [i].handle;
		OS.SendMessage (hwndChild, OS.WM_SYSCOLORCHANGE, 0, 0);
	}
	return null;
}

LRESULT WM_SYSCOMMAND (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;
		
	/*
	* Check to see if the command is a system command or
	* a user menu item that was added to the system menu.
	*/
	if ((wParam & 0xF000) == 0) return result;

	/*
	* Bug in Windows.  When a vertical or horizontal scroll bar is
	* hidden or shown while the opposite scroll bar is being scrolled
	* by the user (with WM_HSCROLL code SB_LINEDOWN), the scroll bar
	* does not redraw properly.  The fix is to detect this case and
	* redraw the non-client area.
	*/
	if (!OS.IsWinCE) {
		int cmd = wParam & 0xFFF0;
		switch (cmd) {
			case OS.SC_HSCROLL:
			case OS.SC_VSCROLL:
				boolean showHBar = horizontalBar != null && horizontalBar.getVisible ();
				boolean showVBar = verticalBar != null && verticalBar.getVisible ();
				int code = callWindowProc (OS.WM_SYSCOMMAND, wParam, lParam);
				if ((showHBar != (horizontalBar != null && horizontalBar.getVisible ())) ||
					(showVBar != (verticalBar != null && verticalBar.getVisible ()))) {
						int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_UPDATENOW;
						OS.RedrawWindow (handle, null, 0, flags);
					}		
				if (code == 0) return LRESULT.ZERO;
				return new LRESULT (code);
		}
	}
	/* Return the result */
	return result;
}

}
