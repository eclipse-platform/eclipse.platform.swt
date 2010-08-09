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

import java.util.ArrayList;

import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QSizePolicy.Policy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class implement the notebook user interface metaphor. It
 * allows the user to select a notebook page from set of pages.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>TabItem</code>. <code>Control</code> children are created and then
 * set into a tab item using <code>TabItem#setControl</code>.
 * </p>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to set a layout on it.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder,
 *      TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
	private java.util.List<TabItem> items;
	private java.util.List<QWidget> tabWidgets;

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
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TabFolder(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state &= ~(CANVAS | THEME_BACKGROUND);
		items = new ArrayList<TabItem>(4);
		tabWidgets = new ArrayList<QWidget>(4);
		return new QTabWidget();
	}

	@Override
	protected void connectSignals() {
		getQTabWidget().currentChanged.connect(this, "selectionEvent(int)");//$NON-NLS-1$
	}

	QTabWidget getQTabWidget() {
		return (QTabWidget) getQWidget();
	}

	protected void selectionEvent(int index) {
		if (index != -1 && items != null) {
			TabItem item = getItem(index);
			Event event = new Event();
			event.item = item;
			sendEvent(SWT.Selection, event);
		}
	}

	protected void addQChild() {
		// do nothing
	}

	static int checkStyle(int style) {
		style = checkBits(style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);

		/*
		 * Even though it is legal to create this widget with scroll bars, they
		 * serve no useful purpose because they do not automatically scroll the
		 * widget's client area. The fix is to clear the SWT style.
		 */
		return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	void createItem(TabItem item, int index) {
		int count = getQTabWidget().count();
		if (!(0 <= index && index <= count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}

		items.add(index, item);

		QWidget widget = new QWidget();
		widget.setSizePolicy(Policy.Expanding, Policy.Expanding);
		widget.setContentsMargins(0, 0, 0, 0);
		QVBoxLayout layout = new QVBoxLayout();
		layout.setContentsMargins(0, 0, 0, 0);
		widget.setLayout(layout);

		tabWidgets.add(index, widget);

		getQTabWidget().insertTab(index, widget, item.getText());

		/*
		 * Send a selection event when the item that is added becomes the new
		 * selection. This only happens when the first item is added.
		 */
		if (index == 0) {
			Event event = new Event();
			event.item = item;
			sendEvent(SWT.Selection, event);
		}
	}

	void updateItem(TabItem item, Control oldControl) {
		int index = indexOf(item);
		QWidget container = tabWidgets.get(index);

		if (oldControl != null) {
			QWidget widget = oldControl.getQMasterWidget();
			if (widget != null) {
				container.layout().removeWidget(widget);
			}
		}

		Control control = item.getControl();
		QWidget widget = null;
		if (control != null) {
			widget = control.getQMasterWidget();
		}
		if (widget == null) {
			return;
		}
		container.layout().addWidget(widget);
	}

	void destroyItem(TabItem item) {
		int count = getQTabWidget().count();
		int index = 0;
		while (index < count) {
			if (items.get(index) == item) {
				break;
			}
			index++;
		}
		if (index == count) {
			return;
		}
		int selectionIndex = getQTabWidget().currentIndex();
		items.remove(count);
		if (count > 0 && index == selectionIndex) {
			setSelection(Math.max(0, selectionIndex - 1), true);
		}
	}

	@Override
	Control findThemeControl() {
		/* It is not possible to change the background of this control */
		return this;
	}

	@Override
	public Rectangle getClientArea() {
		checkWidget();
		return QtSWTConverter.convert(getQTabWidget().contentsRect());
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's selection, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the item field of the event
	 * object is valid. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the user changes
	 *            the receiver's selection
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
	public TabItem getItem(int index) {
		checkWidget();
		int count = getQTabWidget().count();
		if (!(0 <= index && index < count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		return items.get(index);
	}

	/**
	 * Returns the tab item at the given point in the receiver or null if no
	 * such item exists. The point is in the coordinate system of the receiver.
	 * 
	 * @param point
	 *            the point used to locate the item
	 * @return the tab item at the given point, or null if the point is not in a
	 *         tab item
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
	 * 
	 * @since 3.4
	 */
	public TabItem getItem(Point point) {
		checkWidget();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		QWidget widget = getQTabWidget().childAt(point.x, point.y);
		if (widget == null) {
			return null;
		}
		int index = getQTabWidget().indexOf(widget);
		if (index < 0 || index >= items.size()) {
			return null;
		}
		return items.get(index);
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
		return getQTabWidget().count();
	}

	/**
	 * Returns an array of <code>TabItem</code>s which are the items in the
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
	public TabItem[] getItems() {
		checkWidget();
		int count = getQTabWidget().count();
		TabItem[] result = new TabItem[count];
		System.arraycopy(items, 0, result, 0, count);
		return result;
	}

	/**
	 * Returns an array of <code>TabItem</code>s that are currently selected in
	 * the receiver. An empty array indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return an array representing the selection
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TabItem[] getSelection() {
		checkWidget();
		int index = getQTabWidget().currentIndex();
		if (index == -1) {
			return new TabItem[0];
		}
		return new TabItem[] { items.get(index) };
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected
	 * in the receiver, or -1 if no item is selected.
	 * 
	 * @return the index of the selected item
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelectionIndex() {
		checkWidget();
		return getQTabWidget().currentIndex();
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
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int indexOf(TabItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int count = getQTabWidget().count();
		for (int i = 0; i < count; i++) {
			if (items.get(i) == item) {
				return i;
			}
		}
		return -1;
	}

	@Override
	Point minimumSize(int wHint, int hHint, boolean flushCache) {
		Control[] children = _getChildren();
		int width = 0, height = 0;
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			int index = 0;
			int count = getChildrenCount();
			while (index < count) {
				if (items.get(index).control == child) {
					break;
				}
				index++;
			}
			if (index == count) {
				Rectangle rect = child.getBounds();
				width = Math.max(width, rect.x + rect.width);
				height = Math.max(height, rect.y + rect.height);
			} else {
				Point size = child.computeSize(wHint, hHint, flushCache);
				width = Math.max(width, size.x);
				height = Math.max(height, size.y);
			}
		}
		return new Point(width, height);
	}

	@Override
	boolean mnemonicHit(char key) {
		for (int i = 0; i < items.size(); i++) {
			TabItem item = items.get(i);
			if (item != null) {
				char ch = findMnemonic(item.getText());
				if (Character.toUpperCase(key) == Character.toUpperCase(ch)) {
					if (forceFocus()) {
						if (i != getSelectionIndex()) {
							setSelection(i, true);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	boolean mnemonicMatch(char key) {
		for (int i = 0; i < items.size(); i++) {
			TabItem item = items.get(i);
			if (item != null) {
				char ch = findMnemonic(item.getText());
				if (Character.toUpperCase(key) == Character.toUpperCase(ch)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (items != null) {
			int count = getQTabWidget().count();
			for (int i = 0; i < count; i++) {
				TabItem item = items.get(i);
				if (item != null && !item.isDisposed()) {
					item.release(false);
				}
			}
			items = null;
		}
		super.releaseChildren(destroy);
	}

	@Override
	void removeControl(Control control) {
		super.removeControl(control);
		int count = getQTabWidget().count();
		for (int i = 0; i < count; i++) {
			TabItem item = items.get(i);
			if (item.control == control) {
				getQTabWidget().removeTab(i);
				item.setControl(null);
			}
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's selection.
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
	 * Sets the receiver's selection to the given item. The current selected is
	 * first cleared, then the new item is selected.
	 * 
	 * @param item
	 *            the item to select
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                </ul>
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
	public void setSelection(TabItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSelection(new TabItem[] { item });
	}

	/**
	 * Sets the receiver's selection to be the given array of items. The current
	 * selected is first cleared, then the new items are selected.
	 * 
	 * @param items
	 *            the array of items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(TabItem[] items) {
		checkWidget();
		if (items == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (items.length == 0) {
			setSelection(-1, false);
		} else {
			for (int i = items.length - 1; i >= 0; --i) {
				int index = indexOf(items[i]);
				if (index != -1) {
					setSelection(index, false);
				}
			}
		}
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		Rectangle oldRect = getClientArea();
		super.setFont(font);
		Rectangle newRect = getClientArea();
		if (!oldRect.equals(newRect)) {
			//sendResize();
			int index = getQTabWidget().currentIndex();
			if (index != -1) {
				TabItem item = items.get(index);
				Control control = item.control;
				if (control != null && !control.isDisposed()) {
					control.setBounds(getClientArea());
				}
			}
		}
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. If the
	 * item at the index was already selected, it remains selected. The current
	 * selection is first cleared, then the new items are selected. Indices that
	 * are out of range are ignored.
	 * 
	 * @param index
	 *            the index of the item to select
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(int index) {
		checkWidget();
		int count = getQTabWidget().count();
		if (!(0 <= index && index < count)) {
			return;
		}
		setSelection(index, false);
	}

	void setSelection(int index, boolean notify) {
		int oldIndex = getQTabWidget().currentIndex();
		if (oldIndex == index) {
			return;
		}
		if (oldIndex != -1) {
			TabItem item = items.get(oldIndex);
			Control control = item.control;
			if (control != null && !control.isDisposed()) {
				control.setVisible(false);
			}
		}
		getQTabWidget().setCurrentIndex(index);
		int newIndex = getQTabWidget().currentIndex();
		if (newIndex != -1) {
			TabItem item = items.get(newIndex);
			Control control = item.control;
			if (control != null && !control.isDisposed()) {
				control.setBounds(getClientArea());
				control.setVisible(true);
			}
			if (notify) {
				Event event = new Event();
				event.item = item;
				sendEvent(SWT.Selection, event);
			}
		}
	}

	@Override
	boolean traversePage(boolean next) {
		int count = getItemCount();
		if (count <= 1) {
			return false;
		}
		int index = getSelectionIndex();
		if (index == -1) {
			index = 0;
		} else {
			int offset = next ? 1 : -1;
			index = (index + offset + count) % count;
		}
		setSelection(index, true);
		if (index == getSelectionIndex()) {
			// TODO
			//OS.SendMessage(handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			return true;
		}
		return false;
	}

}
