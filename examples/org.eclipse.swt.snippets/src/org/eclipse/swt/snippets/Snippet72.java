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
 * FileDialog example snippet: prompt for a file name (to save)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet72 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.open ();
	FileDialog dialog = new FileDialog (shell, SWT.SAVE);
	dialog.setFilterNames (new String [] {"Batch Files", "All Files (*.*)"});
	dialog.setFilterExtensions (new String [] {"*.bat", "*.*"}); //Windows wild cards
	dialog.setFilterPath ("c:\\"); //Windows path
	dialog.setFileName ("fred.bat");
	System.out.println ("Save to: " + dialog.open ());
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
