/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Button example: focus indication on a button with a custom background and
 * foreground. Tab between the controls to move the focus. On Windows the focus
 * rectangle used to be XOR-inverted, which turned into an arbitrary
 * complementary color on the saturated background below.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet395 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Focus rectangle on a styled Button");
		shell.setLayout(new GridLayout());

		Text text = new Text(shell, SWT.BORDER);
		text.setText("Tab out of this field");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button styled = new Button(shell, SWT.PUSH);
		styled.setText("Styled default button");
		styled.setBackground(new Color(0x00, 0x71, 0xBC));
		styled.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		styled.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button plain = new Button(shell, SWT.PUSH);
		plain.setText("Unstyled button");
		plain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		shell.setDefaultButton(styled);
		shell.setSize(360, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
