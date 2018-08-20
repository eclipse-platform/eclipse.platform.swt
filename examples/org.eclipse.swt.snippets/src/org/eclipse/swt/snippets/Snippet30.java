/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
