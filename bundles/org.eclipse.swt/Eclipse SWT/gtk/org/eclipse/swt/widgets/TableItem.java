package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents an item in a table.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class TableItem extends Item {
	Table parent;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the index to store the receiver in its parent
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
public TableItem (Table parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
public TableItem (Table parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public Color getBackground () {
	checkWidget ();
	Table parent = getParent();
	return parent.getBackground();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the table.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds (int index) {
	checkWidget();
	int CELL_SPACING=1;
	int clist = parent.handle;
	int columnHandle = OS.GTK_CLIST_COLUMN (clist);
	columnHandle= columnHandle+index*GtkCListColumn.sizeof;
	GtkCListColumn column=new GtkCListColumn();
	OS.memmove(column, columnHandle, GtkCListColumn.sizeof);
	double haj = OS.gtk_adjustment_get_value(OS.GTK_CLIST_HADJUSTMENT (clist));
	double vaj = OS.gtk_adjustment_get_value(OS.GTK_CLIST_VADJUSTMENT (clist));
	int x=(short)column.area_x+OS.GTK_CLIST_HOFFSET(clist);
	int width=(short)column.area_width;
	int height=parent.getItemHeight();
	int row=parent.indexOf(this);
	int y=OS.GTK_CLIST_COLUMN_TITLE_AREA_HEIGHT(clist)+height*row+(row+2)*CELL_SPACING-(int)vaj;
	return new Rectangle (x, y, width, height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return false;
	int row = parent.indexOf (this);
	if (row == -1) return false;
	int clist = parent.handle;
	int [] pixmap = new int [1];
	OS.gtk_clist_get_pixtext (clist, row, 0, null, null, pixmap, null);
	return pixmap [0] == parent.check;
}

public Display getDisplay () {
	Table parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public Color getForeground () {
	checkWidget ();
	Table parent = getParent();
	return parent.getForeground();
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 *
 * @return the grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
	checkWidget ();
	//NOT DONE
	if ((parent.style & SWT.CHECK) == 0) return false;
	return false;
}

/**
* Gets the image at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be queried from
* the OS.
*
* @param index the index of the image
* @return the image
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Image getImage (int index) {
	checkWidget ();
	if (index == 0) return getImage ();
	int row = parent.indexOf (this);
	if (row == -1) return null;
	int clist = parent.handle;
	int [] pixmap = new int [1], mask = new int [1];
	OS.gtk_clist_get_pixtext (clist, row, index, null, null, pixmap, null);
	Display display = getDisplay ();
	return Image.gtk_new (display, mask [0] == 0 ? SWT.BITMAP : SWT.ICON, pixmap [0], mask [0]);
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * table.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getImageBounds (int index) {
	checkWidget ();
	//NOT IMPLEMENTED
	return new Rectangle (0, 0, 0, 0);
}

/**
 * Gets the image indent.
 *
 * @return the indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getImageIndent () {
	checkWidget ();
	/* Image indent is not supported on GTK */
	return 0;
}

/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget ();
	return parent;
}

/**
* Gets the item text at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be queried from
* the OS.
*
* @param index the index of the item
* @return the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_TEXT)
*	when the operation fails
*/
public String getText (int index) {
	checkWidget ();
	if (index == 0) return getText ();
	int row = parent.indexOf (this);
	if (row == -1) return null;
	int clist = parent.handle;
//	int ptr = 0;
	int ptr = OS.g_malloc (256);
	int [] address = new int [] {ptr};
	OS.gtk_clist_get_pixtext (clist, row, index, address, null, null, null);
	byte [] buffer = new byte [OS.strlen (address [0])];
	OS.memmove (buffer, address [0], buffer.length);
	OS.g_free (ptr);
	return new String (Converter.mbcsToWcs (null, buffer));
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int row = parent.indexOf (this);
	if (row == -1) return;
	int clist = parent.handle;
	OS.gtk_clist_set_background (clist, row, color != null ? color.handle : null);
}

/**
 * Sets the checked state of the receiver.
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget();
	int row = parent.indexOf (this);
	if (row == -1) return;
	int ctable = parent.handle;
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int pixmap = checked ? parent.check : parent.uncheck;
	byte [] spacing = new byte [1];
	OS.gtk_clist_get_pixtext(ctable, row, 0, null, spacing, null, null);
	OS.gtk_clist_set_pixtext (ctable, row, 0, buffer, spacing [0], pixmap, 0);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setForeground (Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int row = parent.indexOf (this);
	if (row == -1) return;
	int clist = parent.handle;
	OS.gtk_clist_set_foreground (clist, row, color != null ? color.handle : null);
}

/**
 * Sets the grayed state of the receiver.
 *
 * @param checked the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget();
	//NOT IMPLEMENTED
}

/**
 * Sets the receiver's image at a column.
 *
 * @param index the column index
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (int index, Image image) {
	checkWidget ();
	if (image != null && image.isDisposed()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (index == 0) {
		if ((parent.style & SWT.CHECK) != 0) return;
		super.setImage (image);
	}
	int row = parent.indexOf (this);
	if (row == -1) return;
	int clist = parent.handle;
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	if (image == null) {
		OS.gtk_clist_set_text (clist, row, 0, buffer);
	} else {
		int pixmap = image.pixmap, mask = image.mask;
		byte [] spacing = new byte [] {2};
//		OS.gtk_clist_get_pixtext (clist, row, index, null, spacing, null, null);
		OS.gtk_clist_set_pixtext (clist, row, index, buffer, spacing [0], pixmap, mask);
		if (parent.imageHeight == 0) {	
			int [] width = new int [1], height = new int [1];
			OS.gdk_drawable_get_size (pixmap, width, height);
			if (height [0] > OS.GTK_CLIST_ROW_HEIGHT (parent.handle)) {
				parent.imageHeight = height [0];
				OS.gtk_widget_realize (clist);
				OS.gtk_clist_set_row_height (clist, height [0]);
			}
		}
	}
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the image for multiple columns in the Table. 
 * 
 * @param images the array of new images
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image [] images) {
	checkWidget ();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
}

/**
 * Sets the image indent.
 *
 * @param indent the new indent
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImageIndent (int indent) {
	checkWidget ();
	/* Image indent is not supported on GTK */
}

/**
 * Sets the receiver's text at a column
 *
 * @param index the column index
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
public void setText (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == 0) {
		super.setText (string);
	}
	int row = parent.indexOf (this);
	if (row == -1) return;
	int clist = parent.handle;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	byte [] spacing = new byte [1];
	int [] pixmap = new int [1], mask = new int [1];
	OS.gtk_clist_get_pixtext (clist, row, index, null, spacing, pixmap, mask);
	if (pixmap [0] == 0) {
		OS.gtk_clist_set_text (clist, row, index, buffer);
	} else {
		OS.gtk_clist_set_pixtext (clist, row, index, buffer, spacing [0], pixmap [0], mask [0]);
	}
}

public void setText (String string) {
	checkWidget ();
	setText (0, string);
}

/**
 * Sets the text for multiple columns in the table. 
 * 
 * @param strings the array of new strings
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String [] strings) {
	checkWidget ();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
}

}
