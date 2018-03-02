package org.eclipse.swt.tests.manualJUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/** Convienience class for easy copy & paste */
@FixMethodOrder(MethodSorters.JVM)
public class Template extends MJ_root {

	@Test
	public void test_template() {
		Shell shell = mkShell("Instructions: Pass:F1  Fail:F2  Skip:F3  Exit:Esc");

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Hello World");
		button.pack();
		button.setLocation(10, 10);

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

}
