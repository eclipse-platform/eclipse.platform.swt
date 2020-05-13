/*******************************************************************************
 * Copyright (c) 2019 Andrey Loskutov and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrey Loskutov (loskutov@gmx.de) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug558222_BlackIcon {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("See org.eclipse.help.ui.internal.views.HelpTray.createImages()");

		int[] shape = new int[] {
				3,  3, 5,  3, 7,  5, 8,  5, 10, 3, 12, 3,
				12, 5, 10, 7, 10, 8, 12,10, 12,12,
				10,12, 8, 10, 7, 10, 5, 12, 3, 12,
				3, 10, 5,  8, 5,  7, 3,  5
		};

		/*
		 * Use magenta as transparency color since it is used infrequently.
		 */
		Color border = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
		Color background = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		Color backgroundHot = new Color(new RGB(252, 160, 160));
		Color transparent = display.getSystemColor(SWT.COLOR_MAGENTA);

		PaletteData palette = new PaletteData(new RGB[] { transparent.getRGB(), border.getRGB(), background.getRGB(), backgroundHot.getRGB() });
		ImageData data = new ImageData(16, 16, 8, palette);
		data.transparentPixel = 0;

		Image normal = new Image(display, data);
		normal.setBackground(transparent);
		GC gc = new GC(normal);
		gc.setBackground(background);
		gc.fillPolygon(shape);
		gc.setForeground(border);
		gc.drawPolygon(shape);
		gc.dispose();

		Image hover = new Image(display, data);
		hover.setBackground(transparent);
		gc = new GC(hover);
		gc.setBackground(backgroundHot);
		gc.fillPolygon(shape);
		gc.setForeground(border);
		gc.drawPolygon(shape);
		gc.dispose();

		backgroundHot.dispose();

		ToolBar bar = new ToolBar (shell, SWT.BORDER | SWT.FLAT);
		Rectangle clientArea = shell.getClientArea ();
		bar.setBounds (clientArea.x, clientArea.y, 200, 32);
		final ToolItem item = new ToolItem(bar, SWT.PUSH);
		item.setImage(normal);
		item.setHotImage(hover);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}