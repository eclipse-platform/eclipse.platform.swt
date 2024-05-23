/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
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
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

@SuppressWarnings("restriction")
public class Bug563475_DarkDisabledTable {
	static void setColors(Control control, Color backColor, Color foreColor) {
		control.setBackground(backColor);
		control.setForeground(foreColor);

		if (control instanceof Table) {
			Table table = (Table)control;
			table.setHeaderBackground(backColor);
			table.setHeaderForeground(foreColor);
		}

		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				setColors(child, backColor, foreColor);
			}
		}
	}

	static Image createImage(Display display) {
		Color transparentColor = display.getSystemColor(SWT.COLOR_BLACK);

		Image image = new Image(display, 16, 16);

		GC gc = new GC(image);
		gc.setBackground(transparentColor);
		gc.fillRectangle(image.getBounds());
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(6, 6, 4, 4);
		gc.dispose();

		return image;
	}

	static Image createItemImage(Display display) {
		Image image = createImage(display);
		ImageData imageData = image.getImageData();
		imageData.transparentPixel = imageData.getPixel(0, 0);

		Image result = new Image(display, imageData);
		image.dispose();

		return result;
	}

	static Image createItemIcon(Display display) {
		Image image = createImage(display);

		Image result = new Image(display, image.getImageData(), image.getImageData());
		image.dispose();

		return result;
	}

	public static void main (String [] args) {
		Display display = new Display ();
		OS.setTheme(true);

		Color backColor = new Color(display, 0x30, 0x30, 0x30);
		Color foreColor = new Color(display, 0xD0, 0xD0, 0xD0);

		Color backColorItem = new Color(display, 0x80, 0x00, 0x00);
		Color foreColorItem = new Color(display, 0x00, 0x00, 0x80);

		Image itemImage = createItemImage(display);
		Image itemIcon  = createItemIcon(display);

		Shell shell = new Shell (display);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.numColumns = 5;
		shell.setLayout(layout);

		new Label(shell, SWT.NONE).setText("");
		Label hint = new Label(shell, 0);
		GridData gridSpan4H = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridSpan4H.horizontalSpan = layout.numColumns - 1;
		hint.setLayoutData(gridSpan4H);
		hint.setText(
			"1. Bug 563475 (since Bug 536621):\tNotice that 1C, 1D has background that doesn't fit dark theme\n" +
			"2. Bug 563475:\t\t\tNotice that 1C, 1D, 2C, 2D has image background that doesn't fit dark theme\n" +
			"3. Bug 547989 (since Bug 536621):\tNotice that 2A ignores Table#setBackground in sort column\n" +
			"4. Bug 547989 (since Bug 536621):\tsame for 2B but it gets fixed after repainting items\n" +
			"5. Bug 536621 (since Bug 516365):\t1B, 2B shall have background=red, foreground=blue on items 2 and 3\n" +
			"6. Bug 516365:\t\t\t1C, 1D, 2C, 2D shall not have light background\n" +
			"");

		new Label(shell, SWT.NONE).setText("");
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridSpan4H = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridSpan4H.horizontalSpan = layout.numColumns - 1;
		separator.setLayoutData(gridSpan4H);

		new Label(shell, SWT.NONE).setText("");
		new Label(shell, SWT.NONE).setText("(A) Enabled");
		new Label(shell, SWT.NONE).setText("(B) Enabled\n+ TableItem.setBackground()");
		new Label(shell, SWT.NONE).setText("(C) Disabled");
		new Label(shell, SWT.NONE).setText("(D) Disabled\n+ TableItem.setBackground()");

		final int numColumns = 3;

		for (int iHasColumns = 0; iHasColumns < 2; iHasColumns++) {
			if (iHasColumns == 0)
				new Label(shell, SWT.NONE).setText("(1) No columns");
			else
				new Label(shell, SWT.NONE).setText("(2) With columns");

			for (int iDisabled = 0; iDisabled < 2; iDisabled++)
			for (int iStyledItem = 0; iStyledItem < 2; iStyledItem++) {
				// SWT.CHECK is there to make sure that background behind it is also good
				final Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
				table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

				if (iHasColumns != 0) {
					// Not important
					table.setHeaderVisible(true);

					// SWT code in `Table#CDDS_PREPAINT` significantly depends
					// on whether there are columns in table.
					for (int iColumn = 0; iColumn < numColumns; iColumn++) {
						TableColumn tableColumn = new TableColumn(table, SWT.NONE);
						tableColumn.setText("Col#" + iColumn);

						if (iColumn == 0)
							tableColumn.setWidth(60);
						else
							tableColumn.setWidth(45);
					}

					// Test for Bug 547989, where sort column ignores
					// `Table#setBackground`
					{
						table.addListener(SWT.PaintItem, event -> {});

						table.setRedraw(false);
						table.setSortColumn(table.getColumn(1));
						table.setSortDirection(SWT.UP);
						table.setRedraw(true);
					}
				}

				if (iDisabled != 0) {
					// Test for Bug 563475, where disabled Table has unfitting
					// background in dark theme
					table.setEnabled(false);
				}

				for (int iItem = 0; iItem < 4; iItem++) {
					TableItem item = new TableItem(table, SWT.NONE);

					for (int iColumn = 0; iColumn < numColumns; iColumn++) {
						item.setText(iColumn, iItem + ":" + iColumn);

						// Test for Bug 563475, where image background ignores Table background.
						// Let's test both icons and images - just in case.
						if (((iItem + iColumn) % 2) == 0)
							item.setImage(iColumn, itemImage);
						else
							item.setImage(iColumn, itemIcon);
					}
				}

				// Let's also test checks - just in case.
				table.getItem(1).setChecked(true);
				table.getItem(3).setChecked(true);

				// Test for Bug 536621, where TableItem's style was ignored.
				if (iStyledItem != 0) {
					table.getItem(2).setBackground(backColorItem);
					table.getItem(2).setForeground(foreColorItem);

					for (int iColumn = 0; iColumn < numColumns; iColumn++) {
						table.getItem(3).setBackground(iColumn, backColorItem);
						table.getItem(3).setForeground(iColumn, foreColorItem);
					}
				}
			}
		}

		setColors(shell, backColor, foreColor);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}
