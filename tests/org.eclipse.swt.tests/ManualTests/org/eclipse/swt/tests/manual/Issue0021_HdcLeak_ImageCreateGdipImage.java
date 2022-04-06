/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Issue0021_HdcLeak_ImageCreateGdipImage {
	static Image makeTrasparentImage(Display display) {
		Image image = new Image(display, 40, 40);
		GC gc = new GC(image);
		Rectangle bounds = image.getBounds();
		Color transparent = new Color(display, 0x00, 0x00, 0x00);
		Color contents = new Color(display, 0x00, 0x80, 0x00);
		gc.setBackground(transparent);
		gc.fillRectangle(bounds);
		gc.setBackground(contents);
		gc.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		gc.dispose();

		ImageData imageData = image.getImageData();
		imageData.transparentPixel = 0;
		image.dispose();

		return new Image(display, imageData);
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		new Label(shell, 0).setText(
			"1) Use Windows\n" +
			"2) Open Task Manager\n" +
			"3)     Go to Details tab\n" +
			"4)     Right-click table header and add column: 'GDI objects'\n" +
			"5) Issue #21: The number will constantly grow because there's a leak\n" +
			"6) Issue #21: Entire Windows will get gradually slower\n" +
			"7) Issue #21: When counter reaches 10000, there will be SWT.error exception\n"
		);

		Menu menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);
		for (int iMenu = 0; iMenu < 2; iMenu++) {
			Menu menu = new Menu(shell, SWT.DROP_DOWN);
			MenuItem cascade = new MenuItem(menubar, SWT.CASCADE);
			cascade.setText("menuItem#" + iMenu);
			cascade.setMenu(menu);

			for (int iMenuItem = 0; iMenuItem < 10; iMenuItem++) {
				MenuItem menuItem = new MenuItem(menu, 0);
				menuItem.setText("item#" + iMenu + ":" + iMenuItem);
			}
		}

		Image image = makeTrasparentImage(display);

		Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.addListener(SWT.Paint, e -> {
			e.gc.setAdvanced(true);

			for (int i = 0; i < 100; i++) {
				e.gc.drawImage(image, 0, 0);
			}
		});

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (canvas.isDisposed()) return;
				canvas.redraw();
				display.timerExec(30, this);
			}
		};
		runnable.run();

		shell.setSize(500, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		image.dispose();
		display.dispose();
	}
}