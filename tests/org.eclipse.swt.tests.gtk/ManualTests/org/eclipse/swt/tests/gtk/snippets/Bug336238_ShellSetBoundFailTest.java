/*******************************************************************************
 * Copyright (c) 2018, 2025 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;

public class Bug336238_ShellSetBoundFailTest {

	static int cycles = 100;

	@Test
	public void testSetBounds() {

		int x;
		int y;
		int width = 100;
		int height = 100;

		for (int i = 0; i < cycles; i++) {

			x = (Double.valueOf(Math.random() * 1000)).intValue();
			y = (Double.valueOf(Math.random() * 1000)).intValue() + 27;

			Shell testShell = new Shell();
			testShell.open();

			testShell.setBounds(x, y, width, height);

			assertEquals(x, testShell.getLocation().x);
			assertEquals(y, testShell.getLocation().y);
			testShell.close();
		}
	}

}
