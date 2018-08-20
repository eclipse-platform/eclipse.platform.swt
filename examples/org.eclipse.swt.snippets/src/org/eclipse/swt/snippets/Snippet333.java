/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT skin snippet: Listen for and print out skin events
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.6
 */
import static org.eclipse.swt.events.SelectionListener.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet333 {

	static final int colors[] = {
		SWT.COLOR_RED,
		SWT.COLOR_MAGENTA,
		SWT.COLOR_GREEN,
		SWT.COLOR_YELLOW,
		SWT.COLOR_BLUE,
		SWT.COLOR_CYAN,
		SWT.COLOR_DARK_RED,
		SWT.COLOR_DARK_MAGENTA,
		SWT.COLOR_DARK_GREEN,
		SWT.COLOR_DARK_YELLOW,
		SWT.COLOR_DARK_BLUE,
		SWT.COLOR_DARK_CYAN
	};

	public static void main(String[] arg) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Skin example");
		shell.setLayout (new GridLayout());

		Group container = new Group(shell, SWT.None);
		container.setText("Container");
		container.setLayout(new GridLayout(3, false));

		Composite child1 = new Composite(container,SWT.BORDER);
		child1.setLayout(new GridLayout());
		new Label(child1,SWT.NONE).setText("Label in pane 1");

		Composite child2 = new Composite(container,SWT.BORDER);
		child2.setLayout(new GridLayout());
		new Button(child2,SWT.PUSH).setText("Button in pane2");

		final Composite child3 = new Composite(container,SWT.BORDER);
		child3.setLayout(new GridLayout());
		new Text(child3, SWT.BORDER).setText("Text in pane3");

		display.addListener(SWT.Skin, event -> {
			System.out.println("Skin: " + event.widget);
			setBackground (event.display, (Control) event.widget);
		});

		Composite buttonContainer = new Composite(shell, SWT.NONE);
		buttonContainer.setLayout(new GridLayout(3, false));
		Button reskin = new Button(buttonContainer, SWT.PUSH);
		reskin.setText("Reskin All");
		reskin.addSelectionListener(widgetSelectedAdapter(e -> {
			System.out.println("=======");
			shell.reskin(SWT.ALL);
		}));
		Button reskin2 = new Button(buttonContainer, SWT.PUSH);
		reskin2.setText("Reskin Shell");
		reskin2.addSelectionListener(widgetSelectedAdapter(e -> {
			System.out.println("=======");
			shell.reskin(SWT.None);
		}));
		Button reskin3 = new Button(buttonContainer, SWT.PUSH);
		reskin3.setText("Reskin Right Composite");
		reskin3.addSelectionListener(widgetSelectedAdapter(e -> {
			System.out.println("=======");
			child3.reskin(SWT.ALL);
		}));

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected static void setBackground(Display display, Control control) {
		 Random randomGenerator = new Random();
		 int nextColor = randomGenerator.nextInt(100) + randomGenerator.nextInt(100);
		 control.setBackground(display.getSystemColor(colors[nextColor % colors.length]));
	}


}
