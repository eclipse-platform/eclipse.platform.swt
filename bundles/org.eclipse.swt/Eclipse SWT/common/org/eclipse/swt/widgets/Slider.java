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
 *     Raghunandana Murthappa(Advantest) - SkiJa Slider implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface objects that represent
 * a range of positive, numeric values.
 * <p>
 * At any given moment, a given slider will have a single 'selection' that is
 * considered to be its value, which is constrained to be within the range of
 * values the slider represents (that is, between its <em>minimum</em> and
 * <em>maximum</em> values).
 * </p>
 * <p>
 * Typically, sliders will be made up of five areas:
 * </p>
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * <p>
 * Based on their style, sliders are either <code>HORIZONTAL</code> (which have
 * a left facing button for decrementing the value and a right facing button for
 * incrementing it) or <code>VERTICAL</code> (which have an upward facing button
 * for decrementing the value and a downward facing buttons for incrementing
 * it).
 * </p>
 * <p>
 * On some platforms, the size of the slider's thumb can be varied relative to
 * the magnitude of the range of values it represents (that is, relative to the
 * difference between its maximum and minimum values). Typically, this is used
 * to indicate some proportional value such as the ratio of the visible area of
 * a document to the total amount of space that it would take to display it. SWT
 * supports setting the thumb size even if the underlying platform does not, but
 * in this case the appearance of the slider will not change.
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
 * @see ScrollBar
 * @see <a href="http://www.eclipse.org/swt/snippets/#slider">Slider
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Slider extends CustomControl {
	private final boolean horizontal;

	private int selection;
	private int minimum = 0;
	private int maximum = 100;
	private int thumb = 10;
	private int increment;
	private int pageIncrement;

	private int dragOffset;
	private int drawWidth;
	private int drawHeight;
	private int thumbPosition;

	private Rectangle trackRectangle;
	private Rectangle thumbRectangle;

	private final SliderRenderer renderer;

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
	public Slider(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createSliderRenderer(this);

		Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
			case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
			case SWT.Resize -> redraw();
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
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
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		horizontal = (style & SWT.VERTICAL) == 0;
		super.style |= horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
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

	private void onMouseEnter() {
		renderer.setDrawTrack(true);
		redraw();
	}

	private void onMouseExit() {
		renderer.setDrawTrack(false);
		renderer.setThumbHovered(false);
		redraw();
	}

	private void onMouseVerticalWheel(Event event) {
		if (event.count > 0) {
			increment(1);
		} else if (event.count < 0) {
			increment(-1);
		}
	}

	private void onMouseHorizontalWheel(Event event) {
		if (event.count < 0) {
			increment(1);
		} else if (event.count > 0) {
			increment(-1);
		}
	}

	private void increment(int count) {
		int delta = getIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
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

	private void pageIncrement(int count) {
		int delta = getPageIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}

		Rectangle drawingArea = getBounds();
		Drawing.drawWithGC(this, event.gc, gc -> renderer.paint(gc, drawingArea.width, drawingArea.height));
		this.drawWidth = drawingArea.width;
		this.drawHeight = drawingArea.height;
		this.thumbRectangle = renderer.getThumbRectangle();
		this.trackRectangle = renderer.getTrackRectangle();
	}

	private void onMouseDown(Event event) {
		if (event.button != 1) {
			return;
		}

		// Drag of the thumb.
		if (thumbRectangle != null && thumbRectangle.contains(event.x, event.y)) {
			renderer.setDragging(true);
			dragOffset = isHorizontal() ? event.x - thumbRectangle.x : event.y - thumbRectangle.y;
			return;
		}

		// Click on the track. i.e page increment/decrement
		if (trackRectangle != null && trackRectangle.contains(event.x, event.y)) {
			int pageIncrement = getPageIncrement();
			int oldSelection = getSelection();

			int newSelection;
			if (isHorizontal()) {
				if (event.x < thumbRectangle.x) {
					newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
				} else {
					newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
				}
			} else {
				if (event.y < thumbRectangle.y) {
					newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
				} else {
					newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
				}
			}

			setSelection(newSelection);
			redraw();
		}

	}

	private void onMouseUp(Event event) {
		if (renderer.getDragging()) {
			renderer.setDragging(false);
			updateValueFromThumbPosition();
			redraw();
		}
	}

	private void updateValueFromThumbPosition() {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();
		int range = max - min;

		int newValue;
		if (isHorizontal()) {
			int trackWidth = this.drawWidth - 4 - thumbRectangle.width;
			int relativeX = Math.min(trackWidth, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeX * (range - thumb)) / trackWidth;
		} else {
			int trackHeight = this.drawHeight - 4 - thumbRectangle.height;
			int relativeY = Math.min(trackHeight, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeY * (range - thumb)) / trackHeight;
		}
		setSelection(newValue);
	}

	private void onMouseMove(Event event) {
		if (thumbRectangle == null) {
			return;
		}

		boolean isThumbHovered = thumbRectangle.contains(event.x, event.y);
		if (isThumbHovered != renderer.getHovered()) {
			renderer.setThumbHovered(isThumbHovered);
			redraw();
		}

		if (renderer.getDragging()) {
			int newPos;
			int min = getMinimum();
			int max = getMaximum();
			int thumb = getThumb();
			int range = max - min;
			int newValue;

			if (isHorizontal()) {
				newPos = event.x - dragOffset;

				int minX = 2;
				int maxX = drawWidth - thumbRectangle.width - 2;
				newPos = Math.max(minX, Math.min(newPos, maxX));

				thumbRectangle.x = newPos;
				thumbPosition = newPos;

				int trackWidth = drawWidth - 4 - thumbRectangle.width;
				int relativeX = thumbPosition - 2;
				newValue = min + Math.round((relativeX * (float) (range - thumb)) / trackWidth);
			} else {
				newPos = event.y - dragOffset;

				int minY = 2;
				int maxY = drawHeight - thumbRectangle.height - 2;
				newPos = Math.max(minY, Math.min(newPos, maxY));

				thumbRectangle.y = newPos;
				thumbPosition = newPos;

				int trackHeight = drawHeight - 4 - thumbRectangle.height;
				int relativeY = thumbPosition - 2;
				newValue = min + Math.round((relativeY * (float) (range - thumb)) / trackHeight);
			}

			newValue = Math.max(min, Math.min(newValue, max - thumb));

			if (newValue != getSelection()) {
				setSelection(newValue);
			}
			redraw();
		}
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
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);

		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
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
		return pageIncrement;
	}

	/**
	 * Returns the 'selection', which is the receiver's value.
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
		return selection;
	}

	/**
	 * Returns the receiver's thumb value.
	 *
	 * @return the thumb value
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getThumb() {
		return thumb;
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
	 * Sets the maximum. If this value is negative or less than or equal to the
	 * minimum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param max the new maximum, which must be greater than the current minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMaximum(int max) {
		checkWidget();
		setMaximum(max, true);
	}

	private void setMaximum(int max, boolean redraw) {
		if (max <= minimum) {
			return;
		}

		this.maximum = max;

		this.thumb = Math.min(this.thumb, this.maximum - minimum);

		this.selection = Math.min(this.selection, this.maximum - this.thumb);
		this.selection = Math.max(this.selection, minimum);
		if (redraw) {
			redraw();
		}
	}

	/**
	 * Sets the minimum value. If this value is negative or greater than or equal to
	 * the maximum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param minimum the new minimum
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
		setMinimum(minimum, true);
	}

	private void setMinimum(int minimum, boolean redraw) {
		if (internalSetMinimum(minimum) && redraw) {
			redraw();
		}
	}

	private boolean internalSetMinimum(int minimum) {
		if (minimum < 0) {
			return false;
		}

		if (minimum <= this.maximum && minimum <= this.selection) {
			this.minimum = minimum;
			return true;
		}

		if (minimum <= this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			return true;
		}

		if (minimum >= maximum) {
			this.minimum = this.thumb;
			return true;
		}

		if (minimum > this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			this.thumb = this.maximum - this.minimum;
			return true;
		}
		return false;
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
	 * must be greater than or equal to zero.
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
		setSelection(selection, true);
	}

	private void setSelection(int selection, boolean redraw) {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();

		// Ensure selection does not exceed max - thumb
		int newValue = Math.max(min, Math.min(selection, max - thumb));

		if (newValue != this.selection) {
			this.selection = newValue;
			if (redraw) {
				redraw();
				sendEvent(SWT.Selection, new Event());
			}
		}
	}

	/**
	 * Sets the thumb value. The thumb value should be used to represent the size of
	 * the visual portion of the current range. This value is usually the same as
	 * the page increment value.
	 * <p>
	 * This new value will be ignored if it is less than one, and will be clamped if
	 * it exceeds the receiver's current range.
	 * </p>
	 *
	 * @param thumb the new thumb value, which must be at least one and not larger
	 *              than the size of the current range
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setThumb(int thumb) {
		checkWidget();
		setThumb(thumb, true);
	}

	private void setThumb(int thumb, boolean redraw) {
		if (thumb <= 0 || this.thumb == thumb) {
			return;
		}

		if (thumb <= this.maximum && thumb <= this.selection) {
			this.thumb = thumb;
			if (redraw) {
				redraw();
			}
			return;
		}

		if (thumb <= (this.maximum - this.minimum) && thumb > this.selection) {
			this.selection = this.maximum - thumb;
			this.thumb = thumb;
		}

		if (thumb > (this.maximum - this.minimum) && thumb > this.selection) {
			this.thumb = this.maximum - this.minimum;
			this.selection = this.minimum;
		}
		if (redraw) {
			redraw();
		}
	}

	/**
	 * Sets the receiver's selection, minimum value, maximum value, thumb, increment
	 * and page increment all at once.
	 * <p>
	 * Note: This is similar to setting the values individually using the
	 * appropriate methods, but may be implemented in a more efficient fashion on
	 * some platforms.
	 * </p>
	 *
	 * @param selection     the new selection value
	 * @param minimum       the new minimum value
	 * @param maximum       the new maximum value
	 * @param thumb         the new thumb value
	 * @param increment     the new increment value
	 * @param pageIncrement the new pageIncrement value
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
		checkWidget();
		setSelection(selection, false);
		setMinimum(minimum, false);
		setMaximum(maximum, false);
		setThumb(thumb, false);
		this.increment = Math.max(1, increment);
		this.pageIncrement = Math.max(1, pageIncrement);
		redraw();
	}
}
