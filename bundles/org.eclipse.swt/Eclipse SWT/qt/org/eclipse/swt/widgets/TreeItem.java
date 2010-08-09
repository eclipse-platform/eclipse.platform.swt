/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class represent a selectable user interface object that
 * represents a hierarchy of tree items in a tree widget.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem,
 *      TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TreeItem extends Item {
	private QTreeWidgetItem item;
	private List<TreeItem> items;
	private Tree parent;
	private TreeItem parentItem;
	private String[] strings;
	private Image[] images;
	private Font font;
	private Font[] cellFont;
	boolean cached;
	private Color background;
	private Color foreground;
	private int[] cellBackground, cellForeground;
	private boolean grayed;

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code> or a <code>TreeItem</code>) and a style value
	 * describing its behavior and appearance. The item is added to the end of
	 * the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a tree control which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(Tree parent, int style) {
		this(parent, null, style, -1);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code> or a <code>TreeItem</code>), a style value describing
	 * its behavior and appearance, and the index at which to place it in the
	 * items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a tree control which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * @param index
	 *            the zero-relative index to store the receiver in its parent
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the parent (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(Tree parent, int style, int index) {
		this(parent, null, style, index);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code> or a <code>TreeItem</code>) and a style value
	 * describing its behavior and appearance. The item is added to the end of
	 * the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parentItem
	 *            a tree control which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(TreeItem parentItem, int style) {
		this(checkNull(parentItem).parent, parentItem, style, -1);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code> or a <code>TreeItem</code>), a style value describing
	 * its behavior and appearance, and the index at which to place it in the
	 * items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parentItem
	 *            a tree control which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * @param index
	 *            the zero-relative index to store the receiver in its parent
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the parent (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(TreeItem parentItem, int style, int index) {
		this(checkNull(parentItem).parent, parentItem, style, index);
	}

	TreeItem(Tree parent, TreeItem parentItem, int style, int index) {
		super(parent, style);
		this.parent = parent;
		item = new QTreeWidgetItem();
		items = new ArrayList<TreeItem>(4);
		if (parentItem != null) {
			this.parentItem = parentItem;
			parentItem.addItem(this, index);
		} else {
			parent.addItem(this, index);
		}
		display.addControl(item, this);
	}

	void addItem(TreeItem item, int index) {
		if (index >= 0 && index < items.size()) {
			items.add(index, item);
			this.item.insertChild(index, item.getQItem());
		} else {
			items.add(item);
			this.item.addChild(item.getQItem());
		}
	}

	void removeItem(TreeItem item) {
		items.remove(item);
		getQItem().removeChild(item.getQItem());
	}

	TreeItem getItem(QTreeWidgetItem qItem) {
		for (TreeItem item : items) {
			if (item.item == qItem) {
				return item;
			}
			TreeItem ti = item.getItem(qItem);
			if (ti != null) {
				return ti;
			}
		}
		return null;
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		item = null;
	}

	QTreeWidgetItem getQItem() {
		return item;
	}

	QTreeWidget getQTreeWidget() {
		return parent.getQTreeWidget();
	}

	static TreeItem checkNull(TreeItem item) {
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return item;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	void clear() {
		for (int i = 0; i < parent.getColumnCount(); i++) {
			item.setText(i, null);
			item.setIcon(i, (QIcon) null);
			item.setWhatsThis(i, null);
			item.setFont(i, null);
		}
		text = ""; //$NON-NLS-1$
		image = null;
		strings = null;
		images = null;
		if ((parent.style & SWT.CHECK) != 0) {
			//
		}
		background = foreground = null;
		font = null;
		cellBackground = cellForeground = null;
		cellFont = null;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = false;
		}
	}

	/**
	 * Clears the item at the given zero-relative index in the receiver. The
	 * text, icon and other attributes of the item are set to the default value.
	 * If the tree was created with the <code>SWT.VIRTUAL</code> style, these
	 * attributes are requested again as needed.
	 * 
	 * @param index
	 *            the index of the item to clear
	 * @param all
	 *            <code>true</code> if all child items of the indexed item
	 *            should be cleared recursively, and <code>false</code>
	 *            otherwise
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 * 
	 * @since 3.2
	 */
	public void clear(int index, boolean all) {
		checkWidget();
		validateItemIndex(index);
		if (index == 0) {
			clear();
			return;
		}
		TreeItem item = items.get(index);
		item.clear(0, all);
	}

	private void validateItemIndex(int index) {
		if (index < 0 || index >= items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
	}

	/**
	 * Clears all the items in the receiver. The text, icon and other attributes
	 * of the items are set to their default values. If the tree was created
	 * with the <code>SWT.VIRTUAL</code> style, these attributes are requested
	 * again as needed.
	 * 
	 * @param all
	 *            <code>true</code> if all child items should be cleared
	 *            recursively, and <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 * 
	 * @since 3.2
	 */
	public void clearAll(boolean all) {
		checkWidget();
		clear();
		for (TreeItem item : items) {
			item.clearAll(all);
		}
	}

	@Override
	void destroyWidget() {
		if (parentItem != null) {
			parentItem.removeItem(this);
		} else {
			parent.removeItem(this);
		}
		super.destroyWidget();
		item = null;
	}

	/**
	 * Returns the receiver's background color.
	 * 
	 * @return the background color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * 
	 */
	public Color getBackground() {
		checkWidget();
		if (background == null) {
			return parent.getBackground();
		}
		return background;
	}

	/**
	 * Returns the background color at the given column index in the receiver.
	 * 
	 * @param index
	 *            the column index
	 * @return the background color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Color getBackground(int index) {
		checkWidget();
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getBackground();
		}
		int pixel = cellBackground != null ? cellBackground[index] : -1;
		return pixel == -1 ? getBackground() : Color.qt_new(display, pixel);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent.
	 * 
	 * @return the receiver's bounding rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Rectangle getBounds() {
		checkWidget();
		return QtSWTConverter.convert(getQTreeWidget().visualItemRect(item));
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent at a column in the tree.
	 * 
	 * @param index
	 *            the index that specifies the column
	 * @return the receiver's bounding column rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Rectangle getBounds(int index) {
		checkWidget();
		int count = Math.max(1, item.columnCount());
		if (0 > index || index > count - 1) {
			return new Rectangle(0, 0, 0, 0);
		}

		return QtSWTConverter.convert(getQTreeWidget().visualItemRect(item.child(index)));
	}

	/**
	 * Returns <code>true</code> if the receiver is checked, and false
	 * otherwise. When the parent does not have the
	 * <code>CHECK style, return false.
 * <p>
	 * 
	 * @return the checked state
	 * 
	 * @exception SWTException
	 *                <ul> <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getChecked() {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return false;
		}
		return item.isSelected();
	}

	/**
	 * Returns <code>true</code> if the receiver is expanded, and false
	 * otherwise.
	 * <p>
	 * 
	 * @return the expanded state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getExpanded() {
		checkWidget();
		return item.isExpanded();
	}

	/**
	 * Returns the font that the receiver will use to paint textual information
	 * for this item.
	 * 
	 * @return the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public Font getFont() {
		checkWidget();
		return font != null ? font : parent.getFont();
	}

	/**
	 * Returns the font that the receiver will use to paint textual information
	 * for the specified cell in this item.
	 * 
	 * @param index
	 *            the column index
	 * @return the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Font getFont(int index) {
		checkWidget();
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getFont();
		}
		if (cellFont == null || cellFont[index] == null) {
			return getFont();
		}
		return cellFont[index];
	}

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 * 
	 * @return the receiver's foreground color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * 
	 */
	public Color getForeground() {
		checkWidget();
		if (foreground == null) {
			return parent.getForeground();
		}
		return foreground;
	}

	/**
	 * 
	 * Returns the foreground color at the given column index in the receiver.
	 * 
	 * @param index
	 *            the column index
	 * @return the foreground color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Color getForeground(int index) {
		checkWidget();
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getForeground();
		}
		int pixel = cellForeground != null ? cellForeground[index] : -1;
		return pixel == -1 ? getForeground() : Color.qt_new(display, pixel);
	}

	/**
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the parent does not have the <code>CHECK style, return false.
 * <p>
	 * 
	 * @return the grayed state of the checkbox
	 * 
	 * @exception SWTException
	 *                <ul> <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getGrayed() {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return false;
		}
		return grayed;
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range.
	 * 
	 * @param index
	 *            the index of the item to return
	 * @return the item at the given index
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public TreeItem getItem(int index) {
		checkWidget();
		if (index < 0 || index >= items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		return items.get(index);
	}

	/**
	 * Returns the number of items contained in the receiver that are direct
	 * item children of the receiver.
	 * 
	 * @return the number of items
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getItemCount() {
		checkWidget();
		return items.size();
	}

	/**
	 * Returns a (possibly empty) array of <code>TreeItem</code>s which are the
	 * direct item children of the receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the receiver's items
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem[] getItems() {
		checkWidget();
		return items.toArray(new TreeItem[items.size()]);
	}

	@Override
	public Image getImage() {
		checkWidget();
		return super.getImage();
	}

	/**
	 * Returns the image stored at the given column index in the receiver, or
	 * null if the image has not been set or if the column does not exist.
	 * 
	 * @param index
	 *            the column index
	 * @return the image stored at the given column index in the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Image getImage(int index) {
		checkWidget();
		if (index == 0) {
			return getImage();
		}
		if (images != null) {
			if (0 <= index && index < images.length) {
				return images[index];
			}
		}
		return null;
	}

	/**
	 * Returns a rectangle describing the size and location relative to its
	 * parent of an image at a column in the tree.
	 * 
	 * @param index
	 *            the index that specifies the column
	 * @return the receiver's bounding image rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public Rectangle getImageBounds(int index) {
		checkWidget();
		//TODO image only ?
		return QtSWTConverter.convert(getQTreeWidget().visualItemRect(getQItem().child(index)));
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Tree</code>.
	 * 
	 * @return the receiver's parent
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Tree getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>TreeItem</code>
	 * or null when the receiver is a root.
	 * 
	 * @return the receiver's parent item
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem getParentItem() {
		checkWidget();
		return parentItem;
	}

	/**
	 * Returns the text stored at the given column index in the receiver, or
	 * empty string if the text has not been set.
	 * 
	 * @param index
	 *            the column index
	 * @return the text stored at the given column index in the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public String getText(int index) {
		checkWidget();
		if (index == 0) {
			return getText();
		}
		if (strings != null) {
			if (0 <= index && index < strings.length) {
				String string = strings[index];
				return string != null ? string : ""; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns a rectangle describing the size and location relative to its
	 * parent of the text at a column in the tree.
	 * 
	 * @param index
	 *            the index that specifies the column
	 * @return the receiver's bounding text rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public Rectangle getTextBounds(int index) {
		checkWidget();
		//TODO text only ?
		return QtSWTConverter.convert(getQTreeWidget().visualItemRect(getQItem().child(index)));
	}

	/**
	 * Searches the receiver's list starting at the first item (index 0) until
	 * an item is found that is equal to the argument, and returns the index of
	 * that item. If no item is found, returns -1.
	 * 
	 * @param item
	 *            the search item
	 * @return the index of the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int indexOf(TreeItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return items.indexOf(item);
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (destroy) {
			for (int i = items.size() - 1; i > -1; i--) {
				items.get(i).dispose();
			}
			items.clear();
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		strings = null;
		images = null;
		cellBackground = cellForeground = null;
		cellFont = null;
		display.removeControl(item);
	}

	/**
	 * Removes all of the items from the receiver.
	 * <p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void removeAll() {
		checkWidget();
		QTreeWidgetItem qItem = getQItem();
		for (TreeItem item : items) {
			qItem.removeChild(item.getQItem());
		}
		items.clear();
	}

	/**
	 * Sets the receiver's background color to the color specified by the
	 * argument, or to the default system color for the item if the argument is
	 * null.
	 * 
	 * @param color
	 *            the new color (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * 
	 */
	public void setBackground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Color pixel = null;
		if (color != null) {
			parent.customDraw = true;
			pixel = color;
		}
		if (background == pixel) {
			return;
		}
		background = pixel;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
	}

	/**
	 * Sets the background color at the given column index in the receiver to
	 * the color specified by the argument, or to the default system color for
	 * the item if the argument is null.
	 * 
	 * @param index
	 *            the column index
	 * @param color
	 *            the new color (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 * 
	 */
	public void setBackground(int index, Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		int pixel = -1;
		if (color != null) {
			parent.customDraw = true;
			pixel = color.getPixel();
		}
		if (cellBackground == null) {
			cellBackground = new int[count];
			for (int i = 0; i < count; i++) {
				cellBackground[i] = -1;
			}
		}
		if (cellBackground[index] == pixel) {
			return;
		}
		cellBackground[index] = pixel;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
	}

	/**
	 * Sets the checked state of the receiver.
	 * <p>
	 * 
	 * @param checked
	 *            the new checked state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setChecked(boolean checked) {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return;
		}
		getQItem().setSelected(checked);
	}

	/**
	 * Sets the expanded state of the receiver.
	 * <p>
	 * 
	 * @param expanded
	 *            the new expanded state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setExpanded(boolean expanded) {
		checkWidget();
		getQItem().setExpanded(expanded);
	}

	/**
	 * Sets the font that the receiver will use to paint textual information for
	 * this item to the font specified by the argument, or to the default font
	 * for that kind of control if the argument is null.
	 * 
	 * @param font
	 *            the new font (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public void setFont(Font font) {
		checkWidget();
		if (font != null && font.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Font oldFont = this.font;
		if (oldFont == font) {
			return;
		}
		this.font = font;
		if (oldFont != null && oldFont.equals(font)) {
			return;
		}
		if (font != null) {
			parent.customDraw = true;
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		item.setFont(0, font.getQFont());
	}

	/**
	 * Sets the font that the receiver will use to paint textual information for
	 * the specified cell in this item to the font specified by the argument, or
	 * to the default font for that kind of control if the argument is null.
	 * 
	 * @param index
	 *            the column index
	 * @param font
	 *            the new font (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setFont(int index, Font font) {
		checkWidget();
		if (font != null && font.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		if (cellFont == null) {
			if (font == null) {
				return;
			}
			cellFont = new Font[count];
		}
		Font oldFont = cellFont[index];
		if (oldFont == font) {
			return;
		}
		cellFont[index] = font;
		if (oldFont != null && oldFont.equals(font)) {
			return;
		}
		if (font != null) {
			parent.customDraw = true;
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		if (font == null) {
			item.setFont(index, null);
		} else {
			item.setFont(index, font.getQFont());
		}
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the
	 * argument, or to the default system color for the item if the argument is
	 * null.
	 * 
	 * @param color
	 *            the new color (or null)
	 * 
	 * @since 2.0
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * 
	 */
	public void setForeground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.foreground = color;
		item.setForeground(0, new QBrush(color.getColor()));
	}

	/**
	 * Sets the foreground color at the given column index in the receiver to
	 * the color specified by the argument, or to the default system color for
	 * the item if the argument is null.
	 * 
	 * @param index
	 *            the column index
	 * @param color
	 *            the new color (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 * 
	 */
	public void setForeground(int index, Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		int pixel = -1;
		if (color != null) {
			parent.customDraw = true;
			pixel = color.getPixel();
		}
		if (cellForeground == null) {
			cellForeground = new int[count];
			for (int i = 0; i < count; i++) {
				cellForeground[i] = -1;
			}
		}
		if (cellForeground[index] == pixel) {
			return;
		}
		cellForeground[index] = pixel;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
	}

	/**
	 * Sets the grayed state of the checkbox for this item. This state change
	 * only applies if the Tree was created with the SWT.CHECK style.
	 * 
	 * @param grayed
	 *            the new grayed state of the checkbox
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setGrayed(boolean grayed) {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return;
		}
		this.grayed = grayed;
	}

	/**
	 * Sets the image for multiple columns in the tree.
	 * 
	 * @param images
	 *            the array of new images
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if one of the images has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setImage(Image[] images) {
		checkWidget();
		if (images == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		for (int i = 0; i < images.length; i++) {
			setImage(i, images[i]);
		}
	}

	/**
	 * Sets the receiver's image at a column.
	 * 
	 * @param index
	 *            the column index
	 * @param image
	 *            the new image
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setImage(int index, Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (index == 0) {
			if (image != null && image.isIcon()) {
				if (image.equals(this.image)) {
					return;
				}
			}
			super.setImage(image);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		if (images == null && index != 0) {
			images = new Image[count];
			images[0] = image;
		}
		if (images != null) {
			if (image != null && image.isIcon()) {
				if (image.equals(images[index])) {
					return;
				}
			}
			images[index] = image;
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		item.setIcon(index, image != null ? image.getQIcon() : null);
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		setImage(0, image);
	}

	/**
	 * Sets the number of child items contained in the receiver.
	 * 
	 * @param count
	 *            the number of items
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.2
	 */
	public void setItemCount(int count) {
		checkWidget();
		count = Math.max(0, count);
		int itemCount = items.size();
		if (count == itemCount) {
			return;
		}

		if (count < itemCount) {
			for (int i = itemCount - 1; i >= count; i--) {
				removeItem(items.get(i));
			}
			return;
		}
		//		if (isVirtual) {
		//			//TODO
		//		} else {
		for (int i = itemCount; i < count; i++) {
			new TreeItem(this, SWT.NONE, i);
		}
	}

	/**
	 * Sets the text for multiple columns in the tree.
	 * 
	 * @param strings
	 *            the array of new strings
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setText(String[] strings) {
		checkWidget();
		if (strings == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if (string != null) {
				setText(i, string);
			}
		}
	}

	/**
	 * Sets the receiver's text at a column
	 * 
	 * @param index
	 *            the column index
	 * @param string
	 *            the new text
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setText(int index, String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (index == 0) {
			if (string.equals(text)) {
				return;
			}
			super.setText(string);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		if (strings == null && index != 0) {
			strings = new String[count];
			strings[0] = text;
		}
		if (strings != null) {
			if (string.equals(strings[index])) {
				return;
			}
			strings[index] = string;
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		getQItem().setText(index, string);
	}

	@Override
	public void setText(String string) {
		checkWidget();
		setText(0, string);
	}

}
