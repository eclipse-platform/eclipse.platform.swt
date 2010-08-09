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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.LayoutDirection;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QActionGroup;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QPalette.ColorRole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class are user interface objects that contain menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show</dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified. Only one of
 * LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Menu extends Widget {
	private int x, y;
	private boolean hasLocation;
	private Color foreground = null;
	private Color background = null;
	private Image backgroundImage;
	MenuItem cascade;
	Decorations parent;
	private QActionGroup actionGroup;

	/**
	 * Constructs a new instance of this class given its parent, and sets the
	 * style for the instance so that the instance will be a popup menu on the
	 * given parent's shell.
	 * <p>
	 * After constructing a menu, it can be set into its parent using
	 * <code>parent.setMenu(menu)</code>. In this case, the parent may be any
	 * control in the same widget tree as the parent.
	 * </p>
	 * 
	 * @param parent
	 *            a control which will be the parent of the new instance (cannot
	 *            be null)
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
	 * @see SWT#POP_UP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Control parent) {
		this(checkNull(parent).menuShell(), null, SWT.POP_UP);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Decorations</code>) and a style value describing its behavior and
	 * appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * After constructing a menu or menuBar, it can be set into its parent using
	 * <code>parent.setMenu(menu)</code> or
	 * <code>parent.setMenuBar(menuBar)</code>.
	 * </p>
	 * 
	 * @param parent
	 *            a decorations control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of menu to construct
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
	 * @see SWT#BAR
	 * @see SWT#DROP_DOWN
	 * @see SWT#POP_UP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Decorations parent, int style) {
		this(parent, null, checkStyle(style));
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Menu</code>) and sets the style for the instance so that the
	 * instance will be a drop-down menu on the given parent's parent.
	 * <p>
	 * After constructing a drop-down menu, it can be set into its parentMenu
	 * using <code>parentMenu.setMenu(menu)</code>.
	 * </p>
	 * 
	 * @param parentMenu
	 *            a menu which will be the parent of the new instance (cannot be
	 *            null)
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
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Menu parentMenu) {
		this(checkNull(parentMenu).parent, parentMenu.getQWidget(), SWT.DROP_DOWN);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>MenuItem</code>) and sets the style for the instance so that the
	 * instance will be a drop-down menu on the given parent's parent menu.
	 * <p>
	 * After constructing a drop-down menu, it can be set into its parentItem
	 * using <code>parentItem.setMenu(menu)</code>.
	 * </p>
	 * 
	 * @param parentItem
	 *            a menu item which will be the parent of the new instance
	 *            (cannot be null)
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
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(MenuItem parentItem) {
		this(checkNull(parentItem).parent);
	}

	Menu(Decorations parent, QWidget qParent, int style) {
		super(parent, checkStyle(style));
		display = parent.display;
		createWidget(parent, qParent, style);
	}

	protected void createWidget(Decorations parent, QWidget qParent, int style) {
		state |= DRAG_DETECT;
		this.parent = parent;
		checkAndUpdateOrientation(parent);
		QWidget qtControl = createQtControl(qParent != null ? qParent : parent.getQWidget());
		if (qtControl == null) {
			error(SWT.ERROR_UNSPECIFIED);
		}
		setQWidget(qtControl);
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			qtControl.setLayoutDirection(LayoutDirection.RightToLeft);
		}
		register();
		if ((state & PARENT_BACKGROUND) != 0) {
			updateBackground();
		}
		parent.addMenu(this);
	}

	protected QWidget createQtControl(QWidget parent) {
		state &= ~CANVAS;
		if (isMenuBar()) {
			return new QMenuBar(parent);
		}
		return new QMenu(parent);
	}

	QMenuBar getQMenuBar() {
		return (QMenuBar) getQWidget();
	}

	QMenu getQMenu() {
		return (QMenu) getQWidget();
	}

	void addToActionGroup(QAction action) {
		initActionGroup();
		actionGroup.addAction(action);
	}

	void removeFromActionGroup(QAction action) {
		if (hasActionGroup()) {
			actionGroup.removeAction(action);
		}
	}

	private boolean hasActionGroup() {
		return actionGroup != null;
	}

	private void initActionGroup() {
		if (actionGroup == null) {
			actionGroup = new QActionGroup(getQWidget());
			if ((parent.style & SWT.NO_RADIO_GROUP) != 0) {
				actionGroup.setExclusive(false);
			}
		}
	}

	void register() {
		display.addControl(getQWidget(), this);
	}

	void deregister() {
		display.removeControl(getQWidget());
	}

	void _setVisible(boolean visible) {
		if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) {
			return;
		}
		getQWidget().setHidden(!visible);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when help events are generated for the control, by sending it one of the
	 * messages defined in the <code>HelpListener</code> interface.
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
	 * @see HelpListener
	 * @see #removeHelpListener
	 */
	public void addHelpListener(HelpListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Help, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when menus are hidden or shown, by sending it one of the messages defined
	 * in the <code>MenuListener</code> interface.
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
	 * @see MenuListener
	 * @see #removeMenuListener
	 */
	public void addMenuListener(MenuListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Hide, typedListener);
		addListener(SWT.Show, typedListener);
	}

	static Control checkNull(Control control) {
		if (control == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return control;
	}

	static Menu checkNull(Menu menu) {
		if (menu == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return menu;
	}

	static MenuItem checkNull(MenuItem item) {
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return item;
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
	}

	void addAction(QAction action, int index) {
		QWidget menu = getQWidget();
		int itemCount = _getItemCount();
		if (index >= 0 && index < itemCount) {
			MenuItem[] items = getItems();
			QAction before = items[index].getQAction();
			menu.insertAction(before, action);
		} else {
			menu.addAction(action);
		}
	}

	void removeAction(QAction action) {
		if (isMenuBar()) {
			getQMenuBar().removeAction(action);
		} else {
			getQMenu().removeAction(action);
		}
	}

	boolean isMenuBar() {
		return (style & SWT.BAR) != 0;
	}

	Color defaultBackground() {
		return display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	Color defaultForeground() {
		return display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
	}

	void fixMenus(Decorations newParent) {
		MenuItem[] items = getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].fixMenus(newParent);
		}
		parent.removeMenu(this);
		newParent.addMenu(this);
		this.parent = newParent;
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
	 * @since 3.3
	 */
	/* public */Color getBackground() {
		checkWidget();
		return background != null ? Color.qt_new(display, background.getColor()) : defaultBackground();
	}

	/**
	 * Returns the receiver's background image.
	 * 
	 * @return the background image
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
	/* public */Image getBackgroundImage() {
		checkWidget();
		return backgroundImage;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent (or its display if its parent is null), unless the receiver
	 * is a menu or a shell. In this case, the location is relative to the
	 * display.
	 * <p>
	 * Note that the bounds of a menu or menu item are undefined when the menu
	 * is not visible. This is because most platforms compute the bounds of a
	 * menu dynamically just before it is displayed.
	 * </p>
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
	 * @since 3.1
	 */
	/* public */Rectangle getBounds() {
		checkWidget();
		return QtSWTConverter.convert(getQWidget().frameGeometry());
	}

	/**
	 * Returns the default menu item or null if none has been previously set.
	 * 
	 * @return the default menu item.
	 * 
	 *         </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public MenuItem getDefaultItem() {
		checkWidget();
		// if ( OS.IsWinCE )
		// return null;
		// int id = OS.GetMenuDefaultItem( handle, OS.MF_BYCOMMAND,
		// OS.GMDI_USEDISABLED );
		// if ( id == -1 )
		// return null;
		// MENUITEMINFO info = new MENUITEMINFO();
		// info.cbSize = MENUITEMINFO.sizeof;
		// info.fMask = OS.MIIM_ID;
		// if ( OS.GetMenuItemInfo( handle, id, false, info ) ) {
		// return display.getMenuItem( info.wID );
		// }
		return null;
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and
	 * <code>false</code> otherwise. A disabled menu is typically not selectable
	 * from the user interface and draws with an inactive or "grayed" look.
	 * 
	 * @return the receiver's enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		checkWidget();
		return getQMenuBar().isEnabled();
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
	 */
	/* public */Color getForeground() {
		checkWidget();
		return foreground != null ? Color.qt_new(display, foreground.getColor()) : defaultForeground();
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
	public MenuItem getItem(int index) {
		checkWidget();
		List<QAction> list = getQWidget().actions();
		int count = list.size();
		if (!(0 <= index && index < count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		Widget widget = display.findControl(list.get(index));
		if (widget == null || !MenuItem.class.isInstance(widget)) {
			error(SWT.ERROR_CANNOT_GET_ITEM);
		}
		return (MenuItem) widget;
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

	/**
	 * Returns a (possibly empty) array of <code>MenuItem</code>s which are the
	 * items in the receiver.
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
	public MenuItem[] getItems() {
		checkWidget();
		List<QAction> list = getQWidget().actions();
		int count = list.size();
		if (count == 0) {
			return new MenuItem[0];
		}
		MenuItem[] children = new MenuItem[count];
		int items = 0;
		for (QAction action : list) {
			if (action != null) {
				Widget widget = display.findControl(action);
				if (widget != null && widget != this) {
					if (widget instanceof MenuItem) {
						children[items++] = (MenuItem) widget;
					}
				}
			}
		}
		if (items == count) {
			return children;
		}
		MenuItem[] newChildren = new MenuItem[items];
		System.arraycopy(children, 0, newChildren, 0, items);
		return newChildren;
	}

	int _getItemCount() {
		return getQWidget().actions().size();
	}

	@Override
	String getNameText() {
		String result = "";//$NON-NLS-1$
		MenuItem[] items = getItems();
		int length = items.length;
		if (length > 0) {
			for (int i = 0; i < length - 1; i++) {
				result = result + items[i].getNameText() + ", ";//$NON-NLS-1$
			}
			result = result + items[length - 1].getNameText();
		}
		return result;
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Decorations</code>.
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
	public Decorations getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>MenuItem</code>
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
	public MenuItem getParentItem() {
		checkWidget();
		return cascade;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>Menu</code> or
	 * null when the receiver is a root.
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
	public Menu getParentMenu() {
		checkWidget();
		if (cascade != null) {
			return cascade.parent;
		}
		return null;
	}

	/**
	 * Returns the receiver's shell. For all controls other than shells, this
	 * simply returns the control's nearest ancestor shell. Shells return
	 * themselves, even if they are children of other shells.
	 * 
	 * @return the receiver's shell
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getParent
	 */
	public Shell getShell() {
		checkWidget();
		return parent.getShell();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getVisible() {
		checkWidget();
		if ((style & SWT.BAR) != 0) {
			return this == parent.menuShell().menuBar;
		}
		if ((style & SWT.POP_UP) != 0) {
			Menu[] popups = display.popups;
			if (popups == null) {
				return false;
			}
			for (int i = 0; i < popups.length; i++) {
				if (popups[i] == this) {
					return true;
				}
			}
		}
		Shell shell = getShell();
		Menu menu = shell.activeMenu;
		while (menu != null && menu != this) {
			menu = menu.getParentMenu();
		}
		return this == menu;
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
	public int indexOf(MenuItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (item.parent != this) {
			return -1;
		}
		List<QAction> actions = getQWidget().actions();
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i) == item.getQAction()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled menu is typically not selectable from the user interface and
	 * draws with an inactive or "grayed" look.
	 * 
	 * @return the receiver's enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		checkWidget();
		Menu parentMenu = getParentMenu();
		if (parentMenu == null) {
			return getEnabled() && parent.isEnabled();
		}
		return getEnabled() && parentMenu.isEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all of the
	 * receiver's ancestors are visible and <code>false</code> otherwise.
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getVisible
	 */
	public boolean isVisible() {
		checkWidget();
		return getVisible();
	}

	void redraw() {
		if (!isVisible()) {
			return;
		}
		if ((style & SWT.BAR) != 0) {
			display.addBar(this);
		}
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		cascade = null;
	}

	@Override
	void releaseChildren(boolean destroy) {
		MenuItem[] items = getItems();
		for (int i = 0; i < items.length; i++) {
			MenuItem item = items[i];
			if (item != null && !item.isDisposed()) {
				item.release(false);
			}
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if ((style & SWT.BAR) != 0) {
			display.removeBar(this);
			if (this == parent.menuBar) {
				parent.setMenuBar(null);
			}
		} else {
			if ((style & SWT.POP_UP) != 0) {
				display.removePopup(this);
			}
		}
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		backgroundImage = null;
		if (parent != null) {
			parent.removeMenu(this);
		}
		parent = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the help events are generated for the control.
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
	 * @see HelpListener
	 * @see #addHelpListener
	 */
	public void removeHelpListener(HelpListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Help, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the menu events are generated for the control.
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
	 * @see MenuListener
	 * @see #addMenuListener
	 */
	public void removeMenuListener(MenuListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Hide, listener);
		eventTable.unhook(SWT.Show, listener);
	}

	/**
	 * Sets the receiver's background color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
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
	 * @since 3.3
	 */
	/* public */void setBackground(Color color) {
		checkWidget();
		if (color != null) {
			if (color.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
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
		updateBackground();
	}

	/**
	 * Sets the receiver's background image to the image specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null. The background image is tiled to fill the available space.
	 * 
	 * @param image
	 *            the new image (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument is not a
	 *                bitmap</li>
	 *                </ul>
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
	/* public */void setBackgroundImage(Image image) {
		checkWidget();
		if (image != null) {
			if (image.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (!image.isBitmap()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (backgroundImage == image) {
			return;
		}
		backgroundImage = image;
		updateBackground();
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
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
	 * @since 3.3
	 */
	/* public */void setForeground(Color color) {
		checkWidget();
		if (color != null) {
			if (color.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
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
		applyForegroundColor(foreground);
	}

	/**
	 * Sets the default menu item to the argument or removes the default
	 * emphasis when the argument is <code>null</code>.
	 * 
	 * @param item
	 *            the default menu item or null
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the menu item has been
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
	public void setDefaultItem(MenuItem item) {
		checkWidget();
		if (item != null) {
			if (item.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (item.parent != this) {
				return;
			}
			if (!isMenuBar()) {
				getQMenu().setDefaultAction(item.getQAction());
			}
		} else {
			if (!isMenuBar()) {
				getQMenu().setDefaultAction(null);
			}
		}
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise. A disabled menu is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 * 
	 * @param enabled
	 *            the new enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setEnabled(boolean enabled) {
		checkWidget();
		getQWidget().setEnabled(enabled);
		getQWidget().update();
	}

	/**
	 * Sets the location of the receiver, which must be a popup, to the point
	 * specified by the arguments which are relative to the display.
	 * <p>
	 * Note that this is different from most widgets where the location of the
	 * widget is relative to the parent.
	 * </p>
	 * <p>
	 * Note that the platform window manager ultimately has control over the
	 * location of popup menus.
	 * </p>
	 * 
	 * @param x
	 *            the new x coordinate for the receiver
	 * @param y
	 *            the new y coordinate for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLocation(int x, int y) {
		checkWidget();
		if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) {
			return;
		}
		this.x = x;
		this.y = y;
		hasLocation = true;
	}

	/**
	 * Sets the location of the receiver, which must be a popup, to the point
	 * specified by the argument which is relative to the display.
	 * <p>
	 * Note that this is different from most widgets where the location of the
	 * widget is relative to the parent.
	 * </p>
	 * <p>
	 * Note that the platform window manager ultimately has control over the
	 * location of popup menus.
	 * </p>
	 * 
	 * @param location
	 *            the new location for the receiver
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
	 * @since 2.1
	 */
	public void setLocation(Point location) {
		checkWidget();
		if (location == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		setLocation(location.x, location.y);
	}

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>, and
	 * marks it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause
	 * it to be displayed.
	 * </p>
	 * 
	 * @param visible
	 *            the new visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setVisible(boolean visible) {
		checkWidget();
		if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) {
			return;
		}
		if (visible) {
			display.addPopup(this);
			if (hasLocation) {
				getQMenu().move(x, y);
			}
			getQMenu().setVisible(true);
		} else {
			display.removePopup(this);
			_setVisible(false);
		}
	}

	@Override
	public boolean qtHideEvent(QObject source) {
		if (source == getQWidget()) {
			if (!isDisposed()) {
				sendEvent(SWT.Hide);
			}
		}
		return super.qtHideEvent(source);
	}

	@Override
	public boolean qtShowEvent(QObject source) {
		if (source == getQWidget()) {
			if (!isDisposed()) {
				sendEvent(SWT.Show);
			}
		}
		return super.qtShowEvent(source);
	}

	void updateBackground() {
		if (backgroundImage != null) {
			applyBackgroundImage(backgroundImage);
			return;
		}
		if (background != null) {
			applyBackgroundColor(background);
			return;
		}
	}

	private void applyBackgroundColor(Color color) {
		updatedPalette(color, getBackgroundColorRoles());
		updateAutoFillBackground();
	}

	private void updatedPalette(Color color, ColorRole[] colorRoles) {
		QPalette palette = getQWidget().palette();
		if (color != null) {
			QColor qColor = new QColor(color.getColor());
			for (ColorRole role : colorRoles) {
				palette.setColor(role, qColor);
			}
		} else {
			QPalette defaultPalette = QApplication.palette();
			for (ColorRole role : colorRoles) {
				QBrush brush = defaultPalette.brush(role);
				palette.setBrush(role, brush);
			}
		}
		getQWidget().setPalette(palette);
	}

	private QPalette.ColorRole[] getBackgroundColorRoles() {
		return new QPalette.ColorRole[] { QPalette.ColorRole.Window, QPalette.ColorRole.Base, QPalette.ColorRole.Button };
	}

	private void updateAutoFillBackground() {
		if (background != null || backgroundImage != null) {
			getQWidget().setAutoFillBackground(true);
		}
	}

	private void applyBackgroundImage(Image image) {
		QPalette palette = getQWidget().palette().clone();
		try {
			setPaletteBgImage(palette, image);
			getQWidget().setPalette(palette);
		} finally {
			palette.dispose();
		}
		updateAutoFillBackground();
	}

	private void setPaletteBgImage(QPalette palette, Image image) {
		ColorRole[] bkRoles = getBackgroundImageRoles();
		if (image != null) {
			for (ColorRole bkRole : bkRoles) {
				palette.setBrush(bkRole, new QBrush(image.getQPixmap()));
			}
		} else {
			QPalette defaultPalette = QApplication.palette();
			for (ColorRole role : bkRoles) {
				QBrush brush = defaultPalette.brush(role);
				palette.setBrush(role, brush.clone());
			}
		}
	}

	private ColorRole[] getBackgroundImageRoles() {
		return new QPalette.ColorRole[] { QPalette.ColorRole.Window, QPalette.ColorRole.Base };
	}

	private void applyForegroundColor(Color color) {
		updatedPalette(color, getForegroundColorRoles());
	}

	private ColorRole[] getForegroundColorRoles() {
		return new ColorRole[] { QPalette.ColorRole.WindowText, QPalette.ColorRole.Text, QPalette.ColorRole.ButtonText };
	}
}
