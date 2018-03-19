/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 */
package org.eclipse.swt.tests.manualJUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/** Convienience class for easy copy & paste */
@FixMethodOrder(MethodSorters.JVM)
public class MJ_Template extends MJ_root {

	@Test
	public void test_template() {
		Shell shell = mkShell("Instructions: Pass:F1  Fail:F2  Skip:F3  Exit:Esc");

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Hello World");
		button.pack();
		button.setLocation(10, 10);

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

}
