/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Use transformation matrices to reflect, rotate and shear images
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet207 {	
	public static void main(String[] args) {
		final Display display = new Display();
		
		final Image image = new Image(display, 110, 60);
		GC gc = new GC(image);
		Font font = new Font(display, "Times", 30, SWT.BOLD);
		gc.setFont(font);
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, 110, 60);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawText("SWT", 10, 10, true);
		font.dispose();
		gc.dispose();
		
		final Rectangle rect = image.getBounds();
		Shell shell = new Shell(display);
		shell.setText("Matrix Tranformations");
		shell.setLayout(new FillLayout());
		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(new PaintListener () {
			public void paintControl(PaintEvent e) {	
				GC gc = e.gc;
				gc.setAdvanced(true);
				if (!gc.getAdvanced()){
					gc.drawText("Advanced graphics not supported", 30, 30, true);
					return;
				}
				
				// Original image
				int x = 30, y = 30;
				gc.drawImage(image, x, y); 
				x += rect.width + 30;
				
				Transform transform = new Transform(display);
				
				// Note that the tranform is applied to the whole GC therefore
				// the coordinates need to be adjusted too.
				
				// Reflect around the y axis.
				transform.setElements(-1, 0, 0, 1, 0 ,0);
				gc.setTransform(transform);
				gc.drawImage(image, -1*x-rect.width, y);
				
				x = 30; y += rect.height + 30;
				
				// Reflect around the x axis. 
				transform.setElements(1, 0, 0, -1, 0, 0);
				gc.setTransform(transform);
				gc.drawImage(image, x, -1*y-rect.height);
				
				x += rect.width + 30;
				
				// Reflect around the x and y axes	
				transform.setElements(-1, 0, 0, -1, 0, 0);
				gc.setTransform(transform);
				gc.drawImage(image, -1*x-rect.width, -1*y-rect.height);
				
				x = 30; y += rect.height + 30;
				
				// Shear in the x-direction
				transform.setElements(1, 0, -1, 1, 0, 0);
				gc.setTransform(transform);
				gc.drawImage(image, 300, y);
				
				// Shear in y-direction
				transform.setElements(1, -1, 0, 1, 0, 0);
				gc.setTransform(transform);
				gc.drawImage(image, 150, 475);
				
				// Rotate by 45 degrees	
				float cos45 = (float)Math.cos(Math.PI/4);
				float sin45 = (float)Math.sin(Math.PI/4);
				transform.setElements(cos45, sin45, -sin45, cos45, 0, 0);
				gc.setTransform(transform);
				gc.drawImage(image, 400, 60);
				
				transform.dispose();
			}
		});
		
		shell.setSize(350, 550);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}
	



