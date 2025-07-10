/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent an unselectable user interface object
 * that is used to display progress, typically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL, INDETERMINATE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#progressbar">ProgressBar
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ProgressBar extends CustomControl {
	static final int DELAY = 100;
	static final int TIMER_ID = 100;
	static final int MINIMUM_WIDTH = 100;

	int maximum = 100;
	private int minimum = 0;
	private int selection = 0;
	private DefaultProgressBarRenderer renderer = new DefaultProgressBarRenderer(this);
	private Listener listener;
	private int progressBarState = SWT.NORMAL;

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
	 * @see SWT#SMOOTH
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see SWT#INDETERMINATE
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ProgressBar(Composite parent, int style) {
		super(parent, checkStyle(style));



		listener = e -> {

			switch (e.type) {
			case SWT.Paint:
				
				onPaint(e);
				
				break;
			case SWT.Resize:
			{
				this.parent.redraw();
				this.redraw();
				break;
			}
			}

		};

		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		// the progressbar usually does not get a paint event, so we also listen to the
		// parent
		parent.addListener(SWT.Paint, listener);


	}

	private void onPaint(Event e) {
		drawRectangle(e);
		drawSelection(e);
		
	}


	@Override
	public void dispose() {
		parent.removeListener(SWT.Paint, listener);
		super.dispose();
	}

	static int checkStyle(int style) {
		style |= SWT.NO_FOCUS;
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
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
	 * Returns the single 'selection' that is the receiver's position.
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
	 * Returns the state of the receiver. The value will be one of:
	 * <ul>
	 * <li>{@link SWT#NORMAL}</li>
	 * <li>{@link SWT#ERROR}</li>
	 * <li>{@link SWT#PAUSED}</li>
	 * </ul>
	 *
	 * @return the state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.4
	 */
	public int getState() {
		checkWidget();
		return progressBarState;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
	}

	/**
	 * Sets the maximum value that the receiver will allow. This new value will be
	 * ignored if it is not greater than the receiver's current minimum value. If
	 * the new maximum is applied then the receiver's selection value will be
	 * adjusted if necessary to fall within its new range.
	 *
	 * @param value the new maximum, which must be greater than the current minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMaximum(int value) {
		checkWidget();
		if (0 <= minimum && minimum < value) {
			maximum = value;

			if (this.selection >= maximum)
				this.selection = maximum;

		}
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will be
	 * ignored if it is negative or is not less than the receiver's current maximum
	 * value. If the new minimum is applied then the receiver's selection value will
	 * be adjusted if necessary to fall within its new range.
	 *
	 * @param value the new minimum, which must be nonnegative and less than the
	 *              current maximum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMinimum(int value) {
		checkWidget();
		if (0 <= value && value < maximum) {
			minimum = value;
			if (this.selection < minimum)
				this.selection = minimum;
		}
	}

	/**
	 * Sets the single 'selection' that is the receiver's position to the argument
	 * which must be greater than or equal to zero.
	 *
	 * @param value the new selection (must be zero or greater)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSelection(int value) {
		checkWidget();

		if (value < minimum)
			value = minimum;

		if (value > maximum)
			value = maximum;

		if (value != this.selection) {
			this.selection = value;
			redraw();
		}
	}

	/**
	 * Sets the state of the receiver. The state must be one of these values:
	 * <ul>
	 * <li>{@link SWT#NORMAL}</li>
	 * <li>{@link SWT#ERROR}</li>
	 * <li>{@link SWT#PAUSED}</li>
	 * </ul>
	 * <p>
	 * Note: This operation is a hint and is not supported on platforms that do not
	 * have this concept.
	 * </p>
	 *
	 * @param state the new state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.4
	 */
	public void setState(int state) {
		checkWidget();

		if (state == SWT.NORMAL || state == SWT.ERROR || state == SWT.PAUSED)
			this.progressBarState = state;
	}


	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}
	

	private void drawSelection(Event e) {

		var gc = e.gc;
		var s = getSize();
		var width = s.x;
		var height =s.y;
		
		Rectangle b = null;
		
		if(e.widget == this) {
			b = new Rectangle(0, 0, width, height);
		}else {
			b = getBounds();
		}
		
		
		var prevBG = gc.getBackground();
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		gc.fillRectangle(b.x + 1, b.y + 1, b.width - 2, b.height - 2);
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
		if (getState() == SWT.PAUSED)
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		else if (getState() == SWT.ERROR) {
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
		}

		if ((SWT.INDETERMINATE & getStyle()) != 0) {

			int middleHeight = b.height / 2;
			int middleWidth  = b.width / 2;

			gc.fillRectangle(new Rectangle(b.x + 1, b.y + middleHeight - 1, b.width - 2, 2));

			if (b.width > 50) {
				drawDiamond(gc, b, b.width / 4);
				drawDiamond(gc, b, b.width / 2);
				drawDiamond(gc, b, b.width / 4 + b.width / 2);
			}

		} else if ((getStyle() & SWT.HORIZONTAL) != 0) {

			int value = getSelection();
			int min = getMinimum();
			int max = getMaximum();

			int completeDiff = max - min;
			var diff = value - min;


			int pixelWidth = (int) ((b.width - 2) * (((double) diff) / (double) (completeDiff)));


			gc.fillRectangle(new Rectangle(b.x + 1, b.y + 1, pixelWidth, b.height - 2));

		} else if ((getStyle() & SWT.VERTICAL) != 0) {

			int value = getSelection();
			int min = getMinimum();
			int max = getMaximum();

			int completeDiff = max - min;

			var diff = value - min;

			int pixelheight = (int) ((b.height - 2) * (((double) diff) / (double) (completeDiff)));


			gc.fillRectangle(
					new Rectangle(b.x + 1, b.y + 1 + ((b.height - 2) - pixelheight), b.width - 2, pixelheight));

		}

		gc.setBackground(prevBG);

	}

	private void drawDiamond(GC gc, Rectangle b, int position) {

		int middleHeight = b.height / 2;

		// draw a diamond
		gc.fillPolygon(new int[] { b.x + position - 5, b.y + middleHeight, // left of center
				b.x + position, b.y, // middle up
				b.x + position + 5, b.y + middleHeight, // right of center
				b.x + position, b.y + b.height, // middle down
		});

	}

	private void drawRectangle(Event e) {

		var gc = e.gc;
		var s = getSize();

		Rectangle b = null;
		if(e.widget == this)
			b = new Rectangle(0, 0, s.x, s.y);
		
		if(e.widget == getParent()) {
			b = getBounds();
		}
		
		
		var prevFG = gc.getForeground();

		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(b);
		gc.setForeground(prevFG);

	}

	@Override
	public Point computeDefaultSize() {

		return new Point(30, 30);

	}
	

}
