/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QTableView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class represent a selectable user interface object that
 * represents an item in a table.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem,
 *      TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TableItem extends Item {
	Table parent;
	String[] strings;
	Image[] images;
	Font font;
	Font[] cellFont;
	boolean checked, grayed, cached;
	int imageIndent;
	Color background;
	Color foreground;
	int[] cellBackground, cellForeground;
	private QRadioButton radioButton;
	private boolean selected;

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>) and a style value describing its behavior and
	 * appearance. The item is added to the end of the items maintained by its
	 * parent.
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
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
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
	public TableItem(Table parent, int style) {
		this(parent, style, checkNull(parent).getItemCount(), true);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>), a style value describing its behavior and
	 * appearance, and the index at which to place it in the items maintained by
	 * its parent.
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
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
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
	public TableItem(Table parent, int style, int index) {
		this(parent, style, index, true);
	}

	TableItem(Table parent, int style, int index, boolean create) {
		super(parent, style);
		this.parent = parent;
		if (create) {
			parent.addItem(this, index);
			updateCheckAndRadioPropertyForRow(index);
		}
		setText(index, "");//$NON-NLS-1$
	}

	private void updateCheckAndRadioPropertyForRow(int row) {
		if (radioButton == null && (parent.style & SWT.RADIO) != 0 && (parent.style & SWT.SINGLE) != 0) {
			//TODO
			radioButton = new QRadioButton();
			radioButton.released.connect(this, "radioButtonClickEvent()");//$NON-NLS-1$
			radioButton.setFocusPolicy(FocusPolicy.NoFocus);
			//getQTableWidget().setCellWidget(row, 0, radioButton);
		}
	}

	protected void radioButtonClickEvent() {
		sendEvent(SWT.Selection);
	}

	static Table checkNull(Table control) {
		if (control == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return control;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	QTableView getQTableWidget() {
		return parent.getQTableWidget();
	}

	//	int getRow() {
	//		return parent.indexOf(this);
	//	}

	void clear() {
		text = "";//$NON-NLS-1$
		image = null;
		font = null;
		strings = null;
		images = null;
		imageIndent = 0;
		checked = grayed = false;
		if (background != null) {
			background.dispose();
			background = null;
		}
		if (foreground != null) {
			foreground.dispose();
			foreground = null;
		}
		cellFont = null;
		cellBackground = cellForeground = null;
		if (parent.isVirtual()) {
			cached = false;
		}
	}

	@Override
	void destroyWidget() {
		parent.removeRow(this);
		releaseQWidget();
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
	 */
	public Color getBackground() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 * @since 3.0
	 */
	public Color getBackground(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 * 
	 * @since 3.2
	 */
	public Rectangle getBounds() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return getBounds(0);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent at a column in the table.
	 * 
	 * @param column
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
	 */
	public Rectangle getBounds(int column) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}

		if (column < 0 || column >= parent.getItemCount()) {
			return new Rectangle(0, 0, 0, 0);
		}
		return parent.visualRect(this, column);
	}

	/**
	 * Returns <code>true</code> if the receiver is checked, and false
	 * otherwise. When the parent does not have the <code>CHECK</code> style,
	 * return false.
	 * 
	 * @return the checked state of the checkbox
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getChecked() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if ((parent.style & SWT.CHECK) == 0) {
			return false;
		}
		return checked;
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
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 * @since 3.0
	 */
	public Font getFont(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 */
	public Color getForeground() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 * @since 3.0
	 */
	public Color getForeground(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getForeground();
		}
		int pixel = cellForeground != null ? cellForeground[index] : -1;
		return pixel == -1 ? getForeground() : Color.qt_new(display, pixel);
	}

	/**
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the parent does not have the <code>CHECK</code> style, return false.
	 * 
	 * @return the grayed state of the checkbox
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getGrayed() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if ((parent.style & SWT.CHECK) == 0) {
			return false;
		}
		return grayed;
	}

	@Override
	public Image getImage() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 */
	public Image getImage(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
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
	 * parent of an image at a column in the table. An empty rectangle is
	 * returned if index exceeds the index of the table's last column.
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
	 */
	public Rectangle getImageBounds(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		//TODO
		return getBounds(index);
	}

	/**
	 * Gets the image indent.
	 * 
	 * @return the indent
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getImageIndent() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return imageIndent;
	}

	@Override
	String getNameText() {
		if ((parent.style & SWT.VIRTUAL) != 0) {
			if (!cached) {
				return "*virtual*"; //$NON-NLS-1$
			}
		}
		return super.getNameText();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Table</code>.
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
	public Table getParent() {
		checkWidget();
		return parent;
	}

	@Override
	public String getText() {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return super.getText();
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
	 */
	public String getText(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if (index == 0) {
			return getText();
		}
		if (strings != null) {
			if (0 <= index && index < strings.length) {
				String string = strings[index];
				return string != null ? string : "";//$NON-NLS-1$
			}
		}
		return "";//$NON-NLS-1$
	}

	/**
	 * Returns a rectangle describing the size and location relative to its
	 * parent of the text at a column in the table. An empty rectangle is
	 * returned if index exceeds the index of the table's last column.
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
		if (!parent.checkData(this, true)) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		//TODO
		return getBounds(index);
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		radioButton = null;
		parent = null;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		strings = null;
		images = null;
		cellFont = null;
		cellBackground = cellForeground = null;
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
	 */
	public void setBackground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Color oldColor = background;
		if (color == null) {
			background = null;
		} else {
			if (color.equals(background)) {
				return;
			}
			background = color;
		}
		if (oldColor != null) {
			oldColor.dispose();
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}

		parent.rowChanged(this);
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
	 * @since 3.0
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
		parent.cellChanged(this, index);
		//		QBrush brush = new QBrush(QtSWTConverter.convert(color));
		//		getCellItem(index).setBackground(brush);
	}

	/**
	 * Sets the checked state of the checkbox for this item. This state change
	 * only applies if the Table was created with the SWT.CHECK style.
	 * 
	 * @param checked
	 *            the new checked state of the checkbox
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setChecked(boolean checked) {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return;
		}
		if (this.checked == checked) {
			return;
		}
		setChecked(checked, false);
	}

	void setChecked(boolean checked, boolean notify) {
		this.checked = checked;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		if (notify) {
			Event event = new Event();
			event.item = this;
			event.detail = SWT.CHECK;
			parent.postEvent(SWT.Selection, event);
		}
		parent.cellChanged(this, 0);
		//		CheckState state = checked ? CheckState.Checked : CheckState.Unchecked;
		//		getCellItem(0).setData(ItemDataRole.CheckStateRole, state);
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
			if ((parent.style & SWT.VIRTUAL) != 0) {
				cached = true;
			}
		}
		parent.rowChanged(this);
		//		int row = getRow();
		//		int columnCount = parent.getColumnCount();
		//		for (int col = 0; col < columnCount; col++) {
		//			getCellItem(row, col).setFont(font.getQFont());
		//		}
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
	 * @since 3.0
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
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		parent.cellChanged(this, index);
		//getCellItem(index).setFont(font.getQFont());
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the
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
	 */
	public void setForeground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Color oldColor = foreground;
		if (color == null) {
			foreground = null;
		} else {
			if (color.equals(foreground)) {
				return;
			}
			foreground = color;
		}
		if (oldColor != null) {
			oldColor.dispose();
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		parent.rowChanged(this);
		//		int row = getRow();
		//		int columnCount = parent.getColumnCount();
		//		QBrush brush = null;
		//		if (foreground != null) {
		//			brush = new QBrush(foreground.getColor());
		//		}
		//		for (int col = 0; col < columnCount; col++) {
		//			getCellItem(row, col).setForeground(brush);
		//		}
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
	 * @since 3.0
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
		if (parent.isVirtual()) {
			cached = true;
		}
		parent.cellChanged(this, index);
		//getCellItem(index).setForeground(new QBrush(QtSWTConverter.convert(color)));
	}

	/**
	 * Sets the grayed state of the checkbox for this item. This state change
	 * only applies if the Table was created with the SWT.CHECK style.
	 * 
	 * @param grayed
	 *            the new grayed state of the checkbox;
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
		if (this.grayed == grayed) {
			return;
		}
		this.grayed = grayed;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		parent.cellChanged(this, 0);
		//getCellItem(0).setCheckState(CheckState.PartiallyChecked);
	}

	/**
	 * Sets the image for multiple columns in the table.
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
		parent.cellChanged(this, index);
		//getCellItem(index).setIcon(image.getQIcon());
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		setImage(0, image);
	}

	/**
	 * Sets the indent of the first column's image, expressed in terms of the
	 * image's width.
	 * 
	 * @param indent
	 *            the new indent
	 * 
	 *            </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @deprecated this functionality is not supported on most platforms
	 */
	@Deprecated
	public void setImageIndent(int indent) {
		checkWidget();
		//TODO
		// if ( indent < 0 )
		// return;
		// if ( imageIndent == indent )
		// return;
		// imageIndent = indent;
		// if ( ( parent.style & SWT.VIRTUAL ) != 0 ) {
		// cached = true;
		// } else {
		// }
	}

	/**
	 * Sets the text for multiple columns in the table.
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
		if (parent.isVirtual()) {
			cached = true;
		}
		parent.cellChanged(this, index);
		//getCellItem(index).setText(string);
	}

	@Override
	public void setText(String string) {
		checkWidget();
		setText(0, string);
	}

	void addColumn(int index, int columnCount) {
		String[] strings = this.strings;
		if (strings != null) {
			String[] temp = new String[columnCount + 1];
			System.arraycopy(strings, 0, temp, 0, index);
			System.arraycopy(strings, index, temp, index + 1, columnCount - index);
			strings = temp;
		}
		Image[] images = this.images;
		if (images != null) {
			Image[] temp = new Image[columnCount + 1];
			System.arraycopy(images, 0, temp, 0, index);
			System.arraycopy(images, index, temp, index + 1, columnCount - index);
			this.images = temp;
		}
		if (index == 0) {
			if (columnCount != 0) {
				if (strings == null) {
					this.strings = new String[columnCount + 1];
					this.strings[1] = text;
				}
				text = ""; //$NON-NLS-1$
				if (images == null) {
					images = new Image[columnCount + 1];
					images[1] = image;
				}
				image = null;
			}
		}
		if (cellBackground != null) {
			int[] cellBackground = this.cellBackground;
			int[] temp = new int[columnCount + 1];
			System.arraycopy(cellBackground, 0, temp, 0, index);
			System.arraycopy(cellBackground, index, temp, index + 1, columnCount - index);
			temp[index] = -1;
			this.cellBackground = temp;
		}
		if (cellForeground != null) {
			int[] cellForeground = this.cellForeground;
			int[] temp = new int[columnCount + 1];
			System.arraycopy(cellForeground, 0, temp, 0, index);
			System.arraycopy(cellForeground, index, temp, index + 1, columnCount - index);
			temp[index] = -1;
			this.cellForeground = temp;
		}
		if (cellFont != null) {
			Font[] cellFont = this.cellFont;
			Font[] temp = new Font[columnCount + 1];
			System.arraycopy(cellFont, 0, temp, 0, index);
			System.arraycopy(cellFont, index, temp, index + 1, columnCount - index);
			this.cellFont = temp;
		}
	}

	void removeColumn(int index, int columnCount) {
		if (columnCount == 0) {
			strings = null;
			images = null;
			cellBackground = null;
			cellForeground = null;
			cellFont = null;
		} else {
			if (strings != null) {
				String[] strings = this.strings;
				if (index == 0) {
					text = strings[1] != null ? strings[1] : ""; //$NON-NLS-1$
				}
				String[] temp = new String[columnCount];
				System.arraycopy(strings, 0, temp, 0, index);
				System.arraycopy(strings, index + 1, temp, index, columnCount - index);
				this.strings = temp;
			} else {
				if (index == 0) {
					text = ""; //$NON-NLS-1$
				}
			}
			if (images != null) {
				Image[] images = this.images;
				if (index == 0) {
					image = images[1];
				}
				Image[] temp = new Image[columnCount];
				System.arraycopy(images, 0, temp, 0, index);
				System.arraycopy(images, index + 1, temp, index, columnCount - index);
				this.images = temp;
			} else {
				if (index == 0) {
					image = null;
				}
			}
			if (cellBackground != null) {
				int[] cellBackground = this.cellBackground;
				int[] temp = new int[columnCount];
				System.arraycopy(cellBackground, 0, temp, 0, index);
				System.arraycopy(cellBackground, index + 1, temp, index, columnCount - index);
				this.cellBackground = temp;
			}
			if (cellForeground != null) {
				int[] cellForeground = this.cellForeground;
				int[] temp = new int[columnCount];
				System.arraycopy(cellForeground, 0, temp, 0, index);
				System.arraycopy(cellForeground, index + 1, temp, index, columnCount - index);
				this.cellForeground = temp;
			}
			if (cellFont != null) {
				Font[] cellFont = this.cellFont;
				Font[] temp = new Font[columnCount];
				System.arraycopy(cellFont, 0, temp, 0, index);
				System.arraycopy(cellFont, index + 1, temp, index, columnCount - index);
				this.cellFont = temp;
			}
		}

	}

	void setSelected(boolean selected) {
		this.selected = selected;
		//		int columns = parent.getColumnCount();
		parent.cellChanged(this, 0);
		//		int row = getRow();
		//		for (int col = 0; col < columns; col++) {
		//			//getCellItem(row, col).setSelected(selected);
		//		}
		if ((parent.style & SWT.SINGLE) != 0 && (parent.style & SWT.RADIO) != 0) {
			radioButton.setChecked(selected);
		}
	}

	public boolean isSelected() {
		return selected;

		//		int columns = parent.getColumnCount();
		//		int row = getRow();
		//		for (int col = 0; col < columns; col++) {
		//			//TODO
		//			//			if (getCellItem(row, col).isSelected()) {
		//			//				return true;
		//			//			}
		//		}
		//		return false;
	}

}
