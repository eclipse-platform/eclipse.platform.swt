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

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.ContextMenuPolicy;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionToolButton;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QIcon.Mode;
import com.trolltech.qt.gui.QStyle.ComplexControl;
import com.trolltech.qt.gui.QToolButton.ToolButtonPopupMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class represent a selectable user interface object that
 * represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN may
 * be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	private ToolBar parent;
	private Control control;
	private Image disabledImage, hotImage;
	private QAction action;

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>ToolBar</code>) and a style value describing its behavior and
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
	 * @see SWT#PUSH
	 * @see SWT#CHECK
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ToolItem(ToolBar parent, int style) {
		this(parent, style, -1);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>ToolBar</code>), a style value describing its behavior and
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
	 * @see SWT#PUSH
	 * @see SWT#CHECK
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ToolItem(ToolBar parent, int style, int index) {
		super(parent, checkStyle(style));
		this.parent = parent;
		action = createAndAddAction(style, index);
		connectSignals(action);
	}

	protected QAction createAndAddAction(int style, int index) {
		QAction action;
		int bits = SWT.CHECK | SWT.RADIO | SWT.PUSH | SWT.SEPARATOR | SWT.DROP_DOWN;
		switch (style & bits) {
		case SWT.SEPARATOR:
			action = createAndAddSeparator(index);
			break;
		case SWT.DROP_DOWN:
			action = createAndAddDropdownButton(index);
			break;
		case SWT.RADIO:
			// if ( parent.actionGroup == null ) {
			// parent.actionGroup = new QActionGroup( parent.getQtControl()
			// );
			// if ( ( parent.style & SWT.NO_RADIO_GROUP ) != 0 ) {
			// parent.actionGroup.setExclusive( false );
			// }
			// }
			// parent.actionGroup.addAction( action );
		case SWT.CHECK:
			action = createAndAddCheckableAction(index);
			break;
		case SWT.PUSH:
		default:
			action = createAndAddAction(index);
			break;
		}

		display.addControl(action, this);

		QWidget toolWidget = getQToolBar().widgetForAction(action);
		toolWidget.setContextMenuPolicy(ContextMenuPolicy.NoContextMenu);
		display.addControl(toolWidget, this);

		return action;
	}

	private QAction createAndAddSeparator(int index) {
		QAction action = createAction();
		action.setSeparator(true);
		addToToolbar(action, index);
		return action;
	}

	private void addToToolbar(QAction action, int index) {
		parent.addAction(action, index);
	}

	private QAction createAction() {
		return new QAction(parent.getQWidget());
	}

	private QAction createAndAddAction(int index) {
		QAction action = createAction();
		addToToolbar(action, index);
		return action;
	}

	private QAction createAndAddCheckableAction(int index) {
		QAction action = createAction();
		action.setCheckable(true);
		addToToolbar(action, index);
		return action;
	}

	private QAction createAndAddDropdownButton(int index) {
		MyToolButton button = new MyToolButton(parent.getQWidget());
		button.setPopupMode(ToolButtonPopupMode.MenuButtonPopup);
		QAction action = parent.addWidget(button, index);
		button.setDefaultAction(action);
		return action;
	}

	QAction getQAction() {
		return action;
	}

	QWidget getToolbarWidget() {
		return getQToolBar().widgetForAction(action);
	}

	QToolBar getQToolBar() {
		return parent.getQToolBar();
	}

	protected void connectSignals(QAction action) {
		action.triggered.connect(this, "sendTriggeredEvent()"); //$NON-NLS-1$
		action.hovered.connect(this, "sendHoveredEvent()"); //$NON-NLS-1$
	}

	protected void sendTriggeredEvent() {
		Event event = new Event();
		sendEvent(SWT.Selection, event);
	}

	protected void sendHoveredEvent() {
		Event event = new Event();
		sendEvent(SWT.Arm, event);
	}

	protected void sendShowDropDownEvent() {
		Event event = new Event();
		event.detail = SWT.ARROW;

		Rectangle rect = getBounds();
		event.x = rect.x;
		event.y = rect.y + rect.height;

		sendEvent(SWT.Selection, event);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called when the mouse is over the
	 * arrow portion of a drop-down tool, the event object detail field contains
	 * the value <code>SWT.ARROW</code>. <code>widgetDefaultSelected</code> is
	 * not called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the control is
	 *            selected by the user,
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
		return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	void click(boolean dropDown) {
		if (dropDown) {
			sendShowDropDownEvent();
		} else {
			getQAction().trigger();
		}
	}

	@Override
	void destroyWidget() {
		QWidget toolWidget = parent.removeAction(action);
		if (toolWidget != null) {
			toolWidget.disconnect();
			display.removeControl(toolWidget);
		}
		super.destroyWidget();
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
		return QtSWTConverter.convert(getQToolBar().actionGeometry(getQAction()));
	}

	/**
	 * Returns the control that is used to fill the bounds of the item when the
	 * item is a <code>SEPARATOR</code>.
	 * 
	 * @return the control
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Control getControl() {
		checkWidget();
		return control;
	}

	boolean hasControl(Control control) {
		return this.control == control;
	}

	/**
	 * Returns the receiver's disabled image if it has one, or null if it does
	 * not.
	 * <p>
	 * The disabled image is displayed when the receiver is disabled.
	 * </p>
	 * 
	 * @return the receiver's disabled image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getDisabledImage() {
		checkWidget();
		return disabledImage;
	}

	/**
	 * Returns the receiver's hot image if it has one, or null if it does not.
	 * <p>
	 * The hot image is displayed when the mouse enters the receiver.
	 * </p>
	 * 
	 * @return the receiver's hot image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getHotImage() {
		checkWidget();
		return hotImage;
	}

	/**
	 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
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
	public ToolBar getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false
	 * otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked (which some platforms draw as a pushed in
	 * button). If the receiver is of any other type, this method returns false.
	 * </p>
	 * 
	 * @return the selection state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		return getQAction().isChecked();
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
	 */
	public String getToolTipText() {
		checkWidget();
		return getQAction().toolTip();
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
		return getQToolBar().actionGeometry(getQAction()).width();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled control is typically not selectable from the user interface and
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
		return getEnabled() && parent.isEnabled();
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		control = null;
		disabledImage = null;
		hotImage = null;
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		parent.removeAction(action);
		action = null;
		parent = null;
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

	void resizeControl() {
		if (control != null && !control.isDisposed()) {
			/*
			 * Set the size and location of the control separately to minimize
			 * flashing in the case where the control does not resize to the
			 * size that was requested. This case can occur when the control is
			 * a combo box.
			 */
			Rectangle itemRect = getBounds();
			control.setSize(itemRect.width, itemRect.height);
			Rectangle rect = control.getBounds();
			rect.x = itemRect.x + (itemRect.width - rect.width) / 2;
			rect.y = itemRect.y + (itemRect.height - rect.height) / 2;
			control.setLocation(rect.x, rect.y);
		}
	}

	void selectRadio() {
		int index = 0;
		ToolItem[] items = parent.getItems();
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
	 * Sets the control that is used to fill the bounds of the item when the
	 * item is a <code>SEPARATOR</code>.
	 * 
	 * @param control
	 *            the new control
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_PARENT - if the control is not in the
	 *                same widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setControl(Control control) {
		checkWidget();
		if (control != null) {
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (control.parent != parent) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		if ((style & SWT.SEPARATOR) == 0) {
			return;
		}
		this.control = control;
		QAction newAction = getQToolBar().insertWidget(getQAction(), control.getQWidget());
		getQToolBar().removeAction(getQAction());
		newAction.setEnabled(action.isEnabled());
		newAction.setText(action.text());
		newAction.setToolTip(action.toolTip());
		newAction.setChecked(action.isChecked());
		updateImages();
		action = newAction;
		resizeControl();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and
	 * <code>false</code> otherwise. A disabled control is typically not
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
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise.
	 * <p>
	 * A disabled control is typically not selectable from the user interface
	 * and draws with an inactive or "grayed" look.
	 * </p>
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
	 * Sets the receiver's disabled image to the argument, which may be null
	 * indicating that no disabled image should be displayed.
	 * <p>
	 * The disabled image is displayed when the receiver is disabled.
	 * </p>
	 * 
	 * @param image
	 *            the disabled image to display on the receiver (may be null)
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
	public void setDisabledImage(Image image) {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		disabledImage = image;
		updateImages();
	}

	/**
	 * Sets the receiver's hot image to the argument, which may be null
	 * indicating that no hot image should be displayed.
	 * <p>
	 * The hot image is displayed when the mouse enters the receiver.
	 * </p>
	 * 
	 * @param image
	 *            the hot image to display on the receiver (may be null)
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
	public void setHotImage(Image image) {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		hotImage = image;
		updateImages();
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		super.setImage(image);
		updateImages();
	}

	private void updateImages() {
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}

		if (image == null && disabledImage == null && hotImage == null) {
			getQAction().setIcon((QIcon) null);
			return;
		}

		QIcon icon = new QIcon();
		if (image != null) {
			icon.addPixmap(image.getQPixmap());
		}

		if (disabledImage != null) {
			icon.addPixmap(disabledImage.getQPixmap(), Mode.Disabled);
		}

		if (hotImage != null) {
			icon.addPixmap(hotImage.getQPixmap(), Mode.Selected);
		}
		getQAction().setIcon(icon);
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
	 * is selected when it is checked (which some platforms draw as a pushed in
	 * button).
	 * </p>
	 * 
	 * @param selected
	 *            the new selection state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
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
	 * Sets the receiver's text. The string may include the mnemonic character.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasised in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
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
		if (string.equals(text)) {
			return;
		}
		super.setText(string);
		getQAction().setText(string);
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
	 */
	public void setToolTipText(String string) {
		checkWidget();
		getQAction().setToolTip(string);
	}

	/**
	 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
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
		if ((style & SWT.SEPARATOR) == 0) {
			return;
		}
		if (width < 0) {
			return;
		}
		QWidget widget = getQToolBar().widgetForAction(getQAction());
		widget.resize(width, widget.height());
	}

	@Override
	public String toString() {
		return getName() + "{text: " + getQAction().text() + ", tooltip: " + getQAction().toolTip() + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Subclassed QToolButton, to override drop down handling. We check for
	 * clicks on the arrow an the right side of the button and show our own
	 * drop-down menu, ignoring the default menu.
	 */
	private final class MyToolButton extends QToolButton {
		public MyToolButton(QWidget parent) {
			super(parent);
		}

		@Override
		protected void mousePressEvent(QMouseEvent e) {
			if (MouseButton.LeftButton.equals(e.button()) && ToolButtonPopupMode.MenuButtonPopup.equals(popupMode())) {
				QStyleOptionToolButton opt = new QStyleOptionToolButton();
				initStyleOption(opt);
				QRect popupr = style().subControlRect(ComplexControl.CC_ToolButton, opt,
						QStyle.SubControl.SC_ToolButtonMenu, this);
				if (popupr.isValid() && popupr.contains(e.pos())) {
					sendShowDropDownEvent();
					return;
				}
			}
			super.mousePressEvent(e);
		}
	}

}
