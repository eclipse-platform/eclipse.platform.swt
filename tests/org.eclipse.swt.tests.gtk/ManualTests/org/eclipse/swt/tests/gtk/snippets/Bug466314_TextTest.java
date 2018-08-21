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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug466314_TextTest {

	public static void main(String[] args) {
		final int TEXT_WIDTH = 100;
		
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		final Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		text.setLayoutData(new GridData(TEXT_WIDTH, 50));
		text.setText("Eclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other productsEclipse very good IDE to develop multiple RCP apps and other products");
		text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int currentHeight = text.getSize().y;
				int preferredHeight = text.computeSize(TEXT_WIDTH, SWT.DEFAULT).y;
				if (currentHeight != preferredHeight) {
					GridData data = (GridData)text.getLayoutData();
					data.heightHint = preferredHeight;
					shell.pack();
				}
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
