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

import java.util.HashMap;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CHECK, CASCADE, PUSH, RADIO, SEPARATOR</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Arm, Help, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, CASCADE, PUSH, RADIO and SEPARATOR may be
 * specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class MenuItem extends Item {
	private QAction action;
	Menu parent, menu;
	int id, accelerator;

	private HashMap<Integer, Integer> modifiers = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> accelerators = new HashMap<Integer, Integer>();

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Menu</code>) and a style value describing its behavior and
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
	 *            a menu control which will be the parent of the new instance
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
	 * @see SWT#CHECK
	 * @see SWT#CASCADE
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public MenuItem(Menu parent, int style) {
		super(parent, checkStyle(style));
		this.parent = parent;
		createAction(parent, style, -1);
		initializeKeys();
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Menu</code>), a style value describing its behavior and appearance,
	 * and the index at which to place it in the items maintained by its parent.
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
	 *            a menu control which will be the parent of the new instance
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
	 * @see SWT#CHECK
	 * @see SWT#CASCADE
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public MenuItem(Menu parent, int style, int index) {
		super(parent, checkStyle(style));
		this.parent = parent;
		createAction(parent, style, index);
		initializeKeys();
	}

	MenuItem(Menu parent, Menu menu, int style, int index) {
		super(parent, checkStyle(style));
		this.parent = parent;
		this.menu = menu;
		if (menu != null) {
			menu.cascade = this;
		}
		display.addMenuItem(this);
		initializeKeys();
	}

	protected void createAction(Menu parent, int style, int index) {
		action = new QAction(parent.getQWidget());

		parent.addAction(action, index);
		int bits = SWT.CHECK | SWT.RADIO | SWT.PUSH | SWT.SEPARATOR;
		switch (style & bits) {
		case SWT.SEPARATOR:
			action.setSeparator(true);
			break;
		case SWT.RADIO:
			action.setCheckable(true);
			parent.addToActionGroup(action);
			break;
		case SWT.CHECK:
			action.setCheckable(true);
			break;
		case SWT.PUSH:
		default:
			break;
		}
		connectSignals();
		display.addControl(action, this);
	}

	QAction getQAction() {
		return action;
	}

	protected void connectSignals() {
		if ((style & SWT.CASCADE) == 0) {
			getQAction().triggered.connect(this, "triggerEvent(boolean)");//$NON-NLS-1$
		}
		getQAction().hovered.connect(this, "hoverEvent()");//$NON-NLS-1$
	}

	protected void triggerEvent(boolean triggered) {
		Event event = new Event();
		sendEvent(SWT.Selection, event);
	}

	protected void hoverEvent() {
		sendEvent(SWT.Arm);
	}

	/**
	 * Initialize the hashmaps with the SWT keys mapping to QT keys. This is
	 * needed for the accelerator.
	 */
	private void initializeKeys() {

		modifiers.put(SWT.CTRL, Qt.Modifier.CTRL.value()); // 262144, 67108864
		modifiers.put(SWT.MOD1, Qt.Modifier.CTRL.value()); // 262144, 67108864
		modifiers.put(SWT.SHIFT, Qt.Modifier.SHIFT.value());
		modifiers.put(SWT.MOD2, Qt.Modifier.SHIFT.value());
		modifiers.put(SWT.ALT, Qt.Modifier.ALT.value());

		accelerators.put(SWT.ARROW_DOWN, Qt.Key.Key_Down.value()); // 16777218
		accelerators.put(SWT.ARROW_LEFT, Qt.Key.Key_Left.value()); // 16777219
		accelerators.put(SWT.ARROW_RIGHT, Qt.Key.Key_Right.value()); // 16777220
		accelerators.put(SWT.ARROW_UP, Qt.Key.Key_Up.value()); // 16777217
		accelerators.put(SWT.F1, Qt.Key.Key_F1.value()); // 16777226
		accelerators.put(SWT.F2, Qt.Key.Key_F2.value());
		accelerators.put(SWT.F3, Qt.Key.Key_F3.value());
		accelerators.put(SWT.F4, Qt.Key.Key_F4.value());
		accelerators.put(SWT.F5, Qt.Key.Key_F5.value());
		accelerators.put(SWT.F6, Qt.Key.Key_F6.value());
		accelerators.put(SWT.F7, Qt.Key.Key_F7.value());
		accelerators.put(SWT.F8, Qt.Key.Key_F8.value());
		accelerators.put(SWT.F9, Qt.Key.Key_F9.value());
		accelerators.put(SWT.F10, Qt.Key.Key_F10.value());
		accelerators.put(SWT.F11, Qt.Key.Key_F11.value());
		accelerators.put(SWT.F12, Qt.Key.Key_F12.value());
		accelerators.put(SWT.F13, Qt.Key.Key_F13.value());
		accelerators.put(SWT.F14, Qt.Key.Key_F14.value());
		accelerators.put(SWT.F15, Qt.Key.Key_F15.value());
		accelerators.put(SWT.PAGE_UP, Qt.Key.Key_PageUp.value());
		accelerators.put(SWT.PAGE_DOWN, Qt.Key.Key_PageDown.value());
		accelerators.put(SWT.HOME, Qt.Key.Key_Home.value());
		accelerators.put(SWT.END, Qt.Key.Key_End.value());
		accelerators.put(SWT.INSERT, Qt.Key.Key_Insert.value());
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the arm events are generated for the control, by sending it one of
	 * the messages defined in the <code>ArmListener</code> interface.
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
	 * @see ArmListener
	 * @see #removeArmListener
	 */
	public void addArmListener(ArmListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Arm, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the help events are generated for the control, by sending it one of
	 * the messages defined in the <code>HelpListener</code> interface.
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
	 * when the menu item is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the stateMask field of the
	 * event object is valid. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the menu item is
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

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
	}

	@Override
	void destroyWidget() {
		parent.removeAction(getQAction());
		super.destroyWidget();
	}

	void fixMenus(Decorations newParent) {
		if (menu != null) {
			menu.fixMenus(newParent);
		}
	}

	/**
	 * Returns the widget accelerator. An accelerator is the bit-wise OR of zero
	 * or more modifier masks and a key. Examples:
	 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>. The default
	 * value is zero, indicating that the menu item does not have an
	 * accelerator.
	 * 
	 * @return the accelerator or 0
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
	public int getAccelerator() {
		checkWidget();
		return accelerator;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent (or its display if its parent is null).
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
	 * Returns <code>true</code> if the receiver is enabled, and
	 * <code>false</code> otherwise. A disabled menu item is typically not
	 * selectable from the user interface and draws with an inactive or "grayed"
	 * look.
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
		return getQAction().isEnabled();
	}

	/**
	 * Returns the receiver's cascade menu if it has one or null if it does not.
	 * Only <code>CASCADE</code> menu items can have a pull down menu. The
	 * sequence of key strokes, button presses and/or button releases that are
	 * used to request a pull down menu is platform specific.
	 * 
	 * @return the receiver's menu
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public Menu getMenu() {
		checkWidget();
		return menu;
	}

	@Override
	String getNameText() {
		if ((style & SWT.SEPARATOR) != 0) {
			return "|";//$NON-NLS-1$
		}
		return super.getNameText();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Menu</code>.
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
	public Menu getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false
	 * otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked.
	 * 
	 * @return the selection state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		return getQAction().isChecked();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled menu item is typically not selectable from the user interface
	 * and draws with an inactive or "grayed" look.
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
		return getEnabled() && parent.isEnabled();
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (menu != null) {
			menu.release(false);
			menu = null;
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseQWidget() {
		if ((style & SWT.RADIO) != 0) {
			parent.removeFromActionGroup(action);
		}
		parent.removeAction(action);
		display.removeControl(action);
		parent = null;
		action = null;
		id = -1;
		super.releaseQWidget();
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if (menu != null) {
			menu.dispose();
		}
		menu = null;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		accelerator = 0;
		display.removeMenuItem(this);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the arm events are generated for the control.
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
	 * @see ArmListener
	 * @see #addArmListener
	 */
	public void removeArmListener(ArmListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Arm, listener);
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

	void selectRadio() {
		int index = 0;
		MenuItem[] items = parent.getItems();
		while (index < items.length && items[index] != this) {
			index++;
		}
		int i = index - 1;
		while (i >= 0 && items[i].setRadioSelection(false)) {
			--i;
		}
		int j = index + 1;
		while (j < items.length && items[j].setRadioSelection(false)) {
			j++;
		}
		setSelection(true);
	}

	/**
	 * Sets the widget accelerator. An accelerator is the bit-wise OR of zero or
	 * more modifier masks and a key. Examples:
	 * <code>SWT.MOD1 | SWT.MOD2 | 'T', SWT.MOD3 | SWT.F2</code>.
	 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>. The default
	 * value is zero, indicating that the menu item does not have an
	 * accelerator.
	 * 
	 * @param accelerator
	 *            an integer that is the bit-wise OR of masks and a key
	 * 
	 *            </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setAccelerator(int accelerator) {
		checkWidget();
		int seq = 0;
		if (this.accelerator == accelerator) {
			return;
		}
		this.accelerator = accelerator;
		/*
		 * fetch the modifiers of the accelerator (something like
		 * CTRL+SHIFT...). There can be more than one modifier.
		 */
		for (Integer modifier : modifiers.keySet()) {
			if ((accelerator & modifier) != 0) {
				accelerator -= modifier;
				seq += modifiers.get(modifier);
			}
		}

		/*
		 * Fetch uppercase characters and other ascii characters (+, =, ?...).
		 * Only one of this chars is allowed.
		 */
		if (accelerator >= 32 && accelerator <= 96 || accelerator >= 123 && accelerator <= 127) {
			seq += accelerator;
			accelerator -= accelerator;
		}
		/*
		 * Transform lowercase characters to uppercase characters, because QT
		 * can only handle uppercase characters and there is no difference
		 * between CTRL+m and CTRL+M. Only one of this chars is allowed.
		 */
		if (accelerator >= 97 && accelerator <= 122) {
			seq += accelerator - 32; // make a uppercase character out of a lowercase character
			accelerator -= accelerator;
		}

		/* fetch characters like F1, arrow keys or Page Up. Only one is allowed */
		if (accelerator != 0) {
			for (Integer acc : accelerators.keySet()) {
				if (accelerator == acc) {
					accelerator -= acc;
					seq += accelerators.get(acc);
				}
			}
		}

		if (accelerator != 0) { // something went wrong here. Not every character could be fetched.
			return;
		}
		getQAction().setShortcut(new QKeySequence(seq));

	}

	/*
	 * private QKeySequence acceleratorToString(int accelerator) { int seq = 0;
	 * if ((accelerator & SWT.MOD1) != 0 || (accelerator & SWT.CTRL) != 0) { //
	 * on Windows systems SWT.MOD1 is the ctrl key accelerator -= SWT.MOD1; seq
	 * += Qt.Modifier.CTRL.value(); } if ((accelerator & SWT.MOD2) != 0 ||
	 * (accelerator & SWT.SHIFT) != 0) { // on Windows systems SWT.MOD2 is the
	 * shift key accelerator -= SWT.MOD2; seq += Qt.Modifier.SHIFT.value(); } if
	 * ((accelerator & SWT.MOD3) != 0 || (accelerator & SWT.ALT) != 0) {
	 * accelerator -= SWT.MOD3; seq += Qt.Modifier.ALT.value(); } if
	 * (accelerator >= 65 && accelerator <= 90) { seq += accelerator; } else if
	 * (accelerator >= 97 && accelerator <= 122) { seq += accelerator - 32; //
	 * this takes care of lowercase characters. CTRL+m should be the same as
	 * CTRL+M } else if ((accelerator & SWT.ARROW_DOWN) != 0) { seq +=
	 * Qt.Key.Key_Down.value(); } else if ((accelerator & SWT.ARROW_LEFT) != 0)
	 * { seq += Qt.Key.Key_Left.value(); } else if ((accelerator &
	 * SWT.ARROW_RIGHT) != 0) { seq += Qt.Key.Key_Right.value(); } else if
	 * ((accelerator & SWT.ARROW_UP) != 0) { seq += Qt.Key.Key_Up.value(); }
	 * else if ((accelerator & SWT.F1) != 0) { seq += Qt.Key.Key_F1.value(); }
	 * return new QKeySequence(seq); }
	 */
	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise. A disabled menu item is typically not selectable from the
	 * user interface and draws with an inactive or "grayed" look.
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
		getQAction().setEnabled(enabled);
	}

	/**
	 * Sets the image the receiver will display to the argument.
	 * <p>
	 * Note: This operation is a hint and is not supported on platforms that do
	 * not have this concept (for example, Windows NT). Furthermore, some
	 * platforms (such as GTK), cannot display both a check box and an image at
	 * the same time. Instead, they hide the image and display the check box.
	 * </p>
	 * 
	 * @param image
	 *            the image to display
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setImage(Image image) {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		super.setImage(image);
		if (image != null) {
			getQAction().setIcon(image.getQIcon());
		} else {
			getQAction().setIcon((QIcon) null);
		}
	}

	/**
	 * Sets the receiver's pull down menu to the argument. Only
	 * <code>CASCADE</code> menu items can have a pull down menu. The sequence
	 * of key strokes, button presses and/or button releases that are used to
	 * request a pull down menu is platform specific.
	 * <p>
	 * Note: Disposing of a menu item that has a pull down menu will dispose of
	 * the menu. To avoid this behavior, set the menu to null before the menu
	 * item is disposed.
	 * </p>
	 * 
	 * @param menu
	 *            the new pull down menu
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_MENU_NOT_DROP_DOWN - if the menu is not a drop
	 *                down menu</li>
	 *                <li>ERROR_MENUITEM_NOT_CASCADE - if the menu item is not a
	 *                <code>CASCADE</code></li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed
	 *                </li>
	 *                <li>ERROR_INVALID_PARENT - if the menu is not in the same
	 *                widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMenu(Menu menu) {
		checkWidget();

		/* Check to make sure the new menu is valid */
		if ((style & SWT.CASCADE) == 0) {
			error(SWT.ERROR_MENUITEM_NOT_CASCADE);
		}
		if (menu != null) {
			if (menu.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if ((menu.style & SWT.DROP_DOWN) == 0) {
				error(SWT.ERROR_MENU_NOT_DROP_DOWN);
			}
			if (menu.parent != parent.parent) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		setMenu(menu, false);
	}

	void setMenu(Menu menu, boolean dispose) {
		if ((style & SWT.CASCADE) == 0) {
			error(SWT.ERROR_MENUITEM_NOT_CASCADE);
		}
		QMenu qMenu = null;
		if (menu != null) {
			if ((menu.style & SWT.DROP_DOWN) == 0) {
				error(SWT.ERROR_MENU_NOT_DROP_DOWN);
			}
			if (menu.parent != parent.parent) {
				error(SWT.ERROR_INVALID_PARENT);
			}
			qMenu = menu.getQMenu();
		}
		Menu oldMenu = this.menu;
		if (oldMenu == menu) {
			return;
		}
		if (oldMenu != null) {
			oldMenu.cascade = null;
		}
		this.menu = menu;
		getQAction().setMenu(qMenu);
		if (this.menu != null) {
			this.menu.cascade = this;
		}
	}

	boolean setRadioSelection(boolean value) {
		if ((style & SWT.RADIO) == 0) {
			return false;
		}
		if (getSelection() != value) {
			setSelection(value);
			postEvent(SWT.Selection);
		}
		return true;
	}

	/**
	 * Sets the selection state of the receiver.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked.
	 * 
	 * @param selected
	 *            the new selection state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(boolean selected) {
		checkWidget();
		if ((style & (SWT.CHECK | SWT.RADIO)) == 0) {
			return;
		}
		getQAction().setChecked(selected);
	}

	/**
	 * Sets the receiver's text. The string may include the mnemonic character
	 * and accelerator text.
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasised in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
	 * </p>
	 * <p>
	 * Accelerator text is indicated by the '\t' character. On platforms that
	 * support accelerator text, the text that follows the '\t' character is
	 * displayed to the user, typically indicating the key stroke that will
	 * cause the item to become selected. On most platforms, the accelerator
	 * text appears right aligned in the menu. Setting the accelerator text does
	 * not install the accelerator key sequence. The accelerator key sequence is
	 * installed using #setAccelerator.
	 * </p>
	 * 
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
	 * @see #setAccelerator
	 */
	@Override
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		if (text.equals(string)) {
			return;
		}
		super.setText(string);
		getQAction().setText(string);
	}

}
