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

import java.util.List;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.ContextMenuPolicy;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.ToolButtonStyle;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class support the layout of selectable tool bar items.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>ToolItem</code>.
 * </p>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add <code>Control</code> children to it, or set a
 * layout on it.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolBar extends Composite {
	/*
	 * From the Windows SDK for TB_SETBUTTONSIZE:
	 * 
	 * "If an application does not explicitly set the button size, the size
	 * defaults to 24 by 22 pixels".
	 */
	private static final int DEFAULT_WIDTH = 24;
	private static final int DEFAULT_HEIGHT = 22;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
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
	 * @see SWT#FLAT
	 * @see SWT#WRAP
	 * @see SWT#RIGHT
	 * @see SWT#HORIZONTAL
	 * @see SWT#SHADOW_OUT
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass()
	 * @see Widget#getStyle()
	 */
	public ToolBar(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state &= ~CANVAS;
		QToolBar toolbar = new QToolBar();
		initOrientation(toolbar, style);
		toolbar.setStyleSheet("QToolBar { border: 0px; margin: 0px;}"); //$NON-NLS-1$ // background-color: #234; 
		toolbar.setContentsMargins(0, 0, 0, 0);
		toolbar.setIconSize(new QSize(16, 16));
		toolbar.setToolButtonStyle(ToolButtonStyle.ToolButtonTextBesideIcon);
		toolbar.setContextMenuPolicy(ContextMenuPolicy.CustomContextMenu);
		toolbar.setMovable(false);
		toolbar.setFloatable(false);
		toolbar.resize(0, 0);
		// TODO if ( ( style & SWT.RIGHT ) != 0 ) {
		// TODO if ( ( style & SWT.SHADOW_OUT ) != 0 ) {
		// Visually, the tool bar is often separated from the menu bar by a separator.
		// SWT.SHADOW_OUT style was defined for ToolBar. This style causes tool bars to draw the appropriate separator.
		// On the Macintosh and platforms that do not support this look, the separator is not drawn.

		// if ( ( style & SWT.WRAP ) != 0 )
		// qt does not support wrapped mode! :(

		return toolbar;
	}

	private void initOrientation(QToolBar toolbar, int style) {
		if ((style & SWT.VERTICAL) != 0) {
			toolbar.setOrientation(Orientation.Vertical);
			this.style |= SWT.VERTICAL;
		} else {
			this.style |= SWT.HORIZONTAL;
			toolbar.setOrientation(Orientation.Horizontal);
		}
	}

	QToolBar getQToolBar() {
		return (QToolBar) getQWidget();
	}

	void addAction(QAction action, int index) {
		int itemCount = _getItemCount();
		if (index >= 0 && index < itemCount) {
			QAction before = getItems()[index].getQAction();
			getQToolBar().insertAction(before, action);
		} else {
			getQToolBar().addAction(action);
		}
	}

	QAction addWidget(QWidget widget, int index) {
		int itemCount = _getItemCount();
		QAction action;
		if (index >= 0 && index < itemCount) {
			QAction before = getItems()[index].getQAction();
			action = getQToolBar().insertWidget(before, widget);
		} else {
			action = getQToolBar().addWidget(widget);
		}
		return action;
	}

	QWidget removeAction(QAction action) {
		QWidget widget = null;
		if (getQToolBar() != null) {
			widget = getQToolBar().widgetForAction(action);
			getQToolBar().removeAction(action);
		}
		return widget;
	}

	static int checkStyle(int style) {
		/*
		 * On Windows, only flat tool bars can be traversed.
		 */
		if ((style & SWT.FLAT) == 0) {
			style |= SWT.NO_FOCUS;
		}

		/*
		 * A vertical tool bar cannot wrap because TB_SETROWS fails when the
		 * toolbar has TBSTYLE_WRAPABLE.
		 */
		if ((style & SWT.VERTICAL) != 0) {
			style &= ~SWT.WRAP;
		}

		return style;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point size = super.computeSize(wHint, hHint, changed);
		int width = size.x;
		int height = size.y;

		if (width == 0) {
			width = DEFAULT_WIDTH;
		}
		if (height == 0) {
			height = DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		Rectangle trim = computeTrim(0, 0, width, height);
		width = trim.width;
		height = trim.height;
		return new Point(width, height);
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
	 */
	public ToolItem getItem(int index) {
		checkWidget();
		List<QAction> list = getQWidget().actions();
		int count = list.size();
		if (!(0 <= index && index < count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		Widget widget = display.findControl(list.get(index));
		if (widget == null || !ToolItem.class.isInstance(widget)) {
			error(SWT.ERROR_CANNOT_GET_ITEM);
		}
		return (ToolItem) widget;
	}

	/**
	 * Returns the item at the given point in the receiver or null if no such
	 * item exists. The point is in the coordinate system of the receiver.
	 * 
	 * @param point
	 *            the point used to locate the item
	 * @return the item at the given point
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public ToolItem getItem(Point point) {
		checkWidget();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		ToolItem[] items = getItems();
		for (int i = 0; i < items.length; i++) {
			Rectangle rect = items[i].getBounds();
			if (rect.contains(point)) {
				return items[i];
			}
		}
		return null;
	}

	/**
	 * Returns the number of items contained in the receiver.
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
		return _getItemCount();
	}

	int _getItemCount() {
		return getQWidget().actions().size();
	}

	/**
	 * Returns an array of <code>ToolItem</code>s which are the items in the
	 * receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the items in the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public ToolItem[] getItems() {
		checkWidget();
		List<QAction> list = getQWidget().actions();
		int count = list.size();
		if (count == 0) {
			return new ToolItem[0];
		}
		ToolItem[] children = new ToolItem[count];
		int items = 0;
		for (QAction action : list) {
			if (action != null) {
				Widget widget = display.findControl(action);
				if (widget != null && widget != this) {
					if (widget instanceof ToolItem) {
						children[items++] = (ToolItem) widget;
					}
				}
			}
		}
		if (items == count) {
			return children;
		}
		ToolItem[] newChildren = new ToolItem[items];
		System.arraycopy(children, 0, newChildren, 0, items);
		return newChildren;
	}

	/**
	 * Returns the number of rows in the receiver. When the receiver has the
	 * <code>WRAP</code> style, the number of rows can be greater than one.
	 * Otherwise, the number of rows is always one.
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
	public int getRowCount() {
		checkWidget();
		if ((style & SWT.VERTICAL) != 0) {
			return _getItemCount();
		}
		return 1;
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
	 *                <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the tool item has been
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
	public int indexOf(ToolItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return getQToolBar().actions().indexOf(item.getQAction());
	}

	@Override
	boolean mnemonicHit(char ch) {
		//TODO
		//		int key = Display.wcsToMbcs(ch);
		//		int[] id = new int[1];
		//		if (OS.SendMessage(handle, OS.TB_MAPACCELERATOR, key, id) == 0) {
		//			return false;
		//		}
		//		if ((style & SWT.FLAT) != 0 && !setTabGroupFocus())
		//			return false;
		//		int index = (int) /* 64 */OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, id[0], 0);
		//		if (index == -1)
		//			return false;
		//		OS.SendMessage(handle, OS.TB_SETHOTITEM, index, 0);
		//		items[id[0]].click(false);
		return true;
	}

	@Override
	boolean mnemonicMatch(char ch) {
		//TODO
		//		int key = Display.wcsToMbcs(ch);
		//		int[] id = new int[1];
		//		if (OS.SendMessage(handle, OS.TB_MAPACCELERATOR, key, id) == 0) {
		//			return false;
		//		}
		//		/*
		//		 * Feature in Windows. TB_MAPACCELERATOR matches either the mnemonic
		//		 * character or the first character in a tool item. This behavior is
		//		 * undocumented and unwanted. The fix is to ensure that the tool item
		//		 * contains a mnemonic when TB_MAPACCELERATOR returns true.
		//		 */
		//		int index = (int) /* 64 */OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, id[0], 0);
		//		if (index == -1)
		//			return false;
		//		return findMnemonic(items[id[0]].text) != '\0';
		return true;
	}

	@Override
	void releaseChildren(boolean destroy) {
		ToolItem[] items = getItems();
		for (int i = 0; i < items.length; i++) {
			ToolItem item = items[i];
			if (item != null && !item.isDisposed()) {
				item.release(false);
			}
		}
		super.releaseChildren(destroy);
	}

	@Override
	void removeControl(Control control) {
		super.removeControl(control);
		for (ToolItem item : getItems()) {
			if (item != null && item.hasControl(control)) {
				item.setControl(null);
			}
		}
	}

	@Override
	public boolean setParent(Composite parent) {
		checkWidget();
		if (!super.setParent(parent)) {
			return false;
		}
		getQToolBar().setParent(parent.getQWidget());
		return true;
	}

	@Override
	boolean setTabItemFocus() {
		int index = 0;
		ToolItem[] items = getItems();
		while (index < items.length) {
			ToolItem item = items[index];
			if (item != null && (item.style & SWT.SEPARATOR) == 0) {
				if (item.getEnabled()) {
					break;
				}
			}
			index++;
		}
		if (index == items.length) {
			return false;
		}
		return super.setTabItemFocus();
	}

	@Override
	public String toString() {
		String s = getName() + "{items: #" + getItemCount() + ":"; //$NON-NLS-1$ //$NON-NLS-2$
		for (ToolItem item : getItems()) {
			s += item + " " + getQToolBar().widgetForAction(item.getQAction()) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return s + "}"; //$NON-NLS-1$
	}
}
