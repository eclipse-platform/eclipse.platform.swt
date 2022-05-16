/*******************************************************************************
 * Copyright (c) 2022 Phil Beauvoir and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Phil Beauvoir - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Issue0146_CopyImageToClipBoard {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        shell.setSize(400, 300);

        int size = 200;

        // Create a simple image
        Image image = new Image(display, size, size);
        GC gc = new GC(image);
        gc.setBackground(new Color(128, 255, 128));
        gc.setForeground(new Color(0, 0, 0));
        gc.fillRectangle(0, 0, size - 1, size - 1);
        gc.drawRectangle(0, 0, size - 1, size - 1);
        gc.drawText("Hello World", 10, 10);
        gc.dispose();

        // Press the button to copy the image data to the clipboard
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Copy to Clipboard");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Clipboard cb = new Clipboard(display);
                cb.setContents(new Object[] { image.getImageData() }, new Transfer[] { ImageTransfer.getInstance() });
                cb.dispose();
            }
        });

        // This is the image we are copying
        shell.addListener(SWT.Paint, event -> {
            event.gc.drawImage(image, 150, 10);
        });

        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) display.sleep();
        }

        image.dispose();
        display.dispose();
    }
}