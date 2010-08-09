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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify numeric values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#spinner">Spinner
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * 
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Spinner extends Composite {
	boolean ignoreModify;
	int pageIncrement, digits;
	int selection = 0;

	/**
	 * the operating system limit for the number of characters that the text
	 * field in an instance of this class can hold
	 * 
	 * @since 3.4
	 */
	public static final int LIMIT;

	/*
	 * These values can be different on different platforms. Therefore they are
	 * not initialized in the declaration to stop the compiler from inlining.
	 */
	static {
		//TODO: 3 is just an imaginary value. see bug 161   
		LIMIT = 3;
	}

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
	 * @see SWT#READ_ONLY
	 * @see SWT#WRAP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Spinner(Composite parent, int style) {
		super(parent, checkStyle(style));

		// setting default values of the SWT widget:
		((QDoubleSpinBox) getQWidget()).setDecimals(0);
		((QDoubleSpinBox) getQWidget()).setMaximum(100);

		((QDoubleSpinBox) getQWidget()).setSingleStep(getIncrement() / Math.pow(10, this.digits));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state &= ~(CANVAS | THEME_BACKGROUND);
		QWidget spinner = new QDoubleSpinBox();
		connectSignals(spinner);
		return spinner;
	}

	static int checkStyle(int style) {
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

	private void connectSignals(QWidget spinner) {
		((QDoubleSpinBox) spinner).valueChanged.connect(this, "valueChanged()"); //$NON-NLS-1$
		//TODO: connection for selection and verify event is missing, but there's not suitable event in QDoubleSpinBox (bug 159)
	}

	protected void valueChanged() {
		Event event = new Event();
		sendEvent(SWT.Modify, event);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener</code> interface.
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
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is not called for texts.
	 * <code>widgetDefaultSelected</code> is typically called when ENTER is
	 * pressed in a single-line text.
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

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is verified, by sending it one of the messages
	 * defined in the <code>VerifyListener</code> interface.
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
	 * @see VerifyListener
	 * @see #removeVerifyListener
	 */
	void addVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Verify, typedListener);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		QSize size = ((QDoubleSpinBox) getQWidget()).sizeHint();
		return new Point(size.width(), size.height());
	}

	@Override
	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();
		return super.computeTrim(x, y, width, height);
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void copy() {
		checkWidget();
		QApplication.clipboard().setText(((QDoubleSpinBox) getQWidget()).text());
	}

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted
	 * from the widget.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void cut() {
		checkWidget();
		if ((style & SWT.READ_ONLY) != 0) {
			return;
		}
		QApplication.clipboard().setText(((QDoubleSpinBox) getQWidget()).text());
		((QDoubleSpinBox) getQWidget()).setValue(0);
		this.selection = 0;
	}

	@Override
	void enableWidget(boolean enabled) {
		super.enableWidget(enabled);
	}

	@Override
	void deregisterQWidget() {
		super.deregisterQWidget();
	}

	@Override
	boolean hasFocus() {
		return getQWidget().hasFocus();
	}

	/**
	 * Returns the number of decimal places used by the receiver.
	 * 
	 * @return the digits
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getDigits() {
		checkWidget();
		return digits;
	}

	String getDecimalSeparator() {
		// TODO check locale
		return "."; //$NON-NLS-1$
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down arrows are pressed.
	 * 
	 * @return the increment
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getIncrement() {
		checkWidget();
		double step = ((QDoubleSpinBox) getQWidget()).singleStep();
		int qstep = (int) (step * Math.pow(10, this.digits));
		return qstep;
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 * 
	 * @return the maximum
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMaximum() {
		checkWidget();
		return (int) (((QDoubleSpinBox) getQWidget()).maximum() * Math.pow(10, this.digits));
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 * 
	 * @return the minimum
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMinimum() {
		checkWidget();
		return (int) (((QDoubleSpinBox) getQWidget()).minimum() * Math.pow(10, this.digits));
	}

	/**
	 * Returns the amount that the receiver's position will be modified by when
	 * the page up/down keys are pressed.
	 * 
	 * @return the page increment
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getPageIncrement() {
		checkWidget();
		return (int) ((QDoubleSpinBox) getQWidget()).singleStep();
	}

	/**
	 * Returns the <em>selection</em>, which is the receiver's position.
	 * 
	 * @return the selection
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelection() {
		checkWidget();
		int qselection = (int) (((QDoubleSpinBox) getQWidget()).value() * Math.pow(10, this.digits));
		this.selection = qselection;
		return qselection;
	}

	int getSelectionText(boolean[] parseFail) {
		// TODO
		return -1;
	}

	/**
	 * Returns a string containing a copy of the contents of the receiver's text
	 * field, or an empty string if there are no contents.
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
	 * 
	 * @since 3.4
	 */
	public String getText() {
		checkWidget();
		return ((QDoubleSpinBox) getQWidget()).text();
	}

	/**
	 * Returns the maximum number of characters that the receiver's text field
	 * is capable of holding. If this has not been changed by
	 * <code>setTextLimit()</code>, it will be the constant
	 * <code>Spinner.LIMIT</code>.
	 * 
	 * @return the text limit
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 * 
	 * @since 3.4
	 */
	public int getTextLimit() {
		checkWidget();
		//TODO: not sure about this...
		int maxChars = String.valueOf(getMaximum()).trim().length();
		int minChars = String.valueOf(getMinimum()).trim().length();
		return maxChars > minChars ? maxChars : minChars;
	}

	/**
	 * Pastes text from clipboard.
	 * <p>
	 * The selected text is deleted from the widget and new text inserted from
	 * the clipboard.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void paste() {
		boolean isNumber = true;
		double pasteNumber;

		checkWidget();
		if ((style & SWT.READ_ONLY) != 0) {
			return;
		}
		String pasteText = QApplication.clipboard().text();

		try {
			pasteNumber = Double.parseDouble(pasteText);
		} catch (NumberFormatException e) {
			isNumber = false;
			return; // it's not a valid number, so just return and don't paste anything
		}
		if (isNumber && pasteNumber <= getMaximum() && pasteNumber >= getMinimum()) {
			this.selection = Integer.valueOf(QApplication.clipboard().text());
			((QDoubleSpinBox) getQWidget()).setValue(pasteNumber);
		}
	}

	@Override
	void registerQWidget() {
		super.registerQWidget();
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
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
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Modify, listener);
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
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is verified.
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
	 * @see VerifyListener
	 * @see #addVerifyListener
	 */
	void removeVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Verify, listener);
	}

	/**
	 * Sets the number of decimal places used by the receiver.
	 * <p>
	 * The digit setting is used to allow for floating point values in the
	 * receiver. For example, to set the selection to a floating point value of
	 * 1.37 call setDigits() with a value of 2 and setSelection() with a value
	 * of 137. Similarly, if getDigits() has a value of 2 and getSelection()
	 * returns 137 this should be interpreted as 1.37. This applies to all
	 * numeric APIs.
	 * </p>
	 * 
	 * @param value
	 *            the new digits (must be greater than or equal to zero)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value is less than
	 *                zero</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setDigits(int value) {
		checkWidget();
		if (value < 0) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (value == this.digits) {
			return;
		}

		this.digits = value;
		((QDoubleSpinBox) getQWidget()).setDecimals(value);
		((QDoubleSpinBox) getQWidget()).setValue(selection / Math.pow(10, value));
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down arrows are pressed to the argument, which must be at least one.
	 * 
	 * @param value
	 *            the new increment (must be greater than zero)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setIncrement(int value) {
		checkWidget();
		if (value < 1) {
			return;
		}
		((QDoubleSpinBox) getQWidget()).setSingleStep(value / Math.pow(10, this.digits));
	}

	/**
	 * Sets the maximum value that the receiver will allow. This new value will
	 * be ignored if it is not greater than the receiver's current minimum
	 * value. If the new maximum is applied then the receiver's selection value
	 * will be adjusted if necessary to fall within its new range.
	 * 
	 * @param value
	 *            the new maximum, which must be greater than the current
	 *            minimum
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMaximum(int value) {
		checkWidget();
		((QDoubleSpinBox) getQWidget()).setMaximum(value / Math.pow(10, this.digits));
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will
	 * be ignored if it is not less than the receiver's current maximum value.
	 * If the new minimum is applied then the receiver's selection value will be
	 * adjusted if necessary to fall within its new range.
	 * 
	 * @param value
	 *            the new minimum, which must be less than the current maximum
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMinimum(int value) {
		checkWidget();
		((QDoubleSpinBox) getQWidget()).setMinimum(value / Math.pow(10, this.digits));
	}

	/**
	 * Sets the amount that the receiver's position will be modified by when the
	 * page up/down keys are pressed to the argument, which must be at least
	 * one.
	 * 
	 * @param value
	 *            the page increment (must be greater than zero)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setPageIncrement(int value) {
		checkWidget();
		if (value < 1) {
			return;
		}
		pageIncrement = value; // TODO: see bug 160
	}

	/**
	 * Sets the <em>selection</em>, which is the receiver's position, to the
	 * argument. If the argument is not within the range specified by minimum
	 * and maximum, it will be adjusted to fall within this range.
	 * 
	 * @param value
	 *            the new selection (must be zero or greater)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(int value) {
		checkWidget();
		this.selection = value;
		((QDoubleSpinBox) getQWidget()).setDecimals(this.digits);
		((QDoubleSpinBox) getQWidget()).setValue(value / Math.pow(10, digits));

	}

	/**
	 * Sets the maximum number of characters that the receiver's text field is
	 * capable of holding to be the argument.
	 * <p>
	 * To reset this value to the default, use
	 * <code>setTextLimit(Spinner.LIMIT)</code>. Specifying a limit value larger
	 * than <code>Spinner.LIMIT</code> sets the receiver's limit to
	 * <code>Spinner.LIMIT</code>.
	 * </p>
	 * 
	 * @param limit
	 *            new text limit
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 * 
	 * @since 3.4
	 */
	public void setTextLimit(int limit) {
		checkWidget();
		if (limit == 0) {
			error(SWT.ERROR_CANNOT_BE_ZERO);
		}

		if (limit < 0 || limit > LIMIT) {
			limit = LIMIT;
		}

		//TODO setting the text limit to the QDoubleSpinBox
	}

	/**
	 * Sets the receiver's selection, minimum value, maximum value, digits,
	 * increment and page increment all at once.
	 * <p>
	 * Note: This is similar to setting the values individually using the
	 * appropriate methods, but may be implemented in a more efficient fashion
	 * on some platforms.
	 * </p>
	 * 
	 * @param selection
	 *            the new selection value
	 * @param minimum
	 *            the new minimum value
	 * @param maximum
	 *            the new maximum value
	 * @param digits
	 *            the new digits value
	 * @param increment
	 *            the new increment value
	 * @param pageIncrement
	 *            the new pageIncrement value
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
	public void setValues(int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
		checkWidget();
		if (maximum <= minimum) {
			return;
		}
		if (digits < 0) {
			return;
		}
		if (increment < 1) {
			return;
		}
		if (pageIncrement < 1) {
			return;
		}
		this.selection = Math.min(Math.max(minimum, selection), maximum);
		((QDoubleSpinBox) getQWidget()).setValue(this.selection);
		setIncrement(increment);
		this.pageIncrement = pageIncrement;
		this.digits = digits;
	}

	String verifyText(String string, int start, int end, Event keyEvent) {
		//		Event event = new Event();
		//		event.text = string;
		//		event.start = start;
		//		event.end = end;
		//		if (keyEvent != null) {
		//			event.character = keyEvent.character;
		//			event.keyCode = keyEvent.keyCode;
		//			event.stateMask = keyEvent.stateMask;
		//		}
		//		int index = 0;
		//		if (digits > 0) {
		//			String decimalSeparator = getDecimalSeparator();
		//			index = string.indexOf(decimalSeparator);
		//			if (index != -1) {
		//				string = string.substring(0, index) + string.substring(index + 1);
		//			}
		//			index = 0;
		//		}
		//		if (string.length() > 0) {
		//			int[] min = new int[1];
		//			OS.SendMessage(hwndUpDown, OS.UDM_GETRANGE32, min, null);
		//			if (min[0] < 0 && string.charAt(0) == '-')
		//				index++;
		//		}
		//		while (index < string.length()) {
		//			if (!Character.isDigit(string.charAt(index)))
		//				break;
		//			index++;
		//		}
		//		event.doit = index == string.length();
		// if ( !OS.IsUnicode && OS.IsDBLocale ) {
		// event.start = mbcsToWcsPos( start );
		// event.end = mbcsToWcsPos( end );
		// }
		//		sendEvent(SWT.Verify, event);
		//		if (!event.doit || isDisposed())
		return null;
		//		return event.text;
	}

}
