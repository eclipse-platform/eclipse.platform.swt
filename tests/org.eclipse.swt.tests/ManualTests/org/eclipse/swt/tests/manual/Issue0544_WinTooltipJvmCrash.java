/*******************************************************************************
 * Copyright (c) 2023 Syntevo and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Issue0544_WinTooltipJvmCrash {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		ToolTip customTooltip = new ToolTip(shell, 0);
		final int tooltipID[] = new int[1];
		final Rectangle rectCustomText = new Rectangle(10, 0, 100, 100);
		final Rectangle rectCustomTextAndVisible = new Rectangle(350, 0, 100, 100);

		customTooltip.setText("Custom tooltip");
		customTooltip.setMessage("Custom tooltip #" + tooltipID[0]);

		shell.setToolTipText("Builtin tooltip #" + tooltipID[0]);
		tooltipID[0]++;

		shell.addListener(SWT.Paint, e -> {
			e.gc.drawRectangle(rectCustomText);
			e.gc.drawText("custom tooltip:\n.setText()", rectCustomText.x + 10, rectCustomText.y + 10);

			e.gc.drawRectangle(rectCustomTextAndVisible);
			e.gc.drawText("custom tooltip:\n.setVisible(true)\n.setText()", rectCustomTextAndVisible.x + 10, rectCustomTextAndVisible.y + 10);
		});

		shell.addListener(SWT.MouseMove, e -> {
			if (rectCustomText.contains (e.x, e.y)) {
				customTooltip.setMessage("Custom tooltip #" + tooltipID[0]);
			} else if (rectCustomTextAndVisible.contains (e.x, e.y)) {
				if (!customTooltip.getVisible()) {
					customTooltip.setVisible(true);
				}

				customTooltip.setMessage("Custom tooltip #" + tooltipID[0]);
			} else {
				shell.setToolTipText("Builtin tooltip #" + tooltipID[0]);
			}

			tooltipID[0]++;
		});

		TabFolder tabFolder = new TabFolder(shell, 0);

		// Issue #554 - #A
		{
			Composite composite = new Composite(tabFolder, 0);
			composite.setLayout(new GridLayout (1, true));

			TabItem tab = new TabItem(tabFolder, 0);
			tab.setText("Issue #554 - #A");
			tab.setControl(composite);

			Label label = new Label(composite, 0);
			label.setText(
				"Problem A:\n" +
				"1. Run on Windows\n" +
				"2. Move mouse into right rectangle without moving into left\n" +
				"3. You will see custom tooltip (note tooltip's header)\n" +
				"4. Move mouse outside the rectangle\n" +
				"5. JVM will crash\n"
			);
		}

		// Issue #554 - #B
		{
			Composite composite = new Composite(tabFolder, 0);
			composite.setLayout(new GridLayout (1, true));

			TabItem tab = new TabItem(tabFolder, 0);
			tab.setText("Issue #554 - #B");
			tab.setControl(composite);

			Label label = new Label(composite, 0);
			label.setText(
				"Problem B:\n" +
				"1. Run on Windows\n" +
				"2. Move mouse into left rectangle\n" +
				"3. Issue #544: builtin tooltip will now incorrectly have text from custom tooltip\n" +
				"   Can be understood even better if you move mouse away from Shell and then back in\n"
			);
		}

		// Bug 473894 - #A
		{
			Composite composite = new Composite(tabFolder, 0);
			composite.setLayout(new GridLayout (1, true));

			TabItem tab = new TabItem(tabFolder, 0);
			tab.setText("Bug 473894 - #A");
			tab.setControl(composite);

			Label label = new Label(composite, 0);
			label.setText(
				"Bug 473894 (fixed in 2015, shall remain fixed)\n" +
				"1. Run on Windows\n" +
				"2. Click button below\n" +
				"3. Each time, tooltip shall have new text"
			);

			final int count[] = new int[1];
			Button button = new Button(composite, SWT.PUSH);
			button.setText("Press for balloon tip");
			button.addListener(SWT.Selection, e -> {
				final ToolTip tip = new ToolTip(shell, SWT.BALLOON);
				tip.setMessage("Tooltip with setLocation #" + count[0]);
				tip.setLocation(400, 400 + count[0]*10);
				count[0]++;
				tip.setVisible(true);
			});
		}

		// Bug 473894 - #B
		{
			Composite composite = new Composite(tabFolder, 0);
			composite.setLayout(new GridLayout (1, true));

			TabItem tab = new TabItem(tabFolder, 0);
			tab.setText("Bug 473894 - #B");
			tab.setControl(composite);

			Label label = new Label(composite, 0);
			label.setText(
				"Bug 473894 (fixed in 2015, shall remain fixed)\n" +
				"1. Run on Windows\n" +
				"2. Move mouse into right rect\n" +
				"3. As you move mouse inside rect, tooltip text shall update"
			);
		}

		// Bug 498895
		{
			Composite composite = new Composite(tabFolder, 0);
			composite.setLayout(new GridLayout (1, true));

			TabItem tab = new TabItem(tabFolder, 0);
			tab.setText("Bug 498895");
			tab.setControl(composite);

			Label label = new Label(composite, 0);
			label.setText(
				"Bug 498895 (fixed in 2016, shall remain fixed)\n" +
				"1. Run on Windows\n" +
				"2. Click first button, then second, then first ...\n" +
				"3. Tooltip shall update each time"
			);

			final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);

			Button button = new Button (composite, SWT.PUSH);
			button.setText("Press for balloon tip");
			button.addListener(SWT.Selection,  e-> {
				tip.setMessage("Here is a message for the user. When the message is too long it wraps. I should say something cool but nothing comes to my mind.");
				tip.setText("Notification from anywhere");
				tip.setLocation(400, 400);
				tip.setVisible(true);
			});

			Button button2 = new Button (composite, SWT.PUSH);
			button2.setText("empty message tip");
			button2.addListener(SWT.Selection, e -> {
				tip.setText("TEXT PART");
				tip.setMessage(""); // setting the message to the empty string results in the tool tip never being visible again!
				tip.setVisible(true);
			});
		}

		shell.pack();

		Rectangle buttonRect = tabFolder.getBounds();
		Rectangle shellRect = shell.getBounds();

		rectCustomText.y = buttonRect.y + buttonRect.height + 20;
		rectCustomTextAndVisible.y = rectCustomText.y;

		shellRect.height += 150;
		shell.setBounds(shellRect);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
