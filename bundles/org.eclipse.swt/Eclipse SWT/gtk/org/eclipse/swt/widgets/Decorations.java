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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	String text;
	Image image;
	Image [] images = new Image [0];
	boolean minimized, maximized;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
	long accelGroup, vboxHandle;

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

static int checkStyle (int style) {
	if ((style & SWT.NO_TRIM) != 0) {
		style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
	} else if ((style & SWT.NO_MOVE) != 0) {
		style |= SWT.TITLE;
	}
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
	return style;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void _setImages (Image [] images) {
	if (images != null && images.length > 1) {
		Image [] bestImages = new Image [images.length];
		System.arraycopy (images, 0, bestImages, 0, images.length);
		sort (bestImages);
		images = bestImages;
	}

	// Allocate GList of icons
	long icon_list = 0;
	if (images != null) {
		for (Image image : images) {
			long pixbuf = ImageList.createPixbuf(image);
			if (GTK.GTK4) {
				long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
				OS.g_object_unref(pixbuf);
				icon_list = OS.g_list_append(icon_list, texture);
			} else {
				icon_list = OS.g_list_append(icon_list, pixbuf);
			}
		}
	}

	if (GTK.GTK4) {
		/*
		 * Set texture list to window's surface. Can no longer
		 * use GtkWindow functions as they require a themed icon.
		 */
		long surface = GTK4.gtk_native_get_surface(topHandle());
		GTK4.gdk_toplevel_set_icon_list(surface, icon_list);
	} else {
		GTK3.gtk_window_set_icon_list(topHandle(), icon_list);
	}

	// Release GList
	long temp = icon_list;
	while (temp != 0) {
		OS.g_object_unref(OS.g_list_data(temp));
		temp = OS.g_list_next(temp);
	}
	if (icon_list != 0) OS.g_list_free(icon_list);
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

int compare (ImageData data1, ImageData data2) {
	if (data1.width == data2.width && data1.height == data2.height) {
		int transparent1 = data1.getTransparencyType ();
		int transparent2 = data2.getTransparencyType ();
		if (transparent1 == SWT.TRANSPARENCY_ALPHA) return -1;
		if (transparent2 == SWT.TRANSPARENCY_ALPHA) return 1;
		if (transparent1 == SWT.TRANSPARENCY_MASK) return -1;
		if (transparent2 == SWT.TRANSPARENCY_MASK) return 1;
		if (transparent1 == SWT.TRANSPARENCY_PIXEL) return -1;
		if (transparent2 == SWT.TRANSPARENCY_PIXEL) return 1;
		return 0;
	}
	return data1.width > data2.width || data1.height > data2.height ? -1 : 1;
}

@Override
Widget computeTabGroup () {
	return this;
}

@Override
Control computeTabRoot () {
	return this;
}

void createAccelGroup () {
	if (accelGroup != 0) return;
	accelGroup = GTK.gtk_accel_group_new ();
	if (accelGroup == 0) error (SWT.ERROR_NO_HANDLES);
	//FIXME - what should we do for Decorations
	long shellHandle = topHandle ();
	GTK3.gtk_window_add_accel_group (shellHandle, accelGroup);
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

void destroyAccelGroup () {
	if (accelGroup == 0) return;
	if (menuBar != null) menuBar.removeAccelerators(accelGroup);
	long shellHandle = topHandle ();
	GTK3.gtk_window_remove_accel_group (shellHandle, accelGroup);
	OS.g_object_unref (accelGroup);
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
 * @see #setDefaultButton(Button)
 */
public Button getDefaultButton () {
	checkWidget();
	Button button = defaultButton != null ? defaultButton : saveDefault;
	if (button != null && button.isDisposed ()) return null;
	return button;
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

@Override
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

@Override
public boolean isReparentable () {
	checkWidget ();
	return false;
}

@Override
boolean isTabGroup () {
	return true;
}

@Override
boolean isTabItem () {
	return false;
}

@Override
Decorations menuShell () {
	return this;
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

@Override
void releaseHandle () {
	super.releaseHandle ();
	vboxHandle = 0;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	image = null;
	images = null;
	savedFocus = null;
	defaultButton = saveDefault = null;
}

@Override
void reskinChildren (int flags) {
	if (menuBar != null) menuBar.reskin (flags);
	if (menus != null) {
		for (int i=0; i<menus.length; i++) {
			Menu menu = menus [i];
			if (menu != null) menu.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

boolean restoreFocus () {
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	boolean restored = savedFocus != null && savedFocus.setFocus ();
	savedFocus = null;
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
	checkWidget();
	long buttonHandle = 0;
	if (saveDefault != null && !saveDefault.isDisposed ()  ) {
		long saveButtonHandle = saveDefault.handle;
		if (saveButtonHandle != 0) {
			long context = GTK.gtk_widget_get_style_context (saveButtonHandle);
			GTK.gtk_style_context_remove_class(context, GTK.GTK_STYLE_CLASS_SUGGESTED_ACTION);
		}
	}
	if (button != null) {
		if (button.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (button.menuShell () != this) error (SWT.ERROR_INVALID_PARENT);
		buttonHandle = button.handle;
	}
	saveDefault = defaultButton = button;
	if (buttonHandle != 0) {
		long context = GTK.gtk_widget_get_style_context (buttonHandle);
		GTK.gtk_style_context_add_class(context, GTK.GTK_STYLE_CLASS_SUGGESTED_ACTION);
	}

	if (GTK.GTK4) {
		GTK4.gtk_window_set_default_widget (topHandle(), buttonHandle);
	} else {
		GTK3.gtk_window_set_default (topHandle (), buttonHandle);
	}
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
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	_setImages (image != null ? new Image [] {image} : null);
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
	checkWidget();
	this.minimized = minimized;
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if (!create) {
		if (menuBar != null) menuBar._setOrientation (style & (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT));
	}
}

void setSavedFocus (Control control) {
	if (this == control) return;
	savedFocus = control;
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

void sort (Image [] images) {
	/* Shell Sort from K&R, pg 108 */
	int length = images.length;
	if (length <= 1) return;
	ImageData [] datas = new ImageData [length];
	for (int i = 0; i < length; i++) {
		datas [i] = images [i].getImageData ();
	}
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

@Override
boolean traverseItem (boolean next) {
	return false;
}

@Override
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
	long shellHandle = _getShell ().topHandle ();

	if (GTK.GTK4) {
		long defaultWidget = GTK.gtk_window_get_default_widget(shellHandle);
		return GTK.gtk_widget_activate(defaultWidget);
	} else {
		return GTK3.gtk_window_activate_default (shellHandle);
	}
}

}
