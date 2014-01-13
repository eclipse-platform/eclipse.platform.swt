/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.VerifyKeyListener
 *
 * @see org.eclipse.swt.custom.VerifyKeyListener
 */
public class Test_org_eclipse_swt_custom_VerifyKeyListener extends TestCase {
	Shell shell;
	StyledText styledText;
	int verify = -1;

@Override
protected void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

public void test_verifyKeyLorg_eclipse_swt_events_VerifyEvent() {
	VerifyKeyListener listener = new VerifyKeyListener() {
		public void verifyKey(VerifyEvent event) {
			if (verify != 1) {event.doit = false;}
		}
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
	assertTrue(":2:", styledText.getText().equals(""));
	styledText.removeVerifyKeyListener(listener);
}
}
