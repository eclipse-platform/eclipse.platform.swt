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


import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.LineStyleListener
 *
 * @see org.eclipse.swt.custom.LineStyleListener
 */
public class Test_org_eclipse_swt_custom_LineStyleListener {
	Shell shell;
	StyledText styledText;

@Before
public void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

@Test
public void test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent() {
	styledText.setText("0123456789");
	LineStyleListener listener = event -> {
		assertEquals(0, event.lineOffset);
		assertEquals("0123456789",event.lineText);
	};
	styledText.addLineStyleListener(listener);
	// force get line styles callback
	styledText.getLocationAtOffset(5);
	styledText.removeLineStyleListener(listener);
}
}
