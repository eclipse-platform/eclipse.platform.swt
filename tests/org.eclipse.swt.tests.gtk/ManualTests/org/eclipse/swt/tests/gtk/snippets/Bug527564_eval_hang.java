/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title:  [GTK3][Webkit2] Implement webkit2 support for browser function (Part 2: Java return a value from callback.)
 * How to run: Snippet to execute javascript via input prompt, to test Browser Func return value ability.
 * Bug description:
 * Expected results:
 * GTK Version(s):
 */
public class Bug527564_eval_hang {

	static int count = 0;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 600);

		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.None);
		browser.setText("Hello 1");
//		Boolean retVal1 = (Boolean) browser.evaluate("return true");  // Eval doesn't cause it to hang, only function.
		new CustomFunction (browser, "theJavaFunction");
		
		final Browser browser2 = new Browser(shell, SWT.None);
		browser2.setText("Hello 2");
//		Boolean retVal2 = (Boolean) browser2.evaluate("return true"); // Eval doesn't cause it to hang, only function.
		new CustomFunction2 (browser2, "theJavaFunction2");
//		browser2.execute("document.body.style.backgroundColor = 'red'");
//		browser2.execute("");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static class CustomFunction extends BrowserFunction { // copied from snippet 307
		CustomFunction (Browser browser, String name) {
			super (browser, name);
		}
		@Override
		public Object function (Object[] arguments) {
			System.out.println ("theJavaFunction() called from javascript with args:");
			for (int i = 0; i < arguments.length; i++) {
				Object arg = arguments[i];
				if (arg == null) {
					System.out.println ("\t-->null");
				} else {
					System.out.println ("\t-->" + arg.getClass ().getName () + ": " + arg.toString ());
				}
			}
			return arguments;
		}
	}

	static class CustomFunction2 extends BrowserFunction { // copied from snippet 307
		CustomFunction2 (Browser browser, String name) {
			super (browser, name);
		}
		@Override
		public Object function (Object[] arguments) {
			System.out.println ("theJavaFunction() called from javascript with args:");
			for (int i = 0; i < arguments.length; i++) {
				Object arg = arguments[i];
				if (arg == null) {
					System.out.println ("\t-->null");
				} else {
					System.out.println ("\t-->" + arg.getClass ().getName () + ": " + arg.toString ());
				}
			}
			return arguments;
		}
	}
}
