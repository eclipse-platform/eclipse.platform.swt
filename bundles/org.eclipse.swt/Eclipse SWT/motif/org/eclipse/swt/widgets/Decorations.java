package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/**
*	The decorations class implements a widget that
* looks and behaves like a shell, but has the same
* properties as a child widget.
*
* Styles
*
*	NO_TRIM,
*	BORDER, RESIZE,
*	TITLE, MENU, MIN, MAX
*
* Events
*
**/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/* Class Definition */
public class Decorations extends Canvas {
	String label;
	Image image;
	int dialogHandle;
	boolean minimized, maximized;
	Menu menuBar;
	Menu [] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
Decorations () {
	/* Do nothing */
}
/**
* Creates a new instance of the widget.
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
void bringToTop () {
	/*
	* Feature in X.  Calling XSetInputFocus() when the
	* widget is not viewable causes an X bad match error.
	* The fix is to call XSetInputFocus() when the widget
	* is viewable.
	*/
	if (minimized) return;
	if (!isVisible ()) return;
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	OS.XSetInputFocus (display, window, OS.RevertToParent, OS.CurrentTime);
}
static int checkStyle (int style) {
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
	return style;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Rectangle trim = super.computeTrim (x, y, width, height);
	if (menuBar != null) {
		XtWidgetGeometry request = new XtWidgetGeometry ();
		XtWidgetGeometry result = new XtWidgetGeometry ();
		request.request_mode = OS.CWWidth;
		request.width = trim.width;
		OS.XtQueryGeometry (menuBar.handle, request, result);
		trim.height += result.height;
	}
	return trim;
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	createScrolledHandle (parent.handle);
}
void createWidget (int index) {
	super.createWidget (index);
	label = "";
}
int dialogHandle () {
	if (dialogHandle != 0) return dialogHandle;
	return dialogHandle = OS.XmCreateDialogShell (handle, null, null, 0);
}
public Button getDefaultButton () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return defaultButton;
}
/**
* Gets the image.
* <p>
* @return the image
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Image getImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return image;
}
public boolean getMaximized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return maximized;
}
public Menu getMenuBar () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menuBar;
}
public boolean getMinimized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return minimized;
}
String getNameText () {
	return getText ();
}
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return label;
}
Decorations menuShell () {
	return this;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	int [] argList = {OS.XmNmenuBar, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	if (argList [1] != 0) propagateHandle (enabled, argList [1]);
}
void releaseHandle () {
	super.releaseHandle ();
	dialogHandle = 0;
}
void releaseWidget () {
	if (menus != null) {
		for (int i=0; i<menus.length; i++) {
			Menu menu = menus [i];
			if (menu != null && !menu.isDisposed ()) {
				menu.releaseWidget ();
				menu.releaseHandle ();
			}
		}
	}
	menuBar = null;
	menus = null;
	super.releaseWidget ();
	defaultButton = saveDefault = null;
	label = null;
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
public void setDefaultButton (Button button) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setDefaultButton (button, true);
}
void setDefaultButton (Button button, boolean save) {
	if (button == null) {
		if (defaultButton == saveDefault) return;
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
	if (save || saveDefault == null) saveDefault = defaultButton;
	if (saveDefault != null && saveDefault.isDisposed ()) saveDefault = null;
}
public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int pixmap = 0, mask = 0;
	if (image != null) {
		switch (image.type) {
			case SWT.BITMAP:
				pixmap = image.pixmap;
				break;
			case SWT.ICON:
				pixmap = image.pixmap;
				mask = image.mask;
				break;
			default:
				error (SWT.ERROR_INVALID_IMAGE);
		}
	}
	this.image = image;
	int [] argList = {
		OS.XmNiconPixmap, pixmap,
		OS.XmNiconMask, mask,
	};
	int topHandle = topHandle ();
	OS.XtSetValues (topHandle, argList, argList.length / 2);
}
public void setMaximized (boolean maximized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.maximized = maximized;
}
public void setMenuBar (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}

	/* Ensure the new menu bar is correctly enabled */
	if (menuBar != null) {
		if (!isEnabled () && menuBar.getEnabled ()) {
			propagateHandle (true, menuBar.handle); 
		}
	}
	if (menu != null) {
		if (!isEnabled ()) {
			propagateHandle (false, menu.handle); 
		}
	}
		
	/*
	* Bug in Motif.  When a XmMainWindowSetAreas () is used
	* to replace an existing menu, both menus must be managed
	* before the call to XmMainWindowSetAreas () or the new
	* menu will not be layed out properly.
	*/
	int newHandle = (menu != null) ? menu.handle : 0;
	int oldHandle = (menuBar != null) ? menuBar.handle : 0;
	menuBar = menu;
	int hHandle = (horizontalBar != null) ? horizontalBar.handle : 0;
	int vHandle = (verticalBar != null) ? verticalBar.handle : 0;
	if (newHandle != 0) {
		OS.XtSetMappedWhenManaged (newHandle, false);
		OS.XtManageChild (newHandle);
	}
	int clientHandle = (formHandle != 0) ? formHandle : handle;
	OS.XmMainWindowSetAreas (scrolledHandle, newHandle, 0, hHandle, vHandle, clientHandle);
	if (oldHandle != 0) OS.XtUnmanageChild (oldHandle);
	if (newHandle != 0) {
		OS.XtSetMappedWhenManaged (newHandle, true);
	}

	/*
	* Bug in Motif.  When a menu bar is removed after the
	* main window has been realized, the main window does
	* not layout the new menu bar or the work window.
	* The fix is to force a layout by temporarily resizing
	* the main window.
	*/
	if (newHandle == 0 && OS.XtIsRealized (scrolledHandle)) {
		int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		OS.XtResizeWidget (scrolledHandle, argList [1] + 1, argList [3], argList [5]);
		OS.XtResizeWidget (scrolledHandle, argList [1], argList [3], argList [5]);
	}
}
public void setMinimized (boolean minimized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.minimized = minimized;
}
void setSavedFocus (Control control) {
	if (this == control) return;
	savedFocus = control;
}
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	label = string;
}
public void setVisible (boolean visible) {
	super.setVisible (visible);
	if (!visible) return;
	if (savedFocus != null && !savedFocus.isDisposed ()) {
		savedFocus.setFocus ();
	}
	savedFocus = null;
}
}
