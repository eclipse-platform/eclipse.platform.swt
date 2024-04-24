/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 * </p>
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
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Decorations extends Canvas {
	Image image, smallImage, largeImage;
	Image [] images;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
	int swFlags, nAccel;
	long hAccel;
	boolean moved, resized, opened;
	int oldX = OS.CW_USEDEFAULT, oldY = OS.CW_USEDEFAULT;
	int oldWidth = OS.CW_USEDEFAULT, oldHeight = OS.CW_USEDEFAULT;
	RECT maxRect = new RECT();

	static {
		DPIZoomChangeRegistry.registerHandler(Decorations::handleDPIChange, Decorations.class);
	}

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
 * @see SWT#NO_MOVE
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

void _setMaximized (boolean maximized) {
	swFlags = maximized ? OS.SW_SHOWMAXIMIZED : OS.SW_RESTORE;
	if (!OS.IsWindowVisible (handle)) return;
	if (maximized == OS.IsZoomed (handle)) return;
	OS.ShowWindow (handle, swFlags);
	OS.UpdateWindow (handle);
}

void _setMinimized (boolean minimized) {
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
	} else if ((style & SWT.NO_MOVE) != 0) {
		style |= SWT.TITLE;
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

	return style;
}

@Override
void checkBorder () {
	/* Do nothing */
}

void checkComposited (Composite parent) {
	/* Do nothing */
}

@Override
void checkOpened () {
	if (!opened) resized = false;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
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
		if (transparent1 == SWT.TRANSPARENCY_ALPHA) return -1;
		if (transparent2 == SWT.TRANSPARENCY_ALPHA) return 1;
		if (transparent1 == SWT.TRANSPARENCY_MASK) return -1;
		if (transparent2 == SWT.TRANSPARENCY_MASK) return 1;
		if (transparent1 == SWT.TRANSPARENCY_PIXEL) return -1;
		if (transparent2 == SWT.TRANSPARENCY_PIXEL) return 1;
		return 0;
	}
	return value1 < value2 ? -1 : 1;
}

@Override
Widget computeTabGroup () {
	return this;
}

@Override
Control computeTabRoot () {
	return this;
}

@Override Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget ();

	/* Get the size of the trimmings */
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	boolean hasMenu = OS.GetMenu (handle) != 0;
	OS.AdjustWindowRectEx (rect, bits1, hasMenu, bits2);

	/* Get the size of the scroll bars */
	if (horizontalBar != null) rect.bottom += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	if (verticalBar != null) rect.right += OS.GetSystemMetrics (OS.SM_CXVSCROLL);

	/* Compute the height of the menu bar */
	if (hasMenu) {
		RECT testRect = new RECT ();
		OS.SetRect (testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
		OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		while ((testRect.bottom - testRect.top) < height) {
			if (testRect.bottom - testRect.top == 0) break;
			rect.top -= OS.GetSystemMetrics (OS.SM_CYMENU) - OS.GetSystemMetrics (OS.SM_CYBORDER);
			OS.SetRect (testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
			OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		}
	}
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

void createAccelerators () {
	hAccel = nAccel = 0;
	MenuItem [] items = display.items;
	if (menuBar == null || items == null) {
		return;
	}
	ACCEL accel = new ACCEL ();
	byte [] buffer1 = new byte [ACCEL.sizeof];
	byte [] buffer2 = new byte [items.length * ACCEL.sizeof];
	for (MenuItem item : items) {
		if (item != null && item.accelerator != 0) {
			Menu menu = item.parent;
			if (menu.parent == this) {
				while (menu != null && menu != menuBar) {
					menu = menu.getParentMenu ();
				}
				if (menu == menuBar && item.fillAccel (accel)) {
					OS.MoveMemory (buffer1, accel, ACCEL.sizeof);
					System.arraycopy (buffer1, 0, buffer2, nAccel * ACCEL.sizeof, ACCEL.sizeof);
					nAccel++;
				}
			}
		}
	}
	if (nAccel != 0) hAccel = OS.CreateAcceleratorTable (buffer2, nAccel);
}

@Override
void createHandle () {
	super.createHandle ();
	if (parent != null || ((style & SWT.TOOL) != 0)) {
		setParent ();
		setSystemMenu ();
	}
}

@Override
void createWidget () {
	super.createWidget ();
	swFlags = OS.SW_SHOWNOACTIVATE;
	hAccel = -1;
}

void destroyAccelerators () {
	if (hAccel != 0 && hAccel != -1) OS.DestroyAcceleratorTable (hAccel);
	hAccel = -1;
}

@Override
public void dispose () {
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!(this instanceof Shell)) {
		if (!traverseDecorations (true)) {
			Shell shell = getShell ();
			shell.setFocus ();
		}
		setVisible (false);
	}
	super.dispose ();
}

Menu findMenu (long hMenu) {
	if (menus == null) return null;
	for (Menu menu : menus) {
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

@Override Rectangle getBoundsInPixels () {
	checkWidget ();
	if (OS.IsIconic (handle)) {
		WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
		lpwndpl.length = WINDOWPLACEMENT.sizeof;
		OS.GetWindowPlacement (handle, lpwndpl);
		if ((lpwndpl.flags & OS.WPF_RESTORETOMAXIMIZED) != 0) {
			int width = maxRect.right - maxRect.left;
			int height = maxRect.bottom - maxRect.top;
			return new Rectangle (maxRect.left, maxRect.top, width, height);
		}
		int width = lpwndpl.right - lpwndpl.left;
		int height = lpwndpl.bottom - lpwndpl.top;
		return new Rectangle (lpwndpl.left, lpwndpl.top, width, height);
	}
	return super.getBoundsInPixels ();
}

@Override Rectangle getClientAreaInPixels () {
	checkWidget ();
	if (OS.IsIconic (handle)) {
		WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
		lpwndpl.length = WINDOWPLACEMENT.sizeof;
		OS.GetWindowPlacement (handle, lpwndpl);
		if ((lpwndpl.flags & OS.WPF_RESTORETOMAXIMIZED) != 0) {
			return new Rectangle (0, 0, oldWidth, oldHeight);
		}
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
		boolean hasMenu = OS.GetMenu (handle) != 0;
		OS.AdjustWindowRectEx (rect, bits1, hasMenu, bits2);
		width = Math.max (0, width - (rect.right - rect.left));
		height = Math.max (0, height - (rect.bottom - rect.top));
		return new Rectangle (0, 0, width, height);
	}
	return super.getClientAreaInPixels ();
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
 * @see #setDefaultButton(Button)
 */
public Button getDefaultButton () {
	checkWidget ();
	if (defaultButton != null && defaultButton.isDisposed ()) return null;
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

@Override Point getLocationInPixels () {
	checkWidget ();
	if (OS.IsIconic (handle)) {
		WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
		lpwndpl.length = WINDOWPLACEMENT.sizeof;
		OS.GetWindowPlacement (handle, lpwndpl);
		if ((lpwndpl.flags & OS.WPF_RESTORETOMAXIMIZED) != 0) {
			return new Point (maxRect.left, maxRect.top);
		}
		return new Point (lpwndpl.left, lpwndpl.top);
	}
	return super.getLocationInPixels ();
}

/**
 * Returns <code>true</code> if the receiver is currently
 * maximized, and false otherwise.
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
	if (OS.IsWindowVisible (handle)) return OS.IsIconic (handle);
	return swFlags == OS.SW_SHOWMINNOACTIVE;
}

@Override
String getNameText () {
	return getText ();
}

@Override Point getSizeInPixels () {
	checkWidget ();
	if (OS.IsIconic (handle)) {
		WINDOWPLACEMENT lpwndpl = new WINDOWPLACEMENT ();
		lpwndpl.length = WINDOWPLACEMENT.sizeof;
		OS.GetWindowPlacement (handle, lpwndpl);
		if ((lpwndpl.flags & OS.WPF_RESTORETOMAXIMIZED) != 0) {
			int width = maxRect.right - maxRect.left;
			int height = maxRect.bottom - maxRect.top;
			return new Point (width, height);
		}
		int width = lpwndpl.right - lpwndpl.left;
		int height = lpwndpl.bottom - lpwndpl.top;
		return new Point (width, height);
	}
	return super.getSizeInPixels ();
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
	char [] buffer = new char [length + 1];
	OS.GetWindowText (handle, buffer, length + 1);
	return new String (buffer, 0, length);
}

@Override
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

@Override
boolean isTabGroup () {
	/*
	* Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
	*/
	return true;
}

@Override
boolean isTabItem () {
	/*
	* Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
	*/
	return false;
}

@Override
Decorations menuShell () {
	return this;
}

@Override
void releaseChildren (boolean destroy) {
	if (menuBar != null) {
		menuBar.release (false);
		menuBar = null;
	}
	super.releaseChildren (destroy);
	if (menus != null) {
		for (Menu menu : menus) {
			if (menu != null && !menu.isDisposed ()) {
				menu.dispose ();
			}
		}
		menus = null;
	}
}

@Override
void releaseWidget () {
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

@Override
void reskinChildren (int flags) {
	if (menuBar != null) menuBar.reskin (flags);
	if (menus != null) {
		for (Menu menu : menus) {
			if (menu != null) menu.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

boolean restoreFocus () {
	if (display.ignoreRestoreFocus) return true;
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	if (savedFocus != null && savedFocus.setFocus ()) return true;
	return false;
}

void saveFocus () {
	Control control = display._getFocusControl ();
	if (control != null && control != this && this == control.menuShell ()) {
		setSavedFocus (control);
	}
}

@Override
void setBoundsInPixels (int x, int y, int width, int height, int flags, boolean defer) {
	swFlags = OS.SW_SHOWNOACTIVATE;
	if (OS.IsIconic (handle)) {
		setPlacement (x, y, width, height, flags);
		return;
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
	if (OS.IsZoomed (handle)) {
		if (sameOrigin && sameExtent) return;
		setPlacement (x, y, width, height, flags);
		_setMaximized (false);
		return;
	}
	super.setBoundsInPixels (x, y, width, height, flags, defer);
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
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultButton (Button button) {
	checkWidget ();
	if (button != null) {
		if (button.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (button.menuShell () != this) error(SWT.ERROR_INVALID_PARENT);
	}
	setDefaultButton (button, true);
}

void setDefaultButton (Button button, boolean save) {
	if (button == null) {
		if (defaultButton == saveDefault) {
			if (save) saveDefault = null;
			return;
		}
	} else {
		if ((button.style & SWT.PUSH) == 0) return;
		if (button == defaultButton) {
			if (save) saveDefault = defaultButton;
			return;
		}
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
	if (smallImage != null) smallImage.dispose ();
	if (largeImage != null) largeImage.dispose ();
	smallImage = largeImage = null;
	long hSmallIcon = 0, hLargeIcon = 0;
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
					datas [i] = images [i].getImageData (getZoom());
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
				smallImage = Display.createIcon (smallIcon);
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
				largeImage = Display.createIcon (largeIcon);
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
	if (hSmallIcon == 0 && hLargeIcon == 0 && (style & SWT.BORDER) != 0) {
		int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE;
		OS.RedrawWindow (handle, null, 0, flags);
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
	for (Image image : images) {
		if (image == null || image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
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
	Display.lpStartupInfo = null;
	_setMaximized (maximized);
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
	if (menu != null) display.removeBar (menu);
	menuBar = menu;
	long hMenu = menuBar != null ? menuBar.handle: 0;
	OS.SetMenu (handle, hMenu);
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
 * @param minimized the new minimized state
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
	Display.lpStartupInfo = null;
	_setMinimized (minimized);
}

@Override
public void setOrientation (int orientation) {
	super.setOrientation (orientation);
	if (menus != null) {
		for (Menu menu : menus) {
			if (menu != null && !menu.isDisposed () && (menu.getStyle () & SWT.POP_UP) != 0) {
				menu._setOrientation (menu.getOrientation ());
			}
		}
	}
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
	long hwndParent = parent.handle;
	display.lockActiveWindow = true;
	OS.SetParent (handle, hwndParent);
	if (!OS.IsWindowVisible (hwndParent)) {
		OS.ShowWindow (handle, OS.SW_SHOWNA);
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	bits &= ~OS.WS_CHILD;
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits | OS.WS_POPUP);
	OS.SetWindowLongPtr (handle, OS.GWLP_ID, 0);
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
	OS.SetWindowPos (handle, OS.HWND_BOTTOM, 0, 0, 0, 0, flags);
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
			Point location = getLocationInPixels ();
			oldX = location.x;
			oldY = location.y;
			sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
		if (sameExtent) {
			resized = true;
			Rectangle rect = getClientAreaInPixels ();
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
	long hMenu = OS.GetSystemMenu (handle, false);
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
 * <p>
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
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	TCHAR buffer = new TCHAR (0, string, true);
	/* Ensure that the title appears in the task bar.*/
	if ((state & FOREIGN_HANDLE) != 0) {
		long hHeap = OS.GetProcessHeap ();
		int byteCount = buffer.length () * TCHAR.sizeof;
		long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (pszText, buffer, byteCount);
		OS.DefWindowProc (handle, OS.WM_SETTEXT, 0, pszText);
		if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	} else {
		OS.SetWindowText (handle, buffer);
	}
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		updateTextDirection (AUTO_TEXT_DIRECTION);
	}
}

@Override
public void setVisible (boolean visible) {
	checkWidget ();
	if (!getDrawing()) {
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
		if (!getDrawing()) {
			state &= ~HIDDEN;
		} else {
			if (menuBar != null) {
				display.removeBar (menuBar);
				OS.DrawMenuBar (handle);
			}
			STARTUPINFO lpStartUpInfo = Display.lpStartupInfo;
			if (lpStartUpInfo != null && (lpStartUpInfo.dwFlags & OS.STARTF_USESHOWWINDOW) != 0) {
				OS.ShowWindow (handle, lpStartUpInfo.wShowWindow);
			} else {
				OS.ShowWindow (handle, swFlags);
			}
			if (isDisposed ()) return;
			opened = true;
			if (!moved) {
				moved = true;
				Point location = getLocationInPixels ();
				oldX = location.x;
				oldY = location.y;
			}
			if (!resized) {
				resized = true;
				Rectangle rect = getClientAreaInPixels ();
				oldWidth = rect.width;
				oldHeight = rect.height;
			}
			/*
			* Bug in Windows.  On Vista using the Classic theme,
			* when the window is hung and UpdateWindow() is called,
			* nothing is drawn, and outstanding WM_PAINTs are cleared.
			* This causes pixel corruption.  The fix is to avoid calling
			* update on hung windows.
			*/
			if (OS.IsAppThemed () || !OS.IsHungAppWindow (handle)) {
				OS.UpdateWindow (handle);
			}
		}
	} else {
		if (OS.IsIconic (handle)) {
			swFlags = OS.SW_SHOWMINNOACTIVE;
		} else {
			if (OS.IsZoomed (handle)) {
				swFlags = OS.SW_SHOWMAXIMIZED;
			} else {
				swFlags = OS.SW_SHOWNOACTIVATE;
			}
		}
		if (!getDrawing()) {
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

@Override
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
		long hwndMDIClient = shell.hwndMDIClient;
		if (hwndMDIClient != 0 && OS.TranslateMDISysAccel (hwndMDIClient, msg)) {
			return true;
		}
		if (msg.message == OS.WM_KEYDOWN) {
			if (OS.GetKeyState (OS.VK_CONTROL) >= 0) return false;
			switch ((int)(msg.wParam)) {
				case OS.VK_F4:
					OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
					return true;
				case OS.VK_F6:
					if (traverseDecorations (true)) return true;
			}
			return false;
		}
		if (msg.message == OS.WM_SYSKEYDOWN) {
			switch ((int)(msg.wParam)) {
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

@Override
boolean traverseItem (boolean next) {
	return false;
}

@Override
boolean traverseReturn () {
	if (defaultButton == null || defaultButton.isDisposed ()) return false;
	if (!defaultButton.isVisible () || !defaultButton.isEnabled ()) return false;
	defaultButton.click ();
	return true;
}

@Override
CREATESTRUCT widgetCreateStruct () {
	return new CREATESTRUCT ();
}

@Override
int widgetExtStyle () {
	int bits = super.widgetExtStyle () | OS.WS_EX_MDICHILD;
	bits &= ~OS.WS_EX_CLIENTEDGE;
	if ((style & SWT.NO_TRIM) != 0) return bits;
	if ((style & SWT.RESIZE) != 0) return bits;
	if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_DLGMODALFRAME;
	return bits;
}

@Override
long widgetParent () {
	Shell shell = getShell ();
	return shell.hwndMDIClient ();
}

@Override
int widgetStyle () {
	/*
	* Clear WS_VISIBLE and WS_TABSTOP.  NOTE: In Windows, WS_TABSTOP
	* has the same value as WS_MAXIMIZEBOX so these bits cannot be
	* used to control tabbing.
	*/
	int bits = super.widgetStyle () & ~(OS.WS_TABSTOP | OS.WS_VISIBLE);

	/* Set the title bits and no-trim bits */
	bits &= ~OS.WS_BORDER;
	if ((style & SWT.NO_TRIM) != 0) {
		if (parent == null) {
			bits |= OS.WS_SYSMENU | OS.WS_MINIMIZEBOX;
		}
		return bits;
	}
	if ((style & SWT.TITLE) != 0) bits |= OS.WS_CAPTION;

	/* Set the min and max button bits */
	if ((style & SWT.MIN) != 0) bits |= OS.WS_MINIMIZEBOX;
	if ((style & SWT.MAX) != 0) bits |= OS.WS_MAXIMIZEBOX;

	/* Set the resize, dialog border or border bits */
	if ((style & SWT.RESIZE) != 0) {
		bits |= OS.WS_THICKFRAME;
	} else if ((style & SWT.BORDER) == 0) {
		bits |= OS.WS_BORDER;
	}

	/* Set the system menu and close box bits */
	if ((style & SWT.CLOSE) != 0) bits |= OS.WS_SYSMENU;

	return bits;
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
	switch (msg) {
		case Display.SWT_GETACCEL:
		case Display.SWT_GETACCELCOUNT:
			if (hAccel == -1) createAccelerators ();
			return msg == Display.SWT_GETACCELCOUNT ? nAccel : hAccel;
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

@Override
LRESULT WM_ACTIVATE (long wParam, long lParam) {
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
		char [] buffer = new char [128];
		int length = OS.GetClassName (lParam, buffer, buffer.length);
		String className = new String (buffer, 0, length);
		if (className.equals (Display.AWT_WINDOW_CLASS)) {
			return LRESULT.ZERO;
		}
	}
	int loWord = OS.LOWORD (wParam);
	if (loWord != 0) {
		/*
		* When the high word of wParam is non-zero, the activation
		* state of the window is being changed while the window is
		* minimized. If this is the case, do not report activation
		* events or restore the focus.
		*/
		if (OS.HIWORD (wParam) != 0) return result;
		Control control = display.findControl (lParam);
		if (control == null || control instanceof Shell) {
			if (this instanceof Shell) {
				Event event = new Event ();
				event.detail = loWord == OS.WA_CLICKACTIVE ? SWT.MouseDown : SWT.None;
				sendEvent (SWT.Activate, event);
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

@Override
LRESULT WM_CLOSE (long wParam, long lParam) {
	LRESULT result = super.WM_CLOSE (wParam, lParam);
	if (result != null) return result;
	if (isEnabled () && isActive ()) closeWidget ();
	return LRESULT.ZERO;
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	saveFocus ();
	return result;
}

@Override
LRESULT WM_MOVE (long wParam, long lParam) {
	if (moved) {
		Point location = getLocationInPixels ();
		if (location.x == oldX && location.y == oldY) {
			return null;
		}
		oldX = location.x;
		oldY = location.y;
	}
	return super.WM_MOVE (wParam, lParam);
}

@Override
LRESULT WM_NCACTIVATE (long wParam, long lParam) {
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
		long hwndShell = getShell().handle;
		OS.SendMessage (hwndShell, OS.WM_NCACTIVATE, wParam, lParam);
	}
	return result;
}

@Override
LRESULT WM_QUERYOPEN (long wParam, long lParam) {
	LRESULT result = super.WM_QUERYOPEN (wParam, lParam);
	if (result != null) return result;
	sendEvent (SWT.Deiconify);
	// widget could be disposed at this point
	return result;
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	if (isDisposed ()) return result;
	if (savedFocus != this) restoreFocus ();
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = null;
	boolean changed = true;
	if (resized) {
		int newWidth = 0, newHeight = 0;
		switch ((int)wParam) {
			case OS.SIZE_MAXIMIZED:
				OS.GetWindowRect (handle, maxRect);
			case OS.SIZE_RESTORED:
				newWidth = OS.LOWORD (lParam);
				newHeight = OS.HIWORD (lParam);
				break;
			case OS.SIZE_MINIMIZED:
				Rectangle rect = getClientAreaInPixels ();
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

@Override
LRESULT WM_SYSCOMMAND (long wParam, long lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;
	if (!(this instanceof Shell)) {
		int cmd = (int)wParam & 0xFFF0;
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

@Override
LRESULT WM_WINDOWPOSCHANGING (long wParam, long lParam) {
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

private static void handleDPIChange(Widget widget, int newZoom, float scalingFactor) {
	if (!(widget instanceof Decorations decorations)) {
		return;
	}

	Image image = decorations.getImage();
	if (image != null) {
		decorations.setImage(Image.win32_new(image, newZoom));
	}

	Image[] images = decorations.getImages();
	if (images != null) {
		for(Image subImage : images) {
			if (subImage != null) {
				Image.win32_new(subImage, newZoom);
			}
		}
		decorations.setImages(images);
	}

	Menu menuBar = decorations.getMenuBar();
	if (menuBar != null) {
		DPIZoomChangeRegistry.applyChange(menuBar, newZoom, scalingFactor);
	}
}
}
