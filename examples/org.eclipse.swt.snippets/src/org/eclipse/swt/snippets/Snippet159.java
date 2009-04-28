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
 * Browser example snippet: modify HTML title tag
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

public class Snippet159 {
	public static void main(String [] args) {
		final String newTitle = "New Value for Title";
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
				System.out.println("TitleEvent: "+event.title);
				shell.setText(event.title);
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				/* Set HTML title tag using JavaScript and DOM when page has been loaded */
				boolean result = browser.execute("document.title='"+newTitle+"'");
				if (!result) {
					/* Script may fail or may not be supported on certain platforms. */
					System.out.println("Script was not executed.");
				}
			}
		});
		/* Load an HTML document */
		browser.setUrl("http://www.eclipse.org");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}


