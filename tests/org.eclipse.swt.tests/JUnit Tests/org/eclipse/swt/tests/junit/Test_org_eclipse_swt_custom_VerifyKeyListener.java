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


import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.VerifyKeyListener
 *
 * @see org.eclipse.swt.custom.VerifyKeyListener
 */
public class Test_org_eclipse_swt_custom_VerifyKeyListener {
	Shell shell;
	StyledText styledText;
	int verify = -1;

@Before
public void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

@Test
public void test_verifyKeyLorg_eclipse_swt_events_VerifyEvent() {
	VerifyKeyListener listener = event -> {
		if (verify != 1) {event.doit = false;}
	};
	styledText.addVerifyKeyListener(listener);
	verify = 1;
	Event e = new Event();
	e.character = 'a';
	styledText.notifyListeners(SWT.KeyDown, e);
	assertTrue(":1:", styledText.getText().equals("a"));

	verify = 2;
	styledText.setText("");
	e = new Event();
	e.character = 'a';
	styledText.notifyListeners(SWT.KeyDown, e);
	assertTrue(":2:", styledText.getText().isEmpty());
	styledText.removeVerifyKeyListener(listener);
}
}
