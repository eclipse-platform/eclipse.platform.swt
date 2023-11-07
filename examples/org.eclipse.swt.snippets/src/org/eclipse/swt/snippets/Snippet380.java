/*******************************************************************************
 * Copyright (c) 2022 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.text.*;
import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This shows the usage of the display in the executor framework in conjunction with a CompletableFuture
 */
public class Snippet380 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 100);
		shell.setLayout(new BorderLayout());
		shell.setText("Snippet 380 (Executor)");
		Button button = new Button(shell, SWT.PUSH);
		button.setLayoutData(new BorderData(SWT.BOTTOM));
		Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new BorderData(SWT.CENTER));
		button.setText("Calculate");
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			button.setEnabled(false);
			button.setText("Calculating...");
			CompletableFuture.supplyAsync(() -> {
				double value = 0;
				for (int i = 0; i < 10; i++) {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e1) {
						break;
					}
					value += Math.random();
				}
				return value;
			}).thenApply(result -> NumberFormat.getInstance().format(result))//
					.thenApply(formatedNumber -> String.format("The result is %s", formatedNumber))//
					.thenAcceptAsync(label::setText, label.getDisplay())//
					.whenCompleteAsync((x, y) -> button.setEnabled(true), label.getDisplay());
		}));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
