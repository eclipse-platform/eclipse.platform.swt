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


// This snippet causes errors on a regular setup. Commenting out for now.
//
//import java.awt.Frame;
//
//import javax.help.HelpSet;
//import javax.help.HelpSetException;
//import javax.help.JHelp;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.awt.SWT_AWT;
//import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//
//public class Bug268105_JavahelpCrash {
//
//	public static void main(String[] args) {
//		HelpSet helpSet;
//		Display display;
//		Shell widget.shell;
//		Composite composite;
//		Frame frame;
//		JHelp help;
//
//		try {
//			helpSet = new HelpSet(null, HelpSet.findHelpSet(null, "Animals.hs"));
//		} catch (HelpSetException e) {
//			e.printStackTrace();
//			return;
//		}
//
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//
//		display = new Display();
//		widget.shell = new Shell(display);
//		widget.shell.setLayout(new FillLayout());
//		composite = new Composite(widget.shell, SWT.NO_BACKGROUND | SWT.EMBEDDED);
//		frame = SWT_AWT.new_Frame(composite);
//		help = new JHelp(helpSet);
//		frame.add(help);
//
//		frame.setVisible(true);
//		widget.shell.open();
//
//		while (!widget.shell.isDisposed())
//			if (!display.readAndDispatch())
//				display.sleep();
//
//		display.dispose();
//	}
//
//}