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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug535657_MultipleComboOverlap {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 200);
		shell.setText("Bug_odd_combos_on_resize");
		shell.setLayout(new GridLayout());

		createGroup(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createGroup(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_IN);

		group.setText("some group");
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);

		GridLayout gridLayout = new GridLayout(2, false);
		group.setLayout(gridLayout);

		class LabelAndText {
				String labelText;
				String comboItem;

				LabelAndText(String labelText, String comboItem) {
					this.labelText = labelText;
					this.comboItem = comboItem;
				}
		}

		LabelAndText[] texts = {
				new LabelAndText("Some label text: ", "some text"),
				new LabelAndText("Some label text: ", "some long combo text"),
				new LabelAndText("Some label text: ", "some text"),
		};

		for (LabelAndText text : texts) {
			Label label = new Label(group, SWT.NONE);
			label.setText(text.labelText);
			Combo combo = new Combo(group, SWT.READ_ONLY | SWT.DROP_DOWN);
			GridData comboGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			combo.setLayoutData(comboGridData);
			combo.add(text.comboItem);
		}
	}
}