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
 * Text example snippet: verify input (format for date)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet179 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.BORDER);
	text.setText("YYYY/MM/DD");
	final Calendar calendar = Calendar.getInstance();
	text.addListener(SWT.Verify, new Listener() {
		boolean ignore;
		@Override
		public void handleEvent(Event e) {
			if (ignore) return;
			e.doit = false;
			StringBuilder buffer = new StringBuilder(e.text);
			char[] chars = new char[buffer.length()];
			buffer.getChars(0, chars.length, chars, 0);
			if (e.character == '\b') {
				for (int i = e.start; i < e.end; i++) {
					switch (i) {
						case 0: /* [Y]YYY */
						case 1: /* Y[Y]YY */
						case 2: /* YY[Y]Y */
						case 3: /* YYY[Y] */ {
							buffer.append('Y'); 	break;
						}
						case 5: /* [M]M*/
						case 6: /* M[M] */{
							buffer.append('M'); break;
						}
						case 8: /* [D]D */
						case 9: /* D[D] */ {
							buffer.append('D'); break;
						}
						case 4: /* YYYY[/]MM */
						case 7: /* MM[/]DD */ {
							buffer.append('/'); break;
						}
						default:
							return;
					}
				}
				text.setSelection(e.start, e.start + buffer.length());
				ignore = true;
				text.insert(buffer.toString());
				ignore = false;
				text.setSelection(e.start, e.start);
				return;
			}

			int start = e.start;
			if (start > 9) return;
			int index = 0;
			for (int i = 0; i < chars.length; i++) {
				if (start + index == 4 || start + index == 7) {
					if (chars[i] == '/') {
						index++;
						continue;
					}
					buffer.insert(index++, '/');
				}
				if (chars[i] < '0' || '9' < chars[i]) return;
				if (start + index == 5 &&  '1' < chars[i]) return; /* [M]M */
				if (start + index == 8 &&  '3' < chars[i]) return; /* [D]D */
				index++;
			}
			String newText = buffer.toString();
			int length = newText.length();
			StringBuilder date = new StringBuilder(text.getText());
			date.replace(e.start, e.start + length, newText);
			calendar.set(Calendar.YEAR, 1901);
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
			calendar.set(Calendar.DATE, 1);
			String yyyy = date.substring(0, 4);
			if (yyyy.indexOf('Y') == -1) {
				int year = Integer.parseInt(yyyy);
				calendar.set(Calendar.YEAR, year);
			}
			String mm = date.substring(5, 7);
			if (mm.indexOf('M') == -1) {
				int month =  Integer.parseInt(mm) - 1;
				int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
				if (0 > month || month > maxMonth) return;
				calendar.set(Calendar.MONTH, month);
			}
			String dd = date.substring(8,10);
			if (dd.indexOf('D') == -1) {
				int day = Integer.parseInt(dd);
				int maxDay = calendar.getActualMaximum(Calendar.DATE);
				if (1 > day || day > maxDay) return;
				calendar.set(Calendar.DATE, day);
			} else {
				if (calendar.get(Calendar.MONTH)  == Calendar.FEBRUARY) {
					char firstChar = date.charAt(8);
					if (firstChar != 'D' && '2' < firstChar) return;
				}
			}
			text.setSelection(e.start, e.start + length);
			ignore = true;
			text.insert(newText);
			ignore = false;
		}
	});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
