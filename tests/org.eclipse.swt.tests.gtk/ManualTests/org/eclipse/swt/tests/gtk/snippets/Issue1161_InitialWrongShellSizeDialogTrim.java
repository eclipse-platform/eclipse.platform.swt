package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Please remove $HOME/swt/trims.prefs to see the issue.
 */
public class Issue1161_InitialWrongShellSizeDialogTrim {
	static String message = "Soooooooooooooooooooooooooooooooooooooooooooooooooome Extraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.\n"
			+ "Loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeext";
	/**
	 * This problem happens with SWT.DIALOG_TRIM. In general any trim with No Resize
	 * style
	 */
	static int style = SWT.DIALOG_TRIM;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, style);
		shell.setText("Issue1161_InitialWrongShellSizeDialogTrim");

		// Even with the FillLayout issue exists.
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 20;
		layout.marginBottom = layout.marginTop = layout.marginLeft = layout.marginRight = 10;
		shell.setLayout(layout);

		// many labels to make the dialog bigger
		for (int i = 0; i < 8; i++) {
			Label label = new Label(shell, SWT.NONE);
			label.setText("Line:" + i + "  " + message);
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}

		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// x and y location are randomly chosen.
		// width and height are correctly calculated.
		shell.setBounds(600, 600, size.x, size.y);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
