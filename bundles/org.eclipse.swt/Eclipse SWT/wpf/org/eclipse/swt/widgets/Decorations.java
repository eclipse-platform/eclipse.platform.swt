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
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Decorations extends Canvas {
	int shellHandle;
	Image image;
//	Image image, smallImage, largeImage;
	Image [] images;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;

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
//	/*
//	* This code is intentionally commented.  On some platforms,
//	* the ON_TOP style creates a shell that will stay on top
//	* of every other shell on the desktop.  Using SetWindowPos ()
//	* with HWND_TOP caused problems on Windows 98 so this code is
//	* commented out until this functionality is specified and
//	* the problems are fixed.
//	*/
////	if ((style & SWT.ON_TOP) != 0) {
////		int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
////		OS.SetWindowPos (handle, OS.HWND_TOP, 0, 0, 0, 0, flags);
////	} else {
//		OS.BringWindowToTop (handle);
//		// widget could be disposed at this point
////	}
}

static int checkStyle (int style) {
	if ((style & SWT.NO_TRIM) != 0) {
		style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
	}
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
//	
//	/*
//	* If either WS_MINIMIZEBOX or WS_MAXIMIZEBOX are set,
//	* we must also set WS_SYSMENU or the buttons will not
//	* appear.
//	*/
//	if ((style & (SWT.MIN | SWT.MAX)) != 0) style |= SWT.CLOSE;
//	
//	/*
//	* Both WS_SYSMENU and WS_CAPTION must be set in order
//	* to for the system menu to appear.
//	*/
//	if ((style & SWT.CLOSE) != 0) style |= SWT.TITLE;
//	
//	/*
//	* Bug in Windows.  The WS_CAPTION style must be
//	* set when the window is resizable or it does not
//	* draw properly.
//	*/
//	/*
//	* This code is intentionally commented.  It seems
//	* that this problem originally in Windows 3.11,
//	* has been fixed in later versions.  Because the
//	* exact nature of the drawing problem is unknown,
//	* keep the commented code around in case it comes
//	* back.
//	*/
////	if ((style & SWT.RESIZE) != 0) style |= SWT.TITLE;
//	
	return style;
}

void checkBorder () {
	/* Do nothing */
}

void checkOpened () {
	//TODO - add back
//	if (!opened) resized = false;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void closeWidget () {
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

int compare (ImageData data1, ImageData data2) {
	int transparent1 = data1.getTransparencyType ();
	int transparent2 = data2.getTransparencyType ();
	if (transparent1 == transparent2) {
		if (data1.depth != data2.depth) {
			return data1.depth > data2.depth ? -1 : 1;
		}
		if (data1.width * data1.height != data2.width * data2.height) {
			return (data1.width * data1.height) > (data2.width * data2.height) ? -1 : 1;
		}
	}
	if (transparent1 == SWT.TRANSPARENCY_ALPHA) return -1;
	if (transparent2 == SWT.TRANSPARENCY_ALPHA) return 1;
	if (transparent1 == SWT.TRANSPARENCY_MASK) return -1;
	if (transparent2 == SWT.TRANSPARENCY_MASK) return 1;
	if (transparent1 == SWT.TRANSPARENCY_PIXEL) return -1;
	if (transparent2 == SWT.TRANSPARENCY_PIXEL) return 1;
	return 0;
}

Control computeTabGroup () {
	return this;
}

Control computeTabRoot () {
	return this;
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
//		destroyAccelerators ();
//		newDecorations.destroyAccelerators ();
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
 * @see #setDefaultButton(Button)
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
	if ((style & SWT.ON_TOP) != 0) return false;
	return OS.Window_WindowState (shellHandle) == OS.WindowState_Maximized;
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
	if ((style & SWT.ON_TOP) != 0) return false;
	return OS.Window_WindowState (shellHandle) == OS.WindowState_Minimized;
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
	checkWidget ();
	if ((style & SWT.ON_TOP) != 0) return "";
	int shellHandle = topHandle ();
	int str = OS.Window_Title (shellHandle);
	String string = createJavaString(str);
	OS.GCHandle_Free (str);
	return string;
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

void releaseChildren (boolean destroy) {
	if (menuBar != null) {
		menuBar.release (false);
		menuBar = null;
	}
	super.releaseChildren (destroy);
	if (menus != null) {
		for (int i=0; i<menus.length; i++) {
			Menu menu = menus [i];
			if (menu != null && !menu.isDisposed ()) {
				menu.dispose ();
			}
		}
		menus = null;
	}
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
	images = null;
	savedFocus = null;
//	defaultButton = saveDefault = null;
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
//	if (display.ignoreRestoreFocus) return true;
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
	if ((style & SWT.ON_TOP) != 0) return;
	int encoder = OS.gcnew_TiffBitmapEncoder ();
	int frames = OS.BitmapEncoder_Frames (encoder);
	if (image != null) {
		int frame = OS.BitmapFrame_Create (image.handle);
		OS.BitmapFrameCollection_Add (frames, frame);
		OS.GCHandle_Free (frame);
	}
	if (images != null) {
		if (images.length > 1) {
			ImageData [] datas = null;
			Image [] bestImages = new Image [images.length];
			System.arraycopy (images, 0, bestImages, 0, images.length);
			datas = new ImageData [images.length];
			for (int i=0; i<datas.length; i++) {
				datas [i] = images [i].getImageData ();
			}
			images = bestImages;
			sort (images, datas);
		}
		for (int i = 0; i < images.length; i++) {
			int frame = OS.BitmapFrame_Create (images [i].handle);
			OS.BitmapFrameCollection_Add (frames, frame);
			OS.GCHandle_Free (frame);
		}
	}
	OS.GCHandle_Free (frames);
	int stream = OS.gcnew_MemoryStream ();
	OS.BitmapEncoder_Save (encoder, stream);
	OS.GCHandle_Free (encoder);
	int decoder = OS.BitmapDecoder_Create (stream, OS.BitmapCreateOptions_None, OS.BitmapCacheOption_Default);
	OS.GCHandle_Free (stream);
	frames = OS.BitmapDecoder_Frames (decoder);
	int icon = OS.BitmapFrameCollection_default (frames, 0);
	OS.GCHandle_Free (frames);
	OS.GCHandle_Free (decoder);
	OS.Window_Icon(shellHandle, icon);
	OS.GCHandle_Free (icon);
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
	if ((style & SWT.ON_TOP) != 0) return;
	int state = maximized ? OS.WindowState_Maximized : OS.WindowState_Normal;
	OS.Window_WindowState (shellHandle, state);
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
	int children = OS.Panel_Children (scrolledHandle);
	if (menuBar != null) {
		OS.Menu_IsMainMenu (menuBar.handle, false);
		OS.UIElementCollection_Remove (children, menuBar.handle);
	}
	menuBar = menu;
	if (menuBar != null) {
		int menuHandle = menuBar.handle;
		OS.Menu_IsMainMenu (menuHandle, true);
		OS.Grid_SetRow (menuHandle, 0);
		OS.Grid_SetColumn (menuHandle, 0);
		OS.Grid_SetColumnSpan (menuHandle, 2);
		OS.UIElementCollection_Add (children, menuHandle);
	}
	OS.GCHandle_Free (children);
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
	if ((style & SWT.ON_TOP) != 0) return;
	int state = minimized ? OS.WindowState_Minimized : OS.WindowState_Normal;
	OS.Window_WindowState (shellHandle, state);
}

void setSavedFocus (Control control) {
	savedFocus = control;
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
	if ((style & SWT.ON_TOP) != 0) return;
	int strPtr = createDotNetString (string, false);
	int shellHandle = topHandle ();
	OS.Window_Title (shellHandle, strPtr);
	OS.GCHandle_Free (strPtr);
}

void sort (Image [] images, ImageData [] datas) {
	/* Shell Sort from K&R, pg 108 */
	int length = images.length;
	if (length <= 1) return;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (compare (datas [j], datas [j + gap]) >= 0) {
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
//	defaultButton.click ();
//	TODO
	defaultButton.sendEvent (SWT.Selection);
	return true;
}

}
