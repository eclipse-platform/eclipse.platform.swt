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

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
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
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.Browser
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class Test_org_eclipse_swt_browser_Browser extends Test_org_eclipse_swt_widgets_Composite {

	Browser browser;

	boolean browser_debug = false;

@Override
@Before
public void setUp() {
	super.setUp();
	shell.setLayout(new FillLayout());
	browser = new Browser(shell, SWT.NONE);

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

// TODO - change test cases to use this method instead of shell.setText(..).
/**
 * Append relevant information to the shell title.
 *
 * On Gtk, we support multiple versions of Webkit. It's useful to know which webkit version the test runs on.
 *
 * @param title
 */
void setTitle(String title) {
	if (SwtTestUtil.isGTK) {
		String SWT_WEBKITGTK_VERSION = "org.eclipse.swt.internal.webkitgtk.version"; //$NON-NLS-1$
		Properties sp = System.getProperties();
		String webkitGtkVer = sp.getProperty(SWT_WEBKITGTK_VERSION);
		if (webkitGtkVer != null)
			title = title + "  Webkit version: " + webkitGtkVer;
	}
	shell.setText(title);
}


/**
 * Test that if Browser is constructed with the parent being "null", Browser throws an exception.
 */
@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	shell.setText("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	Browser browser = new Browser(shell, SWT.NONE);
	browser.dispose();
	browser = new Browser(shell, SWT.BORDER);
	System.out.println("Browser#getBrowserType(): " + browser.getBrowserType());
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
	shell.setText("test_addCloseWindowListenerLorg_eclipse_swt_browser_CloseWindowListener");
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
	shell.setText("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener");
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
	shell.setText("test_addOpenWindowListenerLorg_eclipse_swt_browser_OpenWindowListener");
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
	shell.setText("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener");
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


/**
 * Test that if addStatusTextListener() is called without a listener, IllegalArgumentException is thrown.
 */
@Test
public void test_listener_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	shell.setText("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
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
	shell.setText("test_addTitleLorg_eclipse_swt_browser_TitleListener");
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
	shell.setText("test_addVisibilityWindowListenerLorg_eclipse_swt_browser_VisibilityWindowListener");
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


/**
 * Test that going back in history, when no new pages were visited, returns false.
 */
@Test
public void test_back() {
	shell.setText("test_back");
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
	shell.setText("test_setTextNull");
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
	shell.setText("test_setUrlLjava_lang_String");
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
	shell.setText("test_forward");
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
	shell.setText("test_getUrl");
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
	shell.setText("test_isBackEnabled");

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
	shell.setText("test_isForwardEnabled");

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
	shell.setText("test_refresh");
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
	shell.setText("test_setTextLjava_lang_String");

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
	shell.setText("test_setUrl");
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
	shell.setText("test_stop");
	/* THIS TEST REQUIRES WEB ACCESS! How else can we really test the http:// part of a browser widget? */
	browser.setUrl("http://www.eclipse.org/swt");
	runLoopTimer(1000);
	browser.stop();
}

/**
 * Test the evaluate() api. Functionality based on Snippet308.
 * Only wait till success. Otherwise timeout after 3 seconds.
 */
@Test
public void test_evaluate() {
	setTitle("test_evalute");

	final String html = "<html><body><p id='myid'>HelloWorld</p></body></html>";

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

	browser.setText(html);
	shell.open();

	boolean passed = false;

	int secondsToWaitTillFail = 3;
	int waitMS = browser_debug ? 1000 : 1;       // Normally, sleep in 1 ms intervals 1000 times.
	int loopMultipier = browser_debug ? 1 : 1000;// during debug, sleep 1000 ms for 1 interval.
	for (int i = 0; i < (loopMultipier * secondsToWaitTillFail); i++) {  // Wait up to seconds before declaring test as failed.
		runLoopTimer(waitMS);
		if ("HelloWorld".equals(returnValue.get())) {
			passed = true;
			break;
		}
	}
	assertTrue(passed);
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
}
