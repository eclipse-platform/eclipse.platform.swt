package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Label extends Control {
	Image image;
	
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;  height += 3;
		} else {
			width += 3; height += DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
		return new Point (width, height);
	}
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	width = dim.w; height = dim.h;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		PhRect_t rect = new PhRect_t ();
		PhArea_t area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) width = area.size_w;
		if (hHint != SWT.DEFAULT) height = area.size_h;
	}
	return new Point (width, height);
}

void createHandle (int index) {
	Display display = getDisplay ();
	int parentHandle = parent.handle;
		
	if ((style & SWT.SEPARATOR) != 0) {
		int clazz = display.PtSeparator;
		int orientation = (style & SWT.HORIZONTAL) != 0 ? OS.Pt_SEP_HORIZONTAL : OS.Pt_SEP_VERTICAL;
		int type = OS.Pt_ETCHED_IN;
		if ((style & (SWT.SHADOW_OUT)) != 0) type = OS.Pt_ETCHED_OUT;
		int [] args = {
			OS.Pt_ARG_SEP_FLAGS, orientation, OS.Pt_SEP_VERTICAL | OS.Pt_SEP_HORIZONTAL,
			OS.Pt_ARG_SEP_TYPE, type, 0,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};		
		handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}

	int clazz = display.PtLabel;
	int alignment = OS.Pt_LEFT;
	if ((style & SWT.CENTER) != 0) alignment = OS.Pt_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.Pt_RIGHT;
	int [] args = {
		OS.Pt_ARG_HORIZONTAL_ALIGNMENT, alignment, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

public int getAlignment () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return 0;
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

String getNameText () {
	return getText ();
}

public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return "";
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

int processPaint (int damage) {
	int clazz = OS.PtLabel ();
	if ((style & SWT.SEPARATOR) != 0) clazz = OS.PtSeparator ();
	OS.PtSuperClassDraw (clazz, handle, damage);
	return super.processPaint (damage);
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
}

public void setAlignment (int alignment) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int [] args = {OS.Pt_ARG_HORIZONTAL_ALIGNMENT, OS.Pt_LEFT, 0};
	if ((style & SWT.CENTER) != 0) args [1] = OS.Pt_CENTER;
	if ((style & SWT.RIGHT) != 0) args [1] = OS.Pt_RIGHT;
	OS.PtSetResources (handle, args.length / 3, args);
}

public boolean setFocus () {
	return false;
}

public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
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
	if ((style & SWT.SEPARATOR) != 0) return;
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