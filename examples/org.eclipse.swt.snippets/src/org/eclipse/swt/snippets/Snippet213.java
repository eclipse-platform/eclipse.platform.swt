/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * SWT StyledText snippet: use indent, alignment and justify.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;

public class Snippet213 {

	static String text =
		"The first paragraph has an indentation of fifty pixels. Indentation is the amount of white space in front of the first line of a paragraph. If this paragraph wraps to several lines you should see the indentation only on the first line.\n\n" +
		"The second paragraph is center aligned. Alignment only works when the StyledText is using word wrap. Alignment, as with all other line attributes, can be set for the whole widget or just for a set of lines.\n\n" +
		"The third paragraph is justified. Like alignment, justify only works when the StyledText is using word wrap. If the paragraph wraps to several lines, the justification is performed on all lines but the last one.\n\n" +
		"The last paragraph is justified and right aligned. In this case, the alignment is only noticeable in the final line.";

	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		StyledText styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setText(text);
		styledText.setLineIndent(0, 1, 50);
		styledText.setLineAlignment(2, 1, SWT.CENTER);
		styledText.setLineJustify(4, 1, true);
		styledText.setLineAlignment(6, 1, SWT.RIGHT);
		styledText.setLineJustify(6, 1, true);

		shell.setSize(300, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
