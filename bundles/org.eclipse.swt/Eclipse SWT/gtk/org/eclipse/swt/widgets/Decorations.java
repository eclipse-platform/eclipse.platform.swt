/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
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
	String text;
	Image image;
	Image [] images = new Image [0];
	boolean minimized, maximized;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
	int accelGroup;	
	
Decorations () {
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

static int checkStyle (int style) {
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
	return style;
}

void _setImages (Image [] images) {
	int pixbufs = 0;
	if (images != null) {
		for (int i = 0; i < images.length; i++) {
			Image icon = images [i];
			int [] w = new int [1], h = new int [1];
			OS.gdk_drawable_get_size (icon.pixmap, w, h);
			int width = w [0], height = h [0]; 	
			boolean hasMask = icon.mask != 0;
			int pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, hasMask, 8, width, height);
			if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			int colormap = OS.gdk_colormap_get_system ();
			OS.gdk_pixbuf_get_from_drawable (pixbuf, icon.pixmap, colormap, 0, 0, 0, 0, width, height);
			if (hasMask) {
				int gdkMaskImagePtr = OS.gdk_drawable_get_image (icon.mask, 0, 0, width, height);
				if (gdkMaskImagePtr == 0) SWT.error (SWT.ERROR_NO_HANDLES);
				int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
				int pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
				byte [] line = new byte [stride];
				for (int y=0; y<height; y++) {
					int offset = pixels + (y * stride);
					OS.memmove (line, offset, stride);
					for (int x=0; x<width; x++) {
						if (OS.gdk_image_get_pixel (gdkMaskImagePtr, x, y) == 0) {
							line[x*4+3] = 0;
						}
					}
					OS.memmove (offset, line, stride);
				}
				OS.g_object_unref (gdkMaskImagePtr);
			}
			pixbufs = OS.g_list_append (pixbufs, pixbuf);			
		}
	}
	int window = OS.GTK_WIDGET_WINDOW (topHandle ());
	OS.gdk_window_set_icon_list (window, pixbufs);
	int [] data = new int [1];
	int temp = pixbufs;
	while (temp != 0) {
		OS.memmove (data, temp, 4);
		OS.g_object_unref (data [0]);
		temp = OS.g_list_next (temp);
	}
	if (pixbufs != 0) OS.g_list_free (pixbufs);
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

Control computeTabGroup () {
	return this;
}

Control computeTabRoot () {
	return this;
}

void createAccelGroup () {
	if (accelGroup != 0) return;
	accelGroup = OS.gtk_accel_group_new ();
	if (accelGroup == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	//FIXME - what should we do for Decorations
	int shellHandle = topHandle ();
	OS.gtk_window_add_accel_group (shellHandle, accelGroup);
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

void destroyAccelGroup () {
	if (accelGroup == 0) return;
	int shellHandle = topHandle ();
	OS.gtk_window_remove_accel_group (shellHandle, accelGroup);
	//TEMPORARY CODE
//	OS.g_object_unref (accelGroup);
	accelGroup = 0;
}

void fixAccelGroup () {
	if (menuBar == null) return;
	destroyAccelGroup ();
	createAccelGroup ();
	menuBar.addAccelerators (accelGroup);
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
	}
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
	checkWidget();
	return defaultButton != null ? defaultButton : saveDefault;
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

public Image [] getImages () {
	checkWidget ();
	return images;
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
	checkWidget();
	return maximized;
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
	checkWidget();
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
	checkWidget();
	return minimized;
}

String getNameText () {
	return getText ();
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
	checkWidget();
	return text;
}

public boolean isReparentable () {
	checkWidget ();
	return false;
}

boolean isTabGroup () {
	return true;
}

boolean isTabItem () {
	return false;
}

Decorations menuShell () {
	return this;
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

void releaseWidget () {
	if (menuBar != null) menuBar.releaseResources ();
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
	image = null;
	images = null;
}

boolean restoreFocus () {
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	boolean restored = savedFocus != null && savedFocus.setFocus ();
	savedFocus = null;
	/*
	* This code is intentionally commented.  When no widget
	* has been given focus, some platforms give focus to the
	* default button.  Motif doesn't do this.
	*/
//	if (restored) return true;
//	if (defaultButton != null && !defaultButton.isDisposed ()) {
//		if (defaultButton.setFocus ()) return true;
//	}
//	return false;
	return restored;
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
	checkWidget();
	int buttonHandle = 0;
	if (button != null) {
		if (button.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		buttonHandle = button.handle;
	}
	saveDefault = defaultButton = button;
	OS.gtk_window_set_default (topHandle (), buttonHandle);
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
	this.image = image;
	_setImages (image != null ? new Image [] {image} : null);
}

public void setImages (Image [] images) {
	checkWidget ();
	if (images == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i = 0; i < images.length; i++) {
		if (images [i] == null || images [i].isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.images = images;
	_setImages (images);
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
	checkWidget();
	this.maximized = maximized;
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
	checkWidget();
	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	menuBar = menu;
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
	checkWidget();
	this.minimized = minimized;
}

void setSavedFocus (Control control) {
	if (this == control) return;
	savedFocus = control;
}

/**
 * Sets the receiver's text, which is the string that the
 * window manager will typically display as the receiver's
 * <em>title</em>, to the argument, which may not be null. 
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

boolean traverseItem (boolean next) {
	return false;
}

boolean traverseReturn () {
	Button button = defaultButton != null ? defaultButton: saveDefault;
	if (button == null || button.isDisposed ()) return false;
	/*
	* Bug in GTK.  When a default button that is disabled is
	* activated using the Enter key, GTK GP's.  The fix is to
	* detect this case and stop GTK from processing the Enter
	* key.
	*/
	if (!button.isVisible () || !button.isEnabled ()) return true;
	int shellHandle = _getShell ().topHandle ();
	return OS.gtk_window_activate_default (shellHandle);
}

}
