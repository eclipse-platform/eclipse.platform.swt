package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 *
 To get this guy to work,
 1) Uncomment line with "UNCOMMENT THIS".
 2) Get hold of nebula rich text project. (found inside org.eclipse.nebula)
   edit .classpath and add:
 <classpathentry kind="src" path="/org.eclipse.nebula.widgets.richtext"/>
 (This can be done via auto fix.

 Before Fix: When running, error messages thrown into console.
 After fix: Snippet runs as expected.
 */
public class Bug518961_RTTest extends org.eclipse.swt.widgets.Shell {

	public Bug518961_RTTest(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	private void createContents() {
//		new RichTextEditor(this);                                         /// UNCOMMENT THIS.
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Bug518961_RTTest(display);
		shell.setSize(800, 800);
		shell.setLayout(new FillLayout());

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	@Override
	protected void checkSubclass() {
	}

}