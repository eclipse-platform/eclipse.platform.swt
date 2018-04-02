/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
public class Bug430600_ExpandItemHeaderHeight {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		ExpandBar expandBar = new ExpandBar(shell, SWT.NONE);

		final ExpandItem xpndtmTest = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTest.setExpanded(true);
		
		System.out.println(xpndtmTest.getHeaderHeight()); //Correct 
		xpndtmTest.setHeight(100);
		System.out.println(xpndtmTest.getHeaderHeight()); //Some negative number
	}
}