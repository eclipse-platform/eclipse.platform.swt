/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class Bug119157_ShellWithoutFocus {
	static Shell dialog = null;
	public static void main(String[] args) {
	    final Display display = new Display();
	    final Shell main = new Shell(display);
	    main.setLayout(new FillLayout());
	    Button button = new Button(main, SWT.PUSH);
	    button.setText("Press me");
	    button.addListener(SWT.Selection, new Listener() {
	        @Override
			public void handleEvent(Event event) {
	            if (dialog == null) {
	                dialog = new Shell(main);
	                dialog.setLayout(new FillLayout());
	                Text text = new Text(dialog, SWT.MULTI);
	                text.setText("No focus");
	                List list = new List(dialog, SWT.MULTI);
	                list.add("Multi-select will");
	                list.add("not work");
	                list.add("the second time.");
	                Button button = new Button(dialog, SWT.PUSH);
	                button.setText("OK");
	                button.addListener(SWT.Selection, new Listener() {
	                    @Override
						public void handleEvent(Event event) {
	                        dialog.setVisible(false);
	                    }
	                });
	                dialog.setSize(400, 200);
	            }
	            dialog.setVisible(true);
	        }
	    });
	    main.setSize(200, 200);
	    main.open();
	    while (!main.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    display.dispose();
	}
}
