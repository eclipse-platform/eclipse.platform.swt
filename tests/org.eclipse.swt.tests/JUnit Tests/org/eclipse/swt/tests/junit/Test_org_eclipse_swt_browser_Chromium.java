/********************************************************************************
 * Copyright (c) 2020 Equo
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Guillermo Zunino, Equo - initial implementation
 ********************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.Browser
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class Test_org_eclipse_swt_browser_Chromium extends Test_org_eclipse_swt_browser_Browser {

	@BeforeClass
	public static void setChromiumStyle() {
		System.setProperty("org.eclipse.swt.browser.DefaultType", "chromium");
	}

	@AfterClass
	public static void setDefaultStyle() {
		System.clearProperty("org.eclipse.swt.browser.DefaultType");
	}

	@Before
	public void setup() {
		debug_show_browser = true;
		debug_show_browser_timeout_seconds = 1;
	}

	/**
	 * Test that execute works works in callback BrowserFunction multiple times.
	 */
	@Test
	public void test_BrowserFunction_callback_and_execute() {
		AtomicBoolean javaCallbackExecuted = new AtomicBoolean(false);
		AtomicInteger callCount = new AtomicInteger(0);

		new BrowserFunction(browser, "callFunc") {
			@Override
			public Object function(Object[] arguments) {
				System.out.println("callFunc BrowserFunc " +callCount.get() + " " + arguments[0]);
				browser.execute("doSomething()");
				if (arguments != null && arguments.length == 1 && "de".equals(arguments[0])) {
					callCount.incrementAndGet();
					return "{\"Reset\":\"Zur0cksetzen\"}";
				}
				callCount.incrementAndGet();
				return "{\"Reset\":\"Reset\"}";
			}
		};
		new BrowserFunction(browser, "finishedSomething") {
			@Override
			public Object function(Object[] arguments) {
				System.out.println("finishedSomething BrowserFunc " + callCount.get() + " " + arguments[0]);
				callCount.incrementAndGet();
				browser.execute("doSomething1()");
				javaCallbackExecuted.set(true);
				return null;
			}
		};
		browser.setText("<html>\n" +
				"<body>\n" +
				"\n" +
				"	English\n" +
				"	<div id=\"english\"></div>\n" +
				"	German\n" +
				"	<div id=\"german\"></div>\n" +
				"\n" +
				"	<script>\n" +
				"		console.log('callFunc(en)')\n" +
				"		var en = callFunc('en');\n" +
				"		console.log('en json received');\n" +
				"		var enResult = JSON.parse(en);\n" +
				"		console.log('en json parsed')\n" +
				"		document.getElementById('english').innerHTML = \"Success: \" + enResult.Reset;\n" +
				"		console.log('html updated')\n" +
				"	\n" +
				"		console.log('callFunc(de)');\n" +
				"		var de = callFunc('de');\n" +
				"		console.log('de json received');\n" +
				"		var deResult = JSON.parse(de);\n" +
				"		console.log('de json parsed');\n" +
				"		document.getElementById('german').innerHTML = \"Success: \" + deResult.Reset;\n" +
				"		console.log('html updated 2');\n" +
				"		\n" +
				"		// functions needed for demonstration of issue 31\n" +
				"		function doSomething() {\n" +
				"			console.log('Doin something');\n" +
				"			var tmp = 0;\n" +
				"			for (var i = 0; i < 10000; i++) {\n" +
				"				tmp = tmp + 1 * 2;\n" +
				"			}\n" +
				"			console.log('Finished something');\n" +
				"			finishedSomething('a');\n" +
				"		}\n" +
				"		function doSomething1() {\n" +
				"			console.log('Doin something1');\n" +
				"			var tmp = 0;\n" +
				"			for (var i = 0; i < 10000; i++) {\n" +
				"				tmp = tmp + 1 * 2;\n" +
				"			}\n" +
				"			console.log('Finished something1');\n" +
				"		}\n" +
				"	</script>\n" +
				"</body>\n" +
				"</html>");

		shell.open();
		boolean passed = waitForPassCondition(() -> javaCallbackExecuted.get() && callCount.get() == 4);
		String message = "A javascript callback should work. But something went wrong. Call count: " + callCount.get();
		assertTrue(message, passed);
	}

	/**
	 * Test that javascript can call java, java returns a String back to javascript.
	 *
	 * It's a bit tricky to tell if javascript actually received the correct value from java.
	 * Solution: make a second function/callback that is called with the value that javascript received from java.
	 *
	 * Logic:
	 *  1) Java registers function callCustomFunction() by setting html body.
	 *  2) which in turn calls JavascriptCallback, which returns value string back to javascript.
	 *  3) javascript then calls JavascriptCallback_javascriptReceivedJavaString() and passes it value received from java.
	 *  4) Java validates that the correct value was passed to javascript and was passed back to java.
	 *
	 * loosely based on Snippet307.
	 */
	@Test
	public void test_BrowserFunction_callback_with_javaReturningString () {
		AtomicReference<String> returnString = new AtomicReference<>();

		class JavascriptCallback extends BrowserFunction { // Note: Local class defined inside method.
			JavascriptCallback(Browser browser, String name) {
				super(browser, name);
			}

			@Override
			public Object function(Object[] arguments) {
				return "a � string";
			}
		}

		class JavascriptCallback_javascriptReceivedJavaString extends BrowserFunction { // Note: Local class defined inside method.
			JavascriptCallback_javascriptReceivedJavaString(Browser browser, String name) {
				super(browser, name);
			}

			@Override
			public Object function(Object[] arguments) {
				System.out.println("JavascriptCallback_javascriptReceivedJavaString");
				String returnVal = (String) arguments[0];
				returnString.set(returnVal);  // 4)
				return null;
			}
		}

		String htmlWithScript = "<html><head>\n"
				+ "<script language=\"JavaScript\">\n"
				+ "function callCustomFunction() {\n"  // Define a javascript function.
				+ "     document.body.style.backgroundColor = 'red'\n"
				+ "     var retVal = jsCallbackToJava()\n"  // 2)
				+ "		console.log('val:' + retVal)\n"        // This calls the javafunction that we registered. Set HTML body to return value.
				+ "		document.write(retVal)\n"        // This calls the javafunction that we registered. Set HTML body to return value.
				+ "     jsSuccess(retVal)\n"				// 3)
				+ "}"
				+ "</script>\n"
				+ "</head>\n"
				+ "<body> If you see this, javascript did not receive anything from Java. This page should just be a string </body>\n"
				+ "</html>\n";
		// 1)
		browser.setText(htmlWithScript);
		new JavascriptCallback(browser, "jsCallbackToJava");
		new JavascriptCallback_javascriptReceivedJavaString(browser, "jsSuccess");

		browser.addProgressListener(callCustomFunctionUponLoad);

		shell.open();
		boolean passed = waitForPassCondition(() -> "a � string".equals(returnString.get()));
		String message = "Java should have returned something back to javascript. But something went wrong";
		assertTrue(message, passed);
	}

	/**
	 * Test that javascript can call java, java returns an mixed array back to javascript.
	 *
	 * It's a bit tricky to tell if javascript actually received the correct value from java.
	 * Solution: make a second function/callback that is called with the value that javascript received from java.
	 *
	 * Logic:
	 *  1) Java registers function callCustomFunction() by setting html body.
	 *  2) which in turn calls JavascriptCallback, which returns mixed array value back to javascript.
	 *  3) javascript then calls JavascriptCallback_javascriptReceivedJavaArray() and passes it value received from java.
	 *  4) Java validates that the correct value was passed to javascript and was passed back to java.
	 *
	 * loosely based on Snippet307.
	 */
	@Test
	public void test_BrowserFunction_callback_with_javaReturningArray () {
		AtomicReferenceArray<Object> returnArray = new AtomicReferenceArray<>(3);

		class JavascriptCallback extends BrowserFunction { // Note: Local class defined inside method.
			JavascriptCallback(Browser browser, String name) {
				super(browser, name);
			}

			@Override
			public Object function(Object[] arguments) {
				return new Object[] {"a String", 42, true};
			}
		}

		class JavascriptCallback_javascriptReceivedJavaArray extends BrowserFunction { // Note: Local class defined inside method.
			JavascriptCallback_javascriptReceivedJavaArray(Browser browser, String name) {
				super(browser, name);
			}

			@Override
			public Object function(Object[] arguments) {
				Object[] returnVal = (Object[]) arguments[0];
				returnArray.set(0, returnVal[0]);
				returnArray.set(1, returnVal[1]);
				returnArray.set(2, returnVal[2]);
				return null;
			}
		}

		String htmlWithScript = "<html><head>\n"
				+ "<script language=\"JavaScript\">\n"
				+ "function callCustomFunction() {\n"  // Define a javascript function.
				+ "     document.body.style.backgroundColor = 'red'\n"
				+ "     var retVal = jsCallbackToJava()\n"  // 2)
				+ "		document.write(retVal)\n"        // This calls the javafunction that we registered. Set HTML body to return value.
				+ "     jsSuccess(retVal)\n"				// 3)
				+ "}"
				+ "</script>\n"
				+ "</head>\n"
				+ "<body> If you see this, javascript did not receive anything from Java. This page should just be '{\"a String\", 42, true}' </body>\n"
				+ "</html>\n";
		// 1)
		browser.setText(htmlWithScript);
		new JavascriptCallback(browser, "jsCallbackToJava");
		new JavascriptCallback_javascriptReceivedJavaArray(browser, "jsSuccess");

		browser.addProgressListener(callCustomFunctionUponLoad);

		shell.open();
		boolean passed = waitForPassCondition(() ->
		"a String".equals(returnArray.get(0)) && Double.valueOf(42.0d).equals(returnArray.get(1)) && Boolean.valueOf((boolean) returnArray.get(2)) == true);
		String message = "Java should have returned something back to javascript. But something went wrong";
		assertTrue(message, passed);
	}

}
