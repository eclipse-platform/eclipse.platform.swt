package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public /*final*/ class ToolItem extends Item {
	ToolBar parent;
	Control control;
	String toolTipText;
	Image hotImage, disabledImage;

public ToolItem (ToolBar parent, int style) {
	this(parent, style, parent.getItemCount ());
}

public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle (int index) {
	int count = parent.getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int parentHandle = parent.handle;
	
	if ((style & SWT.SEPARATOR) != 0) {
		int [] args = {
//			OS.Pt_ARG_SEP_FLAGS, OS.Pt_SEP_VERTICAL, OS.Pt_SEP_VERTICAL | OS.Pt_SEP_HORIZONTAL,
//			OS.Pt_ARG_SEP_TYPE, OS.Pt_NOLINE, 0,
			OS.Pt_ARG_WIDTH, 2, 0,
			OS.Pt_ARG_RESIZE_FLAGS, OS.Pt_RESIZE_Y_ALWAYS, OS.Pt_RESIZE_XY_BITS,
		};		
		handle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	} else {
		boolean rightAligned = (parent.style & SWT.RIGHT) != 0;
		boolean toggle = (style & (SWT.CHECK | SWT.RADIO)) != 0;
		int [] args = {
			OS.Pt_ARG_BALLOON_POSITION, rightAligned ? OS.Pt_BALLOON_RIGHT : OS.Pt_BALLOON_BOTTOM, 0,
			OS.Pt_ARG_FLAGS, toggle ? OS.Pt_TOGGLE : 0, OS.Pt_TOGGLE,
		};
		handle = OS.PtCreateWidget (OS.PtButton (), parentHandle, args.length / 3, args);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index != count) {
		int i = 0;
		int child = OS.PtWidgetChildBack (parentHandle);
		/*
		* Feature in Photon.  Tool bars have an extra widget which
		* is the parent of all tool items. PtValidParent() can not be
		* used, since it does not return that widget.
		*/
		if (child != 0) child = OS.PtWidgetChildBack (child);
		while (i != index && child != 0) {
			child = OS.PtWidgetBrotherInFront (child);
			i++;
		}
		OS.PtWidgetInsert (topHandle (), child, 1);
	}
	if (OS.PtWidgetIsRealized (parentHandle)) {
		OS.PtRealizeWidget (topHandle ());
	}
}

public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (handle, area);
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
}

public Control getControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return control;
}

public Image getDisabledImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return disabledImage;
}

public Display getDisplay () {
	ToolBar parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public Image getHotImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return hotImage;
}

public ToolBar getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

public boolean getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_SET) != 0;
}

public String getToolTipText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return toolTipText;
}

public int getWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
}

public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}

int processSelection (int info) {
	if ((style & SWT.RADIO) != 0) {
		setSelection (true);		
		ToolItem [] items = parent.getItems ();
		int index = 0;
		while (index < items.length && items [index] != this) index++;
		ToolItem item;
		int i = index;
		while (--i >= 0 && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
		i = index;
		while (++i < items.length && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
	}
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	control = null;
	hotImage = null;
	disabledImage = null;
}

public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setControl (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (control != null && control.parent != parent) {
		error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	Control oldControl = this.control;
	this.control = control;
	if (oldControl != null) {
		OS.PtReParentWidget(oldControl.handle, parent.handle);
	}
	if (control != null && !control.isDisposed ()) {
		OS.PtReParentWidget(control.handle, handle);
		control.setBounds (getBounds ());
	}
}

public void setDisabledImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
}

public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setHotImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
}

public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	int imageHandle = 0;
	int type = OS.Pt_Z_STRING;
	if (image != null) {
		imageHandle = copyPhImage (image.handle);
		int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		if (args [1] != 0 && OS.strlen (args [1]) > 0) type = OS.Pt_TEXT_IMAGE;
		else type = OS.Pt_IMAGE;
	}	
	int [] args = {
		OS.Pt_ARG_LABEL_IMAGE, imageHandle, 0,
		OS.Pt_ARG_LABEL_TYPE, type, 0
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (imageHandle != 0) OS.free (imageHandle);
}

public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int [] args = {OS.Pt_ARG_FLAGS, selected ? OS.Pt_SET : 0, OS.Pt_SET};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	int ptr = 0;
	int type = OS.Pt_IMAGE;
	if (string != null) {
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		ptr = OS.malloc (buffer.length);
		OS.memmove (ptr, buffer, buffer.length);
		int [] args = {OS.Pt_ARG_LABEL_IMAGE, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		if (args [1] != 0) type = OS.Pt_TEXT_IMAGE;
		else type = OS.Pt_Z_STRING;
	}	
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr, 0,
		OS.Pt_ARG_LABEL_TYPE, type, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (ptr != 0) OS.free (ptr);
}

public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	toolTipText = string;
}

public void setWidth (int width) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	int [] args = {OS.Pt_ARG_WIDTH, width, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
}