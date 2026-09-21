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
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Widget
 *
 * @see org.eclipse.swt.widgets.Widget
 */
public class Test_org_eclipse_swt_widgets_Widget{
	// Use this variable to help validate callbacks
	boolean listenerCalled;
	/**
	 * Set this to true if the unit test intentionally disposed its widget as part of the test
	 */
	boolean disposedIntentionally;
	@Rule public TestName name = new TestName();

@Before
public void setUp() {
	shell = new Shell();
}

@After
public void tearDown() {
	if (widget != null) {
		assertEquals(disposedIntentionally, widget.isDisposed());
	}
	Display display = null;
	if (!disposedIntentionally) {
		assertFalse(shell.isDisposed());
		display = shell.getDisplay();
	}
	shell.dispose();
	if (widget != null) {
		assertTrue(widget.isDisposed());
		if(SwtTestUtil.isLinux && display != null) {
			assertNotExists(getWidgetTable(display), widget);
		}
	}
	assertTrue(shell.isDisposed());
	if(SwtTestUtil.isCocoa || SwtTestUtil.isGTK) {
		// process pending events to properly close the shell
		while (display != null && !display.isDisposed() && display.readAndDispatch()) {
		}
	}
	if(SwtTestUtil.isLinux && display != null) {
		assertNotExists(getWidgetTable(display), shell);
	}
}
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_WidgetI() {
	// abstract class
}
@Test
public void test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener() {
	DisposeListener listener = e -> {
	};
	widget.addDisposeListener(listener);
	widget.removeDisposeListener(listener);
}
@Test
public void test_addListenerILorg_eclipse_swt_widgets_Listener() {
	try {
		widget.addListener(SWT.Dispose, null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	Listener listener = e -> {
	};
	widget.addListener(SWT.Dispose, listener);
	widget.removeListener(SWT.Dispose, listener);
}
@Test
public void test_getDisplay() {
	assertEquals(widget.getDisplay(), widget.getDisplay());
}
@Test
public void test_isDisposed() {
	assertFalse(widget.isDisposed());
}
@Test
public void test_notifyListenersILorg_eclipse_swt_widgets_Event() {
	widget.notifyListeners(0, null);
	Event event = new Event();
	GC gc = null;
	if (widget instanceof Control) {
		gc = event.gc = new GC((Control)widget);
	}
	widget.notifyListeners(SWT.Paint, event);
	if (gc != null) gc.dispose();
}
@Test
public void test_removeListenerILorg_eclipse_swt_widgets_Listener() {
	// this method is further tested by all of the removeTypedListener tests
	try {
		widget.removeListener(SWT.Paint, null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	widget.removeListener(SWT.Paint, e -> {
	});

	Listener listener = e -> {
	};
	widget.addListener(SWT.Paint, listener);
	widget.removeListener(SWT.Paint, listener);
}
@Test
public void test_setDataLjava_lang_Object() {
	widget.setData(widget);
	assertEquals(widget, widget.getData());

	widget.setData(null);
	assertNull(widget.getData());
}
@Test
public void test_setDataLjava_lang_StringLjava_lang_Object() {
	widget.setData("the widget", widget);
	assertEquals(widget, widget.getData("the widget"));

	widget.setData("the widget", null);
	assertNull(widget.getData("the widget"));

	try {
		widget.setData(null, null);
		fail();
	} catch(IllegalArgumentException e) {
		// expected
	}
}
@Test
public void test_toString() {
	assertNotNull(widget.toString());
	assertTrue(widget.toString().length() > 0);
}

/* custom */
public Shell shell;
private Widget widget;

protected void setWidget(Widget w) {
	widget = w;
}

protected void hookListeners(Widget w, int[] types, Listener listener) {
	for (int type : types) {
		w.addListener(type, listener);
	}
}

protected String[] hookExpectedEvents(String type, final java.util.List<String> events) {
	return hookExpectedEvents(widget, type, events);
}

protected String[] hookExpectedEvents(Widget w, String type, final java.util.List<String> events) {
	String[] expectedEvents = ConsistencyUtility.eventOrdering.get(type);
	hookExpectedEvents(w, expectedEvents, events);
	return expectedEvents;
}

protected void hookExpectedEvents(Widget w, String[] types, final java.util.List<String> events) {
	hookListeners(w, ConsistencyUtility.convertEventNames(types),
			e -> {
				String temp = ConsistencyUtility.eventNames[e.type];
				if(e.type == SWT.Traverse)
					temp += ":"+ConsistencyUtility.getTraversalType(e.detail);
				else if(e.type == SWT.Selection)
					temp += ":"+ConsistencyUtility.getSelectionType(e.detail);
				events.add(temp);
				System.out.println(temp + e.widget);
			});
}

protected String getTestName() {
	String test = "" + name.getMethodName();
	int index = test.lastIndexOf('_');
	if(index != -1)
		test = test.substring(index+1);
	String clss = getClassName();
	if((!test.equals("MenuDetect") || clss.equals("Table") || test.startsWith("Chevron")) &&
		(!test.equals("DragDetect") || clss.equals("Tree") || test.startsWith("Chevron")) &&
		(!test.equals("DoubleClick") || clss.equals("List")) &&
		(!test.equals("KeySelection") || clss.equals("Slider") || clss.equals("Combo") || clss.equals("CCombo") || clss.equals("CTabFolder")) &&
		(!test.equals("EnterSelection") || clss.equals("Button") || clss.equals("ToolBar") || clss.equals("CCombo") || clss.equals("ExpandBar")))
		test = clss + test;
	return test;
}

protected String getClassName() {
	String clazz = getClass().getName();
	int index = clazz.lastIndexOf('_');
	if(index != -1)
		clazz = clazz.substring(index+1);
	return clazz;
}

/**
 * Renders the shell and process events for duration.
 * Convenience method for debugging tests, ⚠️ should not be
 * called for headless tests!
 * @param shell The shell to render.
 * @param durationMs duration in milliseconds. Method exits after duration.
 */
protected void render(Shell shell, int durationMs) throws InterruptedException {
	long timestamp = System.currentTimeMillis();
	shell.pack();
	shell.layout();
	shell.setVisible(true);
	while (System.currentTimeMillis() - timestamp < durationMs) {
		if (!shell.getDisplay().readAndDispatch()) {
			Thread.sleep(50);
		}
	}
}

protected void assertNotExists(Widget[] widgetTable, Widget w) {
	for (Widget widget : widgetTable) {
		if(widget == w) {
			fail("Widget table leaks: " + w);
		}
	}
}

protected static Widget[] getWidgetTable(Display display) {
	try {
		Field field = Display.class.getDeclaredField("widgetTable");
		field.setAccessible(true);
		Widget[] widgetTable = (Widget[]) field.get(display);
		return widgetTable;
	} catch (Throwable t) {
		t.printStackTrace();
		return null;
	}
}

}
