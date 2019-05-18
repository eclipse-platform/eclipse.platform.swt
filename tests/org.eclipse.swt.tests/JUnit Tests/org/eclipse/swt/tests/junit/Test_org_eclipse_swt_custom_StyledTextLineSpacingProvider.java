/**
 *  Copyright (c) 2017 Angelo ZERR.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 */
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextLineSpacingProvider;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class
 * org.eclipse.swt.custom.StyledTextLineSpacingProvider
 *
 * @see org.eclipse.swt.custom.StyledTextLineSpacingProvider
 */
public class Test_org_eclipse_swt_custom_StyledTextLineSpacingProvider {

	class IntegerLineSpacingProvider implements StyledTextLineSpacingProvider {

		@Override
		public Integer getLineSpacing(int lineIndex) {
			try {
				String text = styledText.getLine(lineIndex);
				return Integer.parseInt(text);
			} catch (Exception e) {
				return null;
			}
		}
	}

	Shell shell;
	StyledText styledText;

	@Before
	public void setUp() {
		shell = new Shell();
		styledText = new StyledText(shell, SWT.NULL);
	}

	@Test
	public void test_setLineSpacingProvider() {
		styledText.setText("a\n45\nc\n57");
		// Tests with none line spacing provider
		assertLineSpacingEquals(0, 0, 0, 0, 0);
		// Tests with integer line spacing provider
		styledText.setLineSpacingProvider(new IntegerLineSpacingProvider());
		assertLineSpacingEquals(0, 0, 45, 0, 57);
		// Tests with reset line spacing provider
		styledText.setLineSpacingProvider(null);
		assertLineSpacingEquals(0, 0, 0, 0, 0);
	}

	@Test
	public void test_replaceTextRange() {
		styledText.setText("a\n45\nc\n57");
		styledText.setLineSpacingProvider(new IntegerLineSpacingProvider());
		assertLineSpacingEquals(0, 0, 45, 0, 57);
		// Replace 45 with b
		styledText.replaceTextRange(2, 2, "b");
		assertLineSpacingEquals(0, 0, 0, 0, 57);
		// Replace b with 45
		styledText.replaceTextRange(2, 1, "45");
		assertLineSpacingEquals(0, 0, 45, 0, 57);
	}

	private void assertLineSpacingEquals(int... expecteds) {
		int lineHeight = styledText.getLineHeight();
		int[] actuals = new int[expecteds.length];
		for (int i = 0; i < expecteds.length; i++) {
			actuals[i] = styledText.getLinePixel(i) - ((i > 0) ? styledText.getLinePixel(i - 1) + lineHeight : 0);
		}
		assertArrayEquals(expecteds, actuals);
	}
}
