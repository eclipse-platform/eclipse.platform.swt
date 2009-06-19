/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.Browser
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class Test_org_eclipse_swt_browser_Browser extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_browser_Browser(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	shell.setLayout(new FillLayout());
	browser = new Browser(shell, SWT.NONE);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	shell.setText("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	Browser browser = new Browser(shell, SWT.NONE);
	browser.dispose();
	browser = new Browser(shell, SWT.BORDER);
	browser.dispose();
	try {
		browser = new Browser(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}	catch (IllegalArgumentException e) {
	}
}

public void test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener() {
	shell.setText("test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener");
	try {
		browser.addCloseWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	CloseWindowListener listener = new CloseWindowListener() {
		public void close(WindowEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addCloseWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeCloseWindowListener(listener);
}

public void test_addLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	shell.setText("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener");
	try {
		browser.addLocationListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	LocationListener listener = new LocationListener() {
		public void changed(LocationEvent event) {
		}
		public void changing(LocationEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addLocationListener(listener);
	for (int i = 0; i < 100; i++) browser.removeLocationListener(listener);
}

public void test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener() {
	shell.setText("test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener");
	try {
		browser.addOpenWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	OpenWindowListener listener = new OpenWindowListener() {
		public void open(WindowEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addOpenWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeOpenWindowListener(listener);
}

public void test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	shell.setText("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	try {
		browser.addProgressListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	ProgressListener listener = new ProgressListener() {
		public void changed(ProgressEvent event) {
		}
		public void completed(ProgressEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addProgressListener(listener);
	for (int i = 0; i < 100; i++) browser.removeProgressListener(listener);
}

public void test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	shell.setText("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	try {
		browser.addStatusTextListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	StatusTextListener listener = new StatusTextListener() {
		public void changed(StatusTextEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addStatusTextListener(listener);
	for (int i = 0; i < 100; i++) browser.removeStatusTextListener(listener);
}

public void test_addTitleListenerLorg_eclipse_swt_browser_TitleListener() {
	shell.setText("test_addTitleLorg_eclipse_swt_browser_TitleListener");
	try {
		browser.addTitleListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	TitleListener listener = new TitleListener() {
		public void changed(TitleEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addTitleListener(listener);
	for (int i = 0; i < 100; i++) browser.removeTitleListener(listener);
}

public void test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener() {
	shell.setText("test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener");
	try {
		browser.addVisibilityWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	VisibilityWindowListener listener = new VisibilityWindowListener() {
		public void hide(WindowEvent event) {
		}
		public void show(WindowEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addVisibilityWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeVisibilityWindowListener(listener);
}

public void test_back() {
	shell.setText("test_back");
	for (int i = 0; i < 10; i++) {
		browser.back();
		runLoopTimer(1);
	}
	/* returning 10 times in history - expecting false is returned */
	boolean result = browser.back();
	assertFalse(result);
}

public void test_executeLjava_lang_String() {
	shell.setText("test_executeLjava_lang_String");
	try {
		browser.execute(null);
		fail("No exception thrown for script == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	/* Real testing is done in the tests that run the event loop
	 * since a document must have been loaded to execute a script on it.
	 */
}

public void test_forward() {
	shell.setText("test_forward");
	for (int i = 0; i < 10; i++) {
		browser.forward();
		runLoopTimer(1);
	}
	/* going forward 10 times in history - expecting false is returned */
	boolean result = browser.forward();
	assertFalse(result);
}

public void test_getUrl() {
	shell.setText("test_getUrl");
	String string = browser.getUrl();
	assertTrue(string != null);
}

public void test_isBackEnabled() {
	shell.setText("test_isBackEnabled");
	
	/* back should return the same value that isBackEnabled previously returned */
	assertEquals(browser.isBackEnabled(), browser.back());
	
	for (int i = 0; i < 10; i++) {
		browser.back();
		runLoopTimer(1);
	}
	/* going back 10 times in history - expecting false is returned */
	boolean result = browser.isBackEnabled();
	assertFalse(result);
}

public void test_isForwardEnabled() {
	shell.setText("test_isForwardEnabled");
	
	/* forward should return the same value that isForwardEnabled previously returned */
	assertEquals(browser.isForwardEnabled(), browser.forward());
	
	for (int i = 0; i < 10; i++) {
		browser.forward();
		runLoopTimer(1);
	}
	/* going forward 10 times in history - expecting false is returned */
	boolean result = browser.isForwardEnabled();
	assertFalse(result);
}

public void test_refresh() {
	shell.setText("test_refresh");
	for (int i = 0; i < 10; i++) {
		browser.refresh();
		runLoopTimer(1);
	}
}

public void test_removeCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener() {
	try {
		browser.removeCloseWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addCloseWindowListener
}

public void test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	try {
		browser.removeLocationListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addLocationListener
}

public void test_removeOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener() {
	try {
		browser.removeOpenWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addOpenWindowListener
}

public void test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	try {
		browser.removeProgressListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addProgressListener
}

public void test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	try {
		browser.removeStatusTextListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addStatusTextListener
}

public void test_removeTitleListenerLorg_eclipse_swt_browser_TitleListener() {
	try {
		browser.removeTitleListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addTitleListener
}

public void test_removeVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener() {
	try {
		browser.removeVisibilityWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addVisibilityWindowListener
}

public void test_setTextLjava_lang_String() {
	shell.setText("test_setTextLjava_lang_String");
	
	String html = "<HTML><HEAD><TITLE>HTML example 2</TITLE></HEAD><BODY><H1>HTML example 2</H1>";
	for (int i = 0; i < 1000; i++) {
		html +="<P>That is a test line with the number "+i+"</P>";
	}
	html += "</BODY></HTML>";
	boolean result = browser.setText(html);
	assertTrue(result);
	runLoopTimer(10);
	
	try {
		browser.setText(null);
		fail("No exception thrown for text == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setUrlLjava_lang_String() {
	shell.setText("test_setUrlLjava_lang_String");
	try {
		browser.setUrl(null);
		fail("No exception thrown for url == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
	runLoopTimer(10);
}

public void test_stop() {
	shell.setText("test_stop");
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
	runLoopTimer(1);
	browser.stop();
	runLoopTimer(10);
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_browser_Browser((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener");
	methodNames.addElement("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener");
	methodNames.addElement("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_addTitleListenerLorg_eclipse_swt_browser_TitleListener");
	methodNames.addElement("test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener");
	methodNames.addElement("test_back");
	methodNames.addElement("test_executeLjava_lang_String");
	methodNames.addElement("test_forward");
	methodNames.addElement("test_getUrl");
	methodNames.addElement("test_isBackEnabled");
	methodNames.addElement("test_isForwardEnabled");
	methodNames.addElement("test_refresh");
	methodNames.addElement("test_removeCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener");
	methodNames.addElement("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_removeOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener");
	methodNames.addElement("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_removeTitleListenerLorg_eclipse_swt_browser_TitleListener");
	methodNames.addElement("test_removeVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setUrlLjava_lang_String");
	methodNames.addElement("test_stop");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener")) test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener();
	else if (getName().equals("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_addLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener")) test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener();
	else if (getName().equals("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_addTitleListenerLorg_eclipse_swt_browser_TitleListener")) test_addTitleListenerLorg_eclipse_swt_browser_TitleListener();
	else if (getName().equals("test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener")) test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener();
	else if (getName().equals("test_back")) test_back();
	else if (getName().equals("test_executeLjava_lang_String")) test_executeLjava_lang_String();
	else if (getName().equals("test_forward")) test_forward();
	else if (getName().equals("test_getUrl")) test_getUrl();
	else if (getName().equals("test_isBackEnabled")) test_isBackEnabled();
	else if (getName().equals("test_isForwardEnabled")) test_isForwardEnabled();
	else if (getName().equals("test_refresh")) test_refresh();
	else if (getName().equals("test_removeCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener")) test_removeCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener();
	else if (getName().equals("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_removeOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener")) test_removeOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener();
	else if (getName().equals("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_removeTitleListenerLorg_eclipse_swt_browser_TitleListener")) test_removeTitleListenerLorg_eclipse_swt_browser_TitleListener();
	else if (getName().equals("test_removeVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener")) test_removeVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setUrlLjava_lang_String")) test_setUrlLjava_lang_String();
	else if (getName().equals("test_stop")) test_stop();
	else super.runTest();
}

/* custom */
Browser browser;

void runLoopTimer(final int seconds) {
	final boolean[] exit = {false};
	new Thread() {
		public void run() {
			try {Thread.sleep(seconds * 1000);} catch (Exception e) {}
			exit[0] = true;
			/* wake up the event loop */
			Display display = Display.getDefault();
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						if (!shell.isDisposed()) shell.redraw();						
					}
				});
			}
		}
	}.start();
	shell.open();
	Display display = Display.getCurrent();
	while (!exit[0] && !shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
}
}
