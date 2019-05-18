/**
 *  Copyright (c) 2018 Angelo ZERR.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - StyledText.setStyleRange resets less cache - Bug 530019
 */
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledText to check there is no regression with Bug 530019
 *
 * @see org.eclipse.swt.custom.StyledText
 */
public class Test_org_eclipse_swt_custom_StyledText_VariableLineHeight {

	Shell shell;
	StyledText styledText;

	@Before
	public void setUp() {
		shell = new Shell();
		styledText = new StyledText(shell, SWT.NULL);
		styledText.setLineSpacingProvider(l -> 0);
	}

	@After
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void testRise() {
		styledText.setText("a\nb\nc\nd");
		int length = styledText.getCharCount();

		// No style
		assertVariableLineHeightEquals(0, 0);

		// style with rise=1
		StyleRange style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.rise = 1;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(1, 0);

		// style with rise=0
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.rise = 0;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0);

		// style with rise=2
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.rise = 2;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(2, 0);

		// style with rise=2 again
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.rise = 2;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(2, 0);
	}

	@Test
	public void testFont() {
		styledText.setText("a\nb\nc\nd");

		// Create little, big font
		Font initialFont = styledText.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(24);
		}
		Font bigFont = new Font(styledText.getDisplay(), fontData);
		fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(fontData[i].getHeight() - 1);
		}
		Font littleFont = new Font(styledText.getDisplay(), fontData);

		int length = styledText.getCharCount();

		// No style
		assertVariableLineHeightEquals(0, 0);

		// style with big font
		StyleRange style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = bigFont;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0, bigFont);

		// style with no font
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = null;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0);

		// style with big font
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = bigFont;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0, bigFont);

		// style with little font
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = littleFont;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0);

		// style with no font
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = null;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0);

		// style with little font
		style = new StyleRange();
		style.start = 0;
		style.length = length;
		style.foreground = styledText.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		style.font = littleFont;
		styledText.replaceStyleRanges(0, length, new StyleRange[] { style });
		assertVariableLineHeightEquals(0, 0);

		bigFont.dispose();
		littleFont.dispose();
	}

	@Test
	public void testLittleFontSeveralRanges() {
		styledText.setText("abc\nd");

		// Create little, big font
		Font initialFont = styledText.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(fontData[i].getHeight() / 2);
		}
		Font littleFont1 = new Font(styledText.getDisplay(), fontData);
		fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(fontData[i].getHeight() / 4);
		}
		Font littleFont2 = new Font(styledText.getDisplay(), fontData);

		StyleRange[] ranges = new StyleRange[2];
		ranges[0] = new StyleRange();
		ranges[0].start = 0;
		ranges[0].length = 1;
		ranges[0].font = littleFont1;

		ranges[1] = new StyleRange();
		ranges[1].start = 1;
		ranges[1].length = 1;
		ranges[1].font = littleFont2;

		styledText.replaceStyleRanges(0, 3,ranges);
		// 2 little fonts, line height is the height of the default font of StyledText
		assertVariableLineHeightEquals(0, 0);

		littleFont1.dispose();
		littleFont2.dispose();
	}

	@Test
	public void testBigFontSeveralRanges() {
		styledText.setText("abc\nd");

		// Create little, big font
		Font initialFont = styledText.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(fontData[i].getHeight() * 2);
		}
		Font bigFont = new Font(styledText.getDisplay(), fontData);
		fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(fontData[i].getHeight() / 2);
		}
		Font littleFont = new Font(styledText.getDisplay(), fontData);


		StyleRange[] ranges = new StyleRange[2];
		ranges[0] = new StyleRange();
		ranges[0].start = 0;
		ranges[0].length = 1;
		ranges[0].font = bigFont;

		ranges[1] = new StyleRange();
		ranges[1].start = 1;
		ranges[1].length = 1;
		ranges[1].font = littleFont;

		styledText.replaceStyleRanges(0, 3,ranges);
		// line height is the height of the big font
		assertVariableLineHeightEquals(0, 0, bigFont);

		bigFont.dispose();
		littleFont.dispose();
	}

	private void assertVariableLineHeightEquals(int expected, int lineIndex) {
		assertVariableLineHeightEquals(expected, lineIndex, null);
	}

	private void assertVariableLineHeightEquals(int expected, int lineIndex, Font font) {
		int fontHeight = styledText.getLineHeight();
		if (font != null) {
			TextLayout layout = new TextLayout(font.getDevice());
			layout.setFont(font);
			layout.setText("a");
			fontHeight = layout.getBounds().height;
			layout.dispose();
		}
		int actual = styledText.getLinePixel(lineIndex + 1) - styledText.getLinePixel(lineIndex) - fontHeight;
		Assert.assertEquals(expected, actual);
	}

}