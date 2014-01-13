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
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.LineStyleListener
 *
 * @see org.eclipse.swt.custom.LineStyleListener
 */
public class Test_org_eclipse_swt_custom_LineStyleListener extends TestCase {
	Shell shell;
	StyledText styledText;

@Override
protected void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

public void test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent() {
	LineStyleListener listener = new LineStyleListener() {
		public void lineGetStyle(LineStyleEvent event) {
			assertTrue(":1:", event.lineOffset==0);
			assertTrue(":2:",event.lineText.equals("0123456789"));
		}
	};
	styledText.addLineStyleListener(listener);
	styledText.setText("0123456789");
	// force get line styles callback
	styledText.getLocationAtOffset(5);
	styledText.removeLineStyleListener(listener);
}
}
