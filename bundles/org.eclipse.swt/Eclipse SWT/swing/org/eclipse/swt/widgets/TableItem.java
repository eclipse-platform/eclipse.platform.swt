/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import javax.swing.ImageIcon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CTable;
import org.eclipse.swt.internal.swing.CTableItem;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.internal.swing.CTableItem.TableItemObject;

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
  /**
   * the handle to the OS resource 
   * (Warning: This field is platform dependent)
   * <p>
   * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by SWT. It is not available on all
   * platforms and should never be accessed from application code.
   * </p>
   */ 
  public CTableItem handle;
	Table parent;
//	String [] strings;
	Image [] images;
//	boolean checked, grayed
  boolean cached;
//	int imageIndent, background = -1, foreground = -1, font = -1;
//	int [] cellBackground, cellForeground, cellFont;

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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableItem (Table parent, int style) {
	this (parent, style, checkNull (parent).getItemCount (), true);
}

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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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
	this (parent, style, index, true);
}

TableItem (Table parent, int style, int index, boolean create) {
	super (parent, style);
	this.parent = parent;
  handle = createHandle();
	if (create) parent.createItem (this, index);
}

static Table checkNull (Table control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

CTableItem createHandle () {
  return CTableItem.Factory.newInstance(this, style);
}

void clear () {
	text = "";
	image = null;
  images = null;
	handle.setBackground(null);
	handle.setForeground(null);
	handle.setFont(null);
	handle.setChecked(false);
	handle.setGrayed(false);
	int count = parent.getColumnCount();
	for(int i=0; i<count; i++) {
	  TableItemObject tableItemObject = handle.getTableItemObject(count);
	  tableItemObject.setText(null);
	  tableItemObject.setBackground(null);
	  tableItemObject.setForeground(null);
	  tableItemObject.setFont(null);
	  tableItemObject.setIcon(null);
	}
	int itemIndex = parent.indexOf (this);
	((CTable)parent.handle).getModel().fireTableRowsUpdated(itemIndex, itemIndex);
//	strings = null;
//	images = null;
//	imageIndent = 0;
//	checked = grayed = false;
//	background = foreground = font = -1;
//	cellBackground = cellForeground = cellFont = null;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
}

void destroyWidget () {
  parent.destroyItem (this);
  releaseHandle ();
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
 */
public Color getBackground () {
  checkWidget ();
  java.awt.Color color = handle.getBackground();
  if(color == null) return parent.getBackground();
  return Color.swing_new(display, color);
}

/**
 * Returns the background color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getBackground (int index) {
  checkWidget ();
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return getBackground ();
  java.awt.Color color = handle.getTableItemObject(index).getBackground();
  if(color == null) {
    if(index != 0) {
      return getBackground();
    }
    return Color.swing_new(display, parent.handle.getBackground());
  }
  return Color.swing_new(display, color);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Rectangle getBounds () {
  checkWidget();
  if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
  int itemIndex = parent.indexOf (this);
  if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
  return getBounds(0);
//  RECT rect = getBounds (itemIndex, 0, true, false, false);
//  int width = rect.right - rect.left, height = rect.bottom - rect.top;
//  return new Rectangle (rect.left, rect.top, width, height);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
  java.awt.Rectangle rect = ((CTable)parent.handle).getCellRect(parent.indexOf(this), index, false);
  Point offset = parent.getInternalOffset();
  return new Rectangle(rect.x + offset.x, rect.y + offset.y, rect.width, rect.height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the checked state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget();
  if ((parent.style & SWT.CHECK) == 0) return false;
  return handle.isChecked();
}

/**
 * Returns the font that the receiver will use to paint textual information for this item.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont () {
  return getFont(0);
}

/**
 * Returns the font that the receiver will use to paint textual information
 * for the specified cell in this item.
 *
 * @param index the column index
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont (int index) {
  checkWidget ();
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count -1) return getFont ();
  java.awt.Font font = handle.getTableItemObject(index).getFont();
  if(font == null) {
    if(index != 0) {
      return getFont();
    }
    return Font.swing_new(display, parent.handle.getFont());
  }
  return Font.swing_new(display, font);
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
 */
public Color getForeground () {
  checkWidget ();
  java.awt.Color color = handle.getForeground();
  if(color == null) return parent.getForeground();
  return Color.swing_new(display, color);
}

/**
 * 
 * Returns the foreground color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getForeground (int index) {
  checkWidget ();
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count -1) return getForeground ();
  java.awt.Color color = handle.getTableItemObject(index).getForeground();
  if(color == null) {
    if(index != 0) {
      return getBackground();
    }
    return Color.swing_new(display, parent.handle.getForeground());
  }
  return Color.swing_new(display, color);
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
  checkWidget ();
  if ((parent.style & SWT.CHECK) == 0) return false;
  return handle.isGrayed();
}

public Image getImage () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getImage ();
}

/**
 * Returns the image stored at the given column index in the receiver,
 * or null if the image has not been set or if the column does not exist.
 *
 * @param index the column index
 * @return the image stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getImage ();
  if (images != null) {
    if (0 <= index && index < images.length) return images [index];
  }
	return null;
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * table.  An empty rectangle is returned if index exceeds
 * the index of the table's last column.
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
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return new Rectangle (0, 0, 0, 0);
  java.awt.Rectangle imageBounds = ((CTable)parent.handle).getImageBounds(itemIndex, index);
  return new Rectangle(imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height);
//	RECT rect = getBounds (itemIndex, index, false, true);
//	int width = rect.right - rect.left, height = rect.bottom - rect.top;
//	return new Rectangle (rect.left, rect.top, width, height);
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
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
  Utils.notImplemented(); return 0;
//	return imageIndent;
}

String getNameText () {
  if ((parent.style & SWT.VIRTUAL) != 0) {
    if (!cached) return "*virtual*"; //$NON-NLS-1$
  }
  return super.getNameText ();
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
	checkWidget();
	return parent;
}

public String getText () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getText ();
}

/**
 * Returns the text stored at the given column index in the receiver,
 * or empty string if the text has not been set.
 *
 * @param index the column index
 * @return the text stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return "";
  return handle.getTableItemObject(index).getText();
}

//void redraw () {
//	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
//	if (parent.currentItem == this || parent.drawCount != 0) return;
//	int hwnd = parent.handle;
//	if (!OS.IsWindowVisible (hwnd)) return;
//	int index = parent.indexOf (this);
//	if (index == -1) return;
//	OS.SendMessage (hwnd, OS.LVM_REDRAWITEMS, index, index);
//}

//void redraw (int column, boolean drawText, boolean drawImage) {
//	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
//	if (parent.currentItem == this || parent.drawCount != 0) return;
//	int hwnd = parent.handle;
//	if (!OS.IsWindowVisible (hwnd)) return;
//	int index = parent.indexOf (this);
//	if (index == -1) return;
//	RECT rect = getBounds (index, column, drawText, drawImage);
//	OS.InvalidateRect (hwnd, rect, true);
//}

void releaseHandle () {
  super.releaseHandle ();
  parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
//	strings = null;
	images = null;
//	cellBackground = cellForeground = cellFont = null;
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
  handle.setBackground(color == null? null: color.handle);
  int index = parent.indexOf(this);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableRowsUpdated(index, index);
}

/**
 * Sets the background color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
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
 * @since 3.0
 * 
 */
public void setBackground (int index, Color color) {
	checkWidget ();
  if (color != null && color.isDisposed ()) {
    SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  }
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return;
  handle.getTableItemObject(index).setBackground(color == null? null: color.handle);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), index);
}

/**
 * Sets the checked state of the checkbox for this item.  This state change 
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param checked the new checked state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget();
  if ((parent.style & SWT.CHECK) == 0) return;
  handle.setChecked(checked);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  // TODO: is it always 0 if columns are reordered?
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), 0);
}

//void setChecked (boolean checked, boolean notify) {
//	this.checked = checked;
//	if (notify) {
//		Event event = new Event();
//		event.item = this;
//		event.detail = SWT.CHECK;
//		parent.postEvent (SWT.Selection, event);
//	}
//	redraw ();
//}

/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setFont (Font font){
  checkWidget ();
  if (font != null && font.isDisposed ()) {
    SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  }
  handle.setFont(font == null? null: font.handle);
  int index = parent.indexOf(this);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableRowsUpdated(index, index);
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for the specified cell in this item to the font specified by the 
 * argument, or to the default font for that kind of control if the 
 * argument is null.
 *
 * @param index the column index
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setFont (int index, Font font) {
	checkWidget ();
  if (font != null && font.isDisposed ()) {
    SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  }
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return;
  handle.getTableItemObject(index).setFont(font == null? null: font.handle);
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), index);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  parent.adjustColumnWidth();
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
 */
public void setForeground (Color color){
  checkWidget ();
  if (color != null && color.isDisposed ()) {
    SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  }
  handle.setForeground(color == null? null: color.handle);
  int index = parent.indexOf(this);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableRowsUpdated(index, index);
}

/**
 * Sets the foreground color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
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
 * @since 3.0
 */
public void setForeground (int index, Color color){
	checkWidget ();
  if (color != null && color.isDisposed ()) {
    SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  }
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return;
  handle.getTableItemObject(index).setForeground(color == null? null: color.handle);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), index);
}

/**
 * Sets the grayed state of the checkbox for this item.  This state change 
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox; 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
  checkWidget ();
  if ((parent.style & SWT.CHECK) == 0) return;
  handle.setGrayed(grayed);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  // TODO: is it always 0 if columns are reordered?
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), 0);
}

/**
 * Sets the image for multiple columns in the table. 
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
	checkWidget();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
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
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (index == 0) {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (this.image)) return;
		}
		super.setImage (image);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
  if (images == null && index != 0) {
    images = new Image [count];
    images [0] = image;
  }
  if (images != null) {
    if (image != null && image.type == SWT.ICON) {
      if (image.equals (images [index])) return;
    }
    images [index] = image;
  }
  handle.getTableItemObject(index).setIcon(image != null? new ImageIcon(image.handle): null);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableCellUpdated(index, index);
  parent.adjustColumnWidth();
//
//  
//	if (images == null && index != 0) images = new Image [count];
//	if (images != null) {
//		if (image != null && image.type == SWT.ICON) {
//			if (image.equals (images [index])) return;
//		}
//		images [index] = image;
//	}
//	
//	/* Ensure that the image list is created */
//	parent.imageIndex (image);
//	
//	if (index == 0) parent.setScrollWidth (this, false);
//	redraw (index, false, true);
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the indent of the first column's image, expressed in terms of the image's width.
 *
 * @param indent the new indent
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @deprecated this functionality is not supported on most platforms
 */
public void setImageIndent (int indent) {
	checkWidget();
	if (indent < 0) return;
  Utils.notImplemented();
//	if (imageIndent == indent) return;
//	imageIndent = indent;
//  if ((parent.style & SWT.VIRTUAL) != 0) {
//    cached = true;
//  } else {
//		int index = parent.indexOf (this);
//		if (index != -1) {
//			int hwnd = parent.handle;
//			LVITEM lvItem = new LVITEM ();
//			lvItem.mask = OS.LVIF_INDENT;
//			lvItem.iItem = index;
//			lvItem.iIndent = indent;
//			OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
//		}
//	}
//	parent.setScrollWidth (this, false);
//	redraw ();
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
	checkWidget();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
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
  checkWidget();
  if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  int count = Math.max (1, parent.getColumnCount ());
  if (0 > index || index > count - 1) return;
  if(index == 0) {
    super.setText(string);
  }
  handle.getTableItemObject(index).setText(string);
  if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
  ((CTable)parent.handle).getModel().fireTableCellUpdated(parent.indexOf(this), index);
  parent.adjustColumnWidth();
}

public void setText (String string) {
  checkWidget();
  setText (0, string);
}

}
