/*******************************************************************************
 * Copyright (c) 2019 Olivier Prouvost and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Olivier Prouvost - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.awt.Frame;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class Bug550517_SwingComponentFocus {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 109");
		shell.setSize(600, 250);
		shell.setLayout(new GridLayout(1, false));


		Text txtInput = new Text(shell, SWT.BORDER);
		txtInput.setText("This is a text using swt");
		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create the swing widgets in a group
		Group group = new Group(shell, SWT.BORDER);
		group.setText("Swing widgets");
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		Composite composite = new Composite(group, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		Frame frame = SWT_AWT.new_Frame(composite);

		JPanel p = new JPanel();

		JTextField tf = new JTextField();
		tf.setText("Text in a swing component keeps the focus on Linux");
		p.add(tf);

		JComboBox<String> cb = new JComboBox<>(new String[] { "choice 1", "choice 2", "choice 3"});
		cb.setEditable(true);

		p.add(cb);

		frame.add(p);
		frame.pack();


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


}