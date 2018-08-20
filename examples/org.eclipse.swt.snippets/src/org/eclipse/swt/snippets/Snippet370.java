/**
 *  Copyright (c) 2018 Lablicate GmbH.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Lablicate GmbH - Snippet for Bug 248075
 */
package org.eclipse.swt.snippets;

import java.util.*;

/*
 * example snippet: DateTime with localization support.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.107
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet370 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			createForLocale(shell, locale);
		}
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createForLocale(Shell shell, Locale locale) {
		System.setProperty("swt.datetime.locale", locale.toLanguageTag());
		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		new Label(composite, SWT.NONE).setText(locale.toLanguageTag());
		new DateTime(composite, SWT.DROP_DOWN);
		new DateTime(composite, SWT.SHORT);
		new DateTime(composite, SWT.TIME);
	}
}
