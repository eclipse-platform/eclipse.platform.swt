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
 * Table snippet: modify the clipping of custom background paints
 *
 * For a detailed explanation of this snippet see
 * http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm#_example4
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet273 {

public static void main(String[] args) {
	final String[] MONTHS = {
		"Jan", "Feb", "Mar", "Apr", "May", "Jun",
		"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
	};
	final int[] HIGHS = {-7, -4, 1, 11, 18, 24, 26, 25, 20, 13, 5, -4};
	final int[] LOWS = {-15, -13, -7, 1, 7, 13, 15, 14, 10, 4, -2, -11};
	final int SCALE_MIN = -30; final int SCALE_MAX = 30;
	final int SCALE_RANGE = Math.abs(SCALE_MIN - SCALE_MAX);

	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,400,350);
	shell.setText("Ottawa Average Daily Temperature Ranges");
	final Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	final Color white = display.getSystemColor(SWT.COLOR_WHITE);
	final Color red = display.getSystemColor(SWT.COLOR_RED);
	// final Image parliamentImage = new Image(display, "./parliament.jpg");
	final Table table = new Table(shell, SWT.NONE);
	table.setBounds(10,10,350,300);
	// table.setBackgroundImage(parliamentImage);
	for (int i = 0; i < 12; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(MONTHS[i] + " (" + LOWS[i] + "C..." + HIGHS[i] + "C)");
	}
	final int clientWidth = table.getClientArea().width;

	/*
	 * NOTE: MeasureItem and EraseItem are called repeatedly. Therefore it is
	 * critical for performance that these methods be as efficient as possible.
	 */
	table.addListener(SWT.MeasureItem, event -> {
		int itemIndex = table.indexOf((TableItem)event.item);
		int rightX = (HIGHS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
		event.width = rightX;
	});
	table.addListener(SWT.EraseItem, event -> {
		int itemIndex = table.indexOf((TableItem)event.item);
		int leftX = (LOWS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
		int rightX = (HIGHS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
		GC gc = event.gc;
		Rectangle clipping = gc.getClipping();
		clipping.x = leftX;
		clipping.width = rightX - leftX;
		gc.setClipping(clipping);
		Color oldForeground = gc.getForeground();
		Color oldBackground = gc.getBackground();
		gc.setForeground(blue);
		gc.setBackground(white);
		gc.fillGradientRectangle(event.x, event.y, event.width / 2, event.height, false);
		gc.setForeground(white);
		gc.setBackground(red);
		gc.fillGradientRectangle(
			event.x + event.width / 2, event.y, event.width / 2, event.height, false);
		gc.setForeground(oldForeground);
		gc.setBackground(oldBackground);
		event.detail &= ~SWT.BACKGROUND;
		event.detail &= ~SWT.HOT;
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	// parliamentImage.dispose();
	display.dispose();
}
}
