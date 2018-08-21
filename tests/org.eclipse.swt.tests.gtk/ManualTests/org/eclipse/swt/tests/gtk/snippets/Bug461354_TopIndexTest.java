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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class Bug461354_TopIndexTest {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		List list= new List(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
		// necessary to make the first getTopIndex() call return non-zero:
		list.setItems(new String[] {"Hello", "World"});
		
		shell.setSize(500, 500);
		shell.open();
		
		list.removeAll();
		String[] items = new String[100];
		for (int i = 0; i < items.length; i++) {
			items[i] = "0-" + i + " name-" + i;
		}
		list.setItems(items);
		list.setSelection(new int[] { 50 });
		list.showSelection();
		System.out.println(list.getTopIndex()); // should not be 0

		while (display.readAndDispatch()) { }
		
		System.out.println(list.getTopIndex()); // should not be 0
		
		display.dispose();
	}
}