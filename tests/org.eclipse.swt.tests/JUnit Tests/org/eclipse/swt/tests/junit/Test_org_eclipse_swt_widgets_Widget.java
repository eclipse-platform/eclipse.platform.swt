/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Widget
 *
 * @see org.eclipse.swt.widgets.Widget
 */
public class Test_org_eclipse_swt_widgets_Widget extends TestCase {
	// Use this variable to help validate callbacks
	boolean listenerCalled;

public Test_org_eclipse_swt_widgets_Widget(String name) {
	super(name);
}

@Override
protected void setUp() {
	shell = new Shell();
}

@Override
protected void tearDown() {
	if (widget != null) {
		assertEquals(false, widget.isDisposed());
	}
	assertEquals(false, shell.isDisposed());
	shell.dispose();
	if (widget != null) {
		assertTrue(widget.isDisposed());
	}
	assertTrue(shell.isDisposed());
}
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_WidgetI() {
	// abstract class
}
@Test
public void test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener() {
	DisposeListener listener = new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
		}
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

	Listener listener = new Listener() {
		public void handleEvent(Event e) {
		}
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
	assertEquals(false, widget.isDisposed());
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

	widget.removeListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
		}
	});
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
		}
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
    for (int i = 0; i < types.length; i++) {
        w.addListener(types[i], listener);
    }
}

protected String[] hookExpectedEvents(String type, final java.util.Vector<String> events) {
    return hookExpectedEvents(widget, type, events);
}

protected String[] hookExpectedEvents(Widget w, String type, final java.util.Vector<String> events) {
    String[] expectedEvents = ConsistencyUtility.eventOrdering.get(type);
    hookExpectedEvents(w, expectedEvents, events);
    return expectedEvents;
}

protected void hookExpectedEvents(Widget w, String[] types, final java.util.Vector<String> events) {
    hookListeners(w, ConsistencyUtility.convertEventNames(types), 
	        new Listener() {
	            public void handleEvent(Event e) {
	                String temp = ConsistencyUtility.eventNames[e.type];
	                if(e.type == SWT.Traverse)
	                    temp += ":"+ConsistencyUtility.getTraversalType(e.detail);
	                else if(e.type == SWT.Selection)
	                    temp += ":"+ConsistencyUtility.getSelectionType(e.detail);
                    events.add(temp);
	                System.out.println(temp + e.widget);
	            }
	        });
}

protected String getTestName() {
    String test = getName();
    int index = test.lastIndexOf('_');
    if(index != -1)
        test = test.substring(index+1, test.length());
    String clss = getClassName();
    if((!test.equals("MenuDetect") || clss.equals("Table") || clss.equals("TableTree") || test.startsWith("Chevron")) &&
       (!test.equals("DragDetect") || clss.equals("Tree") || clss.equals("TableTree") || test.startsWith("Chevron")) &&
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
        clazz = clazz.substring(index+1, clazz.length());
    return clazz;
}



}
