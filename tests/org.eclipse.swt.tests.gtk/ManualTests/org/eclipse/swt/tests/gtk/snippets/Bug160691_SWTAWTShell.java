/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 160691 - RFE: Please add a snipplet about SWT_AWT.new_Shell
 * How to run: snippet shows lack of features
 * Bug description: N/A, enhancement and not a bug
 * Expected results: N/A, enhancement and not a bug
 * GTK Version(s): N/A
 */
public class Bug160691_SWTAWTShell {

	public static void main(String[] args) {
        java.awt.Frame frame = new java.awt.Frame("AWT Frame");
        java.awt.Button button = new java.awt.Button("AWT Button");
        frame.add(button, java.awt.BorderLayout.NORTH);
        java.awt.Canvas canvas = new java.awt.Canvas();
        frame.add(canvas, java.awt.BorderLayout.CENTER);

        frame.addNotify();

        Display display = new Display();
        Shell shell = SWT_AWT.new_Shell(display, canvas);
        shell.setLayout(new FillLayout());
        Button swtButton = new Button(shell, SWT.PUSH);
        swtButton.setText("SWT Button");

        frame.setBounds(20, 20, 300, 300);
        shell.layout();
        frame.setVisible(true);

        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
}
}
