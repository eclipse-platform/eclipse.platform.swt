/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
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
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * Description: Combo box drop-down menu is narrower than combo itself, for read-only combos.
 * Steps to reproduce:
 * <ol>
 * <li>click on main menu entry "Window"</li>
 * <li>click on "Preferences"</li>
 * <li>choose "Help"</li>
 * <li>click on any combo in the preference page</li>
 * </ol>
 * Expected results: The drop down menu is as long as the combo.
 * Actual results: The drop down menu is only as long as its longest entry.
 */
public class Bug528498_Combo_dropDownMenu_is_too_short {

	public static void main (String [] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setSize(200, 50);
		shell.setLayout(new FillLayout());
		shell.setLocation(center(display, shell));
		shell.setText("Bug 528498");

		Combo c = new Combo(shell, SWT.READ_ONLY);

		for (int i = 0; i < 40; ++i) {
			c.add("item " + i);
		}

		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static Point center(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Point shellBounds = shell.getSize();
		Point center = new Point(
				displayBounds.x + (displayBounds.width - shellBounds.x) / 2,
				displayBounds.y + (displayBounds.height - shellBounds.y) / 2);

		return center;
	}
}
