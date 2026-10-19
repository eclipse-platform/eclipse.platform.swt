/*******************************************************************************
 * Copyright (c) 2019 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.io.*;

/**
 * Used to store some metadata, argument or configuration stuff for snippets.
 * Most of this was hardcoded in {@link SnippetLauncher} in the past.
 */
public class SnippetsConfig {

	public static final File SNIPPETS_SOURCE_DIR = new File("src/org/eclipse/swt/snippets");

	public static final String SNIPPETS_PACKAGE = "org.eclipse.swt.snippets";

	public static boolean isPrintingSnippet(int snippetNumber) {
		return snippetNumber == 132 || snippetNumber == 133 || snippetNumber == 318;
	}

	public static String[] getSnippetArguments(int snippetNumber) {
		switch (snippetNumber) {
		case 81:
			return new String[] { "Shell.Explorer" };

		default:
			return new String[0];
		}
	}
}
