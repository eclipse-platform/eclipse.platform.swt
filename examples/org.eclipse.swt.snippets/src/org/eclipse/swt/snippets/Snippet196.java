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

/*
 * Text example snippet: ise regular expression to verify input
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.util.regex.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet196 {
	/*
	 * Phone numbers follow the rule [1-9]-[1-9][1-9][1-9]-[1-9][1-9][1-9]-[1-9][1-9][1-9][1-9]
	 */
	private static final String REGEX = "\\d.\\d{3}.\\d{3}.\\d{4}"; 
	private static final String template = "#-###-###-####";
	private static final String defaultText = "0-000-000-0000";
	
	
public static void main(String[] args) {
	
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.BORDER);
	Font font = new Font(display, "Courier New", 10, SWT.NONE); //$NON-NLS-1$
	text.setFont(font);
	text.setText(template);	
	text.addListener(SWT.Verify, new Listener() {
		//create the pattern for verificatopn
		Pattern pattern = Pattern.compile(REGEX);	
		//ignore event when caused by inserting text inside event handler
		boolean ignore;
		public void handleEvent(Event e) {
			if (ignore) return;
			e.doit = false;
			if (e.start > 13 || e.end > 14) return;
			StringBuffer buffer = new StringBuffer(e.text);
			
			//handle backspace
			if (e.character == '\b') {
				for (int i = e.start; i < e.end; i++) {
					// skip over separators
					if(i == 1 || i == 5 || i == 9){
						if (e.start + 1 == e.end) {
							buffer.append(new char [] {'#','-'});
							e.start--;
						} else {
							buffer.append('-');
						}
					} else {
						buffer.append('#');
					}
				}
				text.setSelection(e.start, e.start + buffer.length());
				ignore = true;
				text.insert(buffer.toString());
				ignore = false;
				// move cursor backwards over separators
				if (e.start == 2 || e.start == 6 || e.start == 10) e.start--;
				text.setSelection(e.start, e.start);
				return;
			}
			
			StringBuffer newText = new StringBuffer(defaultText);
			char[] chars = e.text.toCharArray();
			int index = e.start-1;
			for (int i = 0; i < e.text.length(); i++) {
				index++;
				if (index == 1 || index == 5 || index == 9) {
					index++;
					if (chars[i] == '-') continue;
				}
				if (index >= newText.length()) return;
				newText.setCharAt(index, chars[i]);
			}

			if (e.start < e.end && index + 1 != e.end) return;
			Matcher matcher = pattern.matcher(newText);
			if (matcher.lookingAt()) {
				text.setSelection(e.start, index + 1);
				ignore = true;
				String temp = newText.substring(e.start, index + 1);
				text.insert(temp);
				ignore = false;
			}			
		}
	});
		
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	font.dispose();
	display.dispose();
}
}
