/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.text.DateFormatSymbols; //TODO: not in CLDC
import java.util.Calendar; // TODO: Gregorian not in CLDC

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify date
 * or time values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DATE, TIME, CALENDAR, SHORT, MEDIUM, LONG</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DATE, TIME, or CALENDAR may be specified,
 * and only one of the styles SHORT, MEDIUM, or LONG may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#datetime">DateTime snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.3
 */
// TODO: locale is currently hard-coded to EN_US. This needs to be fixed. Use java.text.DateFormat?

// TODO: add accessibility to calendar - note: win32 calendar is not accessible... test gtk

// TODO: Consider allowing an optional drop-down calendar for SWT.DATE | SWT.DROP_DOWN

// TODO: Consider adding set/get day-of-week API, i.e. 1-7 (Sun-Sat)
// Win, Mac, and Java all provide this (but GTK does not).

public class DateTime extends Composite {
	Color fg, bg;
	Calendar calendar;
	DateFormatSymbols formatSymbols;
	Button down, up, monthDown, monthUp, yearDown, yearUp;
	Text text;
	Point[] fieldIndices;
	int[] fieldNames;
	int fieldCount, currentField = 0, characterCount = 0;
	boolean ignoreVerify = false;
	
	// TODO: default format strings need more work for locale
	static final String DEFAULT_SHORT_DATE_FORMAT = "MM/YYYY";
	static final String DEFAULT_MEDIUM_DATE_FORMAT = "MM/DD/YYYY";
	static final String DEFAULT_LONG_DATE_FORMAT = "MM/DD/YYYY";
	static final String DEFAULT_SHORT_TIME_FORMAT = "HH:MM AM";
	static final String DEFAULT_MEDIUM_TIME_FORMAT = "HH:MM:SS AM";
	static final String DEFAULT_LONG_TIME_FORMAT = "HH:MM:SS AM";
	static final int MARGIN_WIDTH = 2;
	static final int MARGIN_HEIGHT = 1;
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DATE
 * @see SWT#TIME
 * @see SWT#CALENDAR
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public DateTime(Composite parent, int style) {
	super(parent, checkStyle(style) | SWT.NO_REDRAW_RESIZE);
	calendar = Calendar.getInstance();
	formatSymbols = new DateFormatSymbols();
	if ((this.style & SWT.CALENDAR) != 0) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch(event.type) {
					case SWT.Paint:		handlePaint(event); break;
					case SWT.Resize:	handleResize(event); break;
					case SWT.MouseDown:	handleMouseDown(event); break;
					case SWT.KeyDown:	handleKeyDown(event); break;
					case SWT.Traverse:	handleTraverse(event); break;
				}
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.Traverse, listener);
		yearDown = new Button(this, SWT.ARROW | SWT.LEFT);
		//yearDown.setToolTipText(SWT.getMessage ("SWT_Last_Year")); //$NON-NLS-1$
		monthDown = new Button(this, SWT.ARROW | SWT.LEFT);
		//monthDown.setToolTipText(SWT.getMessage ("SWT_Last_Month")); //$NON-NLS-1$
		monthUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		//monthUp.setToolTipText(SWT.getMessage ("SWT_Next_Month")); //$NON-NLS-1$
		yearUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		//yearUp.setToolTipText(SWT.getMessage ("SWT_Next_Year")); //$NON-NLS-1$
		listener = new Listener() {
			public void handleEvent(Event event) {
				handleSelection(event);
			}
		};
		yearDown.addListener(SWT.Selection, listener);
		monthDown.addListener(SWT.Selection, listener);
		monthUp.addListener(SWT.Selection, listener);
		yearUp.addListener(SWT.Selection, listener);
	} else {
		text = new Text(this, SWT.SINGLE);
		if ((this.style & SWT.DATE) != 0) {
			setFormat((this.style & SWT.SHORT) != 0 ? DEFAULT_SHORT_DATE_FORMAT : (this.style & SWT.LONG) != 0 ? DEFAULT_LONG_DATE_FORMAT : DEFAULT_MEDIUM_DATE_FORMAT);
		} else { // SWT.TIME
			setFormat((this.style & SWT.SHORT) != 0 ? DEFAULT_SHORT_TIME_FORMAT : (this.style & SWT.LONG) != 0 ? DEFAULT_LONG_TIME_FORMAT : DEFAULT_MEDIUM_TIME_FORMAT);
		}
		text.setText(getFormattedString(this.style));
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch(event.type) {
					case SWT.KeyDown: onKeyDown(event); break;
					case SWT.FocusIn: onFocusIn(event); break;
					case SWT.FocusOut: onFocusOut(event); break;
					case SWT.MouseDown: onMouseClick(event); break;
					case SWT.MouseUp: onMouseClick(event); break;
					case SWT.Verify: onVerify(event); break;
				}
			}
		};
		text.addListener(SWT.KeyDown, listener);
		text.addListener(SWT.FocusIn, listener);
		text.addListener(SWT.FocusOut, listener);
		text.addListener(SWT.MouseDown, listener);
		text.addListener(SWT.MouseUp, listener);
		text.addListener(SWT.Verify, listener);
		up = new Button(this, SWT.ARROW | SWT.UP);
		//up.setToolTipText(SWT.getMessage ("SWT_Up")); //$NON-NLS-1$
		down = new Button(this, SWT.ARROW | SWT.DOWN);
		//down.setToolTipText(SWT.getMessage ("SWT_Down")); //$NON-NLS-1$
		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				incrementField(+1);
				text.setFocus();
			}
		});
		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				incrementField(-1);
				text.setFocus();
			}
		});
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				onResize(event);
			}
		});
	}
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
	return checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
}

String formattedStringValue(int fieldName, int value, boolean adjust) {
	if (fieldName == Calendar.AM_PM) {
		String[] ampm = formatSymbols.getAmPmStrings();
		return ampm[value];
	}
	if (adjust) {
		if (fieldName == Calendar.HOUR && value == 0) {
			return String.valueOf(12); // TODO: needs more work for setFormat and locale
		}
		if (fieldName == Calendar.MONTH) {
			return String.valueOf(value + 1);
		}
	}
	return String.valueOf(value);
}

String getFormattedString(int style) {
	if ((style & SWT.TIME) != 0) {
		String[] ampm = formatSymbols.getAmPmStrings();
		int h = calendar.get(Calendar.HOUR); if (h == 0) h = 12;
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		int a = calendar.get(Calendar.AM_PM);
		if ((style & SWT.SHORT) != 0) return "" + (h < 10 ? " " : "") + h + ":" + (m < 10 ? "0" : "") + m + " " + ampm[a];
		return "" + (h < 10 ? " " : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s + " " + ampm[a];
	}
	/* SWT.DATE */
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH) + 1;
	int d = calendar.get(Calendar.DAY_OF_MONTH);
	if ((style & SWT.SHORT) != 0) return "" + (m < 10 ? " " : "") + m + "/" + y;
	return "" + (m < 10 ? " " : "") + m + "/" + (d < 10 ? " " : "") + d + "/" + y;
}

String getComputeSizeString(int style) {
	if ((style & SWT.DATE) != 0) {
		return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_DATE_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_DATE_FORMAT : DEFAULT_MEDIUM_DATE_FORMAT;
	}
	// SWT.TIME
	return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_TIME_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_TIME_FORMAT : DEFAULT_MEDIUM_TIME_FORMAT;
}

int getFieldIndex(int fieldName) {
	for (int i = 0; i < fieldCount; i++) {
		if (fieldNames[i] == fieldName) {
			return i;
		}
	}
	return -1;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the control's value.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void commitCurrentField() {
	if (characterCount > 0) {
		characterCount = 0;
		int fieldName = fieldNames[currentField];
		int start = fieldIndices[currentField].x;
		int end = fieldIndices[currentField].y;
		String value = text.getText(start, end - 1);
		int s = value.lastIndexOf(' ');
		if (s != -1) value = value.substring(s + 1);
		int newValue = unformattedIntValue(fieldName, value, characterCount == 0, calendar.getActualMaximum(fieldName));
		if (newValue != -1) setTextField(fieldName, newValue, true, true);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	Rectangle trim;
	if ((style & SWT.CALENDAR) != 0) {
		Point cellSize = getCellSize(null);
		Point buttonSize = monthDown.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		width = cellSize.x * 7;
		height = cellSize.y * 7 + Math.max(cellSize.y, buttonSize.y);
	} else { /* SWT.DATE and SWT.TIME */
		GC gc = new GC(text);
		Point textSize = gc.stringExtent(getComputeSizeString(style));
		gc.dispose();
		trim = text.computeTrim(0, 0, textSize.x, textSize.y);
		Point buttonSize = up.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		width = trim.width + buttonSize.x;
		height = Math.max(trim.height, buttonSize.y);
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int borderWidth = getBorderWidth ();
	return new Point (width + 2*borderWidth, height + 2*borderWidth);
}

void drawDay(GC gc, Point cellSize, int day) {
	int cell = getCell(day);
	Point location = getCellLocation(cell, cellSize);
	String str = String.valueOf(day);
	Point extent = gc.stringExtent(str);
	int date = calendar.get(Calendar.DAY_OF_MONTH);
	if (day == date) {
		Display display = getDisplay();
		gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
		gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		gc.fillRectangle(location.x, location.y, cellSize.x, cellSize.y);
	}
	gc.drawString(str, location.x + (cellSize.x - extent.x) / 2, location.y + (cellSize.y - extent.y) / 2, true);
	if (day == date) {
		gc.setBackground(getBackground());
		gc.setForeground(getForeground());
	}
}

void drawDays(GC gc, Point cellSize, Rectangle client) {
	gc.setBackground(getBackground());
	gc.setForeground(getForeground());
	gc.fillRectangle(0, cellSize.y, client.width, cellSize.y * 7);
	int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	for (int day = firstDay; day <= lastDay; day++) {
		drawDay(gc, cellSize, day);
	}
}

void drawDaysOfWeek(GC gc, Point cellSize, Rectangle client) {
	Display display = getDisplay();
	gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
	gc.fillRectangle(0, 0, client.width, cellSize.y);
	String[] days = formatSymbols.getShortWeekdays();
	int x = 0, y = 0; 
	for (int i = 1; i < days.length; i++) {
		String day = days[i];
		Point extent = gc.stringExtent(day);
		gc.drawString(day, x + (cellSize.x - extent.x) / 2, y + (cellSize.y - extent.y) / 2, true);
		x += cellSize.x;
	}
	gc.drawLine(0, cellSize.y - 1, client.width, cellSize.y - 1);
}

void drawMonth(GC gc, Point cellSize, Rectangle client) {
	Display display = getDisplay();
	gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
	int y = cellSize.y * 7;
	gc.fillRectangle(0, y, client.width, cellSize.y);
	gc.drawLine(0, y - 1, client.width, y - 1);
	String str = formatSymbols.getShortMonths()[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR);
	Point extent = gc.stringExtent(str);
	gc.drawString(str, (cellSize.x * 7 - extent.x) / 2, y + (cellSize.y - extent.y) / 2, true);
}

Point getCellSize(GC gc) {
	boolean dispose = gc == null; 
	if (gc == null) gc = new GC(this);
	int width = 0, height = 0;
	String[] days = formatSymbols.getShortWeekdays();
	for (int i = 0; i < days.length; i++) {
		Point extent = gc.stringExtent(days[i]);
		width = Math.max(width, extent.x);
		height = Math.max(height, extent.y);
	}
	int firstDay = calendar.getMinimum(Calendar.DAY_OF_MONTH);
	int lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
	for (int day = firstDay; day <= lastDay; day++) {
		Point extent = gc.stringExtent(String.valueOf(day));
		width = Math.max(width, extent.x);
		height = Math.max(height, extent.y);	
	}
	if (dispose) gc.dispose();
	return new Point(width + MARGIN_WIDTH * 2, height + MARGIN_HEIGHT * 2);
}

Point getCellLocation(int cell, Point cellSize) {
	return new Point(cell % 7 * cellSize.x, cell / 7 * cellSize.y);
}

int getCell(int date) {
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	int result = date + calendar.get(Calendar.DAY_OF_WEEK) + 5;
	calendar.set(Calendar.DAY_OF_MONTH, day);
	return result;
}

int getDate(int cell) {
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	int result = cell - calendar.get(Calendar.DAY_OF_WEEK) - 5;
	calendar.set(Calendar.DAY_OF_MONTH, day);
	return result;
}

public Color getBackground() {
	checkWidget();
	if (bg == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}
	return bg;
}

/**
 * Returns the receiver's date, or day of the month.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * </p>
 *
 * @return a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDay() {
	checkWidget();
	return calendar.get(Calendar.DAY_OF_MONTH);
}

public Color getForeground() {
	checkWidget();
	if (fg == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}
	return fg;
}

/**
 * Returns the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @return an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHours () {
	checkWidget ();
	return calendar.get(Calendar.HOUR_OF_DAY);
}

/**
 * Returns the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinutes () {
	checkWidget ();
	return calendar.get(Calendar.MINUTE);
}

/**
 * Returns the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * </p>
 *
 * @return an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMonth() {
	checkWidget();
	return calendar.get(Calendar.MONTH);
}

String getNameText() {
	return (style & SWT.TIME) != 0 ? getHours() + ":" + getMinutes() + ":" + getSeconds()
			: (getMonth() + 1) + "/" + getDay() + "/" + getYear();
}

/**
 * Returns the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSeconds () {
	checkWidget ();
	return calendar.get(Calendar.SECOND);
}

/**
 * Returns the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * </p>
 *
 * @return an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getYear() {
	checkWidget();
	return calendar.get(Calendar.YEAR);
}

void handleKeyDown(Event event) {
	int newDay = calendar.get(Calendar.DAY_OF_MONTH);
	switch (event.keyCode) {
		case SWT.ARROW_DOWN: newDay += 7; break;
		case SWT.ARROW_UP: newDay -= 7; break;
		case SWT.ARROW_RIGHT: newDay += 1; break;
		case SWT.ARROW_LEFT: newDay -= 1; break;
	}
	setDay(newDay, true);
}

void handleMouseDown(Event event) {
	setFocus();
	Point cellSize = getCellSize(null);
	int column = event.x / cellSize.x;
	int row = event.y / cellSize.y;	
	int cell = row * 7 + column;
	int newDay = getDate(cell);
	setDay(newDay, true);
}

void handlePaint(Event event) {
	GC gc = event.gc;
	Rectangle client = getClientArea();
	Point cellSize = getCellSize(gc);
	drawDaysOfWeek(gc, cellSize, client);
	drawDays(gc, cellSize, client);
	drawMonth(gc, cellSize, client);
}

void handleResize(Event event) {
	yearDown.pack();
	monthDown.pack();
	monthUp.pack();
	yearUp.pack();
	Point cellSize = getCellSize(null);
	Point size = monthDown.getSize();
	int height = Math.max(cellSize.y, size.y);
	int y = cellSize.y * 7 + (height - size.y) / 2;
	yearDown.setLocation(0, y);
	monthDown.setLocation(size.x, y);
	int x = cellSize.x * 7 - size.x;
	monthUp.setLocation(x - size.x, y);
	yearUp.setLocation(x, y);
}

void handleSelection(Event event) {
	if (event.widget == monthDown) {
		calendar.add(Calendar.MONTH, -1);
	} else if (event.widget == monthUp) {
		calendar.add(Calendar.MONTH, 1);
	} else if (event.widget == yearDown) {
		calendar.add(Calendar.YEAR, -1);
	} else if (event.widget == yearUp) {				
		calendar.add(Calendar.YEAR, 1);
	} else {
		return;
	}
	redraw();
	postEvent(SWT.Selection);
}

void handleTraverse(Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
			event.doit = true;
			break;
	}	
}

boolean isValidTime(int fieldName, int value) {
	int min = calendar.getActualMinimum(fieldName);
	int max = calendar.getActualMaximum(fieldName);
	return value >= min && value <= max;
}

boolean isValidDate(int year, int month, int day) {
	if (year < MIN_YEAR || year > MAX_YEAR) return false;
	Calendar valid = Calendar.getInstance();
	valid.set(year, month, day);
	return valid.get(Calendar.YEAR) == year
		&& valid.get(Calendar.MONTH) == month
		&& valid.get(Calendar.DAY_OF_MONTH) == day;
}

void onKeyDown(Event event) {
	int fieldName;
	switch (event.keyCode) {
		case SWT.ARROW_RIGHT:
		case SWT.KEYPAD_DIVIDE:
			// a right arrow or a valid separator navigates to the field on the right, with wraping
			selectField((currentField + 1) % fieldCount);
			break;
		case SWT.ARROW_LEFT:
			// navigate to the field on the left, with wrapping
			int index = currentField - 1;
			selectField(index < 0 ? fieldCount - 1 : index);
			break;
		case SWT.ARROW_UP:
		case SWT.KEYPAD_ADD:
			// set the value of the current field to value + 1, with wrapping
			commitCurrentField();
			incrementField(+1);
			break;
		case SWT.ARROW_DOWN:
		case SWT.KEYPAD_SUBTRACT:
			// set the value of the current field to value - 1, with wrapping
			commitCurrentField();
			incrementField(-1);
			break;
		case SWT.HOME:
			// set the value of the current field to its minimum
			fieldName = fieldNames[currentField];
			setTextField(fieldName, calendar.getActualMinimum(fieldName), true, true);
			break;
		case SWT.END:
			// set the value of the current field to its maximum
			fieldName = fieldNames[currentField];
			setTextField(fieldName, calendar.getActualMaximum(fieldName), true, true);
			break;
		default:
			switch (event.character) {
				case '/':
				case ':':
				case '-':
				case '.':
					// a valid separator navigates to the field on the right, with wraping
					selectField((currentField + 1) % fieldCount);
					break;
			}
	}
}

void onFocusIn(Event event) {
	selectField(currentField);
}

void onFocusOut(Event event) {
	commitCurrentField();
}

void onMouseClick(Event event) {
	if (event.button != 1) return;
	Point sel = text.getSelection();
	for (int i = 0; i < fieldCount; i++) {
		if (fieldIndices[i].x <= sel.x && sel.x <= fieldIndices[i].y) {
			selectField(i);
			break;
		}
	}
}

void onResize(Event event) {
	Rectangle rect = getClientArea ();
	int width = rect.width;
	int height = rect.height;
	Point buttonSize = up.computeSize(SWT.DEFAULT, height);
	int buttonHeight = buttonSize.y / 2;
	text.setBounds(0, 0, width - buttonSize.x, height);
	up.setBounds(width - buttonSize.x, 0, buttonSize.x, buttonHeight);
	down.setBounds(width - buttonSize.x, buttonHeight, buttonSize.x, buttonHeight);
}

void onVerify(Event event) {
	if (ignoreVerify) return;
	event.doit = false;
	int fieldName = fieldNames[currentField];
	int start = fieldIndices[currentField].x;
	int end = fieldIndices[currentField].y;
	int length = end - start;
	String newText = event.text;
	if (fieldName == Calendar.AM_PM) {
		String[] ampm = formatSymbols.getAmPmStrings();
		if (newText.equalsIgnoreCase(ampm[Calendar.AM].substring(0, 1)) || newText.equalsIgnoreCase(ampm[Calendar.AM])) {
			setTextField(fieldName, Calendar.AM, true, false);
		} else if (newText.equalsIgnoreCase(ampm[Calendar.PM].substring(0, 1)) || newText.equalsIgnoreCase(ampm[Calendar.PM])) {
			setTextField(fieldName, Calendar.PM, true, false);
		}
		return;
	}
	if (characterCount > 0) {
		try {
			Integer.parseInt(newText);
		} catch (NumberFormatException ex) {
			return;
		}
		String value = text.getText(start, end - 1);
		int s = value.lastIndexOf(' ');
		if (s != -1) value = value.substring(s + 1);
		newText = "" + value + newText;
	}
	int newTextLength = newText.length();
	boolean first = characterCount == 0;
	characterCount = (newTextLength < length) ? newTextLength : 0;
	int max = calendar.getActualMaximum(fieldName);
	int min = calendar.getActualMinimum(fieldName);
	int newValue = unformattedIntValue(fieldName, newText, characterCount == 0, max);
	if (newValue == -1) {
		characterCount = 0;
		return;
	}
	if (first && newValue == 0 && length > 1) {
		setTextField(fieldName, newValue, false, false);
	} else if (min <= newValue && newValue <= max) {
		setTextField(fieldName, newValue, characterCount == 0, characterCount == 0);
	} else {
		if (newTextLength >= length) {
			newText = newText.substring(newTextLength - length + 1);
			newValue = unformattedIntValue(fieldName, newText, characterCount == 0, max);
			if (newValue != -1) {
				characterCount = length - 1;
				if (min <= newValue && newValue <= max) {
					setTextField(fieldName, newValue, characterCount == 0, true);
				}
			}
		}
	}
}

void incrementField(int amount) {
	int fieldName = fieldNames[currentField];
	int value = calendar.get(fieldName);
	if (fieldName == Calendar.HOUR) {
		int max = calendar.getMaximum(Calendar.HOUR);
		int min = calendar.getMinimum(Calendar.HOUR);
		if ((value == max && amount == 1) || (value == min && amount == -1)) {
			int temp = currentField;
			currentField = getFieldIndex(Calendar.AM_PM);
			setTextField(Calendar.AM_PM, (calendar.get(Calendar.AM_PM) + 1) % 2, true, true);
			currentField = temp;
		}
	}
	setTextField(fieldName, value + amount, true, true);
}

void selectField(int index) {
	if (index != currentField) {
		commitCurrentField();
	}
	final int start = fieldIndices[index].x;
	final int end = fieldIndices[index].y;
	Point pt = text.getSelection();
	if (index == currentField && start == pt.x && end == pt.y) return;
	currentField = index;
	display.asyncExec(new Runnable() {
		public void run() {
			if (!text.isDisposed()) {
				String value = text.getText(start, end - 1);
				int s = value.lastIndexOf(' ');
				if (s == -1) s = start;
				else s = start + s + 1;
				text.setSelection(s, end);
			}
		}
	});	
}

void setTextField(int fieldName, int value, boolean commit, boolean adjust) {
	if (commit) {
		int max = calendar.getActualMaximum(fieldName);
		int min = calendar.getActualMinimum(fieldName);
		if (fieldName == Calendar.YEAR) {
			max = MAX_YEAR;
			min = MIN_YEAR;
			/* Special case: convert 1 or 2-digit years into reasonable 4-digit years. */
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int currentCentury = (currentYear / 100) * 100;
			if (value < (currentYear + 30) % 100) value += currentCentury;
			else if (value < 100) value += currentCentury - 100;
		}
		if (value > max) value = min; // wrap
		if (value < min) value = max; // wrap
	}
	int start = fieldIndices[currentField].x;
	int end = fieldIndices[currentField].y;
	text.setSelection(start, end);
	String newValue = formattedStringValue(fieldName, value, adjust);
	StringBuffer buffer = new StringBuffer(newValue);
	/* Convert leading 0's into spaces. */
	int prependCount = end - start - buffer.length();
	for (int i = 0; i < prependCount; i++) {
		switch (fieldName) {
		case Calendar.MINUTE:
		case Calendar.SECOND:
			buffer.insert(0, 0);
		break;
		default:
			buffer.insert(0, ' ');
		break;
		}
	}	
	newValue = buffer.toString();
	ignoreVerify = true;
	text.insert(newValue);
	ignoreVerify = false;
	selectField(currentField);
	if (commit) setField(fieldName, value);
}

void setField(int fieldName, int value) {
	if (calendar.get(fieldName) == value) return;
	if (fieldName == Calendar.AM_PM) {
		calendar.roll(Calendar.HOUR_OF_DAY, 12); // TODO: needs more work for setFormat and locale
	}
	calendar.set(fieldName, value);
	postEvent(SWT.Selection);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection,listener);	
}

void redraw(int cell, Point cellSize) {
	Point location = getCellLocation(cell, cellSize);
	redraw(location.x, location.y, cellSize.x, cellSize.y, false);	
}

public void setBackground(Color color) {
	checkWidget();
	super.setBackground(color);
	bg = color;
	if (text != null) text.setBackground(color);
}

/**
 * Sets the receiver's year, month, and day in a single operation.
 * <p>
 * This is the recommended way to set the date, because setting the year,
 * month, and day separately may result in invalid intermediate dates.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 * @param month an integer between 0 and 11
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setDate (int year, int month, int day) {
	checkWidget ();
	if (!isValidDate(year, month, day)) return;
	calendar.set(Calendar.YEAR, year);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	calendar.set(Calendar.MONTH, month);
	if ((style & SWT.CALENDAR) != 0) {
		updateControl();
		setDay(day, false);
	} else {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		updateControl();
	}
}

/**
 * Sets the receiver's date, or day of the month, to the specified day.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * </p>
 *
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDay (int day) {
	checkWidget();
	if (!isValidDate(getYear(), getMonth(), day)) return;
	if ((style & SWT.CALENDAR) != 0) {
		setDay(day, false);
	} else {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		updateControl();
	}
}

void setDay(int newDay, boolean notify) {
	int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	if (!(firstDay <= newDay && newDay <= lastDay)) return;
	Point cellSize = getCellSize(null);
	redraw(getCell(calendar.get(Calendar.DAY_OF_MONTH)), cellSize);
	calendar.set(Calendar.DAY_OF_MONTH, newDay);
	redraw(getCell(calendar.get(Calendar.DAY_OF_MONTH)), cellSize);
	if (notify) postEvent(SWT.Selection);
}

public void setFont(Font font) {
	checkWidget();
	super.setFont(font);
	if (text != null) text.setFont(font);
	redraw();
}

public void setForeground(Color color) {
	checkWidget();
	super.setForeground(color);
	fg = color;
	if (text != null) text.setForeground(color);
}

void setFormat(String string) {
	checkWidget();
	// TODO: this needs to be locale sensitive
	fieldCount = (style & SWT.DATE) != 0 ? ((style & SWT.SHORT) != 0 ? 2 : 3) : ((style & SWT.SHORT) != 0 ? 3 : 4);
	fieldIndices = new Point[fieldCount];
	fieldNames = new int[fieldCount];
	if ((style & SWT.DATE) != 0) {
		fieldNames[0] = Calendar.MONTH;
		fieldIndices[0] = new Point(0, 2);
		if ((style & SWT.SHORT) != 0) {
			fieldNames[1] = Calendar.YEAR;
			fieldIndices[1] = new Point(3, 7);
		} else {
			fieldNames[1] = Calendar.DAY_OF_MONTH;
			fieldIndices[1] = new Point(3, 5);
			fieldNames[2] = Calendar.YEAR;
			fieldIndices[2] = new Point(6, 10);
		}
	} else { /* SWT.TIME */
		fieldNames[0] = Calendar.HOUR;
		fieldIndices[0] = new Point(0, 2);
		fieldNames[1] = Calendar.MINUTE;
		fieldIndices[1] = new Point(3, 5);
		if ((style & SWT.SHORT) != 0) {
			fieldNames[2] = Calendar.AM_PM;
			fieldIndices[2] = new Point(6, 8);
		} else {
			fieldNames[2] = Calendar.SECOND;
			fieldIndices[2] = new Point(6, 8);
			fieldNames[3] = Calendar.AM_PM;
			fieldIndices[3] = new Point(9, 11);
		}
	}
}

/**
 * Sets the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @param hours an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHours (int hours) {
	checkWidget ();
	if (!isValidTime(Calendar.HOUR_OF_DAY, hours)) return;
	calendar.set(Calendar.HOUR_OF_DAY, hours);
	updateControl();
}

/**
 * Sets the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @param minutes an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinutes (int minutes) {
	checkWidget ();
	if (!isValidTime(Calendar.MINUTE, minutes)) return;
	calendar.set(Calendar.MINUTE, minutes);
	updateControl();
}

/**
 * Sets the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * </p>
 *
 * @param month an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMonth (int month) {
	checkWidget();
	if (!isValidDate(getYear(), month, getDay())) return;
	calendar.set(Calendar.MONTH, month);
	updateControl();
}

/**
 * Sets the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSeconds (int seconds) {
	checkWidget ();
	if (!isValidTime(Calendar.SECOND, seconds)) return;
	calendar.set(Calendar.SECOND, seconds);
	updateControl();
}

/**
 * Sets the receiver's hours, minutes, and seconds in a single operation.
 *
 * @param hours an integer between 0 and 23
 * @param minutes an integer between 0 and 59
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setTime (int hours, int minutes, int seconds) {
	checkWidget ();
	if (!isValidTime(Calendar.HOUR_OF_DAY, hours)) return;
	if (!isValidTime(Calendar.MINUTE, minutes)) return;
	if (!isValidTime(Calendar.SECOND, seconds)) return;
	calendar.set(Calendar.HOUR_OF_DAY, hours);
	calendar.set(Calendar.MINUTE, minutes);
	calendar.set(Calendar.SECOND, seconds);
	updateControl();
}

/**
 * Sets the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setYear (int year) {
	checkWidget();
	//if (!isValidDate(year, getMonth(), getDay())) return;
	if (year < MIN_YEAR || year > MAX_YEAR) return;
	calendar.set(Calendar.YEAR, year);
	updateControl();
}

int unformattedIntValue(int fieldName, String newText, boolean adjust, int max) {
	int newValue;
	try {
		newValue = Integer.parseInt(newText);
	} catch (NumberFormatException ex) {
		return -1;
	}
	if (fieldName == Calendar.MONTH && adjust) {
		newValue--;
		if (newValue == -1) newValue = max;
	}
	if (fieldName == Calendar.HOUR && adjust) {
		if (newValue == 12) newValue = 0; // TODO: needs more work for setFormat and locale
	}
	return newValue;
}

void updateControl() {
	if (text != null) {
		String string = getFormattedString(style);
		ignoreVerify = true;
		text.setText(string);
		ignoreVerify = false;
	}
	redraw();	
}
}