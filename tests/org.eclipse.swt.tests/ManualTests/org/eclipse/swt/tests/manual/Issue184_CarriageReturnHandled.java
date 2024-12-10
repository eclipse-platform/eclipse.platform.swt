/*******************************************************************************
 * Copyright (c) 2024 IBM and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Issue184_CarriageReturnHandled {

	private static FontData[] getFontData(Font font, int style) {
		FontData[] fontDatas = font.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(style);
		}
		return fontDatas;
	}

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setText("Underline, Strike Out");
		String text1 = "\r\nde\nep\rika\r\rudaya\n\ngiri\r";

		FontData[] fontData = getFontData(shell.getFont(), SWT.BOLD);
		Font font = new Font(shell.getDisplay(), fontData);

		FontData[] fontData1 = getFontData(shell.getFont(), SWT.ITALIC | SWT.BOLD);
		Font font1 = new Font(shell.getDisplay(), fontData1);

		FontData[] fontData2 = getFontData(shell.getFont(), SWT.BOLD);
		Font font2 = new Font(shell.getDisplay(), fontData2);

		final TextLayout layout = new TextLayout(display);
		layout.setText(text1);

		TextStyle style1 = new TextStyle(font, null, null);
		layout.setStyle(style1, 3, 7); // eep in bold

		TextStyle style2 = new TextStyle(font1, null, null);
		layout.setStyle(style2, 12, 18); // udaya in bold

		TextStyle style3 = new TextStyle(font2, null, null);
		layout.setStyle(style3, 21, 24); // iri in bold

		shell.addListener(SWT.Paint, event -> {
			Point point = new Point(10, 10);
			int width = shell.getClientArea().width - 2 * point.x;
			layout.setWidth(width);
			layout.draw(event.gc, point.x, point.y);
		});
		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		layout.dispose();
		display.dispose();
	}
}
