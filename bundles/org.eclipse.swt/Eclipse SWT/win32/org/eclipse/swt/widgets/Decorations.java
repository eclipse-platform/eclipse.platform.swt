/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


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
 * IMPORTANT: This class was intended to be abstract and
 * should <em>never</em> be referenced or instantiated.
 * Instead, the class <code>Shell</code> should be used.
 * </p>
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
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL</dd>
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
	Image image, smallImage, largeImage;
	Image [] images;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
	int swFlags, hAccel, nAccel;
	boolean moved, resized, opened;
	int oldX = OS.CW_USEDEFAULT, oldY = OS.CW_USEDEFAULT;
	int oldWidth = OS.CW_USEDEFAULT, oldHeight = OS.CW_USEDEFAULT;

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
 * @see SWT#BORDER
 * @see SWT#CLOSE
 * @see SWT#MIN
 * @see SWT#MAX
 * @see SWT#RESIZE
 * @see SWT#TITLE
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#ON_TOP
 * @see SWT#TOOL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Decorations (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void addMenu (Menu menu) {
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

void bringToTop () {
	/*
	* This code is intentionally commented.  On some platforms,
	* the ON_TOP style creates a shell that will stay on top
	* of every other shell on the desktop.  Using SetWindowPos ()
	* with HWND_TOP caused problems on Windows 98 so this code is
	* commented out until this functionality is specified and
	* the problems are fixed.
	*/
//	if ((style & SWT.ON_TOP) != 0) {
//		int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
//		OS.SetWindowPos (handle, OS.HWND_TOP, 0, 0, 0, 0, flags);
//	} else {
		OS.BringWindowToTop (handle);
		// widget could be disposed at this point
//	}
}

static int checkStyle (int style) {
	if ((style & SWT.NO_TRIM) != 0) {
		style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
	}
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
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
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

void checkBorder () {
	/* Do nothing */
}

void checkOpened () {
	if (!opened) resized = false;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.DefMDIChildProc (hwnd, msg, wParam, lParam);
}

void closeWidget () {
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

int compare (ImageData data1, ImageData data2, int width, int height, int depth) {
	int value1 = Math.abs (data1.width - width), value2 = Math.abs (data2.width - width);
	if (value1 == value2) {
		int transparent1 = data1.getTransparencyType ();
		int transparent2 = data2.getTransparencyType ();
		if (transparent1 == transparent2) {
			if (data1.depth == data2.depth) return 0;
			return data1.depth > data2.depth && data1.depth <= depth ? -1 : 1;
		}
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
			if (transparent1 == SWT.TRANSPARENCY_ALPHA) return -1;
			if (transparent2 == SWT.TRANSPARENCY_ALPHA) return 1;
		}
		if (transparent1 == SWT.TRANSPARENCY_MASK) return -1;
		if (transparent2 == SWT.TRANSPARENCY_MASK) return 1;
		if (transparent1 == SWT.TRANSPARENCY_PIXEL) return -1;
		if (transparent2 == SWT.TRANSPARENCY_PIXEL) return 1;
		return 0;
	}
	return value1 < value2 ? -1 : 1;
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
	int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	boolean hasMenu = OS.IsWinCE ? false : OS.GetMenu (handle) != 0;
	OS.AdjustWindowRectEx (rect, bits1, hasMenu, bits2);

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

void createAccelerators () {
	hAccel = nAccel = 0;
	int maxAccel = 0;
	MenuItem [] items = display.items;
	if (menuBar == null || items == null) {
		if (!OS.IsPPC) return;
		maxAccel = 1;
	} else {
		maxAccel = OS.IsPPC ? items.length + 1 : items.length;
	}
	ACCEL accel = new ACCEL ();
	byte [] buffer1 = new byte [ACCEL.sizeof];	
	byte [] buffer2 = new byte [maxAccel * ACCEL.sizeof];
	if (menuBar != null && items != null) {
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item != null && item.accelerator != 0) {
				Menu menu = item.parent;
				if (menu.parent == this) {
					while (menu != null && menu != menuBar) {
						menu = menu.getParentMenu ();
					}
					if (menu == menuBar) {
						item.fillAccel (accel);
						OS.MoveMemory (buffer1, accel, ACCEL.sizeof);
						System.arraycopy (buffer1, 0, buffer2, nAccel * ACCEL.sizeof, ACCEL.sizeof);
						nAccel++;
					}
				}
			}
		}
	}
	if (OS.IsPPC) {
		/* 
		* Note on WinCE PPC.  Close the shell when user taps CTRL-Q.
		* IDOK represents the "Done Button" which also closes the shell.
		*/
		accel.fVirt = (byte) (OS.FVIRTKEY | OS.FCONTROL);
		accel.key = (short) 'Q';
		accel.cmd = (short) OS.IDOK;
		OS.MoveMemory (buffer1, accel, ACCEL.sizeof);
		System.arraycopy (buffer1, 0, buffer2, nAccel * ACCEL.sizeof, ACCEL.sizeof);
		nAccel++;			
	}
	if (nAccel != 0) hAccel = OS.CreateAcceleratorTable (buffer2, nAccel);
}

Image createIcon (Image image) {
	ImageData data = image.getImageData ();
	if (data.alpha == -1 && data.alphaData == null) {
		ImageData mask = data.getTransparencyMask ();
		return new Image (display, data, mask);
	}
	int width = data.width, height = data.height;
	int hMask, hBitmap;
	int hDC = OS.GetDC (handle);
	int dstHdc = OS.CreateCompatibleDC (hDC), oldDstBitmap;
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		hBitmap = Display.create32bitDIB (image.handle, data.alpha, data.alphaData, data.transparentPixel);
		hMask = OS.CreateBitmap (width, height, 1, 1, null);
		oldDstBitmap = OS.SelectObject (dstHdc, hMask);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
	} else {
		hMask = Display.createMaskFromAlpha (data, width, height);
		/* Icons need black pixels where the mask is transparent */
		hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
		oldDstBitmap = OS.SelectObject (dstHdc, hBitmap);
		int srcHdc = OS.CreateCompatibleDC (hDC);
		int oldSrcBitmap = OS.SelectObject (srcHdc, image.handle);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, hMask);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCAND);
		OS.SelectObject (srcHdc, image.handle);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, oldSrcBitmap);
		OS.DeleteDC (srcHdc);
	}
	OS.SelectObject (dstHdc, oldDstBitmap);
	OS.DeleteDC (dstHdc);
	OS.ReleaseDC (handle, hDC);
	ICONINFO info = new ICONINFO ();
	info.fIcon = true;
	info.hbmColor = hBitmap;
	info.hbmMask = hMask;
	int hIcon = OS.CreateIconIndirect (info);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.DeleteObject (hBitmap);
	OS.DeleteObject (hMask);
	return Image.win32_new (display, SWT.ICON, hIcon);
}

void createHandle () {
	super.createHandle ();
	if (parent != null || ((style & SWT.TOOL) != 0)) {
		setParent ();
		setSystemMenu ();
	}
}

void createWidget () {
	super.createWidget ();
	swFlags = OS.IsWinCE ? OS.SW_SHOWMAXIMIZED : OS.SW_SHOWNOACTIVATE;
	hAccel = -1;
}

void destroyAccelerators () {
	if (hAccel != 0 && hAccel != -1) OS.DestroyAcceleratorTable (hAccel);
	hAccel = -1;
}

public void dispose () {
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!(this instanceof Shell)) {
		setVisible (false);
		if (!traverseDecorations (false)) {
			Shell shell = getShell ();
			shell.setFocus ();
		}
	}
	super.dispose ();
}

Menu findMenu (int hMenu) {
	if (menus == null) return null;
	for (int i=0; i<menus.length; i++) {
		Menu menu = menus [i];
		if (menu != null && hMenu == menu.handle) return menu;
	}
	return null;
}

void fixDecorations (Decorations newDecorations, Control control, Menu [] menus) {
	if (this == newDecorations) return;
	if (control == savedFocus) savedFocus = null;
	if (control == defaultButton) defaultButton = null;
	if (control == saveDefault) saveDefault = null;
	if (menus == null) return;
	Menu menu = control.menu;
	if (menu != null) {
		int index = 0;
		while (index <menus.length) {
			if (menus [index] == menu) {
				control.setMenu (null);
				return;
			}
			index++;
		}
		menu.fixMenus (newDecorations);
		destroyAccelerators ();
		newDecorations.destroyAccelerators ();
	}
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
	/* 
	* Note: The CommandBar is part of the client area,
	* not the trim.  Applications don't expect this so
	* subtract the height of the CommandBar.
	*/
	if (OS.IsHPC) {
		Rectangle rect = super.getClientArea ();
		if (menuBar != null) {
			int hwndCB = menuBar.hwndCB;
			int height = OS.CommandBar_Height (hwndCB);
			rect.y += height;
			rect.height -= height;
		}
		return rect;
	}
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
			lpwndpl.length = WINDOWPLACEMENT.sizeof;
			OS.GetWindowPlacement (handle, lpwndpl);
			int width = lpwndpl.right - lpwndpl.left;
			int height = lpwndpl.bottom - lpwndpl.top;
			/*
			* Feature in Windows.  For some reason WM_NCCALCSIZE does
			* not compute the client area when the window is minimized.
			* The fix is to compute it using AdjustWindowRectEx() and
			* GetSystemMetrics().
			* 
			* NOTE: This code fails to compute the correct client area
			* for a minimized window where the menu bar would wrap were
			* the window restored.  There is no fix for this problem at
			* this time.
			*/
			if (horizontalBar != null) width -= OS.GetSystemMetrics (OS.SM_CYHSCROLL);
			if (verticalBar != null) height -= OS.GetSystemMetrics (OS.SM_CXVSCROLL);
			RECT rect = new RECT ();
			int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
			int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
			boolean hasMenu = OS.IsWinCE ? false : OS.GetMenu (handle) != 0;
			OS.AdjustWindowRectEx (rect, bits1, hasMenu, bits2);
			width = Math.max (0, width - (rect.right - rect.left));
			height = Math.max (0, height - (rect.bottom - rect.top));
			return new Rectangle (0, 0, width, height);
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

/**
 * Returns the receiver's images if they had previously been 
 * set using <code>setImages()</code>. Images are typically
 * displayed by the window manager when the instance is
 * marked as iconified, and may also be displayed somewhere
 * in the trim when the instance is in normal or maximized
 * states. Depending where the icon is displayed, the platform
 * chooses the icon with the "best" attributes.  It is expected
 * that the array will contain the same icon rendered at different
 * sizes, with different depth and transparency attributes.
 * 
 * <p>
 * Note: This method will return an empty array if called before
 * <code>setImages()</code> is called. It does not provide
 * access to a window manager provided, "default" image
 * even if one exists.
 * </p>
 * 
 * @return the images
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Image [] getImages () {
	checkWidget ();
	if (images == null) return new Image [0];
	Image [] result = new Image [images.length];
	System.arraycopy (images, 0, result, 0, images.length);
	return result;
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
	if (OS.IsWinCE) return swFlags == OS.SW_SHOWMAXIMIZED;
	if (OS.IsWindowVisible (handle)) return OS.IsZoomed (handle);
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
	return super.getSize ();
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

public boolean isReparentable () {
	checkWidget ();
	/*
	* Feature in Windows.  Calling SetParent() for a shell causes
	* a kind of fake MDI to happen.  It doesn't work well on Windows
	* and is not supported on the other platforms.  The fix is to
	* disallow the SetParent().
	*/
	return false;
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
	if (menuBar != null) menuBar.releaseResources ();
	menuBar = null;
	if (menus != null) {
		do {
			int index = 0;
			while (index < menus.length) {
				Menu menu = menus [index];
				if (menu != null && !menu.isDisposed ()) {
					while (menu.getParentMenu () != null) {
						menu = menu.getParentMenu ();
					}
					menu.dispose ();
					break;
				}
				index++;
			}
			if (index == menus.length) break;
		} while (true);
	}
	menus = null;
	super.releaseWidget ();
	if (smallImage != null) smallImage.dispose ();
	if (largeImage != null) largeImage.dispose ();
	smallImage = largeImage = image = null;
	images = null;
	savedFocus = null;
	defaultButton = saveDefault = null;
	if (hAccel != 0 && hAccel != -1) OS.DestroyAcceleratorTable (hAccel);
	hAccel = -1;
}

void removeMenu (Menu menu) {
	if (menus == null) return;
	for (int i=0; i<menus.length; i++) {
		if (menus [i] == menu) {
			menus [i] = null;
			return;
		}
	}
}

boolean restoreFocus () {
	if (display.ignoreRestoreFocus) return true;
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	if (savedFocus != null && savedFocus.setSavedFocus ()) return true;
	/*
	* This code is intentionally commented.  When no widget
	* has been given focus, some platforms give focus to the
	* default button.  Windows doesn't do this.
	*/
//	if (defaultButton != null && !defaultButton.isDisposed ()) {
//		if (defaultButton.setFocus ()) return true;
//	}
	return false;
}

void saveFocus () {
	Control control = display._getFocusControl ();
	if (control != null && control != this && this == control.menuShell ()) {
		setSavedFocus (control);
	}
}

void setBounds (int x, int y, int width, int height, int flags, boolean defer) {
	if (OS.IsWinCE) {
		swFlags = OS.SW_RESTORE;
	} else {
		if (OS.IsIconic (handle)) {
			setPlacement (x, y, width, height, flags);
			return;
		}
	}
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	boolean sameOrigin = true;
	if ((OS.SWP_NOMOVE & flags) == 0) {
		sameOrigin = rect.left == x && rect.top == y;
		if (!sameOrigin) moved = true;
	}
	boolean sameExtent = true;
	if ((OS.SWP_NOSIZE & flags) == 0) {
		sameExtent = rect.right - rect.left == width && rect.bottom - rect.top == height;
		if (!sameExtent) resized = true;
	}
	if (!OS.IsWinCE) {
		if (OS.IsZoomed (handle)) {
			if (sameOrigin && sameExtent) return;
			setPlacement (x, y, width, height, flags);
			setMaximized (false);
			return;
		}
	}
	super.setBounds (x, y, width, height, flags, defer);
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
 * <p>
 * The default button is the button that is selected when
 * the receiver is active and the user presses ENTER.
 * </p>
 *
 * @param button the new default button
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
		if (defaultButton == saveDefault) {
			if (save) saveDefault = null;
			return;
		}
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
	if (save) saveDefault = defaultButton;
	if (saveDefault != null && saveDefault.isDisposed ()) saveDefault = null;
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
	this.image = image;
	setImages (image, null);
}

void setImages (Image image, Image [] images) {
	/*
	* Feature in WinCE.  WM_SETICON and WM_GETICON set the icon
	* for the window class, not the window instance.  This means
	* that it is possible to set an icon into a window and then
	* later free the icon, thus freeing the icon for every window.
	* The fix is to avoid the API.
	* 
	* On WinCE PPC, icons in windows are not displayed.
	*/
	if (OS.IsWinCE) return;
	if (smallImage != null) smallImage.dispose ();
	if (largeImage != null) largeImage.dispose ();
	smallImage = largeImage = null;
	int hSmallIcon = 0, hLargeIcon = 0;
	Image smallIcon = null, largeIcon = null;
	if (image != null) {
		smallIcon = largeIcon = image;
	} else {
		if (images != null && images.length > 0) {
			int depth = display.getIconDepth ();
			ImageData [] datas = null;
			if (images.length > 1) {
				Image [] bestImages = new Image [images.length];
				System.arraycopy (images, 0, bestImages, 0, images.length);
				datas = new ImageData [images.length];
				for (int i=0; i<datas.length; i++) {
					datas [i] = images [i].getImageData ();
				}
				images = bestImages;
				sort (images, datas, OS.GetSystemMetrics (OS.SM_CXSMICON), OS.GetSystemMetrics (OS.SM_CYSMICON), depth);
			}
			smallIcon = images [0];
			if (images.length > 1) {
				sort (images, datas, OS.GetSystemMetrics (OS.SM_CXICON), OS.GetSystemMetrics (OS.SM_CYICON), depth);
			}
			largeIcon = images [0];
		}
	}
	if (smallIcon != null) {
		switch (smallIcon.type) {
			case SWT.BITMAP:
				smallImage = createIcon (smallIcon);
				hSmallIcon = smallImage.handle;
				break;
			case SWT.ICON:
				hSmallIcon = smallIcon.handle;
				break;
		}
	}
	OS.SendMessage (handle, OS.WM_SETICON, OS.ICON_SMALL, hSmallIcon);
	if (largeIcon != null) {
		switch (largeIcon.type) {
			case SWT.BITMAP:
				largeImage = createIcon (largeIcon);
				hLargeIcon = largeImage.handle;
				break;
			case SWT.ICON:
				hLargeIcon = largeIcon.handle;
				break;
		}
	}
	OS.SendMessage (handle, OS.WM_SETICON, OS.ICON_BIG, hLargeIcon);
	
	/*
	* Bug in Windows.  When WM_SETICON is used to remove an
	* icon from the window trimmings for a window with the
	* extended style bits WS_EX_DLGMODALFRAME, the window
	* trimmings do not redraw to hide the previous icon.
	* The fix is to force a redraw.
	*/
	if (!OS.IsWinCE) {
		if (hSmallIcon == 0 && hLargeIcon == 0 && (style & SWT.BORDER) != 0) {
			int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE;
			OS.RedrawWindow (handle, null, 0, flags);
		}
	}
}

/**
 * Sets the receiver's images to the argument, which may
 * be an empty array. Images are typically displayed by the
 * window manager when the instance is marked as iconified,
 * and may also be displayed somewhere in the trim when the
 * instance is in normal or maximized states. Depending where
 * the icon is displayed, the platform chooses the icon with
 * the "best" attributes. It is expected that the array will
 * contain the same icon rendered at different sizes, with
 * different depth and transparency attributes.
 * 
 * @param images the new image array
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images is null or has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setImages (Image [] images) {
	checkWidget ();
	if (images == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i = 0; i < images.length; i++) {
		if (images [i] == null || images [i].isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.images = images;
	setImages (null, images);
}

/**
 * Sets the maximized state of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the maximized state, and if the argument is
 * <code>false</code> and the receiver was previously maximized,
 * causes the receiver to switch back to either the minimized
 * or normal states.
 * <p>
 * Note: The result of intermixing calls to <code>setMaximized(true)</code>
 * and <code>setMinimized(true)</code> will vary by platform. Typically,
 * the behavior will match the platform user's expectations, but not
 * always. This should be avoided if possible.
 * </p>
 *
 * @param maximized the new maximized state
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
		*/
		if (maximized) {
			RECT rect = new RECT ();
			OS.SystemParametersInfo (OS.SPI_GETWORKAREA, 0, rect, 0);
			int width = rect.right - rect.left, height = rect.bottom - rect.top;
			if (OS.IsPPC) {
				/* Leave space for the menu bar */
				if (menuBar != null) {
					int hwndCB = menuBar.hwndCB;
					RECT rectCB = new RECT ();
					OS.GetWindowRect (hwndCB, rectCB);
					height -= rectCB.bottom - rectCB.top;
				}
			}
			int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
			SetWindowPos (handle, 0, rect.left, rect.top, width, height, flags);	
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
public void setMenuBar (Menu menu) {
	checkWidget ();
	if (menuBar == menu) return;
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}	
	if (OS.IsWinCE) {
		if (OS.IsHPC) {
			boolean resize = menuBar != menu;
			if (menuBar != null) OS.CommandBar_Show (menuBar.hwndCB, false);
			menuBar = menu;
			if (menuBar != null) OS.CommandBar_Show (menuBar.hwndCB, true);
			if (resize) {
				sendEvent (SWT.Resize);
				if (isDisposed ()) return;
				if (layout != null) {
					markLayout (false, false);
					updateLayout (true, false);
				}
			}
		} else {
			if (OS.IsPPC) {
				/*
				* Note in WinCE PPC.  The menu bar is a separate popup window.
				* If the shell is full screen, resize its window to leave
				* space for the menu bar.
				*/
				boolean resize = getMaximized () && menuBar != menu;
				if (menuBar != null) OS.ShowWindow (menuBar.hwndCB, OS.SW_HIDE);
				menuBar = menu;
				if (menuBar != null) OS.ShowWindow (menuBar.hwndCB, OS.SW_SHOW);
				if (resize) setMaximized (true);
			}
			if (OS.IsSP) {
				if (menuBar != null) OS.ShowWindow (menuBar.hwndCB, OS.SW_HIDE);
				menuBar = menu;
				if (menuBar != null) OS.ShowWindow (menuBar.hwndCB, OS.SW_SHOW);
			}
		} 
	} else {
		if (menu != null) display.removeBar (menu);
		menuBar = menu;
		int hMenu = menuBar != null ? menuBar.handle: 0;
		OS.SetMenu (handle, hMenu);
	}
	destroyAccelerators ();
}

/**
 * Sets the minimized stated of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the minimized state, and if the argument is
 * <code>false</code> and the receiver was previously minimized,
 * causes the receiver to switch back to either the maximized
 * or normal states.
 * <p>
 * Note: The result of intermixing calls to <code>setMaximized(true)</code>
 * and <code>setMinimized(true)</code> will vary by platform. Typically,
 * the behavior will match the platform user's expectations, but not
 * always. This should be avoided if possible.
 * </p>
 *
 * @param minimized the new maximized state
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
	swFlags = minimized ? OS.SW_SHOWMINNOACTIVE : OS.SW_RESTORE;
	if (!OS.IsWindowVisible (handle)) return;
	if (minimized == OS.IsIconic (handle)) return;
	int flags = swFlags;
	if (flags == OS.SW_SHOWMINNOACTIVE && handle == OS.GetActiveWindow ()) {
		flags = OS.SW_MINIMIZE;
	}
	OS.ShowWindow (handle, flags);
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
	int hwndParent = parent.handle;
	display.lockActiveWindow = true;
	OS.SetParent (handle, hwndParent);
	if (!OS.IsWindowVisible (hwndParent)) {
		OS.ShowWindow (handle, OS.SW_SHOWNA);
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	bits &= ~OS.WS_CHILD;
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits | OS.WS_POPUP);
	OS.SetWindowLong (handle, OS.GWL_ID, 0);
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
	SetWindowPos (handle, OS.HWND_BOTTOM, 0, 0, 0, 0, flags);
	display.lockActiveWindow = false;
}

void setPlacement (int x, int y, int width, int height, int flags) {
	WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
	lpwndpl.length = WINDOWPLACEMENT.sizeof;
	OS.GetWindowPlacement (handle, lpwndpl);
	lpwndpl.showCmd = OS.SW_SHOWNA;
	if (OS.IsIconic (handle)) {
		lpwndpl.showCmd = OS.SW_SHOWMINNOACTIVE;
	} else {
		if (OS.IsZoomed (handle)) {
			lpwndpl.showCmd = OS.SW_SHOWMAXIMIZED;
		}
	}
	boolean sameOrigin = true;
	if ((flags & OS.SWP_NOMOVE) == 0) {
		sameOrigin = lpwndpl.left != x || lpwndpl.top != y;
		lpwndpl.right = x + (lpwndpl.right - lpwndpl.left);
		lpwndpl.bottom = y + (lpwndpl.bottom - lpwndpl.top);
		lpwndpl.left = x;
		lpwndpl.top = y;
	}
	boolean sameExtent = true;
	if ((flags & OS.SWP_NOSIZE) == 0) {
		sameExtent = lpwndpl.right - lpwndpl.left != width || lpwndpl.bottom - lpwndpl.top != height;
		lpwndpl.right = lpwndpl.left + width;
		lpwndpl.bottom = lpwndpl.top + height;
	}
	OS.SetWindowPlacement (handle, lpwndpl);
	if (OS.IsIconic (handle)) {
		if (sameOrigin) {
			moved = true;
			Point location = getLocation ();
			oldX = location.x;
			oldY = location.y;
			sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
		if (sameExtent) {
			resized = true;
			Rectangle rect = getClientArea ();
			oldWidth = rect.width;
			oldHeight = rect.height;
			sendEvent (SWT.Resize);
			if (isDisposed ()) return;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (true, false);
			}
		}
	}
}

void setSavedFocus (Control control) {
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
 * <em>title</em>, to the argument, which must not be null. 
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
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	/* Use the character encoding for the default locale */
	TCHAR buffer = new TCHAR (0, string, true);
	OS.SetWindowText (handle, buffer);
}

public void setVisible (boolean visible) {
	checkWidget ();
	if (drawCount != 0) {
		if (((state & HIDDEN) == 0) == visible) return;
	} else {
		if (visible == OS.IsWindowVisible (handle)) return;
	}
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		if (OS.IsHPC) {
			if (menuBar != null) {
				int hwndCB = menuBar.hwndCB;
				OS.CommandBar_DrawMenuBar (hwndCB, 0);
			}
		}
		if (drawCount != 0) {
			state &= ~HIDDEN;
		} else {
			if (OS.IsWinCE) {
				OS.ShowWindow (handle, OS.SW_SHOW);
			} else {
				if (menuBar != null) {
					display.removeBar (menuBar);
					OS.DrawMenuBar (handle);
				}
				OS.ShowWindow (handle, swFlags);
			}
			if (isDisposed ()) return;
			opened = true;
			if (!moved) {
				moved = true;
				Point location = getLocation ();
				oldX = location.x;
				oldY = location.y;
			}
			if (!resized) {
				resized = true;
				Rectangle rect = getClientArea ();
				oldWidth = rect.width;
				oldHeight = rect.height;
			}
			OS.UpdateWindow (handle);
		}
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
		if (drawCount != 0) {
			state |= HIDDEN;
		} else {
			OS.ShowWindow (handle, OS.SW_HIDE);
		}
		if (isDisposed ()) return;
		sendEvent (SWT.Hide);
	}
}

void sort (Image [] images, ImageData [] datas, int width, int height, int depth) {
	/* Shell Sort from K&R, pg 108 */
	int length = images.length;
	if (length <= 1) return;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (compare (datas [j], datas [j + gap], width, height, depth) >= 0) {
					Image swap = images [j];
					images [j] = images [j + gap];
					images [j + gap] = swap;
					ImageData swapData = datas [j];
					datas [j] = datas [j + gap];
					datas [j + gap] = swapData;
		   		}
	    	}
	    }
	}
}

boolean translateAccelerator (MSG msg) {
	if (!isEnabled () || !isActive ()) return false;
	if (menuBar != null && !menuBar.isEnabled ()) return false;
	if (translateMDIAccelerator (msg) || translateMenuAccelerator (msg)) return true;
	Decorations decorations = parent.menuShell ();
	return decorations.translateAccelerator (msg);
}

boolean translateMenuAccelerator (MSG msg) {
	if (hAccel == -1) createAccelerators ();
	return hAccel != 0 && OS.TranslateAccelerator (handle, hAccel, msg) != 0;
}

boolean translateMDIAccelerator (MSG msg) {
	if (!(this instanceof Shell)) {
		Shell shell = getShell ();
		int hwndMDIClient = shell.hwndMDIClient;
		if (hwndMDIClient != 0 && OS.TranslateMDISysAccel (hwndMDIClient, msg)) {
			return true;
		}
		if (msg.message == OS.WM_KEYDOWN) {
			if (OS.GetKeyState (OS.VK_CONTROL) >= 0) return false;
			switch (msg.wParam) {
				case OS.VK_F4:
					OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
					return true;
				case OS.VK_F6:
					if (traverseDecorations (true)) return true;
			}
			return false;
		}
		if (msg.message == OS.WM_SYSKEYDOWN) {
			switch (msg.wParam) {
				case OS.VK_F4:
					OS.PostMessage (shell.handle, OS.WM_CLOSE, 0, 0);
					return true;
			}
			return false;
		}
	}
	return false;
}

boolean traverseDecorations (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child instanceof Decorations) {
			if (child.setFocus ()) return true;
		}
	}
	return false;
}

boolean traverseItem (boolean next) {
	return false;
}

boolean traverseReturn () {
	if (defaultButton == null || defaultButton.isDisposed ()) return false;
	if (!defaultButton.isVisible () || !defaultButton.isEnabled ()) return false;
	defaultButton.click ();
	return true;
}

CREATESTRUCT widgetCreateStruct () {
	return new CREATESTRUCT ();
}

int widgetExtStyle () {
	int bits = super.widgetExtStyle () | OS.WS_EX_MDICHILD;
	bits &= ~OS.WS_EX_CLIENTEDGE;
	if ((style & SWT.NO_TRIM) != 0) return bits;
	if (OS.IsPPC) {
		if ((style & SWT.CLOSE) != 0) bits |= OS.WS_EX_CAPTIONOKBTN;
	}
	if ((style & SWT.RESIZE) != 0) return bits;
	if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_DLGMODALFRAME;
	return bits;
}

int widgetParent () {
	Shell shell = getShell ();
	return shell.hwndMDIClient ();
}

int widgetStyle () {
	/* 
	* Clear WS_VISIBLE and WS_TABSTOP.  NOTE: In Windows, WS_TABSTOP
	* has the same value as WS_MAXIMIZEBOX so these bits cannot be
	* used to control tabbing.
	*/
	int bits = super.widgetStyle () & ~(OS.WS_TABSTOP | OS.WS_VISIBLE);
	
	/* Set the title bits and no-trim bits */
	bits &= ~OS.WS_BORDER;
	if ((style & SWT.NO_TRIM) != 0) return bits;
	if ((style & SWT.TITLE) != 0) bits |= OS.WS_CAPTION;
	
	/* Set the min and max button bits */
	if ((style & SWT.MIN) != 0) bits |= OS.WS_MINIMIZEBOX;
	if ((style & SWT.MAX) != 0) bits |= OS.WS_MAXIMIZEBOX;
	
	/* Set the resize, dialog border or border bits */
	if ((style & SWT.RESIZE) != 0) {
		/*
		* Note on WinCE PPC.  SWT.RESIZE is used to resize
		* the Shell according to the state of the IME.
		* It does not set the WS_THICKFRAME style.
		*/
		if (!OS.IsPPC) bits |= OS.WS_THICKFRAME;	
	} else {
		if ((style & SWT.BORDER) == 0) bits |= OS.WS_BORDER;
	}

	/* Set the system menu and close box bits */
	if (!OS.IsPPC && !OS.IsSP) {
		if ((style & SWT.CLOSE) != 0) bits |= OS.WS_SYSMENU;
	}
	
	return bits;
}

int windowProc (int hwnd, int msg, int wParam, int lParam) {
	switch (msg) {
		case Display.SWT_GETACCEL:
		case Display.SWT_GETACCELCOUNT:
			if (hAccel == -1) createAccelerators ();
			return msg == Display.SWT_GETACCELCOUNT ? nAccel : hAccel;
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_ACTIVATE (int wParam, int lParam) {
	LRESULT result = super.WM_ACTIVATE (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in AWT.  When an AWT Window is activated,
	* for some reason, it seems to forward the WM_ACTIVATE
	* message to the parent.  Normally, the parent is an
	* AWT Frame.  When AWT is embedded in SWT, the SWT
	* shell gets the WM_ACTIVATE and assumes that it came
	* from Windows.  When an SWT shell is activated it
	* restores focus to the last control that had focus.
	* If this control is an embedded composite, it takes
	* focus from the AWT Window.  The fix is to ignore
	* WM_ACTIVATE messages that come from AWT Windows.
	*/
	if (OS.GetParent (lParam) == handle) {
		TCHAR buffer = new TCHAR (0, 128);
		OS.GetClassName (lParam, buffer, buffer.length ());
		String className = buffer.toString (0, buffer.strlen ());
		if (className.equals (Display.AWT_WINDOW_CLASS)) {
			return LRESULT.ZERO;
		}
	}
	if ((wParam & 0xFFFF) != 0) {
		/*
		* When the high word of wParam is non-zero, the activation
		* state of the window is being changed while the window is
		* minimized. If this is the case, do not report activation
		* events or restore the focus.
		*/
		if ((wParam >> 16) != 0) return result;
		Control control = display.findControl (lParam);
		if (control == null || control instanceof Shell) {
			if (this instanceof Shell) {
				sendEvent (SWT.Activate);
				if (isDisposed ()) return LRESULT.ZERO;
			}
		}
		if (restoreFocus ()) return LRESULT.ZERO;	
	} else {
		Display display = this.display;
		boolean lockWindow = display.isXMouseActive ();
		if (lockWindow) display.lockActiveWindow = true;
		Control control = display.findControl (lParam);
		if (control == null || control instanceof Shell) {
			if (this instanceof Shell) {
				sendEvent (SWT.Deactivate);
				if (!isDisposed ()) {
					Shell shell = getShell ();
					shell.setActiveControl (null);
					// widget could be disposed at this point
				}
			}
		}
		if (lockWindow) display.lockActiveWindow = false;
		if (isDisposed ()) return LRESULT.ZERO;
		saveFocus ();
	}
	return result;
}

LRESULT WM_CLOSE (int wParam, int lParam) {
	LRESULT result = super.WM_CLOSE (wParam, lParam);
	if (result != null) return result;
	if (isEnabled () && isActive ()) closeWidget ();
	return LRESULT.ZERO;
}

LRESULT WM_HOTKEY (int wParam, int lParam) {
	LRESULT result = super.WM_HOTKEY (wParam, lParam);
	if (result != null) return result;
	if (OS.IsSP) {
		/*
		* Feature on WinCE SP.  The Back key is either used to close
		* the foreground Dialog or used as a regular Back key in an EDIT
		* control. The article 'Back Key' in MSDN for Smartphone 
		* describes how an application should handle it.  The 
		* workaround is to override the Back key when creating
		* the menubar and handle it based on the style of the Shell.
		* If the Shell has the SWT.CLOSE style, close the Shell.
		* Otherwise, send the Back key to the window with focus.
		*/
		if (((lParam >> 16) & 0xFFFF) == OS.VK_ESCAPE) {
			if ((style & SWT.CLOSE) != 0) {
				OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
			} else {
				OS.SHSendBackToFocusWindow (OS.WM_HOTKEY, wParam, lParam);
			}
			return LRESULT.ZERO;
		}
	}
	return result;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	saveFocus ();
	return result;
}

LRESULT WM_MOVE (int wParam, int lParam) {
	if (moved) {
		Point location = getLocation ();
		if (location.x == oldX && location.y == oldY) {
			return null;
		}
		oldX = location.x;
		oldY = location.y;
	}
	return super.WM_MOVE (wParam, lParam);
}

LRESULT WM_NCACTIVATE (int wParam, int lParam) {
	LRESULT result = super.WM_NCACTIVATE (wParam, lParam);
	if (result != null) return result;
	if (wParam == 0) {
		if (display.lockActiveWindow) return LRESULT.ZERO;
		Control control = display.findControl (lParam);
		if (control != null) {
			Shell shell = getShell ();
			Decorations decorations = control.menuShell ();
			if (decorations.getShell () == shell) {
				if (this instanceof Shell) return LRESULT.ONE;
				if (display.ignoreRestoreFocus) {
					if (display.lastHittest != OS.HTCLIENT) {
						result = LRESULT.ONE;
					}
				}
			}
		}
	}
	if (!(this instanceof Shell)) {
		int hwndShell = getShell().handle;
		OS.SendMessage (hwndShell, OS.WM_NCACTIVATE, wParam, lParam);
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
	if (savedFocus != this) restoreFocus ();
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result = null;
	boolean changed = true;
	if (resized) {
		int newWidth = 0, newHeight = 0;
		switch (wParam) {
			case OS.SIZE_RESTORED:
			case OS.SIZE_MAXIMIZED:
				newWidth = lParam & 0xFFFF;
				newHeight = lParam >> 16;
				break;
			case OS.SIZE_MINIMIZED:
				Rectangle rect = getClientArea ();
				newWidth = rect.width;
				newHeight = rect.height;
				break;
		}
		changed = newWidth != oldWidth || newHeight != oldHeight;
		if (changed) {
			oldWidth = newWidth;
			oldHeight = newHeight;
		}
	}
	if (changed) {
		result = super.WM_SIZE (wParam, lParam);
		if (isDisposed ()) return result;
	}
	if (wParam == OS.SIZE_MINIMIZED) {
		sendEvent (SWT.Iconify);
		// widget could be disposed at this point
	}
	return result;
}

LRESULT WM_SYSCOMMAND (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;
	if (!(this instanceof Shell)) {
		int cmd = wParam & 0xFFF0;
		switch (cmd) {
			case OS.SC_CLOSE: {
				OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
				return LRESULT.ZERO;
			}
			case OS.SC_NEXTWINDOW: {
				traverseDecorations (true);
				return LRESULT.ZERO;
			}
		}
	}
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	if (display.lockActiveWindow) {
		WINDOWPOS lpwp = new WINDOWPOS ();
		OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
		lpwp.flags |= OS.SWP_NOZORDER;
		OS.MoveMemory (lParam, lpwp, WINDOWPOS.sizeof);
	}
	return result;
}

}
