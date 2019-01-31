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

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title:
 * How to run:
 * Bug description:
 * Expected results:
 * GTK Version(s):
 */
public class Bug543984_GTK4EventTypeConstants {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(500, 200);

		shell.open();

		shell.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("MouseUp");

			}

			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("MouseDown");

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO: GDK_2BUTTON_PRESS removed in GTK4, no signal for double click
				System.out.println("MouseDoubleClick");

			}
		});

		shell.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("KeyReleased " + e.character);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("KeyPressed " + e.character);
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}