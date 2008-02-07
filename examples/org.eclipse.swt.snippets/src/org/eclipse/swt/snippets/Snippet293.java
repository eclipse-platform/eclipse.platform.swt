package org.eclipse.swt.snippets;

/*
 * create a tri-state button.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet293 {

	public static void main(java.lang.String[] args) {
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setLayout(new GridLayout());
			
			Button b1 = new Button (shell, SWT.CHECK);
			b1.setText("State 1");
			b1.setSelection(true);
			
			Button b2 = new Button (shell, SWT.CHECK);
			b2.setText("State 2");
			b2.setSelection(false);
			
			Button b3 = new Button (shell, SWT.CHECK);
			b3.setText("State 3");
			b3.setSelection(true);
			b3.setGrayed(true);
			
			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
}
