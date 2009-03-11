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
 * StyledText example: use margins in StyledText
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet316 {
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		StyledText text = new StyledText(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		text.setText("StyledText with margins.");
		Font font = new Font(display, "Tahoma", 14, SWT.ITALIC);
		text.setText("\"If you go down to the woods today\nYou'd better not go alone\nIt's lovely down in the woods today\nBut safer to stay at home\"");
		StyleRange italic = new StyleRange();
		italic.font = font;
		text.setStyleRanges(new int[] {0, text.getCharCount()}, new StyleRange[] {italic});
		text.setMargins(30, 30, 30, 30);
		Color color = new Color (display, 138, 226, 255);
		text.setMarginColor(color);
		shell.setSize(500, 300);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		font.dispose();
		color.dispose();
		display.dispose ();
	}
}
