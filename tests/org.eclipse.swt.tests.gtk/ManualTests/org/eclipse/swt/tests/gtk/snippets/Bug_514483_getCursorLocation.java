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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug_514483_getCursorLocation
{
	public static void main(String[] args)
	{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(0, 0, 600, 600);

		Label parentShellLabel = new Label(shell, SWT.None);
		parentShellLabel.setText("Parent widget.shell.\n"
				+ "INSTRUCTIONS:\n"
				+ "- Parent widget.shell should be maximized.\n"
				+ "- Child widget.shell should be at x400 y400 (in yellow square).\n"
				+ "- Click inside the child widget.shell, observe result coordinates below.\n"
				+ "\n"
				+ "The bug is that x,y is not relative to parent, but relative to child-widget.shell itself (0-200 range).\n"
				+ "Expected coordinates: between ~400 to ~600. (i.e, relative to parent's x,y.)\n"
				+ "Result Coordinates:");
		parentShellLabel.setBounds(0, 0, 600, 200);

		final Label resultLbl = new Label(shell, SWT.None);
		resultLbl.setBounds(0,180, 600, 100);
		resultLbl.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));

		Label childShellLocation = new Label(shell, SWT.None);
		childShellLocation.setText("Child Shell should be here.\nIf it is not, move it here \nmanually");
		childShellLocation.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		childShellLocation.setBounds(400, 400, 200, 160);

		Shell childShell = new Shell(shell, SWT.ON_TOP);
		childShell.setBackground(display.getSystemColor(SWT.COLOR_DARK_YELLOW));

		MouseAdapter clickListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Point loc = display.getCursorLocation();
				resultLbl.setText(loc.toString());
				if (loc.x > 300 && loc.x < 700 && loc.y > 300 && loc.y < 700) // give user some slack.
					resultLbl.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
				else
					resultLbl.setBackground(display.getSystemColor(SWT.COLOR_RED));
			}
		};
		childShell.addMouseListener(clickListener);

//		display.addFilter(SWT.KeyDown, new Listener() {
//			  public void handleEvent(Event e) {
//			    if (e.type == SWT.KeyDown) {
//			      switch (e.keyCode) {
//			        case SWT.F1:
//			          System.out.println("Passed");
//			          break;
//			        case SWT.F2:
//			          System.out.println("Failed");
//			          break;
//			        case SWT.F3:
//			          System.out.println("Skipped");
//			          break;
//			        case SWT.F4:
//			          System.out.println("Exit Test suite");
//			          break;
//			      }
//			    }
//			  }
//			});

		shell.open();
		childShell.open();
		childShell.setBounds(400, 400, 200, 200);
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}