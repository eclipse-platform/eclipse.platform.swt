/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Display
 *
 * @see org.eclipse.swt.widgets.Display
 */
public class Test_org_eclipse_swt_widgets_Display extends Test_org_eclipse_swt_graphics_Device {

public Test_org_eclipse_swt_widgets_Display(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	// There can only be one Display object per thread.
	// If a second Display is created on the same thread, an 
	// InvalidThreadAccessException is thrown.
	//
	// Each test will create its own Display and must dispose of it 
	// before completing.
}

protected void tearDown() {
}

public void test_Constructor() {
	Display disp = new Display();
	disp.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceData() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceData not written");
}

public void test_addFilterILorg_eclipse_swt_widgets_Listener() {
	warnUnimpl("Test test_addFilterILorg_eclipse_swt_widgets_Listener not written");
}

public void test_addListenerILorg_eclipse_swt_widgets_Listener() {
	warnUnimpl("Test test_addListenerILorg_eclipse_swt_widgets_Listener not written");
}

public void test_asyncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.asyncExec(new Runnable() {
			public void run() {
				display.beep();
			}
		});
	} finally {
		display.dispose();
	}
}

public void test_beep() {
	Display display = new Display();
	try {
		display.beep();
	} finally {
		display.dispose();
	}
}

public void test_close() {
	warnUnimpl("Test test_close not written");
}

public void test_disposeExecLjava_lang_Runnable() {
	// Also tests dispose and isDisposed
	Display testDisplay = new Display();
	disposeExecRan = false;
	testDisplay.disposeExec(new Runnable() {
		public void run() {
			disposeExecRan = true;
		}
	});
	assertEquals("Display should not be disposed", false, testDisplay.isDisposed());
	testDisplay.dispose();
	assertTrue("Display should be disposed", testDisplay.isDisposed());
	assertTrue("DisposeExec Runnable did not run", disposeExecRan);
}

public void test_findDisplayLjava_lang_Thread() {
	warnUnimpl("Test test_findDisplayLjava_lang_Thread not written");
}

public void test_findWidgetI() {
	warnUnimpl("Test test_findWidgetI not written");
}

public void test_getActiveShell() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display);
		shell.open();
		assertTrue(display.getActiveShell() == shell);
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_getBounds() {
	Display display = new Display();
	try {
		Rectangle rect = display.getBounds();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

public void test_getClientArea() {
	Display display = new Display();
	try {
		Rectangle rect = display.getClientArea();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

public void test_getCurrent() {
	Display display = new Display();
	try {
		assertTrue(display.getThread() == Thread.currentThread());
	} finally {
		display.dispose();
	}
}

public void test_getCursorControl() {
	Display display = new Display();
	try {
		display.getCursorControl();
	} finally {
		display.dispose();
	}
}

public void test_getCursorLocation() {
	Display display = new Display();
	try {
		Point pt = display.getCursorLocation();
		assertNotNull(pt);
		assertTrue(pt.x >= 0);
		assertTrue(pt.y >= 0);
	} finally {
		display.dispose();
	}
}

public void test_getCursorSize() {
	warnUnimpl("Test test_getCursorSize not written");
}

public void test_getData() {
	// tested in setData(Object) method
}

public void test_getDataLjava_lang_String() {
	// tested in setData(String, Object) method
}

public void test_getDefault() {
	Display display = new Display();
	try {
		assertNotNull(Display.getDefault());
	} finally {
		display.dispose();
	}
}

public void test_getDismissalAlignment() {
	Display display = new Display();
	try {
		int alignment = display.getDismissalAlignment();
		assertTrue("getDismissalAlignment should return SWT.LEFT or SWT.RIGHT",
			alignment == SWT.LEFT || alignment == SWT.RIGHT);
	} finally {
		display.dispose();
	}
}

public void test_getDoubleClickTime() {
	Display display = new Display();
	try {
		int time = display.getDoubleClickTime();
		assertTrue(time > 0);
	} finally {
		display.dispose();
	}
}

public void test_getFocusControl() {
	Display display = new Display();
	try {
		display.getFocusControl();
	} finally {
		display.dispose();
	}
}

public void test_getIconDepth() {
	warnUnimpl("Test test_getIconDepth not written");
}

public void test_getMonitors() {
	Display display = new Display();
	Monitor[] monitors = display.getMonitors();
	assertNotNull(monitors);
	assertTrue("at least one monitor should be returned", monitors.length >= 1);
	for (int i = 0; i < monitors.length; i++)
		assertTrue("monitor at index "+i+" should not be null", monitors[i] != null);
	display.dispose();
}

public void test_getPrimaryMonitor() {
	Display display = new Display();
	Monitor monitor = display.getPrimaryMonitor();
	assertNotNull(monitor);
	display.dispose();
}

public void test_getShells() {
	Display display = new Display();
	try {
		Shell shell1 = new Shell(display);
		Shell shell2 = new Shell(display);
		assertTrue(display.getShells().length == 2);
		shell1.dispose();
		shell2.dispose();
	} finally {
		display.dispose();
	}
}

public void test_getSyncThread() {
	warnUnimpl("Test test_getSyncThread not written");
}

public void test_getSystemColorI() {
	Display display = new Display();
	try {
		int [] colorIds = {
			SWT.COLOR_WIDGET_DARK_SHADOW, SWT.COLOR_WIDGET_NORMAL_SHADOW,
			SWT.COLOR_WIDGET_LIGHT_SHADOW, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW,
			SWT.COLOR_WIDGET_BACKGROUND, SWT.COLOR_WIDGET_BORDER,
			SWT.COLOR_WIDGET_FOREGROUND, SWT.COLOR_LIST_FOREGROUND,
			SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_SELECTION,
			SWT.COLOR_LIST_SELECTION_TEXT,
		};
		for (int i=0; i < colorIds.length; i++) {
			assertNotNull(display.getSystemColor(colorIds[i]));
		}
	} finally {
		display.dispose();
	}
}

public void test_getSystemFont() {
	Display display = new Display();
	try {
		Font font = display.getSystemFont();
		assertNotNull(font);
	} finally {
		display.dispose();
	}
}

public void test_getThread() {
	Display display = new Display();
	try {
		assertTrue(display.getThread() == Thread.currentThread());
	} finally {
		display.dispose();
	}
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII() {
	warnUnimpl("Test test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII not written");
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII() {
	warnUnimpl("Test test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII not written");
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point not written");
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_postLorg_eclipse_swt_widgets_Event() {
	warnUnimpl("Test test_postLorg_eclipse_swt_widgets_Event not written");
}

public void test_readAndDispatch() {
	warnUnimpl("Test test_readAndDispatch not written");
}

public void test_removeFilterILorg_eclipse_swt_widgets_Listener() {
	warnUnimpl("Test test_removeFilterILorg_eclipse_swt_widgets_Listener not written");
}

public void test_removeListenerILorg_eclipse_swt_widgets_Listener() {
	warnUnimpl("Test test_removeListenerILorg_eclipse_swt_widgets_Listener not written");
}

public void test_release() {
	// Overriding test_release from Device.
}

public void test_setAppNameLjava_lang_String() {
	warnUnimpl("Test test_setAppNameLjava_lang_String not written");
}

public void test_setCursorLocationII() {
	Display display = new Display();
	try {
		display.setCursorLocation(0,0);
	} finally {
		display.dispose();
	}
}

public void test_setCursorLocationLorg_eclipse_swt_graphics_Point() {
	Display display = new Display();
	try {
		display.setCursorLocation(new Point(0,0));
		try {
			display.setCursorLocation(null);
			fail("No exception thrown for null argument");
		}
		catch (IllegalArgumentException e) {
		}
	} finally {
		display.dispose();
	}
}

public void test_setDataLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData(new Integer(10));
		Integer i = (Integer)display.getData();
		assertNotNull(i);
		assertTrue(i.equals(new Integer(10)));
	} finally {
		display.dispose();
	}
}

public void test_setDataLjava_lang_StringLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData("Integer", new Integer(10));
		display.setData("String", "xyz");
		Integer i = (Integer)display.getData("Integer");
		assertNotNull(i);
		assertTrue(i.equals(new Integer(10)));
		String s = (String)display.getData("String");
		assertNotNull(s);
		assertTrue(s.equals("xyz"));
	} finally {
		display.dispose();
	}
}

public void test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer() {
	warnUnimpl("Test test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer not written");
}

public void test_sleep() {
	warnUnimpl("Test test_sleep not written");
}

public void test_syncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.syncExec(new Runnable() {
			public void run() {
				display.beep();
			}
		});
	} finally {
		display.dispose();
	}
}

public void test_timerExecILjava_lang_Runnable() {
	warnUnimpl("Test test_timerExecILjava_lang_Runnable not written");
}

public void test_update() {
	warnUnimpl("Test test_update not written");
}

public void test_wake() {
	warnUnimpl("Test test_wake not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Display((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceData");
	methodNames.addElement("test_addFilterILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_addListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_asyncExecLjava_lang_Runnable");
	methodNames.addElement("test_beep");
	methodNames.addElement("test_close");
	methodNames.addElement("test_disposeExecLjava_lang_Runnable");
	methodNames.addElement("test_findDisplayLjava_lang_Thread");
	methodNames.addElement("test_findWidgetI");
	methodNames.addElement("test_getActiveShell");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getCurrent");
	methodNames.addElement("test_getCursorControl");
	methodNames.addElement("test_getCursorLocation");
	methodNames.addElement("test_getCursorSize");
	methodNames.addElement("test_getData");
	methodNames.addElement("test_getDataLjava_lang_String");
	methodNames.addElement("test_getDefault");
	methodNames.addElement("test_getDismissalAlignment");
	methodNames.addElement("test_getDoubleClickTime");
	methodNames.addElement("test_getFocusControl");
	methodNames.addElement("test_getIconDepth");
	methodNames.addElement("test_getMonitors");
	methodNames.addElement("test_getPrimaryMonitor");
	methodNames.addElement("test_getShells");
	methodNames.addElement("test_getSyncThread");
	methodNames.addElement("test_getSystemColorI");
	methodNames.addElement("test_getSystemFont");
	methodNames.addElement("test_getThread");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_postLorg_eclipse_swt_widgets_Event");
	methodNames.addElement("test_readAndDispatch");
	methodNames.addElement("test_removeFilterILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_removeListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_setAppNameLjava_lang_String");
	methodNames.addElement("test_setCursorLocationII");
	methodNames.addElement("test_setCursorLocationLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setDataLjava_lang_Object");
	methodNames.addElement("test_setDataLjava_lang_StringLjava_lang_Object");
	methodNames.addElement("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer");
	methodNames.addElement("test_sleep");
	methodNames.addElement("test_syncExecLjava_lang_Runnable");
	methodNames.addElement("test_timerExecILjava_lang_Runnable");
	methodNames.addElement("test_update");
	methodNames.addElement("test_wake");
	methodNames.addAll(Test_org_eclipse_swt_graphics_Device.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_addFilterILorg_eclipse_swt_widgets_Listener")) test_addFilterILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_addListenerILorg_eclipse_swt_widgets_Listener")) test_addListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_asyncExecLjava_lang_Runnable")) test_asyncExecLjava_lang_Runnable();
	else if (getName().equals("test_beep")) test_beep();
	else if (getName().equals("test_close")) test_close();
	else if (getName().equals("test_disposeExecLjava_lang_Runnable")) test_disposeExecLjava_lang_Runnable();
	else if (getName().equals("test_findDisplayLjava_lang_Thread")) test_findDisplayLjava_lang_Thread();
	else if (getName().equals("test_findWidgetI")) test_findWidgetI();
	else if (getName().equals("test_getActiveShell")) test_getActiveShell();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getCurrent")) test_getCurrent();
	else if (getName().equals("test_getCursorControl")) test_getCursorControl();
	else if (getName().equals("test_getCursorLocation")) test_getCursorLocation();
	else if (getName().equals("test_getCursorSize")) test_getCursorSize();
	else if (getName().equals("test_getData")) test_getData();
	else if (getName().equals("test_getDataLjava_lang_String")) test_getDataLjava_lang_String();
	else if (getName().equals("test_getDefault")) test_getDefault();
	else if (getName().equals("test_getDismissalAlignment")) test_getDismissalAlignment();
	else if (getName().equals("test_getDoubleClickTime")) test_getDoubleClickTime();
	else if (getName().equals("test_getFocusControl")) test_getFocusControl();
	else if (getName().equals("test_getIconDepth")) test_getIconDepth();
	else if (getName().equals("test_getMonitors")) test_getMonitors();
	else if (getName().equals("test_getPrimaryMonitor")) test_getPrimaryMonitor();
	else if (getName().equals("test_getShells")) test_getShells();
	else if (getName().equals("test_getSyncThread")) test_getSyncThread();
	else if (getName().equals("test_getSystemColorI")) test_getSystemColorI();
	else if (getName().equals("test_getSystemFont")) test_getSystemFont();
	else if (getName().equals("test_getThread")) test_getThread();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_postLorg_eclipse_swt_widgets_Event")) test_postLorg_eclipse_swt_widgets_Event();
	else if (getName().equals("test_readAndDispatch")) test_readAndDispatch();
	else if (getName().equals("test_removeFilterILorg_eclipse_swt_widgets_Listener")) test_removeFilterILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_removeListenerILorg_eclipse_swt_widgets_Listener")) test_removeListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_setAppNameLjava_lang_String")) test_setAppNameLjava_lang_String();
	else if (getName().equals("test_setCursorLocationII")) test_setCursorLocationII();
	else if (getName().equals("test_setCursorLocationLorg_eclipse_swt_graphics_Point")) test_setCursorLocationLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setDataLjava_lang_Object")) test_setDataLjava_lang_Object();
	else if (getName().equals("test_setDataLjava_lang_StringLjava_lang_Object")) test_setDataLjava_lang_StringLjava_lang_Object();
	else if (getName().equals("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer")) test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer();
	else if (getName().equals("test_sleep")) test_sleep();
	else if (getName().equals("test_syncExecLjava_lang_Runnable")) test_syncExecLjava_lang_Runnable();
	else if (getName().equals("test_timerExecILjava_lang_Runnable")) test_timerExecILjava_lang_Runnable();
	else if (getName().equals("test_update")) test_update();
	else if (getName().equals("test_wake")) test_wake();
	else super.runTest();
}

/* custom */
boolean disposeExecRan;
}
