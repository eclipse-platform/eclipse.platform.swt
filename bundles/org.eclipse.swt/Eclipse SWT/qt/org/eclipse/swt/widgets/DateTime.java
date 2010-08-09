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

import com.trolltech.qt.core.QDate;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.gui.QCalendarWidget;
import com.trolltech.qt.gui.QDateTimeEdit;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify date or time values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DATE, TIME, CALENDAR, SHORT, MEDIUM, LONG, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DATE, TIME, or CALENDAR may be specified, and
 * only one of the styles SHORT, MEDIUM, or LONG may be specified. The DROP_DOWN
 * style is only valid with the DATE style.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#datetime">DateTime
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * 
 * @since 3.3
 * @noextend This class is not intended to be subclassed by clients.
 */

public class DateTime extends Composite {
	boolean doubleClick, ignoreSelection;
	private QTime calendarTime;

	/**
	 * @return the time
	 */
	private QTime getCalendarTime() {
		return calendarTime;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	private void setCalendarTime(QTime time) {
		this.calendarTime = time;
	}

	static final char SINGLE_QUOTE = '\'';// short date format may include quoted text
	static final char DAY_FORMAT_CONSTANT = 'd';// 1-4 lowercase 'd's represent day
	static final char MONTH_FORMAT_CONSTANT = 'M';// 1-4 uppercase 'M's represent month
	static final char YEAR_FORMAT_CONSTANT = 'y';// 1-5 lowercase 'y's represent year
	static final char HOURS_FORMAT_CONSTANT = 'h';// 1-2 upper or lowercase 'h's represent hours
	static final char MINUTES_FORMAT_CONSTANT = 'm';// 1-2 lowercase 'm's represent minutes
	static final char SECONDS_FORMAT_CONSTANT = 's';// 1-2 lowercase 's's represent seconds
	static final char AMPM_FORMAT_CONSTANT = 't';// 1-2 lowercase 't's represent am/pm

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
	 * @see SWT#DATE
	 * @see SWT#TIME
	 * @see SWT#CALENDAR
	 * @see SWT#SHORT
	 * @see SWT#MEDIUM
	 * @see SWT#LONG
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */

	public DateTime(Composite parent, int style) {
		super(parent, checkStyle(style));
		setDateToToday();
		setTimeToNow();
	}

	@Override
	protected QWidget createQWidget(int style) {
		state &= ~(CANVAS | THEME_BACKGROUND);
		QWidget calendar = new QWidget();
		boolean isCalendarWidget = false;
		boolean isTimeEdit = false;
		if ((style & SWT.CALENDAR) != 0) {
			calendar = new QCalendarWidget();
			isCalendarWidget = true;
			((QCalendarWidget) calendar).setSelectedDate(QDate.currentDate());
			connectSignals(calendar, isCalendarWidget);
		} else if ((style & SWT.TIME) != 0) {
			isTimeEdit = true;
			/* Need to set time twice to get the right widget *with* time. */
			calendar = new QDateTimeEdit(QTime.currentTime());
			((QDateTimeEdit) calendar).setDate(QDate.currentDate());
			((QDateTimeEdit) calendar).setTime(QTime.currentTime());
		} else if ((style & SWT.ARROW) != 0 || (style & SWT.DROP_DOWN) != 0) {
			calendar = new QDateTimeEdit(QDate.currentDate());
			((QDateTimeEdit) calendar).setCalendarPopup(true);
			((QDateTimeEdit) calendar).setDate(QDate.currentDate());
			((QDateTimeEdit) calendar).setTime(QTime.currentTime());
		} else /* default is a QDateEdit */{
			calendar = new QDateTimeEdit(QDate.currentDate());
			((QDateTimeEdit) calendar).setTime(QTime.currentTime());
			((QDateTimeEdit) calendar).setDate(QDate.currentDate());
		}
		if (!isCalendarWidget) {
			connectSignals(calendar, isCalendarWidget);
			if ((style & SWT.SHORT) != 0) {
				if (isTimeEdit) {
					((QDateTimeEdit) calendar).setDisplayFormat(getShortTimeFormat());
				} else {
					((QDateTimeEdit) calendar).setDisplayFormat(getShortDateFormat());
				}
				/*
				 * when DisplayFormat is set I need to set the time *again* for
				 * some strange reasons
				 */
				((QDateTimeEdit) calendar).setTime(QTime.currentTime());
			} else if ((style & SWT.LONG) != 0) {
				((QDateTimeEdit) calendar).setDisplayFormat(getLongDateFormat());
				((QDateTimeEdit) calendar).setTime(QTime.currentTime());
			}
		}

		return calendar;
	}

	private void connectSignals(QWidget calendar, boolean isCalendarWidget) {
		if (isCalendarWidget) {
			((QCalendarWidget) calendar).clicked.connect(this, "clicked()"); //$NON-NLS-1$
			((QCalendarWidget) calendar).selectionChanged.connect(this, "clicked()"); //$NON-NLS-1$
		} else {
			/* some missing events */
			//((QDateTimeEdit) calendar).dateChanged.connect(this, "dateChanged()"); //$NON-NLS-1$
			//			((QDateTimeEdit) calendar).dateTimeChanged.connect(this, "dateTimeChanged()"); //$NON-NLS-1$
			//			((QDateTimeEdit) calendar).timeChanged.connect(this, "timeChanged()"); //$NON-NLS-1$
			//			((QDateTimeEdit) calendar).editingFinished.connect(this, "editingFinished"); //$NON-NLS-1$

		}
	}

	protected void clicked() {
		Event event = new Event();
		sendEvent(SWT.Selection, event);
	}

	/**
	 * sets the date to now
	 */
	private void setDateToToday() {
		QDate date = QDate.currentDate();
		setQtDate(date);
	}

	/**
	 * sets the date to now
	 */
	private void setTimeToNow() {
		setQtTime(QTime.currentTime());
		setCalendarTime(new QTime(0, 0, 0));
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the user changes the control's
	 * value. <code>widgetDefaultSelected</code> is typically called when ENTER
	 * is pressed.
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

	static int checkStyle(int style) {
		/*
		 * Even though it is legal to create this widget with scroll bars, they
		 * serve no useful purpose because they do not automatically scroll the
		 * widget's client area. The fix is to clear the SWT style.
		 */
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
		style = checkBits(style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
		style = checkBits(style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
		if ((style & SWT.DATE) == 0) {
			style &= ~SWT.DROP_DOWN;
		}
		return style;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		QWidget calendar = getQWidget();
		QSize size = calendar.sizeHint();
		return new Point(size.width(), size.height());
	}

	String getComputeSizeString() {
		// TODO: Not currently used but might need for WinCE
		if ((style & SWT.DATE) != 0) {
			if ((style & SWT.SHORT) != 0) {
				return getCustomShortDateFormat();
			}
			if ((style & SWT.MEDIUM) != 0) {
				return getShortDateFormat();
			}
			if ((style & SWT.LONG) != 0) {
				return getLongDateFormat();
			}
		}
		if ((style & SWT.TIME) != 0) {
			if ((style & SWT.SHORT) != 0) {
				return getCustomShortTimeFormat();
			}
			return getTimeFormat();
		}
		return ""; //$NON-NLS-1$
	}

	String getCustomShortDateFormat() {
		// TODO
		return "M/yyyy"; //$NON-NLS-1$

	}

	String getCustomShortTimeFormat() {
		StringBuffer buffer = new StringBuffer(getTimeFormat());
		int length = buffer.length();
		boolean inQuotes = false;
		int start = 0, end = 0;
		while (start < length) {
			char ch = buffer.charAt(start);
			if (ch == SINGLE_QUOTE) {
				inQuotes = !inQuotes;
			} else if (ch == SECONDS_FORMAT_CONSTANT && !inQuotes) {
				end = start + 1;
				while (end < length && buffer.charAt(end) == SECONDS_FORMAT_CONSTANT) {
					end++;
				}
				// skip the preceding separator
				while (start > 0 && buffer.charAt(start) != MINUTES_FORMAT_CONSTANT) {
					start--;
				}
				start++;
				break;
			}
			start++;
		}
		if (start < end) {
			buffer.delete(start, end);
		}
		return buffer.toString();
	}

	String getLongDateFormat() {
		return "dddd, d. MMMM  yyyy"; //$NON-NLS-1$
	}

	String getShortDateFormat() {
		return "MMMM yyyy"; //$NON-NLS-1$
	}

	String getShortTimeFormat() {
		return "hh:mm"; //$NON-NLS-1$
	}

	int getShortDateFormatOrdering() {
		// TODO
		return 0;
	}

	String getTimeFormat() {
		// TODO
		return "h:mm:ss tt"; //$NON-NLS-1$
	}

	boolean is24HourTime() {
		// TODO 
		return true;
	}

	/**
	 * Returns the receiver's date, or day of the month.
	 * <p>
	 * The first day of the month is 1, and the last day depends on the month
	 * and year.
	 * </p>
	 * 
	 * @return a positive integer beginning with 1
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getDay() {
		checkWidget();
		if (getQWidget() instanceof QCalendarWidget) {
			return ((QCalendarWidget) getQWidget()).selectedDate().day();
		}
		return ((QDateTimeEdit) getQWidget()).date().day();

	}

	/**
	 * Returns the receiver's hours.
	 * <p>
	 * Hours is an integer between 0 and 23.
	 * </p>
	 * 
	 * @return an integer between 0 and 23
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getHours() {
		checkWidget();
		if (getQWidget() instanceof QCalendarWidget) {
			return getCalendarTime().hour();
		}
		return ((QDateTimeEdit) getQWidget()).time().hour();

	}

	/**
	 * Returns the receiver's minutes.
	 * <p>
	 * Minutes is an integer between 0 and 59.
	 * </p>
	 * 
	 * @return an integer between 0 and 59
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMinutes() {
		checkWidget();
		if (getQWidget() instanceof QCalendarWidget) {
			return getCalendarTime().minute();
		}
		return ((QDateTimeEdit) getQWidget()).time().minute();

	}

	/**
	 * Returns the receiver's month.
	 * <p>
	 * The first month of the year is 0, and the last month is 11.
	 * </p>
	 * 
	 * @return an integer between 0 and 11
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMonth() {
		checkWidget();

		QDate date = getQtDate();
		return date.month() - 1;
	}

	@Override
	String getNameText() {
		return (style & SWT.TIME) != 0 ? getHours() + ":" + getMinutes() + ":" + getSeconds() : getMonth() + "/" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ getDay() + "/" + getYear(); //$NON-NLS-1$
	}

	/**
	 * Returns the receiver's seconds.
	 * <p>
	 * Seconds is an integer between 0 and 59.
	 * </p>
	 * 
	 * @return an integer between 0 and 59
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSeconds() {
		checkWidget();
		if (getQWidget() instanceof QCalendarWidget) {
			return getCalendarTime().second();
		}
		return ((QDateTimeEdit) getQWidget()).time().second();
	}

	/**
	 * Returns the receiver's year.
	 * <p>
	 * The first year is 1752 and the last year is 9999.
	 * </p>
	 * 
	 * @return an integer between 1752 and 9999
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getYear() {
		checkWidget();
		if (getQWidget() instanceof QCalendarWidget) {
			return ((QCalendarWidget) getQWidget()).selectedDate().year();
		}
		return ((QDateTimeEdit) getQWidget()).date().year();
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		//		lastSystemTime = null;
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
	 * Sets the receiver's year, month, and day in a single operation.
	 * <p>
	 * This is the recommended way to set the date, because setting the year,
	 * month, and day separately may result in invalid intermediate dates.
	 * </p>
	 * 
	 * @param year
	 *            an integer between 1752 and 9999
	 * @param month
	 *            an integer between 0 and 11
	 * @param day
	 *            a positive integer beginning with 1
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
	public void setDate(int year, int month, int day) {
		checkWidget();
		QDate date = new QDate(year, month + 1, day);
		if (getQWidget() instanceof QCalendarWidget) {
			((QCalendarWidget) getQWidget()).setSelectedDate(date);
		} else {
			((QDateTimeEdit) getQWidget()).setDate(date);
		}
	}

	/**
	 * Sets the receiver's date, or day of the month, to the specified day.
	 * <p>
	 * The first day of the month is 1, and the last day depends on the month
	 * and year. If the specified day is not valid for the receiver's month and
	 * year, then it is ignored.
	 * </p>
	 * 
	 * @param day
	 *            a positive integer beginning with 1
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setDate
	 */
	public void setDay(int day) {
		checkWidget();
		QDate date = getQtDate();
		date.setDate(date.year(), date.month(), day);
		setQtDate(date);
	}

	private QDate getQtDate() {
		if (getQWidget() instanceof QCalendarWidget) {
			return ((QCalendarWidget) getQWidget()).selectedDate();
		}
		return ((QDateTimeEdit) getQWidget()).date();
	}

	private QTime getQtTime() {
		if (getQWidget() instanceof QCalendarWidget) {
			return getCalendarTime();
		}
		return ((QDateTimeEdit) getQWidget()).time();
	}

	private void setQtDate(final QDate date) {
		if (getQWidget() instanceof QCalendarWidget) {
			((QCalendarWidget) getQWidget()).setSelectedDate(date);
		} else {
			((QDateTimeEdit) getQWidget()).setDate(date);
		}
	}

	private void setQtTime(final QTime time) {
		if (getQWidget() instanceof QCalendarWidget) {
			setCalendarTime(time);
		} else {
			((QDateTimeEdit) getQWidget()).setTime(time);
		}
	}

	/**
	 * Sets the receiver's hours.
	 * <p>
	 * Hours is an integer between 0 and 23.
	 * </p>
	 * 
	 * @param hours
	 *            an integer between 0 and 23
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setHours(int hours) {
		checkWidget();
		QTime time = new QTime(hours, getQtTime().minute(), getQtTime().second());
		setQtTime(time);
	}

	/**
	 * Sets the receiver's minutes.
	 * <p>
	 * Minutes is an integer between 0 and 59.
	 * </p>
	 * 
	 * @param minutes
	 *            an integer between 0 and 59
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMinutes(int minutes) {
		checkWidget();
		QTime time = new QTime(getQtTime().hour(), minutes, getQtTime().second());
		setQtTime(time);
	}

	/**
	 * Sets the receiver's month.
	 * <p>
	 * The first month of the year is 0, and the last month is 11. If the
	 * specified month is not valid for the receiver's day and year, then it is
	 * ignored.
	 * </p>
	 * 
	 * @param month
	 *            an integer between 0 and 11
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setDate
	 */
	public void setMonth(final int month) {
		checkWidget();

		QDate date = getQtDate();
		date.setDate(date.year(), month + 1, date.day());
		setQtDate(date);
	}

	/**
	 * Sets the receiver's seconds.
	 * <p>
	 * Seconds is an integer between 0 and 59.
	 * </p>
	 * 
	 * @param seconds
	 *            an integer between 0 and 59
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSeconds(int seconds) {
		checkWidget();
		QTime time = new QTime(getQtTime().hour(), getQtTime().minute(), seconds);
		setQtTime(time);

	}

	/**
	 * Sets the receiver's hours, minutes, and seconds in a single operation.
	 * 
	 * @param hours
	 *            an integer between 0 and 23
	 * @param minutes
	 *            an integer between 0 and 59
	 * @param seconds
	 *            an integer between 0 and 59
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
	public void setTime(int hours, int minutes, int seconds) {
		checkWidget();
		setQtTime(new QTime(hours, minutes, seconds));
	}

	/**
	 * Sets the receiver's year.
	 * <p>
	 * The first year is 1752 and the last year is 9999. If the specified year
	 * is not valid for the receiver's day and month, then it is ignored.
	 * </p>
	 * 
	 * @param year
	 *            an integer between 1752 and 9999
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setDate
	 */
	public void setYear(int year) {
		checkWidget();
		QDate date = new QDate(year, getQtDate().month(), getQtDate().day());
		setQtDate(date);
	}
}
