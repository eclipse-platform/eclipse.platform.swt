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
import org.eclipse.swt.browser.*;

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
	browser = new Browser(shell, SWT.NONE);
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
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

public void test_addLocationListenerLorg_eclipse_swt_browser_LocationListener() {
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

public void test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
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

public void test_back() {
	for (int i = 0; i < 50; i++) {
		browser.back();
	}
	/* returning 50 times in history - expecting false is returned */
	boolean result = browser.back();
	assertFalse(result);
}

public void test_forward() {
	for (int i = 0; i < 50; i++) {
		browser.forward();
	}
	/* going forward 50 times in history - expecting false is returned */
	boolean result = browser.forward();
	assertFalse(result);
}

public void test_getUrl() {
	String string = browser.getUrl();
	assertTrue(string != null);
}

public void test_refresh() {
	for (int i = 0; i < 50; i++) browser.refresh();
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

public void test_setUrlLjava_lang_String() {
	try {
		browser.setUrl(null);
		fail("No exception thrown for url == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
}

public void test_stop() {
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
	browser.stop();
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
	methodNames.addElement("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_back");
	methodNames.addElement("test_forward");
	methodNames.addElement("test_getUrl");
	methodNames.addElement("test_refresh");
	methodNames.addElement("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_setUrlLjava_lang_String");
	methodNames.addElement("test_stop");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_addLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_back")) test_back();
	else if (getName().equals("test_forward")) test_forward();
	else if (getName().equals("test_getUrl")) test_getUrl();
	else if (getName().equals("test_refresh")) test_refresh();
	else if (getName().equals("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_setUrlLjava_lang_String")) test_setUrlLjava_lang_String();
	else if (getName().equals("test_stop")) test_stop();
	else super.runTest();
}

/* custom */
Browser browser;
}
