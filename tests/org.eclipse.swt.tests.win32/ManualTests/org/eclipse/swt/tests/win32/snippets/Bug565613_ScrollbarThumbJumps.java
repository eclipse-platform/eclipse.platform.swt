/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
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
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug565613_ScrollbarThumbJumps {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(1, true);
		layout.horizontalSpacing = 10;
		shell.setLayout(layout);

		Label hint = new Label(shell, 0);
		hint.setText(
			"1. Drag scrollbar thumb with you mouse\n" +
			"2. The problem is that thumb \"jumps\" each time scrollbar reaches new value"
		);

		{
			final Composite drawArea = new Composite(shell, SWT.BORDER | SWT.V_SCROLL);
			drawArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			final ScrollBar scrollBarV = drawArea.getVerticalBar();
			scrollBarV.setValues(10, 0, 20, 1, 1, 10);

			drawArea.addListener(SWT.Paint, event -> {
				int scrollValueV = scrollBarV.getSelection();
				final Point size = drawArea.getSize();
				final int rowHeight = event.gc.getFontMetrics().getHeight();

				int x = 0;
				int y = 0;
				while (y < size.y) {
					event.gc.drawString(Integer.toString(scrollValueV++), x, y);
					y += rowHeight;
				}
			});

			scrollBarV.addListener(SWT.Selection, event -> drawArea.redraw());
		}

		shell.setSize(500, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
