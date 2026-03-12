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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: {@link GC#setClipping(Path)} doesn't respect a previous call to
 * {@link GC#setTransform(Transform)}.
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: The painted rectangle should be green.<br>
 * Actual results: The painted rectangle is red.
 *
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=531667">Bug
 *      531667 - [GTK3] Cannot draw Canvas with Control.print(GC) </a>
 */
public class Issue3120_GC_setClipping_is_wrong_after_transform {

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.setLayout(new FillLayout());
        shell.setSize(600, 300);

        Display display = shell.getDisplay();

        shell.addPaintListener(event -> {
        	GC gc = event.gc;

        	Path p = new Path(display);
        	p.addRectangle(50, 350, 100, 100);

        	Transform t = new Transform(display);
        	t.translate(0, -300);

        	gc.setTransform(t);

        	gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        	gc.fillPath(p);
        	gc.setClipping(p);
        	gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        	gc.fillPath(p);

        	p.dispose();

        	gc.setTransform(null);
        	t.dispose();
        });

        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}