package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Label extends Control {
	String text = "";
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
	if ((style & SWT.SEPARATOR) != 0) {
		int border = getBorderWidth ();
		int width = border * 2, height = border * 2;
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;  height += 3;
		} else {
			width += 3; height += DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
		return new Point (width, height);
	}

	if ((style & SWT.WRAP) != 0) {
		int [] args = {
			OS.Pt_ARG_LABEL_TYPE, 0, 0,		// 1
			OS.Pt_ARG_TEXT_FONT, 0, 0,		// 4
			OS.Pt_ARG_LINE_SPACING, 0, 0,	// 7
			OS.Pt_ARG_MARGIN_WIDTH, 0, 0,	// 10
			OS.Pt_ARG_MARGIN_HEIGHT, 0, 0,	// 13
			OS.Pt_ARG_MARGIN_LEFT, 0, 0,	// 16
			OS.Pt_ARG_MARGIN_RIGHT, 0, 0,	// 19
			OS.Pt_ARG_MARGIN_TOP, 0, 0,		// 22
			OS.Pt_ARG_MARGIN_BOTTOM, 0, 0,	// 25
		};
		OS.PtGetResources (handle, args.length / 3, args);
		/* If we are wrapping text, calculate the height based on wHint. */
		if (args [1] == OS.Pt_Z_STRING) {
			int length = OS.strlen (args [4]);
			byte [] font = new byte [length + 1];
			OS.memmove (font, args [4], length);
			Display display = getDisplay ();
			PhRect_t rect = new PhRect_t ();
			String string = text;
			if (wHint != SWT.DEFAULT) {
				string = display.wrapText (text, font, wHint);
			}
			if (hHint != SWT.DEFAULT) {
				rect.ul_y = 0;
				rect.lr_y = (short)(hHint - 1);
			} else {
				byte [] buffer = Converter.wcsToMbcs (null, string, false);
				OS.PgExtentMultiText (rect, null, font, buffer, buffer.length, args [7]);
			}
			PhArea_t area = new PhArea_t ();
			OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
			int width = area.size_w;
			int height = area.size_h;
			width += (args [10] * 2) + args [16] + args [19];
			height += (args [13] * 2) + args [22] + args [25];
			return new Point (width, height);
		}
	}
	
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w, height = dim.h;
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
		
	if ((style & SWT.SEPARATOR) != 0) {
		int clazz = display.PtSeparator;
		int orientation = (style & SWT.HORIZONTAL) != 0 ? OS.Pt_SEP_HORIZONTAL : OS.Pt_SEP_VERTICAL;
		int type = OS.Pt_ETCHED_IN;
		if ((style & (SWT.SHADOW_OUT)) != 0) type = OS.Pt_ETCHED_OUT;
		int [] args = {
			OS.Pt_ARG_SEP_FLAGS, orientation, OS.Pt_SEP_VERTICAL | OS.Pt_SEP_HORIZONTAL,
			OS.Pt_ARG_SEP_TYPE, type, 0,
			OS.Pt_ARG_FILL_COLOR, display.WIDGET_BACKGROUND, 0,
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
	int verticalAlign = (style & SWT.WRAP) != 0 ? OS.Pt_TOP : OS.Pt_CENTER;
	boolean hasBorder = (style & SWT.BORDER) != 0;
	int [] args = {
		OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_HORIZONTAL_ALIGNMENT, alignment, 0,
		OS.Pt_ARG_VERTICAL_ALIGNMENT, verticalAlign, 0,
		OS.Pt_ARG_FILL_COLOR, display.WIDGET_BACKGROUND, 0,
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
	return text;
}

int processPaint (int damage) {
	int clazz = OS.PtLabel ();
	if ((style & SWT.SEPARATOR) != 0) clazz = OS.PtSeparator ();
	OS.PtSuperClassDraw (clazz, handle, damage);
	return super.processPaint (damage);
}

int processActivate (int info) {
	Composite control = this.parent;
	while (control != null) {
		Control [] children = control._getChildren ();
		int index = 0;
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		index++;
		if (index < children.length) {
			if (children [index].setFocus ()) return OS.Pt_CONTINUE;
		}
		control = control.parent;
	}
	return OS.Pt_CONTINUE;
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
	text = null;
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

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	super.setBounds (x, y, width, height, move, resize);
	if (resize && (style & SWT.WRAP) != 0) setText (text);
}

public boolean setFocus () {
	return false;
}

public void setFont (Font font) {
	super.setFont (font);
	if ((style & SWT.WRAP) != 0) setText (text);
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
	text = string;
	char [] unicode = new char [string.length ()];
	string.getChars (0, unicode.length, unicode, 0);
	int i=0, j=0;
	char mnemonic=0;
	while (i < unicode.length) {
		if ((unicode [j++] = unicode [i++]) == Mnemonic) {
			if (i == unicode.length) {continue;}
			if (unicode [i] == Mnemonic) {i++; continue;}
			if (mnemonic == 0) mnemonic = unicode [i];
			j--;
		}
	}
	while (j < unicode.length) unicode [j++] = 0;
	/* Wrap the text if necessary, and convert to mbcs. */
	byte [] buffer;
	if ((style & SWT.WRAP) != 0) {
		int [] args = {
			OS.Pt_ARG_TEXT_FONT, 0, 0,		// 1
			OS.Pt_ARG_WIDTH, 0, 0,			// 4
			OS.Pt_ARG_MARGIN_WIDTH, 0, 0,	// 7
			OS.Pt_ARG_MARGIN_LEFT, 0, 0,	// 10
			OS.Pt_ARG_MARGIN_RIGHT, 0, 0,	// 13
		};
		OS.PtGetResources (handle, args.length / 3, args);
		int length = OS.strlen (args [1]);
		byte [] font = new byte [length + 1];
		OS.memmove (font, args [1], length);
		int border = 0;
		if ((style & SWT.BORDER) != 0) border = 2;
		int width = args [4];
		width -= (args [7] * 2) + args [10] + args [13] + border * 2;
		Display display = getDisplay ();
		if (mnemonic != '\0') string = new String (unicode);
		string = display.wrapText (string, font, width);
		buffer = Converter.wcsToMbcs (null, string, true);
	} else {
		buffer = Converter.wcsToMbcs (null, unicode, true);
	}
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int ptr2 = 0;
	if (mnemonic != 0) {
		byte [] buffer2 = Converter.wcsToMbcs (null, new char []{mnemonic}, true);
		ptr2 = OS.malloc (buffer2.length);
		OS.memmove (ptr2, buffer2, buffer2.length);
	}
	replaceMnemonic (mnemonic, 0);
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
