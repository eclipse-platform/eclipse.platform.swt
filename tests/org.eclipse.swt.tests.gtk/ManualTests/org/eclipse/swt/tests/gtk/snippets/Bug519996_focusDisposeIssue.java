/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
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
