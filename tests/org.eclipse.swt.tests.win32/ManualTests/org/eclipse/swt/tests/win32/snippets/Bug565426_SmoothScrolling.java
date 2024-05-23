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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class Bug565426_SmoothScrolling {
	static void simulateScroll(Scrollable control, boolean vertical, int wheelDelta) {
		long wParam = wheelDelta << 16;

		if (vertical)
			OS.SendMessage(control.handle, OS.WM_MOUSEWHEEL,  wParam, 0);
		else
			OS.SendMessage(control.handle, OS.WM_MOUSEHWHEEL, wParam, 0);
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, SWT.WRAP);
		hint.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		hint.setText(
			"Problem: when you scroll the control below slowly, old SWT will not scroll at all. " +
			"This only happens when mouse/touchpad supports smooth scrolling. " +
			"Also see output in console." +
			"\n\n" +
			"If you don't have such a device, use buttons below to simulate it. " +
			"After the patch, you should see a change every 2 clicks, because " +
			"(1) one scroll line is 120 points " +
			"(2) Windows settings use 3x wheel speed by default, see " +
			"'Control Panel | Mouse | Wheel'" +
			"\n\n" +
			"Note that SWT Scrollable doesn't scroll on horizontal mouse wheel yet. " +
			"You will still see console output for it. " +
			"Some touchpads will directly scroll control instead of sending horz mouse wheel events, " +
			"which would appear as if Scrollable actually scrolls on horz wheel."
		);

		final Composite drawArea = new Composite(shell, SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
		drawArea.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));
		final ScrollBar scrollBarV = drawArea.getVerticalBar();
		final ScrollBar scrollBarH = drawArea.getHorizontalBar();
		scrollBarV.setValues(10000, 0, 20000, 1, 1, 10);
		scrollBarH.setValues(0, 0, 100, 1, 1, 10);

		drawArea.addListener(SWT.Paint, event -> {
			int scrollValueV = scrollBarV.getSelection();
			int scrollValueH = scrollBarH.getSelection();
			final Point size = drawArea.getSize();
			final int rowHeight = event.gc.getFontMetrics().getHeight();

			int x = (size.x / 2) - (size.x * scrollValueH / 100);
			int y = 0;
			while (y < size.y) {
				event.gc.drawString(Integer.toString(scrollValueV++), x, y);
				y += rowHeight;
			}
		});

		scrollBarH.addListener(SWT.Selection, event -> drawArea.redraw());
		scrollBarV.addListener(SWT.Selection, event -> drawArea.redraw());

		drawArea.addListener(SWT.MouseVerticalWheel, event -> {
			System.out.format(
				"SWT.MouseVerticalWheel:       %+d%n",
				event.count
			);
		});

		drawArea.addListener(SWT.MouseHorizontalWheel, event -> {
			System.out.format(
				"SWT.MouseHorizontalWheel:     %+d%n",
				event.count
			);
		});

		final Composite buttonsComposite = new Composite(shell, 0);
		buttonsComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		buttonsComposite.setLayout(new RowLayout());
		{
			Button button;

			button = new Button(buttonsComposite, SWT.PUSH);
			button.setText("Horz -20 points");
			button.addListener(SWT.Selection, e -> simulateScroll(drawArea, false, -20));

			button = new Button(buttonsComposite, SWT.PUSH);
			button.setText("Horz +20 points");
			button.addListener(SWT.Selection, e -> simulateScroll(drawArea, false, +20));

			button = new Button(buttonsComposite, SWT.PUSH);
			button.setText("Vert -20 points");
			button.addListener(SWT.Selection, e -> simulateScroll(drawArea, true,  -20));

			button = new Button(buttonsComposite, SWT.PUSH);
			button.setText("Vert +20 points");
			button.addListener(SWT.Selection, e -> simulateScroll(drawArea, true,  +20));
		}

		shell.setSize(600, 500);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
