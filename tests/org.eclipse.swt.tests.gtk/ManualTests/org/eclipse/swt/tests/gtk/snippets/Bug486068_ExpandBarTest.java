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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug486068_ExpandBarTest {
	
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		shell.setText("ExpandBar Example");
		ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
		  
		
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout ();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
	 	layout.verticalSpacing = 10;
		composite.setLayout(layout);
		
		
//		Button button = new Button (composite, SWT.PUSH);
//		button.setText("SWT.PUSH");
		Label label = new Label(composite, SWT.NONE);
		label.setText("meh");
		
		ExpandItem item0 = new ExpandItem (bar, SWT.NONE, 0);

		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setExpanded(true);
		bar.setSpacing(8);
		
		
		shell.setSize(400, 350);
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose();
	}
}
