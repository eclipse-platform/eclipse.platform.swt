/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class represent a column in a table widget.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p>
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
public class TableColumn extends Item {

	private Table parent;
	// TODO implement moveable
	private boolean resizable, moveable;
	private String toolTipText;

	private Point location;

	private int width = -1;
	private int height = -1;

	private Integer horizontalShiftAtCalculation;

	private TableColumnRenderer renderer = new TableColumnRenderer(this);

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>) and a style value describing its behavior and appearance.
	 * The item is added to the end of the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TableColumn(Table parent, int style) {
		super(parent, checkStyle(style));
		resizable = true;
		this.parent = parent;
		parent.createItem(this, parent.getColumnCount());
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>), a style value describing its behavior and appearance,
	 * and the index at which to place it in the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * Note that due to a restriction on some platforms, the first column is always
	 * left aligned.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 * @param index  the zero-relative index to store the receiver in its parent
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the parent (inclusive)</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TableColumn(Table parent, int style, int index) {
		super(parent, checkStyle(style));
		if (index < 0) error(SWT.ERROR_INVALID_RANGE);
		if (index > parent.getColumnCount()) error(SWT.ERROR_INVALID_RANGE);

		resizable = true;
		this.parent = parent;
		parent.createItem(this, index);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the control is moved or resized, by sending it one of the messages defined in
	 * the <code>ControlListener</code> interface.
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see ControlListener
	 * @see #removeControlListener
	 */
	public void addControlListener(ControlListener listener) {
		addTypedListener(listener, SWT.Resize, SWT.Move);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the control is selected by the user, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the column header is selected.
	 * <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the control is
	 *                 selected by the user
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) error(SWT.ERROR_INVALID_SUBCLASS);
	}

	@Override
	void destroyWidget() {
		parent.destroyItem(this);
		releaseHandle();
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code>.
	 *
	 * @return the alignment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getAlignment() {
		checkWidget();
		if ((style & SWT.LEFT) != 0) {
			return SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			return SWT.CENTER;
		}
		if ((style & SWT.RIGHT) != 0) {
			return SWT.RIGHT;
		}
		return SWT.LEFT;
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Table</code>.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Table getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Gets the moveable attribute. A column that is not moveable cannot be
	 * reordered by the user by dragging the header but may be reordered by the
	 * programmer.
	 *
	 * @return the moveable attribute
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Table#getColumnOrder()
	 * @see Table#setColumnOrder(int[])
	 * @see TableColumn#setMoveable(boolean)
	 * @see SWT#Move
	 *
	 * @since 3.1
	 */
	public boolean getMoveable() {
		checkWidget();
		return moveable;
	}

	/**
	 * Gets the resizable attribute. A column that is not resizable cannot be
	 * dragged by the user but may be resized by the programmer.
	 *
	 * @return the resizable attribute
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getResizable() {
		checkWidget();
		return resizable;
	}

	/**
	 * Returns the receiver's tool tip text, or null if it has not been set.
	 *
	 * @return the receiver's tool tip text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.2
	 */
	public String getToolTipText() {
		checkWidget();
		return toolTipText;
	}

	/**
	 * Gets the width of the receiver.
	 *
	 * @return the width
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getWidth() {
		checkWidget();

		return Math.max(this.width, 0);
	}

	int getHeight() {
		checkWidget();

		if (this.height == -1) {
			this.height = TableColumnRenderer.guessColumnHeight(this);
		}

		return this.height;
	}


	/**
	 * Causes the receiver to be resized to its preferred size. For a composite,
	 * this involves computing the preferred size from its layout, if there is one.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void pack() {
		checkWidget();

		Point p = renderer.computeSize();
		this.setWidth(p.x);
		this.setHeight(p.y);

		getParent().redraw();
	}

	@Override
	void releaseHandle() {
		super.releaseHandle();
		parent = null;
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if (parent.sortColumn == this) {
			parent.sortColumn = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is moved or resized.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see ControlListener
	 * @see #addControlListener
	 */
	public void removeControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Move, listener);
		eventTable.unhook(SWT.Resize, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is selected by the user.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Controls how text and images will be displayed in the receiver. The argument
	 * should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code>.
	 * <p>
	 * Note that due to a restriction on some platforms, the first column is always
	 * left aligned.
	 * </p>
	 *
	 * @param alignment the new alignment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setAlignment(int alignment) {
		checkWidget();

		if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
		int index = parent.indexOf(this);
		if (index == -1 || index == 0) return;

		style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);

		super.setImage(image);
		if (parent.sortColumn != this || parent.sortDirection != SWT.NONE) {
			setImage(image, false, false);
		}
	}

	void setImage(Image image, boolean sort, boolean right) {
		int index = parent.indexOf(this);
		if (index == -1) return;
		Table.logNotImplemented();
	}

	/**
	 * Sets the moveable attribute. A column that is moveable can be reordered by
	 * the user by dragging the header. A column that is not moveable cannot be
	 * dragged by the user but may be reordered by the programmer.
	 *
	 * @param moveable the moveable attribute
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Table#setColumnOrder(int[])
	 * @see Table#getColumnOrder()
	 * @see TableColumn#getMoveable()
	 * @see SWT#Move
	 *
	 * @since 3.1
	 */
	public void setMoveable(boolean moveable) {
		checkWidget();
		this.moveable = moveable;
		parent.updateMoveable();
	}

	/**
	 * Sets the resizable attribute. A column that is resizable can be resized by
	 * the user dragging the edge of the header. A column that is not resizable
	 * cannot be dragged by the user but may be resized by the programmer.
	 *
	 * @param resizable the resize attribute
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setResizable(boolean resizable) {
		checkWidget();
		this.resizable = resizable;
	}

	void setSortDirection(int direction) {
		int index = parent.indexOf(this);
		if (index == -1) return;
		Table.logNotImplemented();
	}

	@Override
	public void setText(String string) {
		checkWidget();

		super.setText(string);

		pack();
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default, causing no
	 * tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip. To
	 * display a single '&amp;' in the tool tip, the character '&amp;' can be
	 * escaped by doubling it in the string.
	 * </p>
	 * <p>
	 * NOTE: This operation is a hint and behavior is platform specific, on Windows
	 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip
	 * text are not shown in tooltip.
	 * </p>
	 *
	 * @param string the new tool tip text (or null)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.2
	 */
	public void setToolTipText(String string) {
		checkWidget();
		toolTipText = string;
		long hwndHeaderToolTip = parent.headerToolTipHandle;
		if (hwndHeaderToolTip == 0) {
			parent.createHeaderToolTips();
			parent.updateHeaderToolTips();
		}
	}

	/**
	 * Sets the width of the receiver.
	 *
	 * @param width the new width
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setWidth(int width) {
		checkWidget();

		if (width < 0) return;
		int index = parent.indexOf(this);
		if (index == -1) return;

		this.width = width;

		parent.getColumnsHandler().clearCache();

		if (index < parent.getColumnCount() - 1) {
			for (int i = index + 1; i < parent.getColumnCount(); i++) {
				parent.getColumn(i).clearCache();
			}
		}

		redraw();
	}

	private void clearCache() {
		location = null;
		height = -1;
	}

	private void setHeight(int height) {
		checkWidget();
		setHeightInPixels(DPIUtil.scaleUp(height, 100));
	}

	private void setHeightInPixels(int height) {
		if (height < 0) return;
		int index = parent.indexOf(this);
		if (index == -1) return;

		this.height = height;
	}

	void updateToolTip(int index) {
		Table.logNotImplemented();
	}

	Point getLocation() {
		int horizontalShift = getParent().getHorizontalBar().getSelection();
		if (this.location == null || this.horizontalShiftAtCalculation == null
				|| this.horizontalShiftAtCalculation != horizontalShift) {
			calculateLocation();
		}

		return this.location;
	}

	private void calculateLocation() {
		var index = getParent().indexOf(this);
		if (index == 0) {
			this.horizontalShiftAtCalculation = getParent().getHorizontalBar().getSelection();
			this.location = new Point(-horizontalShiftAtCalculation, 0);
		} else {
			var prevBounds = getParent().getColumn(index - 1).getBounds();
			this.location = new Point(prevBounds.x + prevBounds.width, prevBounds.y);
		}
	}

	void paint(GC gc) {
		renderer.doPaint(gc);
	}

	Rectangle getBounds() {
		Point l = getLocation();
		int width = getWidth();
		int height = getHeight();

		return new Rectangle(l.x, l.y, width, height);
	}

	public void redraw() {
		Rectangle b = getBounds();
		getParent().redraw(b.x, b.y, b.width, b.height, true);
	}
}
