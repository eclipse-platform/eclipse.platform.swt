/**
 *  Copyright (c) 2019 Laurent CARON.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation (bug 542777)
 */
package org.eclipse.swt.snippets;

import java.util.*;

/*
 * Example snippet: mouse navigation for Styled Text Widget
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * @since 3.110
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet375 {

	public static void main(String[] args) throws Exception {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		final StringBuilder sb = new StringBuilder();
		final Random random = new Random(2546);
		for (int i = 0; i < 200; i++) {
			sb.append("Very very long text about ").append(random.nextInt(2000)).append("\t");
			if (i % 10 == 0) {
				sb.append("\n");
			}
		}

		// H SCROLL
		final Label lbl1 = new Label(shell, SWT.NONE);
		lbl1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		lbl1.setText("Horizontal Scroll");

		final StyledText txt1 = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		txt1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		txt1.setText(sb.toString());
		txt1.setMouseNavigatorEnabled(true);

		// V_SCROLL
		final Label lbl2 = new Label(shell, SWT.NONE);
		lbl2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		lbl2.setText("Vertical Scroll");

		final StyledText txt2 = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txt2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		txt2.setText(sb.toString());
		txt2.setMouseNavigatorEnabled(true);

		// H SCROLL & V_SCROLL
		final Label lbl3 = new Label(shell, SWT.NONE);
		lbl3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		lbl3.setText("Horizontal and Vertical Scroll");

		final StyledText txt3 = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		txt3.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		txt3.setText(sb.toString());
		txt3.setMouseNavigatorEnabled(true);

		final Button enableDisableButton = new Button(shell, SWT.PUSH);
		enableDisableButton.setLayoutData(new GridData(GridData.END, GridData.FILL, true, false));
		enableDisableButton.setText("Disable Mouse Navigation");
		enableDisableButton.addListener(SWT.Selection, e -> {
			if (txt3.getMouseNavigatorEnabled()) {
				enableDisableButton.setText("Enable Mouse Navigation");
			} else {
				enableDisableButton.setText("Disable Mouse Navigation");
			}
			txt3.setMouseNavigatorEnabled(!txt3.getMouseNavigatorEnabled());
		});

		// Disabled Scroll at start
		final Label lbl4 = new Label(shell, SWT.NONE);
		lbl4.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		lbl4.setText("No scroll at start");

		final StyledText txt4 = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		gd.minimumHeight = 100;
		txt4.setLayoutData(gd);

		txt4.setText("Disabled scroll");
		txt4.setMouseNavigatorEnabled(true);

		// Disabled Scroll
		final Label lbl5 = new Label(shell, SWT.NONE);
		lbl5.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		lbl5.setText("No scroll");

		final StyledText txt5 = new StyledText(shell, SWT.MULTI | SWT.BORDER);
		final GridData gd5 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gd5.minimumHeight = 100;
		txt5.setLayoutData(gd5);

		txt5.setText("No scroll");
		txt5.setMouseNavigatorEnabled(true);

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
