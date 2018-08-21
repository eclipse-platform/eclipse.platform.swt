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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug205199_LabelSetImageNull
{
	public static void main(String[] args)
	{
		new Bug205199_LabelSetImageNull();
	}

	private Image image;
	private Label label;
	private int state=0;

	public Bug205199_LabelSetImageNull()
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout( new FillLayout());

		// Create an graphics.image
		this.image = new Image (display, 16, 16);
		Color color = display.getSystemColor (SWT.COLOR_RED);
		GC gc = new GC (image);
		gc.setBackground (color);
		gc.fillRectangle (image.getBounds());
		gc.dispose ();

		this.label = new Label(shell,SWT.NONE);
		Button button = new Button(shell,SWT.PUSH);
		button.setText("click Me");
		button.addSelectionListener(
			new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					if (state == 0)
					{
						label.setImage(image);
						label.setText("1. widget.text");
					}
					else if (state == 1)
					{
						label.setText("2. widget.text");
						label.setImage(image);
					}
					else if (state == 2)
					{
						label.setText("3. widget.text");
						label.setImage(null);
					}
					state++;
				}
			}
		);

		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				 display.sleep();
	 }
}