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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug299045_TableLines {

    public static void main(String[] args) {
        final Display display = new Display();
        final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
        Shell shell = new Shell(display);
        shell.setText("Images on the right side of the TableItem");
        shell.setLayout(new FillLayout());
        Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(false);  // changing this to true makes the dotted lines thicker
        int columnCount = 3;
        for(int i = 0; i < columnCount; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText("Column " + i);
        }
        final int itemCount = 10;
        for(int i = 0; i < itemCount; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] {"item " + i + " a", "item " + i + " Bug510183_javadocHang", "item " + i + " c"});
        }
        Listener paintListener = new Listener() {
            @Override
			public void handleEvent(Event event) {
                switch(event.type) {
                case SWT.MeasureItem: {
                    Rectangle rect = image.getBounds();
                    event.width += rect.width;
                    event.height = Math.max(event.height, rect.height + 2);
                    break;
                }
                case SWT.EraseItem: {
                    event.detail &= ~SWT.FOREGROUND;
                    event.detail &= ~SWT.BACKGROUND;
                    event.detail &= ~SWT.SELECTED;
                    event.detail &= ~SWT.FOCUSED;
                    event.detail &= ~SWT.HOT;
                    event.doit = false;
                    break;
                }
                case SWT.PaintItem: {
                    Rectangle rect = image.getBounds();
                    
                 // shouldn't we subtract rect.width?
                 // it looks like the event.width value set in the SWT.MeasureItem wasn't retained... 
                    int x = event.x + event.width /*- rect.width*/;
                    int offset = Math.max(0, (event.height - rect.height) / 2);
                    event.gc.drawImage(image, x, event.y + offset);
                    
                    // draws only a blue background filling up the entire cell
//                    Color blue = display.getSystemColor(SWT.COLOR_BLUE);
//                    event.gc.setBackground(blue);
//                    int adhocOffset = 2;
//                    event.gc.fillRectangle(event.x - adhocOffset, event.y,
//                            event.width + rect.width + adhocOffset, event.height);
                    
                    break;
                }
                }
            }
        };
        table.addListener(SWT.MeasureItem, paintListener);
        table.addListener(SWT.EraseItem, paintListener);
        table.addListener(SWT.PaintItem, paintListener);

        for(int i = 0; i < columnCount; i++) {
            table.getColumn(i).pack();
        }
        shell.setSize(500, 500);
        shell.open();
        while(!shell.isDisposed()) {
            if(!display.readAndDispatch())
                display.sleep();
        }
        
        // this is incorrect, the graphics.image wasn't allocated by the user!
//        if(graphics.image != null)
//            graphics.image.dispose();
        
        display.dispose();
    }
}