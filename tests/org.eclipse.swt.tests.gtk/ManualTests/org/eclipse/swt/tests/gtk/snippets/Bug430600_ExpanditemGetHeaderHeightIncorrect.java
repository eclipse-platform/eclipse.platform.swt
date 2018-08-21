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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug430600_ExpanditemGetHeaderHeightIncorrect {
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		shell.setText("ExpandBar Example");

		ExpandBar expandBar = new ExpandBar(shell, SWT.NONE);
		Image image = display.getSystemImage(SWT.ICON_QUESTION);

		Composite composite = new Composite (expandBar, SWT.NONE);
		GridLayout layout = new GridLayout ();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("SWT.PUSH");
		button = new Button (composite, SWT.RADIO);
		button.setText("SWT.RADIO");
		button = new Button (composite, SWT.CHECK);
		button.setText("SWT.CHECK");
		button = new Button (composite, SWT.TOGGLE);
		button.setText("SWT.TOGGLE");
		ExpandItem item0 = new ExpandItem (expandBar, SWT.NONE, 0);
		item0.setText("What is your favorite button");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		
		System.out.println(item0.getHeaderHeight());
		item0.setHeight(40);
		System.out.println(item0.getHeaderHeight());
		
		shell.setSize(400, 350);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();

	}
}

