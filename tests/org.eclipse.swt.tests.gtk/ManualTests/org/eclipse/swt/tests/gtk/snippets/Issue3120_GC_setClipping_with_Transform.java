/*******************************************************************************
 * Copyright (c) 2026 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link GC#setClipping(Path)} doesn't respect a previous call to
 * {@link GC#setTransform(Transform)}.
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: All painted rectangle should be green.<br>
 * Actual results: The rectangles are at least partially painted red.
 *
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=531667">Bug
 *      531667 - [GTK3] Cannot draw Canvas with Control.print(GC) </a>
 */
public class Issue3120_GC_setClipping_with_Transform {
	private static Display DISPLAY = Display.getDefault();
	private static Color COLOR_RED = DISPLAY.getSystemColor(SWT.COLOR_RED);
	private static Color COLOR_GREEN = DISPLAY.getSystemColor(SWT.COLOR_GREEN);

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.setLayout(new FillLayout());
		shell.setSize(800, 300);

		Composite composite1 = new Composite(shell, SWT.BORDER);
		composite1.addPaintListener(
				event -> paint(event.gc, p -> p.addRectangle(310, 310, 100, 100), t -> t.translate(-300, -300)));

		Composite composite2 = new Composite(shell, SWT.BORDER);
		composite2.addPaintListener(
				event -> paint(event.gc, p -> p.addRectangle(20, 5, 200, 50), t -> t.scale(0.5f, 2.0f)));

		Composite composite3 = new Composite(shell, SWT.BORDER);
		composite3.addPaintListener(
				event -> paint(event.gc, p -> p.addRectangle(10, 10, 100, 100), t -> t.shear(1.0f, 0.0f)));

		Composite composite4 = new Composite(shell, SWT.BORDER);
		composite4.addPaintListener(event -> paint(event.gc, p -> p.addRectangle(10, 605, 100, 50), t -> {
			t.scale(1.0f, 2.0f);
			t.translate(0, -600);
		}));

		Composite composite5 = new Composite(shell, SWT.BORDER);
		composite5.addPaintListener(event -> paint(event.gc, p -> p.addRectangle(10, 10, 100, 100), t -> {}, gc -> {
			Path p = new Path(DISPLAY);
			p.addRectangle(55, 55, 10, 10);
			gc.setClipping(p);
			p.dispose();
		}));

		Composite composite6 = new Composite(shell, SWT.BORDER);
		composite6.addPaintListener(event -> paint(event.gc, p -> p.addRectangle(10, 275, 100, 50), t -> {
			t.scale(1.0f, 2.0f);
			t.translate(0, -270);
		}, gc -> {
			gc.setClipping(10, 10, 100, 100);
		}));

		shell.open();

        while(!shell.isDisposed()) {
			if (!DISPLAY.readAndDispatch()) {
				DISPLAY.sleep();
            }
        }
    }

	private static void paint(GC gc, Consumer<Path> c1, Consumer<Transform> c2) {
		paint(gc, c1, c2, ignore -> {
		});
	}

	private static void paint(GC gc, Consumer<Path> c1, Consumer<Transform> c2, Consumer<GC> c3) {
		Path p = new Path(DISPLAY);
		c1.accept(p);

		Transform t = new Transform(DISPLAY);
		c2.accept(t);

		gc.setTransform(t);
		t.dispose();

		gc.setBackground(COLOR_RED);
		gc.fillPath(p);
		c3.accept(gc);
		gc.setClipping(p);
		gc.setBackground(COLOR_GREEN);
		gc.fillPath(p);

		p.dispose();
	}
}