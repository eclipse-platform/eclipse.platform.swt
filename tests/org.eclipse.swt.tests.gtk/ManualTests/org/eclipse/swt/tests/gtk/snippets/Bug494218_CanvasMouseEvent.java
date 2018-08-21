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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug494218_CanvasMouseEvent {
	/**
	 * Runs the application
	 */
	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("bad canvas");
		createContents(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void createContents(Shell shell) {
		shell.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL
				| SWT.V_SCROLL);
		Canvas canvas = new Canvas(sc, SWT.NONE);
		canvas.setSize(70000, 100);

		canvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_GREEN));
				e.gc.fillRectangle(0, 0, 32767, 200);
				e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_RED));
				e.gc.fillRectangle(32768, 0, 40000, 200);

			}
		});

		sc.setContent(canvas);

		canvas.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				System.out.println(e.x);
			}

		});

		sc.setMinWidth(200);
	}

	public static void main(String[] args) {
		new Bug494218_CanvasMouseEvent().run();
	}
}