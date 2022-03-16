/*******************************************************************************
 * Copyright (c) 2022 Simeon Andreev and others. All rights reserved.
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Description: Toolbar items in a toolbar overflow submenu (enabled with style flag {@link SWT.WRAP})
 * do not show correct image enablement.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Resize the {@link Shell}, so that only some of the icons are visible.</li>
 * <li>Mouse-over the first toolbar button, which is enabled.</li>
 * <li>The overflow button (downward arrow) should be visible to the right now.</li>
 * <li>Click it, observe that the disabled buttons don't have disabled images.</li>
 * <li>Click one of the overflow submenu items.</li>
 * <li>Show the overflow menu again, observe that the image of the clicked item is not changed.</li>
 * </ol>
 * </p>
 * Expected results: Images of disabled buttons should appear grayed out.
 *                   Images should change on overflow submenu item click.
 *                   Text and tooltip of an overflow submenu item should also change on click.
 *                   Clicking on overflow submenu items should result in only 1 widget selected notification (see console output).
 * Actual results: Images of disabled buttons in the overflow submenu are not grayed out.
 *                 Images are not updated on overflow submenu item click.
 */
public class Bug579283_TestToolbarOverflow_itemEnablement {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bug 579283 test enablement of overflowing toolbar items");
		shell.setLayout(new FillLayout());
		Composite c1 = new Composite(shell, SWT.BORDER);
		c1.setLayout(new FillLayout());

		ToolBar toolBar = new ToolBar(c1, SWT.WRAP);
		toolBar.setLayout(new RowLayout());

		Image[] icons = {
				display.getSystemImage(SWT.ICON_INFORMATION),
				display.getSystemImage(SWT.ICON_WARNING),
				display.getSystemImage(SWT.ICON_ERROR),
		};

		for (int i = 0; i < 8; ++i) {
			final int index = i;
			ToolItem item = new ToolItem(toolBar, SWT.CHECK);
			item.setImage(icons[0]);
			item.setText(String.valueOf(i));
			item.setToolTipText(String.valueOf(i));
			if (i % 2 == 0) {
				item.setEnabled(false);
			}
			item.addSelectionListener(new SelectionAdapter() {
				int iconIndex = 0;
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Toolbar button " + index + " clicked!");
					++iconIndex;
					item.setImage(icons[iconIndex % icons.length]);
					item.setText(String.valueOf(index) + "[" + iconIndex + "]");
					item.setToolTipText(String.valueOf(index) + "[" + iconIndex + "]");
				}
			});
		}

		shell.setSize(500, 100);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

}