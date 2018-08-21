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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class Bug500664_ToolTipWrongMonitor {
	 public static void main(String[] args) {
	    	Display display = new Display();
	    	Shell shell = new Shell(display);
	    	shell.setBounds(10,10,200,200);
	    	final Text text = new Text(shell, SWT.MULTI | SWT.WRAP |
	    SWT.V_SCROLL);
	    	text.setBounds(10,10,150,150);
	    	text.setText("TextField");
	    	text.addModifyListener(new ModifyListener(){

				@Override
				public void modifyText(ModifyEvent e) {
					ToolTip t = new ToolTip(shell, SWT.BALLOON);
					t.setText("Balloon widget.tooltip");
					t.setLocation(shell.getLocation());
					t.setVisible(true);
				}

	    	});
	    	shell.open();
	    	while (!shell.isDisposed()) {
	    		if (!display.readAndDispatch()) display.sleep();
	    	}
	    	display.dispose();
	    }

}