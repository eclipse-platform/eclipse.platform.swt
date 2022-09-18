/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Issue0351_EventKeyCode {
	static String formatStateMask(int stateMask) {
		assert(SWT.MODIFIER_MASK == (SWT.ALT | SWT.SHIFT | SWT.CTRL | SWT.COMMAND | SWT.ALT_GR));

		StringBuilder sb = new StringBuilder();
		if ((stateMask & SWT.ALT_GR) != 0)      sb.append("AltGr+");
		if ((stateMask & SWT.ALT) != 0)         sb.append("Alt+");
		if ((stateMask & SWT.COMMAND) != 0)     sb.append("Cmd+");
		if ((stateMask & SWT.CTRL) != 0)        sb.append("Ctrl+");
		if ((stateMask & SWT.SHIFT) != 0)       sb.append("Shift+");

		return sb.toString();
	}

	static char formatChar(int character) {
		if ((character >= 0x20) && (character < 0xFFFF)) {
			return (char) character;
		}

		return ' ';
	}

	static String formatKey(int key) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%04X", key & ~SWT.MODIFIER_MASK));

		key &= ~SWT.MODIFIER_MASK;

		return String.format(
			"%04X '%s%c'",
			key,
			formatStateMask(key),
			formatChar(key)
		);
	}

	static char formatEventType(Event event) {
		switch(event.type)
		{
			case SWT.KeyUp:
				return '↑';
			case SWT.KeyDown:
				return '↓';
		}

		return '?';
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final Label hint = new Label(shell, 0);
		hint.setText(
			"This snippet is for debugging purposes\n" +
			"1) Press a key in text field below\n" +
			"2) Check console output for debug info\n"
		);

		Listener listener = event -> {
			// In order to reduce output clutter, skip events for pure modifiers such as Ctrl
			if (((event.keyCode & ~SWT.MODIFIER_MASK) == 0) && (event.character == 0)) {
				return;
			}

			System.out.format(
				"%c keyCode=%s character=%s state=%s%n",
				formatEventType(event),
				formatKey(event.keyCode),
				formatKey(event.character),
				formatStateMask(event.stateMask)
			);
		};

		new Label(shell, 0).setText("Text");
		final Text text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text.addListener(SWT.KeyDown, listener);
		text.addListener(SWT.KeyUp,   listener);

		new Label(shell, 0).setText("StyledText");
		final StyledText styledText = new StyledText(shell, SWT.BORDER);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		styledText.addListener(SWT.KeyDown, listener);
		styledText.addListener(SWT.KeyUp,   listener);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
