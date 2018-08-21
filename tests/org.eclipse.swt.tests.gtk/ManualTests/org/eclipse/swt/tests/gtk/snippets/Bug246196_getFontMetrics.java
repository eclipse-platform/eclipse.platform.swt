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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug246196_getFontMetrics {

	public static void main(String[] args) {
		long start, end;
		Display display = new Display();
		Shell shell = new Shell(display);
		
		System.out.println("========for default graphics.font==========");
		
		GC gc = new GC(shell);
		System.out.println("for first time");
		start = System.currentTimeMillis();
		gc.getFontMetrics();
		end = System.currentTimeMillis();
		System.out.println("in total:" + (end - start));
		System.out.println("for second time");
		start = System.currentTimeMillis();
		gc.getFontMetrics();
		end = System.currentTimeMillis();
		System.out.println("in total:" + (end - start));		
		System.out.println("===============================");
		
		System.out.println("=======for Sans Italic============");		
		Font font = new Font(display, "Sans", 10, SWT.ITALIC);
		gc.setFont(font);
		System.out.println("for first time");
		start = System.currentTimeMillis();
		gc.getFontMetrics();
		end = System.currentTimeMillis();
		System.out.println("in total:" + (end - start));
		System.out.println("for second time");
		start = System.currentTimeMillis();
		gc.getFontMetrics();
		end = System.currentTimeMillis();
		System.out.println("in total:" + (end - start));		
		System.out.println("===============================");
		shell.dispose();
		display.dispose();
	}	
}

