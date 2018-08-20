/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Draw using transformations, paths and alpha blending
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet10 {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Advanced Graphics");
		FontData fd = shell.getFont().getFontData()[0];
		final Font font = new Font(display, fd.getName(), 60, SWT.BOLD | SWT.ITALIC);
		final Image image = new Image(display, 640, 480);
		final Rectangle rect = image.getBounds();
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillOval(rect.x, rect.y, rect.width, rect.height);
		gc.dispose();
		shell.addListener(SWT.Paint, event -> {
			GC gc1 = event.gc;
			Transform tr = new Transform(display);
			tr.translate(50, 120);
			tr.rotate(-30);
			gc1.drawImage(image, 0, 0, rect.width, rect.height, 0, 0, rect.width / 2, rect.height / 2);
			gc1.setAlpha(100);
			gc1.setTransform(tr);
			Path path = new Path(display);
			path.addString("SWT", 0, 0, font);
			gc1.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc1.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			gc1.fillPath(path);
			gc1.drawPath(path);
			tr.dispose();
			path.dispose();
		});
		shell.setSize(shell.computeSize(rect.width / 2, rect.height / 2));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		font.dispose();
		display.dispose();
	}
}
