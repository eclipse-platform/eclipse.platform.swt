package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Display
 *
 * @see org.eclipse.swt.widgets.Display
 */
public class Test_org_eclipse_swt_widgets_Display extends Test_org_eclipse_swt_graphics_Device {

boolean disposeExecRan;

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

public void test_asyncExecLjava_lang_Runnable() {
	warnUnimpl("Test test_asyncExecLjava_lang_Runnable not written");
}

public void test_beep() {
	Display display = new Display();
	try {
		display.beep();
	} finally {
		display.dispose();
	}
}

public void test_checkSubclass() {
	warnUnimpl("Test test_checkSubclass not written");
}

public void test_checkDevice() {
	warnUnimpl("Test test_checkDevice not written");
}

public void test_createLorg_eclipse_swt_graphics_DeviceData() {
	warnUnimpl("Test test_createLorg_eclipse_swt_graphics_DeviceData not written");
}

public void test_destroy() {
	warnUnimpl("Test test_destroy not written");
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

public void test_findWidgetI() {
	warnUnimpl("Test test_findWidgetI not written");
}

public void test_findDisplayLjava_lang_Thread() {
	warnUnimpl("Test test_findDisplayLjava_lang_Thread not written");
}

public void test_getActiveShell() {
	warnUnimpl("Test test_getActiveShell not written");
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

public void test_getCurrent() {
	warnUnimpl("Test test_getCurrent not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
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
	warnUnimpl("Test test_getCursorLocation not written");
}

public void test_getDefault() {
	Display display = new Display();
	try {
		assertNotNull(display.getDefault());
	} finally {
		display.dispose();
	}
}

public void test_getDataLjava_lang_String() {
	warnUnimpl("Test test_getDataLjava_lang_String not written");
}

public void test_getData() {
	warnUnimpl("Test test_getData not written");
}

public void test_getDoubleClickTime() {
	warnUnimpl("Test test_getDoubleClickTime not written");
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

public void test_getShells() {
	warnUnimpl("Test test_getShells not written");
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
	warnUnimpl("Test test_getSystemFont not written");
}

public void test_getThread() {
	warnUnimpl("Test test_getThread not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_init() {
	warnUnimpl("Test test_init not written");
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_readAndDispatch() {
	warnUnimpl("Test test_readAndDispatch not written");
}

public void test_release() {
	warnUnimpl("Test test_release not written");
}

public void test_setDataLjava_lang_StringLjava_lang_Object() {
	warnUnimpl("Test test_setDataLjava_lang_StringLjava_lang_Object not written");
}

public void test_setDataLjava_lang_Object() {
	warnUnimpl("Test test_setDataLjava_lang_Object not written");
}

public void test_setAppNameLjava_lang_String() {
	warnUnimpl("Test test_setAppNameLjava_lang_String not written");
}

public void test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer() {
	warnUnimpl("Test test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer not written");
}

public void test_sleep() {
	warnUnimpl("Test test_sleep not written");
}

public void test_syncExecLjava_lang_Runnable() {
	warnUnimpl("Test test_syncExecLjava_lang_Runnable not written");
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
	methodNames.addElement("test_asyncExecLjava_lang_Runnable");
	methodNames.addElement("test_beep");
	methodNames.addElement("test_checkSubclass");
	methodNames.addElement("test_checkDevice");
	methodNames.addElement("test_createLorg_eclipse_swt_graphics_DeviceData");
	methodNames.addElement("test_destroy");
	methodNames.addElement("test_disposeExecLjava_lang_Runnable");
	methodNames.addElement("test_findWidgetI");
	methodNames.addElement("test_findDisplayLjava_lang_Thread");
	methodNames.addElement("test_getActiveShell");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getCurrent");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getCursorControl");
	methodNames.addElement("test_getCursorLocation");
	methodNames.addElement("test_getDefault");
	methodNames.addElement("test_getDataLjava_lang_String");
	methodNames.addElement("test_getData");
	methodNames.addElement("test_getDoubleClickTime");
	methodNames.addElement("test_getFocusControl");
	methodNames.addElement("test_getIconDepth");
	methodNames.addElement("test_getShells");
	methodNames.addElement("test_getSyncThread");
	methodNames.addElement("test_getSystemColorI");
	methodNames.addElement("test_getSystemFont");
	methodNames.addElement("test_getThread");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_init");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_readAndDispatch");
	methodNames.addElement("test_release");
	methodNames.addElement("test_setDataLjava_lang_StringLjava_lang_Object");
	methodNames.addElement("test_setDataLjava_lang_Object");
	methodNames.addElement("test_setAppNameLjava_lang_String");
	methodNames.addElement("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer");
	methodNames.addElement("test_sleep");
	methodNames.addElement("test_syncExecLjava_lang_Runnable");
	methodNames.addElement("test_timerExecILjava_lang_Runnable");
	methodNames.addElement("test_update");
	methodNames.addElement("test_wake");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_asyncExecLjava_lang_Runnable")) test_asyncExecLjava_lang_Runnable();
	else if (getName().equals("test_beep")) test_beep();
	else if (getName().equals("test_checkSubclass")) test_checkSubclass();
	else if (getName().equals("test_checkDevice")) test_checkDevice();
	else if (getName().equals("test_createLorg_eclipse_swt_graphics_DeviceData")) test_createLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_destroy")) test_destroy();
	else if (getName().equals("test_disposeExecLjava_lang_Runnable")) test_disposeExecLjava_lang_Runnable();
	else if (getName().equals("test_findWidgetI")) test_findWidgetI();
	else if (getName().equals("test_findDisplayLjava_lang_Thread")) test_findDisplayLjava_lang_Thread();
	else if (getName().equals("test_getActiveShell")) test_getActiveShell();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getCurrent")) test_getCurrent();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getCursorControl")) test_getCursorControl();
	else if (getName().equals("test_getCursorLocation")) test_getCursorLocation();
	else if (getName().equals("test_getDefault")) test_getDefault();
	else if (getName().equals("test_getDataLjava_lang_String")) test_getDataLjava_lang_String();
	else if (getName().equals("test_getData")) test_getData();
	else if (getName().equals("test_getDoubleClickTime")) test_getDoubleClickTime();
	else if (getName().equals("test_getFocusControl")) test_getFocusControl();
	else if (getName().equals("test_getIconDepth")) test_getIconDepth();
	else if (getName().equals("test_getShells")) test_getShells();
	else if (getName().equals("test_getSyncThread")) test_getSyncThread();
	else if (getName().equals("test_getSystemColorI")) test_getSystemColorI();
	else if (getName().equals("test_getSystemFont")) test_getSystemFont();
	else if (getName().equals("test_getThread")) test_getThread();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_init")) test_init();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_readAndDispatch")) test_readAndDispatch();
	else if (getName().equals("test_release")) test_release();
	else if (getName().equals("test_setDataLjava_lang_StringLjava_lang_Object")) test_setDataLjava_lang_StringLjava_lang_Object();
	else if (getName().equals("test_setDataLjava_lang_Object")) test_setDataLjava_lang_Object();
	else if (getName().equals("test_setAppNameLjava_lang_String")) test_setAppNameLjava_lang_String();
	else if (getName().equals("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer")) test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer();
	else if (getName().equals("test_sleep")) test_sleep();
	else if (getName().equals("test_syncExecLjava_lang_Runnable")) test_syncExecLjava_lang_Runnable();
	else if (getName().equals("test_timerExecILjava_lang_Runnable")) test_timerExecILjava_lang_Runnable();
	else if (getName().equals("test_update")) test_update();
	else if (getName().equals("test_wake")) test_wake();
}
}
