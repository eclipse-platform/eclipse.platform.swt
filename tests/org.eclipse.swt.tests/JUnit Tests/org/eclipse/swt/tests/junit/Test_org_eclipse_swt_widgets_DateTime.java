/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
package org.eclipse.swt.tests.junit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DateTime
 *
 * @see org.eclipse.swt.widgets.DateTime
 */
public abstract class Test_org_eclipse_swt_widgets_DateTime extends Test_org_eclipse_swt_widgets_Control {
	static final int JAN = 0, FEB = 1, AUG = 7, NOV = 10;
	DateTime datetime;
	private int style;

public Test_org_eclipse_swt_widgets_DateTime(int style) {
	this.style = style;
}

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	datetime = new DateTime(shell, style);
	setWidget(datetime);
}


@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	new DateTime(shell, SWT.NULL);

	new DateTime(shell, SWT.DATE);

	new DateTime(shell, SWT.TIME);

	new DateTime(shell, SWT.CALENDAR);

	new DateTime(shell, SWT.DATE | SWT.LONG);

	new DateTime(shell, SWT.TIME | SWT.LONG);

	new DateTime(shell, SWT.CALENDAR | SWT.LONG);

	new DateTime(shell, SWT.DATE | SWT.MEDIUM);

	new DateTime(shell, SWT.TIME | SWT.MEDIUM);

	new DateTime(shell, SWT.CALENDAR | SWT.MEDIUM);

	new DateTime(shell, SWT.DATE | SWT.SHORT);

	new DateTime(shell, SWT.TIME | SWT.SHORT);

	new DateTime(shell, SWT.CALENDAR | SWT.SHORT);

	new DateTime(shell, SWT.DROP_DOWN);

	assertThrows(IllegalArgumentException.class, () -> new DateTime(null, 0));
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};

	assertThrows(IllegalArgumentException.class, () -> datetime.addSelectionListener(null));

	datetime.addSelectionListener(listener);
	datetime.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	assertThrows(IllegalArgumentException.class, () -> datetime.removeSelectionListener(null));

	listenerCalled = false;
	datetime.removeSelectionListener(listener);
	datetime.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	datetime.addSelectionListener(listener);
	datetime.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;
	datetime.removeSelectionListener(listener);
	datetime.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_setBackgroundCalendarDateTime() {
	DateTime calendar = new DateTime(shell, SWT.CALENDAR);
	Color color = new Color(255, 0, 0);
	calendar.setBackground(color);
	assertEquals(color, calendar.getBackground());
	calendar.setBackground(null);
	assertNotEquals(color, calendar.getBackground());
	color = new Color(255, 0, 0, 0);
	calendar.setBackground(color);
	assertEquals(color, calendar.getBackground());
	calendar.setBackground(null);
	assertNotEquals(color, calendar.getBackground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		calendar.setBackground(color);
		calendar.setForeground(fg);
		assertEquals(color, calendar.getBackground());
		assertEquals(fg, calendar.getForeground());
	}
	calendar.dispose();
}

@Test
public void test_setBackgroundAlphaCalendarDateTime() {
	DateTime calendar = new DateTime(shell, SWT.CALENDAR);
	Color color = new Color (255, 0, 0, 0);
	calendar.setBackground(color);
	assertEquals(color, calendar.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	calendar.setForeground(fg);
	assertEquals(color, calendar.getBackground());
	calendar.dispose();
}

@Test
public void test_setBackgroundTimeDateTime() {
	DateTime time = new DateTime(shell, SWT.TIME);
	Color color = new Color(255, 0, 0);
	time.setBackground(color);
	assertEquals(color, time.getBackground());
	time.setBackground(null);
	assertNotEquals(color, time.getBackground());
	color = new Color(255, 0, 0, 0);
	time.setBackground(color);
	assertEquals(color, time.getBackground());
	time.setBackground(null);
	assertNotEquals(color, time.getBackground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		time.setBackground(color);
		time.setForeground(fg);
		assertEquals(color, time.getBackground());
		assertEquals(fg, time.getForeground());
	}
	time.dispose();
}

@Test
public void test_setBackgroundAlphaTimeDateTime() {
	DateTime time = new DateTime(shell, SWT.TIME);
	Color color = new Color (255, 0, 0, 0);
	time.setBackground(color);
	assertEquals(color, time.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	time.setForeground(fg);
	assertEquals(color, time.getBackground());
	time.dispose();
}

@Test
public void test_setBackgroundDateDateTime() {
	DateTime date = new DateTime(shell, SWT.DATE);
	Color color = new Color(255, 0, 0);
	date.setBackground(color);
	assertEquals(color, date.getBackground());
	date.setBackground(null);
	assertNotEquals(color, date.getBackground());
	color = new Color(255, 0, 0, 0);
	date.setBackground(color);
	assertEquals(color, date.getBackground());
	date.setBackground(null);
	assertNotEquals(color, date.getBackground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		date.setBackground(color);
		date.setForeground(fg);
		assertEquals(color, date.getBackground());
		assertEquals(fg, date.getForeground());
	}
	date.dispose();
}

@Test
public void test_setBackgroundAlphaDateDateTime() {
	DateTime date = new DateTime(shell, SWT.DATE);
	Color color = new Color (255, 0, 0, 0);
	date.setBackground(color);
	assertEquals(color, date.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	date.setForeground(fg);
	assertEquals(color, date.getBackground());
	date.dispose();
}

@Test
public void test_setDateIII() {
	datetime.setDate(2008, AUG, 31);
	assertEquals(31, datetime.getDay());
	assertEquals(AUG, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, NOV, 30);
	assertEquals(30, datetime.getDay());
	assertEquals(NOV, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, FEB, 29);
	assertEquals(29, datetime.getDay());
	assertEquals(FEB, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2007, FEB, 28);
	assertEquals(28, datetime.getDay());
	assertEquals(FEB, datetime.getMonth());
	assertEquals(2007, datetime.getYear());

	datetime.setDate(2008, JAN, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	// The following lines are commented out because they fail on Windows.
	// The UK and the US adopted the Gregorian calendar on September 14, 1752
	// so the Windows control considers Jan 1, 1752 to be in the Julian calendar.
//	datetime.setDate(1752, JAN, 1);
//	assertEquals(1, datetime.getDay());
//	assertEquals(JAN, datetime.getMonth());
//	assertEquals(1752, datetime.getYear());

	datetime.setDate(1752, NOV, 30);
	assertEquals(30, datetime.getDay());
	assertEquals(NOV, datetime.getMonth());
	assertEquals(1752, datetime.getYear());
	datetime.setDate(2008, JAN, 1);

	datetime.setDate(9999, JAN, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(9999, datetime.getYear());

	datetime.setDate(2008, 0, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(0, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, 11, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(11, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, JAN, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(1999, FEB, 29); // Feb 29, 1999 is not valid, so setDate should be ignored
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(1999, NOV, 31); // Nov 31 is not valid, so setDate should be ignored
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(1751, JAN, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(10000, JAN, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, -1, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, 12, 1);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, JAN, 0);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());

	datetime.setDate(2008, JAN, 99);
	assertEquals(1, datetime.getDay());
	assertEquals(JAN, datetime.getMonth());
	assertEquals(2008, datetime.getYear());
}

@Test
public void test_setDayI() {
	datetime.setDate(2008, AUG, 1);
	for (int day = 1; day <= 31; day++) {
		datetime.setDay(day);
		assertEquals(day, datetime.getDay());
	}

	int day = datetime.getDay();
	datetime.setDay(32); // No month has 32 days, so setDay should be ignored
	assertEquals(day, datetime.getDay());

	datetime.setDay(-5); // Make sure negative setDay is ignored
	assertEquals(day, datetime.getDay());

	datetime.setDate(2008, FEB, 1);
	datetime.setDay(30); // Feb never has 30 days, so setDay should be ignored
	assertEquals(1, datetime.getDay());

	datetime.setDate(2007, FEB, 5);
	datetime.setDay(29); // Feb 2007 did not have 29 days, so setDay should be ignored
	assertEquals(5, datetime.getDay());
}

@Test
public void test_setForegroundCalendarDateTime() {
	DateTime calendar = new DateTime(shell, SWT.CALENDAR);
	Color color = new Color(255, 0, 0);
	calendar.setForeground(color);
	assertEquals(color, calendar.getForeground());
	calendar.setForeground(null);
	assertNotEquals(color, calendar.getForeground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		calendar.setForeground(color);
		calendar.setBackground(bg);
		assertEquals(color, calendar.getForeground());
		assertEquals(bg, calendar.getBackground());
	}
	calendar.dispose();
}

@Test
public void test_setForegroundAlphaCalendarDateTime() {
	DateTime calendar = new DateTime(shell, SWT.CALENDAR);
	assumeTrue(SwtTestUtil.isCocoa || SwtTestUtil.isGTK,
			"Alpha support for foreground colors does not exist on Win32");
	Color color = new Color (255, 0, 0, 0);
	calendar.setForeground(color);
	assertEquals(color, calendar.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	calendar.setBackground(bg);
	assertEquals(color, calendar.getForeground());
	calendar.dispose();
}

@Test
public void test_setForegroundTimeDateTime() {
	DateTime time = new DateTime(shell, SWT.TIME);
	Color color = new Color(255, 0, 0);
	time.setForeground(color);
	assertEquals(color, time.getForeground());
	time.setForeground(null);
	assertNotEquals(color, time.getForeground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		time.setForeground(color);
		time.setBackground(bg);
		assertEquals(color, time.getForeground());
		assertEquals(bg, time.getBackground());
	}
	time.dispose();
}

@Test
public void test_setForegroundAlphaTimeDateTime() {
	DateTime time = new DateTime(shell, SWT.TIME);
	assumeTrue(SwtTestUtil.isCocoa || SwtTestUtil.isGTK,
			"Alpha support for foreground colors does not exist on Win32");
	Color color = new Color (255, 0, 0, 0);
	time.setForeground(color);
	assertEquals(color, time.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	time.setBackground(bg);
	assertEquals(color, time.getForeground());
	time.dispose();
}

@Test
public void test_setForegroundDateDateTime() {
	DateTime date = new DateTime(shell, SWT.DATE);
	Color color = new Color(255, 0, 0);
	date.setForeground(color);
	assertEquals(color, date.getForeground());
	date.setForeground(null);
	assertNotEquals(color, date.getForeground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		date.setForeground(color);
		date.setBackground(bg);
		assertEquals(color, date.getForeground());
		assertEquals(bg, date.getBackground());
	}
	date.dispose();
}

@Test
public void test_setForegroundAlphaDateDateTime() {
	DateTime date = new DateTime(shell, SWT.DATE);
	assumeTrue(SwtTestUtil.isCocoa || SwtTestUtil.isGTK,
			"Alpha support for foreground colors does not exist on Win32");
	Color color = new Color (255, 0, 0, 0);
	date.setForeground(color);
	assertEquals(color, date.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	date.setBackground(bg);
	assertEquals(color, date.getForeground());
	date.dispose();
}

@Test
public void test_setHoursI() {
	datetime.setTime(2, 10, 30);
	datetime.setHours(21);
	assertEquals(21, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setHours(24); // The max is 23 hours, so the setHours should be ignored
	assertEquals(21, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setHours(-1); // The min is 0 hours, so the setHours should be ignored
	assertEquals(21, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());
}

@Test
public void test_setMinutesI() {
	datetime.setTime(2, 10, 30);
	datetime.setMinutes(22);
	assertEquals(2, datetime.getHours());
	assertEquals(22, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setMinutes(0);
	assertEquals(2, datetime.getHours());
	assertEquals(0, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setMinutes(59);
	assertEquals(2, datetime.getHours());
	assertEquals(59, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setMinutes(-1); // The max is 59 minutes, so the setMinutes should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(59, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setMinutes(60); // The min is 0 minutes, so the setMinutes should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(59, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());
}

@Test
public void test_setMonthI() {
	datetime.setDate(2008, NOV, 1);
	datetime.setMonth(AUG);
	assertEquals(AUG, datetime.getMonth());

	datetime.setMonth(JAN);
	assertEquals(JAN, datetime.getMonth());

	datetime.setMonth(FEB);
	assertEquals(FEB, datetime.getMonth());

	datetime.setMonth(NOV);
	assertEquals(NOV, datetime.getMonth());

	datetime.setDate(2008, NOV, 30);
	datetime.setMonth(FEB); // Feb does not have 30 days, so setMonth should be ignored
	assertEquals(NOV, datetime.getMonth());

	datetime.setDate(2008, AUG, 31);
	datetime.setMonth(NOV); // Nov does not have 31 days, so setMonth should be ignored
	assertEquals(AUG, datetime.getMonth());

	datetime.setDate(2007, NOV, 29);
	datetime.setMonth(FEB); // Feb 2007 does not have 29 days, so setMonth should be ignored
	assertEquals(NOV, datetime.getMonth());
}

@Test
public void test_setSecondsI() {
	datetime.setTime(2, 10, 30);
	datetime.setSeconds(52);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(52, datetime.getSeconds());

	datetime.setSeconds(0);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(0, datetime.getSeconds());

	datetime.setSeconds(59);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());

	datetime.setSeconds(-1); // The max is 59 seconds, so the setSeconds should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());

	datetime.setSeconds(60); // The min is 0 seconds, so the setSeconds should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());
}

@Test
public void test_setTimeIII() {
	datetime.setTime(2, 10, 30);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setTime(0, 10, 30);
	assertEquals(0, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setTime(23, 10, 30);
	assertEquals(23, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setTime(2, 0, 30);
	assertEquals(2, datetime.getHours());
	assertEquals(0, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setTime(2, 59, 30);
	assertEquals(2, datetime.getHours());
	assertEquals(59, datetime.getMinutes());
	assertEquals(30, datetime.getSeconds());

	datetime.setTime(2, 10, 0);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(0, datetime.getSeconds());

	datetime.setTime(2, 10, 59);
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());

	datetime.setTime(29, 10, 30); // Max hours is 23 so the entire setTime should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());

	datetime.setTime(2, 73, 30); // Max minutes is 59 so the entire setTime should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());

	datetime.setTime(2, 10, 95); // Max seconds is 59 so the entire setTime should be ignored
	assertEquals(2, datetime.getHours());
	assertEquals(10, datetime.getMinutes());
	assertEquals(59, datetime.getSeconds());
}

@Test
public void test_setYearI() {
	datetime.setDate(2008, NOV, 1);
	datetime.setYear(1947);
	assertEquals(1947, datetime.getYear());

	datetime.setYear(1752);
	assertEquals(1752, datetime.getYear());

	datetime.setYear(9999);
	assertEquals(9999, datetime.getYear());

	datetime.setYear(2005);
	assertEquals(2005, datetime.getYear());

	datetime.setYear(0);
	assertEquals(2005, datetime.getYear());

	datetime.setYear(-1947);
	assertEquals(2005, datetime.getYear());

	datetime.setDate(2008, FEB, 29);
	datetime.setYear(2007);
	assertEquals(2008, datetime.getYear());
}
@Test
public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener()
}

protected void setUp(int style) {
	super.setUp();
	datetime = new DateTime(shell, style);
	setWidget(datetime);
}

@Test
public void test_consistency_MenuDetect () {
	consistencyEvent(10, 10, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.DATE);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.TIME);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.CALENDAR);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);

}

@Test
public void test_consistency_MouseSelection () {
	consistencyEvent(10, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.DATE);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.TIME);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.CALENDAR);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}
@Test
public void test_consistency_EnterSelection () {
	tearDown();
	setUp(SWT.DATE);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.TIME);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.CALENDAR);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
}
@Test
public void test_consistency_DragDetect () {
	consistencyEvent(10, 10, 20, 20, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.DATE);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.TIME);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.CALENDAR);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
}


}
