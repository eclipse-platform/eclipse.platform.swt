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
 */
package org.eclipse.swt.tests.gtk.snippets;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Bug535392_getText {
	static int run = 1;
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 400);
		shell.setLayout(new GridLayout(2, false));
		Browser browser = new Browser(shell, SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		shell.open();

		// Most of below work, but a few single unicode characters are miss-understood. Heuristic works better with many characters.
		// https://en.wikipedia.org/wiki/List_of_Unicode_characters
		testValue(display, shell, browser, "-", true); //  working, regular ascii '-'
		testValue(display, shell, browser, "‐", true);  // BROKEN, (single char read as UTF-8 instead of UTF-16).
		testValue(display, shell, browser, "ABC", true); // 65 66 67
		testValue(display, shell, browser, "A®A", true); //  U+00AE 	® 	0174 	&reg; 	Registered sign 	0110
		testValue(display, shell, browser, "A¢A", true); // U+00BF 	¿ 	0191 	&iquest; 	Inverted Question Mark 	0127
		testValue(display, shell, browser, "ABCüDü", true); // U+00FC 	ü 	0252 	&uuml; 	Latin Small Letter U with diaeresis 	0188
		testValue(display, shell, browser, "AӛB", true); // U+04DB 	ӛ 	Cyrillic Small Letter Schwa with diaeresis 	0620
		testValue(display, shell, browser, "Ё", true); // BROKEN. (single char read as UTF-8 instead of UTF-16).  U+04DB 	ӛ 	Cyrillic Small Letter Schwa with diaeresis 	0620

		if (run == 0) {
			for (int i = 0; i < 100000; i++) {
				final String testStr = new String(new char [] {'A', (char) i});
				testValue(display, shell, browser, testStr, i, true);

			}
		}

		testValue(display, shell, browser, "SYNOPSIS\n" +
				"       find [-H] [-L] [-P] [-D debugopts] [-Olevel] [starting-point...] [expression]\n" +
				"\n" +
				"DESCRIPTION\n" +
				"       This manual page documents the GNU version of find.  GNU find searches the directory tree rooted at each given starting-point by evaluating the given expression from left to right, according to the rules of precedence (see sec‐\n" +
				"       tion OPERATORS), until the outcome is known (the left hand side is false for and operations, true for or), at which point find moves on to the next file name.  If no starting-point is specified, `.' is assumed.\n" +
				"\n" +
				"       If you are using find in an environment where security is important (for example if you are using it to search directories that are writable by other users), you should read the \"Security Considerations\" chapter of  the  findu‐\n" +
				"       tils documentation, which is called Finding Files and comes with findutils.   That document also includes a lot more detail and discussion than this manual page, so you may find it a more useful source of information.\n" +
				"\n" +
				"OPTIONS\n" +
				"       The  -H,  -L  and  -P  options control the treatment of symbolic links.  Command-line arguments following these are taken to be names of files or directories to be examined, up to the first argument that begins with `-', or the\n" +
				"       argument `(' or `!'.  That argument and any following arguments are taken to be the expression describing what is to be searched for.  If no paths are given, the current directory is  used.   If  no  expression  is  given,  the\n" +
				"       expression -print is used (but you should probably consider using -print0 instead, anyway).\n" +
				"\n" +
				"       This  manual page talks about `options' within the expression list.  These options control the behaviour of find but are specified immediately after the last path name.  The five `real' options -H, -L, -P, -D and -O must appear\n" +
				"       before the first path name, if at all.  A double dash -- can also be used to signal that any remaining arguments are not options (though ensuring that all start points begin with either `./' or `/' is generally safer if you use\n" +
				"       wildcards in the list of start points).", true);




		display.dispose();
	}


	private static void testValue(Display display, Shell shell, Browser browser, String testStr, boolean autoTest) {
		testValue(display, shell, browser, testStr, 0, autoTest);
	}

	// I think this is broken for values above 127 :-/.
	private static void testValue(Display display, Shell shell, Browser browser, String testStr, int testID, boolean autoTest) {
		AtomicBoolean testFinished = new AtomicBoolean(false);
		browser.setText(testStr);

		ProgressAdapter completionTester = new ProgressAdapter() {
			@Override
			public void completed(ProgressEvent event) {
				Browser browser = (Browser) event.widget;
				String returnedStr = browser.getText();
				if (testStr.equals(returnedStr)) {
					System.out.println("(PASS): testStr/returnedStr: " + testStr + "/" + returnedStr + "  Test id:" + testID);
				} else {
					System.err.println("(FAIL): testStr/returnedStr: " + testStr + "/" + returnedStr + "  Test id:" + testID);
				}
				testFinished.set(true);
			}
		};

		browser.addProgressListener(completionTester);

		if (autoTest) {
			while (!shell.isDisposed() && !testFinished.get()) {
				display.readAndDispatch();
			}
			browser.removeProgressListener(completionTester);
		} else {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}
}
