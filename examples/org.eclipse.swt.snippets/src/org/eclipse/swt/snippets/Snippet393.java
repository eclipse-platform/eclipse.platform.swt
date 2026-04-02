/*******************************************************************************
 * Copyright (c) 2026 Eclipse Foundation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Dark Theme Preferred API snippet: demonstrate how to request a dark theme for 
 * native components (title bars, scrollbars, and native dialogs).
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet393 {

public static void main (String [] args) {
	Display display = new Display ();
	
	/* Signal that the application prefers a dark theme */
	display.setDarkThemePreferred(true);
	
	Shell shell = new Shell (display);
	shell.setText("Snippet 393 - Dark Theme Preferred");
	shell.setLayout(new GridLayout(1, false));
	
	Label label = new Label(shell, SWT.NONE);
	label.setText("The title bar and native dialogs should appear dark if supported by the OS.");
	
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Open DirectoryDialog");
	button.addListener(SWT.Selection, event -> {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setMessage("Select a directory (dialog should be dark)");
		dialog.open();
	});
	
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
