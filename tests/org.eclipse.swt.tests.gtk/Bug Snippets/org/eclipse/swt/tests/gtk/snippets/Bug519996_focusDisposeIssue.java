package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug519996_focusDisposeIssue {

	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		CCombo combo = new CCombo(shell, SWT.BORDER);
		new CCombo(shell, SWT.BORDER);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("focus and close");
		button.addSelectionListener(new SelectionAdapter() {
		 @Override
		 public void widgetSelected(SelectionEvent e) {
		  combo.setFocus();
		  shell.close();
		 }
		});

		shell.layout();
		shell.open();

		Display display = shell.getDisplay();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
