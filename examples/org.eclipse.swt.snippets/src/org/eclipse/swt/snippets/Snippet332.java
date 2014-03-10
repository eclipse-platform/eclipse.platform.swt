/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
 * SWT StyledText and Combo snippet: using BidiSegmentListener and
 * SegmentListener to implement custom bidi segments, such as right-to-left
 * override
 * 
 * Note that SegmentListener in the Combo widget is supported since 4.4 and
 * currently only on win32 platform
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class Snippet332 {
	
	public static void main(String [] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 10;
		shell.setLayout(layout);
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.BORDER);
		final String segment = "Eclipse";
		String string = "Force RTL direction on this segment \""+segment+"\".";
		text.setText(string);
		int[] segments = {string.indexOf(segment), segment.length()};
		StyleRange[] ranges = {new StyleRange(0, 0, display.getSystemColor(SWT.COLOR_RED), null)};
		text.setStyleRanges(segments, ranges);
		Font font = new Font(display, "Tahoma", 16, 0);
		text.setFont(font);
		text.addBidiSegmentListener(new BidiSegmentListener() {
			@Override
			public void lineGetSegments(BidiSegmentEvent event) {
				String string = event.lineText;
				int start = string.indexOf(segment);
				event.segments = new int []{start, start + segment.length()};
				event.segmentsChars = new char[] {'\u202e', '\u202C'};
			}
		});
		Combo combo = new Combo(shell, SWT.SIMPLE);
		combo.setFont(font);
		combo.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		combo.setItems(new String[] { "Option 1...", "Option 2...", "Option 3...", "Option 4..." });
		combo.select(1);
		combo.addSegmentListener(new SegmentListener() {
			@Override
			public void getSegments(SegmentEvent event) {
				event.segments = new int [] {0, event.lineText.length()};
				event.segmentsChars = new char [] {'\u202e', '\u202c'};
			}
		});
		shell.setSize(500, 250);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		font.dispose();
		display.dispose();
	}
}
