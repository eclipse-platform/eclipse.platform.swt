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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.util.function.Consumer;

public class Bug536008_DarkDisabledLabel {
	static void moveControl(Control control, Point point) {
		Point size = control.getSize();
		control.setBounds(point.x, point.y, size.x, size.y);
	}

	static void setColors(Control control, Color backColor, Color foreColor) {
		control.setBackground(backColor);
		control.setForeground(foreColor);

		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				setColors(child, backColor, foreColor);
			}
		}
	}

	static void addSelectionListener(Button button, Label label1, Label label2, Consumer<Label> handler, boolean isDefault) {
		Runnable runnable = () -> {
			handler.accept(label1);
			handler.accept(label2);

			label2.setSize(label2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		};

		button.addListener(SWT.Selection, e -> {
			if (!((Button)e.widget).getSelection ()) return;
			runnable.run();
		});

		if (isDefault) {
			button.setSelection(true);
			runnable.run();
		}
	}

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		Color backColor  = new Color(display, 0x30, 0x30, 0x30);
		Color foreColor  = new Color(display, 0xD0, 0xD0, 0xD0);
		Color labelColor = new Color(display, 0x80, 0x80, 0x80);
		display.setData("org.eclipse.swt.internal.win32.Label.disabledForegroundColor", labelColor);

		int margin = 10;
		int cx = 60;
		int cy = 60;
		int nRow = 0;

		Image image = new Image(display, cx / 2, cy / 2);
		{
			GC gc = new GC(image);
			gc.setBackground(backColor);
			gc.setForeground(foreColor);

			Rectangle imageBounds = image.getBounds();
			gc.fillRectangle(imageBounds);
			gc.drawOval(imageBounds.x, imageBounds.y, imageBounds.width - 1, imageBounds.height - 1);
		}

		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 10;
		fillLayout.marginHeight = 10;

		Group group = new Group(shell, SWT.NONE);
		{
			group.setText("Various Label styles && states");
			group.setLayout(fillLayout);

			Composite labelComposite = new Composite(group, SWT.NONE);

			for (int style : new int[] {SWT.LEFT, SWT.CENTER, SWT.RIGHT})
			{
				int nCol = 0;

				for (int test1 = 0; test1 < 5; test1++)
				for (int test2 = 0; test2 < 2; test2++)
				for (int test3 = 0; test3 < 2; test3++)
				{
					Label label = new Label(labelComposite, SWT.BORDER | style);

					switch (test1) {
						case 0:
							label.setText("&Label");
							break;
						case 1:
							label.setText("&Mul-\nlabel");
							break;
						case 2:
							label.setImage(image);
							break;
						case 3:
							label.setText("L&abel");
							label.setImage(image);
							break;
						case 4:
							label.setImage(image);
							label.setText("La&bel");
							break;
						default:
							throw new RuntimeException("Unknown test");
					}

					switch (test2) {
						case 0:
							label.setEnabled(false);
							break;
						case 1:
							break;
						default:
							throw new RuntimeException("Unknown test");
					}

					switch (test3) {
						case 0:
							label.setSize(cx, cy);
							break;
						case 1:
							label.setSize(label.computeSize(SWT.DEFAULT, SWT.DEFAULT));
							break;
						default:
							throw new RuntimeException("Unknown test");
					}

					moveControl(label, new Point(nCol*(cx+margin), nRow*(cy+margin)));
					nCol++;
				}

				nRow++;
			}
		}

		group = new Group(shell, SWT.NONE);
		{
			group.setText("Label playground");
			group.setLayout(new RowLayout());

			Composite labelComposite1 = new Composite(group, SWT.NONE);
			labelComposite1.setLayout(new FormLayout());
			Label label1 = new Label(labelComposite1, SWT.BORDER);
			label1.setLayoutData(new FormData(cx, cy));

			Composite labelComposite2 = new Composite(group, SWT.NONE);
			labelComposite2.setLayout(new FormLayout());
			Composite labelComposite2a = new Composite(labelComposite2, SWT.NONE);
			labelComposite2a.setLayoutData(new FormData(cx, cy));
			Label label2 = new Label(labelComposite2a, SWT.BORDER);
			moveControl(label2, new Point(0, 0));

			Composite subgroup = new Composite(group, SWT.NONE);
			{
				subgroup.setLayout(new RowLayout(SWT.VERTICAL));

				Button button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setText(text)");
				addSelectionListener(button, label1, label2, label -> {
					label.setText("Lab&el");
				}, true);

				button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setImage(image)");
				addSelectionListener(button, label1, label2, label -> {
					label.setImage(image);
				}, false);

				button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setImage(null)");
				addSelectionListener(button, label1, label2, label -> {
					label.setImage(null);
				}, false);
			}

			subgroup = new Composite(group, SWT.NONE);
			{
				subgroup.setLayout(new RowLayout(SWT.VERTICAL));

				Button button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setEnabled(true)");
				addSelectionListener(button, label1, label2, label -> {
					label.setEnabled(true);
				}, true);

				button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setEnabled(false)");
				addSelectionListener(button, label1, label2, label -> {
					label.setEnabled(false);
				}, false);
			}

			subgroup = new Composite(group, SWT.NONE);
			{
				subgroup.setLayout(new RowLayout(SWT.VERTICAL));

				Button button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setAlignment(SWT.LEFT)");
				addSelectionListener(button, label1, label2, label -> {
					label.setAlignment(SWT.LEFT);
				}, true);

				button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setAlignment(SWT.CENTER)");
				addSelectionListener(button, label1, label2, label -> {
					label.setAlignment(SWT.CENTER);
				}, false);

				button = new Button(subgroup, SWT.RADIO);
				button.setText("Label.setAlignment(SWT.RIGHT)");
				addSelectionListener(button, label1, label2, label -> {
					label.setAlignment(SWT.RIGHT);
				}, false);
			}
		}

		// Set shell colors
		setColors(shell, backColor, foreColor);

		// Pack and show shell
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}
