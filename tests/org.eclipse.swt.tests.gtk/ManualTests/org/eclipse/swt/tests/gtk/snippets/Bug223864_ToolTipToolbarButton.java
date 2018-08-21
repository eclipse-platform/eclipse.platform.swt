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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug223864_ToolTipToolbarButton {

    public static void main(String[] args) {
        Display display= new Display();
        Shell parent= new Shell(display);
        parent.setText("Parent");
        
        Shell child= new Shell(parent, SWT.ON_TOP | SWT.SHELL_TRIM);
        GridLayout gridLayout= new GridLayout(1, false);
        child.setLayout(gridLayout);
        ToolBar bar= new ToolBar(child, SWT.NONE);
        ToolItem button= new ToolItem(bar, SWT.PUSH);
        button.setImage(display.getSystemImage(SWT.ICON_QUESTION));
        button.setToolTipText("Do you see me?");

        parent.setBounds(100, 100, 160, 100);
        child.setBounds(150, 150, 160, 100);
        
        parent.open();
        
        child.setVisible(true);
        child.setActive(); // comment this out and widget.tooltip appears...
        parent.setActive();
        
        while (!parent.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

}
