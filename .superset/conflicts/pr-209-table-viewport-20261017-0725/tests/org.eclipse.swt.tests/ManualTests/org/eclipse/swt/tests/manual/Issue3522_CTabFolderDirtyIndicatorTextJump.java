/*******************************************************************************
 * Copyright (c) 2026 Vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vogella GmbH - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Press the two buttons alternately: the text of the selected tab must look exactly the
 * same, no matter whether the dirty tab is repainted along with it or not.
 */
public class Issue3522_CTabFolderDirtyIndicatorTextJump {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));

		CTabFolder folder = new CTabFolder(shell, SWT.BORDER | SWT.CLOSE);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		folder.setUnselectedCloseVisible(false);
		folder.setDirtyIndicatorStyle(true);

		CTabItem selectedItem = new CTabItem(folder, SWT.NONE);
		selectedItem.setText("TestClass.java");
		CTabItem dirtyItem = new CTabItem(folder, SWT.NONE);
		dirtyItem.setText("module-info.java");
		dirtyItem.setShowDirty(true);
		folder.setSelection(selectedItem);

		Button redrawAll = new Button(shell, SWT.PUSH);
		redrawAll.setText("Repaint all tabs (dirty indicator is painted first)");
		redrawAll.addListener(SWT.Selection, event -> {
			folder.redraw();
			folder.update();
		});

		Button redrawSelected = new Button(shell, SWT.PUSH);
		redrawSelected.setText("Repaint selected tab only");
		redrawSelected.addListener(SWT.Selection, event -> {
			Rectangle bounds = selectedItem.getBounds();
			folder.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
			folder.update();
		});

		shell.setSize(700, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
