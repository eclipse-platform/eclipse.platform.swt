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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Bug251354_TabItemToolTip {
  public void run() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new GridLayout());
    shell.setText("Show ToolTips");
    shell.setToolTipText("Shell");
    create(shell);
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
  private void create(Shell shell) {
    final TabFolder tabFolder1 = new TabFolder(shell, SWT.NONE);
    tabFolder1.setToolTipText("Tabfolder1");
    Text text = new Text(tabFolder1, SWT.BORDER);  
    text.setToolTipText("Text");
    for (int i = 0; i < 4; i++) 
    {
            TabItem item = new TabItem(tabFolder1, SWT.CLOSE);
            item.setText("Item "+i);
            item.setControl(text);
            item.setToolTipText("Item "+i);
    }

    final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
    TabItem one = new TabItem(tabFolder, SWT.NONE);
    one.setText("Tab 1");
//    one.setToolTipText("This is Tab 1.");
   
    TabItem two = new TabItem(tabFolder, SWT.NONE);
    two.setText("Tab 2");
//    two.setToolTipText("This is Tab 2.");
   
    TabItem three = new TabItem(tabFolder, SWT.NONE);
    three.setText("Tab 3");
    three.setToolTipText("This is Tab 3.");
   
    TabItem four = new TabItem(tabFolder, SWT.NONE);
    four.setText("Tab 4");
    four.setToolTipText("This is Tab 4.");
    
  }
  public static void main(String[] args) {
    new Bug251354_TabItemToolTip().run();
  }
}