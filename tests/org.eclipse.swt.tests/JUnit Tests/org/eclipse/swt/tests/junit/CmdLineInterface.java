package org.eclipse.swt.tests.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


/**
 * This java file is intended to be used with the bash script "gtk_version_tests.sh" normally found in:
 * ~/git/eclipse.platform.swt/tests/org.eclipse.swt.tests/
 *
 * To run this from the command line:
 * 1) Ensure 'junit' package is installed on your system. (ex: sudo dnf install junit junit-javadoc)
 * 2) Run via: (Assuming your eclipse git repositories are in $HOME/git)
REPO=$HOME/git
ARG=AllNonBrowserTests
java -Djava.library.path=$REPO/eclipse.platform.swt.binaries/bundles/org.eclipse.swt.gtk.linux.x86_64   -Dfile.encoding=UTF-8   -classpath /usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar:$REPO/eclipse.platform.swt/bundles/org.eclipse.swt/bin:$REPO/eclipse.platform.swt/tests/org.eclipse.swt.tests/bin org.eclipse.swt.tests.junit.CmdLineInterface $ARG

 * TODO someday: generalize this so that any class#method can be ran:
 * http://stackoverflow.com/questions/9288107/run-single-test-from-a-junit-class-using-command-line
 *
 * It is intended to run SWT JUnit tests on multiple GTK versions in batch. Ex GTK2.24, GTK3.14 GTK3.16 etc..
 * This is convenient for patch submissions/reviews.
 *
 * For more details, see: https://wiki.eclipse.org/SWT/Devel/Gtk/JUnitTests#Running_jUnit_tests_across_multiple_versions_of_GTK
 *
 * @author For questions/bug reports: Leo Ufimtsev lufimtse@redhat.com, 2016.
 */
public class CmdLineInterface {

	public static void main(String[] args) {

		Result result = null;

		result = runTests(args, result);

		printResults(result);
	}

	private static Result runTests(String[] args, Result result) {
		if (args[0].equals("AllNonBrowserTests")) {
			result = JUnitCore.runClasses(AllNonBrowserTests.class);
		} else {
			System.out.println("ERROR: Incorrect paramaters were given. See file. ex: AllNonBrowserTests");
		}
		return result;
	}

	private static void printResults(Result result) {
		System.out.println("    ~~ SUMMARY ~~");

		String gtkVersion = System.getProperty("org.eclipse.swt.internal.gtk.version");

		if (result.wasSuccessful()) {
			System.out.println("    " + gtkVersion + " GTK_TEST_RESULT : ALL TESTS PASS");
		} else {
			System.out.println("    " + gtkVersion + " GTK_TEST_RESULT : SOME TESTS FAIL: " + result.getFailureCount() + " / " + result.getRunCount());
			int i = 1;
			for (Failure failure : result.getFailures()) {
				System.out.print("    " + i + ": ");
				System.out.println(failure.toString());
				i++;
			}
		}
		System.out.println("");
	}




}
