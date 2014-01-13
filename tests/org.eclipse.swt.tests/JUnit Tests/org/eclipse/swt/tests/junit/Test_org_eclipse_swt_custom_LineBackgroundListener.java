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
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.LineBackgroundListener
 *
 * @see org.eclipse.swt.custom.LineBackgroundListener
 */
public class Test_org_eclipse_swt_custom_LineBackgroundListener extends TestCase {
	Shell shell;
	StyledText styledText;

@Override
protected void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

public void test_lineGetBackgroundLorg_eclipse_swt_custom_LineBackgroundEvent() {
	LineBackgroundListener listener = new LineBackgroundListener() {
		public void lineGetBackground(LineBackgroundEvent event) {
			assertTrue(":1:", event.lineOffset==0);
			assertTrue(":2:",event.lineText.equals("0123456789"));
		}
	};
	styledText.addLineBackgroundListener(listener);
	styledText.setText("0123456789");
	// force get line bg callback
	styledText.selectAll();
	styledText.copy();
	styledText.removeLineBackgroundListener(listener);
}
}
