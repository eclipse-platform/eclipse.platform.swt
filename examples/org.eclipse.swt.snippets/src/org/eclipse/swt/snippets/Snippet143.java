/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 * Tray example snippet: place an icon with a popup menu on the system tray
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet143 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 143");
		ImageGcDrawer imgc1 = (gc, iwidth, iheight) -> {};
		Image image = new Image(display, imgc1, 16, 16);
		ImageGcDrawer imgc = (gc, iwidth, iheight) -> {
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.fillRectangle(0, 0, iwidth, iheight);
		};
		Image image2 = new Image(display, imgc, 16, 16);
		final Tray tray = display.getSystemTray();
		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("SWT TrayItem");
			item.addListener(SWT.Show, event -> System.out.println("show"));
			item.addListener(SWT.Hide, event -> System.out.println("hide"));
			item.addListener(SWT.Selection, event -> System.out.println("selection"));
			item.addListener(SWT.DefaultSelection, event -> System.out.println("default selection"));
			final Menu menu = new Menu(shell, SWT.POP_UP);
			for (int i = 0; i < 8; i++) {
				MenuItem mi = new MenuItem(menu, SWT.PUSH);
				mi.setText("Item" + i);
				mi.addListener(SWT.Selection, event -> System.out.println("selection " + event.widget));
				if (i == 0)
					menu.setDefaultItem(mi);
			}
			item.addListener(SWT.MenuDetect, event -> menu.setVisible(true));
			item.setImage(image2);
			//item.setHighlightImage(image);
		}
		shell.setBounds(50, 50, 300, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		image2.dispose();
		display.dispose();
	}
}