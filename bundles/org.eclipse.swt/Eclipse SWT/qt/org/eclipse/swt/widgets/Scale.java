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

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAbstractSlider;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QSlider.TickPosition;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

/**
 * Instances of the receiver represent a selectable user interface object that
 * present a range of continuous numeric values.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#scale">Scale snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Scale extends Control {

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
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Scale(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		final Orientation orientation = (style & SWT.HORIZONTAL) != 0 ? Orientation.Horizontal : Orientation.Vertical;
		return new QSlider(orientation);
	}

	@Override
	protected void setupQWidget() {
		super.setupQWidget();
		setMinimum(0);
		setMaximum(100);
		setIncrement(1);
		setPageIncrement(10);
		getQSlider().setTickPosition(TickPosition.TicksBothSides);
	}

	QSlider getQSlider() {
		return (QSlider) getQWidget();
	}

	@Override
	protected void connectSignals() {
		getQSlider().actionTriggered.connect(this, "actionTriggered(int)"); //$NON-NLS-1$
		getQSlider().rangeChanged.connect(this, "rangeChanged(int,int)"); //$NON-NLS-1$
		getQSlider().sliderMoved.connect(this, "sliderMoved(int)"); //$NON-NLS-1$
		getQSlider().sliderPressed.connect(this, "sliderPressed()"); //$NON-NLS-1$
		getQSlider().sliderReleased.connect(this, "sliderReleased()"); //$NON-NLS-1$
		getQSlider().valueChanged.connect(this, "valueChanged(int)"); //$NON-NLS-1$
	}

	protected void actionTriggered(int action) {
		System.out.println("actionTriggered :" + action + " " + getQSlider().value()); //$NON-NLS-1$//$NON-NLS-2$
		Event event = new Event();
		if (action == QAbstractSlider.SliderAction.SliderToMinimum.value()) {
			event.detail = SWT.HOME;
		} else if (action == QAbstractSlider.SliderAction.SliderToMaximum.value()) {
			event.detail = SWT.END;
		} else if (action == QAbstractSlider.SliderAction.SliderSingleStepAdd.value()) {
			event.detail = SWT.ARROW_DOWN;
		} else if (action == QAbstractSlider.SliderAction.SliderSingleStepSub.value()) {
			event.detail = SWT.ARROW_UP;
		} else if (action == QAbstractSlider.SliderAction.SliderPageStepAdd.value()) {
			event.detail = SWT.PAGE_UP;
		} else if (action == QAbstractSlider.SliderAction.SliderPageStepSub.value()) {
			event.detail = SWT.PAGE_DOWN;
		} else if (action == QAbstractSlider.SliderAction.SliderMove.value()) {
			event.detail = SWT.DRAG;
		} else {
			return;
		}
		// see actionTrigger signal
		getQSlider().setValue(getQSlider().sliderPosition());
		sendEvent(SWT.Selection, event);
	}

	// TODO: implement the events
	protected void rangeChanged(int min, int max) {
		System.out.println("rangeChanged :" + min + ", " + max); //$NON-NLS-1$//$NON-NLS-2$
	}

	protected void sliderMoved(int pos) {
		System.out.println("sliderMoved :" + pos); //$NON-NLS-1$
	}

	protected void sliderPressed() {
		System.out.println("sliderPressed"); //$NON-NLS-1$
	}

	protected void sliderReleased() {
		System.out.println("sliderReleased"); //$NON-NLS-1$
		Event event = new Event();
		event.detail = SWT.NONE;
		sendEvent(SWT.Selection, event);
	}

	protected void valueChanged(int value) {
		System.out.println("valueChanged :" + value); //$NON-NLS-1$
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's value, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the user changes the
	 * receiver's value. <code>widgetDefaultSelected</code> is not called.
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
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int border = getBorderWidth();
		int width = border * 2;
		int height = border * 2;
		// default size: this is just the two arrows + thumb
		Point defaultSize = super.computeSize(wHint, hHint, changed);
		if ((style & SWT.HORIZONTAL) != 0) {
			width += defaultSize.y * 6;
			height += defaultSize.y;
		} else {
			width += defaultSize.x;
			height += defaultSize.x * 6;
		}
		if (wHint != SWT.DEFAULT) {
			width = wHint + border * 2;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint + border * 2;
		}
		return new Point(width, height);
	}

	// TODO What should happen here?
	//	int defaultForeground() {
	//		return display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	//	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed.
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
		return getQSlider().singleStep();
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
		return getQSlider().maximum();
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
		return getQSlider().minimum();
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected.
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
		return getQSlider().pageStep();
	}

	/**
	 * Returns the 'selection', which is the receiver's position.
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
		return getQSlider().value();
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's value.
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
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be
	 * at least one.
	 * 
	 * @param increment
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
	public void setIncrement(int increment) {
		checkWidget();
		if (increment < 1) {
			return;
		}
		if (increment > getMaximum() - getMinimum()) {
			return;
		}
		getQSlider().setSingleStep(increment);
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
		if (0 <= getMinimum() && getMinimum() < value) {
			getQSlider().setMaximum(value);
		}
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will
	 * be ignored if it is negative or is not less than the receiver's current
	 * maximum value. If the new minimum is applied then the receiver's
	 * selection value will be adjusted if necessary to fall within its new
	 * range.
	 * 
	 * @param value
	 *            the new minimum, which must be nonnegative and less than the
	 *            current maximum
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
		if (0 <= value && value < getMaximum()) {
			getQSlider().setMinimum(value);
		}
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected to the argument, which must
	 * be at least one.
	 * 
	 * @param pageIncrement
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
	public void setPageIncrement(int pageIncrement) {
		checkWidget();
		if (pageIncrement < 1) {
			return;
		}
		if (pageIncrement > getMaximum() - getMinimum()) {
			return;
		}
		getQSlider().setPageStep(pageIncrement);
		getQSlider().setTickInterval(pageIncrement);
	}

	/**
	 * Sets the 'selection', which is the receiver's value, to the argument
	 * which must be greater than or equal to zero.
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
		getQSlider().setValue(value);
	}

}
