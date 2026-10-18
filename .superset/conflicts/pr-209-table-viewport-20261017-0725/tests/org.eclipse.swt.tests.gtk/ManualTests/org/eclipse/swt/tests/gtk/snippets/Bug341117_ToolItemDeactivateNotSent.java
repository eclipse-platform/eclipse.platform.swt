/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;



public class Bug341117_ToolItemDeactivateNotSent {

  public static void main(final String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    shell.setLayout(gridLayout);
    shell.setSize(100, 140);
    ToolBar toolBar = new ToolBar(shell, SWT.HORIZONTAL | SWT.FLAT);
    toolBar.addListener(SWT.Deactivate, event -> System.out.println("Toolbar deactivated"));
    toolBar.addListener(SWT.Activate, event -> System.out.println("Toolbar activated"));
    ToolItem button = new ToolItem(toolBar, SWT.PUSH);
    button.setText("A button");
    addText("Text 1", shell);
    addText("Text 2", shell);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private static void addText(final String textString, final Shell shell) {
    Text text = new Text(shell, SWT.NONE);
    text.setText(textString);
    text.addListener(SWT.Deactivate, event -> System.out.println(textString + " deactivated"));
    text.addListener(SWT.Activate, event -> System.out.println(textString + " activated"));
    text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
  }

}
