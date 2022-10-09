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
package org.eclipse.swt.tests.win32;

import org.eclipse.swt.tests.win32.widgets.TestTreeColumn;
import org.eclipse.swt.tests.win32.widgets.Test_org_eclipse_swt_widgets_Display;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	Test_org_eclipse_swt_dnd_DND.class,
	Test_org_eclipse_swt_widgets_Display.class,
	TestTreeColumn.class
})

public class AllWin32Tests {

	public static void main(String[] args) {
		JUnitCore.main(AllWin32Tests.class.getName());
	}

}
