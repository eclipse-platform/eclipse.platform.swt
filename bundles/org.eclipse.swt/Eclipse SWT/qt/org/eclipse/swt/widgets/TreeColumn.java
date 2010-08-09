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

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;

/**
 * Instances of this class represent a column in a tree widget.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize, Selection</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem,
 *      TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * 
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TreeColumn extends Item {
	Tree parent;
	private boolean resizable, moveable;

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code>) and a style value describing its behavior and
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
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeColumn(Tree parent, int style) {
		super(parent, checkStyle(style));
		resizable = true;
		this.parent = parent;
		createQAction(parent, style, parent.getColumnCount());
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code>), a style value describing its behavior and appearance,
	 * and the index at which to place it in the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * Note that due to a restriction on some platforms, the first column is
	 * always left aligned.
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
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeColumn(Tree parent, int style, int index) {
		super(parent, checkStyle(style));
		resizable = true;
		this.parent = parent;
		createQAction(parent, style, index);
	}

	private void adjustColumnCount() {
		int columnCount = parent.getColumnCount();
		if (getQTreeWidget().columnCount() <= columnCount) {
			getQTreeWidget().setColumnCount(columnCount + 1);
		}
	}

	protected void createQAction(Tree parent, int style, int index) {
		adjustColumnCount();
		parent.addColumn(this, index);
		getQHeaderItem().setText(index, ""); //$NON-NLS-1$
	}

	@Override
	void releaseQWidget() {
		//display.removeControl(action);
		super.releaseQWidget();
	}

	QTreeWidget getQTreeWidget() {
		return parent.getQTreeWidget();
	}

	QTreeWidgetItem getQHeaderItem() {
		return getQTreeWidget().headerItem();
	}

	QHeaderView getQHeaderView() {
		return getQTreeWidget().header();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is moved or resized, by sending it one of the messages
	 * defined in the <code>ControlListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ControlListener
	 * @see #removeControlListener
	 */
	public void addControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Resize, typedListener);
		addListener(SWT.Move, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the column header is selected.
	 * <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the control is
	 *            selected by the user
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	void destroyWidget() {
		parent.removeColumn(this);
		super.destroyWidget();
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code>
	 * or <code>CENTER</code>.
	 * 
	 * @return the alignment
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
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

	/**
	 * Gets the moveable attribute. A column that is not moveable cannot be
	 * reordered by the user by dragging the header but may be reordered by the
	 * programmer.
	 * 
	 * @return the moveable attribute
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Tree#getColumnOrder()
	 * @see Tree#setColumnOrder(int[])
	 * @see TreeColumn#setMoveable(boolean)
	 * @see SWT#Move
	 * 
	 * @since 3.2
	 */
	public boolean getMoveable() {
		checkWidget();
		return moveable;
	}

	@Override
	String getNameText() {
		return getText();
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
	 * Gets the resizable attribute. A column that is not resizable cannot be
	 * dragged by the user but may be resized by the programmer.
	 * 
	 * @return the resizable attribute
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
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
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.2
	 */
	public String getToolTipText() {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1) {
			return ""; //$NON-NLS-1$
		}
		return getQHeaderItem().toolTip(index);
	}

	/**
	 * Gets the width of the receiver.
	 * 
	 * @return the width
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getWidth() {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1) {
			return 0;
		}
		return getQTreeWidget().header().sectionSize(index);
	}

	/**
	 * Causes the receiver to be resized to its preferred size. For a composite,
	 * this involves computing the preferred size from its layout, if there is
	 * one.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 */
	public void pack() {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		getQTreeWidget().resizeColumnToContents(index);
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if (parent.sortColumn == this) {
			parent.sortColumn = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is moved or resized.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ControlListener
	 * @see #addControlListener
	 */
	public void removeControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Move, listener);
		eventTable.unhook(SWT.Resize, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Controls how text and images will be displayed in the receiver. The
	 * argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code>.
	 * <p>
	 * Note that due to a restriction on some platforms, the first column is
	 * always left aligned.
	 * </p>
	 * 
	 * @param alignment
	 *            the new alignment
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setAlignment(int alignment) {
		checkWidget();
		if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) {
			return;
		}
		int index = parent.indexOf(this);
		if (index == -1 || index == 0) {
			return;
		}
		style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);

		getQTreeWidget().headerItem().setTextAlignment(index, convertAlignment(style));
	}

	private int convertAlignment(int style) {
		if ((style & SWT.CENTER) != 0) {
			return AlignmentFlag.AlignCenter.value();
		} else if ((style & SWT.RIGHT) != 0) {
			return AlignmentFlag.AlignRight.value();
		}
		return AlignmentFlag.AlignLeft.value();
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		super.setImage(image);
		if (parent.sortColumn != this || parent.sortDirection != SWT.NONE) {
			setImage(image, false, false);
		}
	}

	void setImage(Image image, boolean sort, boolean right) {
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		if (image != null) {
			QIcon icon = new QIcon();
			icon.addPixmap(image.getQPixmap());
			getQHeaderItem().setIcon(index, icon);
		} else {
			getQHeaderItem().setIcon(index, (QIcon) null);
		}
	}

	/**
	 * Sets the moveable attribute. A column that is moveable can be reordered
	 * by the user by dragging the header. A column that is not moveable cannot
	 * be dragged by the user but may be reordered by the programmer.
	 * 
	 * @param moveable
	 *            the moveable attribute
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Tree#setColumnOrder(int[])
	 * @see Tree#getColumnOrder()
	 * @see TreeColumn#getMoveable()
	 * @see SWT#Move
	 * 
	 * @since 3.2
	 */
	public void setMoveable(boolean moveable) {
		checkWidget();
		this.moveable = moveable;
	}

	/**
	 * Sets the resizable attribute. A column that is not resizable cannot be
	 * dragged by the user but may be resized by the programmer.
	 * 
	 * @param resizable
	 *            the resize attribute
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setResizable(boolean resizable) {
		checkWidget();
		this.resizable = resizable;
		getQHeaderView().setResizeMode(parent.indexOf(this), resizable ? ResizeMode.Interactive : ResizeMode.Fixed);
	}

	@Override
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (string.equals(text)) {
			return;
		}
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		super.setText(string);
		getQHeaderItem().setText(index, string);
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default,
	 * causing no tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool
	 * tip. To display a single '&amp;' in the tool tip, the character '&amp;'
	 * can be escaped by doubling it in the string.
	 * </p>
	 * 
	 * @param string
	 *            the new tool tip text (or null)
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
	public void setToolTipText(String string) {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		getQHeaderItem().setToolTip(index, string);
	}

	/**
	 * Sets the width of the receiver.
	 * 
	 * @param width
	 *            the new width
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setWidth(int width) {
		checkWidget();
		if (width < 0) {
			return;
		}
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		getQTreeWidget().setColumnWidth(index, width);
	}

}
