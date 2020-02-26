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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Bug560546_GC_drawString_vs_GC_drawText {
	static Font fontSegoeUI;
	static Font fontSegoeUISymbol;

	static class TestString
	{
		TestString(String caption, String string) {
			this.caption = caption;
			this.string  = string;
		}

		String caption;
		String string;
	}

	static TestString testStrings[] = {
		new TestString("1-char string",   "A"),
		new TestString("10-char string",  "ABCDEFGHIJ"),
		new TestString("100-char string", "ABCDEFGHIJ012345678'ABCDEFGHIJ012345678'ABCDEFGHIJ012345678'ABCDEFGHIJ012345678'ABCDEFGHIJ012345678'"),
	};

	static abstract class TestFunction
	{
		String caption;
		abstract void function(GC gc, int x, int y, String string);
	}

	static class TestDrawString extends TestFunction {
		TestDrawString() {
			caption = "GC.drawString";
		}

		@Override
		void function(GC gc, int x, int y, String string) {
			gc.drawString(string, x, y);
		}
	}

	static class TestDrawText extends TestFunction {
		TestDrawText() {
			caption = "GC.drawText";
		}

		@Override
		void function(GC gc, int x, int y, String string) {
			gc.drawText(string, x, y);
		}
	}

	static TestFunction testFunctions[] = {
		new TestDrawText(),
		new TestDrawString(),
	};

	static void testSpeeds(Shell shell) {
		final int finalIterations = 20 * 1000;
		String report = "";

		Image image = new Image(shell.getDisplay(), 2000, 100);
		GC gc = new GC(image);
		for (int isFinalCalc = 0; isFinalCalc < 2; isFinalCalc++) {
			for (int iTestString = 0; iTestString < testStrings.length; iTestString++) {
				for (int iTestFunction = 0; iTestFunction < testFunctions.length; iTestFunction++) {
					TestString testString = testStrings[iTestString];
					TestFunction testFunction = testFunctions[iTestFunction];

					// Warm up before measuring
					final int iterations = (isFinalCalc != 0) ? finalIterations : 10;

					final long time1 = System.nanoTime();
					for (int iIteration = 0; iIteration < iterations; iIteration++) {
						testFunction.function(gc, 0, 0, testString.string);
					}
					final long time2 = System.nanoTime();

					if (isFinalCalc != 0) {
						final double elapsed = (time2 - time1) / 1000000000.0;
						report += String.format("%s, %s - %.3f sec\n", testString.caption, testFunction.caption, elapsed);
					}
				}
			}
		}

		gc.dispose();
		image.dispose();

		MessageBox messageBox = new MessageBox(shell);
		messageBox.setMessage(report);
		messageBox.open();
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		Font systemFont = display.getSystemFont();
		int systemFontHeight = systemFont.getFontData()[0].getHeight();
		fontSegoeUI = new Font(display, "Segoe UI", systemFontHeight, 0);
		fontSegoeUISymbol = new Font(display, "Segoe UI Symbol", systemFontHeight, 0);

		final Button btnTestSpeed = new Button(shell, SWT.PUSH);
		btnTestSpeed.setText("Test speeds");
		btnTestSpeed.addListener(SWT.Selection, event -> testSpeeds(shell));
		btnTestSpeed.setBounds(10, 10, 100, 20);

		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.NONE);
		canvas.addListener(SWT.Paint, event -> {
			final GC gc = event.gc;

			int x = 10;
			int y = 10;

			gc.setFont(fontSegoeUI);
			gc.drawText("\u26b7 Segoe UI, GC.drawText", x, y);
			y += 20;

			gc.setFont(fontSegoeUISymbol);
			gc.drawText("\u26b7 Segoe UI Symbol, GC.drawText", x, y);
			y += 20;

			gc.setFont(fontSegoeUI);
			gc.drawString("\u26b7 Segoe UI, GC.drawString", x, y);
			y += 20;

			gc.setFont(fontSegoeUISymbol);
			gc.drawString("\u26b7 Segoe UI, GC.drawString", x, y);
		});
		canvas.setBounds(10, 40, 300, 200);

		shell.setSize(400, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}