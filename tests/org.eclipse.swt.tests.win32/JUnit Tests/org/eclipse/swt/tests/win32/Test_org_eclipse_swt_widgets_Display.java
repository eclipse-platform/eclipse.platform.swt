/*******************************************************************************
 * Copyright (c) 2021 Joerg Kubitz
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import org.eclipse.swt.widgets.Display;
import org.junit.Test;

public class Test_org_eclipse_swt_widgets_Display {

	private final class DisplayExtension extends Display {
		@Override
		public boolean isXMouseActive() {
			return super.isXMouseActive();
		}
		@Override
		protected void checkSubclass () {
			//dont
		}
	}

	@Test
	public void test_isXMouseActive() {
		DisplayExtension display = new DisplayExtension();
		try {
			boolean xMouseActive = display.isXMouseActive();
			System.out.println("org.eclipse.swt.widgets.Display.isXMouseActive(): " + xMouseActive);
		} finally {
			display.dispose();
		}
	}
}
