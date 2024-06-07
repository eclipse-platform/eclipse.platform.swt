/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *     Christoph Läubrich - modernize snippet
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * BusyIndicator example snippet: display busy cursor during long running task
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet130 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 130");
		shell.setLayout(new GridLayout());
		final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		AtomicInteger nextId = new AtomicInteger();
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invoke long running job");
		Button b2 = new Button(shell, SWT.PUSH);
		b2.setText("wait for predefined future");
		CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
			try {
				TimeUnit.SECONDS.sleep(30);
				System.out.println("Future is done!");
			} catch (InterruptedException e) {
				return "Task is interrupted";
			}
			return "Text computed in the background";
		});
		b.addSelectionListener(widgetSelectedAdapter(e -> {
			int id = nextId.incrementAndGet();
			text.append("\nStart long running task " + id);
			BusyIndicator.execute(() -> {
				for (int i = 0; i < 20; i++) {
					if (display.isDisposed())
						return;
					TimeUnit.MILLISECONDS.sleep(500);
					System.out.println("do task that takes a long time in a separate thread [id=" + id+"] iteration "+i);
				}
			}).thenRunAsync(() -> {
				if (text.isDisposed())
					return;
				text.append("\nCompleted long running task " + id);
			}, display);

		}));
		b2.addSelectionListener(widgetSelectedAdapter(e -> {
			text.append("\nWaiting for Background Work to complete...");
			b2.setEnabled(false);
			BusyIndicator.showWhile(future);
			if (text.isDisposed() || b2.isDisposed()) {
				return;
			}
			b2.setEnabled(true);
			text.append("\nBackground work has completed");
		}));
		shell.setSize(500, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
