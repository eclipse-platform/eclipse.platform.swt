package org.eclipse.swt.widgets;


import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class DateTime extends Canvas {
	Color foreground, background;
	Calendar calendar;
	DateFormatSymbols formatSymbols;
	Button monthDown, monthUp, yearDown, yearUp;
	Text text;
	boolean ignore = false;
	
	static final int HOURS = 0, COLON1 = 2, MINUTES = 3, COLON2 = 5, SECONDS = 6, SP = 8, AM_PM = 9;
	static final int MONTH = 0, SL1 = 2, DAY = 3, SL2 = 5, YEAR = 6;
	static final int MARGIN_WIDTH = 2;
	static final int MARGIN_HEIGHT = 1;

public DateTime(Composite parent, int style) {
	super(parent, checkStyle (style) | SWT.NO_REDRAW_RESIZE | (((style & SWT.CALENDAR) != 0)? 0 : SWT.BORDER));
	calendar = Calendar.getInstance();
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			DateTime.this.handleEvent(e);
		}
	};
	if ((this.style & SWT.CALENDAR) != 0) {
		formatSymbols = new DateFormatSymbols();
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.MouseDown, listener);
		monthDown = new Button(this, SWT.ARROW | SWT.LEFT);
		monthUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		yearDown = new Button(this, SWT.ARROW | SWT.LEFT);
		yearUp = new Button(this, SWT.ARROW | SWT.RIGHT);
		monthDown.addListener(SWT.Selection, listener);
		monthUp.addListener(SWT.Selection, listener);
		yearDown.addListener(SWT.Selection, listener);
		yearUp.addListener(SWT.Selection, listener);
	} else {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		super.setLayout(layout); // TODO: use resize, not layout
		text = new Text(this, SWT.SINGLE);
		if ((this.style & SWT.TIME) != 0) {
			int h = calendar.get(Calendar.HOUR);
			int m = calendar.get(Calendar.MINUTE);
			int s = calendar.get(Calendar.SECOND);
			int a = calendar.get(Calendar.AM_PM);
			text.setText("" + (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s + " " + (a == Calendar.AM ? "AM" : "PM"));
		} else { /* SWT.DATE */
			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH);
			int d = calendar.get(Calendar.DATE);
			text.setText("" + (m < 10 ? "0" : "") + m + "/" + (d < 10 ? "0" : "") + d + "/" + y);
		}
		text.addListener(SWT.Verify, listener);
	}
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if ((style & SWT.CALENDAR) != 0) {
		Point cellSize = getCellSize(null);
		Point buttonSize = monthDown.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int width = wHint == SWT.DEFAULT ? cellSize.x * 7 : wHint;
		int height = hHint == SWT.DEFAULT ? cellSize.y * 7 + Math.max(cellSize.y, buttonSize.y) : hHint;
		Rectangle trim = computeTrim(0, 0, width, height);
		return new Point(trim.width, trim.height);
	}
	return super.computeSize (wHint, hHint, changed);
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
	return calendar.get(Calendar.MONTH);
}

public int getSecond () {
	checkWidget ();
	return calendar.get(Calendar.SECOND);
}

public int getYear() {
	checkWidget();
	return calendar.get(Calendar.YEAR);
}

void handleEvent(Event event) {
	switch(event.type) {
		case SWT.Paint:
			handlePaint(event);
			break;
		case SWT.Resize:
			handleResize(event);
			break;
		case SWT.MouseDown:
			handleMouseDown(event);
			break;
		case SWT.KeyDown:
			handleKeyDown(event);
			break;
		case SWT.Traverse:
			handleTraverse(event);
			break;
		case SWT.Selection:
			handleSelection(event);
			break;
		case SWT.Verify:
			handleVerify(event);
			break;
	}
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
	monthDown.pack();
	monthUp.pack();
	yearDown.pack();
	yearUp.pack();
	Point cellSize = getCellSize(null);
	Point size = monthDown.getSize();
	int height = Math.max(cellSize.y, size.y);
	int y = cellSize.y * 7 + (height - size.y) / 2;
	monthDown.setLocation(0, y);
	monthUp.setLocation(size.x, y);
	int x = cellSize.x * 7 - size.x;
	yearUp.setLocation(x, y);
	yearDown.setLocation(x - size.x, y);
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

void handleVerify(Event event) {
	if (ignore) return;
	event.doit = false;
	int start = event.start;
	String newText = "";
	int length = 0;
	boolean notify = false;
	int back = 0;
	if ((style & SWT.DATE) != 0) {
		final int MAX = 9;
		if (start > MAX) return;
		StringBuffer buffer = new StringBuffer(event.text);
		char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		int index = 0;
		for (int i = 0; i < chars.length; i++) {
			if (start + index == SL1 || start + index == SL2) {
				if (chars[i] != '/') {
					buffer.insert(index, '/');
				}
				index++;
				continue;
			}
			if (chars[i] < '0' || chars[i] > '9') return;
			if (start + index == MONTH && chars[i] > '1') return;
			if (start + index == DAY && chars[i] > '3') return;
			index++;
		}
		newText = buffer.toString();
		length = newText.length();
		StringBuffer date = new StringBuffer(text.getText());
		date.replace(start, start + length, newText);

		String yyyy = date.substring(YEAR, YEAR+4);
		int year = Integer.parseInt(yyyy);
		if (calendar.get(Calendar.YEAR) != year) {
			calendar.set(Calendar.YEAR, year);
			notify = true;
		}
		String mm = date.substring(MONTH, MONTH+2);
		int month = Integer.parseInt(mm) - 1;
		int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
		if (month < 0) return;
		if (month > maxMonth) {
			if (start + length - 1 == MONTH) {
				newText += "0";
				length = newText.length();
				back = 1;
			} else return;
		}
		if (calendar.get(Calendar.MONTH) != month) {
			calendar.set(Calendar.MONTH, month);
			notify = true;
		}
		String dd = date.substring(DAY, DAY+2);
		int day = Integer.parseInt(dd);
		int maxDay = calendar.getActualMaximum(Calendar.DATE);
		if (day < 1) return;
		if (day > maxDay)  {
			if (start + length - 1 == DAY) {
				newText += "0";
				length = newText.length();
				back = 1;
			} else return;
		}
		if (calendar.get(Calendar.DATE) != day) {
			calendar.set(Calendar.DATE, day);
			notify = true;
		}
	} else { /* SWT.TIME */
		final int MAX = 10;
		if (start > MAX) return;
		StringBuffer buffer = new StringBuffer(event.text);
		char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		int index = 0;
		for (int i = 0; i < chars.length; i++) {
			if (start + index == COLON1 || start + index == COLON2) {
				if (chars[i] == ':') {
					index++;
					continue;
				}
				buffer.insert(index++, ':');
			}
			if (start + index == SP) {
				if (chars[i] == ' ') {
					index++;
					continue;
				}
				buffer.insert(index++, ' ');
			}
			char c = Character.toUpperCase(chars[i]);
			if (start + index == AM_PM) {
				if (c == 'A' || c == 'P') {
					buffer.setCharAt(index++, c);
					continue;
				}
			}
			if (start + index == AM_PM+1) {
				if (c == 'M') {
					buffer.setCharAt(index++, c);
					continue;
				}
			}
			if (chars[i] < '0' || chars[i] > '9') return;
			if (start + index == HOURS && chars[i] > '1') return;
			if (start + index == MINUTES && chars[i] > '5') return;
			if (start + index == SECONDS && chars[i] > '5') return;
			index++;
		}
		newText = buffer.toString();
		length = newText.length();
		StringBuffer time = new StringBuffer(text.getText());
		time.replace(start, start + length, newText);

		String hh = time.substring(HOURS, HOURS+2);
		int hour = Integer.parseInt(hh);
		int maxHour = calendar.getActualMaximum(Calendar.HOUR);
		if (0 > hour || hour > maxHour) return;
		if (calendar.get(Calendar.HOUR) != hour) {
			calendar.set(Calendar.HOUR, hour);
			notify = true;
		}
		String mm = time.substring(MINUTES, MINUTES+2);
		int minute = Integer.parseInt(mm);
		int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
		if (0 > minute || minute > maxMinute) return;
		if (calendar.get(Calendar.MINUTE) != minute) {
			calendar.set(Calendar.MINUTE, minute);
			notify = true;
		}
		String ss = time.substring(SECONDS, SECONDS+2);
		int second = Integer.parseInt(ss);
		int maxSec = calendar.getActualMaximum(Calendar.SECOND);
		if (0 > second || second > maxSec) return;
		if (calendar.get(Calendar.SECOND) != second) {
			calendar.set(Calendar.SECOND, second);
			notify = true;
		}
		String ap = time.substring(AM_PM, AM_PM+2);
		if (ap.equals("AM")) {
			if (calendar.get(Calendar.AM_PM) != Calendar.AM) {
				calendar.set(Calendar.AM_PM, Calendar.AM);
				notify = true;
			}
		} else if (ap.equals("PM")) {
			if (calendar.get(Calendar.AM_PM) != Calendar.PM) {
				calendar.set(Calendar.AM_PM, Calendar.PM);
				notify = true;
			}
		} else return;
	}
	text.setSelection(start, start + length);
	ignore = true;
	text.insert(newText);
	ignore = false;
	if (back > 0) text.setSelection(start + length - back);
	if (notify) notifyListeners(SWT.Selection, new Event());
}
	
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection,listener);	
}

void redraw(int cell, Point cellSize) {
	Point location = getCellLocation(cell, cellSize);
	redraw(location.x, location.y, cellSize.x, cellSize.y, false);	
}

public void setBackground(Color color) {
	checkWidget();
	super.setBackground(color);
	background = color;
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
	redraw();
}

public void setForeground(Color color) {
	checkWidget();
	super.setForeground(color);
	foreground = color;
}

public void setHour (int hour) {
	checkWidget ();
	calendar.set(Calendar.HOUR, hour);
	redraw();
}

public void setLayout(Layout layout) {
	checkWidget();
}

public void setMinute (int minute) {
	checkWidget ();
	calendar.set(Calendar.MINUTE, minute);
	redraw();
}

public void setMonth(int month) {
	checkWidget();
	calendar.set(Calendar.MONTH, month);
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