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


import java.nio.charset.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug489640_ComboPerformanceTest {

	public static void main(String[] args) {
		final Map<String, Charset> charSets = Charset.availableCharsets();
		final List<String> encodings = new ArrayList<>(charSets.keySet());
		Collections.sort(encodings);

		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final Combo combo = new Combo(shell, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final Button button = new Button(shell, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		button.setText("Fill Combo");
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				final String[] items = encodings.toArray(new String[encodings.size()]);
				System.out.println("Setting widget.combo with " + items.length + " items");
				final long start = System.currentTimeMillis();
				combo.setItems(items);
				final long end = System.currentTimeMillis();
				System.out.println("took " + (end - start) + " ms.");
				combo.select(0);
			}
		});

		shell.setSize(400, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
