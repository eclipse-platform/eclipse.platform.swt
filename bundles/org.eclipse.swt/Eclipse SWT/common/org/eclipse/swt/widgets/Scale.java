/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#scale">Scale snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Scale extends CustomControl {

	private int minimum = 0;
	private int maximum = 100;
	private int increment = 1;
	private int pageIncrement = 10;
	private int selection = 0;

	private final boolean horizontal;
	private final int orientation;

	private final ScaleRenderer renderer;

	/** The state of the handle */
	public enum HandleState {
		IDLE, HOVER, DRAG
	}

	private HandleState handleState = HandleState.IDLE;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Scale(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;

		Listener listener = event -> {
			switch (event.type) {
				case SWT.KeyDown -> onKeyDown(event);
				case SWT.MouseDown -> onMouseDown(event);
				case SWT.MouseMove -> onMouseMove(event);
				case SWT.MouseUp -> onMouseUp(event);
				case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
				case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
				case SWT.Paint -> onPaint(event);
				case SWT.Resize -> redraw();
			}
		};
		addListener(SWT.KeyDown, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseHorizontalWheel, listener);
		addListener(SWT.MouseVerticalWheel, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);

		horizontal = (style & SWT.VERTICAL) == 0;
		orientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);

		// re-add the horizontal/vertical flags
		super.style |= horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createScaleRenderer(this);
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	private static int checkStyle(int style) {
		// Do not propagate this flags to the super class
		style &= ~SWT.HORIZONTAL;
		style &= ~SWT.VERTICAL;

		if ((style & SWT.LEFT_TO_RIGHT) != 0) {
			style &= ~SWT.RIGHT_TO_LEFT;
		} else if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			style &= ~SWT.LEFT_TO_RIGHT;
		} else {
			style |= SWT.LEFT_TO_RIGHT;
			style &= ~SWT.RIGHT_TO_LEFT;
		}

		return style;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public boolean isVertical() {
		return !horizontal;
	}

	@Override
	public int getOrientation() {
		return orientation;
	}

	HandleState getHandleState() {
		return handleState;
	}

	private void onMouseMove(Event event) {
		if (!isVisible()) {
			return;
		}

		switch (handleState) {
		case IDLE, HOVER -> onSimpleMove(event);
		case DRAG -> onDrag(event);
		}
	}

	private void onSimpleMove(Event event) {
		Point position = toPoint(event);

		HandleState newHandleState;
		if (renderer.isWithinHandle(position)) {
			newHandleState = HandleState.HOVER;
		} else {
			newHandleState = HandleState.IDLE;
		}

		updateHandleColor(newHandleState);
	}

	private void onDrag(Event event) {
		HandleState newHandleState = HandleState.DRAG;
		int value = renderer.handlePosToValue(toPoint(event));
		selectAndNotify(minMax(minimum, value, maximum));
		updateHandleColor(newHandleState);
		redraw();
	}

	private void onMouseDown(Event event) {
		if (!isVisible()) {
			return;
		}

		Point position = toPoint(event);
		HandleState newHandleState;
		if (renderer.isWithinHandle(position)) {
			newHandleState = HandleState.DRAG;
		} else {
			newHandleState = HandleState.IDLE;
			if (renderer.isAfterHandle(position)) {
				pageIncrement(1);
			} else if (renderer.isBeforeHandle(position)) {
				pageIncrement(-1);
			}
		}

		updateHandleColor(newHandleState);

		forceFocus();
	}

	private void onMouseUp(Event event) {
		if (!isVisible()) {
			return;
		}

		Point position = toPoint(event);
		HandleState newHandleState;
		if (renderer.isWithinHandle(position)) {
			newHandleState = HandleState.HOVER;
		} else {
			newHandleState = HandleState.IDLE;
		}

		updateHandleColor(newHandleState);
	}

	private void onMouseVerticalWheel(Event event) {
		if (!isVisible()) {
			return;
		}

		if (event.count > 0) {
			increment(2);
		} else if (event.count < 0) {
			increment(-2);
		}
	}

	private void onMouseHorizontalWheel(Event event) {
		if (!isVisible()) {
			return;
		}

		if (event.count < 0) {
			increment(2);
		} else if (event.count > 0) {
			increment(-2);
		}
	}

	private void onKeyDown(Event event) {
		KeyEvent keyEvent = new KeyEvent(event);
		switch (keyEvent.keyCode) {
		case SWT.ARROW_DOWN, SWT.ARROW_LEFT -> increment(-1);
		case SWT.ARROW_RIGHT, SWT.ARROW_UP -> increment(1);
		case SWT.PAGE_DOWN -> pageIncrement(-1);
		case SWT.PAGE_UP -> pageIncrement(1);
		case SWT.HOME -> setSelection(getMinimum());
		case SWT.END -> setSelection(getMaximum());
		}
	}

	private void increment(int count) {
		int delta = increment * count;
		int newValue = minMax(minimum, selection + delta, maximum);
		setSelection(newValue);
		redraw();
	}

	private void pageIncrement(int count) {
		int delta = pageIncrement * count;
		int newValue = minMax(minimum, selection + delta, maximum);
		setSelection(newValue);
		redraw();
	}

	private void updateHandleColor(HandleState newHandleState) {
		if (newHandleState != handleState) {
			handleState = newHandleState;
			redraw();
		}
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}

		final Point size = getSize();
		if (size.x == 0 && size.y == 0) {
			return;
		}

		Drawing.drawWithGC(this, event.gc, gc -> renderer.paint(gc, size.x, size.y));
	}

	@Override
	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the user changes the receiver's value, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the user changes the receiver's
	 * value. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed.
	 *
	 * @return the increment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getIncrement() {
		checkWidget();
		return increment;
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 *
	 * @return the maximum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getMaximum() {
		checkWidget();
		return maximum;
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 *
	 * @return the minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getMinimum() {
		checkWidget();
		return minimum;
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected.
	 *
	 * @return the page increment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getPageIncrement() {
		checkWidget();
		return pageIncrement;
	}

	/**
	 * Returns the 'selection', which is the receiver's position.
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getSelection() {
		checkWidget();
		return selection;
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the user changes the receiver's value.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be at
	 * least one.
	 *
	 * @param increment the new increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setIncrement(int increment) {
		checkWidget();
		this.increment = Math.max(1, increment);
		redraw();
	}

	/**
	 * Sets the maximum value that the receiver will allow. This new value will be
	 * ignored if it is not greater than the receiver's current minimum value. If
	 * the new maximum is applied then the receiver's selection value will be
	 * adjusted if necessary to fall within its new range.
	 *
	 * @param maximum the new maximum, which must be greater than the current
	 *                minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMaximum(int maximum) {
		checkWidget();
		if (maximum < minimum) {
			return;
		}
		this.maximum = maximum;
		redraw();
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will be
	 * ignored if it is negative or is not less than the receiver's current maximum
	 * value. If the new minimum is applied then the receiver's selection value will
	 * be adjusted if necessary to fall within its new range.
	 *
	 * @param minimum the new minimum, which must be nonnegative and less than the
	 *                current maximum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMinimum(int minimum) {
		checkWidget();
		if (minimum < 0 || minimum < maximum) {
			return;
		}
		this.minimum = minimum;
		redraw();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the page
	 * increment/decrement areas are selected to the argument, which must be at
	 * least one.
	 *
	 * @param pageIncrement the page increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setPageIncrement(int pageIncrement) {
		checkWidget();
		this.pageIncrement = Math.max(1, pageIncrement);
		redraw();
	}

	/**
	 * Sets the 'selection', which is the receiver's value, to the argument which
	 * must be greater than or equal to zero. If set to a value smaller then the
	 * minimum, will the new selection value will be the minimum. If greater then
	 * the maximum, will set the new selection value will be the maximum.
	 *
	 * @param selection the new selection (must be zero or greater)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSelection(int selection) {
		checkWidget();
		selectAndNotify(minMax(minimum, selection, maximum));
		redraw();
	}

	private void selectAndNotify(int newValue) {
		if (newValue != selection) {
			selection = newValue;
			sendEvent(SWT.Selection, new Event());
		}
	}

	private int minMax(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
	}

	private Point toPoint(Event event) {
		return new Point(event.x, event.y);
	}
}
