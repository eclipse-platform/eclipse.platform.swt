/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug497071_LinkColorStyledText {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Link link = new Link(shell, SWT.NONE);
		link.setText("This is a <a href=\"dummy\">link</a>.");

		final StyledText styledText = new StyledText(shell, SWT.READ_ONLY);
		styledText.setBackground(link.getBackground());
		styledText.setText("This is a styledText-link.");
		final StyleRange range = new StyleRange(21, 4, null, null);
		range.underline = true;
		range.underlineStyle = SWT.UNDERLINE_LINK;
		styledText.setStyleRange(range);

		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
