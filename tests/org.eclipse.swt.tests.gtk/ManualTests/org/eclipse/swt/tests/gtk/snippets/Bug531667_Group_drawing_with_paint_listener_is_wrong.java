/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
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
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: Drawing an image with a paint listener of a {@link Group} yields wrong results,
 *              namely the drawing is done in the group area and not in client area of the group.
 *              This causes problems e.g. with JFace ControlDecoration.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: The shell contains a group, with a square fully within the group area.
 * Actual results: The shell contains a group, with a square painted partially outside of the group area.
 */
public class Bug531667_Group_drawing_with_paint_listener_is_wrong {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(200, 200);
		shell.setText("Bug 531667 paint inside a Group is wrong");

		final Group group = new Group(shell, SWT.NONE);
		group.setText("some group");
		group.setLayout(new FillLayout());

		final Image image = new Image(display, 40, 40);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
		gc.fillRectangle(0,  0, 40, 40);
		gc.dispose();

		class DrawSquare implements PaintListener {
			@Override
			public void paintControl(PaintEvent e) {
			    GC gc = e.gc;
			    gc.drawImage(image, 0, 0);
			}
		}
		group.addPaintListener(new DrawSquare());

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
