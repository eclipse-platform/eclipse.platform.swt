/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * SWT StyledText snippet: use rise and font with StyleRange.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.custom.*;

public class Snippet211 {

	static String text =
		"You can set any font you want in a range. You can also set a baseline rise and all other old features" +
		" like background and foreground, and mix them any way you want. Totally awesome.";

	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		StyledText styledText = new StyledText(shell, SWT.WRAP | SWT.BORDER);
		styledText.setText(text);
		FontData data = styledText.getFont().getFontData()[0];
		Font font1 = new Font(display, data.getName(), data.getHeight() * 2, data.getStyle());
		Font font2 = new Font(display, data.getName(), data.getHeight() * 4 / 5, data.getStyle());
		StyleRange[] styles = new StyleRange[8];
		styles[0] = new StyleRange();
		styles[0].font = font1;
		styles[1] = new StyleRange();
		styles[1].rise = data.getHeight() / 3;
		styles[2] = new StyleRange();
		styles[2].background = display.getSystemColor(SWT.COLOR_GREEN);
		styles[3] = new StyleRange();
		styles[3].foreground = display.getSystemColor(SWT.COLOR_MAGENTA);
		styles[4] = new StyleRange();
		styles[4].font = font2;
		styles[4].foreground = display.getSystemColor(SWT.COLOR_BLUE);
		styles[4].underline = true;
		styles[5] = new StyleRange();
		styles[5].rise = -data.getHeight() / 3;
		styles[5].strikeout = true;
		styles[5].underline = true;
		styles[6] = new StyleRange();
		styles[6].font = font1;
		styles[6].foreground = display.getSystemColor(SWT.COLOR_YELLOW);
		styles[6].background = display.getSystemColor(SWT.COLOR_BLUE);
		styles[7] = new StyleRange();
		styles[7].rise =  data.getHeight() / 3;
		styles[7].underline = true;
		styles[7].fontStyle = SWT.BOLD;
		styles[7].foreground = display.getSystemColor(SWT.COLOR_RED);
		styles[7].background = display.getSystemColor(SWT.COLOR_BLACK);

		int[] ranges = new int[] {16, 4, 61, 13, 107, 10, 122, 10, 134, 3, 143, 6, 160, 7, 168, 7};
		styledText.setStyleRanges(ranges, styles);

		shell.setSize(300, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		font1.dispose();
		font2.dispose();
		display.dispose();
	}
}
