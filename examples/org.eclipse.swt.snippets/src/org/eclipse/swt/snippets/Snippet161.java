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
package org.eclipse.swt.snippets;

/*
 * Browser example snippet: modify DOM (executing javascript)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet161 {
	public static void main(String [] args) {
		final String html = "<html><title>Snippet</title><body><p id='myid'>Best Friends</p><p id='myid2'>Cat and Dog</p></body></html>";
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser;
		try {
			browser = new Browser(shell, SWT.BORDER);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new FillLayout(SWT.VERTICAL));
		final Text text = new Text(comp, SWT.MULTI);
		text.setText("var newNode = document.createElement('P'); \r\n"+
				"var text = document.createTextNode('At least when I am around');\r\n"+
				"newNode.appendChild(text);\r\n"+
				"document.getElementById('myid').appendChild(newNode);\r\n"+
				"\r\n"+
				"document.bgColor='yellow';");
		final Button button = new Button(comp, SWT.PUSH);
		button.setText("Execute Script");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean result = browser.execute(text.getText());
				if (!result) {
					/* Script may fail or may not be supported on certain platforms. */
					System.out.println("Script was not executed.");
				}
			}
		});
		browser.setText(html);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}


