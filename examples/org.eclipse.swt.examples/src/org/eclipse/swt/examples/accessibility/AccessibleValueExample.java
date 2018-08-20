/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleValueAdapter;
import org.eclipse.swt.accessibility.AccessibleValueEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This example shows how to use an AccessibleValueListener to provide
 * additional information to an AT.
 */
public class AccessibleValueExample {
	private static int value = 40;
	private static int min = 0;
	private static int max = 100;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Accessible Value Example");

		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(e -> {
			Rectangle rect = canvas.getClientArea();
			String val = String.valueOf(value);
			Point size = e.gc.stringExtent(val);
			e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_LIST_SELECTION));
			e.gc.fillRectangle(0, 0, rect.width * value / (max - min), rect.height);
			e.gc.drawString(val, rect.x + (rect.width - size.x) / 2, rect.y + (rect.height - size.y) / 2, true);
		});

		Accessible accessible = canvas.getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = "The value of this canvas is " + value;
			}
		});
		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_PROGRESSBAR;
			}
		});
		accessible.addAccessibleValueListener(new AccessibleValueAdapter() {
			@Override
			public void setCurrentValue(AccessibleValueEvent e) {
				value = e.value.intValue();
				canvas.redraw();
			}
			@Override
			public void getMinimumValue(AccessibleValueEvent e) {
				e.value = Integer.valueOf(min);
			}
			@Override
			public void getMaximumValue(AccessibleValueEvent e) {
				e.value = Integer.valueOf(max);
			}
			@Override
			public void getCurrentValue(AccessibleValueEvent e) {
				e.value = Integer.valueOf(value);
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}