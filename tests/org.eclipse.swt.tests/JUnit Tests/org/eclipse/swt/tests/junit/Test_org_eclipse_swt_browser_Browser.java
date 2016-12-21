/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowAdapter;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.Browser
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class Test_org_eclipse_swt_browser_Browser extends Test_org_eclipse_swt_widgets_Composite {

	@Rule
	public TestName name = new TestName();

	Browser browser;

	boolean browser_debug = false;

	boolean isWebkit1 = false;

	/**
	 * Normally, sleep in 1 ms intervals 1000 times. During browser_debug, sleep 1000 ms for 1 interval.
	 * This allows one to see the browser shell for a few seconds, which would normally not be visible during
	 * automated testing.
	 */
	int secondsToWaitTillFail, waitMS, loopMultipier;


@Override
@Before
public void setUp() {
	super.setUp();

	// Print test name if running on hudson. This makes it easier to tell in the logs which test case caused crash.
	if (SwtTestUtil.isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Running Test_org_eclipse_swt_browser_Browser#" + name.getMethodName());
	}

	secondsToWaitTillFail = 3;
	waitMS = browser_debug ? 1000 : 1;
	loopMultipier = browser_debug ? 1 : 1000;

	shell.setLayout(new FillLayout());
	browser = new Browser(shell, SWT.NONE);

	String shellTitle = name.getMethodName();
	if (SwtTestUtil.isGTK && browser.getBrowserType().equals("webkit")) {

		// Note, webkitGtk version is only available once Browser is instantiated.
		String webkitGtkVersionStr = System.getProperty("org.eclipse.swt.internal.webkitgtk.version"); //$NON-NLS-1$

		shellTitle = shellTitle + " Webkit version: " + webkitGtkVersionStr;

		String[] webkitGtkVersionStrParts = webkitGtkVersionStr.split("\\.");
		int[] webkitGtkVersionInts = new int[3];
		for (int i = 0; i < 3; i++) {
			webkitGtkVersionInts[i] = Integer.parseInt(webkitGtkVersionStrParts[i]);
		}

		// webkitgtk 2.5 and onwards uses webkit2.
		if (webkitGtkVersionInts[0] == 1 || (webkitGtkVersionInts[0] == 2 && webkitGtkVersionInts[1] <= 4)) {
			isWebkit1 = true;
		}
	}
	shell.setText(shellTitle);

	/*
	 * setWidget() is needed for Browser to occupy the whole shell.
	 * (Without setWidget(), composite and browser receive half the shell each).
	 *
	 * However, on Win32 & IE, an OleFrame() child is created in Browser, which break the first
	 * assertion in test_getChildren() ":a:: array lengths differed, expected.length=0 actual.length=1"
	 *
	 * See: Bug 499387
	 */
	if (! SwtTestUtil.isWindows) {
		setWidget(browser);
	}

}


/**
 * Test that if Browser is constructed with the parent being "null", Browser throws an exception.
 */
@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	Browser browser = new Browser(shell, SWT.NONE);
	browser.dispose();
	browser = new Browser(shell, SWT.BORDER);
	// System.out.println("Test_org_eclipse_swt_browser_Browser#test_Constructor*#getBrowserType(): " + browser.getBrowserType());
	browser.dispose();
	try {
		browser = new Browser(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}	catch (IllegalArgumentException e) {
	}
}
/**
 * Test that if you invoke the addWindowListener without a listener, a proper IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener() {
	try {
		browser.addCloseWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	CloseWindowListener listener = event -> {
	};
	for (int i = 0; i < 100; i++) browser.addCloseWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeCloseWindowListener(listener);
}

/**
 * Test that addLocationListener() throws a IllegalArgumentException if no arument given.
 */
@Test
public void test_listener_addLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	try {
		browser.addLocationListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	LocationListener listener = new LocationListener() {
		@Override
		public void changed(LocationEvent event) {
		}
		@Override
		public void changing(LocationEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addLocationListener(listener);
	for (int i = 0; i < 100; i++) browser.removeLocationListener(listener);
}

/**
 * Tests that when addOpenWindowListener() is called without an argument, a IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener() {
	try {
		browser.addOpenWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	OpenWindowListener listener = event -> {
	};
	for (int i = 0; i < 100; i++) browser.addOpenWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeOpenWindowListener(listener);
}

/**
 * Test that if addProgressListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	try {
		browser.addProgressListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	ProgressListener listener = new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addProgressListener(listener);
	for (int i = 0; i < 100; i++) browser.removeProgressListener(listener);
}

@Override
@Test
public void test_isVisible() {
	// Note. This test sometimes crashes with webkit1 because shell.setVisible() calls g_main_context_iteration(). See Bug 509411
	// To reproduce, try running test suite 20 times in a loop.
	super.test_isVisible();
}

/**
 * Test that if addStatusTextListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	try {
		browser.addStatusTextListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	StatusTextListener listener = event -> {
	};
	for (int i = 0; i < 100; i++) browser.addStatusTextListener(listener);
	for (int i = 0; i < 100; i++) browser.removeStatusTextListener(listener);
}

/**
 * Test that if addTitleListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addTitleListenerLorg_eclipse_swt_browser_TitleListener() {
	try {
		browser.addTitleListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	TitleListener listener = event -> {
	};
	for (int i = 0; i < 100; i++) browser.addTitleListener(listener);
	for (int i = 0; i < 100; i++) browser.removeTitleListener(listener);
}

/**
 * Test that if addTitleListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener() {
	try {
		browser.addVisibilityWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}

	VisibilityWindowListener listener = new VisibilityWindowListener() {
		@Override
		public void hide(WindowEvent event) {
		}
		@Override
		public void show(WindowEvent event) {
		}
	};
	for (int i = 0; i < 100; i++) browser.addVisibilityWindowListener(listener);
	for (int i = 0; i < 100; i++) browser.removeVisibilityWindowListener(listener);
}

/**
 * Test that if execute() is called with 'null' as argument, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_executeLjava_lang_String() {
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


/**
 * Test that going back in history, when no new pages were visited, returns false.
 */
@Test
public void test_back() {
	for (int i = 0; i < 2; i++) {
		browser.back();
	}
	/* returning 10 times in history - expecting false is returned */
	boolean result = browser.back();
	assertFalse(result);
}

/**
 * Test that if removeLocationListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	try {
		browser.removeLocationListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addLocationListener
}

/**
 * Test that if removeOpenWindowListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener() {
	try {
		browser.removeOpenWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addOpenWindowListener
}

/**
 * Test that if removeProgressListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	try {
		browser.removeProgressListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addProgressListener
}

/**
 * Test that if removeStatusTextListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	try {
		browser.removeStatusTextListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addStatusTextListener
}

/**
 * Test that if removeTitleListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeTitleListenerLorg_eclipse_swt_browser_TitleListener() {
	try {
		browser.removeTitleListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addTitleListener
}

/**
 * Test that if removeVisibilityWindowListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener() {
	try {
		browser.removeVisibilityWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addVisibilityWindowListener
}

/**
 * Test that if removeCloseWindowListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_removeCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener() {
	try {
		browser.removeCloseWindowListener(null);
		fail("No exception thrown for listener == null");
	}
	catch (IllegalArgumentException e) {
	}
	// tested in addCloseWindowListener
}



/**
 * Test that addCloseWindowListener() doesn't throw an error.
 */
@Test
public void test_listener_closeLorg_eclipse_swt_browser_WindowEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addCloseWindowListener(event -> {
	});
	shell.close();
}

/**
 * Test that addLocationListener() doesn't throw an error.
 */
@Test
public void test_listener_changedLorg_eclipse_swt_browser_LocationEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	LocationAdapter adapter = new LocationAdapter() {
	};
	browser.addLocationListener(adapter);
	shell.close();
}


@Test
public void test_listener_openWindowLorg_eclipse_swt_browser_WindowEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addOpenWindowListener(event -> {
	});
	shell.close();
}

@Test
public void test_adapter_ProgressAdaptor() {
	new ProgressAdapter() {};
}

@Test
public void test_listener_changedLorg_eclipse_swt_browser_ProgressEvent_adapter() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addProgressListener(new ProgressAdapter() {});
	shell.close();
}


@Test
public void test_listener_changedLorg_eclipse_swt_browser_ProgressEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
		}
	});
	shell.close();
}

@Test
public void test_listener_changedLorg_eclipse_swt_browser_StatusTextEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addStatusTextListener(event -> {
	});
	shell.close();
}

@Test
public void test_listener_changedLorg_eclipse_swt_browser_TitleEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addTitleListener(event -> {
	});
	shell.close();
}

@Test
public void test_adapter_VisibilityWindowAdapter_() {
	new VisibilityWindowAdapter() {};
}

@Test
public void test_listener_hideLorg_eclipse_swt_browser_WindowEvent_adapter() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addVisibilityWindowListener(new VisibilityWindowAdapter(){});
	shell.close();
}

@Test
public void test_listener_hideLorg_eclipse_swt_browser_WindowEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addVisibilityWindowListener(new VisibilityWindowListener() {
		@Override
		public void hide(WindowEvent event) {
		}
		@Override
		public void show(WindowEvent event) {
		}
	});
	shell.close();
}

/**
 * Test that calling setText() with null as argument leads to IllegalArgumentException.
 */
@Test
public void test_setTextNull() {
	try {
		browser.setText(null);
		fail("No exception thrown for text == null");
	}
	catch (IllegalArgumentException e) {
	}
}

/**
 * Test that setText() without an argument throws a IllegalArgumentException.
 */
@Test
public void test_setUrlWithNullArg() {
	try {
		browser.setUrl(null);
		fail("No exception thrown for url == null");
	}
	catch (IllegalArgumentException e) {
	}
}


/**
 * Test that going forward in history (without having gone back before) returns false.
 */
@Test
public void test_forward() {
	for (int i = 0; i < 2; i++) {
		browser.forward();
	}
	/* going forward 10 times in history - expecting false is returned */
	boolean result = browser.forward();
	assertFalse(result);
}

/**
 * Test that getUrl() returns a non-null string.
 */
@Test
public void test_getUrl() {
	String string = browser.getUrl();
	assertTrue(string != null);
}


/**
 * Test of 'back in history' api.
 * - Test isBackEnabled() and back() return the same value.
 * - Test that going isBackEnabled still returns false if back was called multiple times.
 */
@Test
public void test_isBackEnabled() {

	/* back should return the same value that isBackEnabled previously returned */
	assertEquals(browser.isBackEnabled(), browser.back());

	for (int i = 0; i < 2; i++) {
		browser.back();
	}
	/* going back 10 times in history - expecting false is returned */
	boolean result = browser.isBackEnabled();
	assertFalse(result);
}

/**
 * Test of 'forward in history' api.
 * - Test isForwardEnabled() and forward() return the same value.
 * - Test that going isBackEnabled still returns false if back was called multiple times.
 */
@Test
public void test_isForwardEnabled() {
	/* forward should return the same value that isForwardEnabled previously returned */
	assertEquals(browser.isForwardEnabled(), browser.forward());

	for (int i = 0; i < 10; i++) {
		browser.forward();
	}
	/* going forward 10 times in history - expecting false is returned */
	boolean result = browser.isForwardEnabled();
	assertFalse(result);
}

/**
 * Test that refresh executes without throwing exceptions.
 * (Maybe we should actually load a page first?)
 */
@Test
public void test_refresh() {
	for (int i = 0; i < 2; i++) {
		browser.refresh();
	}
}



/**
 * Test that HTML can be loaded into the browser.
 * Assertion is based on the return value of setText().
 * (A true return value doesn't neccessarily mean the text is actually rendered,
 * You should see a browser page with 'That is a test line...' printed many times.)
 */
@Test
public void test_setTextLjava_lang_String() {
	String html = "<HTML><HEAD><TITLE>HTML example 2</TITLE></HEAD><BODY><H1>HTML example 2</H1>";
	for (int i = 0; i < 1000; i++) {
		html +="<P>That is a test line with the number "+i+"</P>";
	}
	html += "</BODY></HTML>";
	boolean result = browser.setText(html);
	assertTrue(result);
	runLoopTimer(2000);
}

/**
 * Test that setUrl() finishes without throwing an error.
 */
@Test
public void test_setUrl() {
	// Note, this test sometimes crashes on webkit1. See Bug 509411

	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	assert(browser.setUrl("http://www.eclipse.org/swt"));
	runLoopTimer(2000);
	// TODO - it would be good to verify that the page actually loaded. ex download the webpage etc..
}


/**
 * Test that a page load an be stopped (stop()) without throwing an exception.
 */
@Test
public void test_stop() {
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
	runLoopTimer(1000);
	browser.stop();
}

/**
 * Test the evaluate() api that returns a String type. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_string() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	final AtomicReference<String> returnValue = new AtomicReference<>();
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			String evalResult = (String) browser.evaluate("return document.getElementById('myid').childNodes[0].nodeValue;");
			returnValue.set(evalResult);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body><p id='myid'>HelloWorld</p></body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if ("HelloWorld".equals(returnValue.get())) {
			return; // passed.
		}
	}
	fail();
}

/**
 * Test the evaluate() api that returns a number (Double). Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_number_normal() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411
	Double testNum = 123.0;
	boolean passed = evaluate_number_helper(testNum);
	assertTrue(passed);
}

/**
 * Test the evaluate() api that returns a number (Double). Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_number_negative() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	Double testNum = -123.0;
	boolean passed = evaluate_number_helper(testNum);
	assertTrue(passed);
}

/**
 * Test the evaluate() api that returns a number (Double). Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_number_big() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	Double testNum = 10000000000.0;
	boolean passed = evaluate_number_helper(testNum);
	assertTrue(passed);
}

boolean evaluate_number_helper(Double testNum) {
	final AtomicReference<Double> returnValue = new AtomicReference<>();
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Double evalResult = (Double) browser.evaluate("return " +testNum.toString());
			returnValue.set(evalResult);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body>HelloWorld</body></html>");
	shell.open();
	boolean passed = false;
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (testNum.equals(returnValue.get())) {
			passed = true;
			break;
		}
	}
	return passed;
}

/**
 * Test the evaluate() api that returns a boolean. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_boolean() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411
	final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Boolean evalResult = (Boolean) browser.evaluate("return true");
			atomicBoolean.set(evalResult);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body>HelloWorld</body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (atomicBoolean.get()) {
			return; // passed.
		}
	}
	fail();
}

/**
 * Test the evaluate() api that returns null. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_null() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411
	 // Boolen only used as dummy placeholder so the object is not null.
	final AtomicReference<Object> returnValue = new AtomicReference<>(new Boolean(true));
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Object evalResult = browser.evaluate("return null");
			returnValue.set(evalResult);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body>HelloWorld</body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (returnValue.get() == null) {
			return; // passed
		}
	}
	fail();
}

/**
 * Test the evaluate() api that throws the invalid return value exception. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_invalid_return_value() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	if (SwtTestUtil.isWindows) {
		/* Bug 508210 . Inconsistent beahiour on windows at the moment.
		 * Fixing requires deeper investigation. Disabling newly added test for now.
		 */
		return;
	}

	final AtomicInteger exception = new AtomicInteger(-1);
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {}
		@Override
		public void completed(ProgressEvent event) {
			try {
			browser.evaluate("return new Date()"); //Date is not supoprted as return value.
			} catch (SWTException e) {
				exception.set(e.code);
			}
		}
	});

	browser.setText("<html><body>HelloWorld</body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (exception.get() != -1) {
			if (exception.get() == SWT.ERROR_INVALID_RETURN_VALUE) {
				return; // passed.
			} else if (exception.get() == SWT.ERROR_FAILED_EVALUATE) {
				System.err.println("SWT Warning: test_evaluate_invalid_return_value threw wrong exception code."
						+ " Expected ERROR_INVALID_RETURN_VALUE but got ERROR_FAILED_EVALUATE");
				return; // passed.
				// Webkit1 is known to throw the wrong exception.
			} else  {
				System.err.println("test_evaluate_invalid_return_value - Invalid exception code : " + exception.get());
			}
			break;
		}
	}
	fail();
}

/**
 * Test the evaluate() api that throws the evaluation failed exception. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_evaluation_failed_exception() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411
	final AtomicInteger exception = new AtomicInteger(-1);
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {}
		@Override
		public void completed(ProgressEvent event) {
			try {
			browser.evaluate("return runSomeUndefinedFunctionInJavaScriptWhichCausesUndefinedError()");
			} catch (SWTException e) {
				exception.set(e.code);
			}
		}
	});

	browser.setText("<html><body>HelloWorld</body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (exception.get() != -1) {
			if (exception.get() == SWT.ERROR_FAILED_EVALUATE) {
				return; // passed
			} else  {
				System.err.println("test_evaluate_invalid_return_value - Invalid exception code");
			}
			break;
		}
	}
	fail();
}

/**
 * Test the evaluate() api that returns an array of numbers. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_array_numbers() {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	// Small note:
	// evaluate() returns 'Double' type. Java doesn't have AtomicDouble
	// for convienience we simply convert double to int as we're dealing with integers anyway.
	final AtomicIntegerArray atomicIntArray = new AtomicIntegerArray(3);
	atomicIntArray.set(0, -1);
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Object[] evalResult = (Object[]) browser.evaluate("return new Array(1,2,3)");
			atomicIntArray.set(0, ((Double) evalResult[0]).intValue());
			atomicIntArray.set(1, ((Double) evalResult[1]).intValue());
			atomicIntArray.set(2, ((Double) evalResult[2]).intValue());
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body><p id='myid'>HelloWorld</p></body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (atomicIntArray.get(0) != -1) {
			if (atomicIntArray.get(0) == 1 && atomicIntArray.get(1) == 2 && atomicIntArray.get(2) == 3) {
				return; // passed
			} else {
				System.err.println("ERROR: test_evaluate_array_numbers, resulting numbers not as expected");
			}
		}
	}
	fail();
}

/**
 * Test the evaluate() api that returns an array of strings. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_array_strings () {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411

	final AtomicReferenceArray<String> atomicStringArray = new AtomicReferenceArray<>(3);
	atomicStringArray.set(0, "executing");
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Object[] evalResult = (Object[]) browser.evaluate("return new Array(\"str1\", \"str2\", \"str3\")");
			atomicStringArray.set(0, (String) evalResult[0]);
			atomicStringArray.set(1, (String) evalResult[1]);
			atomicStringArray.set(2, (String) evalResult[2]);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});

	browser.setText("<html><body><p id='myid'>HelloWorld</p></body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (! "executing".equals(atomicStringArray.get(0))) {
			if (atomicStringArray.get(0).equals("str1")
					&& atomicStringArray.get(1).equals("str2")
					&& atomicStringArray.get(2).equals("str3")) {
				return; // passed
			} else {
				System.err.println("ERROR: test_evaluate_array_strings, resulting strings not as expected");
			}
		}
	}
	fail();
}

/**
 * Test the evaluate() api that returns an array of mixed types. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate_array_mixedTypes () {
	assumeFalse(webkit1SkipMsg(), isWebkit1); // Bug 509411
	final AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(3);
	atomicArray.set(0, "executing");
	browser.addProgressListener(new ProgressListener() {
		@Override
		public void changed(ProgressEvent event) {
		}
		@Override
		public void completed(ProgressEvent event) {
			Object[] evalResult = (Object[]) browser.evaluate("return new Array(\"str1\", 2, true)");
			atomicArray.set(0, evalResult[0]);
			atomicArray.set(1, evalResult[1]);
			atomicArray.set(2, evalResult[2]);
			if (browser_debug)
				System.out.println("Node value: "+ evalResult);
		}
	});


	browser.setText("<html><body><p id='myid'>HelloWorld</p></body></html>");
	shell.open();
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if (! "executing".equals(atomicArray.get(0))) {
			if (atomicArray.get(0).equals("str1")
					&& ((Double) atomicArray.get(1)) == 2
					&& ((Boolean) atomicArray.get(2))) {
				return; //passed.
			} else {
				System.err.println("ERROR: test_evaluate_array_mixedTypes, resulting strings not as expected");
			}
		}
	}
	fail();
}

/* custom */
void runLoopTimer(final int milliseconds) {
	final boolean[] exit = {false};
	new Thread() {
		@Override
		public void run() {
			try {Thread.sleep(milliseconds);} catch (Exception e) {}
			exit[0] = true;
			/* wake up the event loop */
			Display display = Display.getDefault();
			if (!display.isDisposed()) {
				display.asyncExec(() -> {
					if (!shell.isDisposed()) shell.redraw();
				});
			}
		}
	}.start();
	shell.open();
	Display display = Display.getCurrent();
	while (!exit[0] && !shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
}

private String webkit1SkipMsg() {
	return "Test_org_eclipse_swt_browser. Bug 509411. Skipping test on Webkit1 due to sporadic crash: "+ name.getMethodName();
}

}
