/*******************************************************************************
 * Copyright (c) 2022 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Description: After resizing a pop-up {@link Shell},
 * scrollbars of {@link Scrollable} child widgets
 * no longer paint sliders correctly when scrolling.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Resize the pop-up {@link Shell}, so that scrollbars can still be used.</li>
 * <li>Scroll with mouse drag, observe that the sliders of the scrollbars don't move.</li>
 * </ol>
 * </p>
 * Expected results: Scrollbar sliders move during scrolling.
 * Actual results: Scrollbar sliders don't move when scrolling.
 */
public class Bug546961_Scrollbar_Slider_Popup_Shell {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 300);
		shell.setText("Bug 546961");
		shell.setLayout(new FillLayout());

		Shell s = new Shell(shell, SWT.ON_TOP | SWT.RESIZE);
		s.setLayout(new FillLayout());
		s.setSize(300, 200);
		Composite c = new Composite(s, SWT.BORDER);
		c.setLayout(new FillLayout());

		int n = 30;
		int m = 30;
		StringBuilder content  = new StringBuilder();
		for (int i = 10; i <= n; ++i) {
			for (int j = 10; j <= m; ++j) {
				content.append(i + "." + j);
				content.append(' ');
			}
			content.append(System.lineSeparator());
		}
		Text text = new Text(c, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setText(content.toString());
		s.open();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
