package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, SHADOW_IN, SHADOW_OUT, VERTICAL</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Label extends Control {
	String text = "";
	Image image;
	
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
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

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in 
 * which case, <code>NONE</code> is returned.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return 0;
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
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

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int align = OS.Pt_LEFT;
	if ((style & SWT.CENTER) != 0) align = OS.Pt_CENTER;
	if ((style & SWT.RIGHT) != 0) align = OS.Pt_RIGHT;
	OS.PtSetResource (handle, OS.Pt_ARG_HORIZONTAL_ALIGNMENT, align, 0);
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize && (style & SWT.WRAP) != 0) setText (text);
	return changed;
}

public boolean setFocus () {
	checkWidget();
	return false;
}

public void setFont (Font font) {
	super.setFont (font);
	if ((style & SWT.WRAP) != 0) setText (text);
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
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
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	this.image = image;
	int imageHandle = 0;
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		imageHandle = copyPhImage (image.handle);
	}
	int [] args = {
		OS.Pt_ARG_LABEL_IMAGE, imageHandle, 0,
		OS.Pt_ARG_LABEL_TYPE, OS.Pt_IMAGE, 0
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (imageHandle != 0) OS.free (imageHandle);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic characters and line delimiters.
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
