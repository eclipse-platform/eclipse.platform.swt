/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This Example Content is intended to demonstrate
 * usage of Eclipse technology. It is provided to you under the terms and
 * conditions of the Eclipse Distribution License v1.0 which is available
 * at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Accessibility example snippet: provide a way for an AT to
 * set text attributes in a StyledText.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class Snippet357 {
	
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		final StyledText text = new StyledText(shell, SWT.BORDER | SWT.MULTI);
		text.setText("The quick brown fox jumps over the lazy dog.\nThat's all folks!");
		TextStyle textStyle = new TextStyle(new Font(display, "Courier", 12, SWT.BOLD), display.getSystemColor(SWT.COLOR_RED), null);
		textStyle.strikeout = true;
		textStyle.underline = true;
		textStyle.underlineStyle = SWT.UNDERLINE_SINGLE;
		text.setStyleRanges(new int[] {4, 5}, new StyleRange[] {new StyleRange(textStyle)});

		text.getAccessible().addAccessibleEditableTextListener(new AccessibleEditableTextAdapter() {
			public void setTextAttributes(AccessibleTextAttributeEvent e) {
				TextStyle textStyle = e.textStyle;
				if (textStyle != null) {
					/* Copy all of the TextStyle fields into the new StyleRange. */
					StyleRange style = new StyleRange(textStyle);
					/* Create new graphics resources because the old ones are only valid during the event. */
					if (textStyle.font != null) style.font = new Font(display, textStyle.font.getFontData());
					if (textStyle.foreground != null) style.foreground = new Color(display, textStyle.foreground.getRGB());
					if (textStyle.background != null) style.background = new Color(display, textStyle.background.getRGB());
					if (textStyle.underlineColor != null) style.underlineColor = new Color(display, textStyle.underlineColor.getRGB());
					if (textStyle.strikeoutColor != null) style.strikeoutColor = new Color(display, textStyle.strikeoutColor.getRGB());
					if (textStyle.borderColor != null) style.borderColor = new Color(display, textStyle.borderColor.getRGB());
					/* Set the StyleRange into the StyledText. */
					style.start = e.start;
					style.length = e.end - e.start;
					text.setStyleRange(style);
					e.result = ACC.OK;
				} else {
					text.setStyleRanges(e.start, e.end - e.start, null, null);
				}
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}
