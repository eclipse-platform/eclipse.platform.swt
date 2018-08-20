/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 * Text example snippet: use a regular expression to verify input
 * In this case a phone number is used.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.util.regex.*;
import java.util.regex.Pattern;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet196 {
	/*
	 * Phone numbers follow the rule [(][1-9][1-9][1-9][)][1-9][1-9][1-9][-][1-9][1-9][1-9][1-9]
	 */
	private static final String REGEX = "[(]\\d{3}[)]\\d{3}[-]\\d{4}";  //$NON-NLS-1$
	private static final String template = "(###)###-####"; //$NON-NLS-1$
	private static final String defaultText = "(000)000-0000"; //$NON-NLS-1$


public static void main(String[] args) {

	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.BORDER);
	Font font = new Font(display, "Courier New", 10, SWT.NONE); //$NON-NLS-1$
	text.setFont(font);
	text.setText(template);
	text.addListener(SWT.Verify, new Listener() {
		//create the pattern for verification
		Pattern pattern = Pattern.compile(REGEX);
		//ignore event when caused by inserting text inside event handler
		boolean ignore;
		@Override
		public void handleEvent(Event e) {
			if (ignore) return;
			e.doit = false;
			if (e.start > 13 || e.end > 14) return;
			StringBuilder buffer = new StringBuilder(e.text);

			//handle backspace
			if (e.character == '\b') {
				for (int i = e.start; i < e.end; i++) {
					// skip over separators
					switch (i) {
						case 0:
							if (e.start + 1 == e.end) {
								return;
							} else {
								buffer.append('(');
							}
							break;
						case 4:
							if (e.start + 1 == e.end) {
								buffer.append(new char [] {'#',')'});
								e.start--;
							} else {
								buffer.append(')');
							}
							break;
						case 8:
							if (e.start + 1 == e.end) {
								buffer.append(new char [] {'#','-'});
								e.start--;
							} else {
								buffer.append('-');
							}
							break;
						default: buffer.append('#');
					}
				}
				text.setSelection(e.start, e.start + buffer.length());
				ignore = true;
				text.insert(buffer.toString());
				ignore = false;
				// move cursor backwards over separators
				if (e.start == 5 || e.start == 9) e.start--;
				text.setSelection(e.start, e.start);
				return;
			}

			StringBuilder newText = new StringBuilder(defaultText);
			char[] chars = e.text.toCharArray();
			int index = e.start - 1;
			for (int i = 0; i < e.text.length(); i++) {
				index++;
				switch (index) {
					case 0:
						if (chars[i] == '(') continue;
						index++;
						break;
					case 4:
						if (chars[i] == ')') continue;
						index++;
						break;
					case 8:
						if (chars[i] == '-') continue;
						index++;
						break;
				}
				if (index >= newText.length()) return;
				newText.setCharAt(index, chars[i]);
			}
			// if text is selected, do not paste beyond range of selection
			if (e.start < e.end && index + 1 != e.end) return;
			Matcher matcher = pattern.matcher(newText);
			if (matcher.lookingAt()) {
				text.setSelection(e.start, index + 1);
				ignore = true;
				text.insert(newText.substring(e.start, index + 1));
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
