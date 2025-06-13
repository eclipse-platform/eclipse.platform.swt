/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class CoolBarExample {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("CoolBar Example");
		shell.setSize(400, 200);
		shell.setLayout(new GridLayout());
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setLayoutData(layoutData);

		CoolItem item1 = new CoolItem(coolBar, SWT.NONE);
		Button button1 = new Button(coolBar, SWT.PUSH);
		button1.setText("Button 1");
		Point size1 = button1.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		item1.setControl(button1);
		item1.setPreferredSize(item1.computeSize(size1.x, size1.y));

		CoolItem item2 = new CoolItem(coolBar, SWT.NONE);
		Combo combo = new Combo(coolBar, SWT.READ_ONLY);
		combo.add("Option 1");
		combo.add("Option 2");
		combo.select(0);
		Point size2 = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		item2.setControl(combo);
		item2.setPreferredSize(item2.computeSize(size2.x, size2.y));

		//Cannot drag and resize/mouse the item controls
		coolBar.setLocked(true);

		CoolBar coolBar1 = new CoolBar(shell, SWT.NONE);
		coolBar1.setLayoutData(layoutData);

		CoolItem item11 = new CoolItem(coolBar1, SWT.NONE);
		Button button11 = new Button(coolBar1, SWT.PUSH);
		button11.setText("Button 1");
		item11.setControl(button11);
		item11.setPreferredSize(item11.computeSize(size1.x, size1.y));

		CoolItem item12 = new CoolItem(coolBar1, SWT.NONE);
		Combo combo12 = new Combo(coolBar1, SWT.READ_ONLY);
		combo12.add("Option 1");
		combo12.add("Option 2");
		combo12.select(0);
		item12.setControl(combo12);
		item12.setPreferredSize(item12.computeSize(size2.x, size2.y));

		//Cannot drag and resize/mouse the item controls
		coolBar1.setLocked(false);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
