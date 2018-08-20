/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * StyledText snippet: Draw a box around text.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet244 {
	static String SEARCH_STRING = "box";
    public static void main(String[] args) {
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        final StyledText text = new StyledText(shell, SWT.NONE);
        StyleRange style = new StyleRange();
        style.borderColor = display.getSystemColor(SWT.COLOR_RED);
        style.borderStyle = SWT.BORDER_SOLID;
        StyleRange[] styles = {style};
        String contents = "This demonstrates drawing a box\naround every occurrence of the word\nbox in the StyledText";
        text.setText(contents);
		int index = contents.indexOf(SEARCH_STRING);
		while (index != -1) {
			text.setStyleRanges(0, 0, new int[] {index, SEARCH_STRING.length()}, styles);
			index = contents.indexOf(SEARCH_STRING, index + 1);
		}
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
