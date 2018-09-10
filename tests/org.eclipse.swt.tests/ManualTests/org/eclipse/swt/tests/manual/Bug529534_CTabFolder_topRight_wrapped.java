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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Description: Closing a tab in a part stack does not hide toolbar line, if toolbar was below tabs line.
 *
 * <ol>
 * <li>Open many views in the same part stack, so that the toolbar of the last view is rendered on a line below the tabs.</li>
 * <li>Close views in the that part stack, until the toolbar of the current view is rendered in the same line as the tabs.</li>
 * </ol>
 * Expected results: The line in which the toolbar was rendered vanishes on step 2.
 * Actual results: The line remains until a resize is done.
 */
public class Bug529534_CTabFolder_topRight_wrapped {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(300, 300);
		shell.setText("Bug 529534 - missing CTabFolder layout update");
		shell.setLayout(new FillLayout());

		CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
		folder.setTabHeight(26);

		Composite folderTopRight = new Composite(folder, SWT.BORDER);
		RowLayout rl = new RowLayout();
		folderTopRight.setLayout(rl);
		rl.marginBottom = 0;
		rl.marginTop = 0;
		rl.marginRight = 0;
		rl.marginLeft = 0;
		folder.setTopRight(folderTopRight, SWT.RIGHT | SWT.WRAP);

		ToolBar toolbar1 = new ToolBar(folderTopRight, SWT.BORDER);

		for (int i = 0; i < 4; ++i) {
			ToolItem ti1 = new ToolItem(toolbar1, SWT.PUSH);
			ti1.setImage(image(display, SWT.COLOR_BLUE));
			ti1.setHotImage(null);
		}

		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayout(new FillLayout());

		for (int i = 1; i <= 2; ++i) {
			String[] steps = {
					"1. Close \"some tab 2\".",
					"2. Observe that grey toolbar line remains.",
					"   (line between this text field and the tabs)",
					"3. Resize shell, observe that line vanishes."
			};
			Text text = new Text(folder, SWT.BORDER | SWT.MULTI);
			text.setText(String.join(System.lineSeparator(), steps));
			CTabItem tab = new CTabItem(folder, SWT.CLOSE);
			tab.setText("some tab " + i);
			tab.setControl(text);
		}

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Image image(Display display, int shapeColor) {
		Rectangle bounds = new Rectangle(0, 0, 16, 16);
		Image image = new Image(display, bounds);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(bounds);
		gc.setBackground(display.getSystemColor(shapeColor));
		gc.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		gc.dispose();
		return image;
	}
}
