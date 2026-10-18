/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.LineBackgroundListener
 *
 * @see org.eclipse.swt.custom.LineBackgroundListener
 */
public class Test_org_eclipse_swt_custom_LineBackgroundListener {
	Shell shell;
	StyledText styledText;

@BeforeEach
public void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

@Tag("clipboard")
@Test
public void test_lineGetBackgroundLorg_eclipse_swt_custom_LineBackgroundEvent() {
	LineBackgroundListener listener = event -> {
		assertEquals(0, event.lineOffset);
		assertEquals("0123456789", event.lineText);
	};
	styledText.addLineBackgroundListener(listener);
	styledText.setText("0123456789");
	// force get line bg callback
	styledText.selectAll();
	styledText.copy();
	styledText.removeLineBackgroundListener(listener);
}
}
