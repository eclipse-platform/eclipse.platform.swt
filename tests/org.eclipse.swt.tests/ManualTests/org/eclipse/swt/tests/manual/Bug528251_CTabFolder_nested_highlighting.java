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
 *******************************************************************************/

package org.eclipse.swt.tests.manual;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: Console View tab is not highlighted when selected.
 * Steps to reproduce:
 * <ol>
 * <li>Open the Console View.</li>
 * <li>Open a view with a CTabFolder in the same part stack as the Console View.</li>
 * <li>Select a tab in the new view.</li>
 * <li>Select the Console View.</li>
 * </ol>
 * Expected results: Console View tab is highlighted, indicating a selection.
 * Actual results: Console View tab is not highlighted.
 */
public class Bug528251_CTabFolder_nested_highlighting {

	private static final String[] REPRODUCTION_STEPS = {
			"1. select \"Another View\" tab",
			"2. select \"nested tab\"",
			"3. select \"Console View\" tab",
			"4. \"Console View\" tab must be highlighted",
	};

	private final Display display;
	private final Shell shell;

	private final CTabFolder partStackTabs;
	private final CTabItem anotherViewTab;

	public Bug528251_CTabFolder_nested_highlighting() {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(550, 200);
		shell.setLayout(new FillLayout());
		shell.setLocation(center(display, shell));
		shell.setText("Bug 528251");

		partStackTabs = new CTabFolder(shell, SWT.NONE);

		// Console View
		CTabItem consoleViewTab = new CTabItem(partStackTabs, SWT.NONE);
		consoleViewTab.setText("Console View");

		// some other view with a CTabFolder
		SashForm anotherView = new SashForm(partStackTabs, SWT.NONE);
		anotherViewTab = new CTabItem(partStackTabs, SWT.NONE);
		anotherViewTab.setText("Another View");
		anotherViewTab.setControl(anotherView);
		CTabFolder anotherViewNestedTabs = new CTabFolder(anotherView, SWT.NONE);
		CTabItem anotherViewNestedTab = new CTabItem(anotherViewNestedTabs, SWT.NONE);
		anotherViewNestedTab.setText("nested tab");

		// what to do to see the problem
		StyledText text = new StyledText(shell, SWT.NONE);
		text.setText(String.join(System.lineSeparator(), REPRODUCTION_STEPS));
		text.setEditable(false);
	}

	void showCase() {
		shell.open();
		waitForClose();
	}

	private void waitForClose() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Point center(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Point shellBounds = shell.getSize();
		Point center = new Point(
				displayBounds.x + (displayBounds.width - shellBounds.x) / 2,
				displayBounds.y + (displayBounds.height - shellBounds.y) / 2);

		return center;
	}


	public static void main(String[] args) {
		Bug528251_CTabFolder_nested_highlighting bug = new Bug528251_CTabFolder_nested_highlighting();
		bug.showCase();
	}
}
