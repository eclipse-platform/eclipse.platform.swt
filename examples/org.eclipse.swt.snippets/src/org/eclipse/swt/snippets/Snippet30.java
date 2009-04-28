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
 * Program example snippet: invoke the system text editor on a new file
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

public class Snippet30 {

public static void main (String [] args) {
	Display display = new Display ();
	Program p = Program.findProgram (".txt");
	if (p != null) p.execute ("newfile");
	display.dispose ();
}

} 
