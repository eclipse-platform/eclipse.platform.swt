/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Create a CTabFolder with min and max buttons, as well as close button and
 * image only on selected tab.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet165 {

public static void main (String [] args) {
	Display display = new Display ();
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, 0, 16, 16);
		gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(3, 3, 10, 10);
	};
	Image image = new Image(display, imageGcDrawer, 16, 16);

	final Shell shell = new Shell (display);
	shell.setText("Snippet 165");
	shell.setLayout(new GridLayout());
	final CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	folder.setSimple(false);
	folder.setUnselectedImageVisible(false);
	folder.setUnselectedCloseVisible(false);
	for (int i = 0; i < 8; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Item "+i);
		item.setImage(image);
		Text text = new Text(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setText("Text for item "+i+"\n\none, two, three\n\nabcdefghijklmnop");
		item.setControl(text);
	}
	folder.setMinimizeVisible(true);
	folder.setMaximizeVisible(true);
	folder.addCTabFolder2Listener(new CTabFolder2Adapter() {
		@Override
		public void minimize(CTabFolderEvent event) {
			folder.setMinimized(true);
			folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			shell.layout(true);
		}
		@Override
		public void maximize(CTabFolderEvent event) {
			folder.setMaximized(true);
			folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			shell.layout(true);
		}
		@Override
		public void restore(CTabFolderEvent event) {
			folder.setMinimized(false);
			folder.setMaximized(false);
			folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			shell.layout(true);
		}
	});
	shell.setSize(300, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose();
	display.dispose ();
}
}
