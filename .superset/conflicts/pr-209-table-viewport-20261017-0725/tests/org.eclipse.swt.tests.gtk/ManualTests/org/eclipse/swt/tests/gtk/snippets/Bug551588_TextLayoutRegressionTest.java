/*******************************************************************************
 * Copyright (c) 2019 Peter Severin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Peter Severin - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class Bug551588_TextLayoutRegressionTest {

	public static void main(String[] args) {
		Display display = new Display();

		Font font = display.getSystemFont();
		FontData fontData = font.getFontData()[0];
		fontData.setHeight(100);
		Font bigFont = new Font(display, fontData);

		test(display, bigFont, 0);
		test(display, bigFont, 1);
		test(display, bigFont, 2);
		test(display, bigFont, 3);

		bigFont.dispose();

		display.dispose();
	}

	private static void test(Display display, Font bigFont, int line)
	{
		TextLayout textLayout = new TextLayout(display);
		textLayout.setText("a\na\na\na");
		textLayout.setStyle(new TextStyle(bigFont, null, null), line * 2, line * 2);
		System.out.println("Text bounds: " + textLayout.getBounds());
		textLayout.dispose();
	}
}