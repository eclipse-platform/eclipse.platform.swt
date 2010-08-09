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

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE may be
 * specified.
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p>
 * <p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified when
 * the ARROW style is specified.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Button extends Control {
	private Image image;
	private Image image2;
	private Image disabledImage;
	private static/* final */boolean COMMAND_LINK = false;

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
	 * @see SWT#ARROW
	 * @see SWT#CHECK
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#TOGGLE
	 * @see SWT#FLAT
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Button(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected void connectSignals() {
		getQButton().released.connect(this, "clickEvent()"); //$NON-NLS-1$
	}

	@Override
	protected QWidget createQWidget(int style) {
		int marginLeft = 0;
		int marginTop = 0;
		int marginRight = 0;
		int marginBottom = 0;
		QAbstractButton tb;
		if ((style & SWT.CHECK) != 0) {
			tb = new MyQCheckBox();
			marginTop = 0;
			marginBottom = 2;
		} else if ((style & SWT.RADIO) != 0) {
			tb = new MyQRadioButton();
			if ((getParent().style & SWT.NO_RADIO_GROUP) != 0) {
				tb.setAutoExclusive(false);
			}
			marginTop = 3;
			marginBottom = 0;
		} else {
			tb = new MyQPushButton();
			if ((style & SWT.TOGGLE) != 0) {
				tb.setCheckable(true);
			}
			if ((style & SWT.FLAT) != 0) {
				((QPushButton) tb).setFlat(true);
			}
			// In SWT, push buttons do not inherit color from
			// their parent, not even with INHERIT_FORCE.
			tb.setPalette(QApplication.palette());
			marginTop = 3;
		}
		tb.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);
		tb.setContentsMargins(marginLeft, marginTop, marginRight, marginBottom);
		return tb;
	}

	QAbstractButton getQButton() {
		return (QAbstractButton) getQWidget();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the
	 * user. <code>widgetDefaultSelected</code> is not called.
	 * </p>
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

	protected void clickEvent() {
		sendEvent(SWT.Selection);
	}

	static int checkStyle(int style) {
		style = checkBits(style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, COMMAND_LINK ? SWT.COMMAND : 0);
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
			return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
		}
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			return checkBits(style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
		}
		if ((style & SWT.ARROW) != 0) {
			style |= SWT.NO_FOCUS;
			return checkBits(style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
		}
		return style;
	}

	void click() {
		getQButton().click();
	}

	// public Point computeSize( int wHint, int hHint, boolean changed ) {
	// checkWidget();
	// Point size = super.computeSize( wHint, hHint, changed );
	// size.x += 2;
	// return size;
	// }

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code>
	 * or <code>CENTER</code> unless the receiver is an <code>ARROW</code>
	 * button, in which case, the alignment will indicate the direction of the
	 * arrow (one of <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
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
		if ((style & SWT.ARROW) != 0) {
			if ((style & SWT.UP) != 0) {
				return SWT.UP;
			}
			if ((style & SWT.DOWN) != 0) {
				return SWT.DOWN;
			}
			if ((style & SWT.LEFT) != 0) {
				return SWT.LEFT;
			}
			if ((style & SWT.RIGHT) != 0) {
				return SWT.RIGHT;
			}
			return SWT.UP;
		}
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
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the widget does not have the <code>CHECK</code> style, return false.
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
	 * 
	 * @since 3.4
	 */
	public boolean getGrayed() {
		checkWidget();
		if ((style & SWT.CHECK) == 0) {
			return false;
		}
		QCheckBox box = (QCheckBox) getQButton();
		if (box.isTristate()) {
			return CheckState.PartiallyChecked.equals(box.checkState());
		}
		return false;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 * 
	 * @return the receiver's image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return image;
	}

	/**
	 * Returns the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 * 
	 * @return the widget message
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
	/* public */String getMessage() {
		checkWidget();
		return getQButton().whatsThis();
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false
	 * otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in. If the receiver is of any other
	 * type, this method returns false.
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
		if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) {
			return false;
		}
		return getQButton().isChecked();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has
	 * never been set or if the receiver is an <code>ARROW</code> button.
	 * 
	 * @return the receiver's text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		if ((style & SWT.ARROW) != 0) {
			return ""; //$NON-NLS-1$
		}
		return getQButton().text();
	}

	@Override
	boolean isTabItem() {
		if ((style & SWT.PUSH) != 0) {
			return isTabGroup();
		}
		return super.isTabItem();
	}

	@Override
	boolean mnemonicHit(char ch) {
		if (!setFocus()) {
			return false;
		}
		/*
		 * Feature in Windows. When a radio button gets focus, it selects the
		 * button in WM_SETFOCUS. Therefore, it is not necessary to click the
		 * button or send events because this has already happened in
		 * WM_SETFOCUS.
		 */
		if ((style & SWT.RADIO) == 0) {
			click();
		}
		return true;
	}

	@Override
	boolean mnemonicMatch(char key) {
		char mnemonic = findMnemonic(getText());
		if (mnemonic == '\0') {
			return false;
		}
		return Character.toUpperCase(key) == Character.toUpperCase(mnemonic);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		if (disabledImage != null) {
			disabledImage.dispose();
			disabledImage = null;
		}
		if (image2 != null) {
			image2.dispose();
			image2 = null;
		}
		image = null;
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
		/*
		 * This code is intentionally commented. When two groups of radio
		 * buttons with the same parent are separated by another control, the
		 * correct behavior should be that the two groups act independently.
		 * This is consistent with radio tool and menu items. The commented code
		 * implements this behavior.
		 */
		// int index = 0;
		// Control [] children = parent._getChildren ();
		// while (index < children.length && children [index] != this) index++;
		// int i = index - 1;
		// while (i >= 0 && children [i].setRadioSelection (false)) --i;
		// int j = index + 1;
		// while (j < children.length && children [j].setRadioSelection (false))
		// j++;
		// setSelection (true);
		Control[] children = parent._getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (this != child) {
				child.setRadioSelection(false);
			}
		}
		setSelection(true);
	}

	/**
	 * Controls how text, images and arrows will be displayed in the receiver.
	 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code> unless the receiver is an <code>ARROW</code> button,
	 * in which case, the argument indicates the direction of the arrow (one of
	 * <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
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
		// TODO how to change it for the qt control
		if ((style & SWT.ARROW) != 0) {
			if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) {
				return;
			}
			style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
			style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
			return;
		}
		if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) {
			return;
		}
		style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	}

	void setDefault(boolean value) {
		if ((style & SWT.PUSH) == 0) {
			return;
		}
		((QPushButton) getQButton()).setDefault(value);
	}

	@Override
	boolean setFixedFocus() {
		/*
		 * Feature in Windows. When a radio button gets focus, it selects the
		 * button in WM_SETFOCUS. The fix is to not assign focus to an
		 * unselected radio button.
		 */
		if ((style & SWT.RADIO) != 0 && !getSelection()) {
			return false;
		}
		return super.setFixedFocus();
	}

	/**
	 * Sets the receiver's image to the argument, which may be <code>null</code>
	 * indicating that no image should be displayed.
	 * <p>
	 * Note that a Button can display an image and text simultaneously on
	 * Windows (starting with XP), GTK+ and OSX. On other platforms, a Button
	 * that has an image and text set into it will display the image or text
	 * that was set most recently.
	 * </p>
	 * 
	 * @param image
	 *            the image to display on the receiver (may be <code>null</code>
	 *            )
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
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if ((style & SWT.ARROW) != 0) {
			return;
		}
		this.image = image;

		getQButton().setIcon(image.getQIcon());
		getQButton().setIconSize(image.getDefaultIconSize());
	}

	/**
	 * Sets the grayed state of the receiver. This state change only applies if
	 * the control was created with the SWT.CHECK style.
	 * 
	 * @param grayed
	 *            the new grayed state
	 * 
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
	public void setGrayed(boolean grayed) {
		checkWidget();
		if ((style & SWT.CHECK) == 0) {
			return;
		}
		QCheckBox box = (QCheckBox) getQButton();
		box.setTristate(true);
		box.setCheckState(CheckState.PartiallyChecked);
	}

	/**
	 * Sets the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 * 
	 * @param message
	 *            the new message
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
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
	/* public */void setMessage(String message) {
		checkWidget();
		if (message == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		getQButton().setWhatsThis(message);
	}

	@Override
	boolean setRadioFocus(boolean tabbing) {
		if ((style & SWT.RADIO) == 0 || !getSelection()) {
			return false;
		}
		return tabbing ? setTabItemFocus() : setFocus();
	}

	@Override
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

	@Override
	boolean setSavedFocus() {
		/*
		 * Feature in Windows. When a radio button gets focus, it selects the
		 * button in WM_SETFOCUS. If the previous saved focus widget was a radio
		 * button, allowing the shell to automatically restore the focus to the
		 * previous radio button will unexpectedly check that button. The fix is
		 * to not assign focus to an unselected radio button.
		 */
		if ((style & SWT.RADIO) != 0 && !getSelection()) {
			return false;
		}
		return super.setSavedFocus();
	}

	/**
	 * Sets the selection state of the receiver, if it is of type
	 * <code>CHECK</code>, <code>RADIO</code>, or <code>TOGGLE</code>.
	 * 
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in.
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
		if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) {
			return;
		}
		getQButton().setChecked(selected);
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * This method sets the button label. The label may include the mnemonic
	 * character but must not contain line delimiters.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasized in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
	 * </p>
	 * <p>
	 * Note that a Button can display an image and text simultaneously on
	 * Windows (starting with XP), GTK+ and OSX. On other platforms, a Button
	 * that has an image and text set into it will display the image or text
	 * that was set most recently.
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
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if ((style & SWT.ARROW) != 0) {
			return;
		}
		getQButton().setText(string);
	}

	private final class MyQCheckBox extends QCheckBox {

		@Override
		protected void mousePressEvent(QMouseEvent e) {
			super.mousePressEvent(e);
			e.setAccepted(false);
		}

	}

	private final class MyQRadioButton extends QRadioButton {

		@Override
		protected void mousePressEvent(QMouseEvent e) {
			super.mousePressEvent(e);
			e.setAccepted(false);
		}

	}

	private final class MyQPushButton extends QPushButton {

		@Override
		protected void mousePressEvent(QMouseEvent e) {
			super.mousePressEvent(e);
			e.setAccepted(false);
		}

	}
}
