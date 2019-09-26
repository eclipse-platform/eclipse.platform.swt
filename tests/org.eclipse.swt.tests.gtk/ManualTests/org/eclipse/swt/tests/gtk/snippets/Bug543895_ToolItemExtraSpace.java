/*******************************************************************************
 * Copyright (c) 2019 Andreu B and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Andreu B - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug543895_ToolItemExtraSpace {

	public static void main(String[] args) {

		Display d = new Display();

		Shell s = new Shell(d);
		s.setLayout(new GridLayout());

		Image img = new Image(d, "./images/save.png");

		ToolBar bar_1 = new ToolBar(s, SWT.FLAT | SWT.RIGHT);

		ToolItem ti_1_1 = new ToolItem(bar_1, SWT.PUSH);
		ti_1_1.setImage(img);

		ToolItem ti_1_2 = new ToolItem(bar_1, SWT.PUSH);
		ti_1_2.setImage(img);

		ToolItem ti_1_3 = new ToolItem(bar_1, SWT.PUSH);
		ti_1_3.setImage(img);

		new Label(s, SWT.SEPARATOR | SWT.HORIZONTAL);

		ToolBar bar_2 = new ToolBar(s, SWT.FLAT | SWT.RIGHT);

		ToolItem ti_2_1 = new ToolItem(bar_2, SWT.PUSH);
		ti_2_1.setImage(img);

		ToolItem ti_2_2 = new ToolItem(bar_2, SWT.PUSH);
		ti_2_2.setImage(img);

		ToolItem ti_2_3 = new ToolItem(bar_2, SWT.PUSH);
		ti_2_3.setImage(img);
		ti_2_3.setText("foo");

		s.open();
		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		d.dispose();
	}
}