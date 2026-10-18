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
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link CTabItem} in {@link CTabFolder} are not drawn if {@link CTabFolder} is placed
 * below some other widget and is using {@link SWT#RIGHT_TO_LEFT}.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Observe tabs of tab folder.</li>
 * </ol>
 * Expected results: The tabs of the tab folder are seen.
 * Actual results: The tabs of the tab folder are not seen.
 */
public class Bug531667_CTabFolder_right_to_left_tabs_are_not_painted {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 300);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setText("Bug 531667: tab painting is broken");

		Composite c = new Composite(shell, SWT.NONE);
		c.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		CTabFolder tabFolder = new CTabFolder(shell, SWT.NONE | SWT.RIGHT_TO_LEFT);
		createTab(tabFolder, "Tab 1", SWT.COLOR_GREEN);
		createTab(tabFolder, "Tab 2", SWT.COLOR_YELLOW);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static CTabItem createTab(CTabFolder parent, String text, int color) {
		CTabItem tab1 = new CTabItem(parent, SWT.NONE);
		tab1.setText(text);
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(comp.getDisplay().getSystemColor(color));
		tab1.setControl(comp);
		return tab1;
	}

}
