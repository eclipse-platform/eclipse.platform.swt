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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Thomas Singer
 */
/*
 * Title: Bug 509514 - GTK3: Incorrect calculation of Combo location (toDisplay)
 * How to run: launch snippet, edit the Combo or Text box, press space
 * Bug description: A popup appears under the Text widget but not the Combo
 * Expected results: A popup should appear under the widget being edited
 * GTK Version(s): 3
 */
public class Bug509514_IncorrectToDisplayLocation {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		final RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;
		shell.setLayout(layout);

		final Combo combo = new Combo(shell, SWT.BORDER);
		installPopup(combo);

		final Text text = new Text(shell, SWT.BORDER);
		installPopup(text);

		shell.setSize(400, 300);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void installPopup(Control control) {
		final Listener listener = new Listener() {
			private Shell shell;

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.KeyDown) {
					if (event.character == ' ') {
						shell = new Shell(control.getShell(), SWT.POP_UP | SWT.ON_TOP | SWT.BORDER);
						final Point location = control.toDisplay(0, control.getSize().y);
						shell.setBounds(location.x, location.y, 100, 100);
						shell.setVisible(true);
					}
				} else if (event.type == SWT.FocusOut) {
					if (shell != null) {
						shell.dispose();
						shell = null;
					}
				}
			}
		};
		control.addListener(SWT.KeyDown, listener);
		control.addListener(SWT.FocusOut, listener);
	}
}
