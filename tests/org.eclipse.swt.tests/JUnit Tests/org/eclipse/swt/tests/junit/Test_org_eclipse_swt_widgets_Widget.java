/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Widget
 *
 * @see org.eclipse.swt.widgets.Widget
 */
public class Test_org_eclipse_swt_widgets_Widget extends SwtTestCase {
	// Use this variable to help validate callbacks
	boolean listenerCalled;

public Test_org_eclipse_swt_widgets_Widget(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	shell = new Shell();
}

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
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_WidgetI() {
	// abstract class
}

public void test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener() {
	DisposeListener listener = new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
		}
	};
	widget.addDisposeListener(listener);
	widget.removeDisposeListener(listener);
}

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

public void test_dispose() {
	// tested in tearDown
}

public void test_getData() {
	// tested in test_setDataLjava_lang_Object
}

public void test_getDataLjava_lang_String() {
	// tested in test_setDataLjava_lang_StringLjava_lang_Object
}

public void test_getDisplay() {
	assertEquals(widget.getDisplay(), widget.getDisplay());
}

public void test_getStyle() {
	// this test should be overridden by leaf subclasses
}

public void test_isDisposed() {
	assertEquals(false, widget.isDisposed());
}

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

public void test_removeDisposeListenerLorg_eclipse_swt_events_DisposeListener() {
	// tested in test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener
}

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

public void test_setDataLjava_lang_Object() {
	widget.setData(widget);
	assertEquals(widget, widget.getData());

	widget.setData(null);
	assertNull(widget.getData());
}

public void test_setDataLjava_lang_StringLjava_lang_Object() {
	widget.setData("the widget", widget);
	assertEquals(widget, widget.getData("the widget"));

	widget.setData("the widget", null);
	assertNull(widget.getData("the widget"));
}

public void test_toString() {
	assertNotNull(widget.toString());
	assertTrue(widget.toString().length() > 0);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Widget((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_WidgetI");
	methodNames.addElement("test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener");
	methodNames.addElement("test_addListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getData");
	methodNames.addElement("test_getDataLjava_lang_String");
	methodNames.addElement("test_getDisplay");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_notifyListenersILorg_eclipse_swt_widgets_Event");
	methodNames.addElement("test_removeDisposeListenerLorg_eclipse_swt_events_DisposeListener");
	methodNames.addElement("test_removeListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_setDataLjava_lang_Object");
	methodNames.addElement("test_setDataLjava_lang_StringLjava_lang_Object");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_WidgetI")) test_ConstructorLorg_eclipse_swt_widgets_WidgetI();
	else if (getName().equals("test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener")) test_addDisposeListenerLorg_eclipse_swt_events_DisposeListener();
	else if (getName().equals("test_addListenerILorg_eclipse_swt_widgets_Listener")) test_addListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getData")) test_getData();
	else if (getName().equals("test_getDataLjava_lang_String")) test_getDataLjava_lang_String();
	else if (getName().equals("test_getDisplay")) test_getDisplay();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_notifyListenersILorg_eclipse_swt_widgets_Event")) test_notifyListenersILorg_eclipse_swt_widgets_Event();
	else if (getName().equals("test_removeDisposeListenerLorg_eclipse_swt_events_DisposeListener")) test_removeDisposeListenerLorg_eclipse_swt_events_DisposeListener();
	else if (getName().equals("test_removeListenerILorg_eclipse_swt_widgets_Listener")) test_removeListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_setDataLjava_lang_Object")) test_setDataLjava_lang_Object();
	else if (getName().equals("test_setDataLjava_lang_StringLjava_lang_Object")) test_setDataLjava_lang_StringLjava_lang_Object();
	else if (getName().equals("test_toString")) test_toString();
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

protected String[] hookExpectedEvents(String type, final java.util.Vector events) {
    return hookExpectedEvents(widget, type, events);
}

protected String[] hookExpectedEvents(Widget w, String type, final java.util.Vector events) {
    String[] expectedEvents = (String[])ConsistencyUtility.eventOrdering.get(type);
    hookExpectedEvents(w, expectedEvents, events);
    return expectedEvents;
}

protected void hookExpectedEvents(Widget w, String[] types, final java.util.Vector events) {
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
