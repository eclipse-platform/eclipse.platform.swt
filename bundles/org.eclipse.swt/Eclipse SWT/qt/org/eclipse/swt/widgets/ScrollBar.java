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

import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QAbstractSlider.SliderAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class are selectable user interface objects that represent
 * a range of positive, numeric values.
 * <p>
 * At any given moment, a given scroll bar will have a single 'selection' that
 * is considered to be its value, which is constrained to be within the range of
 * values the scroll bar represents (that is, between its <em>minimum</em> and
 * <em>maximum</em> values).
 * </p>
 * <p>
 * Typically, scroll bars will be made up of five areas:
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * Based on their style, scroll bars are either <code>HORIZONTAL</code> (which
 * have a left facing button for decrementing the value and a right facing
 * button for incrementing it) or <code>VERTICAL</code> (which have an upward
 * facing button for decrementing the value and a downward facing buttons for
 * incrementing it).
 * </p>
 * <p>
 * On some platforms, the size of the scroll bar's thumb can be varied relative
 * to the magnitude of the range of values it represents (that is, relative to
 * the difference between its maximum and minimum values). Typically, this is
 * used to indicate some proportional value such as the ratio of the visible
 * area of a document to the total amount of space that it would take to display
 * it. SWT supports setting the thumb size even if the underlying platform does
 * not, but in this case the appearance of the scroll bar will not change.
 * </p>
 * <p>
 * Scroll bars are created by specifying either <code>H_SCROLL</code>,
 * <code>V_SCROLL</code> or both when creating a <code>Scrollable</code>. They
 * are accessed from the <code>Scrollable</code> using
 * <code>getHorizontalBar</code> and <code>getVerticalBar</code>.
 * </p>
 * <p>
 * Note: Scroll bars are not Controls. On some platforms, scroll bars that
 * appear as part of some standard controls such as a text or list have no
 * operating system resources and are not children of the control. For this
 * reason, scroll bars are treated specially. To create a control that looks
 * like a scroll bar but has operating system resources, use <code>Slider</code>
 * .
 * </p>
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see Slider
 * @see Scrollable
 * @see Scrollable#getHorizontalBar
 * @see Scrollable#getVerticalBar
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class ScrollBar extends Widget {
	private Scrollable parent;
	private boolean visible = true;
	private boolean enabled = true;

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
	ScrollBar(Scrollable parent, int style) {
		super(parent, checkStyle(style));
		this.parent = parent;
		createWidget(style);
		connectSignals();
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	private void createWidget(int style) {
		if (!parent.isQScrollArea()) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		QScrollBar scrollBar = null;
		if ((style & SWT.HORIZONTAL) != 0) {
			scrollBar = parent.getQScrollArea().horizontalScrollBar();
		} else {
			scrollBar = parent.getQScrollArea().verticalScrollBar();
		}
		if (scrollBar == null) {
			SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		setQWidget(scrollBar);
		scrollBar.setMinimum(0);
		scrollBar.setSliderPosition(0);
		scrollBar.setTracking(false);
	}

	private void connectSignals() {
		QScrollBar sb = getQScrollBar();
		sb.sliderMoved.connect(this, "dragging()"); //$NON-NLS-1$
		sb.sliderReleased.connect(this, "dragEnd()"); //$NON-NLS-1$
		sb.actionTriggered.connect(this, "actionTriggered(int)"); //$NON-NLS-1$
	}

	void dragging() {
		postSliderEvent(SWT.DRAG);
	}

	void dragEnd() {
		postSliderEvent(SWT.NONE);
	}

	void actionTriggered(int code) {
		SliderAction action = SliderAction.resolve(code);
		switch (action) {
		case SliderSingleStepSub: // button up
			postSliderEvent(SWT.ARROW_UP);
			break;
		case SliderSingleStepAdd: // button down
			postSliderEvent(SWT.ARROW_DOWN);
			break;
		case SliderPageStepSub: // page up
			postSliderEvent(SWT.PAGE_UP);
			break;
		case SliderPageStepAdd: // page down
			postSliderEvent(SWT.PAGE_DOWN);
			break;
		case SliderToMinimum: // at start
			postSliderEvent(SWT.HOME);
			break;
		case SliderToMaximum: // at end
			postSliderEvent(SWT.END);
			break;
		case SliderMove: // at end
			postSliderEvent(SWT.DRAG);
			break;
		default:
			System.err.println("unknown action: " + action); //$NON-NLS-1$
		}
	}

	QScrollBar getQScrollBar() {
		return (QScrollBar) getQWidget();
	}

	private void postSliderEvent(int subType) {
		Event event = new Event();
		event.detail = subType;
		postEvent(SWT.Selection, event);
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		parent = null;
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		parent.releaseBar(this);
	}

	@Override
	void destroyWidget() {
		parent.destroyScrollBar(style);
	}

	/**
	 * Returns the receiver's parent, which must be a Scrollable.
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
	public Scrollable getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's value, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the event object detail field
	 * contains one of the following values: <code>SWT.NONE</code> - for the end
	 * of a drag. <code>SWT.DRAG</code>. <code>SWT.HOME</code>.
	 * <code>SWT.END</code>. <code>SWT.ARROW_DOWN</code>.
	 * <code>SWT.ARROW_UP</code>. <code>SWT.PAGE_DOWN</code>.
	 * <code>SWT.PAGE_UP</code>. <code>widgetDefaultSelected</code> is not
	 * called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the user changes
	 *            the receiver's value
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
		return getQScrollBar().singleStep();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be
	 * at least one.
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
		getQScrollBar().setSingleStep(value);
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
		return getQScrollBar().maximum(); // + getQScrollBar().pageStep();
	}

	/**
	 * Sets the maximum. If this value is negative or less than or equal to the
	 * minimum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 * 
	 * @param value
	 *            the new maximum
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
		if (value < 0) {
			return;
		}
		QScrollBar scrollBar = getQScrollBar();
		int min = scrollBar.minimum();
		if (value <= min) {
			return;
		}

		//		int maxValue = value - scrollBar.pageStep();
		//		if (maxValue < min) {
		//			scrollBar.setPageStep(value - min);
		//			maxValue = min;
		//		}
		//
		//		if (scrollBar.value() > maxValue) {
		//			scrollBar.setValue(maxValue);
		//		}
		scrollBar.setMaximum(value);
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
		return getQScrollBar().minimum();
	}

	/**
	 * Sets the minimum value. If this value is negative or greater than or
	 * equal to the maximum, the value is ignored. If necessary, first the thumb
	 * and then the selection are adjusted to fit within the new range.
	 * 
	 * @param value
	 *            the new minimum
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
		if (value < 0) {
			return;
		}
		QScrollBar scrollBar = getQScrollBar();
		int max = scrollBar.maximum();
		if (value >= max) {
			return;
		}
		//
		//		if (max + pageStep - value < pageStep) {
		//			scrollBar.setPageStep(max + pageStep - value);
		//			scrollBar.setMaximum(value);
		//		}
		//
		//		if (scrollBar.value() < value) {
		//			scrollBar.setValue(value);
		//		}
		scrollBar.setMinimum(value);
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
		return getQScrollBar().pageStep();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected to the argument, which must
	 * be at least one.
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
		getQScrollBar().setPageStep(value);
	}

	/**
	 * Returns the single 'selection' that is the receiver's value.
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
		return getQScrollBar().sliderPosition();
	}

	/**
	 * Sets the single <em>selection</em> that is the receiver's value to the
	 * argument which must be greater than or equal to zero.
	 * 
	 * @param selection
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
	public void setSelection(int selection) {
		checkWidget();
		int min = getQScrollBar().minimum();
		if (selection < min) {
			selection = min;
		} else {
			int max = getQScrollBar().maximum();
			if (selection > max) {
				selection = max;
			}
		}
		int value = getQScrollBar().value();
		if (value != selection) {
			getQScrollBar().setSliderPosition(selection);
		}
		//System.out.println(this);
	}

	/**
	 * Returns a point describing the receiver's size. The x coordinate of the
	 * result is the width of the receiver. The y coordinate of the result is
	 * the height of the receiver.
	 * 
	 * @return the receiver's size
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getSize() {
		checkWidget();
		if (!isVisible()) {
			parent.updateQLayouts();
		}
		return QtSWTConverter.convert(getQWidget().size());
	}

	/**
	 * Returns the size of the receiver's thumb relative to the difference
	 * between its maximum and minimum values.
	 * 
	 * @return the thumb value
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ScrollBar
	 */
	public int getThumb() {
		checkWidget();
		return getQScrollBar().pageStep();
	}

	/**
	 * Sets the size of the receiver's thumb relative to the difference between
	 * its maximum and minimum values. This new value will be ignored if it is
	 * less than one, and will be clamped if it exceeds the receiver's current
	 * range.
	 * 
	 * @param value
	 *            the new thumb value, which must be at least one and not larger
	 *            than the size of the current range
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setThumb(int value) {
		checkWidget();
		if (value < 1) {
			return;
		}
		if (value < 1) {
			return;
		}
		int pageStep = getQScrollBar().pageStep();
		if (value == pageStep) {
			return;
		}

		int qMax = getQScrollBar().maximum();
		int range = qMax + pageStep - getQScrollBar().minimum();
		if (value > range) {
			value = range;
		}
		int newQMaximum = qMax - (value - pageStep);
		if (getQScrollBar().value() > newQMaximum) {
			getQScrollBar().setValue(newQMaximum);
		}

		getQScrollBar().setPageStep(value);

		getQScrollBar().setMaximum(newQMaximum);
	}

	/**
	 * Sets the receiver's selection, minimum value, maximum value, thumb,
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
	 * @param thumb
	 *            the new thumb value
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
	 */
	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
		checkWidget();
		setSelection(selection);
		setMinimum(minimum);
		setMaximum(maximum);
		setThumb(thumb);
		setIncrement(increment);
		setPageIncrement(pageIncrement);
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
		return this.visible;
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
		return getQWidget().isVisible() && parent.isVisible();
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
		if (visible == getVisible()) {
			return;
		}
		this.visible = visible;
		getQWidget().setVisible(visible);
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
		return enabled;
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
		return getQWidget().isEnabled() && parent.isEnabled();
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise. A disabled control is typically not selectable from the
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
		this.enabled = enabled;
		getQWidget().setEnabled(enabled);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ScrollBar{"); //$NON-NLS-1$
		sb.append("orient: "); //$NON-NLS-1$
		sb.append(((style & SWT.HORIZONTAL) != 0 ? "HORI" : "VERT")); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(", selection: "); //$NON-NLS-1$
		sb.append(getSelection());
		sb.append(", minimum: "); //$NON-NLS-1$
		sb.append(getMinimum());
		sb.append(", maximum: "); //$NON-NLS-1$
		sb.append(getMaximum());
		sb.append(", thumb: "); //$NON-NLS-1$
		sb.append(getThumb());
		sb.append(", increment: "); //$NON-NLS-1$
		sb.append(getIncrement());
		sb.append(", pageIncrement: "); //$NON-NLS-1$
		sb.append(getPageIncrement());
		sb.append("}"); //$NON-NLS-1$
		return sb.toString();
	}
}
