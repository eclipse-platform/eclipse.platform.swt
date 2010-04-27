/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.accessibility.*;

/**
 * This example shows how to use an AccessibleValueListener to provide
 * additional information to an AT.
 */
public class AccessibleValueExample {
	static int value = 40;
	static int min = 0;
	static int max = 100;
	
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Accessible Value Example");
		
		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle rect = canvas.getClientArea();
				String val = String.valueOf(value);
				Point size = e.gc.stringExtent(val);
				e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_LIST_SELECTION));
				e.gc.fillRectangle(0, 0, rect.width * value / (max - min), rect.height);
				e.gc.drawString(val, rect.x + (rect.width - size.x) / 2, rect.y + (rect.height - size.y) / 2, true);
			}
		});
		
		Accessible accessible = canvas.getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = "The value of this canvas is " + value;
			}
		});
		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_PROGRESSBAR;
			}
		});
		accessible.addAccessibleValueListener(new AccessibleValueAdapter() {
			public void setCurrentValue(AccessibleValueEvent e) {
				value = e.value.intValue();
				canvas.redraw();
			}
			public void getMinimumValue(AccessibleValueEvent e) {
				e.value = new Integer(min);
			}
			public void getMaximumValue(AccessibleValueEvent e) {
				e.value = new Integer(max);
			}
			public void getCurrentValue(AccessibleValueEvent e) {
				e.value = new Integer(value);
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