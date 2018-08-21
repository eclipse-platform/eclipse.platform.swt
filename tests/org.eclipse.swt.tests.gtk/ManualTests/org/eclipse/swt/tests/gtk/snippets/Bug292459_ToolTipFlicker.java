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


import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug292459_ToolTipFlicker {

	public static void main(String[] args) {
	    final Display display = new Display();
	    Shell shell = new Shell(display, SWT.SHELL_TRIM);
	    shell.setLayout(new FillLayout());

	    Label test1 = new Label(shell, SWT.BORDER);
	    test1.setText("Test 1");
	    test1.setToolTipText("This is test1\nTest is this is\nwhee");

	    final Label test2 = new Label(shell, SWT.BORDER);
	    test2.setText("Test 2");
	    test2.setToolTipText("This is test2\nTest is this is\nwhee");

	    final CLabel test3 = new CLabel(shell, SWT.BORDER);
	    test3.setText("Test 3");
	    test3.setToolTipText("This is test3\nTest is this is\nwhee");

	    shell.open();

	    Timer timer = new Timer(true);
	    timer.schedule(new TimerTask() {
	      @Override
		public void run() {
	        display.asyncExec(new Runnable() {
	          @Override
			public void run() {
	            // make Label's widget.tooltip flicker by setting it to the same widget.text
	            test2.setToolTipText(test2.getToolTipText());

	            // make CLabel's widget.tooltip flicker just by invoking doPaint
	            test3.redraw();
	          }
	        });
	      }
	    }, 1000, 1000);

	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	  }
}
