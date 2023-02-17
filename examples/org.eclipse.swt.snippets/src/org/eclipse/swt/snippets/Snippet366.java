/*******************************************************************************
 * Copyright (c) 2015, 2023 Red Hat Inc and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Buttons with various arrows and methods that affect arrow directions.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet366 {
	static Display display;
	static Shell shell;

	public static void main (String [] args) {
		display = new Display();
		shell = new Shell(display);
		shell.setText("Snippet 366");
		shell.setLayout (new RowLayout ());

		makeArrowGroup ();
		makeAlignGroup ();
		makeOrientationGroup ();

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}

	private static void makeOrientationGroup () {
		Group orientationGroup = new Group (shell, SWT.None);
		orientationGroup.setLayout (new RowLayout ());
		orientationGroup.setText ("Orientation group");

		final AtomicInteger prevDir = new AtomicInteger (0);
		final Button orientationButton = new Button (orientationGroup, SWT.ARROW | SWT.RIGHT);
		orientationButton.addSelectionListener (SelectionListener.widgetSelectedAdapter (event -> {
			switch (prevDir.get ()) {
				case 0:
					orientationButton.setOrientation (SWT.LEFT_TO_RIGHT);
					prevDir.set (1);
					break;
				case 1:
					orientationButton.setOrientation (SWT.RIGHT_TO_LEFT);
					prevDir.set (0);
					break;
				default:
					break;
			}
		}));
	}

	private static void makeAlignGroup () {
		Group alignGroup = new Group (shell, SWT.None);
		alignGroup.setLayout (new RowLayout ());
		alignGroup.setText ("Alignment group");

		final AtomicInteger prevDir = new AtomicInteger (0);
		final Button alignmentButton = new Button (alignGroup, SWT.ARROW | SWT.UP);
		alignmentButton.addSelectionListener (SelectionListener.widgetSelectedAdapter (event -> {
			switch (prevDir.get ()) {
				case 0:
					alignmentButton.setAlignment (SWT.RIGHT);
					prevDir.set (1);
					break;
				case 1:
					alignmentButton.setAlignment (SWT.DOWN);
					prevDir.set (2);
					break;
				case 2:
					alignmentButton.setAlignment (SWT.LEFT);
					prevDir.set (3);
					break;
				case 3:
					alignmentButton.setAlignment (SWT.UP);
					prevDir.set (0);
				default:
					break;
			}
		}));
	}

	private static void makeArrowGroup () {
		Group arrowGroup = new Group(shell, SWT.None);
		arrowGroup.setText ("Arrow group");
		arrowGroup.setLayout (new RowLayout ());

		new Button (arrowGroup,  SWT.ARROW | SWT.LEFT);
		new Button (arrowGroup,  SWT.ARROW | SWT.RIGHT);
		new Button (arrowGroup,  SWT.ARROW | SWT.UP);
		new Button (arrowGroup,  SWT.ARROW | SWT.DOWN);
	}
}
