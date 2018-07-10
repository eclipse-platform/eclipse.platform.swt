/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others. All rights reserved.
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

/*
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug395296_LongerText {

	private static ToolItem itemChange;
	private static ToolBar bar;

	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());

		Text textEntry = new Text(shell, SWT.NONE);
		textEntry.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Button setItemText = new Button(shell, SWT.PUSH);
		setItemText.setText("Set Item Text");
		setItemText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = textEntry.getText();
				if (itemChange != null) {
					itemChange.setText(text);
				}
			}
		});

		bar = new ToolBar (shell, SWT.WRAP);
		for (int i=0; i<3; i++) {
			ToolItem item = new ToolItem (bar, SWT.PUSH);
			item.setText ("ITEM " + i);
		}

		itemChange = new ToolItem(bar, SWT.PUSH);
		itemChange.setText("1234567890111111111111");

		Rectangle clientArea = shell.getClientArea ();
		bar.setLocation (clientArea.x, clientArea.y);

		bar.pack ();
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
