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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Title: Bug 483097 - [GTK3.16+] gtk_widget_override_color is deprecated
 * How to run: launch snippet and check sysout
 * Bug description: N/A
 * Expected results: if the pattern on line 18 matches: true will be printed
 * GTK Version(s): N/A
 */
public class Bug483097_RegexForegroundTesting {

	public static void main(String[] args) {
		// Test string
		String searched = "* {color: rgb(255, 0, 0);{";
		// Test pattern
		String pattern = "[^-]color: rgba?\\((\\d+(,\\s?)?){3,4}";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(searched);
		System.out.println(m.find());
	}
}
