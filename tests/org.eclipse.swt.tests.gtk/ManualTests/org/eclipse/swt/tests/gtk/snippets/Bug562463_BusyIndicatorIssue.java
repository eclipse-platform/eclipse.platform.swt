package org.eclipse.swt.tests.gtk.snippets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug562463_BusyIndicatorIssue {

	// start copy-past from original BussyIndicator class
	static int nextBusyId = 1;
	static final String BUSYID_NAME = "SWT BusyIndicator"; //$NON-NLS-1$
	static final String BUSY_CURSOR = "SWT BusyIndicator Cursor"; //$NON-NLS-1$
	// end copy-past from original BussyIndicator class

	private static class Task implements Runnable {

		private String description;

		Task(String description){
			this.description = description;
		}

        @Override
        public void run() {
        	System.out.printf("A task %s has started\n", description);
        	for (int i = 0; i < 40; i++) {
        		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        	System.out.printf("A task %s has ended\n", description);

        }
	};

    public static void main (String [] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new RowLayout(SWT.HORIZONTAL));
        Button b1 = new Button(shell, SWT.NONE);
        b1.setText("Run task with default BusyIndicator...");
        b1.addListener(SWT.Selection, event -> BusyIndicator.showWhile(display, new Task("without a spinning cursor")));
        Button b2 = new Button(shell, SWT.NONE);
        b2.setText("Run task with updated BusyIndicator...");
        b2.addListener(SWT.Selection, event -> showWhile(display, new Task("with a spinning cursor")));
        shell.pack();
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        shell.dispose();
        display.dispose ();
    }

    // start copy-past from BussyIndicator class

    public static void showWhile(Display display, Runnable runnable) {
		if (runnable == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (display == null) {
			display = Display.getCurrent();
			if (display == null) {
				runnable.run();
				return;
			}
		}

		Integer busyId = Integer.valueOf(nextBusyId);
		nextBusyId++;
		Cursor cursor = display.getSystemCursor(SWT.CURSOR_WAIT);
		Shell[] shells = display.getShells();
		for (Shell shell : shells) {
			Integer id = (Integer)shell.getData(BUSYID_NAME);
			if (id == null) {
				setCursorAndId(shell, cursor, busyId);
			}
		}

		try {
			runnable.run();
		} finally {
			shells = display.getShells();
			for (Shell shell : shells) {
				Integer id = (Integer)shell.getData(BUSYID_NAME);
				if (id == busyId) {
					setCursorAndId(shell, null, null);
				}
			}
		}
	}

	/**
	 * Paranoia code to make sure we don't break UI because of one shell disposed, see bug 532632 comment 20
	 */
	private static void setCursorAndId(Shell shell, Cursor cursor, Integer busyId) {
		if (!shell.isDisposed()) {
			shell.setCursor(cursor);
			// change: Additional Explicit update !
			shell.update();
			// end change
		}
		if (!shell.isDisposed()) {
			shell.setData(BUSYID_NAME, busyId);
		}
	}

	// end copy-past from BussyIndicator class
}