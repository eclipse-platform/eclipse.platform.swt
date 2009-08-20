/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * StyledText snippet: embed controls
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
public class Snippet217 {
	
	static StyledText styledText;
	static String text = 
		"This snippet shows how to embed widgets in a StyledText.\n"+
		"Here is one: \uFFFC, and here is another: \uFFFC.";
	static int MARGIN = 5;
	
	static void addControl(Control control, int offset) {
		StyleRange style = new StyleRange ();
		style.start = offset;
		style.length = 1;
		style.data = control;
		control.pack();
		Rectangle rect = control.getBounds();
		int ascent = 2*rect.height/3;
		int descent = rect.height - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2*MARGIN);
		styledText.setStyleRange(style);	
	}
	
	public static void main(String [] args) {
		final Display display = new Display();
		Font font = new Font(display, "Tahoma", 32, SWT.NORMAL);
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setFont(font);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		styledText.setText(text);
		Button button = new Button(styledText, SWT.PUSH);
		button.setText("Button 1");
		int offset = text.indexOf('\uFFFC');
		addControl(button, offset);
		Combo combo = new Combo(styledText, SWT.NONE);
		combo.add("item 1");
		combo.add("another item");
		offset = text.indexOf('\uFFFC', offset + 1);
		addControl(combo, offset);
		
		// use a verify listener to dispose the controls
		styledText.addVerifyListener(new VerifyListener()  {
			public void verifyText(VerifyEvent event) {
				if (event.start == event.end) return;
				String text = styledText.getText(event.start, event.end - 1);
				int index = text.indexOf('\uFFFC');
				if (index != -1) {
					StyleRange style = styledText.getStyleRangeAtOffset(event.start + index);
					if (style != null) {
						Control control = (Control)style.data;
						if (control != null) control.dispose();
					}
				}
			}
		});
		
		// reposition widgets on paint event
		styledText.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				Control control = (Control)event.style.data;
				Point pt = control.getSize();
				int x = event.x + MARGIN;
				int y = event.y + event.ascent - 2*pt.y/3;
				control.setLocation(x, y);
			}
		});
			
		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		font.dispose();
		display.dispose();
	}
}
