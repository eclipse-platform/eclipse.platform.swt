package org.eclipse.swt.widgets;


import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.events.*;

// TODO: Windows returns 1-12 for MONTH. Java returns 0-11. GTK calendar returns 0-11. Mac text date returns 1-12.
// TODO: need to be consistent. Show 1-12 AND spec that setting is 1-12. Ignore Java, and fix GTK.

// TODO: bug in emulated get/setHour - should be 24 hour. Win is ok - check Mac.

// TODO: Make sure all set/get API's work - write the spec for these (incl min/max?).
// make sure to test for all styles and for Win & emulated (Mac & GTK later)

// TODO: AMPM field in calendar won't change to AM (0)??

// TODO: Make sure that SWT.Selection works correctly for all styles and all cases (including Win & emulated)

// TODO: Make sure setting the day/month/etc programmatically does NOT send selection callback.

// TODO: Update all text fields after setting one text field, because others can change too

// TODO: Use roll for increment/decrement? (should be ok - do the update all fields first)

// TODO: Note that on Win, time 'roll' modifies am/pm (i.e. it 'rolls' through 24 hours, not 12). Verify.

// TODO: implement setFormat - figure out what formats the Mac supports. Figure out the semantics
// of setFormat for Calendar widget (Win, GTK, Mac cocoa)... Felipe was saying maybe just have long/short...
// if possible, make it API.

// TODO: Figure out what happens in different locales, i.e. I assume the native guys change somehow.
// currently only Mac & Win have the text guys - what happens in those? (how to create for different locale?)
// (maybe the Nebula guys Locale changers could be useful for this)

// TODO: Consider adding set/get day-of-week API, i.e. 1-7 (Sun-Sat)
// Otherwise people have to go look it up, and Win, Mac, and Java all provide this - we should, too

// TODO: Write the little print calendar month app

// TODO: Would be nice to allow an optional drop-down calendar for SWT.DATE - figure out semantics.

public class DateTime extends Composite {
	Color foreground, background;
	Calendar calendar;
	DateFormatSymbols formatSymbols;
	Button down, up, monthDown, monthUp, yearDown, yearUp;
	Text text;
	String format;
	Point[] fieldIndices;
	int[] fieldNames;
	int fieldCount, currentField = 0, characterCount = 0;
	boolean ignoreVerify = false;
	
	static int[] validSeparators = {'/', ':', '-', '.', SWT.KEYPAD_DIVIDE}; // TODO might not need this
	static String defaultDateFormat = "MM/DD/YYYY";
	static String defaultTimeFormat = "HH:MM:SS AM";
	static final int MARGIN_WIDTH = 2;
	static final int MARGIN_HEIGHT = 1;

public DateTime(Composite parent, int style) {
	super(parent, checkStyle(style) | SWT.NO_REDRAW_RESIZE);
	calendar = Calendar.getInstance();
	if ((this.style & SWT.CALENDAR) != 0) {
		formatSymbols = new DateFormatSymbols();
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
		// TODO: move tooltip strings to .properties
		yearDown = new Button(this, SWT.ARROW | SWT.LEFT);
		yearDown.setToolTipText("Last Year");
		monthDown = new Button(this, SWT.ARROW | SWT.LEFT);
		monthDown.setToolTipText("Last Month");
		monthUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		monthUp.setToolTipText("Next Month");
		yearUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		yearUp.setToolTipText("Next Year");
		listener = new Listener() {
			public void handleEvent(Event event) {
				handleSelection(event);
			}
		};
		yearDown.addListener(SWT.Selection, listener);
		monthDown.addListener(SWT.Selection, listener);
		monthUp.addListener(SWT.Selection, listener);
		yearUp.addListener(SWT.Selection, listener);
		// TODO: add accessibility - hmmm... win32 calendar is not accessible... but text guys are...
	} else {
		text = new Text(this, SWT.SINGLE);
		setFormat((this.style == SWT.DATE ? defaultDateFormat : defaultTimeFormat));
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
		// TODO: move tooltip strings to .properties
		up = new Button(this, SWT.ARROW | SWT.UP);
		up.setToolTipText("Up");
		down = new Button(this, SWT.ARROW | SWT.DOWN);
		down.setToolTipText("Down");
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

String getFormattedString(int style) {
	// TODO: this method needs to be more generic (see setFormat)
	if ((style & SWT.TIME) != 0) {
		int h = calendar.get(Calendar.HOUR);
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		int a = calendar.get(Calendar.AM_PM);
		return "" + (h < 10 ? " " : "") + h + ":" + (m < 10 ? " " : "") + m + ":" + (s < 10 ? " " : "") + s + " " + (a == Calendar.AM ? "AM" : "PM");
	}
	/* SWT.DATE */
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH);
	int d = calendar.get(Calendar.DAY_OF_MONTH);
	return "" + (m < 10 ? " " : "") + m + "/" + (d < 10 ? " " : "") + d + "/" + y;
}

String getComputeSizeString(int style) {
	// TODO: this method needs to be more generic (see setFormat)
	return (style & SWT.TIME) != 0 ? defaultTimeFormat : defaultDateFormat;
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
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

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
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
	if (background == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}
	return background;
}

public int getDay() {
	checkWidget();
	return calendar.get(Calendar.DAY_OF_MONTH);
}

//public long getDate() {
//	checkWidget();
//	return calendar.getTimeInMillis();
//}
//
public Color getForeground() {
	checkWidget();
	if (foreground == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}
	return foreground;
}

public int getHour () {
	checkWidget ();
	return calendar.get(Calendar.HOUR);
}

public int getMinute () {
	checkWidget ();
	return calendar.get(Calendar.MINUTE);
}

public int getMonth() {
	checkWidget();
	return calendar.get(Calendar.MONTH) + 1;
}

public int getSecond () {
	checkWidget ();
	return calendar.get(Calendar.SECOND);
}

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
	notifyListeners(SWT.Selection, new Event());
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
			incrementField(+1);
			break;
		case SWT.ARROW_DOWN:
		case SWT.KEYPAD_SUBTRACT:
			// set the value of the current field to value - 1, with wrapping
			incrementField(-1);
			break;
		case SWT.HOME:
			// set the value of the current field to its minimum
			fieldName = fieldNames[currentField];
			setTextField(fieldName, calendar.getActualMinimum(fieldName), true);
			break;
		case SWT.END:
			// set the value of the current field to its maximum
			fieldName = fieldNames[currentField];
			setTextField(fieldName, calendar.getActualMaximum(fieldName), true);
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
	if (characterCount > 0) {
		characterCount = 0;
		int fieldName = fieldNames[currentField];
		setTextField(fieldName, Integer.parseInt(text.getSelectionText()), true);
	}
}

void onMouseClick(Event event) {
	if (event.button != 1) return;
	Point sel = text.getSelection();
	for (int i = 0; i < fieldCount; i++) {
		if (sel.x >= fieldIndices[i].x && sel.x <= fieldIndices[i].y) {
			currentField = i;
			break;
		}
	}
	selectField(currentField);
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
		if (newText.equalsIgnoreCase("A") || newText.equalsIgnoreCase("AM")) {
			setTextField(fieldName, Calendar.AM, true);
		} else if (newText.equalsIgnoreCase("P") || newText.equalsIgnoreCase("PM")) {
			setTextField(fieldName, Calendar.PM, true);
		}
		return;
	}
	if (characterCount > 0) {
		newText = "" + text.getSelectionText() + newText;
	}
	int newTextLength = newText.length();
	characterCount = (newTextLength < length) ? newTextLength : 0;
	try {
		int max = calendar.getActualMaximum(fieldName);
		int min = calendar.getActualMinimum(fieldName);
		int newValue = Integer.parseInt(newText);
		if (min <= newValue && newValue <= max) {
			setTextField(fieldName, newValue, characterCount == 0);
		} else {
			if (newTextLength >= length) {
				newText = newText.substring(newTextLength - length + 1);
				newValue = Integer.parseInt(newText);
				characterCount = length - 1;
				if (min <= newValue && newValue <= max) {
					setTextField(fieldName, newValue, characterCount == 0);
				}
			}
		}
	} catch (NumberFormatException ex) {
	}
}

void incrementField(int amount) {
	int fieldName = fieldNames[currentField];
	int value = calendar.get(fieldName);
	setTextField(fieldName, value + amount, true);
}

void selectField(int index) {
	if (characterCount > 0 && index != currentField) {
		characterCount = 0;
		int fieldName = fieldNames[currentField];
		setTextField(fieldName, Integer.parseInt(text.getSelectionText()), true);
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

void setTextField(int fieldName, int value, boolean commit) {
	int max = calendar.getActualMaximum(fieldName);
	int min = calendar.getActualMinimum(fieldName);
	if (fieldName == Calendar.YEAR) {
		max = 9999;
		min = 1000;
	}
	if (value > max) value = min; // wrap
	if (commit) {
		if (fieldName == Calendar.YEAR) {
			/* Special case: convert 2-digit years into reasonable 4-digit years. */
			if (value < 30) value += 2000;
			else if (value <= 99) value += 1900;
		}
		if (value < min) value = max; // wrap
	}
	int start = fieldIndices[currentField].x;
	int end = fieldIndices[currentField].y;
	text.setSelection(start, end);
	String newValue;
	if (fieldName == Calendar.AM_PM) {
		newValue = value == Calendar.AM ? "AM" : "PM";
	} else {
		StringBuffer buffer = new StringBuffer(String.valueOf(value));
		int prependCount = end - start - buffer.length();
		for (int i = 0; i < prependCount; i++) {
			buffer.insert(0, ' ');
		}
		newValue = buffer.toString();
	}
	ignoreVerify = true;
	text.insert(newValue);
	ignoreVerify = false;
	selectField(currentField);
	if (commit) setField(fieldName, value);
}

void setField(int fieldName, int value) {
	// TODO: make sure this is called on focus lost, field change, and field 'full' (plus home, end, increment)
	// assumes that the value is already valid for this field
	if (calendar.get(fieldName) == value) return;
	// TODO: other fields can change value also, so when current field changes,
	// check whether other fields changes and set edit fields accordingly
	calendar.set(fieldName, value);
	notifyListeners(SWT.Selection, new Event());
}

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
	background = color;
	if (text != null) text.setBackground(color);
}

//public void setDate(long date) {
//	checkWidget();
//	calendar.setTimeInMillis(date);
//	redraw();
//}
//
public void setDay(int day) {
	checkWidget();
	setDay(day, false);
}

void setDay(int newDay, boolean notify) {
	int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	if (!(firstDay <= newDay && newDay <= lastDay)) return;
	Point cellSize = getCellSize(null);
	redraw(getCell(calendar.get(Calendar.DAY_OF_MONTH)), cellSize);
	calendar.set(Calendar.DAY_OF_MONTH, newDay);
	redraw(getCell(calendar.get(Calendar.DAY_OF_MONTH)), cellSize);
	if (notify) notifyListeners(SWT.Selection, new Event());
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
	foreground = color;
	if (text != null) text.setForeground(color);
}

/*public*/ void setFormat(String string) {
	checkWidget();
	// TODO: this function needs to be more generic.
	// i.e. parse string (e.g. look for hh:mm:ss, etc), and set fieldNames and fieldIndices according to string
	fieldCount = (style & SWT.DATE) != 0 ? 3 : 4;
	fieldIndices = new Point[fieldCount];
	fieldNames = new int[fieldCount];
	if ((style & SWT.DATE) != 0) {
		fieldNames[0] = Calendar.MONTH;
		fieldIndices[0] = new Point(0, 2);
		fieldNames[1] = Calendar.DAY_OF_MONTH;
		fieldIndices[1] = new Point(3, 5);
		fieldNames[2] = Calendar.YEAR;
		fieldIndices[2] = new Point(6, 10);
	} else { /* SWT.TIME */
		fieldNames[0] = Calendar.HOUR;
		fieldIndices[0] = new Point(0, 2);
		fieldNames[1] = Calendar.MINUTE;
		fieldIndices[1] = new Point(3, 5);
		fieldNames[2] = Calendar.SECOND;
		fieldIndices[2] = new Point(6, 8);
		fieldNames[3] = Calendar.AM_PM;
		fieldIndices[3] = new Point(9, 11);
	}
	// TODO: if the format string is bogus, ignore? or throw illegal parameter?
	format = string;
}

public void setHour (int hour) {
	checkWidget ();
	calendar.set(Calendar.HOUR, hour);
	redraw();
}

public void setLayout(Layout layout) {
	checkWidget();
	// TODO: Get the comment that says that it does not make sense to set a layout on this
}

public void setMinute (int minute) {
	checkWidget ();
	calendar.set(Calendar.MINUTE, minute);
	redraw();
}

public void setMonth(int month) {
	checkWidget();
	calendar.set(Calendar.MONTH, month - 1);
	redraw();
}

public void setSecond (int second) {
	checkWidget ();
	calendar.set(Calendar.SECOND, second);
	redraw();
}

public void setYear(int year) {
	checkWidget();
	calendar.set(Calendar.YEAR, year);
	redraw();
}
}