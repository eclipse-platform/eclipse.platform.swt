package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public /*final*/ class Button extends Control {
	Image image;

public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & SWT.PUSH) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

public void addSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void click () {
	int rid = OS.PtWidgetRid (handle);
	if (rid == 0) return;
	PhEvent_t event = new PhEvent_t ();
	event.emitter_rid = rid;
	event.emitter_handle = handle;
	event.collector_rid = rid;
	event.collector_handle = handle;
	event.flags = OS.Ph_EVENT_DIRECT;
	event.processing_flags = OS.Ph_FAKE_EVENT;
	event.type = OS.Ph_EV_BUT_PRESS;
	event.num_rects = 1;
	PhPointerEvent_t pe = new PhPointerEvent_t ();
	pe.click_count = 1;
	pe.buttons = OS.Ph_BUTTON_SELECT;
	PhRect_t rect = new PhRect_t ();
	int ptr = OS.malloc (PhEvent_t.sizeof + PhPointerEvent_t.sizeof + PhRect_t.sizeof);
	OS.memmove (ptr, event, PhEvent_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof,  rect, PhRect_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof + PhRect_t.sizeof, pe, PhPointerEvent_t.sizeof);
	OS.PtSendEventToWidget (handle, ptr);	
	OS.PtFlush ();
	event.type = OS.Ph_EV_BUT_RELEASE;
	event.subtype = OS.Ph_EV_RELEASE_REAL;
	OS.memmove (ptr, event, PhEvent_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof,  rect, PhRect_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof + PhRect_t.sizeof, pe, PhPointerEvent_t.sizeof);
	OS.PtSendEventToWidget (handle, ptr);	
	OS.free (ptr);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.ARROW) != 0) {
		if (wHint != SWT.DEFAULT) width += wHint;
		else width = 17;
		if (hHint != SWT.DEFAULT) height += hHint;
		else height = 17;
		return new Point (width, height);
	}
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	width = dim.w; height = dim.h;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		int [] args = {
			OS.Pt_ARG_MARGIN_WIDTH, 0, 0,	// 1
			OS.Pt_ARG_MARGIN_HEIGHT, 0, 0,	// 4
			OS.Pt_ARG_MARGIN_LEFT, 0, 0,	// 7
			OS.Pt_ARG_MARGIN_RIGHT, 0, 0,	// 10
			OS.Pt_ARG_MARGIN_TOP, 0, 0,		// 13
			OS.Pt_ARG_MARGIN_BOTTOM, 0, 0,	// 16
		};
		OS.PtGetResources (handle, args.length / 3, args);
		PhRect_t rect = new PhRect_t ();
		PhArea_t area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) {
			width = area.size_w + (args [1] * 2) + args [7] + args [10];
		}
		if (hHint != SWT.DEFAULT) {
			height = area.size_h + (args [4] * 2) + args [13] + args [16];
		}
	}
	return new Point (width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int parentHandle = parent.handle;
		
	/* ARROW button */
	if ((style & SWT.ARROW) != 0) {
		int [] args = {
			OS.Pt_ARG_BASIC_FLAGS, OS.Pt_HORIZONTAL_GRADIENT, OS.Pt_STATIC_GRADIENT | OS.Pt_HORIZONTAL_GRADIENT,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		handle = OS.PtCreateWidget (display.PtButton, parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}

	/* Compute alignment */
	int alignment = OS.Pt_LEFT;
	if ((style & SWT.CENTER) != 0) alignment = OS.Pt_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.Pt_RIGHT;
	
	/* CHECK or RADIO button */
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int [] args = {
			OS.Pt_ARG_HORIZONTAL_ALIGNMENT, alignment, 0,
			OS.Pt_ARG_INDICATOR_TYPE, (style & SWT.CHECK) != 0 ? OS.Pt_N_OF_MANY : OS.Pt_ONE_OF_MANY, 0,
			OS.Pt_ARG_FILL_COLOR, display.WIDGET_BACKGROUND, 0,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};	

		handle = OS.PtCreateWidget (display.PtToggleButton, parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	
	/* PUSH or TOGGLE button */
	int [] args = {
		OS.Pt_ARG_FLAGS, (style & SWT.TOGGLE) != 0 ? OS.Pt_TOGGLE : 0, OS.Pt_TOGGLE,
		OS.Pt_ARG_HORIZONTAL_ALIGNMENT, alignment, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (display.PtButton, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

public int getAlignment () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

public Image getImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return image;
}

public boolean getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_SET) != 0;
}

String getNameText () {
	return getText ();
}

public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) return "";
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, 0, 0,
		OS.Pt_ARG_ACCEL_KEY, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] result = Converter.mbcsToWcs (null, buffer);
	int count = 0;
	int mnemonic = 0;
	if (args [4] != 0) {
		int length2 = OS.strlen (args [4]);
		if (length2 > 0) {
			byte [] buffer2 = new byte [length2];
			OS.memmove (buffer2, args [4], length2);
			char [] result2 = Converter.mbcsToWcs (null, buffer2);
			if (result2.length > 0) mnemonic = result2 [0];
		}
	}
	if (mnemonic != 0) count++;
	for (int i=0; i<result.length-1; i++)
		if (result [i] == Mnemonic) count++;
	char [] newResult = result;
	if ((count != 0) || (mnemonic != 0)) {
		newResult = new char [result.length + count];
		int i = 0, j = 0;
		while (i < result.length) {
			if ((mnemonic != 0) && (result [i] == mnemonic)) {
				if (j < newResult.length) newResult [j++] = Mnemonic;
				mnemonic = 0;
			}
			if ((newResult [j++] = result [i++]) == Mnemonic)
				if (j < newResult.length) newResult [j++] = Mnemonic;
		}
	}
	return new String (newResult);
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
}

int processActivate (int info) {
	if (setFocus ()) click ();
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	if ((style & SWT.ARROW) != 0) {
		OS.PtSuperClassDraw (OS.PtButton (), handle, damage);
		PhRect_t rect = new PhRect_t ();
		OS.PtCalcCanvas (handle, rect);
		int flags = 0;
		if ((style & SWT.RIGHT) != 0) flags = 2;
		if ((style & SWT.LEFT) != 0) flags = 1;
		if ((style & SWT.DOWN) != 0) flags = 8;
		if ((style & SWT.UP) != 0) flags = 4;
		OS.PgDrawArrow (rect, (short)0, 0x000000, flags);
	} else if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		OS.PtSuperClassDraw (OS.PtToggleButton (), handle, damage);
	} else {
		OS.PtSuperClassDraw (OS.PtButton (), handle, damage);
	}
	return super.processPaint (damage);
}

int processSelection (int info) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) selectRadio ();
	}
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
}

public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void selectRadio () {
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child && child instanceof Button) {
			Button button = (Button) child;
			if ((button.style & SWT.RADIO) != 0) {
				if (button.getSelection ()) {
					button.setSelection (false);
					button.postEvent (SWT.Selection);
				}
			}
		}
	}
	setSelection (true);
}

public void setAlignment (int alignment) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		OS.PtDamageWidget (handle);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int [] args = {OS.Pt_ARG_HORIZONTAL_ALIGNMENT, OS.Pt_LEFT, 0};
	if ((style & SWT.CENTER) != 0) args [1] = OS.Pt_CENTER;
	if ((style & SWT.RIGHT) != 0) args [1] = OS.Pt_RIGHT;
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int [] args = {OS.Pt_ARG_FLAGS, selected ? OS.Pt_SET : 0, OS.Pt_SET};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	int imageHandle = 0;
	if (image != null) imageHandle = copyPhImage (image.handle);
	int [] args = {
		OS.Pt_ARG_LABEL_IMAGE, imageHandle, 0,
		OS.Pt_ARG_LABEL_TYPE, OS.Pt_IMAGE, 0
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (imageHandle != 0) OS.free (imageHandle);
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	int i=0, j=0;
	char mnemonic=0;
	while (i < text.length) {
		if ((text [j++] = text [i++]) == Mnemonic) {
			if (i == text.length) {continue;}
			if (text [i] == Mnemonic) {i++; continue;}
			if (mnemonic == 0) mnemonic = text [i];
			j--;
		}
	}
	while (j < text.length) text [j++] = 0;
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int ptr2 = 0;
	if (mnemonic != 0) {
		byte [] buffer2 = Converter.wcsToMbcs (null, new char []{mnemonic}, true);
		ptr2 = OS.malloc (buffer2.length);
		OS.memmove (ptr2, buffer2, buffer2.length);
	}
	replaceMnemonic (mnemonic, true, true);
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr, 0,
		OS.Pt_ARG_LABEL_TYPE, OS.Pt_Z_STRING, 0,
		OS.Pt_ARG_ACCEL_KEY, ptr2, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
	OS.free (ptr2);
}

}
