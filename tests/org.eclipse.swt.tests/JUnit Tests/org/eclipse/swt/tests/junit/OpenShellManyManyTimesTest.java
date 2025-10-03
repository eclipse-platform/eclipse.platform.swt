/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.RepeatedTest;


public class OpenShellManyManyTimesTest {

	@RepeatedTest(value = 1000)
	public void test_OpenShell() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}

		Shell shell = new Shell(display);
		try {
		shell.open();
		shell.setFocus();
		SwtTestUtil.processEvents();
		} finally {
			shell.dispose();
		}
	}
}
