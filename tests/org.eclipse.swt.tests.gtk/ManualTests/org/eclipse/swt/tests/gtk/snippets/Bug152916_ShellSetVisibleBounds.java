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


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug152916_ShellSetVisibleBounds {
	public static void main(String[] args) {
	    Display display = new Display();
	    
	    Shell shell = new Shell(display);
	    shell.open();
	    shell.setBounds(200, 200, 200, 200);
	    System.out.println("1=" + shell.getBounds());
	    shell.setVisible(false);
	    System.out.println("2=" + shell.getBounds());
	    shell.setVisible(true);
	    System.out.println("3=" + shell.getBounds());
	    
	    display.dispose();
	  }
}
