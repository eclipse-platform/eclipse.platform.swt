/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title:  Bug 536141: [Webkit2][Gtk] BrowserFunction lost after page reload on Linux/GTK
 * How to run: Read the instructions printed in the console
 * Bug description: N/A
 * Expected results: BrowserFunctions should be added/removed as expected, see instructions for details
 * GTK Version(s):
 */
public class Bug536141_BrowserFunctionLostReload {

	static int count = 0;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 600);

		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		final Text jsConsole = new Text(shell, SWT.BORDER);
//		jsConsole.setText("document.body.innerHTML = theJavaFunction(123, 'hello', null, true)");
		jsConsole.setText("document.body.innerHTML = theJavaFunction()"); // Case where there are no paramaters.
		jsConsole.setSelection(jsConsole.getText().length());
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		jsConsole.setLayoutData(data);

		final Browser browser = new Browser(shell, SWT.NONE);
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

		Button loadNewPage = new Button(shell, SWT.PUSH);
		loadNewPage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		loadNewPage.setText("Load new page");
		loadNewPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setText("New page!" + count++);
			}
		});


		// BrowserFunction Code
		final BrowserFunction function = new CustomFunction (browser, "theJavaFunction");

		Button create = new Button (shell, SWT.PUSH);
		create.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		create.setText("Create function");
		create.addSelectionListener(new SelectionListener () {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("creating function");
				@SuppressWarnings("unused")
				final BrowserFunction function2 = new CustomFunction (browser, "theJavaFunction2");
			}
		});

		Button destroy = new Button (shell, SWT.PUSH);
		destroy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		destroy.setText("Destroy function");
		destroy.addSelectionListener(new SelectionListener () {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("destroying function");
				function.dispose();
			}
		});
		shell.open();
		System.out.println("INSTRUCTIONS: pressing the \"Create function\" button will create a function called theJavaFunction2.");
		System.out.println("Pressing the \"Destroy function\" button will destory the function called theJavaFunction.");
		System.out.println("To test that theJavaFunction2 was created, add the \"2\""
				+ " at the end of theJavaFunction in the console and press enter.");
		System.out.println("To test that theJavaFunction was deleted, remove the \"2\""
				+ " at the end of theJavaFunction in the console and press enter.");
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
//			return new Point(1, 2);

//			Object returnValue = new Object[] {
//					new Short ((short)3),
//					new Boolean (true),
//					null,
//					new Object[] {"a string", new Boolean (false)},
//					"hi",
//					new Float (2.0f / 3.0f),
//				};
//			return returnValue;

//			return new Double(42.0);
		}
	}
}
