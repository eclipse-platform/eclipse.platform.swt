

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * Description: A CCombo box that has "READ_ONLY" property still has it's text selected.
 * Steps to reproduce: Run snippet, observe selection of CCombo
 * Expected results: Text should not be selected, as it's read-only, there is no purpose in having text selected.
 * Actual results: Text is selected. This looks ugly.
 */
public class Bug304893_CCombo_read_only_combo_text_selection {

	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		CCombo combo = new CCombo(shell, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		for (int i = 0; i < 5; i++) {
			combo.add("item" + i);
		}
		combo.setText("item0");

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
