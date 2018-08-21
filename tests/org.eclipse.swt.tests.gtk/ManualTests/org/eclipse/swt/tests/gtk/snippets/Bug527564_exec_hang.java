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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title:  [GTK3][Webkit2] Implement webkit2 support for browser function (Part 2: Java return a value from callback.)
 * How to run: Snippet to execute javascript via input prompt, to test Browser Func return value ability.
 * Bug description:
 * Expected results:
 * GTK Version(s):
 */
public class Bug527564_exec_hang {

	static int count = 0;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 600);

		shell.setLayout(new RowLayout());

		Composite leftBrowser = new Composite(shell, SWT.NONE);
		Composite rightBrowser = new Composite(shell, SWT.None);
		Button button = new Button (rightBrowser, SWT.PUSH);
		button.setText("my button");

		final Browser browser = makeBrowserWithConsole(leftBrowser, "theJavaFunction");
		final Browser browser2 = makeBrowserWithConsole(rightBrowser, "theJavaFunction2");
		new CustomFunction (browser, "theJavaFunction");
		new CustomFunction2 (browser2, "theJavaFunction2");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * @param leftBrowser
	 * @return
	 */
	private static Browser makeBrowserWithConsole(Composite leftBrowser, String funcName) {
		GridLayout gridLayout = new GridLayout();
		leftBrowser.setLayout(gridLayout);

		final Text jsConsole = new Text(leftBrowser, SWT.BORDER);
		jsConsole.setText("document.body.innerHTML = " + funcName + "(123)"); // Case where there are no paramaters.
		jsConsole.setSelection(jsConsole.getText().length());
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		jsConsole.setLayoutData(data);

		final Browser browser = new Browser(leftBrowser, SWT.NONE);
		browser.setText("hello <b>world!</b>");
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		browser.setLayoutData(data);

		jsConsole.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) { // 13 = Enter
					browser.execute(jsConsole.getText());
				}
			}
		});

		Button loadNewPage = new Button(leftBrowser, SWT.PUSH);
		loadNewPage.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		loadNewPage.setText("Load new Page");
		loadNewPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setText("New page!" + count++);
			}
		});
		return browser;
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
