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
