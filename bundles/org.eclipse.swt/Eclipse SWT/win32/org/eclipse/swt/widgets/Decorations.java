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
 * Instances of this class provide the appearance and
 * behavior of <code>Shells</code>, but are not top
 * level shells or dialogs. Class <code>Shell</code>
 * shares a significant amount of code with this class,
 * and is a subclass.
 * <p>
 * Instances are always displayed in one of the maximized, 
 * minimized or normal states:
 * <ul>
 * <li>
 * When an instance is marked as <em>maximized</em>, the
 * window manager will typically resize it to fill the
 * entire visible area of the display, and the instance
 * is usually put in a state where it can not be resized 
 * (even if it has style <code>RESIZE</code>) until it is
 * no longer maximized.
 * </li><li>
 * When an instance is in the <em>normal</em> state (neither
 * maximized or minimized), its appearance is controlled by
 * the style constants which were specified when it was created
 * and the restrictions of the window manager (see below).
 * </li><li>
 * When an instance has been marked as <em>minimized</em>,
 * its contents (client area) will usually not be visible,
 * and depending on the window manager, it may be
 * "iconified" (that is, replaced on the desktop by a small
 * simplified representation of itself), relocated to a
 * distinguished area of the screen, or hidden. Combinations
 * of these changes are also possible.
 * </li>
 * </ul>
 * </p>
 * Note: The styles supported by this class must be treated
 * as <em>HINT</em>s, since the window manager for the
 * desktop on which the instance is visible has ultimate
 * control over the appearance and behavior of decorations.
 * For example, some window managers only support resizable
 * windows and will always assume the RESIZE style, even if
 * it is not set.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * Class <code>SWT</code> provides two "convenience constants"
 * for the most commonly required style combinations:
 * <dl>
 * <dt><code>SHELL_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application top level shell: (that 
 * is, <code>CLOSE | TITLE | MIN | MAX | RESIZE</code>)
 * </dd>
 * <dt><code>DIALOG_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application dialog shell: (that 
 * is, <code>TITLE | CLOSE | BORDER</code>)
 * </dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see #getMinimized
 * @see #getMaximized
 * @see Shell
 * @see SWT
 */

public class Decorations extends Canvas {
	Image image;
	Menu menuBar;
	Menu [] menus;
	MenuItem [] items;
	Control savedFocus;
	Button defaultButton, saveDefault;
	int swFlags, hAccel, nAccel;
	int hwndCB, hwndTB, hIcon;
	
	/*
	* The start value for WM_COMMAND id's.
	* Windows reserves the values 0..100.
	*/
	static final int ID_START = 100;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Decorations () {
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Decorations (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void add (Menu menu) {
	if (menus == null) menus = new Menu [4];
	for (int i=0; i<menus.length; i++) {
		if (menus [i] == null) {
			menus [i] = menu;
			return;
		}
	}
	Menu [] newMenus = new Menu [menus.length + 4];
	newMenus [menus.length] = menu;
	System.arraycopy (menus, 0, newMenus, 0, menus.length);
	menus = newMenus;
}

void add (MenuItem item) {
	if (items == null) items = new MenuItem [12];
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) {
			item.id = i + ID_START;
			items [i] = item;
			return;
		}
	}
	item.id = items.length + ID_START;
	MenuItem [] newItems = new MenuItem [items.length + 12];
	newItems [items.length] = item;
	System.arraycopy (items, 0, newItems, 0, items.length);
	items = newItems;
}

void bringToTop () {
	/*
	* This code is intentionally commented.  On some platforms,
	* the ON_TOP style creates a shell that will stay on top
	* of every other shell on the desktop.  Using SetWindowPos ()
	* with HWND_TOP caused problems on Windows so this code is
	* commented out until this functionality is specified and
	* the problems are fixed.
	*/
//	if ((style & SWT.ON_TOP) != 0) {
//		int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
//		OS.SetWindowPos (handle, OS.HWND_TOP, 0, 0, 0, 0, flags);
//	} else {
		OS.BringWindowToTop (handle);
//	}
}

static int checkStyle (int style) {
	if (OS.IsWinCE) {
		/*
		* Feature in WinCE PPC.  WS_MINIMIZEBOX or WS_MAXIMIZEBOX
		* are not supposed to be used.  If they are, the result
		* is a button which does not repaint correctly.  The fix
		* is to remove this style.
		*/
		if ((style & SWT.MIN) != 0) style &= ~SWT.MIN;
		if ((style & SWT.MAX) != 0) style &= ~SWT.MAX;
		return style;
	}

	/*
	* If either WS_MINIMIZEBOX or WS_MAXIMIZEBOX are set,
	* we must also set WS_SYSMENU or the buttons will not
	* appear.
	*/
	if ((style & (SWT.MIN | SWT.MAX)) != 0) style |= SWT.CLOSE;
	
	/*
	* Both WS_SYSMENU and WS_CAPTION must be set in order
	* to for the system menu to appear.
	*/
	if ((style & SWT.CLOSE) != 0) style |= SWT.TITLE;
	
	/*
	* Bug in Windows.  The WS_CAPTION style must be
	* set when the window is resizable or it does not
	* draw properly.
	*/
	/*
	* This code is intentionally commented.  It seems
	* that this problem originally in Windows 3.11,
	* has been fixed in later versions.  Because the
	* exact nature of the drawing problem is unknown,
	* keep the commented code around in case it comes
	* back.
	*/
//	if ((style & SWT.RESIZE) != 0) style |= SWT.TITLE;
	
	return style;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

Control computeTabGroup () {
	return this;
}

Control computeTabRoot () {
	return this;
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();

	/* Get the size of the trimmings */
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	boolean hasMenu = OS.IsWinCE ? false : OS.GetMenu (handle) != 0;
	OS.AdjustWindowRectEx (rect, bits, hasMenu, OS.GetWindowLong (handle, OS.GWL_EXSTYLE));

	/* Get the size of the scroll bars */
	if (horizontalBar != null) rect.bottom += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	if (verticalBar != null) rect.right += OS.GetSystemMetrics (OS.SM_CXVSCROLL);

	/* Get the height of the menu bar */
	if (hasMenu) {
		RECT testRect = new RECT ();
		OS.SetRect (testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
		OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		while ((testRect.bottom - testRect.top) < height) {
			rect.top -= OS.GetSystemMetrics (OS.SM_CYMENU) - OS.GetSystemMetrics (OS.SM_CYBORDER);
			OS.SetRect(testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
			OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		}
	}
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

void createAcceleratorTable () {
	hAccel = nAccel = 0;
	int maxAccel = 0;
	if (menuBar == null || items == null) {
		if (!OS.IsWinCE) return;
		maxAccel = 1;
	} else {
		maxAccel = OS.IsWinCE ? items.length + 1 : items.length;
	}
	int size = ACCEL.sizeof;
	ACCEL accel = new ACCEL ();
	byte [] buffer1 = new byte [size];	
	byte [] buffer2 = new byte [maxAccel * size];
	if (menuBar != null && items != null) {
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item != null && item.accelerator != 0) {
				Menu parent = item.parent;
				while (parent != null && parent != menuBar) {
					parent = parent.getParentMenu ();
				}
				if (parent == menuBar) {
					item.fillAccel (accel);
					OS.MoveMemory (buffer1, accel, size);
					System.arraycopy (buffer1, 0, buffer2, nAccel * size, size);
					nAccel++;
				}
			}
		}
	}
	if (OS.IsWinCE) {
		/* 
		* Note on WinCE PPC.  Close the shell when user taps CTRL-Q.
		* IDOK represents the "Done Button" which also closes the shell.
		*/
		accel.fVirt = OS.FVIRTKEY | OS.FCONTROL;
		accel.key = 'Q';
		accel.cmd = OS.IDOK;
		OS.MoveMemory (buffer1, accel, size);
		System.arraycopy (buffer1, 0, buffer2, nAccel * size, size);
		nAccel++;			
	}
	if (nAccel != 0) hAccel = OS.CreateAcceleratorTable (buffer2, nAccel);
}

void createHandle () {
	super.createHandle ();
	if (parent == null) return;
	setParent ();
	setSystemMenu ();
}

void createWidget () {
	super.createWidget ();
	swFlags = OS.IsWinCE ? OS.SW_SHOWMAXIMIZED : OS.SW_SHOWNOACTIVATE;
	hAccel = -1;
}

void destroyAcceleratorTable () {
	if (hAccel != 0 && hAccel != -1) OS.DestroyAcceleratorTable (hAccel);
	hAccel = -1;
}

Menu findMenu (int hMenu) {
	if (menus == null) return null;
	for (int i=0; i<menus.length; i++) {
		Menu menu = menus [i];
		if ((menu != null) && (hMenu == menu.handle)) return menu;
	}
	return null;
}

MenuItem findMenuItem (int id) {
	if (items == null) return null;
	id = id - ID_START;
	if (0 <= id && id < items.length) return items [id];
	return null;
}

public Rectangle getBounds () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			int width = lpwndpl.right - lpwndpl.left;
			int height = lpwndpl.bottom - lpwndpl.top;
			return new Rectangle (lpwndpl.left, lpwndpl.top, width, height);
		}
	}
	return super.getBounds ();
}

public Rectangle getClientArea () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			RECT rect = new RECT ();
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			int width = lpwndpl.right - lpwndpl.left;
			int height = lpwndpl.bottom - lpwndpl.top;
			OS.SetRect (rect, 0, 0, width, height);
			OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, rect);
			return new Rectangle (0, 0, rect.right, rect.bottom);
		}
	}
	return super.getClientArea ();
}

/**
 * Returns the receiver's default button if one had
 * previously been set, otherwise returns null.
 *
 * @return the default button or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDefaultButton
 */
public Button getDefaultButton () {
	checkWidget ();
	return defaultButton;
}

/**
 * Returns the receiver's image if it had previously been 
 * set using <code>setImage()</code>. The image is typically
 * displayed by the window manager when the instance is
 * marked as iconified, and may also be displayed somewhere
 * in the trim when the instance is in normal or maximized
 * states.
 * <p>
 * Note: This method will return null if called before
 * <code>setImage()</code> is called. It does not provide
 * access to a window manager provided, "default" image
 * even if one exists.
 * </p>
 * 
 * @return the image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

public Point getLocation () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			return new Point (lpwndpl.left, lpwndpl.top);
		}
	}
	return super.getLocation ();
}

/**
 * Returns <code>true</code> if the receiver is currently
 * maximized, and false otherwise. 
 * <p>
 *
 * @return the maximized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setMaximized
 */
public boolean getMaximized () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsWindowVisible (handle)) return OS.IsZoomed (handle);
	}
	return swFlags == OS.SW_SHOWMAXIMIZED;
}

/**
 * Returns the receiver's menu bar if one had previously
 * been set, otherwise returns null.
 *
 * @return the menu bar or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenuBar () {
	checkWidget ();
	return menuBar;
}

/**
 * Returns <code>true</code> if the receiver is currently
 * minimized, and false otherwise. 
 * <p>
 *
 * @return the minimized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setMinimized
 */
public boolean getMinimized () {
	checkWidget ();
	if (OS.IsWinCE) return false;
	if (OS.IsWindowVisible (handle)) return OS.IsIconic (handle);
	return swFlags == OS.SW_SHOWMINNOACTIVE;
}

String getNameText () {
	return getText ();
}

public Point getSize () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			int width = lpwndpl.right - lpwndpl.left;
			int height = lpwndpl.bottom - lpwndpl.top;
			return new Point (width, height);
		}
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Point (width, height);
}

/**
 * Returns the receiver's text, which is the string that the
 * window manager will typically display as the receiver's
 * <em>title</em>. If the text has not previously been set, 
 * returns an empty string.
 *
 * @return the text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	/* Use the character encoding for the default locale */
	TCHAR buffer = new TCHAR (0, length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	return buffer.toString (0, length);
}

boolean isTabGroup () {
	/*
	* Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
	*/
	return true;
}

boolean isTabItem () {
	/*
	* Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
	*/
	return false;
}

Decorations menuShell () {
	return this;
}

void releaseWidget () {
	if (menuBar != null) {
		menuBar.releaseWidget ();
		menuBar.releaseHandle ();
	}
	menuBar = null;
	if (menus != null) {
		for (int i=0; i<menus.length; i++) {
			Menu menu = menus [i];
			if (menu != null && !menu.isDisposed ()) {
				menu.dispose ();
			}
		}
	}
	menus = null;
	super.releaseWidget ();
	if (hIcon != 0) OS.DestroyIcon (hIcon);
	hIcon = 0;
	items = null;
	image = null;
	savedFocus = null;
	defaultButton = saveDefault = null;
	if (hAccel != 0 && hAccel != -1) OS.DestroyAcceleratorTable (hAccel);
	hAccel = -1;
	hwndCB = 0;
}

void remove (Menu menu) {
	if (menus == null) return;
	for (int i=0; i<menus.length; i++) {
		if (menus [i] == menu) {
			menus [i] = null;
			return;
		}
	}
}

void remove (MenuItem item) {
	if (items == null) return;
	items [item.id - ID_START] = null;
	item.id = -1;
}

boolean restoreFocus () {
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	if (savedFocus == null) return false;
	return savedFocus.forceFocus ();
}

void saveFocus () {
	Control control = getDisplay ().getFocusControl ();
	if (control != null) setSavedFocus (control);
}

void setBounds (int x, int y, int width, int height, int flags) {
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			lpwndpl.showCmd = 0;
			if ((flags & OS.SWP_NOMOVE) == 0) {
				lpwndpl.left = x;
				lpwndpl.top = y;
			}
			if ((flags & OS.SWP_NOSIZE) == 0) {
				lpwndpl.right = x + width;
				lpwndpl.bottom = y + height;
			}
			OS.SetWindowPlacement (handle, lpwndpl);
			return;
		}
	}
	super.setBounds (x, y, width, height, flags);
}

/**
 * If the argument is not null, sets the receiver's default
 * button to the argument, and if the argument is null, sets
 * the receiver's default button to the first button which
 * was set as the receiver's default button (called the 
 * <em>saved default button</em>). If no default button had
 * previously been set, or the saved default button was
 * disposed, the receiver's default button will be set to
 * null. 
 *
 * @param the new default button
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the button has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultButton (Button button) {
	checkWidget ();
	setDefaultButton (button, true);
}

void setDefaultButton (Button button, boolean save) {
	if (button == null) {
		if (defaultButton == saveDefault) return;
	} else {
		if (button.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((button.style & SWT.PUSH) == 0) return;
		if (button == defaultButton) return;
	}
	if (defaultButton != null) {
		if (!defaultButton.isDisposed ()) defaultButton.setDefault (false);
	}
	if ((defaultButton = button) == null) defaultButton = saveDefault;
	if (defaultButton != null) {
		if (!defaultButton.isDisposed ()) defaultButton.setDefault (true);
	}
	if (save || saveDefault == null) saveDefault = defaultButton;
	if (saveDefault != null && saveDefault.isDisposed ()) saveDefault = null;
}

public boolean setFocus () {
	checkWidget ();
	if (this instanceof Shell) return super.setFocus ();
	/*
	* Bug in Windows.  Setting the focus to a child of the
	* receiver interferes with moving and resizing of the
	* parent shell.  The fix (for now) is to always set the
	* focus to the shell.
	*/
	int hwndFocus = OS.SetFocus (getShell ().handle);
	return hwndFocus == OS.GetFocus ();
}
/**
 * Sets the receiver's image to the argument, which may
 * be null. The image is typically displayed by the window
 * manager when the instance is marked as iconified, and
 * may also be displayed somewhere in the trim when the
 * instance is in normal or maximized states.
 * 
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	/*
	* Feature in WinCE.  WM_SETICON and WM_GETICON set the icon
	* for the window class, not the window instance.  This means
	* that it is possible to set an icon into a window and then
	* later free the icon, thus freeing the icon for every window.
	* The fix is to avoid the API.
	* 
	* On WinCE PPC, icons in windows are not displayed anyways.
	*/
	if (OS.IsWinCE) {
		this.image = image;
		return;
	}
	int hImage = 0;
	if (image != null) {
		if (hIcon != 0) OS.DestroyIcon (hIcon);
		hIcon = 0;
		switch (image.type) {
			case SWT.BITMAP:
				/* Copy the bitmap in case it's a DIB */
				int hBitmap = image.handle;
				BITMAP bm = new BITMAP ();
				OS.GetObject (hBitmap, BITMAP.sizeof, bm);
				byte [] lpvBits = new byte [(bm.bmWidth + 15) / 16 * 2 * bm.bmHeight];
				int hMask = OS.CreateBitmap (bm.bmWidth, bm.bmHeight, 1, 1, lpvBits);
				int hDC = OS.GetDC (handle);
				int hdcMem = OS.CreateCompatibleDC (hDC);
				int hColor = OS.CreateCompatibleBitmap (hDC, bm.bmWidth, bm.bmHeight);
				OS.SelectObject (hdcMem, hColor);
				int hdcBmp = OS.CreateCompatibleDC (hDC);
				OS.SelectObject (hdcBmp, hBitmap);
				OS.BitBlt (hdcMem, 0, 0, bm.bmWidth, bm.bmHeight, hdcBmp, 0, 0, OS.SRCCOPY);
				ICONINFO info = new ICONINFO ();
				info.fIcon = true;
				info.hbmMask = hMask;
				info.hbmColor = hColor;
				hImage = hIcon = OS.CreateIconIndirect (info);
				OS.DeleteObject (hMask);
				OS.DeleteObject(hColor);
				OS.DeleteDC (hdcBmp);
				OS.DeleteDC (hdcMem);
				OS.ReleaseDC (handle, hDC);
				break;
			case SWT.ICON:
				hImage = image.handle;
				break;
			default:
				return;
		}
	}
	this.image = image;
	OS.SendMessage (handle, OS.WM_SETICON, OS.ICON_BIG, hImage);
	
	/*
	* Bug in Windows.  When WM_SETICON is used to remove an
	* icon from the window trimmings for a window with the
	* extended style bits WS_EX_DLGMODALFRAME, the window
	* trimmings do not redraw to hide the previous icon.
	* The fix is to force a redraw.
	*/
	if (!OS.IsWinCE) {
		if (hIcon == 0 && (style & SWT.BORDER) != 0) {
			int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE;
			OS.RedrawWindow (handle, null, 0, flags);
		}
	}
}

/**
 * Sets the maximized state of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the maximized state, and if the argument is
 * <code>false</code> and the receiver was previously maximized,
 * causes the receiver to switch back to either the minimized
 * or normal states.
 * <p>
 * Note: The result of intermixing calls to<code>setMaximized(true)</code>
 * and <code>setMinimized(true)</code> will vary by platform. Typically,
 * the behavior will match the platform user's expectations, but not
 * always. This should be avoided if possible.
 * </p>
 *
 * @param the new maximized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setMinimized
 */
public void setMaximized (boolean maximized) {
	checkWidget ();
	swFlags = maximized ? OS.SW_SHOWMAXIMIZED : OS.SW_RESTORE;
	if (OS.IsWinCE) {
		/*
		* Note: WinCE does not support SW_SHOWMAXIMIZED and SW_RESTORE. The
		* workaround is to resize the window to fit the parent client area.
		* PocketPC windows typically don't have a caption when they are
		* maximized. They usually have one when they are not occupying all the
		* space. We implement this behavior by default - it can be overriden by
		* setting SWT.TITLE or SWT.NO_TRIM.
		*/
		if (maximized) {
			if ((style & SWT.TITLE) == 0) {
				/* remove caption when maximized */
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				bits &= ~OS.WS_CAPTION;
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
			}
			int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
			RECT rect = new RECT ();
			OS.SystemParametersInfo (OS.SPI_GETWORKAREA, 0, rect, 0);
			int width = rect.right - rect.left, height = rect.bottom - rect.top;
			/* leave space for menubar */
			if (menuBar != null) {
				RECT rectCB = new RECT ();
				OS.GetWindowRect (hwndCB, rectCB);
				height -= rectCB.bottom - rectCB.top;
			}
			OS.SetWindowPos (handle, 0, rect.left, rect.top, width, height, flags);	
		} else {
			if ((style & SWT.NO_TRIM) == 0) {
				/* insert caption when no longer maximized */
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				bits |= OS.WS_CAPTION;
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
				int flags = OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_DRAWFRAME;
				OS.SetWindowPos (handle, 0, 0, 0, 0, 0, flags);
			}
		}
	} else {
		if (!OS.IsWindowVisible (handle)) return;
		if (maximized == OS.IsZoomed (handle)) return;
		OS.ShowWindow (handle, swFlags);
		OS.UpdateWindow (handle);
	}
}

/**
 * Sets the receiver's menu bar to the argument, which
 * may be null.
 *
 * @param menu the new menu bar
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
/**
 * Sets the receiver's menu bar to the argument, which
 * may be null.
 *
 * @param menu the new menu bar
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenuBar (Menu menu) {
	checkWidget ();
	if (menuBar == menu) return;
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}	
	if (OS.IsWinCE) {
		/*
		* Note in WinCE PPC.  MenuBar is a separate popup window. If
		* the Shell is full screen, resize its window to leave
		* space for the MenuBar.
		*/
		boolean resize = (getMaximized() && menuBar != menu);
		if (menuBar != null) {
			OS.CommandBar_Destroy (hwndCB);
			hwndCB = 0;
			hwndTB = 0;
		}
		menuBar = menu;
		if (menuBar != null) {		
			SHMENUBARINFO mbi = new SHMENUBARINFO ();
			mbi.cbSize = mbi.sizeof;
			mbi.hwndParent = handle;
			mbi.dwFlags = 0;
			mbi.nToolBarId = 100; /* as defined in .rc file */
			mbi.hInstRes = OS.GetLibraryHandle ();
			boolean res = OS.SHCreateMenuBar (mbi);
			hwndCB = mbi.hwndMB;

			/* Get ToolBar */
			if (hwndCB != 0) hwndTB = OS.GetWindow (hwndCB, OS.GW_CHILD);
						
			if (hwndTB == 0) {
				/* we can't use the menubar */
				if (hwndCB != 0) OS.CommandBar_Destroy (hwndCB);
				return;
			}
			/* remove the menu item coming from the resource file */
			OS.SendMessage (hwndTB, OS.TB_DELETEBUTTON, 0, 0);
			
			/* populate tool bar mapping menu items to tool items */
			if (menuBar.getItemCount () > 0) {
				MenuItem[] items = menuBar.getItems ();
				TBBUTTON lpButton = new TBBUTTON ();
				TBBUTTONINFO info = new TBBUTTONINFO ();
				info.cbSize = TBBUTTONINFO.sizeof;
				info.dwMask = OS.TBIF_TEXT;
				for (int i = 0; i < items.length; i++) {
					MenuItem item = items[i];
					/* insert item */
					lpButton.idCommand = item.id;
					lpButton.fsStyle = (byte) (OS.TBSTYLE_DROPDOWN | OS.TBSTYLE_AUTOSIZE | 0x80);
					lpButton.fsState = (byte) OS.TBSTATE_ENABLED;
					lpButton.iBitmap = OS.I_IMAGENONE;
					if ((item.style & SWT.SEPARATOR) != 0) {
						lpButton.fsStyle = (byte) OS.BTNS_SEP;
					}
					OS.SendMessage (hwndTB, OS.TB_INSERTBUTTON, i, lpButton);

					if ((item.style & SWT.SEPARATOR) == 0) {
						/* set text */
						String string = item.getText();
						int hHeap = OS.GetProcessHeap ();
						TCHAR buffer = new TCHAR (0, string, true);
						int byteCount = buffer.length () * TCHAR.sizeof;
						int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
						OS.MoveMemory (pszText, buffer, byteCount);
						info.pszText = pszText;
						OS.SendMessage (hwndTB, OS.TB_SETBUTTONINFO, item.id, info);
						if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);

						/* set state */
						if (!item.isEnabled()) {
							int fsState = 0;
							OS.SendMessage (hwndTB, OS.TB_SETSTATE, item.id, fsState);
						}

						/* set menu */
						Menu menu2 = item.menu;
						if (menu2 != null) {
							OS.SendMessage (hwndCB, OS.SHCMBM_SETSUBMENU, item.id, menu2.handle);
						}
					}
				}
			}
		}
		if (resize) setMaximized (true);
	} else {
		menuBar = menu;
		int hMenu = 0;
		if (menuBar != null) hMenu = menuBar.handle;
		OS.SetMenu (handle, hMenu);
	}
	destroyAcceleratorTable ();
}

/**
 * Sets the minimized stated of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the minimized state, and if the argument is
 * <code>false</code> and the receiver was previously minimized,
 * causes the receiver to switch back to either the maximized
 * or normal states.
 * <p>
 * Note: The result of intermixing calls to<code>setMaximized(true)</code>
 * and <code>setMinimized(true)</code> will vary by platform. Typically,
 * the behavior will match the platform user's expectations, but not
 * always. This should be avoided if possible.
 * </p>
 *
 * @param the new maximized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setMaximized
 */
public void setMinimized (boolean minimized) {
	checkWidget ();
	if (OS.IsWinCE) return;
	swFlags = OS.SW_RESTORE;
	if (minimized) swFlags = OS.SW_SHOWMINNOACTIVE;
	if (!OS.IsWindowVisible (handle)) return;
	if (minimized == OS.IsIconic (handle)) return;
	OS.ShowWindow (handle, swFlags);
	OS.UpdateWindow (handle);
}

void setParent () {
	/*
	* In order for an MDI child window to support
	* a menu bar, setParent () is needed to reset
	* the parent.  Otherwise, the MDI child window
	* will appear as a separate shell.  This is an
	* undocumented and possibly dangerous Windows
	* feature.
	*/
	Display display = getDisplay ();
	int hwndParent = parent.handle;
	display.lockActiveWindow = true;
	OS.SetParent (handle, hwndParent);
	if (!OS.IsWindowVisible (hwndParent)) {
		OS.ShowWindow (handle, OS.SW_SHOWNA);
	}
	display.lockActiveWindow = false;
}

void setSavedFocus (Control control) {
	if (this == control) {
		savedFocus = null;
		return;
	}
	if (this != control.menuShell ()) return;
	savedFocus = control;
}

void setSystemMenu () {
	if (OS.IsWinCE) return;
	int hMenu = OS.GetSystemMenu (handle, false);
	if (hMenu == 0) return;
	int oldCount = OS.GetMenuItemCount (hMenu);
	if ((style & SWT.RESIZE) == 0) {
		OS.DeleteMenu (hMenu, OS.SC_SIZE, OS.MF_BYCOMMAND);
	}
	if ((style & SWT.MIN) == 0) {
		OS.DeleteMenu (hMenu, OS.SC_MINIMIZE, OS.MF_BYCOMMAND);
	}
	if ((style & SWT.MAX) == 0) {
		OS.DeleteMenu (hMenu, OS.SC_MAXIMIZE, OS.MF_BYCOMMAND);
	}
	if ((style & (SWT.MIN | SWT.MAX)) == 0) {
		OS.DeleteMenu (hMenu, OS.SC_RESTORE, OS.MF_BYCOMMAND);
	}
	int newCount = OS.GetMenuItemCount (hMenu);
	if ((style & SWT.CLOSE) == 0 || newCount != oldCount) {	
		OS.DeleteMenu (hMenu, OS.SC_TASKLIST, OS.MF_BYCOMMAND);
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		info.fMask = OS.MIIM_ID;
		int index = 0;
		while (index < newCount) {
			if (OS.GetMenuItemInfo (hMenu, index, true, info)) {
				if (info.wID == OS.SC_CLOSE) break;
			}
			index++;
		}
		if (index != newCount) {
			OS.DeleteMenu (hMenu, index - 1, OS.MF_BYPOSITION);
			if ((style & SWT.CLOSE) == 0) {
				OS.DeleteMenu (hMenu, OS.SC_CLOSE, OS.MF_BYCOMMAND);
			}
		}
	}
}

/**
 * Sets the receiver's text, which is the string that the
 * window manager will typically display as the receiver's
 * <em>title</em>, to the argument, which may not be null. 
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	/* Use the character encoding for the default locale */
	TCHAR buffer = new TCHAR (0, string, true);
	OS.SetWindowText (handle, buffer);
}

public void setVisible (boolean visible) {
	checkWidget ();
	if (visible == OS.IsWindowVisible (handle)) return;
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		if (OS.IsWinCE) {
			OS.ShowWindow (handle, OS.SW_SHOW);
		} else {
			OS.DrawMenuBar (handle);
			OS.ShowWindow (handle, swFlags);
		}
		OS.UpdateWindow (handle);
	} else {
		if (!OS.IsWinCE) {
			if (OS.IsIconic (handle)) {
				swFlags = OS.SW_SHOWMINNOACTIVE;
			} else {
				if (OS.IsZoomed (handle)) {
					swFlags = OS.SW_SHOWMAXIMIZED;
				} else {
					if (handle == OS.GetActiveWindow ()) {
						swFlags = OS.SW_RESTORE;
					} else {
						swFlags = OS.SW_SHOWNOACTIVATE;
					}
				}
			}
		}
		OS.ShowWindow (handle, OS.SW_HIDE);
		sendEvent (SWT.Hide);
	}
}

boolean translateAccelerator (MSG msg) {
	if (!isEnabled ()) return false;
	if (hAccel == -1) createAcceleratorTable ();
	if (hAccel == 0) return false;
	return OS.TranslateAccelerator (handle, hAccel, msg) != 0;
}

boolean traverseItem (boolean next) {
	return false;
}

int widgetExtStyle () {
	int bits = 0;
	if ((style & SWT.NO_TRIM) != 0) return bits;
	if (OS.IsWinCE) {
		if ((style & SWT.CLOSE) != 0) bits |= OS.WS_EX_CAPTIONOKBTN;
	}
	if ((style & SWT.TOOL) != 0) bits |= OS.WS_EX_TOOLWINDOW;
	if ((style & SWT.RESIZE) != 0) return bits;
	if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_DLGMODALFRAME;
	return bits;
}

int widgetStyle () {
	/* 
	* Set WS_POPUP and clear WS_VISIBLE and WS_TABSTOP.
	* NOTE: WS_TABSTOP is the same as WS_MAXIMIZEBOX so
	* it cannot be used to do tabbing with decorations.
	*/
	int bits = super.widgetStyle () | OS.WS_POPUP;
	bits &= ~(OS.WS_VISIBLE | OS.WS_TABSTOP);
	
	/* Set the title bits and no-trim bits */
	bits &= ~OS.WS_BORDER;
	if ((style & SWT.NO_TRIM) != 0) return bits;
	if ((style & SWT.TITLE) != 0) bits |= OS.WS_CAPTION;
	
	/* Set the min and max button bits */
	if ((style & SWT.MIN) != 0) bits |= OS.WS_MINIMIZEBOX;
	if ((style & SWT.MAX) != 0) bits |= OS.WS_MAXIMIZEBOX;
	
	/* Set the resize, dialog border or border bits */
	if ((style & SWT.RESIZE) != 0) {
		bits |= OS.WS_THICKFRAME;	
	} else {
		if ((style & SWT.BORDER) == 0) bits |= OS.WS_BORDER;
	}

	/* Set the system menu and close box bits */
	if (!OS.IsWinCE) {
		if ((style & SWT.CLOSE) != 0) bits |= OS.WS_SYSMENU;
	}
	
	return bits;
}

int windowProc (int msg, int wParam, int lParam) {
	switch (msg) {
		case OS.WM_APP:
		case OS.WM_APP+1:
			if (hAccel == -1) createAcceleratorTable ();
			return msg == OS.WM_APP ? nAccel : hAccel;
	}
	return super.windowProc (msg, wParam, lParam);
}

LRESULT WM_ACTIVATE (int wParam, int lParam) {
	LRESULT result = super.WM_ACTIVATE (wParam, lParam);
	if (result != null) return result;
	if ((wParam & 0xFFFF) == 0) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the deactivate
		* event.  If this happens, end the processing of the
		* Windows message by returning zero as the result of
		* the window proc.
		*/
		Shell shell = getShell ();
		shell.setActiveControl (null);
		if (isDisposed ()) return LRESULT.ZERO;
		sendEvent (SWT.Deactivate);
		if (isDisposed ()) return LRESULT.ZERO;
		saveFocus ();
	} else {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the activate
		* event.  If this happens, end the processing of the
		* Windows message by returning zero as the result of
		* the window proc.
		*/
		sendEvent (SWT.Activate);
		if (isDisposed ()) return LRESULT.ZERO;
		if (restoreFocus ()) return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_CLOSE (int wParam, int lParam) {
	LRESULT result = super.WM_CLOSE (wParam, lParam);
	if (result != null) return result;
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	// the widget could be disposed at this point
	if (event.doit && !isDisposed ()) dispose ();
	return LRESULT.ZERO;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result  = super.WM_KILLFOCUS (wParam, lParam);
	saveFocus ();
	return result;
}

LRESULT WM_NCACTIVATE (int wParam, int lParam) {
	LRESULT result  = super.WM_NCACTIVATE (wParam, lParam);
	if (result != null) return result;
	if (wParam == 0) {
		Display display = getDisplay ();
		if (display.lockActiveWindow) return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_QUERYOPEN (int wParam, int lParam) {
	LRESULT result = super.WM_QUERYOPEN (wParam, lParam);
	if (result != null) return result;
	sendEvent (SWT.Deiconify);
	// widget could be disposed at this point
	return result;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	restoreFocus ();
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the resize
	* event.  If this happens, end the processing of the
	* Windows message by returning the result of the
	* WM_SIZE message.
	*/
	if (isDisposed ()) return result;
	if (wParam == OS.SIZE_MINIMIZED) {
		sendEvent (SWT.Iconify);
		// widget could be disposed at this point
	}
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam,lParam);
	if (result != null) return result;
	Display display = getDisplay ();
	if (display.lockActiveWindow) {
		WINDOWPOS lpwp = new WINDOWPOS ();
		OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
		lpwp.flags |= OS.SWP_NOZORDER;
		OS.MoveMemory (lParam, lpwp, WINDOWPOS.sizeof);
	}
	return result;
}

}
