package org.eclipse.swt.tests.junit.memoryleak;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 * Test Widgets for memory leaks. Used when updating dispose logic.
 *
 * Due to lack of better alternative, the current mechanism creates & disposes a widget in a loop.
 * If the test makes it to the end without crashing your system, then you have no (major?) leaks.
 *
 * Run these on demand if updating dispose logic of a particular widget.
 *
 * Note:
 * - The tests are a bit long, so they're not part of the main test suite.
 * - Note, JNI != Java memory leaks. JNI leaks are not detected by Java profilers as they occur *outside* of the heap.
 *   Finding JNI leaks is not a trivial matter as typical C memory tools see the JVM as a memory leak itself.
 *   It's possible thou. Have not tried myself but see:
 *   https://stackoverflow.com/questions/33334126/how-to-find-memory-leaks-in-java-jni-c-process
 *   https://gdstechnology.blog.gov.uk/2015/12/11/using-jemalloc-to-get-to-the-bottom-of-a-memory-leak/
 */
public class Test_Memory_Leak {

	static int COUNT_PRINT_PER_ROW = 50;

	/**
	 * Create and dispose Browser instances.
	 *
	 * If this test runs at linear speed and passes, then it's fairly safe to say you have no memory leaks.
	 * A typical run will take 5 minutes.
	 *
	 * If you have a memory leak in the dispoal logic, then the loop eventually slows down and the test crashes.
	 * You would see 'memory pressure' errors. The Java process of the jUnit would grow significantly (100's of mbs)
	 * On my machine with Intel i7 & 16 GB of ram, this occurs at the ~420th iteration.
	 * (although with my testing, without memory leaks, it grows a little bit (by 100mb by end of test)).
	 */
	@Test
	public void test_Browser() {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.open ();

		Browser browser;
		int count = 50_000;

		for (int i = 1; i <= count; i++) {
			browser = new Browser(shell, SWT.None);
			browser.setUrl("http://www.google.com");
			while (display.readAndDispatch()) {
				// This loop is needed because some disposal is delayed and done asynchronously in main loop.
				// This loop typically performs ~12 iterations.
			}
			if (i != count) browser.dispose();
			if (i % (COUNT_PRINT_PER_ROW) == 0) System.out.println();
			System.out.print(i+ " ");
		}
		System.out.println();
	}
}
