/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug209975_PopupListOpenFocus {
	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display);

		GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);

		Text text1 = new Text(shell, SWT.SINGLE);
		Text text2 = new Text(shell, SWT.SINGLE);
		text2.setText("Text 2");

		text1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				PopupList popupList = new PopupList(shell);
				String[] items = new String[100];
				for (int i = 0; i < 100; i++) {
					items[i] = "Item " + i;
				}
				popupList.setItems(items);
				Rectangle reactangle = new Rectangle(shell.getBounds().x, shell.getBounds().y, 300, 300);
				String chosenItem = popupList.open(reactangle);
				if (chosenItem != null) {
					text1.setText(chosenItem);
				}
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
