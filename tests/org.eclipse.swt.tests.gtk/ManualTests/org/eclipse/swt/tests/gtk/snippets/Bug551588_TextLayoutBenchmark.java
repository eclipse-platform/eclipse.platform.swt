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

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

public class Bug551588_TextLayoutBenchmark {
	final static String longString;

	static {
		String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			b.append(loremIpsum);
		}
		longString = b.toString();
	}

	public static void main(String[] args) {
		Display display = new Display();

		long start = System.currentTimeMillis();
		TextLayout textLayout = new TextLayout(display);
		textLayout.setText(longString);
		textLayout.setWidth(1);
		Rectangle textBounds = textLayout.getBounds();
		long end = System.currentTimeMillis();

		System.out.println("Time: " + (end - start) + "ms");
		System.out.println("Text bounds: " + textBounds);

		textLayout.dispose();

		display.dispose();
	}
}