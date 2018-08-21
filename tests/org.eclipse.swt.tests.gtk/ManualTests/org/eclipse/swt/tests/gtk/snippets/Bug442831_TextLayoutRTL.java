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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug442831_TextLayoutRTL {

	protected Shell shell;
	Button btnRightToLeft;
	Text text;
	Canvas canvas;

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Bug442831_TextLayoutRTL window = new Bug442831_TextLayoutRTL();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(801, 481);
		shell.setText("SWT Application");

		btnRightToLeft = new Button(shell, SWT.CHECK);
		btnRightToLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				update();
			}
		});
		btnRightToLeft.setBounds(643, 30, 134, 25);
		btnRightToLeft.setData("name", "btnRightToLeft");
		btnRightToLeft.setText("RTL");

		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text.addModifyListener(e -> update());
		text.setBounds(22, 30, 593, 125);
		text.setData("name", "text");

		canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(22, 177, 593, 224);
		canvas.setData("name", "canvas");

		canvas.addPaintListener(e -> drawCanvas(e.gc));

		text.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit."
				+ "Praesent quis dapibus nibh."
				+ "Integer dolor sem, sagittis quis pharetra in, lacinia laoreet augue."
				+ "Ut porttitor rhoncus gravida." + "Mauris nec orci vel mi posuere rutrum."
				+ "Nam aliquam interdum condimentum."
				+ "Aenean a sollicitudin mi, sit amet pharetra nisl."
				+ "Nulla imperdiet quam non metus blandit porta id ut sem."
				+ "Nunc bibendum et ex id ullamcorper."
				+ "Morbi tincidunt lacus id eros volutpat pretium."
				+ "Cras at sapien non justo porttitor dignissim." + "Vivamus sed risus libero."
				+ "Sed et nisi sit amet nibh malesuada cursus sit amet vitae ligula."
				+ "Vestibulum varius quam at mauris pharetra maximus eget non diam."
				+ "Nunc volutpat consequat dolor ut interdum."
				+ "Suspendisse sed tristique ipsum, nec dictum eros.");

	}

	protected void drawCanvas(GC gc) {
		Display display = shell.getDisplay();
		TextLayout layout = new TextLayout(display);
		layout.setAlignment(SWT.LEFT);
		layout.setOrientation(btnRightToLeft.getSelection() ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);
		layout.setWidth(canvas.getBounds().width);
		layout.setText(text.getText());

		layout.draw(gc, 0, 0);

		gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		Rectangle bounds = layout.getBounds();
		gc.drawRectangle(bounds);

		gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gc.setLineDash(new int[] { 1, 7 });
		int lines = layout.getLineCount();
		for (int i = 0; i < lines; i++) {
			bounds = layout.getLineBounds(i);
			gc.drawRectangle(bounds);
		}

	}

	protected void update() {
		canvas.redraw();
	}
}
