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
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;

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
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
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
public class ProgressBar extends Control {
	static final int DELAY = 100;
	int foreground = -1, background = -1;
	private int timerID = -1;

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
	 * @see SWT#SMOOTH
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ProgressBar(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		QProgressBar progressBar = new QProgressBar();
		return progressBar;
	}

	@Override
	protected void setupQWidget() {
		if ((style & SWT.INDETERMINATE) != 0) {
			getQProgressBar().setRange(0, 0);
			// Funny: this avoids a JVM crash: EXCEPTION_INT_DIVIDE_BY_ZERO (0xc0000094)
			// http://eclipse.compeople.eu/wiki/index.php/Compeople:SWTQtImplementierungsdetails#DIVISION_BY_ZERO
			getQProgressBar().setTextVisible(false);
		}
		// this causes a lot of events, commented for debugging
		//startTimer(getQProgressBar());
		super.setupQWidget();
	}

	private QProgressBar getQProgressBar() {
		return (QProgressBar) getQWidget();
	}

	static int checkStyle(int style) {
		style |= SWT.NO_FOCUS;
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		QSize size = getQProgressBar().sizeHint();
		return new Point(size.width(), size.height());
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
		return getQProgressBar().maximum();
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
		return getQProgressBar().minimum();
	}

	/**
	 * Returns the single 'selection' that is the receiver's position.
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
		return getQProgressBar().value();
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
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public int getState() {
		checkWidget();
		return SWT.NORMAL;
	}

	@Override
	void releaseWidget() {
		stopTimer();
		super.releaseWidget();
	}

	void startTimer(QProgressBar progressBar) {
		stopTimer();
		// TODO dynamic interval for startTimer
		timerID = progressBar.startTimer(DELAY);
	}

	void stopTimer() {
		if (timerID != -1) {
			getQProgressBar().killTimer(timerID);
		}
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
		getQProgressBar().setMaximum(value);
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
		getQProgressBar().setMinimum(value);
	}

	/**
	 * Sets the single 'selection' that is the receiver's position to the
	 * argument which must be greater than or equal to zero.
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
		getQProgressBar().setValue(value);
	}

	/**
	 * Sets the state of the receiver. The state must be one of these values:
	 * <ul>
	 * <li>{@link SWT#NORMAL}</li>
	 * <li>{@link SWT#ERROR}</li>
	 * <li>{@link SWT#PAUSED}</li>
	 * </ul>
	 * 
	 * @param state
	 *            the new state
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
	public void setState(int state) {
		checkWidget();
	}

}
