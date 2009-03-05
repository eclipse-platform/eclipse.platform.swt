/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * Shell example snippet: use shell modified state to prompt for save
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet314 {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText ("Text Editor");
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&File");
		Menu fileMenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (fileMenu);
		MenuItem saveItem = new MenuItem (fileMenu, SWT.PUSH);
		saveItem.setText ("&Save\tCtrl+S");
		saveItem.setAccelerator (SWT.MOD1 + 'S');
		saveItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				//SAVE CODE GOES HERE ...
				shell.setModified (false);
			}
		});
		MenuItem exitItem =  new MenuItem (fileMenu, SWT.PUSH);
		exitItem.setText ("Exit");
		exitItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				shell.close ();
			}
		});
		Text text = new Text (shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		text.addListener (SWT.Modify, new Listener () {
			public void handleEvent (Event e) {
				shell.setModified (true);
			}
		});
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event e) {
				if (shell.getModified()) {
					MessageBox box = new MessageBox (shell, SWT.PRIMARY_MODAL | SWT.OK | SWT.CANCEL);
					box.setText (shell.getText ());
					box.setMessage ("You have unsaved changes, do you want to exit?");
					e.doit = box.open () == SWT.OK;
				}
			}
		});
		shell.setLayout (new FillLayout());
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
}
