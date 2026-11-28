/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.AssertionFailedError;

/**
 * Keeps the test classes of a suite from influencing each other through the
 * display of the shared test thread: the display is disposed after every class,
 * and a test that leaves shells open fails. The display is kept for all tests
 * of a class, since recreating it per test disturbs the focus that keyboard and
 * drag-and-drop tests rely on.
 * <p>
 * JUnit registers this extension for every test class of this bundle by
 * automatic extension detection, which {@code junit-platform.properties}
 * enables.
 */
public final class DisplayLifecycleExtension implements BeforeAllCallback, AfterEachCallback, AfterAllCallback {

	@Override
	public void beforeAll(ExtensionContext context) {
		disposeDisplayOfCurrentThread();
	}

	@Override
	public void afterEach(ExtensionContext context) {
		Display display = Display.getCurrent();
		if (display == null || display.isDisposed()) {
			return;
		}
		Shell[] leakedShells = Arrays.stream(display.getShells()).filter(shell -> !shell.isDisposed()).toArray(Shell[]::new);
		if (leakedShells.length == 0) {
			return;
		}
		String description = Arrays.stream(leakedShells).map(DisplayLifecycleExtension::describe)
				.collect(Collectors.joining(", "));
		for (Shell shell : leakedShells) {
			shell.dispose();
		}
		while (!display.isDisposed() && display.readAndDispatch()) {
		}
		// a failed test usually did not reach its cleanup, so do not hide its failure
		if (context.getExecutionException().isEmpty()) {
			throw new AssertionFailedError("Test left " + leakedShells.length + " shell(s) open: " + description);
		}
	}

	@Override
	public void afterAll(ExtensionContext context) {
		disposeDisplayOfCurrentThread();
	}

	private static void disposeDisplayOfCurrentThread() {
		Display display = Display.getCurrent();
		if (display != null && !display.isDisposed()) {
			display.dispose();
		}
	}

	private static String describe(Shell shell) {
		String text = shell.getText();
		return text == null || text.isEmpty() ? shell.toString() : shell.toString() + " \"" + text + "\"";
	}

}
