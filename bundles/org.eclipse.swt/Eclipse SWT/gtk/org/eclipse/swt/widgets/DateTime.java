/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Red Hat         - GtkSpinButton rewrite. 2014.10.09
 *     Lablicate GmbH  - add locale support/improve editing support for date/time styles. 2017.02.08
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.text.*;
import java.text.AttributedCharacterIterator.*;
import java.text.DateFormat.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/*
 * Developer note: Unit tests for this class can be found under:
 * org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_DateTime
 */

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
 * <dd>DATE, TIME, CALENDAR, SHORT, MEDIUM, LONG, DROP_DOWN, CALENDAR_WEEKNUMBERS</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DATE, TIME, or CALENDAR may be specified,
 * and only one of the styles SHORT, MEDIUM, or LONG may be specified.
 * The DROP_DOWN style is only valid with the DATE style.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#datetime">DateTime snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.3
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DateTime extends Composite {
	int day, month, year, hours, minutes, seconds;

	/**
	 * Major handles of this class.
	 * Note, these can vary or all equal each other depending on Date/Time/Calendar/Drop_down
	 * configuration used. See createHandle () */
	long textEntryHandle, spinButtonHandle,
	containerHandle,
	calendarHandle;

	/**
	 * GTK4 field used to hold the main GtkEditable widget.
	 * Primarily used for styles SWT.DROP_DOWN & SWT.TIME
	 */
	long editableHandle;

	/* Emulated DATE and TIME fields */
	Calendar calendar;
	Button down;
	FieldPosition currentField;
	StringBuilder typeBuffer = new StringBuilder();
	int typeBufferPos = -1;
	boolean firstTime = true;
	private DateFormat dateFormat;
	/* DROP_DOWN calendar fields for DATE */
	Color fg, bg;
	boolean hasFocus;
	int savedYear, savedMonth, savedDay;
	Shell popupShell;
	DateTime popupCalendar;
	Listener popupListener, popupFilter;

	Point prefferedSize;
	Locale locale;

	/** Used when SWT.DROP_DOWN is set */
	Listener mouseEventListener;

	/*
	 * Used for easier access to format pattern of DATE and TIME.
	 * See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
	 */
	static final String DEFAULT_SHORT_DATE_FORMAT = "dd/MM/yy";
	static final String DEFAULT_MEDIUM_DATE_FORMAT = "d-MMM-yyyy";
	static final String DEFAULT_LONG_DATE_FORMAT = "MMMM d, yyyy";
	static final String DEFAULT_SHORT_TIME_FORMAT = "h:mm a";
	static final String DEFAULT_MEDIUM_TIME_FORMAT = "h:mm:ss a";
	static final String DEFAULT_LONG_TIME_FORMAT = "h:mm:ss z a";
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
	static final int SPACE_FOR_CURSOR = 1;

	private int mdYear;

	private int mdMonth;

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
 * @see SWT#CALENDAR_WEEKNUMBERS
 * @see SWT#SHORT
 * @see SWT#MEDIUM
 * @see SWT#LONG
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public DateTime (Composite parent, int style) {
	super (parent, checkStyle (style));
	if (isDate () || isTime ()) {
		createText ();
	}

	if (isCalendar ()) {
		GTK.gtk_calendar_mark_day (calendarHandle, Calendar.getInstance ().get (Calendar.DAY_OF_MONTH));
	}

	if (isDateWithDropDownButton ()) {
		createDropDownButton ();
		createPopupShell (-1, -1, -1);
		addListener (SWT.Resize, event -> setDropDownButtonSize ());
	}
	initAccessible ();

	if (isDateWithDropDownButton ()) {
		//Date w/ drop down button is in containers.
		//first time round we set the bounds manually for correct Right_to_left behaviour
		Point size = computeSizeInPixels (SWT.DEFAULT, SWT.DEFAULT);
		setBoundsInPixels (0, 0, size.x, size.y);
	}
}

void createText() {
	String property = System.getProperty("swt.datetime.locale");
	if (property == null || property.isEmpty()) {
		locale = Locale.getDefault();
	} else {
		locale = Locale.forLanguageTag(property);
	}
	dateFormat = getFormat(locale, style);
	dateFormat.setLenient(false);
	calendar = Calendar.getInstance(locale);
	updateControl();
	selectField(updateField(currentField));
}

DateFormat getFormat(Locale locale, int style) {
	int dfStyle;
	if ((style & SWT.LONG) != 0) {
		dfStyle = DateFormat.LONG;
	} else if ((style & SWT.SHORT) != 0) {
		dfStyle = DateFormat.SHORT;
	} else {
		dfStyle = DateFormat.MEDIUM;
	}
	if (isDate()) {
		return DateFormat.getDateInstance(dfStyle, locale);
	} else if (isTime()) {
		return DateFormat.getTimeInstance(dfStyle, locale);
	} else {
		throw new IllegalStateException("can only be called for date or time widgets!");
	}
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);

	style = checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
	if ((style & SWT.DATE) == 0) style &=~ SWT.DROP_DOWN;
	return checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the control's value.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Compute the native text entry size when the formatted text inside the entry
 * is at the longest length possible. i.e. Assume DATE/HOUR field to be double digit,
 * MONTH field for SWT.DATE | SWT.LONG is the longest text.
 *
 * @return text entry size to hold the longest possible formatted text.
 */
Point computeMaxTextSize (int wHint, int hHint, boolean changed) {
	String currentText = getFormattedString();
	String formatPattern = getComputeSizeString(style);

	switch (formatPattern) {
		case DEFAULT_MEDIUM_DATE_FORMAT:
			// Make the DATE field a double digit
			String longDateText = currentText.replaceFirst("\\d{1,2}", "00");
			setText(longDateText);
			break;
		case DEFAULT_LONG_DATE_FORMAT:
			// Make the MONTH field the longest length possible, the DATE field a double digit.
			Set<String> months = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, locale).keySet();
			String longestMonth = Collections.max(months, (s1, s2) -> s1.length() - s2.length()); // Probably September
			String doubleDigitDate = currentText.replaceFirst("\\d{1,2}", "00");
			String longText = doubleDigitDate.replaceFirst("[^\\s]+", longestMonth);
			setText(longText);
			break;
		case DEFAULT_SHORT_TIME_FORMAT:
		case DEFAULT_MEDIUM_TIME_FORMAT:
		case DEFAULT_LONG_TIME_FORMAT:
			// Make the HOUR field a double digit
			String longTimeText = currentText.replaceFirst("\\d{1,2}", "00");
			setText(longTimeText);
			break;
		default:
			// Fixed length for DEFAULT_SHORT_DATE_FORMAT, no need to adjust text length.
	}

	Point textSize = computeNativeSize (GTK.GTK4 ? handle : textEntryHandle, wHint, hHint, changed);
	// Change the text back to match the current calendar
	updateControl();
	return textSize;
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();

	int width = 0, height = 0;
	//For Date and Time, we cache the preffered size as there is no need to recompute it.
	if (!changed && (isDate () || isTime ()) && prefferedSize != null) {
		width = (wHint != SWT.DEFAULT) ? wHint : prefferedSize.x;
		height= (hHint != SWT.DEFAULT) ? hHint : prefferedSize.y;
		return new Point (width,height);
	}

	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if (isCalendar ()) {
			Point size = computeNativeSize (containerHandle, wHint, hHint, changed);
			width = size.x;
			height = size.y;
		} else {
			/*
			 * Bug 538612: Computing the native size for textEntry when the current text
			 * is not the longest length possible causes sizing issues when the entry text
			 * is changed. Fix is to always allocate enough size to hold the longest possible
			 * formatted text.
			 */
			Point textSize = computeMaxTextSize (wHint, hHint, changed);
			Rectangle trim = computeTrimInPixels (0,0, textSize.x,textSize.y);
			if (isDateWithDropDownButton ()){
				Point buttonSize = down.computeSizeInPixels (SWT.DEFAULT, SWT.DEFAULT, changed);
				width = trim.width + buttonSize.x;
				height = Math.max (trim.height, buttonSize.y);
			} else if (isDate () || isTime ()) {
				width = trim.width;
				height = trim.height;
			}
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int borderWidth = getBorderWidthInPixels ();

	if (prefferedSize == null && isDateWithDropDownButton ()) {
		prefferedSize = new Point (width + 2*borderWidth, height+ 2*borderWidth);
		return prefferedSize;
	} else {
		return new Point (width + 2*borderWidth, height+ 2*borderWidth);
	}
}

@Override
Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	if (isCalendar ()) {
		return super.computeTrimInPixels (x, y, width, height);
	}

	checkWidget ();
	Rectangle trim = super.computeTrimInPixels (x, y, width, height);
	int xborder = 0, yborder = 0;
		GtkBorder tmp = new GtkBorder ();
		long context = GTK.gtk_widget_get_style_context (GTK.GTK4 ? editableHandle : textEntryHandle);
		int state_flag = GTK.gtk_widget_get_state_flags(GTK.GTK4 ? editableHandle : textEntryHandle);
		gtk_style_context_get_padding(context, state_flag, tmp);
		trim.x -= tmp.left;
		trim.y -= tmp.top;
		trim.width += tmp.left + tmp.right;
		trim.height += tmp.top + tmp.bottom;
		if ((style & SWT.BORDER) != 0) {
			int state = GTK.gtk_widget_get_state_flags(GTK.GTK4 ? editableHandle : textEntryHandle);
			gtk_style_context_get_border(context, state, tmp);
			trim.x -= tmp.left;
			trim.y -= tmp.top;
			trim.width += tmp.left + tmp.right;
			trim.height += tmp.top + tmp.bottom;
		}
		trim.x -= xborder;
		trim.y -= yborder;
		trim.width += 2 * xborder;
		trim.height += 2 * yborder;
		trim.width += SPACE_FOR_CURSOR;
		return new Rectangle (trim.x, trim.y, trim.width, trim.height);
}


@Override
void createHandle(int index) {
	if (isCalendar()) {
		state |= HANDLE;

		createSWTFixedHandle();
		createHandleForCalendar ();
	} else {
		createSWTFixedHandle();
		if (isDateWithDropDownButton()) {
			createHandleForDateWithDropDown();
		} else {
			createHandleForDateTime();
		}

		GTK.gtk_editable_set_editable(textEntryHandle, (style & SWT.READ_ONLY) == 0);
	}
}

private void createSWTFixedHandle() {
	fixedHandle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
	if (fixedHandle == 0) error(SWT.ERROR_NO_HANDLES);

	if (!GTK.GTK4) GTK3.gtk_widget_set_has_window(fixedHandle, true);
}

private void createHandleForCalendar() {
	handle = GTK.gtk_calendar_new();
	if (handle == 0) error(SWT.ERROR_NO_HANDLES);

	//Calendar becomes container in this case.
	calendarHandle = handle;
	containerHandle = calendarHandle;

	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, handle);

		GTK4.gtk_calendar_set_show_heading(handle, true);
		GTK4.gtk_calendar_set_show_day_names(handle, true);
		GTK4.gtk_calendar_set_show_week_numbers(handle, showWeekNumbers());
	} else {
		GTK3.gtk_container_add(fixedHandle, handle);

		int flags = GTK.GTK_CALENDAR_SHOW_HEADING | GTK.GTK_CALENDAR_SHOW_DAY_NAMES;
		if (showWeekNumbers()) {
			flags |= GTK.GTK_CALENDAR_SHOW_WEEK_NUMBERS;
		}
		GTK3.gtk_calendar_set_display_options(handle, flags);
		GTK.gtk_widget_show(handle);
	}
}

private void createHandleForDateWithDropDown () {
	handle = gtk_box_new(GTK.GTK_ORIENTATION_HORIZONTAL, false, 0);
	if (handle == 0) error(SWT.ERROR_NO_HANDLES);
	containerHandle = handle;

	if (GTK.GTK4) {
		editableHandle = GTK.gtk_entry_new();
		if (editableHandle == 0) error(SWT.ERROR_NO_HANDLES);

		textEntryHandle = GTK4.gtk_editable_get_delegate(editableHandle);

		OS.swt_fixed_add(fixedHandle, handle);
		GTK4.gtk_box_append(handle, editableHandle);
	} else {
		textEntryHandle = GTK.gtk_entry_new();
		if (textEntryHandle == 0) error(SWT.ERROR_NO_HANDLES);

		GTK3.gtk_container_add(fixedHandle, handle);
		GTK3.gtk_container_add(handle, textEntryHandle);
		GTK.gtk_widget_show(containerHandle);
		GTK.gtk_widget_show(textEntryHandle);


		// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
		// reset to default font to get the usual behavior
		setFontDescription(defaultFont().handle);
	}
}

private void createHandleForDateTime() {
	long adjustment = GTK.gtk_adjustment_new(0, -9999, 9999, 1, 0, 0);
	if (GTK.GTK4) {
		handle = GTK.gtk_spin_button_new(adjustment, 1, 0);
		editableHandle = handle;
		textEntryHandle = GTK4.gtk_editable_get_delegate(editableHandle);
		containerHandle = textEntryHandle;
	} else {
		handle = GTK.gtk_spin_button_new(adjustment, 1, 0);
		textEntryHandle = handle;
		containerHandle = textEntryHandle;
	}
	if (textEntryHandle == 0) error(SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, handle);
	} else {
		GTK3.gtk_container_add(fixedHandle, handle);
	}

	GTK.gtk_spin_button_set_numeric(handle, false);
	GTK.gtk_spin_button_set_wrap(handle, (style & SWT.WRAP) != 0);
}

void createDropDownButton () {
	down = new Button (this, SWT.ARROW  | SWT.DOWN);
	GTK.gtk_widget_set_can_focus (down.handle, false);
	down.addListener (SWT.Selection, event -> {
		setFocus ();
		dropDownCalendar (!isDropped ());
	});

	popupListener = event -> {
		if (event.widget == popupShell) {
			popupShellEvent (event);
			return;
		}
		if (event.widget == popupCalendar) {
			popupCalendarEvent (event);
			return;
		}
		if (event.widget == DateTime.this) {
			onDispose (event);
			return;
		}
		if (event.widget == getShell ()) {
			getDisplay ().asyncExec (() -> {
				if (isDisposed ()) return;
				handleFocus (SWT.FocusOut);
			});
		}
	};
	popupFilter = event -> {
		Shell shell = ((Control)event.widget).getShell ();
		if (shell == DateTime.this.getShell ()) {
			handleFocus (SWT.FocusOut);
		}
	};
}

void createPopupShell (int year, int month, int day) {
	popupShell = new Shell (getShell (), SWT.NO_TRIM | SWT.ON_TOP);
	int popupStyle = SWT.CALENDAR;
	if (showWeekNumbers()) {
		popupStyle |= SWT.CALENDAR_WEEKNUMBERS;
	}
	popupCalendar = new DateTime (popupShell, popupStyle);
	if (font != null) popupCalendar.setFont (font);
	if (fg != null) popupCalendar.setForeground (fg);
	if (bg != null) popupCalendar.setBackground (bg);

	mouseEventListener = event -> {
		if (event.widget instanceof Control) {
			Control c = (Control)event.widget;
			if (c != down && c.getShell () != popupShell)
				dropDownCalendar (false);
		}
	};

	int [] listeners = {SWT.Close, SWT.MouseUp};
	for (int i=0; i < listeners.length; i++) {
		popupShell.addListener (listeners [i], popupListener);
	}
	listeners = new int [] {SWT.MouseDown, SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.FocusOut, SWT.Dispose};
	for (int i=0; i < listeners.length; i++) {
		popupCalendar.addListener (listeners [i], popupListener);
	}
	addListener (SWT.Dispose, popupListener);
	if (year != -1) popupCalendar.setDate (year, month, day);
}

@Override
void setFontDescription (long font) {
	if (isDateWithDropDownButton ()) {
		prefferedSize = null; //flush cache for computeSize as font can cause size to change.
		setFontDescription (textEntryHandle, font);
	}
	super.setFontDescription (font);
}

@Override
boolean checkSubwindow () {
	return false;
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	if (isCalendar ()) {
		getDate ();
	}
}

void onDispose (Event event) {
	if (popupShell != null && !popupShell.isDisposed ()) {
		popupCalendar.removeListener (SWT.Dispose, popupListener);
		popupShell.dispose ();
	}
	Shell shell = getShell ();
	shell.removeListener (SWT.Deactivate, popupListener);
	Display display = getDisplay ();
	display.removeFilter (SWT.FocusIn, popupFilter);
	popupShell = null;
	popupCalendar = null;
	down = null;
}

/**
 * Called when pressing the SWT.DROP_DOWN button on a Date Field
 * @param drop true if the calendar is suppose to drop down.
 */
void dropDownCalendar (boolean drop) {
	if (drop == isDropped ()) return;

	if (!drop) {
		hideDropDownCalendar ();
		return;
	}

	setCurrentDate ();

	if (getShell () != popupShell.getParent ()) {
		recreateCalendar ();
	}

	//This is the x/y/width/height of the container of DateTime
	Point containerBounds = getSizeInPixels ();
	Point calendarSize = popupCalendar.computeSizeInPixels (SWT.DEFAULT, SWT.DEFAULT, false);

	//Set the inner calendar pos/size. (not the popup shell pos/size)
	popupCalendar.setBoundsInPixels (1, 1, Math.max (containerBounds.x - 2, calendarSize.x), calendarSize.y);

	//Set Date & focus current day
	popupCalendar.setDate (savedYear, savedMonth, savedDay);
	focusDayOnPopupCalendar ();

	Display display = getDisplay ();

	//To display popup calendar, we need to know where the parent is relative to the whole screen.
	Rectangle coordsRelativeToScreen = display.mapInPixels (getParent (), null, getBoundsInPixels ());
	Rectangle displayRect = DPIUtil.autoScaleUp(getMonitor ().getClientArea ());

	showPopupShell (containerBounds, calendarSize, coordsRelativeToScreen, displayRect);

	display.addFilter (SWT.MouseDown, mouseEventListener);
}

private void showPopupShell (Point containerBounds, Point calendarSize, Rectangle coordsRelativeToScreen,
		Rectangle displayRect) {
	int width = Math.max (containerBounds.x, calendarSize.x + 2);
	int height = calendarSize.y + 2;
	int y = calculateCalendarYpos (containerBounds, coordsRelativeToScreen, height, displayRect);
	int x = calculateCalendarXpos (calendarSize, coordsRelativeToScreen, displayRect, width);

	popupShell.setBoundsInPixels (x, y, width, height);
	popupShell.setVisible (true);
	if (isFocusControl ()) {
		popupCalendar.setFocus ();
	}
}

private int calculateCalendarYpos (Point containerBounds, Rectangle coordsRelativeToScreen, int height,
		Rectangle displayRect) {
	int dateEntryHeight = computeNativeSize (containerHandle, SWT.DEFAULT, SWT.DEFAULT, false).y;
	int y = coordsRelativeToScreen.y + containerBounds.y/2 + dateEntryHeight/2;

	//Put Calendar above control if it would be cut off at the bottom.
	if (y + height > displayRect.y + displayRect.height) {
		y -= (height + dateEntryHeight);
	}
	return y;
}

private int calculateCalendarXpos (Point calendarSize, Rectangle coordsRelativeToScreen, Rectangle displayRect,
		int width) {
	Integer x;
	x = coordsRelativeToScreen.x;
	//Move calendar to the right if it would be cut off.
	if (x + width > displayRect.x + displayRect.width) {
		x = displayRect.x + displayRect.width - calendarSize.x;
	}
	return x;
}


private void focusDayOnPopupCalendar () {
	int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
	int currentMonth = Calendar.getInstance ().get (Calendar.MONTH);

	if (savedYear == currentYear && savedMonth == currentMonth) {
		int currentDay = Calendar.getInstance ().get (Calendar.DAY_OF_MONTH);
		GTK.gtk_calendar_mark_day (popupCalendar.handle, currentDay);
	}
}

private void setCurrentDate () {
	savedYear = getYear ();
	savedMonth = getMonth ();
	savedDay = getDay ();
}

private void recreateCalendar () {
	int year = popupCalendar.getYear ();
	int month = popupCalendar.getMonth ();
	int day = popupCalendar.getDay ();
	popupCalendar.removeListener (SWT.Dispose, popupListener);
	popupShell.dispose ();
	popupShell = null;
	popupCalendar = null;
	createPopupShell (year, month, day);
}

private void hideDropDownCalendar () {
	popupShell.setVisible (false);
	GTK.gtk_calendar_clear_marks (popupCalendar.handle);
	display.removeFilter (SWT.MouseDown, mouseEventListener);
	return;
}

String getComputeSizeString (int style) {
	if ((style & SWT.DATE) != 0) {
		return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_DATE_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_DATE_FORMAT : DEFAULT_MEDIUM_DATE_FORMAT;
	}
	// SWT.TIME
	return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_TIME_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_TIME_FORMAT : DEFAULT_MEDIUM_TIME_FORMAT;
}

String getFormattedString() {
	return dateFormat.format(calendar.getTime());
}

void getDate() {
	int[] y = new int[1], m = new int[1], d = new int[1];

	if (GTK.GTK4) {
		long dateTime = GTK4.gtk_calendar_get_date(calendarHandle);
		OS.g_date_time_get_ymd(dateTime, y, m, d);

		year = y[0];
		month = m[0] - 1;
		day = d[0];
	} else {
		GTK3.gtk_calendar_get_date(calendarHandle, y, m, d);

		year = y[0];
		month = m[0];
		day = d[0];
	}
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
public int getDay () {
	checkWidget ();
	if (isCalendar ()) {
		getDate ();
		return day;
	} else {
		return calendar.get (Calendar.DAY_OF_MONTH);
	}
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
	if (isCalendar ()) {
		return hours;
	} else {
		return calendar.get (Calendar.HOUR_OF_DAY);
	}
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
	if (isCalendar ()) {
		return minutes;
	} else {
		return calendar.get (Calendar.MINUTE);
	}
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
public int getMonth () {
	checkWidget ();
	if (isCalendar ()) {
		getDate ();
		return month;
	} else {
		return calendar.get (Calendar.MONTH);
	}
}

@Override
String getNameText () {
	if(calendar == null) {
		return "";
	}
	if (isTime ()) {
		return getHours () + ":" + getMinutes () + ":" + getSeconds ();
	} else {
		return (getMonth () + 1) + "/" + getDay () + "/" + getYear ();
	}
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
	if (isCalendar ()) {
		return seconds;
	} else {
		return calendar.get (Calendar.SECOND);
	}
}

/*
 * Returns a textual representation of the receiver,
 * intended for speaking the text aloud.
 */
String getSpokenText() {
	if (isTime()) {
		return DateFormat.getTimeInstance(DateFormat.FULL).format(calendar.getTime());
	} else if (isDate()) {
		return DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
	} else {
		Calendar cal = Calendar.getInstance();
		getDate();
		cal.set(year, month, day);
		return DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
	}
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
public int getYear () {
	checkWidget ();
	if (isCalendar ()) {
		getDate ();
		return year;
	} else {
		return calendar.get (Calendar.YEAR);
	}
}

@Override
long gtk_day_selected (long widget) {
	sendSelectionEvent ();
	return 0;
}

@Override
long gtk_day_selected_double_click (long widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

@Override
long gtk_month_changed (long widget) {
	sendSelectionEvent ();
	return 0;
}

@Override
long eventHandle () {
	return dateTimeHandle ();
}

@Override
long focusHandle () {
	return dateTimeHandle ();
}

@Override
long fontHandle () {
	return dateTimeHandle ();
}

private long dateTimeHandle () {
	if (isCalendar () && calendarHandle != 0) {
		return calendarHandle;
	} else if ((isDate () || isTime ())) {
		if (textEntryHandle != 0) return textEntryHandle;
		return super.focusHandle ();
	} else {
		return super.focusHandle ();
	}
}

@Override
void hookEvents () {
	super.hookEvents ();
	if (isCalendar ()) {
		hookEventsForCalendar ();
	} else {
		if ((style & SWT.DROP_DOWN) == 0 ) {
			hookEventsForDateTimeSpinner ();
		}

		if (!GTK.GTK4) {
			int eventMask =	GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK;
			GTK3.gtk_widget_add_events(textEntryHandle, eventMask);

			if (OS.G_OBJECT_TYPE (textEntryHandle) == GTK3.GTK_TYPE_MENU ()) {
				hookEventsForMenu ();
			}
		}
	}
}


final private void hookEventsForCalendar () {
	OS.g_signal_connect_closure(calendarHandle, OS.day_selected, display.getClosure(DAY_SELECTED), false);

	if (GTK.GTK4) {
		OS.g_signal_connect_closure(calendarHandle, OS.next_month, display.getClosure(MONTH_CHANGED), false);
		OS.g_signal_connect_closure(calendarHandle, OS.next_year, display.getClosure(MONTH_CHANGED), false);
		OS.g_signal_connect_closure(calendarHandle, OS.prev_month, display.getClosure(MONTH_CHANGED), false);
		OS.g_signal_connect_closure(calendarHandle, OS.prev_year, display.getClosure(MONTH_CHANGED), false);
	} else {
		OS.g_signal_connect_closure(calendarHandle, OS.day_selected_double_click, display.getClosure(DAY_SELECTED_DOUBLE_CLICK), false);
		OS.g_signal_connect_closure(calendarHandle, OS.month_changed, display.getClosure(MONTH_CHANGED), false);
	}
}

final private void hookEventsForDateTimeSpinner () {
	OS.g_signal_connect_closure (handle, OS.output, display.getClosure (OUTPUT), true);
	if (GTK.GTK4) {
		//TODO: GTK4 focus-in (focus event)?
	} else {
		OS.g_signal_connect_closure (textEntryHandle, OS.focus_in_event, display.getClosure (FOCUS_IN_EVENT), true);
	}
}

final private void hookEventsForMenu () {
	OS.g_signal_connect_closure (down.handle, OS.selection_done, display.getClosure (SELECTION_DONE), true);
}

void incrementField(int amount) {
	if (currentField != null) {
		int field = getCalendarField(currentField);
		if (field == Calendar.HOUR && hasAmPm()) {
			int max = calendar.getMaximum(Calendar.HOUR);
			int min = calendar.getMinimum(Calendar.HOUR);
			int value = calendar.get(Calendar.HOUR);
			if ((value == max && amount == 1) || (value == min && amount == -1)) {
				calendar.roll(Calendar.AM_PM, amount);
			}
		}
		if (field > -1) {
			calendar.roll(field, amount);
			updateControl();
			selectField(updateField(currentField));
		}
	}
}

private boolean hasAmPm() {
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	while (iterator.current() != CharacterIterator.DONE) {
		for (Attribute attribute : iterator.getAttributes().keySet()) {
			if (Field.AM_PM.equals(attribute)) {
				return true;
			}
		}
		iterator.setIndex(iterator.getRunLimit());
	}
	return false;
}

boolean isDropped () {
	return popupShell.getVisible ();
}

private boolean isCalendar () {
	return ((style & SWT.CALENDAR) != 0);
}

private boolean isDateWithDropDownButton () {
	return ((style & SWT.DROP_DOWN) != 0 && (style & SWT.DATE) != 0);
}

private boolean isDate () {
	return ((style & SWT.DATE) != 0);
}

private boolean isTime () {
	return ((style & SWT.TIME) != 0);
}

private boolean isReadOnly () {
	return ((style & SWT.READ_ONLY) != 0);
}

private boolean showWeekNumbers() {
	return ((style & SWT.CALENDAR_WEEKNUMBERS) != 0);
}

void initAccessible () {
	Accessible accessible = getAccessible ();
	accessible.addAccessibleListener (new AccessibleAdapter () {
		@Override
		public void getName (AccessibleEvent e) {
			e.result = getSpokenText ();
		}

		@Override
		public void getHelp (AccessibleEvent e) {
			e.result = getToolTipText ();
		}
	});

	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override
		public void getChildAtPoint (AccessibleControlEvent e) {
			e.childID = ACC.CHILDID_SELF;
		}

		@Override
		public void getLocation (AccessibleControlEvent e) {
			Rectangle rect = display.map (getParent (), null, getBounds ());
			e.x = rect.x;
			e.y = rect.y;
			e.width = rect.width;
			e.height = rect.height;
		}

		@Override
		public void getChildCount (AccessibleControlEvent e) {
			e.detail = 0;
		}

		@Override
		public void getRole (AccessibleControlEvent e) {
			e.detail = (isCalendar ()) ? ACC.ROLE_LABEL : ACC.ROLE_TEXT;
		}

		@Override
		public void getState (AccessibleControlEvent e) {
			e.detail = ACC.STATE_FOCUSABLE;
			if (hasFocus ()) e.detail |= ACC.STATE_FOCUSED;
		}

		@Override
		public void getSelection (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}

		@Override
		public void getFocus (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}
	});
}

boolean isValidTime (int fieldName, int value) {
	Calendar validCalendar;
	if (isCalendar ()) {
		validCalendar = Calendar.getInstance ();
	} else {
		validCalendar = calendar;
	}
	int min = validCalendar.getActualMinimum (fieldName);
	int max = validCalendar.getActualMaximum (fieldName);
	return value >= min && value <= max;
}

boolean isValidDate (int year, int month, int day) {
	if (year < MIN_YEAR || year > MAX_YEAR) return false;
	Calendar valid = Calendar.getInstance ();
	valid.set (year, month, day);
	return valid.get (Calendar.YEAR) == year
		&& valid.get (Calendar.MONTH) == month
		&& valid.get (Calendar.DAY_OF_MONTH) == day;
}

void popupCalendarEvent (Event event) {
	switch (event.type) {
		case SWT.Dispose:
			if (popupShell != null && !popupShell.isDisposed () && !isDisposed () && getShell () != popupShell.getParent ()) {
				int year = popupCalendar.getYear ();
				int month = popupCalendar.getMonth ();
				int day = popupCalendar.getDay ();
				popupShell = null;
				popupCalendar = null;
				createPopupShell (year, month, day);
			}
			break;
		case SWT.FocusIn: {
			handleFocus (SWT.FocusIn);
			break;
		}
		case SWT.MouseDown: {
			if (event.button != 1) return;
			mdYear = getYear();
			mdMonth = getMonth();
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			/*
			* The drop-down should stay visible when
			* either the year or month is changed.
			*/
			if (mdYear == getYear() && mdMonth == getMonth()) {
				dropDownCalendar (false);
			}
			break;
		}
		case SWT.Selection: {
			int year = popupCalendar.getYear ();
			int month = popupCalendar.getMonth ();
			int day = popupCalendar.getDay ();
			setDate (year, month, day);
			Event e = new Event ();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners (SWT.Selection, e);
			event.doit = e.doit;
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_ARROW_PREVIOUS:
				case SWT.TRAVERSE_ARROW_NEXT:
					event.doit = false;
					break;
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
//					event.doit = text.traverse (event.detail);
					event.detail = SWT.TRAVERSE_NONE;
					if (event.doit) dropDownCalendar (false);
					return;
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					return;
			}
			Event e = new Event ();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners (SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyUp, e);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) {
				/* Escape key cancels popupCalendar and reverts date */
				popupCalendar.setDate (savedYear, savedMonth, savedDay);
				setDate (savedYear, savedMonth, savedDay);
				dropDownCalendar (false);
			}
			if (event.keyCode == SWT.CR || (event.stateMask & SWT.ALT) != 0 && (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				/* Return, Alt+Down, and Alt+Up cancel popupCalendar and select date. */
				dropDownCalendar (false);
			}
			if (event.keyCode == SWT.SPACE) {
				dropDownCalendar (false);
			}
			/* At this point the widget may have been disposed.
			 * If so, do not continue. */
			if (isDisposed ()) break;
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyDown, e);
			break;
		}
	}
}

void handleFocus (int type) {
	if (isDisposed ()) return;
	switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) return;
			selectAll ();
			hasFocus = true;
			Shell shell = getShell ();
			shell.removeListener (SWT.Deactivate, popupListener);
			shell.addListener (SWT.Deactivate, popupListener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, popupFilter);
			Event e = new Event ();
			notifyListeners (SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) return;
			Control focusControl = getDisplay ().getFocusControl ();
			if (focusControl == down || focusControl == popupCalendar ) return;
			hasFocus = false;
			Shell shell = getShell ();
			shell.removeListener (SWT.Deactivate, popupListener);
			Display display = getDisplay ();
			display.removeFilter (SWT.MouseDown, mouseEventListener);
			Event e = new Event ();
			notifyListeners (SWT.FocusOut, e);
			break;
		}
	}
}

void popupShellEvent (Event event) {
	switch (event.type) {
		case SWT.Close:
			event.doit = false;
			dropDownCalendar (false);
			break;
		case SWT.MouseUp:
			dropDownCalendar (false);
			break;
	}
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
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * selects the first occurrence of the given field
 */
void selectField(Field field) {
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	while (iterator.current() != CharacterIterator.DONE) {
		for (Attribute attribute : iterator.getAttributes().keySet()) {
			if (attribute.equals(field)) {
				selectField(getFieldPosition(field, iterator));
				return;
			}
		}
		iterator.setIndex(iterator.getRunLimit());
	}
}

/**
 * Selects the given field at the given start/end coordinates
 */
void selectField(FieldPosition fieldPosition) {
	boolean sameField = isSameField(fieldPosition, currentField);
	if (sameField) {
		if (typeBufferPos > -1) {
			typeBufferPos = 0;
		}
	} else {
		typeBufferPos = -1;
		commitData();
		fieldPosition = updateField(fieldPosition);
	}
	Point pt = getTextSelection();
	int start = fieldPosition.getBeginIndex();
	int end = fieldPosition.getEndIndex();
	if (sameField && start == pt.x && end == pt.y) {
		return;
	}
	currentField = fieldPosition;
	display.syncExec(() -> {
		if (textEntryHandle != 0) {
			String value = getText(getText(), start, end - 1);
			int s = value.lastIndexOf(' ');
			s = (s == -1) ? start : start + s + 1;
			setTextSelection(s, end);
		}
	});
	sendSelectionEvent(SWT.Selection);
}

void sendSelectionEvent () {
	int [] y = new int [1];
	int [] m = new int [1];
	int [] d = new int [1];

	if (GTK.GTK4) {
		long dateTime = GTK4.gtk_calendar_get_date(calendarHandle);
		OS.g_date_time_get_ymd(dateTime, y, m, d);
	} else {
		GTK3.gtk_calendar_get_date (calendarHandle, y, m, d);
	}

	if (d[0] != day ||
		m[0] != month ||
		y[0] != year) {
		year = y[0];
		month = m[0];
		day = d[0];
		/* Highlight the current (today) date */
		if (year == Calendar.getInstance ().get (Calendar.YEAR) && month == Calendar.getInstance ().get (Calendar.MONTH)) {
			GTK.gtk_calendar_mark_day (calendarHandle, Calendar.getInstance ().get (Calendar.DAY_OF_MONTH));
		} else {
			GTK.gtk_calendar_clear_marks (calendarHandle);
		}
		sendSelectionEvent (SWT.Selection);
	}
}

@Override
public void setBackground (Color color) {
	super.setBackground (color);
	bg = color;
	if (popupCalendar != null) popupCalendar.setBackground (color);
}

@Override
void setBackgroundGdkRGBA (GdkRGBA rgba) {
	super.setBackgroundGdkRGBA(rgba);
	if (calendarHandle != 0) {
		setBackgroundGdkRGBA (calendarHandle, rgba);
	}
	super.setBackgroundGdkRGBA(rgba);

}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	// We need to override here because DateTime widgets use "background" instead of
	// "background-color" as their CSS property.

	// Form background string
	String name = display.gtk_widget_class_get_css_name(handle);
	String css = name + " {background: " + display.gtk_rgba_to_css_string (rgba) + ";}\n" +
			name + ":selected" + " {background: " + display.gtk_rgba_to_css_string(display.COLOR_LIST_SELECTION_RGBA) + ";}";

	// Cache background
	cssBackground = css;

	// Apply background color and any cached foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css (context, finalCss);
}

@Override
public void setEnabled (boolean enabled){
	super.setEnabled (enabled);
	if (isDateWithDropDownButton ())
		down.setEnabled (enabled);
}

@Override
public void setFont (Font font) {
	super.setFont (font);
	this.font = font;
	if (popupCalendar != null) popupCalendar.setFont (font);
	redraw ();
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	setForegroundGdkRGBA (containerHandle, rgba);
}

@Override
public void setForeground (Color color) {
	super.setForeground (color);
	fg = color;
	if (popupCalendar != null) popupCalendar.setForeground (color);
}

void setFieldOfInternalDataStructure(FieldPosition field, int value) {
	int calendarField = getCalendarField(field);
	if (calendar.get(calendarField) == value)
		return;
	if (calendarField == Calendar.AM_PM && hasAmPm()) {
		calendar.roll(Calendar.HOUR_OF_DAY, 12);
	}
	calendar.set(calendarField, value);

	//When dealing with months with 31 days and have days set to 31, then if you change the month
	//to one that has 30 (or less) days, then in calendar only the day is changed but the month stays.
	//e.g 10.31.2014  -> decrement month, becomes:
	//    10.01.2014.
	//To get around this behaviour, we set the field again.
	if (calendar.get(calendarField) != value) {
		calendar.set(calendarField, value);
	}
	sendSelectionEvent (SWT.Selection);
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
	if (!isValidDate (year, month, day)) return;
	if (isCalendar ()) {
		this.year = year;
		this.month = month;
		this.day = day;

		if (GTK.GTK4) {
			long dateTime = OS.g_date_time_new_local(year, month + 1, day, 0, 0, 0);
			GTK4.gtk_calendar_select_day(calendarHandle, dateTime);
			OS.g_date_time_unref(dateTime);
		} else {
			GTK3.gtk_calendar_select_month (calendarHandle, month, year);
			GTK3.gtk_calendar_select_day (calendarHandle, day);
		}
	} else {
		calendar.set (year, month, day);
		updateControl ();
	}
}

/**
 * Sets the receiver's date, or day of the month, to the specified day.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * If the specified day is not valid for the receiver's month and year, then it is ignored.
 * </p>
 *
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setDay (int day) {
	checkWidget ();
	if (!isValidDate (getYear (), getMonth (), day)) return;
	if (isCalendar ()) {
		this.day = day;

		if (GTK.GTK4) {
			long dateTime = OS.g_date_time_new_local(this.year, this.month + 1, day, 0, 0, 0);
			GTK4.gtk_calendar_select_day(calendarHandle, dateTime);
		} else {
			GTK3.gtk_calendar_select_day (calendarHandle, day);
		}
	} else {
		calendar.set (Calendar.DAY_OF_MONTH, day);
		updateControl ();
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
	if (!isValidTime (Calendar.HOUR_OF_DAY, hours)) return;
	if (isCalendar ()) {
		this.hours = hours;
	} else {
		calendar.set (Calendar.HOUR_OF_DAY, hours);
		updateControl ();
	}
}

@Override
public void setMenu (Menu menu) {
	super.setMenu (menu);
	if (down != null) down.setMenu (menu);
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
	if (!isValidTime (Calendar.MINUTE, minutes)) return;
	if (isCalendar ()) {
		this.minutes = minutes;
	} else {
		calendar.set (Calendar.MINUTE, minutes);
		updateControl ();
	}
}

/**
 * Sets the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * If the specified month is not valid for the receiver's day and year, then it is ignored.
 * </p>
 *
 * @param month an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setMonth (int month) {
	checkWidget ();
	if (!isValidDate (getYear (), month, getDay ())) return;
	if (isCalendar ()) {
		this.month = month;

		if (GTK.GTK4) {
			long dateTime = OS.g_date_time_new_local(year, month + 1, day, 0, 0, 0);
			GTK4.gtk_calendar_select_day(calendarHandle, dateTime);
			OS.g_date_time_unref(dateTime);
		} else {
			GTK3.gtk_calendar_select_month(calendarHandle, month, year);
		}
	} else {
		calendar.set (Calendar.MONTH, month);
		updateControl ();
	}
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
	if (!isValidTime (Calendar.SECOND, seconds)) return;
	if (isCalendar ()) {
		this.seconds = seconds;
	} else {
		calendar.set (Calendar.SECOND, seconds);
		updateControl ();
	}
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
	if (!isValidTime (Calendar.HOUR_OF_DAY, hours)) return;
	if (!isValidTime (Calendar.MINUTE, minutes)) return;
	if (!isValidTime (Calendar.SECOND, seconds)) return;
	if (isCalendar ()) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	} else {
		calendar.set (Calendar.HOUR_OF_DAY, hours);
		calendar.set (Calendar.MINUTE, minutes);
		calendar.set (Calendar.SECOND, seconds);
		updateControl ();
	}
}

/**
 * Sets the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * If the specified year is not valid for the receiver's day and month, then it is ignored.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setYear (int year) {
	checkWidget ();
	if (!isValidDate (year, getMonth (), getDay ())) return;
	if (isCalendar ()) {
		this.year = year;

		if (GTK.GTK4) {
			long dateTime = OS.g_date_time_new_local(year, month + 1, day, 0, 0, 0);
			GTK4.gtk_calendar_select_day(calendarHandle, dateTime);
			OS.g_date_time_unref(dateTime);
		} else {
			GTK3.gtk_calendar_select_month(calendarHandle, month, year);
		}
	} else {
		calendar.set (Calendar.YEAR, year);
		updateControl ();
	}
}

@Override
void setBoundsInPixels (int x, int y, int width, int height) {

	//Date with Drop down is in container. Needs extra handling.
	if (isDateWithDropDownButton ()) {
		long sizingHandle = GTK.GTK4 ? editableHandle : textEntryHandle;
		GtkRequisition requisition = new GtkRequisition ();
		GTK.gtk_widget_get_preferred_size (sizingHandle, null, requisition);
		int oldHeight = requisition.height; //Entry should not expand vertically. It is single liner.

		int newWidth = width - (down.getSizeInPixels ().x + getGtkBorderPadding ().right);
		GTK.gtk_widget_set_size_request (sizingHandle, (newWidth >= 0) ? newWidth : 0, oldHeight);
	}

	super.setBoundsInPixels (x, y, width, height);
}

/**
 * Usually called when control is resized or first initialized.
 */
private void setDropDownButtonSize() {
	Rectangle rect = getClientAreaInPixels();
	int parentWidth = rect.width;
	int parentHeight = rect.height;
	Point buttonSize = down.computeSizeInPixels(SWT.DEFAULT, parentHeight);

	int dateEntryHeight = computeNativeSize(GTK.GTK4 ? editableHandle : textEntryHandle, SWT.DEFAULT, SWT.DEFAULT, false).y;

	//Move button a little closer to entry field, by amount of padding.
	int newXpos = parentWidth - buttonSize.x - getGtkBorderPadding().left - getGtkBorderPadding().right;

	int newYPos = parentHeight/2 - dateEntryHeight/2;
	down.setBoundsInPixels (newXpos, newYPos, buttonSize.x, dateEntryHeight);
}

/**
 * Gets the border padding structure, which can be used to determine the inner padding of the text field.
 * Note, this function returns the correct padding only under GTK3.
 * @return GtkBorder object that holds the padding values.
 */
GtkBorder getGtkBorderPadding () {
	//In Gtk3, acquire border.
	GtkBorder gtkBorderPadding = new GtkBorder ();
	long contextHandle = GTK.GTK4 ? editableHandle : textEntryHandle;
	long context = GTK.gtk_widget_get_style_context (contextHandle);
	int state_flag = GTK.gtk_widget_get_state_flags(contextHandle);
	gtk_style_context_get_padding(context, state_flag, gtkBorderPadding);
	return gtkBorderPadding;
}

boolean onNumberKeyInput(int key) {
	if (currentField == null) {
		return false;
	}
	int fieldName = getCalendarField(currentField);
	StringBuilder prefix = new StringBuilder();
	StringBuilder current = new StringBuilder();
	StringBuilder suffix = new StringBuilder();

	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	char c = iterator.first();
	do {
		if (isSameField(currentField, getFieldPosition(iterator))) {
			current.append(c);
		} else if (current.length() == 0) {
			prefix.append(c);
		} else {
			suffix.append(c);
		}
	} while ((c = iterator.next()) != CharacterIterator.DONE);

	if (typeBufferPos < 0) {
		typeBuffer.setLength(0);
		typeBuffer.append(current);
		typeBufferPos = 0;
	}
	if (key == GDK.GDK_BackSpace) {
		if (typeBufferPos > 0 && typeBufferPos <= typeBuffer.length()) {
			typeBuffer.deleteCharAt(typeBufferPos - 1);
			typeBufferPos--;
		}
	} else if (key == GDK.GDK_Delete) {
		if (typeBufferPos >= 0 && typeBufferPos < typeBuffer.length()) {
			typeBuffer.deleteCharAt(typeBufferPos);
		}
	} else {
		char newText = keyToString(key);
		// Don't allow non-digit character inputs for SWT.TIME, unless modifying the AM/PM field
		if ((style & SWT.TIME) != 0 && fieldName != Calendar.AM_PM && !Character.isDigit(newText)) {
			return false;
		}
		if (!Character.isAlphabetic(newText) && !Character.isDigit(newText)) {
			return false;
		}
		if (fieldName == Calendar.AM_PM) {
			if (dateFormat instanceof SimpleDateFormat) {
				String[] amPmStrings = ((SimpleDateFormat) dateFormat).getDateFormatSymbols().getAmPmStrings();
				if (amPmStrings[Calendar.AM].charAt(0) == newText) {
					setTextField(currentField, Calendar.AM);
					return false;
				} else if (amPmStrings[Calendar.PM].charAt(0) == newText) {
					setTextField(currentField, Calendar.PM);
					return false;
				}
			}
		}
		if (typeBufferPos < typeBuffer.length()) {
			typeBuffer.replace(typeBufferPos, typeBufferPos + 1, Character.toString(newText));
		} else {
			typeBuffer.append(newText);
		}
		typeBufferPos++;
	}
	StringBuilder newText = new StringBuilder(prefix);
	newText.append(typeBuffer);
	newText.append(suffix);
	setText(newText.toString());
	setTextSelection(prefix.length() + typeBufferPos, prefix.length() + typeBuffer.length());
	currentField.setBeginIndex(prefix.length());
	currentField.setEndIndex(prefix.length() + typeBuffer.length());
	if (!isCalendar()) {
		try {
			Date date = dateFormat.parse(getText());
			calendar.setTime(date);
		} catch (ParseException e) {
			// invalid value, input will reset...
		}
	}
	return false;
}

private char keyToString(int key) {
	// If numberpad keys were pressed.
	if (key >= GDK.GDK_KP_0 && key <= GDK.GDK_KP_9) {
		// convert numberpad button to regular key;
		key -= 65408;
	}
	return (char) GDK.gdk_keyval_to_unicode(key);
}

void updateControl() {
	if ((isDate() || isTime()) && textEntryHandle != 0) {
		setText(getFormattedString());
	}
	redraw ();
}

@Override
void register() {
	super.register();
	if (handle != 0 && display.getWidget(handle) == null) display.addWidget(handle, this);
	if (containerHandle != 0 && containerHandle != handle) display.addWidget (containerHandle, this);
	if (textEntryHandle != 0 && textEntryHandle != containerHandle) display.addWidget (textEntryHandle, this);
}

@Override
GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).handle;
}

@Override
void deregister () {
	super.deregister ();
	if (handle != 0 && display.getWidget(handle) != null) display.removeWidget(handle);
	if (containerHandle != 0 && containerHandle != handle) display.removeWidget (containerHandle);
	if (textEntryHandle != 0 && textEntryHandle != containerHandle) display.removeWidget (textEntryHandle);
}

int getArrow(long widget) {
	updateControl();
	int adj_value = (int) GTK.gtk_adjustment_get_value(GTK.gtk_spin_button_get_adjustment(widget));
	int new_value = 0;
	if (isDate()) {
		FieldPosition firstField = getNextField(null);
		int firstFieldConstant = getCalendarField(firstField);
		new_value = calendar.get(getCalendarField(firstField));
		if (firstFieldConstant == Calendar.MONTH) {
			if ((style & SWT.SHORT) != 0) {
				// adj_value returns the month as a number between 1-12
				// new_value gets the month as a number between 0-11
				// shift the adj_value by offset so that we get the correct arrow direction
				adj_value--;
			} else if ((style & SWT.MEDIUM) != 0 || (style & SWT.LONG) != 0 ) {
				// adj_value is either +1, 0, -1 when month is displayed as string
				if (adj_value == 0) {
					return 0;
				} else {
					return adj_value > 0 ? SWT.ARROW_UP : SWT.ARROW_DOWN;
				}
			}
		}
	} else if (isTime()) {
		new_value = getHours();
		if (hasAmPm()) {
			// as getHours () has 24h format but spinner 12h format, new_value needs to be
			// converted to 12h format
			if (getHours() > 12) {
				new_value = getHours() - 12;
			}
			if (new_value == 0)
				new_value = 12;
		}
	}
	if (adj_value == 0 && firstTime)
		return 0;
	firstTime = false;
	if (adj_value == new_value)
		return 0;
	return adj_value > new_value ? SWT.ARROW_UP : SWT.ARROW_DOWN;
}

/**
 * Calculates appropriate width of GtkEntry and
 * adds Date/Time string to the Date/Time Spinner
 */
void setText(String dateTimeText) {
	if (dateTimeText != null) {
		byte[] dateTimeConverted = Converter.javaStringToCString(dateTimeText);

		if (GTK.GTK4) {
			GTK.gtk_entry_buffer_set_text(GTK4.gtk_text_get_buffer(textEntryHandle), dateTimeConverted, dateTimeText.length());
		} else {
			//note, this is ignored if the control is in a fill-layout.
			GTK3.gtk_entry_set_width_chars(textEntryHandle, dateTimeText.length());
			GTK3.gtk_entry_set_text(textEntryHandle, dateTimeConverted);
		}

		if (popupCalendar != null && calendar != null) {
			Date parse;
			try {
				parse = dateFormat.parse(dateTimeText);
			} catch(ParseException e) {
				//not a valid date (yet), return for now
				return;
			}
			Calendar clone = (Calendar) calendar.clone();
			clone.setTime(parse);
			try {
				popupCalendar.setDate (clone.get(Calendar.YEAR), clone.get(Calendar.MONTH), clone.get(Calendar.DAY_OF_MONTH));
			} catch(SWTException e) {
				if (e.code == SWT.ERROR_WIDGET_DISPOSED) {
					//the calendar popup was disposed in the meantime so nothing to update
					return;
				}
				throw e;
			}
		}
	}
}

@Override
boolean gtk4_key_press_event(long controller, int keyval, int keycode, int state, long event) {
	if (!isReadOnly() && (isTime() || isDate())) {
		switch (keyval) {
		case GDK.GDK_Up:
		case GDK.GDK_KP_Up:
			incrementField(+1);
			break;
		case GDK.GDK_Down:
		case GDK.GDK_KP_Down:
			incrementField(-1);
			break;
		case GDK.GDK_Tab:
		case GDK.GDK_Right:
		case GDK.GDK_KP_Right:
			selectField(getNextField(currentField));
			sendEvent(SWT.Traverse);
			break;
		case GDK.GDK_Left:
		case GDK.GDK_KP_Left:
			selectField(getPreviousField(currentField));
			sendEvent(SWT.Traverse);
			break;
		case GDK.GDK_Home:
		case GDK.GDK_KP_Home:
			/* Set the value of the current field to its minimum */
			if (currentField != null) {
				setTextField(currentField, calendar.getActualMinimum(getCalendarField(currentField)));
			}
			break;
		case GDK.GDK_End:
		case GDK.GDK_KP_End:
			/* Set the value of the current field to its maximum */
			if (currentField != null) {
				setTextField(currentField, calendar.getActualMaximum(getCalendarField(currentField)));
			}
			break;
		default:
			onNumberKeyInput(keyval);
		}
	}

	return true;
}

@Override
long gtk_key_press_event (long widget, long event) {
	if (!isReadOnly () && (isTime () || isDate ())) {
		int [] key = new int[1];
		GDK.gdk_event_get_keyval(event, key);

		switch (key[0]) {
		case GDK.GDK_Up:
		case GDK.GDK_KP_Up:
			incrementField(+1);
			commitData();
			break;
		case GDK.GDK_Down:
		case GDK.GDK_KP_Down:
			incrementField(-1);
			commitData();
			break;
		case GDK.GDK_Tab:
		case GDK.GDK_Right:
		case GDK.GDK_KP_Right:
			selectField(getNextField(currentField));
			sendEvent(SWT.Traverse);
			break;
		case GDK.GDK_Left:
		case GDK.GDK_KP_Left:
			selectField(getPreviousField(currentField));
			sendEvent(SWT.Traverse);
			break;
		case GDK.GDK_Home:
		case GDK.GDK_KP_Home:
			/* Set the value of the current field to its minimum */
			if (currentField != null) {
				setTextField(currentField, calendar.getActualMinimum(getCalendarField(currentField)));
			}
			break;
		case GDK.GDK_End:
		case GDK.GDK_KP_End:
			/* Set the value of the current field to its maximum */
			if (currentField != null) {
				setTextField(currentField, calendar.getActualMaximum(getCalendarField(currentField)));
			}
			break;
		default:
			onNumberKeyInput(key[0]);
		}
	}
	return 1;
}

void commitData() {
	try {
		Date date = dateFormat.parse(getText());
		calendar.setTime(date);
	} catch (ParseException e) {
		// invalid value, input will reset...
	}
	updateControl();
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return Spinner's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
String getText() {
	checkWidget();
	if (textEntryHandle != 0) {
		long stringPtr;

		if (GTK.GTK4) {
			stringPtr = GTK.gtk_entry_buffer_get_text(GTK4.gtk_text_get_buffer(textEntryHandle));
		} else {
			stringPtr = GTK3.gtk_entry_get_text(textEntryHandle);
		}
		if (stringPtr == 0) return "";

		int length = C.strlen(stringPtr);
		byte[] buffer = new byte[length];
		C.memmove(buffer, stringPtr, length);

		return new String(Converter.mbcsToWcs(buffer));
	} else {
		return "";
	}
}

/**
 * Gets the substring between start and end indices of the
 * caller provided string. Checks are done to ensure that start
 * and end indices are well formed.
 *
 * @return substring of str between start & end. If start & end
 * indices are malformed, an empty string is returned.
 */
String getText(String str, int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";

	int length = str.length();
	end = Math.min(end, length - 1);
	if (start > end) return "";
	start = Math.max(0, start);

	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return str.substring(start, end + 1);
}

/**
 * Returns the selected text. If selected text is less than
 * one character the returned Point has equal start and end values.
 *
 * @return the highlighted (selected) text or position of cursor
 **/
Point getTextSelection() {
	checkWidget();
	int[] start = new int[1];
	int[] end = new int[1];
	GTK.gtk_editable_get_selection_bounds(textEntryHandle, start, end);

	long stringPtr;
	if (GTK.GTK4) {
		stringPtr = GTK.gtk_entry_buffer_get_text(GTK4.gtk_text_get_buffer(textEntryHandle));
	} else {
		stringPtr = GTK3.gtk_entry_get_text (textEntryHandle);
	}

	start[0] = (int)OS.g_utf8_offset_to_utf16_offset(stringPtr, start[0]);
	end[0] = (int)OS.g_utf8_offset_to_utf16_offset(stringPtr, end[0]);

	return new Point(start[0], end[0]);
}

/** Highlight (select) the text between the start and end. **/
void setTextSelection(int start, int end) {
	checkWidget();

	long stringPtr;
	if (GTK.GTK4) {
		stringPtr = GTK.gtk_entry_buffer_get_text(GTK4.gtk_text_get_buffer(textEntryHandle));
	} else {
		stringPtr = GTK3.gtk_entry_get_text(textEntryHandle);
	}

	start = (int) OS.g_utf16_offset_to_utf8_offset(stringPtr, start);
	end = (int) OS.g_utf16_offset_to_utf8_offset(stringPtr, end);

	GTK.gtk_editable_set_position(textEntryHandle, start);
	GTK.gtk_editable_select_region(textEntryHandle, start, end);
}

void setTextField(FieldPosition field, int value) {
	int validValue = validateValueBounds(field, value);
	setFieldOfInternalDataStructure(field, validValue);
	setFieldOfInternalDataStructure(field, value);
	updateControl();
	if (currentField != null) {
		selectField(currentField);
	}
}

private int validateValueBounds(FieldPosition field, int value) {
	int calendarField = getCalendarField(field);
	int max = calendar.getActualMaximum (calendarField);
	int min = calendar.getActualMinimum (calendarField);
	if (calendarField == Calendar.YEAR) {
		max = MAX_YEAR;
		min = MIN_YEAR;
		/* Special case: convert 1 or 2-digit years into reasonable 4-digit years. */
		int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
		int currentCentury = (currentYear / 100) * 100;
		if (value < (currentYear + 30) % 100) value += currentCentury;
		else if (value < 100) value += currentCentury - 100;
	}
	if (value > max) value = min; // wrap
	if (value < min) value = max; // wrap
	return value;
}

@Override
long gtk_button_release_event (long widget, long event) {
	if (isDate() || isTime()) {
		int [] eventButton = new int [1];
		GDK.gdk_event_get_button(event, eventButton);

		if (eventButton[0] == 1) { // left mouse button.
			onTextMouseClick();
		}
	}
	return super.gtk_button_release_event(widget, event);
}

@Override
void gtk_gesture_release_event(long gesture, int n_press, double x, double y, long event) {
	if (isDate() || isTime()) {
		int button = GTK.gtk_gesture_single_get_current_button(gesture);

		if (button == 1) {
			onTextMouseClick();
		}
	}

	super.gtk_gesture_release_event(gesture, n_press, x, y, event);
}

/**
 * Output signal is called when Spinner's arrow buttons are triggered,
 * usually by clicking the mouse on the [gtk3: +/-] buttons.
 * On every click output is called twice presenting current and previous value.
 * This method compares two values and determines if Up or down arrow was called.
 */
@Override
long gtk_output (long widget) {
	if (calendar == null) {
		return 0; //Guard: Object not fully initialized yet.
	}
	int arrowType = getArrow(widget);
	switch (arrowType) {
	case SWT.ARROW_UP: // Gtk3 "+" button.
		commitData();
		incrementField(+1);
		break;
	case SWT.ARROW_DOWN: // Gtk3 "-" button.
		commitData();
		incrementField(-1);
		break;
	}
	return 1;
}

void replaceCurrentlySelectedTextRegion (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (string, false);
	int [] start = new int [1], end = new int [1];
	GTK.gtk_editable_get_selection_bounds (textEntryHandle, start, end);
	GTK.gtk_editable_delete_selection (textEntryHandle);
	GTK.gtk_editable_insert_text (textEntryHandle, buffer, buffer.length, start);
	GTK.gtk_editable_set_position (textEntryHandle, start [0]);
}

void onTextMouseClick() {
	if (calendar == null) {
		return; // Guard: Object not fully initialized yet.
	}
	int clickPosition = getTextSelection().x;
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	iterator.first();
	int pos = 0;
	do {
		FieldPosition position = getFieldPosition(iterator);
		iterator.setIndex(iterator.getRunLimit());
		if (isSameField(position, currentField)) {
			// use the current field instead then!
			position = currentField;
		}
		int fieldWidth = position.getEndIndex() - position.getBeginIndex();
		pos += fieldWidth;
		if (position.getFieldAttribute() == null) {
			continue;
		}
		if (pos >= clickPosition) {
			FieldPosition selectField = new FieldPosition(position.getFieldAttribute());
			selectField.setBeginIndex(pos - fieldWidth);
			selectField.setEndIndex(pos);
			selectField(selectField);
			break;
		}
	} while (iterator.current() != CharacterIterator.DONE);

}

String getText (int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	String str = getText ();
	int length = str.length ();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return str.substring (start, end + 1);
}

void selectAll () {
	checkWidget ();
	if (textEntryHandle != 0)
		GTK.gtk_editable_select_region (textEntryHandle, 0, -1);
}


void hideDateTime () {
	if (isDate () || isTime ()){
		GTK.gtk_widget_hide (fixedHandle);
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (fixedHandle != 0)
		hideDateTime ();
}

/**
 * Returns a field with updated positionla data
 */
private FieldPosition updateField(FieldPosition field) {
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	while (iterator.current() != CharacterIterator.DONE) {
		FieldPosition current = getFieldPosition(iterator);
		iterator.setIndex(iterator.getRunLimit());
		if (field == null || isSameField(current, field)) {
			return current;
		}
	}
	return field;
}

/**
 * Given a {@link FieldPosition} searches the next field in the format string
 *
 * @param field
 *            the Field to start from
 * @return the next {@link FieldPosition}
 */
private FieldPosition getNextField(FieldPosition field) {
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	FieldPosition first = null;
	boolean found = false;
	while (iterator.current() != CharacterIterator.DONE) {
		FieldPosition current = getFieldPosition(iterator);
		iterator.setIndex(iterator.getRunLimit());
		if (current.getFieldAttribute() == null) {
			continue;
		}
		if (found) {
			return current;
		}
		if (first == null) {
			first = current;
		}
		if (isSameField(current, field)) {
			found = true;
		}
	}
	return first;
}

/**
 *
 * @return the next field of the given one
 */
private FieldPosition getPreviousField(FieldPosition field) {
	AttributedCharacterIterator iterator = dateFormat.formatToCharacterIterator(calendar.getTime());
	FieldPosition last = null;
	do {
		FieldPosition current = getFieldPosition(iterator);
		if (isSameField(current, field)) {
			if (last != null) {
				return last;
			}
		}
		if (current.getFieldAttribute() != null) {
			last = current;
		}
		iterator.setIndex(iterator.getRunLimit());
	} while (iterator.current() != CharacterIterator.DONE);
	return last;
}

/**
 * Searches the current postion of the iterator for a {@link Field} and
 * constructs a {@link FieldPosition} from it
 *
 * @param iterator
 *            the iterator to use
 * @return a new {@link FieldPosition}
 */
private static FieldPosition getFieldPosition(AttributedCharacterIterator iterator) {
	Set<Attribute> keySet = iterator.getAttributes().keySet();
	for (Attribute attribute : keySet) {
		if (attribute instanceof Field) {
			return getFieldPosition((Field) attribute, iterator);
		}
	}
	return getFieldPosition((Field) null, iterator);
}

/**
 * creates a {@link FieldPosition} out of a {@link Field} and and a
 * {@link AttributedCharacterIterator}s current position
 *
 * @param field
 *            the field to use
 * @param iterator
 *            the iterator to extract the data from
 * @return a {@link FieldPosition} init to this Field and begin/end index
 */
private static FieldPosition getFieldPosition(Field field, AttributedCharacterIterator iterator) {
	FieldPosition position = new FieldPosition(field);
	position.setBeginIndex(iterator.getRunStart());
	position.setEndIndex(iterator.getRunLimit());
	return position;
}

/**
 * Check if the given {@link FieldPosition} are considdered "the same", this is
 * when both are not <code>null</code> and reference the same
 * {@link java.text.Format.Field} attribute, or both of them have no
 * fieldattribute and have the same position
 *
 * @param p1
 *            first position to compare
 * @param p2
 *            second position to compare
 * @return <code>true</code> if considered the same, <code>false</code>
 *         otherwise
 */
private static boolean isSameField(FieldPosition p1, FieldPosition p2) {
	if (p1 == p2) {
		return true;
	}
	if (p1 == null || p2 == null) {
		return false;
	}
	if (p1.getFieldAttribute() == null && p2.getFieldAttribute() == null) {
		return p1.equals(p2);
	}
	if (p1.getFieldAttribute() == null) {
		return false;
	}
	return p1.getFieldAttribute().equals(p2.getFieldAttribute());
}

/**
 * Extracts the calendarfield for the given fieldposition
 *
 * @return the {@link Calendar} field or -1 if this is not a valid Fieldposition
 */
private static int getCalendarField(FieldPosition fieldPosition) {
	if ((fieldPosition.getFieldAttribute() instanceof Field)) {
		return getCalendarField((Field) fieldPosition.getFieldAttribute());
	} else {
		return -1;
	}
}

/**
 * Extracts the calendarfield transforming HOUR1 types to HOUR0
 *
 * @return the calendarfield coresponding to the {@link Field}
 */
private static int getCalendarField(Field field) {
	if (Field.HOUR1.equals(field)) {
		field = Field.HOUR0;
	} else if (Field.HOUR_OF_DAY1.equals(field)) {
		field = Field.HOUR_OF_DAY0;
	}
	return field.getCalendarField();
}

}