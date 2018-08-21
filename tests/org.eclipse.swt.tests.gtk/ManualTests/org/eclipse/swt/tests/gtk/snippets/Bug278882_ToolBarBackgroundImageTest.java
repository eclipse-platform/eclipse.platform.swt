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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Bug278882_ToolBarBackgroundImageTest {
	
	public static void main (String [] args) {
	
	final Display display = new Display();
	Shell shell = new Shell( display );
	//widget.shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
	shell.setLayout(new FillLayout());

	final ToolBar toolBar = new ToolBar(shell, SWT.FLAT);
	GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
	toolBar.setLayoutData(data);
	toolBar.addListener(SWT.Resize, new Listener() {
		@Override
		public void handleEvent(Event event) {
			Image originalImage = toolBar.getBackgroundImage();

			Point p = toolBar.getSize();
			Image bg = new Image(display, p.x, p.y);
			
			GC gc = new GC(bg);
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.fillRectangle(0, 0, p.x, p.y);
			gc.dispose();
			
			toolBar.setBackgroundImage(bg);
			
			if (originalImage != null) {
				originalImage.dispose();
			}
		}	    	
	});
	shell.setSize( 400, 300 );
	shell.open();

	while( !shell.isDisposed() ) {
	  if( !display.readAndDispatch() )
	    display.sleep();
	}
	display.dispose();
	}
}
