/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
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
 * Ligatures support added in StyledText with 'Fira Code' font:
 * https://github.com/tonsky/FiraCode/tree/master/distr/ttf
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet377 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 377");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		Label label1= new Label(shell, SWT.NONE);
		label1.setText("StyledText:");
		StyledText styledText = new StyledText(shell, SWT.BORDER);
		styledText.setFont(new Font(display, "Fira Code", 9, SWT.NORMAL));
		styledText.setText(
				"Ligatures support added in StyledText: \n");
		styledText.append(" == != <= >= |= || -> <- \n");
		styledText.append("c:\\ぷろぐらむ\\program \n");
		styledText.append("c:\\\u3077\u308d\u3050\u3089\u3080\\program \n");
		styledText.append("a\\&~あ\\&~a\\&~ \n");
		styledText.append("a\\&~\u3042\\&~a\\&~");

		Label label2= new Label(shell, SWT.NONE);
		label2.setText("Text:");
		Text text = new Text(shell, SWT.BORDER | SWT.MULTI);
		text.setFont(new Font(display, "Fira Code", 9, SWT.NORMAL));
		text.setText(
				"Ligatures support(from native): \n");
		text.append(" == != <= >= |= || -> <- \n");
		text.append("c:\\ぷろぐらむ\\program \n");
		text.append("c:\\\u3077\u308d\u3050\u3089\u3080\\program \n");
		text.append("a\\&~あ\\&~a\\&~ \n");
		text.append("a\\&~\u3042\\&~a\\&~");

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}