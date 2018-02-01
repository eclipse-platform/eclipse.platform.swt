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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * On Webkit2, mouseUp/Down were not working.
 * On Webkit1/2, Focus in/out were not working.
 *
 * Snippet prints events to console. Run and observer.
 *
 * On webkit1, signals were partially handled via javascript, on webkit2 only through gdk events.
 *
 */
public class Bug528549_browser_MouseFocusEventListeners {

	static int loadCounter;
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(500, 200);
		final Browser browser = new Browser(shell, SWT.NONE);

		browser.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("Browser Focus lost " + e.toString());
			}
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("Browser Focus gained " + e.toString());
			}
		});

		browser.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("Browser Mouse Up " + e.toString());
			}
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("Browser Mouse Down " + e.toString());

			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				System.out.println("Browse Mouse Double click " + e.toString());
			}
		});


		// Below listeners already worked before bug. But good to have around.
		browser.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("Browser key released " + e.toString());
			}
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Browser key pressed " + e.toString());
			}
		});

		browser.addMouseWheelListener(e -> System.out.println("Browser scroll event " + e.toString()));

		// Generates a lot of events...
		browser.addMouseMoveListener(e -> System.out.println("Browser mouse moved " + e.toString()));

		Button jsOnButton = new Button(shell, SWT.PUSH);
		jsOnButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		jsOnButton.setText("JS Off");
		jsOnButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> browser.setJavascriptEnabled(false)));

		Button jsOffButton = new Button(shell, SWT.PUSH);
		jsOffButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		jsOffButton.setText("JS On");
		jsOffButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> browser.setJavascriptEnabled(true)));


		Button loadNextPage = new Button(shell, SWT.PUSH);
		loadNextPage.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		loadNextPage.setText("Load next page");
		loadNextPage.addSelectionListener( SelectionListener.widgetSelectedAdapter(e -> browser.setText(getNewText())));

		browser.setText(getNewText());

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static String getNewText() {
		return "Hello world<br>"
				+ "<input type=\"button\" value=\"Open Curtain\" onclick=\"return change(this);\" />\n" +
				"\n" +
				"<script type=\"text/javascript\">\n" +
				"function change( el )\n" +
				"{\n" +
				"    if ( el.value === \"Open Curtain\" )\n" +
				"        el.value = \"Close Curtain\";\n" +
				"    else\n" +
				"        el.value = \"Open Curtain\";\n" +
				"}\n" +
				"</script>" + loadCounter++;
	}


}
