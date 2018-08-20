/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 507339, 508155
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT accessibility snippet: respond to text-based questions from an AT
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.6
 */

import static org.eclipse.swt.events.FocusListener.*;
import static org.eclipse.swt.events.KeyListener.*;
import static org.eclipse.swt.events.MouseListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet334 {
	final static String text = "line one\nline two\nline three";
	static Display display;
	static Shell shell;
	static Canvas canvas;

public static void main(String[] arg) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		Label label = new Label(shell, SWT.NONE);
		label.setText("Test:");
		canvas = new Canvas(shell, SWT.MULTI | SWT.BORDER);
		final Caret caret = new Caret (canvas, SWT.NONE);
		canvas.addPaintListener(e -> {
			GC gc = e.gc;
			gc.drawText(text, 10, 10);
			caret.setBounds (10, 10, 2, gc.getFontMetrics().getHeight());
			Rectangle rect = canvas.getClientArea();
			if (canvas.isFocusControl()) {
				gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
			}
		});
		canvas.addTraverseListener(e -> {
			switch (e.detail) {
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
					e.doit = true; // enable traversal
					break;
			}
		});

		// key listener enables traversal out)
		canvas.addKeyListener(keyPressedAdapter(e-> {}));

		canvas.addFocusListener(focusGainedAdapter(event-> canvas.redraw()));
		canvas.addFocusListener(focusLostAdapter(event-> canvas.redraw()));
		canvas.addMouseListener(mouseDownAdapter(e ->canvas.setFocus()));
		Accessible acc = canvas.getAccessible();
		acc.addRelation(ACC.RELATION_LABELLED_BY, label.getAccessible());
		acc.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_TEXT;
			}
			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = canvas.getBounds();
				Point pt = shell.toDisplay(rect.x, rect.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = rect.width;
				e.height = rect.height;
			}
			@Override
			public void getValue(AccessibleControlEvent e) {
				e.result = text;
			}
			@Override
			public void getFocus(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}
			@Override
			public void getChildCount (AccessibleControlEvent e) {
				e.detail = 0;
			}
			@Override
			public void getState (AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL | ACC.STATE_FOCUSABLE;
				if (canvas.isFocusControl()) e.detail |= ACC.STATE_FOCUSED | ACC.STATE_SELECTABLE;
			}
		});
		acc.addAccessibleTextListener(new AccessibleTextExtendedAdapter() {
			@Override
			public void getSelectionRange(AccessibleTextEvent e) {
				// select the first 4 characters for testing
				e.offset = 0;
				e.length = 4;
			}
			@Override
			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = 0;
			}
			@Override
			public void getTextBounds(AccessibleTextEvent e) {
				// for now, assume that start = 0 and end = text.length
				GC gc = new GC(canvas);
				Point extent = gc.textExtent(text);
				gc.dispose();
				Rectangle rect = display.map(canvas, null, 10, 10, extent.x, extent.y);
				e.x = rect.x;
				e.y = rect.y;
				e.width = rect.width;
				e.height = rect.height;
			}
			@Override
			public void getText(AccessibleTextEvent e) {
				int start = 0, end = text.length();
				switch (e.type) {
					case ACC.TEXT_BOUNDARY_ALL:
						start = e.start;
						end = e.end;
						break;
					case ACC.TEXT_BOUNDARY_CHAR:
						start = e.count >= 0 ? e.start + e.count : e.start - e.count;
						start = Math.max(0, Math.min(end, start));
						end = start;
						e.count = start - e.start;
						e.start = start;
						e.end = start;
						break;
					case ACC.TEXT_BOUNDARY_LINE:
						int offset = e.count <= 0 ? e.start : e.end;
						offset = Math.min(offset, text.length());
						int lineCount = 0;
						int index = 0;
						while(index != -1) {
							lineCount ++;
							index = text.indexOf("\n", index);
							if (index != -1) index++;
						}
						e.count = e.count < 0 ? Math.max(e.count, -lineCount) : Math.min(e.count, lineCount);
				 		index = 0;
						int lastIndex = 0;
						String[] lines = new String[lineCount];
						for (int i = 0; i < lines.length; i++) {
							index = text.indexOf("\n", index);
							lines[i] = index != -1 ?
											text.substring(lastIndex, index) :
											text.substring(lastIndex);
							lastIndex = index;
							index++;
						}
						int len = 0;
						int lineAtOffset = 0;
						for (int i = 0; i < lines.length; i++) {
							len += lines[i].length();
							if (len >= e.offset) {
								lineAtOffset = i;
								break;
							}
						}
						int result = Math.max(0, Math.min(lineCount-1, lineAtOffset + e.count));
						e.count = result -lineAtOffset;
						e.result = lines[result];
						break;
				}
				e.result = text.substring(start, end);
			}
			@Override
			public void getSelectionCount(AccessibleTextEvent e) {
				e.count = 1;
			}
			@Override
			public void getSelection(AccessibleTextEvent e) {
				// there is only 1 selection, so index = 0
				getSelectionRange(e);
				e.start = e.offset;
				e.end = e.offset + e.length;
			}
			@Override
			public void getRanges(AccessibleTextEvent e) {
				// for now, ignore bounding box
				e.start = 0;
				e.end = text.length() - 1;
			}
			@Override
			public void getCharacterCount(AccessibleTextEvent e) {
				e.count = text.length();
			}
			@Override
			public void getVisibleRanges(AccessibleTextEvent e) {
				e.start = 0;
				e.end = text.length() - 1;
			}
		});
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
}
}
