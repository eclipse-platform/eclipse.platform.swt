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
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/*
 * Title: Bug 528284: [GTK3] Toolbar: TCF dropdown control is cutoff
 * How to run: launch snippet and observe ToolBar inside the Shell
 * Bug description: the Label and Button are cut off on the bottom
 * Expected results: the Label and Button should be displayed as expected
 * GTK Version(s): GTK3
 */
public class Bug528284_TCFToolBarIssue {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		ToolBar bar = new ToolBar (shell, SWT.HORIZONTAL);

		Composite panel = new Composite(bar, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 1; layout.marginWidth = 1;
		panel.setLayout(layout);


		Composite labelPanel = new Composite(panel, SWT.BORDER);
		labelPanel.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		labelPanel.setLayoutData(layoutData);
		layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		labelPanel.setLayout(layout);

		Label image = new Label(labelPanel, SWT.NONE);
		layoutData = new GridData(SWT.LEAD, SWT.CENTER, false, true);
		layoutData.horizontalIndent = 1;
		layoutData.minimumWidth=20;
		layoutData.widthHint=20;
		image.setLayoutData(layoutData);

		Label text = new Label(labelPanel, SWT.NONE);
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		layoutData.minimumWidth = 25;
		text.setLayoutData(layoutData);

		Button button = new Button(labelPanel, SWT.ARROW | SWT.DOWN | SWT.FLAT | SWT.NO_FOCUS);
		layoutData = new GridData(SWT.TRAIL, SWT.CENTER, false, true);
		layoutData.minimumWidth=20;
		layoutData.widthHint = 20;
		button.setLayoutData(layoutData);

		text.setText("this is a test");


		ToolItem item = new ToolItem(bar, SWT.SEPARATOR);
		item.setControl(panel);
		item.setWidth(panel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);

		bar.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}