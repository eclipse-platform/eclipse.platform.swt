package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Decorations extends Canvas {
	Menu menuBar;
	Menu [] menus;
	String text = "";
	Image image;
	Button defaultButton, saveDefault;

Decorations () {
	/* Do nothing */
}

public Decorations (Composite parent, int style) {
	super (parent, style);
}

static int checkStyle (int style) {
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
	return style;
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
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Button getDefaultButton () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return defaultButton;
}

public Image getImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return image;
}

public boolean getMaximized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public boolean getMinimized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public Menu getMenuBar () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menuBar;
}

String getNameText () {
	return getText ();
}

public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return text;
}

boolean hasBorder () {
	return false;
}

Decorations menuShell () {
	return this;
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
	image = null;
	super.releaseWidget ();
	defaultButton = saveDefault = null;
	text = null;
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

void resizeBounds (int width, int height) {
	int menuHeight = 0;
	if (menuBar != null) {
		PhDim_t dim = new PhDim_t ();
		int menuHandle = menuBar.handle;
		if (!OS.PtWidgetIsRealized (menuHandle)) {
			OS.PtExtentWidgetFamily (menuHandle);
		}
		OS.PtWidgetPreferredSize (menuHandle, dim);
		menuHeight = dim.h;
		int [] args = {OS.Pt_ARG_HEIGHT, menuHeight, 0};
		OS.PtSetResources (menuHandle, args.length / 3, args);
		height = height - menuHeight;
	}
	PhArea_t area = new PhArea_t ();
	area.pos_y = (short) menuHeight;
	area.size_w = (short) Math.max (width, 0);
	area.size_h = (short) Math.max (height ,0);
	int ptr = OS.malloc (PhArea_t.sizeof);
	OS.memmove (ptr, area, PhArea_t.sizeof);
	int [] args = new int [] {OS.Pt_ARG_AREA, ptr, 0};
	OS.PtSetResources (scrolledHandle, args.length / 3, args);
	OS.free (ptr);
	resizeClientArea (width, height);
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
	this.image = image;
}

public void setMaximized (boolean maximized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
 
public void setMenuBar (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE
}

public void setMinimized (boolean minimized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

}
